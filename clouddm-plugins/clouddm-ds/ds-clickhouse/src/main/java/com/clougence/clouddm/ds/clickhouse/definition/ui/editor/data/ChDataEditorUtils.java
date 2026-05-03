package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.adapter.clickhouse.ClickHouseTableEngine;
import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.adapter.clickhouse.ClickHouseAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorUtils;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

/**
 * @author caishan
 * @date 2023-10-25 11:45:54
 * @version: 1.0
 */
public class ChDataEditorUtils {

    public static void configTableHeader(RdbTable rdbTable, DataEditorColumn headerCol, RdbColumn colDef, I18nUtils i18nUtils) {
        ClickHouseTypes sqlType = (ClickHouseTypes) colDef.getSqlType();

        //because the primary key is not unique in the clickhouse, it is better for him to choose it
        if (headerCol.getIsPk() && !headerCol.getIsUk()) {
            headerCol.setWhereKey(false);
        }
        headerCol.setUpdateReadOnly(headerCol.getIsPk() || headerCol.getIsUk());

        ClickHouseTableEngine engine = ClickHouseTableEngine.valueOfCode(rdbTable.getAttribute(ClickHouseAttributeNames.ENGINE.getCodeKey()));
        switch (engine) {
            case MergeTree:
            case CollapsingMergeTree:
                headerCol.setUpdateReadOnly(true);
                break;
            default:
                break;
        }

        switch (sqlType) {
            case Nothing:
            case IntervalSecond:
            case IntervalMinute:
            case IntervalHour:
            case IntervalDay:
            case IntervalWeek:
            case IntervalMonth:
            case IntervalQuarter:
            case IntervalYear:
            case SimpleAggregateFunction:
            case AggregateFunction:
            case Enum:
            case Enum8:
            case Enum16:
            case Nested:
            case Tuple:
            case Array:
            case Map:
            case LowCardinality:
            case Nullable:
                headerCol.setSpareWhere(false);
                headerCol.setWhereKey(false);
                headerCol.setUpdateReadOnly(true);
                headerCol.setInsertReadOnly(true);
                break;
            case Point:
            case Polygon:
            case MultiPolygon:
            case Ring:
                headerCol.setSpareWhere(false);
                headerCol.setWhereKey(false);
                break;
            default:
                break;
        }

    }

    public static String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String colName = dialect.fmtName(true, col.getName());
        switch ((ClickHouseTypes) col.getSqlType()) {
            case FixedString:
                return "toStringCutToZero(" + colName + ") as " + colName;
            default:
                return DsFamilyDataEditorUtils.templateOfSelectCol(dialect, col);
        }
    }

    public static String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        if (value == null) {
            return col.getDefaultValue() != null ? "default" : "null";
        }

        ClickHouseTypes sqlType = (ClickHouseTypes) col.getSqlType();
        switch (sqlType) {
            case Date:
            case Date32:
                return "toDate(" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfInsert(dialect, col, value);
        }
    }

    public static String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        String setCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return setCol + " = null";
        }

        ClickHouseTypes sqlType = (ClickHouseTypes) col.getSqlType();
        switch (sqlType) {
            case Date:
            case Date32:
                return setCol + " = toDate(" + fmtDataValue(sqlType, value, true) + ")";
            default:
                return DsFamilyDataEditorUtils.templateOfSet(dialect, col, value);
        }
    }

    public static String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        String whereCol = dialect.fmtName(true, col.getName());
        if (value == null) {
            return whereCol + " is null";
        }

        ClickHouseTypes sqlType = (ClickHouseTypes) col.getSqlType();
        switch (sqlType) {
            case Date32:
                return whereCol + " = " + fmtDataValue(sqlType, value, true);
            case IPv4:
                return "IPv4NumToString(" + whereCol + ") = " + fmtDataValue(sqlType, value, true);
            case IPv6:
                return "IPv6NumToString(" + whereCol + ") = " + fmtDataValue(sqlType, value, true);
            default:
                return DsFamilyDataEditorUtils.templateOfWhere(dialect, col, value);
        }
    }

    private static String fmtDataValue(ClickHouseTypes type, String value, boolean asVarchar) {
        if (type.isGeometry() || type.hasDate() || type.hasTime() || type.isString() || asVarchar) {
            return "'" + value + "'";
        } else {
            return value;
        }
    }

    private void addSelectOption(Map<String, Map<String, String>> columnAttr, List<DataEditorColumn> columnList) {
        List<DataEditorColumn> optionList = columnList.stream().filter(item -> hasOption(item.getColumnType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(optionList)) {
            for (DataEditorColumn column : optionList) {
                Map<String, String> map = columnAttr.get(column.getColumn());
                if (map != null) {
                    //rdbck_ct -> Enum16('d' = 10, 'e' = 11, 'f' = 12) rdbck_ct -> Enum8('a' = 1, 'b' = 2, 'c' = 3)
                    String options = map.get(ClickHouseAttributeNames.COLUMN_TYPE.getCodeKey());
                    String[] optionStr = splitOptionStr(options);
                    column.setOption(optionStr);
                }
            }
        }
    }

    private Boolean hasOption(String colType) {
        if (StringUtils.isBlank(colType)) {
            return false;
        }
        ClickHouseTypes clickHouseTypes = ClickHouseTypes.valueOfCode(colType);
        switch (clickHouseTypes) {
            case Enum:
            case Enum8:
            case Enum16:
                return true;
            default:
                return false;
        }
    }

    private String[] splitOptionStr(String str) {
        String replace = str.replace("'", "");
        int index = replace.indexOf("(");
        int lastIndexOf = replace.lastIndexOf(")");
        if (index >= 0 && lastIndexOf >= 0) {
            //set1,set2,set3,set4,set5,set6,set7,set8
            String substring = replace.substring(index + 1, lastIndexOf);
            String[] split = substring.split(",");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("=");
                split[i] = split1[0].replace(" ", "");
            }
            return split;
        }
        return null;
    }
}
