package com.clougence.clouddm.ds.oracle.definition.ui.editor.role;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.role.RoleFields;

public interface OraRoleFields extends RoleFields {

    String ROLE_AUTHENTICATION_TYPE = OracleAttributeNames.ROLE_AUTHENTICATION_TYPE.getCodeKey();
    String USER_COMMON              = OracleAttributeNames.USER_OR_ROLE_COMMON.getCodeKey();
    String USER_ORACLE_MAINTAINED   = OracleAttributeNames.USER_OR_ROLE_ORACLE_MAINTAINED.getCodeKey();
}
