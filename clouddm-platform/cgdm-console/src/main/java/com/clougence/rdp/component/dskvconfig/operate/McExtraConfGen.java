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

import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.McExtraConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class McExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public McExtraConfig newDsExtraConfig() {
        return new McExtraConfig();
    }

    @Override
    public McExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        McExtraConfig extraConfig = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    @Override
    public McExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        McExtraConfig extraConfig = newDsExtraConfig();

        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    protected void fillEntry(McExtraConfig config, String key, String val) {
        if (key.equals(McExtraConfig.Fields.sdkEndpoint)) {
            config.setSdkEndpoint(val);
        }
        if (key.equals(McExtraConfig.Fields.schemaStyle)) {
            config.setSchemaStyle(StringUtils.isNotBlank(val) && Boolean.parseBoolean(val));
        }
    }
}
