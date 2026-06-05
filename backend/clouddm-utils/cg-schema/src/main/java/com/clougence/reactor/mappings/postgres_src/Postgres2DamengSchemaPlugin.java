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

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.Dameng));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.Dameng).to(new PostgresTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, DmSqlTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, DmSqlTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, DmSqlTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, DmSqlTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, DmSqlTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC, DmSqlTypes.NUMERIC);
        typeMapping.mapping(PostgresTypes.REAL, DmSqlTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, DmSqlTypes.FLOAT);

        typeMapping.mapping(PostgresTypes.MONEY, DmSqlTypes.FLOAT);

        typeMapping.mapping(PostgresTypes.CHARACTER, DmSqlTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, DmSqlTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, DmSqlTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, DmSqlTypes.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, DmSqlTypes.TIME);
        typeMapping.mapping(PostgresTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, DmSqlTypes.TIMESTAMP);

        typeMapping.mapping(PostgresTypes.BIT, DmSqlTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, DmSqlTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, DmSqlTypes.TINYINT);

        typeMapping.mapping(PostgresTypes.XML, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BYTEA, DmSqlTypes.BINARY);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LINE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LSEG, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BOX, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.PATH, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.POLYGON, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CIRCLE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOMETRY, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, DmSqlTypes.TEXT);

        typeMapping.mapping(PostgresTypes.CIDR, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INET, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR8, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.HSTORE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CITEXT, DmSqlTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, DmSqlTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.JSONB, DmSqlTypes.TEXT);

        typeMapping.mapping(PostgresTypes.INT4RANGE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INT8RANGE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMRANGE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSRANGE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, DmSqlTypes.TEXT);
        typeMapping.mapping(PostgresTypes.DATERANGE, DmSqlTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT, null);
        typeMapping.mapping(PostgresTypes.PG_LSN, null);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE, null);

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

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
