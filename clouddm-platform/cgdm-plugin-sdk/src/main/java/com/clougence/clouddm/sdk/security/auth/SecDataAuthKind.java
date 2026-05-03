package com.clougence.clouddm.sdk.security.auth;

import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import lombok.Getter;

@Getter
public enum SecDataAuthKind {

    // for all
    READ(SecDataAuthLabel.DM_DAUTH_QUERY),
    WRITE(SecDataAuthLabel.DM_DAUTH_DML),
    ADMIN(SecDataAuthLabel.DM_DAUTH_DCL),
    OTHER(SecDataAuthLabel.DM_DAUTH_OTHER),

    // for RDBMS
    SPACE(SecDataAuthLabel.DM_DAUTH_SPACE),
    DDL(SecDataAuthLabel.DM_DAUTH_DDL),
    OBJECT(SecDataAuthLabel.DM_DAUTH_OBJ),
    CALL(SecDataAuthLabel.DM_DAUTH_CALL),;

    private final String authLabel;

    SecDataAuthKind(String authLabel){
        this.authLabel = authLabel;
    }
}
