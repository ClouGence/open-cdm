package com.clougence.reactor.mappings.dm_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.doris.DorisTypes;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.Doris));

        binder.bindTableHandler(DsType.Dameng, DsType.Doris).to(new DorisTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.VARCHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, DorisTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(DmSqlTypes.CLOB, DorisTypes.STRING);
        typeMapping.mapping(DmSqlTypes.NUMERIC, DorisTypes.DECIMAL);
        typeMapping.mapping(DmSqlTypes.TINYINT, DorisTypes.TINYINT);
        typeMapping.mapping(DmSqlTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(DmSqlTypes.INT, DorisTypes.INT);
        typeMapping.mapping(DmSqlTypes.BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(DmSqlTypes.FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(DmSqlTypes.REAL, DorisTypes.DOUBLE);
        typeMapping.mapping(DmSqlTypes.BIT, DorisTypes.INT);
        typeMapping.mapping(DmSqlTypes.DATE, DorisTypes.DATEV2);
        typeMapping.mapping(DmSqlTypes.TIME, DorisTypes.STRING);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, DorisTypes.DATETIMEV2);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, DorisTypes.DATETIMEV2);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, DorisTypes.DATETIMEV2);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, DorisTypes.DATETIMEV2);

        typeMapping.mapping(DmSqlTypes.BINARY, null);
        typeMapping.mapping(DmSqlTypes.VARBINARY, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR_TO_MONTH, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MONTH, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_HOUR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_SECOND, null);
        typeMapping.mapping(DmSqlTypes.ROWID, null);

        typeMapping.mapping(DmSqlTypes.IMAGE, null);
        typeMapping.mapping(DmSqlTypes.BFILE, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
