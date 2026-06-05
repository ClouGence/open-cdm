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
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Db2ToStarRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Db2, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.Db2, DsType.StarRocks));

        binder.bindTableHandler(DsType.Db2, DsType.StarRocks).to(new StarRocksTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(Db2Types.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(Db2Types.INTEGER, StarRocksTypes.INT);
        typeMapping.mapping(Db2Types.BIGINT, StarRocksTypes.BIGINT);

        typeMapping.mapping(Db2Types.REAL, StarRocksTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DOUBLE, StarRocksTypes.DOUBLE);
        typeMapping.mapping(Db2Types.DECIMAL, StarRocksTypes.DECIMAL);

        typeMapping.mapping(Db2Types.CHARACTER, StarRocksTypes.CHAR);
        typeMapping.mapping(Db2Types.VARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(Db2Types.GRAPHIC, StarRocksTypes.CHAR);
        typeMapping.mapping(Db2Types.VARGRAPHIC, StarRocksTypes.VARCHAR);

        typeMapping.mapping(Db2Types.CHAR_FOR_BIT_DATA, null);
        typeMapping.mapping(Db2Types.VARCHAR_FOR_BIT_DATA, null);

        typeMapping.mapping(Db2Types.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(Db2Types.TIME, StarRocksTypes.STRING);
        typeMapping.mapping(Db2Types.TIMESTAMP, StarRocksTypes.DATETIME);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
