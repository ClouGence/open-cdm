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
package com.clougence.clouddm.ds.starrocks.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorUtils;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorHeader;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiVerify;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbPrimaryKey;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

/**
 * @author caishan
 * @date 2023-10-25 11:45:54
 * @version: 1.0
 */
public class SrDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        //SQLSyntaxErrorException: only support updating primary key table; primary key column cannot be updated
        //https://docs.starrocks.io/zh-cn/latest/sql-reference/sql-statements/data-manipulation/DELETE
        RdbPrimaryKey primaryKey = rdbTable.getPrimaryKey();
        if (primaryKey == null || !"PRIMARY KEY".equals(primaryKey.getName())) {
            headerCol.setUpdateReadOnly(true);
            //uk not unique on detailed model.
        } else {
            if (headerCol.getIsPk() || headerCol.getIsUk()) {
                headerCol.setUpdateReadOnly(true);
            }
            StarRocksTypes sqlType = (StarRocksTypes) colDef.getSqlType();
            switch (sqlType) {
                case ARRAY://unknown how to handle it
                case MAP:
                case STRUCT:
                case HLL:
                case JSON:
                case VARBINARY:
                    headerCol.setSpareWhere(false);
                    headerCol.setWhereKey(false);
                    break;
                default: {
                    if (sqlType.isBinary()) {
                        DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                        DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                        headerCol.setCheck(Collections.singletonList(hCheck));
                    }
                    break;
                }
            }
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String colName = dialect.fmtName(true, col.getName());
        switch ((StarRocksTypes) col.getSqlType()) {
            case BITMAP:
                return "bitmap_to_string(" + colName + ") as " + colName;
            case HLL:
                return "HLL_CARDINALITY(" + colName + ") as " + colName;
            case VARBINARY:
                return "HEX(" + colName + ") as " + colName;
            default:
                return colName;
        }
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        StarRocksTypes sqlType = (StarRocksTypes) col.getSqlType();
        switch (sqlType) {
            case BITMAP:
                return "bitmap_from_string(" + fmtDataValue(sqlType, value, true) + ")";
            case HLL:
                return "HLL_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            case VARBINARY:
                return "x" + fmtDataValue(sqlType, value, false);
            case JSON:
                return fmtDataValue(sqlType, value, true);
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }
        StarRocksTypes sqlType = (StarRocksTypes) col.getSqlType();
        switch (sqlType) {
            case BITMAP:
                return setCol + " = bitmap_from_string(" + fmtDataValue(sqlType, value, true) + ")";
            case HLL:
                return setCol + " = HLL_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            case VARBINARY:
                return setCol + " = x" + fmtDataValue(sqlType, value, false);
            case JSON:
                return setCol + " = " + fmtDataValue(sqlType, value, true);
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }

    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        StarRocksTypes sqlType = (StarRocksTypes) col.getSqlType();
        switch (sqlType) {
            case TINYINT:
            case BOOLEAN: {//type -> tinyint
                value = "110001".equals(value) ? "true" : "110000".equals(value) ? "false" : value;
                return whereCol + " = " + fmtDataValue(sqlType, value, true);
            }
            case BITMAP:
                return "bitmap_to_string(" + whereCol + ") = " + fmtDataValue(sqlType, value, true);
            case HLL:
                return whereCol + " = HLL_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            case VARBINARY:
                return whereCol + " = x" + fmtDataValue(sqlType, value, false);
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(StarRocksTypes type, String value, boolean asVarchar) {
        switch (type) {
            case ARRAY://unknown how to handle it
            case MAP:
            case STRUCT:
            case HLL:
            case JSON:
            case VARBINARY:
                return "'" + StringUtils.replace(value, "'", "''") + "'";
            default:
                break;
        }

        if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
