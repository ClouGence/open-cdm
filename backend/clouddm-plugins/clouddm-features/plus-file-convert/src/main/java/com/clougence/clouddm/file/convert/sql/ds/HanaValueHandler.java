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
package com.clougence.clouddm.file.convert.sql.ds;

import com.clougence.adapter.hana.HanaSqlValues;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.io.result.EntityType;

public class HanaValueHandler implements SqlValueHandler {
    public static final HanaValueHandler HANDLER = new HanaValueHandler();

    @Override
    public void handle(SqlRowData row, String data, int index) {
        HanaTypes type;
        switch (row.getEntityTypes().get(index)) {
            case EntityType.Boolean:
                type = HanaTypes.BOOLEAN;
                break;
            case EntityType.Byte:
            case EntityType.Short:
            case EntityType.Integer:
            case EntityType.Long:
            case EntityType.BigInteger:
                type = HanaTypes.BIGINT;
                break;
            case EntityType.Float:
            case EntityType.Double:
            case EntityType.BigDecimal:
                type = HanaTypes.DECIMAL;
                break;
            case EntityType.Date:
                type = HanaTypes.DATE;
                break;
            case EntityType.Time:
                type = HanaTypes.TIME;
                break;
            case EntityType.DateTime:
                type = HanaTypes.TIMESTAMP;
                break;
            case EntityType.Bytes:
                type = HanaTypes.BLOB;
                break;
            case EntityType.String:
                type = HanaTypes.NCLOB;
                break;
            default:
                throw ThirdPartyApiException.as().with(new IllegalArgumentException("Unsupported HANA SQL export value type"));
        }
        try {
            row.getRowData().set(index, HanaSqlValues.literal(type, data));
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
