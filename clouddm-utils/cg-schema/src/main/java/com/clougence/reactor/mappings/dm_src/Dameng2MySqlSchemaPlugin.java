package com.clougence.reactor.mappings.dm_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.MySQL));

        binder.bindTableHandler(DsType.Dameng, DsType.MySQL).to(new MySqlTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, MySQLTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.NCHAR, MySQLTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, MySQLTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.TEXT, MySQLTypes.TEXT);
        typeMapping.mapping(DmSqlTypes.CLOB, MySQLTypes.TEXT);
        typeMapping.mapping(DmSqlTypes.NUMERIC, MySQLTypes.DECIMAL);
        typeMapping.mapping(DmSqlTypes.TINYINT, MySQLTypes.TINYINT);
        typeMapping.mapping(DmSqlTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(DmSqlTypes.INT, MySQLTypes.INT);
        typeMapping.mapping(DmSqlTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(DmSqlTypes.FLOAT, MySQLTypes.FLOAT);
        typeMapping.mapping(DmSqlTypes.REAL, MySQLTypes.DOUBLE);
        typeMapping.mapping(DmSqlTypes.BIT, MySQLTypes.BIT);
        typeMapping.mapping(DmSqlTypes.BINARY, MySQLTypes.BINARY);
        typeMapping.mapping(DmSqlTypes.VARBINARY, MySQLTypes.VARBINARY);
        typeMapping.mapping(DmSqlTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(DmSqlTypes.TIME, MySQLTypes.TIME);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, MySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, MySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, MySQLTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, MySQLTypes.TIMESTAMP);

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
