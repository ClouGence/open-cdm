package com.clougence.clouddm.ds.hana.execute;

import static com.clougence.adapter.hana.HanaAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.clougence.adapter.hana.HanaIndexType;
import com.clougence.adapter.hana.HanaTableType;
import com.clougence.adapter.hana.HanaTypes;
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
            column.setCharLength((long) rs.getInt("LENGTH"));
            column.setNumericPrecision(rs.getInt("LENGTH"));
            column.setDefaultValue(rs.getString("DEFAULT_VALUE"));
            column.setNumericScale(rs.getInt("SCALE"));
            String generationType = rs.getString("GENERATION_TYPE");
            if (!StringUtils.isBlank(generationType) && "ALWAYS AS IDENTITY".equals(generationType)) {
                column.setAttribute(AUTO_INCREMENT, "true");
            } else {
                column.setAttribute(AUTO_INCREMENT, "false");
            }

            HanaTypes sqlType = safeToHanaTypes(rs.getString("DATA_TYPE_NAME"));
            column.setSqlType(sqlType);
            column.setIndex(rs.getInt("POSITION"));
            columns.add(column);
        }
        return columns;
    }

    private static HanaTypes safeToHanaTypes(String dataType) {
        String dat = (dataType == null) ? null : dataType.toString();
        for (HanaTypes type : HanaTypes.values()) {
            if (StringUtils.equalsIgnoreCase(type.getCodeKey(), dat)) {
                return type;
            }
        }
        return null;
    }

    public static void mapToPkExt(ResultSet rs, Map<String, UmiConstraint> constraints) throws SQLException {
        String consName = rs.getString("CONSTRAINT");
        RdbPrimaryKey pk = (RdbPrimaryKey) (constraints.computeIfAbsent(consName, n -> new RdbPrimaryKey()));
        pk.setAttribute(INDEX_WAY, consName);
        String indexName = rs.getString("INDEX_NAME");
        pk.setName(indexName);
        String columnName = rs.getString("COLUMN_NAME");
        pk.addColumn(columnName);
        pk.setAttribute("INDEX_NAME", rs.getString("INDEX_NAME"));
        pk.setSchema(rs.getString("SCHEMA_NAME"));
        pk.setTable(rs.getString("TABLE_NAME"));

        String indexType = rs.getString("INDEX_TYPE");
        HanaIndexType hanaIndexType = HanaIndexType.valueOfCode(indexType);
        if (hanaIndexType != null) {
            pk.setAttribute(INDEX_TYPE, indexType);
        }

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
        pk.setAttribute(ORDER_TYPE, subOrderJson);
    }

    public static void mapToUkExt(ResultSet rs, Map<String, UmiConstraint> constraints) throws SQLException {
        String consName = rs.getString("CONSTRAINT");
        RdbUniqueKey uk = (RdbUniqueKey) (constraints.computeIfAbsent(consName, n -> new RdbUniqueKey()));
        uk.setAttribute(INDEX_WAY, "Unique");
        String indexName = rs.getString("INDEX_NAME");
        uk.setName(indexName);
        String columnName = rs.getString("COLUMN_NAME");
        uk.addColumn(columnName);
        uk.setAttribute("INDEX_NAME", indexName);
        uk.setSchema(rs.getString("SCHEMA_NAME"));
        uk.setTable(rs.getString("TABLE_NAME"));

        String indexType = rs.getString("INDEX_TYPE");
        HanaIndexType hanaIndexType = HanaIndexType.valueOfCode(indexType);
        if (hanaIndexType != null) {
            uk.setAttribute(INDEX_TYPE, indexType);
        }

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
        uk.setAttribute(ORDER_TYPE, subOrderJson);
    }

    public static List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> rdbIndices = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(StringUtils.trim(rs.getString("SCHEMA_NAME")));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("INDEX_NAME"));

            String indexType = rs.getString("INDEX_TYPE");
            HanaIndexType hanaIndexType = HanaIndexType.valueOfCode(indexType);
            if (hanaIndexType != null) {
                idx.setType(RdbIndexType.Normal);
                idx.setAttribute(INDEX_TYPE, indexType);
                idx.setAttribute(INDEX_WAY, RdbIndexType.Normal.getTypeName());
            }

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
        oldJsonMap = oldJsonMap.substring(0, oldJsonMap.length() - 1);
        newJsonMap = newJsonMap.substring(1);
        return oldJsonMap + "," + newJsonMap;
    }

    public static List<RdbTable> convertTable(ResultSet rs, String catalogName) throws SQLException {
        List<RdbTable> tables = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setCatalog(catalogName);
            table.setSchema(rs.getString("SCHEMA_NAME"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setTableType(Objects.requireNonNull(HanaTableType.valueOfCode(rs.getString("TABLE_TYPE"))).getTypeName());
            table.setUmiType(UmiTypes.Table);
            if (table.getTableType() == null) {
                continue;
            }

            table.setAttribute(CREATE_TIME, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS").format(rs.getTimestamp("CREATE_TIME")));

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
            table.setTableType(Objects.requireNonNull(HanaTableType.valueOfCode(rs.getString("VIEW_TYPE"))).getTypeName());
            table.setUmiType(UmiTypes.View);
            if (table.getTableType() == null) {
                continue;
            }
            table.setSql(rs.getString("DEFINITION"));

            table.setAttribute(CREATE_TIME, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSSSS").format(rs.getTimestamp("CREATE_TIME")));

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
            result.add(rdbProcedure);
        }

        return result;
    }

    public static List<RdbParam> convertProcedureParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setSchema(rs.getString("SCHEMA_NAME"));
            rdbParam.setReferenceObject(rs.getString("PROCEDURE_NAME"));
            rdbParam.setOrdinal(rs.getInt("POSITION"));
            rdbParam.setName(rs.getString("PARAMETER_NAME"));
            rdbParam.setType(rs.getString("DATA_TYPE_NAME"));
            rdbParam.setCharacterMaximumLength(rs.getString("LENGTH"));
            result.add(rdbParam);
        }
        return result;
    }

    public static List<RdbParam> convertFunctionParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setSchema(rs.getString("SCHEMA_NAME"));
            rdbParam.setReferenceObject(rs.getString("FUNCTION_NAME"));
            rdbParam.setOrdinal(rs.getInt("POSITION"));
            rdbParam.setName(rs.getString("PARAMETER_NAME"));
            rdbParam.setType(rs.getString("DATA_TYPE_NAME"));
            rdbParam.setCharacterMaximumLength(rs.getString("LENGTH"));
            result.add(rdbParam);
        }
        return result;
    }

    public static List<RdbTrigger> convertTrigger(ResultSet rs) throws SQLException {
        List<RdbTrigger> result = new ArrayList<>();
        while (rs.next()) {
            RdbTrigger rdbTrigger = new RdbTrigger();
            rdbTrigger.setName(rs.getString("TRIGGER_NAME"));
            rdbTrigger.setTriggerTime(rs.getString("TRIGGER_ACTION_TIME"));
            rdbTrigger.setTriggerEvent(Collections.singletonList(rs.getString("TRIGGER_EVENT")));
            rdbTrigger.setTriggerTableName(rs.getString("SUBJECT_TABLE_NAME"));
            String definition = rs.getString("DEFINITION");
            if (StringUtils.isNotBlank(definition)) {
                String tmp = definition.toUpperCase();
                int bodyStart = tmp.indexOf("BEGIN");
                int bodyEnd = tmp.lastIndexOf("END");
                if (bodyStart >= 0 && bodyEnd >= 0) {
                    rdbTrigger.setSql(definition.substring(bodyStart, bodyEnd + 3));
                }

                // trigger columns
                String header = definition.substring(0, bodyStart);
                int updateStart = header.indexOf("UPDATE OF");
                int updateEnd = header.indexOf("ON");
                if (updateStart >= 0 && updateEnd >= 0) {
                    String updateColumns = definition.substring(updateStart + 9, updateEnd);
                    rdbTrigger.setTriggerTableColumns(Arrays.asList(updateColumns.split(",")));
                }

                // trigger event
                List<String> triggerEvent = new ArrayList<>();
                if (header.contains("INSERT")) {
                    triggerEvent.add("INSERT");
                }
                if (header.contains("UPDATE")) {
                    triggerEvent.add("UPDATE");
                }
                if (header.contains("DELETE")) {
                    triggerEvent.add("DELETE");
                }
                rdbTrigger.setTriggerEvent(triggerEvent);
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
