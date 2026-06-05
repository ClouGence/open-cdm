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
import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.StarRocksTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2StartRocksSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.StarRocks));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.StarRocks));

        binder.bindTableHandler(DsType.MySQL, DsType.StarRocks).to(new MySqlTableExporter(), new StarRocksTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, StarRocksTypes.INT);
        typeMapping.mapping(MySQLTypes.TINYINT, StarRocksTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, StarRocksTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, StarRocksTypes.INT);
        typeMapping.mapping(MySQLTypes.INT, StarRocksTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, StarRocksTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, StarRocksTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, StarRocksTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, StarRocksTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, StarRocksTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, StarRocksTypes.DATETIME);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, StarRocksTypes.DATETIME);
        typeMapping.mapping(MySQLTypes.TIME, StarRocksTypes.STRING);
        typeMapping.mapping(MySQLTypes.YEAR, StarRocksTypes.INT);

        typeMapping.mapping(MySQLTypes.CHAR, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, StarRocksTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, StarRocksTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, StarRocksTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, null);
        typeMapping.mapping(MySQLTypes.BLOB, null);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, null);
        typeMapping.mapping(MySQLTypes.LONGBLOB, null);

        typeMapping.mapping(MySQLTypes.TINYTEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.TEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.LONGTEXT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.ENUM, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.SET, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.JSON, StarRocksTypes.JSON);

        typeMapping.mapping(MySQLTypes.GEOMETRY, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POINT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.LINESTRING, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.POLYGON, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, StarRocksTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, StarRocksTypes.VARCHAR);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
