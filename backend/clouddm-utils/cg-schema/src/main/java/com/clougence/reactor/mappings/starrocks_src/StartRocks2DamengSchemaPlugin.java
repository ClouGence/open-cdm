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
package com.clougence.reactor.mappings.starrocks_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.Dameng));

        binder.bindTableHandler(DsType.StarRocks, DsType.Dameng).to(new StarRocksTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, DmSqlTypes.TINYINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(StarRocksTypes.INT, DmSqlTypes.INT);
        typeMapping.mapping(StarRocksTypes.BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, DmSqlTypes.NUMERIC);
        typeMapping.mapping(StarRocksTypes.DOUBLE, DmSqlTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, DmSqlTypes.BIT);

        typeMapping.mapping(StarRocksTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(StarRocksTypes.STRING, DmSqlTypes.TEXT);

        typeMapping.mapping(StarRocksTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, DmSqlTypes.TIMESTAMP);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, DmSqlTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.BITMAP, DmSqlTypes.BIGINT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
