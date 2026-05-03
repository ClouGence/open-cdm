package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2Db2SchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.Db2));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.Db2));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.Db2).to(new PostgresTableExporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, Db2Types.INTEGER);
        typeMapping.mapping(PostgresTypes.SERIAL, Db2Types.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, Db2Types.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, Db2Types.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, Db2Types.INTEGER);
        typeMapping.mapping(PostgresTypes.BIGINT, Db2Types.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, Db2Types.BIGINT);
        typeMapping.mapping(PostgresTypes.NUMERIC, Db2Types.DECIMAL);
        typeMapping.mapping(PostgresTypes.REAL, Db2Types.DECIMAL);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, Db2Types.DOUBLE);

        typeMapping.mapping(PostgresTypes.MONEY, Db2Types.DECIMAL);

        typeMapping.mapping(PostgresTypes.CHARACTER, Db2Types.VARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, Db2Types.VARCHAR);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, Db2Types.VARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, null);
        typeMapping.mapping(PostgresTypes.NAME, null);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, Db2Types.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, Db2Types.TIMESTAMP);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, Db2Types.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, Db2Types.TIME);
        typeMapping.mapping(PostgresTypes.DATE, Db2Types.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, Db2Types.TIMESTAMP);

        typeMapping.mapping(PostgresTypes.BIT, Db2Types.BINARY);
        typeMapping.mapping(PostgresTypes.BIT_VARYING, Db2Types.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, Db2Types.SMALLINT);

        typeMapping.mapping(PostgresTypes.XML, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.BYTEA, Db2Types.BINARY);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.LINE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.LSEG, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.BOX, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.PATH, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.POLYGON, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.CIRCLE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.GEOMETRY, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, Db2Types.CLOB);

        typeMapping.mapping(PostgresTypes.CIDR, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.INET, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.MACADDR, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.MACADDR8, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.HSTORE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.CITEXT, Db2Types.CLOB);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, Db2Types.VARCHAR);

        typeMapping.mapping(PostgresTypes.JSON, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.JSONB, Db2Types.CLOB);

        typeMapping.mapping(PostgresTypes.INT4RANGE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.INT8RANGE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.NUMRANGE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.TSRANGE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, Db2Types.CLOB);
        typeMapping.mapping(PostgresTypes.DATERANGE, Db2Types.CLOB);

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
