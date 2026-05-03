package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.ClickHouseTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2ClickhouseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.ClickHouse));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.ClickHouse));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.ClickHouse).to(new PostgresTableExporter(), new ClickHouseTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, ClickHouseTypes.Int16);
        typeMapping.mapping(PostgresTypes.SERIAL, ClickHouseTypes.Int32);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, ClickHouseTypes.Int64);

        typeMapping.mapping(PostgresTypes.SMALLINT, ClickHouseTypes.Int32);
        typeMapping.mapping(PostgresTypes.INTEGER, ClickHouseTypes.Int32);
        typeMapping.mapping(PostgresTypes.BIGINT, ClickHouseTypes.Int64);
        typeMapping.mapping(PostgresTypes.OID, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.NUMERIC, ClickHouseTypes.Decimal);
        typeMapping.mapping(PostgresTypes.REAL, ClickHouseTypes.Float64);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, ClickHouseTypes.Float64);

        typeMapping.mapping(PostgresTypes.MONEY, ClickHouseTypes.Float64);

        typeMapping.mapping(PostgresTypes.CHARACTER, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.BPCHAR, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.TEXT, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.NAME, ClickHouseTypes.String);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(PostgresTypes.DATE, ClickHouseTypes.Date);
        typeMapping.mapping(PostgresTypes.INTERVAL, ClickHouseTypes.DateTime64);

        typeMapping.mapping(PostgresTypes.BIT, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.BOOLEAN, ClickHouseTypes.Int16);

        typeMapping.mapping(PostgresTypes.XML, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.BYTEA, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, ClickHouseTypes.String); // ClickHouseTypes.Point is experimental
        typeMapping.mapping(PostgresTypes.LINE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.LSEG, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.BOX, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.PATH, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.POLYGON, ClickHouseTypes.String); // ClickHouseTypes.Polygon is experimental
        typeMapping.mapping(PostgresTypes.CIRCLE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.GEOMETRY, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, ClickHouseTypes.String);

        typeMapping.mapping(PostgresTypes.CIDR, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.INET, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.MACADDR, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.MACADDR8, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.HSTORE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.CITEXT, ClickHouseTypes.String);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, ClickHouseTypes.String);

        typeMapping.mapping(PostgresTypes.JSON, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.JSONB, null);

        typeMapping.mapping(PostgresTypes.INT4RANGE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.INT8RANGE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.NUMRANGE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.TSRANGE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, ClickHouseTypes.String);
        typeMapping.mapping(PostgresTypes.DATERANGE, ClickHouseTypes.String);

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
