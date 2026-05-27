package com.clougence.clouddm.platform.dal.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

public class DmDalConfigDsFactory implements DsFactory<Connection> {

    private final ClassLoader driverClassLoader;

    public DmDalConfigDsFactory(ClassLoader driverClassLoader){
        this.driverClassLoader = driverClassLoader;
    }

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws Exception {
        String jdbcUrl = StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey()));
        String username = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("jdbcUrl is blank.");
        }

        Properties properties = new Properties();
        if (StringUtils.isNotBlank(username)) {
            properties.setProperty("user", username);
        }
        if (password != null) {
            properties.setProperty("password", password);
        }

        String loginTimeoutMs = StringUtils.trimToNull(dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey()));
        if (loginTimeoutMs != null) {
            properties.setProperty("connectTimeout", loginTimeoutMs);
        }

        Driver driver = (Driver) this.driverClassLoader.loadClass("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        Connection connection = driver.connect(jdbcUrl, properties);
        if (connection == null) {
            throw new SQLException("MySQL driver refused jdbcUrl: " + jdbcUrl);
        }
        return new DsObject<>(dsConfig, connection, this);
    }
}
