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
package com.clougence.clouddm.console.web.service.sdk;

import static com.clougence.clouddm.sdk.openapi.OpenApiErrorCode.*;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_USER_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_USER_READ;
import static com.clougence.clouddm.sdk.security.auth.def.SecSysRole.ADMIN_ROLE_NAME;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.clougence.clouddm.console.web.model.fo.role.UpdateUserRoleFO;
import com.clougence.clouddm.console.web.model.fo.user.AccountAbilityFO;
import com.clougence.clouddm.console.web.model.fo.user.AddSubAccountFO;
import com.clougence.clouddm.console.web.model.fo.user.UpdateSubAccountFO;
import com.clougence.clouddm.console.web.service.auth.RdpRoleService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.AccountBindType;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.OpenApiException;
import com.clougence.clouddm.sdk.openapi.account.OpenApiAccountService;
import com.clougence.clouddm.sdk.openapi.account.OpenApiAssignableRole;
import com.clougence.clouddm.sdk.openapi.account.OpenApiSubAccount;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ConsoleOpenApiAccountService implements OpenApiAccountService {

    @Resource
    private AuthDal           authDal;
    @Resource
    private RdpUserService    userService;
    @Resource
    private RdpRoleService    roleService;
    @Resource
    private RdpOpAuditService auditService;

    @Override
    public List<OpenApiSubAccount> listSubAccounts(OpenApiCaller caller, String prefix, Long roleId, Boolean enabled) {
        this.requireRole(caller, RDP_USER_READ);
        Set<Long> localRoles = this.roleService.listRoleByUID(caller.puid())
            .stream()
            .filter(role -> !isAdministratorRole(role))
            .map(DmAuthRoleDO::getId)
            .collect(Collectors.toSet());
        return this.userService.listSubAccounts(caller.puid())
            .stream()
            .filter(ConsoleOpenApiAccountService::isLocalAccount)
            .filter(user -> localRoles.contains(user.getRoleId()))
            .filter(user -> StringUtils.isBlank(prefix) || startsWith(user.getUsername(), prefix) || startsWith(user.getAccount(), prefix))
            .filter(user -> roleId == null || roleId.equals(user.getRoleId()))
            .filter(user -> enabled == null || enabled != user.isDisable())
            .sorted(Comparator.comparing(DmAuthUserDO::getId))
            .map(ConsoleOpenApiAccountService::toAccount)
            .toList();
    }

    @Override
    public OpenApiSubAccount getSubAccount(OpenApiCaller caller, String uid) {
        this.requireRole(caller, RDP_USER_READ);
        DmAuthUserDO target = this.userService.getUserByUid(uid);
        this.requireLocalTarget(this.userService.getUserByUid(caller.puid()), target, uid);
        DmAuthRoleDO role = this.findRole(target);
        if (role == null || !caller.puid().equals(role.getOwnerUid()) || isAdministratorRole(role)) {
            throw new OpenApiException(NOT_FOUND);
        }
        return toAccount(target);
    }

    @Override
    public List<OpenApiAssignableRole> listAssignableRoles(OpenApiCaller caller) {
        DmAuthRoleDO operator = this.requireRole(caller, RDP_USER_MANAGE);
        return this.roleService.listRoleByUID(caller.puid())
            .stream()
            .filter(role -> canAssignRole(operator, role))
            .sorted(Comparator.comparing(DmAuthRoleDO::getId))
            .map(role -> new OpenApiAssignableRole(role.getId(), role.getRoleName()))
            .toList();
    }

    @Override
    public Supplier<OpenApiSubAccount> prepareCreateSubAccount(OpenApiCaller caller, String account, String userName, long roleId, String password) {
        DmAuthRoleDO operator = this.requireRole(caller, RDP_USER_MANAGE);
        this.requireAssignableRole(operator, roleId);

        AddSubAccountFO form = new AddSubAccountFO();
        form.setAccount(account);
        form.setUserName(userName);
        form.setRoleId(roleId);
        form.setPassword(password);
        return () -> {
            if (!this.userService.validateSubAccountPwd(caller.puid(), password).isSuccess()) {
                throw new OpenApiException(INVALID_ARGUMENT);
            }
            var result = this.userService.addSubAccountForInternal(caller.puid(), form);
            if (!result.isSuccess()) {
                throw new OpenApiException(STATE_CONFLICT);
            }
            DmAuthUserDO created = this.userService.getUserByUid(result.getSubUid());
            this.audit(caller, created.getUid(), AuditType.ADD_SUB_ACCOUNT);
            return toAccount(created);
        };
    }

    @Override
    public Runnable prepareUpdateSubAccount(OpenApiCaller caller, String uid, String userName, String email, String phone) {
        DmAuthRoleDO operator = this.requireRole(caller, RDP_USER_MANAGE);
        // Match the Console lock order for contact updates.
        DmAuthUserDO primary;
        if (email != null || phone != null) {
            primary = this.authDal.userMapper().lockByUid(caller.puid());
        } else {
            primary = this.userService.getUserByUid(caller.puid());
        }
        this.lockManagedTarget(operator, primary, uid);

        UpdateSubAccountFO form = new UpdateSubAccountFO();
        form.setTargetUid(uid);
        form.setUserName(userName);
        form.setEmail(email);
        form.setPhone(phone);
        return () -> {
            if (!this.userService.updateSubAccount(form, caller.puid()).isSuccess()) {
                throw new OpenApiException(STATE_CONFLICT);
            }
            this.audit(caller, uid, AuditType.UPDATE_SUB_ACCOUNT);
        };
    }

    @Override
    public Runnable prepareSetSubAccountRole(OpenApiCaller caller, String uid, long roleId) {
        DmAuthRoleDO operator = this.requireOtherAccount(caller, uid);
        this.lockManagedTarget(operator, this.userService.getUserByUid(caller.puid()), uid);
        this.requireAssignableRole(operator, roleId);

        UpdateUserRoleFO form = new UpdateUserRoleFO();
        form.setSubAccountUid(uid);
        form.setRoleId(roleId);
        return () -> {
            this.userService.updateUserRole(form);
            this.audit(caller, uid, AuditType.UPDATE_SUB_ACCOUNT_ROLE);
        };
    }

    @Override
    public Runnable prepareSetSubAccountEnabled(OpenApiCaller caller, String uid, boolean enabled) {
        DmAuthRoleDO operator = this.requireOtherAccount(caller, uid);
        this.lockManagedTarget(operator, this.userService.getUserByUid(caller.puid()), uid);

        AccountAbilityFO form = new AccountAbilityFO();
        form.setUid(uid);
        form.setDisable(!enabled);
        return () -> {
            if (!this.userService.updateAccountAbility(caller.puid(), form).isSuccess()) {
                throw new OpenApiException(STATE_CONFLICT);
            }
            this.audit(caller, uid, enabled ? AuditType.ENABLE_SUB_ACCOUNT : AuditType.DISABLE_SUB_ACCOUNT);
        };
    }

    private DmAuthRoleDO requireRole(OpenApiCaller caller, String label) {
        DmAuthUserDO user = this.userService.getUserByUid(caller.uid());
        if (user == null || user.isDisable()) {
            throw new OpenApiException(FORBIDDEN);
        }
        DmAuthRoleDO role = this.findRole(user);
        if (role == null || !caller.puid().equals(role.getOwnerUid()) || role.getRoleAuthLabels() == null || !role.getRoleAuthLabels().contains(label)) {
            throw new OpenApiException(FORBIDDEN);
        }
        return role;
    }

    private DmAuthRoleDO requireOtherAccount(OpenApiCaller caller, String uid) {
        DmAuthRoleDO role = this.requireRole(caller, RDP_USER_MANAGE);
        if (caller.uid().equals(uid)) {
            throw new OpenApiException(FORBIDDEN);
        }
        return role;
    }

    private void lockManagedTarget(DmAuthRoleDO operator, DmAuthUserDO primary, String uid) {
        DmAuthUserDO target = this.authDal.userMapper().lockByUid(uid);
        this.requireLocalTarget(primary, target, uid);
        if (!canAssignRole(operator, this.findRole(target))) {
            throw new OpenApiException(FORBIDDEN);
        }
    }

    private void requireLocalTarget(DmAuthUserDO primary, DmAuthUserDO target, String uid) {
        if (primary == null || target == null || !uid.equals(target.getUid()) || !Objects.equals(primary.getId(), target.getParentId()) || !isLocalAccount(target)) {
            throw new OpenApiException(NOT_FOUND);
        }
    }

    private void requireAssignableRole(DmAuthRoleDO operator, long roleId) {
        DmAuthRoleDO role = this.roleService.fetchRoleById(roleId);
        if (role == null || !operator.getOwnerUid().equals(role.getOwnerUid())) {
            throw new OpenApiException(NOT_FOUND);
        }
        if (!canAssignRole(operator, role)) {
            throw new OpenApiException(FORBIDDEN);
        }
    }

    private DmAuthRoleDO findRole(DmAuthUserDO user) {
        if (user.getRoleId() == null) {
            return null;
        }
        return this.roleService.fetchRoleById(user.getRoleId());
    }

    private static boolean canAssignRole(DmAuthRoleDO operator, DmAuthRoleDO role) {
        return role != null && operator.getOwnerUid().equals(role.getOwnerUid()) && !isAdministratorRole(role)
               && RdpConvertUtils.removeSelectedRoleAuthLabelsMeta(operator.getRoleAuthLabels())
                   .containsAll(RdpConvertUtils.removeSelectedRoleAuthLabelsMeta(role.getRoleAuthLabels()));
    }

    private static boolean isAdministratorRole(DmAuthRoleDO role) {
        return role != null && role.isInnerTag() && ADMIN_ROLE_NAME.equals(role.getRoleName());
    }

    private static boolean isLocalAccount(DmAuthUserDO user) {
        return user.getAccountType() == AccountType.SUB_ACCOUNT && user.getBindType() == AccountBindType.INTERNAL && !user.isMaintainer();
    }

    private static boolean startsWith(String value, String prefix) {
        return value != null && value.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    private static OpenApiSubAccount toAccount(DmAuthUserDO user) {
        return new OpenApiSubAccount(user.getUid(),
            user.getAccount(),
            user.getUsername(),
            user.getRoleId(),
            !user.isDisable(),
            StringUtils.trimToNull(user.getEmail()),
            StringUtils.trimToNull(user.getPhone()));
    }

    private void audit(OpenApiCaller caller, String uid, AuditType type) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        this.auditService
            .logAndAddOperationAudit(caller.puid(), caller.uid(), request.getRequestURI(), request.getRemoteAddr(), uid, null, SecurityLevel.HIGH, type, ResourceType.ACCOUNT);
    }
}
