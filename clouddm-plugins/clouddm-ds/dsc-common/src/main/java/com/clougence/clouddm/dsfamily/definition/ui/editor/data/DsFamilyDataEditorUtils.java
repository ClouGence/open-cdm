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
