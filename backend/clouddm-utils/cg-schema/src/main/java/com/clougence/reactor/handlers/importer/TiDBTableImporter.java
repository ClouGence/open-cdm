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

import static com.clougence.adapter.tidb.TiDBAttributeNames.INDEX_TYPE;
import static com.clougence.adapter.tidb.TiDBAttributeNames.SUB_PART;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.mysql.MySQLIndexType;
import com.clougence.adapter.tidb.TiDBTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

/**
 * @author mode 2021/1/8 19:56
 */
public class TiDBTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // timestamp
        processTimestampStrategy(tableEditor, triggerContext);

        // process maxRowSize
        processMaxRowSize(tableEditor);

        // process maxKeySize
        processMaxKeySize(tableEditor, triggerContext, dstMainVersion);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void processMaxKeySize(TableEditor tableEditor, TriggerContext triggerContext, MainVersion dstMainVersion) {
        if (dstMainVersion == null) {
            return;
        }

        processMaxKeySize(tableEditor, 3072, 768);
        processIdxWithText(tableEditor, 3072);
    }

    private void processIdxWithText(TableEditor tableEditor, int defaultMaxKeySize) {
        List<String> indexNames = tableEditor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            String idxName = indexEditor.getSource().getName();
            List<String> columnList = indexEditor.getSource().getColumnList();

            boolean hasLob = false;
            boolean allFullText = true;
            int lobMaxKeySize = defaultMaxKeySize;
            Map<String, Integer> blobSubPart = new HashMap<>();

            for (String column : columnList) {
                ColumnEditor editorColumn = tableEditor.getColumn(column);
                TiDBTypes type = TiDBTypes.valueOfCode(editorColumn.getSource().getDbType());

                if (isFullText(type)) {
                    blobSubPart.put(column, null);
                } else {
                    lobMaxKeySize -= typeOfSize(type, editorColumn.getSource());
                }

                allFullText = allFullText && isFullText(type);
                hasLob = hasLob || isLob(type);
            }

            if (!hasLob) {
                continue;
            }

            // has blob / text in index
            indexEditor.delete();
            IndexEditor editor = tableEditor.addIndexEditor(idxName, EIndexType.Normal, columnList);
            if (allFullText) {
                // all column is blob / text => normal fulltext index
                INDEX_TYPE.setValue(editor.getSource().getAttribute(), MySQLIndexType.FullText.name());
            } else {
                // blob / number / time / geo not support fulltext index => prefix (unique / normal) index
                int maxCollationBytes = 4;
                int avgSize = lobMaxKeySize / (blobSubPart.size() * maxCollationBytes);
                blobSubPart.forEach((k, v) -> blobSubPart.put(k, avgSize));
                SUB_PART.setValue(editor.getSource().getAttribute(), JSON.toString(blobSubPart));
            }
        }
    }

    private boolean isFullText(TiDBTypes type) {
        return !type.isGeometry() && !type.isNumber() && !type.isDataOrTime() && !type.isBinary();
    }

    private boolean isLob(TiDBTypes type) {
        switch (type) {
            case BLOB:
            case TINYBLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TEXT:
            case TINYTEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
                return true;
            default:
                return false;
        }
    }

    @SneakyThrows
    private void processMaxKeySize(TableEditor tableEditor, int defaultMaxKeySize, int partKeySize) {
        Map<String, List<String>> dispose = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> disposeParts = new LinkedHashMap<>();

        for (String index : tableEditor.getIndexes()) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(index);
            EIndex eIndex = indexEditor.getSource();
            String subPartJson = SUB_PART.getValue(eIndex.getAttribute());
            Map<String, Integer> subPart = StringUtils.isBlank(subPartJson) ? new HashMap<>() : //
                new ObjectMapper().readValue(subPartJson, new TypeReference<Map<String, Integer>>() {
                });

            List<String> columnList = eIndex.getColumnList();
            for (String column : columnList) {
                ColumnEditor columnEditor = tableEditor.getColumn(column);
                TiDBTypes types = TiDBTypes.valueOfCode(columnEditor.getSource().getDbType());
                long columnSize = typeOfSize(types, columnEditor.getSource());
                if (columnSize >= defaultMaxKeySize) {
                    subPart.put(column, partKeySize);
                }
            }

            boolean hasAnyPartColumn = subPart.values().stream().anyMatch(Objects::nonNull);
            if (hasAnyPartColumn) {
                dispose.put(index, columnList);
                disposeParts.put(index, subPart);
            }
        }

        dispose.forEach((index, columns) -> {
            tableEditor.getIndexEditor(index).delete();
            IndexEditor indexEditor = tableEditor.addIndexEditor(index, EIndexType.Normal, columns);
            Map<String, Integer> indexSubParts = disposeParts.get(index);
            if (indexSubParts != null) {
                SUB_PART.setValue(indexEditor.getSource().getAttribute(), JSON.toString(indexSubParts));
            }
        });
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        TiDBTypes sqlTypes = TiDBTypes.valueOfCode(domain.getDbType());

        // CHAR / VARCHAR default has value but length out of size
        processDefaultValueLengthOutOfColumnLength(sqlTypes, columnEditor);

        // decimal P/M fix
        decimalDefaultCompletion(sqlTypes, columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);

        // BLOB/TEXT/JSON has not default value
        removeDefaultValue(sqlTypes, columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void processDefaultValueLengthOutOfColumnLength(TiDBTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        if (StringUtils.isBlank(defaultValue) || domain.isDefaultValueIsFunc()) {
            return;
        }
        switch (sqlTypes) {
            case CHAR:
            case VARCHAR:
            case BINARY:
            case VARBINARY:
            case BIT:
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

    private void decimalDefaultCompletion(TiDBTypes sqlTypes, ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn source = columnEditor.getSource();
        String defaultValue = source.getDefaultValue();
        if (sqlTypes != TiDBTypes.DECIMAL || source.isDefaultValueIsFunc() || StringUtils.isBlank(defaultValue)) {
            return;
        }

        Integer metaPrecision = source.getNumericPrecision();
        Integer metaScale = source.getNumericScale();
        Integer samplePrecision = null;
        Integer sampleScale = null;

        String[] split = defaultValue.split("\\.");
        if (split.length == 1) {
            samplePrecision = split[0].length();
            sampleScale = 0;
        } else if (split.length == 2) {
            samplePrecision = split[0].length();
            sampleScale = split[1].length();
        }

        if (metaPrecision != null && metaScale != null && samplePrecision != null && sampleScale != null) {
            samplePrecision = (metaPrecision >= samplePrecision) ? metaPrecision : samplePrecision;
            sampleScale = (metaScale >= sampleScale) ? metaScale : sampleScale;
            columnEditor.setNumberLength(samplePrecision, sampleScale);
        }
    }

    private void removeDefaultValue(TiDBTypes sqlTypes, ColumnEditor columnEditor, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        if (StringUtils.isBlank(defaultValue)) {
            return;
        }
        switch (sqlTypes) {
            case BLOB:
            case TEXT:
            case JSON:
                break;
            default:
                return;
        }

        columnEditor.setDefault("", false);
        columnEditor.setNullable(true);
    }

    private void processTimestampStrategy(TableEditor tableEditor, TriggerContext triggerContext) {
        // find all timestamp column
        List<String> timestampColumns = tableEditor.getSource().getColumnList().stream().filter(eColumn -> {
            TiDBTypes sqlTypes = TiDBTypes.valueOfCode(eColumn.getDbType());
            return sqlTypes == TiDBTypes.TIMESTAMP;
        }).map(EColumn::getName).collect(Collectors.toList());

        for (int i = 1; i < timestampColumns.size(); i++) {
            String columnName = timestampColumns.get(i);
            ColumnEditor editorColumn = tableEditor.getColumn(columnName);
            String defaultValue = editorColumn.getSource().getDefaultValue();
            if (StringUtils.isBlank(defaultValue) || StringUtils.startsWith(defaultValue, "0000-00-00 00:00:00")) {
                editorColumn.setNullable(true);
                editorColumn.setDateTimePrecision(0);
                editorColumn.setDefault("", false);
            }
        }
    }

    private void processMaxRowSize(TableEditor tableEditor) {
        long rowSize = evalRowSize(tableEditor, tableEditor.getColumnNames());
        if (rowSize >= 65535) {
            processColumns(tableEditor, rowSize - 65535);
        }
    }

    private long evalRowSize(TableEditor tableEditor, List<String> columnNames) {
        long rowSize = 0;
        for (String columnName : columnNames) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            TiDBTypes types = TiDBTypes.valueOfCode(columnEditor.getSource().getDbType());
            rowSize += typeOfSize(types, columnEditor.getSource());
        }
        return rowSize;
    }

    private void processColumns(TableEditor tableEditor, long rowOverFlowSize) {

        Map<String, TiDBTypes> columnTypes = new HashMap<>();
        List<ColumnEditor> needProcess = new ArrayList<>();
        for (String columnName : tableEditor.getColumnNames()) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            EColumn eColumn = columnEditor.getSource();

            TiDBTypes types = TiDBTypes.valueOfCode(eColumn.getDbType());
            if (isChars(types) || isBinary(types)) {
                needProcess.add(columnEditor);
                columnTypes.put(eColumn.getName(), types);
            }
        }
        needProcess.sort((o1, o2) -> {
            TiDBTypes t1 = columnTypes.get(o1.getSource().getName());
            TiDBTypes t2 = columnTypes.get(o2.getSource().getName());
            long typeSize1 = typeOfSize(t1, o1.getSource());
            long typeSize2 = typeOfSize(t2, o2.getSource());
            return -Long.compare(typeSize1, typeSize2);
        });

        for (ColumnEditor column : needProcess) {
            EColumn eColumn = column.getSource();

            TiDBTypes type = columnTypes.get(eColumn.getName());
            long typeSize = typeOfSize(type, eColumn);

            if (typeSize < 20) {
                continue;
            }

            if (isChars(type)) {
                column.changeType(TiDBTypes.TEXT.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }
            if (isBinary(type)) {
                column.changeType(TiDBTypes.BLOB.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }

            if (rowOverFlowSize <= 0) {
                return;
            }
        }
    }

    private boolean isChars(TiDBTypes type) {
        switch (type) {
            case CHAR:
            case VARCHAR:
                return true;
            default:
                return false;
        }
    }

    private boolean isBinary(TiDBTypes type) {
        switch (type) {
            case BIT:
            case BINARY:
            case VARBINARY:
                return true;
            default:
                return false;
        }
    }

    // https://dev.mysql.com/doc/refman/5.7/en/storage-requirements.html
    private long typeOfSize(TiDBTypes type, EColumn eColumn) {
        Long length = eColumn.getLength();
        if (length == null || length <= 0) {
            length = 0L;
        }

        switch (type) {
            case BIT:
                return (length + 7) / 8; // BIT(M) approximately (M+7)/8 bytes
            case TINYINT:
            case YEAR:
                return 1;
            case SMALLINT:
                return 2;
            case MEDIUMINT:
            case DATE:
                return 3;
            case INT:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
                return 8;
            case DECIMAL:
                return 8; // max is 8 bytes.
            case DATETIME:
                return 8; // 5 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIMESTAMP:
                return 7; // 4 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIME:
                return 6; // 3 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case CHAR:
            case VARCHAR:
                return length * 4;// use utf-8 length TODO Character set factors should be considered
            case BINARY:
            case VARBINARY:
                return length;
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case JSON:
                return 20;
            case ENUM:
                return 2; // 1 or 2 bytes, depending on the number of enumeration values (65,535 values maximum)
            case SET:
                return 8; // 1, 2, 3, 4, or 8 bytes, depending on the number of set members (64 members maximum)
            default:
                break;
        }
        return 0;
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return TiDBTypes.valueOfCode(columnType);
    }
}
