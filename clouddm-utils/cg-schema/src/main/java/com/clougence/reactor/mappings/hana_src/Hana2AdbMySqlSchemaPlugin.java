package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.AdbMySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2AdbMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.AdbForMySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.AdbForMySQL));

        binder.bindTableHandler(DsType.Hana, DsType.AdbForMySQL).to(new HanaTableExporter(), new AdbMySqlTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, AdbMySQLTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, AdbMySQLTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, AdbMySQLTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, AdbMySQLTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, AdbMySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, AdbMySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, AdbMySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, AdbMySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, AdbMySQLTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, AdbMySQLTypes.BIGINT);

        typeMapping.mapping(HanaTypes.DATE, AdbMySQLTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, AdbMySQLTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIMESTAMP, AdbMySQLTypes.TIMESTAMP);
        typeMapping.mapping(HanaTypes.TIME, AdbMySQLTypes.TIME);

        typeMapping.mapping(HanaTypes.CHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NCHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NCLOB, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.TEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.BINTEXT, AdbMySQLTypes.VARCHAR);

        typeMapping.mapping(HanaTypes.BLOB, AdbMySQLTypes.BINARY);
        typeMapping.mapping(HanaTypes.BINARY, AdbMySQLTypes.BINARY);
        typeMapping.mapping(HanaTypes.VARBINARY, AdbMySQLTypes.BINARY);
        typeMapping.mapping(HanaTypes.ARRAY, null);

        typeMapping.mapping(HanaTypes.ST_POINT, AdbMySQLTypes.POINT);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, AdbMySQLTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
