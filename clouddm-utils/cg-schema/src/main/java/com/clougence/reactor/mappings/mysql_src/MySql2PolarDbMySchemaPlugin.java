package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.polar.pormy.PolarDBMyTypes;
import com.clougence.reactor.handlers.importer.PolarDBMyTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2PolarDbMySchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.PolarDBMySQL));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.PolarDBMySQL));

        binder.bindTableHandler(DsType.MySQL, DsType.PolarDBMySQL).to(new PolarDBMyTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (MySQLTypes type : MySQLTypes.values()) {
            typeMapping.mapping(type, PolarDBMyTypes.valueOf(type.name()));
        }
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }
}
