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
package com.clougence.reactor.mappings.mysql_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.SqlServerTableImporter;
import com.clougence.reactor.handlers.special.MySql2SqlServerHandler;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2SqlServerSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.SqlServer));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.SqlServer));

        binder.bindTableHandler(DsType.MySQL, DsType.SqlServer).to(new MySqlTableExporter(), new SqlServerTableImporter(), new MySql2SqlServerHandler());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, SqlServerTypes.VARBINARY); // sqlserver bit only include (1、0 or NULL)
        typeMapping.mapping(MySQLTypes.TINYINT, SqlServerTypes.SMALLINT);// TINYINT has -1, so SMALLINT better safe
        typeMapping.mapping(MySQLTypes.SMALLINT, SqlServerTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, SqlServerTypes.INT);
        typeMapping.mapping(MySQLTypes.INT, SqlServerTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, SqlServerTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, SqlServerTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, SqlServerTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, SqlServerTypes.FLOAT);

        typeMapping.mapping(MySQLTypes.DATE, SqlServerTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, SqlServerTypes.DATETIME2);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, SqlServerTypes.DATETIME2);
        typeMapping.mapping(MySQLTypes.TIME, SqlServerTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.YEAR, SqlServerTypes.INT);

        typeMapping.mapping(MySQLTypes.CHAR, SqlServerTypes.NCHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, SqlServerTypes.NVARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, SqlServerTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, SqlServerTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, SqlServerTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.BLOB, SqlServerTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, SqlServerTypes.IMAGE);
        typeMapping.mapping(MySQLTypes.LONGBLOB, SqlServerTypes.IMAGE);

        typeMapping.mapping(MySQLTypes.TINYTEXT, SqlServerTypes.NTEXT);
        typeMapping.mapping(MySQLTypes.TEXT, SqlServerTypes.NTEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, SqlServerTypes.NTEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, SqlServerTypes.NTEXT);
        typeMapping.mapping(MySQLTypes.ENUM, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(MySQLTypes.SET, SqlServerTypes.NVARCHAR);
        typeMapping.mapping(MySQLTypes.JSON, SqlServerTypes.NTEXT);

        typeMapping.mapping(MySQLTypes.GEOMETRY, null); // SqlServerTypes.GEOMETRY
        typeMapping.mapping(MySQLTypes.POINT, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LINESTRING, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POLYGON, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, SqlServerTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, SqlServerTypes.TEXT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp", "getdate()");
        funMapping.mapping("current_timestamp(1)", "getdate()");
        funMapping.mapping("current_timestamp(2)", "getdate()");
        funMapping.mapping("current_timestamp(3)", "getdate()");
        funMapping.mapping("current_timestamp(4)", "getdate()");
        funMapping.mapping("current_timestamp(5)", "getdate()");
        funMapping.mapping("current_timestamp(6)", "getdate()");
        funMapping.mapping("curdate()", "getdate()");
        funMapping.mapping("curtime()", "getdate()");
        funMapping.mapping("current_time", "getdate()");
        funMapping.mapping("current_date", "getdate()");
        funMapping.mapping("now()", "getdate()");
        funMapping.mapping("sysdate()", "getdate()");
    }
}
