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
package com.clougence.clouddm.ds.tidb.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.tidb.TiDBAttributeNames;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorUtils;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorHeader;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiVerify;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

/**
 * @author caishan
 * @date 2023-10-25 11:45:54
 * @version: 1.0
 */
public class TiDBDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        TiDBTypes sqlType = (TiDBTypes) colDef.getSqlType();
        switch (sqlType) {
            case SET:
            case ENUM: {
                //rdbmy_ct -> enum('enum1','enum2','enum3','enum4','enum5','enum6','enum7','enum8')
                //rdbmy_ct -> set('set1','set2','set3','set4','set5','set6','set7','set8')
                String options = colDef.getAttribute(TiDBAttributeNames.COLUMN_TYPE);
                headerCol.setOption(splitOptionStr(options));
                break;
            }
            case BIT: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_BINARY;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_BINARY));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case BINARY:
            case VARBINARY:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_BINARY));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            default: {
                break;
            }
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return dialect.fmtName(true, col.getName());
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        TiDBTypes sqlType = (TiDBTypes) col.getSqlType();
        switch (sqlType) {
            case BIT:
                if (StringUtils.equalsIgnoreCase("true", value) || StringUtils.equalsIgnoreCase("on", value)) {
                    return "0b1";
                } else if (StringUtils.equalsIgnoreCase("false", value) || StringUtils.equalsIgnoreCase("off", value)) {
                    return "0b0";
                } else {
                    return "0b" + fmtDataValue(sqlType, value, false);
                }
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }

        TiDBTypes sqlType = (TiDBTypes) col.getSqlType();
        switch (sqlType) {
            case BIT:
                return setCol + " = 0b" + fmtDataValue(sqlType, value, false);
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        TiDBTypes sqlType = (TiDBTypes) col.getSqlType();
        switch (sqlType) {
            case BIT:
                return whereCol + " = 0b" + fmtDataValue(sqlType, value, false);
            case JSON:
                return whereCol + " = cast(" + fmtDataValue(sqlType, value, true) + " as json)";
            case DATE:
                return whereCol + " = DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case FLOAT:
                // abs( colName - ? ) < 1e-7
                // ref https://github.com/TFdream/blog/issues/378
                int scale = col.getNumericScale() == null ? 7 : col.getNumericScale();
                return String.format("abs(%s - %s) < 1e-%s", col.getName(), fmtDataValue(sqlType, value, true), scale);
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    protected static String[] splitOptionStr(String str) {
        String replace = str.replace("'", "");
        int index = replace.indexOf("(");
        int lastIndexOf = replace.lastIndexOf(")");
        if (index >= 0 && lastIndexOf >= 0) {
            //set1,set2,set3,set4,set5,set6,set7,set8
            String substring = replace.substring(index + 1, lastIndexOf);
            return substring.split(",");
        }
        return null;
    }

    private static String fmtDataValue(TiDBTypes type, String value, boolean asVarchar) {
        if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
