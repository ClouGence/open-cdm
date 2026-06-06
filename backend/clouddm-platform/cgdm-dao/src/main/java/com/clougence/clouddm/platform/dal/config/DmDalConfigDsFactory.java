package com.clougence.clouddm.platform.dal.config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

public class DmDalConfigDsFactory implements DsFactory<Connection> {

    private static final String DEFAULT_CONNECT_TIMEOUT_MS = "3000";
    private static final String DEFAULT_SOCKET_TIMEOUT_MS  = "30000";
    private static final String DEFAULT_CHARACTER_ENCODING = "utf8";
    private final ClassLoader   driverClassLoader;

    public DmDalConfigDsFactory(ClassLoader driverClassLoader){
        this.driverClassLoader = driverClassLoader;
    }

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        String jdbcUrl = normalizeJdbcUrl(StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey())));
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("jdbcUrl is blank.");
        }

        Properties properties = new Properties();
        applyDefaultJdbcProperties(properties);
        collectJdbcUrlProperties(dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey()), properties);

        if (StringUtils.isNotBlank(username)) {
            properties.setProperty("user", username);
        }
        if (password != null) {
            properties.setProperty("password", password);
        }

        String loginTimeoutMs = StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey()));
        if (loginTimeoutMs != null && !properties.containsKey("connectTimeout")) {
            properties.setProperty("connectTimeout", loginTimeoutMs);
        }

        Driver driver = (Driver) this.driverClassLoader.loadClass("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = driver.connect(jdbcUrl, properties);
        if (connection == null) {
            throw new SQLException("MySQL driver refused jdbcUrl: " + jdbcUrl);
        }
        return new DsObject<>(dsConfig, connection, this);
    }

    private static void applyDefaultJdbcProperties(Properties properties) {
        properties.setProperty("connectTimeout", DEFAULT_CONNECT_TIMEOUT_MS);
        properties.setProperty("socketTimeout", DEFAULT_SOCKET_TIMEOUT_MS);
        properties.setProperty("characterEncoding", DEFAULT_CHARACTER_ENCODING);
    }

    private static String normalizeJdbcUrl(String jdbcUrl) {
        if (jdbcUrl == null) {
            return null;
        }
        int queryIndex = jdbcUrl.indexOf('?');
        if (queryIndex < 0) {
            return jdbcUrl;
        }
        return StringUtils.trimToNull(jdbcUrl.substring(0, queryIndex));
    }

    private static void collectJdbcUrlProperties(String jdbcUrl, Properties properties) {
        if (StringUtils.isBlank(jdbcUrl)) {
            return;
        }
        int queryIndex = jdbcUrl.indexOf('?');
        if (queryIndex < 0 || queryIndex == jdbcUrl.length() - 1) {
            return;
        }
        String query = jdbcUrl.substring(queryIndex + 1);
        for (String pair : query.split("&")) {
            if (StringUtils.isBlank(pair)) {
                continue;
            }
            int separatorIndex = pair.indexOf('=');
            String key = separatorIndex < 0 ? pair : pair.substring(0, separatorIndex);
            String value = separatorIndex < 0 ? "" : pair.substring(separatorIndex + 1);
            key = decodeJdbcValue(key);
            if (StringUtils.isBlank(key)) {
                continue;
            }
            properties.setProperty(key, decodeJdbcValue(value));
        }
    }

    private static String decodeJdbcValue(String value) {
        return URLDecoder.decode(StringUtils.defaultString(value), StandardCharsets.UTF_8);
    }
}
