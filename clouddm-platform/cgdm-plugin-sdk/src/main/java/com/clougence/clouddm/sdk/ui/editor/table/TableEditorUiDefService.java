package com.clougence.clouddm.sdk.ui.editor.table;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.utils.i18n.I18nUtils;

public interface TableEditorUiDefService extends Spi {

    TableEditorUiPanel fetchTableEditorUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;

    PropertyUiPanel fetchTablePropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;
}
