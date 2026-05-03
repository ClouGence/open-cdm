package com.clougence.clouddm.console.web.service.editor;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyEditorUiData;
import com.clougence.schema.umi.struts.UmiTypes;

public interface DsObjPropertyService {

    PropertyEditorUiData loadPropertyData(String puid, String uid, DsLevels levels, UmiTypes types, String leafName);

    TableEditorForm loadPropertyDef(String puid, String uid, DsLevels levels, UmiTypes types);
}
