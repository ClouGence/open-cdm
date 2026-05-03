package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.OracleTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2OracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.Oracle));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.Oracle));

        binder.bindTableHandler(DsType.StarRocks, DsType.Oracle).to(new StarRocksTableExporter(), new OracleTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(StarRocksTypes.INT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(StarRocksTypes.BIGINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(StarRocksTypes.DOUBLE, OracleSqlTypes.BINARY_DOUBLE);
        typeMapping.mapping(StarRocksTypes.FLOAT, OracleSqlTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, OracleSqlTypes.BLOB);

        typeMapping.mapping(StarRocksTypes.CHAR, OracleSqlTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(StarRocksTypes.STRING, OracleSqlTypes.CLOB);

        typeMapping.mapping(StarRocksTypes.DATE, OracleSqlTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, OracleSqlTypes.TIMESTAMP);

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
