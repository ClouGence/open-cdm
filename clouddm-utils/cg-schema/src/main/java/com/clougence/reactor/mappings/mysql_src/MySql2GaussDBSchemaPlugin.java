package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.gauss.GaussDBTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.GaussDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2GaussDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.GaussDB));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.GaussDB));

        binder.bindTableHandler(DsType.MySQL, DsType.GaussDB).to(new MySqlTableExporter(), new GaussDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, GaussDBTypes.BIT);
        typeMapping.mapping(MySQLTypes.TINYINT, GaussDBTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, GaussDBTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, GaussDBTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.INT, GaussDBTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.BIGINT, GaussDBTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, GaussDBTypes.NUMERIC);
        typeMapping.mapping(MySQLTypes.FLOAT, GaussDBTypes.REAL);
        typeMapping.mapping(MySQLTypes.DOUBLE, GaussDBTypes.DOUBLE_PRECISION);

        typeMapping.mapping(MySQLTypes.DATE, GaussDBTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, GaussDBTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, GaussDBTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(MySQLTypes.TIME, GaussDBTypes.CHARACTER_VARYING);
        typeMapping.mapping(MySQLTypes.YEAR, GaussDBTypes.INTEGER);

        typeMapping.mapping(MySQLTypes.CHAR, GaussDBTypes.CHARACTER);
        typeMapping.mapping(MySQLTypes.VARCHAR, GaussDBTypes.CHARACTER_VARYING);

        typeMapping.mapping(MySQLTypes.BINARY, GaussDBTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.VARBINARY, GaussDBTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.TINYBLOB, GaussDBTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.BLOB, GaussDBTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, GaussDBTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.LONGBLOB, GaussDBTypes.BYTEA);

        typeMapping.mapping(MySQLTypes.TINYTEXT, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.TEXT, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.ENUM, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.SET, GaussDBTypes.CHARACTER_VARYING_ARRAY);
        typeMapping.mapping(MySQLTypes.JSON, GaussDBTypes.JSON);

        typeMapping.mapping(MySQLTypes.GEOMETRY, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POINT, GaussDBTypes.POINT);
        typeMapping.mapping(MySQLTypes.LINESTRING, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POLYGON, GaussDBTypes.POLYGON);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, GaussDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, GaussDBTypes.TEXT);

        //for ai virtual column.
        typeMapping.mapping(MySQLTypes.VECTOR, GaussDBTypes.VECTOR);
        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp", "localtimestamp");
        funMapping.mapping("current_timestamp(1)", "localtimestamp(1)");
        funMapping.mapping("current_timestamp(2)", "localtimestamp(2)");
        funMapping.mapping("current_timestamp(3)", "localtimestamp(3)");
        funMapping.mapping("current_timestamp(4)", "localtimestamp(4)");
        funMapping.mapping("current_timestamp(5)", "localtimestamp(5)");
        funMapping.mapping("current_timestamp(6)", "localtimestamp(6)");
        funMapping.mapping("curdate()", "current_date");
        funMapping.mapping("curtime()", "localtime");
        funMapping.mapping("current_date", "current_date");
        funMapping.mapping("current_time", "localtime");
        funMapping.mapping("now()", "localtimestamp");
        funMapping.mapping("sysdate()", "localtimestamp");
    }
}
