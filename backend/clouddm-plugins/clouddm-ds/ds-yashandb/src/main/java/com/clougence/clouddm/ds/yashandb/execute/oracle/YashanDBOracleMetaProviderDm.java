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
package com.clougence.clouddm.ds.yashandb.execute.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaProviderDm;
import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;

/**
 * Metadata reading for YashanDB.
 * <p>
 * The YashanDB data dictionary is broadly Oracle-compatible, but the columns of several views do not exactly
 * match Oracle (measured on YashanDB 23.4.7): {@code PRODUCT_COMPONENT_VERSION} and {@code ALL_MVIEW_LOGS} do
 * not exist; {@code ALL_TABLES} lacks the IOT / materialized-view-log columns; {@code ALL_VIEWS} lacks
 * {@code VIEW_TYPE}, {@code SUPERVIEW_NAME} and {@code EDITIONING_VIEW}; {@code DBA_TAB_COLS} lacks
 * {@code CHARACTER_SET_NAME}; the length column of {@code ALL_MVIEWS} is {@code QYERT_LEN}; {@code ALL_SEQUENCES}
 * lacks {@code SESSION_FLAG} and {@code KEEP_VALUE}; and the maintained flag column is named
 * {@code DATABASE_MAINTAINED} instead of {@code ORACLE_MAINTAINED}.
 * </p>
 * <p>
 * The affected queries are therefore rewritten against the YashanDB data dictionary while <b>keeping column
 * aliases exactly identical to Oracle</b>, so that the result set conversion logic of
 * {@link OraMetaProviderUtils} can still be reused.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBOracleMetaProviderDm extends OraMetaProviderDm {

    private static final String YAS_TABLE   = "select TAB.OWNER,TAB.TABLE_NAME,TABLESPACE_NAME,TAB.TABLE_TYPE,TAB.LOG_TABLE,TAB.LOG_ROWIDS,TAB.LOG_PK,TAB.LOG_SEQ,COMMENTS,TAB.TEMPORARY,TAB.IOT_TYPE ,\n"
                                              + "   TAB.STATUS as VALID_FLAG, TAB.CLUSTER_NAME, TAB.PCT_FREE, TAB.PCT_USED, TAB.INI_TRANS,TAB.MAX_TRANS, TAB.INITIAL_EXTENT, TAB.NEXT_EXTENT, TAB.MIN_EXTENTS, TAB.MAX_EXTENTS,\n"
                                              + " PARTITIONED,  to_char(CREATED, 'yyyy-MM-dd HH24:MI:SS') as CREATE_TIME, to_char(LAST_DDL_TIME, 'yyyy-MM-dd HH24:MI:SS') as LAST_DDL_TIME from (\n"
                                              + "  select OWNER,TABLE_NAME,TABLESPACE_NAME,'TABLE' TABLE_TYPE,null LOG_TABLE,null LOG_ROWIDS,null LOG_PK,null LOG_SEQ,TEMPORARY,null IOT_TYPE,\n"
                                              + "   STATUS, null CLUSTER_NAME, PCT_FREE, null PCT_USED, INI_TRANS, MAX_TRANS, null INITIAL_EXTENT, null NEXT_EXTENT, null MIN_EXTENTS, null MAX_EXTENTS, PARTITIONED from SYS.ALL_TABLES\n"
                                              + ") TAB\n"
                                              + "left join SYS.ALL_TAB_COMMENTS on TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME and TAB.TABLE_TYPE = SYS.ALL_TAB_COMMENTS.TABLE_TYPE\n"
                                              + "left join ALL_OBJECTS on TAB.OWNER = ALL_OBJECTS.OWNER and TAB.TABLE_NAME = ALL_OBJECTS.OBJECT_NAME and TAB.TABLE_TYPE = ALL_OBJECTS.OBJECT_TYPE";

    private static final String YAS_VIEW    = "select TAB.OWNER,TAB.TABLE_NAME,TABLESPACE_NAME,TAB.TABLE_TYPE,TAB.LOG_TABLE,TAB.LOG_ROWIDS,TAB.LOG_PK,TAB.LOG_SEQ,COMMENTS,TAB.TEMPORARY,TAB.SQL,\n"
                                              + "  TAB.TEXT_LENGTH, TAB.SUPERVIEW_NAME, TAB.EDITIONING_VIEW, TAB.READ_ONLY,\n"
                                              + "  to_char(CREATED, 'yyyy-MM-dd HH24:MI:SS') as CREATE_TIME,\n"
                                              + "  to_char(LAST_DDL_TIME, 'yyyy-MM-dd HH24:MI:SS') as LAST_DDL_TIME,  status as VALID_FLAG from (\n"
                                              + "  select OWNER,VIEW_NAME TABLE_NAME, TEXT SQL, null TABLESPACE_NAME,'VIEW' TABLE_TYPE,null LOG_TABLE,null LOG_ROWIDS,null LOG_PK,null LOG_SEQ, null TEMPORARY,\n"
                                              + "  TEXT_LENGTH as TEXT_LENGTH, null SUPERVIEW_NAME, null EDITIONING_VIEW, READ_ONLY as READ_ONLY\n"
                                              + "  from SYS.ALL_VIEWS) TAB\n"
                                              + "left join SYS.ALL_TAB_COMMENTS on TAB.OWNER = SYS.ALL_TAB_COMMENTS.OWNER and TAB.TABLE_NAME = SYS.ALL_TAB_COMMENTS.TABLE_NAME and TAB.TABLE_TYPE = SYS.ALL_TAB_COMMENTS.TABLE_TYPE\n"
                                              + "left join ALL_OBJECTS on TAB.OWNER = ALL_OBJECTS.OWNER and TAB.TABLE_NAME = ALL_OBJECTS.OBJECT_NAME and TAB.TABLE_TYPE = ALL_OBJECTS.OBJECT_TYPE";

    /**
     * YashanDB {@code DBA_TAB_COLS.DATA_TYPE} uses YashanDB's own type names
     * (BIGINT/TINYINT/DOUBLE/BOOLEAN/BIT/TIME/JSON), and the DATA_TYPE of view columns is even {@code UNKNOWN}.
     * None of these are covered by the Oracle family {@code OracleSqlTypes}; passing them straight to
     * {@code OracleSqlTypes.toOracleType(..)} throws UnsupportedOperationException (which is not swallowed by
     * {@code OraMetaProviderUtils}, so a single BIT column used to break the whole table browse).
     * <p>
     * The YashanDB native type names are therefore normalized at the SQL layer to the closest Oracle family
     * type names (keeping the column aliases unchanged so the result set conversion logic of
     * {@code OraMetaProviderUtils} can still be reused). All mapping targets have been verified to be accepted
     * by DDL on YashanDB 23.4.7.
     * </p>
     * <p>
     * Note on {@code BIT} -&gt; {@code RAW}: the Oracle family has no BIT type, and {@code RAW} is its closest
     * binary type ({@code OracleType.RAW}, binary). The mapping is approximate rather than exactly equivalent:
     * YashanDB reports {@code DATA_LENGTH} for BIT in <b>bits</b> while RAW counts <b>bytes</b>, so
     * {@link #YAS_DATA_LENGTH} divides the BIT length by 8 to keep the reported size the same as the real
     * storage (a {@code BIT(64)} is reported as {@code RAW(8)}).
     * </p>
     */
    private static final String YAS_DATA_TYPE
            = "decode(COLS.DATA_TYPE," //
              + "'BIGINT','NUMBER BIGINT'," //
              + "'TINYINT','NUMBER BIGINT'," //
              + "'DOUBLE','BINARY DOUBLE'," //
              + "'BOOLEAN','PLSQL_BOOLEAN'," //
              + "'BIT','RAW'," //
              + "'TIME','DATE'," //
              + "'JSON','CLOB'," //
              + "'UNKNOWN','VARCHAR'," //
              + "COLS.DATA_TYPE) as DATA_TYPE";

    /**
     * YashanDB stores the TIMESTAMP precision in {@code DATA_PRECISION} (Oracle stores it in
     * {@code DATA_SCALE}); align it to Oracle semantics here, otherwise {@code RdbColumn.datetimePrecision}
     * would be lost.
     */
    private static final String YAS_DATA_SCALE = "case when COLS.DATA_TYPE like 'TIMESTAMP%' then COLS.DATA_PRECISION else COLS.DATA_SCALE end as DATA_SCALE";

    /**
     * YashanDB reports {@code DATA_LENGTH} for BIT columns in <b>bits</b>, whereas the Oracle type it is
     * normalized to ({@code RAW}) counts <b>bytes</b>; convert it so the size shown matches the real storage
     * (a {@code BIT(64)} becomes {@code RAW(8)}).
     */
    private static final String YAS_DATA_LENGTH = "case when COLS.DATA_TYPE = 'BIT' then ceil(COLS.DATA_LENGTH/8) else COLS.DATA_LENGTH end as DATA_LENGTH";

    private static final String YAS_COLUMNS = "select COLS.OWNER,COLS.TABLE_NAME,COLS.COLUMN_NAME," + YAS_DATA_TYPE + ",COLS.DATA_TYPE_OWNER,COLS.COLUMN_ID," + YAS_DATA_LENGTH
                                              + ",COLS.CHAR_LENGTH,COLS.DATA_PRECISION," + YAS_DATA_SCALE
                                              + ",COLS.NULLABLE,null CHARACTER_SET_NAME,COLS.HIDDEN_COLUMN,COLS.VIRTUAL_COLUMN,COLS.IDENTITY_COLUMN,COMM.COMMENTS,COLS.DATA_DEFAULT from SYS.DBA_TAB_COLS COLS\n"
                                              + "left join SYS.DBA_COL_COMMENTS COMM on COLS.OWNER = COMM.OWNER and COLS.TABLE_NAME = COMM.TABLE_NAME and COLS.COLUMN_NAME = COMM.COLUMN_NAME";

    private static final String YAS_MVIEWS  = "select TABLE_NAME, TABLE_TYPE, '' AS COMMENTS from (\n"
                                              + "    select OWNER,MVIEW_NAME AS TABLE_NAME, 'MATERIALIZED' AS TABLE_TYPE from SYS.ALL_MVIEWS\n"
                                              + ") TAB";

    /** Whether to exclude database-maintained schemas from the schema list (YashanDB uses {@code DATABASE_MAINTAINED = 'Y'}). */
    private final boolean excludeMaintainedSchemas;

    public YashanDBOracleMetaProviderDm(Connection connection){
        this(connection, true);
    }

    public YashanDBOracleMetaProviderDm(Connection connection, boolean excludeMaintainedSchemas){
        super(connection, excludeMaintainedSchemas);
        this.excludeMaintainedSchemas = excludeMaintainedSchemas;
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet();
                PreparedStatement ps = conn.prepareStatement("select VERSION_NUMBER from V$VERSION");
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    @Override
    public List<Value> selectSchemas() throws SQLException {
        // The maintained flag column of the YashanDB data dictionary is DATABASE_MAINTAINED (ORACLE_MAINTAINED on Oracle).
        String sql = this.isExcludeMaintainedSchemas() ? "select USERNAME from SYS.ALL_USERS where DATABASE_MAINTAINED = 'N' order by USERNAME asc"
                                                       : "select USERNAME from SYS.ALL_USERS order by USERNAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return OraMetaProviderUtils.convertSchema(rs);
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = YAS_TABLE + " where TAB.OWNER = ? and TAB.TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndNames(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertTable(rs);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = YAS_VIEW + " where TAB.OWNER = ? and TAB.TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndNames(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertTable(rs);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        // The parent implementation queries the native Oracle ALL_TABLES/ALL_MVIEW_LOGS and ALL_VIEWS directly,
        // which YashanDB does not support; the YashanDB versions must be used.
        List<RdbTable> result = new ArrayList<>(fetchTableByPart(conn, catalog, schema, tabs));
        result.addAll(fetchViewByPart(conn, catalog, schema, tabs));
        return result;
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        // YashanDB SEGMENT_COLUMN_ID is always NULL (measured on 23.4.7), so ordering by COLUMN_ID is required;
        // otherwise the column order is not deterministic.
        String sql = YAS_COLUMNS + " where COLS.HIDDEN_COLUMN = 'NO' and COLS.OWNER = ? and COLS.TABLE_NAME in " + buildWhereIn(tabs) + " order by COLS.COLUMN_ID asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndNames(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> cols = OraMetaProviderUtils.convertColumn(rs, false);
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

    @Override
    public List<Value> selectMaterializedView(String schema) throws SQLException {
        // ALL_MVIEW_LOGS does not exist on YashanDB; read from ALL_MVIEWS only.
        String sql = YAS_MVIEWS + " where TAB.OWNER = ? order by TABLE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertTableName(rs).stream().filter(v -> v.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public Value loadMaterialized(String schema, String leafName) throws SQLException {
        // The length column of YashanDB ALL_MVIEWS is named QYERT_LEN, and there are no LAST_REFRESH_* columns.
        String sql = "select AM.OWNER,AM.MVIEW_NAME,AM.QUERY,AM.QYERT_LEN as QUERY_LEN,null as LAST_REFRESH_DATE,null as LAST_REFRESH_END_TIME,AO.CREATED,AO.LAST_DDL_TIME,AO.STATUS from SYS.ALL_MVIEWS AM left join ALL_OBJECTS AO on AM.OWNER = AO.OWNER and MVIEW_NAME = AO.OBJECT_NAME\n"
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

    @Override
    public Value loadSequence(String schema, String leafName) throws SQLException {
        // YashanDB ALL_SEQUENCES has no SESSION_FLAG / KEEP_VALUE.
        String sql = "select SEQUENCE_OWNER,SEQUENCE_NAME,MIN_VALUE,MAX_VALUE,INCREMENT_BY,CYCLE_FLAG,ORDER_FLAG,CACHE_SIZE,LAST_NUMBER,null SESSION_FLAG,null KEEP_VALUE,OBJ.CREATED,OBJ.LAST_DDL_TIME,OBJ.STATUS "
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

    @Override
    public Value loadScheduleJob(String schema, String leafName) throws SQLException {
        // YashanDB ALL_SCHEDULER_JOBS has no JOB_CLASS / RESTART_ON_RECOVERY / RESTART_ON_FAILURE.
        String sql = "select OWNER,JOB_NAME,JOB_STYLE,JOB_CREATOR,JOB_TYPE,JOB_ACTION,NUMBER_OF_ARGUMENTS,SCHEDULE_TYPE,START_DATE,REPEAT_INTERVAL,"
                     + "END_DATE,null JOB_CLASS,ENABLED,AUTO_DROP,null RESTART_ON_RECOVERY,null RESTART_ON_FAILURE,COMMENTS,STATE from SYS.ALL_SCHEDULER_JOBS where OWNER = ? and JOB_NAME = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbScheduleJob> values = OraMetaProviderUtils.convertScheduleJob(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    @Override
    public Value loadJob(String schema, String leafName) throws SQLException {
        // YashanDB DBA_JOBS.JOB is a numeric column and needs an explicit conversion to avoid a type
        // conversion error caused by string comparison.
        String sql = "select JOB,LOG_USER,SCHEMA_USER,LAST_DATE,NEXT_DATE,BROKEN,INTERVAL,FAILURES,WHAT from dba_Jobs where SCHEMA_USER = ? and JOB = TO_NUMBER(?)";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbJob> values = OraMetaProviderUtils.convertJob(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    @Override
    public Value loadUser(String schema, String leafName) throws SQLException {
        // YashanDB ALL_USERS has no COMMON, and the maintained flag column is DATABASE_MAINTAINED.
        String sql = "select USERNAME,USER_ID,CREATED,null COMMON,DATABASE_MAINTAINED as ORACLE_MAINTAINED from SYS.ALL_USERS where USERNAME = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbUser> values = OraMetaProviderUtils.convertUser(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    @Override
    public Value loadRole(String schema, String leafName) throws SQLException {
        // YashanDB DBA_ROLES only has ROLE / ROLE_ID / TYPE / SYS_MAINTAINED.
        String sql = "select ROLE,null AUTHENTICATION_TYPE,null COMMON,SYS_MAINTAINED as ORACLE_MAINTAINED from DBA_ROLES where ROLE = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbRole> values = OraMetaProviderUtils.convertRole(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    protected boolean isExcludeMaintainedSchemas() {
        return this.excludeMaintainedSchemas;
    }

    private void bindSchemaAndNames(PreparedStatement ps, String schema, List<String> names) throws SQLException {
        List<String> params = new ArrayList<>(names);
        params.add(0, schema);
        for (int i = 1; i <= params.size(); i++) {
            ps.setString(i, params.get(i - 1));
        }
    }
}
