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
package com.clougence.reactor.mappings.oracle_src;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.DamengTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2DamengSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.Dameng));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.Dameng));

        binder.bindTableHandler(DsType.Oracle, DsType.Dameng).to(new OracleTableExporter(), new DamengTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, DmSqlTypes.CHAR);
        typeMapping.mapping(OracleSqlTypes.NCHAR, DmSqlTypes.NCHAR);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, DmSqlTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, DmSqlTypes.NVARCHAR2);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, DmSqlTypes.NVARCHAR2);
        typeMapping.mapping(OracleSqlTypes.LONG, DmSqlTypes.TEXT);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, DmSqlTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, DmSqlTypes.NUMERIC);
        typeMapping.mapping(OracleSqlTypes.FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, DmSqlTypes.FLOAT);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, DmSqlTypes.FLOAT);

        typeMapping.mapping(OracleSqlTypes.CLOB, DmSqlTypes.CLOB);
        typeMapping.mapping(OracleSqlTypes.NCLOB, DmSqlTypes.CLOB);
        typeMapping.mapping(OracleSqlTypes.BLOB, null);
        typeMapping.mapping(OracleSqlTypes.BFILE, null);

        typeMapping.mapping(OracleSqlTypes.DATE, DmSqlTypes.DATE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, DmSqlTypes.TIMESTAMP);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, DmSqlTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, DmSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, DmSqlTypes.INTERVAL_YEAR_TO_MONTH);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, DmSqlTypes.INTERVAL_DAY_TO_SECOND);

        typeMapping.mapping(OracleSqlTypes.RAW, null);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, null);

        typeMapping.mapping(OracleSqlTypes.ROWID, DmSqlTypes.VARCHAR);
        typeMapping.mapping(OracleSqlTypes.UROWID, null);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, DmSqlTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, DmSqlTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.XDBURITYPE, null);
        typeMapping.mapping(OracleSqlTypes.DBURITYPE, null);

        typeMapping.mapping(OracleSqlTypes.SDO_GEOMETRY, null);
        typeMapping.mapping(OracleSqlTypes.SDO_TOPO_GEOMETRY, null);
        typeMapping.mapping(OracleSqlTypes.SDO_GEORASTER, null);

        typeMapping.mapping(OracleSqlTypes.ORDAUDIO, null);
        typeMapping.mapping(OracleSqlTypes.ORDDICOM, null);
        typeMapping.mapping(OracleSqlTypes.ORDDOC, null);
        typeMapping.mapping(OracleSqlTypes.ORDIMAGE, null);
        typeMapping.mapping(OracleSqlTypes.ORDVIDEO, null);
        typeMapping.mapping(OracleSqlTypes.SI_AVERAGE_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_COLOR_HISTOGRAM, null);
        typeMapping.mapping(OracleSqlTypes.SI_FEATURE_LIST, null);
        typeMapping.mapping(OracleSqlTypes.SI_POSITIONAL_COLOR, null);
        typeMapping.mapping(OracleSqlTypes.SI_STILL_IMAGE, null);
        typeMapping.mapping(OracleSqlTypes.SI_TEXTURE, null);

        mappingTypeWithVersion(typeMapping);
    }

    private void mappingTypeWithVersion(MappingBindingBuilder<FieldType> typeMapping) {

    }

    private void mappingFoo(MappingBindingBuilder<String> funMapping) {
        funMapping.mapping("*", "");
    }
}
