package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mongo.MongoTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2MongoSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.MongoDB));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.MongoDB));

        binder.bindTableHandler(DsType.MySQL, DsType.MongoDB).to(new MySqlTableExporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.TINYINT, MongoTypes.Integer);
        typeMapping.mapping(MySQLTypes.SMALLINT, MongoTypes.Integer);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, MongoTypes.Integer);
        typeMapping.mapping(MySQLTypes.INT, MongoTypes.Integer);
        typeMapping.mapping(MySQLTypes.BIGINT, MongoTypes.Long);
        typeMapping.mapping(MySQLTypes.DECIMAL, MongoTypes.Decimal);
        typeMapping.mapping(MySQLTypes.FLOAT, MongoTypes.Double);
        typeMapping.mapping(MySQLTypes.DOUBLE, MongoTypes.Double);

        typeMapping.mapping(MySQLTypes.DATE, MongoTypes.Date);
        typeMapping.mapping(MySQLTypes.DATETIME, MongoTypes.Date);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, MongoTypes.Date);
        typeMapping.mapping(MySQLTypes.TIME, MongoTypes.Date);
        typeMapping.mapping(MySQLTypes.YEAR, MongoTypes.String);

        typeMapping.mapping(MySQLTypes.CHAR, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.VARCHAR, MongoTypes.String);

        typeMapping.mapping(MySQLTypes.BINARY, MongoTypes.Binary);
        typeMapping.mapping(MySQLTypes.VARBINARY, MongoTypes.Binary);
        typeMapping.mapping(MySQLTypes.TINYBLOB, MongoTypes.Binary);
        typeMapping.mapping(MySQLTypes.BLOB, MongoTypes.Binary);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, MongoTypes.Binary);
        typeMapping.mapping(MySQLTypes.LONGBLOB, MongoTypes.Binary);

        typeMapping.mapping(MySQLTypes.TINYTEXT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.TEXT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.LONGTEXT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.ENUM, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.SET, MongoTypes.Array);
        typeMapping.mapping(MySQLTypes.JSON, MongoTypes.Object);

        typeMapping.mapping(MySQLTypes.GEOMETRY, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.POINT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.LINESTRING, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.POLYGON, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, MongoTypes.String);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, MongoTypes.String);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
