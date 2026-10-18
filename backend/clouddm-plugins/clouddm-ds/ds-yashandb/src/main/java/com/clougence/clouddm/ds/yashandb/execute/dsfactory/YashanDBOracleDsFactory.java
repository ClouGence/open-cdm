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
package com.clougence.clouddm.ds.yashandb.execute.dsfactory;

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
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Connection factory for the YashanDB Oracle compatibility mode.
 * <p>
 * The YashanDB JDBC connection URL has the form {@code jdbc:yasdb://host:port/database}, the default port is
 * 1688, and the driver class is {@code com.yashandb.jdbc.Driver}.
 * </p>
 * <p>
 * The two YashanDB compatibility modes use different connection protocols (Oracle mode uses the YashanDB
 * native protocol on {@code 1688}, MySQL mode uses the standard MySQL protocol port and is handled by
 * {@link YashanDBMySQLDsFactory}), so each binds its own driver family and factory rather than sharing one
 * factory and distinguishing the mode through a driver property as GoldenDB does.
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBOracleDsFactory implements DsFactory<Connection> {

    private static final char[] TIME_ZONE_INJECT_CHAR = new char[] { ' ', ';', '\'' };

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String connectTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put("user", username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put("password", password);
        }
        applyTimeoutOptions(props, connectTimeoutMs, soTimeoutSec);

        String jdbcUrl = buildJdbcUrl(dsConfig);

        Connection connection = null;
        try {
            connection = new com.yashandb.jdbc.Driver().connect(jdbcUrl, props);
            if (connection == null) {
                throw new SQLException("YashanDB JDBC driver rejected the configured URL.");
            }

            applyClientTimeZone(connection, clientTimeZone);
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }
            log.info("Create YashanDB connection instanceId={}, jdbcUrl={}", id, jdbcUrl);
            return new DsObject<>(dsConfig, connection, this);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeError) {
                    log.warn("Close failed YashanDB connection: {}", closeError.getMessage());
                }
            }
            String msg = "Create YashanDB connection failed: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw e;
        }
    }

    private static void applyClientTimeZone(Connection connection, String clientTimeZone) {
        if (StringUtils.isBlank(clientTimeZone) || StringUtils.containsAny(clientTimeZone, TIME_ZONE_INJECT_CHAR)) {
            return;
        }

        String offset;
        try {
            offset = toTimeZoneOffset(clientTimeZone);
        } catch (DateTimeException e) {
            log.warn("Ignore unsupported YashanDB client time zone: {}", clientTimeZone);
            return;
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER SESSION SET TIME_ZONE = '" + offset + "'");
        } catch (SQLException e) {
            // The YashanDB TIME_ZONE option only accepts UTC offset forms such as +08:00; region names are
            // rejected. A failure here must not prevent the connection from being established, so the server
            // default time zone is kept.
            log.warn("Set YashanDB session time zone failed, keep server default: {}", e.getMessage());
        }
    }

    /**
     * YashanDB {@code ALTER SESSION SET TIME_ZONE} only accepts UTC offsets such as {@code +08:00}; passing a
     * region name such as {@code Asia/Shanghai} raises {@code YAS-00008}, so the value is always converted to
     * an offset.
     */
    private static String toTimeZoneOffset(String clientTimeZone) {
        ZoneOffset offset;
        if (clientTimeZone.startsWith("+") || clientTimeZone.startsWith("-")) {
            offset = ZoneOffset.of(clientTimeZone);
        } else {
            offset = ZoneId.of(clientTimeZone).getRules().getOffset(Instant.now());
        }

        int totalSeconds = offset.getTotalSeconds();
        String sign = totalSeconds < 0 ? "-" : "+";
        int absSeconds = Math.abs(totalSeconds);
        return String.format("%s%02d:%02d", sign, absSeconds / 3600, (absSeconds % 3600) / 60);
    }

    /**
     * The YashanDB driver reads its timeouts in milliseconds ({@code connectTimeout} / {@code socketTimeout}).
     * Values that are not plain non-negative integers are ignored so the driver default stays in effect.
     */
    private static void applyTimeoutOptions(Properties props, String connectTimeoutMs, String soTimeoutSec) {
        if (isDigits(connectTimeoutMs)) {
            props.put("connectTimeout", connectTimeoutMs.trim());
        }
        if (isDigits(soTimeoutSec)) {
            props.put("socketTimeout", String.valueOf(Long.parseLong(soTimeoutSec.trim()) * 1000L));
        }
    }

    private static boolean isDigits(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String customUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(customUrl)) {
            return customUrl;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String database = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        if (StringUtils.isBlank(database)) {
            // The YashanDB connection URL must carry a database name, which maps to the "Catalog" field (the
            // default database) in the UI; give an actionable hint here.
            throw new IllegalArgumentException("YashanDB database name is required, please fill in the 'Catalog' field of the data source configuration.");
        }

        String[] hostPort = host.split(":");
        if (hostPort.length == 1) {
            return String.format("jdbc:yasdb://%s:1688/%s", hostPort[0], database);
        }
        if (hostPort.length == 2) {
            return String.format("jdbc:yasdb://%s:%s/%s", hostPort[0], hostPort[1], database);
        }
        throw new IllegalArgumentException("Unsupported YashanDB host format: " + host);
    }
}
