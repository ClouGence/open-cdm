package com.clougence.clouddm.team.provider.ldap.auth;

import org.springframework.ldap.query.LdapQuery;

import lombok.Getter;

@Getter
public class BaseSearch {

    private final LdapQuery ldapQuery;
    private final String    ldapWhere;
    private final String    ldapCondition;

    public BaseSearch(LdapQuery ldapQuery, String ldapWhere, String ldapCondition){
        this.ldapQuery = ldapQuery;
        this.ldapWhere = ldapWhere;
        this.ldapCondition = ldapCondition;
    }
}
