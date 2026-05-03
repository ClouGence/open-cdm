package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.Doris));

        binder.bindTableHandler(DsType.Hana, DsType.Doris).to(new HanaTableExporter(), new DorisTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, DorisTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, DorisTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, DorisTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, DorisTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, DorisTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, DorisTypes.BOOLEAN);

        typeMapping.mapping(HanaTypes.DATE, DorisTypes.DATEV2);
        typeMapping.mapping(HanaTypes.SECONDDATE, DorisTypes.DATETIMEV2);
        typeMapping.mapping(HanaTypes.TIMESTAMP, DorisTypes.DATETIMEV2);
        typeMapping.mapping(HanaTypes.TIME, DorisTypes.STRING);

        typeMapping.mapping(HanaTypes.CHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, DorisTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.NCLOB, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.BINTEXT, DorisTypes.STRING);

        typeMapping.mapping(HanaTypes.BLOB, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.BINARY, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.VARBINARY, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.ARRAY, DorisTypes.ARRAY);

        typeMapping.mapping(HanaTypes.ST_POINT, DorisTypes.STRING);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, DorisTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
