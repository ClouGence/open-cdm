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
package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.clougence.clouddm.ds.mongodb.dsconf.MongoConnectType;
import com.clougence.drivers.adapter.AdapterFactory;
import com.clougence.drivers.adapter.AdapterTypeSupport;
import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.drivers.adapter.TypeSupport;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConnectionFactory implements AdapterFactory {

    private final String adapterName;

    public MongoConnectionFactory(String adapterName){
        this.adapterName = adapterName;
    }

    @Override
    public String getAdapterName() { return this.adapterName; }

    @Override
    public String[] getPropertyNames() {
        return new String[] { MongoKeys.SERVER, MongoKeys.ADAPTER_NAME, MongoKeys.DRIVER_VERSION, MongoKeys.INTERCEPTOR, MongoKeys.TIME_ZONE, MongoKeys.CONN_TIMEOUT,
                              MongoKeys.SO_TIMEOUT, MongoKeys.USERNAME, MongoKeys.PASSWORD, MongoKeys.DATABASE, MongoKeys.CLIENT_NAME, MongoKeys.MAX_TOTAL, MongoKeys.MAX_IDLE,
                              MongoKeys.MIN_IDLE, MongoKeys.TEST_WHILE_IDLE, MongoKeys.CONNECT_TYPE, MongoKeys.SSL_MODE, MongoKeys.SSL_CA_FILE, MongoKeys.SSL_CA_FORMAT,
                              MongoKeys.SSL_CA_PASSWORD, MongoKeys.SSL_CLIENT_CERT_FILE, MongoKeys.SSL_CLIENT_CERT_FORMAT, MongoKeys.SSL_CLIENT_KEY_FILE,
                              MongoKeys.SSL_CLIENT_KEY_PASSWORD };
    }

    @Override
    public TypeSupport createTypeSupport(Properties properties) {
        return new AdapterTypeSupport(properties);
    }

    @Override
    public MongoConnection createConnection(Connection owner, String jdbcUrl, Properties props) throws SQLException {
        String connTimeout = props.getProperty(MongoKeys.CONN_TIMEOUT);
        String soTimeout = props.getProperty(MongoKeys.SO_TIMEOUT);
        String clientName = props.getProperty(MongoKeys.CLIENT_NAME);

        int connTimeoutMs = StringUtils.isBlank(connTimeout) ? 5000 : Integer.parseInt(connTimeout);
        int soTimeoutMs = StringUtils.isBlank(soTimeout) ? 10000 : Integer.parseInt(soTimeout);

        if (StringUtils.isBlank(clientName)) {
            clientName = MongoKeys.DEFAULT_CLIENT_NAME;
        }

        MongoConnectType connectType = MongoConnectType.of(props.getProperty(MongoKeys.CONNECT_TYPE));
        int i = jdbcUrl.indexOf(JdbcDriver.START_URL) + JdbcDriver.START_URL.length();
        String mongoUrl = connectType.getUriScheme() + jdbcUrl.substring(i + this.adapterName.length());
        int queryIndex = mongoUrl.indexOf('?');
        int schemeEnd = mongoUrl.indexOf("://") + 3;
        int authorityEnd = queryIndex < 0 ? mongoUrl.length() : queryIndex;
        int pathIndex = mongoUrl.indexOf('/', schemeEnd);
        if (pathIndex < 0 || pathIndex >= authorityEnd) {
            mongoUrl = mongoUrl.substring(0, authorityEnd) + "/" + mongoUrl.substring(authorityEnd);
        }

        String optionSeparator = "?";
        if (mongoUrl.contains("?")) {
            optionSeparator = "&";
        }
        mongoUrl = mongoUrl + optionSeparator + "connectTimeoutMS=" + connTimeoutMs + "&socketTimeoutMS=" + soTimeoutMs;

        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder().applicationName(clientName).applyConnectionString(new ConnectionString(mongoUrl));

        Map<String, String> caseProps = new LinkedCaseInsensitiveMap<>();
        props.forEach((k, v) -> caseProps.put((String) k, v == null ? "" : String.valueOf(v)));
        MongoSslFactory.apply(settingsBuilder, caseProps);

        MongoClient client = MongoClients.create(settingsBuilder.build());
        return new MongoConnection(owner, client, jdbcUrl, props, props.getProperty(MongoKeys.DATABASE));
    }

}
