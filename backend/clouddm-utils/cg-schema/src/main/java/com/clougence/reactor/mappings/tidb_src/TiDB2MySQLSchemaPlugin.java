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
package com.clougence.reactor.mappings.tidb_src;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.exporter.TiDBTableExporter;
import com.clougence.reactor.handlers.importer.MySqlTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @Author: baili
 * @Date: 2023/02/15/17:31
 * @Description:
 */
public class TiDB2MySQLSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.MySQL));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.MySQL));

        binder.bindTableHandler(DsType.TiDB, DsType.MySQL).to(new TiDBTableExporter(), new MySqlTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, MySQLTypes.BIT);
        typeMapping.mapping(TiDBTypes.TINYINT, MySQLTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, MySQLTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, MySQLTypes.MEDIUMINT);
        typeMapping.mapping(TiDBTypes.INT, MySQLTypes.INT);
        typeMapping.mapping(TiDBTypes.BIGINT, MySQLTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, MySQLTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.FLOAT, MySQLTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.DOUBLE, MySQLTypes.DOUBLE);

        typeMapping.mapping(TiDBTypes.DATE, MySQLTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, MySQLTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, MySQLTypes.TIMESTAMP);
        typeMapping.mapping(TiDBTypes.TIME, MySQLTypes.TIME);
        typeMapping.mapping(TiDBTypes.YEAR, MySQLTypes.YEAR);

        typeMapping.mapping(TiDBTypes.CHAR, MySQLTypes.CHAR);
        typeMapping.mapping(TiDBTypes.VARCHAR, MySQLTypes.VARCHAR);

        typeMapping.mapping(TiDBTypes.BINARY, MySQLTypes.BINARY);
        typeMapping.mapping(TiDBTypes.VARBINARY, MySQLTypes.VARBINARY);
        typeMapping.mapping(TiDBTypes.TINYBLOB, MySQLTypes.TINYBLOB);
        typeMapping.mapping(TiDBTypes.BLOB, MySQLTypes.BLOB);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, MySQLTypes.MEDIUMBLOB);
        typeMapping.mapping(TiDBTypes.LONGBLOB, MySQLTypes.LONGBLOB);

        typeMapping.mapping(TiDBTypes.TINYTEXT, MySQLTypes.TINYTEXT);
        typeMapping.mapping(TiDBTypes.TEXT, MySQLTypes.TEXT);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, MySQLTypes.MEDIUMTEXT);
        typeMapping.mapping(TiDBTypes.LONGTEXT, MySQLTypes.LONGTEXT);
        typeMapping.mapping(TiDBTypes.ENUM, MySQLTypes.ENUM);
        typeMapping.mapping(TiDBTypes.SET, MySQLTypes.SET);
        typeMapping.mapping(TiDBTypes.JSON, MySQLTypes.JSON);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
