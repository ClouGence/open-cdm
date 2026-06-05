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

import com.clougence.adapter.gauss.GaussDBTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.GaussDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2GaussDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.GaussDB));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.GaussDB));

        binder.bindTableHandler(DsType.SqlServer, DsType.GaussDB).to(new SqlServerTableExporter(), new GaussDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, GaussDBTypes.BOOLEAN);
        typeMapping.mapping(SqlServerTypes.DECIMAL, GaussDBTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.NUMERIC, GaussDBTypes.NUMERIC);
        typeMapping.mapping(SqlServerTypes.SMALLINT, GaussDBTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, GaussDBTypes.SMALLINT); // sqlserver 0~255 , mysql -128~127 (unsigned 0~255)
        typeMapping.mapping(SqlServerTypes.INT, GaussDBTypes.INTEGER);
        typeMapping.mapping(SqlServerTypes.BIGINT, GaussDBTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, GaussDBTypes.MONEY);
        typeMapping.mapping(SqlServerTypes.MONEY, GaussDBTypes.MONEY);
        typeMapping.mapping(SqlServerTypes.FLOAT, GaussDBTypes.REAL);
        typeMapping.mapping(SqlServerTypes.REAL, GaussDBTypes.REAL);

        typeMapping.mapping(SqlServerTypes.DATE, GaussDBTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, GaussDBTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME2, GaussDBTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, GaussDBTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME, GaussDBTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.TIME, GaussDBTypes.TIME_WITHOUT_TIME_ZONE);

        typeMapping.mapping(SqlServerTypes.CHAR, GaussDBTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.VARCHAR, GaussDBTypes.CHARACTER_VARYING);
        typeMapping.mapping(SqlServerTypes.TEXT, GaussDBTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.NCHAR, GaussDBTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, GaussDBTypes.CHARACTER_VARYING);
        typeMapping.mapping(SqlServerTypes.NTEXT, GaussDBTypes.TEXT);

        typeMapping.mapping(SqlServerTypes.BINARY, GaussDBTypes.BYTEA);
        typeMapping.mapping(SqlServerTypes.VARBINARY, GaussDBTypes.BYTEA);
        typeMapping.mapping(SqlServerTypes.IMAGE, GaussDBTypes.BYTEA);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, GaussDBTypes.BIGINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, GaussDBTypes.BIGINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, GaussDBTypes.CHARACTER);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, GaussDBTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, GaussDBTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, GaussDBTypes.TEXT);
        typeMapping.mapping(SqlServerTypes.SYSNAME, GaussDBTypes.CHARACTER);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
