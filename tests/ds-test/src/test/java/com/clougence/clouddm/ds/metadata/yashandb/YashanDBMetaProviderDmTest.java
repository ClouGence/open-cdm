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
package com.clougence.clouddm.ds.metadata.yashandb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.ds.yashandb.execute.YashanDBMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * Covers YashanDB metadata acquisition in {@link YashanDBMetaProviderDm}.
 *
 * <p>YashanDB follows the Oracle data dictionary closely enough to reuse the whole
 * {@code dsfamily.oracle} implementation, but its dictionary differs in specific
 * places (verified against YashanDB 23.4.7). These cases lock down the two kinds of
 * adaptation the plugin relies on:
 * <ul>
 * <li><b>SQL shape</b> — never reads {@code PRODUCT_COMPONENT_VERSION}, {@code GLOBAL_NAME} or
 * {@code ALL_MVIEW_LOGS}; the Oracle-only dictionary columns ({@code ORACLE_MAINTAINED},
 * {@code IOT_TYPE}, {@code SUPERVIEW_NAME}, {@code EDITIONING_VIEW}, {@code CHARACTER_SET_NAME},
 * {@code SEGMENT_COLUMN_ID}) are never referenced — their Oracle-compatible aliases are supplied
 * as {@code null} literals instead, so the shared converters keep working;</li>
 * <li><b>Type vocabulary</b> — YashanDB reports native names such as {@code BIGINT},
 * {@code TINYINT}, {@code DOUBLE}, {@code BOOLEAN}, {@code TIME}, {@code JSON} and
 * {@code UNKNOWN} (view columns), which are normalized onto Oracle family type names
 * inside the SQL so the shared converter keeps working.</li>
 * </ul>
 *
 * <p>JDBC behaviour is simulated with JDK dynamic proxies (same pattern as
 * {@code CrdbMetaProviderDmTest}), so no real YashanDB instance is required.
 */
public final class YashanDBMetaProviderDmTest {

    private static final String SCHEMA = "SZ_NCIS_CUSTOMER";

    // ------------------------------------------------------------------ SQL shape

