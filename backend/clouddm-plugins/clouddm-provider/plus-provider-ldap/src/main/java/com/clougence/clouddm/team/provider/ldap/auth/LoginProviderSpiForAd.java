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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.clougence.utils.*;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class LoginProviderSpiForAd extends BaseLoginProviderSpi implements LoginProviderSpi {

    private static final Pattern                    AD_SUB_ERROR_CODE = Pattern.compile("data\\s+([0-9a-f]{3,4})\\b", Pattern.CASE_INSENSITIVE);
    private final Map<String, BaseConfig>           configMap;
    private final Map<String, Map<String, BaseCtx>> contextMap;

    public LoginProviderSpiForAd(ConsoleConfigService configService){
        super(configService);
        this.configMap = new ConcurrentHashMap<>();
        this.contextMap = new ConcurrentHashMap<>();
    }

    @Override
    public int order() {
        return 20;
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        BaseConfig conf = ConfigHelper.fetchConfig(this.configService, ownerUid);

        // enable is false.
        if (!containsProvider(conf.getAuthType(), LoginProvider.AD)) {
            log.info("ignoreLogin[Ad] primaryUid：" + ownerUid + ", enable is false.");
            return new LifeSpiResponse();
        }

        // start and init.
        synchronized (this) {
            if (this.configMap.containsKey(ownerUid) || this.contextMap.containsKey(ownerUid)) {
                log.info("ignoreLogin[Ad] primaryUid：" + ownerUid + ", already started.");
                return new LifeSpiResponse();
            }

            this.configMap.put(ownerUid, ConfigHelper.checkAdConfig(conf));
            this.contextMap.put(ownerUid, new ConcurrentHashMap<>());

            // finish
            log.info("startLogin[Ad] primaryUid：" + ownerUid);
            return new LifeSpiResponse();
        }
    }

    @Override
    public LifeSpiResponse stop(String ownerUid, LifeSpiRequest requestDTO) {
        synchronized (this) {
            this.configMap.remove(ownerUid);
            this.contextMap.remove(ownerUid);
        }

        log.info("stopLogin[Ad] ownerUid：" + ownerUid);
        return new LifeSpiResponse();
    }

    @Override
    public LifeSpiResponse status(String ownerUid, LifeSpiRequest requestDTO) {
        LifeSpiStatus dto = new LifeSpiStatus();
        dto.setRunning(this.configMap.containsKey(ownerUid) || this.contextMap.containsKey(ownerUid));
        dto.setNameKey(LdapI18nKey.AD_LOGIN_SERVICES_NAME.name());
        return new LifeSpiResponse(JsonUtils.toJson(dto));
    }

    private String extractLdapUrl(final String ownerUid, final String ldapAccount) {
        BaseConfig cfg = this.configMap.get(ownerUid);
        if (ldapAccount.contains("\\")) {
            Map<String, List<String>> oriRoleMap = StringUtils.toArrayMap(cfg.getLdapNetBIOSRoute());
            Map<String, String> roleMap = new LinkedCaseInsensitiveMap<>();
            oriRoleMap.forEach((key, to) -> roleMap.put(key, to.get(0)));

            String[] split = StringUtils.split(ldapAccount, "\\");
            if (!roleMap.containsKey(split[0])) {
                throw ThirdPartyApiException.as().with(LdapI18nKey.AD_NET_BIOS_IP_MAP.name());
            }

            return "ldap://" + roleMap.get(split[0]) + ":" + cfg.getLdapPort();
        } else {
            return "ldap://" + cfg.getLdapHost() + ":" + cfg.getLdapPort();
        }
    }

    @Override
    protected BaseCtx loginCtx(String ownerUid, String ldapAccount) {
        if (!this.configMap.containsKey(ownerUid) || !this.contextMap.containsKey(ownerUid)) {
            throw new UnsupportedOperationException("adLoginService is was closed");
        }

        String ldapUrl = extractLdapUrl(ownerUid, ldapAccount);
        return this.contextMap.get(ownerUid).computeIfAbsent(ldapUrl, s -> {
            BaseConfig cfg = this.configMap.get(ownerUid);

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
        int splitIdx = fullLoginName.lastIndexOf("@");
        if (splitIdx == -1) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_LOGIN_FAIL_PRIMARY_MISSING_ARGS.name());
        }

        String userAccount = fullLoginName.substring(0, splitIdx);
        String domain = fullLoginName.substring(splitIdx + 1);
        return new String[] { userAccount, domain };
    }

    private static final String AD_ObjectGUID         = "objectGUID";
    private static final String AD_UserPrincipalName  = "userPrincipalName";
    private static final String AD_UserAccountControl = "userAccountControl";
    private static final String AD_SamAccountName     = "sAMAccountName";
    private static final String AD_UserName           = "displayName";
    private static final String AD_UserMail           = "mail";
    private static final String AD_UserPhone          = "telephoneNumber";

    @Override
    protected BaseSearch buildQuery(BaseCtx ldapCtx, String ldapAccount) {
        if (ldapAccount.contains("@")) {
            // UPN format
            String ldapWhere = AD_UserPrincipalName;
            String ldapCondition = ldapAccount;
            return new BaseSearch(eqFilter(ldapWhere, ldapCondition), ldapWhere, ldapCondition);
        } else if (ldapAccount.contains("\\")) {
            // NetBIOS format
            String[] split = StringUtils.split(ldapAccount, "\\");

            String ldapWhere = AD_SamAccountName;
            String ldapCondition = split[1];
            return new BaseSearch(eqFilter(ldapWhere, ldapCondition), ldapWhere, ldapCondition);
        } else {
            String ldapWhere = AD_UserPrincipalName;
            String ldapCondition = ldapAccount + "@" + ldapCtx.getLdapConfig().getLdapDomain();
            return new BaseSearch(eqFilter(ldapWhere, ldapCondition), ldapWhere, ldapCondition);
        }
    }

    @Override
    protected UserData mapUser(BaseCtx ldapCtx, UserData primaryUser, String ldapAccount, Attributes attributes) throws NamingException {
        // filter objectClass
        List<String> userObjectClass = getAttributeList("objectClass", attributes);
        List<String> filterd = Arrays.asList("person", "organizationalPerson", "user");
        boolean match = false;
        for (String filter : filterd) {
            if (userObjectClass.contains(filter)) {
                match = true;
                break;
            }
        }
        if (!match) {
            String objClass = StringUtils.join(filterd.toArray(), ",");
            log.info("LDAP: user objectClass {} does not match any userType.", objClass);
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_OBJECTCLASS_NOT_ALLOWED_ERROR.name());
        }

        // user ACL
        String userAcl = getAttribute(attributes, AD_UserAccountControl);
        if (NumberUtils.isNumber(userAcl)) {
            checkUserAcl(Long.parseLong(userAcl));
        }

        String adUPN = getAttribute(attributes, AD_UserPrincipalName);
        String adSam = getAttribute(attributes, AD_SamAccountName);
        String adEmail = getAttribute(attributes, AD_UserMail);
        String finalAdName = StringUtils.isNotBlank(adUPN) ? adUPN : adSam;

        // map user
        UserData user = new UserData();
        user.setExternalUID(HexadecimalUtils.bytes2bit((byte[]) attributes.get(AD_ObjectGUID).get()));
        user.setUserName(getAttribute(attributes, AD_UserName));
        user.setEmail(StringUtils.isBlank(adEmail) ? adUPN : adEmail);
        user.setPhone(getAttribute(attributes, AD_UserPhone));
        user.setAccount(finalAdName);
        user.setBindAccount(finalAdName);

        // mapping role
        RoleData role = searchRole(primaryUser.getInternalUID(), ldapCtx);
        if (role == null) {
            log.info("Ad: user(" + user.getAccount() + ") not found any role, find roleName=" + ldapCtx.getLdapConfig().getLdapRoleMap());
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_USER_ROLE_MAPPING_FAILED.name());
        }
        user.setRoleId(role.getRoleId());

        return user;
    }

    @Override
    protected void checkThrowError(Exception e) {
        String subErrorCode = extractAdSubErrorCode(e);
        if (StringUtils.isBlank(subErrorCode)) {
            return;
        }

        switch (subErrorCode.toLowerCase()) {
            case "525" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_NOT_EXIST.name());
            case "52e" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_PASSWORD_ERROR.name());
            case "530" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_TIME.name());
            case "531" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_PC.name());
            case "532" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_PASSWORD_EXPIRED.name());
            case "533" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_USER_DISABLED.name());
            case "701" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_EXPIRED.name());
            case "773" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_NEED_RESET_PASSWORD.name());
            case "775" -> throw ThirdPartyApiException.as().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_LOCKED.name());
            default -> {
            }
        }
    }

    private String extractAdSubErrorCode(Throwable e) {
        for (Throwable cursor = e; cursor != null; cursor = cursor.getCause()) {
            Matcher matcher = AD_SUB_ERROR_CODE.matcher(StringUtils.defaultString(cursor.getMessage()));
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private void checkUserAcl(long userAclNum) {
        //https://learn.microsoft.com/en-us/troubleshoot/windows-server/active-directory/useraccountcontrol-manipulate-account-properties
        if (userAclNum == (userAclNum | 2)) {
            throw ThirdPartyApiException.as().with(LdapI18nKey.LDAP_USER_IS_DISABLED_ERROR.name());
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
