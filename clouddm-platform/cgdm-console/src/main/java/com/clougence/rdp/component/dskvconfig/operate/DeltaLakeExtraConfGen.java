package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DeleteStrategy;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DeltaLakeExtraConfig;
import com.clougence.rdp.component.dskvconfig.util.PropsCryptUtil;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

@Service
public class DeltaLakeExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfigForDefaultVal(DataSourceType dsType) {
        return newDsExtraConfig();
    }

    @Override
    public DeltaLakeExtraConfig newDsExtraConfig() {
        return new DeltaLakeExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> fos) {
        DeltaLakeExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        DeltaLakeExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        config.deserialize();
        validate(dsDO, config);
        return config;
    }

    protected void fillEntry(DeltaLakeExtraConfig config, String key, String val) {
        switch (key) {
            case DeltaLakeExtraConfig.Fields.httpsEnabled:
                config.setHttpsEnabled(Boolean.parseBoolean(val));
                break;
            case DeltaLakeExtraConfig.Fields.metastoreType:
                config.setMetastoreType(val);
                break;
            case DeltaLakeExtraConfig.Fields.warehouse:
                config.setWarehouse(val);
                break;
            case DeltaLakeExtraConfig.Fields.catalogProps:
                String newVal = PropsCryptUtil.encryptSecretPropsToStr(DataSourceType.DeltaLake, val);
                if (StringUtils.isNotBlank(newVal)) {
                    config.setCatalogProps(newVal);
                } else {
                    config.setCatalogProps(val);
                }
                break;
            case DeltaLakeExtraConfig.Fields.dvPrefix:
                config.setDvPrefix(val);
                break;
            case DeltaLakeExtraConfig.Fields.dvStorage:
                config.setDvStorage(val);
                break;
            case DeltaLakeExtraConfig.Fields.deleteStrategy:
                String strategy = DeleteStrategy.valueOfCode(val).name();
                config.setDeleteStrategy(strategy);
                break;
            default:
                break;
        }
    }

    protected void validate(RdpDataSourceDO dsDO, DeltaLakeExtraConfig extraConfig) {
        String metastoreType = extraConfig.getMetastoreType();
        if (StringUtils.isBlank(metastoreType)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config metastoreType can not blank");
        }

        String warehouse = extraConfig.getWarehouse();
        if (StringUtils.isBlank(warehouse)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config warehouse can not blank");
        }

        String deleteStrategy = extraConfig.getDeleteStrategy();
        DeleteStrategy strategy = DeleteStrategy.valueOfCode(deleteStrategy);
        if (strategy == DeleteStrategy.AUTO || strategy == DeleteStrategy.FORCE_DV) {
            String dvStorage = extraConfig.getDvStorage();
            if (StringUtils.isBlank(dvStorage)) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config dvStorage can not blank");
            }
        }
    }
}
