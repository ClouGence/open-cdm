package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2OracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.Oracle));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.Oracle));
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (OracleSqlTypes sqlType : OracleSqlTypes.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
