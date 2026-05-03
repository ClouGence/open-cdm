package com.clougence.clouddm.ds.redis.execute.dsfactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.clouddm.ds.redis.execute.jdbc.JedisKeys;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author mode 2021/01/08 20:29
 */
@Slf4j
public class JdbcDsFactory implements DsFactory<Connection> {

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
        String defaultSchema;
        if (StringUtils.isNotBlank(dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey()))) {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        } else {
            defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        }
        String clientEncoding = dsConfig.getProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        if (StringUtils.isNotBlank(username)) {
            props.put(JedisKeys.USERNAME, username);
        }
        if (StringUtils.isNotBlank(password)) {
            props.put(JedisKeys.PASSWORD, password);
        }
        if (StringUtils.isNotBlank(clientName)) {
            // client info cannot contain spaces, newlines or special characters.
            clientName = clientName.replace(" ", "-");
            props.put(JedisKeys.CLIENT_NAME, clientName);
        }
        if (StringUtils.isNotBlank(defaultSchema)) {
            props.put(JedisKeys.DATABASE, defaultSchema);
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            props.put(JedisKeys.CONN_TIMEOUT, connTimeoutMs);
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put(JedisKeys.SO_TIMEOUT, Long.parseLong(soTimeoutSec) * 1000);
        }
        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put(JedisKeys.TIME_ZONE, clientTimeZone);
        }
        if (StringUtils.isNotBlank(tcpKeepAlive)) {
            props.put("tcpKeepAlive", tcpKeepAlive);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection jedisConnect = new com.clougence.drivers.adapter.JdbcDriver().connect(jdbcUrl, props);

            return new DsObject<>(dsConfig, jedisConnect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(JdbcJedis)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        if (host.contains(":")) {
            return JedisKeys.START_URL + "//" + host;
        } else {
            return JedisKeys.START_URL + "//" + host + ":6379";
        }
    }
}
