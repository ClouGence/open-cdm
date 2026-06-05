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

import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @author wanshao create time is 2022/2/15
 **/
public class Postgres2OceanBaseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.OceanBase));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.OceanBase));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.OceanBase).to(new PostgresTableExporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, ObForMySQLTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, ObForMySQLTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, ObForMySQLTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, ObForMySQLTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, ObForMySQLTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, ObForMySQLTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, null);
        typeMapping.mapping(PostgresTypes.NUMERIC, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, ObForMySQLTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, ObForMySQLTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, ObForMySQLTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, null);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, ObForMySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, null);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.DATE, ObForMySQLTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, ObForMySQLTypes.DATE);

        typeMapping.mapping(PostgresTypes.BIT, ObForMySQLTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, ObForMySQLTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, ObForMySQLTypes.SMALLINT);

        typeMapping.mapping(PostgresTypes.XML, ObForMySQLTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BYTEA, ObForMySQLTypes.LONGBLOB);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, null);
        typeMapping.mapping(PostgresTypes.LINE, null);
        typeMapping.mapping(PostgresTypes.LSEG, null);
        typeMapping.mapping(PostgresTypes.BOX, null);
        typeMapping.mapping(PostgresTypes.PATH, null);
        typeMapping.mapping(PostgresTypes.POLYGON, null);
        typeMapping.mapping(PostgresTypes.CIRCLE, null);
        typeMapping.mapping(PostgresTypes.GEOMETRY, null);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, null);

        typeMapping.mapping(PostgresTypes.CIDR, null);
        typeMapping.mapping(PostgresTypes.INET, null);
        typeMapping.mapping(PostgresTypes.MACADDR, null);
        typeMapping.mapping(PostgresTypes.MACADDR8, null);
        typeMapping.mapping(PostgresTypes.HSTORE, null);
        typeMapping.mapping(PostgresTypes.CITEXT, null);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, ObForMySQLTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, ObForMySQLTypes.JSON);
        typeMapping.mapping(PostgresTypes.JSONB, ObForMySQLTypes.JSON);

        typeMapping.mapping(PostgresTypes.INT4RANGE, null);
        typeMapping.mapping(PostgresTypes.INT8RANGE, null);
        typeMapping.mapping(PostgresTypes.NUMRANGE, null);
        typeMapping.mapping(PostgresTypes.TSRANGE, null);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, null);
        typeMapping.mapping(PostgresTypes.DATERANGE, null);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT, null);
        typeMapping.mapping(PostgresTypes.PG_LSN, null);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE, null);

        typeMapping.mapping(PostgresTypes.SMALLINT_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.INTEGER_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.BIGINT_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.OID_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.REAL_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION_ARRAY, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.MONEY_ARRAY, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.CHARACTER_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.BPCHAR_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.TEXT_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.NAME_ARRAY, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DATE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INTERVAL_ARRAY, null);

        typeMapping.mapping(PostgresTypes.BIT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BIT_VARYING_ARRAY, null);
        typeMapping.mapping(PostgresTypes.BOOLEAN_ARRAY, ObForMySQLTypes.LONGTEXT);

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
        typeMapping.mapping(PostgresTypes.CIDR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.GEOMETRY_ARRAY, null);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY_ARRAY, null);

        typeMapping.mapping(PostgresTypes.INET_ARRAY, null);
        typeMapping.mapping(PostgresTypes.MACADDR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.MACADDR8_ARRAY, null);
        typeMapping.mapping(PostgresTypes.HSTORE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.CITEXT_ARRAY, null);

        typeMapping.mapping(PostgresTypes.TSVECTOR_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSQUERY_ARRAY, null);

        typeMapping.mapping(PostgresTypes.UUID_ARRAY, null);

        typeMapping.mapping(PostgresTypes.JSON_ARRAY, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(PostgresTypes.JSONB_ARRAY, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(PostgresTypes.INT4RANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.INT8RANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.NUMRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.TSTZRANGE_ARRAY, null);
        typeMapping.mapping(PostgresTypes.DATERANGE_ARRAY, null);

        typeMapping.mapping(PostgresTypes.TXID_SNAPSHOT_ARRAY, null);
        typeMapping.mapping(PostgresTypes.PG_LSN_ARRAY, null);
        typeMapping.mapping(PostgresTypes.PG_NODE_TREE_ARRAY, null);
    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
