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
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Dameng2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Dameng, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.Dameng, DsType.Dameng));

        binder.bindTableHandler(DsType.Dameng, DsType.Dameng).to(new DamengTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(DmSqlTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(DmSqlTypes.NCHAR, DmSqlTypes.NCHAR);
        typeMapping.mapping(DmSqlTypes.VARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(DmSqlTypes.NVARCHAR2, DmSqlTypes.NVARCHAR2);
        typeMapping.mapping(DmSqlTypes.TEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(DmSqlTypes.CLOB, DmSqlTypes.CLOB);
        typeMapping.mapping(DmSqlTypes.NUMERIC, DmSqlTypes.NUMERIC);
        typeMapping.mapping(DmSqlTypes.TINYINT, DmSqlTypes.TINYINT);
        typeMapping.mapping(DmSqlTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(DmSqlTypes.INT, DmSqlTypes.INT);
        typeMapping.mapping(DmSqlTypes.BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(DmSqlTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(DmSqlTypes.REAL, DmSqlTypes.REAL);
        typeMapping.mapping(DmSqlTypes.BIT, DmSqlTypes.BIT);
        typeMapping.mapping(DmSqlTypes.BINARY, DmSqlTypes.BINARY);
        typeMapping.mapping(DmSqlTypes.VARBINARY, DmSqlTypes.VARBINARY);
        typeMapping.mapping(DmSqlTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(DmSqlTypes.TIME, DmSqlTypes.TIME);
        typeMapping.mapping(DmSqlTypes.TIME_WITH_TIME_ZONE, DmSqlTypes.TIME_WITH_TIME_ZONE);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE);
        typeMapping.mapping(DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE, DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE);

        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR, DmSqlTypes.INTERVAL_YEAR);
        typeMapping.mapping(DmSqlTypes.INTERVAL_YEAR_TO_MONTH, DmSqlTypes.INTERVAL_YEAR_TO_MONTH);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MONTH, DmSqlTypes.INTERVAL_MONTH);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY, DmSqlTypes.INTERVAL_DAY);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_HOUR, DmSqlTypes.INTERVAL_DAY_TO_HOUR);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_MINUTE, DmSqlTypes.INTERVAL_DAY_TO_MINUTE);
        typeMapping.mapping(DmSqlTypes.INTERVAL_DAY_TO_SECOND, DmSqlTypes.INTERVAL_DAY_TO_SECOND);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR, DmSqlTypes.INTERVAL_HOUR);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_MINUTE, DmSqlTypes.INTERVAL_HOUR_TO_MINUTE);
        typeMapping.mapping(DmSqlTypes.INTERVAL_HOUR_TO_SECOND, DmSqlTypes.INTERVAL_HOUR_TO_SECOND);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE, DmSqlTypes.INTERVAL_MINUTE);
        typeMapping.mapping(DmSqlTypes.INTERVAL_MINUTE_TO_SECOND, DmSqlTypes.INTERVAL_MINUTE_TO_SECOND);
        typeMapping.mapping(DmSqlTypes.INTERVAL_SECOND, DmSqlTypes.INTERVAL_SECOND);
        typeMapping.mapping(DmSqlTypes.ROWID, DmSqlTypes.ROWID);

        typeMapping.mapping(DmSqlTypes.IMAGE, DmSqlTypes.IMAGE);
        typeMapping.mapping(DmSqlTypes.BFILE, DmSqlTypes.BFILE);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
