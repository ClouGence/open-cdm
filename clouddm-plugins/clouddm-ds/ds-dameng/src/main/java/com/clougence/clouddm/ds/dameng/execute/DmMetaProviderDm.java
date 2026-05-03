package com.clougence.clouddm.ds.dameng.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;
import com.clougence.utils.jdbc.mapper.StringMapRowMapper;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 达梦 元信息获取，参考资料：
 * <li>https://docs.oracle.com/en/database/oracle/oracle-database/21/drdag/all_synonyms-drda-gateway.html#GUID-E814A6AC-5E00-4DB6-8170-DC147F7879F8</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-29
 */
@Slf4j
public class DmMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    private static final String TABLE        = "SELECT TAB.OWNER,TAB.TABLE_NAME,DIS.TABLE_TYPE,COM.COMMENTS,TAB.TEMPORARY,TAB.TABLESPACE_NAME\n" //
                                               + "FROM SYS.ALL_TABLES TAB\n"
                                               + "LEFT JOIN SYS.all_tables_dis_info dis ON dis.schema_name = TAB.OWNER AND TAB.TABLE_NAME = dis.table_name\n"
                                               + "LEFT JOIN SYS.ALL_TAB_COMMENTS COM ON TAB.OWNER = COM.OWNER AND TAB.TABLE_NAME = COM.TABLE_NAME AND COM.TABLE_TYPE = 'TABLE'\n";
    private static final String VIEW         = "SELECT TAB.OWNER,TAB.VIEW_NAME TABLE_NAME,'VIEW' TABLE_TYPE,COMMENTS,null TEMPORARY,null TABLESPACE_NAME,VIEWS.TEXT QUERY_SQL\n"//
                                               + "FROM SYS.ALL_VIEWS TAB\n"
                                               + "LEFT JOIN SYS.ALL_TAB_COMMENTS on TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.VIEW_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME AND SYS.ALL_TAB_COMMENTS.TABLE_TYPE = 'VIEW'\n"
                                               + "LEFT JOIN SYS.ALL_VIEWS VIEWS  on VIEWS.VIEW_NAME = TAB.VIEW_NAME";
    private static final String COLUMNS      = "SELECT COLS.OWNER, COLS.TABLE_NAME, COLS.COLUMN_NAME, DATA_TYPE, DATA_TYPE_OWNER, COLUMN_ID, DATA_LENGTH, CHAR_LENGTH, DATA_PRECISION, DATA_SCALE, NULLABLE, CHARACTER_SET_NAME, HIDDEN_COLUMN, VIRTUAL_COLUMN, COMM.COMMENTS, DATA_DEFAULT,\n"
                                               + "CASE WHEN IDENT_TAB.INFO2 = 1 THEN TRUE ELSE FALSE  END AS IS_IDENT,\n"//
                                               + "CASE WHEN IDENT_TAB.INFO2 = 1 THEN IDENT_TAB.INFO6 ELSE NULL END AS IDENT,\n"//
                                               + "CASE WHEN VIRT_TAB.COL_NAME IS NOT NULL THEN TRUE ELSE FALSE  END AS IS_VIRT \n"//
                                               + "FROM\n" //
                                               + " SYS.DBA_TAB_COLS COLS\n" //
                                               + " LEFT JOIN SYS.DBA_COL_COMMENTS COMM ON COLS.OWNER = COMM.OWNER AND COLS.TABLE_NAME = COMM.TABLE_NAME  AND COLS.COLUMN_NAME = COMM.COLUMN_NAME\n" //
                                               + " LEFT JOIN (" //
                                               + "         SELECT COL.*, OBJ.INFO6, OBJ.NAME AS TABLE_NAME \n" //
                                               + "          FROM\n" //
                                               + "            SYSCOLUMNS COL\n" //
                                               + "            LEFT JOIN SYSOBJECTS OBJ ON OBJ.ID = COL.ID \n" //
                                               + "           ) IDENT_TAB ON IDENT_TAB.NAME = COMM.COLUMN_NAME AND IDENT_TAB.TABLE_NAME = COMM.TABLE_NAME\n" //
                                               + " LEFT JOIN ( " //
                                               + "         SELECT C.NAME AS COL_NAME, B.NAME AS TAB_NAME \n" //
                                               + "           FROM\n" //
                                               + "             SYSCOLINFOS A,\n" //
                                               + "             SYSOBJECTS B,\n" //
                                               + "             SYSCOLUMNS C \n" //
                                               + "           WHERE\n" //
                                               + "              A.ID = B.ID  AND A.COLID = C.COLID  AND A.ID = C.ID \n" //
                                               + "           ) VIRT_TAB ON VIRT_TAB.COL_NAME = COMM.COLUMN_NAME AND VIRT_TAB.TAB_NAME = COLS.TABLE_NAME ";

    private static final String VIEW_COLUMNS = "SELECT COLS.OWNER, COLS.TABLE_NAME, COLS.COLUMN_NAME, DATA_TYPE, DATA_TYPE_OWNER, COLUMN_ID, DATA_LENGTH, CHAR_LENGTH, DATA_PRECISION, DATA_SCALE, NULLABLE, CHARACTER_SET_NAME, HIDDEN_COLUMN, VIRTUAL_COLUMN, COMM.COMMENTS, DATA_DEFAULT,\n"
                                               + "FALSE AS IS_IDENT,\n"//
                                               + "CASE WHEN IDENT_TAB.INFO2 = 1 THEN IDENT_TAB.INFO6 ELSE NULL END AS IDENT,\n"//
                                               + "CASE WHEN VIRT_TAB.COL_NAME IS NOT NULL THEN TRUE ELSE FALSE  END AS IS_VIRT \n"//
                                               + "FROM\n" //
                                               + " SYS.DBA_TAB_COLS COLS\n" //
                                               + " LEFT JOIN SYS.DBA_COL_COMMENTS COMM ON COLS.OWNER = COMM.OWNER AND COLS.TABLE_NAME = COMM.TABLE_NAME  AND COLS.COLUMN_NAME = COMM.COLUMN_NAME\n" //
                                               + " LEFT JOIN (" //
                                               + "         SELECT COL.*, OBJ.INFO6, OBJ.NAME AS TABLE_NAME \n" //
                                               + "          FROM\n" //
                                               + "            SYSCOLUMNS COL\n" //
                                               + "            LEFT JOIN SYSOBJECTS OBJ ON OBJ.ID = COL.ID \n" //
                                               + "           ) IDENT_TAB ON IDENT_TAB.NAME = COMM.COLUMN_NAME AND IDENT_TAB.TABLE_NAME = COMM.TABLE_NAME\n" //
                                               + " LEFT JOIN ( " //
                                               + "         SELECT C.NAME AS COL_NAME, B.NAME AS TAB_NAME \n" //
                                               + "           FROM\n" //
                                               + "             SYSCOLINFOS A,\n" //
                                               + "             SYSOBJECTS B,\n" //
                                               + "             SYSCOLUMNS C \n" //
                                               + "           WHERE\n" //
                                               + "              A.ID = B.ID  AND A.COLID = C.COLID  AND A.ID = C.ID \n" //
                                               + "           ) VIRT_TAB ON VIRT_TAB.COL_NAME = COMM.COLUMN_NAME AND VIRT_TAB.TAB_NAME = COLS.TABLE_NAME ";

    public DmMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            List<Map<String, String>> vars;
            try (PreparedStatement ps = conn.prepareStatement("select SVR_VERSION,DB_VERSION from V$INSTANCE")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    Map<String, Integer> extractColumn = extractColumn(resultSet.getMetaData());
                    StringMapRowMapper rowMapper = new StringMapRowMapper(extractColumn);
                    vars = new MultipleRowResultSetExtractor<>(rowMapper).extractData(resultSet);
                }
            }

            String version = null;
            for (Map<String, String> var : vars) {
                if (StringUtils.contains(var.get("SVR_VERSION"), "DM Database")) {
                    version = var.get("SVR_VERSION").trim();
                    break;
                }
            }

            if (StringUtils.isNotBlank(version)) {
                return version;
            } else {
                try (PreparedStatement ps = conn.prepareStatement("select * from v$version limit 1"); ResultSet resultSet = ps.executeQuery()) {
                    return ((ValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
                }
            }
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "select NAME from SYS.SYSOBJECTS where TYPE$ = 'SCH' order by NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return DmMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select NAME from SYS.SYSOBJECTS where TYPE$ = 'SCH' and NAME = ? limit 1";
            } else {
                return "select NAME from SYS.SYSOBJECTS where TYPE$ = 'SCH' and NAME = " + DmDialect.INSTANCE.fmtValue(schema) + " limit 1";
            }
        }, (rs -> {
            List<Value> valueList = DmMetaProviderUtils.convertSchema(rs);
            return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
        }), Collections.singletonList(schema), true, true);

    }

    public List<Value> selectTables(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select TAB.TABLE_NAME as TAB_NAME,'TABLE' as TAB_TYPE,COM.COMMENTS from SYS.ALL_TABLES TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.TABLE_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'TABLE' " //
                       + "where TAB.OWNER = ?  and TAB.TABLE_NAME not like '%$\\_%'  escape '\\' order by TAB.TABLE_NAME asc";
            } else {
                return "select TAB.TABLE_NAME as TAB_NAME,'TABLE' as TAB_TYPE,COM.COMMENTS from SYS.ALL_TABLES TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.TABLE_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'TABLE' " //
                       + "where TAB.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + "  and TAB.TABLE_NAME not like '%$\\_%'  escape '\\' order by TAB.TABLE_NAME asc";
            }
        }, (rs -> {
            List<Value> schemas = DmMetaProviderUtils.convertTableName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }), Collections.singletonList(schema), true, true);
    }

    public List<Value> selectViews(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select TAB.VIEW_NAME as TAB_NAME,'VIEW' as TAB_TYPE,COM.COMMENTS from SYS.ALL_VIEWS TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.VIEW_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'VIEW' "//
                       + "where TAB.OWNER = ? order by TAB.VIEW_NAME asc";
            } else {
                return "select TAB.VIEW_NAME as TAB_NAME,'VIEW' as TAB_TYPE,COM.COMMENTS from SYS.ALL_VIEWS TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.VIEW_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'VIEW' "//
                       + "where TAB.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " order by TAB.VIEW_NAME asc";
            }
        }, (rs -> {
            List<Value> schemas = DmMetaProviderUtils.convertTableName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }), Collections.singletonList(schema), true, true);
    }

    public List<Value> selectMaterializedView(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select TAB.TABLE_NAME as TAB_NAME,'TABLE' as TAB_TYPE,COM.COMMENTS from SYS.ALL_TABLES TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.TABLE_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'TABLE' " //
                       + "where TAB.OWNER = ?  and TAB.TABLE_NAME like '%$\\_%'  escape '\\' order by TAB.TABLE_NAME asc";
            } else {
                return "select TAB.TABLE_NAME as TAB_NAME,'TABLE' as TAB_TYPE,COM.COMMENTS from SYS.ALL_TABLES TAB "
                       + "left join SYS.ALL_TAB_COMMENTS COM on TAB.OWNER = COM.OWNER and TAB.TABLE_NAME = COM.TABLE_NAME and COM.TABLE_TYPE = 'TABLE' " //
                       + "where TAB.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + "  and TAB.TABLE_NAME like '%$\\_%'  escape '\\' order by TAB.TABLE_NAME asc";
            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertTableName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);
    }

    public List<Value> selectTrigger(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT TRIGGER_NAME as OBJECT_NAME FROM DBA_TRIGGERS where OWNER = ? order by OBJECT_NAME asc";
            } else {
                return "SELECT TRIGGER_NAME as OBJECT_NAME FROM DBA_TRIGGERS where OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " order by OBJECT_NAME asc";
            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertTriggerName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);
    }

    public List<Value> selectSequences(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select SEQUENCE_NAME as OBJECT_NAME from dba_sequences where SEQUENCE_OWNER = ? order by OBJECT_NAME asc ";
            } else {
                return "select SEQUENCE_NAME as OBJECT_NAME from dba_sequences where SEQUENCE_OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " order by OBJECT_NAME asc ";
            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertSequenceName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);
    }

    public List<Value> selectSynonym(String schema) throws SQLException {
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT SYNONYM_NAME as OBJECT_NAME FROM SYS.ALL_SYNONYMS t WHERE t.owner = ? order by OBJECT_NAME asc ";
            } else {
                return "SELECT SYNONYM_NAME as OBJECT_NAME FROM SYS.ALL_SYNONYMS t WHERE t.owner = " + DmDialect.INSTANCE.fmtValue(schema) + " order by OBJECT_NAME asc ";
            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertSynonymName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);

    }

    public List<Value> selectProcedures(String schema) throws SQLException {

        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH LENGTH "
                       + "FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' AND s2.NAME = ? and s1.INFO1 = 1 order by OBJECT_NAME asc";

            } else {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH LENGTH "
                       + "FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' AND s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + " and s1.INFO1 = 1 order by OBJECT_NAME asc";

            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertProcedureName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);

    }

    public List<Value> selectFunctions(String schema) throws SQLException {

        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH LENGTH "
                       + "FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' AND s2.NAME = ? and s1.INFO1 = 0 order by OBJECT_NAME asc";
            } else {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH LENGTH "
                       + "FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' AND s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + " and s1.INFO1 = 0 order by OBJECT_NAME asc";
            }
        }, (rs) -> {
            List<Value> schemas = DmMetaProviderUtils.convertFunctionName(rs);
            return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
        }, Collections.singletonList(schema), true, true);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchTableByPart(conn, schema, tabs, TABLE);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchViewByPart(conn, schema, tabs, VIEW);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    private List<RdbTable> fetchTableByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return conditions + " where TAB.OWNER = ? and TAB.TABLE_NAME in " + buildWhereIn(tabs);
            } else {
                return conditions + " where TAB.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and TAB.TABLE_NAME in " + buildWhereString(tabs);
            }
        }, DmMetaProviderUtils::convertTable, params, true, true);

    }

    private List<RdbTable> fetchViewByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return conditions + " where TAB.OWNER = ? and TAB.VIEW_NAME in " + buildWhereIn(tabs);
            } else {
                return conditions + " where TAB.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and TAB.VIEW_NAME in " + buildWhereString(tabs);
            }
        }, DmMetaProviderUtils::convertView, params, true, true);
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String tabHolders = buildWhereIn(tabs);
        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);

        return execute(needUserParams -> {
            if (needUserParams) {
                return VIEW_COLUMNS.replace("###TABLE_NAME###", tabHolders) + " where COLS.OWNER = ? and COLS.TABLE_NAME in " + buildWhereIn(tabs) + " order by COLUMN_ID asc";
            } else {
                return VIEW_COLUMNS.replace("###TABLE_NAME###", tabHolders) + " where COLS.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and COLS.TABLE_NAME in "
                       + buildWhereString(tabs) + " order by COLUMN_ID asc";
            }
        }, (rs) -> {
            List<RdbColumn> cols = DmMetaProviderUtils.convertColumn(rs);
            if (CollectionUtils.isEmpty(cols)) {
                return Collections.emptyMap();
            }

            Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
            for (RdbColumn column : cols) {
                result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
            }
            return result;
        }, params, true, true);
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String tabHolders = buildWhereIn(tabs);

        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return COLUMNS.replace("###TABLE_NAME###", tabHolders) + " where COLS.OWNER = ? and COLS.TABLE_NAME in " + tabHolders + " order by COLUMN_ID asc";
            } else {
                return COLUMNS.replace("###TABLE_NAME###", tabHolders) + " where COLS.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and COLS.TABLE_NAME in "
                       + buildWhereString(tabs) + " order by COLUMN_ID asc";
            }
        }, (rs) -> {
            List<RdbColumn> cols = DmMetaProviderUtils.convertColumn(rs);
            if (CollectionUtils.isEmpty(cols)) {
                return Collections.emptyMap();
            }

            Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
            for (RdbColumn column : cols) {
                result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
            }
            return result;
        }, params, true, true);
    }

    @Override
    protected Map<String, List<ConstraintObject>> fetchTableConstraints(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT  a.STATUS STATUS, a.CONSTRAINT_NAME CONSTRAINT_NAME,a.CONSTRAINT_TYPE CONSTRAINT_TYPE,a.TABLE_NAME TABLE_NAME,a.SEARCH_CONDITION SEARCH_CONDITION,a.R_OWNER R_OWNER,a.DELETE_RULE DELETE_RULE,b.COLUMN_NAME COLUMN_NAME"
                       + " FROM    DBA_CONSTRAINTS a left JOIN     ALL_CONS_COLUMNS b  ON    a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  " + " where a.OWNER = ? and a.TABLE_NAME in"
                       + buildWhereIn(tabs) + " and CONSTRAINT_TYPE in ('C','U')";
            } else {
                return "SELECT a.STATUS STATUS, a.CONSTRAINT_NAME CONSTRAINT_NAME,a.CONSTRAINT_TYPE CONSTRAINT_TYPE,a.TABLE_NAME TABLE_NAME,a.SEARCH_CONDITION SEARCH_CONDITION,a.R_OWNER R_OWNER,a.DELETE_RULE DELETE_RULE,b.COLUMN_NAME COLUMN_NAME FROM    DBA_CONSTRAINTS a left JOIN     ALL_CONS_COLUMNS b  ON    a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  "
                       + " where a.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and a.TABLE_NAME in" + buildWhereString(tabs) + " and CONSTRAINT_TYPE in ('C','U')";
            }
        }, (rs) -> {
            // sometime prepare handle sql execute will fail

            List<ConstraintObject> list = DmMetaProviderUtils.convertConstraints(rs);
            Map<String, Map<String, ConstraintObject>> idxMap = new LinkedHashMap<>();

            for (ConstraintObject key : list) {
                String cacheKey = key.getTable() + key.getName();
                Map<String, ConstraintObject> indexMap = idxMap.computeIfAbsent(key.getTable(), s -> new LinkedHashMap<>());
                if (indexMap.containsKey(cacheKey)) {
                    ConstraintObject constraintObject = indexMap.get(cacheKey);
                    RdbUniqueKey uniqueKey = (RdbUniqueKey) constraintObject;
                    uniqueKey.getColumnList().addAll(((RdbUniqueKey) key).getColumnList());
                } else {
                    indexMap.put(cacheKey, key);
                }
            }
            return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
        }, params, true, true);
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {

        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
        execute(needUserParams -> {
            if (needUserParams) {
                return "select CON.OWNER,CC.TABLE_NAME,CON.CONSTRAINT_NAME,CON.CONSTRAINT_TYPE,STATUS,VALIDATED,GENERATED,COLUMN_NAME\n"//
                       + "from SYS.ALL_CONS_COLUMNS CC\n"//
                       + "left join SYS.ALL_CONSTRAINTS CON on CC.CONSTRAINT_NAME = CON.CONSTRAINT_NAME and CC.OWNER = CON.OWNER\n" //
                       + "where CON.CONSTRAINT_TYPE in ('P') and CC.OWNER = ? and CC.TABLE_NAME in " + buildWhereIn(tabs) + " order by POSITION asc";
            } else {
                return "select CON.OWNER,CC.TABLE_NAME,CON.CONSTRAINT_NAME,CON.CONSTRAINT_TYPE,STATUS,VALIDATED,GENERATED,COLUMN_NAME\n"//
                       + "from SYS.ALL_CONS_COLUMNS CC\n"//
                       + "left join SYS.ALL_CONSTRAINTS CON on CC.CONSTRAINT_NAME = CON.CONSTRAINT_NAME and CC.OWNER = CON.OWNER\n" //
                       + "where CON.CONSTRAINT_TYPE in ('P') and CC.OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and CC.TABLE_NAME in " + buildWhereString(tabs)
                       + " order by POSITION asc";
            }
        }, (rs) -> {

            while (rs.next()) {
                String type = rs.getString("CONSTRAINT_TYPE");
                String table = rs.getString("TABLE_NAME");
                Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                if (type.equals("P")) {
                    String consName = rs.getString("CONSTRAINT_NAME");
                    RdbPrimaryKey pk = (RdbPrimaryKey) (constraints.computeIfAbsent(consName, k -> new RdbPrimaryKey()));
                    pk.setName(consName);
                    pk.addColumn(rs.getString("COLUMN_NAME"));
                } else if (type.equals("U")) {
                    String consName = rs.getString("CONSTRAINT_NAME");
                    RdbUniqueKey uk = (RdbUniqueKey) (constraints.computeIfAbsent(consName, k -> new RdbUniqueKey()));
                    uk.setName(rs.getString("CONSTRAINT_NAME"));
                    uk.addColumn(rs.getString("COLUMN_NAME"));
                    uk.setAttribute(DmAttributeNames.INDEX_WAY.getCodeKey(), "UNIQUE");
                } else {
                    throw new IllegalArgumentException("unsupported type constraint type:" + type);
                }
            }
            return pkUkMap;
        }, params, true, true);

        execute(needUserParams -> {
            if (needUserParams) {
                return "select IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,IDX.INDEX_TYPE,IDX.UNIQUENESS,IDX.GENERATED,DESCEND,PARTITIONED,TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " //
                       + "from SYS.ALL_INDEXES IDX " //
                       + "left join SYS.ALL_IND_COLUMNS COL on IDX.OWNER = COL.INDEX_OWNER and IDX.INDEX_NAME = COL.INDEX_NAME " //
                       + "where IDX.TABLE_OWNER = ? and IDX.TABLE_NAME in " + buildWhereIn(tabs) + " and COLUMN_NAME is not null "//
                       + "and UNIQUENESS='UNIQUE' and GENERATED != 'Y' order by COL.COLUMN_POSITION asc";
            } else {
                return "select IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,IDX.INDEX_TYPE,IDX.UNIQUENESS,IDX.GENERATED,DESCEND,PARTITIONED,TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " //
                       + "from SYS.ALL_INDEXES IDX " //
                       + "left join SYS.ALL_IND_COLUMNS COL on IDX.OWNER = COL.INDEX_OWNER and IDX.INDEX_NAME = COL.INDEX_NAME " //
                       + "where IDX.TABLE_OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and IDX.TABLE_NAME in " + buildWhereIn(tabs) + " and COLUMN_NAME is not null "//
                       + "and UNIQUENESS='UNIQUE' and GENERATED != 'Y' order by COL.COLUMN_POSITION asc";
            }
        }, (rs) -> {
            while (rs.next()) {
                String table = rs.getString("TABLE_NAME");
                Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                String consName = rs.getString("INDEX_NAME");
                RdbUniqueKey uk = (RdbUniqueKey) (constraints.computeIfAbsent(consName, k -> new RdbUniqueKey()));
                uk.setName(rs.getString("INDEX_NAME"));
                uk.addColumn(rs.getString("COLUMN_NAME"));
                uk.setAttribute(DmAttributeNames.INDEX_WAY.getCodeKey(), "UNIQUE");
            }
            return pkUkMap;
        }, params, true, true);
        return pkUkMap;
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT /*+ MAX_OPT_N_TABLES(5) */ NULL AS PKTABLE_CAT,T_REFED.SCHNAME AS PKTABLE_SCHEM, T_REFED.NAME AS PKTABLE_NAME, T_REFED.REFED_COL_NAME AS PKCOLUMN_NAME, NULL AS FKTABLE_CAT,T_REF.SCHNAME AS FKTABLE_SCHEM,T_REF.NAME AS FKTABLE_NAME, T_REF.REF_COL_NAME AS FKCOLUMN_NAME, T_REF.REF_KEYNO AS KEY_SEQ, SF_GET_UPD_RULE(T_REF.FACTION) AS UPDATE_RULE, SF_GET_DEL_RULE(T_REF.FACTION) AS DELETE_RULE, T_REF.REF_CONS_NAME AS FK_NAME, T_REFED.REFED_CONS_NAME AS PK_NAME, 0 AS DEFERRABILITY "
                       + "FROM (SELECT T_REF_TAB.NAME AS NAME, T_REF_TAB.SCHNAME AS SCHNAME, T_REF_CONS.FINDEXID AS REFED_ID, T_REF_CONS.NAME AS REF_CONS_NAME, SF_GET_INDEX_KEY_SEQ(T_REF_IND.KEYNUM, T_REF_IND.KEYINFO, T_REF_COL.COLID) AS REF_KEYNO, T_REF_COL.NAME AS REF_COL_NAME, T_REF_CONS.FACTION AS FACTION "
                       + "FROM (SELECT NAME, INDEXID, FINDEXID, TABLEID, FACTION, CONS.TYPE$ as TYPE FROM SYS.SYSCONS CONS, SYS.SYSOBJECTS OBJECTS WHERE CONS.ID = OBJECTS.ID) AS T_REF_CONS, (SELECT TABS.NAME AS NAME, TABS.ID, SCHEMAS.NAME AS SCHNAME "
                       + "FROM(SELECT ID, PID, NAME FROM SYS.SYSOBJECTS WHERE TYPE$ = 'SCH' AND NAME = ?) SCHEMAS,(SELECT ID, SCHID, NAME FROM SYS.SYSOBJECTS WHERE TYPE$ = 'SCHOBJ' AND SUBTYPE$ = 'UTAB' AND NAME in "
                       + buildWhereIn(tabs)
                       + " ) TABS WHERE SCHEMAS.ID == TABS.SCHID)T_REF_TAB,SYS.SYSINDEXES AS T_REF_IND, (SELECT ID, PID FROM SYS.SYSOBJECTS WHERE SUBTYPE$='INDEX') AS T_REF_INDS_OBJ, SYS.SYSCOLUMNS AS T_REF_COL WHERE T_REF_TAB.ID = T_REF_CONS.TABLEID AND T_REF_CONS.TYPE='F' AND T_REF_TAB.ID = T_REF_INDS_OBJ.PID AND T_REF_TAB.ID = T_REF_COL.ID AND T_REF_CONS.INDEXID = T_REF_INDS_OBJ.ID AND T_REF_IND.ID = T_REF_INDS_OBJ.ID AND SF_COL_IS_IDX_KEY(T_REF_IND.KEYNUM, T_REF_IND.KEYINFO, T_REF_COL.COLID)=1) AS T_REF, (SELECT T_REFED_CONS.INDEXID AS REFED_ID, T_REFED_TAB.SCH_NAME AS SCHNAME, T_REFED_TAB.TAB_NAME AS NAME, T_REFED_IND.ID AS REFED_IND_ID, T_REFED_CONS.NAME AS REFED_CONS_NAME, SF_GET_INDEX_KEY_SEQ(T_REFED_IND.KEYNUM, T_REFED_IND.KEYINFO, T_REFED_COL.COLID) AS REFED_KEYNO, T_REFED_COL.NAME AS REFED_COL_NAME FROM "
                       + "(SELECT NAME, INDEXID, FINDEXID, TABLEID, FACTION, CONS.TYPE$ as TYPE "
                       + "FROM SYS.SYSCONS CONS, SYS.SYSOBJECTS OBJECTS WHERE CONS.ID = OBJECTS.ID) AS T_REFED_CONS, (SELECT TAB.ID AS ID, TAB.NAME AS TAB_NAME, SCH.NAME AS SCH_NAME "
                       + "FROM SYS.SYSOBJECTS TAB, SYS.SYSOBJECTS SCH WHERE TAB.SUBTYPE$='UTAB' AND SCH.TYPE$='SCH' AND TAB.SCHID=SCH.ID) AS T_REFED_TAB, SYS.SYSINDEXES AS T_REFED_IND, (SELECT ID, PID, NAME FROM SYS.SYSOBJECTS WHERE SUBTYPE$='INDEX') AS T_REFED_INDS_OBJ, SYS.SYSCOLUMNS AS T_REFED_COL WHERE T_REFED_TAB.ID = T_REFED_CONS.TABLEID AND T_REFED_CONS.TYPE='P' AND T_REFED_TAB.ID = T_REFED_INDS_OBJ.PID AND T_REFED_TAB.ID = T_REFED_COL.ID AND T_REFED_CONS.INDEXID = T_REFED_INDS_OBJ.ID AND T_REFED_IND.ID = T_REFED_INDS_OBJ.ID AND SF_COL_IS_IDX_KEY(T_REFED_IND.KEYNUM, T_REFED_IND.KEYINFO, T_REFED_COL.COLID)=1) AS T_REFED WHERE T_REF.REFED_ID = T_REFED.REFED_ID AND T_REF.REF_KEYNO = T_REFED.REFED_KEYNO ORDER BY FKTABLE_CAT ASC, FKTABLE_SCHEM ASC, FKTABLE_NAME ASC, KEY_SEQ ASC";
            } else {
                return "SELECT /*+ MAX_OPT_N_TABLES(5) */ NULL AS PKTABLE_CAT,T_REFED.SCHNAME AS PKTABLE_SCHEM, T_REFED.NAME AS PKTABLE_NAME, T_REFED.REFED_COL_NAME AS PKCOLUMN_NAME, NULL AS FKTABLE_CAT,T_REF.SCHNAME AS FKTABLE_SCHEM,T_REF.NAME AS FKTABLE_NAME, T_REF.REF_COL_NAME AS FKCOLUMN_NAME, T_REF.REF_KEYNO AS KEY_SEQ, SF_GET_UPD_RULE(T_REF.FACTION) AS UPDATE_RULE, SF_GET_DEL_RULE(T_REF.FACTION) AS DELETE_RULE, T_REF.REF_CONS_NAME AS FK_NAME, T_REFED.REFED_CONS_NAME AS PK_NAME, 0 AS DEFERRABILITY "
                       + "FROM (SELECT T_REF_TAB.NAME AS NAME, T_REF_TAB.SCHNAME AS SCHNAME, T_REF_CONS.FINDEXID AS REFED_ID, T_REF_CONS.NAME AS REF_CONS_NAME, SF_GET_INDEX_KEY_SEQ(T_REF_IND.KEYNUM, T_REF_IND.KEYINFO, T_REF_COL.COLID) AS REF_KEYNO, T_REF_COL.NAME AS REF_COL_NAME, T_REF_CONS.FACTION AS FACTION "
                       + "FROM (SELECT NAME, INDEXID, FINDEXID, TABLEID, FACTION, CONS.TYPE$ as TYPE FROM SYS.SYSCONS CONS, SYS.SYSOBJECTS OBJECTS WHERE CONS.ID = OBJECTS.ID) AS T_REF_CONS, (SELECT TABS.NAME AS NAME, TABS.ID, SCHEMAS.NAME AS SCHNAME "
                       + "FROM(SELECT ID, PID, NAME FROM SYS.SYSOBJECTS WHERE TYPE$ = 'SCH' AND NAME = " + DmDialect.INSTANCE.fmtValue(schema)
                       + ") SCHEMAS,(SELECT ID, SCHID, NAME FROM SYS.SYSOBJECTS WHERE TYPE$ = 'SCHOBJ' AND SUBTYPE$ = 'UTAB' AND NAME in " + buildWhereString(tabs)
                       + " ) TABS WHERE SCHEMAS.ID == TABS.SCHID)T_REF_TAB,SYS.SYSINDEXES AS T_REF_IND, (SELECT ID, PID FROM SYS.SYSOBJECTS WHERE SUBTYPE$='INDEX') AS T_REF_INDS_OBJ, SYS.SYSCOLUMNS AS T_REF_COL WHERE T_REF_TAB.ID = T_REF_CONS.TABLEID AND T_REF_CONS.TYPE='F' AND T_REF_TAB.ID = T_REF_INDS_OBJ.PID AND T_REF_TAB.ID = T_REF_COL.ID AND T_REF_CONS.INDEXID = T_REF_INDS_OBJ.ID AND T_REF_IND.ID = T_REF_INDS_OBJ.ID AND SF_COL_IS_IDX_KEY(T_REF_IND.KEYNUM, T_REF_IND.KEYINFO, T_REF_COL.COLID)=1) AS T_REF, (SELECT T_REFED_CONS.INDEXID AS REFED_ID, T_REFED_TAB.SCH_NAME AS SCHNAME, T_REFED_TAB.TAB_NAME AS NAME, T_REFED_IND.ID AS REFED_IND_ID, T_REFED_CONS.NAME AS REFED_CONS_NAME, SF_GET_INDEX_KEY_SEQ(T_REFED_IND.KEYNUM, T_REFED_IND.KEYINFO, T_REFED_COL.COLID) AS REFED_KEYNO, T_REFED_COL.NAME AS REFED_COL_NAME FROM "
                       + "(SELECT NAME, INDEXID, FINDEXID, TABLEID, FACTION, CONS.TYPE$ as TYPE "
                       + "FROM SYS.SYSCONS CONS, SYS.SYSOBJECTS OBJECTS WHERE CONS.ID = OBJECTS.ID) AS T_REFED_CONS, (SELECT TAB.ID AS ID, TAB.NAME AS TAB_NAME, SCH.NAME AS SCH_NAME "
                       + "FROM SYS.SYSOBJECTS TAB, SYS.SYSOBJECTS SCH WHERE TAB.SUBTYPE$='UTAB' AND SCH.TYPE$='SCH' AND TAB.SCHID=SCH.ID) AS T_REFED_TAB, SYS.SYSINDEXES AS T_REFED_IND, (SELECT ID, PID, NAME FROM SYS.SYSOBJECTS WHERE SUBTYPE$='INDEX') AS T_REFED_INDS_OBJ, SYS.SYSCOLUMNS AS T_REFED_COL WHERE T_REFED_TAB.ID = T_REFED_CONS.TABLEID AND T_REFED_CONS.TYPE='P' AND T_REFED_TAB.ID = T_REFED_INDS_OBJ.PID AND T_REFED_TAB.ID = T_REFED_COL.ID AND T_REFED_CONS.INDEXID = T_REFED_INDS_OBJ.ID AND T_REFED_IND.ID = T_REFED_INDS_OBJ.ID AND SF_COL_IS_IDX_KEY(T_REFED_IND.KEYNUM, T_REFED_IND.KEYINFO, T_REFED_COL.COLID)=1) AS T_REFED WHERE T_REF.REFED_ID = T_REFED.REFED_ID AND T_REF.REF_KEYNO = T_REFED.REFED_KEYNO ORDER BY FKTABLE_CAT ASC, FKTABLE_SCHEM ASC, FKTABLE_NAME ASC, KEY_SEQ ASC";
            }
        }, (rs) -> {
            // sometime prepare handle sql execute will fail

            List<RdbForeignKey> keyList = DmMetaProviderUtils.convertForeignKeys(rs);
            Map<String, Map<String, RdbForeignKey>> idxMap = new LinkedHashMap<>();

            for (RdbForeignKey key : keyList) {
                Map<String, RdbForeignKey> indexMap = idxMap.computeIfAbsent(key.getTable(), s -> new LinkedHashMap<>());
                if (indexMap.containsKey(key.getTable())) {
                    indexMap.get(key.getTable()).getReferenceMapping().putAll(key.getReferenceMapping());
                    indexMap.get(key.getTable()).getColumnList().addAll(key.getColumnList());
                } else {
                    indexMap.put(key.getTable(), key);
                }
            }
            return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
        }, params, true, true);
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(tabs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "select IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,IDX.INDEX_TYPE,IDX.UNIQUENESS,IDX.GENERATED,DESCEND,PARTITIONED,TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " //
                       + "from SYS.ALL_INDEXES IDX " //
                       + "left join SYS.ALL_IND_COLUMNS COL on IDX.OWNER = COL.INDEX_OWNER and IDX.INDEX_NAME = COL.INDEX_NAME " //
                       + "where IDX.TABLE_OWNER = ? and IDX.TABLE_NAME in " + buildWhereIn(tabs) + " and COLUMN_NAME is not null "//
                       + "and UNIQUENESS='NONUNIQUE'  and GENERATED != 'Y' order by COL.COLUMN_POSITION asc";
            } else {
                return "select IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,IDX.INDEX_TYPE,IDX.UNIQUENESS,IDX.GENERATED,DESCEND,PARTITIONED,TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " //
                       + "from SYS.ALL_INDEXES IDX " //
                       + "left join SYS.ALL_IND_COLUMNS COL on IDX.OWNER = COL.INDEX_OWNER and IDX.INDEX_NAME = COL.INDEX_NAME " //
                       + "where IDX.TABLE_OWNER = " + DmDialect.INSTANCE.fmtValue(schema) + " and IDX.TABLE_NAME in " + buildWhereIn(tabs) + " and COLUMN_NAME is not null "//
                       + "and UNIQUENESS='NONUNIQUE'  and GENERATED != 'Y' order by COL.COLUMN_POSITION asc";
            }
        }, (rs) -> {
            // sometime prepare handle sql execute will fail

            List<RdbIndex> idxList = DmMetaProviderUtils.convertIndex(rs);
            Map<String, Map<String, RdbIndex>> idxMap = new LinkedHashMap<>();

            for (RdbIndex index : idxList) {
                String cacheKey = "\"" + index.getSchema() + "\".\"" + index.getName() + "\"";
                Map<String, RdbIndex> indexMap = idxMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
                if (indexMap.containsKey(cacheKey)) {
                    indexMap.get(cacheKey).getColumnList().addAll(index.getColumnList());
                } else {
                    indexMap.put(cacheKey, index);
                }
            }
            return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
        }, params, true, true);
    }

    public List<RdbFunction> loadFunctions(String catalog, String schema, List<String> functionNames) throws SQLException {
        functionNames = stringArray2List(functionNames);
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funs : CollectionUtils.splitList(functionNames, defaultGroupSize())) {
                List<RdbFunction> rdbFunctions = this.fetchFunctionByPart(conn, catalog, schema, funs);
                List<RdbParam> rdbParams = this.fetchFunctionParams(conn, catalog, schema, funs, UmiTypes.Function);
                if (rdbParams != null && !rdbParams.isEmpty()) {
                    result.addAll(rdbFunctions.stream().peek(rdbFunction -> {
                        List<RdbParam> rdbParamList = rdbParams.stream()
                            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                            .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbFunction.getName()))
                            .collect(Collectors.toList());
                        if (!rdbParamList.isEmpty()) {
                            rdbFunction.setReturns(rdbParamList.remove(0));
                        }
                        rdbFunction.setRdbParams(rdbParamList);

                    }).collect(Collectors.toList()));
                }

            }
        }
        return result;
    }

    private List<RdbParam> fetchFunctionParams(Connection conn, String catalog, String schema, List<String> procs, UmiTypes umiTypes) throws SQLException {
        String sql = "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH DATA_LENGTH,s2.NAME OWNER FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                     + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s2.NAME = ?  and s1.INFO1 = 0 AND s1.NAME in " + buildWhereIn(procs) + " order by POSITION asc";
        //        String sql = "select OWNER,POSITION,OBJECT_NAME,ARGUMENT_NAME,DATA_TYPE,DATA_LENGTH  from ALL_ARGUMENTS where OWNER = ?  and OBJECT_NAME in " +
        //                + " order by POSITION";

        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(procs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH DATA_LENGTH,s2.NAME OWNER FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s2.NAME = ?  and s1.INFO1 = 0 AND s1.NAME in " + buildWhereIn(procs) + " order by POSITION asc";
            } else {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH DATA_LENGTH,s2.NAME OWNER FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + "  and s1.INFO1 = 0 AND s1.NAME in "
                       + buildWhereString(procs) + " order by POSITION asc";
            }
        }, this::convertParams, params, true, true);
    }

    private List<RdbParam> fetchProcedureParams(Connection conn, String catalog, String schema, List<String> procs, UmiTypes umiTypes) throws SQLException {

        List<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(procs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH DATA_LENGTH,s2.NAME OWNER FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s2.NAME = ?  and s1.INFO1 = 1 AND s1.NAME in " + buildWhereIn(procs) + " order by POSITION asc";
            } else {
                return "SELECT s1.NAME OBJECT_NAME,aa.POSITION POSITION,aa.DATA_TYPE DATA_TYPE,aa.ARGUMENT_NAME ARGUMENT_NAME,aa.DATA_LENGTH DATA_LENGTH,s2.NAME OWNER FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID left join ALL_ARGUMENTS aa on aa.OBJECT_ID = s1.ID\n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + "  and s1.INFO1 = 1 AND s1.NAME in "
                       + buildWhereString(procs) + " order by POSITION asc";
            }
        }, this::convertParams, params, true, true);
    }

    private List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        return DmMetaProviderUtils.convertParams(rs);
    }

    private List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funcs) throws SQLException {
        String sql = "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                     + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 0 and s2.NAME = ? AND s1.NAME in " + buildWhereIn(funcs);

        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(funcs);
        return execute(needUserParams -> {
            if (needUserParams) {
                return "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 0 and s2.NAME = ? AND s1.NAME in " + buildWhereIn(funcs);

            } else {
                return "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 0 and s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + " AND s1.NAME in "
                       + buildWhereString(funcs);
            }
        }, this::convertFunctions, params, true, true);
    }

    private List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        return DmMetaProviderUtils.convertFunctions(rs);
    }

    public List<RdbProcedure> loadProcedures(String catalog, String schema, List<String> procedureNames) throws SQLException {
        procedureNames = stringArray2List(procedureNames);
        if (procedureNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbProcedure> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> procs : CollectionUtils.splitList(procedureNames, defaultGroupSize())) {
                List<RdbProcedure> rdbProcedures = this.fetchProcedureByPart(conn, catalog, schema, procs);
                List<RdbParam> rdbParams = this.fetchProcedureParams(conn, catalog, schema, procs, UmiTypes.Procedure);
                result.addAll(rdbProcedures.stream()
                    .peek(rdbProcedure -> rdbProcedure.setRdbParams(rdbParams.stream()
                        .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                        .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbProcedure.getName()))
                        .collect(Collectors.toList())))
                    .collect(Collectors.toList()));
            }
        }
        return result;
    }

    private List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) throws SQLException {
        String sql = "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                     + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 1 and s2.NAME = ? AND s1.NAME in " + buildWhereIn(procs);
        ArrayList<String> params = new ArrayList<>();
        params.add(schema);
        params.addAll(procs);
        return execute(needUseParams -> {
            if (needUseParams) {
                return "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 1 and s2.NAME = ? AND s1.NAME in " + buildWhereIn(procs);
            } else {
                return "SELECT s1.NAME SPECIFIC_NAME,S2.NAME SCHEMA_NAME FROM SYSOBJECTS s1 left join SYSOBJECTS s2 on s1.SCHID = s2.ID \n"
                       + " WHERE  s1.TYPE$ = 'SCHOBJ' and s1.SUBTYPE$ = 'PROC' and s1.INFO1 = 1 and s2.NAME = " + DmDialect.INSTANCE.fmtValue(schema) + " AND s1.NAME in "
                       + buildWhereString(procs);
            }
        }, DmMetaProviderUtils::convertProcedures, params, true, true);
    }

    public List<Value> selectRoles(String schema) throws SQLException {
        String sql = "select b.name NAME from sys.sysobjects a, sys.sysobjects b where a.ID=b.SCHID and b.SUBTYPE$='ROLE' and b.name not like 'DB__%'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return DmMetaProviderUtils.convertRoles(rs);
            }
        }
    }

    public List<Value> selectUsers(String schema) throws SQLException {
        String sql = "select USERNAME from all_users";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return DmMetaProviderUtils.convertUsers(rs);
            }
        }
    }

    public List<Value> selectTablespacees(String schema) throws SQLException {
        String sql = "select NAME from v$tablespace";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return DmMetaProviderUtils.convertTablespaces(rs);
            }
        }
    }

    private interface SQLHandler {

        String getSql(boolean needUseParams);
    }

    private interface ResultHandler<T> {

        T handlerResult(ResultSet rs) throws SQLException;
    }

    // Dameng driver preprocessing error fallback
    private <T> T execute(final SQLHandler sqlHandler, final ResultHandler<T> handler, List<String> params, boolean needUseParams, boolean needRetry) throws SQLException {
        String sql = sqlHandler.getSql(needUseParams);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (needUseParams) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setString(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                return handler.handlerResult(rs);
            }
        } catch (Exception e) {
            if (needRetry) {
                return execute(sqlHandler, handler, params, false, false);
            } else {
                throw e;
            }
        }
    }

    public String buildWhereString(List<String> params) {
        StringBuilder whereIn = new StringBuilder();
        whereIn.append("(");
        for (String param : params) {
            whereIn.append(DmDialect.INSTANCE.fmtValue(param));
            whereIn.append(",");
        }
        whereIn.deleteCharAt(whereIn.length() - 1);
        whereIn.append(")");
        return whereIn.toString();
    }

    private List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        return DmMetaProviderUtils.convertProcedures(rs);
    }
}
