package com.clougence.clouddm.ds.maxcompute.execute.dsfactory;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McDsFactory implements DsFactory<Connection> {

    @Override
    public DsObject<Connection> create(Properties dsConfig) throws SQLException {
        Properties props = new Properties();
        props.putAll(dsConfig);
        for (DsConfigKeys confKey : DsConfigKeys.values()) {
            props.remove(confKey.getConfigKey());
        }

        String id = dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
        String accessId = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String accessKey = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        String loginTimeoutMs = dsConfig.getProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey());
        String connTimeoutMs = dsConfig.getProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey());
        String soTimeoutSec = dsConfig.getProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey());
        String clientName = dsConfig.getProperty(DsConfigKeys.CLIENT_NAME.getConfigKey());
        String defaultDataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String defaultSchema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());
        String clientTimeZone = dsConfig.getProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey());
        String tcpKeepAlive = dsConfig.getProperty(DsConfigKeys.TCP_KEEP_ALIVE.getConfigKey());
        String autoCommit = dsConfig.getProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey());

        String odpsSchemaStyle = dsConfig.getProperty(DsConfigKeys.ODPS_SCHEMA_STYLE.getConfigKey());

        props.put("access_id", accessId);
        props.put("access_key", accessKey);

        if (StringUtils.isNotBlank(clientTimeZone)) {
            props.put("use_project_time_zone", "true");
        }
        if (StringUtils.isNotBlank(connTimeoutMs)) {
            int connSec = Integer.parseInt(connTimeoutMs) / 1000;
            if (connSec > 0) {
                props.put("connect_timeout", String.valueOf(connSec));
            }
        }
        if (StringUtils.isNotBlank(soTimeoutSec)) {
            props.put("read_timeout", soTimeoutSec);
        }

        String jdbcUrl = buildJdbcUrl(dsConfig);
        try {
            Connection connect = new com.aliyun.odps.jdbc.OdpsDriver().connect(jdbcUrl, props);

            if (StringUtils.isNotBlank(clientTimeZone)) {
                try (PreparedStatement ps = connect.prepareStatement("set odps.sql.timezone=" + clientTimeZone)) {
                    ps.execute();
                }
            }

            if (StringUtils.equalsIgnoreCase("true", odpsSchemaStyle)) {
                try (PreparedStatement ps = connect.prepareStatement("set odps.namespace.schema=true")) {
                    ps.execute();
                }
            } else {
                try (PreparedStatement ps = connect.prepareStatement("set odps.namespace.schema=false")) {
                    ps.execute();
                }
            }

            return new DsObject<>(dsConfig, connect, this);
        } catch (Exception e) {
            log.error("create connection instanceID(ODPS)=" + id + " ,jdbcUrl= " + jdbcUrl + ", error:" + e.getMessage());
            throw e;
        }
    }

    protected String buildJdbcUrl(Properties dsConfig) {
        String jdbcURL = dsConfig.getProperty(DsConfigKeys.CUSTOM_URL.getConfigKey());
        if (StringUtils.isNotBlank(jdbcURL)) {
            return jdbcURL;
        }

        String host = dsConfig.getProperty(DsConfigKeys.HOST.getConfigKey());
        String dataBase = dsConfig.getProperty(DsConfigKeys.DEFAULT_DATABASE.getConfigKey());
        String schema = dsConfig.getProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey());

        String interactiveStr;
        String interactiveMode = dsConfig.getProperty(DsConfigKeys.ODPS_INTERACTIVE.getConfigKey());
        if (StringUtils.equalsIgnoreCase("true", interactiveMode)) {
            interactiveStr = "&interactiveStr=true";
        } else {
            interactiveStr = "&interactiveStr=false";
        }

        if (StringUtils.isBlank(schema)) {
            return String.format("jdbc:odps:%s?project=%s", host, safeUrlString(dataBase)) + interactiveStr;
        } else {
            return String.format("jdbc:odps:%s?project=%s&schema=%s", host, safeUrlString(dataBase), safeUrlString(schema)) + interactiveStr;
        }
    }

    private String safeUrlString(String value) {
        return StringUtils.isBlank(value) ? "" : URLEncoder.encode(value);
    }
}
