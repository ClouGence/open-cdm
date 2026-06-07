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

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class LoginProviderSpiForLdap extends BaseLoginProviderSpi implements LoginProviderSpi {

    private final Map<String, BaseConfig> configMap;
    private final Map<String, BaseCtx>    contextMap;

    public LoginProviderSpiForLdap(ConsoleConfigService configService){
        super(configService);
        this.configMap = new ConcurrentHashMap<>();
        this.contextMap = new ConcurrentHashMap<>();
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        BaseConfig conf = ConfigHelper.fetchConfig(this.configService, ownerUid);

        // enable is false.
        if (!containsProvider(conf.getAuthType(), LoginProvider.LDAP)) {
            log.info("ignoreLogin[Ldap] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.configMap.containsKey(ownerUid)) {
                log.info("ignoreLogin[Ldap] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            this.configMap.put(ownerUid, ConfigHelper.checkLdapConfig(conf));

            // finish
            log.info("startLogin[Ldap] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        synchronized (this) {
            this.configMap.remove(ownerUid);
            this.contextMap.remove(ownerUid);
        }

        log.info("stopLogin[Ldap] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.configMap.containsKey(ownerUid));
        dto.setNameKey(LdapI18nKey.LDAP_LOGIN_SERVICES_NAME.name());
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    @Override
    protected BaseCtx loginCtx(String ownerUid, String ldapAccount) {
        if (!this.configMap.containsKey(ownerUid)) {
            throw new UnsupportedOperationException("ldapLoginService is was closed");
        }

        return this.contextMap.computeIfAbsent(ownerUid, s -> {
            BaseConfig cfg = this.configMap.get(ownerUid);

            String ldapUrl = "ldap://" + cfg.getLdapHost() + ":" + cfg.getLdapPort();
            Hashtable<String, Object> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, cfg.getLdapUser());
            env.put(Context.SECURITY_CREDENTIALS, cfg.getLdapPassword());
            env.put("java.naming.ldap.attributes.binary", "objectGUID");
            env.put("com.sun.jndi.ldap.read.timeout", StringUtils.isBlank(cfg.getLdapSoTimeout()) ? "3000" : cfg.getLdapSoTimeout());
            return new BaseCtx(cfg, env);
        });
    }

    //

    @Override
    public String loginExtractAccount(String fullLoginName) {
        return this.extractSplit(fullLoginName)[0];
    }

    @Override
    public String loginExtractDomain(String fullLoginName) {
        return this.extractSplit(fullLoginName)[1];
    }

    private String[] extractSplit(String fullLoginName) {
        String[] split = fullLoginName.split("@");
        if (split.length == 1) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_LOGIN_FAIL_PRIMARY_MISSING_ARGS.name());
        }

        String userAccount = split[0];
        String domain = split[1];
        return new String[] { userAccount, domain };
    }

    @Override
    protected BaseSearch buildQuery(BaseCtx ldapCtx, String ldapAccount) {
        String ldapWhere = ldapCtx.getLdapConfig().getLdapFieldLogin();
        String ldapCondition = ldapAccount;
        return new BaseSearch(eqFilter(ldapWhere, ldapCondition), ldapWhere, ldapCondition);
    }

    @Override
    protected UserData mapUser(BaseCtx ldapCtx, UserData primaryUser, String ldapAccount, Attributes attributes) throws NamingException {
        BaseConfig ldapConfig = ldapCtx.getLdapConfig();

        // filter objectClass
        List<String> userObjectClass = getAttributeList("objectClass", attributes);
        if (StringUtils.isNotBlank(ldapConfig.getLdapUserObjectClass())) {
            String ldapUserObjectClass = ldapConfig.getLdapUserObjectClass();
            List<String> filterd = Arrays.stream(ldapUserObjectClass.split(",")).map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterd)) {
                boolean match = false;
                for (String filter : filterd) {
                    if (userObjectClass.contains(filter)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    log.info("LDAP: user objectClass {} does not match any group.", StringUtils.join(filterd.toArray(), ","));
                    throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_OBJECTCLASS_NOT_ALLOWED_ERROR.name());
                }
            }
        }

        // map user
        UserData user = new UserData();
        user.setUserName(getAttribute(attributes, "objectSid"));
        user.setUserName(getAttribute(attributes, ldapConfig.getLdapFieldUser()));
        user.setEmail(getAttribute(attributes, ldapConfig.getLdapFieldEmail()));
        user.setPhone(getAttribute(attributes, ldapConfig.getLdapFieldPhone()));
        user.setAccount(ldapAccount.trim());
        user.setBindAccount(getAttribute(attributes, ldapConfig.getLdapFieldLogin()));

        // mapping role
        RoleData role = searchRole(primaryUser.getInternalUID(), ldapCtx);
        if (role == null) {
            log.info("LDAP: user(" + user.getAccount() + ") not found any role, find roleName=" + ldapCtx.getLdapConfig().getLdapRoleMap());
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_USER_ROLE_MAPPING_FAILED.name());
        }
        user.setRoleId(role.getRoleId());

        return user;
    }

    @Override
    protected void checkThrowError(Exception e) {
        if (StringUtils.contains(e.getMessage(), "Invalid Credentials")) {
            throw ThirdPartyApiException.as().with(e, LdapI18nKey.LDAP_LOGIN_FAIL_PASSWORD_ERROR.name());
        }
    }

    private RoleData searchRole(String primaryUID, BaseCtx ldapCtx) {
        BaseConfig ldapConfig = ldapCtx.getLdapConfig();
        String roleName = ldapConfig.getLdapRoleMap();
        roleName = StringUtils.isEmpty(roleName) ? SecSysRole.DEV_ROLE_NAME : roleName;
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
    }

    private boolean containsProvider(String authType, LoginProvider provider) {
        return Arrays.stream(StringUtils.defaultString(authType).split("[,，;；]")).anyMatch(item -> StringUtils.equalsIgnoreCase(item.trim(), provider.name()));
    }
}
