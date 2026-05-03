package com.clougence.clouddm.ds.doris.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.doris.DorisTypes;
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
public class DrDataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        DorisTypes sqlType = (DorisTypes) colDef.getSqlType();
        switch (sqlType) {
            case QUANTILE_STATE:
                headerCol.setInsertReadOnly(true);
                headerCol.setUpdateReadOnly(true);
                break;
            case ARRAY:
                headerCol.setSpareWhere(false);
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

        if (headerCol.getIsUk() || headerCol.getIsPk()) {
            headerCol.setUpdateReadOnly(true);
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String colName = dialect.fmtName(true, col.getName());
        switch ((DorisTypes) col.getSqlType()) {
            case HLL:
                return "HLL_CARDINALITY(" + colName + ") as " + colName;
            case BITMAP:
                return "BITMAP_UNION(" + colName + ") as " + colName;
            default:
                return colName;
        }
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        DorisTypes sqlType = (DorisTypes) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return "DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case BITMAP:
                return "BITMAP_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            case QUANTILE_STATE:
                return "TO_QUANTILE_STATE(" + fmtDataValue(sqlType, value, true) + ", 2048)";//unsupport
            case HLL:
                return "HLL_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }
        DorisTypes sqlType = (DorisTypes) col.getSqlType();
        switch (sqlType) {
            case JSON: {
                return setCol + " = " + fmtDataValue(sqlType, value, true);
            }
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        DorisTypes sqlType = (DorisTypes) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return whereCol + " = DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case BITMAP:
                return whereCol + " = BITMAP_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            case QUANTILE_STATE:
                return whereCol + " = TO_QUANTILE_STATE(" + fmtDataValue(sqlType, value, true) + ", 2048)";//unsupport
            case HLL:
                return whereCol + " = HLL_HASH(" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(DorisTypes type, String value, boolean asVarchar) {
        if (asVarchar || type.isGeometry() || type.hasDate() || type.hasTime() || type.isString()) {
            return "'" + StringUtils.replace(value, "'", "''") + "'";
        } else {
            return value;
        }
    }
}
