package com.clougence.reactor.mappings.db2_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.MySQL));

        binder.bindTableHandler(DsType.Db2, DsType.MySQL).to(new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(Db2Types.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(Db2Types.INTEGER, MySQLTypes.INT);
        typeMapping.mapping(Db2Types.BIGINT, MySQLTypes.BIGINT);

        typeMapping.mapping(Db2Types.REAL, MySQLTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DOUBLE, MySQLTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DECIMAL, MySQLTypes.DECIMAL);

        typeMapping.mapping(Db2Types.CHARACTER, MySQLTypes.CHAR);
        typeMapping.mapping(Db2Types.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(Db2Types.GRAPHIC, MySQLTypes.CHAR);
        typeMapping.mapping(Db2Types.VARGRAPHIC, MySQLTypes.VARCHAR);

        typeMapping.mapping(Db2Types.CHAR_FOR_BIT_DATA, MySQLTypes.BINARY);
        typeMapping.mapping(Db2Types.VARCHAR_FOR_BIT_DATA, MySQLTypes.VARBINARY);

        typeMapping.mapping(Db2Types.DATE, MySQLTypes.DATE);
        typeMapping.mapping(Db2Types.TIME, MySQLTypes.TIME);
        typeMapping.mapping(Db2Types.TIMESTAMP, MySQLTypes.TIMESTAMP);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
