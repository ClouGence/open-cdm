/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
