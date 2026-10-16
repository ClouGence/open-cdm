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
package com.clougence.clouddm.ds.goldendb.execute.oracle;

import static com.clougence.adapter.oracle.OracleAttributeNames.STORAGE_TYPE;

import java.sql.*;
import java.util.*;

import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaProviderDm;
import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class GoldenDBOracleMetaProvider extends OraMetaProviderDm {

    private static final String GLOBAL_INDEX_TABLE_COMMENT_PREFIX = "Global Index Table Name = ";

    public GoldenDBOracleMetaProvider(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection connection = this.connectSupplier.eGet();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT VERSION()")) {
            if (!resultSet.next()) {
                throw new SQLException("GoldenDB VERSION() returned no rows.");
            }
            return resultSet.getString(1);
        }
    }

    @Override
    public List<Value> selectSchemas() throws SQLException {
        String sql = "SELECT SCHEMA_NAME AS USERNAME FROM INFORMATION_SCHEMA.SCHEMATA ORDER BY SCHEMA_NAME";
        try (Connection connection = this.connectSupplier.eGet(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            return OraMetaProviderUtils.convertSchema(resultSet);
        }
    }

    @Override
    public Value selectSchema(String schema) throws SQLException {
        String sql = "SELECT SCHEMA_NAME AS USERNAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertSchema(resultSet);
                return schemas.isEmpty() ? null : schemas.get(0);
            }
        }
    }

    @Override
    public List<Value> selectTables(String schema) throws SQLException {
        String sql = "SELECT TABLE_NAME,'TABLE' AS TABLE_TYPE,TABLE_COMMENT AS COMMENTS FROM INFORMATION_SCHEMA.TABLES " + "WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE' "
                     + "AND (TABLE_COMMENT IS NULL OR TABLE_COMMENT NOT LIKE ?) ORDER BY TABLE_NAME";
        List<Value> tables;
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, GLOBAL_INDEX_TABLE_COMMENT_PREFIX + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                tables = OraMetaProviderUtils.convertTableName(resultSet);
            }
        }

        Set<String> materializedViews = new HashSet<>();
        for (Value value : selectMaterializedView(schema)) {
            materializedViews.add(StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT));
        }
        return tables.stream().filter(value -> {
            String tableName = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
            return !materializedViews.contains(tableName) && !tableName.endsWith("_gdb_tmp_mview");
        }).toList();
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        String sql = "SELECT TABLE_NAME AS OBJECT_NAME,'VALID' AS STATUS FROM INFORMATION_SCHEMA.VIEWS " + "WHERE TABLE_SCHEMA = ? ORDER BY TABLE_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertViewName(resultSet);
            }
        }
    }

    @Override
    public List<Value> selectSequences(String schema) throws SQLException {
        String sql = "SELECT SEQUENCE_NAME AS OBJECT_NAME,'VALID' AS STATUS FROM _GDB_SYSDB._GDB_SEQUENCE_SYSTB_INFO "
                     + "WHERE UPPER(SEQUENCE_OWNER) = UPPER(?) ORDER BY SEQUENCE_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertSequenceName(resultSet);
            }
        }
    }

    @Override
    public List<Value> selectMaterializedView(String schema) throws SQLException {
        String sql = "SELECT MVIEW_NAME AS TABLE_NAME,'MATERIALIZED' AS TABLE_TYPE,'' AS COMMENTS "
                     + "FROM _GDB_SYSDB.DBA_MVIEWS WHERE UPPER(OWNER) = UPPER(?) ORDER BY MVIEW_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertTableName(resultSet);
            }
        }
    }

    @Override
    public List<Value> selectProcedures(String schema) throws SQLException {
        String sql = "SELECT OBJECT_NAME,NULL AS ARGUMENT_NAME,NULL AS LENGTH,NULL AS POSITION,NULL AS DATA_TYPE," + "'VALID' AS STATUS FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO "
                     + "WHERE UPPER(OWNER) = UPPER(?) AND OBJECT_TYPE = 'PROCEDURE' ORDER BY OBJECT_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertProcedureName(resultSet);
            }
        }
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String sql = "SELECT OBJECT_NAME,NULL AS ARGUMENT_NAME,NULL AS LENGTH,NULL AS POSITION,NULL AS DATA_TYPE," + "'VALID' AS STATUS FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO "
                     + "WHERE UPPER(OWNER) = UPPER(?) AND OBJECT_TYPE = 'FUNCTION' ORDER BY OBJECT_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertFunctionName(resultSet);
            }
        }
    }

    @Override
    public List<Value> selectTrigger(String schema) throws SQLException {
        String sql = "SELECT OBJECT_NAME,'VALID' AS STATUS FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO "
                     + "WHERE UPPER(OWNER) = UPPER(?) AND OBJECT_TYPE = 'TRIGGER' ORDER BY OBJECT_NAME";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertTriggerName(resultSet);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection connection, String catalog, String schema, List<String> tables) throws SQLException {
        String sql = "SELECT TABLE_SCHEMA AS OWNER,TABLE_NAME,NULL AS TABLESPACE_NAME,'TABLE' AS TABLE_TYPE,"
                     + "NULL AS LOG_TABLE,NULL AS LOG_ROWIDS,NULL AS LOG_PK,NULL AS LOG_SEQ,TABLE_COMMENT AS COMMENTS,"
                     + "'N' AS TEMPORARY,'VALID' AS VALID_FLAG,NULL AS CLUSTER_NAME,NULL AS PCT_FREE,NULL AS PCT_USED,"
                     + "NULL AS INI_TRANS,NULL AS MAX_TRANS,NULL AS INITIAL_EXTENT,NULL AS NEXT_EXTENT,NULL AS MIN_EXTENTS,"
                     + "NULL AS MAX_EXTENTS,'NO' AS PARTITIONED,CREATE_TIME,UPDATE_TIME AS LAST_DDL_TIME "
                     + "FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE' "
                     + "AND (TABLE_COMMENT IS NULL OR TABLE_COMMENT NOT LIKE ?) AND TABLE_NAME IN " + buildWhereIn(tables);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, GLOBAL_INDEX_TABLE_COMMENT_PREFIX + "%");
            for (int i = 0; i < tables.size(); i++) {
                statement.setString(i + 3, tables.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertTable(resultSet);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection connection, String catalog, String schema, List<String> views) throws SQLException {
        String sql = "SELECT VIEW.TABLE_SCHEMA AS OWNER,VIEW.TABLE_NAME,NULL AS TABLESPACE_NAME,'VIEW' AS TABLE_TYPE,"
                     + "NULL AS LOG_TABLE,NULL AS LOG_ROWIDS,NULL AS LOG_PK,NULL AS LOG_SEQ,TAB.TABLE_COMMENT AS COMMENTS,"
                     + "'N' AS TEMPORARY,VIEW.VIEW_DEFINITION AS \"SQL\",CHAR_LENGTH(VIEW.VIEW_DEFINITION) AS TEXT_LENGTH,"
                     + "TAB.CREATE_TIME,TAB.UPDATE_TIME AS LAST_DDL_TIME,'VALID' AS VALID_FLAG," + "CASE WHEN VIEW.IS_UPDATABLE = 'YES' THEN 'N' ELSE 'Y' END AS READ_ONLY "
                     + "FROM INFORMATION_SCHEMA.VIEWS VIEW LEFT JOIN INFORMATION_SCHEMA.TABLES TAB "
                     + "ON VIEW.TABLE_SCHEMA = TAB.TABLE_SCHEMA AND VIEW.TABLE_NAME = TAB.TABLE_NAME " + "WHERE VIEW.TABLE_SCHEMA = ? AND VIEW.TABLE_NAME IN "
                     + buildWhereIn(views);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            for (int i = 0; i < views.size(); i++) {
                statement.setString(i + 2, views.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertTable(resultSet);
            }
        }
    }

    @Override
    public Value loadSequence(String schema, String leafName) throws SQLException {
        String sql = "SELECT SEQUENCE_OWNER,SEQUENCE_NAME,MIN_VALUE,MAX_VALUE,INCREMENT_BY,CYCLE_FLAG,CACHE_SIZE,"
                     + "LAST_NUMBER,SESSION_FLAG,KEEP_VALUE,'VALID' AS STATUS,NULL AS CREATED,NULL AS LAST_DDL_TIME "
                     + "FROM _GDB_SYSDB._GDB_SEQUENCE_SYSTB_INFO WHERE UPPER(SEQUENCE_OWNER) = UPPER(?) " + "AND UPPER(SEQUENCE_NAME) = UPPER(?)";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, leafName);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<RdbSequence> sequences = OraMetaProviderUtils.convertSequence(resultSet);
                return sequences.isEmpty() ? null : sequences.get(0);
            }
        }
    }

    @Override
    public Value loadMaterialized(String schema, String leafName) throws SQLException {
        String sql = "SELECT OWNER,MVIEW_NAME,QUERY,QUERY_LEN,LAST_REFRESH_DATE,LAST_REFRESH_END_TIME,"
                     + "NULL AS CREATED,NULL AS LAST_DDL_TIME,COMPILE_STATE AS STATUS FROM _GDB_SYSDB.DBA_MVIEWS "
                     + "WHERE UPPER(OWNER) = UPPER(?) AND UPPER(MVIEW_NAME) = UPPER(?)";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, leafName);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<RdbView> views = OraMetaProviderUtils.convertMaterialized(resultSet);
                return views.isEmpty() ? null : views.get(0);
            }
        }
    }

    @Override
    public List<RdbProcedure> loadProcedures(String catalog, String schema, List<String> procedureNames) throws SQLException {
        if (procedureNames.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "SELECT OBJECT_NAME,OWNER,OBJECT_TYPE,'VALID' AS STATUS,NULL AS CREATED,NULL AS LAST_DDL_TIME,"
                     + "NULL AS AGGREGATE,NULL AS PIPELINED,NULL AS PARALLEL,NULL AS INTERFACE,NULL AS \"DETERMINISTIC\" "
                     + "FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO WHERE UPPER(OWNER) = UPPER(?) " + "AND OBJECT_TYPE = 'PROCEDURE' AND UPPER(OBJECT_NAME) IN "
                     + buildWhereIn(procedureNames);
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            for (int i = 0; i < procedureNames.size(); i++) {
                statement.setString(i + 2, procedureNames.get(i).toUpperCase(Locale.ROOT));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertProcedures(resultSet);
            }
        }
    }

    @Override
    public List<RdbFunction> loadFunctions(String catalog, String schema, List<String> functionNames) throws SQLException {
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "SELECT OBJECT_NAME,OWNER,OBJECT_TYPE,'VALID' AS STATUS,NULL AS CREATED,NULL AS LAST_DDL_TIME,"
                     + "NULL AS AGGREGATE,NULL AS PIPELINED,NULL AS PARALLEL,NULL AS INTERFACE,NULL AS \"DETERMINISTIC\" "
                     + "FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO WHERE UPPER(OWNER) = UPPER(?) " + "AND OBJECT_TYPE = 'FUNCTION' AND UPPER(OBJECT_NAME) IN "
                     + buildWhereIn(functionNames);
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            for (int i = 0; i < functionNames.size(); i++) {
                statement.setString(i + 2, functionNames.get(i).toUpperCase(Locale.ROOT));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return OraMetaProviderUtils.convertFunctions(resultSet);
            }
        }
    }

    @Override
    public Value loadTrigger(String schema, String leafName) throws SQLException {
        String sql = "SELECT TRIGGER_TYPE,OBJECT_NAME AS TRIGGER_NAME,TRIGGERING_EVENT,TABLE_NAME,TRIGGER_BODY,NULL AS COLUMN_NAME,"
                     + "REFERENCING_NAMES,WHEN_CLAUSE,OWNER,TABLE_OWNER,STATUS,'VALID' AS OBJ_STATUS,"
                     + "NULL AS CREATE_TIME,NULL AS LAST_DDL_TIME FROM _GDB_SYSDB._GDB_DICTIONARY_SYSTB_INFO "
                     + "WHERE UPPER(OWNER) = UPPER(?) AND OBJECT_TYPE = 'TRIGGER' AND UPPER(OBJECT_NAME) = UPPER(?)";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, leafName);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<RdbTrigger> triggers = OraMetaProviderUtils.convertTrigger(resultSet);
                return triggers.isEmpty() ? null : triggers.get(0);
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection connection, String catalog, String schema, List<String> tables) throws SQLException {
        String sql = "SELECT IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,'NORMAL' AS INDEX_TYPE,"
                     + "IDX.UNIQUENESS,IDX.GENERATED,IDX.PARTITIONED,IDX.TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " + "FROM SYS.ALL_INDEXES IDX LEFT JOIN SYS.ALL_IND_COLUMNS COL "
                     + "ON IDX.OWNER = COL.INDEX_OWNER AND IDX.INDEX_NAME = COL.INDEX_NAME " + "WHERE IDX.TABLE_OWNER = ? AND IDX.TABLE_NAME IN " + buildWhereIn(tables)
                     + " AND COL.COLUMN_NAME IS NOT NULL ORDER BY COL.COLUMN_POSITION";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            for (int i = 0; i < tables.size(); i++) {
                statement.setString(i + 2, tables.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return groupIndexes(OraMetaProviderUtils.convertIndex(resultSet));
            }
        }
    }

    private Map<String, List<RdbIndex>> groupIndexes(List<RdbIndex> indexes) {
        if (indexes.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Map<String, RdbIndex>> indexesByTable = new LinkedHashMap<>();
        for (RdbIndex index : indexes) {
            Map<String, RdbIndex> tableIndexes = indexesByTable.computeIfAbsent(index.getTable(), key -> new LinkedHashMap<>());
            RdbIndex existing = tableIndexes.get(index.getName());
            if (existing == null) {
                tableIndexes.put(index.getName(), index);
                continue;
            }

            existing.getColumnList().addAll(index.getColumnList());
            Map<String, String> storage = JsonUtils.toMap(existing.getAttribute(STORAGE_TYPE));
            storage.putAll(JsonUtils.toMap(index.getAttribute(STORAGE_TYPE)));
            existing.setAttribute(STORAGE_TYPE, JsonUtils.toJson(storage));
        }

        Map<String, List<RdbIndex>> result = new LinkedHashMap<>();
        indexesByTable.forEach((table, tableIndexes) -> result.put(table, new ArrayList<>(tableIndexes.values())));
        return result;
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection connection, String catalog, String schema, List<String> tables) throws SQLException {
        String sql = "SELECT C.CONSTRAINT_NAME,C.TABLE_SCHEMA,C.TABLE_NAME,C.COLUMN_NAME,C.REFERENCED_TABLE_SCHEMA,"
                     + "C.REFERENCED_TABLE_NAME,C.REFERENCED_COLUMN_NAME,R.UPDATE_RULE,R.DELETE_RULE "
                     + "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE C JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R "
                     + "ON C.CONSTRAINT_CATALOG = R.CONSTRAINT_CATALOG AND C.CONSTRAINT_SCHEMA = R.CONSTRAINT_SCHEMA "
                     + "AND C.CONSTRAINT_NAME = R.CONSTRAINT_NAME AND C.TABLE_NAME = R.TABLE_NAME " + "WHERE C.TABLE_SCHEMA = ? AND C.TABLE_NAME IN " + buildWhereIn(tables)
                     + " AND C.REFERENCED_TABLE_NAME IS NOT NULL ORDER BY C.ORDINAL_POSITION";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            for (int i = 0; i < tables.size(); i++) {
                statement.setString(i + 2, tables.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return convertForeignKeys(resultSet);
            }
        }
    }

    private Map<String, List<RdbForeignKey>> convertForeignKeys(ResultSet resultSet) throws SQLException {
        Map<String, Map<String, RdbForeignKey>> foreignKeyMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            String table = resultSet.getString("TABLE_NAME");
            String constraint = resultSet.getString("CONSTRAINT_NAME");
            Map<String, RdbForeignKey> tableForeignKeys = foreignKeyMap.computeIfAbsent(table, key -> new LinkedHashMap<>());
            RdbForeignKey foreignKey = tableForeignKeys.get(constraint);
            if (foreignKey == null) {
                foreignKey = new RdbForeignKey();
                foreignKey.setName(constraint);
                foreignKey.setSchema(resultSet.getString("TABLE_SCHEMA"));
                foreignKey.setTable(table);
                foreignKey.setReferenceSchema(resultSet.getString("REFERENCED_TABLE_SCHEMA"));
                foreignKey.setReferenceTable(resultSet.getString("REFERENCED_TABLE_NAME"));
                foreignKey.setUpdateRule(RdbForeignKeyRule.valueOfCode(resultSet.getString("UPDATE_RULE")));
                foreignKey.setDeleteRule(RdbForeignKeyRule.valueOfCode(resultSet.getString("DELETE_RULE")));
                tableForeignKeys.put(constraint, foreignKey);
            }
            foreignKey.addColumn(resultSet.getString("COLUMN_NAME"), resultSet.getString("REFERENCED_COLUMN_NAME"));
        }

        Map<String, List<RdbForeignKey>> result = new LinkedHashMap<>();
        foreignKeyMap.forEach((table, keys) -> result.put(table, new ArrayList<>(keys.values())));
        return result;
    }
}
