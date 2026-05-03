package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.function;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;

public interface Db2FunctionFields extends FunctionEditorFields {

    String CREATE_TIME   = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME   = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String DEBUG_MODE    = Db2AttributeNames.DEBUG_MODE.getCodeKey();
    String DETERMINISTIC = Db2AttributeNames.DETERMINISTIC.getCodeKey();
}
