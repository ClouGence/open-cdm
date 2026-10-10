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
package com.clougence.clouddm.ds.yashandb.execute;

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
 * 崖山数据库元数据读取。
 * <p>
 * 崖山数据库的数据字典整体兼容 Oracle，但若干视图的列与 Oracle 并不完全一致（实测 YashanDB
 * 23.4.7）：不存在 {@code PRODUCT_COMPONENT_VERSION}、{@code ALL_MVIEW_LOGS}；{@code ALL_TABLES}
 * 缺少 IOT/物化视图日志列；{@code ALL_VIEWS} 缺少 {@code VIEW_TYPE}、{@code SUPERVIEW_NAME}、
 * {@code EDITIONING_VIEW}；{@code DBA_TAB_COLS} 缺少 {@code CHARACTER_SET_NAME}；{@code ALL_MVIEWS}
 * 的长度列为 {@code QYERT_LEN}；{@code ALL_SEQUENCES} 缺少 {@code SESSION_FLAG}、{@code KEEP_VALUE}；
 * 维护标记列名为 {@code DATABASE_MAINTAINED} 而非 {@code ORACLE_MAINTAINED}。
 * </p>
 * <p>
 * 因此这里按崖山的数据字典重写受影响的查询，同时<b>保持与 Oracle 完全一致的列别名</b>，
 * 以便继续复用 {@link OraMetaProviderUtils} 的结果集转换逻辑。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMetaProviderDm extends OraMetaProviderDm {

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
     * 崖山 {@code DBA_TAB_COLS.DATA_TYPE} 用的是崖山自己的类型名（BIGINT/TINYINT/DOUBLE/BOOLEAN/TIME/JSON），
     * 视图列的 DATA_TYPE 甚至是 {@code UNKNOWN}；这些都落在 Oracle 家族的 {@code OracleSqlTypes} 之外，
     * 直接交给 {@code OracleSqlTypes.toOracleType(..)} 会抛 UnsupportedOperationException。
     * <p>
     * 因此在 SQL 层把崖山原生类型名归一化成 Oracle 家族最接近的类型名（保持列别名不变，继续复用
     * {@code OraMetaProviderUtils} 的结果集转换逻辑）。映射目标均已在崖山 23.4.7 上验证过 DDL 可接受性。
     * </p>
     */
    private static final String YAS_DATA_TYPE
            = "decode(COLS.DATA_TYPE," //
              + "'BIGINT','NUMBER BIGINT'," //
              + "'TINYINT','NUMBER BIGINT'," //
              + "'DOUBLE','BINARY DOUBLE'," //
              + "'BOOLEAN','PLSQL_BOOLEAN'," //
              + "'TIME','DATE'," //
              + "'JSON','CLOB'," //
              + "'UNKNOWN','VARCHAR'," //
              + "COLS.DATA_TYPE) as DATA_TYPE";

    /**
     * 崖山把 TIMESTAMP 的精度放在 {@code DATA_PRECISION}（Oracle 放在 {@code DATA_SCALE}），
     * 这里对齐成 Oracle 的语义，否则 {@code RdbColumn.datetimePrecision} 会丢失。
     */
    private static final String YAS_DATA_SCALE = "case when COLS.DATA_TYPE like 'TIMESTAMP%' then COLS.DATA_PRECISION else COLS.DATA_SCALE end as DATA_SCALE";

    private static final String YAS_COLUMNS = "select COLS.OWNER,COLS.TABLE_NAME,COLS.COLUMN_NAME," + YAS_DATA_TYPE + ",COLS.DATA_TYPE_OWNER,COLS.COLUMN_ID,COLS.DATA_LENGTH,COLS.CHAR_LENGTH,COLS.DATA_PRECISION," + YAS_DATA_SCALE
                                              + ",COLS.NULLABLE,null CHARACTER_SET_NAME,COLS.HIDDEN_COLUMN,COLS.VIRTUAL_COLUMN,COLS.IDENTITY_COLUMN,COMM.COMMENTS,COLS.DATA_DEFAULT from SYS.DBA_TAB_COLS COLS\n"
                                              + "left join SYS.DBA_COL_COMMENTS COMM on COLS.OWNER = COMM.OWNER and COLS.TABLE_NAME = COMM.TABLE_NAME and COLS.COLUMN_NAME = COMM.COLUMN_NAME";

    private static final String YAS_MVIEWS  = "select TABLE_NAME, TABLE_TYPE, '' AS COMMENTS from (\n"
                                              + "    select OWNER,MVIEW_NAME AS TABLE_NAME, 'MATERIALIZED' AS TABLE_TYPE from SYS.ALL_MVIEWS\n"
                                              + ") TAB";

    /** 是否在 schema 列表中排除数据库自维护的 schema（崖山为 {@code DATABASE_MAINTAINED = 'Y'}）。 */
    private final boolean excludeMaintainedSchemas;

    public YashanDBMetaProviderDm(Connection connection){
        this(connection, true);
    }

    public YashanDBMetaProviderDm(Connection connection, boolean excludeMaintainedSchemas){
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
        // YashanDB 的数据字典维护标记列为 DATABASE_MAINTAINED（Oracle 为 ORACLE_MAINTAINED）。
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
        // 父类实现直接用 Oracle 原生的 ALL_TABLES/ALL_MVIEW_LOGS、ALL_VIEWS 查询，崖山不支持，必须走崖山版本。
        List<RdbTable> result = new ArrayList<>(fetchTableByPart(conn, catalog, schema, tabs));
        result.addAll(fetchViewByPart(conn, catalog, schema, tabs));
        return result;
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        // 崖山的 SEGMENT_COLUMN_ID 恒为 NULL（实测 23.4.7），必须按 COLUMN_ID 排序，否则列序不确定。
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
        // ALL_MVIEW_LOGS 在崖山不存在，仅从 ALL_MVIEWS 读取。
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
        // 崖山 ALL_MVIEWS 的长度列名为 QYERT_LEN，且无 LAST_REFRESH_* 列。
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
        // 崖山 ALL_SEQUENCES 无 SESSION_FLAG / KEEP_VALUE。
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
        // 崖山 ALL_SCHEDULER_JOBS 无 JOB_CLASS / RESTART_ON_RECOVERY / RESTART_ON_FAILURE。
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
        // 崖山 DBA_JOBS.JOB 为数值列，需显式转换，避免字符串比较引发类型转换错误。
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
        // 崖山 ALL_USERS 无 COMMON，维护标记列为 DATABASE_MAINTAINED。
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
        // 崖山 DBA_ROLES 仅有 ROLE / ROLE_ID / TYPE / SYS_MAINTAINED。
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
