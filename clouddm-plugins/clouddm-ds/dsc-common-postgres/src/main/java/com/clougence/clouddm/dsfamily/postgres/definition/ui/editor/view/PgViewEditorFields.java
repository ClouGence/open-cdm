package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.view;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface PgViewEditorFields extends ViewEditorFields {

    String VIEW_TEMP             = PostgreAttributeNames.VIEW_TEMP.getCodeKey();
    String VIEW_SECURITY_BARRIER = PostgreAttributeNames.VIEW_SECURITY_BARRIER.getCodeKey();
    String VIEW_CHECK_OPTION     = PostgreAttributeNames.VIEW_CHECK_OPTION.getCodeKey();
}
