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
package com.clougence.clouddm.ds.kafka.execute;

import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.kafka.dsconf.KafkaConfig;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslFile;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaSessionFactory implements SessionFactory<KafkaConfig> {
    @Override
    public Session createSession(DsResourceManager resources, KafkaConfig config, SessionContextDTO context) {
        DsObject<KafkaClients> resource = null;
        try {
            if (config.getSecurityType() == null || config.getSecurityType() == SecurityType.NONE) {
                config.setUserName(null);
                config.setPassword(null);
            }
            if (config.getSslMode() != null && config.getSslMode() != SslMode.DISABLED) {
                resolveSsl(resources, config);
            }
            resource = resources.requestResource(config);
            return new KafkaSession(context, config, resource);
        } catch (Exception e) {
            if (resource != null) {
                resource.close();
            }
            String msg = "Create Kafka session failed";
            log.error(msg, e);
            if (e instanceof ThirdPartyApiException apiException) {
                throw apiException;
            }
            throw ThirdPartyApiException.as().with(e);
        }
    }

    private void resolveSsl(DsResourceManager resources, KafkaConfig config) throws Exception {
        SslConfig ssl = resources.fetchSslConfig(config);
        if (ssl == null) {
            throw ThirdPartyApiException.as().with(KafkaConfigI18nKeys.CONFIG_KAFKA_SSL_FILES_ERROR);
        }
        config.setSslCaFilePath(null);
        config.setSslCaFileFormat(null);
        config.setSslClientCertFilePath(null);
        config.setSslClientCertFileFormat(null);
        config.setSslClientKeyFilePath(null);
        config.setSslClientKeyFileFormat(null);
        SslFile ca = ssl.getCaFile();
        if (ca != null && ca.getFile() != null) {
            config.setSslCaFilePath(ca.getFile().getAbsolutePath());
            config.setSslCaFileFormat(ca.getFormat());
        }
        SslFile cert = ssl.getClientCertFile();
        if (cert != null && cert.getFile() != null) {
            config.setSslClientCertFilePath(cert.getFile().getAbsolutePath());
            config.setSslClientCertFileFormat(cert.getFormat());
        }
        SslFile key = ssl.getClientKeyFile();
        if (key != null && key.getFile() != null) {
            config.setSslClientKeyFilePath(key.getFile().getAbsolutePath());
            config.setSslClientKeyFileFormat(key.getFormat());
        }
        // KafkaDsFactory validates the resolved files required by each TLS mode.
    }
}