    @Test
    void fetchTableColumns_sqlNormalizesYashanNativeTypeNames() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("CUS_REGION"));

        String sql = connection.sqlOf("DBA_TAB_COLS");
        assertNotNull(sql, "fetchTableColumns should read DBA_TAB_COLS");
        // every YashanDB native name that is absent from OracleSqlTypes must be mapped in SQL
        assertTrue(sql.contains("'BIGINT','NUMBER BIGINT'"), sql);
        assertTrue(sql.contains("'TINYINT','NUMBER BIGINT'"), sql);
        assertTrue(sql.contains("'DOUBLE','BINARY DOUBLE'"), sql);
        assertTrue(sql.contains("'BOOLEAN','PLSQL_BOOLEAN'"), sql);
        assertTrue(sql.contains("'TIME','DATE'"), sql);
        assertTrue(sql.contains("'JSON','CLOB'"), sql);
        assertTrue(sql.contains("'UNKNOWN','VARCHAR'"), sql);
    }

    @Test
    void fetchTableColumns_sqlAvoidsOracleOnlyDictionaryColumns() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("CUS_REGION"));

        String sql = connection.sqlOf("DBA_TAB_COLS");
        // the column does not exist on YashanDB, so the Oracle-compatible alias must come from a null literal
        assertFalse(sql.contains("COLS.CHARACTER_SET_NAME"), sql);
        assertTrue(sql.contains("null CHARACTER_SET_NAME"), sql);
        assertFalse(sql.contains("SEGMENT_COLUMN_ID"), sql);
        // SEGMENT_COLUMN_ID is always NULL on YashanDB, so column order must come from COLUMN_ID
        assertTrue(sql.contains("order by COLS.COLUMN_ID asc"), sql);
    }

    @Test
    void fetchTableColumns_sqlReadsTimestampPrecisionFromDataPrecision() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("CUS_REGION"));

        String sql = connection.sqlOf("DBA_TAB_COLS");
        // YashanDB keeps the TIMESTAMP fractional-seconds precision in DATA_PRECISION, Oracle keeps it in DATA_SCALE
        assertTrue(sql.contains("case when COLS.DATA_TYPE like 'TIMESTAMP%' then COLS.DATA_PRECISION else COLS.DATA_SCALE end"), sql);
    }

    @Test
    void fetchSelectObjectByPart_sqlAvoidsOracleOnlyViews() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchSelectObjectByPart(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("BIL_FT_APP_VW"));

        List<String> issued = connection.issuedSql();
        assertEquals(2, issued.size(), "should query tables then views: " + issued);
        String tableSql = issued.get(0);
        String viewSql = issued.get(1);
        assertTrue(tableSql.contains("SYS.ALL_TABLES"), tableSql);
        assertTrue(viewSql.contains("SYS.ALL_VIEWS"), viewSql);

        // ALL_MVIEW_LOGS does not exist on YashanDB and must not be joined by either statement
        assertFalse(tableSql.contains("ALL_MVIEW_LOGS"), tableSql);
        assertFalse(viewSql.contains("ALL_MVIEW_LOGS"), viewSql);

        // Oracle-only dictionary columns are absent, so the Oracle-compatible aliases are supplied as null literals
        assertTrue(tableSql.contains("null IOT_TYPE"), tableSql);
        assertTrue(viewSql.contains("null SUPERVIEW_NAME"), viewSql);
        assertTrue(viewSql.contains("null EDITIONING_VIEW"), viewSql);
    }

    @Test
    void selectSchemas_sqlUsesDatabaseMaintainedFlag() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        newProvider(connection).selectSchemas();

        String sql = connection.sqlOf("ALL_USERS");
        assertNotNull(sql, "selectSchemas should read ALL_USERS");
        assertTrue(sql.contains("DATABASE_MAINTAINED = 'N'"), sql);
        assertFalse(sql.contains("ORACLE_MAINTAINED"), sql);
    }

    @Test
    void getVersion_sqlUsesVVersionAndAvoidsProductComponentVersion() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        newProvider(connection).getVersion();

        List<String> issued = connection.issuedSql();
        assertEquals(1, issued.size(), "should issue exactly one version query: " + issued);
        assertTrue(issued.get(0).contains("V$VERSION"), issued.get(0));
        assertFalse(issued.get(0).contains("PRODUCT_COMPONENT_VERSION"), issued.get(0));
        assertFalse(issued.get(0).contains("GLOBAL_NAME"), issued.get(0));
    }

    // ------------------------------------------------------------------ behaviour

    @Test
    void getVersion_returnsVVersionValue() throws Exception {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("1", "23.4.7.106");
        CapturingConnection connection = new CapturingConnection(Collections.singletonList(row));

        assertEquals("23.4.7.106", newProvider(connection).getVersion());
    }

    @Test
    void fetchTableColumns_bindsSchemaThenTableNames() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Arrays.asList("CUS_REGION", "SYS_USER"));

        assertEquals(Arrays.asList(SCHEMA, "CUS_REGION", "SYS_USER"), connection.boundParams());
    }

    @Test
    void fetchTableColumns_returnsEmptyWhenNoRows() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        Map<String, List<RdbColumn>> columns = fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("CUS_REGION"));

        assertTrue(columns.isEmpty());
    }

    @Test
    void fetchTableColumns_groupsColumnsByTable() throws Exception {
        // the mock bypasses the SQL layer, so DATA_TYPE carries what the normalized query would return
        List<Map<String, Object>> rows = Arrays.asList(//
                columnRow("CUS_REGION", "REGION_CD", "VARCHAR", 0, "N", 80L, 20L, null, null),//
                columnRow("SYS_USER", "USER_ID", "NUMBER BIGINT", 1, "Y", 8L, null, null, null));
        CapturingConnection connection = new CapturingConnection(rows);

        Map<String, List<RdbColumn>> columns = fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Arrays.asList("CUS_REGION", "SYS_USER"));

        assertEquals(2, columns.size());
        assertEquals("REGION_CD", columns.get("CUS_REGION").get(0).getName());
        assertEquals("USER_ID", columns.get("SYS_USER").get(0).getName());
    }

    @Test
    void fetchTableColumns_mapsNormalizedTypesOntoOracleFamilyTypes() throws Exception {
        List<Map<String, Object>> rows = Arrays.asList(//
                columnRow("T", "A_BIGINT", "NUMBER BIGINT", 0, "Y", 8L, null, null, null),//
                columnRow("T", "A_DOUBLE", "BINARY DOUBLE", 1, "Y", 8L, null, null, null),//
                columnRow("T", "A_BOOLEAN", "PLSQL_BOOLEAN", 2, "Y", 1L, null, null, null),//
                columnRow("T", "A_TIME", "DATE", 3, "Y", 8L, null, null, null),//
                columnRow("T", "A_JSON", "CLOB", 4, "Y", 32000L, null, null, null),//
                columnRow("T", "A_UNKNOWN", "VARCHAR", 5, "Y", 1L, null, null, null),//
                columnRow("T", "A_TIMESTAMP", "TIMESTAMP(6)", 6, "Y", 8L, null, 6, 6));
        CapturingConnection connection = new CapturingConnection(rows);

        List<RdbColumn> columns = fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("T")).get("T");

        assertEquals(7, columns.size());
        assertTrue(columns.get(0).getSqlType().isNumber(), "BIGINT should be numeric");
        assertEquals(OracleSqlTypes.BINARY_DOUBLE, columns.get(1).getSqlType());
        assertTrue(columns.get(2).getSqlType().isBoolean(), "BOOLEAN should stay boolean");
        assertEquals(OracleSqlTypes.DATE, columns.get(3).getSqlType());
        assertEquals(OracleSqlTypes.CLOB, columns.get(4).getSqlType());
        assertEquals(OracleSqlTypes.VARCHAR2, columns.get(5).getSqlType());
        assertEquals(OracleSqlTypes.TIMESTAMP, columns.get(6).getSqlType());
    }

    @Test
    void fetchTableColumns_readsTimestampPrecisionFromDataScale() throws Exception {
        List<Map<String, Object>> rows = Collections.singletonList(//
                columnRow("T", "CREATED_TIME", "TIMESTAMP(6)", 0, "Y", 8L, null, 6, 6));
        CapturingConnection connection = new CapturingConnection(rows);

        RdbColumn column = fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("T")).get("T").get(0);

        assertEquals(6, column.getDatetimePrecision());
    }

    @Test
    void fetchTableColumns_mapsLengthCommentAndNotNullConstraint() throws Exception {
        List<Map<String, Object>> rows = Collections.singletonList(//
                columnRow("CUS_REGION", "REGION_CD", "VARCHAR", 0, "N", 80L, 20L, null, null, "区域编码"));
        CapturingConnection connection = new CapturingConnection(rows);

        RdbColumn column = fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("CUS_REGION")).get("CUS_REGION").get(0);

        assertEquals("CUS_REGION", column.getTable());
        assertEquals("REGION_CD", column.getName());
        assertEquals("区域编码", column.getComment());
        assertEquals(80L, column.getByteLength());
        assertEquals(20L, column.getCharLength());
        assertFalse(column.getConstraints().isEmpty(), "NULLABLE=N should add a non-null constraint");
    }

    @Test
    void fetchTableColumns_skipsHiddenColumns() throws Exception {
        Map<String, Object> hidden = columnRow("T", "HIDDEN_COL", "VARCHAR", 0, "Y", 10L, 10L, null, null);
        hidden.put("HIDDEN_COLUMN", "YES");
        CapturingConnection connection = new CapturingConnection(Collections.singletonList(hidden));

        assertTrue(fetchTableColumns(newProvider(connection), connection.proxy(), null, SCHEMA, Collections.singletonList("T")).isEmpty());
    }

    @Test
    void fetchSelectObjectByPart_keepsTableAndViewResults() throws Exception {
        Map<String, Object> tableRow = objectRow("CUS_REGION", "TABLE", "区域地址;");
        Map<String, Object> viewRow = objectRow("BIL_FT_APP_VW", "VIEW", "财务交易表视图");
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());
        connection.routeRows("SYS.ALL_TABLES", Collections.singletonList(tableRow));
        connection.routeRows("SYS.ALL_VIEWS", Collections.singletonList(viewRow));

        List<RdbTable> objects = fetchSelectObjectByPart(newProvider(connection), connection.proxy(), null, SCHEMA, Arrays.asList("CUS_REGION", "BIL_FT_APP_VW"));

        assertEquals(2, objects.size());
        assertEquals("CUS_REGION", objects.get(0).getName());
        assertEquals(UmiTypes.Table, objects.get(0).getUmiType());
        assertEquals("区域地址;", objects.get(0).getComment());
        assertEquals("BIL_FT_APP_VW", objects.get(1).getName());
        assertEquals(UmiTypes.View, objects.get(1).getUmiType());
        assertEquals("财务交易表视图", objects.get(1).getComment());
    }

    // ------------------------------------------------------------------ helpers

    private static YashanDBMetaProviderDm newProvider(CapturingConnection connection) {
        return new YashanDBMetaProviderDm(connection.proxy(), true);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, List<RdbColumn>> fetchTableColumns(YashanDBMetaProviderDm provider, Connection connection, String catalog, String schema,
                                                                  List<String> tables) throws Exception {
        return (Map<String, List<RdbColumn>>) invoke(provider, "fetchTableColumns", new Class<?>[] { Connection.class, String.class, String.class, List.class },//
                new Object[] { connection, catalog, schema, tables });
    }

    @SuppressWarnings("unchecked")
    private static List<RdbTable> fetchSelectObjectByPart(YashanDBMetaProviderDm provider, Connection connection, String catalog, String schema,
                                                          List<String> tables) throws Exception {
        return (List<RdbTable>) invoke(provider, "fetchSelectObjectByPart", new Class<?>[] { Connection.class, String.class, String.class, List.class },//
                new Object[] { connection, catalog, schema, tables });
    }

    private static Object invoke(YashanDBMetaProviderDm provider, String name, Class<?>[] types, Object[] args) throws Exception {
        Method method = YashanDBMetaProviderDm.class.getDeclaredMethod(name, types);
        method.setAccessible(true);
        try {
            return method.invoke(provider, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw e;
        }
    }

    private static Map<String, Object> columnRow(String table, String column, String dataType, int columnId, String nullable, Long dataLength,
                                                 Long charLength, Integer dataPrecision, Integer dataScale) {
        return columnRow(table, column, dataType, columnId, nullable, dataLength, charLength, dataPrecision, dataScale, null);
    }

    private static Map<String, Object> columnRow(String table, String column, String dataType, int columnId, String nullable, Long dataLength,
                                                 Long charLength, Integer dataPrecision, Integer dataScale, String comment) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("OWNER", SCHEMA);
        row.put("TABLE_NAME", table);
        row.put("COLUMN_NAME", column);
        row.put("DATA_TYPE", dataType);
        row.put("DATA_TYPE_OWNER", null);
        row.put("COLUMN_ID", columnId);
        row.put("DATA_LENGTH", dataLength);
        row.put("CHAR_LENGTH", charLength);
        row.put("DATA_PRECISION", dataPrecision);
        row.put("DATA_SCALE", dataScale);
        row.put("NULLABLE", nullable);
        row.put("HIDDEN_COLUMN", "NO");
        row.put("VIRTUAL_COLUMN", "NO");
        row.put("IDENTITY_COLUMN", "NO");
        row.put("COMMENTS", comment);
        row.put("DATA_DEFAULT", null);
        return row;
    }

    private static Map<String, Object> objectRow(String name, String tableType, String comment) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("OWNER", SCHEMA);
        row.put("TABLE_NAME", name);
        row.put("TABLE_TYPE", tableType);
        row.put("TABLESPACE_NAME", "USERS");
        row.put("TEMPORARY", "N");
        row.put("COMMENTS", comment);
        row.put("PARTITIONED", "NO");
        row.put("VALID_FLAG", "VALID");
        row.put("CREATE_TIME", "2026-01-01 00:00:00");
        row.put("LAST_DDL_TIME", "2026-01-01 00:00:00");
        row.put("SQL", "select 1 from dual");
        row.put("TEXT_LENGTH", "18");
        row.put("READ_ONLY", "N");
        return row;
    }

    private static final class CapturingConnection {

        private final List<String>                        issuedSql   = new ArrayList<>();
        private final List<String>                        boundParams = new ArrayList<>();
        private final Map<String, List<Map<String, Object>>> routes  = new LinkedHashMap<>();
        private final List<Map<String, Object>>           defaultRows;
        private final Connection                          proxy;

        private CapturingConnection(List<Map<String, Object>> defaultRows){
            this.defaultRows = defaultRows;
            ClassLoader loader = YashanDBMetaProviderDmTest.class.getClassLoader();
            this.proxy = (Connection) Proxy.newProxyInstance(loader, new Class<?>[] { Connection.class }, this::invokeConnection);
        }

        private Connection proxy() {
            return this.proxy;
        }

        private List<String> issuedSql() {
            return this.issuedSql;
        }

        private List<String> boundParams() {
            return this.boundParams;
        }

        private void routeRows(String sqlFragment, List<Map<String, Object>> rows) {
            this.routes.put(sqlFragment, rows);
        }

        /** @return the most recently issued statement whose text contains {@code fragment}, or {@code null}. */
        private String sqlOf(String fragment) {
            String found = null;
            for (String sql : this.issuedSql) {
                if (sql.contains(fragment)) {
                    found = sql;
                }
            }
            return found;
        }

        private List<Map<String, Object>> rowsFor(String sql) {
            for (Map.Entry<String, List<Map<String, Object>>> entry : this.routes.entrySet()) {
                if (sql.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return this.defaultRows;
        }

        private Object invokeConnection(Object proxy, Method method, Object[] args) {
            if ("prepareStatement".equals(method.getName()) && args != null && args.length > 0) {
                String sql = (String) args[0];
                this.issuedSql.add(sql);
                this.boundParams.clear();
                return preparedStatement(rowsFor(sql));
            }
            return defaultReturn(method.getReturnType());
        }

        private PreparedStatement preparedStatement(List<Map<String, Object>> rows) {
            ClassLoader loader = YashanDBMetaProviderDmTest.class.getClassLoader();
            return (PreparedStatement) Proxy.newProxyInstance(loader, new Class<?>[] { PreparedStatement.class }, (proxy, method, args) -> {
                String name = method.getName();
                if ("setString".equals(name) && args != null && args.length >= 2) {
                    int index = (Integer) args[0];
                    while (this.boundParams.size() < index) {
                        this.boundParams.add(null);
                    }
                    this.boundParams.set(index - 1, (String) args[1]);
                    return null;
                }
                if ("executeQuery".equals(name)) {
                    return resultSet(rows);
                }
                if ("close".equals(name)) {
                    return null;
                }
                return defaultReturn(method.getReturnType());
            });
        }

        private static ResultSet resultSet(List<Map<String, Object>> rows) {
            ClassLoader loader = YashanDBMetaProviderDmTest.class.getClassLoader();
            return (ResultSet) Proxy.newProxyInstance(loader, new Class<?>[] { ResultSet.class }, new ResultSetHandler(rows));
        }
    }

    private static final class ResultSetHandler implements InvocationHandler {

        private final List<Map<String, Object>> rows;
        private int                             cursor = -1;
        private boolean                         wasNull;

        private ResultSetHandler(List<Map<String, Object>> rows){
            this.rows = rows;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String name = method.getName();
            if ("next".equals(name)) {
                this.cursor++;
                return this.cursor < this.rows.size();
            }
            if ("getMetaData".equals(name)) {
                return metaData();
            }
            if ("wasNull".equals(name)) {
                return this.wasNull;
            }
            if ("close".equals(name)) {
                return null;
            }
            if (name.startsWith("get") && args != null && args.length == 1) {
                Object value = currentRow().get(String.valueOf(args[0]));
                this.wasNull = value == null;
                if (value == null) {
                    return defaultReturn(method.getReturnType());
                }
                if (method.getReturnType() == String.class) {
                    return String.valueOf(value);
                }
                if (method.getReturnType() == long.class || method.getReturnType() == Long.class) {
                    return ((Number) value).longValue();
                }
                if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) {
                    return ((Number) value).intValue();
                }
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
                    return value;
                }
                return value;
            }
            return defaultReturn(method.getReturnType());
        }

        private Map<String, Object> currentRow() {
            return this.rows.get(this.cursor);
        }

        private ResultSetMetaData metaData() {
            ClassLoader loader = YashanDBMetaProviderDmTest.class.getClassLoader();
            return (ResultSetMetaData) Proxy.newProxyInstance(loader, new Class<?>[] { ResultSetMetaData.class }, (proxy, method, args) -> {
                if ("getColumnCount".equals(method.getName())) {
                    return 1;
                }
                if ("getColumnType".equals(method.getName())) {
                    return Types.VARCHAR;
                }
                if ("getColumnTypeName".equals(method.getName())) {
                    return "text";
                }
                if ("getColumnClassName".equals(method.getName())) {
                    return String.class.getName();
                }
                return defaultReturn(method.getReturnType());
            });
        }
    }

    private static Object defaultReturn(Class<?> returnType) {
        if (returnType == void.class) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == short.class) {
            return (short) 0;
        }
        if (returnType == byte.class) {
            return (byte) 0;
        }
        if (returnType == char.class) {
            return '\0';
        }
        if (returnType == double.class) {
            return 0.0d;
        }
        if (returnType == float.class) {
            return 0.0f;
        }
        return null;
    }
}
