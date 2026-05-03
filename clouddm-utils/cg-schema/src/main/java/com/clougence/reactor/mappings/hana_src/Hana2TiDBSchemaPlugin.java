package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.TiDB));

        binder.bindTableHandler(DsType.Hana, DsType.TiDB).to(new HanaTableExporter(), new TiDBTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, TiDBTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, TiDBTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, TiDBTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, TiDBTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, TiDBTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, TiDBTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, TiDBTypes.BIT);

        typeMapping.mapping(HanaTypes.DATE, TiDBTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, TiDBTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIMESTAMP, TiDBTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIME, TiDBTypes.TIME);

        typeMapping.mapping(HanaTypes.CHAR, TiDBTypes.CHAR);
        typeMapping.mapping(HanaTypes.NCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, TiDBTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, TiDBTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, TiDBTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.NCLOB, TiDBTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.TEXT, TiDBTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.BINTEXT, TiDBTypes.LONGTEXT);

        typeMapping.mapping(HanaTypes.BLOB, TiDBTypes.LONGBLOB);
        typeMapping.mapping(HanaTypes.BINARY, TiDBTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.VARBINARY, TiDBTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.ARRAY, null);

        typeMapping.mapping(HanaTypes.ST_POINT, null);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
