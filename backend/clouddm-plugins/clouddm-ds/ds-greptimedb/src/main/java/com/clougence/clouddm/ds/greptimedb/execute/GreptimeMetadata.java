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

package com.clougence.clouddm.ds.greptimedb.execute;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.greptimedb.metadata.GreptimeFieldType;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.Primary;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.EFunction;

final class GreptimeMetadata {

    private static final String  SCHEMATA       = """
            SELECT schema_name, options
            FROM information_schema.schemata
            """;
    private static final String  RELATIONS      = """
            SELECT table_id, table_name, table_type, engine, table_comment
            FROM information_schema.tables
            WHERE table_schema = ?
            """;
    private static final String  COLUMNS        = """
            SELECT table_name, column_name, ordinal_position, is_nullable, column_type, semantic_type,
                   column_key, column_comment, column_default, character_maximum_length,
                   numeric_precision, numeric_scale, datetime_precision
            FROM information_schema.columns
            WHERE table_schema = ? AND table_name IN (%s)
            ORDER BY table_name, ordinal_position
            """;
    private static final String  INDEXES        = """
            SELECT index_name, column_name, index_type, non_unique, index_comment, greptime_index_type
            FROM information_schema.statistics
            WHERE table_schema = ? AND table_name = ?
            ORDER BY index_name, seq_in_index
            """;
    private static final String  VIEW           = "VIEW";
    private static final Pattern TIMESTAMP_TYPE = Pattern.compile("Timestamp\\((s|ms|us|ns)(?:, .+)?\\)");
    private static final Pattern DECIMAL_TYPE   = Pattern.compile("Decimal(?:128|256)\\((\\d+),\\s*(-?\\d+)\\)");

    private final Connection     connection;

    GreptimeMetadata(Connection connection){
        this.connection = connection;
    }

    List<DsElement> databases(String database) throws SQLException {
        String sql = SCHEMATA + "ORDER BY schema_name";
        List<String> args = List.of();
        if (database != null) {
            sql = SCHEMATA + "WHERE schema_name = ?";
            args = List.of(database);
        }
        return query(sql, args, rows -> {
            List<DsElement> databases = new ArrayList<>();
            while (rows.next()) {
                DsElement element = element(UmiTypes.Schema, rows.getString("schema_name"), -1);
                element.getObjAttr().put("options", rows.getString("options"));
                databases.add(element);
            }
            return databases;
        });
    }

    List<DsElement> list(String database, UmiTypes type, String pattern) throws SQLException {
        String sql = RELATIONS;
        if (type == UmiTypes.View) {
            sql += "AND table_type = 'VIEW' ";
        } else {
            sql += "AND table_type IN ('BASE TABLE', 'LOCAL TEMPORARY') ";
        }

        List<String> args = new ArrayList<>();
        args.add(database);
        if (StringUtils.isNotBlank(pattern)) {
            sql += "AND table_name ILIKE ? ";
            args.add(pattern);
        }
        return query(sql + "ORDER BY table_name", args, rows -> {
            List<DsElement> objects = new ArrayList<>();
            while (rows.next()) {
                DsElement element = element(type, rows.getString("table_name"), rows.getLong("table_id"));
                element.getObjAttr().put(RdbAttributeNames.COMMENT.getCodeKey(), rows.getString("table_comment"));
                element.getObjAttr().put("engine", rows.getString("engine"));
                element.getObjAttr().put("tableType", rows.getString("table_type"));
                objects.add(element);
            }
            return objects;
        });
    }

    RdbTable detail(String database, UmiTypes type, String name) throws SQLException {
        RdbTable table = query(RELATIONS + "AND table_name = ?", List.of(database, name), rows -> {
            if (!rows.next()) {
                return null;
            }
            String tableType = rows.getString("table_type");
            RdbTable detail;
            if (VIEW.equals(tableType)) {
                detail = new RdbView();
            } else {
                detail = new RdbTable();
            }
            if (type != null && type != detail.getUmiType()) {
                return null;
            }
            detail.setSchema(database);
            detail.setName(name);
            detail.setTableType(tableType);
            detail.setComment(rows.getString("table_comment"));
            detail.setAttribute(RdbAttributeNames.RDB_OBJ_ID, rows.getString("table_id"));
            detail.setAttribute("engine", rows.getString("engine"));
            return detail;
        });

        if (table == null) {
            return null;
        }

        List<RdbColumn> columns = columns(database, List.of(name)).getOrDefault(name, List.of());
        if (table instanceof RdbView view) {
            if (columns.isEmpty()) {
                columns = viewColumns(database, name);
            }
            view.setSql(createScript(database, UmiTypes.View, name).stream().findFirst().orElse(""));
        } else {
            readIndexes(table);
        }
        columns.forEach(table::addColumn);
        return table;
    }

