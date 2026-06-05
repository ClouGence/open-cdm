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
package com.clougence.clouddm.dsfamily.mysql.execute;

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
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * MySQL 元信息获取，参考资料：
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class MyMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    protected static final String TABLE         = "select `TABLE_CATALOG`,`TABLE_SCHEMA`,`TABLE_NAME`,`TABLE_TYPE`,`TABLE_COLLATION`,`CREATE_TIME`,`UPDATE_TIME`,`TABLE_COMMENT`,`ENGINE`,`ROW_FORMAT`,`AVG_ROW_LENGTH`,`CREATE_OPTIONS`,`AUTO_INCREMENT`, "
                                                  + "`CHECK_TIME`,`INDEX_LENGTH`,`DATA_LENGTH`,`TABLE_ROWS`,`CREATE_OPTIONS`,`DATA_FREE` from INFORMATION_SCHEMA.TABLES";
    protected static final String COLUMNS       = "select `TABLE_SCHEMA`,`TABLE_NAME`,`COLUMN_NAME`,`IS_NULLABLE`,`DATA_TYPE`,`CHARACTER_MAXIMUM_LENGTH`,`CHARACTER_OCTET_LENGTH`,`NUMERIC_SCALE`,`NUMERIC_PRECISION`,`DATETIME_PRECISION`,`CHARACTER_SET_NAME`,`COLLATION_NAME`,`COLUMN_TYPE`,`COLUMN_DEFAULT`,`COLUMN_COMMENT`,`ORDINAL_POSITION`,`EXTRA` "
                                                  + "from INFORMATION_SCHEMA.COLUMNS";
    protected MyMetaProviderUtils providerUtils = new MyMetaProviderUtils();

    public MyMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement("select version() from dual"); ResultSet resultSet = ps.executeQuery()) {
            return ((ValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "select SCHEMA_NAME,DEFAULT_COLLATION_NAME,DEFAULT_CHARACTER_SET_NAME from INFORMATION_SCHEMA.SCHEMATA order by SCHEMA_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return this.providerUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String schema) throws SQLException {
        String sql = "select SCHEMA_NAME,DEFAULT_COLLATION_NAME,DEFAULT_CHARACTER_SET_NAME from INFORMATION_SCHEMA.SCHEMATA where SCHEMA_NAME = ? limit 1";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> values = this.providerUtils.convertSchema(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public RdbTrigger loadTrigger(String catalog, String schema, String leafName) throws SQLException {
        String sql = "SELECT TRIGGER_SCHEMA,TRIGGER_NAME,EVENT_MANIPULATION,EVENT_OBJECT_TABLE,ACTION_STATEMENT,ACTION_TIMING,CREATED,DEFINER FROM information_schema.triggers "
                     + "WHERE TRIGGER_SCHEMA = ? and trigger_name= ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbTrigger> values = this.providerUtils.convertTrigger(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }

    public List<Value> selectTables(String schema) throws SQLException {
        return this.selectByConditions(schema, "TABLE_TYPE='BASE TABLE'");
    }

    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "(TABLE_TYPE ='VIEW' or TABLE_TYPE ='SYSTEM VIEW')");
    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String sql = "select TABLE_NAME,TABLE_TYPE,TABLE_COLLATION,TABLE_COMMENT,ENGINE from INFORMATION_SCHEMA.TABLES " //
                     + "where TABLE_SCHEMA = ? and " + conditions + " order by TABLE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    /**
     * the routines table from MySQL store the information about procedure and function
     * @param conditions the condition to distinct procedure and function
     * @return routines list include correlate params
     */
    private String conditionsSqlBuilderWithRoutinesName(String conditions) {
        return " (SELECT ROUT.SPECIFIC_NAME,ROUT.ROUTINE_COMMENT, " + //
               " PARAM.PARAMETER_NAME,PARAM.DATA_TYPE,PARAM.CHARACTER_MAXIMUM_LENGTH,PARAM.ORDINAL_POSITION " + //
               " FROM information_schema.ROUTINES AS ROUT " + //
               " LEFT JOIN information_schema.PARAMETERS AS PARAM ON ROUT.SPECIFIC_NAME = PARAM.SPECIFIC_NAME " + //
               " WHERE ROUT.ROUTINE_SCHEMA = ? AND " + conditions + " )" + //
               " UNION " + " (SELECT ROUT.SPECIFIC_NAME,ROUT.ROUTINE_COMMENT, " + //
               " PARAM.PARAMETER_NAME,PARAM.DATA_TYPE,PARAM.CHARACTER_MAXIMUM_LENGTH,PARAM.ORDINAL_POSITION " + //
               " FROM information_schema.ROUTINES AS ROUT " + //
               " RIGHT JOIN information_schema.PARAMETERS AS PARAM ON ROUT.SPECIFIC_NAME = PARAM.SPECIFIC_NAME " + //
               " WHERE ROUT.ROUTINE_SCHEMA = ? AND " + conditions + " )" + //
               " ORDER BY SPECIFIC_NAME ASC, ORDINAL_POSITION ASC ";

    }

    /**
     * select all procedures in specified schema
     * @param schema MySQL database
     * @return procedure list include correlate params
     * @throws SQLException SQLException
     */
    public List<Value> selectProcedures(String schema) throws SQLException {
        String conditions = " ROUT.ROUTINE_TYPE = 'PROCEDURE' ";
        String sql = this.conditionsSqlBuilderWithRoutinesName(conditions);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertProcedureName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    /**
     * select all functions in specified schema
     * @param schema MySQL database
     * @return function list include correlate params
     * @throws SQLException SQLException
     */
    public List<Value> selectFunctions(String schema) throws SQLException {
        String conditions = " ROUT.ROUTINE_TYPE = 'FUNCTION' ";
        String sql = this.conditionsSqlBuilderWithRoutinesName(conditions);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas;
                if (schema.equals("mysql")) {
                    schemas = this.providerUtils.convertFunctionNameWithBuiltIn(rs);
                } else {
                    schemas = this.providerUtils.convertFunctionName(rs);
                }

                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    /**
     * select all triggers in specified schema
     * @param schema MySQL database
     * @return trigger list include correlate params
     * @throws SQLException SQLException
     */
    public List<Value> selectTriggers(String schema) throws SQLException {
        String sql = "SELECT TRIGGER_NAME, EVENT_MANIPULATION, ACTION_TIMING, CHARACTER_SET_CLIENT, COLLATION_CONNECTION " + " FROM information_schema.TRIGGERS "
                     + " WHERE TRIGGER_SCHEMA = ? ORDER BY TRIGGER_NAME ASC ";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTriggerName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
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

    protected List<RdbProcedure> fetchProcedureByPart(Connection conn, String catalog, String schema, List<String> procs) throws SQLException {
        String sql = "SELECT SPECIFIC_NAME, ROUTINE_DEFINITION,ROUTINE_CATALOG, ROUTINE_SCHEMA, ROUTINE_TYPE,SQL_DATA_ACCESS,IS_DETERMINISTIC,CREATED,LAST_ALTERED,DEFINER,SECURITY_TYPE"
                     + " FROM information_schema.ROUTINES WHERE ROUTINE_SCHEMA = ? AND ROUTINE_TYPE = 'PROCEDURE' AND SPECIFIC_NAME IN " + buildWhereIn(procs);
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
        String sql = "SELECT SPECIFIC_NAME, ROUTINE_DEFINITION, ROUTINE_CATALOG, ROUTINE_SCHEMA, ROUTINE_TYPE,SQL_DATA_ACCESS,IS_DETERMINISTIC,CREATED,LAST_ALTERED,DEFINER,SECURITY_TYPE FROM information_schema.ROUTINES WHERE ROUTINE_SCHEMA = ? AND ROUTINE_TYPE = 'FUNCTION' AND SPECIFIC_NAME IN "
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

    protected List<RdbParam> fetchParams(Connection conn, String catalog, String schema, List<String> procs, UmiTypes umiTypes) throws SQLException {
        String routineType;
        if (umiTypes.equals(UmiTypes.Procedure)) {
            routineType = " AND ROUTINE_TYPE = 'PROCEDURE'";
        } else if (umiTypes.equals(UmiTypes.Function)) {
            routineType = " AND ROUTINE_TYPE = 'FUNCTION'";
        } else {
            routineType = " ";
        }

        String sql = "SELECT SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME, ORDINAL_POSITION, PARAMETER_NAME, DATA_TYPE,NUMERIC_SCALE,NUMERIC_PRECISION,DATETIME_PRECISION, CHARACTER_MAXIMUM_LENGTH,PARAMETER_MODE FROM information_schema.PARAMETERS "
                     + "WHERE SPECIFIC_SCHEMA = ? " + routineType + " AND SPECIFIC_NAME IN " + buildWhereIn(procs) + " ORDER BY ORDINAL_POSITION";
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

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return fetchByPart(conn, schema, tabs, "TABLE_TYPE='BASE TABLE'");
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select t.`TABLE_CATALOG` TABLE_CATALOG,t.`TABLE_SCHEMA` TABLE_SCHEMA,t.`TABLE_NAME` TABLE_NAME,t.`TABLE_TYPE` TABLE_TYPE,t.`TABLE_COLLATION` TABLE_COLLATION,t.`CREATE_TIME` CREATE_TIME,v.`CHECK_OPTION` CHECK_OPTION,"
                     + "t.`UPDATE_TIME` UPDATE_TIME,t.`TABLE_COMMENT` TABLE_COMMENT,t.`ENGINE` ENGINE,t.`ROW_FORMAT` ROW_FORMAT,t.`AVG_ROW_LENGTH` AVG_ROW_LENGTH,t.`CREATE_OPTIONS` CREATE_OPTIONS,t.`AUTO_INCREMENT` AUTO_INCREMENT,"
                     + "v.VIEW_DEFINITION VIEW_DEFINITION,v.DEFINER DEFINER,v.SECURITY_TYPE SECURITY_TYPE,v.CHARACTER_SET_CLIENT CHARACTER_SET_CLIENT,v.COLLATION_CONNECTION COLLATION_CONNECTION,v.IS_UPDATABLE IS_UPDATABLE "
                     + "from INFORMATION_SCHEMA.TABLES t left join INFORMATION_SCHEMA.VIEWS v on t.TABLE_NAME = v.TABLE_NAME where t.TABLE_SCHEMA = ? and t.TABLE_TYPE in ('VIEW','SYSTEM VIEW')"
                     + " and t.TABLE_NAME in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertView(rs);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    protected List<RdbTable> convertView(ResultSet rs) throws SQLException {
        return this.providerUtils.convertView(rs);
    }

    protected List<RdbTable> fetchByPart(Connection conn, String schema, List<String> tabs, String conditions) throws SQLException {
        String sql = TABLE + " where TABLE_SCHEMA = ? and " + conditions + " and TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return this.convertTables(rs);
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select CONSTRAINT_TYPE,S.TABLE_SCHEMA,S.TABLE_NAME,S.INDEX_COMMENT,S.COMMENT,S.NON_UNIQUE,S.SEQ_IN_INDEX,S.COLLATION,INDEX_NAME,COLUMN_NAME,INDEX_TYPE,SUB_PART "
                     + "FROM INFORMATION_SCHEMA.STATISTICS S "//
                     + "left join INFORMATION_SCHEMA.TABLE_CONSTRAINTS CON on S.TABLE_SCHEMA = CON.TABLE_SCHEMA and S.TABLE_NAME = CON.TABLE_NAME and S.INDEX_NAME = CON.CONSTRAINT_NAME "//
                     + "where (CONSTRAINT_TYPE not in ('PRIMARY KEY', 'UNIQUE', 'FOREIGN KEY') or CONSTRAINT_TYPE is null) "//
                     + "    and S.NON_UNIQUE = 1 and S.TABLE_SCHEMA = ? and S.TABLE_NAME in " + buildWhereIn(tabs) + " order by SEQ_IN_INDEX asc";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbIndex> idxList = this.providerUtils.convertIndex(rs);
                Map<String, Map<String, RdbIndex>> idxMap = this.providerUtils.convertIndexList(idxList);

                return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select C.CONSTRAINT_SCHEMA,C.CONSTRAINT_NAME,C.TABLE_SCHEMA,C.TABLE_NAME,C.COLUMN_NAME,C.REFERENCED_TABLE_SCHEMA,R.REFERENCED_TABLE_NAME,C.REFERENCED_COLUMN_NAME,R.UPDATE_RULE,R.DELETE_RULE "
                     + "from INFORMATION_SCHEMA.KEY_COLUMN_USAGE C " //
                     + "left join INFORMATION_SCHEMA.TABLE_CONSTRAINTS T "
                     + "    on C.CONSTRAINT_CATALOG = T.CONSTRAINT_CATALOG and C.CONSTRAINT_SCHEMA = T.CONSTRAINT_SCHEMA and C.CONSTRAINT_NAME = T.CONSTRAINT_NAME and C.TABLE_SCHEMA = T.TABLE_SCHEMA and C.TABLE_NAME = T.TABLE_NAME "
                     + "left join INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R "
                     + "    on C.CONSTRAINT_CATALOG = R.CONSTRAINT_CATALOG and C.CONSTRAINT_SCHEMA = R.CONSTRAINT_SCHEMA and C.CONSTRAINT_NAME = R.CONSTRAINT_NAME and C.TABLE_SCHEMA = T.TABLE_SCHEMA and C.TABLE_NAME = R.TABLE_NAME "
                     + "where T.TABLE_SCHEMA = ? and T.TABLE_NAME in " + buildWhereIn(tabs) + " and T.CONSTRAINT_TYPE = 'FOREIGN KEY' "
                     + "order by C.POSITION_IN_UNIQUE_CONSTRAINT asc";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbForeignKey> fkList = this.providerUtils.convertForeignKey(rs);
                Map<String, Map<String, RdbForeignKey>> fkMap = this.providerUtils.convertForeignKeyList(fkList);

                return fkMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select S.TABLE_SCHEMA,S.TABLE_NAME,S.COMMENT,S.INDEX_NAME,S.INDEX_TYPE,S.SUB_PART,S.COLLATION,T.CONSTRAINT_NAME,S.COLUMN_NAME,T.CONSTRAINT_TYPE " //
                     + "from INFORMATION_SCHEMA.STATISTICS S " //
                     + "left join INFORMATION_SCHEMA.TABLE_CONSTRAINTS  T " //
                     + "on T.CONSTRAINT_CATALOG = S.TABLE_CATALOG and T.TABLE_SCHEMA = S.TABLE_SCHEMA and T.TABLE_NAME = S.TABLE_NAME and T.CONSTRAINT_NAME = S.INDEX_NAME and T.CONSTRAINT_SCHEMA = S.INDEX_SCHEMA " //
                     + "where S.TABLE_SCHEMA = ? and S.TABLE_NAME = " + buildWhereIn(tabs) + " and T.CONSTRAINT_TYPE IN ('PRIMARY KEY','UNIQUE') "//
                     + "order by S.SEQ_IN_INDEX asc";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String type = rs.getString("CONSTRAINT_TYPE");
                    String table = rs.getString("TABLE_NAME");
                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    if (type.equals("PRIMARY KEY")) {
                        this.providerUtils.mapToPkExt(rs, constraints);
                    } else if (type.equals("UNIQUE")) {
                        this.providerUtils.mapToUkExt(rs, constraints);
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint type:" + type);
                    }
                }
                return (Map<String, Map<String, UmiConstraint>>) CollectionUtils.decorateCaseSensitive(pkUkMap);
            }
        }
    }

    @Override
    protected Map<String, RdbPartition> fetchTablePartition(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select * " + "from INFORMATION_SCHEMA.PARTITIONS where PARTITION_NAME is not NULL and TABLE_SCHEMA = ? and TABLE_NAME in " + buildWhereIn(tabs)
                     + " order by PARTITION_ORDINAL_POSITION,SUBPARTITION_ORDINAL_POSITION ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return convertPartitions(rs);
            }
        }
    }

    private Map<String, RdbPartition> convertPartitions(ResultSet rs) throws SQLException {
        return this.providerUtils.convertPartitions(rs);
    }

    @Override
    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return fetchByPart(conn, schema, tabs, " 1=1 ");
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = COLUMNS + " where TABLE_SCHEMA = ? and TABLE_NAME in " + buildWhereIn(tabs) + " order by ORDINAL_POSITION asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> columns = convertColumns(rs);

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    protected List<RdbTable> convertTables(ResultSet rs) throws SQLException {
        return this.providerUtils.convertTable(rs);
    }

    protected List<RdbColumn> convertColumns(ResultSet rs) throws SQLException {
        return this.providerUtils.convertColumn(rs);
    }

    protected List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        return this.providerUtils.convertProcedures(rs);
    }

    protected List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        return this.providerUtils.convertFunctions(rs);
    }

    protected List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        return this.providerUtils.convertParams(rs);
    }
}
