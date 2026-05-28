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
package com.clougence.clouddm.console.web.component.schema;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

/**
 * Fetch behavior
 *
 * @author mode 2020/12/8 15:21
 */
public interface DsSchemaService {

    String getVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam);

    String getVersion(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLevels(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache);

    DsElement detailLevel(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache);

    Value detailLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache);

    Value fetchSelectObject(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName);

    // RDB

    List<String> requestObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    List<String> generateObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option);

    TableEditorUiPanel fetchTableEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    String loadTableEditor(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache);

    EditorContext createEditorContext(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options);

    Map<String, List<RdbColumn>> loadColumns(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names);

    //

    UiPanel fetchFunctionUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchProcedureUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchViewUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTriggerEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTablespaceUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchDbLinkUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchJobUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchScheduleJobEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchUserPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSequencePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchRolePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTablePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

}
