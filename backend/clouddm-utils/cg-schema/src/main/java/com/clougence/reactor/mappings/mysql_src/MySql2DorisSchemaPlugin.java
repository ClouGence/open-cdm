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

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.DorisTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2DorisSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.Doris));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.Doris));

        binder.bindTableHandler(DsType.MySQL, DsType.Doris).to(new MySqlTableExporter(), new DorisTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, DorisTypes.INT);
        typeMapping.mapping(MySQLTypes.TINYINT, DorisTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, DorisTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, DorisTypes.INT);
        typeMapping.mapping(MySQLTypes.INT, DorisTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, DorisTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, DorisTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, DorisTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, DorisTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, DorisTypes.DATEV2);
        typeMapping.mapping(MySQLTypes.DATETIME, DorisTypes.DATETIMEV2);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, DorisTypes.DATETIMEV2);
        typeMapping.mapping(MySQLTypes.TIME, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.YEAR, DorisTypes.INT);

        typeMapping.mapping(MySQLTypes.CHAR, DorisTypes.VARCHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, DorisTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, null);
        typeMapping.mapping(MySQLTypes.VARBINARY, null);
        typeMapping.mapping(MySQLTypes.TINYBLOB, null);
        typeMapping.mapping(MySQLTypes.BLOB, null);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, null);
        typeMapping.mapping(MySQLTypes.LONGBLOB, null);

        typeMapping.mapping(MySQLTypes.TINYTEXT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.TEXT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.LONGTEXT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.ENUM, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.SET, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.JSON, DorisTypes.STRING);

        typeMapping.mapping(MySQLTypes.GEOMETRY, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.POINT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.LINESTRING, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.POLYGON, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, DorisTypes.STRING);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, DorisTypes.STRING);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }

}
