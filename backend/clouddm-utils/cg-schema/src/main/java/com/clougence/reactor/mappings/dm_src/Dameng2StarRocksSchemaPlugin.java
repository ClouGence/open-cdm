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
package com.clougence.reactor.mappings.dm_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2StarRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.StarRocks));

        binder.bindTableHandler(DsType.Dameng, DsType.StarRocks).to(new StarRocksTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NCHAR, StarRocksTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.VARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, StarRocksTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.TEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.CLOB, StarRocksTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NUMERIC, StarRocksTypes.DECIMAL);
        typeMapping.mapping(DmSqlTypes.TINYINT, StarRocksTypes.TINYINT);
        typeMapping.mapping(DmSqlTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(DmSqlTypes.INT, StarRocksTypes.INT);
        typeMapping.mapping(DmSqlTypes.BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(DmSqlTypes.FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(DmSqlTypes.REAL, StarRocksTypes.DOUBLE);
        typeMapping.mapping(DmSqlTypes.BIT, StarRocksTypes.INT);
        typeMapping.mapping(DmSqlTypes.BINARY, StarRocksTypes.VARBINARY);
        typeMapping.mapping(DmSqlTypes.VARBINARY, StarRocksTypes.VARBINARY);
        typeMapping.mapping(DmSqlTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(DmSqlTypes.TIME, StarRocksTypes.STRING);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, StarRocksTypes.DATETIME);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, StarRocksTypes.DATETIME);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, StarRocksTypes.DATETIME);

        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR_TO_MONTH, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MONTH, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_HOUR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE_TO_SECOND, null);
        typeMapping.mapping(DmSqlTypes.INTERVAL_SECOND, null);
        typeMapping.mapping(DmSqlTypes.ROWID, null);

        typeMapping.mapping(DmSqlTypes.IMAGE, null);
        typeMapping.mapping(DmSqlTypes.BFILE, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
