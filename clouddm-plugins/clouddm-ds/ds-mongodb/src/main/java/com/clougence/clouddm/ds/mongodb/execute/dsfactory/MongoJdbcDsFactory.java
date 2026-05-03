package com.clougence.clouddm.ds.mongodb.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.clougence.clouddm.ds.mongodb.execute.jdbc.MongoKeys;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;
import com.mongodb.ServerAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * https://dev.mysql.com/doc/refman/8.0/en/information-schema.html
 *
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class MongoJdbcDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String loginTimeoutMs = dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultSchema;
        if (StringUtils.isNotBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()))) {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        } else {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        }
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

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
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put(MongoKeys.TIME_ZONE, clientTimeZone);
        }

        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);

        try {
            Connection connection = new JdbcDriver().connect(jdbcUrl, props);
            return new DsObject<>(dsConfig, connection, this);
        } catch (Exception e) {
            log.error("create EsClient instanceID(MongoDB)=" + id + " ,hosts= " + dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey()) + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());

        return String.format(JdbcDriver.START_URL + "mongodb://%s:%s@%s", username, password, host);
    }

    private List<ServerAddress> parseServerAddress(String mongoAddress) {
        String[] addrs = mongoAddress.trim().split(",");
        List<ServerAddress> serverAddrs = new ArrayList<>(4);
        for (String addr : addrs) {
            String[] hostAndPort = addr.split(":");
            serverAddrs.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        return serverAddrs;
    }
}
