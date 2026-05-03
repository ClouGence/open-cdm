package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.reactor.handlers.importer.ClickHouseTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class TiDB2ClickhouseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.ClickHouse));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.ClickHouse));

        binder.bindTableHandler(DsType.TiDB, DsType.ClickHouse).to(new TiDBTableExporter(), new ClickHouseTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, ClickHouseTypes.Int64);
        typeMapping.mapping(TiDBTypes.TINYINT, ClickHouseTypes.Int8);
        typeMapping.mapping(TiDBTypes.SMALLINT, ClickHouseTypes.Int16);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, ClickHouseTypes.Int32);
        typeMapping.mapping(TiDBTypes.INT, ClickHouseTypes.Int32);
        typeMapping.mapping(TiDBTypes.BIGINT, ClickHouseTypes.Int64);
        typeMapping.mapping(TiDBTypes.DECIMAL, ClickHouseTypes.Decimal);
        typeMapping.mapping(TiDBTypes.FLOAT, ClickHouseTypes.Float32);
        typeMapping.mapping(TiDBTypes.DOUBLE, ClickHouseTypes.Float64);

        typeMapping.mapping(TiDBTypes.DATE, ClickHouseTypes.Date);
        typeMapping.mapping(TiDBTypes.DATETIME, ClickHouseTypes.DateTime64);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, ClickHouseTypes.DateTime64);
        typeMapping.mapping(TiDBTypes.TIME, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.YEAR, ClickHouseTypes.Int32);

        typeMapping.mapping(TiDBTypes.CHAR, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.VARCHAR, ClickHouseTypes.String);

        typeMapping.mapping(TiDBTypes.BINARY, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.VARBINARY, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.TINYBLOB, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.BLOB, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.LONGBLOB, ClickHouseTypes.String);

        typeMapping.mapping(TiDBTypes.TINYTEXT, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.TEXT, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.LONGTEXT, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.ENUM, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.SET, ClickHouseTypes.String);
        typeMapping.mapping(TiDBTypes.JSON, ClickHouseTypes.String);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
