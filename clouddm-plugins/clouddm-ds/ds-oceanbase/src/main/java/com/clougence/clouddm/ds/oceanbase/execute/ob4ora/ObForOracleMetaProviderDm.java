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
package com.clougence.clouddm.ds.oceanbase.execute.ob4ora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.oracle.execute.OraMetaProviderDm;
import com.clougence.clouddm.ds.oracle.execute.OraMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

public class ObForOracleMetaProviderDm extends OraMetaProviderDm {

    public ObForOracleMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            try (PreparedStatement ps = conn.prepareStatement("select BANNER from v$version"); ResultSet resultSet = ps.executeQuery()) {
                return ((ValueRowMapper<String>) (rs, columnType, typeName, className) -> rs.getString(1)).mapRow(resultSet);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select t.OWNER,t.TABLE_NAME,'TABLE' AS TABLE_TYPE,c.COMMENTS from ALL_TABLES t left join ALL_TAB_COMMENTS c " +//
                     "on t.OWNER=c.OWNER and t.TABLE_NAME = c.TABLE_NAME  where t.OWNER = ? and t.TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }
            try (ResultSet resultSet = ps.executeQuery()) {
                return ObForOracleMetaProviderUtils.convertTable(resultSet);
            }
        }
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select * from ALL_TAB_COLUMNS where OWNER = ? and TABLE_NAME in " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }
            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbColumn> cols = ObForOracleMetaProviderUtils.convertColumn(resultSet);
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

    public List<Value> selectProcedures(String schema) throws SQLException {
        String sql = "SELECT OBJ.OBJECT_NAME OBJECT_NAME,ARG.ARGUMENT_NAME ARGUMENT_NAME,"
                     + "NULL LENGTH,ARG.POSITION POSITION,ARG.DATA_TYPE DATA_TYPE,OBJ.STATUS STATUS FROM ALL_OBJECTS OBJ " + "LEFT JOIN ALL_ARGUMENTS ARG "
                     + "ON OBJ.OBJECT_ID = ARG.OBJECT_ID " + "WHERE OBJ.OWNER = ? AND OBJ.OBJECT_TYPE = 'PROCEDURE' ORDER BY OBJECT_NAME ASC";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertProcedureName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String sql = "SELECT OBJ.OBJECT_NAME OBJECT_NAME,ARG.ARGUMENT_NAME ARGUMENT_NAME,"
                     + "null LENGTH,ARG.POSITION POSITION,ARG.DATA_TYPE DATA_TYPE,OBJ.STATUS STATUS FROM ALL_OBJECTS OBJ " + "LEFT JOIN ALL_ARGUMENTS ARG "
                     + "ON OBJ.OBJECT_ID = ARG.OBJECT_ID " + "WHERE OBJ.OWNER = ? AND OBJ.OBJECT_TYPE = 'FUNCTION' ORDER BY OBJECT_NAME ASC";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);

            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = OraMetaProviderUtils.convertFunctionName(rs);
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    protected List<RdbParam> fetchParams(Connection conn, String catalog, String schema, List<String> procs, UmiTypes umiTypes) throws SQLException {
        String sql = "select OWNER,POSITION,OBJECT_NAME,ARGUMENT_NAME,DATA_TYPE,NULL DATA_LENGTH  from ALL_ARGUMENTS where OWNER = ?  and OBJECT_NAME in " + buildWhereIn(procs)
                     + " order by POSITION";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(procs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return OraMetaProviderUtils.convertParams(rs);
            }
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "select USERNAME from SYS.ALL_USERS order by USERNAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return convertSchema(rs);
            }
        }
    }

    private List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            String name = rs.getString("USERNAME");
            if ("PUBLIC".equalsIgnoreCase(name)) {
                continue;
            }
            schema.setValue(name);

            schema.setUmiType(UmiTypes.Schema);

            result.add(schema);
        }
        return result;
    }
}
