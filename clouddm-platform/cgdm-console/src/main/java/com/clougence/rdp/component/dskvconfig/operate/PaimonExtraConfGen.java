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

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DataLakeFormationExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.PaimonExtraConfig;
import com.clougence.rdp.component.dskvconfig.util.PropsCryptUtil;
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
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> fos) {
        PaimonExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
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

    protected void validate(DmDsDO dsDO, PaimonExtraConfig extraConfig) {
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
