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
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.RedisExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public RedisExtraConfig newDsExtraConfig() {
        return new RedisExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        RedisExtraConfig config = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }
        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(config);
        }
        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        RedisExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(RedisExtraConfig config, String key, String val) {
        if (key.equals(RedisExtraConfig.Fields.isSentinel)) {
            if (StringUtils.isNotBlank(val)) {
                config.setIsSentinel(Boolean.valueOf(val));
            }
        } else if (key.equals(RedisExtraConfig.Fields.sentinelMasterName)) {
            config.setSentinelMasterName(val);
        } else if (key.equals(RedisExtraConfig.Fields.sentinelUser)) {
            config.setSentinelUser(val);
        } else if (key.equals(RedisExtraConfig.Fields.sentinelPassword)) {
            config.setSentinelPassword(val);
        } else if (key.equals(RedisExtraConfig.Fields.useTLS)) {
            config.setUseTLS(Boolean.parseBoolean(val));
        }
    }

    protected void validate(RedisExtraConfig extraConfig) {
        if (extraConfig.getIsSentinel() != null && extraConfig.getIsSentinel()) {
            if (StringUtils.isBlank(extraConfig.getSentinelMasterName())) {
                throw new IllegalArgumentException("DataSource config isSentinel is true, but sentinelMasterName is blank!");
            }
        }
    }
}
