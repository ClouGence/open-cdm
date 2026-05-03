package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.Doris));

        binder.bindTableHandler(DsType.Oracle, DsType.Doris).to(new OracleTableExporter(), new DorisTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, DorisTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, DorisTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, DorisTypes.DOUBLE);

        typeMapping.mapping(OracleSqlTypes.CLOB, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.NCLOB, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.BLOB, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.BFILE, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.DATE, DorisTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, DorisTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, DorisTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, DorisTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, DorisTypes.DATETIME);

        typeMapping.mapping(OracleSqlTypes.RAW, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.ROWID, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.UROWID, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.OBJECT, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.REF, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.VARRAY, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, DorisTypes.BOOLEAN);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.XDBURITYPE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.DBURITYPE, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.SDO_GEOMETRY, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SDO_TOPO_GEOMETRY, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SDO_GEORASTER, DorisTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.ORDAUDIO, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDDICOM, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDDOC, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDIMAGE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDVIDEO, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_AVERAGE_COLOR, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR_HISTOGRAM, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_FEATURE_LIST, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_POSITIONAL_COLOR, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_STILL_IMAGE, DorisTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_TEXTURE, DorisTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
