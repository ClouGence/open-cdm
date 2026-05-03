package com.clougence.clouddm.ds.polardb.definition.pormy.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.polardb.i18n.PorMyDsI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTableEditorUiPanelFactory;

/**
 * create table
 * https://help.aliyun.com/document_detail/123333.html?spm=a2c4g.123333.0.0.3bf05f26FtH1c4
 * */
public class PorMyTableEditorUiPanelFactory extends MyTableEditorUiPanelFactory implements PorMyTableEditorFields {

    // tableEditor TableInfo panel
    @Override
    protected List<ValueDef> fetchTableEngine(DataSourceConfig dsConfig) {
        List<ValueDef> result = super.fetchTableEngine(dsConfig);
        result.add(2, optionDef(PorMyDsI18nKeys.EDITOR_TABLEINFO_ENGINE_XENGINE_LABEL, "X-Engine"));
        return result;
    }
}
