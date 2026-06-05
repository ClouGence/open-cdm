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
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class MySql2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.TiDB));

        binder.bindTableHandler(DsType.MySQL, DsType.TiDB).to(new TiDBTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, TiDBTypes.BIT);
        typeMapping.mapping(MySQLTypes.TINYINT, TiDBTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, TiDBTypes.MEDIUMINT);
        typeMapping.mapping(MySQLTypes.INT, TiDBTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, TiDBTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, TiDBTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, TiDBTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, TiDBTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, TiDBTypes.DATETIME);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, TiDBTypes.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, TiDBTypes.YEAR);

        typeMapping.mapping(MySQLTypes.CHAR, TiDBTypes.CHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, TiDBTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, TiDBTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, TiDBTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, TiDBTypes.TINYBLOB);
        typeMapping.mapping(MySQLTypes.BLOB, TiDBTypes.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, TiDBTypes.MEDIUMBLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, TiDBTypes.LONGBLOB);

        typeMapping.mapping(MySQLTypes.TINYTEXT, TiDBTypes.TINYTEXT);
        typeMapping.mapping(MySQLTypes.TEXT, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, TiDBTypes.MEDIUMTEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, TiDBTypes.LONGTEXT);
        typeMapping.mapping(MySQLTypes.ENUM, TiDBTypes.VARCHAR); // todo # "enum"
        typeMapping.mapping(MySQLTypes.SET, TiDBTypes.VARCHAR); // todo # "SET"
        typeMapping.mapping(MySQLTypes.JSON, TiDBTypes.JSON);

        typeMapping.mapping(MySQLTypes.GEOMETRY, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POINT, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.LINESTRING, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.POLYGON, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, TiDBTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, TiDBTypes.TEXT);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
