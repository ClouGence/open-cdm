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
package com.clougence.clouddm.dsfamily.db2.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.ibm.com/docs/api/v1/content/SSEPGG_9.7.0/com.ibm.db2.luw.apdv.java.doc/src/tpc/imjcc_rjvdsprp.html
 * https://www.ibm.com/docs/api/v1/content/SSEPGG_9.7.0/com.ibm.db2.luw.apdv.java.doc/src/tpc/imjcc_r0052075.html
 *  -
 *  Db2 for z/OS: https://www.ibm.com/products/db2-for-zos
 *  Db2 for i: https://www.ibm.com/docs/en/ssw_ibm_i_74/pdf/rzatcpdf.pdf
 *  Db2 for LUW & Cloud:  https://www.ibm.com/products/db2/database
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class Db2JccDsFactory implements DsFactory<Connection> {

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
            props.put("loginTimeout", Long.parseLong(loginTimeoutMs) / 1000);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put("connectionTimeout", connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("blockingReadConnectionTimeout", Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put("currentSchema", defaultSchema);
        }
        if (StringUtils.isNotBlank(clientName)) {
            props.put("clientProgramName", clientName);
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put("sessionTimeZone", clientTimeZone);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection myConnect = new com.ibm.db2.jcc.DB2Driver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(autoCommit)) {
                if (StringUtils.equalsIgnoreCase("false", autoCommit)) {
                    myConnect.setAutoCommit(false);
                } else {
                    myConnect.setAutoCommit(true);
                }
            }

            return new DsObject<>(dsConfig, myConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(IBM DB2)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
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
            return String.format("jdbc:db2://%s:5000/%s", ipPort[0], safeString(defaultDb).toUpperCase());
        } else if (ipPort.length == 2) {
            return String.format("jdbc:db2://%s:%s/%s", ipPort[0], ipPort[1], safeString(defaultDb).toUpperCase());
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

}
