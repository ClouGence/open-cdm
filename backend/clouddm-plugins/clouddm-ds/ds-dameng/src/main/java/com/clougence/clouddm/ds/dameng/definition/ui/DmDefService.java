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
package com.clougence.clouddm.ds.dameng.definition.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.function.DmFunctionEditorUiPanelFactory;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.procedure.DmProcedureEditorUiPanelFactory;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.table.DmTableEditorUiPanelFactory;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.tablespace.DmTablespaceUiPanelFactory;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.trigger.DmTriggerEditorUiPanelFactory;
import com.clougence.clouddm.ds.dameng.definition.ui.editor.view.DmViewUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.tablespace.TablespaceUiDefService;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * @author Ekko
 * @date 2023/10/8 14:24
 */
public class DmDefService extends AbstractDefService implements TableEditorUiDefService, FunctionUiDefService, ViewUiDefService, ProcedureUiDefService, TriggerUiDefService, TablespaceUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        TableEditorUiPanel uiPanel = new DmTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
        return uiPanel;
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new DmFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new DmViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new DmProcedureEditorUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new DmTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    public UiPanel fetchTablespaceEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new DmTablespaceUiPanelFactory().createTablespaceUiPanel(dsConfig, viewMode, dsConnection);
    }
}
