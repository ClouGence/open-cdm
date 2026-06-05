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

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.StarRocksTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class StartRocks2MySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.StarRocks, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.StarRocks, DsType.MySQL));

        binder.bindTableHandler(DsType.StarRocks, DsType.MySQL).to(new StarRocksTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(StarRocksTypes.TINYINT, MySQLTypes.TINYINT);
        typeMapping.mapping(StarRocksTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(StarRocksTypes.INT, MySQLTypes.INT);
        typeMapping.mapping(StarRocksTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.LARGEINT, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.DECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(StarRocksTypes.DOUBLE, MySQLTypes.DOUBLE);
        typeMapping.mapping(StarRocksTypes.FLOAT, MySQLTypes.FLOAT);
        typeMapping.mapping(StarRocksTypes.BOOLEAN, MySQLTypes.BIT);

        typeMapping.mapping(StarRocksTypes.CHAR, MySQLTypes.CHAR);
        typeMapping.mapping(StarRocksTypes.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(StarRocksTypes.STRING, MySQLTypes.TEXT);

        typeMapping.mapping(StarRocksTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(StarRocksTypes.DATETIME, MySQLTypes.DATETIME);

        typeMapping.mapping(StarRocksTypes.ARRAY, null);

        typeMapping.mapping(StarRocksTypes.HLL, MySQLTypes.BIGINT);
        typeMapping.mapping(StarRocksTypes.BITMAP, MySQLTypes.BIGINT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
