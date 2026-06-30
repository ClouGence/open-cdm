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
package com.clougence.clouddm.ds.oracle.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        props.put("oracle.jdbc.timezoneAsRegion", "false");
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
            if (StringUtils.isNotBlank(clientTimeZone)) {
                try (Statement stmt = oraConnect.createStatement()) {
                    stmt.executeUpdate("ALTER SESSION SET TIME_ZONE = '" + toOracleTimeZoneOffset(clientTimeZone) + "'");
                }
            }
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

    private String toOracleTimeZoneOffset(String clientTimeZone) {
        try {
            ZoneOffset offset;
            if (clientTimeZone.startsWith("+") || clientTimeZone.startsWith("-")) {
                offset = ZoneOffset.of(clientTimeZone);
            } else {
                offset = ZoneId.of(clientTimeZone).getRules().getOffset(Instant.now());
            }
            int totalSeconds = offset.getTotalSeconds();
            String sign = totalSeconds < 0 ? "-" : "+";
            int absSeconds = Math.abs(totalSeconds);
            int hours = absSeconds / 3600;
            int minutes = (absSeconds % 3600) / 60;
            return String.format("%s%02d:%02d", sign, hours, minutes);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("unsupported Oracle client time zone: " + clientTimeZone, e);
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
        boolean tcps = StringUtils.containsIgnoreCase(dsConfig.getProperty("oracle.net.authentication_services"), "TCPS");
        String protocol = tcps ? "tcps" : "tcp";

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
            if (tcps) {
                jdbcUrl = String.format("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=%s)(HOST=%s)(PORT=%s))(CONNECT_DATA=(SID=%s)))",//
                        protocol, ipPort[0], ipPort[1], sid);
            } else {
                jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s:%s",//
                        ipPort[0], ipPort[1], sid);
            }

        } else if ((StringUtils.equalsIgnoreCase("service", connType) || StringUtils.equalsIgnoreCase("oracle_service", connType)) && StringUtils.isNotBlank(serviceName)) {
            if (tcps) {
                jdbcUrl = String.format("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=%s)(HOST=%s)(PORT=%s))(CONNECT_DATA=(SERVICE_NAME=%s)))",//
                        protocol, ipPort[0], ipPort[1], serviceName);
            } else {
                jdbcUrl = String.format("jdbc:oracle:thin:@//%s:%s/%s",//
                        ipPort[0], ipPort[1], serviceName);
            }

        } else if ((StringUtils.equalsIgnoreCase("pdb", connType) || (StringUtils.equalsIgnoreCase("oracle_pdb", connType))) && StringUtils.isNotBlank(pdbName)) {
            if (tcps) {
                jdbcUrl = String.format("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=%s)(HOST=%s)(PORT=%s))(CONNECT_DATA=(SERVICE_NAME=%s)))",//
                        protocol, ipPort[0], ipPort[1], pdbName);
            } else {
                jdbcUrl = String.format("jdbc:oracle:thin:@//%s:%s/%s",//
                        ipPort[0], ipPort[1], pdbName);
            }

        } else if (StringUtils.equalsIgnoreCase("tns", connType) || (StringUtils.equalsIgnoreCase("oracle_tns", connType))) {
            System.setProperty("oracle.net.tns_admin", tnsAdmin);
            jdbcUrl = "jdbc:oracle:thin:@" + tnsName;

        } else {
            throw new IllegalArgumentException("unsupported Oracle connect type:" + connType);
        }

        return jdbcUrl;
    }
}
