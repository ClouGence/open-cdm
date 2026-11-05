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
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.EFunction;

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
    public String getCurrentCatalog() { return jdbc.dialect().name(); }

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
        IotdbSqlDialect model = model(params);
        if (StringUtils.isBlank(parameter(params, UmiTypes.Catalog))) {
            DsElement tree = IotdbMetadataRows.element(UmiTypes.Catalog, IotdbSqlDialect.TREE.name(), IotdbSqlDialect.TREE, Map.of());
            if (!jdbc.supportsTableDialect()) {
                return List.of(tree);
            }
            String version = getVersion();
            if (version.startsWith("0.") || version.startsWith("1.")) {
                return List.of(tree);
            }
            return List.of(tree, IotdbMetadataRows.element(UmiTypes.Catalog, IotdbSqlDialect.TABLE.name(), IotdbSqlDialect.TABLE, Map.of()));
        }
        if (StringUtils.isNotBlank(parameter(params, UmiTypes.Schema))) {
            return List.of();
        }
        return metadata(statement -> IotdbMetadataRows.read(statement, "SHOW DATABASES")
            .stream()
            .map(row -> IotdbMetadataRows.element(UmiTypes.Schema, row.get("database"), model, row))
            .toList());
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> params) {
        IotdbSqlDialect model = model(params);
        String database = parameter(params, UmiTypes.Schema);
        if (StringUtils.isBlank(database)) {
            return IotdbMetadataRows.element(UmiTypes.Catalog, model.name(), model, Map.of());
        }
        return metadata(statement -> IotdbMetadataRows.read(statement, "SHOW DATABASES")
            .stream()
            .filter(row -> database.equals(row.get("database")))
            .map(row -> IotdbMetadataRows.element(UmiTypes.Schema, database, model, row))
            .findFirst()
            .orElse(null));
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> params, UmiTypes type, String pattern) {
        IotdbSqlDialect model = model(params);
        String database = parameter(params, UmiTypes.Schema);
        if (StringUtils.isBlank(database)) {
            return List.of();
        }
        if (model == IotdbSqlDialect.TREE && (type == UmiTypes.Device || type == UmiTypes.Timeseries)) {
            return metadata(statement -> IotdbTreeMetadata.list(statement, database, type, pattern));
        }
        if (model == IotdbSqlDialect.TABLE && (type == UmiTypes.Table || type == UmiTypes.View)) {
            return metadata(statement -> IotdbTableMetadata.list(statement, database, type, pattern));
        }
        return List.of();
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        IotdbSqlDialect model = model(params);
        String database = parameter(params, UmiTypes.Schema);
        if (StringUtils.isBlank(database) || StringUtils.isBlank(name)) {
            return null;
        }
        if (model == IotdbSqlDialect.TREE && (type == UmiTypes.Device || type == UmiTypes.Timeseries)) {
            return metadata(statement -> IotdbTreeMetadata.detail(statement, database, type, name));
        }
        if (model == IotdbSqlDialect.TABLE && (type == UmiTypes.Table || type == UmiTypes.View)) {
            return metadata(statement -> IotdbTableMetadata.detail(statement, database, type, name));
        }
        return null;
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> params, String name) {
        if (model(params) == IotdbSqlDialect.TREE) {
            return detailLeaf(params, UmiTypes.Timeseries, name);
        }
        String database = parameter(params, UmiTypes.Schema);
        if (StringUtils.isBlank(database) || StringUtils.isBlank(name)) {
            return null;
        }
        return metadata(statement -> IotdbTableMetadata.detail(statement, database, null, name));
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> params, UmiTypes type, List<String> names) {
        IotdbSqlDialect model = model(params);
        String database = parameter(params, UmiTypes.Schema);
        if (model != IotdbSqlDialect.TABLE || (type != UmiTypes.Table && type != UmiTypes.View) || StringUtils.isBlank(database) || names.isEmpty()) {
            return Map.of();
        }
        return metadata(statement -> IotdbTableMetadata.batchColumns(statement, database, type, names));
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> params, String table) {
        throw IotdbSessionHook.unsupported("table editor");
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        IotdbSqlDialect model = model(params);
        String database = parameter(params, UmiTypes.Schema);
        if (StringUtils.isBlank(database) || StringUtils.isBlank(name)) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_OBJECT_SCRIPT_UNSUPPORTED, name);
        }
        if (model == IotdbSqlDialect.TREE && (type == UmiTypes.Timeseries || type == UmiTypes.Device)) {
            return metadata(statement -> IotdbObjectScripts.tree(statement, database, type, name));
        }
        if (model == IotdbSqlDialect.TABLE && type == UmiTypes.Table) {
            return metadata(statement -> IotdbObjectScripts.table(statement, database, name));
        }
        throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_OBJECT_SCRIPT_UNSUPPORTED, name);
    }

    private IotdbSqlDialect model(Map<UmiTypes, Object> params) {
        IotdbSqlDialect model = jdbc.dialect();
        String catalog = parameter(params, UmiTypes.Catalog);
        if (StringUtils.isBlank(catalog)) {
            if (StringUtils.isNotBlank(parameter(params, UmiTypes.Schema))) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_METADATA_MODEL_MISMATCH);
            }
        } else if (model != IotdbSqlDialect.parse(catalog)) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_METADATA_MODEL_MISMATCH);
        }
        return model;
    }

    private String parameter(Map<UmiTypes, Object> params, UmiTypes type) {
        if (params == null) {
            return null;
        }
        return (String) params.get(type);
    }

    private <T> T metadata(EFunction<Statement, T, SQLException> query) {
        try {
            return session.executeQuery(conn -> {
                try (Statement statement = conn.createStatement()) {
                    return query.eApply(statement);
                }
            });
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Read IoTDB metadata failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
