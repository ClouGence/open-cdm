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

import com.clougence.adapter.mysql.MySQLMainVersion;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.MySQL));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.MySQL).to(new PostgresTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, MySQLTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, MySQLTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, MySQLTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, MySQLTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, MySQLTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC, MySQLTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, MySQLTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, MySQLTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, MySQLTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, MySQLTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, MySQLTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, MySQLTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, MySQLTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, MySQLTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, MySQLTypes.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, MySQLTypes.TIME);
        typeMapping.mapping(PostgresTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, MySQLTypes.TIMESTAMP);

        typeMapping.mapping(PostgresTypes.BIT, MySQLTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, MySQLTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, MySQLTypes.TINYINT);

        typeMapping.mapping(PostgresTypes.XML, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BYTEA, MySQLTypes.LONGBLOB);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LINE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LSEG, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BOX, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.PATH, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.POLYGON, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CIRCLE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOMETRY, MySQLTypes.GEOMETRY);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, MySQLTypes.TEXT);

        typeMapping.mapping(PostgresTypes.CIDR, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INET, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR8, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.HSTORE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CITEXT, MySQLTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, MySQLTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, MySQLTypes.JSON);
        typeMapping.mapping(PostgresTypes.JSONB, MySQLTypes.JSON);

        typeMapping.mapping(PostgresTypes.INT4RANGE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INT8RANGE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMRANGE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSRANGE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, MySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.DATERANGE, MySQLTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT, null);
        typeMapping.mapping(PostgresTypes.PG_LSN, null);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE, null);

        typeMapping.mapping(PostgresTypes.SMALLINT_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.INTEGER_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.BIGINT_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.OID_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.REAL_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION_ARRAY, MySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.MONEY_ARRAY, MySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.CHARACTER_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.BPCHAR_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.TEXT_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.NAME_ARRAY, MySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DATE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INTERVAL_ARRAY, null);

        typeMapping.mapping(PostgresTypes.BIT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BIT_VARYING_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BOOLEAN_ARRAY, MySQLTypes.LONGTEXT);

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

        typeMapping.mapping(PostgresTypes.JSON_ARRAY, MySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.JSONB_ARRAY, MySQLTypes.LONGTEXT);

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
        typeMapping.mapping(PostgresTypes.JSON, MySQLTypes.LONGTEXT, MySQLMainVersion.MySQL_5_6);
        typeMapping.mapping(PostgresTypes.JSONB, MySQLTypes.LONGTEXT, MySQLMainVersion.MySQL_5_6);
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
