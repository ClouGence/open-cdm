package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.CatalogType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.IcebergExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
import com.clougence.utils.StringUtils;

@Service
public class IcebergExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public IcebergExtraConfig newDsExtraConfig() {
        return new IcebergExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> fos) {
        IcebergExtraConfig config = newDsExtraConfig();
        for (RdpDsKvBaseConfigDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        IcebergExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        validate(dsDO, config);
        config.deserialize();
        return config;
    }

    protected void fillEntry(IcebergExtraConfig config, String key, String val) {
        switch (key) {
            case IcebergExtraConfig.Fields.httpsEnabled:
                config.setHttpsEnabled(Boolean.parseBoolean(val));
                break;
            case IcebergExtraConfig.Fields.catalogName:
                config.setCatalogName(val);
                break;
            case IcebergExtraConfig.Fields.catalogType:
                config.setCatalogType(val);
                break;
            case IcebergExtraConfig.Fields.catalogWarehouse:
                config.setCatalogWarehouse(val);
                break;
            case IcebergExtraConfig.Fields.catalogProps:
                config.setCatalogProps(val);
                break;
            default:
                break;
        }
    }

    protected void validate(RdpDataSourceDO dsDO, IcebergExtraConfig extraConfig) {
        String catalogName = extraConfig.getCatalogName();
        if (StringUtils.isBlank(catalogName)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config catalogName can not blank");
        }

        String catalogType = extraConfig.getCatalogType();
        if (StringUtils.isBlank(catalogType)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config catalogType can not blank");
        }

        if (CatalogType.valueOfCode(catalogType) == null) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config catalogType can not set:" + catalogType);
        }

        if (StringUtils.isBlank(extraConfig.getCatalogWarehouse())) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config catalogWarehouse can not blank");
        }
    }
}
