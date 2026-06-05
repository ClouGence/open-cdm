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
package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.sqlserver.SqlServerTypes;
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
public class SqlServerDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        SqlServerTypes sqlType = (SqlServerTypes) colDef.getSqlType();
        switch (sqlType) {
            case ROWVERSION:
            case TIMESTAMP:
                headerCol.setSpareWhere(false);
                headerCol.setWhereKey(false);
                headerCol.setUpdateReadOnly(true);
                headerCol.setInsertReadOnly(true);
                break;
            case HIERARCHYID:
                headerCol.setSpareWhere(false);
                headerCol.setWhereKey(false);
                headerCol.setUpdateReadOnly(true);
                break;
            case BIT: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_BINARY;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_BINARY));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case IMAGE:
            case BINARY:
            case VARBINARY: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            default:
                break;
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String colName = dialect.fmtName(true, col.getName());
        switch ((SqlServerTypes) col.getSqlType()) {
            case HIERARCHYID:
                return colName + ".ToString() as " + colName;
            case GEOGRAPHY:
            case GEOMETRY:
                return colName + ".STAsText() as " + colName;
            default:
                return colName;
        }
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        SqlServerTypes sqlType = (SqlServerTypes) col.getSqlType();
        switch (sqlType) {
            case GEOMETRY:
                return "geometry::STGeomFromText(" + fmtDataValue(sqlType, value, true) + ", 0)";
            case GEOGRAPHY:
                return "geography::STGeomFromText(" + fmtDataValue(sqlType, value, true) + ", 4326)";
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }

        SqlServerTypes sqlType = (SqlServerTypes) col.getSqlType();
        switch (sqlType) {
            case GEOMETRY:
                return setCol + " = geometry::STGeomFromText(" + fmtDataValue(sqlType, value, true) + ", 0)";
            case GEOGRAPHY:
                return setCol + " = geography::STGeomFromText(" + fmtDataValue(sqlType, value, true) + ", 4326)";
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        SqlServerTypes sqlType = (SqlServerTypes) col.getSqlType();
        switch (sqlType) {
            case IMAGE:
                return "cast( " + whereCol + " as varbinary(max)) = " + fmtDataValue(sqlType, value, true);
            case XML:
                return "cast( " + whereCol + " as varchar(max)) = " + fmtDataValue(sqlType, value, true);
            case NTEXT:
            case TEXT:
                return whereCol + " like N" + fmtDataValue(sqlType, value, true) + " ESCAPE '#'";
            case GEOGRAPHY:
            case GEOMETRY:
                return whereCol + ".STEquals(" + fmtDataValue(sqlType, value, true) + ") = 1";
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(SqlServerTypes type, String value, boolean asVarchar) {
        if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
