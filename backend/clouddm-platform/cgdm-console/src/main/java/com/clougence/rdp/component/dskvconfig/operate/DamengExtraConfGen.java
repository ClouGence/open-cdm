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

import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.DamengExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DamengExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public DamengExtraConfig newDsExtraConfig() {
        return new DamengExtraConfig();
    }

    @Override
    public DamengExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        DamengExtraConfig extraConfig = newDsExtraConfig();

        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(extraConfig);
        }

        return extraConfig;
    }

    @Override
    public DamengExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        DamengExtraConfig extraConfig = newDsExtraConfig();

        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(extraConfig, f.getConfigName(), f.getConfigValue());
        }

        return extraConfig;
    }

    protected void fillEntry(DamengExtraConfig config, String key, String val) {
        if (key.equals(DamengExtraConfig.Fields.isDscNode)) {
            config.setIsDscNode(Boolean.parseBoolean(val));
        }

        if (key.equals(DamengExtraConfig.Fields.dscHosts)) {
            config.setDscHosts(val);
        }

        if (key.equals(DamengExtraConfig.Fields.dscSyncLsnTable)) {
            config.setDscSyncLsnTable(val);
        }
    }

    protected void validate(DamengExtraConfig extraConfig) {
        if (extraConfig.getIsDscNode() != null && extraConfig.getIsDscNode()) {
            if (StringUtils.isBlank(extraConfig.getDscHosts())) {
                throw new IllegalArgumentException("Dameng extra config dscHosts can not blank");
            }

            if (StringUtils.isBlank(extraConfig.getDscSyncLsnTable())) {
                throw new IllegalArgumentException("Dameng extra config dscSyncLsnTable can not blank");
            }
        }
    }
}
