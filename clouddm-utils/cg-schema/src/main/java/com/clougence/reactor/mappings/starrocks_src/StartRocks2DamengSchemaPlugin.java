package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.Dameng));

        binder.bindTableHandler(DsType.StarRocks, DsType.Dameng).to(new StarRocksTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, DmSqlTypes.TINYINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(StarRocksTypes.INT, DmSqlTypes.INT);
        typeMapping.mapping(StarRocksTypes.BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, DmSqlTypes.NUMERIC);
        typeMapping.mapping(StarRocksTypes.DOUBLE, DmSqlTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, DmSqlTypes.BIT);

        typeMapping.mapping(StarRocksTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(StarRocksTypes.STRING, DmSqlTypes.TEXT);

        typeMapping.mapping(StarRocksTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, DmSqlTypes.TIMESTAMP);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.BITMAP, DmSqlTypes.BIGINT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
