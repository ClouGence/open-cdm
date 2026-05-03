package com.clougence.clouddm.console.web.service.editor;

import java.util.List;

import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.model.fo.editor.table.*;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.service.editor.model.ResultSetDTO;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;

/**
 * Fetch behavior
 *
 * @author mode 2020/12/8 15:21
 */
public interface DsTableEditorService {

    /** for service API '/editor/table/editorDef' */
    TableEditorForm loadTableEditorDef(String puid, String uid, DsLevels levels, EditorDefFO defFO);

    /** for service API '/editor/table/initEditor' */
    TableEditorUiData loadTableEditorData(String puid, String uid, DsLevels levels, EditorInitFO initO);

    /** for service API '/editor/table/generateScript' */
    List<ResultSetDTO> tableEditorGenerate(String puid, String uid, DsLevels levels, EditorGenFO genFO);

    /** for service API '/editor/table/scriptExecute' */
    List<ResultSetDTO> tableEditorSave(String puid, String uid, DsLevels levels, EditorExecFO execFO, String clientIp);

    List<String> fetchReferencedColumns(String puid, String uid, DsLevels levels, EditorReferencedFO execFO);
}
