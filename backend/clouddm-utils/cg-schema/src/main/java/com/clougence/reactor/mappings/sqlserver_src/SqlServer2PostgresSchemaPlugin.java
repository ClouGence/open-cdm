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

import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.SqlServer, DsType.PostgreSQL).to(new SqlServerTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, PostgresTypes.BOOLEAN);
        typeMapping.mapping(SqlServerTypes.DECIMAL, PostgresTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.NUMERIC, PostgresTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.SMALLINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, PostgresTypes.SMALLINT); // sqlserver 0~255 , mysql -128~127 (unsigned 0~255)
        typeMapping.mapping(SqlServerTypes.INT, PostgresTypes.INTEGER);
        typeMapping.mapping(SqlServerTypes.BIGINT, PostgresTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, PostgresTypes.MONEY);
        typeMapping.mapping(SqlServerTypes.MONEY, PostgresTypes.MONEY);
        typeMapping.mapping(SqlServerTypes.FLOAT, PostgresTypes.REAL);
        typeMapping.mapping(SqlServerTypes.REAL, PostgresTypes.REAL);

        typeMapping.mapping(SqlServerTypes.DATE, PostgresTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, PostgresTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME2, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.TIME, PostgresTypes.TIME_WITHOUT_TIME_ZONE);

        typeMapping.mapping(SqlServerTypes.CHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.VARCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(SqlServerTypes.TEXT, PostgresTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.NCHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(SqlServerTypes.NTEXT, PostgresTypes.TEXT);

        typeMapping.mapping(SqlServerTypes.BINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(SqlServerTypes.VARBINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(SqlServerTypes.IMAGE, PostgresTypes.BYTEA);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, PostgresTypes.BIGINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, PostgresTypes.BIGINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, PostgresTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, PostgresTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, PostgresTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, PostgresTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.SYSNAME, PostgresTypes.CHARACTER);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
