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
package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class TiDB2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.TiDB, DsType.PostgreSQL).to(new TiDBTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

        typeMapping.mapping(TiDBTypes.BIT, PostgresTypes.BIT);
        typeMapping.mapping(TiDBTypes.TINYINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, PostgresTypes.INTEGER);
        typeMapping.mapping(TiDBTypes.INT, PostgresTypes.INTEGER);
        typeMapping.mapping(TiDBTypes.BIGINT, PostgresTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, PostgresTypes.NUMERIC);
        typeMapping.mapping(TiDBTypes.FLOAT, PostgresTypes.REAL);
        typeMapping.mapping(TiDBTypes.DOUBLE, PostgresTypes.DOUBLE_PRECISION);

        typeMapping.mapping(TiDBTypes.DATE, PostgresTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(TiDBTypes.TIME, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(TiDBTypes.YEAR, PostgresTypes.INTEGER);

        typeMapping.mapping(TiDBTypes.CHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(TiDBTypes.VARCHAR, PostgresTypes.CHARACTER_VARYING);

        typeMapping.mapping(TiDBTypes.BINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(TiDBTypes.VARBINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(TiDBTypes.TINYBLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(TiDBTypes.BLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(TiDBTypes.LONGBLOB, PostgresTypes.BYTEA);

        typeMapping.mapping(TiDBTypes.TINYTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(TiDBTypes.TEXT, PostgresTypes.TEXT);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(TiDBTypes.LONGTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(TiDBTypes.ENUM, PostgresTypes.TEXT);
        typeMapping.mapping(TiDBTypes.SET, PostgresTypes.CHARACTER_VARYING_ARRAY);
        typeMapping.mapping(TiDBTypes.JSON, PostgresTypes.JSON);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
