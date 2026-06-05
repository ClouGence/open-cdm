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
package com.clougence.clouddm.ds.sqlserver.execute;

import static com.clougence.adapter.sqlserver.SqlServerAttributeNames.*;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.AUTO_INCREMENT;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.DEFAULT_VALUE_OF_TIME_TYPE;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.sqlserver.*;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

/**
 * https://docs.microsoft.com/zh-cn/sql/relational-databases/system-catalog-views/sys-databases-transact-sql?view=sql-server-ver16
 *
 * @author mode 2021/11/16 09:55:55
 */
@Slf4j
public class MsSqlMetaProviderUtils {

    public static List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("name"));
            schema.setUmiType(UmiTypes.Catalog);

            schema.setAttribute(SqlServerAttributeNames.READ_ONLY, rs.getString("is_read_only"));
            schema.setAttribute(SqlServerAttributeNames.DEFAULT_COLLATION_NAME, rs.getString("collation_name"));

            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("SCHEMA_NAME"));
            schema.setUmiType(UmiTypes.Schema);

            schema.setAttribute(SqlServerAttributeNames.DEFAULT_CHARACTER_SET_NAME, rs.getString("DEFAULT_CHARACTER_SET_NAME"));
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("table_name"));
            String tableType = rs.getString("type").trim();

            if (StringUtils.equalsIgnoreCase(tableType, "V")) {
                schema.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(tableType, "U")) {
                schema.setUmiType(UmiTypes.Table);
            }

            schema.setAttribute(SqlServerAttributeNames.COMMENT, rs.getString("comment"));
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        return convertObjectNameByType(rs, UmiTypes.Procedure);
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        return convertObjectNameByType(rs, UmiTypes.Function);
    }

    public static List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        return convertObjectNameByType(rs, UmiTypes.Trigger);
    }

    public static List<Value> convertSequenceName(ResultSet rs) throws SQLException {
        return convertObjectNameByType(rs, UmiTypes.Sequence);
    }

    public static List<Value> convertSynonymName(ResultSet rs) throws SQLException {
        return convertObjectNameByType(rs, UmiTypes.Synonym);
    }

    private static List<Value> convertObjectNameByType(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("NAME"));

            schema.setUmiType(umiTypes);
            result.add(schema);
        }
        return result;
    }

    public static List<RdbTable> convertTables(ResultSet rs, String catalog, boolean isView, SqlServerMainVersion mainVersion) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setCatalog(catalog);
            table.setSchema(rs.getString("schema_name"));
            table.setName(rs.getString("table_name"));
            table.setTableType(rs.getString("type").trim());
            if (StringUtils.equalsIgnoreCase(table.getTableType(), "V")) {
                table.setUmiType(UmiTypes.View);
            } else if (StringUtils.equalsIgnoreCase(table.getTableType(), "U")) {
                table.setUmiType(UmiTypes.Table);
            }
            table.setComment(rs.getString("comment"));

            Date createDate = rs.getDate("create_date");
            if (createDate != null) {
                table.setAttribute(CREATE_DATE, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(createDate));
            }

            Date modifyDate = rs.getDate("modify_date");
            if (modifyDate != null) {
                table.setAttribute(CREATE_DATE, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(modifyDate));
            }

            if (!SqlServerMainVersion.SqlServer_2017.isGt(mainVersion) && !isView) {
                table.setAttribute(TEXT_IN_ROW_LIMIT, StringUtils.toString(rs.getInt("text_in_row_limit")));
                table.setAttribute(MAX_COLUMN_ID_USED, StringUtils.toString(tryWasNull(rs.getInt("max_column_id_used"), rs)));
                table.setAttribute(EXTERNAL, StringUtils.toString(tryWasNull(rs.getBoolean("is_external"), rs)));
            }

            result.add(table);
        }
        return result;
    }

    public static List<RdbIndex> convertIndex(String dbName, ResultSet rs) throws SQLException {
        List<RdbIndex> result = new ArrayList<>();
        while (rs.next()) {
            RdbIndex index = new RdbIndex();
            index.setCatalog(dbName);
            index.setSchema(rs.getString("schema_name"));
            index.setTable(rs.getString("table_name"));
            index.setName(rs.getString("index_name"));
            index.setColumnList(new ArrayList<>());
            index.getColumnList().add(rs.getString("column_name"));

            if (rs.getBoolean("is_unique")) {
                index.setType(RdbIndexType.Unique);
            } else {
                index.setType(RdbIndexType.Normal);
            }

            SqlServerIndexType idxType = SqlServerIndexType.valueOfCode(rs.getString("index_type"));
            index.setAttribute(INDEX_TYPE, idxType.getTypeName());
            result.add(index);
        }
        return result;
    }

    public static List<RdbForeignKey> convertForeignKey(ResultSet rs, String catalog, SqlServerMainVersion mainVersion) throws SQLException {
        List<RdbForeignKey> result = new ArrayList<>();
        while (rs.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setCatalog(catalog);
            fk.setSchema(rs.getString("table_schema"));
            fk.setTable(rs.getString("table_name"));
            fk.setName(rs.getString("fk_name"));
            fk.setReferenceSchema(rs.getString("ref_schema"));
            fk.setReferenceTable(rs.getString("ref_table"));
            fk.addColumn(rs.getString("column_name"), rs.getString("ref_column"));
            fk.setUpdateRule(convertForeignKeyRule(SqlServerForeignKeyRule.valueOfCode(rs.getString("delete_referential_action"))));
            fk.setDeleteRule(convertForeignKeyRule(SqlServerForeignKeyRule.valueOfCode(rs.getString("update_referential_action"))));

            result.add(fk);
        }
        return result;
    }

    private static RdbForeignKeyRule convertForeignKeyRule(SqlServerForeignKeyRule rule) {
        if (rule == null) {
            return null;
        }
        switch (rule) {
            case NoAction:
                return RdbForeignKeyRule.NoAction;
            case Cascade:
                return RdbForeignKeyRule.Cascade;
            case SetNull:
                return RdbForeignKeyRule.SetNull;
            case SetDefault:
                return RdbForeignKeyRule.SetDefault;
            default:
                return null;
        }
    }

    public static List<RdbColumn> convertColumn(ResultSet rs, String catalog, SqlServerMainVersion mainVersion) throws SQLException {
        List<RdbColumn> result = new ArrayList<>();
        while (rs.next()) {
            if (!SqlServerMainVersion.SqlServer_2019.isGt(mainVersion)) {
                Boolean hidden = tryWasNull(rs.getBoolean("is_hidden"), rs);
                if (Boolean.TRUE.equals(hidden)) {
                    continue;
                }
            }

            RdbColumn col = new RdbColumn();
            col.setUmiType(UmiTypes.Column);

            col.setCatalog(catalog);
            col.setSchema(rs.getString("schema_name"));
            col.setTable(rs.getString("table_name"));
            col.setName(rs.getString("column_name"));
            col.setComment(rs.getString("comment"));
            col.setIndex(rs.getInt("column_order"));

            col.setSqlType(safeToSqlServerTypes(rs.getString("column_type")));
            if (!rs.getBoolean("is_nullable")) {
                col.addConstraint(new NonNull());
            }

            Long maxLength;
            if (col.getSqlType() == SqlServerTypes.NCHAR || col.getSqlType() == SqlServerTypes.NVARCHAR) {
                maxLength = tryWasNull(rs.getLong("max_length") / 2, rs);
            } else {
                maxLength = tryWasNull(rs.getLong("max_length"), rs);
            }
            col.setCharLength(maxLength);
            col.setByteLength(maxLength);

            col.setNumericPrecision(tryWasNull(rs.getInt("precision"), rs));
            col.setNumericScale(tryWasNull(rs.getInt("scale"), rs));
            col.setDatetimePrecision(tryWasNull(rs.getInt("scale"), rs));

            col.setAttribute(COLLATION_NAME, rs.getString("collation_name"));
            col.setAttribute(ROW_GUID_COL, StringUtils.toString(tryWasNull(rs.getBoolean("is_rowguidcol"), rs)));
            col.setAttribute(COMPUTED_COL, StringUtils.toString(tryWasNull(rs.getBoolean("is_computed"), rs)));
            col.setAttribute(AUTO_INCREMENT, StringUtils.toString(tryWasNull(rs.getBoolean("is_identity"), rs)));

            String dataDefault = rs.getString("default_value");
            if (dataDefault != null) {
                parseDefault(dataDefault, col);
            }

            result.add(col);
        }
        return result;
    }

    protected static SqlServerTypes safeToSqlServerTypes(Object obj) {
        String dat = (obj == null) ? null : obj.toString();
        for (SqlServerTypes type : SqlServerTypes.values()) {
            if (StringUtils.equalsIgnoreCase(type.getCodeKey(), dat)) {
                return type;
            }
        }
        return null;
    }

    protected static void parseDefault(final String dataDefault, RdbColumn col) {
        boolean isBit = col.getSqlType() == SqlServerTypes.BIT;
        boolean isBinary = col.getSqlType() == SqlServerTypes.BINARY //
                           || col.getSqlType() == SqlServerTypes.VARBINARY//
                           || col.getSqlType() == SqlServerTypes.IMAGE;
        boolean isDate = col.getSqlType() == SqlServerTypes.DATE //
                         || col.getSqlType() == SqlServerTypes.DATETIMEOFFSET //
                         || col.getSqlType() == SqlServerTypes.DATETIME2 //
                         || col.getSqlType() == SqlServerTypes.SMALLDATETIME //
                         || col.getSqlType() == SqlServerTypes.DATETIME//
                         || col.getSqlType() == SqlServerTypes.TIME//
                         || col.getSqlType() == SqlServerTypes.TIMESTAMP;
        boolean isNumber = col.getSqlType() == SqlServerTypes.BIT //
                           || col.getSqlType() == SqlServerTypes.DECIMAL //
                           || col.getSqlType() == SqlServerTypes.NUMERIC//
                           || col.getSqlType() == SqlServerTypes.SMALLINT//
                           || col.getSqlType() == SqlServerTypes.TINYINT//
                           || col.getSqlType() == SqlServerTypes.INT//
                           || col.getSqlType() == SqlServerTypes.BIGINT //
                           || col.getSqlType() == SqlServerTypes.SMALLMONEY//
                           || col.getSqlType() == SqlServerTypes.MONEY//
                           || col.getSqlType() == SqlServerTypes.FLOAT//
                           || col.getSqlType() == SqlServerTypes.REAL;

        if (StringUtils.startsWithIgnoreCase(dataDefault, "(NEXT VALUE FOR ") && dataDefault.endsWith(")")) {
            col.setAttribute(AUTO_INCREMENT, "true");
            return;
        }
        if (StringUtils.equalsIgnoreCase(dataDefault, "(NULL)")) {
            return;
        }

        String dataDefaultTmp = dataDefault;
        if (dataDefaultTmp.startsWith("('") && dataDefaultTmp.endsWith("')")) {
            dataDefaultTmp = dataDefaultTmp.substring(2, dataDefaultTmp.length() - 2);
        }
        while (dataDefaultTmp.startsWith("(") && dataDefaultTmp.endsWith(")")) {
            dataDefaultTmp = dataDefaultTmp.substring(1, dataDefaultTmp.length() - 1);
        }

        if (isBit | isBinary) {
            if (dataDefaultTmp.startsWith("0x") || dataDefaultTmp.startsWith("0X")) {
                col.setDefaultValue(dataDefaultTmp.substring(2));
            }
        } else if (isDate) {
            parseDefaultTime(dataDefaultTmp, col);
        } else if (isNumber) {
            if (NumberUtils.isNumber(dataDefaultTmp)) {
                parseDefaultNumber(dataDefaultTmp, col);
            } else {
                parseDefaultNumber(dataDefault, col);
            }
        } else {
            parseDefaultOther(dataDefaultTmp, col);
        }
    }

    protected static void parseDefaultTime(String dataDefault, RdbColumn col) {
        boolean isFunc = false;
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "getdate()");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            col.setDefaultValue(dataDefault);
            col.setDefaultValueIsFunc(true);
        } else {
            DateFormatType dateFormatType = DateFormatType.passerTypeWithoutUnsupported(dataDefault);
            if (dateFormatType != null) {
                col.setAttribute(DEFAULT_VALUE_OF_TIME_TYPE, dateFormatType.name());
                col.setDefaultValue(dataDefault);
            }
        }
    }

    protected static void parseDefaultNumber(String dataDefault, RdbColumn col) {
        boolean isFunc = false;
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            col.setDefaultValue(dataDefault);
            col.setDefaultValueIsFunc(true);
        } else {
            col.setDefaultValue(dataDefault);
        }
    }

    protected static void parseDefaultOther(String dataDefault, RdbColumn col) {
        boolean isFunc = false;
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "newid()");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            col.setDefaultValue(dataDefault);
            col.setDefaultValueIsFunc(true);
        } else {
            col.setDefaultValue(replaceDoubleQuotes(dataDefault));
        }
    }

    private static String replaceDoubleQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replace("''", "'");
    }

}
