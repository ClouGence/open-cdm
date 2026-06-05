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
import com.clougence.adapter.ob.obfororacle.ObForOracleTypes;
import com.clougence.reactor.handlers.exporter.MySqlTableExporter;
import com.clougence.reactor.handlers.importer.ObForOracleTableImporter;
import com.clougence.reactor.handlers.special.MySql2ObForOracleHandler;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @author chunlin create time is 2024/9/11
 */
public class MySql2ObForOracleSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.MySQL, DsType.ObForOracle));
        mappingFoo(binder.bindFuncMapping(DsType.MySQL, DsType.ObForOracle));

        binder.bindTableHandler(DsType.MySQL, DsType.ObForOracle).to(new MySql2ObForOracleHandler(), new MySqlTableExporter(), new ObForOracleTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(MySQLTypes.BIT, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.TINYINT, ObForOracleTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.SMALLINT, ObForOracleTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.MEDIUMINT, ObForOracleTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.INT, ObForOracleTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.BIGINT, ObForOracleTypes.NUMBER_BIGINT);
        typeMapping.mapping(MySQLTypes.DECIMAL, ObForOracleTypes.NUMBER_DECIMAL);
        typeMapping.mapping(MySQLTypes.FLOAT, ObForOracleTypes.NUMBER_DECIMAL);
        typeMapping.mapping(MySQLTypes.DOUBLE, ObForOracleTypes.NUMBER_DECIMAL);

        typeMapping.mapping(MySQLTypes.DATE, ObForOracleTypes.DATE);
        typeMapping.mapping(MySQLTypes.DATETIME, ObForOracleTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIMESTAMP, ObForOracleTypes.TIMESTAMP);
        typeMapping.mapping(MySQLTypes.TIME, ObForOracleTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.YEAR, ObForOracleTypes.NUMBER_BIGINT);

        typeMapping.mapping(MySQLTypes.CHAR, ObForOracleTypes.CHAR);
        typeMapping.mapping(MySQLTypes.VARCHAR, ObForOracleTypes.VARCHAR2);

        typeMapping.mapping(MySQLTypes.BINARY, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.VARBINARY, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.TINYBLOB, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.BLOB, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMBLOB, ObForOracleTypes.BLOB);
        typeMapping.mapping(MySQLTypes.LONGBLOB, ObForOracleTypes.BLOB);

        typeMapping.mapping(MySQLTypes.TINYTEXT, ObForOracleTypes.CLOB);
        typeMapping.mapping(MySQLTypes.TEXT, ObForOracleTypes.CLOB);
        typeMapping.mapping(MySQLTypes.MEDIUMTEXT, ObForOracleTypes.CLOB);
        typeMapping.mapping(MySQLTypes.LONGTEXT, ObForOracleTypes.CLOB);
        typeMapping.mapping(MySQLTypes.ENUM, ObForOracleTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.SET, ObForOracleTypes.VARCHAR2);
        typeMapping.mapping(MySQLTypes.JSON, ObForOracleTypes.CLOB);

        typeMapping.mapping(MySQLTypes.GEOMETRY, null/*ObForOracleTypes.SDO_GEOMETRY*/); // need function, https://blog.csdn.net/weixin_33921089/article/details/86324465
        typeMapping.mapping(MySQLTypes.POINT, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.LINESTRING, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.POLYGON, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTIPOINT, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.GEOMETRY_COLLECTION, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.GEOM_COLLECTION, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTILINESTRING, null/*ObForOracleTypes.SDO_GEOMETRY*/);
        typeMapping.mapping(MySQLTypes.MULTIPOLYGON, null/*ObForOracleTypes.SDO_GEOMETRY*/);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
        funMapping.mapping("current_timestamp", "sysdate");
        funMapping.mapping("current_timestamp(1)", "sysdate");
        funMapping.mapping("current_timestamp(2)", "sysdate");
        funMapping.mapping("current_timestamp(3)", "sysdate");
        funMapping.mapping("current_timestamp(4)", "sysdate");
        funMapping.mapping("current_timestamp(5)", "sysdate");
        funMapping.mapping("current_timestamp(6)", "sysdate");
        funMapping.mapping("current_time", "sysdate");
        funMapping.mapping("current_date", "sysdate");
        funMapping.mapping("now()", "sysdate");
        funMapping.mapping("sysdate()", "sysdate");
    }

}
