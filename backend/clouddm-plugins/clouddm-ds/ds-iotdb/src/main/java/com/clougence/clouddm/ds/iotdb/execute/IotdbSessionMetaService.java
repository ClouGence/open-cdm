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
package com.clougence.clouddm.ds.iotdb.execute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class IotdbSessionMetaService implements DsMetaService {

    private final Session          session;
    private final IotdbJdbcSession jdbc;
    private final Connection       connection;

    IotdbSessionMetaService(Session session, IotdbJdbcSession jdbc, Connection connection){
        this.session = session;
        this.jdbc = jdbc;
        this.connection = connection;
    }

    @Override
    public void testConnect() {
        getVersion();
    }

    @Override
    public String getVersion() {
        try {
            return session.executeQuery(conn -> {
                try (Statement statement = conn.createStatement(); ResultSet rows = statement.executeQuery("SHOW VERSION")) {
                    if (!rows.next()) {
                        throw new SQLException("SHOW VERSION returned no rows");
                    }
                    return rows.getString(1);
                }
            });
        } catch (Exception e) {
            log.error("Read IoTDB version failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public Map<String, String> getSqlParserParameters() { return Map.of(IotdbSqlDialect.DRIVER_PROPERTY, jdbc.dialect().name()); }

    @Override
    public String getCurrentCatalog() { return null; }

    @Override
    public String getCurrentSchema() {
        if (jdbc.dialect() != IotdbSqlDialect.TABLE) {
            return null;
        }
        try {
            // IoTDB 2.x reads the database from connection state, including after a broken transport.
            return connection.getSchema();
        } catch (SQLException e) {
            log.error("Read IoTDB current database failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> params) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> params) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> params, UmiTypes type, String pattern) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> params, String name) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> params, UmiTypes type, List<String> names) {
        throw IotdbSessionHook.unsupported("metadata browsing");
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> params, String table) {
        throw IotdbSessionHook.unsupported("table editor");
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        throw IotdbSessionHook.unsupported("object script");
    }
}
