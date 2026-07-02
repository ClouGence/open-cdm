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
package com.clougence.clouddm.ds.selectdb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/12/2
 **/
@Slf4j
public class SelMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public SelMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new SelMetaProviderUtils();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet();
                PreparedStatement ps = conn.prepareStatement("show variables like '%version_comment%'");
                ResultSet resultSet = ps.executeQuery()) {
            resultSet.next();// or show variables like '%version_comment%'
            return resultSet.getString("Value");
        }
    }

    @Override
    public List<Value> selectSchemas() throws SQLException {
        String sql = "show databases";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schema = this.providerUtils.convertSchema(rs);
                schema.sort(Comparator.comparing(Value::asValue));
                return schema;
            }
        }
    }

    public Value selectSchema(String schema) throws SQLException {
        final String replaceName = schema.replace("'", "''");

        String sql = "show databases like '" + replaceName + "'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = this.providerUtils.convertSchema(rs);
                return valueList.stream().filter(value -> StringUtils.equals(value.asValue(), schema)).findFirst().orElse(null);
            }
        }
    }

    public List<Value> selectTables(String catalog, String schema) throws SQLException {

        String sql = "show full tables from " + catalog + "." + schema;
        if (catalog.equals("internal")) {
            sql = sql + " where Table_type = 'BASE TABLE'";
        }
        //        String sql = "select TABLE_NAME,'BASE TABLE' as TABLE_TYPE from information_schema.tables where TABLE_TYPE = 'BASE TABLE' and ENGINE is not null and  TABLE_SCHEMA = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> convertView(ResultSet rs) throws SQLException {
        return this.providerUtils.convertView(rs);
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "where table_type like '%VIEW'");
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("show full functions from %s ", safeName);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertFunctionName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }

    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("show full tables from %s ", safeName) + conditions;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> convertTables(ResultSet rs) throws SQLException {
        return this.providerUtils.convertTable(rs);
    }

    @Override
    protected List<RdbColumn> convertColumns(ResultSet rs) throws SQLException {
        return this.providerUtils.convertColumn(rs);
    }

    public List<Value> selectMaterializeds(String schema) throws SQLException {
        String sql = "select TABLE_NAME from information_schema.tables where TABLE_TYPE = 'BASE TABLE' and ENGINE is null and  TABLE_SCHEMA = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = ((SelMetaProviderUtils) this.providerUtils).convertName(rs, UmiTypes.Materialized);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectExternalTables(String catalog, String schema) throws SQLException {
        if (catalog.equals(SelMetaProviderUtils.INTERNAL_CATALOG)) {
            String sql = "select TABLE_NAME from information_schema.tables where TABLE_TYPE = 'EXTERNAL TABLE' and  TABLE_SCHEMA = ?";
            try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = ((SelMetaProviderUtils) this.providerUtils).convertName(rs, UmiTypes.ExternalTable);
                    schemas.sort(Comparator.comparing(Value::asValue));
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        } else {
            List<Value> values = selectTables(catalog, schema);
            for (Value value : values) {
                RdbValue value1 = (RdbValue) value;
                value1.setUmiType(UmiTypes.ExternalTable);
            }
            return values;
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "show catalogs";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = ((SelMetaProviderUtils) this.providerUtils).convertCatalogName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSchemas(String catalog) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, catalog);
        String sql = "show databases from " + safeName;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schema = this.providerUtils.convertSchema(rs);
                schema.sort(Comparator.comparing(Value::asValue));
                return schema;
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        final String replaceName = catalog.replace("'", "''");

        String sql = "show catalogs like '" + replaceName + "'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = ((SelMetaProviderUtils) this.providerUtils).convertCatalog(rs);
                return valueList.stream().filter(value -> StringUtils.equals(value.asValue(), catalog)).findFirst().orElse(null);
            }
        }
    }

    @Override
    public Value loadSelectObject(String catalog, String schema, String table) throws SQLException {
        RdbTable rdbTable = new RdbTable();
        rdbTable.setCatalog(catalog);
        rdbTable.setSchema(schema);
        rdbTable.setName(table);
        String sql = "show columns from " + DorisDialect.INSTANCE.fmtName(false, catalog) + "." + DorisDialect.INSTANCE.fmtName(false, schema) + "."
                     + DorisDialect.INSTANCE.fmtName(false, table);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    RdbColumn rdbColumn = new RdbColumn();
                    rdbColumn.setCatalog(catalog);
                    rdbColumn.setSchema(schema);
                    rdbColumn.setTable(table);
                    rdbColumn.setName(rs.getString("Field"));
                    rdbColumn.setIndex(i++);
                    rdbTable.addColumn(rdbColumn);
                }
            }
        }
        return rdbTable;
    }
}
