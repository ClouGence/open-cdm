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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.KafkaSecurityProtocol;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SaslMechanism;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
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
