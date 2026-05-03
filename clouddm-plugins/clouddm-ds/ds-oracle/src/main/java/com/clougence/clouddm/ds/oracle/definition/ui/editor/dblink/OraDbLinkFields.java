package com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.dblink.DbLinkEditorFields;

public interface OraDbLinkFields extends DbLinkEditorFields {

    String BASE_INFO   = "baseInfo";

    String CREATE_TIME = OracleAttributeNames.RDB_OBJ_CREATE_TIME.getCodeKey();
}
