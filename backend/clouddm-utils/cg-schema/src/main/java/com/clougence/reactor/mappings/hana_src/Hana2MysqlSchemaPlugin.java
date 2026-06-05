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
package com.clougence.reactor.mappings.hana_src;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2MysqlSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.MySQL));

        binder.bindTableHandler(DsType.Hana, DsType.MySQL).to(new HanaTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, MySQLTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, MySQLTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, MySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, MySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, MySQLTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, MySQLTypes.BIT);

        typeMapping.mapping(HanaTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, MySQLTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIMESTAMP, MySQLTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIME, MySQLTypes.TIME);

        typeMapping.mapping(HanaTypes.CHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, MySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, MySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.NCLOB, MySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.TEXT, MySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.BINTEXT, MySQLTypes.LONGTEXT);

        typeMapping.mapping(HanaTypes.BLOB, MySQLTypes.LONGBLOB);
        typeMapping.mapping(HanaTypes.BINARY, MySQLTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.VARBINARY, MySQLTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.ARRAY, null);

        typeMapping.mapping(HanaTypes.ST_POINT, MySQLTypes.POINT);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, MySQLTypes.GEOMETRY);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
