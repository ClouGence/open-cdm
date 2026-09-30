/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.rdp.service.model.AddSubAccountMO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Login with the identity forwarded by an upstream connect gateway.
 * <p>
 * The gateway authenticates the user and forwards the request with a {@code X-Connect-Token} header,
 * a JWT signed by the shared secret key. The user is mapped to an internal account (an existing
 * account is used, otherwise a sub account is created) and permissions decide the role.
 */
@Slf4j
@Service
public class ConnectGatewayLoginService {

    private static final String TOKEN_HEADER      = "X-Connect-Token";
    private static final String CLAIM_ID          = "id";
    private static final String CLAIM_USERNAME    = "username";
    private static final String CLAIM_NICKNAME    = "nickname";
    private static final String CLAIM_EMAIL       = "email";
    private static final String CLAIM_PERMISSIONS = "permissions";

    @Resource
    private ConsoleConfig       config;
    @Resource
    private AuthDal             authDal;
    @Resource
    private NamingDao           namingDao;
    @Resource
    private JwtService          jwtService;
    @Resource
    private RdpUserService      rdpUserService;
    private final Map<String, Object> createLocks = new ConcurrentHashMap<>();

    /**
     * Login with the gateway identity carried by the request.
     *
     * @return the console session token, null when the request carries no gateway identity.
     */
    public DecodedJWT login(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(this.config.getConnectSecretKey())) {
            return null;
        }

        String connectToken = request.getHeader(TOKEN_HEADER);
        if (StringUtils.isBlank(connectToken)) {
            return null;
        }

        DecodedJWT connectJwt = this.verifyConnectToken(connectToken);
        if (connectJwt == null) {
            return null;
        }

        String connectAccount = StringUtils.defaultIfBlank(connectJwt.getClaim(CLAIM_USERNAME).asString(), connectJwt.getClaim(CLAIM_ID).asString());
        if (StringUtils.isBlank(connectAccount)) {
            log.error("connectGateway token without username and id.");
            return null;
        }

