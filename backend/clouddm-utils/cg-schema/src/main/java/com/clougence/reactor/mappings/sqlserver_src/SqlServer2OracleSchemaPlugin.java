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

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.SqlServerTableExporter;
import com.clougence.reactor.handlers.importer.OracleTableImporter;
import com.clougence.reactor.handlers.special.SqlServer2OracleHandler;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class SqlServer2OracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.SqlServer, DsType.Oracle));
        mappingFoo(binder.bindFuncMapping(DsType.SqlServer, DsType.Oracle));

        binder.bindTableHandler(DsType.SqlServer, DsType.Oracle).to(new SqlServerTableExporter(), new OracleTableImporter(), new SqlServer2OracleHandler());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(SqlServerTypes.BIT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(SqlServerTypes.DECIMAL, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(SqlServerTypes.NUMERIC, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(SqlServerTypes.SMALLINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(SqlServerTypes.TINYINT, OracleSqlTypes.NUMBER_BIGINT); // sqlserver 0~255 , mysql -128~127 (unsigned 0~255)
        typeMapping.mapping(SqlServerTypes.INT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(SqlServerTypes.BIGINT, OracleSqlTypes.NUMBER_BIGINT);
        typeMapping.mapping(SqlServerTypes.SMALLMONEY, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(SqlServerTypes.MONEY, OracleSqlTypes.NUMBER_DECIMAL);
        typeMapping.mapping(SqlServerTypes.FLOAT, OracleSqlTypes.BINARY_FLOAT);
        typeMapping.mapping(SqlServerTypes.REAL, OracleSqlTypes.BINARY_FLOAT);

        typeMapping.mapping(SqlServerTypes.DATE, OracleSqlTypes.DATE);
        typeMapping.mapping(SqlServerTypes.DATETIMEOFFSET, OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(SqlServerTypes.DATETIME2, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.SMALLDATETIME, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.DATETIME, OracleSqlTypes.TIMESTAMP);
        typeMapping.mapping(SqlServerTypes.TIME, OracleSqlTypes.VARCHAR2);

        typeMapping.mapping(SqlServerTypes.CHAR, OracleSqlTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.VARCHAR, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(SqlServerTypes.TEXT, OracleSqlTypes.CLOB);
        typeMapping.mapping(SqlServerTypes.NCHAR, OracleSqlTypes.CHAR);
        typeMapping.mapping(SqlServerTypes.NVARCHAR, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(SqlServerTypes.NTEXT, OracleSqlTypes.CLOB);

        typeMapping.mapping(SqlServerTypes.BINARY, OracleSqlTypes.BLOB);
        typeMapping.mapping(SqlServerTypes.VARBINARY, OracleSqlTypes.BLOB);
        typeMapping.mapping(SqlServerTypes.IMAGE, OracleSqlTypes.BLOB);

        typeMapping.mapping(SqlServerTypes.TIMESTAMP, OracleSqlTypes.NUMBER_BIGINT); // Unsigned
        typeMapping.mapping(SqlServerTypes.ROWVERSION, OracleSqlTypes.NUMBER_BIGINT);// Unsigned

        typeMapping.mapping(SqlServerTypes.HIERARCHYID, null);
        typeMapping.mapping(SqlServerTypes.UNIQUEIDENTIFIER, OracleSqlTypes.VARCHAR2);
        typeMapping.mapping(SqlServerTypes.SQL_VARIANT, null);
        typeMapping.mapping(SqlServerTypes.XML, OracleSqlTypes.CLOB); /* XMLTYPE write is very slow.*/
        typeMapping.mapping(SqlServerTypes.GEOMETRY, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(SqlServerTypes.GEOGRAPHY, null/*OracleSqlTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(SqlServerTypes.SYSNAME, OracleSqlTypes.VARCHAR2);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        //funMapping.mapping("getdate()", "current_timestamp");
    }

}
