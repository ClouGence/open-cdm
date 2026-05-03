package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.polar.porx.PolarDbXTypes;
import com.clougence.reactor.handlers.importer.PolarDbXTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2PolarDbXSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.PolarDbX));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.PolarDbX));

        binder.bindTableHandler(DsType.MySQL, DsType.PolarDbX).to(new PolarDbXTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (MySQLTypes type : MySQLTypes.values()) {
            typeMapping.mapping(type, PolarDbXTypes.valueOf(type.name()));
        }
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }
}
