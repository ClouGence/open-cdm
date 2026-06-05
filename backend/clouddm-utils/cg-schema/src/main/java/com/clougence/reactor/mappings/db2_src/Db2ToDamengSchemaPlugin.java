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

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.db2.Db2Types;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToDamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.Dameng));

        binder.bindTableHandler(DsType.Db2, DsType.Dameng).to(new DamengTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(Db2Types.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(Db2Types.INTEGER, DmSqlTypes.INT);
        typeMapping.mapping(Db2Types.BIGINT, DmSqlTypes.BIGINT);

        typeMapping.mapping(Db2Types.REAL, DmSqlTypes.FLOAT);
        typeMapping.mapping(Db2Types.DOUBLE, DmSqlTypes.FLOAT);
        typeMapping.mapping(Db2Types.DECIMAL, DmSqlTypes.NUMERIC);

        typeMapping.mapping(Db2Types.CHARACTER, DmSqlTypes.CHAR);
        typeMapping.mapping(Db2Types.VARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(Db2Types.GRAPHIC, DmSqlTypes.CHAR);
        typeMapping.mapping(Db2Types.VARGRAPHIC, DmSqlTypes.VARCHAR);

        typeMapping.mapping(Db2Types.CHAR_FOR_BIT_DATA, DmSqlTypes.BINARY);
        typeMapping.mapping(Db2Types.VARCHAR_FOR_BIT_DATA, DmSqlTypes.VARBINARY);

        typeMapping.mapping(Db2Types.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(Db2Types.TIME, DmSqlTypes.TIME);
        typeMapping.mapping(Db2Types.TIMESTAMP, DmSqlTypes.TIMESTAMP);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
