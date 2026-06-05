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
package com.clougence.clouddm.dsfamily.execute;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

import com.clougence.schema.umi.service.RdbTableUtils;
import com.clougence.schema.umi.service.RdbTableUtilsInfo;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.function.ESupplier;

/**
 * MetadataSupplier 系列的公共类。
 * 
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class AbstractMetadataProvider {

    protected static final int                          DEFAULT_GROUP_SIZE = 500;
    protected final ESupplier<Connection, SQLException> connectSupplier;
    private String                                      version;

    /** Connection will be proxy, Calling the close method in an AbstractMetadatasupplier subclass will be invalid. */
    public AbstractMetadataProvider(Connection connection){
        Connection conn = newProxyConnection(connection);
        this.connectSupplier = () -> conn;
    }

    public String getVersion() throws SQLException {
        if (this.version == null) {
            try (Connection conn = this.connectSupplier.eGet()) {
                DatabaseMetaData metaData = conn.getMetaData();
                this.version = metaData.getDatabaseProductVersion();
            }
        }
        return this.version;
    }

    protected Map<String, Integer> extractColumn(ResultSetMetaData resultSetMetaData) throws SQLException {
        Map<String, Integer> columnInfo = new LinkedHashMap<>();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String name = resultSetMetaData.getColumnLabel(i);
            if (name == null || name.length() < 1) {
                name = resultSetMetaData.getColumnName(i);
            }
            columnInfo.put(name, i);
        }
        return columnInfo;
    }

    protected static Connection newProxyConnection(Connection connection) {
        CloseIsNothingInvocationHandler handler = new CloseIsNothingInvocationHandler(connection);
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class, Closeable.class }, handler);
    }

    private static class CloseIsNothingInvocationHandler implements InvocationHandler {

        private final Connection connection;

        CloseIsNothingInvocationHandler(Connection connection){
            this.connection = connection;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            switch (method.getName()) {
                case "getTargetConnection":
                    return connection;
                case "toString":
                    return this.connection.toString();
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "close":
                    return null;
            }

            try {
                return method.invoke(this.connection, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    protected Integer tryWasNull(int value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    protected Boolean tryWasNull(boolean value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    protected Long tryWasNull(long value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return value;
        }
    }

    protected Integer tryWasNull(short value, ResultSet record) throws SQLException {
        if (record.wasNull()) {
            return null;
        } else {
            return (int) value;
        }
    }

    protected static String safeToString(Object obj) {
        return (obj == null) ? null : obj.toString();
    }

    protected static Integer safeToInteger(Object obj) {
        return (obj == null) ? null : (Integer) ConverterUtils.convert(Integer.class, obj);
    }

    protected static int safeToInt(Object obj) {
        return (obj == null) ? 0 : (Integer) ConverterUtils.convert(Integer.TYPE, obj);
    }

    protected static Long safeToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return (Long) ConverterUtils.convert(Long.class, obj);
    }

    protected static Boolean safeToBoolean(Object obj) {
        return (obj == null) ? null : (Boolean) ConverterUtils.convert(Boolean.class, obj);
    }

    protected static Date safeToDate(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = ((ZonedDateTime) obj);
            return Timestamp.from(zonedDateTime.toInstant());
        } else if (obj instanceof OffsetDateTime) {
            ZonedDateTime zonedDateTime = ((OffsetDateTime) obj).atZoneSameInstant(ZoneOffset.systemDefault());
            return Timestamp.from(zonedDateTime.toInstant());
        } else if (obj instanceof OffsetTime) {
            ZonedDateTime zonedDateTime = ((OffsetTime) obj).atDate(LocalDate.ofEpochDay(0)).atZoneSameInstant(ZoneOffset.UTC);
            return Timestamp.from(zonedDateTime.toInstant());
        } else if (obj instanceof LocalDateTime) {
            return Timestamp.valueOf((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            LocalDateTime dateTime = LocalDateTime.of((LocalDate) obj, LocalTime.of(0, 0, 0, 0));
            return Timestamp.valueOf(dateTime);
        } else if (obj instanceof Number) {
            return new Date(((Number) obj).longValue());
        } else {
            throw new ClassCastException(obj.getClass() + " Type cannot be converted to Date");
        }
    }

    protected static String buildWhereIn(Collection<?> paramMap) {
        StringBuilder whereIn = new StringBuilder();
        whereIn.append("(");
        whereIn.append(StringUtils.repeat("?,", paramMap.size()));
        whereIn.deleteCharAt(whereIn.length() - 1);
        whereIn.append(")");
        return whereIn.toString();
    }

    protected static String buildWhereIn(int size) {
        StringBuilder whereIn = new StringBuilder();
        whereIn.append("(");
        whereIn.append(StringUtils.repeat("?,", size));
        whereIn.deleteCharAt(whereIn.length() - 1);
        whereIn.append(")");
        return whereIn.toString();
    }

    protected static List<String> stringArray2List(List<String> stringArray) {
        if (stringArray == null || stringArray.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<String> stringList = new ArrayList<>();
        for (String string : stringArray) {
            if (StringUtils.isNotBlank(string)) {
                stringList.add(string);
            }
        }
        return stringList;
    }

    protected int defaultGroupSize() {
        return DEFAULT_GROUP_SIZE;
    }

    public List<RdbTable> loadTables(String catalog, String schema, List<String> tables) throws SQLException {
        tables = stringArray2List(tables);
        if (tables.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbTable> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> tabs : CollectionUtils.splitList(tables, defaultGroupSize())) {
                List<RdbTable> rdbTables = this.fetchTableByPart(conn, catalog, schema, tabs);
                if (CollectionUtils.isEmpty(rdbTables)) {
                    continue;
                }
                RdbTableUtilsInfo batchInfo = new RdbTableUtilsInfo();
                batchInfo.setAllColumns(this.fetchTableColumns(conn, catalog, schema, tabs));
                batchInfo.setPkUkMap(this.fetchPrimaryUnique(conn, catalog, schema, tabs));
                batchInfo.setFkMap(this.fetchForeignKeys(conn, catalog, schema, tabs));
                batchInfo.setIndexMap(this.fetchIndexes(conn, catalog, schema, tabs));
                batchInfo.setPartitionMap(this.fetchTablePartition(conn, catalog, schema, tabs));
                batchInfo.setConstraintMap(this.fetchTableConstraints(conn, catalog, schema, tabs));
                result.addAll(RdbTableUtils.fillRdbTable(rdbTables, batchInfo));
            }
        }
        return result;
    }

    public Value loadSelectObject(String catalog, String schema, String table) throws SQLException {
        List<String> tables = stringArray2List(Collections.singletonList(table));
        if (tables.isEmpty()) {
            return null;
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            List<RdbTable> rdbTables = this.fetchSelectObjectByPart(conn, catalog, schema, tables);
            if (CollectionUtils.isEmpty(rdbTables)) {
                return null;
            }
            RdbTable result = rdbTables.get(0);
            Map<String, List<RdbColumn>> allColumns = this.fetchTableColumns(conn, catalog, schema, tables);
            List<RdbColumn> rdbColumns = allColumns.get(table);
            Map<String, RdbColumn> columnHashMap = new HashMap<>();
            for (RdbColumn rdbColumn : rdbColumns) {
                columnHashMap.put(rdbColumn.getName(), rdbColumn);
            }
            result.setColumns(columnHashMap);
            return result;
        }
    }

    public List<RdbTable> loadViews(String catalog, String schema, List<String> views) throws SQLException {
        views = stringArray2List(views);
        if (views.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbTable> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> tabs : CollectionUtils.splitList(views, defaultGroupSize())) {
                List<RdbTable> rdbViews = this.fetchViewByPart(conn, catalog, schema, tabs);
                if (CollectionUtils.isEmpty(rdbViews)) {
                    continue;
                }
                RdbTableUtilsInfo batchInfo = new RdbTableUtilsInfo();
                batchInfo.setAllColumns(this.fetchViewColumns(conn, catalog, schema, tabs));
                batchInfo.setPkUkMap(this.fetchPrimaryUnique(conn, catalog, schema, tabs));
                batchInfo.setFkMap(this.fetchForeignKeys(conn, catalog, schema, tabs));
                batchInfo.setIndexMap(this.fetchIndexes(conn, catalog, schema, tabs));
                result.addAll(RdbTableUtils.fillRdbTable(rdbViews, batchInfo));
            }
        }
        return result;
    }

    public List<RdbTable> loadMaterialized(String catalog, String schema, List<String> views) throws SQLException {
        views = stringArray2List(views);
        if (views.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbTable> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> tabs : CollectionUtils.splitList(views, defaultGroupSize())) {
                List<RdbTable> rdbViews = this.fetchMaterializedByPart(conn, catalog, schema, tabs);
                if (CollectionUtils.isEmpty(rdbViews)) {
                    continue;
                }
                RdbTableUtilsInfo batchInfo = new RdbTableUtilsInfo();
                batchInfo.setAllColumns(this.fetchMaterializedColumns(conn, catalog, schema, tabs));
                result.addAll(RdbTableUtils.fillRdbTable(rdbViews, batchInfo));
            }
        }
        return result;
    }

    public Map<String, List<RdbColumn>> loadColumns(String catalog, String schema, List<String> tables) throws SQLException {
        tables = stringArray2List(tables);
        if (tables.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<RdbColumn>> result = new HashMap<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> tabs : CollectionUtils.splitList(tables, defaultGroupSize())) {
                List<RdbTable> tabList = tables.stream().map(s -> {
                    RdbTable tab = new RdbTable();
                    tab.setSchema(schema);
                    tab.setName(s);
                    return tab;
                }).collect(Collectors.toList());

                RdbTableUtilsInfo batchInfo = new RdbTableUtilsInfo();
                batchInfo.setAllColumns(this.fetchTableColumns(conn, catalog, schema, tabs));
                batchInfo.setPkUkMap(this.fetchPrimaryUnique(conn, catalog, schema, tabs));
                batchInfo.setFkMap(this.fetchForeignKeys(conn, catalog, schema, tabs));
                batchInfo.setIndexMap(this.fetchIndexes(conn, catalog, schema, tabs));

                List<RdbTable> tableList = RdbTableUtils.fillRdbTable(tabList, batchInfo);
                for (RdbTable tab : tableList) {
                    Map<String, RdbColumn> columns = tab.getColumns();
                    List<RdbColumn> rdbColumns = columns.values().stream().sorted(Comparator.comparingInt(RdbColumn::getIndex)).collect(Collectors.toList());
                    result.put(tab.asValue(), rdbColumns);
                }
            }
        }
        return result;
    }

    protected abstract List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected abstract Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException;

    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        throw new UnsupportedOperationException();
    }

    protected Map<String, RdbPartition> fetchTablePartition(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyMap();
    }

    protected Map<String, List<ConstraintObject>> fetchTableConstraints(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyMap();
    }

    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return fetchTableColumns(conn, catalog, schema, tabs);
    }

    protected Map<String, List<RdbColumn>> fetchMaterializedColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return fetchTableColumns(conn, catalog, schema, tabs);
    }
}
