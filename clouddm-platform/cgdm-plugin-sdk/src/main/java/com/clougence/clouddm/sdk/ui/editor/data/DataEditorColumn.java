package com.clougence.clouddm.sdk.ui.editor.data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorColumn {

    private String                 column;

    private String                 columnType;

    private String[]               option;

    private Boolean                isUk;

    private Boolean                isPk;

    private DataEditorUiStyle      uiType;

    private Boolean                hasDefault     = false;

    private Boolean                isNullable     = true;

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
