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
package com.clougence.reactor.mappings.db2_src;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.MySQL));

        binder.bindTableHandler(DsType.Db2, DsType.MySQL).to(new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(Db2Types.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(Db2Types.INTEGER, MySQLTypes.INT);
        typeMapping.mapping(Db2Types.BIGINT, MySQLTypes.BIGINT);

        typeMapping.mapping(Db2Types.REAL, MySQLTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DOUBLE, MySQLTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DECIMAL, MySQLTypes.DECIMAL);

        typeMapping.mapping(Db2Types.CHARACTER, MySQLTypes.CHAR);
        typeMapping.mapping(Db2Types.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(Db2Types.GRAPHIC, MySQLTypes.CHAR);
        typeMapping.mapping(Db2Types.VARGRAPHIC, MySQLTypes.VARCHAR);

        typeMapping.mapping(Db2Types.CHAR_FOR_BIT_DATA, MySQLTypes.BINARY);
        typeMapping.mapping(Db2Types.VARCHAR_FOR_BIT_DATA, MySQLTypes.VARBINARY);

        typeMapping.mapping(Db2Types.DATE, MySQLTypes.DATE);
        typeMapping.mapping(Db2Types.TIME, MySQLTypes.TIME);
        typeMapping.mapping(Db2Types.TIMESTAMP, MySQLTypes.TIMESTAMP);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
