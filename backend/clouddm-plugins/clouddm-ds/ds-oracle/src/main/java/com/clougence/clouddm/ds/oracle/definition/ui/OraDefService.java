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
package com.clougence.clouddm.ds.oracle.definition.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink.OraDbLinkEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink.OraDbLinkPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.function.OraFunctionEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.function.OraFunctionPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.job.OraJobEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.job.OraJobPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.procedure.OraProcedureEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.procedure.OraProcedurePropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.role.OraRolePropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule.OraScheduleJobEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule.OraScheduleJobPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.sequence.OraSequencePropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.synonym.OraSynonymPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraTableEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.table.OraTablePropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.trigger.OraTriggerEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.trigger.OraTriggerPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.user.OraUserPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.view.OraMaterializedViewPropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.view.OraViewEditorUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.view.OraViewPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
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
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.user.UserUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * @author Ekko
 * @date 2023/9/7 15:51
 */
public class OraDefService extends AbstractDefService implements Spi, SynonymUiDefService, TableEditorUiDefService, FunctionUiDefService, ProcedureUiDefService, ViewUiDefService, TriggerUiDefService, DbLinkUiDefService, SequenceUiDefService, JobUiDefService, UserUiDefService, RoleUiDefService {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraProcedureEditorUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraViewEditorUiPanelFactory().createViewUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchDbLinkEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraDbLinkEditorUiPanelFactory().create(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchJobEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraJobEditorUiPanelFactory().create(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchScheduleJobEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new OraScheduleJobEditorUiPanelFactory().create(dsConfig, viewMode, con);
    }

    @Override
    protected PropertyUiPanel fetchJobPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraJobPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchDbLinkPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraDbLinkPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchSequencePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraSequencePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchSynonymPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraSynonymPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraMaterializedViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraRolePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchScheduleJobPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraScheduleJobPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchProcedurePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraProcedurePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraFunctionPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraTriggerPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchUserPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new OraUserPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }
}
