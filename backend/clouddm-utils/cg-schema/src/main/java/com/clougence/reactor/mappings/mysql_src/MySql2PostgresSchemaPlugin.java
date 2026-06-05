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
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.MySQL, DsType.PostgreSQL).to(new MySqlTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, PostgresTypes.BIT);
        typeMapping.mapping(MySQLTypes.TINYINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, PostgresTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, PostgresTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.INT, PostgresTypes.INTEGER);
        typeMapping.mapping(MySQLTypes.BIGINT, PostgresTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, PostgresTypes.NUMERIC);
        typeMapping.mapping(MySQLTypes.FLOAT, PostgresTypes.REAL);
        typeMapping.mapping(MySQLTypes.DOUBLE, PostgresTypes.DOUBLE_PRECISION);

        typeMapping.mapping(MySQLTypes.DATE, PostgresTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(MySQLTypes.TIME, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(MySQLTypes.YEAR, PostgresTypes.INTEGER);

        typeMapping.mapping(MySQLTypes.CHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(MySQLTypes.VARCHAR, PostgresTypes.CHARACTER_VARYING);

        typeMapping.mapping(MySQLTypes.BINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.VARBINARY, PostgresTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.TINYBLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.BLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, PostgresTypes.BYTEA);
        typeMapping.mapping(MySQLTypes.LONGBLOB, PostgresTypes.BYTEA);

        typeMapping.mapping(MySQLTypes.TINYTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.TEXT, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.ENUM, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.SET, PostgresTypes.CHARACTER_VARYING_ARRAY);
        typeMapping.mapping(MySQLTypes.JSON, PostgresTypes.JSON);

        typeMapping.mapping(MySQLTypes.GEOMETRY, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POINT, PostgresTypes.POINT);
        typeMapping.mapping(MySQLTypes.LINESTRING, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POLYGON, PostgresTypes.POLYGON);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, PostgresTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, PostgresTypes.TEXT);

        //for ai virtual column.
        typeMapping.mapping(MySQLTypes.VECTOR, PostgresTypes.VECTOR);
        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp", "localtimestamp");
        funMapping.mapping("current_timestamp(1)", "localtimestamp(1)");
        funMapping.mapping("current_timestamp(2)", "localtimestamp(2)");
        funMapping.mapping("current_timestamp(3)", "localtimestamp(3)");
        funMapping.mapping("current_timestamp(4)", "localtimestamp(4)");
        funMapping.mapping("current_timestamp(5)", "localtimestamp(5)");
        funMapping.mapping("current_timestamp(6)", "localtimestamp(6)");
        funMapping.mapping("curdate()", "current_date");
        funMapping.mapping("curtime()", "localtime");
        funMapping.mapping("current_date", "current_date");
        funMapping.mapping("current_time", "localtime");
        funMapping.mapping("now()", "localtimestamp");
        funMapping.mapping("sysdate()", "localtimestamp");
    }
}
