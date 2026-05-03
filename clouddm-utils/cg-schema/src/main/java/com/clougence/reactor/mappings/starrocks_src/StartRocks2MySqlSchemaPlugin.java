package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.MySQL));

        binder.bindTableHandler(DsType.StarRocks, DsType.MySQL).to(new StarRocksTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, MySQLTypes.TINYINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(StarRocksTypes.INT, MySQLTypes.INT);
        typeMapping.mapping(StarRocksTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(StarRocksTypes.DOUBLE, MySQLTypes.DOUBLE);
        typeMapping.mapping(StarRocksTypes.FLOAT, MySQLTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, MySQLTypes.BIT);

        typeMapping.mapping(StarRocksTypes.CHAR, MySQLTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(StarRocksTypes.STRING, MySQLTypes.TEXT);

        typeMapping.mapping(StarRocksTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, MySQLTypes.DATETIME);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.BITMAP, MySQLTypes.BIGINT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
