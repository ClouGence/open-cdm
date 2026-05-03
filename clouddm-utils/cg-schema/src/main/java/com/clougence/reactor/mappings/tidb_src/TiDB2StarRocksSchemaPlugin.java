package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class TiDB2StarRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.StarRocks));

        binder.bindTableHandler(DsType.TiDB, DsType.StarRocks).to(new TiDBTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, StarRocksTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.TINYINT, StarRocksTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, StarRocksTypes.INT);
        typeMapping.mapping(TiDBTypes.INT, StarRocksTypes.INT);
        typeMapping.mapping(TiDBTypes.BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(TiDBTypes.DOUBLE, StarRocksTypes.DOUBLE);

        typeMapping.mapping(TiDBTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, StarRocksTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, StarRocksTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIME, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.YEAR, StarRocksTypes.INT);

        typeMapping.mapping(TiDBTypes.CHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(TiDBTypes.VARCHAR, StarRocksTypes.VARCHAR);

        typeMapping.mapping(TiDBTypes.BINARY, null);
        typeMapping.mapping(TiDBTypes.VARBINARY, null);
        typeMapping.mapping(TiDBTypes.TINYBLOB, null);
        typeMapping.mapping(TiDBTypes.BLOB, null);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, null);
        typeMapping.mapping(TiDBTypes.LONGBLOB, null);

        typeMapping.mapping(TiDBTypes.TINYTEXT, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.TEXT, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.LONGTEXT, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.ENUM, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.SET, StarRocksTypes.STRING);
        typeMapping.mapping(TiDBTypes.JSON, StarRocksTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
