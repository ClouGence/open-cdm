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
package com.clougence.clouddm.ds.opensearch.execute.dsfactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfigSpi;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchEndpoints;
import com.clougence.clouddm.ds.opensearch.execute.OpenSearchClient;
import com.clougence.clouddm.ds.opensearch.execute.OpenSearchConnectionManager;
import com.clougence.clouddm.ds.opensearch.execute.OpenSearchExecInterceptor;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenSearchDsFactory implements DsFactory<OpenSearchClient> {
    @Override
    public DsObject<OpenSearchClient> create(Properties properties) {
        OpenSearchClient client = null;
        CloseableHttpAsyncClient http = null;
        PoolingAsyncClientConnectionManager pool = null;
        ScheduledThreadPoolExecutor deadlines = null;
        try {
            OpenSearchConfig config = new OpenSearchConfig();
            config.setHost(properties.getProperty(DsConfigKeys.HOST.getConfigKey()));
            config.setSecurityType(SecurityType.valueOf(properties.getProperty(DataSourceConfig.Fields.securityType, "NONE")));
            config.setUserName(properties.getProperty(DsConfigKeys.USER.getConfigKey()));
            config.setPassword(properties.getProperty(DsConfigKeys.PASSWORD.getConfigKey()));
            config.setSslMode(SslMode.valueOf(properties.getProperty(DsConfigKeys.SSL_MODE.getConfigKey(), "DISABLED")));
            Map<String, String> values = new HashMap<>();
            properties.stringPropertyNames().forEach(name -> values.put(name, properties.getProperty(name)));
            new OpenSearchConfigSpi().fillConfig(config, values);
            config.validate();

            var endpoints = OpenSearchEndpoints.parse(config.getHost()).stream().map(uri -> {
                int port = uri.getPort();
                if (port == -1) {
                    port = 9200;
                }
                return URI.create(new HttpHost(uri.getScheme(), uri.getHost(), port).toURI() + "/");
            }).toList();

            var tls = ClientTlsStrategyBuilder.create().setSslContext(OpenSearchSslFactory.create(values)).buildAsync();
            pool = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(config.getMaxConnections())
                .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(config.getConnectTimeoutMs()))
                    .setSocketTimeout(Timeout.ofMilliseconds(config.getSocketTimeoutMs()))
                    .build())
                .setDefaultTlsConfig(TlsConfig.custom()
                    .setHandshakeTimeout(Timeout.ofMilliseconds(config.getHandshakeTimeoutMs()))
                    .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
                    .build())
                .setTlsStrategy(tls)
                .build();
            var builder = HttpAsyncClients.custom()
                .setConnectionManager(new OpenSearchConnectionManager(pool))
                .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectionRequestTimeout(Timeout.ofMilliseconds(config.getConnectionRequestTimeoutMs()))
                    .setResponseTimeout(Timeout.ofMilliseconds(config.getSocketTimeoutMs()))
                    .setHardCancellationEnabled(true)
                    .setAuthenticationEnabled(false)
                    .build())
                .setIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(2).setSelectInterval(TimeValue.ofMilliseconds(Math.min(1000, config.getSocketTimeoutMs()))).build())
                .disableAutomaticRetries()
                .disableRedirectHandling()
                .disableCookieManagement()
                .addExecInterceptorFirst("opensearch-track", new OpenSearchExecInterceptor())
                .addExecInterceptorLast("opensearch-cancel", new OpenSearchExecInterceptor());
            if (config.getSecurityType() == SecurityType.USER_PASSWD) {
                String credentials = config.getUserName() + ":" + config.getPassword();
                String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
                builder.setDefaultHeaders(List.of(new BasicHeader("Authorization", "Basic " + encoded)));
            }

            http = builder.build();
            deadlines = new ScheduledThreadPoolExecutor(1, task -> {
                Thread thread = new Thread(task, "opensearch-deadline");
                thread.setDaemon(true);
                return thread;
            });
            deadlines.setRemoveOnCancelPolicy(true);
            client = new OpenSearchClient(http, deadlines, endpoints, config);
            http.start();
            client.probe();
            return new DsObject<>(properties, client, this);
        } catch (Exception e) {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception closeFailure) {
                    e.addSuppressed(closeFailure);
                }
            } else {
                try {
                    if (http != null) {
                        http.close(CloseMode.IMMEDIATE);
                    } else if (pool != null) {
                        pool.close(CloseMode.IMMEDIATE);
                    }
                } finally {
                    if (deadlines != null) {
                        deadlines.shutdownNow();
                    }
                }
            }
            if (e instanceof ThirdPartyApiException apiException) {
                throw apiException;
            }
            String msg = "Create OpenSearch client failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CREATE_ERROR);
        }
    }
}
