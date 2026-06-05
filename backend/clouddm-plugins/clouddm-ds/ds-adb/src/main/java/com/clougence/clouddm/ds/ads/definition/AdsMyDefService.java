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
package com.clougence.clouddm.ds.ads.definition;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.ads.definition.ui.editor.table.AdsMyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure.MyProcedureUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.trigger.MyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.view.MyViewUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.procedure.ProcedureUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class AdsMyDefService extends AbstractDefService implements TableEditorUiDefService, TriggerUiDefService, ViewUiDefService, FunctionUiDefService, ProcedureUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        TableEditorUiPanel uiPanel = new AdsMyTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);

        //https://help.aliyun.com/document_detail/26125.html?spm=a2c4g.95798.0.0.1fa8d0f8Xy6NsS
        if (StringUtils.isNotBlank(dsConfig.getAliyunInstanceId())) {
            UiPanelField engine = uiPanel.getTableInfo().findField(MyTableEditorUiPanelFactory.FIELD_TABLE_ENGINE);

            List<ValueDef> result = new ArrayList<>();
            result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_EMPTY_LABEL, ""));
            result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_INNODB_LABEL, "InnoDB"));
            result.add(optionDef(MyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_MYISAM_LABEL, "X-Engine"));
            engine.setOptions(result);
        }

        //https://help.aliyun.com/document_detail/90333.html?spm=a2c4g.26125.0.0.35862deaa3zfao#concept-hb5-px2-vdb
        // aliyun MariaDB -> InnoDB

        return uiPanel;
    }

    @Override
    protected UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyFunctionEditorUiPanelFactory().createFunctionUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchProcedureUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyProcedureUiPanelFactory().createProcedureUiPanel(dsConfig, viewMode, con);
    }

    @Override
    public UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyViewUiPanelFactory().createViewUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new MyTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }
}
