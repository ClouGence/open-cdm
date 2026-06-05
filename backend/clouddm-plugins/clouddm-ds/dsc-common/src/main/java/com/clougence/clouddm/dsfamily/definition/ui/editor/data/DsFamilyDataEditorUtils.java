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
package com.clougence.clouddm.dsfamily.definition.ui.editor.data;

import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.StringUtils;

public class DsFamilyDataEditorUtils {

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return dialect.fmtName(true, col.getName());
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        } else {
            return fmtDataValue(col.getSqlType(), value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        } else {
            return setCol + " = " + fmtDataValue(col.getSqlType(), value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        } else {
            return whereCol + " = " + fmtDataValue(col.getSqlType(), value);
        }
    }

    public static String fmtDataValue(FieldType sqlType, String value) {
        if (sqlType.isString() || sqlType.hasDate() || sqlType.hasTime() || sqlType.isArray()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
