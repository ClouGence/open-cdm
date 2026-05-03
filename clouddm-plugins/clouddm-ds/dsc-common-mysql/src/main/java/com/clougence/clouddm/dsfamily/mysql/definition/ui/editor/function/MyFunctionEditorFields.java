package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionEditorFields;

public interface MyFunctionEditorFields extends FunctionEditorFields {

    String DETERMINISTIC   = MyUmiAttributeNames.DETERMINISTIC.getCodeKey();
    String SQL_DATA_ACCESS = MyUmiAttributeNames.SQL_DATA_ACCESS.getCodeKey();
    String DEFINER         = MyUmiAttributeNames.DEFINER.getCodeKey();
    String SECURITY_TYPE   = MyUmiAttributeNames.SECURITY_TYPE.getCodeKey();
    String CREATE_TIME     = MyUmiAttributeNames.CREATE_TIME.getCodeKey();
    String UPDATE_TIME     = MyUmiAttributeNames.UPDATE_TIME.getCodeKey();
}
