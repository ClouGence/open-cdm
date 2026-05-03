package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.clougence.drivers.adapter.AdapterFactory;
import com.clougence.drivers.adapter.AdapterTypeSupport;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.drivers.adapter.TypeSupport;
import com.clougence.utils.StringUtils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConnectionFactory implements AdapterFactory {

    @Override
    public String getAdapterName() { return "mongodb"; }

    @Override
    public String[] getPropertyNames() {
        return new String[] { MongoKeys.SERVER, MongoKeys.ADAPTER_NAME, MongoKeys.INTERCEPTOR, MongoKeys.TIME_ZONE, MongoKeys.CONN_TIMEOUT, MongoKeys.SO_TIMEOUT,
                              MongoKeys.USERNAME, MongoKeys.PASSWORD, MongoKeys.DATABASE, MongoKeys.CLIENT_NAME, MongoKeys.MAX_TOTAL, MongoKeys.MAX_IDLE, MongoKeys.MIN_IDLE,
                              MongoKeys.TEST_WHILE_IDLE };
    }

    @Override
    public TypeSupport createTypeSupport(Properties properties) {
        return new AdapterTypeSupport(properties);
    }

    @Override
    public MongoConnection createConnection(Connection owner, String jdbcUrl, Properties props) throws SQLException {
        String timeout = props.getProperty(MongoKeys.CONN_TIMEOUT);
        String clientName = props.getProperty(MongoKeys.CLIENT_NAME);

        int connTimeoutMs = StringUtils.isBlank(timeout) ? 5000 : Integer.parseInt(timeout);

        if (StringUtils.isBlank(clientName)) {
            clientName = MongoKeys.DEFAULT_CLIENT_NAME;
        }

        int i = jdbcUrl.indexOf(JdbcDriver.START_URL) + JdbcDriver.START_URL.length();
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
            .applicationName(clientName)
            .applyConnectionString(new ConnectionString(jdbcUrl.substring(i)))
            .applyToSocketSettings(builder -> builder.connectTimeout(connTimeoutMs, TimeUnit.MILLISECONDS));

        MongoClient client = MongoClients.create(settingsBuilder.build());
        return new MongoConnection(owner, client, jdbcUrl, props, props.getProperty(MongoKeys.DATABASE));
    }

}
