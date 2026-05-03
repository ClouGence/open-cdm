package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.SqlServerTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2SqlServerSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.SqlServer));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.SqlServer));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.SqlServer).to(new PostgresTableExporter(), new SqlServerTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(PostgresTypes.SMALLSERIAL, SqlServerTypes.INT);
        typeMapping.mapping(PostgresTypes.SERIAL, SqlServerTypes.BIGINT);
        typeMapping.mapping(PostgresTypes.BIGSERIAL, SqlServerTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.SMALLINT, SqlServerTypes.SMALLINT);
        typeMapping.mapping(PostgresTypes.INTEGER, SqlServerTypes.INT);
        typeMapping.mapping(PostgresTypes.BIGINT, SqlServerTypes.BIGINT);

        typeMapping.mapping(PostgresTypes.OID, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMERIC, SqlServerTypes.NUMERIC);
        typeMapping.mapping(PostgresTypes.REAL, SqlServerTypes.FLOAT);
        typeMapping.mapping(PostgresTypes.DOUBLE_PRECISION, SqlServerTypes.NUMERIC);

        typeMapping.mapping(PostgresTypes.MONEY, SqlServerTypes.MONEY);

        typeMapping.mapping(PostgresTypes.CHARACTER, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(PostgresTypes.BPCHAR, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CHARACTER_VARYING, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(PostgresTypes.TEXT, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NAME, SqlServerTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE, SqlServerTypes.DATETIME2);
        typeMapping.mapping(PostgresTypes.TIMESTAMP_WITH_TIME_ZONE, SqlServerTypes.DATETIMEOFFSET);
        typeMapping.mapping(PostgresTypes.TIME_WITHOUT_TIME_ZONE, SqlServerTypes.TIME);
        typeMapping.mapping(PostgresTypes.TIME_WITH_TIME_ZONE, SqlServerTypes.DATETIMEOFFSET);
        typeMapping.mapping(PostgresTypes.DATE, SqlServerTypes.DATE);
        typeMapping.mapping(PostgresTypes.INTERVAL, SqlServerTypes.DATETIME2);

        typeMapping.mapping(PostgresTypes.BIT, SqlServerTypes.VARBINARY);// sqlserver bit only include (1、0 or NULL)
        typeMapping.mapping(PostgresTypes.BIT_VARYING, SqlServerTypes.BINARY);
        typeMapping.mapping(PostgresTypes.BOOLEAN, SqlServerTypes.BIT);

        typeMapping.mapping(PostgresTypes.XML, SqlServerTypes.XML);
        typeMapping.mapping(PostgresTypes.BYTEA, SqlServerTypes.BINARY);
        typeMapping.mapping(PostgresTypes.REF_CURSOR, null);

        typeMapping.mapping(PostgresTypes.POINT, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LINE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.LSEG, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.BOX, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.PATH, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.POLYGON, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CIRCLE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOMETRY, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.GEOGRAPHY, SqlServerTypes.TEXT);

        typeMapping.mapping(PostgresTypes.CIDR, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INET, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.MACADDR8, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.HSTORE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.CITEXT, SqlServerTypes.TEXT);

        typeMapping.mapping(PostgresTypes.TSVECTOR, null);
        typeMapping.mapping(PostgresTypes.TSQUERY, null);

        typeMapping.mapping(PostgresTypes.UUID, SqlServerTypes.UNIQUEIDENTIFIER);

        typeMapping.mapping(PostgresTypes.JSON, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.JSONB, SqlServerTypes.TEXT);

        typeMapping.mapping(PostgresTypes.INT4RANGE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.INT8RANGE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.NUMRANGE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSRANGE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.TSTZRANGE, SqlServerTypes.TEXT);
        typeMapping.mapping(PostgresTypes.DATERANGE, SqlServerTypes.TEXT);

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
