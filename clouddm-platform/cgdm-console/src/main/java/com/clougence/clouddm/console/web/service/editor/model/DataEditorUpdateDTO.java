package com.clougence.clouddm.console.web.service.editor.model;

import java.util.Map;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorUpdateDTO {

    private Map<String, String> whereData;

    private Map<String, String> updateData;

    private DataEditorSqlType   dmlType;

    private Integer             sequence;

}
