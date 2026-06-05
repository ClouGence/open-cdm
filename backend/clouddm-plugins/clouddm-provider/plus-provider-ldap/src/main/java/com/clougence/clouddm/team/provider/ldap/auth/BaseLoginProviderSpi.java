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
package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.security.login.LoginRequest;
import com.clougence.clouddm.sdk.security.login.LoginResponse;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
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
                throw (ThirdPartyApiException) e;
            } else {
                throw ThirdPartyApiException.as().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
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
            authenticate(ldapCtx, ldapSearch, ldapPassword);
            return new LoginResponse(ldapUser, true);
        } catch (Exception e) {
            this.checkThrowError(e);
            if (e instanceof ThirdPartyApiException) {
                throw (ThirdPartyApiException) e;
            } else {
                throw ThirdPartyApiException.as().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
            }
        }
    }

    protected UserData queryUser(BaseCtx ldapCtx, UserData primaryUser, String ldapAccount) {
        // query user
        List<UserData> searchResult;
        BaseSearch ldapSearch = this.buildQuery(ldapCtx, ldapAccount);
        try {
            searchResult = search(ldapCtx, ldapSearch).stream().map(attributes -> {
                try {
                    return this.mapUser(ldapCtx, primaryUser, ldapAccount, attributes);
                } catch (ThirdPartyApiException e) {
                    throw e;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e instanceof ThirdPartyApiException) {
                throw (ThirdPartyApiException) e;
            } else {
                throw ThirdPartyApiException.as().with(e, LdapI18nKey.LDAP_SERVICE_ERROR.name(), ExceptionUtils.getRootCauseMessage(e));
            }
        }

        // result
        if (searchResult.isEmpty()) {
            return null;
        } else if (searchResult.size() > 1) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_MATCH_MULTIPLE_ACCOUNT.name(), ldapSearch.getLdapWhere(), ldapSearch.getLdapCondition(), searchResult.size());
        } else {
            return searchResult.get(0);
        }
    }

    private void authenticate(BaseCtx ldapCtx, BaseSearch ldapSearch, String password) throws NamingException {
        SearchResult searchResult = findSingleSearchResult(ldapCtx, ldapSearch);
        String userDn = searchResult.getNameInNamespace();
        Hashtable<String, Object> env = new Hashtable<>(ldapCtx.getEnvironment());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);
        close(new InitialDirContext(env));
    }

    private List<Attributes> search(BaseCtx ldapCtx, BaseSearch ldapSearch) throws NamingException {
        List<Attributes> result = new ArrayList<>();
        DirContext ctx = null;
        NamingEnumeration<SearchResult> enumeration = null;
        try {
            ctx = new InitialDirContext(ldapCtx.getEnvironment());
            enumeration = ctx.search(ldapCtx.getLdapConfig().getLdapBase(), ldapSearch.getLdapFilter(), searchControls());
            while (enumeration.hasMore()) {
                result.add(enumeration.next().getAttributes());
            }
        } finally {
            close(enumeration);
            close(ctx);
        }
        return result;
    }

    private SearchResult findSingleSearchResult(BaseCtx ldapCtx, BaseSearch ldapSearch) throws NamingException {
        List<SearchResult> result = new ArrayList<>();
        DirContext ctx = null;
        NamingEnumeration<SearchResult> enumeration = null;
        try {
            ctx = new InitialDirContext(ldapCtx.getEnvironment());
            enumeration = ctx.search(ldapCtx.getLdapConfig().getLdapBase(), ldapSearch.getLdapFilter(), searchControls());
            while (enumeration.hasMore()) {
                result.add(enumeration.next());
            }
        } finally {
            close(enumeration);
            close(ctx);
        }

        if (result.isEmpty()) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_LOGIN_FAIL_PASSWORD_ERROR.name());
        }
        if (result.size() > 1) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_MATCH_MULTIPLE_ACCOUNT.name(), ldapSearch.getLdapWhere(), ldapSearch.getLdapCondition(), result.size());
        }
        return result.get(0);
    }

    private SearchControls searchControls() {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        return controls;
    }

    protected String eqFilter(String attrName, String attrValue) {
        return "(" + escapeFilter(attrName) + "=" + escapeFilter(attrValue) + ")";
    }

    private String escapeFilter(String value) {
        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\' -> sb.append("\\5c");
                case '*' -> sb.append("\\2a");
                case '(' -> sb.append("\\28");
                case ')' -> sb.append("\\29");
                case '\u0000' -> sb.append("\\00");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    private void close(NamingEnumeration<?> enumeration) {
        if (enumeration == null) {
            return;
        }
        try {
            enumeration.close();
        } catch (NamingException e) {
            log.warn("LDAP: close search result failed, but ignore.", e);
        }
    }

    private void close(Context ctx) {
        if (ctx == null) {
            return;
        }
        try {
            ctx.close();
        } catch (NamingException e) {
            log.warn("LDAP: close context failed, but ignore.", e);
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
