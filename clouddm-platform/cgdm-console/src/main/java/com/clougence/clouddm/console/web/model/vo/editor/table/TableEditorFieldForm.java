package com.clougence.clouddm.console.web.model.vo.editor.table;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableEditorFieldForm {

    private String                     field;
    private String                     type;
    private Object                     typeConfig;
    private boolean                    require;
    private boolean                    readOnly;
    private boolean                    hide;
    private Object                     defaultVal;
    private List<Object>               options;
    private String                     titleI18N;
    private String                     descI18N;
    private List<TableEditorFieldForm> children;
}
