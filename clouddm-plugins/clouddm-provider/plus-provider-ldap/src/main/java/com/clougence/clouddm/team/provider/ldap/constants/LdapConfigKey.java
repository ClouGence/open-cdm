package com.clougence.clouddm.team.provider.ldap.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum LdapConfigKey {

    AuthType("subAccountAuthType"),

    LdapHost("ldapHost"),
    LdapPort("ldapPort"),
    LdapNetBIOSRoute("ldapNetBIOSRoute"),
    LdapSoTimeout("ldapSoTimeout"),
    LdapBase("ldapBase"),
    LdapUser("ldapUser"),
    LdapPassword("ldapPassword"),
    LdapDomain("ldapDomain"),
    LdapRoleMap("ldapRoleMap"),
    LdapUserObjectClass("ldapUserObjectClass"),
    LdapFieldLogin("ldapFieldLogin"),
    LdapFieldUser("ldapFieldUser"),
    LdapFieldEmail("ldapFieldEmail"),
    LdapFieldPhone("ldapFieldPhone"),;

    private final String configKey;

    LdapConfigKey(String configKey){
        this.configKey = configKey;
    }
}
