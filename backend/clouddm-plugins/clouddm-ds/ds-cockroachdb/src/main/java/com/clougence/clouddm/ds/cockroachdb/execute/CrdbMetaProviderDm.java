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
package com.clougence.clouddm.ds.cockroachdb.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.jdbc.mapper.ValueRowMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * CockroachDB does not implement PostgreSQL information_schema helper functions such as
 * {@code _pg_char_octet_length}. Column metadata therefore uses standard {@code information_schema.columns}
 * fields together with pg_catalog.
 */
@Slf4j
public class CrdbMetaProviderDm extends PgMetaProviderDm {

    private static final String QUERY_SERVER_VERSION_NUM = "select current_setting('server_version_num')";

    private static final String CRDB_COLUMNS             = "  SELECT c.* FROM (  SELECT  n.nspname AS SCHEMA_NAME,\n"
                                                         + "      C.relname AS TABLE_NAME,   A.attname AS COLUMN_NAME,  A.atttypid AS type_oid,\n"
                                                         + "      A.atttypmod AS type_mod,  CASE      WHEN T.typtype = 'd' THEN\n"
                                                         + "         format_type ( T.typbasetype, NULL :: INTEGER ) ELSE format_type ( A.atttypid, NULL :: INTEGER ) \n"
                                                         + "      END AS type_name,   ( T.typelem <> 0 :: oid AND T.typlen = '-1' :: INTEGER ) AS type_is_array,\n"
                                                         + "         T.typbasetype,    T.typtype,   A.attnotnull \n"
                                                         + "         OR ( T.typtype = 'd' AND T.typnotnull ) AS not_null,A.attndims, A.attlen,\n"
                                                         + "         sch.character_maximum_length AS character_maximum_length,\n"
                                                         + "         sch.character_octet_length AS character_octet_length,\n"
                                                         + "         sch.numeric_precision AS numeric_precision,\n"
                                                         + "         sch.numeric_precision_radix AS numeric_precision_radix,\n"
                                                         + "         sch.numeric_scale AS numeric_scale,\n"
                                                         + "         sch.datetime_precision AS datetime_precision,\n"
                                                         + "         T.typtypmod,    ROW_NUMBER ( ) OVER ( PARTITION BY A.attrelid ORDER BY A.attnum ) AS attnum,\n"
                                                         + "         dsc.description AS comments,   sch.column_default AS column_default,\n"
                                                         + "         sch.identity_generation,   sch.identity_increment,   sch.identity_minimum,\n"
                                                         + "         sch.identity_maximum,     sch.identity_start,     sch.identity_cycle,\n"
                                                         + "         sch.generation_expression, co.collname AS COLLATION_NAME,\n"
                                                         + "         nc.nspname AS collation_schema_name     FROM    pg_catalog.pg_namespace n\n"
                                                         + "         JOIN pg_catalog.pg_class C ON ( C.relnamespace = n.oid )\n"
                                                         + "         JOIN pg_catalog.pg_attribute A ON ( A.attrelid = C.oid )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_type T ON ( A.atttypid = T.oid )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_attrdef def ON ( A.attrelid = def.adrelid AND A.attnum = def.adnum )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_description dsc ON ( C.oid = dsc.objoid AND A.attnum = dsc.objsubid )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_class dc ON ( dc.oid = dsc.classoid AND dc.relname = 'pg_class' )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_namespace dn ON ( dc.relnamespace = dn.oid AND dn.nspname = 'pg_catalog' )\n"
                                                         + "         LEFT JOIN information_schema.COLUMNS sch ON ( sch.TABLE_NAME = C.relname AND sch.COLUMN_NAME = A.attname AND sch.table_schema = n.nspname ) "
                                                         + "         LEFT JOIN pg_catalog.pg_collation co ON ( A.attcollation = co.oid )\n"
                                                         + "         LEFT JOIN pg_catalog.pg_namespace nc ON ( co.collnamespace = nc.oid )   WHERE\n"
                                                         + "         C.relkind IN ( 'r', 'p', 'v', 'f', 'm' )     AND A.attnum > 0 \n"
                                                         + "         AND NOT A.attisdropped    AND n.nspname =  ###SCHEMA_NAME### AND C.relname IN ###TABLE_NAME### "
                                                         + "      ) C   WHERE   TRUE   ORDER BY     SCHEMA_NAME,   C.TABLE_NAME,    attnum;";

    public CrdbMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
        String sql = CRDB_COLUMNS.replace("###SCHEMA_NAME###", "?").replace("###TABLE_NAME###", buildWhereIn(tabs));

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                long serverVersionNumber = readServerVersionNumber(conn);
                List<RdbColumn> columns = this.convertColumn(resultSet, serverVersionNumber, false);
                if (columns.isEmpty()) {
                    return result;
                }

                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
            }
        }

        return result;
    }

    private long readServerVersionNumber(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(QUERY_SERVER_VERSION_NUM); ResultSet resultSet = ps.executeQuery()) {
            String serverVersion = ((ValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
            return (Long) ConverterUtils.convert(serverVersion, Long.class);
        }
    }
}
