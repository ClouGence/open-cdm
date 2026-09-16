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
package com.clougence.clouddm.ds.kafka.execute.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;

import com.clougence.drivers.adapter.AdapterFactory;
import com.clougence.drivers.adapter.AdapterTypeSupport;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.drivers.adapter.TypeSupport;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

public class KafkaConnectionFactory implements AdapterFactory {

    @Override
    public String getAdapterName() { return KafkaKeys.ADAPTER_NAME_VALUE; }

    @Override
    public String[] getPropertyNames() {
        return new String[] { KafkaKeys.SERVER, KafkaKeys.ADAPTER_NAME, KafkaKeys.DRIVER_VERSION, KafkaKeys.CONN_TIMEOUT, KafkaKeys.SO_TIMEOUT, KafkaKeys.USERNAME,
                              KafkaKeys.PASSWORD, KafkaKeys.SECURITY_PROTOCOL, KafkaKeys.DATABASE, KafkaKeys.CLIENT_NAME, KafkaKeys.BOOTSTRAP_SERVERS };
    }

    @Override
    public TypeSupport createTypeSupport(Properties properties) {
        return new AdapterTypeSupport(properties);
    }

    @Override
    public KafkaConnection createConnection(Connection owner, String jdbcUrl, Properties props) throws SQLException {
        Map<String, String> caseProps = new LinkedCaseInsensitiveMap<>();
        props.forEach((k, v) -> caseProps.put((String) k, v == null ? "" : String.valueOf(v)));

        if (StringUtils.isBlank(caseProps.get(KafkaKeys.BOOTSTRAP_SERVERS))) {
            String bootstrap = resolveBootstrap(jdbcUrl, caseProps.get(KafkaKeys.SERVER));
            caseProps.put(KafkaKeys.BOOTSTRAP_SERVERS, bootstrap);
            caseProps.put(KafkaKeys.SERVER, bootstrap);
        }

        try {
            Properties adminProps = KafkaAdminConfigs.fromCaseMap(caseProps);
            AdminClient adminClient = AdminClient.create(adminProps);
            return new KafkaConnection(owner, adminClient, jdbcUrl, props, caseProps.get(KafkaKeys.DATABASE));
        } catch (Exception e) {
            throw new SQLException("create Kafka AdminClient failed: " + e.getMessage(), e);
        }
    }

    private static String resolveBootstrap(String jdbcUrl, String server) {
        if (StringUtils.isNotBlank(server)) {
            return server;
        }
        int i = jdbcUrl.indexOf(JdbcDriver.START_URL);
        if (i < 0) {
            return jdbcUrl;
        }
        String rest = jdbcUrl.substring(i + JdbcDriver.START_URL.length());
        if (rest.startsWith(KafkaKeys.ADAPTER_NAME_VALUE)) {
            rest = rest.substring(KafkaKeys.ADAPTER_NAME_VALUE.length());
        }
        if (rest.startsWith(":")) {
            rest = rest.substring(1);
        }
        if (rest.startsWith("//")) {
            rest = rest.substring(2);
        }
        return rest;
    }
}
