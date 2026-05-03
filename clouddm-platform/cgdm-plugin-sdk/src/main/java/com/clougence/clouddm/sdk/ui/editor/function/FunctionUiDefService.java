package com.clougence.clouddm.sdk.ui.editor.function;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.utils.i18n.I18nUtils;

public interface FunctionUiDefService extends Spi {

    UiPanel fetchFunctionUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;

    PropertyUiPanel fetchFunctionPropertyUiPanel(DataSourceConfig dsConfig, Session dsSession, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;
}
