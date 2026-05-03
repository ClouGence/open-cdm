package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.AdbMySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2AdbMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.AdbForMySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.AdbForMySQL));

        binder.bindTableHandler(DsType.Oracle, DsType.AdbForMySQL).to(new OracleTableExporter(), new AdbMySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, AdbMySQLTypes.VARCHAR);

        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, AdbMySQLTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, AdbMySQLTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.FLOAT, AdbMySQLTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, AdbMySQLTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, AdbMySQLTypes.DOUBLE);

        typeMapping.mapping(OracleSqlTypes.CLOB, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NCLOB, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.BLOB, AdbMySQLTypes.BINARY); // reduced capacity
        typeMapping.mapping(OracleSqlTypes.BFILE, null);

        typeMapping.mapping(OracleSqlTypes.DATE, AdbMySQLTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, AdbMySQLTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, AdbMySQLTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, AdbMySQLTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, AdbMySQLTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, AdbMySQLTypes.DATETIME);

        typeMapping.mapping(OracleSqlTypes.RAW, null);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, null);

        typeMapping.mapping(OracleSqlTypes.ROWID, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.UROWID, null);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, AdbMySQLTypes.VARCHAR);
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
        funMapping.mapping("sysdate", "now()");
        funMapping.mapping("systimestamp", "now()");
    }

}
