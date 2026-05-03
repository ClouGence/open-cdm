package com.clougence.clouddm.api.sidecar.definition;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * @author mode 2021/1/14 14:10
 */
@RSocketApiClass
public interface DefinitionRService {

    TableEditorUiPanel fetchTableEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dbConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchFunctionUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchProcedureUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchViewUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTriggerEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTablespaceUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchDbLinkUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchJobUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchScheduleJobEditorUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchJobPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchUserPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchDbLinkPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTablePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSequencePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSynonymPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTriggerPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchViewPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchMaterializedViewPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchRolePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchScheduleJobPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchProcedurePropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchFunctionPropertyUiPanel(RSocketSendDTO sendDTO, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

}
