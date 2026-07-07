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

    String realTimeFetchVersion(long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam);

    String realTimeFetchVersion(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam);

    Value realTimeFetchSelectObject(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName);

    List<String> realTimeRequestObjectScript(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    List<DsElement> cachedObjectNames(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    // any DB

    List<DsElement> listLevels(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache);

    DsElement detailLevel(DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache);

    Value detailLeaf(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache);

    // RDB only

    List<String> generateObjectScript(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option);

    TableEditorUiPanel fetchTableEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    String loadTableEditor(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache);

    EditorContext createEditorContext(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options);

    Map<String, List<RdbColumn>> loadColumns(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names);

    // UI Panel

    UiPanel fetchFunctionUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchProcedureUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchViewUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTriggerEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTablespaceUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchDbLinkUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchJobUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchScheduleJobEditorUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchJobPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchUserPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSequencePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSynonymPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTriggerPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchViewPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchRolePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchScheduleJobPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchProcedurePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchFunctionPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchDbLinkPropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTablePropertyUiPanel(DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);
}
