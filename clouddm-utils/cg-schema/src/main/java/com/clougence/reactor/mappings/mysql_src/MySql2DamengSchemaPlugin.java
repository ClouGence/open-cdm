package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Dameng));

        binder.bindTableHandler(DsType.MySQL, DsType.Dameng).to(new MySqlTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, DmSqlTypes.BINARY);
        typeMapping.mapping(MySQLTypes.TINYINT, DmSqlTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, DmSqlTypes.INT);
        typeMapping.mapping(MySQLTypes.INT, DmSqlTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, DmSqlTypes.NUMERIC);
        typeMapping.mapping(MySQLTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, DmSqlTypes.FLOAT);

        typeMapping.mapping(MySQLTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, DmSqlTypes.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, DmSqlTypes.SMALLINT);

        typeMapping.mapping(MySQLTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, DmSqlTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, DmSqlTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, DmSqlTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, DmSqlTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.BLOB, DmSqlTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, DmSqlTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.LONGBLOB, DmSqlTypes.IMAGE);

        typeMapping.mapping(MySQLTypes.TINYTEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.TEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.ENUM, DmSqlTypes.VARCHAR); // todo # "enum"
        typeMapping.mapping(MySQLTypes.SET, DmSqlTypes.VARCHAR); // todo # "SET"
        typeMapping.mapping(MySQLTypes.JSON, DmSqlTypes.TEXT);

        typeMapping.mapping(MySQLTypes.GEOMETRY, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POINT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LINESTRING, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POLYGON, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, DmSqlTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, DmSqlTypes.TEXT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
