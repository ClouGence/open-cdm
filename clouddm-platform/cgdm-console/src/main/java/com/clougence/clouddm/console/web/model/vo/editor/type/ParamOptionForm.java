package com.clougence.clouddm.console.web.model.vo.editor.type;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorPanelForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamOptionForm {

    private Map<String, TableEditorPanelForm> uiPanels = new HashMap<>();
}
