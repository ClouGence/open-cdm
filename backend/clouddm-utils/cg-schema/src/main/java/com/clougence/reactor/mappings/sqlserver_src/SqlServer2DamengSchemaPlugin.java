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
package com.clougence.reactor.mappings.sqlserver_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.Dameng));

        binder.bindTableHandler(DsType.SqlServer, DsType.Dameng).to(new SqlServerTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, DmSqlTypes.TINYINT);
        typeMapping.mapping(SqlServerTypes.DECIMAL, DmSqlTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.NUMERIC, DmSqlTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.SMALLINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, DmSqlTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.INT, DmSqlTypes.INT);
        typeMapping.mapping(SqlServerTypes.BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, DmSqlTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.MONEY, DmSqlTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.REAL, DmSqlTypes.FLOAT);

        typeMapping.mapping(SqlServerTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME2, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.DATETIME, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.TIME, DmSqlTypes.TIME);

        typeMapping.mapping(SqlServerTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.VARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.TEXT, DmSqlTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.NCHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, DmSqlTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.NTEXT, DmSqlTypes.TEXT);

        typeMapping.mapping(SqlServerTypes.BINARY, null);
        typeMapping.mapping(SqlServerTypes.VARBINARY, null);
        typeMapping.mapping(SqlServerTypes.IMAGE, null);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, DmSqlTypes.TEXT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, DmSqlTypes.TEXT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, DmSqlTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, DmSqlTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, DmSqlTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, DmSqlTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.SYSNAME, DmSqlTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
