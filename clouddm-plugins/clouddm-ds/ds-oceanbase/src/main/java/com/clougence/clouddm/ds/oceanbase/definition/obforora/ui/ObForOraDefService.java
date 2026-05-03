package com.clougence.clouddm.ds.oceanbase.definition.obforora.ui;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.oceanbase.definition.obforora.ui.editor.table.ObForOraTablePropertyUiPanelFactory;
import com.clougence.clouddm.ds.oracle.definition.ui.OraDefService;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;

/**
 * @author Ekko
 * @date 2023/9/7 15:51
 */
public class ObForOraDefService extends OraDefService {

    @Override
    protected PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Connection dsConnection, Map<String, String> envVariables) {
        return new ObForOraTablePropertyUiPanelFactory().create(dsConfig, dsConnection);
    }
}
