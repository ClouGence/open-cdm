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

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.reactor.handlers.importer.ClickHouseTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2ClickHouseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.ClickHouse));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.ClickHouse));
        binder.bindTableHandler(DsType.Dameng, DsType.ClickHouse).to(new ClickHouseTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.NCHAR, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.VARCHAR, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.TEXT, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.CLOB, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.NUMERIC, ClickHouseTypes.Decimal);
        typeMapping.mapping(DmSqlTypes.TINYINT, ClickHouseTypes.Int8);
        typeMapping.mapping(DmSqlTypes.SMALLINT, ClickHouseTypes.Int16);
        typeMapping.mapping(DmSqlTypes.INT, ClickHouseTypes.Int32);
        typeMapping.mapping(DmSqlTypes.BIGINT, ClickHouseTypes.Int64);
        typeMapping.mapping(DmSqlTypes.FLOAT, ClickHouseTypes.Float32);
        typeMapping.mapping(DmSqlTypes.REAL, ClickHouseTypes.Float64);
        typeMapping.mapping(DmSqlTypes.BIT, ClickHouseTypes.Int64);
        typeMapping.mapping(DmSqlTypes.BINARY, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.VARBINARY, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.DATE, ClickHouseTypes.Date);
        typeMapping.mapping(DmSqlTypes.TIME, ClickHouseTypes.String);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, ClickHouseTypes.DateTime64);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, ClickHouseTypes.DateTime64);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, ClickHouseTypes.DateTime64);

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
