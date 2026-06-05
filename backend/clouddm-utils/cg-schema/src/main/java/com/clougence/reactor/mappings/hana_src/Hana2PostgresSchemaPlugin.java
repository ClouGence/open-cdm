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
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.Hana, DsType.PostgreSQL).to(new HanaTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.SMALLINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, PostgresTypes.INTEGER);
        typeMapping.mapping(HanaTypes.BIGINT, PostgresTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, PostgresTypes.NUMERIC);
        //typeMapping.mapping(HanaTypes.SMALLDECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, PostgresTypes.NUMERIC);
        typeMapping.mapping(HanaTypes.FLOAT, PostgresTypes.NUMERIC);
        typeMapping.mapping(HanaTypes.DOUBLE, PostgresTypes.NUMERIC);

        typeMapping.mapping(HanaTypes.BOOLEAN, PostgresTypes.BIT);

        typeMapping.mapping(HanaTypes.DATE, PostgresTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(HanaTypes.TIMESTAMP, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(HanaTypes.TIME, PostgresTypes.CHARACTER_VARYING);

        typeMapping.mapping(HanaTypes.CHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.NCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.VARCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.NVARCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.ALPHANUM, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.SHORTTEXT, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(HanaTypes.CLOB, PostgresTypes.TEXT);
        typeMapping.mapping(HanaTypes.NCLOB, PostgresTypes.TEXT);
        typeMapping.mapping(HanaTypes.TEXT, PostgresTypes.TEXT);
        typeMapping.mapping(HanaTypes.BINTEXT, PostgresTypes.TEXT);

        typeMapping.mapping(HanaTypes.BLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(HanaTypes.BINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(HanaTypes.VARBINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(HanaTypes.ARRAY, null);

        typeMapping.mapping(HanaTypes.ST_POINT, PostgresTypes.POINT);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, PostgresTypes.TEXT);
        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
