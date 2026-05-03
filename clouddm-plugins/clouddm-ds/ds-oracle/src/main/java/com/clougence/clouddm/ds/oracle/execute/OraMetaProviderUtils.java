package com.clougence.clouddm.ds.oracle.execute;

import static com.clougence.adapter.oracle.OracleAttributeNames.*;
import static com.clougence.adapter.oracle.OracleSqlTypes.NUMBER_BIGINT;
import static com.clougence.adapter.oracle.OracleSqlTypes.NUMBER_DECIMAL;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.oracle.OracleForeignKeyRule;
import com.clougence.adapter.oracle.OracleIndexType;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Oracle 元信息获取，参考资料：
 * <li>https://docs.oracle.com/en/database/oracle/oracle-database/21/drdag/all_synonyms-drda-gateway.html#GUID-E814A6AC-5E00-4DB6-8170-DC147F7879F8</li>
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class OraMetaProviderUtils {

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("USERNAME"));
            schema.setUmiType(UmiTypes.Schema);

            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue tab = new RdbValue();
            tab.setValue(rs.getString("TABLE_NAME"));

            String tableType = rs.getString("TABLE_TYPE");
            switch (tableType) {
                case "VIEW":
                    tab.setUmiType(UmiTypes.View);
                    break;
                case "TABLE":
                    tab.setUmiType(UmiTypes.Table);
                    break;
                case "MATERIALIZED":
                    tab.setUmiType(UmiTypes.Materialized);
                    break;
                default:
                    tab.setUmiType(null);
            }

            tab.setAttribute(COMMENT, rs.getString("COMMENTS"));
            result.add(tab);
        }
        return result;
    }

    public static List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        return convertRoutineName(rs, UmiTypes.Procedure);
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        return convertRoutineName(rs, UmiTypes.Function);
    }

    public static List<Value> convertSequenceName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Sequence);
    }

    public static List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Trigger);
    }

    public static List<Value> convertDbLinkName(ResultSet rs) throws SQLException {
        return convertNameForDbLink(rs, UmiTypes.DBLink);
    }

    public static List<Value> convertJobName(ResultSet rs) throws SQLException {
        return convertNameForJob(rs, UmiTypes.Job);
    }

    public static List<Value> convertScheduleJobName(ResultSet rs) throws SQLException {
        return convertNameForScheduleJob(rs, UmiTypes.ScheduleJob);
    }

    public static List<Value> convertUserName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.USER);
    }

    public static List<Value> convertRoleName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.ROLE);
    }

    public static List<Value> convertSynonymName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Synonym);
    }

    public static List<Value> convertViewName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.View);
    }

    /**
     * resolve result from ALL_OBJECTS table by UmiTypes
     * @param rs data result from jdbc
     * @param umiTypes the types in object table such as PROCEDURE, FUNCTION, SEQUENCE, TRIGGER, SYNONYM.
     * @return object name list
     * @throws SQLException sqlException
     */
    private static List<Value> convertNameForObject(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue function = new RdbValue();
            function.setValue(rs.getString("OBJECT_NAME"));
            if (!"VALID".equalsIgnoreCase(rs.getString("STATUS"))) {
                function.setAttribute(OBJ_INVALID, "true");
            }

            function.setUmiType(umiTypes);
            result.add(function);
        }
        return result;
    }

    private static List<Value> convertNameForScheduleJob(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue function = new RdbValue();
            function.setValue(rs.getString("OBJECT_NAME"));
            function.setUmiType(umiTypes);
            function.setAttribute(OBJ_UI_TIPS, rs.getString("JOB_ACTION"));
            if (!"TRUE".equalsIgnoreCase(rs.getString("ENABLED"))) {
                function.setAttribute(OBJ_DISABLED, "true");
            }

            result.add(function);
        }
        return result;
    }

    private static List<Value> convertNameForJob(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue function = new RdbValue();
            function.setValue(rs.getString("OBJECT_NAME"));
            function.setUmiType(umiTypes);
            function.setAttribute(OBJ_UI_TIPS, rs.getString("WHAT"));
            if ("Y".equalsIgnoreCase(rs.getString("BROKEN"))) {
                function.setAttribute(OBJ_DISABLED, "true");
            }
            result.add(function);
        }
        return result;
    }

    private static List<Value> convertNameForDbLink(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue value = new RdbValue();
            value.setValue(rs.getString("OBJECT_NAME"));
            value.setUmiType(umiTypes);
            value.setAttribute(OBJ_UI_TIPS, rs.getString("HOST"));
            result.add(value);
        }
        return result;
    }

    private static List<Value> convertRoutineName(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        Map<String, Routine> map = new HashMap<>();
        Map<String, String> status = new HashMap<>();
        while (rs.next()) {
            String objectName = rs.getString("OBJECT_NAME");
            String objectStatus = rs.getString("STATUS");
            Routine routine = map.get(objectName);
            if (routine == null) {
                routine = new Routine();
                routine.setName(objectName);
                map.put(objectName, routine);
            }
            String argName = rs.getString("ARGUMENT_NAME");
            if (StringUtils.isNotBlank(argName)) {
                String type = rs.getString("DATA_TYPE");
                String length = rs.getString("LENGTH");
                routine.addParam(argName, type, length, rs.getInt("POSITION"));
            }

            status.put(objectName, objectStatus);
        }

        map.forEach((k, v) -> {
            RdbValue value = new RdbValue();
            // setting main params
            value.setValue(v.getName());
            value.setUmiType(umiTypes);
            value.setAttribute(COMMENT, v.getComment());
            value.setAttribute(OBJ_UI_TIPS, v.getParamString());
            if (!"VALID".equalsIgnoreCase(status.get(v.getName()))) {
                value.setAttribute(OBJ_INVALID, "true");
            }

            result.add(value);
        });
        return result;
    }

    public static List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        List<RdbFunction> result = new ArrayList<>();
        while (rs.next()) {
            RdbFunction rdbFunction = new RdbFunction();
            rdbFunction.setCatalog(null);
            rdbFunction.setSchema(rs.getString("OWNER"));
            rdbFunction.setName(rs.getString("OBJECT_NAME"));
            rdbFunction.setAttribute(PROCEDURE_AGGREGATE, rs.getString("AGGREGATE"));
            rdbFunction.setAttribute(PROCEDURE_PIPELINED, rs.getString("PIPELINED"));
            rdbFunction.setAttribute(PROCEDURE_PARALLEL, rs.getString("PARALLEL"));
            rdbFunction.setAttribute(PROCEDURE_DETERMINISTIC, rs.getString("DETERMINISTIC"));
            rdbFunction.setAttribute(PROCEDURE_INTERFACE, rs.getString("INTERFACE"));
            rdbFunction.setAttribute(CREATED, rs.getString("CREATED"));
            rdbFunction.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
            rdbFunction.setAttribute(OBJ_STATUS, rs.getString("STATUS"));
            result.add(rdbFunction);
        }
        return result;
    }

    public static List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setCatalog(null);
            rdbParam.setSchema(rs.getString("OWNER"));
            rdbParam.setReferenceObject(rs.getString("OBJECT_NAME"));
            rdbParam.setOrdinal(rs.getInt("POSITION"));
            rdbParam.setName(rs.getString("ARGUMENT_NAME"));
            rdbParam.setType(rs.getString("DATA_TYPE"));
            rdbParam.setCharacterMaximumLength(rs.getString("DATA_LENGTH"));
            result.add(rdbParam);
        }

        return result;
    }

    public static List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        List<RdbProcedure> result = new ArrayList<>();
        while (rs.next()) {
            RdbProcedure rdbProcedure = new RdbProcedure();
            rdbProcedure.setCatalog(null);
            rdbProcedure.setSchema(rs.getString("OWNER"));
            rdbProcedure.setName(rs.getString("OBJECT_NAME"));
            rdbProcedure.setAttribute(PROCEDURE_AGGREGATE, rs.getString("AGGREGATE"));
            rdbProcedure.setAttribute(PROCEDURE_PIPELINED, rs.getString("PIPELINED"));
            rdbProcedure.setAttribute(PROCEDURE_PARALLEL, rs.getString("PARALLEL"));
            rdbProcedure.setAttribute(PROCEDURE_DETERMINISTIC, rs.getString("DETERMINISTIC"));
            rdbProcedure.setAttribute(PROCEDURE_INTERFACE, rs.getString("INTERFACE"));
            rdbProcedure.setAttribute(CREATED, rs.getString("CREATED"));
            rdbProcedure.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
            rdbProcedure.setAttribute(OBJ_STATUS, rs.getString("STATUS"));
            result.add(rdbProcedure);
        }
        return result;
    }

    public static List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            String tableType = rs.getString("TABLE_TYPE");
            RdbTable tab;
            if (StringUtils.equalsIgnoreCase(tableType, "VIEW")) {
                RdbView rdbView = new RdbView();
                rdbView.setSql(rs.getString("SQL"));
                rdbView.setAttribute(VIEW_TEXT_LENGTH, rs.getString("TEXT_LENGTH"));
                rdbView.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
                rdbView.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
                rdbView.setAttribute(OBJ_STATUS, rs.getString("VALID_FLAG"));
                rdbView.setAttribute(VIEW_READ_ONLY, rs.getString("READ_ONLY"));
                tab = rdbView;
                tab.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "TABLE")) {
                tab = new RdbTable();
                tab.setUmiType(UmiTypes.Table);
                tab.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
                tab.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
                tab.setAttribute(OBJ_STATUS, rs.getString("VALID_FLAG"));
                tab.setAttribute(CLUSTER_NAME, rs.getString("CLUSTER_NAME"));
                tab.setAttribute(PCT_FREE, rs.getString("PCT_FREE"));
                tab.setAttribute(PCT_USED, rs.getString("PCT_USED"));
                tab.setAttribute(INI_TRANS, rs.getString("INI_TRANS"));
                tab.setAttribute(MAX_TRANS, rs.getString("MAX_TRANS"));
                tab.setAttribute(INITIAL_EXTENT, rs.getString("INITIAL_EXTENT"));
                tab.setAttribute(NEXT_EXTENT, rs.getString("NEXT_EXTENT"));
                tab.setAttribute(MIN_EXTENTS, rs.getString("MIN_EXTENTS"));
                tab.setAttribute(MAX_EXTENTS, rs.getString("MAX_EXTENTS"));
                tab.setAttribute(TEMPORARY, rs.getString("TEMPORARY"));
                tab.setAttribute(PARTITION_TABLE, rs.getString("PARTITIONED"));

            } else {
                tab = new RdbTable();
                tab.setUmiType(null);
            }
            tab.setSchema(rs.getString("OWNER"));
            tab.setName(rs.getString("TABLE_NAME"));
            tab.setComment(rs.getString("COMMENTS"));

            tab.setAttribute(TABLESPACE, rs.getString("TABLESPACE_NAME"));
            tab.setAttribute(TEMPORARY, StringUtils.toString("Y".equalsIgnoreCase(rs.getString("TEMPORARY"))));
            //tab.setAttribute(READ_ONLY, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("READ_ONLY"))));

            String logTable = rs.getString("LOG_TABLE");
            if (StringUtils.isNotBlank(logTable)) {
                tab.setAttribute(LOG_TABLE, logTable);
                tab.setAttribute(LOG_ROWIDS, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_ROWIDS"))));
                tab.setAttribute(LOG_PK, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_PK"))));
                tab.setAttribute(LOG_SEQ, StringUtils.toString("YES".equalsIgnoreCase(rs.getString("LOG_SEQ"))));
            }

            result.add(tab);
        }
        return result;
    }

    @SneakyThrows
    public static List<RdbIndex> convertIndex(ResultSet rs) {
        List<RdbIndex> result = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(rs.getString("TABLE_OWNER"));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("INDEX_NAME"));

            String indexType = rs.getString("INDEX_TYPE");
            // no use add affect ddl convert
            //            idx.setAttribute(INDEX_TYPE, indexType);
            idx.setAttribute(INDEX_OWNER, rs.getString("OWNER"));
            //idx.setAttribute(ENABLED, rs.getString("ENABLED"));

            boolean unique = "UNIQUE".equalsIgnoreCase(rs.getString("UNIQUENESS"));
            if (unique) {
                idx.setType(RdbIndexType.Unique);
            } else {
                switch (OracleIndexType.valueOfCode(indexType)) {
                    case Normal:
                        idx.setType(RdbIndexType.Normal);
                        break;
                    case Lob:
                    case NormalRev:
                    case Bitmap:
                    case FunctionBasedNormal:
                    case FunctionBasedNormalRev:
                    case FunctionBasedBitmap:
                    case FunctionBasedDomain:
                    case Cluster:
                    case IotTop:
                    case Domain:
                        idx.setType(RdbIndexType.Other);
                        break;
                    default:
                        throw new UnsupportedOperationException(indexType + " Unsupported.");
                }
            }

            String columnName = rs.getString("COLUMN_NAME");
            String columnDescend = rs.getString("DESCEND");

            Map<String, String> storageType = new HashMap<>();
            idx.addColumn(columnName);
            storageType.put(columnName, columnDescend);
            idx.setAttribute(STORAGE_TYPE, new ObjectMapper().writeValueAsString(storageType));
            result.add(idx);
        }
        return result;
    }

    @SneakyThrows
    protected static void fillIdxsMap(Map<String, Map<String, RdbIndex>> indexesMap, List<RdbIndex> idxList) {
        for (RdbIndex index : idxList) {
            String indexName = "\"" + index.getAttribute(INDEX_OWNER) + "\".\"" + index.getName() + "\"";
            Map<String, RdbIndex> indexMap = indexesMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
            if (indexMap.containsKey(indexName)) {
                indexMap.get(indexName).getColumnList().addAll(index.getColumnList());

                Map<String, String> srcPartObj = (Map<String, String>) JSON.parse(index.getAttribute(STORAGE_TYPE));
                Map<String, String> dstPartObj = (Map<String, String>) JSON.parse(indexMap.get(indexName).getAttribute(STORAGE_TYPE));
                dstPartObj.putAll(srcPartObj);
                indexMap.get(indexName).setAttribute(STORAGE_TYPE, new ObjectMapper().writeValueAsString(dstPartObj));
            } else {
                indexMap.put(indexName, index);
            }
        }
    }

    public static List<RdbForeignKey> convertForeignKey(ResultSet rs) throws SQLException {
        List<RdbForeignKey> result = new ArrayList<>();
        while (rs.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setSchema(rs.getString("OWNER"));
            fk.setTable(rs.getString("TABLE_NAME"));
            fk.setName(rs.getString("CONSTRAINT_NAME"));
            fk.setReferenceSchema(rs.getString("TARGET_OWNER"));
            fk.setReferenceTable(rs.getString("TARGET_TABLE"));

            fk.addColumn(rs.getString("SOURCE_COLUMN"), rs.getString("TARGET_COLUMN"));
            fk.setUpdateRule(null);
            fk.setDeleteRule(convertForeignKeyRule(OracleForeignKeyRule.valueOfCode(rs.getString("DELETE_RULE"))));

            fk.setAttribute(ENABLED, StringUtils.toString("ENABLED".equalsIgnoreCase(rs.getString("STATUS"))));
            fk.setAttribute(VALIDATED, StringUtils.toString("VALIDATED".equalsIgnoreCase(rs.getString("VALIDATED"))));
            fk.setAttribute(GENERATED, StringUtils.toString("GENERATED NAME".equalsIgnoreCase(rs.getString("GENERATED"))));
            result.add(fk);
        }
        return result;
    }

    protected static RdbForeignKeyRule convertForeignKeyRule(OracleForeignKeyRule rule) {
        if (rule == null) {
            return null;
        }
        switch (rule) {
            case NoAction:
                return RdbForeignKeyRule.NoAction;
            case Cascade:
                return RdbForeignKeyRule.Cascade;
            case SetNull:
                return RdbForeignKeyRule.SetNull;
            default:
                return null;
        }
    }

    public static void fillFkMap(Map<String, Map<String, RdbForeignKey>> fkMap, List<RdbForeignKey> fkList) {
        for (RdbForeignKey fk : fkList) {
            Map<String, RdbForeignKey> tabFk = fkMap.computeIfAbsent(fk.getTable(), s -> new LinkedHashMap<>());
            String fkName = fk.getName();
            if (tabFk.containsKey(fkName)) {
                tabFk.get(fkName).getColumnList().addAll(fk.getColumnList());
                tabFk.get(fkName).getReferenceMapping().putAll(fk.getReferenceMapping());
            } else {
                tabFk.put(fkName, fk);
            }
        }
    }

    public static void mapToPk(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");

        RdbPrimaryKey pk = (RdbPrimaryKey) (tabConstraint.computeIfAbsent(consName, k -> new RdbPrimaryKey()));
        pk.setSchema(rs.getString("OWNER"));
        pk.setTable(rs.getString("TABLE_NAME"));
        pk.setName(consName);
        pk.addColumn(rs.getString("COLUMN_NAME"));

        pk.setAttribute(ENABLED, StringUtils.toString("ENABLED".equalsIgnoreCase(rs.getString("STATUS"))));
        pk.setAttribute(VALIDATED, StringUtils.toString("VALIDATED".equalsIgnoreCase(rs.getString("VALIDATED"))));
        pk.setAttribute(GENERATED, StringUtils.toString("GENERATED NAME".equalsIgnoreCase(rs.getString("GENERATED"))));
    }

    public static void mapToUk(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");

        RdbUniqueKey uk = (RdbUniqueKey) (tabConstraint.computeIfAbsent(consName, k -> new RdbUniqueKey()));
        uk.setSchema(rs.getString("OWNER"));
        uk.setTable(rs.getString("TABLE_NAME"));
        uk.setName(rs.getString("CONSTRAINT_NAME"));
        uk.addColumn(rs.getString("COLUMN_NAME"));

        uk.setAttribute(ENABLED, StringUtils.toString("ENABLED".equalsIgnoreCase(rs.getString("STATUS"))));
        uk.setAttribute(VALIDATED, StringUtils.toString("VALIDATED".equalsIgnoreCase(rs.getString("VALIDATED"))));
        uk.setAttribute(GENERATED, StringUtils.toString("GENERATED NAME".equalsIgnoreCase(rs.getString("GENERATED"))));
    }

    protected static List<RdbColumn> convertColumn(ResultSet rs, boolean isLe11g) throws SQLException {
        List<RdbColumn> result = new ArrayList<>();
        while (rs.next()) {
            if ("YES".equals(rs.getString("HIDDEN_COLUMN"))) {
                continue;
            }

            RdbColumn column = new RdbColumn();
            column.setSchema(rs.getString("OWNER"));
            column.setTable(rs.getString("TABLE_NAME"));
            column.setName(rs.getString("COLUMN_NAME"));
            column.setIndex(rs.getInt("COLUMN_ID"));
            column.setComment(rs.getString("COMMENTS"));

            if (!"Y".equals(rs.getString("NULLABLE"))) {
                column.addConstraint(new NonNull());
            }

            Integer dataPrecision = tryWasNull(rs.getInt("DATA_PRECISION"), rs);
            Integer dataScale = tryWasNull(rs.getInt("DATA_SCALE"), rs);
            column.setSqlType(safeToOracleTypes(rs.getString("DATA_TYPE"), dataPrecision, dataScale));

            column.setCharLength(tryWasNull(rs.getLong("CHAR_LENGTH"), rs));
            column.setByteLength(tryWasNull(rs.getLong("DATA_LENGTH"), rs));
            column.setNumericPrecision(dataPrecision);
            column.setNumericScale(dataScale);

            switch ((OracleSqlTypes) column.getSqlType()) {
                case TIMESTAMP:
                case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                case TIMESTAMP_WITH_TIME_ZONE:
                    column.setDatetimePrecision(column.getNumericScale());
                    break;
                default:
                    break;
            }

            column.setAttribute(CHARACTER_SET_NAME, rs.getString("CHARACTER_SET_NAME"));
            column.setAttribute(GENERATED, StringUtils.toString("YES".equals(rs.getString("VIRTUAL_COLUMN"))));
            if (!isLe11g) {
                column.setAttribute(AUTO_INCREMENT, StringUtils.toString("YES".equals(rs.getString("IDENTITY_COLUMN"))));
                //column.setAttribute(SENSITIVE, StringUtils.toString("YES".equals(rs.getString("SENSITIVE_COLUMN"))));
            }

            String dataDefault = rs.getString("DATA_DEFAULT");
            if (dataDefault != null) {
                passerDefault(dataDefault, column);
            }
            result.add(column);
        }
        return result;
    }

    protected static OracleSqlTypes safeToOracleTypes(Object obj, Integer dataPrecision, Integer dataScale) {
        if (obj == null) {
            return null;
        }
        return OracleSqlTypes.toOracleType(obj.toString(), dataPrecision, dataScale);
    }

    protected static void passerDefault(String dataDefault, RdbColumn column) {
        if (StringUtils.equalsIgnoreCase(dataDefault, "null")) {
            return;
        }

        String tempStr = dataDefault.trim();
        if (tempStr.startsWith("'") && tempStr.endsWith("'")) {
            dataDefault = tempStr.substring(1, tempStr.length() - 1);
            column.setDefaultValue(dataDefault);
        } else {
            if ((column.getSqlType() == NUMBER_BIGINT || column.getSqlType() == NUMBER_DECIMAL) && NumberUtils.isNumber(dataDefault)) {
                column.setDefaultValue(dataDefault);
            } else {
                // https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/SYSTIMESTAMP.html
                column.setDefaultValue(dataDefault);
                column.setDefaultValueIsFunc(true);
            }
        }
    }

    public static List<RdbTrigger> convertTrigger(ResultSet rs) throws SQLException {
        List<RdbTrigger> result = new ArrayList<>();
        HashMap<String, RdbTrigger> map = new HashMap<>();
        while (rs.next()) {
            String triggerName = rs.getString("TRIGGER_NAME");
            RdbTrigger rdbTrigger = map.get(triggerName);
            if (rdbTrigger == null) {
                rdbTrigger = new RdbTrigger();
                rdbTrigger.setName(triggerName);
                rdbTrigger.setSql(rs.getString("TRIGGER_BODY"));
                String triggerType = rs.getString("TRIGGER_TYPE");
                rdbTrigger.setTriggerTime(triggerType.split(" ")[0]);
                // parse trigger events
                String[] events = rs.getString("TRIGGERING_EVENT").toLowerCase().split(" or ");
                rdbTrigger.setTriggerEvent(Arrays.stream(events).collect(Collectors.toList()));
                rdbTrigger.setTriggerTableName(rs.getString("TABLE_NAME"));
                // features
                String referencingName = rs.getString("REFERENCING_NAMES");
                HashMap<String, Object> features = new HashMap<>();
                features.put(NEW_ALIAS.getCodeKey(), referencingName.split(" ")[3]);
                features.put(OLD_ALIAS.getCodeKey(), referencingName.split(" ")[6]);
                features.put(CONDITION.getCodeKey(), rs.getString("WHEN_CLAUSE"));
                features.put(TRIGGER_GRANULARITY.getCodeKey(), triggerType.contains("ROW") ? "row" : "statement");

                rdbTrigger.setAttribute(OBJ_ENABLED.getCodeKey(), rs.getString("STATUS"));
                rdbTrigger.setAttribute(OBJ_STATUS.getCodeKey(), rs.getString("OBJ_STATUS"));
                rdbTrigger.setAttribute(CREATE_TIME.getCodeKey(), rs.getString("CREATE_TIME"));
                rdbTrigger.setAttribute(LAST_DDL_TIME.getCodeKey(), rs.getString("LAST_DDL_TIME"));

                rdbTrigger.setFeatures(features);
                // init
                rdbTrigger.setTriggerTableColumns(new LinkedList<>());
                map.put(triggerName, rdbTrigger);
            }
            String columnName = rs.getString("COLUMN_NAME");
            if (columnName != null) {
                rdbTrigger.getTriggerTableColumns().add(columnName);
            }
        }
        map.forEach((k, v) -> {
            result.add(v);
        });
        return result;
    }

    public static List<RdbJob> convertJob(ResultSet rs) throws SQLException {
        List<RdbJob> result = new ArrayList<>();
        while (rs.next()) {
            RdbJob job = new RdbJob();
            job.setName(rs.getString("JOB"));
            job.setExecSql(rs.getString("WHAT"));
            job.setRunning("N".equalsIgnoreCase(rs.getString("BROKEN")));
            job.setCreator(rs.getString("LOG_USER"));
            job.setSchema(rs.getString("SCHEMA_USER"));
            String interval = rs.getString("INTERVAL");
            job.setInterval(interval.replace("'", "''"));
            job.setAttribute(JOB_LAST_EXEC, rs.getString("LAST_DATE"));
            job.setAttribute(JOB_NEXT_EXEC, rs.getString("NEXT_DATE"));
            result.add(job);
        }
        return result;
    }

    public static List<RdbScheduleJob> convertScheduleJob(ResultSet rs) throws SQLException {
        List<RdbScheduleJob> result = new ArrayList<>();
        while (rs.next()) {
            RdbScheduleJob job = new RdbScheduleJob();
            job.setName(rs.getString("JOB_NAME"));
            job.setExecSql(rs.getString("JOB_ACTION"));
            job.setEnabled(rs.getString("ENABLED"));
            job.setCreator(rs.getString("JOB_CREATOR"));
            job.setSchema(rs.getString("OWNER"));
            job.setComment(rs.getString("COMMENTS"));
            job.setStatus(rs.getString("STATE"));

            job.setAttribute(SCHEDULE_JOB_START_DATE, rs.getString("START_DATE"));
            job.setAttribute(SCHEDULE_JOB_TYPE, rs.getString("JOB_TYPE"));
            job.setAttribute(SCHEDULE_JOB_AUTO_DROP, rs.getString("AUTO_DROP"));
            job.setAttribute(SCHEDULE_JOB_END_DATE, rs.getString("END_DATE"));
            job.setAttribute(SCHEDULE_JOB_REPEAT_INTERVAL, rs.getString("REPEAT_INTERVAL"));
            result.add(job);
        }
        return result;
    }

    public static List<RdbDbLink> convertDbLink(ResultSet rs) throws SQLException {
        List<RdbDbLink> result = new ArrayList<>();
        while (rs.next()) {
            RdbDbLink link = new RdbDbLink();
            link.setHost(rs.getString("HOST"));
            link.setName(rs.getString("DB_LINK"));
            link.setUsername(rs.getString("USERNAME"));
            link.setSchema(rs.getString("OWNER"));
            link.setAttribute(RDB_OBJ_CREATE_TIME, rs.getString("CREATED"));
            result.add(link);
        }
        return result;
    }

    public static List<RdbView> convertMaterialized(ResultSet rs) throws SQLException {
        List<RdbView> result = new ArrayList<>();
        while (rs.next()) {
            RdbView view = new RdbView();
            view.setSchema(rs.getString("OWNER"));
            view.setName(rs.getString("MVIEW_NAME"));
            view.setSql(rs.getString("QUERY"));
            view.setAttribute(VIEW_TEXT_LENGTH, rs.getString("QUERY_LEN"));
            view.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            view.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
            view.setAttribute(MVIEW_REFRESH_DATE, rs.getString("LAST_REFRESH_DATE"));
            view.setAttribute(MVIEW_REFRESH_END_TIME, rs.getString("LAST_REFRESH_END_TIME"));
            view.setAttribute(OBJ_STATUS, rs.getString("STATUS"));
            result.add(view);
        }
        return result;
    }

    public static List<RdbSequence> convertSequence(ResultSet rs) throws SQLException {
        List<RdbSequence> result = new ArrayList<>();
        while (rs.next()) {
            RdbSequence sequence = new RdbSequence();
            sequence.setSchema(rs.getString("SEQUENCE_OWNER"));
            sequence.setName(rs.getString("SEQUENCE_NAME"));
            sequence.setIncrementBy(rs.getString("INCREMENT_BY"));
            sequence.setMinValue(rs.getString("MIN_VALUE"));
            sequence.setMaxValue(rs.getString("MAX_VALUE"));

            sequence.setAttribute(SEQUENCE_CYCLE_FLAG, rs.getString("CYCLE_FLAG"));
            sequence.setAttribute(SEQUENCE_CACHE_SIZE, rs.getString("CACHE_SIZE"));
            sequence.setAttribute(SEQUENCE_LAST_NUMBER, rs.getString("LAST_NUMBER"));
            sequence.setAttribute(SEQUENCE_SESSION_FLAG, rs.getString("SESSION_FLAG"));
            sequence.setAttribute(SEQUENCE_KEEP_VALUE, rs.getString("KEEP_VALUE"));
            sequence.setAttribute(OBJ_STATUS, rs.getString("STATUS"));
            sequence.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            sequence.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));
            result.add(sequence);
        }
        return result;
    }

    public static List<RdbUser> convertUser(ResultSet rs) throws SQLException {
        List<RdbUser> result = new ArrayList<>();
        while (rs.next()) {
            RdbUser user = new RdbUser();
            user.setUsername(rs.getString("USERNAME"));
            user.setAttribute(USER_ID, rs.getString("USER_ID"));
            user.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            user.setAttribute(USER_OR_ROLE_COMMON, rs.getString("COMMON"));
            user.setAttribute(USER_OR_ROLE_ORACLE_MAINTAINED, rs.getString("ORACLE_MAINTAINED"));

            result.add(user);
        }
        return result;
    }

    public static List<RdbRole> convertRole(ResultSet rs) throws SQLException {
        List<RdbRole> result = new ArrayList<>();
        while (rs.next()) {
            RdbRole role = new RdbRole();
            role.setRoleName(rs.getString("ROLE"));
            role.setAttribute(ROLE_AUTHENTICATION_TYPE, rs.getString("AUTHENTICATION_TYPE"));
            role.setAttribute(USER_OR_ROLE_COMMON, rs.getString("COMMON"));
            role.setAttribute(USER_OR_ROLE_ORACLE_MAINTAINED, rs.getString("ORACLE_MAINTAINED"));

            result.add(role);
        }
        return result;
    }

    public static List<RdbSynonym> convertSynonym(ResultSet rs) throws SQLException {
        List<RdbSynonym> result = new ArrayList<>();
        while (rs.next()) {
            RdbSynonym role = new RdbSynonym();
            role.setName(rs.getString("SYNONYM_NAME"));
            role.setSchema(rs.getString("OWNER"));
            role.setTable(rs.getString("TABLE_NAME"));
            role.setTableSchema(rs.getString("TABLE_OWNER"));

            role.setAttribute(SYNONYM_TABLE_DBLINK, rs.getString("DB_LINK"));
            role.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            role.setAttribute(OBJ_STATUS, rs.getString("STATUS"));
            role.setAttribute(LAST_DDL_TIME, rs.getString("LAST_DDL_TIME"));

            result.add(role);
        }
        return result;
    }

    private static class Routine extends RoutineUmiData {

    }
}
