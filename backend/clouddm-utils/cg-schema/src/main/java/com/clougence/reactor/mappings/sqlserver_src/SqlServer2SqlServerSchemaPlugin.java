/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
