package com.clougence.clouddm.console.web.service.editor.model;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorChangeDTO {

    private Integer           sequence;
    private String            sql;
    private DataEditorSqlType sqlType;
}
