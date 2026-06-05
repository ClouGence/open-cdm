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
package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.OracleTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2OracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.Oracle));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.Oracle));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.Oracle).to(new PostgresTableExporter(), new OracleTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.SERIAL, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, OracleSqlTypes.NUMBER_BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.INTEGER, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.BIGINT, OracleSqlTypes.NUMBER_BIGINT);

        typeMapping.mapping(PostgresTypes.OID, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.NUMERIC, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(PostgresTypes.REAL, OracleSqlTypes.BINARY_FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, OracleSqlTypes.BINARY_DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, OracleSqlTypes.NUMBER_DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, OracleSqlTypes.NVARCHAR2);
        typeMapping.mapping(PostgresTypes.BPCHAR, OracleSqlTypes.NVARCHAR2);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, OracleSqlTypes.NVARCHAR2);
        typeMapping.mapping(PostgresTypes.TEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.NAME, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(PostgresTypes.DATE, OracleSqlTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, OracleSqlTypes.DATE);

        typeMapping.mapping(PostgresTypes.BIT, OracleSqlTypes.BLOB);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, OracleSqlTypes.BLOB);
        typeMapping.mapping(PostgresTypes.BOOLEAN, OracleSqlTypes.NUMBER_BIGINT);

        typeMapping.mapping(PostgresTypes.XML, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.BYTEA, OracleSqlTypes.BLOB);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.LINE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.LSEG, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.BOX, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.PATH, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.POLYGON, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.CIRCLE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.GEOMETRY, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.CIDR, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.INET, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.MACADDR, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.MACADDR8, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.HSTORE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.CITEXT, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.TSVECTOR, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.TSQUERY, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.UUID, OracleSqlTypes.VARCHAR2);

        typeMapping.mapping(PostgresTypes.JSON, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.JSONB, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.INT4RANGE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.INT8RANGE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.NUMRANGE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.TSRANGE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.DATERANGE, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.PG_LSN, OracleSqlTypes.CLOB);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE, OracleSqlTypes.CLOB);

        typeMapping.mapping(PostgresTypes.SMALLINT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INTEGER_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BIGINT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.OID_ARRAY, null);
        typeMapping.mapping(PostgresTypes.NUMERIC_ARRAY, null);
        typeMapping.mapping(PostgresTypes.REAL_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION_ARRAY, null);

        typeMapping.mapping(PostgresTypes.MONEY_ARRAY, null);

        typeMapping.mapping(PostgresTypes.CHARACTER_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BPCHAR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TEXT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.NAME_ARRAY, null);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DATE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INTERVAL_ARRAY, null);

        typeMapping.mapping(PostgresTypes.BIT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BIT_VARYING_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BOOLEAN_ARRAY, null);

        typeMapping.mapping(PostgresTypes.XML_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BYTEA_ARRAY, null);
        typeMapping.mapping(PostgresTypes.REF_CURSOR_ARRAY, null);

        typeMapping.mapping(PostgresTypes.POINT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.LINE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.LSEG_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BOX_ARRAY, null);
        typeMapping.mapping(PostgresTypes.PATH_ARRAY, null);
        typeMapping.mapping(PostgresTypes.POLYGON_ARRAY, null);
        typeMapping.mapping(PostgresTypes.CIRCLE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.GEOMETRY_ARRAY, null);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY_ARRAY, null);

        typeMapping.mapping(PostgresTypes.CIDR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INET_ARRAY, null);
        typeMapping.mapping(PostgresTypes.MACADDR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.MACADDR8_ARRAY, null);
        typeMapping.mapping(PostgresTypes.HSTORE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.CITEXT_ARRAY, null);

        typeMapping.mapping(PostgresTypes.TSVECTOR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSQUERY_ARRAY, null);

        typeMapping.mapping(PostgresTypes.UUID_ARRAY, null);

        typeMapping.mapping(PostgresTypes.JSON_ARRAY, null);
        typeMapping.mapping(PostgresTypes.JSONB_ARRAY, null);

        typeMapping.mapping(PostgresTypes.INT4RANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INT8RANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.NUMRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSTZRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DATERANGE_ARRAY, null);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.PG_LSN_ARRAY, null);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE_ARRAY, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {
        //
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
