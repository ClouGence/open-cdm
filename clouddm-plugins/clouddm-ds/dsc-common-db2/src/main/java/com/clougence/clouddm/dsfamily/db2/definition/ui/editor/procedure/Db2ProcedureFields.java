package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.procedure;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureEditorFields;

public interface Db2ProcedureFields extends ProcedureEditorFields {

    String CREATE_TIME = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String DEBUG_MODE  = Db2AttributeNames.DEBUG_MODE.getCodeKey();
}
