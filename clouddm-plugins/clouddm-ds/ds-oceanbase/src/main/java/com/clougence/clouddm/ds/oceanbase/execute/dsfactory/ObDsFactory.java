package com.clougence.clouddm.ds.oceanbase.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObDsFactory implements DsFactory<Connection> {

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
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        String tenant = dsConfig.getProperty(DsConfigKeys.OB_TENANT.getConfigKey());
        String cluster = dsConfig.getProperty(DsConfigKeys.OB_CLUSTER.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            if (StringUtils.isNotBlank(tenant)) {
                username = username + "@" + tenant;
                if (StringUtils.isNotBlank(cluster)) {
                    username = username + "#" + cluster;
                }
            }
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("connectTimeout", connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socketTimeout", Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put("connectionTimeZone", clientTimeZone);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection connect = new com.oceanbase.jdbc.Driver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                connect.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(ObForOracle)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
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
        String defaultSchema;
        if (StringUtils.isNotBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()))) {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        } else {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        }

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return String.format("jdbc:oceanbase:oracle://%s:2881/%s", ipPort[0], safeString(defaultSchema));
        } else if (ipPort.length == 2) {
            return String.format("jdbc:oceanbase:oracle://%s:%s/%s", ipPort[0], ipPort[1], safeString(defaultSchema));
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }
}
