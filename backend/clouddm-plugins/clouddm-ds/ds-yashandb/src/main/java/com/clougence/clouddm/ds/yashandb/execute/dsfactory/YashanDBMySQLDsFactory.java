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

import java.sql.*;
import java.util.Properties;

import com.clougence.clouddm.ds.yashandb.dsconf.YashanDBCompatibilityMode;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 崖山数据库（YashanDB）MySQL 兼容模式连接工厂。
 * <p>
 * 崖山 MySQL 兼容模式对外提供标准 MySQL 协议端口（实测 23.4.1.109 单机为 {@code 1690}），
 * 官方明示可用 MySQL 客户端/驱动直连，因此本变体使用 {@code com.mysql.cj.jdbc.Driver}，
 * 连接串形如 {@code jdbc:mysql://host:1690/database}。
 * </p>
 * <p>
 * 为什么不用崖山自己的驱动 + {@code ?compatVector=mysql}：该参数只做 <b>SQL 方言</b>兼容，
 * 驱动的 JDBC 元数据语义仍是 Oracle 取向。实测同一实例上，
 * {@code getCatalogs()} 返回 0 行、{@code getSchemas()} 返回全大写并混入幽灵 schema
 * {@code $MY_test}、{@code identifierQuoteString} 是双引号（MySQL 是反引号），
 * 且 {@code DatabaseMetaData.getColumns()} 直接抛 {@code YAS-04323 arguments count must be 2}，
 * 无法复用 {@code dsc-common-mysql} 整族假定「MySQL Connector/J 语义」的元数据实现。
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBMySQLDsFactory implements DsFactory<Connection> {

    private static final String                DRIVER_CLASS     = "com.mysql.cj.jdbc.Driver";

    /** 崖山在 {@code information_schema.ENGINES} 中登记的引擎名，用于确认目标实例确实是崖山。 */
    private static final String                YASHANDB_ENGINE  = "YashanDB";

    /**
     * 本变体固定对应 MySQL 兼容模式。
     * <p>
     * 与 GoldenDB「两个变体共用一个工厂、靠驱动属性传期望模式」不同，崖山两个变体的驱动与连接协议
     * 完全不同，各自绑定了独立的 driver family 与工厂，因此期望模式由工厂自持，无需再往驱动属性里
     * 塞自定义项（也避免无效属性被透传给 yasdb 驱动）。
     * </p>
     */
    private static final YashanDBCompatibilityMode EXPECTED_MODE = YashanDBCompatibilityMode.MYSQL;

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties properties = driverProperties(dsConfig);
        String instanceId = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String jdbcUrl = buildJdbcUrl(dsConfig);

        Connection connection = null;
        try {
            Class<?> driverClass = YashanDBMySQLDsFactory.class.getClassLoader().loadClass(DRIVER_CLASS);
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            connection = driver.connect(jdbcUrl, properties);
            if (connection == null) {
                throw new SQLException("YashanDB MySQL JDBC driver rejected the connection URL.");
            }
            validateCompatibilityMode(connection);

            String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());
            if (StringUtils.isNotBlank(autoCommit)) {
                connection.setAutoCommit(!StringUtils.equalsIgnoreCase("false", autoCommit));
            }
            log.info("Create YashanDB(MySQL mode) connection instanceId={}, jdbcUrl={}", instanceId, jdbcUrl);
            return new DsObject<>(dsConfig, connection, this);
        } catch (SQLException e) {
            closeConnection(connection);
            log.error("Create YashanDB(MySQL mode) connection failed, instanceId={}", instanceId, e);
            throw e;
        } catch (Exception e) {
            closeConnection(connection);
            String msg = "Load YashanDB MySQL JDBC driver failed.";
            log.error(msg + " instanceId={}", instanceId, e);
            throw new SQLException(msg, e);
        }
    }

    /**
     * 连接后确认目标实例确实运行在崖山 MySQL 兼容模式上。
     * <p>
     * 崖山 MySQL 兼容模式没有 {@code COMPAT_VECTOR} 之类的系统变量可供判断，但它在
     * {@code information_schema.ENGINES} 里把自己登记为唯一引擎 {@code YashanDB}，
     * 而标准 MySQL 会返回 {@code InnoDB} 等，因此以此作为指纹。
     * </p>
     */
    private void validateCompatibilityMode(Connection connection) throws SQLException {
        boolean matched = false;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ENGINE FROM information_schema.ENGINES")) {
            while (resultSet.next()) {
                if (StringUtils.equalsIgnoreCase(YASHANDB_ENGINE, resultSet.getString(1))) {
                    matched = true;
                    break;
                }
            }
        }
        if (!matched) {
            throw new SQLException("YashanDB compatibility mode mismatch, expected: " + EXPECTED_MODE.getServerMode()
                                   + ", but the target instance does not expose the " + YASHANDB_ENGINE + " engine in information_schema.ENGINES.");
        }
    }

    private void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("Close failed YashanDB(MySQL mode) connection: {}", e.getMessage());
        }
    }

    private Properties driverProperties(Properties dsConfig) {
        Properties properties = new Properties();
        properties.putAll(dsConfig);
        for (DsConfigKeys configKey : DsConfigKeys.values()) {
            properties.remove(configKey.getConfigKey());
        }
        properties.entrySet().removeIf(entry -> entry.getValue() == null || StringUtils.isBlank(String.valueOf(entry.getValue())));

        putIfNotBlank(properties, "user", dsConfig.getProperty(DsConfigKeys.USER.getConfigKey()));
        putIfNotBlank(properties, "password", dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey()));
        putIfNotBlank(properties, "connectTimeout", dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey()));
        putSocketTimeout(properties, dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey()));
        putIfNotBlank(properties, "characterEncoding", dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey()));
        putIfNotBlank(properties, "connectionTimeZone", dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey()));
        // 与 dsc-common-mysql 的缺省保持一致：崖山的 bit(1) 不应被驱动当成 Boolean。
        properties.putIfAbsent("tinyInt1isBit", "false");
        return properties;
    }

    private void putIfNotBlank(Properties properties, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            properties.setProperty(key, value);
        }
    }

    private void putSocketTimeout(Properties properties, String soTimeoutSec) {
        if (StringUtils.isNotBlank(soTimeoutSec) && StringUtils.isNumeric(soTimeoutSec.trim())) {
            properties.setProperty("socketTimeout", Long.toString(Long.parseLong(soTimeoutSec.trim()) * 1000));
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String customUrl = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(customUrl)) {
            return customUrl;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        // 库名在界面上可能填在「默认 Schema」或「默认数据库」任一栏位，两者都接受；
        // 实测崖山 MySQL 兼容模式允许不带库名连接，因此都为空时同样照常构造连接串。
        String database = StringUtils.defaultIfBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey()),
            dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()));

        String[] hostPort = StringUtils.defaultIfBlank(host, "").split(":");
        if (hostPort.length == 1) {
            return String.format("jdbc:mysql://%s:%s/%s", hostPort[0], EXPECTED_MODE.getDefaultPort(), StringUtils.defaultString(database));
        }
        if (hostPort.length == 2) {
            return String.format("jdbc:mysql://%s:%s/%s", hostPort[0], hostPort[1], StringUtils.defaultString(database));
        }
        throw new IllegalArgumentException("Unsupported YashanDB host format: " + host);
    }
}
