package com.clougence.reactor.mappings.db2_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToDb2SchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.Db2));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.Db2));
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        for (Db2Types sqlType : Db2Types.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }
}
