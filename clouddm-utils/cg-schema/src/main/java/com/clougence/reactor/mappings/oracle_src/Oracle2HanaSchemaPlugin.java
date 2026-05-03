package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.HanaTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2HanaSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.Hana));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.Hana));

        binder.bindTableHandler(DsType.Oracle, DsType.Hana).to(new OracleTableExporter(), new HanaTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, HanaTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, HanaTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, HanaTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, HanaTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, HanaTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, HanaTypes.TEXT);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, HanaTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, HanaTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.FLOAT, HanaTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, HanaTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, HanaTypes.DOUBLE);

        typeMapping.mapping(OracleSqlTypes.CLOB, HanaTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.NCLOB, HanaTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.BLOB, null);
        typeMapping.mapping(OracleSqlTypes.BFILE, null);

        typeMapping.mapping(OracleSqlTypes.DATE, HanaTypes.DATE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, HanaTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, HanaTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, HanaTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, HanaTypes.DATE);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, HanaTypes.DATE);

        typeMapping.mapping(OracleSqlTypes.RAW, null);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, null);

        typeMapping.mapping(OracleSqlTypes.ROWID, HanaTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.UROWID, null);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, HanaTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, HanaTypes.VARCHAR);
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
