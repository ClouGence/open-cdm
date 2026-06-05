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
package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2StarRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.StarRocks));

        binder.bindTableHandler(DsType.Hana, DsType.StarRocks).to(new HanaTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, StarRocksTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, StarRocksTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, StarRocksTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, StarRocksTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, StarRocksTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, StarRocksTypes.BOOLEAN);

        typeMapping.mapping(HanaTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, StarRocksTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIMESTAMP, StarRocksTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIME, StarRocksTypes.STRING);

        typeMapping.mapping(HanaTypes.CHAR, StarRocksTypes.CHAR);
        typeMapping.mapping(HanaTypes.NCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, StarRocksTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.NCLOB, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.TEXT, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.BINTEXT, StarRocksTypes.STRING);

        typeMapping.mapping(HanaTypes.BLOB, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.BINARY, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.VARBINARY, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.ARRAY, StarRocksTypes.ARRAY);

        typeMapping.mapping(HanaTypes.ST_POINT, StarRocksTypes.STRING);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, StarRocksTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
