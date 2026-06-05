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
package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.adapter.postgre.PostgresTypes;
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

public class PgDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        PostgresTypes sqlType = (PostgresTypes) colDef.getSqlType();

        // VIRTUAL_TYPE
        String identity = colDef.getAttribute(PostgreAttributeNames.VIRTUAL_TYPE);
        if (StringUtils.equalsIgnoreCase("BY DEFAULT", identity) || StringUtils.equalsIgnoreCase("ALWAYS", identity)) {
            headerCol.setAutoincrement(true);
            headerCol.setInsertReadOnly(true);
            headerCol.setUpdateReadOnly(true);
        } else if (StringUtils.equalsIgnoreCase("STORED", identity)) {
            headerCol.setInsertReadOnly(true);
        }

        switch (sqlType) {
            case TXID_SNAPSHOT:
            case TXID_SNAPSHOT_ARRAY:
            case PG_LSN:
            case PG_LSN_ARRAY:
            case PG_NODE_TREE:
            case PG_NODE_TREE_ARRAY:
                headerCol.setInsertReadOnly(true);
                headerCol.setUpdateReadOnly(true);
                break;
            case BIT:
            case BIT_VARYING: {
                headerCol.setWhereKey(false);
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_BINARY;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_BINARY));
                headerCol.setCheck(Collections.singletonList(hCheck));
                break;
            }
            case JSONB:
            case BYTEA:
                headerCol.setWhereKey(false);
                break;
            default: {
                headerCol.setWhereKey(false);
                if (sqlType.isBinary() && !sqlType.isArray()) {
                    DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                    DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                    headerCol.setCheck(Collections.singletonList(hCheck));
                }
                break;
            }
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String colName = dialect.fmtName(true, col.getName());
        switch ((PostgresTypes) col.getSqlType()) {
            case GEOMETRY:
                return "ST_AsEWKT(" + colName + ") as " + colName;
            case BYTEA:
                //escape is pg default ;http://postgres.cn/docs/12/datatype-binary.html#id-1.5.7.12.9
                return "encode(" + colName + ", 'escape') as " + colName;
            default:
                return colName;
        }
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        PostgresTypes sqlType = (PostgresTypes) col.getSqlType();
        if (sqlType.isArray() && value.isEmpty()) {
            value = "{}";
        }

        switch (sqlType) {
            case BIT:
                return fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey() + "(" + col.getNumericPrecision() + ")";
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
            case UUID:
            case UUID_ARRAY:
            case INTERVAL:
            case INTERVAL_ARRAY:
            case POINT:
            case POINT_ARRAY:
            case LINE:
            case LINE_ARRAY:
            case LSEG:
            case LSEG_ARRAY:
            case BOX:
            case BOX_ARRAY:
            case PATH:
            case PATH_ARRAY:
            case POLYGON:
            case POLYGON_ARRAY:
            case CIRCLE:
            case CIRCLE_ARRAY:
            case GEOMETRY:
            case GEOMETRY_ARRAY:
            case MONEY:
            case MONEY_ARRAY:
            case CIDR:
            case CIDR_ARRAY:
            case INET:
            case INET_ARRAY:
            case MACADDR:
            case MACADDR_ARRAY:
            case MACADDR8:
            case MACADDR8_ARRAY:
            case INT4RANGE:
            case INT4RANGE_ARRAY:
            case INT8RANGE:
            case INT8RANGE_ARRAY:
            case NUMRANGE:
            case NUMRANGE_ARRAY:
            case BOOLEAN:
            case BOOLEAN_ARRAY:
                return fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey();
            case JSONB:
            case JSONB_ARRAY:
            case REF_CURSOR:
            case REF_CURSOR_ARRAY:
            case BYTEA:
            case BYTEA_ARRAY:
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

        PostgresTypes sqlType = (PostgresTypes) col.getSqlType();
        if (sqlType.isArray() && value.isEmpty()) {
            value = "{}";
        }

        switch (sqlType) {
            case BIT:
                return setCol + " = " + fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey() + "(" + col.getNumericPrecision() + ")";
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
            case UUID:
            case UUID_ARRAY:
            case INTERVAL:
            case INTERVAL_ARRAY:
            case POINT:
            case POINT_ARRAY:
            case LINE:
            case LINE_ARRAY:
            case LSEG:
            case LSEG_ARRAY:
            case BOX:
            case BOX_ARRAY:
            case PATH:
            case PATH_ARRAY:
            case POLYGON:
            case POLYGON_ARRAY:
            case CIRCLE:
            case CIRCLE_ARRAY:
            case GEOMETRY:
            case GEOMETRY_ARRAY:
            case MONEY:
            case MONEY_ARRAY:
            case CIDR:
            case CIDR_ARRAY:
            case INET:
            case INET_ARRAY:
            case MACADDR:
            case MACADDR_ARRAY:
            case MACADDR8:
            case MACADDR8_ARRAY:
            case INT4RANGE:
            case INT4RANGE_ARRAY:
            case INT8RANGE:
            case INT8RANGE_ARRAY:
            case NUMRANGE:
            case NUMRANGE_ARRAY:
            case BOOLEAN:
            case BOOLEAN_ARRAY:
                return setCol + " = " + fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey();
            case JSONB:
            case JSONB_ARRAY:
            case REF_CURSOR:
            case REF_CURSOR_ARRAY:
            case BYTEA:
            case BYTEA_ARRAY:
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

        PostgresTypes sqlType = (PostgresTypes) col.getSqlType();
        if (sqlType.isArray() && value.isEmpty()) {
            value = "{}";
        }

        switch (sqlType) {
            case BIT:
            case BIT_ARRAY:
                return whereCol + " = " + fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey() + "(" + col.getNumericPrecision() + ")";
            case BIT_VARYING:
            case BIT_VARYING_ARRAY:
            case UUID:
            case UUID_ARRAY:
            case INTERVAL:
            case INTERVAL_ARRAY:
            case POINT:
            case POINT_ARRAY:
            case LINE:
            case LINE_ARRAY:
            case LSEG:
            case LSEG_ARRAY:
            case BOX:
            case BOX_ARRAY:
            case PATH:
            case PATH_ARRAY:
            case POLYGON:
            case POLYGON_ARRAY:
            case CIRCLE:
            case CIRCLE_ARRAY:
            case GEOMETRY:
            case GEOMETRY_ARRAY:
            case MONEY:
            case MONEY_ARRAY:
            case CIDR:
            case CIDR_ARRAY:
            case INET:
            case INET_ARRAY:
            case MACADDR:
            case MACADDR_ARRAY:
            case MACADDR8:
            case MACADDR8_ARRAY:
            case INT4RANGE:
            case INT4RANGE_ARRAY:
            case INT8RANGE:
            case INT8RANGE_ARRAY:
            case NUMRANGE:
            case NUMRANGE_ARRAY:
                return whereCol + " = " + fmtDataValue(sqlType, value, true) + "::" + sqlType.getCodeKey();
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(PostgresTypes type, String value, boolean asVarchar) {
        if (type.isArray() && value.startsWith("[") && value.endsWith("]")) {
            return "ARRAY " + value;
        } else if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
