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
package com.clougence.clouddm.console.web.service.browse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseObjectMO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsTagDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@Slf4j
@Service
public class BrowseServiceImpl implements BrowseService {
    @Resource
    private DataSourceDal     dsDal;
    @Resource
    private DmDsService       dmDsService;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private DsSchemaService   dmDsSchemaService;

    /**
     * for service API '/browse/listLevels'
     */
    @Override
    public List<BrowseLevelsVO> listDs(String puid, String uid, String envId) {
        List<Long> dsIds = this.dsDal.dsMapper().listByUidAndEnvId(puid, envId).stream().map(DmDsDO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<DmDsConfigDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        dsIds = confList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<DmDsDO> dsList = this.dsDal.dsMapper().listByDsIdsAndEnvId(dsIds, envId);
        List<DmDsTagDO> dsTags = this.dsDal.tagMapper().listByDsAndUser(dsIds, uid);
        Map<Long, String> dsTagMap = new HashMap<>();
        dsTags.forEach(dsTag -> dsTagMap.put(dsTag.getDataSourceId(), dsTag.getInstanceDesc()));

        // convert
        final Map<Long, DataSourceConfig> dsConfigMap = new HashMap<>();
        final Map<Long, String> dsHostMap = new HashMap<>();
        final Map<DataSourceType, RdbSupportSpi> dsRdbSupportMap = new HashMap<>();
        for (DmDsDO dsDO : dsList) {
            try {
                RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsDO.getDataSourceType());
                if (supportSpi != null) {
                    dsRdbSupportMap.put(dsDO.getDataSourceType(), supportSpi);
                }

                DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
                String dsHost = this.dmDsConfigService.fetchDsConfig(dsDO.getId(), ConfigKeys.DM_DS_KEY_HOST);
                dsConfigMap.put(dsDO.getId(), dsConfig);
                dsHostMap.put(dsDO.getId(), dsHost);
            } catch (Exception ignored) {
            }
        }

        Map<Long, DmDsConfigDO> dataSourceStatusMap = getDataSourceStatusMap(dsList);

        return dsList.stream().map(dsDO -> {
            DataSourceConfig dsConfig = dsConfigMap.get(dsDO.getId());
            RdbSupportSpi supportSpi = dsRdbSupportMap.get(dsDO.getDataSourceType());
            String dsHost = dsHostMap.get(dsDO.getId());
            BrowseLevelsVO levelVO = DmConvertUtils.convertToBrowseLevelsVO(dsDO, dsConfig, dataSourceStatusMap.get(dsDO.getId()), supportSpi, dsHost);
            levelVO.setObjAlias(dsTagMap.get(dsDO.getId()));
            return levelVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BrowseLevelsVO> listDsIncludeAllEnv(String puid, String uid) {
        List<DmDsDO> dsList = this.dsDal.dsMapper().listByUserWithGmtOrder(puid);
        List<DmDsConfigDO> dsConfList = this.dsDal.configMapper().queryByUid(puid);

        Map<Long, DmDsConfigDO> ruleDOMap = dsConfList.stream().collect(Collectors.toMap(DmDsConfigDO::getDataSourceId, DmDsConfigDO -> DmDsConfigDO));
        dsList.removeIf(next -> !ruleDOMap.containsKey(next.getId()));

        List<Long> dsIds = dsList.stream().map(DmDsDO::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        dsConfList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        dsIds = dsConfList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<DmDsTagDO> dsTags = this.dsDal.tagMapper().listByDsAndUser(dsIds, uid);
        Map<Long, String> dsTagMap = new HashMap<>();
        dsTags.forEach(dsTag -> dsTagMap.put(dsTag.getDataSourceId(), dsTag.getInstanceDesc()));

        return dsList.stream().map(dsDO -> {
            BrowseLevelsVO levelVO = DmConvertUtils.convertToBrowseLevelsVO(dsDO);
            levelVO.setObjAlias(dsTagMap.get(dsDO.getId()));
            return levelVO;
        }).collect(Collectors.toList());
    }

    private Map<Long, DmDsConfigDO> getDataSourceStatusMap(List<DmDsDO> dsList) {
        if (dsList == null || dsList.isEmpty()) {
            return new HashMap<>();
        }
        List<Long> list = dsList.stream().map(dsDO -> dsDO.getId()).collect(Collectors.toList());
        List<DmDsConfigDO> dmDsConfigDOList = dsDal.configMapper().queryByDataSourceIds(list);

        return dmDsConfigDOList.stream().collect(Collectors.toMap(dsDO -> dsDO.getDataSourceId(), dsDO -> dsDO));
    }

    /**
     * for service API '/browse/listLevels'
     */
    @Override
    public List<BrowseLevelsVO> listLevels(String puid, String uid, DsLevels dsLevels, boolean refreshCache) {
        DmDsDO dsDO = dsLevels.dsDO();

        List<UmiTypes> levelsDef = dsLevels.levelsDef();
        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();

        List<DsElement> dsObjects = this.dmDsSchemaService.listLevels(puid, dsDO, levelsDef, levelsParam, refreshCache);
        return dsObjects.stream().map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
    }

    /**
     * for service API '/browse/detailLevels'
     */
    @Override
    public BrowseLevelsVO detailDs(String uid, DsLevels dsLevels) {
        List<Long> searchIds = Collections.singletonList(dsLevels.dsDO().getId());
        List<DmDsDO> dsList = this.dsDal.dsMapper().listByDsIdsAndEnvId(searchIds, dsLevels.envId());
        if (CollectionUtils.isEmpty(dsList)) {
            return null;
        }

        DmDsDO detailDO = dsList.get(0);
        DmDsTagDO dsTags = this.dsDal.tagMapper().getByDsAndUser(detailDO.getId(), uid);
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(detailDO.getId(), detailDO.getDataSourceType());
        RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsConfig.getDataSourceType());
        String dsHost = this.dmDsConfigService.fetchDsConfig(detailDO.getId(), ConfigKeys.DM_DS_KEY_HOST);
        DmDsConfigDO dmDsConfigDO = dsDal.configMapper().queryByDataSourceId(dsLevels.dsDO().getId());
        BrowseLevelsVO levelVO = DmConvertUtils.convertToBrowseLevelsVO(detailDO, dsConfig, dmDsConfigDO, supportSpi, dsHost);

        levelVO.setObjAlias(dsTags != null ? dsTags.getInstanceDesc() : null);
        return levelVO;
    }

    /**
     * for service API '/browse/detailLevels'
     */
    @Override
    public BrowseLevelsVO detailLevels(String puid, String uid, DsLevels levels) {
        DmDsDO dsDO = levels.dsDO();
        List<UmiTypes> levelsDef = levels.levelsDef();
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();

        DsElement dsObject = this.dmDsSchemaService.detailLevel(puid, dsDO, levelsDef, levelsParam);

        return DmConvertUtils.convertToBrowseLevelsVO(dsObject);
    }

    /**
     * for service API '/browse/listLeaf'
     */
    @Override
    public List<BrowseLevelsVO> listLeaf(String puid, String uid, DsLevels levels, UmiTypes leafType, String pattern, boolean refreshCache) {
        DmDsDO dsDO = levels.dsDO();
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();

        List<DsElement> dsObjects = this.dmDsSchemaService.listLeaf(puid, dsDO, levelsParam, leafType, pattern, refreshCache);

        return dsObjects.stream().map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
    }

    /**
     * for service API '/browse/rdbTableDetail'
     */
    @Override
    public BrowseObjectMO rdbObjectDetail(String puid, String uid, DsLevels levels, UmiTypes leafType, String leafName, boolean refreshCache) {
        if (leafType == null || leafName == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TABLE_NAME_IS_EMPTY_ERROR.name()));
        }

        DmDsDO dsDO = levels.dsDO();
        Value value = this.dmDsSchemaService.detailLeaf(uid, dsDO, levels.levelsParam(), leafType, leafName, refreshCache);
        if (value == null) {
            return null;
        }

        BrowseObjectMO mo;
        switch (leafType) {
            case Table:
            case ExternalTable:
            case View:
            case Materialized:
                mo = DmConvertUtils.convertToBrowseTableMO((RdbTable) value);
                break;
            case Procedure:
                mo = DmConvertUtils.convertToBrowseProcedureMo((RdbProcedure) value);
                break;
            case Function:
                mo = DmConvertUtils.convertToBrowseFunctionMo((RdbFunction) value);
                break;
            case Trigger:
                mo = DmConvertUtils.convertToBrowseTriggerMo((RdbTrigger) value);
                break;
            case Key:
                mo = DmConvertUtils.convertToBrowseKeyMo((RdbValue) value);
                break;
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_TYPE_NOT_SUPPORT_ERROR.name(), leafName));
        }

        return mo;
    }

    @Override
    public List<BrowseColumnMO> rdbColumns(String puid, String uid, DsLevels levels, UmiTypes leafType, String leafName) {
        if (leafType == null || leafName == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TABLE_NAME_IS_EMPTY_ERROR.name()));
        }

        DmDsDO dsDO = levels.dsDO();
        Map<String, List<RdbColumn>> value = this.dmDsSchemaService.loadColumns(uid, dsDO, levels.levelsParam(), leafType, Collections.singletonList(leafName));
        if (value == null || value.isEmpty() || !value.containsKey(leafName)) {
            return Collections.emptyList();
        }

        List<RdbColumn> columnList = value.get(leafName);

        return columnList.stream().map(DmConvertUtils::convertToBrowseColumnMOTipsType).collect(Collectors.toList());
    }
}
