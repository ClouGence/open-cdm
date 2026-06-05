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
package com.clougence.clouddm.dsfamily.postgres.execute;

import static com.clougence.adapter.postgre.PostgreAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.postgre.PostgresForeignKeyRule;
import com.clougence.adapter.postgre.PostgresForeignMatchOption;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.postgre.driver.PgServerVersion;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiAttributeNames;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

/**
 * @Author: Ekko
 * @Date: 2023-12-20 15:16
 */
public class PgMetaProviderUtils {

    protected List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("nspname"));
            schema.setUmiType(UmiTypes.Schema);

            result.add(schema);
        }
        return result;
    }

    protected List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("table_name"));

            String tableType = rs.getString("table_type");
            if (StringUtils.equalsIgnoreCase(tableType, "v")) {
                schema.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "r") || StringUtils.equalsIgnoreCase(tableType, "p")) {
                schema.setUmiType(UmiTypes.Table);
            } else if (StringUtils.equalsIgnoreCase(tableType, "m")) {
                schema.setUmiType(UmiTypes.Materialized);
            } else {
                schema.setUmiType(null);
            }

            schema.setAttribute(COMMENT, rs.getString("comment"));

            result.add(schema);
        }
        return result;
    }

    protected List<Value> convertProcedure(ResultSet rs) throws SQLException {
        Map<String, RdbValue> map = new HashMap<>();
        while (rs.next()) {
            RdbValue schema;
            String oid = rs.getString("oid");
            schema = map.get(oid);
            if (schema == null) {
                schema = new RdbValue();
                schema.setValue(rs.getString("proname"));
                schema.setUmiType(UmiTypes.Procedure);
                schema.setAttribute(RDB_OBJ_ID, rs.getString("oid"));
                map.put(oid, schema);
            }
            String attribute = schema.getAttribute(OBJ_UI_TIPS);
            String paramType = rs.getString("paramType");
            if (StringUtils.isNotBlank(paramType)) {
                if (StringUtils.isNotBlank(attribute)) {
                    schema.setAttribute(OBJ_UI_TIPS, attribute + ",[" + paramType + "]");
                } else {
                    schema.setAttribute(OBJ_UI_TIPS, "[" + paramType + "]");
                }
            }
        }
        // order by name
        return map.values().stream().sorted((a, b) -> a.getValue().compareTo(b.getValue())).collect(Collectors.toList());
    }

    public List<Value> convertFunction(ResultSet rs) throws SQLException {
        Map<String, RdbValue> map = new HashMap<>();
        while (rs.next()) {
            RdbValue schema;
            String oid = rs.getString("oid");
            schema = map.get(oid);
            if (schema == null) {
                schema = new RdbValue();
                schema.setValue(rs.getString("proname"));
                schema.setUmiType(UmiTypes.Function);
                schema.setAttribute(RDB_OBJ_ID, rs.getString("oid"));
                String returnType = rs.getString("returnType");
                if (StringUtils.isNotBlank(returnType)) {
                    schema.setAttribute(FUNC_RETURN_TYPE, returnType);
                }
                map.put(oid, schema);
            }
            String attribute = schema.getAttribute(OBJ_UI_TIPS);
            String paramType = rs.getString("paramType");
            if (StringUtils.isNotBlank(paramType)) {
                if (StringUtils.isNotBlank(attribute)) {
                    schema.setAttribute(OBJ_UI_TIPS, attribute + "," + "[" + paramType + "]");
                } else {
                    schema.setAttribute(OBJ_UI_TIPS, "[" + paramType + "]");
                }
            }
        }

        // order by name
        return map.values().stream().sorted((a, b) -> a.getValue().compareTo(b.getValue())).collect(Collectors.toList());
    }

    protected List<Value> convertTrigger(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue val = new RdbValue();
            val.setValue(rs.getString("name"));
            val.setUmiType(UmiTypes.Trigger);
            // PG can have triggers with the same name for different tables
            val.setAttribute(TRIGGER_TABLE, rs.getString("relname"));
            val.setAttribute(RDB_OBJ_ID, rs.getString("oid"));

            result.add(val);
        }
        return result;
    }

    protected List<Value> convertSequence(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("relname"));
            schema.setUmiType(UmiTypes.Sequence);

            result.add(schema);
        }
        return result;
    }

    protected void mapToUkExt(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("index_name");
        String columnName = rs.getString("COLUMN_NAME");
        RdbUniqueKey uk = (RdbUniqueKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbUniqueKey()));
        uk.setName(consName);
        uk.setComment(rs.getString("description"));
        //Prefix length and columnName
        //error: if column rename  SUBSTRING("columnName" FROM 1 FOR 30)
        HashMap<String, String> subPartMap = new HashMap<>();
        if (columnName.contains("SUBSTRING")) {
            String substring = columnName.substring(10);
            String[] splitStr = substring.split(" ");
            columnName = splitStr[0].replace("\"", "");
            String subPart = null;
            for (int i = 1; i < splitStr.length; i++) {
                if (splitStr[i].equals("FOR") && i + 1 < splitStr.length) {
                    subPart = splitStr[i + 1].replace(")", "");
                    break;
                }
            }
            subPartMap.put(columnName, subPart);
        } else {
            subPartMap.put(columnName, null);
        }
        uk.addColumn(columnName);
        String newSubPartJson = JsonUtils.toJson(subPartMap);

        //order
        HashMap<String, String> orderMap = new HashMap<>();
        String ascOrDesc = rs.getString("asc_or_desc");
        if (StringUtils.isBlank(ascOrDesc)) {
            ascOrDesc = "";
        }
        orderMap.put(columnName, ascOrDesc);
        String newOrderMapJson = JsonUtils.toJson(orderMap);

        //sort rules
        String collationName = rs.getString("COLLATION_NAME");
        String collationSchemaName = rs.getString("collation_schema_name");
        HashMap<String, String> sortRulesMap = new HashMap<>();
        if (collationName != null && collationSchemaName != null) {
            sortRulesMap.put(columnName, collationSchemaName + "." + collationName);
        } else {
            sortRulesMap.put(columnName, null);
        }
        String newSortRulesJson = JsonUtils.toJson(sortRulesMap);

        //Operator Category
        String opcname = rs.getString("opcname");
        String opcnameSchemaName = rs.getString("opcname_schema_name");
        HashMap<String, String> opcnameMap = new HashMap<>();
        if (opcname != null && opcnameSchemaName != null) {
            opcnameMap.put(columnName, opcnameSchemaName + "." + opcname);
        } else {
            opcnameMap.put(columnName, null);
        }
        String newOpcNameJson = JsonUtils.toJson(opcnameMap);

        //nulls sort
        HashMap<String, String> nullsFirstMap = new HashMap<>();
        String nullsFirst = rs.getString("nullsfirst");
        if (nullsFirst != null) {
            if (nullsFirst.equals("f")) {
                nullsFirst = "NULLS LAST";
            } else {
                nullsFirst = "NULLS FIRST";
            }
            nullsFirstMap.put(columnName, nullsFirst);
        }
        String newNullsFirstJson = JsonUtils.toJson(nullsFirstMap);

        //att collation
        HashMap<String, String> attCollationMap = new HashMap<>();
        String attCollation = rs.getString("attcollation");
        attCollationMap.put(columnName, attCollation);
        String newAttCollationJSON = JsonUtils.toJson(attCollationMap);

        if (tabConstraint.containsKey(consName)) {
            RdbUniqueKey oldUk = (RdbUniqueKey) tabConstraint.get(consName);
            if (oldUk.getAttributes().containsKey(INDEX_PREFIX_LENGTH.getCodeKey())) {
                String oldSubPartJson = oldUk.getAttribute(INDEX_PREFIX_LENGTH);
                String resultJson = mergeJsonMap(oldSubPartJson, newSubPartJson);
                uk.setAttribute(INDEX_PREFIX_LENGTH, resultJson);
            } else {
                uk.setAttribute(INDEX_PREFIX_LENGTH, newSubPartJson);
            }

            //if (oldUk.getAttributes().containsKey(INDEX_SORT_ORDER.getCodeKey())) {
            //    String oldOrderMapJson = oldUk.getAttribute(INDEX_SORT_ORDER);
            //    String resultJson = mergeJsonMap(oldOrderMapJson, newOrderMapJson);
            //    uk.setAttribute(INDEX_SORT_ORDER, resultJson);
            //} else {
            //    uk.setAttribute(INDEX_SORT_ORDER, newOrderMapJson);
            //}

            //if (oldUk.getAttributes().containsKey(INDEX_NULLS_SORT.getCodeKey())) {
            //    String oldNullsFirstJson = oldUk.getAttribute(INDEX_NULLS_SORT);
            //    String resultJson = mergeJsonMap(oldNullsFirstJson, newNullsFirstJson);
            //    uk.setAttribute(INDEX_NULLS_SORT, resultJson);
            //} else {
            //    uk.setAttribute(INDEX_NULLS_SORT, newNullsFirstJson);
            //}

            // if (oldUk.getAttributes().containsKey(INDEX_OPERATOR_TYPE.getCodeKey())) {
            //     String oldOpcNameJson = oldUk.getAttribute(INDEX_OPERATOR_TYPE);
            //     String resultJson = mergeJsonMap(oldOpcNameJson, newOpcNameJson);
            //     uk.setAttribute(INDEX_OPERATOR_TYPE, resultJson);
            // } else {
            //     uk.setAttribute(INDEX_OPERATOR_TYPE, newOpcNameJson);
            // }

            // if (oldUk.getAttributes().containsKey(INDEX_SORT_RULES.getCodeKey())) {
            //     String oldSortRulesJson = oldUk.getAttribute(INDEX_SORT_RULES);
            //     String resultJson = mergeJsonMap(oldSortRulesJson, newSortRulesJson);
            //     uk.setAttribute(INDEX_SORT_RULES, resultJson);
            // } else {
            //     uk.setAttribute(INDEX_SORT_RULES, newSortRulesJson);
            // }

        } else {
            uk.setAttribute(INDEX_PREFIX_LENGTH, newSubPartJson);
            uk.setAttribute(INDEX_SORT_ORDER, newOrderMapJson);
            uk.setAttribute(INDEX_NULLS_SORT, newNullsFirstJson);
            uk.setAttribute(INDEX_OPERATOR_TYPE, newOpcNameJson);
            uk.setAttribute(INDEX_SORT_RULES, newSortRulesJson);
        }

        uk.setAttribute(INDEX_TYPE, rs.getString("am_name"));
        uk.setAttribute(INDEX_WAY, "Unique");
        if (rs.getBoolean("DEFERRABLE")) {
            uk.setAttribute(INDEX_CONSTRAINT_DELAY, "DEFERRABLE");
            uk.setAttribute(INDEX_CONSTRAINT_INITIAL, rs.getString("initially"));
        } else {
            uk.setAttribute(INDEX_CONSTRAINT_DELAY, "NOT DEFERRABLE");
        }
    }

    protected void mapToPkExt(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("index_name");
        String columnName = rs.getString("COLUMN_NAME");
        RdbPrimaryKey pk = (RdbPrimaryKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbPrimaryKey()));
        pk.setName(consName);
        //Prefix length and columnName
        HashMap<String, String> subPartMap = new HashMap<>();
        if (columnName.contains("SUBSTRING")) {
            String substring = columnName.substring(10);
            String[] splitStr = substring.split(" ");
            columnName = splitStr[0].replace("\"", "");;
        }
        pk.addColumn(columnName);
        pk.setAttribute(INDEX_TYPE, rs.getString("am_name"));
        if (rs.getBoolean("deferrable")) {
            pk.setAttribute(PK_CONSTRAINT_DELAY, "DEFERRABLE");
            //pk.setAttribute(PK_CONSTRAINT_INITIAL, rs.getString("initially"));
        } else {
            pk.setAttribute(PK_CONSTRAINT_DELAY, "NOT DEFERRABLE");
        }
    }

    protected void fillIdxMapExt(Map<String, Map<String, RdbIndex>> indexesMap, List<RdbIndex> idxesFromDb) {
        for (RdbIndex index : idxesFromDb) {
            Map<String, RdbIndex> indexMap = indexesMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
            String indexName = index.getName();
            if (indexMap.containsKey(indexName)) {
                indexMap.get(indexName).getColumnList().addAll(index.getColumnList());
                Map<String, String> indexAtrMap = indexMap.get(indexName).getAttributes();

                if (indexAtrMap.containsKey(INDEX_PREFIX_LENGTH.getCodeKey())) {
                    String oldJsonMap = indexAtrMap.get(INDEX_PREFIX_LENGTH.getCodeKey());
                    String newJsonMap = index.getAttribute(INDEX_PREFIX_LENGTH);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(INDEX_PREFIX_LENGTH, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(INDEX_PREFIX_LENGTH, index.getAttribute(INDEX_PREFIX_LENGTH));
                }

                // if (indexAtrMap.containsKey(INDEX_SORT_RULES.getCodeKey())) {
                //     String oldJsonMap = indexAtrMap.get(INDEX_SORT_RULES.getCodeKey());
                //     String newJsonMap = index.getAttribute(INDEX_SORT_RULES);
                //     String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                //     indexMap.get(indexName).setAttribute(INDEX_SORT_RULES, resultMap);
                // } else {
                //     indexMap.get(indexName).setAttribute(INDEX_SORT_RULES, index.getAttribute(INDEX_SORT_RULES));
                // }

                if (indexAtrMap.containsKey(INDEX_OPERATOR_TYPE.getCodeKey())) {
                    String oldJsonMap = indexAtrMap.get(INDEX_OPERATOR_TYPE.getCodeKey());
                    String newJsonMap = index.getAttribute(INDEX_OPERATOR_TYPE);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(INDEX_OPERATOR_TYPE, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(INDEX_OPERATOR_TYPE, index.getAttribute(INDEX_OPERATOR_TYPE));
                }

                if (indexAtrMap.containsKey(INDEX_SORT_ORDER.getCodeKey())) {
                    String oldJsonMap = indexAtrMap.get(INDEX_SORT_ORDER.getCodeKey());
                    String newJsonMap = index.getAttribute(INDEX_SORT_ORDER);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(INDEX_SORT_ORDER, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(INDEX_SORT_ORDER, index.getAttribute(INDEX_SORT_ORDER));
                }

                if (indexAtrMap.containsKey(INDEX_NULLS_SORT.getCodeKey())) {
                    String oldJsonMap = indexAtrMap.get(INDEX_NULLS_SORT.getCodeKey());
                    String newJsonMap = index.getAttribute(INDEX_NULLS_SORT);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(INDEX_NULLS_SORT, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(INDEX_NULLS_SORT, index.getAttribute(INDEX_NULLS_SORT));
                }
            } else {
                indexMap.put(indexName, index);
            }
        }
    }

    protected String mergeJsonMap(String oldJsonMap, String newJsonMap) {
        oldJsonMap = oldJsonMap.substring(0, oldJsonMap.length() - 1);
        newJsonMap = newJsonMap.substring(1);
        return oldJsonMap + "," + newJsonMap;
    }

    protected void containAndSetOpt(String opt, String createOptions, UmiAttributeNames attributeNames, RdbTable rdbTable) {
        // create_options: {fillfactor=70,security_barrier=true}
        int length = createOptions.length();
        createOptions = createOptions.substring(1, length - 1);
        String[] strings = createOptions.split(",");
        for (String str : strings) {
            String[] split = str.split("=");
            if (split[0].equalsIgnoreCase(opt)) {
                rdbTable.setAttribute(attributeNames, split[1]);
                return;
            }
        }
        rdbTable.setAttribute(attributeNames, null);
    }

    protected void containAndSetOpt(String opt, String createOptions, UmiAttributeNames attributeNames, RdbIndex rdbIndex) {
        // create_options: {fillfactor=70,security_barrier=true}
        int length = createOptions.length();
        createOptions = createOptions.substring(1, length - 1);
        String[] strings = createOptions.split(",");
        for (String str : strings) {
            String[] split = str.split("=");
            if (split[0].equalsIgnoreCase(opt)) {
                rdbIndex.setAttribute(attributeNames, split[1]);
                return;
            }
        }
        rdbIndex.setAttribute(attributeNames, null);
    }

    protected FieldType safeToPostgresTypes(long serverVersionNumber, ResultSet rs) throws SQLException {
        if (rs == null) {
            return null;
        }
        Long typeOid = tryWasNull(rs.getLong("type_oid"), rs);
        String defaultValue = rs.getString("column_default");
        String typeName = rs.getString("type_name");

        PostgresTypes pgTypeEnum = PostgresTypes.valueOfTypeOid(typeOid.intValue());
        if (pgTypeEnum == null) {
            pgTypeEnum = PostgresTypes.valueOfTypeOid(typeOid);
            if (pgTypeEnum == null) {
                pgTypeEnum = PostgresTypes.valueOfCode(typeName);
            }
        }

        if (pgTypeEnum == null) {
            return null;
        }
        if (defaultValue != null && defaultValue.contains("nextval(")) {
            boolean gte_9_2 = serverVersionNumber >= PgServerVersion.v9_2.getVersionNum();
            if (pgTypeEnum == PostgresTypes.INTEGER) {
                pgTypeEnum = PostgresTypes.SERIAL;
            } else if (pgTypeEnum == PostgresTypes.BIGINT) {
                pgTypeEnum = PostgresTypes.BIGSERIAL;
            } else if (pgTypeEnum == PostgresTypes.SMALLINT && gte_9_2) {
                pgTypeEnum = PostgresTypes.SMALLSERIAL;
            }
        }

        return pgTypeEnum;
    }

    public static boolean isFunc(String dataDefault) {
        boolean isFunc = false;
        isFunc = isFunc | dataDefault.matches("CURRENT_TIMESTAMP(\\((\\d+)?\\))?");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "NOW()");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "CURDATE()");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));
        isFunc = isFunc | dataDefault.startsWith("nextval(") && dataDefault.endsWith(")");
        return isFunc;
    }

    protected JDBCType columnTypeMappingToJdbcType(FieldType typeDef, ResultSet rs) throws SQLException {
        if (rs == null) {
            return null;
        }
        String typType = rs.getString("typtype");
        String typeName = rs.getString("type_name");

        if ("c".equals(typType)) {
            return JDBCType.STRUCT;
        } else if ("d".equals(typType)) {
            return JDBCType.DISTINCT;
        } else if ("e".equals(typType)) {
            return JDBCType.VARCHAR;
        }
        if (typeName.endsWith("[]")) {
            return JDBCType.ARRAY;
        }

        if (typeDef != null && typeDef.toJDBCType() != null) {
            return typeDef.toJDBCType();
        }
        return JDBCType.OTHER;
    }

    protected List<RdbTable> convertView(ResultSet rs, MainVersion mainVersion) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbView tab = new RdbView();
            tab.setSchema(rs.getString("nspname"));
            tab.setName(rs.getString("relname"));
            //If comment is null, it will result in a NullPointerException in the diffActions() method
            String comment = rs.getString("table_comment");
            tab.setComment(comment == null ? "" : comment);

            String tableType = rs.getString("relkind");
            tab.setTableType(tableType);
            if (StringUtils.equalsIgnoreCase(tableType, "v")) {
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "r") || StringUtils.equalsIgnoreCase(tableType, "p")) {
                tab.setUmiType(UmiTypes.Table);
            }

            tab.setAttribute(INHERITED_FROM, rs.getString("parent_table"));
            tab.setAttribute(TABLE_TYPE, rs.getString("relpersistence"));
            String createOptions = rs.getString("reloptions");
            tab.setAttribute(TABLESPACE, rs.getString("spcname"));
            if (StringUtils.isNotBlank(createOptions)) {
                containAndSetOpt("fillfactor", createOptions, FILL_FACTOR, tab);
                tab.setFeatures(parseFeatures(createOptions));
            } else {
                tab.setAttribute(FILL_FACTOR, null);
            }
            //            if (PostgresMainVersion.PostgreSQL_12.isGt(mainVersion)) {
            //                tab.setAttribute(WITH_OIDS, String.valueOf(rs.getBoolean("relhasoids")));
            //            }
            tab.setSql(rs.getString("definition"));
            result.add(tab);
        }
        return result;
    }

    private Map<String, Object> parseFeatures(String opt) {
        Map<String, Object> result = new HashMap<>();
        int length = opt.length();
        opt = opt.substring(1, length - 1);
        String[] strings = opt.split(",");
        for (String str : strings) {
            String[] split = str.split("=");
            if (split[0].equals("check_option")) {
                result.put(VIEW_CHECK_OPTION.getCodeKey(), split[1].toUpperCase());
            } else if (split[0].equals("security_barrier")) {
                result.put(VIEW_SECURITY_BARRIER.getCodeKey(), Boolean.parseBoolean(split[1]));
            }

        }
        return result;
    }

    public List<RdbTable> convertTable(ResultSet rs, MainVersion mainVersion) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable tab = new RdbTable();
            tab.setSchema(rs.getString("nspname"));
            tab.setName(rs.getString("relname"));
            //If comment is null, it will result in a NullPointerException in the diffActions() method
            String comment = rs.getString("table_comment");
            tab.setComment(comment == null ? "" : comment);

            String tableType = rs.getString("relkind");
            tab.setTableType(tableType);
            if (StringUtils.equalsIgnoreCase(tableType, "v")) {
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "r") || StringUtils.equalsIgnoreCase(tableType, "p")) {
                tab.setUmiType(UmiTypes.Table);
            }

            tab.setAttribute(INHERITED_FROM, rs.getString("parent_table"));
            tab.setAttribute(TABLE_TYPE, rs.getString("relpersistence"));
            String createOptions = rs.getString("reloptions");
            tab.setAttribute(TABLESPACE, rs.getString("spcname"));
            if (StringUtils.isNotBlank(createOptions)) {
                containAndSetOpt("fillfactor", createOptions, FILL_FACTOR, tab);
            } else {
                tab.setAttribute(FILL_FACTOR, null);
            }
            //            if (PostgresMainVersion.PostgreSQL_12.isGt(mainVersion)) {
            //                tab.setAttribute(WITH_OIDS, String.valueOf(rs.getBoolean("relhasoids")));
            //            }
            result.add(tab);
        }
        return result;
    }

    public List<RdbColumn> convertColumn(ResultSet rs, long serverVersionNumber, boolean gt10) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        while (rs.next()) {
            RdbColumn column = new RdbColumn();
            String tableName = rs.getString("TABLE_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            column.setTable(tableName);
            column.setName(columnName);
            try {
                column.setSqlType(safeToPostgresTypes(serverVersionNumber, rs));
                if (Boolean.TRUE.equals(tryWasNull(rs.getBoolean("not_null"), rs))) {
                    column.addConstraint(new NonNull());
                }

                if (tryWasNull(rs.getBoolean("type_is_array"), rs)) {
                    String dataType = rs.getString("type_name");
                    if (dataType.endsWith("[]")) {
                        column.setAttribute(ELEMENT_TYPE, rs.getString("type_name").substring(0, dataType.length() - 2));
                        column.setAttribute(ARRAY_DIMENSION, rs.getString("attndims"));
                    }
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("datetime_precision"), rs));
                column.setIndex(tryWasNull(rs.getInt("attnum"), rs));
                column.setComment(rs.getString("comments"));
                column.setNumericScale(tryWasNull(rs.getInt("numeric_scale"), rs));
                column.setNumericPrecision(tryWasNull(rs.getInt("numeric_precision"), rs));
                String columnDefault = rs.getString("column_default");
                column.setDefaultValue(columnDefault);
                if (StringUtils.isNotBlank(columnDefault)) {
                    if (isFunc(columnDefault)) {
                        column.setDefaultValueIsFunc(true);
                    }
                }
                //attribute
                column.setAttribute(COLUMN_TYPE, rs.getString("type_name"));
                JDBCType jdbcType = columnTypeMappingToJdbcType(column.getSqlType(), rs);
                column.setAttribute(JDBC_TYPE, jdbcType.getName());
                column.setAttribute(CHARACTERS_MAX_LENGTH, rs.getString("character_maximum_length"));
                column.setCharLength(tryWasNull(rs.getLong("character_maximum_length"), rs));
                column.setAttribute(DATA_TYPE, rs.getString("type_name"));
                column.setAttribute(VIRTUAL_TYPE_EXPRESSION, rs.getString("generation_expression"));
                String identityGeneration = rs.getString("identity_generation");
                String generationExpression = rs.getString("generation_expression");
                if (identityGeneration != null) {
                    column.setAttribute(VIRTUAL_TYPE, identityGeneration);
                    column.setAttribute(VIRTUAL_TYPE_INCREMENTAL, rs.getString("identity_increment"));
                    column.setAttribute(VIRTUAL_TYPE_MAX, rs.getString("identity_maximum"));
                    column.setAttribute(VIRTUAL_TYPE_MIN, rs.getString("identity_minimum"));
                    column.setAttribute(VIRTUAL_TYPE_START, rs.getString("identity_start"));
                    column.setAttribute(VIRTUAL_TYPE_LOOP, rs.getString("identity_cycle"));
                    column.setAttribute(COLUMN_DEFAULT_OPTION, "Virtual Type");
                } else if (generationExpression != null) {
                    column.setAttribute(VIRTUAL_TYPE, "STORED");
                    column.setAttribute(VIRTUAL_TYPE_EXPRESSION, generationExpression);
                    column.setAttribute(COLUMN_DEFAULT_OPTION, "Virtual Type");
                }
                if (gt10 && rs.getString("identity_generation") != null) {
                    column.setAttribute(VIRTUAL_TYPE_CACHE, rs.getString("cache_size"));
                }
                if (column.getSqlType() == PostgresTypes.BIT || column.getSqlType() == PostgresTypes.BIT_VARYING //
                    || column.getSqlType() == PostgresTypes.BIT_ARRAY || column.getSqlType() == PostgresTypes.BIT_VARYING_ARRAY) {
                    column.setNumericPrecision(tryWasNull(rs.getInt("type_mod"), rs));
                }
                column.setAttribute(CHARACTERS_MAX_LENGTH, rs.getString("character_maximum_length"));
                column.setAttribute(NUMERIC_PRECISION_RADIX, String.valueOf(tryWasNull(rs.getInt("numeric_precision_radix"), rs)));
                String collationName = rs.getString("COLLATION_NAME");
                String collationSchemaName = rs.getString("collation_schema_name");

                if (StringUtils.isNotBlank(collationName) && StringUtils.isNotBlank(collationSchemaName)) {
                    column.setAttribute(COLUMN_SORT_RULES, collationSchemaName + "." + collationName);
                }
                columns.add(column);
            } catch (Exception e) {
                String msg = "extract " + column.getTable() + " column (" + column.getName() + ") error.msg:" + ExceptionUtils.getRootCauseMessage(e);
                throw new SQLException(msg, e);
            }
        }
        return columns;
    }

    protected List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> indexList = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(rs.getString("table_schema"));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("index_name"));

            String indexType = rs.getString("am_name");
            if (rs.getBoolean("isunique")) {
                idx.setAttribute(INDEX_WAY, "Unique");
                idx.setType(RdbIndexType.Unique);
            } else {
                idx.setAttribute(INDEX_WAY, "Normal");
                idx.setType(RdbIndexType.Normal);
            }
            idx.setAttribute(INDEX_TYPE, indexType);
            idx.setComment(rs.getString("description"));

            //column attribute
            String columnName = rs.getString("column_name");
            HashMap<String, String> subPartMap = new HashMap<>();
            if (columnName.contains("SUBSTRING")) {
                String substring = columnName.substring(10);
                String[] splitStr = substring.split(" ");
                columnName = splitStr[0].replace("\"", "");;
                String subPart = null;
                for (int i = 1; i < splitStr.length; i++) {
                    if (splitStr[i].equals("FOR") && i + 1 < splitStr.length) {
                        subPart = splitStr[i + 1].replace(")", "");
                        break;
                    }
                }
                subPartMap.put(columnName, subPart);
            } else {
                subPartMap.put(columnName, null);
            }
            idx.addColumn(columnName);
            String newSubPartJson = JsonUtils.toJson(subPartMap);
            idx.setAttribute(INDEX_PREFIX_LENGTH, newSubPartJson);

            //order
            HashMap<String, String> orderMap = new HashMap<>();
            String ascOrDesc = rs.getString("asc_or_desc");
            if (StringUtils.isBlank(ascOrDesc)) {
                ascOrDesc = "";
            }
            orderMap.put(columnName, ascOrDesc);
            String newOrderMapJson = JsonUtils.toJson(orderMap);
            idx.setAttribute(INDEX_SORT_ORDER, newOrderMapJson);

            //sort rules
            String collationName = rs.getString("COLLATION_NAME");
            String collationSchemaName = rs.getString("collation_schema_name");
            HashMap<String, String> sortRulesMap = new HashMap<>();
            if (collationName != null && collationSchemaName != null) {
                sortRulesMap.put(columnName, collationSchemaName + "." + collationName);
            } else {
                sortRulesMap.put(columnName, null);
            }
            String newSortRulesJson = JsonUtils.toJson(sortRulesMap);
            // idx.setAttribute(INDEX_SORT_RULES, newSortRulesJson);

            //Operator Category
            String opcname = rs.getString("opcname");
            String opcnameSchemaName = rs.getString("opcname_schema_name");
            HashMap<String, String> opcnameMap = new HashMap<>();
            if (opcname != null && opcnameSchemaName != null) {
                opcnameMap.put(columnName, opcnameSchemaName + "." + opcname);
            } else {
                opcnameMap.put(columnName, null);
            }
            String newOpcNameJson = JsonUtils.toJson(opcnameMap);
            idx.setAttribute(INDEX_OPERATOR_TYPE, newOpcNameJson);

            //nulls sort
            HashMap<String, String> nullsFirstMap = new HashMap<>();
            nullsFirstMap.put(columnName, rs.getString("nullsfirst"));
            String newNullsFirstJson = JsonUtils.toJson(nullsFirstMap);
            idx.setAttribute(INDEX_NULLS_SORT, newNullsFirstJson);

            //with option
            // String reloptions = rs.getString("reloptions");
            // if (StringUtils.isNotBlank(reloptions)) {
            //     containAndSetOpt("fillfactor", reloptions, INDEX_WITH_FILLFACTOR, idx);
            //     containAndSetOpt("buffering", reloptions, INDEX_WITH_BUFFERING, idx);
            //     containAndSetOpt("fastupdate", reloptions, INDEX_WITH_FASTUPDATE, idx);
            // } else {
            //     idx.setAttribute(INDEX_WITH_FILLFACTOR, null);
            //     idx.setAttribute(INDEX_WITH_BUFFERING, null);
            //     idx.setAttribute(INDEX_WITH_FASTUPDATE, null);
            // }
            //tablespace
            // idx.setAttribute(INDEX_TABLESPACE, rs.getString("tablespace"));

            //where predicate
            // idx.setAttribute(INDEX_WHERE, rs.getString("predicate"));

            indexList.add(idx);
        }
        return indexList;
    }

    public List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("datname"));
            schema.setUmiType(UmiTypes.Catalog);

            result.add(schema);
        }
        return result;
    }

    public List<RdbForeignKey> convertForeignKey(ResultSet rs) throws SQLException {
        class PostgresForeignKeyItem extends PgForeignKey {

            private RdbForeignKey foreignKey;
            private int           keySeq;
        }

        List<PostgresForeignKeyItem> fkList = new ArrayList<>();
        while (rs.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setSchema(rs.getString("fk_table_schema"));
            fk.setTable(rs.getString("fk_table_name"));
            fk.setName(rs.getString("fk_name"));
            fk.setReferenceSchema(rs.getString("pk_table_schema"));
            fk.setReferenceTable(rs.getString("pk_table_name"));
            fk.addColumn(rs.getString("fk_column_name"), rs.getString("pk_column_name"));
            fk.setUpdateRule(convertForeignKeyRule(PostgresForeignKeyRule.valueOfCode(rs.getString("update_rule"))));
            fk.setDeleteRule(convertForeignKeyRule(PostgresForeignKeyRule.valueOfCode(rs.getString("delete_rule"))));

            fk.setAttribute(MATCH_OPTION, PostgresForeignMatchOption.valueOfCode(rs.getString("match_option")).name());

            PostgresForeignKeyItem keyItem = new PostgresForeignKeyItem();
            keyItem.foreignKey = fk;
            keyItem.keySeq = rs.getInt("key_seq");

            fkList.add(keyItem);
        }
        fkList.sort(Comparator.comparingInt(o -> o.keySeq));

        return fkList.stream().map(i -> i.foreignKey).collect(Collectors.toList());
    }

    public RdbForeignKeyRule convertForeignKeyRule(PostgresForeignKeyRule rule) {
        if (rule == null) {
            return null;
        }
        switch (rule) {
            case Cascade:
                return RdbForeignKeyRule.Cascade;
            case SetNull:
                return RdbForeignKeyRule.SetNull;
            case NoAction:
                return RdbForeignKeyRule.NoAction;
            case Restrict:
                return RdbForeignKeyRule.Restrict;
            case SetDefault:
                return RdbForeignKeyRule.SetDefault;
            default:
                return null;
        }
    }

    public List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        List<RdbFunction> result = new ArrayList<>();
        while (rs.next()) {
            RdbFunction rdbFunction = new RdbFunction();
            rdbFunction.setName(rs.getString("proname"));
            rdbFunction.setSql(rs.getString("prosrc"));
            rdbFunction.setSchema(rs.getString("nspname"));
            RdbParam rdbParam = new RdbParam();
            rdbParam.setType(rs.getString("returnType"));
            rdbFunction.setReturns(rdbParam);
            rdbFunction.setAttribute(RDB_OBJ_ID, rs.getString("oid"));
            result.add(rdbFunction);
        }
        return result;
    }

    public List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setName(rs.getString("paramName"));
            rdbParam.setType(rs.getString("paramType"));
            rdbParam.setMode(parseMode(rs.getString("paramMode")));
            rdbParam.setReferenceObject(rs.getString("oid"));
            rdbParam.setOrdinal(rs.getInt("ordinal"));
            result.add(rdbParam);
        }
        return result;
    }

    private static RdbParamMode parseMode(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        switch (name) {
            case "i":
                return RdbParamMode.IN;
            case "o":
                return RdbParamMode.OUT;
            case "v":
                return RdbParamMode.VARIADIC;
            case "b":
                return RdbParamMode.INOUT;
            default:
                return null;
        }
    }

    public List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        List<RdbProcedure> result = new ArrayList<>();
        while (rs.next()) {
            RdbProcedure rdbProcedure = new RdbProcedure();
            rdbProcedure.setName(rs.getString("proname"));
            rdbProcedure.setSql(rs.getString("prosrc"));
            rdbProcedure.setSchema(rs.getString("nspname"));
            RdbParam rdbParam = new RdbParam();
            rdbParam.setType(rs.getString("returnType"));
            rdbProcedure.setAttribute(RDB_OBJ_ID, rs.getString("oid"));
            result.add(rdbProcedure);
        }
        return result;
    }
}
