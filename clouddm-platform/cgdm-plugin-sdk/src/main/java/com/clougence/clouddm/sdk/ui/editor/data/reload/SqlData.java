package com.clougence.clouddm.sdk.ui.editor.data.reload;

import java.util.Map;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlData {

    private Map<String, String> whereData;

    private Map<String, String> updateData;

    private DataEditorSqlType   dmlType;

}
