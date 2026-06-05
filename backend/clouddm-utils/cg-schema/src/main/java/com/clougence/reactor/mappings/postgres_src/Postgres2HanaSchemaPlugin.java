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

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.HanaTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2HanaSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.Hana));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.Hana));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.Hana).to(new PostgresTableExporter(), new HanaTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, HanaTypes.INTEGER);
        typeMapping.mapping(PostgresTypes.SERIAL, HanaTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, HanaTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, HanaTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, HanaTypes.INTEGER);
        typeMapping.mapping(PostgresTypes.BIGINT, HanaTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.OID, HanaTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.NUMERIC, HanaTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, HanaTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, HanaTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, HanaTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, HanaTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, HanaTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, HanaTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, HanaTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, HanaTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, HanaTypes.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, HanaTypes.TIME);
        typeMapping.mapping(PostgresTypes.DATE, HanaTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, HanaTypes.TIMESTAMP);

        typeMapping.mapping(PostgresTypes.BIT, HanaTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, HanaTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BOOLEAN, HanaTypes.BOOLEAN);

        typeMapping.mapping(PostgresTypes.XML, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BYTEA, HanaTypes.BINARY);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, HanaTypes.TEXT); // ClickHouseTypes.Point is experimental
        typeMapping.mapping(PostgresTypes.LINE, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LSEG, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BOX, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.PATH, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.POLYGON, HanaTypes.TEXT); // ClickHouseTypes.Polygon is experimental
        typeMapping.mapping(PostgresTypes.CIRCLE, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOMETRY, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, HanaTypes.TEXT);

        typeMapping.mapping(PostgresTypes.CIDR, HanaTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.INET, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR8, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.HSTORE, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CITEXT, HanaTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, HanaTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, HanaTypes.TEXT);
        typeMapping.mapping(PostgresTypes.JSONB, HanaTypes.TEXT);

        typeMapping.mapping(PostgresTypes.INT4RANGE, HanaTypes.ARRAY);
        typeMapping.mapping(PostgresTypes.INT8RANGE, HanaTypes.ARRAY);
        typeMapping.mapping(PostgresTypes.NUMRANGE, HanaTypes.ARRAY);
        typeMapping.mapping(PostgresTypes.TSRANGE, HanaTypes.ARRAY);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, HanaTypes.ARRAY);
        typeMapping.mapping(PostgresTypes.DATERANGE, HanaTypes.ARRAY);

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
