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
package com.clougence.reactor.handlers.importer;

import static com.clougence.adapter.oracle.OracleAttributeNames.DEFAULT_VALUE_OF_TIME_TYPE;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

/**
 * @author mode 2021/1/8 19:56
 */
public class OracleTableImporter extends AbstractRdbTableImporter {

    private static final Random random     = new Random(System.currentTimeMillis());
    private static final int    multiplier = 3;

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // CLOB/BLOB not in idx and pk
        idxClobAndBlobColumn(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // pk name set auto.
        pkRename(tableEditor);

        // index replication (shared columns)
        indexAntiDuplication(tableEditor);

        // idx name length
        idxRename(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void idxClobAndBlobColumn(TableEditor tableEditor) {
        PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        if (primaryEditor != null) {
            for (String columnName : primaryEditor.getSource().getColumnList()) {
                ColumnEditor editorColumn = tableEditor.getColumn(columnName);
                OracleSqlTypes oracleType = OracleSqlTypes
                    .toOracleType(editorColumn.getSource().getDbType(), editorColumn.getSource().getNumericPrecision(), editorColumn.getSource().getNumericScale());
                if (testBlobOrClob(oracleType)) {
                    primaryEditor.delete();
                    break;
                }
            }
        }

        List<String> indexes = tableEditor.getIndexes();
        for (String index : indexes) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(index);

            List<String> needRemove = new ArrayList<>();
            for (String columnName : indexEditor.getSource().getColumnList()) {
                ColumnEditor editorColumn = tableEditor.getColumn(columnName);
                OracleSqlTypes oracleType = OracleSqlTypes
                    .toOracleType(editorColumn.getSource().getDbType(), editorColumn.getSource().getNumericPrecision(), editorColumn.getSource().getNumericScale());
                if (testBlobOrClob(oracleType)) {
                    needRemove.add(columnName);
                }
            }

            if (indexEditor.getSource().getColumnList().size() == needRemove.size()) {
                indexEditor.delete();
            } else {
                indexEditor.removeColumn(needRemove.toArray(new String[0]));
            }
        }
    }

    private void pkRename(TableEditor tableEditor) {
        PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        if (primaryEditor != null) {
            primaryEditor.rename(null);
        }
    }

    private void indexAntiDuplication(TableEditor tableEditor) {
        Set<String> mastColumnSet = new HashSet<>();

        // pk
        PrimaryEditor primaryKey = tableEditor.getPrimaryEditor();
        if (primaryKey != null) {
            primaryKey.rename(null);
            List<String> pkColumn = primaryKey.getSource().getColumnList();

            String joinPkColumn = "'" + pkColumn.stream().map(s -> s.replace("'", "\\'")).collect(Collectors.joining("','")) + "'";

            mastColumnSet.add(joinPkColumn);
        }

        // uk
        for (String idxName : tableEditor.getIndexes()) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(idxName);
            if (indexEditor.getSource().getType() != EIndexType.Unique) {
                continue;
            }

            List<String> columnList = indexEditor.getSource().getColumnList();
            Collections.sort(columnList);
            String joinColumn = "'" + columnList.stream().map(s -> s.replace("'", "\\'")).collect(Collectors.joining("','")) + "'";

            if (mastColumnSet.contains(joinColumn)) {
                indexEditor.delete();
            } else {
                mastColumnSet.add(joinColumn);
            }
        }

        // idx
        for (String idxName : tableEditor.getIndexes()) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(idxName);
            if (indexEditor.getSource().getType() == EIndexType.Unique) {
                continue;
            }

            List<String> columnList = indexEditor.getSource().getColumnList();
            Collections.sort(columnList);
            String joinColumn = "'" + columnList.stream().map(s -> s.replace("'", "\\'")).collect(Collectors.joining("','")) + "'";

            if (mastColumnSet.contains(joinColumn)) {
                indexEditor.delete();
            } else {
                mastColumnSet.add(joinColumn);
            }
        }
    }

    private void idxRename(TableEditor tableEditor) {
        for (String idxName : tableEditor.getIndexes()) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(idxName);

            String nextInt = StringUtils.leftPad(String.valueOf(random.nextInt(9999)), 4, '0');
            if (idxName.length() > 25) {
                idxName = idxName.substring(0, 25);
            }
            idxName = idxName + "_" + nextInt;

            indexEditor.rename(idxName);
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        OracleSqlTypes sqlTypes = OracleSqlTypes.valueOfCode(domain.getDbType());

        // CHAR / VARCHAR default has value but length out of size
        processDefaultValueLengthOutOfColumnLength(sqlTypes, columnEditor);

        if (srcType != dstType) {
            // process form other db chars type
            enlargeCharsLength(multiplier, columnEditor);

            // process time default.
            convertDefaultToDate(columnEditor);
        }
    }

    private void processDefaultValueLengthOutOfColumnLength(OracleSqlTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        if (StringUtils.isBlank(defaultValue) || domain.isDefaultValueIsFunc()) {
            return;
        }
        switch (sqlTypes) {
            case CHAR:
            case NCHAR:
            case VARCHAR2:
            case NVARCHAR:
            case NVARCHAR2:
                // case RAW:
                break;
            default:
                return;
        }

        int defaultLength = defaultValue.length();
        Long length = domain.getLength();
        if (length != null && length < defaultLength) {
            columnEditor.setCharLength((long) defaultLength);
        }
    }

    private boolean testBlobOrClob(OracleSqlTypes oracleType) {
        switch (oracleType) {
            case CLOB:
            case NCLOB:
            case BLOB:
                return true;
            default:
                return false;
        }
    }

    private void enlargeCharsLength(int multiplier, ColumnEditor columnEditor) {
        OracleSqlTypes oracleType = OracleSqlTypes.valueOfCode(columnEditor.getSource().getDbType());
        switch (oracleType) {
            case CHAR:
            case NCHAR:
            case VARCHAR2:
            case NVARCHAR:
            case NVARCHAR2: {
                Long length = columnEditor.getSource().getLength();
                if (length == null || length == 0) {
                    columnEditor.setCharLength(1L);
                } else {
                    columnEditor.setCharLength(length * Math.max(1, multiplier));
                }
                break;
            }
            default:
                break;
        }
    }

    private void convertDefaultToDate(ColumnEditor columnInfo) {
        EColumn eColumn = columnInfo.getSource();
        if (eColumn.isDefaultValueIsFunc()) {
            return;
        }

        String defaultValue = eColumn.getDefaultValue();
        DateFormatType timeType = DateFormatType.valueOfCode(DEFAULT_VALUE_OF_TIME_TYPE.getValue(eColumn.getAttribute()));
        OracleSqlTypes types = OracleSqlTypes.valueOfCode(eColumn.getDbType());
        Predicate<String> testZeroTime = t -> t.startsWith("0000-00-00 00:00:00");

        switch (types) {
            case DATE:
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            case INTERVAL_YEAR_TO_MONTH:
            case INTERVAL_DAY_TO_SECOND: {
                if (timeType == null) {
                    columnInfo.setDefault(null, false);
                } else if (defaultValue != null && !testZeroTime.test(defaultValue)) {
                    columnInfo.setDefault(dataValueConvertToFunc(defaultValue, timeType), true);
                } else {
                    columnInfo.setDefault(null, false);
                }
                break;
            }
            default:
                break;
        }
    }

    private String dataValueConvertToFunc(String defaultValue, DateFormatType timeType) {
        switch (timeType) {
            case s_yyyyMMdd:
                return "to_date('" + defaultValue + "','yyyy-MM-dd')";
            case d_yyyyMMdd:
                return "to_date('" + defaultValue + "','yyyy/MM/dd')";
            case HHmmss:
                return "to_date('" + defaultValue + "','hh24:mi:ss')";
            case HHmmss_S:
            case HHmmss_SS:
            case HHmmss_SSS:
            case HHmmss_SSSS:
            case HHmmss_SSSSS:
            case HHmmss_SSSSSS:
                return "to_timestamp('" + defaultValue + "','hh24:mi:ss.FF')";
            case s_yyyyMMdd_HHmmss:
                return "to_date('" + defaultValue + "','yyyy-MM-dd HH24:mi:ss')";
            case s_yyyyMMdd_HHmmss_S:
            case s_yyyyMMdd_HHmmss_SS:
            case s_yyyyMMdd_HHmmss_SSS:
            case s_yyyyMMdd_HHmmss_SSSS:
            case s_yyyyMMdd_HHmmss_SSSSS:
            case s_yyyyMMdd_HHmmss_SSSSSS:
                return "to_timestamp('" + defaultValue + "','yyyy-MM-dd HH24:mi:ss.FF')";
            case d_yyyyMMdd_HHmmss:
                return "to_date('" + defaultValue + "','yyyy/MM/dd HH24:mi:ss')";
            case d_yyyyMMdd_HHmmss_S:
            case d_yyyyMMdd_HHmmss_SS:
            case d_yyyyMMdd_HHmmss_SSS:
            case d_yyyyMMdd_HHmmss_SSSS:
            case d_yyyyMMdd_HHmmss_SSSSS:
            case d_yyyyMMdd_HHmmss_SSSSSS:
                return "to_timestamp('" + defaultValue + "','yyyy/MM/dd HH24:mi:ss.FF')";
            default:
                return null;
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return OracleSqlTypes.valueOfCode(columnType);
    }
}
