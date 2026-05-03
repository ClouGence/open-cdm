package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @Author: baili
 * @Date: 2023/04/04/10:02
 * @Description:
 */
public class TiDB2OceanBaseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.OceanBase));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.OceanBase));

        binder.bindTableHandler(DsType.TiDB, DsType.OceanBase).to(new TiDBTableExporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, ObForMySQLTypes.BIT);
        typeMapping.mapping(TiDBTypes.TINYINT, ObForMySQLTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, ObForMySQLTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, ObForMySQLTypes.MEDIUMINT);
        typeMapping.mapping(TiDBTypes.INT, ObForMySQLTypes.INT);
        typeMapping.mapping(TiDBTypes.BIGINT, ObForMySQLTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.FLOAT, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.DOUBLE, ObForMySQLTypes.DOUBLE);

        typeMapping.mapping(TiDBTypes.DATE, ObForMySQLTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(TiDBTypes.TIME, ObForMySQLTypes.TIME);
        typeMapping.mapping(TiDBTypes.YEAR, ObForMySQLTypes.YEAR);

        typeMapping.mapping(TiDBTypes.CHAR, ObForMySQLTypes.CHAR);
        typeMapping.mapping(TiDBTypes.VARCHAR, ObForMySQLTypes.VARCHAR);

        typeMapping.mapping(TiDBTypes.BINARY, ObForMySQLTypes.BINARY);
        typeMapping.mapping(TiDBTypes.VARBINARY, ObForMySQLTypes.VARBINARY);
        typeMapping.mapping(TiDBTypes.TINYBLOB, ObForMySQLTypes.TINYBLOB);
        typeMapping.mapping(TiDBTypes.BLOB, ObForMySQLTypes.BLOB);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, ObForMySQLTypes.MEDIUMBLOB);
        typeMapping.mapping(TiDBTypes.LONGBLOB, ObForMySQLTypes.LONGBLOB);

        typeMapping.mapping(TiDBTypes.TINYTEXT, ObForMySQLTypes.TINYTEXT);
        typeMapping.mapping(TiDBTypes.TEXT, ObForMySQLTypes.TEXT);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, ObForMySQLTypes.MEDIUMTEXT);
        typeMapping.mapping(TiDBTypes.LONGTEXT, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(TiDBTypes.ENUM, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(TiDBTypes.SET, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(TiDBTypes.JSON, ObForMySQLTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }
}
