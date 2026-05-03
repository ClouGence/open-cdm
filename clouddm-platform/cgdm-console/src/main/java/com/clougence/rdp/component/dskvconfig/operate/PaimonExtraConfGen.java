package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DataLakeFormationExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.PaimonExtraConfig;
import com.clougence.rdp.component.dskvconfig.util.PropsCryptUtil;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

@Service
public class PaimonExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfigForDefaultVal(DataSourceType dsType) {
        if (dsType == DataSourceType.DataLakeFormation) {
            // default val is not same with paimon
            return new DataLakeFormationExtraConfig();
        } else {
            return newDsExtraConfig();
        }
    }

    @Override
    public PaimonExtraConfig newDsExtraConfig() {
        return new PaimonExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> fos) {
        PaimonExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        PaimonExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        config.deserialize();
        validate(dsDO, config);
        return config;
    }

    protected void fillEntry(PaimonExtraConfig config, String key, String val) {
        switch (key) {
            case PaimonExtraConfig.Fields.httpsEnabled:
                config.setHttpsEnabled(Boolean.parseBoolean(val));
                break;
            case PaimonExtraConfig.Fields.metastoreType:
                config.setMetastoreType(val);
                break;
            case PaimonExtraConfig.Fields.warehouse:
                config.setWarehouse(val);
                break;
            case PaimonExtraConfig.Fields.catalogProps:
                String newVal = PropsCryptUtil.encryptSecretPropsToStr(DataSourceType.Paimon, val);
                if (StringUtils.isNotBlank(newVal)) {
                    config.setCatalogProps(newVal);
                } else {
                    config.setCatalogProps(val);
                }
                break;
            default:
                break;
        }
    }

    protected void validate(RdpDataSourceDO dsDO, PaimonExtraConfig extraConfig) {
        String metastoreType = extraConfig.getMetastoreType();
        if (StringUtils.isBlank(metastoreType)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config metastoreType can not blank");
        }

        String warehouse = extraConfig.getWarehouse();
        if (StringUtils.isBlank(warehouse)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config warehouse can not blank");
        }
    }
}
