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
import com.clougence.clouddm.base.metadata.rdp.enumeration.MySQLSslMode;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.model.MySQLExtraConfig;
import com.clougence.utils.StringUtils;

@Service
public class MySqlExtraConfGen extends CommonExtraConfGen {

    @Override
    public MySQLExtraConfig newDsExtraConfig() {
        return new MySQLExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        MySQLExtraConfig config = newDsExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        MySQLExtraConfig config = new MySQLExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        return config;
    }

    protected void fillEntry(MySQLExtraConfig config, String key, String val) {
        if (key.equals(MySQLExtraConfig.Fields.sslMode) && StringUtils.isNotBlank(val)) {
            if (MySQLSslMode.nameOf(val) != null) {
                config.setSslMode(MySQLSslMode.valueOfCode(val).name());
            }
        } else {
            super.fillEntry(config, key, val);
        }
    }
}
