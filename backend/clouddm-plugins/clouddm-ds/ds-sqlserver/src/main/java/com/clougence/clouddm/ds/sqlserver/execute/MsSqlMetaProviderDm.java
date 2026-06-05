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
package com.clougence.clouddm.ds.sqlserver.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.sqlserver.SqlServerMainVersion;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import lombok.extern.slf4j.Slf4j;

/**
 * https://docs.microsoft.com/zh-cn/sql/relational-databases/system-catalog-views/sys-databases-transact-sql?view=sql-server-ver16
 *
 * @author mode 2021/11/16 09:55:55
 */
@Slf4j
public class MsSqlMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    public MsSqlMetaProviderDm(Connection connection){
        super(connection);
        init();
    }

    private void init() {
        this.umiTypeMap.put(UmiTypes.Procedure, "P");
        this.umiTypeMap.put(UmiTypes.Function, "FN");
        this.umiTypeMap.put(UmiTypes.Trigger, "TR");
        this.umiTypeMap.put(UmiTypes.Sequence, "SO");
        this.umiTypeMap.put(UmiTypes.Synonym, "SN");
    }

    protected static String supplementTable(String dbName, String schema, String tableName) {
        String replaceDbName = StringUtils.replace(dbName, "]", "]]");
        String replaceSchemaName = StringUtils.replace(schema, "]", "]]");
        String replaceTableName = StringUtils.replace(tableName, "]", "]]");
        return "[" + replaceDbName + "].[" + replaceSchemaName + "].[" + replaceTableName + "]";
    }

    protected String TABLE(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select s.name schema_name,t.name table_name, t.create_date, t.modify_date, t.type, c.value comment");
        if (!SqlServerMainVersion.SqlServer_2017.isGt(mainVersion)) {
            sqlBuilder.append(", t.text_in_row_limit");
            sqlBuilder.append(", t.max_column_id_used");
            sqlBuilder.append(", t.is_external");
        }
        sqlBuilder.append(" ");
        sqlBuilder.append("from " + supplementTable(dbName, "sys", "tables") + " t ");
        sqlBuilder.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sqlBuilder.append("left join " + supplementTable(dbName, "sys", "extended_properties") + " c on c.major_id = t.object_id and c.minor_id = 0 ");
        return sqlBuilder.toString();
    }

    protected String VIEW(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select s.name schema_name,t.name table_name, t.create_date, t.modify_date, t.type, c.value comment ");
        sqlBuilder.append("from " + supplementTable(dbName, "sys", "all_views") + " t ");
        sqlBuilder.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sqlBuilder.append("left join " + supplementTable(dbName, "sys", "extended_properties") + " c on c.major_id = t.object_id and c.minor_id = 0 ");
        return sqlBuilder.toString();
    }

    protected static String COLUMN(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.name schema_name, t.name table_name,c.name column_name,c.column_id column_order,t2.name column_type, p.value comment,d.definition default_value, ");
        sql.append("       c.user_type_id type_id,c.max_length,c.precision,c.scale,c.collation_name,c.is_nullable,c.is_rowguidcol, ");
        sql.append("       c.is_identity,c.is_computed");
        if (!SqlServerMainVersion.SqlServer_2019.isGt(mainVersion)) {
            sql.append(",c.is_hidden");
        }
        sql.append(" ");
        sql.append("from " + supplementTable(dbName, "sys", "all_columns") + " c ");
        sql.append("left join " + supplementTable(dbName, "sys", "all_objects") + " t on c.object_id = t.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "types") + " t2 on c.user_type_id = t2.user_type_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "default_constraints") + " d on d.object_id = c.default_object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "extended_properties") + " p on p.major_id = t.object_id and c.column_id = p.minor_id ");
        return sql.toString();
    }

    protected static String PRIMARY_UNIQUE(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.name schema_name, t.name table_name, k.name key_name, idc.column_id, c.name column_name,k.is_primary_key is_primary,k.is_unique ");
        sql.append("from " + supplementTable(dbName, "sys", "indexes") + " k ");
        sql.append("left join " + supplementTable(dbName, "sys", "objects") + " t on k.object_id = t.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "index_columns") + " idc on k.object_id = idc.object_id and k.index_id = idc.index_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "columns") + " c on c.object_id = t.object_id and c.column_id = idc.column_id ");
        return sql.toString();
    }

    protected static String FOREIGN_KEY(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select srcs.name table_schema, srct.name table_name,c.name column_name, ");
        sql.append("       dsts.name ref_schema, dstt.name ref_table, rc.name ref_column, ");
        sql.append("       fk.name fk_name, fk.update_referential_action ,fk.delete_referential_action ");
        sql.append("from " + supplementTable(dbName, "sys", "foreign_key_columns") + " fkc ");
        sql.append("left join " + supplementTable(dbName, "sys", "foreign_keys") + " fk on fkc.constraint_object_id = fk.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "objects") + " srct on fkc.parent_object_id = srct.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "objects") + " dstt on fkc.referenced_object_id = dstt.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "columns") + "  c on c.object_id  = fkc.parent_object_id     and c.column_id  = fkc.parent_column_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "columns") + " rc on rc.object_id = fkc.referenced_object_id and rc.column_id = fkc.referenced_column_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " srcs on srct.schema_id = srcs.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " dsts on dstt.schema_id = dsts.schema_id ");
        return sql.toString();
    }

    protected static String INDEX(String dbName, SqlServerMainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.name schema_name, t.name table_name,idx.name index_name,idx.type index_type,c.name column_name,idxc.column_id column_order, idx.is_unique ");
        sql.append("from " + supplementTable(dbName, "sys", "index_columns") + " idxc ");
        sql.append("left join " + supplementTable(dbName, "sys", "indexes") + " idx on idxc.object_id = idx.object_id and idxc.index_id = idx.index_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "objects") + " t on idxc.object_id = t.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on s.schema_id = t.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "columns") + " c on idxc.object_id = c.object_id and idxc.column_id = c.column_id ");
        return sql.toString();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            return getVersion(conn);
        }
    }

    protected String getVersion(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("select @@version"); ResultSet resultSet = ps.executeQuery()) {
            String fullVersion = ((ValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
            if (StringUtils.isBlank(fullVersion)) {
                return fullVersion;
            }

            String[] vers = fullVersion.split("-");
            if (vers.length > 1 && vers[0].contains("Microsoft SQL Server")) {
                return vers[0];
            } else {
                return fullVersion;
            }
        }
    }

    // select name,alias,dateformat,months,shortmonths,days from [tester].sys.syslanguages
    private Map<String, String> serverConfig = null;

    public Map<String, String> fetchServerConfig() throws SQLException {
        if (this.serverConfig != null) {
            return this.serverConfig;
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            return this.fetchServerConfig(conn);
        }
    }

    protected Map<String, String> fetchServerConfig(Connection conn) throws SQLException {
        if (this.serverConfig != null) {
            return this.serverConfig;
        }

        Map<String, String> serverConfig = new LinkedCaseInsensitiveMap<>();
        try (PreparedStatement ps = conn.prepareStatement("dbcc useroptions")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    serverConfig.put(rs.getString(1), rs.getString(2));
                }
            }
            this.serverConfig = serverConfig;
            return this.serverConfig;
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "select name,create_date,is_read_only,collation_name from sys.databases where state = 0 order by name asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return MsSqlMetaProviderUtils.convertCatalog(rs);
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        String sql = "select top 1 name,create_date,is_read_only,collation_name from sys.databases where state = 0 and name = ?";
        try (Connection conn = this.connectSupplier.eGet()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, catalog);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = MsSqlMetaProviderUtils.convertCatalog(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    public List<Value> selectSchemas(String catalog) throws SQLException {
        String tabName = supplementTable(catalog, "INFORMATION_SCHEMA", "SCHEMATA");
        String sql = "select SCHEMA_NAME,DEFAULT_CHARACTER_SET_NAME from " + tabName + " order by SCHEMA_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return MsSqlMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String catalog, String schema) throws SQLException {
        String tabName = supplementTable(catalog, "INFORMATION_SCHEMA", "SCHEMATA");
        String sql = "select top 1 SCHEMA_NAME,DEFAULT_CHARACTER_SET_NAME from " + tabName + " where SCHEMA_NAME = ?";

        try (Connection conn = this.connectSupplier.eGet()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = MsSqlMetaProviderUtils.convertSchema(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    public List<Value> selectViews(String catalog, String schema) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select t.name table_name, t.type, c.value comment ");
        sqlBuilder.append("from " + supplementTable(catalog, "sys", "all_views") + " t ");
        sqlBuilder.append("left join " + supplementTable(catalog, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sqlBuilder.append("left join " + supplementTable(catalog, "sys", "extended_properties") + " c on c.major_id = t.object_id and c.minor_id = 0 ");
        sqlBuilder.append("where s.name = ? order by t.name asc");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertTableName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectTables(String catalog, String schema) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select t.name table_name, t.type, c.value comment ");
        sqlBuilder.append("from " + supplementTable(catalog, "sys", "tables") + " t ");
        sqlBuilder.append("left join " + supplementTable(catalog, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sqlBuilder.append("left join " + supplementTable(catalog, "sys", "extended_properties") + " c on c.major_id = t.object_id and c.minor_id = 0 ");
        sqlBuilder.append("where s.name = ? order by t.name asc");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertTableName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    /**
     * mapping umiTypes and type field in SYS.OBJECTS table
     */
    private final Map<UmiTypes, String> umiTypeMap = new HashMap<>(5);

    /**
     * all objects in sql server are stored in SYS.OBJECTS table like procedure, function, trigger and so on
     * @param catalog database in sqlserver
     * @param umiTypes type of objects
     * @return object select sql
     */
    private String objectSqlBuilderByType(String catalog, UmiTypes umiTypes) {
        return "SELECT OBJS.NAME FROM " + supplementTable(catalog, "SYS", "OBJECTS") + " AS OBJS\n" + "LEFT JOIN " + supplementTable(catalog, "SYS", "SCHEMAS")
               + " AS SCHS ON OBJS.SCHEMA_ID = SCHS.SCHEMA_ID\n" + "WHERE SCHS.NAME = ?  AND OBJS.TYPE = '" + umiTypeMap.get(umiTypes) + "'";
    }

    public List<Value> selectProcedure(String catalog, String schema) throws SQLException {
        String sql = objectSqlBuilderByType(catalog, UmiTypes.Procedure);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertProcedureName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectFunction(String catalog, String schema) throws SQLException {
        String sql = objectSqlBuilderByType(catalog, UmiTypes.Function);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertFunctionName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectTrigger(String catalog, String schema) throws SQLException {
        String sql = objectSqlBuilderByType(catalog, UmiTypes.Trigger);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertTriggerName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSequence(String catalog, String schema) throws SQLException {
        String sql = objectSqlBuilderByType(catalog, UmiTypes.Sequence);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertSequenceName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSynonym(String catalog, String schema) throws SQLException {
        String sql = objectSqlBuilderByType(catalog, UmiTypes.Synonym);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = MsSqlMetaProviderUtils.convertSynonymName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        SqlServerMainVersion mainVersion = SqlServerMainVersion.parserVersion(getVersion(conn));
        String tableCondition = TABLE(catalog, mainVersion);
        return fetchByPart(conn, catalog, schema, tabs, tableCondition, false, mainVersion);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        SqlServerMainVersion mainVersion = SqlServerMainVersion.parserVersion(getVersion(conn));
        String viewCondition = VIEW(catalog, mainVersion);
        return fetchByPart(conn, catalog, schema, tabs, viewCondition, true, mainVersion);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    protected List<RdbTable> fetchByPart(Connection conn, String catalog, String schema, List<String> tabs, String conditions, boolean isView,
                                         SqlServerMainVersion mainVersion) throws SQLException {
        String sql = conditions + " where s.name = ? and t.name in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return MsSqlMetaProviderUtils.convertTables(rs, catalog, isView, mainVersion);
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        SqlServerMainVersion mainVersion = SqlServerMainVersion.parserVersion(getVersion());
        String queryIndex = INDEX(catalog, mainVersion)//
                            + " where is_primary_key = 0 and is_unique = 0 and s.name = ? and t.name in " + buildWhereIn(tabs) //
                            + " order by idxc.column_id asc";

        try (PreparedStatement ps = conn.prepareStatement(queryIndex)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbIndex> idxList = MsSqlMetaProviderUtils.convertIndex(catalog, resultSet);
                if (idxList.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbIndex>> result = new LinkedHashMap<>();
                for (RdbIndex idxKey : idxList) {
                    Map<String, RdbIndex> idxMap = result.computeIfAbsent(idxKey.getTable(), s -> new LinkedHashMap<>());
                    String idxName = idxKey.getName();
                    if (idxMap.containsKey(idxName)) {
                        idxMap.get(idxName).getColumnList().addAll(idxKey.getColumnList());
                    } else {
                        idxMap.put(idxName, idxKey);
                    }
                }
                return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        SqlServerMainVersion mainVersion = SqlServerMainVersion.parserVersion(getVersion());
        String foreignSql = FOREIGN_KEY(catalog, mainVersion)//
                            + " where srcs.name = ? and srct.name in " + buildWhereIn(tabs)//
                            + " order by fkc.referenced_column_id asc";

        try (PreparedStatement ps = conn.prepareStatement(foreignSql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbForeignKey> fkList = MsSqlMetaProviderUtils.convertForeignKey(resultSet, catalog, mainVersion);
                if (fkList.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbForeignKey>> result = new LinkedHashMap<>();
                for (RdbForeignKey foreignKey : fkList) {
                    Map<String, RdbForeignKey> fkMap = result.computeIfAbsent(foreignKey.getTable(), s -> new LinkedHashMap<>());
                    String fkName = foreignKey.getName();
                    if (fkMap.containsKey(fkName)) {
                        fkMap.get(fkName).getColumnList().addAll(foreignKey.getColumnList());
                        fkMap.get(fkName).getReferenceMapping().putAll(foreignKey.getReferenceMapping());
                    } else {
                        fkMap.put(fkName, foreignKey);
                    }
                }
                return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String queryPKUK = PRIMARY_UNIQUE(catalog, SqlServerMainVersion.parserVersion(getVersion()))//
                           + " where k.type != 0 and (k.is_primary_key = 1 or k.is_unique = 1) and s.name = ? and t.name in " + buildWhereIn(tabs) //
                           + " order by idc.column_id asc";

        Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(queryPKUK)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Boolean isPrimary = tryWasNull(rs.getBoolean("is_primary"), rs);
                    Boolean isUnique = tryWasNull(rs.getBoolean("is_unique"), rs);

                    String schemaName = rs.getString("schema_name");
                    String tableName = rs.getString("table_name");
                    String consName = rs.getString("key_name");

                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(tableName, t -> new LinkedHashMap<>());
                    if (Boolean.TRUE.equals(isPrimary)) {
                        RdbPrimaryKey pk = (RdbPrimaryKey) (constraints.computeIfAbsent(consName, k -> new RdbPrimaryKey()));
                        pk.setCatalog(catalog);
                        pk.setSchema(schemaName);
                        pk.setTable(tableName);
                        pk.setName(consName);
                        pk.addColumn(rs.getString("column_name"));
                    } else if (Boolean.TRUE.equals(isUnique)) {
                        RdbUniqueKey uk = (RdbUniqueKey) (constraints.computeIfAbsent(consName, k -> new RdbUniqueKey()));
                        uk.setCatalog(catalog);
                        uk.setSchema(schemaName);
                        uk.setTable(tableName);
                        uk.setName(consName);
                        uk.addColumn(rs.getString("column_name"));
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint isPrimary:" + isPrimary + ", isUnique:" + isUnique);
                    }
                }
            }
        }

        return pkUkMap;
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        Map<String, String> serverConfig = fetchServerConfig(conn);
        String language = serverConfig.get("language");
        String dfType = serverConfig.get("dateformat");

        SqlServerMainVersion mainVersion = SqlServerMainVersion.parserVersion(getVersion());
        String columnQuery = COLUMN(catalog, mainVersion) + //
                             " where s.name = ? and t.name in " + buildWhereIn(tabs) + //
                             " order by s.name,t.name,c.column_id";

        try (PreparedStatement ps = conn.prepareStatement(columnQuery)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbColumn> columns = MsSqlMetaProviderUtils.convertColumn(resultSet, catalog, mainVersion);
                if (columns.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }
}
