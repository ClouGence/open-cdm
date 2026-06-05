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
package com.clougence.clouddm.ds.dameng.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.dameng.DmSqlTypes;
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
public class DmDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        DmSqlTypes sqlType = (DmSqlTypes) colDef.getSqlType();
        switch (sqlType) {
            case BIT: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_BINARY;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_BINARY));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case IMAGE: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case ROWID:
                headerCol.setInsertReadOnly(true);
                headerCol.setUpdateReadOnly(true);
                headerCol.setWhereKey(false);
                break;
            case BFILE:
                headerCol.setWhereKey(false);
                headerCol.setSpareWhere(false);
            default:
                headerCol.setWhereKey(false);
                break;
        }

        //https://eco.dameng.com/document/dm/zh-cn/pm/insertion-deletion-modification#5.5%20%E4%BC%AA%E5%88%97%E7%9A%84%E4%BD%BF%E7%94%A8
        if (headerCol.getAutoincrement()) {
            headerCol.setUpdateReadOnly(true);
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {

        return dialect.fmtName(true, col.getName());
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }
        DmSqlTypes dmSqlTypes = (DmSqlTypes) col.getSqlType();

        if (dmSqlTypes.isInterval() && value.toUpperCase().startsWith("INTERVAL")) {
            return fmtDataValue(dmSqlTypes, value, false);
        }

        switch (dmSqlTypes) {
            case INTERVAL_YEAR_TO_MONTH:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " YEAR TO MONTH";
            case INTERVAL_YEAR:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " YEAR";
            case INTERVAL_MONTH:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MONTH";
            case INTERVAL_DAY:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY";
            case INTERVAL_DAY_TO_HOUR:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO HOUR";
            case INTERVAL_DAY_TO_MINUTE:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO MINUTE";
            case INTERVAL_DAY_TO_SECOND:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO SECOND";
            case INTERVAL_HOUR:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR";
            case INTERVAL_HOUR_TO_MINUTE:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR TO MINUTE";
            case INTERVAL_HOUR_TO_SECOND:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR TO SECOND";
            case INTERVAL_MINUTE:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MINUTE";
            case INTERVAL_MINUTE_TO_SECOND:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MINUTE TO SECOND";
            case INTERVAL_SECOND:
                return "INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " SECOND";
            case BFILE: //directory:file.ext  || BFILENAME('directory','file.ext')
                if (value.contains(":")) {
                    String[] split = value.split(":");
                    if (split.length != 2) {
                        return fmtDataValue(dmSqlTypes, value, true);
                    } else {
                        return "BFILENAME('" + split[0] + "','" + split[1] + "')";
                    }
                }
                return fmtDataValue(dmSqlTypes, value, false);
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }
        DmSqlTypes dmSqlTypes = (DmSqlTypes) col.getSqlType();

        if (dmSqlTypes.isInterval() && value.toUpperCase().startsWith("INTERVAL")) {
            return setCol + " = " + fmtDataValue(dmSqlTypes, value, false);
        }

        switch (dmSqlTypes) {
            case INTERVAL_YEAR_TO_MONTH:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " YEAR TO MONTH";
            case INTERVAL_YEAR:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " YEAR";
            case INTERVAL_MONTH:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MONTH";
            case INTERVAL_DAY:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY";
            case INTERVAL_DAY_TO_HOUR:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO HOUR";
            case INTERVAL_DAY_TO_MINUTE:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO MINUTE";
            case INTERVAL_DAY_TO_SECOND:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " DAY TO SECOND";
            case INTERVAL_HOUR:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR";
            case INTERVAL_HOUR_TO_MINUTE:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR TO MINUTE";
            case INTERVAL_HOUR_TO_SECOND:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " HOUR TO SECOND";
            case INTERVAL_MINUTE:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MINUTE";
            case INTERVAL_MINUTE_TO_SECOND:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " MINUTE TO SECOND";
            case INTERVAL_SECOND:
                return setCol + " = INTERVAL " + fmtDataValue(dmSqlTypes, value, true) + " SECOND";
            case BFILE: //directory:file.ext  || BFILENAME('directory','file.ext')
                if (value.contains(":")) {
                    String[] split = value.split(":");
                    if (split.length != 2) {
                        return setCol + " = " + fmtDataValue(dmSqlTypes, value, true);
                    } else {
                        return setCol + " = BFILENAME('" + split[0] + "','" + split[1] + "')";
                    }
                }
                return setCol + " = " + fmtDataValue(dmSqlTypes, value, false);
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }

    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    private static String fmtDataValue(DmSqlTypes type, String value, boolean asVarchar) {
        if (asVarchar || type.isInterval() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
