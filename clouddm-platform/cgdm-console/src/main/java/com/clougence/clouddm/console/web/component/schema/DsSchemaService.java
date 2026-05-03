package com.clougence.clouddm.console.web.component.schema;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

/**
 * Fetch behavior
 *
 * @author mode 2020/12/8 15:21
 */
public interface DsSchemaService {

    String getVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam);

    String getVersion(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLevels(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache);

    DsElement detailLevel(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam);

    List<DsElement> listLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache);

    Value detailLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache);

    Value fetchSelectObject(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName);

    // RDB

    List<String> requestObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName);

    List<String> generateObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option);

    TableEditorUiPanel fetchTableEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    String loadTableEditor(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache);

    EditorContext createEditorContext(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options);

    Map<String, List<RdbColumn>> loadColumns(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names);

    //

    UiPanel fetchFunctionUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchProcedureUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchViewUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTriggerEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchTablespaceUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchDbLinkUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchJobUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    UiPanel fetchScheduleJobEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchUserPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSequencePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchRolePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

    PropertyUiPanel fetchTablePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables);

}
