package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.rdp.enumeration.KafkaSecurityProtocol;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SaslMechanism;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/2/12 16:36
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "saslMechanism", descKey = I18nDsConfigMsgKeys.DS_CONFIG_KAFKA_SASL_MECHANISM, valueAdvance = "PLAIN / SCRAM_SHA_256 / SCRAM_SHA_512", readOnly = false)
    private SaslMechanism         saslMechanism;

    @DsConfigDef(name = "securityProtocol", descKey = I18nDsConfigMsgKeys.DS_CONFIG_KAFKA_SECURITY_PROTOCOL, valueAdvance = "PLAINTEXT / SASL_SSL / SASL_PLAINTEXT", readOnly = false)
    private KafkaSecurityProtocol securityProtocol;

}
