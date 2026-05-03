package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2StarRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.StarRocks));

        binder.bindTableHandler(DsType.Oracle, DsType.StarRocks).to(new OracleTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.LONG, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(OracleSqlTypes.FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, StarRocksTypes.DOUBLE);

        typeMapping.mapping(OracleSqlTypes.CLOB, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NCLOB, StarRocksTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.BLOB, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.BFILE, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.DATE, StarRocksTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, StarRocksTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, StarRocksTypes.DATETIME);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, StarRocksTypes.DATETIME);

        typeMapping.mapping(OracleSqlTypes.RAW, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.ROWID, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.UROWID, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.OBJECT, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.REF, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.VARRAY, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, StarRocksTypes.BOOLEAN);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.XDBURITYPE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.DBURITYPE, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.SDO_GEOMETRY, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SDO_TOPO_GEOMETRY, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SDO_GEORASTER, StarRocksTypes.STRING);

        typeMapping.mapping(OracleSqlTypes.ORDAUDIO, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDDICOM, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDDOC, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDIMAGE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.ORDVIDEO, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_AVERAGE_COLOR, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR_HISTOGRAM, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_FEATURE_LIST, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_POSITIONAL_COLOR, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_STILL_IMAGE, StarRocksTypes.STRING);
        typeMapping.mapping(OracleSqlTypes.SI_TEXTURE, StarRocksTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
