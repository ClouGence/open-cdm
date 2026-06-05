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

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.adapter.postgre.PostgresTypes;
import com.clougence.reactor.handlers.exporter.OracleTableExporter;
import com.clougence.reactor.handlers.importer.PostgresTableImporter;
import com.clougence.schema.DsType;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaBinder.MappingBindingBuilder;
import com.clougence.schema.SchemaPlugin;
import com.clougence.schema.metadata.FieldType;

public class Oracle2PostgresSchemaPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        mappingType(binder.bindTypeMapping(DsType.Oracle, DsType.PostgreSQL));
        mappingFoo(binder.bindFuncMapping(DsType.Oracle, DsType.PostgreSQL));

        binder.bindTableHandler(DsType.Oracle, DsType.PostgreSQL).to(new OracleTableExporter(), new PostgresTableImporter());
    }

    private void mappingType(MappingBindingBuilder<FieldType> typeMapping) {
        typeMapping.mapping(OracleSqlTypes.CHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(OracleSqlTypes.NCHAR, PostgresTypes.CHARACTER);
        typeMapping.mapping(OracleSqlTypes.VARCHAR2, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(OracleSqlTypes.NVARCHAR2, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(OracleSqlTypes.LONG, PostgresTypes.TEXT);

        typeMapping.mapping(OracleSqlTypes.NUMBER_BIGINT, PostgresTypes.BIGINT);
        typeMapping.mapping(OracleSqlTypes.NUMBER_DECIMAL, PostgresTypes.NUMERIC);
        typeMapping.mapping(OracleSqlTypes.FLOAT, PostgresTypes.REAL);
        typeMapping.mapping(OracleSqlTypes.BINARY_FLOAT, PostgresTypes.REAL);
        typeMapping.mapping(OracleSqlTypes.BINARY_DOUBLE, PostgresTypes.DOUBLE_PRECISION);

        typeMapping.mapping(OracleSqlTypes.CLOB, PostgresTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.NCLOB, PostgresTypes.TEXT);
        typeMapping.mapping(OracleSqlTypes.BLOB, null);
        typeMapping.mapping(OracleSqlTypes.BFILE, null);

        typeMapping.mapping(OracleSqlTypes.DATE, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP, PostgresTypes.TIMESTAMP_WITHOUT_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_TIME_ZONE, PostgresTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.TIMESTAMP_WITH_LOCAL_TIME_ZONE, PostgresTypes.TIMESTAMP_WITH_TIME_ZONE);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_YEAR_TO_MONTH, null);
        typeMapping.mapping(OracleSqlTypes.INTERVAL_DAY_TO_SECOND, null);

        typeMapping.mapping(OracleSqlTypes.RAW, null);
        typeMapping.mapping(OracleSqlTypes.LONG_RAW, null);

        typeMapping.mapping(OracleSqlTypes.ROWID, PostgresTypes.CHARACTER_VARYING);
        typeMapping.mapping(OracleSqlTypes.UROWID, null);

        typeMapping.mapping(OracleSqlTypes.OBJECT, null);
        typeMapping.mapping(OracleSqlTypes.REF, null);
        typeMapping.mapping(OracleSqlTypes.VARRAY, null);
        typeMapping.mapping(OracleSqlTypes.NESTED_TABLE, null);

        typeMapping.mapping(OracleSqlTypes.PLSQL_BOOLEAN, null);
        typeMapping.mapping(OracleSqlTypes.ANYTYPE, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATA, null);
        typeMapping.mapping(OracleSqlTypes.ANYDATASET, null);

        typeMapping.mapping(OracleSqlTypes.XMLTYPE, PostgresTypes.XML);
        typeMapping.mapping(OracleSqlTypes.HTTPURITYPE, PostgresTypes.CHARACTER_VARYING);
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
        funMapping.mapping("sysdate", "now()");
        funMapping.mapping("systimestamp", "now()");
    }
}
