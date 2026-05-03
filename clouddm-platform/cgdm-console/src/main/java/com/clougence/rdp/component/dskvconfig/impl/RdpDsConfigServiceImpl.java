package com.clougence.rdp.component.dskvconfig.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsConfigService;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.RdpDsKvConfigHelper;
import com.clougence.rdp.component.dskvconfig.RdpDsResourceService;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.controller.model.vo.DsKvConfigVO;
import com.clougence.rdp.dal.mapper.RdpDsKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2022/8/10 09:52:22
 */
@Service
public class RdpDsConfigServiceImpl implements RdpDsConfigService {

    @Resource
    private RdpDsKvBaseConfigMapper rdpDsKvBaseConfigMapper;

    @Resource
    private RdpDsKvConfigHelper     rdpDsKvConfigHelper;

    @Resource
    private RdpDsResourceService    rdpDsResourceService;

    @Override
    public List<DsKvConfigVO> getAllConfig(long dataSourceId) {
        List<RdpDsKvBaseConfigDO> configs = this.rdpDsKvBaseConfigMapper.listByDsId(dataSourceId);
        for (RdpDsKvBaseConfigDO configDO : configs) {
            if (configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
                String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
                configDO.setConfigValue(val);
            }
        }

        List<DsKvConfigVO> dsKvConfigs = new ArrayList<>();
        for (RdpDsKvBaseConfigDO config : configs) {
            dsKvConfigs.add(RdpConvertUtils.convertToDsKvConfigVO(config));
        }
        return dsKvConfigs;
    }

    @Override
    public void persistDsConfig(RdpDataSourceDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs) {
        List<RdpDsKvBaseConfigDO> configs = collectConfig(dataSourceDO, kvConfigs);
        for (RdpDsKvBaseConfigDO config : configs) {
            if (config.isSecret()) {
                config.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(config.getConfigValue()));
            }

            this.rdpDsKvBaseConfigMapper.insert(config);
        }
    }

    @Override
    public void persistInnerDsConfig(RdpDataSourceDO dataSourceDO, List<RdpDsKvBaseConfigDO> kvConfigs) {
        for (RdpDsKvBaseConfigDO config : kvConfigs) {
            if (config.isSecret()) {
                config.setConfigValue(CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(config.getConfigValue()));
            }

            this.rdpDsKvBaseConfigMapper.insert(config);
        }
    }

    @Override
    public List<RdpDsKvBaseConfigDO> fetchDefaultConfig(long dataSourceId, DataSourceType dataSourceType) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceType);
        if (dsConfigOperate == null) {
            return new ArrayList<>();
        }

        DsExtraConfig extraConfig = dsConfigOperate.newDsExtraConfigForDefaultVal(dataSourceType);
        return this.rdpDsKvConfigHelper.collectConfigs(extraConfig, dataSourceId, dataSourceType);
    }

    @Override
    public DsExtraConfig fetchDsExtraConfig(long dataSourceId, DataSourceType dataSourceType) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceType);
        if (dsConfigOperate == null) {
            return null;
        }

        List<RdpDsKvBaseConfigDO> source = this.rdpDsKvBaseConfigMapper.listByDsId(dataSourceId);

        Map<String, String> configMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(source)) {
            for (RdpDsKvBaseConfigDO configDO : source) {
                configMap.put(configDO.getConfigName(), configDO.getConfigValue());
            }
        }

        DsExtraConfig config = dsConfigOperate.newDsExtraConfig();
        this.rdpDsKvConfigHelper.fillFieldValue(config, configMap);
        return config;
    }

    @Override
    public RdpDsKvBaseConfigDO getSpecifiedConfig(long dataSourceId, String configName) {
        RdpDsKvBaseConfigDO configDO = this.rdpDsKvBaseConfigMapper.queryByDsIdAndConfigName(dataSourceId, configName);
        if (configDO != null && configDO.isSecret() && StringUtils.isNotBlank(configDO.getConfigValue())) {
            String val = CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(configDO.getConfigValue());
            configDO.setConfigValue(val);
        }

        return configDO;
    }

    @Override
    public void cleanDsConfig(long dataSourceId) {
        this.rdpDsKvBaseConfigMapper.deleteDsConfigs(dataSourceId);
    }

    protected List<RdpDsKvBaseConfigDO> collectConfig(RdpDataSourceDO dataSourceDO, List<InitDsKvBaseConfigFO> kvConfigs) {
        RdpDsExtraConfGen dsConfigOperate = this.rdpDsResourceService.getDsExtraConfGen(dataSourceDO.getDataSourceType());
        if (dsConfigOperate == null) {
            return new ArrayList<>();
        }

        DsExtraConfig extraConfig = dsConfigOperate.genDsExtraConfig(dataSourceDO, kvConfigs);
        return this.rdpDsKvConfigHelper.collectConfigs(extraConfig, dataSourceDO.getId(), dataSourceDO.getDataSourceType());
    }
}
