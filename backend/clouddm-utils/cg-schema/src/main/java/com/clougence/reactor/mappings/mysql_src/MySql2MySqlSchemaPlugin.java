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
package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLMainVersion;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.MySQL));
        binder.bindTableHandler(DsType.MySQL, DsType.MySQL).to(new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        for (MySQLTypes sqlType : MySQLTypes.values()) {
            typeMapping.mapping(sqlType, sqlType);
        }

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.JSON, MySQLTypes.LONGTEXT, MySQLMainVersion.MySQL_5_6);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, MySQLTypes.TEXT, MySQLMainVersion.MySQL_5_6);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, MySQLTypes.TEXT, MySQLMainVersion.MySQL_5_7);
    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
