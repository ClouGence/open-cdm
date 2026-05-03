package com.clougence.clouddm.team.provider.ldap.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ldap.core.LdapTemplate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCtx {

    private final BaseConfig   ldapConfig;
    private final List<String> fields;
    private final LdapTemplate ldapTemplate;

    public BaseCtx(BaseConfig ldapConfig, LdapTemplate ldapTemplate){
        this.ldapConfig = ldapConfig;
        this.ldapTemplate = ldapTemplate;
        this.fields = new ArrayList<>();
        this.fields.add(ldapConfig.getLdapFieldLogin());
        this.fields.add(ldapConfig.getLdapFieldUser());
        this.fields.add(ldapConfig.getLdapFieldEmail());
        this.fields.add(ldapConfig.getLdapFieldPhone());
    }
}
