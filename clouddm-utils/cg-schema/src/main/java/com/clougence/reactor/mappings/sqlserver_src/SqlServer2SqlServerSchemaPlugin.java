package com.clougence.reactor.mappings.sqlserver_src;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.importer.SqlServerTableImporter;
import com.clougence.reactor.handlers.special.SqlServer2SqlServerHandler;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2SqlServerSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.SqlServer));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.SqlServer));

        binder.bindTableHandler(DsType.SqlServer, DsType.SqlServer).to(new SqlServer2SqlServerHandler(), new SqlServerTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (SqlServerTypes sqlType : SqlServerTypes.values()) {
            switch (sqlType) {
                case SQL_VARIANT:
                case GEOMETRY:
                case GEOGRAPHY:
                case HIERARCHYID:
                    typeMapping.mapping(sqlType, null);
                    break;
                default:
                    typeMapping.mapping(sqlType, sqlType);
                    break;
            }
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
