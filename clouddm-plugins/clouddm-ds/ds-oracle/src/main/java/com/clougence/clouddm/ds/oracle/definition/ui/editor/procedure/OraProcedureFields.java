package com.clougence.clouddm.ds.oracle.definition.ui.editor.procedure;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;

public interface OraProcedureFields extends ProcedureEditorFields {

    //    String
    String CREATED       = OracleAttributeNames.CREATED.getCodeKey();
    String LAST_DDL_TIME = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String AGGREGATE     = OracleAttributeNames.PROCEDURE_AGGREGATE.getCodeKey();
    String PIPELINED     = OracleAttributeNames.PROCEDURE_PIPELINED.getCodeKey();
    String PARALLEL      = OracleAttributeNames.PROCEDURE_PARALLEL.getCodeKey();
    String STATUS        = OracleAttributeNames.OBJ_STATUS.getCodeKey();

}
