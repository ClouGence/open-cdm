package com.clougence.reactor.mappings.dm_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2OceanBaseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.OceanBase));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.OceanBase));

        binder.bindTableHandler(DsType.Dameng, DsType.OceanBase);
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, ObForMySQLTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.NCHAR, ObForMySQLTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.VARCHAR, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.TEXT, ObForMySQLTypes.TEXT);
        typeMapping.mapping(DmSqlTypes.CLOB, ObForMySQLTypes.TEXT);
        typeMapping.mapping(DmSqlTypes.NUMERIC, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(DmSqlTypes.TINYINT, ObForMySQLTypes.TINYINT);
        typeMapping.mapping(DmSqlTypes.SMALLINT, ObForMySQLTypes.SMALLINT);
        typeMapping.mapping(DmSqlTypes.INT, ObForMySQLTypes.INT);
        typeMapping.mapping(DmSqlTypes.BIGINT, ObForMySQLTypes.BIGINT);
        typeMapping.mapping(DmSqlTypes.FLOAT, ObForMySQLTypes.FLOAT);
        typeMapping.mapping(DmSqlTypes.REAL, ObForMySQLTypes.DOUBLE);
        typeMapping.mapping(DmSqlTypes.BIT, ObForMySQLTypes.BIT);
        typeMapping.mapping(DmSqlTypes.BINARY, ObForMySQLTypes.BINARY);
        typeMapping.mapping(DmSqlTypes.VARBINARY, ObForMySQLTypes.VARBINARY);
        typeMapping.mapping(DmSqlTypes.DATE, ObForMySQLTypes.DATE);
        typeMapping.mapping(DmSqlTypes.TIME, ObForMySQLTypes.TIME);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.ROWID, ObForMySQLTypes.VARCHAR);

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
        typeMapping.mapping(DmSqlTypes.IMAGE, null);
        typeMapping.mapping(DmSqlTypes.BFILE, null);
    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("sysdate", "now()");
        funMapping.mapping("systimestamp", "now()");
    }
}
