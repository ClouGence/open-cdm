package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.data;

import java.util.Collections;

import com.clougence.adapter.db2.Db2Types;
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
public class Db2DataEditorUtils implements DsDataEditorI18nKeys {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        Db2Types sqlType = (Db2Types) colDef.getSqlType();
        switch (sqlType) {
            case CHAR_FOR_BIT_DATA:
            case VARCHAR_FOR_BIT_DATA:
            case LONG_VARCHAR_FOR_BIT_DATA:
            case BLOB: {
                DataEditorUiVerify verify = DataEditorUiVerify.DATA_FORMAT_HEXADECIMAL;
                DataEditorHeader hCheck = new DataEditorHeader(verify.getType(), verify.getExpr(), i18nUtils.getMessage(DATA_EDITOR_CHECK_DATA_FORMAT_HEX));
                headerCol.setCheck(Collections.singletonList(hCheck));
            }
            default:
        }
    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return dialect.fmtName(true, col.getName());
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        Db2Types sqlType = (Db2Types) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return "SYSIBM.DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case TIME:
                return "SYSIBM.TIME(" + fmtDataValue(sqlType, value, true) + ")";
            case TIMESTAMP:
                return "SYSIBM.TIMESTAMP(" + fmtDataValue(sqlType, value, true) + ")";
            case BLOB:
                return "BLOB(x" + fmtDataValue(sqlType, value, true) + ")";
            case BINARY:
                return "BINARY(x" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }

        Db2Types sqlType = (Db2Types) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return setCol + " = SYSIBM.DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case TIME:
                return setCol + " = SYSIBM.TIME(" + fmtDataValue(sqlType, value, true) + ")";
            case TIMESTAMP:
                return setCol + " = SYSIBM.TIMESTAMP(" + fmtDataValue(sqlType, value, true) + ")";
            case BLOB:
                return setCol + " = BLOB(x" + fmtDataValue(sqlType, value, true) + ")";
            case BINARY:
                return setCol + " = BINARY(x" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        Db2Types sqlType = (Db2Types) col.getSqlType();
        switch (sqlType) {
            case DATE:
                return whereCol + " = SYSIBM.DATE(" + fmtDataValue(sqlType, value, true) + ")";
            case TIME:
                return whereCol + " = SYSIBM.TIME(" + fmtDataValue(sqlType, value, true) + ")";
            case TIMESTAMP:
                return whereCol + " = SYSIBM.TIMESTAMP(" + fmtDataValue(sqlType, value, true) + ")";
            case BLOB:
                return whereCol + " = BLOB(x" + fmtDataValue(sqlType, value, true) + ")";
            case BINARY:
                return whereCol + " = BINARY(x" + fmtDataValue(sqlType, value, true) + ")";
            case XML:
                return "XMLSERIALIZE(" + whereCol + " as varchar(8168)) = " + fmtDataValue(sqlType, value, true);//if xml very big,varchar(8168) may be has error
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(Db2Types type, String value, boolean asVarchar) {
        if (type.isBinary() || type.equals(Db2Types.BINARY)) {
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