    Map<String, List<RdbColumn>> batchColumns(String database, UmiTypes type, List<String> names) throws SQLException {
        Map<String, List<RdbColumn>> result = columns(database, names);
        if (type == UmiTypes.View) {
            String sql = RELATIONS + "AND table_type = 'VIEW' AND table_name IN (" + placeholders(names.size()) + ")";
            List<String> views = query(sql, tableArgs(database, names), rows -> {
                List<String> existing = new ArrayList<>();
                while (rows.next()) {
                    existing.add(rows.getString("table_name"));
                }
                return existing;
            });
            result.keySet().retainAll(views);
            for (String view : views) {
                if (!result.containsKey(view)) {
                    result.put(view, viewColumns(database, view));
                }
            }
        }
        return result;
    }

    List<String> createScript(String database, UmiTypes type, String name) throws SQLException {
        String target = MySqlDialect.INSTANCE.fmtTableName(true, null, database, name);
        String sql = switch (type) {
            case Schema -> "SHOW CREATE DATABASE " + MySqlDialect.INSTANCE.fmtName(true, database);
            case Table -> "SHOW CREATE TABLE " + target;
            case View -> "SHOW CREATE VIEW " + target;
            default -> throw new SQLException("SHOW CREATE is unsupported for " + type);
        };
        try (Statement statement = connection.createStatement(); ResultSet rows = statement.executeQuery(sql)) {
            return rows.next() ? List.of(rows.getString(2)) : List.of();
        }
    }

    private Map<String, List<RdbColumn>> columns(String database, List<String> names) throws SQLException {
        return query(COLUMNS.formatted(placeholders(names.size())), tableArgs(database, names), rows -> {
            Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
            while (rows.next()) {
                String name = rows.getString("table_name");
                String nativeType = rows.getString("column_type");
                RdbColumn column = new RdbColumn();
                column.setSchema(database);
                column.setTable(name);
                column.setName(rows.getString("column_name"));
                column.setIndex(rows.getInt("ordinal_position"));
                column.setComment(rows.getString("column_comment"));
                column.setDefaultValue(rows.getString("column_default"));
                column.setCharLength(rows.getObject("character_maximum_length", Long.class));
                column.setNumericPrecision(rows.getObject("numeric_precision", Integer.class));
                column.setNumericScale(rows.getObject("numeric_scale", Integer.class));
                column.setDatetimePrecision(rows.getObject("datetime_precision", Integer.class));
                column.setNumericUnsigned(nativeType.endsWith(" unsigned"));
                column.setSqlType(GreptimeFieldType.valueOfCode(nativeType));
                column.setAttribute(RdbAttributeNames.NULLABLE, Boolean.toString("YES".equals(rows.getString("is_nullable"))));
                column.setAttribute(RdbAttributeNames.COLUMN_ROLE, rows.getString("semantic_type"));
                if ("PRI".equals(rows.getString("column_key"))) {
                    Primary primary = new Primary();
                    primary.setName("PRIMARY");
                    column.addConstraint(primary);
                }
                result.computeIfAbsent(name, ignored -> new ArrayList<>()).add(column);
            }
            return result;
        });
    }

