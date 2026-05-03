package com.clougence.dslparser.detectrule.test.rule;

import lombok.Getter;

@Getter
public enum SecDataAuthKind {

    QUERY("DM_DAUTH_QUERY"),
    DML("DM_DAUTH_DML"),
    DDL("DM_DAUTH_DDL"),
    DCL("DM_DAUTH_DCL"),
    OTHER("DM_DAUTH_OTHER"),;

    private final String authLabel;

    SecDataAuthKind(String authLabel){
        this.authLabel = authLabel;
    }
}
