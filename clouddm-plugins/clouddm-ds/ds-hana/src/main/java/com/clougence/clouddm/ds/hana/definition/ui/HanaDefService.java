package com.clougence.clouddm.ds.hana.definition.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaTableEditorUiPanelFactory;
import com.clougence.clouddm.ds.hana.definition.ui.editor.trigger.HanaTriggerEditorUiPanelFactory;
import com.clougence.clouddm.ds.hana.definition.ui.editor.view.HanaViewEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaDefService extends AbstractDefService implements TableEditorUiDefService, TriggerUiDefService, ViewUiDefService, Spi {

    @Override
    protected TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Connection con, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new HanaTableEditorUiPanelFactory().createTableEditorUiPanel(dsConfig, viewMode, con);
    }

    @Override
    protected UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new HanaTriggerEditorUiPanelFactory().createTriggerEditorUiPanel(dsConfig, viewMode, dsConnection);
    }

    @Override
    protected UiPanel fetchViewUiPanel(DataSourceConfig dsConfig, Connection dsConnection, EditorViewMode viewMode, Map<String, String> envVariables) {
        return new HanaViewEditorUiPanelFactory().createViewUiPanel(dsConfig, viewMode, dsConnection);
    }

}
