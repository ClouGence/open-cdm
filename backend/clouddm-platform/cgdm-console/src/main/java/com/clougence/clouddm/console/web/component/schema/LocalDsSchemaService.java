/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.component.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevelLeaf;
import com.clougence.clouddm.console.web.service.browse.MetaInformatinCacheService;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
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
    private UserConfigService          userConfigService;
    @Resource
    private ObjectCacheDao             cacheDao;

    private boolean isDisableMetaCache(String uid) {
        UserCacheEntry byUID = this.cacheDao.queryByUid(uid);
        if (byUID.getUserType() == AccountType.SUB_ACCOUNT) {
            byUID = this.cacheDao.queryByUid(byUID.getParentUid());
        }

        DmSysUserConfDO configDO = this.userConfigService.getSpecifiedConfig(byUID.getUid(), UserDefinedConfig.Fields.consoleMetadataCache);
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
    public String realTimeFetchVersion(String uid, long clusterId, DataSourceConfig dsConfig, Map<UmiTypes, Object> levelsParam) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String realTimeFetchVersion(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam) {
        return null;
    }

    @Override
    public Value realTimeFetchSelectObject(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String leafName) {
        return null;
    }

    @Override
    public List<String> realTimeRequestObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DsElement> cachedObjectNames(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        List<DsElement> result = new ArrayList<>();
        DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsDO.getDataSourceType());
        if (shouldListLevels(dsConfig, levels)) {
            List<DsElement> levelElements = this.listLevels(uid, dsDO, levels, levelsParam, false);
            if (levelElements != null) {
                result.addAll(levelElements);
            }
        }
        if (levels != null && !levels.isEmpty()) {
            List<DsLevelLeaf> leafTypes = dsConfig.getCategories().getLeafGroup().get(levels.get(levels.size() - 1).getTypeName());
            if (leafTypes != null) {
                for (DsLevelLeaf leafType : leafTypes) {
                    UmiTypes umiType = UmiTypes.valueOfCode(leafType.getType());
                    List<DsElement> leafElements = this.listLeaf(uid, dsDO, levelsParam, umiType, null, false);
                    if (leafElements != null) {
                        result.addAll(leafElements);
                    }
                }
            }
        }
        return result;
    }

    private static boolean shouldListLevels(DsConfig dsConfig, List<UmiTypes> levels) {
        int currentSize = levels == null ? 0 : levels.size();
        return dsConfig != null && dsConfig.getCategories() != null && dsConfig.getCategories().getLevels() != null && dsConfig.getCategories().getLevels().size() > currentSize + 2;
    }

    //

    @Override
    public List<DsElement> listLevels(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam, boolean refreshCache) {
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

        String catalog = (String) levelsParam.get(UmiTypes.Catalog);
        String schema = (String) levelsParam.get(UmiTypes.Schema);
        String context = cacheService.getListCache(uid, dsDO.getId(), catalog, schema, leafType);
        if (context != null) {
            return JsonUtils.toList(context, new TypeReference<>() {});
        }
        return null;
    }

    @Override
    public DsElement detailLevel(String uid, DmDsDO dsDO, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return null;
    }

    @Override
    public List<DsElement> listLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern, boolean refreshCache) {
        if (refreshCache || isDisableMetaCache(uid)) {
            return null;
        }

        String catalog = (String) levelsParam.get(UmiTypes.Catalog);
        String schema = (String) levelsParam.get(UmiTypes.Schema);
        MetaInformationType metaType = MetaInformationType.valueOfCode(leafType.getTypeName() + "List");
        String context = cacheService.getListCache(uid, dsDO.getId(), catalog, schema, metaType);

        if (context != null) {
            return JsonUtils.toList(context, new TypeReference<>() {});
        }
        return null;
    }

    @Override
    public Value detailLeaf(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, boolean refreshCache) {
        if (refreshCache || isDisableMetaCache(uid)) {
            return null;
        }

        String catalog = (String) levelsParam.get(UmiTypes.Catalog);
        String schema = (String) levelsParam.get(UmiTypes.Schema);
        MetaInformationType metaType = MetaInformationType.valueOfCode(leafType.getTypeName());
        String context = cacheService.getDetailCache(uid, dsDO.getId(), catalog, schema, metaType, leafName);
        if (context != null) {
            return JsonUtils.toObj(context, Value.class);
        }
        return null;
    }

    @Override
    public List<String> generateObjectScript(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName, CmdTemplateOption option) {
        return null;
    }

    @Override
    public TableEditorUiPanel fetchTableEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchFunctionUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchProcedureUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchViewUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchTriggerEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchTablespaceUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchDbLinkUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchJobUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public UiPanel fetchScheduleJobEditorUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchUserPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchSequencePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchSynonymPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchTriggerPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchMaterializedViewPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchRolePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchScheduleJobPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchProcedurePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchFunctionPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchDbLinkPropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public PropertyUiPanel fetchTablePropertyUiPanel(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, Map<String, String> envVariables) {
        return null;
    }

    @Override
    public String loadTableEditor(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, String table, boolean refreshCache) {
        String catalog = (String) levelsParam.get(UmiTypes.Catalog);
        String schema = (String) levelsParam.get(UmiTypes.Schema);
        return cacheService.getDetailCache(uid, dsDO.getId(), catalog, schema, MetaInformationType.ETable, table);
    }

    @Override
    public EditorContext createEditorContext(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, EditorOptions options) {
        return null;
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String uid, DmDsDO dsDO, Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> names) {
        return null;
    }
}
