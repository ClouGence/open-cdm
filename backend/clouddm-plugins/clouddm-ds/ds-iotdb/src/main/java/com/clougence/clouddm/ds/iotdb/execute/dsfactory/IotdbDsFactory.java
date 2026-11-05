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

package com.clougence.clouddm.ds.iotdb.execute.dsfactory;

import java.net.URI;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfigSpi;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IotdbDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) {
        String jdbcUrl = buildJdbcUrl(dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey(), ""));
        Properties properties = new Properties();
        properties.setProperty("user", dsConfig.getProperty(DsConfigKeys.USER.getConfigKey(), ""));
        properties.setProperty("password", dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey(), ""));
        properties.setProperty("network_timeout", "5000");
        try {
            Class<?> driverClass = IotdbDsFactory.class.getClassLoader().loadClass("org.apache.iotdb.jdbc.IoTDBDriver");
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            Connection connection = driver.connect(jdbcUrl, properties);
            if (connection == null) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
            }
            return new DsObject<>(dsConfig, connection, this);
        } catch (ReflectiveOperationException | SQLException e) {
            String msg = "Open IoTDB JDBC connection failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, IotdbI18nKeys.IOTDB_CONNECTION_FAILED);
        }
    }

    private String buildJdbcUrl(String host) {
        URI endpoint;
        try {
            endpoint = URI.create("iotdb://" + host.trim());
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
        }
        if (endpoint.getHost() == null || endpoint.getRawUserInfo() != null || !endpoint.getRawPath().isEmpty() || endpoint.getRawQuery() != null
            || endpoint.getRawFragment() != null || endpoint.getPort() == 0 || endpoint.getPort() > 65535) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_ENDPOINT_INVALID);
        }

        String port = IotdbConfigSpi.DEFAULT_PORT;
        if (endpoint.getPort() != -1) {
            port = Integer.toString(endpoint.getPort());
        }
        return "jdbc:iotdb://" + endpoint.getHost() + ":" + port + "/";
    }
}
