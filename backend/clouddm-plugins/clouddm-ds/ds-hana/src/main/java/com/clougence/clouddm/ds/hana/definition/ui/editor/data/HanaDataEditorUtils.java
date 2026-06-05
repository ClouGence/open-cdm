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
package com.clougence.clouddm.ds.hana.definition.ui.editor.data;

import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.utils.StringUtils;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public class HanaDataEditorUtils implements DsDataEditorI18nKeys {

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }

        HanaTypes sqlType = (HanaTypes) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return setCol + " = " + "TO_DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case TIME:
                return setCol + " = " + "TO_TIME(" + fmtDataValue(sqlType, value, true) + ")";
            case TIMESTAMP:
                return setCol + " = " + "TO_TIMESTAMP(" + fmtDataValue(sqlType, value, true) + ")";
            case BLOB:
                return setCol + " = " + "BLOB(x" + fmtDataValue(sqlType, value, true) + ")";
            case BINARY:
                return setCol + " = " + "BINARY(x" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return setCol + " = " + fmtDataValue(sqlType, value, false);
        }
    }

    private static String fmtDataValue(HanaTypes type, String value, boolean asVarchar) {
        if (type.isBinary() || type.equals(HanaTypes.BINARY)) {
            if (value.startsWith("0x") || value.startsWith("0X")) {
                value = value.substring(2);
            }
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
