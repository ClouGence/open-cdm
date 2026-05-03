package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2StartRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.StarRocks));
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (StarRocksTypes sqlType : StarRocksTypes.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
