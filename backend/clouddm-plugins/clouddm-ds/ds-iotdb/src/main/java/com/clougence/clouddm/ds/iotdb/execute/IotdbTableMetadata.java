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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.metadata.IotdbFieldType;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbView;
import com.clougence.schema.umi.struts.UmiTypes;

final class IotdbTableMetadata {

    private static final String TREE_VIEW = "VIEW FROM TREE";

    private IotdbTableMetadata(){
    }

    static List<DsElement> list(Statement statement, String database, UmiTypes type, String pattern) throws SQLException {
        Pattern names = IotdbMetadataRows.namePattern(pattern);
        List<DsElement> elements = new ArrayList<>();
        for (Map<String, String> row : tables(statement, database).values()) {
            String name = row.get("tablename");
            if (objectType(row) == type && names.matcher(name).matches()) {
                elements.add(IotdbMetadataRows.element(type, name, IotdbSqlDialect.TABLE, row));
            }
        }
        return elements;
    }

    static RdbTable detail(Statement statement, String database, UmiTypes type, String name) throws SQLException {
        Map<String, String> row = tables(statement, database).get(name);
        if (row == null || (type != null && type != objectType(row))) {
            return null;
        }

        RdbTable detail;
        if (objectType(row) == UmiTypes.View) {
            RdbView view = new RdbView();
            if (TREE_VIEW.equals(row.get("tabletype"))) {
                List<Map<String, String>> source = IotdbMetadataRows.read(statement, "SHOW CREATE VIEW " + quote(database) + "." + quote(name));
                if (!source.isEmpty()) {
                    view.setSql(source.get(0).get("create view"));
                }
            }
            detail = view;
        } else {
            detail = new RdbTable();
        }

        detail.setCatalog(IotdbSqlDialect.TABLE.name());
        detail.setSchema(database);
        detail.setName(name);
        detail.setTableType(row.get("tabletype"));
        detail.setComment(row.get("comment"));
        detail.merge(row);
        detail.setAttribute("model", IotdbSqlDialect.TABLE.name());
        columns(statement, database, name).forEach(detail::addColumn);
        return detail;
    }

    static Map<String, List<RdbColumn>> batchColumns(Statement statement, String database, UmiTypes type, List<String> names) throws SQLException {
        Map<String, Map<String, String>> objects = tables(statement, database);
        Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
        for (String name : names) {
            Map<String, String> row = objects.get(name);
            if (row != null && objectType(row) == type && !result.containsKey(name)) {
                result.put(name, columns(statement, database, name));
            }
        }
        return result;
    }

    private static List<RdbColumn> columns(Statement statement, String database, String name) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        for (Map<String, String> row : IotdbMetadataRows.read(statement, "DESCRIBE " + quote(database) + "." + quote(name) + " DETAILS")) {
            RdbColumn column = new RdbColumn();
            column.setCatalog(IotdbSqlDialect.TABLE.name());
            column.setSchema(database);
            column.setTable(name);
            column.setName(row.get("columnname"));
            column.setIndex(columns.size() + 1);
            column.setSqlType(IotdbFieldType.valueOfCode(row.get("datatype")));
            column.setComment(row.get("comment"));
            column.merge(row);
            column.setAttribute("columnRole", row.get("category"));
            columns.add(column);
        }
        return columns;
    }

    private static Map<String, Map<String, String>> tables(Statement statement, String database) throws SQLException {
        Map<String, Map<String, String>> tables = new LinkedHashMap<>();
        for (Map<String, String> row : IotdbMetadataRows.read(statement, "SHOW TABLES DETAILS FROM " + quote(database))) {
            row.put("database", database);
            if (objectType(row) == UmiTypes.View) {
                row.put("readOnly", "true");
            }
            tables.put(row.get("tablename"), row);
        }
        return tables;
    }

    private static UmiTypes objectType(Map<String, String> row) {
        String kind = row.get("tabletype");
        if (kind != null && kind.contains("VIEW")) {
            return UmiTypes.View;
        }
        return UmiTypes.Table;
    }

    private static String quote(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}
