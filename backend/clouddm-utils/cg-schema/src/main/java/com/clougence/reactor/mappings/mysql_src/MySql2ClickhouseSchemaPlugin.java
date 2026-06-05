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

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.ClickHouseTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2ClickhouseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.ClickHouse));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.ClickHouse));

        binder.bindTableHandler(DsType.MySQL, DsType.ClickHouse).to(new MySqlTableExporter(), new ClickHouseTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, ClickHouseTypes.Int64);
        typeMapping.mapping(MySQLTypes.TINYINT, ClickHouseTypes.Int8);
        typeMapping.mapping(MySQLTypes.SMALLINT, ClickHouseTypes.Int16);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, ClickHouseTypes.Int32);
        typeMapping.mapping(MySQLTypes.INT, ClickHouseTypes.Int32);
        typeMapping.mapping(MySQLTypes.BIGINT, ClickHouseTypes.Int64);
        typeMapping.mapping(MySQLTypes.DECIMAL, ClickHouseTypes.Decimal);
        typeMapping.mapping(MySQLTypes.FLOAT, ClickHouseTypes.Float32);
        typeMapping.mapping(MySQLTypes.DOUBLE, ClickHouseTypes.Float64);

        typeMapping.mapping(MySQLTypes.DATE, ClickHouseTypes.Date);
        typeMapping.mapping(MySQLTypes.DATETIME, ClickHouseTypes.DateTime64);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, ClickHouseTypes.DateTime64);
        typeMapping.mapping(MySQLTypes.TIME, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.YEAR, ClickHouseTypes.Int32);

        typeMapping.mapping(MySQLTypes.CHAR, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.VARCHAR, ClickHouseTypes.String);

        typeMapping.mapping(MySQLTypes.BINARY, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.VARBINARY, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.TINYBLOB, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.BLOB, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.LONGBLOB, ClickHouseTypes.String);

        typeMapping.mapping(MySQLTypes.TINYTEXT, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.TEXT, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.LONGTEXT, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.ENUM, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.SET, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.JSON, ClickHouseTypes.String);

        typeMapping.mapping(MySQLTypes.GEOMETRY, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.POINT, ClickHouseTypes.String); //ClickHouseTypes.Point is experimental
        typeMapping.mapping(MySQLTypes.LINESTRING, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.POLYGON, ClickHouseTypes.String); //ClickHouseTypes.Polygon is experimental
        typeMapping.mapping(MySQLTypes.MULTIPOINT, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, ClickHouseTypes.String);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, ClickHouseTypes.String); // ClickHouseTypes.MultiPolygon is experimental

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
