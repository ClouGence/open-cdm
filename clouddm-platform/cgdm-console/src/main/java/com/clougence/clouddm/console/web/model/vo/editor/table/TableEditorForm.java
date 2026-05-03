package com.clougence.clouddm.console.web.model.vo.editor.table;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableEditorForm {

    private List<String>                      order;
    private Map<String, TableEditorPanelForm> uiPanels;

}
