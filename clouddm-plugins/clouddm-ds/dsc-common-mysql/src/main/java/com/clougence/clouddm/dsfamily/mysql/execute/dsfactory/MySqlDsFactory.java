package com.clougence.clouddm.dsfamily.mysql.execute.dsfactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html
 *
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class MySqlDsFactory implements DsFactory<Connection> {

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

        String mySqlMode = dsConfig.getProperty(DsConfigKeys.MY_SQL_MODE.getConfigKey());
        String myMaxAllowedPacket = dsConfig.getProperty(DsConfigKeys.MY_MAX_ALLOWED_PACKET.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
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
        if (StringUtils.isNotBlank(clientEncoding)) {
            props.put("characterEncoding", clientEncoding.trim());
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put("connectionTimeZone", clientTimeZone);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection myConnect = new com.mysql.cj.jdbc.Driver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                myConnect.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }
            if (StringUtils.isNotBlank(mySqlMode)) {
                if ("EMPTY".equalsIgnoreCase(mySqlMode)) {
                    try (PreparedStatement ps = myConnect.prepareStatement("set sql_mode=''")) {
                        ps.execute();
                    }
                } else {
                    try (PreparedStatement ps = myConnect.prepareStatement("set sql_mode= ?")) {
                        ps.setString(1, mySqlMode);
                        ps.execute();
                    }
                }
            }
            if (StringUtils.isNotBlank(myMaxAllowedPacket)) {
                try (PreparedStatement ps = myConnect.prepareStatement("set global max_allowed_packet= ?")) {
                    ps.setString(1, myMaxAllowedPacket);
                    ps.execute();
                }
            }

            return new DsObject<>(dsConfig, myConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(MySQL)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
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
            return String.format("jdbc:mysql://%s:3306/%s", ipPort[0], safeString(defaultSchema));
        } else if (ipPort.length == 2) {
            return String.format("jdbc:mysql://%s:%s/%s", ipPort[0], ipPort[1], safeString(defaultSchema));
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

}
