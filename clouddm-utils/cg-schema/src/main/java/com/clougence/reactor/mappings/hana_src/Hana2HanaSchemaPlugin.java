package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.reactor.handlers.importer.HanaTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2HanaSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.Hana));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.Hana));

        binder.bindTableHandler(DsType.Hana, DsType.Hana).to(new HanaTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        for (HanaTypes sqlType : HanaTypes.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }
    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }
}
