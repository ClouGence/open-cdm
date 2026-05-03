package com.clougence.clouddm.sdk.ui.editor.trigger;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.utils.i18n.I18nUtils;

public interface TriggerUiDefService extends Spi {

    UiPanel fetchTriggerEditorUiPanel(DataSourceConfig dsConfig, Session session, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;

    PropertyUiPanel fetchTriggerPropertyUiPanel(DataSourceConfig dsConfig, Session session, Map<String, String> envVariables, I18nUtils i18nMessages) throws Exception;

}
