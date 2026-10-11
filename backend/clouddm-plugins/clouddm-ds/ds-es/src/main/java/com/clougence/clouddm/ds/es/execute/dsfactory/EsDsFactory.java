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
package com.clougence.clouddm.ds.es.execute.dsfactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.es.dsconf.EsConfig;
import com.clougence.clouddm.ds.es.dsconf.EsConfigSpi;
import com.clougence.clouddm.ds.es.dsconf.EsEndpoints;
import com.clougence.clouddm.ds.es.execute.EsClient;
import com.clougence.clouddm.ds.es.execute.EsSingleAttemptNodeSelector;
import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EsDsFactory implements DsFactory<EsClient> {
    @Override
    public DsObject<EsClient> create(Properties properties) {
        EsClient client = null;
        try {
            EsConfig config = new EsConfig();
            config.setHost(properties.getProperty(DsConfigKeys.HOST.getConfigKey()));
            config.setSecurityType(SecurityType.valueOf(properties.getProperty(DataSourceConfig.Fields.securityType, "NONE")));
            config.setUserName(properties.getProperty(DsConfigKeys.USER.getConfigKey()));
            config.setPassword(properties.getProperty(DsConfigKeys.PASSWORD.getConfigKey()));
            config.setSslMode(SslMode.valueOf(properties.getProperty(DsConfigKeys.SSL_MODE.getConfigKey(), "DISABLED")));
            Map<String, String> values = new HashMap<>();
            properties.stringPropertyNames().forEach(name -> values.put(name, properties.getProperty(name)));
            new EsConfigSpi().fillConfig(config, values);
            config.validate();

            HttpHost[] hosts = EsEndpoints.parse(config.getHost()).stream().map(uri -> {
                int port = uri.getPort();
                if (port == -1) {
                    port = 9200;
                }
                return new HttpHost(uri.getHost(), port, uri.getScheme());
            }).toArray(HttpHost[]::new);

            var builder = RestClient.builder(hosts);
            builder.setNodeSelector(new EsSingleAttemptNodeSelector());
            builder.setRequestConfigCallback(request -> request.setConnectTimeout(config.getConnectTimeoutMs())
                .setSocketTimeout(config.getSocketTimeoutMs())
                .setConnectionRequestTimeout(config.getConnectionRequestTimeoutMs())
                .setRedirectsEnabled(false));
            builder.setHttpClientConfigCallback(http -> {
                http.setMaxConnTotal(config.getMaxConnections()).setMaxConnPerRoute(config.getMaxConnectionsPerRoute());
                // The reactor's default one-second sweep would ignore sub-second read/pool deadlines.
                long selectInterval = Math.min(1000, Math.min(config.getSocketTimeoutMs(), config.getConnectionRequestTimeoutMs()));
                http.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(2).setSelectInterval(selectInterval).build());
                // TLS is configured before RestClient starts its IO reactor.
                try {
                    EsSslFactory.apply(http, values);
                } catch (Exception e) {
                    String msg = "Configure ES TLS failed";
                    log.error(msg, e);
                    throw ThirdPartyApiException.as().with(e, EsDsI18nKeys.ES_TLS_INVALID);
                }
                return http;
            });

            if (config.getSecurityType() == SecurityType.USER_PASSWD) {
                String credentials = config.getUserName() + ":" + config.getPassword();
                String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
                builder.setDefaultHeaders(new BasicHeader[] { new BasicHeader("Authorization", "Basic " + encoded) });
            } else if (config.getSecurityType() == SecurityType.API_KEY) {
                builder.setDefaultHeaders(new BasicHeader[] { new BasicHeader("Authorization", "ApiKey " + config.getPassword()) });
            }

            client = new EsClient(builder.build(), config.getRequestTimeoutMs());
            client.probe();
            return new DsObject<>(properties, client, this);
        } catch (Exception e) {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception closeFailure) {
                    e.addSuppressed(closeFailure);
                }
            }
            if (e instanceof ThirdPartyApiException apiException) {
                throw apiException;
            }
            String msg = "Create ES client failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, EsDsI18nKeys.ES_CONNECT_FAILED);
        }
    }
}
