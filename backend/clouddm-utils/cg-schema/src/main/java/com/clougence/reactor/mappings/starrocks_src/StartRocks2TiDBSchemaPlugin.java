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

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.TiDB));

        binder.bindTableHandler(DsType.StarRocks, DsType.TiDB).to(new StarRocksTableExporter(), new TiDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, TiDBTypes.TINYINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(StarRocksTypes.INT, TiDBTypes.INT);
        typeMapping.mapping(StarRocksTypes.BIGINT, TiDBTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, TiDBTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(StarRocksTypes.DOUBLE, TiDBTypes.DOUBLE);
        typeMapping.mapping(StarRocksTypes.FLOAT, TiDBTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, TiDBTypes.BIT);

        typeMapping.mapping(StarRocksTypes.CHAR, TiDBTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, TiDBTypes.VARCHAR);
        typeMapping.mapping(StarRocksTypes.STRING, TiDBTypes.TEXT);

        typeMapping.mapping(StarRocksTypes.DATE, TiDBTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, TiDBTypes.DATETIME);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, TiDBTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.BITMAP, TiDBTypes.BIGINT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
