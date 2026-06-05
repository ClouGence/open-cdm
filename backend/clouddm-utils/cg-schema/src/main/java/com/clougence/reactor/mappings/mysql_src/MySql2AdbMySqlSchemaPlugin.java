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

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.AdbMySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2AdbMySqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.AdbForMySQL));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.AdbForMySQL));

        binder.bindTableHandler(DsType.MySQL, DsType.AdbForMySQL).to(new MySqlTableExporter(), new AdbMySqlTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.TINYINT, AdbMySQLTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, AdbMySQLTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, AdbMySQLTypes.INT);
        typeMapping.mapping(MySQLTypes.INT, AdbMySQLTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, AdbMySQLTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, AdbMySQLTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, AdbMySQLTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, AdbMySQLTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, AdbMySQLTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, AdbMySQLTypes.DATETIME);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, AdbMySQLTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, AdbMySQLTypes.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, AdbMySQLTypes.INT);

        typeMapping.mapping(MySQLTypes.CHAR, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, AdbMySQLTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.BLOB, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, AdbMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.LONGBLOB, AdbMySQLTypes.BINARY);

        typeMapping.mapping(MySQLTypes.TINYTEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.TEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.LONGTEXT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.ENUM, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.SET, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.JSON, AdbMySQLTypes.JSON);

        typeMapping.mapping(MySQLTypes.GEOMETRY, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POINT, AdbMySQLTypes.POINT);
        typeMapping.mapping(MySQLTypes.LINESTRING, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POLYGON, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, AdbMySQLTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, AdbMySQLTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp(1)", "current_timestamp");
        funMapping.mapping("current_timestamp(2)", "current_timestamp");
        funMapping.mapping("current_timestamp(3)", "current_timestamp");
        funMapping.mapping("current_timestamp(4)", "current_timestamp");
        funMapping.mapping("current_timestamp(5)", "current_timestamp");
        funMapping.mapping("current_timestamp(6)", "current_timestamp");
    }

}
