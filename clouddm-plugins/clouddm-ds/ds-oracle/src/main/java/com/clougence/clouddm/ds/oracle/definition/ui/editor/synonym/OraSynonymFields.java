package com.clougence.clouddm.ds.oracle.definition.ui.editor.synonym;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.synonym.SynonymFields;

public interface OraSynonymFields extends SynonymFields {

    String CREATE_TIME   = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String OBJ_STATUS    = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String LAST_DDL_TIME = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String DBLINK        = OracleAttributeNames.SYNONYM_TABLE_DBLINK.getCodeKey();
}
