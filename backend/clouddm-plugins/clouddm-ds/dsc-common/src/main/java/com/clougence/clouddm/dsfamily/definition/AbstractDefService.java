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
package com.clougence.clouddm.dsfamily.definition;

import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorVarKeys;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public abstract class AbstractDefService {

    public final TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                            I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            TableEditorUiPanel uiPanel = fetchTableEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchProcedureUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchViewUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchFunctionUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchTriggerEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchDbLinkEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchDbLinkEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchJobEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchScheduleJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchScheduleJobEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchJobPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchUserPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchUserPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchDbLinkPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                            I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchDbLinkPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                           I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchTablePropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchScheduleJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                                 I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchScheduleJobPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                             I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchTriggerPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchSequencePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                              I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchSequencePropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchSynonymPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                             I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchSynonymPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchViewPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                                      I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchMaterializedViewPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchRolePropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                               I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchProcedurePropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables,
                                                              I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            PropertyUiPanel uiPanel = fetchFunctionPropertyUiPanel(dsConfig, con, envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    public final UiPanel fetchTablespaceEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception {
        return dsSession.executeQuery(con -> {
            UiPanel uiPanel = fetchTablespaceEditorUiPanel(dsConfig, con, fetchViewMode(envVariables), envVariables);
            // i18n
            i18nMessages.putVariables(envVariables);
            i18nMessages.setDefaultI18nKey(i18nName(envVariables));
            uiPanel.initI18n(i18nMessages);
            return uiPanel;
        });
    }

    protected static String i18nName(Map<String, String> envVariables) {
        String i18nLocal;
        if (envVariables.containsKey(TableEditorVarKeys.I18N_LOCAL_USER.codeKey())) {
            i18nLocal = envVariables.get(TableEditorVarKeys.I18N_LOCAL_USER.codeKey());
        } else {
            i18nLocal = envVariables.get(TableEditorVarKeys.I18N_LOCAL_DEFAULT.codeKey());
        }

        if (StringUtils.isBlank(i18nLocal)) {
            i18nLocal = I18nUtils.toI18nKey(Locale.getDefault());
        }

        return i18nLocal;
    }

    protected static EditorViewMode fetchViewMode(Map<String, String> envVariables) {
        String viewMode = envVariables.get(EditorViewMode.class.getName());
        if (StringUtils.equalsIgnoreCase(viewMode, "create")) {
            return EditorViewMode.Create;
        } else if (StringUtils.equalsIgnoreCase(viewMode, "alter")) {
            return EditorViewMode.Alter;
        } else {
            return null;
        }
    }

    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode,
                                                         Map<String, String> envVariables) throws Exception {
        return null;
    }

    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchDbLinkEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchTablespaceEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchJobEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected UiPanel fetchScheduleJobEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchJobPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchUserPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchDbLinkPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchScheduleJobPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchSequencePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchSynonymPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

    protected PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return null;
    }

}
