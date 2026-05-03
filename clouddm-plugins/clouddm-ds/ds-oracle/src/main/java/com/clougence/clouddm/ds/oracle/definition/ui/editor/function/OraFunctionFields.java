package com.clougence.clouddm.ds.oracle.definition.ui.editor.function;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;

public interface OraFunctionFields extends FunctionEditorFields {

    //    String
    String CREATED       = OracleAttributeNames.CREATED.getCodeKey();
    String LAST_DDL_TIME = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String PIPELINED     = OracleAttributeNames.PROCEDURE_PIPELINED.getCodeKey();
    String PARALLEL      = OracleAttributeNames.PROCEDURE_PARALLEL.getCodeKey();
    String STATUS        = OracleAttributeNames.OBJ_STATUS.getCodeKey();
}
