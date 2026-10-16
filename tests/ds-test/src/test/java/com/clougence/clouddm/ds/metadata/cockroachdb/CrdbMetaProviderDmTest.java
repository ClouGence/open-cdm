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
package com.clougence.clouddm.ds.metadata.cockroachdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.clougence.adapter.postgre.driver.Oid;
import com.clougence.clouddm.ds.cockroachdb.execute.CrdbMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;

/**
 * Covers CockroachDB column-metadata acquisition in
 * {@link com.clougence.clouddm.ds.cockroachdb.execute.CrdbMetaProviderDm}.
 * CockroachDB does not implement PostgreSQL {@code information_schema._pg_*} helpers,
 * so these cases assert the CRDB SQL shape and mapping of standard column fields.
 *
 * <p>JDBC behaviour is simulated with JDK dynamic proxies (same pattern as
 * {@code AbstractMetadataProvider}), so no real CockroachDB instance is required.
 */
public final class CrdbMetaProviderDmTest {

    @Test
    void fetchTableColumns_sqlAvoidsPostgresInternalHelpers() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), "defaultdb", "public", Collections.singletonList("users"));

        String sql = connection.columnSql();
        assertFalse(sql.contains("information_schema._pg_"));
        assertFalse(sql.contains("_pg_char_octet_length"));
        assertFalse(sql.contains("_pg_char_max_length"));
        assertFalse(sql.contains("_pg_truetypid"));
        assertFalse(sql.contains("_pg_truetypmod"));
        assertTrue(sql.contains("sch.character_octet_length"));
        assertTrue(sql.contains("sch.character_maximum_length"));
        assertTrue(sql.contains("information_schema.COLUMNS"));
        assertTrue(sql.contains("pg_catalog.pg_attribute"));
    }

    @Test
    void fetchTableColumns_bindsSchemaAndTableParameters() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        fetchTableColumns(newProvider(connection), connection.proxy(), "defaultdb", "public", Arrays.asList("users", "orders"));

        assertEquals(Arrays.asList("public", "users", "orders"), connection.boundParams());
    }

    @Test
    void fetchTableColumns_returnsEmptyWhenNoRows() throws Exception {
        CapturingConnection connection = new CapturingConnection(Collections.emptyList());

        Map<String, List<RdbColumn>> columns = fetchTableColumns(newProvider(connection), connection.proxy(), "defaultdb", "public", Collections.singletonList("users"));

        assertTrue(columns.isEmpty());
    }

    @Test
    void fetchTableColumns_groupsColumnsByTable() throws Exception {
        List<Map<String, Object>> rows = Arrays.asList(//
                                                       columnRow("users", "id", Oid.INT4, "integer", 1, false, null, null, null),//
                                                       columnRow("orders", "amount", Oid.NUMERIC, "numeric", 1, false, null, null, null));
        CapturingConnection connection = new CapturingConnection(rows);

        Map<String, List<RdbColumn>> columns = fetchTableColumns(newProvider(connection), connection.proxy(), "defaultdb", "public", Arrays.asList("users", "orders"));

        assertEquals(2, columns.size());
        assertEquals(1, columns.get("users").size());
        assertEquals(1, columns.get("orders").size());
        assertEquals("id", columns.get("users").get(0).getName());
        assertEquals("amount", columns.get("orders").get(0).getName());
    }

    @Test
    void fetchTableColumns_mapsBasicColumnAttributes() throws Exception {
        List<Map<String, Object>> rows = Collections.singletonList(//
                                                                   columnRow("users", "name", Oid.VARCHAR, "character varying", 2, true, 64L, "display name", "'guest'"));
        CapturingConnection connection = new CapturingConnection(rows);

        Map<String, List<RdbColumn>> columns = fetchTableColumns(newProvider(connection), connection.proxy(), "defaultdb", "public", Collections.singletonList("users"));

        RdbColumn column = columns.get("users").get(0);
        assertEquals("users", column.getTable());
        assertEquals("name", column.getName());
        assertEquals(2, column.getIndex());
        assertEquals("display name", column.getComment());
        assertEquals("'guest'", column.getDefaultValue());
        assertEquals(64L, column.getCharLength());
        assertFalse(column.getConstraints().isEmpty());
    }

    private static CrdbMetaProviderDm newProvider(CapturingConnection connection) {
        return new CrdbMetaProviderDm(connection.proxy());
    }

    @SuppressWarnings("unchecked")
    private static Map<String, List<RdbColumn>> fetchTableColumns(CrdbMetaProviderDm provider, Connection connection, String catalog, String schema,
                                                                  List<String> tables) throws Exception {
        Method method = CrdbMetaProviderDm.class.getDeclaredMethod("fetchTableColumns", Connection.class, String.class, String.class, List.class);
        method.setAccessible(true);
        try {
            return (Map<String, List<RdbColumn>>) method.invoke(provider, connection, catalog, schema, tables);
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

    private static Map<String, Object> columnRow(String table, String column, int typeOid, String typeName, int attnum, boolean notNull, Long charMaxLength,
                                                 String comments, String columnDefault) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("TABLE_NAME", table);
        row.put("COLUMN_NAME", column);
        row.put("type_oid", (long) typeOid);
        row.put("type_name", typeName);
        row.put("column_default", columnDefault);
        row.put("not_null", notNull);
        row.put("type_is_array", false);
        row.put("datetime_precision", null);
        row.put("attnum", attnum);
        row.put("comments", comments);
        row.put("numeric_scale", null);
        row.put("numeric_precision", null);
        row.put("character_maximum_length", charMaxLength);
        row.put("generation_expression", null);
        row.put("identity_generation", null);
        row.put("typtype", "b");
        row.put("numeric_precision_radix", 10);
        row.put("COLLATION_NAME", null);
        row.put("collation_schema_name", null);
        row.put("type_mod", -1);
        return row;
    }

    private static final class CapturingConnection {

        private final List<Map<String, Object>> columnRows;
        private final List<String>              boundParams = new ArrayList<>();
        private String                          columnSql;
        private final Connection                proxy;

        private CapturingConnection(List<Map<String, Object>> columnRows){
            this.columnRows = columnRows;
            ClassLoader loader = CrdbMetaProviderDmTest.class.getClassLoader();
            this.proxy = (Connection) Proxy.newProxyInstance(loader, new Class<?>[] { Connection.class }, this::invokeConnection);
        }

        private Connection proxy() {
            return this.proxy;
        }

        private String columnSql() {
            return this.columnSql;
        }

        private List<String> boundParams() {
            return this.boundParams;
        }

        private Object invokeConnection(Object proxy, Method method, Object[] args) {
            if ("prepareStatement".equals(method.getName()) && args != null && args.length > 0) {
                String sql = (String) args[0];
                if (sql.contains("server_version_num")) {
                    return preparedStatement(versionRows());
                }
                this.columnSql = sql;
                this.boundParams.clear();
                return preparedStatement(this.columnRows);
            }
            return defaultReturn(method.getReturnType());
        }

        private PreparedStatement preparedStatement(List<Map<String, Object>> rows) {
            ClassLoader loader = CrdbMetaProviderDmTest.class.getClassLoader();
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

        private static List<Map<String, Object>> versionRows() {
            Map<String, Object> row = new HashMap<>();
            row.put("1", "130003");
            return Collections.singletonList(row);
        }

        private static ResultSet resultSet(List<Map<String, Object>> rows) {
            ClassLoader loader = CrdbMetaProviderDmTest.class.getClassLoader();
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
                Object key = args[0];
                Object value = currentRow().get(String.valueOf(key));
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
            ClassLoader loader = CrdbMetaProviderDmTest.class.getClassLoader();
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
