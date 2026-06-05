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
package com.clougence.reactor.mappings.postgres_src;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.PostgresTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Postgres2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.PostgreSQL, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.PostgreSQL, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.PostgreSQL, DsType.PostgreSQL).to(new PostgresTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (PostgresTypes sqlType : PostgresTypes.values()) {
            if (sqlType == PostgresTypes.BIT || sqlType == PostgresTypes.BIT_ARRAY || sqlType == PostgresTypes.BIT_VARYING || sqlType == PostgresTypes.BIT_VARYING_ARRAY) {
                // TODO BIT、BIT_ARRAY、BIT_VARYING、BIT_VARYING_ARRAY need columnSize ,but PostgresMetadataProvider has BUG size is null or bad.
                typeMapping.mapping(sqlType, null);
            } else {
                typeMapping.mapping(sqlType, sqlType);
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
