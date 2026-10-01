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
package com.clougence.clouddm.ds.hana.execute;

import static com.clougence.adapter.hana.HanaAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaMetaProviderUtils {

    protected static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("SCHEMA_NAME"));
            schema.setUmiType(UmiTypes.Schema);

            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("DATABASE_NAME"));
            schema.setUmiType(UmiTypes.Catalog);

            result.add(schema);
        }
        return result;
    }

    protected static List<Value> convertViewName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("VIEW_NAME"));
            schema.setUmiType(UmiTypes.View);
            schema.setAttribute(COMMENT, rs.getString("COMMENTS"));
            result.add(schema);
        }
        return result;
    }

    protected static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("TABLE_NAME"));
            schema.setUmiType(UmiTypes.Table);
            schema.setAttribute(COMMENT, rs.getString("COMMENTS"));
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("PROCEDURE_NAME"));
            schema.setUmiType(UmiTypes.Procedure);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("FUNCTION_NAME"));
            schema.setUmiType(UmiTypes.Function);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("TRIGGER_NAME"));
            schema.setUmiType(UmiTypes.Trigger);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertSequenceName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("SEQUENCE_NAME"));
            schema.setUmiType(UmiTypes.Sequence);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertSynonymName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("SYNONYM_NAME"));
            schema.setUmiType(UmiTypes.Synonym);
            result.add(schema);
        }
        return result;
    }

    public static List<RdbColumn> convertColumns(ResultSet rs) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        while (rs.next()) {
            RdbColumn column = new RdbColumn();
            column.setSchema(rs.getString("SCHEMA_NAME"));
            column.setTable(rs.getString("TABLE_NAME"));
            column.setName(rs.getString("COLUMN_NAME"));
            column.setComment(rs.getString("COMMENTS"));
            if (Boolean.FALSE.equals(tryWasNull(rs.getBoolean("IS_NULLABLE"), rs))) {
                column.addConstraint(new NonNull());
            }
            String dataType = rs.getString("DATA_TYPE_NAME");
            column.setAttribute(DATA_TYPE, dataType);
            HanaTypes sqlType;
            try {
                sqlType = HanaTypes.valueOfCode(dataType);
            } catch (UnsupportedOperationException e) {
                String message = "Unsupported HANA column type " + dataType + " for " + column.getSchema() + "." + column.getTable() + "." + column.getName();
                throw ThirdPartyApiException.as().with(new SQLException(message, e));
            }
            column.setSqlType(sqlType);
            JDBCType jdbcType = sqlType.toJDBCType();
            if (jdbcType == JDBCType.CLOB || jdbcType == JDBCType.NCLOB || sqlType.isBinary()) {
                // System views report LOB length in bytes, including character LOBs.
                column.setByteLength(tryWasNull(rs.getLong("LENGTH"), rs));
            } else if (sqlType.isString()) {
                column.setCharLength(tryWasNull(rs.getLong("LENGTH"), rs));
            } else if (sqlType.isNumber()) {
                column.setNumericPrecision(tryWasNull(rs.getInt("LENGTH"), rs));
                column.setNumericScale(tryWasNull(rs.getInt("SCALE"), rs));
            } else if (sqlType.isDataOrTime()) {
                column.setDatetimePrecision(tryWasNull(rs.getInt("SCALE"), rs));
            }
            column.setDefaultValue(rs.getString("DEFAULT_VALUE"));
            String generationType = rs.getString("GENERATION_TYPE");
            column.setAttribute(GENERATION_TYPE, generationType);
            column.setAttribute(GENERATION_ALWAYS_AS, rs.getString("GENERATED_ALWAYS_AS"));
            boolean identity = "ALWAYS AS IDENTITY".equals(generationType) || "BY DEFAULT AS IDENTITY".equals(generationType);
            column.setAttribute(AUTO_INCREMENT, Boolean.toString(identity));

            column.setIndex(rs.getInt("POSITION"));
            columns.add(column);
        }
        return columns;
    }

    public static void mapToPkExt(ResultSet rs, Map<String, UmiConstraint> constraints) throws SQLException {
        String consName = rs.getString("INDEX_NAME");
        RdbPrimaryKey pk = (RdbPrimaryKey) (constraints.computeIfAbsent(consName, n -> new RdbPrimaryKey()));
        pk.setAttribute(INDEX_WAY, "PRIMARY KEY");
        String indexName = rs.getString("INDEX_NAME");
        pk.setName(indexName);
        String columnName = rs.getString("COLUMN_NAME");
        pk.addColumn(columnName);
        pk.setAttribute("INDEX_NAME", rs.getString("INDEX_NAME"));
        pk.setSchema(rs.getString("SCHEMA_NAME"));
        pk.setTable(rs.getString("TABLE_NAME"));

        pk.setAttribute(INDEX_TYPE, rs.getString("INDEX_TYPE"));

        String order = rs.getString("ASCENDING_ORDER");
        Map<String, String> subOrder = new HashMap<>();
        if (StringUtils.isNotBlank(order)) {
            if (order.equalsIgnoreCase("TRUE")) {
                order = "ASC";
            } else if (order.equalsIgnoreCase("FALSE")) {
                order = "DESC";
            }
        }
        subOrder.put(columnName, order);
        String subOrderJson = JSON.toString(subOrder);
        pk.setAttribute(ORDER_TYPE, mergeJsonMap(pk.getAttribute(ORDER_TYPE), subOrderJson));
    }

    public static void mapToUkExt(ResultSet rs, Map<String, UmiConstraint> constraints) throws SQLException {
        String consName = rs.getString("INDEX_NAME");
        RdbUniqueKey uk = (RdbUniqueKey) (constraints.computeIfAbsent(consName, n -> new RdbUniqueKey()));
        uk.setAttribute(INDEX_WAY, "Unique");
        String indexName = rs.getString("INDEX_NAME");
        uk.setName(indexName);
        String columnName = rs.getString("COLUMN_NAME");
        uk.addColumn(columnName);
        uk.setAttribute("INDEX_NAME", indexName);
        uk.setSchema(rs.getString("SCHEMA_NAME"));
        uk.setTable(rs.getString("TABLE_NAME"));

        uk.setAttribute(INDEX_TYPE, rs.getString("INDEX_TYPE"));

        String order = rs.getString("ASCENDING_ORDER");
        Map<String, String> subOrder = new HashMap<>();
        if (StringUtils.isNotBlank(order)) {
            if (order.equalsIgnoreCase("TRUE")) {
                order = "ASC";
            } else if (order.equalsIgnoreCase("FALSE")) {
                order = "DESC";
            }
        }
        subOrder.put(columnName, order);
        String subOrderJson = JSON.toString(subOrder);
        uk.setAttribute(ORDER_TYPE, mergeJsonMap(uk.getAttribute(ORDER_TYPE), subOrderJson));
    }

    public static List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> rdbIndices = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(rs.getString("SCHEMA_NAME"));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("INDEX_NAME"));

            String indexType = rs.getString("INDEX_TYPE");
            idx.setType(RdbIndexType.Normal);
            idx.setAttribute(INDEX_TYPE, indexType);
            idx.setAttribute(INDEX_WAY, RdbIndexType.Normal.getTypeName());

            String columnName = rs.getString("COLUMN_NAME");
            idx.addColumn(columnName);
            String order = rs.getString("ASCENDING_ORDER");
            Map<String, String> subOrder = new HashMap<>();
            if (StringUtils.isNotBlank(order)) {
                if (order.equalsIgnoreCase("TRUE")) {
                    order = "ASC";
                } else if (order.equalsIgnoreCase("FALSE")) {
                    order = "DESC";
                }
            }
            subOrder.put(columnName, order);
            String subOrderJson = JSON.toString(subOrder);
            idx.setAttribute(ORDER_TYPE, subOrderJson);
            rdbIndices.add(idx);
        }
        return rdbIndices;
    }

    public static Map<String, Map<String, RdbIndex>> convertIndexList(List<RdbIndex> idxList) {
        Map<String, Map<String, RdbIndex>> idxMap = new LinkedHashMap<>();
        for (RdbIndex index : idxList) {
            Map<String, RdbIndex> indexMap = idxMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
            String indexName = index.getName();
            if (indexMap.containsKey(indexName)) {
                indexMap.get(indexName).getColumnList().addAll(index.getColumnList());
                Map<String, String> indexMapAtr = indexMap.get(indexName).getAttributes();

                if (indexMapAtr.containsKey(ORDER_TYPE.getCodeKey())) {
                    String oldJsonMap = indexMapAtr.get(ORDER_TYPE.getCodeKey());
                    String newJsonMap = index.getAttribute(ORDER_TYPE);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(ORDER_TYPE, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(ORDER_TYPE, index.getAttribute(ORDER_TYPE));
                }

            } else {
                indexMap.put(indexName, index);
            }
        }
        return idxMap;
    }

    private static String mergeJsonMap(String oldJsonMap, String newJsonMap) {
        Map<String, Object> order = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(oldJsonMap)) {
            order.putAll((Map<String, Object>) JSON.parse(oldJsonMap));
        }
        order.putAll((Map<String, Object>) JSON.parse(newJsonMap));
        return JSON.toString(order);
    }

    public static List<RdbTable> convertTable(ResultSet rs, String catalogName) throws SQLException {
        List<RdbTable> tables = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setCatalog(catalogName);
            table.setSchema(rs.getString("SCHEMA_NAME"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setTableType(rs.getString("TABLE_TYPE"));
            table.setUmiType(UmiTypes.Table);

            Timestamp createTime = rs.getTimestamp("CREATE_TIME");
            if (createTime != null) {
                table.setAttribute(CREATE_TIME, createTime.toString());
            }

            table.setComment(rs.getString("COMMENTS"));
            tables.add(table);
        }
        return tables;
    }

    public static List<RdbTable> convertView(ResultSet rs, String catalogName) throws SQLException {
        List<RdbTable> tables = new ArrayList<>();
        while (rs.next()) {
            RdbView table = new RdbView();
            table.setCatalog(catalogName);
            table.setSchema(rs.getString("SCHEMA_NAME"));
            table.setName(rs.getString("VIEW_NAME"));
            table.setTableType(rs.getString("VIEW_TYPE"));
            table.setUmiType(UmiTypes.View);
            table.setSql(rs.getString("DEFINITION"));

            Timestamp createTime = rs.getTimestamp("CREATE_TIME");
            if (createTime != null) {
                table.setAttribute(CREATE_TIME, createTime.toString());
            }

            table.setComment(rs.getString("COMMENTS"));
            tables.add(table);
        }
        return tables;
    }

    public static List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        List<RdbFunction> result = new ArrayList<>();
        while (rs.next()) {
            RdbFunction rdbFunction = new RdbFunction();
            rdbFunction.setSchema(rs.getString("SCHEMA_NAME"));
            rdbFunction.setName(rs.getString("FUNCTION_NAME"));
            rdbFunction.setSql(rs.getString("DEFINITION"));
            result.add(rdbFunction);
        }

        return result;
    }

    public static List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        List<RdbProcedure> result = new ArrayList<>();
        while (rs.next()) {
            RdbProcedure rdbProcedure = new RdbProcedure();
            rdbProcedure.setSchema(rs.getString("SCHEMA_NAME"));
            rdbProcedure.setName(rs.getString("PROCEDURE_NAME"));
            rdbProcedure.setSql(rs.getString("DEFINITION"));
            result.add(rdbProcedure);
        }

        return result;
    }

    public static List<RdbParam> convertProcedureParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setReferenceObject(rs.getString("PROCEDURE_NAME"));
            fillParameter(rs, rdbParam);
            result.add(rdbParam);
        }
        return result;
    }

    public static List<RdbParam> convertFunctionParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setReferenceObject(rs.getString("FUNCTION_NAME"));
            fillParameter(rs, rdbParam);
            result.add(rdbParam);
        }
        return result;
    }

    private static void fillParameter(ResultSet rs, RdbParam param) throws SQLException {
        param.setSchema(rs.getString("SCHEMA_NAME"));
        param.setOrdinal(rs.getInt("POSITION"));
        param.setName(rs.getString("PARAMETER_NAME"));
        param.setType(rs.getString("DATA_TYPE_NAME"));
        param.setLength(tryWasNull(rs.getLong("LENGTH"), rs));
        param.setMode(RdbParamMode.valueOfCode(rs.getString("PARAMETER_TYPE")));
        param.setAttribute("TABLE_TYPE_SCHEMA", rs.getString("TABLE_TYPE_SCHEMA"));
        param.setAttribute("TABLE_TYPE_NAME", rs.getString("TABLE_TYPE_NAME"));
        HanaTypes type;
        try {
            type = HanaTypes.valueOfCode(param.getType());
        } catch (UnsupportedOperationException e) {
            // TABLE_TYPE and future parameter types retain their original type and type reference.
            return;
        }
        JDBCType jdbcType = type.toJDBCType();
        if (type.isString() && jdbcType != JDBCType.CLOB && jdbcType != JDBCType.NCLOB) {
            param.setCharacterMaximumLength(rs.getString("LENGTH"));
        } else if (type.isNumber()) {
            param.setNumericPrecision(tryWasNull(rs.getInt("LENGTH"), rs));
            param.setNumericScale(tryWasNull(rs.getInt("SCALE"), rs));
        } else if (type.isDataOrTime()) {
            param.setDatetimePrecision(tryWasNull(rs.getInt("SCALE"), rs));
        }
    }

    public static List<RdbTrigger> convertTrigger(ResultSet rs) throws SQLException {
        List<RdbTrigger> result = new ArrayList<>();
        while (rs.next()) {
            RdbTrigger rdbTrigger = new RdbTrigger();
            rdbTrigger.setName(rs.getString("TRIGGER_NAME"));
            rdbTrigger.setTriggerTime(rs.getString("TRIGGER_ACTION_TIME"));
            rdbTrigger.setTriggerTableSchema(rs.getString("SUBJECT_TABLE_SCHEMA"));
            rdbTrigger.setTriggerTableName(rs.getString("SUBJECT_TABLE_NAME"));
            rdbTrigger.setAttribute(SCHEMA, rs.getString("SCHEMA_NAME"));
            rdbTrigger.setAttribute(TRIGGER_GRANULARITY, rs.getString("TRIGGERED_ACTION_LEVEL"));
            String events = rs.getString("TRIGGER_EVENT");
            List<String> triggerEvents = new ArrayList<>();
            for (String event : List.of("INSERT", "UPDATE", "DELETE")) {
                if (events != null && events.contains(event)) {
                    triggerEvents.add(event);
                }
            }
            rdbTrigger.setTriggerEvent(triggerEvents);
            String definition = rs.getString("DEFINITION");
            if (StringUtils.isNotBlank(definition)) {
                rdbTrigger.setFeatures(Collections.singletonMap("definition", definition));
                String upper = definition.toUpperCase(Locale.ROOT);
                int bodyStart = upper.indexOf("BEGIN");
                int bodyEnd = upper.lastIndexOf("END");
                if (bodyStart >= 0 && bodyEnd >= bodyStart) {
                    rdbTrigger.setSql(definition.substring(bodyStart, bodyEnd + 3));
                }
            }
            result.add(rdbTrigger);
        }
        return result;
    }

    public static List<RdbForeignKey> convertForeignKey(ResultSet rs) throws SQLException {
        List<RdbForeignKey> result = new ArrayList<>();
        while (rs.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setSchema(rs.getString("SCHEMA_NAME"));
            fk.setTable(rs.getString("TABLE_NAME"));
            fk.setName(rs.getString("CONSTRAINT_NAME"));
            fk.setUpdateRule(RdbForeignKeyRule.valueOfCode(rs.getString("UPDATE_RULE")));
            fk.setDeleteRule(RdbForeignKeyRule.valueOfCode(rs.getString("DELETE_RULE")));
            fk.setReferenceSchema(rs.getString("REFERENCED_SCHEMA_NAME"));
            fk.setReferenceTable(rs.getString("REFERENCED_TABLE_NAME"));
            fk.addColumn(rs.getString("COLUMN_NAME"), rs.getString("REFERENCED_COLUMN_NAME"));
            result.add(fk);
        }
        return result;
    }

    public static void fillFkMap(Map<String, Map<String, RdbForeignKey>> tableFkMap, List<RdbForeignKey> fksFromDb) {
        for (RdbForeignKey fk : fksFromDb) {
            Map<String, RdbForeignKey> fkMap = tableFkMap.computeIfAbsent(fk.getTable(), s -> new LinkedHashMap<>());
            String fkName = fk.getName();
            if (fkMap.containsKey(fkName)) {
                fkMap.get(fkName).getColumnList().addAll(fk.getColumnList());
                fkMap.get(fkName).getReferenceMapping().putAll(fk.getReferenceMapping());
            } else {
                fkMap.put(fkName, fk);
            }
        }
    }
}
