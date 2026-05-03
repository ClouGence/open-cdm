package com.clougence.clouddm.ds.oracle.definition.ui.editor.view;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.view.ViewEditorFields;

public interface OraViewEditorFields extends ViewEditorFields {

    String FORCE_CREATE_VIEW      = OracleAttributeNames.FORCE_CREATE_VIEW.getCodeKey();

    // property
    String TEXT_LENGTH            = OracleAttributeNames.VIEW_TEXT_LENGTH.getCodeKey();
    String STATUS                 = OracleAttributeNames.OBJ_STATUS.getCodeKey();
    String CREATE_TIME            = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String DDL_TIME               = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String READ_ONLY              = OracleAttributeNames.VIEW_READ_ONLY.getCodeKey();

    String MVIEW_REFRESH_DATE     = OracleAttributeNames.MVIEW_REFRESH_DATE.getCodeKey();
    String MVIEW_REFRESH_END_TIME = OracleAttributeNames.MVIEW_REFRESH_END_TIME.getCodeKey();
}
