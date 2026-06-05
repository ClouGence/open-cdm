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
package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
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
public class SrMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    private static final String TABLE = "select TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,TABLE_TYPE,TABLE_COLLATION,CREATE_TIME,UPDATE_TIME,TABLE_COMMENT from INFORMATION_SCHEMA.TABLES";

    public SrMetaProviderDm(Connection connection){
        super(connection);
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

    public List<Value> selectSchemas(String leafName) throws SQLException {
        String sql = "show databases from " + leafName;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schema = SrMetaProviderUtils.convertSchema(leafName, rs);
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
                List<Value> valueList = SrMetaProviderUtils.convertSchema(rs);
                return valueList.stream().filter(value -> StringUtils.equals(value.asValue(), schema)).findFirst().orElse(null);
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        RdbValue value = new RdbValue();
        value.setValue(catalog);
        value.setUmiType(UmiTypes.Catalog);
        return value;
    }

    public List<Value> selectTables(String catalog, String schema) throws SQLException {
        String sql = "show full tables from " + StarRocksDialect.INSTANCE.fmtName(true, catalog) + "." + StarRocksDialect.INSTANCE.fmtName(true, schema);
        if (catalog.equals(SrMetaProviderUtils.INTERNAL_CATALOG)) {
            sql = sql + " where Table_type in ('TABLE' ,'BASE TABLE')";
        }
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = SrMetaProviderUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "where table_type like '%VIEW'");
    }

    public List<Value> selectMaterialized(String schema) throws SQLException {
        String safeName = StarRocksDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("SHOW MATERIALIZED VIEWS from  %s", safeName);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = SrMetaProviderUtils.convertMaterializedName(rs);
                return valueList.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String safeName = StarRocksDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("SHOW functions FROM %s", safeName);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = SrMetaProviderUtils.convertFunctionName(rs);
                return valueList.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String safeName = StarRocksDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("show full tables from %s ", safeName) + conditions;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = SrMetaProviderUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> fetchByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        String sql = TABLE + " where TABLE_SCHEMA = ? and " + conditions + " and TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return SrMetaProviderUtils.convertTable(rs);
            }
        }
    }

    @Override
    protected List<RdbColumn> convertColumns(ResultSet rs) throws SQLException {
        return SrMetaProviderUtils.convertColumn(rs);
    }

    public List<Value> selectExternalTable(String catalog, String schema) throws SQLException {
        if (SrMetaProviderUtils.INTERNAL_CATALOG.equals(catalog)) {
            String sql = "SELECT TABLE_NAME FROM information_schema.tables WHERE ENGINE != 'StarRocks' and TABLE_TYPE = 'BASE TABLE' and table_schema = ?;";
            try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = SrMetaProviderUtils.convertExternalTableName(rs);
                    schemas.sort(Comparator.comparing(Value::asValue));
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        } else {
            List<Value> values = this.selectTables(catalog, schema);
            for (Value value : values) {
                RdbValue rdbValue = (RdbValue) value;
                rdbValue.setUmiType(UmiTypes.ExternalTable);
            }
            return values;
        }
    }

    public List<Value> selectExternalTables(String catalog, String schema) throws SQLException {
        String sql = "show full tables from " + StarRocksDialect.INSTANCE.fmtName(false, catalog) + "." + StarRocksDialect.INSTANCE.fmtName(false, schema);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = SrMetaProviderUtils.convertExtTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "show catalogs";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = SrMetaProviderUtils.convertCatalogName(rs);
                schemas.sort((o1, o2) -> {
                    String o1Str = o1.asValue();
                    String o2Str = o2.asValue();
                    if (o1Str.equals("default_catalog")) {
                        return -1;
                    } else if (o2Str.equals("default_catalog")) {
                        return 1;
                    } else {
                        return o1Str.compareTo(o2Str);
                    }
                });
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public Value loadSelectObject(String catalog, String schema, String table) throws SQLException {
        RdbTable rdbTable = new RdbTable();
        rdbTable.setCatalog(catalog);
        rdbTable.setSchema(schema);
        rdbTable.setName(table);

        String sql = "show columns from " + StarRocksDialect.INSTANCE.fmtName(false, catalog) + "." + StarRocksDialect.INSTANCE.fmtName(false, schema) + "."
                     + StarRocksDialect.INSTANCE.fmtName(false, table);
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
