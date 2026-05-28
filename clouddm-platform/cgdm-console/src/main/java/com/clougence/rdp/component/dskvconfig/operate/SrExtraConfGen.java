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
import com.clougence.rdp.component.dskvconfig.model.StarRocksExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SrExtraConfGen extends CommonExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public StarRocksExtraConfig newDsExtraConfig() {
        return new StarRocksExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        StarRocksExtraConfig config = newDsExtraConfig();

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
        StarRocksExtraConfig config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(StarRocksExtraConfig config, String key, String val) {
        if (key.equals(StarRocksExtraConfig.Fields.publicHttpHost)) {
            config.setPublicHttpHost(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.privateHttpHost)) {
            config.setPrivateHttpHost(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.feHttpAddr)) {
            config.setFeHttpAddr(val);
        } else if (key.equals(StarRocksExtraConfig.Fields.beThriftAddr)) {
            config.setBeThriftAddr(val);
        } else {
            super.fillEntry(config, key, val);
        }
    }

    protected void validate(DmDsDO dsDO, StarRocksExtraConfig extraConfig) {
        if (StringUtils.isNotBlank(dsDO.getPublicHost())) {
            String publicHttpHost = extraConfig.getPublicHttpHost();
            if (StringUtils.isBlank(publicHttpHost)) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config publicHttpHost can not blank");
            }

            String[] host = publicHttpHost.split(":");
            if (host.length != 2) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config publicHttpHost must be in the ip:port format");
            }
        }

        if (StringUtils.isNotBlank(dsDO.getPrivateHost())) {
            String privateHttpHost = extraConfig.getPrivateHttpHost();
            if (StringUtils.isBlank(privateHttpHost)) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config privateHttpHost can not blank");
            }

            String[] host = privateHttpHost.split(":");
            if (host.length != 2) {
                throw new IllegalArgumentException(dsDO.getDataSourceType() + " datasource extra config privateHttpHost must be in the ip:port format");
            }
        }
    }
}
