package com.clougence.clouddm.dsfamily.db2.execute;

import static com.clougence.adapter.db2.Db2AttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.db2.Db2ForeignKeyRule;
import com.clougence.adapter.db2.Db2Types;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiAttributeUtils;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

/**
 * @author : Ekko
 * @version : 2023-12-21 15:35
 */
public class Db2MetaProviderUtils {

    public static List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> cats = new ArrayList<>();
        while (rs.next()) {
            RdbValue cat = new RdbValue();
            cat.setUmiType(UmiTypes.Catalog);
            cat.setValue(rs.getString("CATALOG_NAME"));
            cat.setAttribute(CAT_STATUS, rs.getString("CATALOG_STATUS"));
            cats.add(cat);
        }
        return cats;
    }

    public static List<RdbTable> convertView(ResultSet rs) throws SQLException {
        List<RdbTable> tables = new ArrayList<>();
        while (rs.next()) {
            RdbView table = new RdbView();
            table.setSchema(rs.getString("VIEWSCHEMA"));
            table.setName(rs.getString("VIEWNAME"));
            table.setSql(rs.getString("TEXT"));

            String tableType = rs.getString("TYPE");

            if (StringUtils.containsAny(tableType, new char[] { 'V', 'W' })) {
                table.setUmiType(UmiTypes.View);
            } else if (StringUtils.containsAny(tableType, new char[] { 'S', 'M' })) {
                table.setUmiType(UmiTypes.Materialized);
            } else {
                table.setUmiType(null);
            }

            table.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));
            table.setAttribute(COLUMN_COUNT, rs.getString("COLCOUNT"));
            table.setAttribute(VIEW_CHECK_OPTION, rs.getString("VIEWCHECK"));
            table.setAttribute(VIEW_READ_ONLY, rs.getString("READONLY"));

            table.setAttribute(INSERTABLE, rs.getString("IS_INSERTABLE_INTO"));
            table.setAttribute(DELETABLE, rs.getString("IS_DELETABLE"));
            table.setAttribute(UPDATABLE, rs.getString("IS_UPDATABLE"));

            tables.add(table);
        }
        return tables;
    }

    public static List<RdbSequence> convertSequence(ResultSet rs) throws SQLException {
        List<RdbSequence> sequences = new ArrayList<>();
        while (rs.next()) {
            RdbSequence sequence = new RdbSequence();
            sequence.setName(rs.getString("SEQNAME"));
            sequence.setIncrementBy(rs.getString("INCREMENT"));
            sequence.setMaxValue(rs.getString("MAXVALUE"));
            sequence.setMinValue(rs.getString("MINVALUE"));

            sequence.setAttribute(CYCLE, rs.getString("CYCLE"));
            sequence.setAttribute(NEXT_CACHE_VALUE, rs.getString("CACHE_NEXT"));
            sequence.setAttribute(ORDER, rs.getString("ORDER"));
            sequence.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            sequence.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));
            sequence.setAttribute(CACHE_SIZE, rs.getString("CACHE"));
            sequence.setAttribute(START_VALUE, rs.getString("START"));

            sequences.add(sequence);

        }
        return sequences;
    }

    public static List<RdbTable> convertTable(ResultSet rs, UmiTypes type) throws SQLException {
        List<RdbTable> tables = new ArrayList<>();
        while (rs.next()) {
            RdbTable table;
            //            String tableType = rs.getString("TYPE");
            if (type == UmiTypes.View) {
                table = new RdbView();
            } else if (type == UmiTypes.Table) {
                table = new RdbTable();
            } else if (type == UmiTypes.Materialized) {
                table = new RdbView();
            } else {
                throw new UnsupportedOperationException("Unsupported type: " + type);
            }
            table.setUmiType(type);

            table.setSchema(rs.getString("TABSCHEMA"));
            table.setName(rs.getString("TABNAME"));

            table.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));
            table.setAttribute(INVALID_DATE, rs.getString("INVALIDATE_TIME"));
            table.setAttribute(STATS_TIME, rs.getString("STATS_TIME"));
            table.setAttribute(COLUMN_COUNT, rs.getString("COLCOUNT"));
            table.setAttribute(ROW_COUNT, rs.getString("CARD"));
            table.setAttribute(NPAGES, rs.getString("NPAGES"));
            table.setAttribute(MPAGES, rs.getString("MPAGES"));
            table.setAttribute(FPAGES, rs.getString("FPAGES"));
            table.setAttribute(SYSTEM_TABLE, rs.getString("SYSTEM_TABLE"));
            table.setAttribute(INSERTABLE, rs.getString("IS_INSERTABLE_INTO"));
            table.setAttribute(DEFINER, rs.getString("TABLE_DEFINER"));

            table.setComment(rs.getString("REMARKS"));
            tables.add(table);
        }
        return tables;
    }

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> list = new ArrayList<>();
        while (rs.next()) {
            RdbValue val = new RdbValue();
            val.setUmiType(UmiTypes.Schema);
            val.setValue(rs.getString("SCHEMANAME"));
            list.add(val);
        }
        return list;
    }

    public static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> cats = new ArrayList<>();
        while (rs.next()) {
            RdbValue tab = new RdbValue();
            tab.setUmiType(UmiTypes.Table);
            tab.setValue(rs.getString("TABNAME"));
            String tableType = rs.getString("TYPE");

            if (StringUtils.containsAny(tableType, new char[] { 'V', 'W' })) {
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.containsAny(tableType, new char[] { 'G', 'H', 'L', 'T', 'U' })) {
                tab.setUmiType(UmiTypes.Table);
            } else if (StringUtils.containsAny(tableType, new char[] { 'S' })) {
                tab.setUmiType(UmiTypes.Materialized);
            } else {
                tab.setUmiType(null);
            }
            tab.setAttribute(COMMENT, rs.getString("REMARKS"));

            cats.add(tab);
        }
        return cats;
    }

    public static List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        Map<String, RdbValue> map = new HashMap<>();
        while (rs.next()) {
            String procID = rs.getString("ID");
            String procName = rs.getString("PROCNAME");
            String procComment = rs.getString("COMMENT");
            RdbValue value = map.get(procID);
            if (value == null) {
                value = new RdbValue();
                value.setValue(procName);
                value.setUmiType(UmiTypes.Procedure);
                value.setAttribute(COMMENT, procComment);
                value.setAttribute(RDB_OBJ_ID, procID);
                map.put(procID, value);
            }

            int pOrdinal = rs.getInt("P_ORDINAL");
            String pName = rs.getString("P_NAME");
            String pMode = rs.getString("P_MODE");
            String pType = rs.getString("P_TYPE");

            if (StringUtils.equals(pMode, "R") || StringUtils.equals(pMode, "C") || StringUtils.equals(pMode, "O") || StringUtils.equals(pMode, "OUT")/* IBM for i*/) {
                pMode = "OUT";
            } else if (StringUtils.equals(pMode, "P") || StringUtils.equals(pMode, "IN")/* IBM for i*/) {
                pMode = "IN";
            } else if (StringUtils.equals(pMode, "B") || StringUtils.equals(pMode, "INOUT")/* IBM for i*/) {
                pMode = "INOUT";
            } else {
                //C = Result after casting
                //R = Result before casting
                //S = Aggregation state variable
                pMode = "";
            }

            UmiAttributeUtils.appendProcParamToDesc(value, pName, pMode, pType);
        }
        return map.values().stream().sorted(Comparator.comparing(RdbValue::getValue)).collect(Collectors.toList());
    }

    public static List<RdbProcedure> convertProcedure(ResultSet rs) throws SQLException {
        Map<String, RdbProcedure> map = new HashMap<>();
        while (rs.next()) {
            String procID = rs.getString("ID");
            String procName = rs.getString("PROCNAME");
            String procComment = rs.getString("COMMENT");
            RdbProcedure value = map.get(procID);
            if (value == null) {
                value = new RdbProcedure();
                value.setName(procName);
                value.setUmiType(UmiTypes.Procedure);
                value.setAttribute(COMMENT, procComment);
                value.setAttribute(RDB_OBJ_ID, procID);
                map.put(procID, value);
            }

            int pOrdinal = rs.getInt("P_ORDINAL");
            String pName = rs.getString("P_NAME");
            String pType = rs.getString("P_TYPE");
            String pMode = rs.getString("P_MODE");

            if (StringUtils.equals(pMode, "R") || StringUtils.equals(pMode, "C") || StringUtils.equals(pMode, "O") || StringUtils.equals(pMode, "OUT")/* IBM for i*/) {
                pMode = "OUT";
            } else if (StringUtils.equals(pMode, "P") || StringUtils.equals(pMode, "IN")/* IBM for i*/) {
                pMode = "IN";
            } else if (StringUtils.equals(pMode, "B") || StringUtils.equals(pMode, "INOUT")/* IBM for i*/) {
                pMode = "INOUT";
            } else {
                //C = Result after casting
                //R = Result before casting
                //S = Aggregation state variable
                pMode = "";
            }

            value.setAttribute(DEBUG_MODE, rs.getString("DEBUG_MODE"));
            value.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            value.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));

            value.addParam(pOrdinal, pName, pType, RdbParamMode.valueOf(pMode));
        }
        return map.values().stream().sorted(Comparator.comparing(RdbProcedure::getName)).collect(Collectors.toList());
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        Map<String, RdbValue> map = new HashMap<>();
        while (rs.next()) {
            String funcID = rs.getString("ID");
            String funcName = rs.getString("FUNCNAME");
            String funcComment = rs.getString("COMMENT");
            RdbValue value = map.get(funcID);
            if (value == null) {
                value = new RdbValue();
                value.setValue(funcName);
                value.setUmiType(UmiTypes.Function);
                value.setAttribute(COMMENT, funcComment);
                value.setAttribute(RDB_OBJ_ID, funcID);
                map.put(funcID, value);
            }

            int pOrdinal = rs.getInt("P_ORDINAL");
            String pName = rs.getString("P_NAME");
            String pType = rs.getString("P_TYPE");
            String pMode = rs.getString("P_MODE");

            if (StringUtils.equals(pMode, "R") || StringUtils.equals(pMode, "C") || StringUtils.equals(pMode, "O") || StringUtils.equals(pMode, "OUT")/* IBM for i*/) {
                pMode = "OUT";
            } else if (StringUtils.equals(pMode, "P") || StringUtils.equals(pMode, "IN")/* IBM for i*/) {
                pMode = "IN";
            } else if (StringUtils.equals(pMode, "B") || StringUtils.equals(pMode, "INOUT")/* IBM for i*/) {
                pMode = "INOUT";
            } else {
                //C = Result after casting
                //R = Result before casting
                //S = Aggregation state variable
                pMode = "";
            }

            UmiAttributeUtils.appendFuncParamToDesc(value, pName, pMode, pType);
        }
        return map.values().stream().sorted(Comparator.comparing(Value::asValue)).collect(Collectors.toList());
    }

    public static List<RdbFunction> convertFunction(ResultSet rs) throws SQLException {
        Map<String, RdbFunction> map = new HashMap<>();
        while (rs.next()) {
            String funcID = rs.getString("ID");
            String funcName = rs.getString("FUNCNAME");
            String funcComment = rs.getString("COMMENT");
            RdbFunction value = map.get(funcID);
            if (value == null) {
                value = new RdbFunction();
                value.setName(funcName);
                value.setUmiType(UmiTypes.Function);
                value.setAttribute(COMMENT, funcComment);
                value.setAttribute(RDB_OBJ_ID, funcID);
                map.put(funcID, value);
            }

            value.setAttribute(DEBUG_MODE, rs.getString("DEBUG_MODE"));
            value.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            value.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));
            value.setAttribute(DETERMINISTIC, rs.getString("DETERMINISTIC"));

            int pOrdinal = rs.getInt("P_ORDINAL");
            String pName = rs.getString("P_NAME");
            String pType = rs.getString("P_TYPE");
            String pMode = rs.getString("P_MODE");
            if (StringUtils.equals(pMode, "R") || StringUtils.equals(pMode, "C") || StringUtils.equals(pMode, "OUT") /* IBM for i*/) {
                if (value.getReturns() != null) {
                    String returnDesc = value.getReturns().getAttribute(RdbAttributeNames.OBJ_UI_TIPS);
                    if (returnDesc.startsWith("table(")) {
                        returnDesc = returnDesc.substring("table(".length(), returnDesc.length() - 1);
                    }
                    value.getReturns().setType("table");
                    value.getReturns().setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "table(" + returnDesc + ", " + pName + " " + pType + ")");
                } else {
                    String pDesc = pName + " " + pType;
                    value.setReturn(pOrdinal, pName, pType, pDesc);
                }
            } else {
                value.addParam(pOrdinal, "", pType, RdbParamMode.IN);
            }
        }
        return map.values().stream().sorted(Comparator.comparing(RdbFunction::getName)).collect(Collectors.toList());
    }

    public static List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        List<Value> list = new ArrayList<>();
        while (rs.next()) {
            RdbValue val = new RdbValue();
            val.setUmiType(UmiTypes.Trigger);
            val.setValue(rs.getString("TRIGNAME"));

            String eventInsert = rs.getString("EVENTINSERT");
            String eventUpdate = rs.getString("EVENTUPDATE");
            String eventDelete = rs.getString("EVENTDELETE");
            String targetTab = rs.getString("TABSCHEMA") + "." + rs.getString("TABNAME");
            boolean eventInsertTag = StringUtils.equalsIgnoreCase(eventInsert, "Y") || StringUtils.equalsIgnoreCase(eventInsert, "YES");
            boolean eventUpdateTag = StringUtils.equalsIgnoreCase(eventUpdate, "Y") || StringUtils.equalsIgnoreCase(eventUpdate, "YES");
            boolean eventDeleteTag = StringUtils.equalsIgnoreCase(eventDelete, "Y") || StringUtils.equalsIgnoreCase(eventDelete, "YES");
            boolean eventOtherTag = !eventInsertTag && !eventUpdateTag && !eventDeleteTag;
            UmiAttributeUtils.appendTriggerDesc(val, targetTab, eventInsertTag, eventUpdateTag, eventDeleteTag, eventOtherTag);

            list.add(val);
        }
        return list;
    }

    public static List<RdbTrigger> convertTrigger(ResultSet rs) throws SQLException {
        List<RdbTrigger> list = new ArrayList<>();
        while (rs.next()) {
            RdbTrigger trigger = new RdbTrigger();
            trigger.setUmiType(UmiTypes.Trigger);
            trigger.setTriggerEvent(new ArrayList<>());
            trigger.setName(rs.getString("TRIGNAME"));
            trigger.setTriggerTableSchema(rs.getString("TABSCHEMA"));
            trigger.setTriggerTableName(rs.getString("TABNAME"));

            String time = rs.getString("TRIGTIME");
            if (StringUtils.equalsIgnoreCase(time, "A") || StringUtils.equalsIgnoreCase(time, "AFTER")) {
                trigger.setTriggerTime("AFTER");
            } else if (StringUtils.equalsIgnoreCase(time, "B") || StringUtils.equalsIgnoreCase(time, "BEFORE")) {
                trigger.setTriggerTime("BEFORE");
            } else if (StringUtils.equalsIgnoreCase(time, "I") || StringUtils.equalsIgnoreCase(time, "INSTEAD")) {
                trigger.setTriggerTime("INSTEAD");
            }

            String eventUpdate = rs.getString("EVENTUPDATE");
            if (StringUtils.equalsIgnoreCase(eventUpdate, "Y") || StringUtils.equalsIgnoreCase(eventUpdate, "YES")) {
                trigger.getTriggerEvent().add("UPDATE");
            }
            String eventDelete = rs.getString("EVENTDELETE");
            if (StringUtils.equalsIgnoreCase(eventDelete, "Y") || StringUtils.equalsIgnoreCase(eventDelete, "YES")) {
                trigger.getTriggerEvent().add("DELETE");
            }
            String eventInsert = rs.getString("EVENTINSERT");
            if (StringUtils.equalsIgnoreCase(eventInsert, "Y") || StringUtils.equalsIgnoreCase(eventInsert, "YES")) {
                trigger.getTriggerEvent().add("INSERT");
            }

            trigger.setSql(rs.getString("TEXT"));

            trigger.setAttribute(TRIGGER_GRANULARITY, rs.getString("GRANULARITY"));
            trigger.setAttribute(CREATE_DATE, rs.getString("CREATE_TIME"));
            trigger.setAttribute(UPDATE_DATE, rs.getString("ALTER_TIME"));
            trigger.setAttribute(DELETE_EVENT, eventDelete);
            trigger.setAttribute(UPDATE_EVENT, eventUpdate);
            trigger.setAttribute(INSERT_EVENT, eventInsert);
            trigger.setAttribute(SECURE, rs.getString("SECURE"));

            list.add(trigger);
        }
        return list;

    }

    public static List<Value> convertSequenceName(ResultSet rs) throws SQLException {
        List<Value> list = new ArrayList<>();
        while (rs.next()) {
            RdbValue val = new RdbValue();
            val.setUmiType(UmiTypes.Sequence);
            val.setValue(rs.getString("SEQNAME"));

            String minValue = rs.getString("MINVALUE");
            String maxValue = rs.getString("MAXVALUE");
            String startValue = rs.getString("START");
            String cycle = rs.getString("CYCLE");
            boolean cycleTag = StringUtils.equals(cycle, "Y") || StringUtils.equals(cycle, "YES");
            UmiAttributeUtils.appendSequenceDesc(val, minValue, maxValue, startValue, cycleTag);

            list.add(val);
        }
        return list;
    }

    public static List<RdbColumn> convertColumn(ResultSet record) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        while (record.next()) {
            RdbColumn column = new RdbColumn();
            column.setSchema(record.getString("TABSCHEMA"));
            column.setTable(record.getString("TABNAME"));
            column.setName(record.getString("COLNAME"));
            String nulls = record.getString("NULLS");
            if (!StringUtils.equalsIgnoreCase(nulls, "YES") || !StringUtils.equalsIgnoreCase(nulls, "Y")) {
                column.addConstraint(new NonNull());
            }
            column.setSqlType(Db2Types.valueOfCode(record.getString("TYPENAME")));
            column.setComment(record.getString("REMARKS"));
            column.setIndex(record.getInt("COLNO"));

            if (column.getSqlType().isNumber()) {
                column.setNumericPrecision(tryWasNull(record.getInt("LENGTH"), record));
                column.setNumericScale(tryWasNull(record.getInt("SCALE"), record));
            }
            if (column.getSqlType().isString()) {
                column.setCharLength(tryWasNull(record.getLong("LENGTH"), record));
                String collationSchema = record.getString("COLLATIONSCHEMA");
                String collationName = record.getString("COLLATIONNAME");
                column.setAttribute(COLLATION_NAME, collationSchema + "." + collationName);
            }
            if (column.getSqlType().isDataOrTime()) {
                column.setDatetimePrecision(tryWasNull(record.getInt("LENGTH"), record));
            }

            String dataDefault = record.getString("DEFAULT");
            if (dataDefault != null) {
                parseDefault(dataDefault, column);
            }

            String identity = record.getString("IDENTITY");
            if (StringUtils.equalsIgnoreCase("Y", identity) || StringUtils.equalsIgnoreCase("YES", identity)) {
                column.setAttribute(AUTO_INCREMENT, "true");
            } else {
                column.setAttribute(AUTO_INCREMENT, "false");
            }

            String generated = record.getString("GENERATED");
            if (StringUtils.equalsIgnoreCase("A", generated)) {
                column.setAttribute(GENERATED, "true");
            } else {
                column.setAttribute(GENERATED, "false");
            }

            columns.add(column);
        }
        return columns;
    }

    public static void parseDefault(final String dataDefault, RdbColumn column) {

        boolean isBinary = column.getSqlType().isBinary();
        boolean isDate = column.getSqlType().hasDate();
        boolean isNumber = column.getSqlType().isNumber();

        if (StringUtils.equalsIgnoreCase(dataDefault, "NULL")) {
            return;
        }

        String newDefault = dataDefault;
        if (newDefault.startsWith("'") && newDefault.endsWith("'")) {
            newDefault = newDefault.substring(1, newDefault.length() - 1);
        }

        if (isBinary) {
            parseBinary(newDefault, column);
        } else if (isDate) {
            parseDefaultTime(newDefault, column);
        } else if (isNumber) {
            if (NumberUtils.isNumber(newDefault)) {
                parseDefaultNumber(newDefault, column);
            } else {
                parseDefaultNumber(dataDefault, column);
            }
        } else {
            column.setDefaultValue(newDefault);
        }

    }

    public static void parseBinary(String dataDefault, RdbColumn column) {
        boolean isFunc = false;
        isFunc = isFunc | dataDefault.matches("\"SYSIBM\".\"BLOB\"(\\('(\\d+)?'\\))?");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            column.setDefaultValueIsFunc(true);
        } else {
            column.setDefaultValue(dataDefault);
        }
    }

    public static void parseDefaultNumber(String dataDefault, RdbColumn column) {
        boolean isFunc = false;
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            column.setDefaultValueIsFunc(true);
        } else {
            column.setDefaultValue(dataDefault);
        }
    }

    public static void parseDefaultTime(String dataDefault, RdbColumn column) {
        boolean isFunc = false;
        isFunc = isFunc | dataDefault.matches("\"SYSIBM\".\"TIME\"(\\('(.*)?'\\))?");
        isFunc = isFunc | dataDefault.matches("\"SYSIBM\".\"DATE\"(\\('(.*)?'\\))?");
        isFunc = isFunc | dataDefault.matches("\"SYSIBM\".\"TIMESTAMP\"(\\('(.*)?'\\))?");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "CURRENT TIME");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "CURRENT DATE");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "CURRENT TIMESTAMP");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            column.setDefaultValueIsFunc(true);
        } else {
            DateFormatType dateFormatType = DateFormatType.passerTypeWithoutUnsupported(dataDefault);
            if (dateFormatType != null) {
                column.setDefaultValue(dataDefault);
            }
        }
    }

    public static void mapToPk(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");
        RdbPrimaryKey pk = (RdbPrimaryKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbPrimaryKey()));
        pk.setName(consName);
        pk.setCatalog(null);
        pk.setSchema(rs.getString("TABLE_SCHEMA"));
        pk.setTable(rs.getString("TABLE_NAME"));
        pk.addColumn(rs.getString("COLNAME"));
    }

    public static void mapToUk(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");

        RdbUniqueKey uk = (RdbUniqueKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbUniqueKey()));
        uk.setCatalog(null);
        uk.setName(consName);
        uk.setSchema(rs.getString("TABLE_SCHEMA"));
        uk.setTable(rs.getString("TABLE_NAME"));
        uk.addColumn(rs.getString("COLNAME"));
    }

    public static List<RdbIndex> convertIndexForZos(ResultSet record) throws SQLException {
        List<RdbIndex> rdbIndices = new ArrayList<>();
        while (record.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(StringUtils.trim(record.getString("TABSCHEMA")));
            idx.setTable(record.getString("TABNAME"));
            idx.setName(record.getString("INDNAME"));

            // must be first
            String indexType = record.getString("INDEXTYPE");
            idx.setAttribute(RdbAttributeNames.INDEX_TYPE.getCodeKey(), indexType); // for Db2IndexType

            String type = record.getString("UNIQUERULE");
            if (StringUtils.equals(type, "U")) {
                idx.setType(RdbIndexType.Unique);
            } else {
                idx.setType(RdbIndexType.Normal);
            }

            String columnName = record.getString("COLNAME");
            idx.addColumn(columnName);
            rdbIndices.add(idx);
        }
        return rdbIndices;
    }

    public static List<RdbIndex> convertIndexFori(ResultSet record) throws SQLException {
        List<RdbIndex> rdbIndices = new ArrayList<>();
        while (record.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(StringUtils.trim(record.getString("TABSCHEMA")));
            idx.setTable(record.getString("TABNAME"));
            idx.setName(record.getString("INDNAME"));

            // must be first
            String indexType = record.getString("INDEXTYPE");
            if (StringUtils.equals(indexType, "V") || StringUtils.equals(indexType, "U")) {
                idx.setType(RdbIndexType.Unique);
            } else {
                idx.setType(RdbIndexType.Normal);
            }

            String columns = record.getString("COLUMNS");
            for (String col : StringUtils.split(columns, ",")) {
                idx.addColumn(col);
            }
            rdbIndices.add(idx);
        }
        return rdbIndices;
    }

    public static void fillIdxMap(Map<String, Map<String, RdbIndex>> indexesMap, List<RdbIndex> idxesFromDb) {
        for (RdbIndex index : idxesFromDb) {
            Map<String, RdbIndex> indexMap = indexesMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
            String indexName = index.getName();
            if (indexMap.containsKey(indexName)) {
                indexMap.get(indexName).getColumnList().addAll(index.getColumnList());
            } else {
                indexMap.put(indexName, index);
            }
        }
    }

    public static List<RdbForeignKey> convertForeignKey(ResultSet record) throws SQLException {
        List<RdbForeignKey> foreignKeyList = new ArrayList<>();
        while (record.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setSchema(record.getString("TABLE_SCHEMA"));
            fk.setName(record.getString("CONSTRAINT_NAME"));
            String columnName = record.getString("FKCOLUMN_NAME");
            fk.setTable(record.getString("TABLE_NAME"));

            String refSchema = record.getString("PKTABLE_SCHEM");
            String refTable = record.getString("PKTABLE_NAME");
            String refColumn = record.getString("PKCOLUMN_NAME");
            fk.addColumn(columnName, refColumn);
            fk.setReferenceSchema(refSchema);
            fk.setReferenceTable(refTable);
            Db2ForeignKeyRule deleteRule = Db2ForeignKeyRule.valueOfCode(record.getString("DELETERULE"));
            switch (deleteRule) {
                case NoAction: {
                    fk.setDeleteRule(RdbForeignKeyRule.NoAction);
                    break;
                }
                case Cascade: {
                    fk.setDeleteRule(RdbForeignKeyRule.Cascade);
                    break;
                }
                case SetNull: {
                    fk.setDeleteRule(RdbForeignKeyRule.SetNull);
                    break;
                }
                case Restrict: {
                    fk.setDeleteRule(RdbForeignKeyRule.Restrict);
                    break;
                }
                case SetDefault: {
                    fk.setDeleteRule(RdbForeignKeyRule.SetDefault);
                    break;
                }
                default: {
                    break;
                }
            }
            Db2ForeignKeyRule updateRule = Db2ForeignKeyRule.valueOfCode(record.getString("UPDATERULE"));
            switch (updateRule) {
                case NoAction: {
                    fk.setUpdateRule(RdbForeignKeyRule.NoAction);
                    break;
                }
                case Cascade: {
                    fk.setUpdateRule(RdbForeignKeyRule.Cascade);
                    break;
                }
                case SetNull: {
                    fk.setUpdateRule(RdbForeignKeyRule.SetNull);
                    break;
                }
                case Restrict: {
                    fk.setUpdateRule(RdbForeignKeyRule.Restrict);
                    break;
                }
                case SetDefault: {
                    fk.setUpdateRule(RdbForeignKeyRule.SetDefault);
                    break;
                }
                default: {
                    break;
                }
            }

            fk.getReferenceMapping().put(columnName, refColumn);

            foreignKeyList.add(fk);
        }
        return foreignKeyList;
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
