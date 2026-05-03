package com.clougence.clouddm.ds.db2i.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaProviderForBasic;
import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaProviderUtils;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.ibm.com/docs/zh/i/7.5?topic=views-i-catalog-tables
 * @author mode 2021/11/16 09:55:55
 */
@Slf4j
public class Db2MetaProviderForAs400 extends Db2MetaProviderForBasic implements MetaDataService {

    public static int GROUP_SIZE = 800;

    public Db2MetaProviderForAs400(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet();
                PreparedStatement ps = conn.prepareStatement("select OS_NAME, OS_VERSION, OS_RELEASE from SYSIBMADM.ENV_SYS_INFO");
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            String osName = rs.getString("OS_NAME");
            String osVersion = rs.getString("OS_VERSION");
            String osRelease = rs.getString("OS_RELEASE");
            return osName + " " + osVersion + "." + osRelease;
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "SELECT CATALOG_NAME,CATALOG_STATUS FROM QSYS2.SYSCATALOGS order by CATALOG_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertCatalog(rs);
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        String sql = "SELECT CATALOG_NAME,CATALOG_STATUS FROM QSYS2.SYSCATALOGS where CATALOG_NAME = ? order by CATALOG_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, catalog);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = Db2MetaProviderUtils.convertCatalog(rs);
                return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
            }
        }
    }

    @Override
    public List<Value> selectSchemas() throws SQLException {
        String sql = "select SCHEMA_NAME SCHEMANAME FROM QSYS2.SYSSCHEMAS order by SCHEMA_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql);) {
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertSchema(rs);
            }
        }
    }

    @Override
    public Value selectSchema(String schema) throws SQLException {
        String sql = "select SCHEMA_NAME SCHEMANAME FROM QSYS2.SYSSCHEMAS where SCHEMA_NAME = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = Db2MetaProviderUtils.convertSchema(rs);
                return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
            }
        }
    }

    @Override
    public List<Value> selectTables(String schema) throws SQLException {
        return this.selectByConditions(schema, "TABLE_TYPE in ('BASE TABLE')");
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "TABLE_TYPE in ('VIEW')");
    }

    @Override
    public List<Value> selectMaterialized(String schema) throws SQLException {
        return this.selectByConditions(schema, "TABLE_TYPE in ('MATERIALIZED QUERY TABLE')");
    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String sql = "select TRIM(TABLE_SCHEMA) TABSCHEMA, TABLE_NAME TABNAME, TABLE_TYPE TYPE, '' REMARKS from QSYS2.TABLES\n" +//
                     "where (" + conditions + ") and TRIM(TABLE_SCHEMA) = ? order by TABLE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTableName(rs);
            }
        }
    }

    @Override
    public List<Value> selectProcedures(String schema) throws SQLException {
        String sql = "select r.SPECIFIC_NAME ID, r.ROUTINE_NAME PROCNAME, r.LONG_COMMENT COMMENT,\n" +//
                     "       p.ORDINAL_POSITION P_ORDINAL, p.PARAMETER_NAME P_NAME, p.DATA_TYPE P_TYPE, p.PARAMETER_MODE P_MODE\n" +//
                     "from QSYS2.SYSROUTINES r left join QSYS2.SYSPARMS p on r.SPECIFIC_SCHEMA = p.SPECIFIC_SCHEMA and r.SPECIFIC_NAME = p.SPECIFIC_NAME\n" +//
                     "where r.ROUTINE_TYPE = 'PROCEDURE' and r.ROUTINE_SCHEMA = ? order by p.ORDINAL_POSITION asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertProcedureName(rs);
            }
        }
    }

    @Override
    public List<RdbProcedure> loadProcedure(String schema, List<String> procedureNames) throws SQLException {
        procedureNames = stringArray2List(procedureNames);
        if (procedureNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbProcedure> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> procs : CollectionUtils.splitList(procedureNames, defaultGroupSize())) {
                List<RdbProcedure> byPart = this.fetchProcedureByPart(conn, schema, procs);
                result.addAll(byPart);
            }
        }
        return result;
    }

    private List<RdbProcedure> fetchProcedureByPart(Connection conn, String schema, List<String> names) throws SQLException {
        List<String> params = stringArray2List(names);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "select r.SPECIFIC_NAME ID, r.ROUTINE_NAME PROCNAME, r.LONG_COMMENT COMMENT,\n" +//
                     "       p.ORDINAL_POSITION P_ORDINAL, p.PARAMETER_NAME P_NAME, p.DATA_TYPE P_TYPE, p.PARAMETER_MODE P_MODE\n" +//
                     ", r.DEBUG_MODE DEBUG_MODE,r.ROUTINE_CREATED CREATE_TIME,r.LAST_ALTERED ALTER_TIME,r.IS_DETERMINISTIC DETERMINISTIC "
                     + "from QSYS2.SYSROUTINES r left join QSYS2.SYSPARMS p on r.SPECIFIC_SCHEMA = p.SPECIFIC_SCHEMA and r.SPECIFIC_NAME = p.SPECIFIC_NAME\n" +//
                     "where r.ROUTINE_TYPE = 'PROCEDURE' and r.ROUTINE_SCHEMA = ? and r.SPECIFIC_NAME in " + buildWhereIn(params) + " order by p.ORDINAL_POSITION asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertProcedure(rs);
            }
        }
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String sql = "select r.SPECIFIC_NAME ID, r.ROUTINE_NAME FUNCNAME, r.LONG_COMMENT COMMENT,\n" +//
                     "       p.ORDINAL_POSITION P_ORDINAL, p.PARAMETER_NAME P_NAME, p.DATA_TYPE P_TYPE, p.PARAMETER_MODE P_MODE\n" +//
                     "from QSYS2.SYSROUTINES r left join QSYS2.SYSPARMS p on r.SPECIFIC_SCHEMA = p.SPECIFIC_SCHEMA and r.SPECIFIC_NAME = p.SPECIFIC_NAME\n" +//
                     "where r.ROUTINE_TYPE = 'FUNCTION' and r.ROUTINE_SCHEMA = ? order by p.ORDINAL_POSITION asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertFunctionName(rs);
            }
        }
    }

    @Override
    public List<RdbFunction> loadFunction(String schema, List<String> functionNames) throws SQLException {
        functionNames = stringArray2List(functionNames);
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funcs : CollectionUtils.splitList(functionNames, defaultGroupSize())) {
                List<RdbFunction> byPart = this.loadFunctionByPart(conn, schema, funcs);
                result.addAll(byPart);
            }
        }
        return result;
    }

    private List<RdbFunction> loadFunctionByPart(Connection conn, String schema, List<String> names) throws SQLException {
        List<String> params = stringArray2List(names);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "select r.SPECIFIC_NAME ID, r.ROUTINE_NAME FUNCNAME, r.LONG_COMMENT COMMENT," +//
                     "       p.ORDINAL_POSITION P_ORDINAL, p.PARAMETER_NAME P_NAME, p.DATA_TYPE P_TYPE, p.PARAMETER_MODE P_MODE\n" +//
                     ", r.DEBUG_MODE DEBUG_MODE,r.ROUTINE_CREATED CREATE_TIME,r.LAST_ALTERED ALTER_TIME,r.IS_DETERMINISTIC DETERMINISTIC "
                     + "from QSYS2.SYSROUTINES r left join QSYS2.SYSPARMS p on r.SPECIFIC_SCHEMA = p.SPECIFIC_SCHEMA and r.SPECIFIC_NAME = p.SPECIFIC_NAME\n" +//
                     "where r.ROUTINE_TYPE = 'FUNCTION' and r.ROUTINE_SCHEMA = ? and r.SPECIFIC_NAME in " + buildWhereIn(params) + " order by p.ORDINAL_POSITION asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertFunction(rs);
            }
        }
    }

    @Override
    public List<Value> selectTriggers(String schema) throws SQLException {
        String sql = "select EVENT_OBJECT_SCHEMA TABSCHEMA, EVENT_OBJECT_TABLE TABNAME, TRIGGER_NAME TRIGNAME,\n" +//
                     "       ACTION_TIMING TRIGTIME, EVENTUPDATE, EVENTDELETE, EVENTINSERT from QSYS2.SYSTRIGGERS\n" +//
                     "WHERE TRIGGER_SCHEMA = ? order by EVENT_OBJECT_SCHEMA asc, EVENT_OBJECT_TABLE asc, TRIGGER_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTriggerName(rs);
            }
        }
    }

    @Override
    public List<RdbTrigger> loadTriggers(String schema, List<String> specNames) throws SQLException {
        specNames = stringArray2List(specNames);
        if (specNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbTrigger> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funcs : CollectionUtils.splitList(specNames, defaultGroupSize())) {
                List<RdbTrigger> byPart = this.loadTriggersByPart(conn, schema, funcs);
                result.addAll(byPart);
            }
        }
        return result;
    }

    @Override
    public List<RdbTable> loadMaterialized(String schema, List<String> specNames) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            return this.findTabByPart(conn, schema, specNames, "TABLE_TYPE in ('M')", UmiTypes.Materialized);
        }
    }

    private List<RdbTrigger> loadTriggersByPart(Connection conn, String schema, List<String> names) throws SQLException {
        List<String> params = stringArray2List(names);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "select EVENT_OBJECT_SCHEMA TABSCHEMA, EVENT_OBJECT_TABLE TABNAME, TRIGGER_NAME TRIGNAME,\n" +//
                     "       ACTION_TIMING TRIGTIME, EVENTUPDATE, EVENTDELETE, EVENTINSERT, TRIGGER_TEXT TEXT,"
                     + " ACTION_ORIENTATION GRANULARITY,CREATED CREATE_TIME,LAST_ALTERED ALTER_TIME,SECURE from QSYS2.SYSTRIGGERS\n" +//
                     "WHERE TRIGGER_SCHEMA = ? and TRIGGER_NAME in " + buildWhereIn(params) + " order by EVENT_OBJECT_SCHEMA asc, EVENT_OBJECT_TABLE asc, TRIGGER_NAME asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTrigger(rs);
            }
        }
    }

    @Override
    public List<Value> selectSequence(String schema) throws SQLException {
        String sql = "select SEQUENCE_NAME SEQNAME, START, MINIMUM_VALUE MINVALUE, MAXIMUM_VALUE MAXVALUE, CYCLE_OPTION CYCLE\n" +//
                     "from QSYS2.SYSSEQUENCES where SEQUENCE_SCHEMA= ? order by SEQUENCE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertSequenceName(rs);
            }
        }
    }

    @Override
    public List<RdbSequence> loadSequences(String schema, List<String> sequenceNames) throws SQLException {
        List<String> params = stringArray2List(sequenceNames);
        String sql = "select SEQUENCE_NAME SEQNAME, START,MINIMUM_VALUE as MINVALUE,MAXIMUM_VALUE as MAXVALUE,CYCLE_OPTION as CYCLE, null as CACHE_NEXT ,INCREMENT , CACHE "
                     + ",ORDER,SEQUENCE_CREATED as CREATE_TIME, LAST_ALTERED_TIMESTAMP as ALTER_TIME " + "from SYSCAT.SEQUENCES WHERE SEQUENCE_SCHEMA= ? and SEQUENCE_NAME in "
                     + buildWhereIn(sequenceNames) + " order by SEQUENCE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertSequence(rs);
            }
        }
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String schema, List<String> tables) throws SQLException {
        return this.loadColumns(null, schema, tables);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TABLE_TYPE in ('T','P')", UmiTypes.Table);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TABLE_TYPE in ('V','L')", UmiTypes.View);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TABLE_TYPE in ('M')", UmiTypes.Materialized);
    }

    //    private List<RdbTable> findViewByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
    //        List<String> params = stringArray2List(tabs);
    //        if (params.isEmpty()) {
    //            return Collections.emptyList();
    //        }
    //
    //        String sql ="select TRIM(TABLE_OWNER) TABSCHEMA, TABLE_NAME TABNAME, TABLE_TYPE TYPE, '' REMARKS,'' CREATE_TIME,LAST_ALTERED_TIMESTAMP ALTER_TIME,"
    //                + "'' as INVALIDATE_TIME,'' STATS_TIME,COLUMN_COUNT COLCOUNT,'' as CARD,''  NPAGES,'' MPAGES,'' FPAGES, "
    //                + " SYSTEM_TABLE,IS_INSERTABLE_INTO,TABLE_DEFINER from QSYS2.SYSTABLES\n" +//
    //                "where (" + conditions + ") and TRIM(TABLE_OWNER) = ? and TABLE_NAME in " + buildWhereIn(params) + " order by TABLE_NAME asc";;
    //        try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //            params.add(0, schema);
    //            for (int i = 1; i <= params.size(); i++) {
    //                ps.setString(i, params.get(i - 1));
    //            }
    //
    //            try (ResultSet rs = ps.executeQuery()) {
    //                return Db2MetaProviderUtils.convertTable(rs);
    //            }
    //        }
    //    }

    private List<RdbTable> findTabByPart(Connection conn, String schemaName, List<String> tabs, String conditions, UmiTypes type) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "select TRIM(TABLE_OWNER) TABSCHEMA, TABLE_NAME TABNAME, TABLE_TYPE TYPE, '' REMARKS,'' CREATE_TIME,LAST_ALTERED_TIMESTAMP ALTER_TIME,"
                     + "'' as INVALIDATE_TIME,'' STATS_TIME,COLUMN_COUNT COLCOUNT,'' as CARD,''  NPAGES,'' MPAGES,'' FPAGES, "
                     + " SYSTEM_TABLE,IS_INSERTABLE_INTO,TABLE_DEFINER from QSYS2.SYSTABLES\n" +//
                     "where (" + conditions + ") and TRIM(TABLE_OWNER) = ? and TABLE_NAME in " + buildWhereIn(params) + " order by TABLE_NAME asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schemaName);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTable(resultSet, type);
            }
        }
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }

        List<List<String>> groupTabs = CollectionUtils.splitList(params, GROUP_SIZE);
        Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
        for (List<String> tables : groupTabs) {
            Map<String, List<RdbColumn>> tabCols = this.fetchTabsCols(conn, schema, tables);
            tabCols.forEach((key, columns) -> result.computeIfAbsent(key, k -> new ArrayList<>()).addAll(columns));
        }
        return result;
    }

    private Map<String, List<RdbColumn>> fetchTabsCols(Connection conn, String schema, List<String> tabs) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "select ORDINAL_POSITION COLNO ,TABLE_SCHEMA TABSCHEMA, TABLE_NAME TABNAME, COLUMN_NAME COLNAME, IS_NULLABLE NULLS,\n" +//
                     "       '' TYPESCHEMA, DATA_TYPE TYPENAME, LENGTH, NUMERIC_SCALE SCALE, COLUMN_DEFAULT \"DEFAULT\",\n" +//
                     "       LONG_COMMENT REMARKS, IS_IDENTITY IDENTITY, '' GENERATED, '' COLLATIONSCHEMA, '' COLLATIONNAME\n" +//
                     "from QSYS2.SYSCOLUMNS\n" +//
                     "where TABLE_SCHEMA = ? and TABLE_NAME in " + buildWhereIn(params) + " order by COLNO asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbColumn> columns = Db2MetaProviderUtils.convertColumn(resultSet);
                if (columns.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "select i.TABLE_SCHEMA TABSCHEMA, i.TABLE_NAME TABNAME, i.INDEX_NAME INDNAME, i.IS_UNIQUE INDEXTYPE, '' COLNAME, 0 COLSEQ, s.COLUMN_NAMES COLUMNS\n" +//
                     "from QSYS2.SYSINDEXES i left join QSYS2.SYSINDEXSTAT s on i.INDEX_SCHEMA = s.INDEX_SCHEMA and i.INDEX_NAME = s.INDEX_NAME\n" +//
                     "where i.TABLE_SCHEMA = ? and i.TABLE_NAME in " + buildWhereIn(params) + " order by i.INDEX_NAME asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbIndex> idxesFromDb = Db2MetaProviderUtils.convertIndexFori(resultSet);
                if (idxesFromDb.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbIndex>> indexesMap = new LinkedHashMap<>();
                Db2MetaProviderUtils.fillIdxMap(indexesMap, idxesFromDb);
                return indexesMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "select TRIM(c.TABLE_SCHEMA) as TABLE_SCHEMA, c.TABLE_NAME, c.CONSTRAINT_NAME, TRIM(c.CONSTRAINT_TYPE) as CONSTRAINT_TYPE,\n" +//
                     "       f.FKCOLUMN_NAME, f.PKCOLUMN_NAME,\n" +//
                     "       f.PKTABLE_SCHEM, f.PKTABLE_NAME,\n" +//
                     "       case when UPDATE_RULE = 1 then 'R'\n" +//RESTRICT
                     "            when UPDATE_RULE = 3 then 'A'\n" +//NO ACTION
                     "            else '' end UPDATERULE,\n" +//
                     "       case when DELETE_RULE = 0 then 'C'\n" +//CASCADE
                     "            when DELETE_RULE = 1 then 'R'\n" +//RESTRICT
                     "            when DELETE_RULE = 2 then 'N'\n" +//SET NULL
                     "            when DELETE_RULE = 3 then 'A'\n" +//NO ACTION
                     "            when DELETE_RULE = 4 then 'D'\n" +//SET DEFAULT
                     "            else '' end DELETERULE\n" +//
                     "from QSYS2.TABLE_CONSTRAINTS as c\n" +//
                     "left join SYSIBM.SQLFOREIGNKEYS as f on c.TABLE_SCHEMA = f.FKTABLE_SCHEM and c.TABLE_NAME = f.FKTABLE_NAME\n" +//
                     "where trim(c.CONSTRAINT_TYPE) = 'FOREIGN KEY' and c.TABLE_SCHEMA = ? and c.TABLE_NAME in " + buildWhereIn(tabs) + "\n" +//
                     "order by c.TABLE_SCHEMA asc, c.TABLE_NAME asc, c.CONSTRAINT_NAME asc, f.KEY_SEQ asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbForeignKey> fksFromDb = Db2MetaProviderUtils.convertForeignKey(resultSet);
                if (fksFromDb.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbForeignKey>> tableFkMap = new LinkedHashMap<>();
                Db2MetaProviderUtils.fillFkMap(tableFkMap, fksFromDb);
                return tableFkMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "select TRIM(cons.TABLE_SCHEMA) as TABLE_SCHEMA, cons.TABLE_NAME, cons.CONSTRAINT_NAME,\n" +//
                     "       col.COLUMN_NAME COLNAME, TRIM(cons.CONSTRAINT_TYPE) as CONSTRAINT_TYPE\n" +//
                     "from QSYS2.SYSCSTCOL AS col\n" +//
                     "left join QSYS2.SYSCST as cons on col.CONSTRAINT_SCHEMA = cons.CONSTRAINT_SCHEMA and col.CONSTRAINT_NAME = cons.CONSTRAINT_NAME\n" +//
                     "where TRIM(cons.CONSTRAINT_TYPE) in ('PRIMARY KEY', 'UNIQUE') and cons.TABLE_SCHEMA = ? and cons.TABLE_NAME in " + buildWhereIn(params);
        Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = StringUtils.trim(rs.getString("CONSTRAINT_TYPE"));
                    String table = rs.getString("TABLE_NAME");
                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    if (type.equals("PRIMARY KEY")) {
                        Db2MetaProviderUtils.mapToPk(rs, constraints);
                    } else if (type.equals("UNIQUE")) {
                        Db2MetaProviderUtils.mapToUk(rs, constraints);
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint type:" + type);
                    }
                }
            }
        }

        return pkUkMap;
    }
}
