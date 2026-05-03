package com.clougence.rdp.component.dskvconfig.operate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.KafkaSecurityProtocol;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SaslMechanism;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.component.dskvconfig.RdpDsExtraConfGen;
import com.clougence.rdp.component.dskvconfig.model.KafkaExtraConfig;
import com.clougence.rdp.controller.model.fo.InitDsKvBaseConfigFO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;
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
    public DsExtraConfig genDsExtraConfig(RdpDataSourceDO dsDO, List<InitDsKvBaseConfigFO> fos) {
        KafkaExtraConfig kafkaExtraConfig = new KafkaExtraConfig();
        if (dsDO.getSecurityType() == SecurityType.USER_PASSWD_WITH_SCRAM) {
            fos.forEach(fo -> fillEntry(kafkaExtraConfig, fo.getConfigName(), fo.getConfigValue()));
        }

        return kafkaExtraConfig;
    }

    @Override
    public DsExtraConfig genDsExtraConfigFromExist(RdpDataSourceDO dsDO, List<RdpDsKvBaseConfigDO> confs) {
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
