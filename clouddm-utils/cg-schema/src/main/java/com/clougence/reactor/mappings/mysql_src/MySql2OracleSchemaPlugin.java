package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.OracleTableImporter;
import com.clougence.reactor.handlers.special.MySql2OracleHandler;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2OracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Oracle));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Oracle));

        binder.bindTableHandler(DsType.MySQL, DsType.Oracle).to(new MySql2OracleHandler(), new MySqlTableExporter(), new OracleTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.TINYINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.INT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.BIGINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, OracleSqlTypes.BINARY_FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, OracleSqlTypes.BINARY_DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, OracleSqlTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.YEAR, OracleSqlTypes.NUMBER_BIGINT);

        typeMapping.mapping(MySQLTypes.CHAR, OracleSqlTypes.CHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, OracleSqlTypes.VARCHAR2);

        typeMapping.mapping(MySQLTypes.BINARY, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.VARBINARY, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.TINYBLOB, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.BLOB, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, OracleSqlTypes.BLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, OracleSqlTypes.BLOB);

        typeMapping.mapping(MySQLTypes.TINYTEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(MySQLTypes.TEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(MySQLTypes.LONGTEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(MySQLTypes.ENUM, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.SET, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.JSON, OracleSqlTypes.CLOB);

        typeMapping.mapping(MySQLTypes.GEOMETRY, null/*OracleSqlTypes.SDO_GEOMETRY*/); // need function, https://blog.csdn.net/weixin_33921089/article/details/86324465
        typeMapping.mapping(MySQLTypes.POINT, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.LINESTRING, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.POLYGON, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, null/*OracleSqlTypes.SDO_GEOMETRY*/);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp", "sysdate");
        funMapping.mapping("current_timestamp(1)", "sysdate");
        funMapping.mapping("current_timestamp(2)", "sysdate");
        funMapping.mapping("current_timestamp(3)", "sysdate");
        funMapping.mapping("current_timestamp(4)", "sysdate");
        funMapping.mapping("current_timestamp(5)", "sysdate");
        funMapping.mapping("current_timestamp(6)", "sysdate");
        funMapping.mapping("current_time", "sysdate");
        funMapping.mapping("current_date", "sysdate");
        funMapping.mapping("now()", "sysdate");
        funMapping.mapping("sysdate()", "sysdate");
    }

}
