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
package com.clougence.clouddm.ds.mongodb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis 元信息获取，参考资料：
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class MongoMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    public MongoMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() {
        String sql = "db.serverBuildInfo()";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getString("version");
            }
        } catch (Exception e) {
            String msg = "getVersion failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<Value> selectSchemas(String schema) throws SQLException {
        String sql = "show databases";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return MongoMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    private void checkSchema(String schema, Connection conn) throws SQLException {
        String connSchema = conn.getSchema();
        if (!connSchema.equals(schema)) {
            throw new SQLException("schema param:" + schema + " is different with connection schema" + connSchema);
        }
    }

    public List<Value> selectTables(String schema) throws SQLException {
        String sql = "show collections";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setSchema(schema);
            checkSchema(schema, conn);
            try (ResultSet rs = ps.executeQuery()) {
                return MongoMetaProviderUtils.convertCollections(rs);
            }
        }
    }

    public List<Value> selectViews(String schema) throws SQLException {
        String sql = "show collections";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setSchema(schema);
            checkSchema(schema, conn);
            try (ResultSet rs = ps.executeQuery()) {
                return MongoMetaProviderUtils.convertViews(rs);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return tabs.stream().map(tab -> {
            RdbTable rdbTable = new RdbTable();
            rdbTable.setName(tab);
            rdbTable.setSchema(schema);
            return rdbTable;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<ConstraintObject>> fetchTableConstraints(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    protected List<RdbIndex> fetchIndexes(String schema, String tab) throws SQLException {
        String sql = "db[\"" + tab + "\"].getIndexes()";
        try (Connection conn = this.connectSupplier.eGet()) {
            checkSchema(schema, conn);
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                List<RdbIndex> indexes = new ArrayList<>();
                while (rs.next()) {
                    RdbIndex rdbIndex = new RdbIndex();
                    rdbIndex.setName(rs.getString("name"));
                    Map<String, String> s = JsonUtils.toMap(rs.getString("key"));
                    s.keySet().forEach(rdbIndex::addColumn);
                    indexes.add(rdbIndex);
                }
                return indexes;
            }
        }
    }

    public RdbTable loadTable(String schema, String table) throws SQLException {
        RdbTable rdbTable = new RdbTable();
        rdbTable.setSchema(schema);
        rdbTable.setName(table);
        rdbTable.setIndices(fetchIndexes(schema, table));
        return rdbTable;
    }
}
