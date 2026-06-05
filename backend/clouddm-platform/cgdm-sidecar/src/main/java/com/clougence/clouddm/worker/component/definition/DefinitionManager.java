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
package com.clougence.clouddm.worker.component.definition;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

public interface DefinitionManager {

    TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchTablespaceEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchDbLinkEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    UiPanel fetchScheduleJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchUserPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchDbLinkPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchSequencePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchSynonymPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchScheduleJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;

    PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception;
}
