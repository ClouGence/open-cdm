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
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.HiveExtraConfig;
import com.clougence.utils.StringUtils;

@Service
public class HiveExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public HiveExtraConfig newDsExtraConfig() {
        return new HiveExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> fos) {
        HiveExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        HiveExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        validate(dsDO, config);
        return config;
    }

    protected void fillEntry(HiveExtraConfig config, String key, String val) {
        switch (key) {
            case HiveExtraConfig.Fields.defaultFS:
                config.setDefaultFS(val);
                break;
            case HiveExtraConfig.Fields.fsDir:
                config.setFsDir(val);
                break;
            case HiveExtraConfig.Fields.hdfsPrincipal:
                config.setHdfsPrincipal(val);
                break;
            case HiveExtraConfig.Fields.hadoopUser:
                config.setHadoopUser(val);
                break;
            default:
                break;
        }
    }

    protected void validate(DmDsDO dsDO, HiveExtraConfig extraConfig) {
        String defaultFS = extraConfig.getDefaultFS();
        if (StringUtils.isBlank(defaultFS)) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config defaultFS can not blank");
        }

        String[] host = defaultFS.split(":");
        if (host.length != 2) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config defaultFS must be in the ip:port format");
        }

        if (StringUtils.isBlank(extraConfig.getHadoopUser())) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config fsDir can not blank");
        }

        if (StringUtils.isBlank(extraConfig.getFsDir())) {
            throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config hadoopUser can not blank");
        }

    }
}
