package com.clougence.clouddm.ds.oracle.definition.ui.editor.user;

import com.clougence.adapter.oracle.OracleAttributeNames;

public interface OraUserFields {

    String USER_ID                = OracleAttributeNames.USER_ID.getCodeKey();
    String CREATE_TIME            = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String USER_COMMON            = OracleAttributeNames.USER_OR_ROLE_COMMON.getCodeKey();
    String USER_ORACLE_MAINTAINED = OracleAttributeNames.USER_OR_ROLE_ORACLE_MAINTAINED.getCodeKey();
}
