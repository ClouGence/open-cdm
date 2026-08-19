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
package com.clougence.clouddm.ds.kingbasees.execute.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlMetaProviderDm;
import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlMetaProviderUtils;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.struts.Value;

public class KingbaseESSQLServerMetaProviderDm extends MsSqlMetaProviderDm {

    public KingbaseESSQLServerMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    protected MainVersion parseMainVersion(String version) {
        return KingbaseESSQLServerMainVersion.parserVersion(version);
    }

    @Override
    protected Map<String, String> fetchServerConfig(Connection connection) {
        return Map.of();
    }

    @Override
    protected String COLUMN(String dbName, MainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.name schema_name, t.name table_name,c.name column_name,c.column_id column_order,");
        sql.append("case t2.name when 'int2' then 'smallint' when 'int4' then 'int' when 'int8' then 'bigint' ");
        sql.append("when 'float4' then 'real' when 'float8' then 'float' when 'bool' then 'bit' ");
        sql.append("when 'bpchar' then 'char' when 'bpcharbyte' then 'char' when 'varcharbyte' then 'varchar' ");
        sql.append("when 'bytea' then 'varbinary' when 'time_stamp' then 'timestamp' else t2.name end column_type, ");
        sql.append("p.value comment,d.column_default default_value, ");
        sql.append("c.user_type_id type_id,c.max_length,c.precision,c.scale,c.collation_name,c.is_nullable,");
        sql.append("c.is_rowguidcol,c.is_identity,c.is_computed,c.is_hidden ");
        sql.append("from " + supplementTable(dbName, "sys", "all_columns") + " c ");
        sql.append("left join " + supplementTable(dbName, "sys", "all_objects") + " t on c.object_id = t.object_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "types") + " t2 on c.user_type_id = t2.user_type_id ");
        sql.append("left join information_schema.columns d on d.table_schema = s.name and d.table_name = t.name and d.column_name = c.name ");
        sql.append("left join " + supplementTable(dbName, "sys", "extended_properties") + " p on p.major_id = t.object_id and c.column_id = p.minor_id ");
        return sql.toString();
    }

    @Override
    protected String VIEW(String dbName, MainVersion mainVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.name schema_name,t.name table_name, t.create_date, t.modify_date, t.type, c.value comment ");
        sql.append("from " + supplementTable(dbName, "sys", "views") + " t ");
        sql.append("left join " + supplementTable(dbName, "sys", "schemas") + " s on t.schema_id = s.schema_id ");
        sql.append("left join " + supplementTable(dbName, "sys", "extended_properties") + " c on c.major_id = t.object_id and c.minor_id = 0 ");
        return sql.toString();
    }

    @Override
    public List<Value> selectViews(String catalog, String schema) throws SQLException {
        String sql = VIEW(catalog, parseMainVersion(getVersion())) + " where s.name = ? order by t.name asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return MsSqlMetaProviderUtils.convertTableName(rs).stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }
}
