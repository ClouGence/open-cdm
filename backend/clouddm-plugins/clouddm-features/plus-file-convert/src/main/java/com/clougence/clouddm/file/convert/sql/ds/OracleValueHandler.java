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

import com.clougence.clouddm.file.convert.sql.SqlRowData;
import com.clougence.utils.io.result.EntityType;

public class OracleValueHandler extends DefaultValueHandler {

    public static final OracleValueHandler HANDLER = new OracleValueHandler();

    @Override
    public void handle(SqlRowData row, String data, int i) {
        Byte b = row.getEntityTypes().get(i);
        switch (b) {
            case EntityType.Date: {
                row.getRowData().set(i, "DATE " + addQuotation(data));
                break;
            }
            case EntityType.Time:
            case EntityType.TimeZ:
            case EntityType.DateTimeZ:
            case EntityType.DateTime: {
                row.getRowData().set(i, "TIMESTAMP " + addQuotation(data));
                break;
            }
            default: {
                super.handle(row, data, i);
            }
        }
    }
}
