package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.Doris));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.Doris).to(new PostgresTableExporter(), new DorisTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, DorisTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, DorisTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, DorisTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, DorisTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, DorisTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, null);
        typeMapping.mapping(PostgresTypes.NUMERIC, DorisTypes.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, DorisTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, DorisTypes.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, DorisTypes.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, DorisTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, DorisTypes.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.NAME, DorisTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(PostgresTypes.DATE, DorisTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, DorisTypes.STRING);

        typeMapping.mapping(PostgresTypes.BIT, null);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, null);
        typeMapping.mapping(PostgresTypes.BOOLEAN, DorisTypes.BOOLEAN);

        typeMapping.mapping(PostgresTypes.XML, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.BYTEA, null);
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

        typeMapping.mapping(PostgresTypes.CIDR, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.INET, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.MACADDR, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.MACADDR8, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.HSTORE, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.CITEXT, DorisTypes.STRING);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, DorisTypes.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, DorisTypes.STRING);
        typeMapping.mapping(PostgresTypes.JSONB, DorisTypes.STRING);

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
