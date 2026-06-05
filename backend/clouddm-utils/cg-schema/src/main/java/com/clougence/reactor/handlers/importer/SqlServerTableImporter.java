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

import static com.clougence.adapter.sqlserver.SqlServerAttributeNames.DEFAULT_VALUE_OF_TIME_TYPE;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MSSQL_DATAFORMAT_TYPE;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MSSQL_DROP_IDENTITY;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.adapter.sqlserver.SqlServerDataFormatType;
import com.clougence.adapter.sqlserver.SqlServerTypes;
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
public class SqlServerTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // index replication (shared columns)
        indexAntiDuplication(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // CLOB/BLOB not in idx and pk
        idxClobAndBlobColumn(tableEditor);

        // column drop identity feature if it exist.
        dropIdentity(tableEditor, triggerContext);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
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

    private void idxClobAndBlobColumn(TableEditor tableEditor) {
        PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        if (primaryEditor != null) {
            for (String columnName : primaryEditor.getSource().getColumnList()) {
                ColumnEditor editorColumn = tableEditor.getColumn(columnName);
                SqlServerTypes msType = SqlServerTypes.valueOfCode(editorColumn.getSource().getDbType());
                if (testBlobOrClob(msType)) {
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
                SqlServerTypes msType = SqlServerTypes.valueOfCode(editorColumn.getSource().getDbType());
                if (testBlobOrClob(msType)) {
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

    private void dropIdentity(TableEditor tableEditor, TriggerContext triggerContext) {
        boolean doDrop = StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), triggerContext.getAttribute(MSSQL_DROP_IDENTITY));
        if (!doDrop) {
            return;
        }

        for (String col : tableEditor.getColumnNames()) {
            ColumnEditor colEditor = tableEditor.getColumn(col);
            if (colEditor.getSource().isAutoGenerate()) {
                colEditor.setAutoGenerate(false);
            }
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

        // process time default.
        convertDefaultToDate(columnEditor, triggerContext);

        // has unsigned upgrade to Uxxx
        processUnsignedUpgrade(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void convertDefaultToDate(ColumnEditor columnInfo, TriggerContext triggerContext) {
        EColumn eColumn = columnInfo.getSource();
        if (eColumn.isDefaultValueIsFunc()) {
            return;
        }

        SqlServerDataFormatType dfType = SqlServerDataFormatType.valueOfCode(triggerContext.getAttribute(MSSQL_DATAFORMAT_TYPE));

        String defaultValue = eColumn.getDefaultValue();
        DateFormatType timeType = DateFormatType.valueOfCode(DEFAULT_VALUE_OF_TIME_TYPE.getValue(eColumn.getAttribute()));
        SqlServerTypes types = SqlServerTypes.valueOfCode(eColumn.getDbType());
        Predicate<String> testZeroTime = t -> t.startsWith("0000-00-00 00:00:00");

        switch (types) {
            case DATE:
            case DATETIMEOFFSET:
            case DATETIME2:
            case SMALLDATETIME:
            case DATETIME:
            case TIME: {
                if (timeType == null) {
                    columnInfo.setDefault(null, false);
                } else if (defaultValue != null && !testZeroTime.test(defaultValue)) {
                    columnInfo.setDefault(dataFormatValueConvert(defaultValue, timeType, dfType), false);
                } else {
                    columnInfo.setDefault(null, false);
                }
                break;
            }
            default:
                break;
        }
    }

    private void processUnsignedUpgrade(ColumnEditor column, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn eColumn = column.getSource();
        boolean unsigned = eColumn.isNumericUnsigned();
        if (!unsigned) {
            return;
        }
        SqlServerTypes sqlType = SqlServerTypes.valueOfCode(eColumn.getDbType());
        switch (sqlType) {
            case TINYINT:
                column.changeType(SqlServerTypes.SMALLINT.getCodeKey());
                break;
            case SMALLINT:
                column.changeType(SqlServerTypes.INT.getCodeKey());
                break;
            case INT:
            case BIGINT:
                column.changeType(SqlServerTypes.BIGINT.getCodeKey());
                break;
            case FLOAT:
            case REAL:
                column.changeType(SqlServerTypes.REAL.getCodeKey());
                break;
            case SMALLMONEY:
            case MONEY:
                column.changeType(SqlServerTypes.MONEY.getCodeKey());
                break;
            case NUMERIC:
            case DECIMAL:
                column.changeType(SqlServerTypes.DECIMAL.getCodeKey());
                break;
            default:
                break;
        }
    }

    private String dataFormatValueConvert(String defaultValue, DateFormatType timeType, SqlServerDataFormatType srcDfType) {
        return defaultValue;
        //        LocalDateTime localDateTime = timeType.toLocalDateTime(defaultValue);
        //        switch (srcDfType) {
        //            case YMD:
        //                return DateFormatType.s_yyyyMMdd_HHmmss_SSSSSSS.format(localDateTime);
        //            case MYD:
        //            case MDY:
        //            case YDM:
        //            case DYM:
        //            case DMY:
        //            default:
        //                return defaultValue;
        //        }
    }

    private boolean testBlobOrClob(SqlServerTypes oracleType) {
        switch (oracleType) {
            case TEXT:
            case NTEXT:
            case IMAGE:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return SqlServerTypes.valueOfCode(columnType);
    }

}
