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
package com.clougence.clouddm.ds.valkey.execute.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.drivers.adapter.AdapterFactory;
import com.clougence.drivers.adapter.AdapterTypeSupport;
import com.clougence.drivers.adapter.TypeSupport;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import glide.api.GlideClient;
import glide.api.models.configuration.GlideClientConfiguration;
import glide.api.models.configuration.NodeAddress;
import glide.api.models.configuration.ServerCredentials;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于 Valkey GLIDE 客户端（io.valkey:valkey-glide）的连接工厂。
 */
@Slf4j
public class GlideConnectionFactory implements AdapterFactory {

    private static int passerPort(String host, int defaultPort) {
        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return defaultPort;
        } else if (ipPort.length == 2) {
            return Integer.parseInt(ipPort[1]);
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

    private static GlideClient createGlideClient(Map<String, String> dsConfig) throws SQLException {
        String username = dsConfig.get(GlideKeys.USERNAME);
        String password = dsConfig.get(GlideKeys.PASSWORD);
        String connTimeoutMsStr = dsConfig.get(GlideKeys.CONN_TIMEOUT);
        String soTimeoutSecStr = dsConfig.get(GlideKeys.SO_TIMEOUT);
        String sslMode = dsConfig.get(GlideKeys.SSL_MODE);

        String host = dsConfig.get(GlideKeys.SERVER);
        String[] ipPort = host.split(":");
        String hostName = ipPort[0];
        int port = passerPort(host, 6379);

        int connTimeoutMs = StringUtils.isBlank(connTimeoutMsStr) ? 5000 : Integer.parseInt(connTimeoutMsStr);
        int soTimeoutMs = (StringUtils.isBlank(soTimeoutSecStr) ? 10 : Integer.parseInt(soTimeoutSecStr)) * 1000;
        boolean useTLS = StringUtils.isNotBlank(sslMode) && !SslMode.DISABLED.name().equals(sslMode);

        GlideClientConfiguration.GlideClientConfigurationBuilder builder = GlideClientConfiguration.builder()
            .address(NodeAddress.builder().host(hostName).port(port).build())
            .useTLS(useTLS)
            .requestTimeout(connTimeoutMs);
        if (StringUtils.isNotBlank(username) || StringUtils.isNotBlank(password)) {
            ServerCredentials.ServerCredentialsBuilder credentials = ServerCredentials.builder();
            if (StringUtils.isNotBlank(username)) {
                credentials.username(username);
            }
            if (StringUtils.isNotBlank(password)) {
                credentials.password(password);
            }
            builder.credentials(credentials.build());
        }

        try {
            return GlideClient.createClient(builder.build()).get();
        } catch (Exception e) {
            throw new SQLException("create glide client failed, " + e.getMessage(), e);
        }
    }

    @Override
    public String getAdapterName() { return GlideKeys.ADAPTER_NAME_VALUE; }

    @Override
    public String[] getPropertyNames() {
        return new String[] { GlideKeys.SERVER, GlideKeys.ADAPTER_NAME, GlideKeys.INTERCEPTOR, GlideKeys.TIME_ZONE, GlideKeys.CONN_TIMEOUT, GlideKeys.SO_TIMEOUT,
                              GlideKeys.USERNAME, GlideKeys.PASSWORD, GlideKeys.DATABASE, GlideKeys.CLIENT_NAME, GlideKeys.SSL_MODE, GlideKeys.SSL_CA_FILE, GlideKeys.SSL_CA_FORMAT,
                              GlideKeys.SSL_CA_PASSWORD, GlideKeys.SSL_CLIENT_CERT_FILE, GlideKeys.SSL_CLIENT_CERT_FORMAT, GlideKeys.SSL_CLIENT_KEY_FILE,
                              GlideKeys.SSL_CLIENT_KEY_PASSWORD };
    }

    @Override
    public TypeSupport createTypeSupport(Properties properties) {
        return new AdapterTypeSupport(properties);
    }

    @Override
    public GlideConnection createConnection(Connection owner, String jdbcUrl, Properties props) throws SQLException {
        Map<String, String> caseProps = new LinkedCaseInsensitiveMap<>();
        props.forEach((k, v) -> {
            caseProps.put((String) k, v == null ? "" : String.valueOf(v));
        });

        GlideClient client = createGlideClient(caseProps);
        String defaultCatalog = caseProps.get(GlideKeys.DATABASE);
        int database = StringUtils.isNotBlank(defaultCatalog) ? Integer.parseInt(defaultCatalog) : 0;

        GlideCmd cmd = new GlideCmd(client);
        return new GlideConnection(owner, cmd, jdbcUrl, caseProps, database);
    }
}
