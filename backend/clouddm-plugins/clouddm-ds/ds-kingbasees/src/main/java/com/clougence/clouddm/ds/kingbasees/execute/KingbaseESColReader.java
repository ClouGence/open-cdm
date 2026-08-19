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
package com.clougence.clouddm.ds.kingbasees.execute;

import java.sql.JDBCType;
import java.util.Locale;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.postgres.execute.PgColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

public class KingbaseESColReader extends PgColReader {

    @Override
    public ValueFetcher readColumn(String col, ColMetaData metadata) {
        String typeName = StringUtils.trimToEmpty(metadata.getColumnType()).toLowerCase(Locale.ROOT);
        String mappedType = switch (typeName) {
            case "nchar" -> "character";
            case "nvarchar", "nvarchar2", "varchar2" -> "character varying";
            case "number" -> "numeric";
            case "double" -> "double precision";
            case "bit" -> {
                if (metadata.getJdbcType() == JDBCType.BOOLEAN || metadata.getPrecision() == 1) {
                    yield "boolean";
                }
                yield "bit";
            }
            case "datetime", "datetime2", "smalldatetime" -> "timestamp without time zone";
            case "binary", "varbinary", "image" -> "bytea";
            default -> typeName;
        };
        if (mappedType.equals(typeName)) {
            return super.readColumn(col, metadata);
        }

        ColMetaData mappedMetadata = new ColMetaData();
        mappedMetadata.setCatalog(metadata.getCatalog());
        mappedMetadata.setSchema(metadata.getSchema());
        mappedMetadata.setTable(metadata.getTable());
        mappedMetadata.setColumn(metadata.getColumn());
        mappedMetadata.setIndex(metadata.getIndex());
        mappedMetadata.setColumnType(mappedType);
        mappedMetadata.setJdbcType(metadata.getJdbcType());
        mappedMetadata.setPrecision(metadata.getPrecision());
        return super.readColumn(col, mappedMetadata);
    }
}
