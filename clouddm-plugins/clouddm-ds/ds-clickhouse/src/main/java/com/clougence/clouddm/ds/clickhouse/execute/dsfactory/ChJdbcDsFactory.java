package com.clougence.clouddm.ds.clickhouse.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://clickhouse.com/docs/integrations/language-clients/java/jdbc
 * @author Ekko
 * @date 2023/4/6 19:23
*/
@Slf4j
public class ChJdbcDsFactory implements DsFactory<Connection> {

    private static final String DEFAULT_PROTOCOL  = "http";
    private static final String DEFAULT_HTTP_PORT = "8123";

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
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        String chSessionTimeoutMs = dsConfig.getProperty(DsConfigKeys.CH_SESSION_TIMEOUT_MS.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.setProperty("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.setProperty("password", password);
        }
        String database = firstNotBlank(defaultSchema, defaultDataBase);
        if (StringUtils.isNotBlank(database)) {
            props.setProperty("database", database);
        }
        String connectTimeout = firstNotBlank(connTimeoutMs, loginTimeoutMs);
        if (StringUtils.isNotBlank(connectTimeout)) {
            props.setProperty("connection_timeout", connectTimeout);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.setProperty("socket_timeout", String.valueOf(Integer.parseInt(soTimeoutSec) * 1000));
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.setProperty("socket_keepalive", tcpKeepAlive);
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.setProperty("client_name", clientName);
        }
        if (StringUtils.isNotBlank(clientEncoding)) {
            props.setProperty("charset", clientEncoding);
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.setProperty("clickhouse_setting_session_timezone", clientTimeZone);
        }
        if (StringUtils.isNotBlank(chSessionTimeoutMs)) {
            props.setProperty("clickhouse_setting_session_timeout", chSessionTimeoutMs);
        }
        props.putIfAbsent("jdbc_ignore_unsupported_values", "true");

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection connect = new com.clickhouse.jdbc.ClickHouseDriver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    connect.setAutoCommit(false);
                } else {
                    connect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (SQLException e) {
            log.error("create connection instanceID(ClickHouse)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String database = safeString(defaultDataBase);

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return buildJdbcUrl(ipPort[0], DEFAULT_HTTP_PORT, database);
        } else if (ipPort.length == 2) {
            return buildJdbcUrl(ipPort[0], ipPort[1], database);
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

    private String buildJdbcUrl(String host, String port, String database) {
        if (StringUtils.isBlank(database)) {
            return String.format("jdbc:clickhouse:%s://%s:%s", DEFAULT_PROTOCOL, host, port);
        }
        return String.format("jdbc:clickhouse:%s://%s:%s/%s", DEFAULT_PROTOCOL, host, port, database);
    }

    private String firstNotBlank(String first, String second) {
        return StringUtils.isNotBlank(first) ? first : second;
    }

    private String safeString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
