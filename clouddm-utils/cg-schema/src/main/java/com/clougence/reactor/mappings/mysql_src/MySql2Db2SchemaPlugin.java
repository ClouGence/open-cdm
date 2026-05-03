package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2Db2SchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Db2));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Db2));

        binder.bindTableHandler(DsType.MySQL, DsType.Db2).to(new MySqlTableExporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, Db2Types.BIGINT);
        typeMapping.mapping(MySQLTypes.TINYINT, Db2Types.SMALLINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, Db2Types.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, Db2Types.INTEGER);
        typeMapping.mapping(MySQLTypes.INT, Db2Types.INTEGER);
        typeMapping.mapping(MySQLTypes.BIGINT, Db2Types.BIGINT);

        typeMapping.mapping(MySQLTypes.FLOAT, Db2Types.DOUBLE);
        typeMapping.mapping(MySQLTypes.DOUBLE, Db2Types.DOUBLE);
        typeMapping.mapping(MySQLTypes.DECIMAL, Db2Types.DECIMAL);

        typeMapping.mapping(MySQLTypes.DATE, Db2Types.DATE);
        typeMapping.mapping(MySQLTypes.TIME, Db2Types.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, Db2Types.INTEGER);
        typeMapping.mapping(MySQLTypes.DATETIME, Db2Types.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, Db2Types.TIMESTAMP);

        typeMapping.mapping(MySQLTypes.CHAR, Db2Types.CHARACTER);
        typeMapping.mapping(MySQLTypes.VARCHAR, Db2Types.VARCHAR);

        typeMapping.mapping(MySQLTypes.TINYTEXT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.TEXT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.LONGTEXT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.ENUM, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.SET, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.JSON, Db2Types.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, Db2Types.BLOB);
        typeMapping.mapping(MySQLTypes.VARBINARY, Db2Types.BLOB);
        typeMapping.mapping(MySQLTypes.TINYBLOB, Db2Types.BLOB);
        typeMapping.mapping(MySQLTypes.BLOB, Db2Types.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, Db2Types.BLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, Db2Types.BLOB);

        typeMapping.mapping(MySQLTypes.GEOMETRY, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.POINT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.LINESTRING, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.POLYGON, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, Db2Types.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, Db2Types.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
