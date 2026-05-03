package com.clougence.clouddm.dsfamily.postgres.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://jdbc.postgresql.org/documentation/use/#connection-parameters
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class PostgresqlDsFactory implements DsFactory<Connection> {

    private static final char[] INJECT_CHAR = new char[] { ' ', ';' };

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
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        String pgIntervalStyle = dsConfig.getProperty(DsConfigKeys.PG_INTERVAL_STYLE.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(loginTimeoutMs)) {
            props.put("loginTimeout", (Long.parseLong(loginTimeoutMs) / 1000)); // sec
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("connectTimeout", (Long.parseLong(connTimeoutMs) / 1000)); // sec
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socketTimeout", Long.parseLong(soTimeoutSec)); // sec
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put("ApplicationName", clientName);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put("currentSchema", defaultSchema);
        }
        if (StringUtils.isNotBlank(clientEncoding)) {
            props.put("allowEncodingChanges", "true"); // latter then exec `set client_encoding = ?`
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection pgConnect = new org.postgresql.Driver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(clientEncoding) && !StringUtils.containsAny(clientEncoding, INJECT_CHAR)) {
                String exec = "set client_encoding = " + clientEncoding;
                try (Statement ps = pgConnect.createStatement()) {
                    ps.execute(exec);
                } catch (SQLException e) {
                    log.error("create connection applyProps failed '" + exec + "'", e);
                }
            }
            if (StringUtils.isNotBlank(pgIntervalStyle) && !StringUtils.containsAny(pgIntervalStyle, INJECT_CHAR)) {
                String exec = "set intervalstyle = " + pgIntervalStyle;
                try (Statement ps = pgConnect.createStatement()) {
                    ps.execute(exec);
                } catch (SQLException e) {
                    log.error("create connection applyProps failed '" + exec + "'", e);
                }
            }
            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    pgConnect.setAutoCommit(false);
                } else {
                    pgConnect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, pgConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(Postgres)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage(), e);
            throw e;
        }
    }

    private String safeString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());

        if (StringUtils.isBlank(defaultDataBase)) {
            defaultDataBase = "postgres";
        }

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return String.format("jdbc:postgresql://%s:5432/%s", ipPort[0], safeString(defaultDataBase));
        } else if (ipPort.length == 2) {
            return String.format("jdbc:postgresql://%s:%s/%s", ipPort[0], ipPort[1], safeString(defaultDataBase));
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }
}
