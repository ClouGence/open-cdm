package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.TiDB));

        binder.bindTableHandler(DsType.Oracle, DsType.TiDB).to(new OracleTableExporter(), new TiDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, TiDBTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, TiDBTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, TiDBTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, TiDBTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, TiDBTypes.LONGTEXT);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, TiDBTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.FLOAT, TiDBTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, TiDBTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, TiDBTypes.DOUBLE);

        typeMapping.mapping(OracleSqlTypes.CLOB, TiDBTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.NCLOB, TiDBTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.BLOB, null);
        typeMapping.mapping(OracleSqlTypes.BFILE, null);

        typeMapping.mapping(OracleSqlTypes.DATE, TiDBTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, TiDBTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, TiDBTypes.DATETIME);

        typeMapping.mapping(OracleSqlTypes.RAW, null);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, null);

        typeMapping.mapping(OracleSqlTypes.ROWID, TiDBTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.UROWID, null);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, TiDBTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, TiDBTypes.VARCHAR);
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
