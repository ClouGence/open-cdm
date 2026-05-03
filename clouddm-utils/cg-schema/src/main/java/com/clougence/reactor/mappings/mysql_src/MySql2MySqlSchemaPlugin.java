package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLMainVersion;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.MySQL));
        binder.bindTableHandler(DsType.MySQL, DsType.MySQL).to(new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (MySQLTypes sqlType : MySQLTypes.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.JSON, MySQLTypes.LONGTEXT, MySQLMainVersion.MySQL_5_6);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, MySQLTypes.TEXT, MySQLMainVersion.MySQL_5_6);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, MySQLTypes.TEXT, MySQLMainVersion.MySQL_5_7);
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
