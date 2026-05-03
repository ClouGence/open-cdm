package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2StartRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.StarRocks));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.StarRocks).to(new PostgresTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, StarRocksTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, StarRocksTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, StarRocksTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, StarRocksTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, StarRocksTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, null);
        typeMapping.mapping(PostgresTypes.NUMERIC, StarRocksTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, StarRocksTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, StarRocksTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, StarRocksTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, StarRocksTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, StarRocksTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.NAME, StarRocksTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, StarRocksTypes.STRING);

        typeMapping.mapping(PostgresTypes.BIT, null);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, null);
        typeMapping.mapping(PostgresTypes.BOOLEAN, StarRocksTypes.BOOLEAN);

        typeMapping.mapping(PostgresTypes.XML, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.BYTEA, null);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.LINE, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.LSEG, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.BOX, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.PATH, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.POLYGON, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.CIRCLE, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.GEOMETRY, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, StarRocksTypes.STRING);

        typeMapping.mapping(PostgresTypes.CIDR, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.INET, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.MACADDR, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.MACADDR8, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.HSTORE, StarRocksTypes.STRING);
        typeMapping.mapping(PostgresTypes.CITEXT, StarRocksTypes.STRING);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, StarRocksTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, StarRocksTypes.JSON);
        typeMapping.mapping(PostgresTypes.JSONB, StarRocksTypes.JSON);

        typeMapping.mapping(PostgresTypes.INT4RANGE, null);
        typeMapping.mapping(PostgresTypes.INT8RANGE, null);
        typeMapping.mapping(PostgresTypes.NUMRANGE, null);
        typeMapping.mapping(PostgresTypes.TSRANGE, null);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, null);
        typeMapping.mapping(PostgresTypes.DATERANGE, null);

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
