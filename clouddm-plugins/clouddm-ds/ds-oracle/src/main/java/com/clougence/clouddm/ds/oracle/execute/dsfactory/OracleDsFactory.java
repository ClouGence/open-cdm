package com.clougence.clouddm.ds.oracle.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://docs.oracle.com/en/database/oracle/oracle-database/21/jajdb/oracle/jdbc/OracleConnection.html
 *
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class OracleDsFactory implements DsFactory<Connection> {

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

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        if (StringUtils.isNotBlank(loginTimeoutMs)) {
            props.put("oracle.jdbc.loginTimeout", loginTimeoutMs);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("oracle.net.CONNECT_TIMEOUT", Long.parseLong(connTimeoutMs));
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("oracle.jdbc.ReadTimeout", Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put("v$session.program", clientName);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("oracle.net.keepAlive", tcpKeepAlive);
        }
        if (StringUtils.isNotBlank(autoCommit)) {
            props.put("autoCommit", autoCommit);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection oraConnect = new oracle.jdbc.driver.OracleDriver().connect(jdbcUrl, props);
            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    oraConnect.setAutoCommit(false);
                } else {
                    oraConnect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, oraConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(Oracle)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String connType = dsConfig.getProperty(DsConfigKeys.ORA_ORACLE_CONNECT_TYPE.getConfigKey());
        String sid = dsConfig.getProperty(DsConfigKeys.ORA_SID.getConfigKey());
        String serviceName = dsConfig.getProperty(DsConfigKeys.ORA_SERVICE_NAME.getConfigKey());
        String pdbName = dsConfig.getProperty(DsConfigKeys.ORA_PDB.getConfigKey());
        String tnsAdmin = dsConfig.getProperty(DsConfigKeys.ORA_TNS_ADMIN.getConfigKey());
        String tnsName = dsConfig.getProperty(DsConfigKeys.ORA_TNS_NAME.getConfigKey());
        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());

        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            ipPort = new String[] { ipPort[0], "1521" };
        } else if (ipPort.length == 2) {
            ipPort = new String[] { ipPort[0], ipPort[1] };
        } else if (ipPort.length == 3) {
            ipPort = new String[] { ipPort[0], ipPort[1] };
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }

        String jdbcUrl;

        if ((StringUtils.equalsIgnoreCase("sid", connType) || StringUtils.equalsIgnoreCase("oracle_sid", connType)) && StringUtils.isNotBlank(sid)) {
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", ipPort[0], ipPort[1], sid);

        } else if ((StringUtils.equalsIgnoreCase("service", connType) || StringUtils.equalsIgnoreCase("oracle_service", connType)) && StringUtils.isNotBlank(serviceName)) {
            jdbcUrl = String.format("jdbc:oracle:thin:@//%s:%s/%s", ipPort[0], ipPort[1], serviceName);

        } else if ((StringUtils.equalsIgnoreCase("pdb", connType) || (StringUtils.equalsIgnoreCase("oracle_pdb", connType))) && StringUtils.isNotBlank(pdbName)) {
            jdbcUrl = String.format("jdbc:oracle:thin:@//%s:%s/%s", ipPort[0], ipPort[1], pdbName);

        } else if (StringUtils.equalsIgnoreCase("tns", connType) || (StringUtils.equalsIgnoreCase("oracle_tns", connType))) {
            System.setProperty("oracle.net.tns_admin", tnsAdmin);
            jdbcUrl = "jdbc:oracle:thin:@" + tnsName;

        } else {
            throw new IllegalArgumentException("unsupported ConnectType:" + connType);
        }

        return jdbcUrl;
    }
}
