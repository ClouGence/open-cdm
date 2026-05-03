package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.ClickHouseTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2ClickhouseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.ClickHouse));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.ClickHouse));

        binder.bindTableHandler(DsType.StarRocks, DsType.ClickHouse).to(new StarRocksTableExporter(), new ClickHouseTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, ClickHouseTypes.Int8);
        typeMapping.mapping(StarRocksTypes.SMALLINT, ClickHouseTypes.Int16);
        typeMapping.mapping(StarRocksTypes.INT, ClickHouseTypes.Int32);
        typeMapping.mapping(StarRocksTypes.BIGINT, ClickHouseTypes.Int64);
        typeMapping.mapping(StarRocksTypes.LARGEINT, ClickHouseTypes.Int16);
        typeMapping.mapping(StarRocksTypes.DECIMAL, ClickHouseTypes.Decimal);
        typeMapping.mapping(StarRocksTypes.DOUBLE, ClickHouseTypes.Float64);
        typeMapping.mapping(StarRocksTypes.FLOAT, ClickHouseTypes.Float32);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, ClickHouseTypes.Bool);

        typeMapping.mapping(StarRocksTypes.CHAR, ClickHouseTypes.String);
        typeMapping.mapping(StarRocksTypes.VARCHAR, ClickHouseTypes.String);
        typeMapping.mapping(StarRocksTypes.STRING, ClickHouseTypes.String);

        typeMapping.mapping(StarRocksTypes.DATE, ClickHouseTypes.Date);
        typeMapping.mapping(StarRocksTypes.DATETIME, ClickHouseTypes.DateTime64);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, null);
        typeMapping.mapping(StarRocksTypes.BITMAP, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
