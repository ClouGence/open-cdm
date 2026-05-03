package com.clougence.clouddm.dsfamily.db2.execute.dsfactory;

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
 * https://jt400.sourceforge.net/
 * https://www.ibm.com/docs/en/i/7.4?topic=java-toolbox
 * https://www.ibm.com/docs/en/i/7.5?topic=connections-jdbc-driver-connection-properties
 *  Db2 for i: https://www.ibm.com/docs/en/ssw_ibm_i_74/pdf/rzatcpdf.pdf
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class Db2JtOpenDsFactory implements DsFactory<Connection> {

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
        String tcpReceiveBufferSize = dsConfig.getProperty(DsConfigKeys.TCP_RCV_BUFFER.getConfigKey());
        String tcpSendBufferSize = dsConfig.getProperty(DsConfigKeys.TCP_SND_BUFFER.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(loginTimeoutMs)) {
            props.put("login timeout", Long.parseLong(loginTimeoutMs) / 1000);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("socket timeout", Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("keep alive", tcpKeepAlive);
        }
        if (StringUtils.isNotBlank(tcpReceiveBufferSize)) {
            props.put("receive buffer size", tcpReceiveBufferSize);
        }
        if (StringUtils.isNotBlank(tcpSendBufferSize)) {
            props.put("send buffer size", tcpSendBufferSize);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection myConnect = new com.ibm.as400.access.AS400JDBCDriver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    myConnect.setAutoCommit(false);
                } else {
                    myConnect.setAutoCommit(true);
                }
            }
            if (StringUtils.isNotBlank(defaultSchema)) {
                try (Statement s = myConnect.createStatement()) {
                    s.execute("set schema \"" + defaultSchema.replace("\"", "\"\"") + "\"");
                }
            }

            return new DsObject<>(dsConfig, myConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(IBM i)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
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
        String defaultDb = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            if (StringUtils.isBlank(defaultDb)) {
                return String.format("jdbc:as400://%s;", ipPort[0]);
            } else {
                return String.format("jdbc:as400://%s/%s;", ipPort[0], safeString(defaultDb).toUpperCase());
            }
        } else if (ipPort.length == 2) {
            if (StringUtils.isBlank(defaultDb)) {
                return String.format("jdbc:as400://%s:%s;", ipPort[0], ipPort[1]);
            } else {
                return String.format("jdbc:as400://%s:%s/%s;", ipPort[0], ipPort[1], safeString(defaultDb).toUpperCase());
            }
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }
}
