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
package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.CatalogType;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.IcebergExtraConfig;
import com.clougence.utils.StringUtils;

@Service
public class IcebergExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public IcebergExtraConfig newDsExtraConfig() {
        return new IcebergExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> fos) {
        IcebergExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
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

    protected void validate(DmDsDO dsDO, IcebergExtraConfig extraConfig) {
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