    private List<RdbColumn> viewColumns(String database, String name) throws SQLException {
        String target = MySqlDialect.INSTANCE.fmtTableName(true, null, database, name);
        String emptyView = "SELECT * FROM " + target + " LIMIT 0";
        try (Statement statement = connection.createStatement()) {
            List<RdbColumn> columns = new ArrayList<>();
            List<String> types = new ArrayList<>();
            try (ResultSet rows = statement.executeQuery(emptyView)) {
                ResultSetMetaData metadata = rows.getMetaData();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    RdbColumn column = new RdbColumn();
                    column.setSchema(database);
                    column.setTable(name);
                    column.setName(metadata.getColumnLabel(i));
                    column.setIndex(i);
                    columns.add(column);
                    types.add("arrow_typeof(v." + MySqlDialect.INSTANCE.fmtName(true, column.getName()) + ")");
                }
            }
            // The MySQL result header loses GreptimeDB's timestamp and decimal precision.
            // The left join supplies a single typed row of NULLs without reading view data.
            String sql = "SELECT " + String.join(", ", types) + " FROM (SELECT 1) AS seed LEFT JOIN (" + emptyView + ") AS v ON TRUE";
            try (ResultSet rows = statement.executeQuery(sql)) {
                if (rows.next()) {
                    for (RdbColumn column : columns) {
                        setViewType(column, rows.getString(column.getIndex()));
                    }
                }
            }
            return columns;
        }
    }

    private static void setViewType(RdbColumn column, String arrowType) {
        Matcher timestamp = TIMESTAMP_TYPE.matcher(arrowType);
        Matcher decimal = DECIMAL_TYPE.matcher(arrowType);
        String nativeType;
        if (timestamp.matches()) {
            int precision = switch (timestamp.group(1)) {
                case "ns" -> 9;
                case "us" -> 6;
                case "ms" -> 3;
                default -> 0;
            };
            column.setDatetimePrecision(precision);
            nativeType = "timestamp";
        } else if (decimal.matches()) {
            int precision = Integer.parseInt(decimal.group(1));
            int scale = Integer.parseInt(decimal.group(2));
            column.setNumericPrecision(precision);
            column.setNumericScale(scale);
            nativeType = "decimal";
        } else {
            nativeType = switch (arrowType) {
                case "Boolean" -> "boolean";
                case "Int8" -> "tinyint";
                case "Int16" -> "smallint";
                case "Int32" -> "int";
                case "Int64" -> "bigint";
                case "UInt8" -> "tinyint unsigned";
                case "UInt16" -> "smallint unsigned";
                case "UInt32" -> "int unsigned";
                case "UInt64" -> "bigint unsigned";
                case "Float32" -> "float";
                case "Float64" -> "double";
                case "Utf8", "Utf8View", "LargeUtf8", "Dictionary(UInt32, Utf8)" -> "string";
                case "Binary", "BinaryView", "LargeBinary" -> "binary";
                case "Date32", "Date64" -> "date";
                default -> arrowType;
            };
        }
        column.setNumericUnsigned(nativeType.endsWith(" unsigned"));
        column.setSqlType(GreptimeFieldType.valueOfCode(nativeType));
    }

    private void readIndexes(RdbTable table) throws SQLException {
        Map<String, RdbIndex> indexes = query(INDEXES, List.of(table.getSchema(), table.getName()), rows -> {
            Map<String, RdbIndex> result = new LinkedHashMap<>();
            while (rows.next()) {
                String name = rows.getString("index_name");
                RdbIndex index = result.get(name);
                if (index == null) {
                    index = new RdbIndex();
                    index.setSchema(table.getSchema());
                    index.setTable(table.getName());
                    index.setIndexedObjectType(UmiTypes.Table);
                    index.setName(name);
                    index.setType(rows.getInt("non_unique") == 0 ? RdbIndexType.Unique : RdbIndexType.Normal);
                    index.setComment(rows.getString("index_comment"));
                    String indexType = rows.getString("index_type");
                    index.setAttribute(RdbAttributeNames.INDEX_TYPE, indexType);
                    index.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, indexType);
                    index.setAttribute("greptimeIndexType", rows.getString("greptime_index_type"));
                    result.put(name, index);
                }
                index.addColumn(rows.getString("column_name"));
            }
            return result;
        });
        for (RdbIndex index : indexes.values()) {
            if ("PRIMARY".equals(index.getAttribute(RdbAttributeNames.INDEX_TYPE))) {
                RdbPrimaryKey key = new RdbPrimaryKey();
                key.setName(index.getName());
                key.setColumnList(index.getColumnList());
                key.merge(index);
                table.setPrimaryKey(key);
            } else {
                table.addIndex(index);
            }
        }
    }

    private static DsElement element(UmiTypes type, String name, long id) {
        DsElement element = new DsElement();
        element.setObjId(id);
        element.setObjName(name);
        element.setObjType(type);
        element.setObjAttr(new LinkedHashMap<>());
        return element;
    }

    private static String placeholders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }

    private static List<String> tableArgs(String database, List<String> names) {
        List<String> args = new ArrayList<>(names.size() + 1);
        args.add(database);
        args.addAll(names);
        return args;
    }

    private <T> T query(String sql, List<String> args, EFunction<ResultSet, T, SQLException> read) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.size(); i++) {
                statement.setString(i + 1, args.get(i));
            }
            try (ResultSet rows = statement.executeQuery()) {
                return read.eApply(rows);
            }
        }
    }
}
