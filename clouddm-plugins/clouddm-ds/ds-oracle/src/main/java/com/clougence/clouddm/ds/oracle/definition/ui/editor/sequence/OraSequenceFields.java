package com.clougence.clouddm.ds.oracle.definition.ui.editor.sequence;

import com.clougence.adapter.oracle.OracleAttributeNames;

public interface OraSequenceFields {

    String STATUS       = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String CREATE_TIME  = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String DDL_TIME     = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();

    String CYCLE_FLAG   = OracleAttributeNames.SEQUENCE_CYCLE_FLAG.getCodeKey();
    String CACHE_SIZE   = OracleAttributeNames.SEQUENCE_CACHE_SIZE.getCodeKey();
    String LAST_NUMBER  = OracleAttributeNames.SEQUENCE_LAST_NUMBER.getCodeKey();
    String SESSION_FLAG = OracleAttributeNames.SEQUENCE_SESSION_FLAG.getCodeKey();
    String KEEP_VALUE   = OracleAttributeNames.SEQUENCE_KEEP_VALUE.getCodeKey();
}
