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

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2StartRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.StarRocks));

        binder.bindTableHandler(DsType.SqlServer, DsType.StarRocks).to(new SqlServerTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, StarRocksTypes.TINYINT);
        typeMapping.mapping(SqlServerTypes.DECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.NUMERIC, StarRocksTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(SqlServerTypes.INT, StarRocksTypes.INT);
        typeMapping.mapping(SqlServerTypes.BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, StarRocksTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.MONEY, StarRocksTypes.DECIMAL);
        typeMapping.mapping(SqlServerTypes.FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(SqlServerTypes.REAL, StarRocksTypes.DOUBLE);

        typeMapping.mapping(SqlServerTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, StarRocksTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.DATETIME2, StarRocksTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, StarRocksTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.DATETIME, StarRocksTypes.DATETIME);
        typeMapping.mapping(SqlServerTypes.TIME, StarRocksTypes.STRING);

        typeMapping.mapping(SqlServerTypes.CHAR, StarRocksTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.VARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.TEXT, StarRocksTypes.STRING);
        typeMapping.mapping(SqlServerTypes.NCHAR, StarRocksTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.NTEXT, StarRocksTypes.STRING);

        typeMapping.mapping(SqlServerTypes.BINARY, null);
        typeMapping.mapping(SqlServerTypes.VARBINARY, null);
        typeMapping.mapping(SqlServerTypes.IMAGE, null);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, StarRocksTypes.LARGEINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, StarRocksTypes.LARGEINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, StarRocksTypes.VARCHAR);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, StarRocksTypes.STRING);
        typeMapping.mapping(SqlServerTypes.GEOMETRY, StarRocksTypes.STRING);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, StarRocksTypes.STRING);
        typeMapping.mapping(SqlServerTypes.SYSNAME, StarRocksTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
