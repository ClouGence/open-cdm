package com.clougence.reactor.mappings.adbmysql_src;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class AdbMySql2AdbMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.AdbForMySQL, DsType.AdbForMySQL));
        mappingFoo(binder.bindFuncMapping(DsType.AdbForMySQL, DsType.AdbForMySQL));
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (AdbMySQLTypes sqlType : AdbMySQLTypes.values()) {
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
