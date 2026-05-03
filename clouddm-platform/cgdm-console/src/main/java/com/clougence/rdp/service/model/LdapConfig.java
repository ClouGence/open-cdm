package com.clougence.rdp.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020-01-04 09:44
 * @since 1.1.3
 */
@Getter
@Setter
public class LdapConfig {

    //
    private String ldapHost;
    private String ldapPort;
    private String ldapNetBIOSRoute;
    private String ldapSoTimeout;
    private String ldapBase;
    private String ldapUser;
    private String ldapPassword;

    //
    private String ldapRoleMap;
    private String ldapAdPrimaryGroup;
    private String ldapUserObjectClass;

    //
    private String ldapFieldLogin;
    private String ldapFieldUser;
    private String ldapFieldEmail;
    private String ldapFieldPhone;

}
