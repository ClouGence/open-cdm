//package com.clougence.reactor.mappings.mysql_src;
//
//import com.clougence.adapter.mysql.domain.MySQLTypes;
//import com.clougence.adapter.redshift.domain.RedshiftTypes;
//import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
//import com.clougence.schema.DsType;
//import com.clougence.schema.Install;
//import com.clougence.schema.SchemaBinder;
//import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
//import com.clougence.schema.SchemaPlugin;
//import com.clougence.schema.metadata.FieldType;
//
//public class MySql2RedshiftSchemaPlugin implements SchemaPlugin {
//
//    @Override
//    public void init(SchemaBinder binder) {
//        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Redshift));
//        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Redshift));
//
//        binder.bindTableHandler(DsType.MySQL, DsType.Redshift).to(new MySqlTableExporter());
//    }
//
//    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
//        typeMapping.mapping(MySQLTypes.BIT, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.TINYINT, RedshiftTypes.SMALLINT);
//        typeMapping.mapping(MySQLTypes.SMALLINT, RedshiftTypes.SMALLINT);
//        typeMapping.mapping(MySQLTypes.INT, RedshiftTypes.INTEGER);
//        typeMapping.mapping(MySQLTypes.MEDIUMINT, RedshiftTypes.BIGINT);
//        typeMapping.mapping(MySQLTypes.BIGINT, RedshiftTypes.BIGINT);
//        typeMapping.mapping(MySQLTypes.DECIMAL, RedshiftTypes.DECIMAL);
//        typeMapping.mapping(MySQLTypes.FLOAT, RedshiftTypes.REAL);
//        typeMapping.mapping(MySQLTypes.DOUBLE, RedshiftTypes.DOUBLE_PRECISION);
//
//        typeMapping.mapping(MySQLTypes.DATE, RedshiftTypes.DATE);
//        typeMapping.mapping(MySQLTypes.DATETIME, RedshiftTypes.TIMESTAMP);
//        typeMapping.mapping(MySQLTypes.TIMESTAMP, RedshiftTypes.TIMESTAMP);
//        typeMapping.mapping(MySQLTypes.TIME, RedshiftTypes.TIME);
//        typeMapping.mapping(MySQLTypes.YEAR, RedshiftTypes.INTEGER);
//
//        typeMapping.mapping(MySQLTypes.CHAR, RedshiftTypes.CHAR);
//        typeMapping.mapping(MySQLTypes.VARCHAR, RedshiftTypes.VARCHAR);
//
//        typeMapping.mapping(MySQLTypes.BINARY, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.VARBINARY, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.TINYBLOB, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.BLOB, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, RedshiftTypes.VARBYTE);
//        typeMapping.mapping(MySQLTypes.LONGBLOB, RedshiftTypes.VARBYTE);
//
//        typeMapping.mapping(MySQLTypes.TINYTEXT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.TEXT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.LONGTEXT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.ENUM, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.SET, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.JSON, RedshiftTypes.VARCHAR);
//
//        typeMapping.mapping(MySQLTypes.GEOMETRY, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.POINT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.LINESTRING, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.POLYGON, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.MULTIPOINT, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.MULTILINESTRING, RedshiftTypes.VARCHAR);
//        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, RedshiftTypes.VARCHAR);
//
//        mappingTypeWithVersion(typeMapping);
//    }
//
//    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {
//
//    }
//
//    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
//        funMapping.mapping("*", "");
//        funMapping.mapping("current_timestamp", "localtimestamp");
//        funMapping.mapping("current_timestamp(1)", "localtimestamp(1)");
//        funMapping.mapping("current_timestamp(2)", "localtimestamp(2)");
//        funMapping.mapping("current_timestamp(3)", "localtimestamp(3)");
//        funMapping.mapping("current_timestamp(4)", "localtimestamp(4)");
//        funMapping.mapping("current_timestamp(5)", "localtimestamp(5)");
//        funMapping.mapping("current_timestamp(6)", "localtimestamp(6)");
//        funMapping.mapping("curdate()", "current_date");
//        funMapping.mapping("curtime()", "localtime");
//        funMapping.mapping("current_date", "current_date");
//        funMapping.mapping("current_time", "localtime");
//        funMapping.mapping("now()", "localtimestamp");
//        funMapping.mapping("sysdate()", "localtimestamp");
//    }
//}
