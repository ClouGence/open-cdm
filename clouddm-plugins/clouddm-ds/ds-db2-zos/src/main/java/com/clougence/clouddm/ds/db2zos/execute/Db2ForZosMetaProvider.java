package com.clougence.clouddm.ds.db2zos.execute;

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
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.ibm.com/docs/en/db2/12.1?topic=views-road-map-catalog
 * @author mode 2021/11/16 09:55:55
 */
@Slf4j
public class Db2ForZosMetaProvider extends Db2MetaProviderForBasic implements MetaDataService {

    public static int GROUP_SIZE = 800;

    public Db2ForZosMetaProvider(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet();
                PreparedStatement ps = conn.prepareStatement("SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO");
                ResultSet resultSet = ps.executeQuery()) {
            String fullVersion = ((ValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
            if (StringUtils.isBlank(fullVersion)) {
                return fullVersion;
            }

            String[] vers = fullVersion.trim().split(" ");
            if (vers.length > 1 && vers[0].contains("DB2")) {
                return vers[1];
            } else {
                return fullVersion;
            }
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "select SCHEMANAME from SYSCAT.SCHEMATA order by SCHEMANAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String schema) throws SQLException {
        String sql = "select SCHEMANAME from SYSCAT.SCHEMATA where SCHEMANAME = ?";

        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = Db2MetaProviderUtils.convertSchema(rs);
                return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
            }
        }
    }

