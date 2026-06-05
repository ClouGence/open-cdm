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
package com.clougence.clouddm.ds.dameng.execute;

import static com.clougence.adapter.dameng.DmAttributeNames.*;
import static com.clougence.adapter.dameng.DmSqlTypes.TIME;
import static com.clougence.adapter.dameng.DmSqlTypes.TIMESTAMP;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.COMMENT;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 达梦 元信息获取，参考资料：
 * <li>https://docs.oracle.com/en/database/oracle/oracle-database/21/drdag/all_synonyms-drda-gateway.html#GUID-E814A6AC-5E00-4DB6-8170-DC147F7879F8</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-04-29
 */
@Slf4j
public class DmMetaProviderUtils {

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("NAME"));
            schema.setUmiType(UmiTypes.Schema);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("TAB_NAME"));

            String tableType = rs.getString("TAB_TYPE");
            if (StringUtils.equalsIgnoreCase(tableType, "VIEW")) {
                schema.setUmiType(UmiTypes.View);
            } else {
                schema.setUmiType(UmiTypes.Table);
            }
            schema.setAttribute(COMMENT, rs.getString("COMMENTS"));
            result.add(schema);
        }
        return result;
    }

    /**
     * resolve result such as SEQUENCE, TRIGGER
     *
     * @param rs       data result from jdbc
     * @param umiTypes the types in object table such as  SEQUENCE, TRIGGER.
     * @return object name list
     * @throws SQLException sqlException
     */
    private static List<Value> convertNameForObject(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue function = new RdbValue();
            function.setValue(rs.getString("OBJECT_NAME"));
            function.setUmiType(umiTypes);
            result.add(function);
        }
        return result;
    }

    public static List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Trigger);
    }

    public static List<Value> convertSequenceName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Sequence);
    }

    public static List<Value> convertSynonymName(ResultSet rs) throws SQLException {
        return convertNameForObject(rs, UmiTypes.Synonym);
    }

    public static List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        return convertRoutineName(rs, UmiTypes.Procedure);
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        return convertRoutineName(rs, UmiTypes.Function);
    }

    private static List<Value> convertRoutineName(ResultSet rs, UmiTypes umiTypes) throws SQLException {
        List<Value> result = new ArrayList<>();
        Map<String, Routine> map = new HashMap<>();
        while (rs.next()) {
            String objectName = rs.getString("OBJECT_NAME");
            Routine routine = map.get(objectName);
            if (routine == null) {
                routine = new Routine();
                routine.setName(objectName);
                map.put(objectName, routine);
            }
            String argName = rs.getString("ARGUMENT_NAME");
            int position = rs.getInt("POSITION");
            // filter return type
            if (umiTypes.equals(UmiTypes.Function) && position == 0) {
                continue;
            }
            if (StringUtils.isNotBlank(argName)) {
                String type = rs.getString("DATA_TYPE");
                String length = rs.getString("LENGTH");
                routine.addParam(argName, type, length, position);
            }

        }

        map.forEach((k, v) -> {
            RdbValue value = new RdbValue();
            // setting main params
            value.setValue(v.getName());
            value.setUmiType(umiTypes);
            value.setAttribute(COMMENT, v.getComment());
            value.setAttribute(OBJ_UI_TIPS, v.getParamString());
            result.add(value);
        });
        return result;
    }

    public static List<ConstraintObject> convertConstraints(ResultSet rs) throws SQLException {
        List<ConstraintObject> result = new ArrayList<>();
        while (rs.next()) {
            String type = rs.getString("CONSTRAINT_TYPE");
            if ("U".equalsIgnoreCase(type)) {
                RdbUniqueKey unique = new RdbUniqueKey();
                unique.setName(rs.getString("CONSTRAINT_NAME"));
                unique.addColumn(rs.getString("COLUMN_NAME"));
                unique.setTable(rs.getString("TABLE_NAME"));
                unique.setEnabled("ENABLED".equals(rs.getString("STATUS")));
                result.add(unique);
            } else if ("C".equalsIgnoreCase(type)) {
                RdbCheckConstraint checkKey = new RdbCheckConstraint();
                checkKey.setName(rs.getString("CONSTRAINT_NAME"));
                checkKey.setExpression(rs.getString("SEARCH_CONDITION"));
                checkKey.setTable(rs.getString("TABLE_NAME"));
                checkKey.setEnabled("ENABLED".equals(rs.getString("STATUS")));
                result.add(checkKey);
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return result;
    }

    public static List<RdbForeignKey> convertForeignKeys(ResultSet rs) throws SQLException {
        List<RdbForeignKey> result = new ArrayList<>();
        while (rs.next()) {
            RdbForeignKey key = new RdbForeignKey();
            //            key.setName(rs.getString("CONSTRAINT_NAME"));
            //            key.setTable(rs.getString("DEPENDENT_TABLE_NAME"));
            //            key.addColumn(rs.getString("DEPENDENT_COLUMN"), rs.getString("REFERENCE_COLUMN"));
            //            key.setName(rs.getString("CONSTRAINT_NAME"));
            //            key.setTable(rs.getString("DEPENDENT_TABLE_NAME"));
            //            key.addColumn(rs.getString("DEPENDENT_COLUMN"), rs.getString("REFERENCE_COLUMN"));
            //            key.setReferenceTable(rs.getString("REFERENCE_TABLE"));
            //            key.setReferenceSchema(rs.getString("REFERENCE_OWNER"));
            //            key.setDeleteRule(RdbForeignKeyRule.valueOfCode(rs.getString("DELETE_RULE")));

            key.setName(rs.getString("FK_NAME"));
            key.setTable(rs.getString("FKTABLE_NAME"));
            key.addColumn(rs.getString("FKCOLUMN_NAME"), rs.getString("PKCOLUMN_NAME"));
            key.setReferenceTable(rs.getString("PKTABLE_NAME"));
            key.setReferenceSchema(rs.getString("PKTABLE_SCHEM"));
            key.setDeleteRule(convertForeignKeyRule(rs.getInt("DELETE_RULE")));
            key.setUpdateRule(convertForeignKeyRule(rs.getInt("UPDATE_RULE")));
            result.add(key);
        }
        return result;
    }

    private static RdbForeignKeyRule convertForeignKeyRule(int ruleId) throws SQLException {
        switch (ruleId) {
            case 0: {
                return RdbForeignKeyRule.Cascade;
            }
            case 2: {
                return RdbForeignKeyRule.SetNull;
            }
            case 3: {
                return RdbForeignKeyRule.NoAction;
            }
            case 4: {
                return RdbForeignKeyRule.SetDefault;
            }
            case 5: {
                return RdbForeignKeyRule.Restrict;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> result = new ArrayList<>();
        while (rs.next()) {
            RdbIndex index = new RdbIndex();
            index.setSchema(rs.getString("TABLE_OWNER"));
            index.setTable(rs.getString("TABLE_NAME"));
            index.setSchema(rs.getString("OWNER"));
            index.setName(rs.getString("INDEX_NAME"));
            if ("UNIQUE".equalsIgnoreCase(rs.getString("UNIQUENESS"))) {
                index.setAttribute(INDEX_WAY.getCodeKey(), "UNIQUE");
                index.setType(RdbIndexType.Unique);
            } else {
                index.setType(RdbIndexType.Normal);
                String indexType = rs.getString("INDEX_TYPE");
                index.setAttribute(INDEX_WAY.getCodeKey(), "BITMAP".equals(indexType) ? "BITMAP" : "NORMAL");
            }

            String columnName = rs.getString("COLUMN_NAME");
            index.setColumnList(new ArrayList<>());
            index.getColumnList().add(columnName);

            result.add(index);
        }
        return result;
    }

    public static List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setSchema(rs.getString("OWNER"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setTableType(rs.getString("TABLE_TYPE"));
            table.setAttribute(TABLESPACE, rs.getString("TABLESPACE_NAME"));
            // table.setReadOnly("YES".equalsIgnoreCase(record.getString("READ_ONLY")));
            table.setAttribute(TABLE_TYPE, rs.getString("TABLE_TYPE"));

            if ("Y".equalsIgnoreCase(rs.getString("TEMPORARY"))) {
                table.setTableType("TEMPORARY");
                table.setAttribute(TABLE_TYPE, "TEMPORARY");
            }

            if (table.getTableType() == null) {
                continue;
            }

            table.setComment(rs.getString("COMMENTS"));
            result.add(table);
        }
        return result;
    }

    public static List<RdbTable> convertView(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbView table = new RdbView();
            table.setSchema(rs.getString("OWNER"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setTableType(rs.getString("TABLE_TYPE"));
            table.setAttribute(TABLESPACE, rs.getString("TABLESPACE_NAME"));
            // table.setReadOnly("YES".equalsIgnoreCase(record.getString("READ_ONLY")));
            table.setAttribute(TABLE_TYPE, rs.getString("TABLE_TYPE"));

            if ("Y".equalsIgnoreCase(rs.getString("TEMPORARY"))) {
                table.setTableType("TEMPORARY");
                table.setAttribute(TABLE_TYPE, "TEMPORARY");
            }

            table.setSql(rs.getString("QUERY_SQL"));

            if (table.getTableType() == null) {
                return null;
            }

            table.setComment(rs.getString("COMMENTS"));
            result.add(table);
        }
        return result;
    }

    public static List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
        List<RdbColumn> result = new ArrayList<>();
        while (rs.next()) {
            RdbColumn column = new RdbColumn();
            column.setSchema(rs.getString("OWNER"));
            column.setTable(rs.getString("TABLE_NAME"));
            column.setName(rs.getString("COLUMN_NAME"));
            column.setByteLength(tryWasNull(rs.getLong("DATA_LENGTH"), rs));//todo
            column.setCharLength(tryWasNull(rs.getLong("CHAR_LENGTH"), rs));
            column.setNumericPrecision(tryWasNull(rs.getInt("DATA_PRECISION"), rs));
            column.setNumericScale(tryWasNull(rs.getInt("DATA_SCALE"), rs));
            if (!"Y".equals(rs.getString("NULLABLE"))) {
                column.addConstraint(new NonNull());
            }
            String dataType = rs.getString("DATA_TYPE");
            String typeOwner = rs.getString("DATA_TYPE_OWNER");
            column.setAttribute(COLUMN_TYPE, dataType);
            column.setSqlType(safeToDmTypes(dataType));
            column.setAttribute(COLUMN_TYPE_OWNER, typeOwner);
            column.setAttribute(RdbAttributeNames.JDBC_TYPE, columnTypeMappingToJdbcType(column.getSqlType(), dataType));

            if (dataType == null && StringUtils.isNotBlank(typeOwner)) {
                column.setAttribute(COLUMN_TYPE, JDBCType.STRUCT.getName()); // 有 Type Name 表示一定是用户创建的类型。
            }

            DmSqlTypes sqlType = (DmSqlTypes) column.getSqlType();
            switch (sqlType) {
                case CHAR:
                case NCHAR:
                case VARCHAR:
                case NVARCHAR2:
                case TEXT:
                case CLOB:
                    // see https://eco.dameng.com/community/question/64c16e0b67172702ee445b2e15796fa6
                    boolean matchType = dataType.equalsIgnoreCase("CHARACTER VARYING") || dataType.equalsIgnoreCase("CHAR VARYING");
                    boolean matchScale = column.getNumericScale() == 7;
                    if (matchType && matchScale) {
                        column.setCharLength(column.getByteLength() / 2);
                    }

                    if (column.getCharLength() == null) {
                        column.setCharLength(column.getByteLength());
                    }
                    break;
                case NUMERIC:
                    column.setNumericPrecision(column.getNumericPrecision() == null ? null : column.getNumericPrecision());
                    break;
                default:
                    break;
            }

            column.setAttribute(CHARACTER_SET_NAME, rs.getString("CHARACTER_SET_NAME"));
            column.setAttribute(HIDDEN, String.valueOf("YES".equals(rs.getString("HIDDEN_COLUMN"))));
            column.setIndex(rs.getInt("COLUMN_ID"));
            column.setComment(rs.getString("COMMENTS"));
            if (rs.getBoolean("IS_IDENT")) {
                column.setAttribute(IDENTITY, "true");
                identityResolver(rs.getString("IDENT"), column);
            } else {
                column.setAttribute(IDENTITY, "false");
            }

            boolean isVirt = rs.getBoolean("IS_VIRT");
            String dataDefault = rs.getString("DATA_DEFAULT");
            if (isVirt && StringUtils.isNotBlank(dataDefault) && dataDefault.startsWith("(") && dataDefault.endsWith(")")) {
                column.setAttribute(DEFAULT_OPTION, "virtual");
                column.setAttribute(VIRTUAL_EXPR, dataDefault);
            } else {
                column.setAttribute(DEFAULT_OPTION, "default");
                if (dataDefault != null) {
                    passerDefault(dataDefault, column);
                }
            }
            result.add(column);
        }

        return result;
    }

    private static void identityResolver(String ident, RdbColumn column) {
        //E703000000000000 15CD5B0700000000 010000000000 00 00
        column.setAttribute(DmAttributeNames.IDENTITY_SEED, parseIdentity(ident, 0, 16));
        column.setAttribute(DmAttributeNames.IDENTITY_INCREMENT, parseIdentity(ident, 16, 32));
    }

    //https://eco.dameng.com/community/question/5d28c68a065c6af6b3a62a9f873451d4
    private static String parseIdentity(String ident, int start, int length) {
        StringBuilder target = new StringBuilder();
        for (int i = start; i < length; i += 2) {
            String substring = ident.substring(i, i + 2);
            target.append(new StringBuilder(substring).reverse());
        }
        long l = Long.parseLong(target.reverse().toString(), 16);
        return String.valueOf(l);
    }

    public static DmSqlTypes safeToDmTypes(Object obj) {
        if (obj == null) {
            return null;
        }
        return DmSqlTypes.toDmdbType(obj.toString());
    }

    public static String columnTypeMappingToJdbcType(FieldType typeDef, String columnType) {
        if (typeDef == null) {
            return null;
        }
        if (typeDef instanceof DmSqlTypes) {
            switch ((DmSqlTypes) typeDef) {
                case TIMESTAMP:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
                case INTERVAL_YEAR_TO_MONTH:
                case INTERVAL_DAY_TO_SECOND:
                    return TIMESTAMP.toJDBCType().name();
                case TIME_WITH_TIME_ZONE:
                case TIME:
                    return TIME.toJDBCType().name();
                default:
                    JDBCType jdbcType = typeDef.toJDBCType();
                    if (jdbcType == null) {
                        return null;
                    }
                    return jdbcType.name();
            }
        } else {
            return typeDef.toJDBCType().name();
        }
    }

    public static void passerDefault(String dataDefault, RdbColumn column) {
        if (StringUtils.equalsIgnoreCase(dataDefault, "null")) {
            return;
        }

        String tempStr = dataDefault.trim();
        if (tempStr.startsWith("'") && tempStr.endsWith("'")) {
            dataDefault = tempStr.substring(1, tempStr.length() - 1);
            column.setDefaultValue(dataDefault);
        } else {
            if (NumberUtils.isNumber(dataDefault)) {
                column.setDefaultValue(dataDefault);
            } else {
                // https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/SYSTIMESTAMP.html
                column.setDefaultValueIsFunc(true);
            }
        }
    }

    public static List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        List<RdbFunction> result = new ArrayList<>();
        while (rs.next()) {
            RdbFunction rdbFunction = new RdbFunction();
            rdbFunction.setSchema(rs.getString("SCHEMA_NAME"));
            rdbFunction.setName(rs.getString("SPECIFIC_NAME"));
            result.add(rdbFunction);
        }

        return result;
    }

    public static List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setCatalog(null);
            rdbParam.setSchema(rs.getString("OWNER"));
            rdbParam.setReferenceObject(rs.getString("OBJECT_NAME"));
            rdbParam.setOrdinal(rs.getInt("POSITION"));
            rdbParam.setName(rs.getString("ARGUMENT_NAME"));
            String dataType = rs.getString("DATA_TYPE");
            if (dataType == null) {
                continue;
            }
            rdbParam.setType(rs.getString("DATA_TYPE"));
            rdbParam.setCharacterMaximumLength(rs.getString("DATA_LENGTH"));
            result.add(rdbParam);
        }

        return result;
    }

    public static List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        List<RdbProcedure> result = new ArrayList<>();
        while (rs.next()) {
            RdbProcedure rdbProcedure = new RdbProcedure();
            rdbProcedure.setSchema(rs.getString("SCHEMA_NAME"));
            rdbProcedure.setName(rs.getString("SPECIFIC_NAME"));
            result.add(rdbProcedure);
        }
        return result;
    }

    public static List<Value> convertRoles(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue role = new RdbValue();
            role.setValue(rs.getString("NAME"));
            role.setUmiType(UmiTypes.ROLE);
            result.add(role);
        }

        return result;
    }

    public static List<Value> convertUsers(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue user = new RdbValue();
            user.setValue(rs.getString("USERNAME"));
            user.setUmiType(UmiTypes.USER);
            result.add(user);
        }

        return result;
    }

    public static List<Value> convertTablespaces(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue space = new RdbValue();
            space.setValue(rs.getString("NAME"));
            space.setUmiType(UmiTypes.TABLESPACE);
            result.add(space);
        }

        return result;
    }

    private static class Routine extends RoutineUmiData {

    }
}
