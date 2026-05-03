package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import org.springframework.ldap.core.AttributesMapper;

import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public abstract class BaseLoginProviderSpi implements LoginProviderSpi {

    protected final ConsoleConfigService configService;

    public BaseLoginProviderSpi(ConsoleConfigService configService){
        this.configService = configService;
    }

    @Override
    public String loginJumpUrl(String primaryUID, String status, String jumpUrl) {
        return "";
    }

    @Override
    public String logoutJumpUrl(String primaryUID, String status, String jumpUrl, String accessToken) {
        return "";
    }

    @Override
    public LoginResponse authLogin(String primaryUID, LoginRequest request) {
        // create ctx
        BaseCtx ldapCtx;
        try {
            ldapCtx = this.loginCtx(primaryUID, request.getLoginAccount());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof ThirdPartyApiException) {
                throw e;
            } else {
                throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
            }
        }

        // auth ldap
        String ldapAccount = request.getLoginAccount();
        String ldapPassword = request.getLoginPassword();
        BaseSearch ldapSearch = this.buildQuery(ldapCtx, ldapAccount);
        UserData primaryUser = this.configService.findUserByUID(primaryUID);
        UserData ldapUser = this.queryUser(ldapCtx, primaryUser, request.getLoginAccount());
        if (ldapUser == null) {
            return new LoginResponse(null, true);
        }
        try {
            ldapCtx.getLdapTemplate().authenticate(ldapSearch.getLdapQuery(), ldapPassword, (ctx, ei) -> null);
            return new LoginResponse(ldapUser, true);
        } catch (Exception e) {
            this.checkThrowError(e);
            if (e instanceof ThirdPartyApiException) {
                throw e;
            } else {
                throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
            }
        }
    }

    protected UserData queryUser(BaseCtx ldapCtx, UserData primaryUser, String ldapAccount) {
        // query user
        List<UserData> searchResult;
        BaseSearch ldapSearch = this.buildQuery(ldapCtx, ldapAccount);
        try {
            searchResult = ldapCtx.getLdapTemplate().search(ldapSearch.getLdapQuery(), (AttributesMapper<UserData>) attributes -> {
                try {
                    return this.mapUser(ldapCtx, primaryUser, ldapAccount, attributes);
                } catch (ThirdPartyApiException e) {
                    throw e;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }).stream().filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof ThirdPartyApiException) {
                throw e;
            } else {
                throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
            }
        }

        // result
        if (searchResult.isEmpty()) {
            return null;
        } else if (searchResult.size() > 1) {
            throw ThirdPartyApiException.asRDP()
                .with(LdapI18nKey.LDAP_MATCH_MULTIPLE_ACCOUNT.name(), ldapSearch.getLdapWhere(), ldapSearch.getLdapCondition(), searchResult.size());
        } else {
            return searchResult.get(0);
        }
    }

    protected String getAttribute(Attributes attr, String filed) throws NamingException {
        Attribute attribute = attr.get(filed);
        if (attribute != null) {
            Object o = attribute.get();
            if (o != null) {
                return String.valueOf(o);
            }
        }
        return null;
    }

    protected List<String> getAttributeList(String name, Attributes attributes) {
        List<String> result = new ArrayList<>();
        try {
            NamingEnumeration<?> objectClass = attributes.get(name).getAll();
            while (objectClass.hasMore()) {
                result.add(objectClass.next().toString());
            }
        } catch (NamingException e) {
            log.warn("LDAP: read " + name + " failed, but ignore.", e);
        }
        return result;
    }

    protected abstract BaseCtx loginCtx(String ownerUid, String ldapAccount);

    protected abstract BaseSearch buildQuery(BaseCtx ldapCtx, String ldapAccount);

    protected abstract UserData mapUser(BaseCtx ldapCtx, UserData primaryUser, String ldapAccount, Attributes attributes) throws NamingException;

    protected abstract void checkThrowError(Exception e);

    protected RoleData findRole(String primaryUID, String roleName) {
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
    }
}
