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

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.TiDB));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.TiDB).to(new PostgresTableExporter(), new TiDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, TiDBTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, TiDBTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, TiDBTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, TiDBTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, TiDBTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC, TiDBTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, TiDBTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, TiDBTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, TiDBTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, TiDBTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, TiDBTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, TiDBTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, TiDBTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, TiDBTypes.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, TiDBTypes.TIME);
        typeMapping.mapping(PostgresTypes.DATE, TiDBTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, TiDBTypes.TIMESTAMP);

        typeMapping.mapping(PostgresTypes.BIT, TiDBTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, TiDBTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, TiDBTypes.SMALLINT);

        typeMapping.mapping(PostgresTypes.XML, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BYTEA, TiDBTypes.BINARY);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LINE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LSEG, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BOX, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.PATH, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.POLYGON, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CIRCLE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOMETRY, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, TiDBTypes.TEXT);

        typeMapping.mapping(PostgresTypes.CIDR, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INET, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR8, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.HSTORE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CITEXT, TiDBTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, TiDBTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, TiDBTypes.JSON);
        typeMapping.mapping(PostgresTypes.JSONB, TiDBTypes.JSON);

        typeMapping.mapping(PostgresTypes.INT4RANGE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INT8RANGE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMRANGE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSRANGE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, TiDBTypes.TEXT);
        typeMapping.mapping(PostgresTypes.DATERANGE, TiDBTypes.TEXT);

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
        funMapping.mapping("current_timestamp", "current_timestamp");
        funMapping.mapping("current_timestamp(1)", "current_timestamp(1)");
        funMapping.mapping("current_timestamp(2)", "current_timestamp(2)");
        funMapping.mapping("current_timestamp(3)", "current_timestamp(3)");
        funMapping.mapping("current_timestamp(4)", "current_timestamp(4)");
        funMapping.mapping("current_timestamp(5)", "current_timestamp(5)");
        funMapping.mapping("current_timestamp(6)", "current_timestamp(6)");
    }
}
