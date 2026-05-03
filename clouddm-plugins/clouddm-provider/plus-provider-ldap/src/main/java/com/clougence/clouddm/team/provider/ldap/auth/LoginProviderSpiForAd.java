package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.clougence.clouddm.sdk.security.login.LoginProviderSpi;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;

import com.clougence.clouddm.team.provider.ldap.constants.LdapI18nKey;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.security.login.LoginProvider;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.ConsoleConfigService;
import com.clougence.clouddm.sdk.service.config.RoleData;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.utils.*;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode
 * @version 2020-01-17 15:29
 */
@Slf4j
public class LoginProviderSpiForAd extends BaseLoginProviderSpi implements LoginProviderSpi {

    private final Map<String, BaseConfig>           configMap;
    private final Map<String, Map<String, BaseCtx>> contextMap;

    public LoginProviderSpiForAd(ConsoleConfigService configService){
        super(configService);
        this.configMap = new ConcurrentHashMap<>();
        this.contextMap = new ConcurrentHashMap<>();
    }

    @Override
    public LifeSpiResponse start(String ownerUid, LifeSpiRequest requestDTO) {
        // fetch config
        BaseConfig conf = ConfigHelper.fetchConfig(this.configService, ownerUid);

        // enable is false.
        if (!conf.getAuthType().equalsIgnoreCase(LoginProvider.AD.name())) {
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
                throw ThirdPartyApiException.asRDP().with(LdapI18nKey.AD_NET_BIOS_IP_MAP.name());
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

        return this.contextMap.get(ownerUid).computeIfAbsent(ownerUid, s -> {
            BaseConfig cfg = this.configMap.get(ownerUid);

            LdapContextSource source = new LdapContextSource();
            source.setUrl(extractLdapUrl(ownerUid, ldapAccount));
            source.setBase(cfg.getLdapBase());
            source.setUserDn(cfg.getLdapUser());
            source.setPassword(cfg.getLdapPassword());
            //source.setReferral("follow");

            Map<String, Object> config = new HashMap<>();
            config.put("java.naming.ldap.attributes.binary", "objectGUID");
            config.put("com.sun.jndi.ldap.read.timeout", StringUtils.isBlank(cfg.getLdapSoTimeout()) ? "3000" : cfg.getLdapSoTimeout());
            config.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            config.put(Context.OBJECT_FACTORIES, new DefaultDirObjectFactory());

            source.setPooled(false);
            source.setBaseEnvironmentProperties(config);
            source.afterPropertiesSet();

            LdapTemplate template = new LdapTemplate(source);
            template.setIgnorePartialResultException(false);
            return new BaseCtx(cfg, template);
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
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_LOGIN_FAIL_PRIMARY_MISSING_ARGS.name());
        }

        String userAccount = fullLoginName.substring(0, splitIdx);
        String userDomain = fullLoginName.substring(splitIdx + 1);
        return new String[] { userAccount, userDomain };
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
            ContainerCriteria ldapQuery = LdapQueryBuilder.query().where(ldapWhere).is(ldapCondition);
            return new BaseSearch(ldapQuery, ldapWhere, ldapCondition);
        } else if (ldapAccount.contains("\\")) {
            // NetBIOS format
            String[] split = StringUtils.split(ldapAccount, "\\");

            String ldapWhere = AD_SamAccountName;
            String ldapDomain = split[0];
            String ldapCondition = split[1];
            ContainerCriteria ldapQuery = LdapQueryBuilder.query().where(ldapWhere).is(ldapCondition);
            return new BaseSearch(ldapQuery, ldapWhere, ldapCondition);
        } else {
            String ldapWhere = AD_UserPrincipalName;
            String ldapCondition = ldapAccount + "@" + ldapCtx.getLdapConfig().getLdapDomain();
            ContainerCriteria ldapQuery = LdapQueryBuilder.query().where(ldapWhere).is(ldapCondition);
            return new BaseSearch(ldapQuery, ldapWhere, ldapCondition);
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
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_OBJECTCLASS_NOT_ALLOWED_ERROR.name());
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
        user.setSubAccount(finalAdName + "@" + primaryUser.getUserDomain());
        user.setBindAccount(finalAdName);
        user.setUserDomain(primaryUser.getUserDomain());

        // mapping role
        RoleData role = searchRole(primaryUser.getInternalUID(), ldapCtx);
        if (role == null) {
            log.info("Ad: user(" + user.getSubAccount() + ") not found any role, find roleName=" + ldapCtx.getLdapConfig().getLdapRoleMap());
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_USER_ROLE_MAPPING_FAILED.name());
        }
        user.setRoleId(role.getRoleId());

        return user;
    }

    @Override
    protected void checkThrowError(Exception e) {
        // https://blog.csdn.net/chaijunkun/article/details/23695001
        if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 525")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_NOT_EXIST.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 52e")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_PASSWORD_ERROR.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 530")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_TIME.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 531")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_LOGIN_NOT_ALLOWED_THIS_PC.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 532")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_PASSWORD_EXPIRED.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 533")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_USER_DISABLED.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 701")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_EXPIRED.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 773")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_NEED_RESET_PASSWORD.name());
        } else if (StringUtils.contains(e.getMessage(), "AcceptSecurityContext error, data 775")) {
            throw ThirdPartyApiException.asRDP().with(e, LdapI18nKey.AD_LOGIN_FAIL_ACCOUNT_LOCKED.name());
        }
    }

    private void checkUserAcl(long userAclNum) {
        //https://learn.microsoft.com/en-us/troubleshoot/windows-server/active-directory/useraccountcontrol-manipulate-account-properties
        if (userAclNum == (userAclNum | 2)) {
            throw ThirdPartyApiException.asRDP().with(LdapI18nKey.LDAP_USER_IS_DISABLED_ERROR.name());
        }
    }

    private RoleData searchRole(String primaryUID, BaseCtx ldapCtx) {
        BaseConfig ldapConfig = ldapCtx.getLdapConfig();
        String roleName = ldapConfig.getLdapRoleMap();
        roleName = StringUtils.isEmpty(roleName) ? SecSysRole.DEV_ROLE_NAME : roleName;
        List<RoleData> roles = this.configService.findRoleByName(primaryUID, roleName);
        return CollectionUtils.isEmpty(roles) ? null : roles.get(0);
    }
}
