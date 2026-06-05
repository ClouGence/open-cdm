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
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.reactor.handlers.exporter.HanaTableExporter;
import com.clougence.reactor.handlers.importer.ObTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Hana2OceanBaseSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Hana, DsType.OceanBase));
        mappingFoo(binder.bindFuncMapping(DsType.Hana, DsType.OceanBase));

        binder.bindTableHandler(DsType.Hana, DsType.OceanBase).to(new HanaTableExporter(), new ObTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(HanaTypes.TINYINT, ObForMySQLTypes.TINYINT);
        typeMapping.mapping(HanaTypes.SMALLINT, ObForMySQLTypes.SMALLINT);
        typeMapping.mapping(HanaTypes.INTEGER, ObForMySQLTypes.INT);
        typeMapping.mapping(HanaTypes.BIGINT, ObForMySQLTypes.BIGINT);
        typeMapping.mapping(HanaTypes.DECIMAL, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.SMALLDECIMAL, ObForMySQLTypes.DECIMAL);
        typeMapping.mapping(HanaTypes.REAL, ObForMySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.FLOAT, ObForMySQLTypes.DOUBLE);
        typeMapping.mapping(HanaTypes.DOUBLE, ObForMySQLTypes.DOUBLE);

        typeMapping.mapping(HanaTypes.BOOLEAN, ObForMySQLTypes.BIT);

        typeMapping.mapping(HanaTypes.DATE, ObForMySQLTypes.DATE);
        typeMapping.mapping(HanaTypes.SECONDDATE, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIMESTAMP, ObForMySQLTypes.DATETIME);
        typeMapping.mapping(HanaTypes.TIME, ObForMySQLTypes.TIME);

        typeMapping.mapping(HanaTypes.CHAR, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NCHAR, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.VARCHAR, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.NVARCHAR, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.ALPHANUM, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.SHORTTEXT, ObForMySQLTypes.VARCHAR);
        typeMapping.mapping(HanaTypes.CLOB, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.NCLOB, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.TEXT, ObForMySQLTypes.LONGTEXT);
        typeMapping.mapping(HanaTypes.BINTEXT, ObForMySQLTypes.LONGTEXT);

        typeMapping.mapping(HanaTypes.BLOB, ObForMySQLTypes.LONGBLOB);
        typeMapping.mapping(HanaTypes.BINARY, ObForMySQLTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.VARBINARY, ObForMySQLTypes.VARBINARY);
        typeMapping.mapping(HanaTypes.ARRAY, null);

        typeMapping.mapping(HanaTypes.ST_POINT, null);
        typeMapping.mapping(HanaTypes.ST_GEOMETRY, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
