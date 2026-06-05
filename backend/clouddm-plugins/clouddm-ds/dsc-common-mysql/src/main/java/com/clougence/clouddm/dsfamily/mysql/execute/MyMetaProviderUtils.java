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
package com.clougence.clouddm.dsfamily.mysql.execute;

import static com.clougence.adapter.mysql.MyUmiAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.adapter.mysql.driver.MysqlType;
import com.clougence.clouddm.dsfamily.mysql.execute.function.MySqlBuiltInFunction;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.*;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

/**
 * MySQL 元信息获取，参考资料：
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class MyMetaProviderUtils {

    protected final String   PART  = "\\((.*?)\\)";
    protected final String[] units = new String[] { "", "B", "KB", "MB", "GB", "TB" };

    public List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("SCHEMA_NAME"));
            schema.setUmiType(UmiTypes.Schema);
            schema.setAttribute(DEFAULT_COLLATION_NAME, rs.getString("DEFAULT_COLLATION_NAME"));
            schema.setAttribute(DEFAULT_CHARACTER_SET_NAME, rs.getString("DEFAULT_CHARACTER_SET_NAME"));

            result.add(schema);
        }
        return result;
    }

    public List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("TABLE_NAME"));
            String tableType = rs.getString("TABLE_TYPE");
            if (StringUtils.endsWith(tableType, "VIEW")) {
                schema.setUmiType(UmiTypes.View);
            } else {
                schema.setUmiType(UmiTypes.Table);
            }

            String tableCollation = rs.getString("TABLE_COLLATION");
            String charset = null;
            if (StringUtils.isNotBlank(tableCollation)) {
                int index = tableCollation.indexOf("_");
                charset = tableCollation.substring(0, index);
            }

            schema.setAttribute(CHARACTER_SET, charset);
            schema.setAttribute(COLLATION, tableCollation);
            schema.setAttribute(COMMENT, rs.getString("TABLE_COMMENT"));
            schema.setAttribute(ENGINE, rs.getString("ENGINE"));

            result.add(schema);
        }
        return result;
    }

    public List<Value> convertProcedureName(ResultSet rs) throws SQLException {
        return convertRoutineName(UmiTypes.Procedure, rs);
    }

    public List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        return convertRoutineName(UmiTypes.Function, rs);
    }

    public List<Value> convertFunctionNameWithBuiltIn(ResultSet rs) throws SQLException {
        List<Value> values = convertRoutineName(UmiTypes.Function, rs);
        values.addAll(MySqlBuiltInFunction.getInstance().convertFunctions2Values());
        values.sort(Comparator.comparing(Value::asValue));
        return values;
    }

    private List<Value> convertRoutineName(UmiTypes umiTypes, ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        Map<String, Routine> routineParamMap = new HashMap<>();
        while (rs.next()) {
            String specificName = rs.getString("SPECIFIC_NAME");

            Routine routine = routineParamMap.get(specificName);
            if (routine == null) {
                routine = new Routine();
                routine.setName(specificName);
                routine.setComment(rs.getString("ROUTINE_COMMENT"));
                routineParamMap.put(specificName, routine);
            }
            String parameterName = rs.getString("PARAMETER_NAME");
            if (StringUtils.isNotBlank(parameterName)) {
                String dataType = rs.getString("DATA_TYPE");
                String maximumLength = rs.getString("CHARACTER_MAXIMUM_LENGTH");
                routine.addParam(parameterName, dataType, maximumLength, rs.getInt("ORDINAL_POSITION"));
            }

        }

        routineParamMap.forEach((k, v) -> {
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

    public List<Value> convertTriggerName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue trigger = new RdbValue();

            trigger.setValue(rs.getString("TRIGGER_NAME"));
            trigger.setUmiType(UmiTypes.Trigger);
            result.add(trigger);
        }

        return result;
    }

    public List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setSchema(rs.getString("TABLE_SCHEMA"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setComment(rs.getString("TABLE_COMMENT"));
            String tableType = rs.getString("TABLE_TYPE");
            if (StringUtils.endsWith(tableType, "VIEW")) {
                table.setUmiType(UmiTypes.View);
                table.setTableType(tableType);
            } else {
                table.setUmiType(UmiTypes.Table);
                table.setTableType(tableType);
            }

            String tableCollation = rs.getString("TABLE_COLLATION");
            if (StringUtils.isNotBlank(tableCollation)) {
                int index = tableCollation.indexOf("_");
                String charset = tableCollation.substring(0, index);

                table.setAttribute(CHARACTER_SET, charset);
                table.setAttribute(COLLATION, tableCollation);
            } else {
                table.setAttribute(CHARACTER_SET, null);
                table.setAttribute(COLLATION, null);
            }

            table.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_TIME, rs.getString("UPDATE_TIME"));
            table.setAttribute(ENGINE, rs.getString("ENGINE"));
            table.setAttribute(ROW_FORMAT, rs.getString("ROW_FORMAT"));
            table.setAttribute(AVG_ROW_LENGTH, rs.getString("AVG_ROW_LENGTH"));
            table.setAttribute(CHECK_TIME, rs.getString("CHECK_TIME"));
            long indexLength = rs.getLong("INDEX_LENGTH");
            table.setAttribute(INDEX_LENGTH, convertUnit(indexLength));
            long dataLength = rs.getLong("DATA_LENGTH");
            table.setAttribute(DATA_LENGTH, convertUnit(dataLength));
            table.setAttribute(DATA_ROW, rs.getString("TABLE_ROWS"));
            table.setAttribute(CREATE_OPTIONS, rs.getString("CREATE_OPTIONS"));
            table.setAttribute(DATA_FREE, convertUnit(rs.getLong("DATA_FREE")));

            String createOptions = rs.getString("CREATE_OPTIONS");
            containAndSetOpt("max_rows", createOptions, MAX_ROWS, table);
            containAndSetOpt("min_rows", createOptions, MIN_ROWS, table);
            containAndSetOpt("compression", createOptions, COMPRESSION, table);
            containAndSetOpt("key_block_size", createOptions, KEY_BLOCK_SIZE, table);

            table.setAttribute(AUTO_INCREMENT_STAR, rs.getString("AUTO_INCREMENT"));
            if (tableType.equalsIgnoreCase("TEMPORARY")) {
                table.setAttribute(TEMPORARY, "true");
            } else {
                table.setAttribute(TEMPORARY, "false");
            }
            result.add(table);
        }
        return result;
    }

    private String convertUnit(long length) {
        int i = 1;
        long remainder = 0;
        for (; i < units.length; i++) {
            if (length > 1024) {
                remainder = length % 1024;
                length = length / 1024;
            } else {
                break;
            }
        }
        String fractional = String.format("%.3f", remainder / 1024.0).split("\\.")[1];
        return String.format("%d.%s %s", length, fractional, units[i]);
    }

    public List<RdbTable> convertView(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbView table = new RdbView();
            table.setSchema(rs.getString("TABLE_SCHEMA"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setComment(rs.getString("TABLE_COMMENT"));
            table.setSql(rs.getString("VIEW_DEFINITION"));
            String tableType = rs.getString("TABLE_TYPE");
            if (StringUtils.endsWith(tableType, "VIEW")) {
                table.setUmiType(UmiTypes.View);
                table.setTableType(tableType);
            } else {
                table.setUmiType(UmiTypes.Table);
                table.setTableType(tableType);
            }

            String tableCollation = rs.getString("TABLE_COLLATION");
            if (StringUtils.isNotBlank(tableCollation)) {
                int index = tableCollation.indexOf("_");
                String charset = tableCollation.substring(0, index);

                table.setAttribute(CHARACTER_SET, charset);
                table.setAttribute(COLLATION, tableCollation);
            } else {
                table.setAttribute(CHARACTER_SET, null);
                table.setAttribute(COLLATION, null);
            }

            table.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_TIME, rs.getString("UPDATE_TIME"));
            table.setAttribute(ENGINE, rs.getString("ENGINE"));
            table.setAttribute(ROW_FORMAT, rs.getString("ROW_FORMAT"));
            table.setAttribute(AVG_ROW_LENGTH, rs.getString("AVG_ROW_LENGTH"));
            table.setAttribute(DEFINER, rs.getString("DEFINER"));
            table.setAttribute(SECURITY_TYPE, rs.getString("SECURITY_TYPE"));
            table.setAttribute(CHARACTER_SET, rs.getString("CHARACTER_SET_CLIENT"));
            table.setAttribute(COLLATION, rs.getString("COLLATION_CONNECTION"));
            table.setAttribute(VIEW_UPDATABLE, rs.getString("IS_UPDATABLE"));

            String createOptions = rs.getString("CREATE_OPTIONS");
            containAndSetOpt("max_rows", createOptions, MAX_ROWS, table);
            containAndSetOpt("min_rows", createOptions, MIN_ROWS, table);
            containAndSetOpt("compression", createOptions, COMPRESSION, table);
            containAndSetOpt("key_block_size", createOptions, KEY_BLOCK_SIZE, table);

            table.setAttribute(AUTO_INCREMENT_STAR, rs.getString("AUTO_INCREMENT"));
            if (tableType.equalsIgnoreCase("TEMPORARY")) {
                table.setAttribute(TEMPORARY, "true");
            } else {
                table.setAttribute(TEMPORARY, "false");
            }
            Map<String, Object> features = new HashMap<>();
            String checkOption = rs.getString("CHECK_OPTION");
            if (!"NONE".equals(checkOption)) {
                features.put(VIEW_CHECK_OPTION.getCodeKey(), checkOption);
            }
            table.setFeatures(features);
            result.add(table);
        }
        return result;
    }

    public List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        Pattern pattern = Pattern.compile(PART);

        while (rs.next()) {
            String table = rs.getString("TABLE_NAME");
            String colName = rs.getString("COLUMN_NAME");
            try {
                RdbColumn column = new RdbColumn();
                column.setTable(table);
                column.setName(colName);
                MySQLTypes sqlType = safeToMySqlTypes(rs.getString("DATA_TYPE"));
                column.setSqlType(sqlType);
                if (!"YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"))) {
                    column.addConstraint(new NonNull());
                }

                column.setAttribute(DATA_TYPE, rs.getString("DATA_TYPE"));
                column.setAttribute(COLUMN_TYPE, rs.getString("COLUMN_TYPE"));

                String columnType = column.getAttribute(COLUMN_TYPE);
                JDBCType jdbcType = columnTypeMappingToJdbcType(column.getSqlType(), columnType);
                column.setAttribute(JDBC_TYPE, jdbcType.getName());

                String dataDefault = rs.getString("COLUMN_DEFAULT");
                if (dataDefault != null) {
                    parseDefault(dataDefault, column);
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("DATETIME_PRECISION"), rs));
                switch (sqlType) {
                    case CHAR:
                    case VARCHAR: {
                        if (tryWasNull(rs.getInt("CHARACTER_MAXIMUM_LENGTH"), rs) != null) {
                            column.setCharLength((long) rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
                        }
                        break;
                    }
                    case ENUM:
                    case SET: {
                        if (StringUtils.isNotBlank(columnType)) {

                            Matcher matcher = pattern.matcher(columnType);
                            String content = "";
                            if (matcher.find()) {
                                content = matcher.group(1);
                            }

                            String[] values = content.split(",");

                            List<String> list = new ArrayList<>();
                            Collections.addAll(list, values);
                            column.setAttribute(DEFAULT_ENUMSET_COLLATION, JsonUtils.toJson(list));
                            break;
                        }
                    }
                    default: {
                        column.setNumericPrecision(tryWasNull(rs.getInt("NUMERIC_PRECISION"), rs));
                    }
                }
                column.setNumericScale(tryWasNull(rs.getInt("NUMERIC_SCALE"), rs));

                if (columnType.contains(" unsigned")) {
                    column.setAttribute(UNSIGNED, "true");
                } else {
                    column.setAttribute(UNSIGNED, "false");
                }

                if (columnType.contains(" zerofill")) {
                    column.setAttribute(ZEROFILL, "true");
                } else {
                    column.setAttribute(ZEROFILL, "false");
                }

                column.setAttribute(DEFAULT_COLLATION_NAME, rs.getString("COLLATION_NAME"));
                column.setAttribute(DEFAULT_CHARACTER_SET_NAME, rs.getString("CHARACTER_SET_NAME"));
                column.setAttribute(CHARACTERS_MAX_LENGTH, rs.getString("CHARACTER_MAXIMUM_LENGTH"));
                column.setAttribute(BYTES_MAX_LENGTH, rs.getString("CHARACTER_OCTET_LENGTH"));

                String extra = rs.getString("EXTRA");
                if (StringUtils.isNotBlank(extra)) {
                    if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_timestamp")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentTimestamp.getTypeName());
                    } else if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_date")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentDate.getTypeName());
                    } else if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_time")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentTime.getTypeName());
                    } else {
                        column.setAttribute(CURRENT_UPDATE_TYPE, null);
                    }
                    if (extra.equalsIgnoreCase("auto_increment")) {
                        column.setAttribute(AUTO_INCREMENT, "true");
                    } else {
                        column.setAttribute(AUTO_INCREMENT, "false");
                    }

                    if (extra.contains("GENERATED")) {
                        column.setAttribute(GENERATED, "true");
                    } else {
                        column.setAttribute(GENERATED, "false");
                    }

                } else {
                    column.setAttribute(CURRENT_UPDATE_TYPE, null);
                }

                column.setComment(rs.getString("COLUMN_COMMENT"));
                column.setIndex(rs.getInt("ORDINAL_POSITION"));
                columns.add(column);
            } catch (Exception e) {
                String msg = "extract " + table + " column (" + colName + ") error.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
                throw new SQLException(msg, e);
            }
        }
        return columns;
    }

    public List<RdbIndex> convertIndex(ResultSet rs) throws SQLException {
        List<RdbIndex> indexList = new ArrayList<>();
        while (rs.next()) {
            RdbIndex idx = new RdbIndex();
            idx.setSchema(rs.getString("TABLE_SCHEMA"));
            idx.setTable(rs.getString("TABLE_NAME"));
            idx.setName(rs.getString("INDEX_NAME"));
            idx.setComment(rs.getString("INDEX_COMMENT"));
            // must be first
            String indexType = rs.getString("INDEX_TYPE");
            if (indexType.equalsIgnoreCase("BTREE")) {
                if (rs.getString("NON_UNIQUE").equals("0")) {
                    idx.setAttribute(INDEX_WAY, "Unique");
                    idx.setType(RdbIndexType.Unique);
                } else {
                    idx.setAttribute(INDEX_WAY, "Normal");
                    idx.setType(RdbIndexType.Normal);
                }
                idx.setAttribute(INDEX_TYPE, "BTREE");
            } else if (indexType.equalsIgnoreCase("HASH")) {
                if (rs.getString("NON_UNIQUE").equals("0")) {
                    idx.setAttribute(INDEX_WAY, "Unique");
                } else {
                    idx.setAttribute(INDEX_WAY, "Normal");
                }
                idx.setAttribute(INDEX_TYPE, "HASH");
                idx.setType(RdbIndexType.Normal);
            } else if (indexType.equalsIgnoreCase("FULLTEXT")) {
                idx.setAttribute(INDEX_WAY, "FullText");
                idx.setAttribute(INDEX_TYPE, null);
                idx.setType(RdbIndexType.Normal);
            } else if (indexType.equalsIgnoreCase("SPATIAL")) {
                idx.setAttribute(INDEX_WAY, "SPATIAL");
                idx.setAttribute(INDEX_TYPE, null);
                idx.setType(RdbIndexType.Normal);
            }

            String comment = rs.getString("COMMENT");
            containAndSetOpt("key_block_size", comment, KEY_BLOCK_SIZE, idx);

            String columnName = rs.getString("COLUMN_NAME");
            idx.addColumn(columnName);

            HashMap<String, String> storageType = new HashMap<>();
            storageType.put(columnName, indexType);
            String storageTypeJson = JsonUtils.toJson(storageType);
            idx.setAttribute(STORAGE_TYPE, storageTypeJson);

            Integer indexSubPart = tryWasNull(rs.getInt("SUB_PART"), rs);
            indexSubPart = indexType.equalsIgnoreCase("SPATIAL") ? null : indexSubPart;

            HashMap<String, String> subPart = new HashMap<>();
            if (indexSubPart != null) {
                subPart.put(columnName, indexSubPart.toString());
            } else {
                subPart.put(columnName, null);
            }
            String subPartJson = JsonUtils.toJson(subPart);
            idx.setAttribute(SUB_PART, subPartJson);

            String indexColl = rs.getString("COLLATION");
            Map<String, String> subOrder = new HashMap<>();
            if (StringUtils.isNotBlank(indexColl)) {
                if (indexColl.equalsIgnoreCase("A")) {
                    indexColl = "ASC";
                } else if (indexColl.equalsIgnoreCase("D")) {
                    indexColl = "DESC";
                }
            }
            subOrder.put(columnName, indexColl);
            String subOrderJson = JsonUtils.toJson(subOrder);
            //idx.setAttribute(SUB_ORDER, subOrderJson);

            indexList.add(idx);
        }
        return indexList;
    }

    public Map<String, Map<String, RdbIndex>> convertIndexList(List<RdbIndex> idxList) {
        Map<String, Map<String, RdbIndex>> idxMap = new LinkedHashMap<>();
        for (RdbIndex index : idxList) {
            Map<String, RdbIndex> indexMap = idxMap.computeIfAbsent(index.getTable(), s -> new LinkedHashMap<>());
            String indexName = index.getName();
            if (indexMap.containsKey(indexName)) {
                indexMap.get(indexName).getColumnList().addAll(index.getColumnList());
                Map<String, String> indexMapAtr = indexMap.get(indexName).getAttributes();

                if (indexMapAtr.containsKey(SUB_PART.getCodeKey())) {
                    String oldJsonMap = indexMapAtr.get(SUB_PART.getCodeKey());
                    String newJsonMap = index.getAttribute(SUB_PART);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(SUB_PART, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(SUB_PART, index.getAttribute(SUB_PART));
                }

                //if (indexMapAtr.containsKey(SUB_ORDER.getCodeKey())) {
                //    String oldJsonMap = indexMapAtr.get(SUB_ORDER.getCodeKey());
                //    String newJsonMap = index.getAttribute(SUB_ORDER);
                //    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                //    indexMap.get(indexName).setAttribute(SUB_ORDER, resultMap);
                //} else {
                //    indexMap.get(indexName).setAttribute(SUB_ORDER, index.getAttribute(SUB_ORDER));
                //}

                if (indexMapAtr.containsKey(STORAGE_TYPE.getCodeKey())) {
                    String oldJsonMap = indexMapAtr.get(STORAGE_TYPE.getCodeKey());
                    String newJsonMap = index.getAttribute(STORAGE_TYPE);
                    String resultMap = mergeJsonMap(oldJsonMap, newJsonMap);
                    indexMap.get(indexName).setAttribute(STORAGE_TYPE, resultMap);
                } else {
                    indexMap.get(indexName).setAttribute(STORAGE_TYPE, index.getAttribute(STORAGE_TYPE));
                }

            } else {
                indexMap.put(indexName, index);
            }
        }
        return idxMap;
    }

    public List<RdbForeignKey> convertForeignKey(ResultSet resultSet) throws SQLException {
        List<RdbForeignKey> fkList = new ArrayList<>();
        while (resultSet.next()) {
            RdbForeignKey fk = new RdbForeignKey();
            fk.setName(resultSet.getString("CONSTRAINT_NAME"));
            fk.setTable(resultSet.getString("TABLE_NAME"));
            fk.setReferenceSchema(resultSet.getString("REFERENCED_TABLE_SCHEMA"));
            fk.setReferenceTable(resultSet.getString("REFERENCED_TABLE_NAME"));
            fk.addColumn(resultSet.getString("COLUMN_NAME"), resultSet.getString("REFERENCED_COLUMN_NAME"));
            fk.setDeleteRule(RdbForeignKeyRule.valueOfCode(resultSet.getString("DELETE_RULE")));
            fk.setUpdateRule(RdbForeignKeyRule.valueOfCode(resultSet.getString("UPDATE_RULE")));
            fkList.add(fk);
        }
        return fkList;
    }

    public Map<String, Map<String, RdbForeignKey>> convertForeignKeyList(List<RdbForeignKey> fkList) {
        Map<String, Map<String, RdbForeignKey>> fkMap = new LinkedHashMap<>();
        for (RdbForeignKey fk : fkList) {
            Map<String, RdbForeignKey> map = fkMap.computeIfAbsent(fk.getAttribute(FK_TABLE.getCodeKey()), s -> new LinkedHashMap<>());
            String fkName = fk.getName();
            if (map.containsKey(fkName)) {
                map.get(fkName).getColumnList().addAll(fk.getColumnList());
                map.get(fkName).getReferenceMapping().putAll(fk.getReferenceMapping());
            } else {
                map.put(fkName, fk);
            }
        }
        return fkMap;
    }

    public List<RdbProcedure> convertProcedures(ResultSet rs) throws SQLException {
        List<RdbProcedure> result = new ArrayList<>();
        while (rs.next()) {
            RdbProcedure rdbProcedure = new RdbProcedure();
            rdbProcedure.setCatalog(rs.getString("ROUTINE_CATALOG"));
            rdbProcedure.setSchema(rs.getString("ROUTINE_SCHEMA"));
            rdbProcedure.setName(rs.getString("SPECIFIC_NAME"));
            rdbProcedure.setSql(rs.getString("ROUTINE_DEFINITION"));
            HashMap<String, Object> features = new HashMap<>();
            String access = rs.getString("SQL_DATA_ACCESS");
            String deterministic = rs.getString("IS_DETERMINISTIC");
            features.put(SQL_DATA_ACCESS.getCodeKey(), access);
            features.put(DETERMINISTIC.getCodeKey(), "YES".equals(deterministic));
            rdbProcedure.setFeatures(features);

            rdbProcedure.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            rdbProcedure.setAttribute(UPDATE_TIME, rs.getString("LAST_ALTERED"));
            rdbProcedure.setAttribute(DEFINER, rs.getString("DEFINER"));
            rdbProcedure.setAttribute(SECURITY_TYPE, rs.getString("SECURITY_TYPE"));
            result.add(rdbProcedure);
        }

        return result;
    }

    public List<RdbFunction> convertFunctions(ResultSet rs) throws SQLException {
        List<RdbFunction> result = new ArrayList<>();
        while (rs.next()) {
            RdbFunction rdbFunction = new RdbFunction();
            rdbFunction.setCatalog(rs.getString("ROUTINE_CATALOG"));
            rdbFunction.setSchema(rs.getString("ROUTINE_SCHEMA"));
            rdbFunction.setName(rs.getString("SPECIFIC_NAME"));
            rdbFunction.setSql(rs.getString("ROUTINE_DEFINITION"));
            HashMap<String, Object> features = new HashMap<>();
            String access = rs.getString("SQL_DATA_ACCESS");
            String deterministic = rs.getString("IS_DETERMINISTIC");
            features.put(SQL_DATA_ACCESS.getCodeKey(), access);
            features.put(DETERMINISTIC.getCodeKey(), "YES".equals(deterministic));

            rdbFunction.setAttribute(CREATE_TIME, rs.getString("CREATED"));
            rdbFunction.setAttribute(UPDATE_TIME, rs.getString("LAST_ALTERED"));
            rdbFunction.setAttribute(DEFINER, rs.getString("DEFINER"));
            rdbFunction.setAttribute(SECURITY_TYPE, rs.getString("SECURITY_TYPE"));
            rdbFunction.setFeatures(features);
            result.add(rdbFunction);
        }

        return result;
    }

    public List<RdbParam> convertParams(ResultSet rs) throws SQLException {
        List<RdbParam> result = new ArrayList<>();
        while (rs.next()) {
            RdbParam rdbParam = new RdbParam();
            rdbParam.setCatalog(rs.getString("SPECIFIC_CATALOG"));
            rdbParam.setSchema(rs.getString("SPECIFIC_SCHEMA"));
            rdbParam.setReferenceObject(rs.getString("SPECIFIC_NAME"));
            rdbParam.setOrdinal(rs.getInt("ORDINAL_POSITION"));
            rdbParam.setName(rs.getString("PARAMETER_NAME"));
            rdbParam.setType(rs.getString("DATA_TYPE"));
            String parameterMode = rs.getString("PARAMETER_MODE");
            rdbParam.setMode(RdbParamMode.valueOfCode(parameterMode));

            String charLength = rs.getString("CHARACTER_MAXIMUM_LENGTH");
            rdbParam.setCharacterMaximumLength(charLength);

            rdbParam.setDatetimePrecision(rs.getInt("DATETIME_PRECISION"));
            rdbParam.setNumericPrecision(rs.getInt("NUMERIC_PRECISION"));
            rdbParam.setLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
            rdbParam.setNumericScale(rs.getInt("NUMERIC_SCALE"));
            result.add(rdbParam);
        }

        return result;
    }

    public void mapToPkExt(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");
        RdbPrimaryKey pk = (RdbPrimaryKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbPrimaryKey()));
        pk.setName(consName);
        String columnName = rs.getString("COLUMN_NAME");
        pk.addColumn(columnName);

        String comment = rs.getString("COMMENT");
        containAndSetOpt("key_block_size", comment, KEY_BLOCK_SIZE, pk);

        String pkType = rs.getString("INDEX_TYPE");
        if (pkType.equalsIgnoreCase("BTREE")) {
            pk.setAttribute(INDEX_TYPE, "BTREE");
        } else if (pkType.equalsIgnoreCase("HASH")) {
            pk.setAttribute(INDEX_TYPE, "HASH");
        }

        Integer indexSubPart = tryWasNull(rs.getInt("SUB_PART"), rs);
        HashMap<String, String> subPart = new HashMap<>();
        if (indexSubPart != null) {
            subPart.put(columnName, indexSubPart.toString());
        } else {
            subPart.put(columnName, null);
        }
        String newSubPartJson = JsonUtils.toJson(subPart);

        String indexColl = rs.getString("COLLATION");
        Map<String, String> subOrder = new HashMap<>();
        if (StringUtils.isNotBlank(indexColl)) {
            if (indexColl.equalsIgnoreCase("A")) {
                indexColl = "ASC";
            } else if (indexColl.equalsIgnoreCase("D")) {
                indexColl = "DESC";
            }
        }
        subOrder.put(columnName, indexColl);
        String newSubOrderJson = JsonUtils.toJson(subOrder);

        String indexName = rs.getString("INDEX_NAME"); // INDEX_NAME = PrimaryKey
        if (tabConstraint.containsKey(indexName)) {
            RdbPrimaryKey oldPk = (RdbPrimaryKey) tabConstraint.get(indexName);

            if (oldPk.getAttributes().containsKey(SUB_PART.getCodeKey())) {
                String oldSubPartJson = oldPk.getAttribute(SUB_PART);
                String resultJson = mergeJsonMap(oldSubPartJson, newSubPartJson);
                pk.setAttribute(SUB_PART, resultJson);
            } else {
                pk.setAttribute(SUB_PART, newSubPartJson);
            }

            //if (oldPk.getAttributes().containsKey(SUB_ORDER.getCodeKey())) {
            //    String oldSubPartJson = oldPk.getAttribute(SUB_ORDER);
            //    String resultJson = mergeJsonMap(oldSubPartJson, newSubOrderJson);
            //    pk.setAttribute(SUB_ORDER, resultJson);
            //} else {
            //    pk.setAttribute(SUB_ORDER, newSubOrderJson);
            //}

        } else {
            pk.setAttribute(SUB_PART, newSubPartJson);
            //pk.setAttribute(SUB_ORDER, newSubOrderJson);
        }

    }

    public void mapToUkExt(ResultSet rs, Map<String, UmiConstraint> tabConstraint) throws SQLException {
        String consName = rs.getString("CONSTRAINT_NAME");

        RdbUniqueKey uk = (RdbUniqueKey) (tabConstraint.computeIfAbsent(consName, n -> new RdbUniqueKey()));
        uk.setName(consName);
        String columnName = rs.getString("COLUMN_NAME");
        uk.addColumn(columnName);

        String comment = rs.getString("COMMENT");
        containAndSetOpt("key_block_size", comment, KEY_BLOCK_SIZE, uk);

        String pkType = rs.getString("INDEX_TYPE");
        if (pkType.equalsIgnoreCase("BTREE")) {
            uk.setAttribute(INDEX_TYPE, "BTREE");
        } else if (pkType.equalsIgnoreCase("HASH")) {
            uk.setAttribute(INDEX_TYPE, "HASH");
        }
        uk.setAttribute(INDEX_WAY, "Unique");

        Integer indexSubPart = tryWasNull(rs.getInt("SUB_PART"), rs);
        HashMap<String, String> subPart = new HashMap<>();
        if (indexSubPart != null) {
            subPart.put(columnName, indexSubPart.toString());
        } else {
            subPart.put(columnName, null);
        }
        String newSubPartJson = JsonUtils.toJson(subPart);

        String indexColl = rs.getString("COLLATION");
        Map<String, String> subOrder = new HashMap<>();
        subOrder.put(columnName, indexColl);
        String newSubOrderJson = JsonUtils.toJson(subOrder);

        String indexName = rs.getString("INDEX_NAME"); // INDEX_NAME = UniqueKey
        if (tabConstraint.containsKey(indexName)) {
            RdbUniqueKey oldPk = (RdbUniqueKey) tabConstraint.get(indexName);

            if (oldPk.getAttributes().containsKey(SUB_PART.getCodeKey())) {
                String oldSubPartJson = oldPk.getAttribute(SUB_PART);
                String resultJson = mergeJsonMap(oldSubPartJson, newSubPartJson);
                uk.setAttribute(SUB_PART, resultJson);
            } else {
                uk.setAttribute(SUB_PART, newSubPartJson);
            }

            //if (oldPk.getAttributes().containsKey(SUB_ORDER.getCodeKey())) {
            //    String oldSubPartJson = oldPk.getAttribute(SUB_ORDER);
            //    String resultJson = mergeJsonMap(oldSubPartJson, newSubOrderJson);
            //    uk.setAttribute(SUB_ORDER, resultJson);
            //} else {
            //    uk.setAttribute(SUB_ORDER, newSubOrderJson);
            //}

        } else {
            uk.setAttribute(SUB_PART, newSubPartJson);
            //uk.setAttribute(SUB_ORDER, newSubOrderJson);
        }

    }

    protected void containAndSetOpt(String opt, String createOptions, UmiAttributeNames attributeNames, AttributeUmiData umiData) {
        // (table) create_options: row_format=DYNAMIC stats_persistent=0 max_rows=55761 compression="zlip"
        // (index) index_comment : KEY_BLOCK_SIZE=16
        if (StringUtils.isNotBlank(createOptions)) {
            String[] strings = createOptions.split(" ");
            for (String str : strings) {
                String[] split = str.split("=");
                if (split[0].equalsIgnoreCase(opt)) {
                    String value = split[1];
                    if (value.startsWith("\"")) {
                        value = value.replace("\"", "");
                    }
                    umiData.setAttribute(attributeNames, value);
                }
                if (StringUtils.isBlank(umiData.getAttribute(attributeNames))) {
                    umiData.setAttribute(attributeNames, null);
                }
            }
        } else {
            umiData.setAttribute(attributeNames, null);
        }
    }

    protected MySQLTypes safeToMySqlTypes(Object obj) {
        String dat = (obj == null) ? null : obj.toString();
        for (MySQLTypes type : MySQLTypes.values()) {
            if (StringUtils.equalsIgnoreCase(type.getCodeKey(), dat)) {
                return type;
            }
        }
        return null;
    }

    protected JDBCType columnTypeMappingToJdbcType(FieldType typeDef, String columnType) {
        if (typeDef instanceof MySQLTypes && StringUtils.isNotBlank(columnType)) {
            MysqlType mysqlType = MysqlType.getByName(columnType);
            try {
                return JDBCType.valueOf(mysqlType.getJdbcType());
            } catch (Exception e) {
                return null;
            }
        } else if (typeDef != null) {
            return typeDef.toJDBCType();
        } else {
            return null;
        }
    }

    protected void parseDefault(String dataDefault, RdbColumn column) {
        boolean isBit = column.getSqlType() == MySQLTypes.BIT;
        boolean isBinary = column.getSqlType() == MySQLTypes.BINARY //
                           || column.getSqlType() == MySQLTypes.VARBINARY;
        boolean isDate = column.getSqlType() == MySQLTypes.TIMESTAMP //
                         || column.getSqlType() == MySQLTypes.DATE //
                         || column.getSqlType() == MySQLTypes.TIME //
                         || column.getSqlType() == MySQLTypes.DATETIME;
        boolean isNumber = column.getSqlType() == MySQLTypes.TINYINT //
                           || column.getSqlType() == MySQLTypes.SMALLINT //
                           || column.getSqlType() == MySQLTypes.MEDIUMINT//
                           || column.getSqlType() == MySQLTypes.INT//
                           || column.getSqlType() == MySQLTypes.BIGINT//
                           || column.getSqlType() == MySQLTypes.FLOAT//
                           || column.getSqlType() == MySQLTypes.DOUBLE//
                           || column.getSqlType() == MySQLTypes.DECIMAL;

        if (isBit) {
            if (dataDefault.startsWith("b'") && dataDefault.endsWith("'")) {
                String bitStr = dataDefault.substring(2, dataDefault.length() - 1);
                column.setDefaultValue("0x" + HexadecimalUtils.bit2hex(bitStr));
            } else {
                column.setDefaultValue(dataDefault);
            }
        } else if (isBinary) {
            if (dataDefault.startsWith("b'") && dataDefault.endsWith("'")) {
                String bitStr = dataDefault.substring(2, dataDefault.length() - 1);
                column.setDefaultValue("0x" + HexadecimalUtils.bit2hex(bitStr));
            } else if (dataDefault.startsWith("0x") || dataDefault.startsWith("0X")) {
                // mysql 8.0
                column.setDefaultValue(dataDefault);
            } else if (dataDefault.length() > 0) {
                // mysql 5.6/5.7
                StringBuilder hexBuilder = new StringBuilder("0x");
                for (char aChar : dataDefault.toCharArray()) {
                    hexBuilder.append(Integer.toHexString(aChar).toUpperCase());
                }
                column.setDefaultValue(hexBuilder.toString());
            }
        } else if (isDate) {
            parseDefaultTime(dataDefault, column);
        } else if (isNumber) {
            parseDefaultNumber(dataDefault, column);
        } else {
            column.setDefaultValue(dataDefault);
        }
    }

    protected void parseDefaultNumber(String dataDefault, RdbColumn column) {
        boolean isFunc = false;
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));

        if (isFunc) {
            column.setDefaultValueIsFunc(true);
        } else {
            column.setDefaultValue(dataDefault);
        }
    }

    protected void parseDefaultTime(String dataDefault, RdbColumn column) {
        if (isFunc(dataDefault)) {
            column.setDefaultValueIsFunc(true);
            column.setDefaultValue(dataDefault);
        } else {
            DateFormatType dateFormatType = DateFormatType.passerTypeWithoutUnsupported(dataDefault);
            if (dateFormatType != null) {
                column.setDefaultValue(dataDefault);
            }
        }
    }

    public boolean isFunc(String dataDefault) {
        boolean isFunc = false;
        isFunc = isFunc | dataDefault.matches("CURRENT_TIMESTAMP(\\((\\d+)?\\))?");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "NOW()");
        isFunc = isFunc | StringUtils.equalsIgnoreCase(dataDefault, "CURDATE()");
        isFunc = isFunc | (dataDefault.length() > 2 && dataDefault.startsWith("(") && dataDefault.endsWith(")"));
        return isFunc;
    }

    private String mergeJsonMap(String oldJsonMap, String newJsonMap) {
        oldJsonMap = oldJsonMap.substring(0, oldJsonMap.length() - 1);
        newJsonMap = newJsonMap.substring(1);
        return oldJsonMap + "," + newJsonMap;
    }

    public List<RdbTrigger> convertTrigger(ResultSet rs) throws SQLException {
        List<RdbTrigger> result = new ArrayList<>();
        while (rs.next()) {
            RdbTrigger rdbTrigger = new RdbTrigger();
            rdbTrigger.setName(rs.getString("TRIGGER_NAME"));
            rdbTrigger.setSql(rs.getString("ACTION_STATEMENT"));
            rdbTrigger.setTriggerTime(rs.getString("ACTION_TIMING"));
            rdbTrigger.setTriggerEvent(Collections.singletonList(rs.getString("EVENT_MANIPULATION")));
            rdbTrigger.setTriggerTableName(rs.getString("EVENT_OBJECT_TABLE"));
            rdbTrigger.setAttribute(DEFINER, rs.getString("DEFINER"));
            rdbTrigger.setAttribute(CREATE_TIME, rs.getString("CREATED"));

            result.add(rdbTrigger);
        }
        return result;
    }

    public Map<String, RdbPartition> convertPartitions(ResultSet rs) throws SQLException {
        Map<String, RdbPartition> result = new HashMap<>();
        Map<String, RdbPartitionItem> itemMap = new HashMap<>();
        while (rs.next()) {
            RdbPartition partition = result.get(rs.getString("TABLE_NAME"));
            String key = rs.getString("TABLE_NAME") + rs.getString("PARTITION_NAME");
            RdbPartitionItem item = itemMap.get(key);
            if (partition == null) {
                partition = new RdbPartition();
                partition.setAttribute(PARTITION_METHOD, rs.getString("PARTITION_METHOD"));
                partition.setAttribute(PARTITION_EXPRESSION, rs.getString("PARTITION_EXPRESSION"));
                partition.setAttribute(SUB_PARTITION_METHOD, rs.getString("SUBPARTITION_METHOD"));
                partition.setAttribute(SUB_PARTITION_EXPRESSION, rs.getString("SUBPARTITION_EXPRESSION"));
            }
            if (item == null) {
                item = new RdbPartitionItem();
                item.setName(rs.getString("PARTITION_NAME"));
                item.setDescription(rs.getString("PARTITION_DESCRIPTION"));
                partition.getPtItems().add(item);
            }
            if (rs.getString("SUBPARTITION_NAME") != null) {
                RdbPartitionItem subItem = new RdbPartitionItem();
                subItem.setName(rs.getString("SUBPARTITION_NAME"));
                item.getSubItems().add(subItem);
            }
            itemMap.put(key, item);

            result.put(rs.getString("TABLE_NAME"), partition);

        }
        return result;

    }

    private class Routine extends RoutineUmiData {

    }
}
