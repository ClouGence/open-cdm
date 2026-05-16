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
package com.clougence.clouddm.ds.oracle.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;
import com.clougence.utils.jdbc.mapper.StringMapRowMapper;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Oracle 元信息获取，参考资料：
 * <li>https://docs.oracle.com/en/database/oracle/oracle-database/21/drdag/all_synonyms-drda-gateway.html#GUID-E814A6AC-5E00-4DB6-8170-DC147F7879F8</li>
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class OraMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    private static final String TABLE           = "select TAB.OWNER,TAB.TABLE_NAME,TABLESPACE_NAME,TAB.TABLE_TYPE,TAB.LOG_TABLE,TAB.LOG_ROWIDS,TAB.LOG_PK,TAB.LOG_SEQ,COMMENTS,TAB.TEMPORARY,TAB.IOT_TYPE ,\n"
                                                  + "   TAB.STATUS as VALID_FLAG, TAB.CLUSTER_NAME, TAB.PCT_FREE, TAB.PCT_USED, TAB.INI_TRANS,TAB.MAX_TRANS,  TAB.INITIAL_EXTENT, TAB.NEXT_EXTENT, TAB.MIN_EXTENTS, TAB.MAX_EXTENTS,\n"
                                                  + " PARTITIONED,  to_char(CREATED, 'yyyy-MM-dd HH24:MI:SS') as CREATE_TIME, to_char(LAST_DDL_TIME, 'yyyy-MM-dd HH24:MI:SS') as LAST_DDL_TIME from (\n"                                                                                                                                     //
                                                  + "  select OWNER,TABLE_NAME,TABLESPACE_NAME,'TABLE' TABLE_TYPE,LOG.LOG_TABLE,LOG.ROWIDS LOG_ROWIDS,LOG.PRIMARY_KEY LOG_PK,LOG.SEQUENCE LOG_SEQ,TEMPORARY,IOT_TYPE, STATUS,\n"
                                                  + "   CLUSTER_NAME,  PCT_FREE, PCT_USED, INI_TRANS,MAX_TRANS, INITIAL_EXTENT,NEXT_EXTENT, MIN_EXTENTS,MAX_EXTENTS,PARTITIONED from SYS.ALL_TABLES\n"                                                                                     //
                                                  + "  left join SYS.ALL_MVIEW_LOGS LOG on LOG_OWNER = OWNER and MASTER = TABLE_NAME\n"                                                                                                                                                                                         //
                                                  + ") TAB\n"                                                                                                                                                                                                                                                                   //
                                                  + "left join SYS.ALL_TAB_COMMENTS on TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME and TAB.TABLE_TYPE = SYS.ALL_TAB_COMMENTS.TABLE_TYPE"
                                                  + "  left join ALL_OBJECTS on TAB.OWNER = ALL_OBJECTS.OWNER and TAB.TABLE_NAME = ALL_OBJECTS.OBJECT_NAME and TAB.TABLE_TYPE = ALL_OBJECTS.OBJECT_TYPE";

    private static final String VIEW            = "select TAB.OWNER,TAB.TABLE_NAME,TABLESPACE_NAME,TAB.TABLE_TYPE,TAB.LOG_TABLE,TAB.LOG_ROWIDS,TAB.LOG_PK,TAB.LOG_SEQ,COMMENTS,TAB.TEMPORARY,TAB.SQL,\n"
                                                  + "  TAB.TEXT_LENGTH, TAB.SUPERVIEW_NAME, TAB.EDITIONING_VIEW, TAB.READ_ONLY,\n"
                                                  + "  to_char(CREATED, 'yyyy-MM-dd HH24:MI:SS') as CREATE_TIME,\n"
                                                  + "  to_char(LAST_DDL_TIME, 'yyyy-MM-dd HH24:MI:SS') as LAST_DDL_TIME,  status as VALID_FLAG from (\n"                                                                                                                                              //
                                                  + "  select OWNER,VIEW_NAME TABLE_NAME, TEXT SQL ,null TABLESPACE_NAME,'VIEW' TABLE_TYPE,null LOG_TABLE,null LOG_ROWIDS,null LOG_PK,null LOG_SEQ, null TEMPORARY,\n"
                                                  + "  TEXT_LENGTH as TEXT_LENGTH, VIEW_TYPE as VIEW_TYPE, SUPERVIEW_NAME as SUPERVIEW_NAME, EDITIONING_VIEW as EDITIONING_VIEW, READ_ONLY as READ_ONLY\n"
                                                  + "  from SYS.ALL_VIEWS) TAB\n"                                                                                                                                                                                                                                                                   //
                                                  + "  left join SYS.ALL_TAB_COMMENTS on TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME and TAB.TABLE_TYPE = SYS.ALL_TAB_COMMENTS.TABLE_TYPE \n"
                                                  + "  left join ALL_OBJECTS on TAB.OWNER = ALL_OBJECTS.OWNER and TAB.TABLE_NAME = ALL_OBJECTS.OBJECT_NAME and TAB.TABLE_TYPE = ALL_OBJECTS.OBJECT_TYPE";

    private static final String COLUMNS_LE_11G  = "select COLS.OWNER,COLS.TABLE_NAME,COLS.COLUMN_NAME,DATA_TYPE,DATA_TYPE_OWNER,COLUMN_ID,DATA_LENGTH,CHAR_LENGTH,DATA_PRECISION,DATA_SCALE,NULLABLE,CHARACTER_SET_NAME,HIDDEN_COLUMN,VIRTUAL_COLUMN,COMM.COMMENTS,DATA_DEFAULT from SYS.DBA_TAB_COLS COLS\n"                   //
                                                  + "left join SYS.DBA_COL_COMMENTS COMM on COLS.OWNER = COMM.OWNER and COLS.TABLE_NAME = COMM.TABLE_NAME and COLS.COLUMN_NAME = COMM.COLUMN_NAME\n";
    private static final String COLUMNS_DEFAULT = "select COLS.OWNER,COLS.TABLE_NAME,COLS.COLUMN_NAME,DATA_TYPE,DATA_TYPE_OWNER,COLUMN_ID,DATA_LENGTH,CHAR_LENGTH,DATA_PRECISION,DATA_SCALE,NULLABLE,CHARACTER_SET_NAME,HIDDEN_COLUMN,VIRTUAL_COLUMN,IDENTITY_COLUMN,COMM.COMMENTS,DATA_DEFAULT from SYS.DBA_TAB_COLS COLS\n"   //
                                                  + "left join SYS.DBA_COL_COMMENTS COMM on COLS.OWNER = COMM.OWNER and COLS.TABLE_NAME = COMM.TABLE_NAME and COLS.COLUMN_NAME = COMM.COLUMN_NAME\n";

    private static final String SCHEMAS         = "select USERNAME from SYS.ALL_USERS";
    private static final String SCHEMAS_ALL     = SCHEMAS + " order by USERNAME asc";
    private static final String SCHEMAS_LE_11G  = SCHEMAS
                                                  + " where USERNAME not in ('ANONYMOUS','APPQOSSYS','CTXSYS','DBSNMP','DIP','EXFSYS','FLOWS_FILES','MDDATA','MDSYS','MGMT_VIEW','OLAPSYS','ORDDATA','ORDPLUGINS','ORDSYS','OUTLN','OWBSYS','SI_INFORMTN_SCHEMA','SYS','SYSMAN','SYSTEM','TSMSYS','WMSYS','XDB','XS$NULL')"
                                                  + " order by USERNAME asc";
    private static final String SCHEMAS_DEFAULT = SCHEMAS + " where ORACLE_MAINTAINED = 'N' order by USERNAME asc";

    private final boolean       excludeOraMaintainedSchemas;

    public OraMetaProviderDm(Connection connection){
        this(connection, true);
    }

    public OraMetaProviderDm(Connection connection, boolean excludeOraMaintainedSchemas){
        super(connection);
        this.excludeOraMaintainedSchemas = excludeOraMaintainedSchemas;
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            List<Map<String, String>> vars;
            try (PreparedStatement ps = conn.prepareStatement("select PRODUCT,VERSION,STATUS FROM PRODUCT_COMPONENT_VERSION")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    Map<String, Integer> extractColumn = extractColumn(resultSet.getMetaData());
                    StringMapRowMapper rowMapper = new StringMapRowMapper(extractColumn);
                    vars = new MultipleRowResultSetExtractor<>(rowMapper).extractData(resultSet);
                }
            }

            String version = null;
            for (Map<String, String> var : vars) {
                if (StringUtils.contains(var.get("PRODUCT"), "Oracle Database")) {
                    version = var.get("VERSION");
                    break;
                }
            }

            if (StringUtils.isNotBlank(version)) {
                return version;
            } else {
                try (PreparedStatement ps = conn.prepareStatement("select * from v$version"); ResultSet resultSet = ps.executeQuery()) {
                    return ((ValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
                }
            }
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = selectSchemasSql();
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    private String selectSchemasSql() throws SQLException {
        if (!this.excludeOraMaintainedSchemas) {
            return SCHEMAS_ALL;
        }
        return isLe11g() ? SCHEMAS_LE_11G : SCHEMAS_DEFAULT;
    }

    private boolean isLe11g() throws SQLException { return isLe11g(getVersion()); }

    private boolean isLe11g(String version) {
        return StringUtils.isNotBlank(version) && (version.startsWith("11.") || version.startsWith("10."));
    }

    public Value selectSchema(String schema) throws SQLException {
        String sql = "select USERNAME from SYS.ALL_USERS where USERNAME = ?";

        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = OraMetaProviderUtils.convertSchema(rs);
                return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
            }
        }
    }

    /**
     * <p>oracle version: 12c</p>
     * <p>the table of 'ALL_TABLES' in Oracle db could not distinct type of table and materialized view,
     * a way to distinct them is join the 'ALL_MVIEWS', but in another way, the table of 'ALL_TAB_COMMENTS'
     * do not contain the materialized view, by this reason, this method try to use inner join with
     * 'ALL_TABLES' and 'ALL_TAB_COMMENTS' to filter the materialized view from tables</p>
     * @param schema db schema
     * @return table name list
     * @throws SQLException sqlException
     */
    public List<Value> selectTables(String schema) throws SQLException {
        String sqlBuilder = "SELECT TAB.TABLE_NAME AS TABLE_NAME, 'TABLE' AS TABLE_TYPE, COMMENTS FROM SYS.ALL_TABLES TAB "
                            + " JOIN SYS.ALL_TAB_COMMENTS ON TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER AND TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME "
                            + " AND SYS.ALL_TAB_COMMENTS.TABLE_TYPE = 'TABLE' ";
        return this.selectByConditions(schema, sqlBuilder);
    }

    public List<Value> selectViews(String schema) throws SQLException {
        //        String sqlBuilder = "select TAB.TABLE_NAME AS TABLE_NAME,'VIEW' as TABLE_TYPE,COMMENTS "
        //                            + "from (select VIEW_NAME TABLE_NAME,OWNER  from SYS.ALL_VIEWS) TAB left join SYS.ALL_TAB_COMMENTS on "
        //                            + "TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME and SYS.ALL_TAB_COMMENTS.TABLE_TYPE = 'VIEW'";
        String sql = objectSqlBuilderByType("VIEW");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertViewName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectMaterializedView(String schema) throws SQLException {
        String sqlBuilder = "select TABLE_NAME, TABLE_TYPE, '' AS COMMENTS from (\n" +//
                            "    select OWNER,MVIEW_NAME AS TABLE_NAME, 'MATERIALIZED' AS TABLE_TYPE from SYS.ALL_MVIEWS\n" +//
                            "    union all\n" +//
                            "    select LOG_OWNER AS OWNER, LOG_TABLE AS TABLE_NAME, 'MATERIALIZED' AS TABLE_TYPE from SYS.ALL_MVIEW_LOGS\n" +//
                            ") TAB";
        return this.selectByConditions(schema, sqlBuilder);
    }

    /**
     * get meta information from table of 'ALL_OBJECTS'
     * @param type PROCEDURE | FUNCTION | TRIGGER
     * @return sql
     */
    private String objectSqlBuilderByType(String type) {
        return "SELECT OBJECT_NAME,STATUS FROM ALL_OBJECTS WHERE OWNER = ? AND OBJECT_TYPE = '" + type + "' ORDER BY OBJECT_NAME ASC";

    }

    public List<Value> selectProcedures(String schema) throws SQLException {
        //        String sql = objectSqlBuilderByType("PROCEDURE");
        String sql = "SELECT OBJ.OBJECT_NAME OBJECT_NAME,ARG.ARGUMENT_NAME ARGUMENT_NAME,"
                     + "ARG.DATA_LENGTH LENGTH,ARG.POSITION POSITION,ARG.DATA_TYPE DATA_TYPE,OBJ.STATUS FROM ALL_OBJECTS OBJ " //
                     + "LEFT JOIN ALL_ARGUMENTS ARG " //
                     + "ON OBJ.OBJECT_ID = ARG.OBJECT_ID " //
                     + "WHERE OBJ.OWNER = ? AND OBJ.OBJECT_TYPE = 'PROCEDURE' ORDER BY OBJECT_NAME ASC";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertProcedureName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectFunctions(String schema) throws SQLException {
        //        String sql = objectSqlBuilderByType("FUNCTION");
        String sql = "SELECT OBJ.OBJECT_NAME OBJECT_NAME,ARG.ARGUMENT_NAME ARGUMENT_NAME,ARG.PLS_TYPE TYPE,"
                     + "ARG.DATA_LENGTH LENGTH,ARG.POSITION POSITION,ARG.DATA_TYPE DATA_TYPE,OBJ.STATUS FROM ALL_OBJECTS OBJ " //
                     + "LEFT JOIN ALL_ARGUMENTS ARG " //
                     + "ON OBJ.OBJECT_ID = ARG.OBJECT_ID "//
                     + "WHERE OBJ.OWNER = ? AND OBJ.OBJECT_TYPE = 'FUNCTION' ORDER BY OBJECT_NAME ASC";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertFunctionName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSequences(String schema) throws SQLException {
        String sql = objectSqlBuilderByType("SEQUENCE");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertSequenceName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectDbLinks(String schema) throws SQLException {
        String sql = "select DB_LINK AS OBJECT_NAME,HOST  from dba_db_links where owner = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertDbLinkName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectRoles(String schema) throws SQLException {
        String sql = "select ROLE OBJECT_NAME,'VALID' STATUS from SYS.DBA_ROLES";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertRoleName(rs);
            }
        }
    }

    public List<Value> selectUsers(String schema) throws SQLException {
        String sql = "select USERNAME OBJECT_NAME,'VALID' STATUS from all_users";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertUserName(rs);
            }
        }
    }

    public List<Value> selectJobs(String schema) throws SQLException {
        String sql = "SELECT job as OBJECT_NAME,WHAT,BROKEN FROM  all_jobs where SCHEMA_USER = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertJobName(rs);
            }
        }
    }

    public List<Value> selectScheduleJobs(String schema) throws SQLException {
        String sql = "SELECT job_name AS OBJECT_NAME,JOB_ACTION,ENABLED FROM all_scheduler_jobs where OWNER = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertScheduleJobName(rs);
            }
        }
    }

    public List<Value> selectTrigger(String schema) throws SQLException {
        String sql = objectSqlBuilderByType("TRIGGER");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertTriggerName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSynonym(String schema) throws SQLException {
        String sql = objectSqlBuilderByType("SYNONYM");
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertSynonymName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String sql = conditions + " where TAB.OWNER = ? order by TABLE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertTableName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchByPart(conn, catalog, schema, tabs, TABLE);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchByPart(conn, catalog, schema, tabs, VIEW);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    private List<RdbTable> fetchByPart(Connection conn, String catalog, String schema, List<String> tabs, String conditions) throws SQLException {
        String queryString = conditions + " where TAB.OWNER = ? and TAB.TABLE_NAME in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(queryString)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                return OraMetaProviderUtils.convertTable(resultSet);
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select IDX.TABLE_OWNER,IDX.TABLE_NAME,IDX.OWNER,IDX.INDEX_NAME,IDX.INDEX_TYPE,IDX.UNIQUENESS,IDX.GENERATED,DESCEND,PARTITIONED,TEMPORARY,COL.COLUMN_NAME,COL.DESCEND " //
                     + "from SYS.ALL_INDEXES IDX " //
                     + "left join SYS.ALL_IND_COLUMNS COL on IDX.OWNER = COL.INDEX_OWNER and IDX.INDEX_NAME = COL.INDEX_NAME " //
                     + "where IDX.TABLE_OWNER = ? and IDX.TABLE_NAME in " + buildWhereIn(tabs) + " and COLUMN_NAME is not null "//
                     + "order by COL.COLUMN_POSITION asc";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbIndex> idxList = OraMetaProviderUtils.convertIndex(resultSet);
                if (idxList.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbIndex>> idxMap = new LinkedHashMap<>();
                OraMetaProviderUtils.fillIdxsMap(idxMap, idxList);
                return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select CON.OWNER,CON.TABLE_NAME,CON.CONSTRAINT_NAME,STATUS,VALIDATED,GENERATED,DELETE_RULE,\n" //
                     + "       C2.OWNER TARGET_OWNER,C2.TABLE_NAME TARGET_TABLE,\n" //
                     + "       C1.COLUMN_NAME SOURCE_COLUMN,C2.COLUMN_NAME TARGET_COLUMN\n" //
                     + "from SYS.ALL_CONSTRAINTS CON,SYS.ALL_CONS_COLUMNS C1,SYS.ALL_CONS_COLUMNS C2\n" //
                     + "where\n" //
                     + "    CON.R_OWNER = C1.OWNER and CON.CONSTRAINT_NAME = C1.CONSTRAINT_NAME and\n" //
                     + "    CON.R_OWNER = C2.OWNER and CON.R_CONSTRAINT_NAME = C2.CONSTRAINT_NAME and\n" //
                     + "    C1.POSITION = C2.POSITION and\n" //
                     + "    CONSTRAINT_TYPE = 'R' and CON.OWNER = ? and CON.TABLE_NAME in " + buildWhereIn(tabs) + " order by C1.POSITION";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbForeignKey> fkList = OraMetaProviderUtils.convertForeignKey(resultSet);
                if (fkList.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbForeignKey>> fkMap = new LinkedHashMap<>();
                OraMetaProviderUtils.fillFkMap(fkMap, fkList);
                return fkMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, fks -> new ArrayList<>(fks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select CON.OWNER,CC.TABLE_NAME,CON.CONSTRAINT_NAME,CON.CONSTRAINT_TYPE,STATUS,VALIDATED,GENERATED,COLUMN_NAME from SYS.ALL_CONS_COLUMNS CC\n" //
                     + "left join SYS.ALL_CONSTRAINTS CON on CC.CONSTRAINT_NAME = CON.CONSTRAINT_NAME and CC.OWNER = CON.OWNER\n" //
                     + "where CON.CONSTRAINT_TYPE in ('P','U') and CC.OWNER = ? and CC.TABLE_NAME in " + buildWhereIn(tabs) + " order by POSITION asc";

        Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("CONSTRAINT_TYPE");
                    String table = rs.getString("TABLE_NAME");
                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    if (type.equals("P")) {
                        OraMetaProviderUtils.mapToPk(rs, constraints);
                    } else if (type.equals("U")) {
                        OraMetaProviderUtils.mapToUk(rs, constraints);
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint type:" + type);
                    }
                }
            }
        }

        return pkUkMap;
    }

    @Override
    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        List<RdbTable> rdbTables = fetchByPart(conn, catalog, schema, tabs, TABLE);
        List<RdbTable> views = fetchByPart(conn, catalog, schema, tabs, VIEW);

        rdbTables.addAll(views);
        return rdbTables;
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String condition = " where COLS.HIDDEN_COLUMN = 'NO' and COLS.OWNER = ? and COLS.TABLE_NAME in " + buildWhereIn(tabs) + " order by SEGMENT_COLUMN_ID asc";

        String version = getVersion();
        boolean isLe11g = isLe11g(version);
        if (isLe11g) {
            condition = COLUMNS_LE_11G + condition;
        } else {
            condition = COLUMNS_DEFAULT + condition;
        }

        try (PreparedStatement ps = conn.prepareStatement(condition)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> cols = OraMetaProviderUtils.convertColumn(rs, isLe11g);
                if (cols.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : cols) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    protected List<RdbParam> fetchParams(Connection conn, String catalog, String schema, List<String> procs, UmiTypes umiTypes) throws SQLException {
        String sql = "select OWNER,POSITION,OBJECT_NAME,ARGUMENT_NAME,DATA_TYPE,DATA_LENGTH  from ALL_ARGUMENTS where OWNER = ?  and OBJECT_NAME in " + buildWhereIn(procs)
                     + " order by POSITION";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(procs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertParams(rs);
            }
        }
    }

    private List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        return OraMetaProviderUtils.convertParams(rs);
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
                List<RdbParam> rdbParams = this.fetchParams(conn, catalog, schema, procs, UmiTypes.Procedure);
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

    public List<RdbFunction> loadFunctions(String catalog, String schema, List<String> functionNames) throws SQLException {
        functionNames = stringArray2List(functionNames);
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funs : CollectionUtils.splitList(functionNames, defaultGroupSize())) {
                List<RdbFunction> rdbFunctions = this.fetchFunctionByPart(conn, catalog, schema, funs);
                List<RdbParam> rdbParams = this.fetchParams(conn, catalog, schema, funs, UmiTypes.Function);
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

    private List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) throws SQLException {
        String sql = "select a.OBJECT_NAME,a.OWNER,a.OBJECT_TYPE,a.STATUS,a.CREATED,a.LAST_DDL_TIME,b.AGGREGATE, b.PIPELINED,b.PARALLEL,b.INTERFACE,b.DETERMINISTIC "
                     + "from all_objects a left join ALL_PROCEDURES b on a.OBJECT_ID = b.OBJECT_ID " + " where  a.OWNER = ? and a.OBJECT_TYPE = 'PROCEDURE' and a.OBJECT_NAME in  "
                     + buildWhereIn(procs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(procs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertProcedures(rs);
            }
        }
    }

    protected List<RdbFunction> fetchFunctionByPart(Connection conn, String catalog, String schema, List<String> funcs) throws SQLException {
        String sql = "select a.OBJECT_NAME,a.OWNER,a.OBJECT_TYPE,a.STATUS,a.CREATED,a.LAST_DDL_TIME,b.AGGREGATE, b.PIPELINED,b.PARALLEL,b.INTERFACE,b.DETERMINISTIC "
                     + "from all_objects a left join ALL_PROCEDURES b on a.OBJECT_ID = b.OBJECT_ID " + " where  a.OWNER = ? and a.OBJECT_TYPE = 'FUNCTION' and a.OBJECT_NAME in  "
                     + buildWhereIn(funcs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(funcs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertFunctions(rs);
            }
        }
    }

    protected List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        return OraMetaProviderUtils.convertFunctions(rs);
    }

    private List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        return OraMetaProviderUtils.convertProcedures(rs);
    }

    public Value loadTrigger(String schema, String leafName) throws SQLException {
        String sql = "select TRI.TRIGGER_TYPE TRIGGER_TYPE,TRI.TRIGGER_NAME TRIGGER_NAME,TRI.TRIGGERING_EVENT TRIGGERING_EVENT,TRI.TABLE_NAME TABLE_NAME,TRI.TRIGGER_BODY TRIGGER_BODY,COL.COLUMN_NAME COLUMN_NAME,"
                     + "TRI.REFERENCING_NAMES REFERENCING_NAMES,TRI.WHEN_CLAUSE WHEN_CLAUSE, TRI.OWNER OWNER, TRI.TABLE_OWNER TABLE_OWNER, TRI.REFERENCING_NAMES REFERENCING_NAMES,"
                     + " TRI.STATUS STATUS,OBJ.STATUS OBJ_STATUS,to_char(CREATED, 'yyyy-MM-dd HH24:MI:SS') as CREATE_TIME, to_char(LAST_DDL_TIME, 'yyyy-MM-dd HH24:MI:SS') as LAST_DDL_TIME "
                     + "from ALL_TRIGGERS TRI left join ALL_TRIGGER_COLS COL on TRI.TRIGGER_NAME = COL.TRIGGER_NAME "
                     + "left join ALL_OBJECTS OBJ on  OBJ.OBJECT_NAME = TRI.TRIGGER_NAME and OBJ.OWNER = TRI.OWNER "
                     + "where TRI.OWNER = ? and TRI.TRIGGER_NAME = ? and OBJ.OBJECT_TYPE = 'TRIGGER' ";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbTrigger> values = OraMetaProviderUtils.convertTrigger(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadJob(String schema, String leafName) throws SQLException {
        String sql = "select JOB,LOG_USER,SCHEMA_USER,LAST_DATE,NEXT_DATE,BROKEN,INTERVAL,FAILURES,WHAT from dba_Jobs where SCHEMA_USER = ? and JOB = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbJob> values = OraMetaProviderUtils.convertJob(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadScheduleJob(String schema, String leafName) throws SQLException {
        String sql = "select OWNER,JOB_NAME,JOB_STYLE,JOB_CREATOR,JOB_TYPE,JOB_ACTION,NUMBER_OF_ARGUMENTS,SCHEDULE_TYPE,START_DATE,REPEAT_INTERVAL,"
                     + "END_DATE,JOB_CLASS,ENABLED,AUTO_DROP,RESTART_ON_RECOVERY,RESTART_ON_FAILURE,COMMENTS,STATE from SYS.ALL_SCHEDULER_JOBS where OWNER = ? and JOB_NAME = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbScheduleJob> values = OraMetaProviderUtils.convertScheduleJob(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadDbLink(String schema, String leafName) throws SQLException {
        String sql = "select OWNER,DB_LINK,USERNAME,HOST,CREATED from SYS.DBA_DB_LINKS where OWNER = ? and DB_LINK = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbDbLink> values = OraMetaProviderUtils.convertDbLink(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadMaterialized(String schema, String leafName) throws SQLException {
        String sql = "select AM.OWNER,AM.MVIEW_NAME,AM.QUERY,AM.QUERY_LEN,AM.LAST_REFRESH_DATE,AM.LAST_REFRESH_END_TIME,AO.CREATED,AO.LAST_DDL_TIME,AO.STATUS from SYS.ALL_MVIEWS AM left join ALL_OBJECTS AO on AM.OWNER = AO.OWNER and MVIEW_NAME = AO.OBJECT_NAME\n"
                     + "where AM.OWNER = ? and AM.MVIEW_NAME = ? and AO.OBJECT_TYPE = 'MATERIALIZED VIEW'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbView> values = OraMetaProviderUtils.convertMaterialized(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadSequence(String schema, String leafName) throws SQLException {
        String sql = "select SEQUENCE_OWNER,SEQUENCE_NAME,MIN_VALUE,MAX_VALUE,INCREMENT_BY,CYCLE_FLAG,ORDER_FLAG,CACHE_SIZE,LAST_NUMBER,SESSION_FLAG,KEEP_VALUE,CREATED,LAST_DDL_TIME,STATUS "
                     + "from SYS.ALL_SEQUENCES SEQ left join SYS.ALL_OBJECTS OBJ on SEQ.SEQUENCE_OWNER = OBJ.OWNER and SEQ.SEQUENCE_NAME = OBJ.OBJECT_NAME "
                     + "where SEQ.SEQUENCE_OWNER = ? and SEQ.SEQUENCE_NAME = ? and OBJ.OBJECT_TYPE = 'SEQUENCE'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbSequence> values = OraMetaProviderUtils.convertSequence(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadUser(String schema, String leafName) throws SQLException {
        String sql = "select USERNAME,USER_ID,CREATED,COMMON,ORACLE_MAINTAINED from SYS.ALL_USERS where USERNAME = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbUser> values = OraMetaProviderUtils.convertUser(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadRole(String schema, String leafName) throws SQLException {
        String sql = "select ROLE,AUTHENTICATION_TYPE,COMMON,ORACLE_MAINTAINED from DBA_ROLES where ROLE = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbRole> values = OraMetaProviderUtils.convertRole(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public Value loadSynonym(String schema, String leafName) throws SQLException {
        String sql = "select SYN.OWNER,SYN.SYNONYM_NAME,SYN.TABLE_OWNER,SYN.TABLE_NAME,SYN.DB_LINK,OBJ.CREATED,OBJ.LAST_DDL_TIME,OBJ.STATUS from "
                     + "SYS.ALL_SYNONYMS SYN left join SYS.ALL_OBJECTS OBJ on SYN.OWNER = OBJ.OWNER and  SYN.SYNONYM_NAME = OBJ.OBJECT_NAME\n"
                     + "where SYN.OWNER = ? and SYN.SYNONYM_NAME = ? and OBJ.OBJECT_TYPE = 'SYNONYM' ";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbSynonym> values = OraMetaProviderUtils.convertSynonym(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }
}
