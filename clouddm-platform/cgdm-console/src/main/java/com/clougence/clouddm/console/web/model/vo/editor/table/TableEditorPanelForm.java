package com.clougence.clouddm.console.web.model.vo.editor.table;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableEditorPanelForm {

    private String                     titleI18N;
    private String                     descI18N;
    private List<TableEditorFieldForm> children;

}
