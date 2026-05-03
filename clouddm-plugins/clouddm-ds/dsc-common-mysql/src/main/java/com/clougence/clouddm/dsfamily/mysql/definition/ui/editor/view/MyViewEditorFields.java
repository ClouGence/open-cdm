package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view;

import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface MyViewEditorFields extends ViewEditorFields {

    String VIEW_CHECK_OPTION  = MyUmiAttributeNames.VIEW_CHECK_OPTION.getCodeKey();

    String VIEW_DEFINER       = MyUmiAttributeNames.DEFINER.getCodeKey();
    String VIEW_SECURITY_TYPE = MyUmiAttributeNames.SECURITY_TYPE.getCodeKey();
    String VIEW_CHARACTER_SET = MyUmiAttributeNames.CHARACTER_SET.getCodeKey();
    String VIEW_COLLATION     = MyUmiAttributeNames.COLLATION.getCodeKey();
    String VIEW_UPDATABLE     = MyUmiAttributeNames.VIEW_UPDATABLE.getCodeKey();
}
