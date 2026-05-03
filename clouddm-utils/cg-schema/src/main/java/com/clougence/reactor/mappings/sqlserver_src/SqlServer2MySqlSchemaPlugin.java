package com.clougence.reactor.mappings.sqlserver_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.MySQL));

        binder.bindTableHandler(DsType.SqlServer, DsType.MySQL).to(new SqlServerTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, MySQLTypes.BIT);
        typeMapping.mapping(SqlServerTypes.DECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.NUMERIC, MySQLTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, MySQLTypes.TINYINT); // sqlserver 0~255 , mysql -128~127 (unsigned 0~255)
        typeMapping.mapping(SqlServerTypes.INT, MySQLTypes.INT);
        typeMapping.mapping(SqlServerTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, MySQLTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.MONEY, MySQLTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.FLOAT, MySQLTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.REAL, MySQLTypes.DOUBLE);

        typeMapping.mapping(SqlServerTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, MySQLTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.DATETIME2, MySQLTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, MySQLTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.DATETIME, MySQLTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.TIME, MySQLTypes.TIME);

        typeMapping.mapping(SqlServerTypes.CHAR, MySQLTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.TEXT, MySQLTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.NCHAR, MySQLTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.NTEXT, MySQLTypes.TEXT);

        typeMapping.mapping(SqlServerTypes.BINARY, MySQLTypes.BINARY);
        typeMapping.mapping(SqlServerTypes.VARBINARY, MySQLTypes.VARBINARY);
        typeMapping.mapping(SqlServerTypes.IMAGE, MySQLTypes.BLOB);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, MySQLTypes.BIGINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, MySQLTypes.BIGINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, MySQLTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, MySQLTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, null);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, null);
        typeMapping.mapping(SqlServerTypes.SYSNAME, MySQLTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("getdate()", "current_timestamp");
    }

}
