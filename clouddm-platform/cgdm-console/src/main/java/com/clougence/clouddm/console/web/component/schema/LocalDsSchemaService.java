package com.clougence.clouddm.console.web.component.schema;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.service.browse.MetaInformatinCacheService;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
@Service
public class LocalDsSchemaService implements DsSchemaService {

    @Resource
    private MetaInformatinCacheService cacheService;

    @Resource
    private DmDsConfigService          dmDsConfigService;
    @Resource
    private RdpUserConfigService       rdpUserConfigService;
    @Resource
    private BizResOwnerCacheService    ownerCacheService;

    private boolean isDisableMetaCache(String uid) {
        UserCacheEntry byUID = this.ownerCacheService.queryByUid(uid);
        if (byUID.getUserType() == AccountType.SUB_ACCOUNT) {
            byUID = this.ownerCacheService.queryByUid(byUID.getParentUid());
        }

        RdpUserKvBaseConfigDO configDO = this.rdpUserConfigService.getSpecifiedConfig(byUID.getUid(), UserDefinedConfig.Fields.consoleMetadataCache);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return true;
        }

        try {
            return !Boolean.parseBoolean(configDO.getConfigValue());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public String getVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getVersion(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam) {
        return null;
    }

    @Override
    public List<DsElement> listLevels(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache) {
        if (refreshCache || isDisableMetaCache(uid)) {
            return null;
        }

        DsConfig dmDsConfig = dmDsConfigService.dsConstantSettings(dsDO.getDataSourceType());
        MetaInformationType leafType;
        if (levelsParam.get(UmiTypes.Catalog) == null && UmiTypes.Catalog.getTypeName().equals(dmDsConfig.getCategories().getLevels().get(2))) {
            leafType = MetaInformationType.CatalogList;
        } else {
            leafType = MetaInformationType.SchemaList;
        }

        String context = cacheService.getListCache(uid, dsDO.getId(), (String) levelsParam.get(UmiTypes.Catalog), (String) levelsParam.get(UmiTypes.Schema), leafType);

        if (context != null) {
            return JsonUtils.toList(context, new TypeReference<List<DsElement>>() {
            });
        }
        return null;
    }

    @Override
    public DsElement detailLevel(String uid, RdpDataSourceDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return null;
    }

    @Override
    public List<DsElement> listLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache) {
        if (refreshCache || isDisableMetaCache(uid)) {
            return null;
        }

        String context = cacheService.getListCache(uid, dsDO.getId(), (String) levelsParam.get(UmiTypes.Catalog), (String) levelsParam.get(UmiTypes.Schema), MetaInformationType
            .valueOfCode(leafType.getTypeName() + "List"));

        if (context != null) {
            return JsonUtils.toList(context, new TypeReference<List<DsElement>>() {
            });
        }
        return null;
    }

    @Override
    public Value detailLeaf(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache) {
        if (refreshCache || isDisableMetaCache(uid)) {
            return null;
        }

        String context = cacheService.getDetailCache(uid, dsDO.getId(), (String) levelsParam.get(UmiTypes.Catalog), (String) levelsParam.get(UmiTypes.Schema), MetaInformationType
            .valueOfCode(leafType.getTypeName()), leafName);
        if (context != null) {
            return JsonUtils.toObj(context, Value.class);
        }
        return null;
    }

    @Override
    public Value fetchSelectObject(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName) {
        return null;
    }

    @Override
    public List<String> requestObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> generateObjectScript(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option) {
        return null;
    }

    @Override
    public TableEditorUiPanel fetchTableEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchFunctionUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchProcedureUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchViewUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchJobUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public String loadTableEditor(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache) {
        return cacheService
            .getDetailCache(uid, dsDO.getId(), (String) levelsParam.get(UmiTypes.Catalog), (String) levelsParam.get(UmiTypes.Schema), MetaInformationType.ETable, table);
    }

    @Override
    public EditorContext createEditorContext(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options) {
        return null;
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names) {
        return null;
    }
}
