package com.clougence.clouddm.console.web.component.schema;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.definition.DefinitionRService;
import com.clougence.clouddm.api.sidecar.session.execute.MetaRService;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsStatusService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.service.browse.MetaInformatinCacheService;
import com.clougence.clouddm.platform.plugin.PluginManager;
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
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RemoteDsSchemaService implements DsSchemaService {

    @Resource
    private DmDsConfigService          dmDsConfigService;
    @Resource
    private DefinitionRService         defRService;
    @Resource
    private MetaRService               dsMetaRService;
    @Resource
    private BizResOwnerCacheService    ownerCacheService;
    @Resource
    private DmDsStatusService          dmDsStatusService;
    @Resource
    private MetaInformatinCacheService cacheService;

    protected RSocketSendDTO genClusterSendDTO(RdpDataSourceDO dsDO, String uid) {
        DsCacheEntry dsCacheEntry = this.ownerCacheService.queryByDsId(dsDO.getId());
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(dsCacheEntry.getClusterId());
        sendDTO.setUid(uid);
        sendDTO.setRSocketSendType(RSocketSendType.CLUSTER);
        return sendDTO;
    }

    protected final DataSourceConfig fetchDsConfig(RdpDataSourceDO dataSourceDO) {
        return this.dmDsConfigService.fetchDsConfigFromDM(dataSourceDO.getId(), dataSourceDO.getDataSourceType());
    }

    @Override
    public String getVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(clusterId);
        sendDTO.setUid(uid);
        sendDTO.setRSocketSendType(RSocketSendType.CLUSTER);
        return this.dsMetaRService.getVersion(sendDTO, dsConfig, levelsParam);
    }

    @Override
    public String getVersion(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.dsMetaRService.getVersion(sendDTO, dsConfig, levelsParam);
    }

    @Override
    public List<DsElement> listLevels(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            List<DsElement> dsElements = this.dsMetaRService.listLevels(sendDTO, dsConfig, levels, levelsParam);

            DsConfig dmDsConfig = dmDsConfigService.dsConstantSettings(dsDO.getDataSourceType());
            MetaInformationType metaType;
            if (levelsParam.get(UmiTypes.Catalog) == null && UmiTypes.Catalog.getTypeName().equals(dmDsConfig.getCategories().getLevels().get(2))) {
                metaType = MetaInformationType.CatalogList;
            } else {
                metaType = MetaInformationType.SchemaList;
            }

            String catalog = (String) levelsParam.get(UmiTypes.Catalog);
            String schema = (String) levelsParam.get(UmiTypes.Schema);
            cacheService.putListCache(uid, dsDO.getId(), catalog, schema, metaType, JsonUtils.toJson(dsElements));
            return dsElements;
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public DsElement detailLevel(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            return this.dsMetaRService.detailLevel(sendDTO, dsConfig, levels, levelsParam);
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public List<DsElement> listLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            List<DsElement> dsElements = this.dsMetaRService.listLeaf(sendDTO, dsConfig, levelsParam, leafType, pattern);

            String catalog = (String) levelsParam.get(UmiTypes.Catalog);
            String schema = (String) levelsParam.get(UmiTypes.Schema);
            MetaInformationType metaType = MetaInformationType.valueOfCode(leafType.getTypeName() + "List");
            cacheService.putListCache(uid, dsDO.getId(), catalog, schema, metaType, JsonUtils.toJson(dsElements));
            return dsElements;
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public Value detailLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            Value value = this.dsMetaRService.detailLeaf(sendDTO, dsConfig, levelsParam, leafType, leafName);

            String catalog = (String) levelsParam.get(UmiTypes.Catalog);
            String schema = (String) levelsParam.get(UmiTypes.Schema);
            MetaInformationType metaType = MetaInformationType.valueOfCode(leafType.getTypeName());
            cacheService.putDetailCache(uid, dsDO.getId(), catalog, schema, metaType, leafName, JsonUtils.toJson(value));
            return value;
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public Value fetchSelectObject(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            return this.dsMetaRService.fetchSelectObject(sendDTO, dsConfig, levelsParam, leafName);
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public List<String> requestObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            return this.dsMetaRService.requestObjectScript(sendDTO, dsConfig, levelsParam, leafType, leafName);
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public List<String> generateObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TableEditorUiPanel fetchTableEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            return this.defRService.fetchTableEditorUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public UiPanel fetchFunctionUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchFunctionUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchProcedureUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchProcedureUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchViewUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchViewUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchTriggerEditorUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchTablespaceUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchDbLinkUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchJobUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchJobUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchScheduleJobEditorUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchJobPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchUserPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchDbLinkPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchTablePropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchSequencePropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchSynonymPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchTriggerPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchViewPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchMaterializedViewPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchRolePropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchScheduleJobPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchProcedurePropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.defRService.fetchFunctionPropertyUiPanel(sendDTO, dsConfig, levelsParam, envVariables);
    }

    @Override
    public String loadTableEditor(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        this.dmDsStatusService.changeStatusIfNecessary(sendDTO, dsConfig, levelsParam);
        try {
            String eTable = this.dsMetaRService.loadTableEditor(sendDTO, dsConfig, levelsParam, table);
            if (eTable == null) {
                return null;
            }

            String catalog = (String) levelsParam.get(UmiTypes.Catalog);
            String schema = (String) levelsParam.get(UmiTypes.Schema);
            MetaInformationType metaType = MetaInformationType.ETable;
            cacheService.putDetailCache(uid, dsDO.getId(), catalog, schema, metaType, table, eTable);
            return eTable;
        } catch (Exception e) {
            dmDsStatusService.handleException(uid, dsConfig, e);
            throw e;
        }
    }

    @Override
    public EditorContext createEditorContext(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options) {
        EditorContext ctx = new EditorContext(PluginManager.findDsSqlBuilder(dsDO.getDataSourceType()));
        ctx.setUseDelimited(options != null && options.isUseDelimited());
        ctx.setSkipHandlers(options != null && options.isSkipHandlers());
        return ctx;
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names) {
        RSocketSendDTO sendDTO = genClusterSendDTO(dsDO, uid);
        DataSourceConfig dsConfig = this.fetchDsConfig(dsDO);
        return this.dsMetaRService.loadColumns(sendDTO, dsConfig, levelsParam, leafType, names);
    }
}
