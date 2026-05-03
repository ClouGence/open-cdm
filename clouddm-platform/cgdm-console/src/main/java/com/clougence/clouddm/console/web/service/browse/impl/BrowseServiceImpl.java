package com.clougence.clouddm.console.web.service.browse.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ConfigKeys;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmDsConfigMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmDsTagMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.dal.model.DmDsTagDO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseObjectMO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.rdp.dal.mapper.RdpDataSourceMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@Slf4j
@Service
public class BrowseServiceImpl implements BrowseService {

    @Resource
    private RdpDataSourceMapper rdpDsMapper;
    @Resource
    private DmDsService         dmDsService;
    @Resource
    private DmDsTagMapper       dmDsTagMapper;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private DsSchemaService     dmDsSchemaService;
    @Resource
    private DmDsConfigMapper    dmDsConfigMapper;

    /**
     * for service API '/browse/listLevels'
     */
    @Override
    public List<BrowseLevelsVO> listDs(String puid, String uid, String envId) {
        List<Long> dsIds = this.rdpDsMapper.listByUidAndEnvId(puid, envId).stream().map(RdpDataSourceDO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<DmDsConfigDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        dsIds = confList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<RdpDataSourceDO> dsList = this.rdpDsMapper.listByDsIdsAndEnvId(dsIds, envId);
        List<DmDsTagDO> dsTags = this.dmDsTagMapper.listByDsAndUser(dsIds, uid);
        Map<Long, String> dsTagMap = new HashMap<>();
        dsTags.forEach(dsTag -> dsTagMap.put(dsTag.getDataSourceId(), dsTag.getInstanceDesc()));

        // convert
        final Map<Long, DataSourceConfig> dsConfigMap = new HashMap<>();
        final Map<Long, String> dsHostMap = new HashMap<>();
        final Map<DataSourceType, RdbSupportSpi> dsRdbSupportMap = new HashMap<>();
        for (RdpDataSourceDO dsDO : dsList) {
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
        List<RdpDataSourceDO> dsList = this.rdpDsMapper.listByUserWithGmtOrder(puid);
        List<DmDsConfigDO> dsConfList = this.dmDsConfigMapper.queryByUid(puid);

        Map<Long, DmDsConfigDO> ruleDOMap = dsConfList.stream().collect(Collectors.toMap(DmDsConfigDO::getDataSourceId, DmDsConfigDO -> DmDsConfigDO));
        dsList.removeIf(next -> !ruleDOMap.containsKey(next.getId()));

        List<Long> dsIds = dsList.stream().map(RdpDataSourceDO::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        dsConfList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        dsIds = dsConfList.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            return Collections.emptyList();
        }

        List<DmDsTagDO> dsTags = this.dmDsTagMapper.listByDsAndUser(dsIds, uid);
        Map<Long, String> dsTagMap = new HashMap<>();
        dsTags.forEach(dsTag -> dsTagMap.put(dsTag.getDataSourceId(), dsTag.getInstanceDesc()));

        return dsList.stream().map(dsDO -> {
            BrowseLevelsVO levelVO = DmConvertUtils.convertToBrowseLevelsVO(dsDO);
            levelVO.setObjAlias(dsTagMap.get(dsDO.getId()));
            return levelVO;
        }).collect(Collectors.toList());
    }

    private Map<Long, DmDsConfigDO> getDataSourceStatusMap(List<RdpDataSourceDO> dsList) {
        if (dsList == null || dsList.isEmpty()) {
            return new HashMap<>();
        }
        List<Long> list = dsList.stream().map(dsDO -> dsDO.getId()).collect(Collectors.toList());
        List<DmDsConfigDO> dmDsConfigDOList = dmDsConfigMapper.queryByDataSourceIds(list);

        return dmDsConfigDOList.stream().collect(Collectors.toMap(dsDO -> dsDO.getDataSourceId(), dsDO -> dsDO));
    }

    /**
     * for service API '/browse/listLevels'
     */
    @Override
    public List<BrowseLevelsVO> listLevels(String puid, String uid, DsLevels dsLevels, boolean refreshCache) {
        RdpDataSourceDO dsDO = dsLevels.getDsDO();

        List<UmiTypes> levelsDef = dsLevels.getLevelsDef();
        Map<UmiTypes, Object> levelsParam = dsLevels.getLevelsParam();

        List<DsElement> dsObjects = this.dmDsSchemaService.listLevels(puid, dsDO, levelsDef, levelsParam, refreshCache);
        return dsObjects.stream().map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
    }

    /**
     * for service API '/browse/detailLevels'
     */
    @Override
    public BrowseLevelsVO detailDs(String uid, DsLevels dsLevels) {
        List<Long> searchIds = Collections.singletonList(dsLevels.getDsDO().getId());
        List<RdpDataSourceDO> dsList = this.rdpDsMapper.listByDsIdsAndEnvId(searchIds, dsLevels.getEnvId());
        if (CollectionUtils.isEmpty(dsList)) {
            return null;
        }

        RdpDataSourceDO detailDO = dsList.get(0);
        DmDsTagDO dsTags = this.dmDsTagMapper.getByDsAndUser(detailDO.getId(), uid);
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(detailDO.getId(), detailDO.getDataSourceType());
        RdbSupportSpi supportSpi = PluginManager.findRdbSupportSpi(dsConfig.getDataSourceType());
        String dsHost = this.dmDsConfigService.fetchDsConfig(detailDO.getId(), ConfigKeys.DM_DS_KEY_HOST);
        DmDsConfigDO dmDsConfigDO = dmDsConfigMapper.queryByDataSourceId(dsLevels.getDsDO().getId());
        BrowseLevelsVO levelVO = DmConvertUtils.convertToBrowseLevelsVO(detailDO, dsConfig, dmDsConfigDO, supportSpi, dsHost);

        levelVO.setObjAlias(dsTags != null ? dsTags.getInstanceDesc() : null);
        return levelVO;
    }

    /**
     * for service API '/browse/detailLevels'
     */
    @Override
    public BrowseLevelsVO detailLevels(String puid, String uid, DsLevels levels) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        List<UmiTypes> levelsDef = levels.getLevelsDef();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();

        DsElement dsObject = this.dmDsSchemaService.detailLevel(puid, dsDO, levelsDef, levelsParam);

        return DmConvertUtils.convertToBrowseLevelsVO(dsObject);
    }

    /**
     * for service API '/browse/listLeaf'
     */
    @Override
    public List<BrowseLevelsVO> listLeaf(String puid, String uid, DsLevels levels, UmiTypes leafType, String pattern, boolean refreshCache) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();

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

        RdpDataSourceDO dsDO = levels.getDsDO();
        Value value = this.dmDsSchemaService.detailLeaf(uid, dsDO, levels.getLevelsParam(), leafType, leafName, refreshCache);
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

        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<String, List<RdbColumn>> value = this.dmDsSchemaService.loadColumns(uid, dsDO, levels.getLevelsParam(), leafType, Collections.singletonList(leafName));
        if (value == null || value.isEmpty() || !value.containsKey(leafName)) {
            return Collections.emptyList();
        }

        List<RdbColumn> columnList = value.get(leafName);

        return columnList.stream().map(DmConvertUtils::convertToBrowseColumnMOTipsType).collect(Collectors.toList());
    }
}
