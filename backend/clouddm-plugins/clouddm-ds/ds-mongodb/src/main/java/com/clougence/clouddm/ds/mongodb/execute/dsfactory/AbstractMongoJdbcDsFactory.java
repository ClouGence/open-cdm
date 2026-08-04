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
package com.clougence.clouddm.ds.mongodb.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.clougence.clouddm.ds.mongodb.dsconf.MongoConnectType;
import com.clougence.clouddm.ds.mongodb.execute.MongoConnectionUri;
import com.clougence.clouddm.ds.mongodb.execute.jdbc.MongoConnectionFactory;
import com.clougence.clouddm.ds.mongodb.execute.jdbc.MongoKeys;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.drivers.adapter.AdapterManager;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.utils.StringUtils;
import com.mongodb.ServerAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractMongoJdbcDsFactory implements DsFactory<Connection> {

    private final String adapterName;

    protected AbstractMongoJdbcDsFactory(String adapterName){
        this.adapterName = adapterName;
        AdapterManager.register(adapterName, new MongoConnectionFactory(adapterName));
    }

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String driverVersion = dsConfig.getProperty(DsConfigKeys.DRIVER_VERSION.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        if (StringUtils.isNotBlank(driverVersion)) {
            props.put(MongoKeys.DRIVER_VERSION, driverVersion);
        }
        if (StringUtils.isNotBlank(clientName)) {
            // client info cannot contain spaces, newlines or special characters.
            clientName = clientName.replace(" ", "-");
            props.put(MongoKeys.CLIENT_NAME, clientName);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put(MongoKeys.DATABASE, defaultSchema);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put(MongoKeys.CONN_TIMEOUT, connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put(MongoKeys.SO_TIMEOUT, String.valueOf(Long.parseLong(soTimeoutSec) * 1000));
        }

        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);

        try {
            Connection connection = new JdbcDriver().connect(jdbcUrl, props);
            return new DsObject<>(dsConfig, connection, this);
        } catch (Exception e) {
            log.error("create MongoDB connection failed, instanceId=" + id + ", hosts=" + dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey()), e);
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcUrl)) {
            if (jdbcUrl.startsWith(MongoKeys.LEGACY_START_URL)) {
                return JdbcDriver.START_URL + this.adapterName + jdbcUrl.substring(MongoKeys.LEGACY_START_URL.length() - 1);
            }
            return jdbcUrl;
        }
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        MongoConnectType connectType = MongoConnectType.of(dsConfig.getProperty(MongoKeys.CONNECT_TYPE));
        if (connectType == MongoConnectType.SRV) {
            return MongoConnectionUri.buildSrvJdbcUrl(this.adapterName, host, username, password);
        }
        String hosts = buildHosts(parseServerAddress(host));
        String startUrl = JdbcDriver.START_URL + this.adapterName + "://";

        if (StringUtils.isBlank(username)) {
            return startUrl + hosts;
        }
        return String.format(startUrl + "%s:%s@%s", username, password, hosts);
    }

    private List<ServerAddress> parseServerAddress(String mongoAddress) {
        String[] addrs = mongoAddress.trim().split(",");
        List<ServerAddress> serverAddrs = new ArrayList<>(4);
        for (String addr : addrs) {
            String[] hostAndPort = addr.trim().split(":");
            if (hostAndPort.length != 2 || StringUtils.isBlank(hostAndPort[0]) || StringUtils.isBlank(hostAndPort[1])) {
                throw new IllegalArgumentException("unsupported MongoDB host format:" + mongoAddress);
            }
            serverAddrs.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        return serverAddrs;
    }

    private String buildHosts(List<ServerAddress> serverAddrs) {
        List<String> hosts = new ArrayList<>(serverAddrs.size());
        for (ServerAddress serverAddr : serverAddrs) {
            hosts.add(serverAddr.getHost() + ":" + serverAddr.getPort());
        }
        return StringUtils.join(hosts, ",");
    }
}
