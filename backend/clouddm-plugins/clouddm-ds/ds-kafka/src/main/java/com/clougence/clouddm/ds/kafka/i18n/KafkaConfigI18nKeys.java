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
package com.clougence.clouddm.ds.kafka.i18n;

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/kafka-config")
public interface KafkaConfigI18nKeys {

    String CONFIG_KAFKA_BROKERS_LABEL         = "CONFIG_KAFKA_BROKERS_LABEL";
    String CONFIG_KAFKA_BROKERS_DESC          = "CONFIG_KAFKA_BROKERS_DESC";
    String CONFIG_KAFKA_SASL_LABEL            = "CONFIG_KAFKA_SASL_LABEL";
    String CONFIG_KAFKA_SASL_DESC             = "CONFIG_KAFKA_SASL_DESC";
    String CONFIG_KAFKA_PLAIN_LABEL           = "CONFIG_KAFKA_PLAIN_LABEL";
    String CONFIG_KAFKA_SCRAM256_LABEL        = "CONFIG_KAFKA_SCRAM256_LABEL";
    String CONFIG_KAFKA_SCRAM512_LABEL        = "CONFIG_KAFKA_SCRAM512_LABEL";
    String CONFIG_KAFKA_CONNECT_TIMEOUT_LABEL = "CONFIG_KAFKA_CONNECT_TIMEOUT_LABEL";
    String CONFIG_KAFKA_CONNECT_TIMEOUT_DESC  = "CONFIG_KAFKA_CONNECT_TIMEOUT_DESC";
    String CONFIG_KAFKA_REQUEST_TIMEOUT_LABEL = "CONFIG_KAFKA_REQUEST_TIMEOUT_LABEL";
    String CONFIG_KAFKA_REQUEST_TIMEOUT_DESC  = "CONFIG_KAFKA_REQUEST_TIMEOUT_DESC";
    String CONFIG_KAFKA_API_TIMEOUT_LABEL     = "CONFIG_KAFKA_API_TIMEOUT_LABEL";
    String CONFIG_KAFKA_API_TIMEOUT_DESC      = "CONFIG_KAFKA_API_TIMEOUT_DESC";
    String CONFIG_KAFKA_BROKERS_ERROR         = "CONFIG_KAFKA_BROKERS_ERROR";
    String CONFIG_KAFKA_TIMEOUT_ERROR         = "CONFIG_KAFKA_TIMEOUT_ERROR";
    String CONFIG_KAFKA_SASL_ERROR            = "CONFIG_KAFKA_SASL_ERROR";
    String CONFIG_KAFKA_SECURITY_ERROR        = "CONFIG_KAFKA_SECURITY_ERROR";
    String CONFIG_KAFKA_CREDENTIALS_ERROR     = "CONFIG_KAFKA_CREDENTIALS_ERROR";
    String CONFIG_KAFKA_SSL_ERROR             = "CONFIG_KAFKA_SSL_ERROR";
    String CONFIG_KAFKA_SSL_FILES_ERROR       = "CONFIG_KAFKA_SSL_FILES_ERROR";
    String CONFIG_KAFKA_SSH_UNSUPPORTED       = "CONFIG_KAFKA_SSH_UNSUPPORTED";
    String CONFIG_KAFKA_DECODE_ERROR          = "CONFIG_KAFKA_DECODE_ERROR";
    String CONFIG_KAFKA_CLIENTS_CLOSED        = "CONFIG_KAFKA_CLIENTS_CLOSED";

}
