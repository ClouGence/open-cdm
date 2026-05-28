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
import com.clougence.rdp.component.dskvconfig.model.TiDBExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2022/11/29
 **/
@Service
@Slf4j
public class TiDbExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public TiDBExtraConfig newDsExtraConfig() {
        return new TiDBExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        TiDBExtraConfig config = new TiDBExtraConfig();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(dsDO, config);
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        TiDBExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(TiDBExtraConfig config, String key, String val) {
        if (key.equals(TiDBExtraConfig.Fields.pdHost)) {
            config.setPdHost(val);
        }
    }

    protected void validate(DmDsDO dsDo, TiDBExtraConfig extraConfig) {
        String pdHost = extraConfig.getPdHost();
        if (StringUtils.isBlank(pdHost)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " datasource extra config pdHost can not blank");
        }

        String[] hosts = StringUtils.split(pdHost, ",");
        for (String host : hosts) {
            if (StringUtils.split(host, ":").length != 2) {
                throw new IllegalArgumentException(dsDo.getDataSourceType() + " datasource extra config pdHost must be in the ip:port,ip:port,... format");
            }
        }
    }
}