        List<String> permissions = this.resolvePermissions(connectJwt);
        this.checkAllowPermissions(connectAccount, permissions);
        DmAuthUserDO loginUser = this.resolveLoginUser(connectAccount, connectJwt, permissions);
        if (loginUser.isDisable()) {
            log.warn("connectGateway user({}) is disabled.", connectAccount);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_ACCOUNT_UNAVAILABLE.name()));
        }

        int cookieAge = Math.max(JwtService.minLoginExpireSec, this.config.getLoginExpireTimeSec());
        String sessionToken = this.jwtService.genJwtToken(loginUser);
        response.addCookie(RdpWebUtils.newCookie(JwtService.jwtTokenName, sessionToken, false, cookieAge));
        log.info("connectGateway login: account={}, uid={}", connectAccount, loginUser.getUid());

        return this.jwtService.verifyJwtToken(sessionToken);
    }

    private DecodedJWT verifyConnectToken(String connectToken) {
        try {
            return JWT.require(Algorithm.HMAC256(this.config.getConnectSecretKey())).build().verify(connectToken);
        } catch (JWTVerificationException e) {
            log.warn("connectGateway token verify failed, {}", e.getMessage());
            return null;
        }
    }

    private List<String> resolvePermissions(DecodedJWT connectJwt) {
        List<String> permissions = connectJwt.getClaim(CLAIM_PERMISSIONS).asList(String.class);
        return permissions == null ? Collections.emptyList() : permissions;
    }

    private void checkAllowPermissions(String connectAccount, List<String> permissions) {
        List<String> allowPermissions = this.configPermissions(this.config.getConnectAllowPermissions());
        if (allowPermissions.isEmpty()) {
            return;
        }

        for (String allowPermission : allowPermissions) {
            for (String permission : permissions) {
                if (StringUtils.equalsIgnoreCase(permission, allowPermission)) {
                    return;
                }
            }
        }

        log.warn("connectGateway user({}) denied, permissions({}) not in allow permissions({}).", connectAccount, permissions, allowPermissions);
        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_CONNECT_PERMISSION_DENIED.name(), connectAccount));
    }

    private DmAuthUserDO resolveLoginUser(String connectAccount, DecodedJWT connectJwt, List<String> permissions) {
        DmAuthUserDO rootUser = this.authDal.queryRootUser();
        if (rootUser == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_NOT_EXIST.name()));
        }
        if (rootUser.isDisable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_FAIL_PRIMARY_ACCOUNT_DISABLED.name()));
        }

        // 1. an account already bound to this gateway identity.
        DmAuthUserDO bindUser = this.queryBindUser(connectAccount, connectJwt);
        if (bindUser != null) {
            this.syncConnectRole(rootUser, bindUser, permissions);
            return this.rdpUserService.getUserByUid(bindUser.getUid());
        }

        // 2. an internal account with the same email, use it as it is, its role is managed by the console.
        DmAuthUserDO matchUser = this.queryMatchUser(rootUser, connectJwt.getClaim(CLAIM_EMAIL).asString());
        if (matchUser != null) {
            log.info("connectGateway user({}) matched internal account({}).", connectAccount, matchUser.getUid());
            return matchUser;
        }

        // 3. first login, create a sub account for the gateway identity.
        return this.createConnectUser(rootUser, connectAccount, connectJwt, permissions);
    }

    private DmAuthUserDO queryBindUser(String connectAccount, DecodedJWT connectJwt) {
        String unionId = connectJwt.getClaim(CLAIM_ID).asString();
        if (StringUtils.isNotBlank(unionId)) {
            DmAuthUserDO user = this.authDal.userMapper().queryByUnionInfo(unionId, AccountBindType.Connect);
            if (user != null) {
                return user;
            }
        }
        return this.authDal.userMapper().queryByBindInfo(connectAccount, AccountBindType.Connect);
    }

    private DmAuthUserDO queryMatchUser(DmAuthUserDO rootUser, String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }

        DmAuthUserDO primaryUser = this.authDal.userMapper().queryPrimaryByEmail(email);
        if (primaryUser != null) {
            return primaryUser;
        }
        return this.authDal.userMapper().queryByEmailAndParentId(email, rootUser.getId());
    }

    private DmAuthUserDO createConnectUser(DmAuthUserDO rootUser, String connectAccount, DecodedJWT connectJwt, List<String> permissions) {
        // first page load sends parallel requests, only one of them may create the account of an identity.
        Object lock = this.createLocks.computeIfAbsent(connectAccount, key -> new Object());
        synchronized (lock) {
            DmAuthUserDO created = this.queryBindUser(connectAccount, connectJwt);
            if (created != null) {
                return created;
            }

            DmAuthUserDO bindUser = new DmAuthUserDO();
            bindUser.setUsername(StringUtils.defaultIfBlank(connectJwt.getClaim(CLAIM_NICKNAME).asString(), connectAccount));
            bindUser.setAccount(this.namingDao.genLoginAccount());
            bindUser.setEmail(connectJwt.getClaim(CLAIM_EMAIL).asString());
            bindUser.setUnionId(connectJwt.getClaim(CLAIM_ID).asString());
            bindUser.setBindAccount(connectAccount);
            bindUser.setRoleId(this.resolveRoleId(rootUser, permissions));

            AddSubAccountMO accountMO = this.rdpUserService.addSubAccountForBind(rootUser.getUid(), AccountBindType.Connect, bindUser);
            if (!accountMO.isSuccess()) {
                log.error("connectGateway create sub account for user({}) failed, {}", connectAccount, accountMO.getErrorMsg());
                throw new ErrorMessageException(accountMO.getErrorMsg());
            }

            log.info("connectGateway user({}) created sub account({}).", connectAccount, accountMO.getSubUid());
            return this.rdpUserService.getUserByUid(accountMO.getSubUid());
        }
    }

    /** Only roles of gateway bound accounts follow gateway permissions, a role assigned by the console is kept. */
    private void syncConnectRole(DmAuthUserDO rootUser, DmAuthUserDO bindUser, List<String> permissions) {
        if (bindUser.getBindType() != AccountBindType.Connect) {
            return;
        }

        Long roleId = this.resolveRoleId(rootUser, permissions);
        if (!roleId.equals(bindUser.getRoleId())) {
            log.info("connectGateway user({}) role changed: {} -> {}", bindUser.getUid(), bindUser.getRoleId(), roleId);
            this.authDal.userMapper().updateRoleById(bindUser.getId(), roleId);
        }
    }

    private Long resolveRoleId(DmAuthUserDO rootUser, List<String> permissions) {
        boolean admin = this.isAdmin(permissions);
        DmAuthRoleDO role = this.findRole(rootUser.getUid(), admin ? this.config.getConnectAdminRole() : this.config.getConnectDefaultRole());
        if (role == null && admin) {
            log.warn("connectGateway admin role({}) not exist, fallback to role({}).", this.config.getConnectAdminRole(), this.config.getConnectDefaultRole());
            role = this.findRole(rootUser.getUid(), this.config.getConnectDefaultRole());
        }
        if (role == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_ROLE_NOT_EXIST_ERROR.name()));
        }
        return role.getId();
    }

    private DmAuthRoleDO findRole(String puid, String roleName) {
        List<DmAuthRoleDO> roles = this.authDal.roleMapper().queryByRoleName(puid, roleName);
        return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
    }

    private boolean isAdmin(List<String> permissions) {
        for (String adminPermission : this.configPermissions(this.config.getConnectAdminPermissions())) {
            for (String permission : permissions) {
                if (StringUtils.equalsIgnoreCase(permission, adminPermission)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> configPermissions(String configValue) {
        List<String> permissions = new ArrayList<>();
        for (String permission : StringUtils.split(configValue, ",")) {
            if (StringUtils.isNotBlank(permission)) {
                permissions.add(permission.trim());
            }
        }
        return permissions;
    }
}
