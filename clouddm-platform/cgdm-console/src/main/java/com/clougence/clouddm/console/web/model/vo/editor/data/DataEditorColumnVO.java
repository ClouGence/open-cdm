package com.clougence.clouddm.console.web.model.vo.editor.data;

import java.util.List;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorHeader;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorColumnVO {

    private String                 column;

    private String                 columnType;

    private String[]               option;

    private Boolean                isUk;

    private Boolean                isPk;

    private DataEditorUiStyle      uiType;

    private Boolean                hasDefault;

    private Boolean                isNullable;

    private Long                   length;

    private Integer                numericPrecision;

    private Integer                numericScale;

    private Boolean                autoincrement  = false;

    private Boolean                whereKey       = false;

    private Boolean                spareWhere     = true;

    private Boolean                insertReadOnly = false;

    private Boolean                updateReadOnly = false;

    private Boolean                hide           = false;

    private List<DataEditorHeader> check;
}
