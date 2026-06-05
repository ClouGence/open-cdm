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

import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.reactor.handlers.importer.TiDBTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

/**
 * @Author: baili
 * @Date: 2023/02/15/20:33
 * @Description:
 */
public class TiDB2TiDBSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.TiDB, DsType.TiDB));
        mappingFoo(binder.bindFuncMapping(DsType.TiDB, DsType.TiDB));

        binder.bindTableHandler(DsType.TiDB, DsType.TiDB).to(new TiDBTableImporter());
    }

    private void mappingType(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(TiDBTypes.BIT, TiDBTypes.BIT);
        typeMapping.mapping(TiDBTypes.TINYINT, TiDBTypes.TINYINT);
        typeMapping.mapping(TiDBTypes.SMALLINT, TiDBTypes.SMALLINT);
        typeMapping.mapping(TiDBTypes.MEDIUMINT, TiDBTypes.MEDIUMINT);
        typeMapping.mapping(TiDBTypes.INT, TiDBTypes.INT);
        typeMapping.mapping(TiDBTypes.BIGINT, TiDBTypes.BIGINT);
        typeMapping.mapping(TiDBTypes.DECIMAL, TiDBTypes.DECIMAL);
        typeMapping.mapping(TiDBTypes.FLOAT, TiDBTypes.FLOAT);
        typeMapping.mapping(TiDBTypes.DOUBLE, TiDBTypes.DOUBLE);

        typeMapping.mapping(TiDBTypes.DATE, TiDBTypes.DATE);
        typeMapping.mapping(TiDBTypes.DATETIME, TiDBTypes.DATETIME);
        typeMapping.mapping(TiDBTypes.TIMESTAMP, TiDBTypes.TIMESTAMP);
        typeMapping.mapping(TiDBTypes.TIME, TiDBTypes.TIME);
        typeMapping.mapping(TiDBTypes.YEAR, TiDBTypes.YEAR);

        typeMapping.mapping(TiDBTypes.CHAR, TiDBTypes.CHAR);
        typeMapping.mapping(TiDBTypes.VARCHAR, TiDBTypes.VARCHAR);

        typeMapping.mapping(TiDBTypes.BINARY, TiDBTypes.BINARY);
        typeMapping.mapping(TiDBTypes.VARBINARY, TiDBTypes.VARBINARY);
        typeMapping.mapping(TiDBTypes.TINYBLOB, TiDBTypes.TINYBLOB);
        typeMapping.mapping(TiDBTypes.BLOB, TiDBTypes.BLOB);
        typeMapping.mapping(TiDBTypes.MEDIUMBLOB, TiDBTypes.MEDIUMBLOB);
        typeMapping.mapping(TiDBTypes.LONGBLOB, TiDBTypes.LONGBLOB);

        typeMapping.mapping(TiDBTypes.TINYTEXT, TiDBTypes.TINYTEXT);
        typeMapping.mapping(TiDBTypes.TEXT, TiDBTypes.TEXT);
        typeMapping.mapping(TiDBTypes.MEDIUMTEXT, TiDBTypes.MEDIUMTEXT);
        typeMapping.mapping(TiDBTypes.LONGTEXT, TiDBTypes.LONGTEXT);
        typeMapping.mapping(TiDBTypes.ENUM, TiDBTypes.ENUM);
        typeMapping.mapping(TiDBTypes.SET, TiDBTypes.SET);
        typeMapping.mapping(TiDBTypes.JSON, TiDBTypes.JSON);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(SchemaBinder.MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(SchemaBinder.MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "*");
    }

}