    public List<Value> selectTables(String schema) throws SQLException {
        return this.selectByConditions(schema, "TYPE in ('G','H','L','T','U')");
    }

    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "TYPE in ('V','W')");
    }

    public List<Value> selectMaterialized(String schema) throws SQLException {
        return this.selectByConditions(schema, "TYPE in ('S')");
    }

    /**
     * according to the <a href="https://www.ibm.com/docs/zh/db2/10.5?topic=views-syscattables">doc</a>, table, view,
     * materialized view can distinct by the TYPE field of SYSCAT.TABLES
     * @param schema schema
     * @param conditions distinct table type
     * @return rdbValueList
     * @throws SQLException sqlException
     */
    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String sql = "SELECT TABNAME, TYPE, REMARKS from SYSCAT.TABLES\n" +//
                     "where TRIM(TABSCHEMA) = ? and (" + conditions + ") order by TABNAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTableName(rs);
            }
        }
    }

    public List<Value> selectProcedures(String schema) throws SQLException {
        String sql = "select r.SPECIFICNAME ID, r.ROUTINENAME PROCNAME, r.REMARKS COMMENT, p.ORDINAL P_ORDINAL, p.PARMNAME P_NAME, p.TYPENAME P_TYPE, p.ROWTYPE P_MODE\n" +//
                     "from SYSCAT.ROUTINES r left join SYSCAT.ROUTINEPARMS p on r.ROUTINESCHEMA = p.ROUTINESCHEMA and r.SPECIFICNAME = p.SPECIFICNAME\n" +//
                     "where r.ROUTINETYPE = 'P' and r.ROUTINEMODULENAME is null and r.ROUTINESCHEMA = ? order by p.ORDINAL asc";
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

        String sql = "select r.SPECIFICNAME ID, r.ROUTINENAME PROCNAME, r.REMARKS COMMENT, p.ORDINAL P_ORDINAL, p.PARMNAME P_NAME, p.TYPENAME P_TYPE, p.ROWTYPE P_MODE\n"
                     + ", r.DEBUG_MODE DEBUG_MODE,r.CREATE_TIME CREATE_TIME,r.ALTER_TIME ALTER_TIME,r.DETERMINISTIC DETERMINISTIC \n" +//
                     "from SYSCAT.ROUTINES r left join SYSCAT.ROUTINEPARMS p on r.ROUTINESCHEMA = p.ROUTINESCHEMA and r.SPECIFICNAME = p.SPECIFICNAME\n" +//
                     "where r.ROUTINETYPE = 'P' and r.ROUTINEMODULENAME is null and r.ROUTINESCHEMA = ? and r.SPECIFICNAME in " + buildWhereIn(params) + " order by p.ORDINAL asc";
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

    public List<Value> selectFunctions(String schema) throws SQLException {
        String sql = "select r.SPECIFICNAME ID, r.ROUTINENAME FUNCNAME, r.REMARKS COMMENT, p.ORDINAL P_ORDINAL, p.PARMNAME P_NAME, p.TYPENAME P_TYPE, p.ROWTYPE P_MODE\n" +//
                     "from SYSCAT.ROUTINES r left join SYSCAT.ROUTINEPARMS p on r.ROUTINESCHEMA = p.ROUTINESCHEMA and r.SPECIFICNAME = p.SPECIFICNAME\n" +//
                     "where r.ROUTINETYPE = 'F' and r.ROUTINEMODULENAME is null and r.ROUTINESCHEMA = ? order by p.ORDINAL asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertFunctionName(rs);
            }
        }
    }

    @Override
    public List<RdbFunction> loadFunction(String schema, List<String> specNames) throws SQLException {
        specNames = stringArray2List(specNames);
        if (specNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funcs : CollectionUtils.splitList(specNames, defaultGroupSize())) {
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

        String sql = "select r.SPECIFICNAME ID, r.ROUTINENAME FUNCNAME, r.REMARKS COMMENT, p.ORDINAL P_ORDINAL, p.PARMNAME P_NAME, p.TYPENAME P_TYPE, p.ROWTYPE P_MODE\n" +//
                     ", r.DEBUG_MODE DEBUG_MODE,r.CREATE_TIME CREATE_TIME,r.ALTER_TIME ALTER_TIME,r.DETERMINISTIC DETERMINISTIC \n" +//
                     "from SYSCAT.ROUTINES r left join SYSCAT.ROUTINEPARMS p on r.ROUTINESCHEMA = p.ROUTINESCHEMA and r.SPECIFICNAME = p.SPECIFICNAME\n" +//
                     "where r.ROUTINETYPE = 'F' and r.ROUTINEMODULENAME is null and r.ROUTINESCHEMA = ? and r.SPECIFICNAME in " + buildWhereIn(params) + " order by p.ORDINAL asc";
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

    public List<Value> selectTriggers(String schema) throws SQLException {
        String sql = "select TABSCHEMA, TABNAME, TRIGNAME, TRIGTIME, EVENTUPDATE, EVENTDELETE, EVENTINSERT from SYSCAT.TRIGGERS\n" +//
                     "where TRIGSCHEMA = ? order by TABSCHEMA asc, TABNAME asc, TRIGNAME asc";
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

    private List<RdbTrigger> loadTriggersByPart(Connection conn, String schema, List<String> names) throws SQLException {
        List<String> params = stringArray2List(names);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "select TABSCHEMA, TABNAME, TRIGNAME, TRIGTIME, EVENTUPDATE, EVENTDELETE, EVENTINSERT, TEXT,"
                     + "case when GRANULARITY = 'R' then 'ROW' when GRANULARITY = 'S' then 'STATEMENT' else null end GRANULARITY"
                     + ",CREATE_TIME,ALTER_TIME,SECURE  from SYSCAT.TRIGGERS\n" +//
                     "where TRIGSCHEMA = ? and TRIGNAME in " + buildWhereIn(params) + " order by TABSCHEMA asc, TABNAME asc, TRIGNAME asc";
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

    public List<Value> selectSequence(String schema) throws SQLException {
        String sql = "select SEQNAME, START, MINVALUE, MAXVALUE, CYCLE from SYSCAT.SEQUENCES " +//
                     "WHERE SEQTYPE in ('I','S') and SEQSCHEMA= ? order by SEQNAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertSequenceName(rs);
            }
        }
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(String schema, List<String> tables) throws SQLException {
        return this.loadColumns(null, schema, tables);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TYPE in ('G','H','L','T','U')", UmiTypes.Table);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TYPE in ('V','W')", UmiTypes.View);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.findTabByPart(conn, schema, tabs, "TYPE in ('S')", UmiTypes.Materialized);
    }

    @Override
    public List<RdbTable> loadMaterialized(String schema, List<String> specNames) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            return this.findTabByPart(conn, schema, specNames, "TABLES.TYPE in ('S')", UmiTypes.Materialized);
        }
    }

    @Override
    public List<RdbSequence> loadSequences(String schema, List<String> sequenceNames) throws SQLException {
        List<String> params = stringArray2List(sequenceNames);
        String sql = "select SEQNAME, START, MINVALUE, MAXVALUE, CYCLE, NEXTCACHEFIRSTVALUE CACHE_NEXT ,INCREMENT , CACHE ,ORDER,CREATE_TIME,ALTER_TIME "
                     + "from SYSCAT.SEQUENCES WHERE SEQTYPE in ('I','S') and SEQSCHEMA= ? and SEQNAME in " + buildWhereIn(sequenceNames) + " order by SEQNAME asc";
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

    private List<RdbTable> findTabByPart(Connection conn, String schema, List<String> tabs, String conditions, UmiTypes type) throws SQLException {
        List<String> params = stringArray2List(tabs);
        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT TABSCHEMA, TABNAME, TYPE, REMARKS, CREATE_TIME,ALTER_TIME,INVALIDATE_TIME,STATS_TIME,COLCOUNT,CARD,NPAGES,MPAGES,FPAGES,"
                     + " null as SYSTEM_TABLE, null as IS_INSERTABLE_INTO, null as TABLE_DEFINER from SYSCAT.TABLES\n" +//
                     "where TRIM(TABSCHEMA) = ? and (" + conditions + ") and TABNAME in " + buildWhereIn(params) + " order by TABNAME asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return Db2MetaProviderUtils.convertTable(rs, type);
            }
        }
    }

    //    private List<RdbTable> findViewByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
    //        List<String> params = stringArray2List(tabs);
    //        if (params.isEmpty()) {
    //            return Collections.emptyList();
    //        }
    //
    //        String sql = "select VIEWSCHEMA,VIEWNAME,VIEWCHECK,READONLY,TYPE,VALID,TEXT,CREATE_TIME,ALTER_TIME,COLCOUNT "
    //                     + ",null as IS_UPDATABLE,null as IS_INSERTABLE_INTO,null as IS_DELETABLE from SYSCAT.VIEWS VIEWS LEFT JOIN SYSCAT.TABLES TABLES\n"
    //                     + " on VIEWS.VIEWSCHEMA = TABLES.TABSCHEMA and VIEWS.VIEWNAME = TABLES.TABNAME" +//
    //                     " where TRIM(VIEWSCHEMA) = ? and  (" + conditions + ") and VIEWNAME in " + buildWhereIn(params) + " order by VIEWNAME asc";
    //        try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //            params.add(0, schema);
    //            for (int i = 1; i <= params.size(); i++) {
    //                ps.setString(i, params.get(i - 1));
    //            }
    //
    //            try (ResultSet rs = ps.executeQuery()) {
    //                return Db2MetaProviderUtils.convertView(rs);
    //            }
    //        }
    //    }

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

        String sql = "select COLNO ,TABSCHEMA, TABNAME, COLNAME, NULLS, TYPESCHEMA, TYPENAME, LENGTH, SCALE, \"DEFAULT\",\n" +//
                     "       REMARKS, IDENTITY, GENERATED, COLLATIONSCHEMA, COLLATIONNAME\n" +//
                     "from SYSCAT.COLUMNS where TABSCHEMA = ? and TABNAME in " + buildWhereIn(params) + " order by COLNO asc";
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

        String sql = "select i.TABSCHEMA, i.TABNAME, i.INDNAME, c.COLNAME, i.INDEXTYPE, c.COLSEQ, i.UNIQUERULE\n" +//
                     "from SYSCAT.INDEXCOLUSE as c left join SYSCAT.INDEXES as i on i.INDSCHEMA = c.INDSCHEMA and i.INDNAME = c.INDNAME\n" +//
                     "where i.INDSCHEMA = ? and TABNAME in " + buildWhereIn(params) + " order by i.INDNAME asc, c.COLSEQ asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbIndex> idxesFromDb = Db2MetaProviderUtils.convertIndexForZos(resultSet);
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
                     "from SYSIBM.TABLE_CONSTRAINTS as c\n" +//
                     "left join SYSIBM.SQLFOREIGNKEYS as f on c.TABLE_SCHEMA = f.FKTABLE_SCHEM and c.TABLE_NAME = f.FKTABLE_NAME\n" +//
                     "where trim(c.CONSTRAINT_TYPE) = 'FOREIGN KEY' and c.TABLE_SCHEMA = ? and c.TABLE_NAME in " + buildWhereIn(params) + "\n" +//
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

        String sql = "select TRIM(cons.TABLE_SCHEMA) as TABLE_SCHEMA, cons.TABLE_NAME,cons.CONSTRAINT_NAME,col.COLNAME,TRIM(cons.CONSTRAINT_TYPE) as CONSTRAINT_TYPE\n" + //
                     "from SYSIBM.SYSKEYCOLUSE AS col\n" + //
                     "left join SYSIBM.TABLE_CONSTRAINTS as cons on TRIM(col.TBCREATOR) = TRIM(cons.TABLE_SCHEMA) and\n" +//
                     "                                              col.TBNAME = cons.TABLE_NAME and col.CONSTNAME = cons.CONSTRAINT_NAME\n" + //
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
