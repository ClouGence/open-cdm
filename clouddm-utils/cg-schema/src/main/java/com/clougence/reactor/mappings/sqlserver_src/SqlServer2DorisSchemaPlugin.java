package com.clougence.reactor.mappings.sqlserver_src;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.Doris));

        binder.bindTableHandler(DsType.SqlServer, DsType.Doris).to(new SqlServerTableExporter(), new DorisTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, DorisTypes.TINYINT);
        typeMapping.mapping(SqlServerTypes.DECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.NUMERIC, DorisTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, DorisTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.INT, DorisTypes.INT);
        typeMapping.mapping(SqlServerTypes.BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, DorisTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.MONEY, DorisTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.REAL, DorisTypes.DOUBLE);

        typeMapping.mapping(SqlServerTypes.DATE, DorisTypes.DATEV2);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, DorisTypes.DATETIMEV2);
        typeMapping.mapping(SqlServerTypes.DATETIME2, DorisTypes.DATETIMEV2);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, DorisTypes.DATETIMEV2);
        typeMapping.mapping(SqlServerTypes.DATETIME, DorisTypes.DATETIMEV2);
        typeMapping.mapping(SqlServerTypes.TIME, DorisTypes.STRING);

        typeMapping.mapping(SqlServerTypes.CHAR, DorisTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.VARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(SqlServerTypes.NCHAR, DorisTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.NTEXT, DorisTypes.STRING);

        typeMapping.mapping(SqlServerTypes.BINARY, null);
        typeMapping.mapping(SqlServerTypes.VARBINARY, null);
        typeMapping.mapping(SqlServerTypes.IMAGE, null);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, DorisTypes.LARGEINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, DorisTypes.LARGEINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, DorisTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, DorisTypes.STRING);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, DorisTypes.STRING);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, DorisTypes.STRING);
        typeMapping.mapping(SqlServerTypes.SYSNAME, DorisTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
