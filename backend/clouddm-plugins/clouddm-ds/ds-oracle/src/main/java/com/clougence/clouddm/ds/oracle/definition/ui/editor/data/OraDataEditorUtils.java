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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.oracle.OracleSqlTypes;
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
public class OraDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        OracleSqlTypes sqlType = (OracleSqlTypes) colDef.getSqlType();
        switch (sqlType) {
            case BLOB:
            case RAW:
            case LONG_RAW: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case REF:
            case VARRAY:
            case OBJECT:
            case NESTED_TABLE:
            case PLSQL_BOOLEAN:
            case SI_AVERAGE_COLOR:
            case SI_COLOR_HISTOGRAM:
            case SI_FEATURE_LIST:
            case SI_POSITIONAL_COLOR:
            case SI_STILL_IMAGE:
            case ANYTYPE:
            case ANYDATA:
            case ANYDATASET:
            case SDO_GEOMETRY:
            case SDO_TOPO_GEOMETRY:
            case SDO_GEORASTER:
            case ORDAUDIO:
            case ORDDICOM:
            case ORDDOC:
            case ORDIMAGE:
            case ORDVIDEO:
            case DBURITYPE:
            case HTTPURITYPE:
            case XDBURITYPE:
            case SI_COLOR:
            case SI_TEXTURE:
            case BFILE:
                headerCol.setInsertReadOnly(true);
                headerCol.setUpdateReadOnly(true);
                headerCol.setSpareWhere(false);
                headerCol.setWhereKey(false);
                break;
            default:
                headerCol.setWhereKey(false);
                break;
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return dialect.fmtName(true, col.getName());
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        OracleSqlTypes sqlType = (OracleSqlTypes) col.getSqlType();
        switch (sqlType) {
            case BLOB:
            case RAW:
            case LONG_RAW:
                if (value.startsWith("0x") || value.startsWith("0X")) {
                    value = value.substring(2);
                }
                return fmtDataValue(sqlType, value, true);
            case INTERVAL_YEAR_TO_MONTH:
                return "INTERVAL " + fmtDataValue(sqlType, value, true) + " YEAR TO MONTH";
            case INTERVAL_DAY_TO_SECOND:
                return "INTERVAL " + fmtDataValue(sqlType, value, true) + " DAY TO SECOND";
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return "TIMESTAMP " + fmtDataValue(sqlType, value, true);
            case XMLTYPE:
                return "XMLTYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case HTTPURITYPE:
                return "HTTPURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case XDBURITYPE:
                return "XDBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case DBURITYPE:
                return "DBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case BFILE: //directory:file.ext  || BFILENAME('directory','file.ext')
                if (value.contains(":")) {
                    String[] split = value.split(":");
                    if (split.length != 2) {
                        return fmtDataValue(sqlType, value, true);
                    } else {
                        return "BFILENAME('" + split[0] + "','" + split[1] + "')";
                    }
                }
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

        OracleSqlTypes sqlType = (OracleSqlTypes) col.getSqlType();
        switch (sqlType) {
            case BLOB:
            case RAW:
            case LONG_RAW:
                if (value.startsWith("0x") || value.startsWith("0X")) {
                    value = value.substring(2);
                }
                return setCol + " =" + fmtDataValue(sqlType, value, true);
            case INTERVAL_YEAR_TO_MONTH:
                return setCol + " = INTERVAL " + fmtDataValue(sqlType, value, true) + " YEAR TO MONTH";
            case INTERVAL_DAY_TO_SECOND:
                return setCol + " = INTERVAL " + fmtDataValue(sqlType, value, true) + " DAY TO SECOND";
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return setCol + " = TIMESTAMP " + fmtDataValue(sqlType, value, true);
            case XMLTYPE:
                return setCol + " = XMLTYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case HTTPURITYPE:
                return setCol + " = HTTPURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case XDBURITYPE:
                return setCol + " = XDBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case DBURITYPE:
                return setCol + " = DBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case BFILE: //directory:file.ext  || BFILENAME('directory','file.ext')
                if (value.contains(":")) {
                    String[] split = value.split(":");
                    if (split.length != 2) {
                        return setCol + " =" + fmtDataValue(sqlType, value, true);
                    } else {
                        return setCol + " = BFILENAME('" + split[0] + "','" + split[1] + "')";
                    }
                }
                return fmtDataValue(sqlType, value, true);
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        OracleSqlTypes sqlType = (OracleSqlTypes) col.getSqlType();
        switch (sqlType) {
            case BLOB:
            case RAW:
            case LONG_RAW:
                if (value.startsWith("0x") || value.startsWith("0X")) {
                    value = value.substring(2);
                }
                return whereCol + " =" + fmtDataValue(sqlType, value, true);
            case INTERVAL_YEAR_TO_MONTH:
                return whereCol + " = INTERVAL " + fmtDataValue(sqlType, value, true) + " YEAR TO MONTH";
            case INTERVAL_DAY_TO_SECOND:
                return whereCol + " = INTERVAL " + fmtDataValue(sqlType, value, true) + " DAY TO SECOND";
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                return whereCol + " = TIMESTAMP " + fmtDataValue(sqlType, value, true);
            case XMLTYPE:
                return whereCol + " = XMLTYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case HTTPURITYPE:
                return whereCol + " = HTTPURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case XDBURITYPE:
                return whereCol + " = XDBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case DBURITYPE:
                return whereCol + " = DBURITYPE(" + fmtDataValue(sqlType, value, true) + ")";
            case BFILE: //directory:file.ext  || BFILENAME('directory','file.ext')
                if (value.contains(":")) {
                    String[] split = value.split(":");
                    if (split.length != 2) {
                        return whereCol + " = " + fmtDataValue(sqlType, value, true);
                    } else {
                        return whereCol + " = BFILENAME('" + split[0] + "','" + split[1] + "')";
                    }
                }
                return fmtDataValue(sqlType, value, true);
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(OracleSqlTypes type, String value, boolean asVarchar) {
        if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
