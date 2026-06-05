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
import com.clougence.clouddm.base.metadata.rdp.enumeration.KafkaSecurityProtocol;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SaslMechanism;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4RdpDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.KafkaExtraConfig;
import com.clougence.utils.StringUtils;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/2/12 16:35
 */
@Service
public class KafkaExtraConfGen implements RdpDsExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new KafkaExtraConfig();
    }

    @Override
    public DsExtraConfig genDsExtraConfig(DmDsDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        KafkaExtraConfig kafkaExtraConfig = new KafkaExtraConfig();
        if (dsDO.getSecurityType() == SecurityType.USER_PASSWD_WITH_SCRAM) {
            fos.forEach(fo -> fillEntry(kafkaExtraConfig, fo.getConfigName(), fo.getConfigValue()));
        }

        return kafkaExtraConfig;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(DmDsDO dsDO, List<DmDsConfigKv4RdpDO> confs) {
        KafkaExtraConfig kafkaExtraConfig = new KafkaExtraConfig();
        if (dsDO.getSecurityType() == SecurityType.USER_PASSWD_WITH_SCRAM) {
            confs.forEach(fo -> fillEntry(kafkaExtraConfig, fo.getConfigName(), fo.getConfigValue()));
        }

        return kafkaExtraConfig;
    }

    private void fillEntry(KafkaExtraConfig kafkaExtraConfig, String configName, String configValue) {
        if (StringUtils.isBlank(configValue)) {
            return;
        }

        configValue = configValue.trim();

        if (configName.equals(KafkaExtraConfig.Fields.saslMechanism)) {
            kafkaExtraConfig.setSaslMechanism(SaslMechanism.valueOfCode(configValue));
        } else if (configName.equals(KafkaExtraConfig.Fields.securityProtocol)) {
            kafkaExtraConfig.setSecurityProtocol(KafkaSecurityProtocol.valueOfCode(configValue));
        }
    }
}
