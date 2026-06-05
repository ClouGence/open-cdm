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
import com.clougence.adapter.ob.obformysql.ObForMySQLMainVersion;
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @author wanshao create time is 2022/2/15
 **/
public class MySql2OceanBaseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.OceanBase));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.OceanBase));

        binder.bindTableHandler(DsType.MySQL, DsType.OceanBase).to(new MySqlTableExporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, ObForMySQLTypes.BIT);
        typeMapping.mapping(MySQLTypes.TINYINT, ObForMySQLTypes.TINYINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, ObForMySQLTypes.SMALLINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, ObForMySQLTypes.MEDIUMINT);
        typeMapping.mapping(MySQLTypes.INT, ObForMySQLTypes.INT);
        typeMapping.mapping(MySQLTypes.BIGINT, ObForMySQLTypes.BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, ObForMySQLTypes.FLOAT);
        typeMapping.mapping(MySQLTypes.DOUBLE, ObForMySQLTypes.DOUBLE);

        typeMapping.mapping(MySQLTypes.DATE, ObForMySQLTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, ObForMySQLTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, ObForMySQLTypes.TIME);
        typeMapping.mapping(MySQLTypes.YEAR, ObForMySQLTypes.YEAR);

        typeMapping.mapping(MySQLTypes.CHAR, ObForMySQLTypes.CHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, ObForMySQLTypes.VARCHAR);

        typeMapping.mapping(MySQLTypes.BINARY, ObForMySQLTypes.BINARY);
        typeMapping.mapping(MySQLTypes.VARBINARY, ObForMySQLTypes.VARBINARY);
        typeMapping.mapping(MySQLTypes.TINYBLOB, ObForMySQLTypes.TINYBLOB);
        typeMapping.mapping(MySQLTypes.BLOB, ObForMySQLTypes.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, ObForMySQLTypes.MEDIUMBLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, ObForMySQLTypes.LONGBLOB);

        typeMapping.mapping(MySQLTypes.TINYTEXT, ObForMySQLTypes.TINYTEXT);
        typeMapping.mapping(MySQLTypes.TEXT, ObForMySQLTypes.TEXT);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, ObForMySQLTypes.MEDIUMTEXT);
        typeMapping.mapping(MySQLTypes.LONGTEXT, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(MySQLTypes.ENUM, ObForMySQLTypes.ENUM);
        typeMapping.mapping(MySQLTypes.SET, ObForMySQLTypes.SET);
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(MySQLTypes.GEOMETRY, null);
        typeMapping.mapping(MySQLTypes.POINT, null);
        typeMapping.mapping(MySQLTypes.LINESTRING, null);
        typeMapping.mapping(MySQLTypes.POLYGON, null);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, null);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, null);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, null);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, null);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_4_0); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_4_1); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_4_2); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_4_3); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_4_4); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_5_0); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_5_1); // 3.2.3+
        typeMapping.mapping(MySQLTypes.JSON, ObForMySQLTypes.JSON, ObForMySQLMainVersion.OceanBase_5_2); // 3.2.3+
    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
