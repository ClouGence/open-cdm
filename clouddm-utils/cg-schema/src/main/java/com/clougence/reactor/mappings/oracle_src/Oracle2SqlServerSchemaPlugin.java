package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.SqlServerTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2SqlServerSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.SqlServer));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.SqlServer));

        binder.bindTableHandler(DsType.Oracle, DsType.SqlServer).to(new OracleTableExporter(), new SqlServerTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, SqlServerTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, SqlServerTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, SqlServerTypes.TEXT);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, SqlServerTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, SqlServerTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.FLOAT, SqlServerTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, SqlServerTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, SqlServerTypes.FLOAT);

        typeMapping.mapping(OracleSqlTypes.CLOB, SqlServerTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.NCLOB, SqlServerTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.BLOB, SqlServerTypes.IMAGE);
        typeMapping.mapping(OracleSqlTypes.BFILE, SqlServerTypes.IMAGE);

        typeMapping.mapping(OracleSqlTypes.DATE, SqlServerTypes.DATETIME2);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, SqlServerTypes.DATETIME2);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, SqlServerTypes.DATETIMEOFFSET);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, SqlServerTypes.DATETIMEOFFSET);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, SqlServerTypes.DATETIME2);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, SqlServerTypes.DATETIME2);

        typeMapping.mapping(OracleSqlTypes.RAW, SqlServerTypes.IMAGE);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, SqlServerTypes.IMAGE);

        typeMapping.mapping(OracleSqlTypes.ROWID, SqlServerTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.UROWID, SqlServerTypes.VARCHAR);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(OracleSqlTypes.XDBURITYPE, null);
        typeMapping.mapping(OracleSqlTypes.DBURITYPE, null);

        typeMapping.mapping(OracleSqlTypes.SDO_GEOMETRY, null);
        typeMapping.mapping(OracleSqlTypes.SDO_TOPO_GEOMETRY, null);
        typeMapping.mapping(OracleSqlTypes.SDO_GEORASTER, null);

        typeMapping.mapping(OracleSqlTypes.ORDAUDIO, null);
        typeMapping.mapping(OracleSqlTypes.ORDDICOM, null);
        typeMapping.mapping(OracleSqlTypes.ORDDOC, null);
        typeMapping.mapping(OracleSqlTypes.ORDIMAGE, null);
        typeMapping.mapping(OracleSqlTypes.ORDVIDEO, null);
        typeMapping.mapping(OracleSqlTypes.SI_AVERAGE_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR_HISTOGRAM, null);
        typeMapping.mapping(OracleSqlTypes.SI_FEATURE_LIST, null);
        typeMapping.mapping(OracleSqlTypes.SI_POSITIONAL_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_STILL_IMAGE, null);
        typeMapping.mapping(OracleSqlTypes.SI_TEXTURE, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("sysdate", "getdate()");
        funMapping.mapping("systimestamp", "getdate()");
        funMapping.mapping("sys_guid()", "newid()");
    }

}
