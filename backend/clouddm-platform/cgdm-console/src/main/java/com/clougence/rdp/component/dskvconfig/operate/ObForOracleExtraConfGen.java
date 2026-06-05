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
import com.clougence.rdp.component.dskvconfig.model.ObForOracleExtraConf;
import com.clougence.rdp.component.dskvconfig.model.OceanBaseExtraConfig;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chunlin create time is 2024/8/28
 */
@Service
@Slf4j
public class ObForOracleExtraConfGen implements RdpDsExtraConfGen {

    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public ObForOracleExtraConf newDsExtraConfig() {
        return new ObForOracleExtraConf();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        ObForOracleExtraConf config = new ObForOracleExtraConf();
        for (InitDsKvBaseConfigFO f : fos) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        // necessary
        if (StringUtils.isBlank(config.getTenant())) {
            throw new IllegalArgumentException("The parameter tenant can not be blank.");
        }

        if (rdpConfig.getRdpDsConfigValidateEnable()) {
            validate(config);
        }

        return config;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        ObForOracleExtraConf config = newDsExtraConfig();
        for (DmDsConfigKv4RdpDO f : confs) {
            fillEntry(config, f.getConfigName(), f.getConfigValue());
        }

        return config;
    }

    protected void fillEntry(ObForOracleExtraConf config, String key, String val) {
        if (key.equals(OceanBaseExtraConfig.Fields.obLogProxyHost)) {
            config.setObLogProxyHost(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.clusterUrl)) {
            config.setClusterUrl(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.rpcPortList)) {
            config.setRpcPortList(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.syncAccount)) {
            config.setSyncAccount(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.syncPwd)) {
            config.setSyncPwd(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.tenant)) {
            config.setTenant(val);
        } else if (key.equals(OceanBaseExtraConfig.Fields.clusterName)) {
            config.setClusterName(val);
        }
    }

    protected void validate(ObForOracleExtraConf config) {
        if (StringUtils.isBlank(config.getObLogProxyHost())) {
            throw new IllegalArgumentException("The parameter obLogProxyHost can not be blank.");
        }

        if (StringUtils.isBlank(config.getRpcPortList())) {
            throw new IllegalArgumentException("The parameter rpcPortList can not be blank.");
        }

    }
}
