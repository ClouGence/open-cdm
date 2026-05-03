package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class TiDB2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.Doris));

        binder.bindTableHandler(DsType.TiDB, DsType.Doris).to(new TiDBTableExporter(), new DorisTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, DorisTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.TINYINT, DorisTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, DorisTypes.INT);
        typeMapping.mapping(TiDBTypes.INT, DorisTypes.INT);
        typeMapping.mapping(TiDBTypes.BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(TiDBTypes.DOUBLE, DorisTypes.DOUBLE);

        typeMapping.mapping(TiDBTypes.DATE, DorisTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, DorisTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, DorisTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIME, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.YEAR, DorisTypes.INT);

        typeMapping.mapping(TiDBTypes.CHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(TiDBTypes.VARCHAR, DorisTypes.VARCHAR);

        typeMapping.mapping(TiDBTypes.BINARY, null);
        typeMapping.mapping(TiDBTypes.VARBINARY, null);
        typeMapping.mapping(TiDBTypes.TINYBLOB, null);
        typeMapping.mapping(TiDBTypes.BLOB, null);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, null);
        typeMapping.mapping(TiDBTypes.LONGBLOB, null);

        typeMapping.mapping(TiDBTypes.TINYTEXT, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.LONGTEXT, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.ENUM, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.SET, DorisTypes.STRING);
        typeMapping.mapping(TiDBTypes.JSON, DorisTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
