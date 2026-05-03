package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.HanaTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2HanaSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Hana));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Hana));

        binder.bindTableHandler(DsType.MySQL, DsType.Hana).to(new MySqlTableExporter(), new HanaTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, HanaTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.TINYINT, HanaTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, HanaTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, HanaTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.INT, HanaTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.BIGINT, HanaTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, HanaTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, HanaTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, HanaTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, HanaTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, HanaTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, HanaTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, HanaTypes.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, HanaTypes.INTEGER);

        typeMapping.mapping(MySQLTypes.CHAR, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, HanaTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, HanaTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, HanaTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, HanaTypes.BLOB);
        typeMapping.mapping(MySQLTypes.BLOB, HanaTypes.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, HanaTypes.BLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, HanaTypes.BLOB);

        typeMapping.mapping(MySQLTypes.TINYTEXT, HanaTypes.TEXT);
        typeMapping.mapping(MySQLTypes.TEXT, HanaTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, HanaTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, HanaTypes.TEXT);
        typeMapping.mapping(MySQLTypes.ENUM, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.SET, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.JSON, HanaTypes.TEXT);

        typeMapping.mapping(MySQLTypes.GEOMETRY, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POINT, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.LINESTRING, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POLYGON, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, HanaTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, HanaTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
