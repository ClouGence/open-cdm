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

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.dblink.DbLinkUiDefService;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.job.JobUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.role.RoleUiDefService;
import com.clougence.clouddm.sdk.ui.editor.sequence.SequenceUiDefService;
import com.clougence.clouddm.sdk.ui.editor.synonym.SynonymUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.tablespace.TablespaceUiDefService;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.user.UserUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefinitionManagerImpl implements DefinitionManager {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        TableEditorUiDefService defService = PluginManager.findUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        TableEditorUiPanel editorDef = defService.fetchTableEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);

        // unified processing some thing.

        return editorDef;
    }

    @Override
    public UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        FunctionUiDefService defService = PluginManager.findFunctionUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchFunctionUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        ProcedureUiDefService defService = PluginManager.findProcedureUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchProcedureUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        ViewUiDefService defService = PluginManager.findViewUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchViewUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        TriggerUiDefService defService = PluginManager.findTriggerUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchTriggerEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchTablespaceEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        TablespaceUiDefService defService = PluginManager.findTablespaceUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchTablespaceEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchDbLinkEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        DbLinkUiDefService defService = PluginManager.findDbLinkUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchDbLinkEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        JobUiDefService defService = PluginManager.findJobUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchJobEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        JobUiDefService defService = PluginManager.findJobUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchScheduleJobEditorUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        JobUiDefService defService = PluginManager.findJobUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchJobPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        UserUiDefService defService = PluginManager.findUserUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchUserPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        DbLinkUiDefService defService = PluginManager.findDbLinkUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchDbLinkPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        TableEditorUiDefService defService = PluginManager.findUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchTablePropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        SequenceUiDefService defService = PluginManager.findSequenceUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchSequencePropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        SynonymUiDefService defService = PluginManager.findSynonymUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchSynonymPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        TriggerUiDefService defService = PluginManager.findTriggerUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchTriggerPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        ViewUiDefService defService = PluginManager.findViewUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchViewPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        ViewUiDefService defService = PluginManager.findViewUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchMaterializedViewPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        RoleUiDefService defService = PluginManager.findRoleUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchRolePropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        JobUiDefService defService = PluginManager.findJobUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchScheduleJobPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        ProcedureUiDefService defService = PluginManager.findProcedureUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchProcedurePropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables) throws Exception {
        DataSourceType dsType = dsConfig.getDataSourceType();
        FunctionUiDefService defService = PluginManager.findFunctionUiDefService(dsType);
        if (defService == null) {
            return null;
        }
        I18nUtils i18nMessages = PluginManager.findDsI18nUtil(dsType);
        return defService.fetchFunctionPropertyUiPanel(dsConfig, dsSession, envVariables, i18nMessages);
    }
}
