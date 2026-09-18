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
package com.clougence.clouddm.ds.kafka.execute.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;

import com.clougence.drivers.adapter.AdapterConnManager;
import com.clougence.drivers.adapter.AdapterConnection;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

public class KafkaConnection extends AdapterConnection {

    private final Connection  owner;
    private final AdminClient adminClient;
    private String            schema;

    KafkaConnection(Connection owner, AdminClient adminClient, String jdbcUrl, Properties properties, String schema){
        super(jdbcUrl, properties.getProperty(KafkaKeys.USERNAME));
        this.owner = owner;
        this.adminClient = adminClient;
        this.schema = StringUtils.defaultIfBlank(schema, KafkaKeys.DEFAULT_SCHEMA);
    }

    public AdminClient getAdminClient() { return this.adminClient; }

    @Override
    public String getCatalog() { return this.getSchema(); }

    @Override
    public void setCatalog(String catalog) {
        this.setSchema(catalog);
    }

    @Override
    public String getSchema() { return this.schema; }

    @Override
    public void setSchema(String schema) {
        this.schema = StringUtils.defaultIfBlank(schema, KafkaKeys.DEFAULT_SCHEMA);
    }

    @Override
    public AdapterRequest newRequest(String sql) {
        return new KafkaRequest(sql);
    }

    @Override
    protected <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == KafkaConnection.class) {
            return (T) this;
        }
        if (iface == AdminClient.class) {
            return (T) this.adminClient;
        }
        if (AdapterConnection.class.isAssignableFrom(iface) && iface.isInstance(this)) {
            return (T) this;
        }
        return super.unwrap(iface);
    }

    public void killDriverConnection(String connID) throws SQLException {
        KafkaConnection conn = (KafkaConnection) AdapterConnManager.getConnection(connID);
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable e) {
                Throwable root = ExceptionUtils.getRootCause(e);
                if (root instanceof SQLException) {
                    throw (SQLException) root;
                }
                throw new SQLException(e);
            }
        }
    }

    @Override
    public synchronized void doRequest(AdapterRequest request, AdapterReceive receive) throws SQLException {
        throw new SQLException("Kafka command dialect is not supported in this milestone.");
    }

    @Override
    public void cancelRequest() {
        throw new UnsupportedOperationException("cancelRequest not support.");
    }

    @Override
    protected void doClose() throws IOException {
        this.adminClient.close();
    }
}
