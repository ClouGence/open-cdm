package com.clougence.clouddm.ds.maxcompute.definition.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.role.McRolePropertyUiPanelFactory;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table.McTablePropertyUiPanelFactory;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.view.McViewPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.AbstractDefService;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.ui.editor.function.FunctionUiDefService;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.role.RoleUiDefService;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDefService;
import com.clougence.clouddm.sdk.ui.editor.view.ViewUiDefService;

/**
 * @author mode
 * @date 2023/10/8 14:24
 */
public class McDefService extends AbstractDefService implements TableEditorUiDefService, FunctionUiDefService, ViewUiDefService, RoleUiDefService, Spi {

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new McTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new McViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchMaterializedViewPropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new McViewPropertyUiPanelFactory().create(dsConfig, dsConnection);
    }

    @Override
    protected PropertyUiPanel fetchRolePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new McRolePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }
}
