package com.clougence.clouddm.ds.gauss.execute.dsfactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GaussDBDsFactory implements DsFactory<Connection> {

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
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }

        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }

        if (StringUtils.isNotBlank(clientName)) {
            props.put("ApplicationName", clientName);
        }

        if (StringUtils.isNotBlank(defaultSchema)) {
            // Sets the default schema for the connection
            props.put("currentSchema", defaultSchema);
        }

        if (StringUtils.isNotBlank(clientEncoding)) {
            props.put("charSet", clientEncoding);
        }

        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        if (StringUtils.isNotBlank(loginTimeoutMs)) {
            props.put("loginTimeout", (Long.parseLong(loginTimeoutMs) / 1000));
        }

        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("connectTimeout", (Long.parseLong(connTimeoutMs)));
        }

        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socketTimeout", soTimeoutSec);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Driver driver = new com.huawei.gaussdb.jdbc.Driver();
            Connection connect = driver.connect(jdbcUrl, props);

            // Set auto-commit property after connection is established
            if (StringUtils.isNotBlank(autoCommit)) {
                connect.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (Exception e) {
            log.error("Failed to create connection for instanceID(GaussDB DataBase)={}, jdbcUrl={}, error: {}", id, jdbcUrl, e.getMessage());
            throw new SQLException("Failed to connect to GaussDB: " + e.getMessage(), e);
        }
    }

    /**
     * Builds the JDBC URL for GaussDB.
     * The format is jdbc:gaussdb://<host>:<port>/<database>
     * @param dsConfig The data source configuration properties.
     * @return The constructed JDBC URL.
     */
    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String database = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey(), ""); // Default to empty string if not provided

        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("Host must be specified for GaussDB connection.");
        }

        String[] ipPort = host.split(":");
        String hostPart = ipPort[0];
        String portPart = (ipPort.length > 1) ? ipPort[1] : "8000"; // default GaussDB port

        return String.format("jdbc:gaussdb://%s:%s/%s", hostPart, portPart, database);
    }
}
