package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.view;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface Db2ViewEditorFields extends ViewEditorFields {

    String VIEW_CHECK_OPTION = Db2AttributeNames.VIEW_CHECK_OPTION.getCodeKey();

    String CREATE_TIME       = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME       = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String COLUMN_COUNT      = Db2AttributeNames.COLUMN_COUNT.getCodeKey();
    String VIEW_READ_ONLY    = Db2AttributeNames.VIEW_READ_ONLY.getCodeKey();
    String INSERTABLE        = Db2AttributeNames.INSERTABLE.getCodeKey();
    String DELETABLE         = Db2AttributeNames.DELETABLE.getCodeKey();
    String UPDATABLE         = Db2AttributeNames.UPDATABLE.getCodeKey();
}
