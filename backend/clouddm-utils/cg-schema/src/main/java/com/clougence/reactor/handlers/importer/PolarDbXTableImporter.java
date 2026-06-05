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

import static com.clougence.adapter.polar.porx.PolarDbXAttributeNames.CURRENT_UPDATE_TYPE;
import static com.clougence.adapter.polar.porx.PolarDbXAttributeNames.SUB_PART;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MYSQL_IS_ALI_SQL;
import static com.clougence.reactor.handlers.attributes.HandlersAttributeNames.MYSQL_ZERO_TIME_STRATEGY;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.polar.porx.PolarDbXOnCurrentUpdateType;
import com.clougence.adapter.polar.porx.PolarDbXTypes;
import com.clougence.reactor.handlers.attributes.TimeDefaultStrategyEnum;
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
public class PolarDbXTableImporter extends AbstractRdbTableImporter {

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
        processMaxKeySize(tableEditor, 767, 191);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // BLOB/TEXT has index
        processIdxWithText(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void processIdxWithText(TableEditor tableEditor) {
        List<String> indexNames = tableEditor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            String idxName = indexEditor.getSource().getName();
            EIndexType idxType = indexEditor.getSource().getType();
            List<String> columnList = indexEditor.getSource().getColumnList();

            boolean hasLob = false;
            for (String column : columnList) {
                ColumnEditor editorColumn = tableEditor.getColumn(column);
                PolarDbXTypes type = PolarDbXTypes.valueOfCode(editorColumn.getSource().getDbType());
                hasLob = hasLob | isLob(type);
            }

            if (hasLob) {
                if (idxType == EIndexType.Unique) {
                    indexEditor.delete();
                    tableEditor.addIndexEditor(idxName, EIndexType.Normal, columnList);
                }
            }
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn domain = columnEditor.getSource();
        PolarDbXTypes sqlTypes = PolarDbXTypes.valueOfCode(domain.getDbType());

        // CHAR / VARCHAR default has value but length out of size
        processDefaultValueLengthOutOfColumnLength(sqlTypes, columnEditor);

        // current_date and current_time process
        processTimesFuncHaveDefaultOrUpdate(sqlTypes, columnEditor);
    }

    private void processDefaultValueLengthOutOfColumnLength(PolarDbXTypes sqlTypes, ColumnEditor columnEditor) {
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

    // only TIMESTAMP/DATE/TIME/DATETIME with have `default xxx` or `on update xxxx` and this is defaultValueIsFunc
    private void processTimesFuncHaveDefaultOrUpdate(PolarDbXTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        boolean defaultValueIsFunc = domain.isDefaultValueIsFunc();
        String updateTypeValue = CURRENT_UPDATE_TYPE.getValue(domain.getAttribute());
        boolean isDate = sqlTypes == PolarDbXTypes.TIMESTAMP || sqlTypes == PolarDbXTypes.DATE || sqlTypes == PolarDbXTypes.TIME || sqlTypes == PolarDbXTypes.DATETIME;

        if (!defaultValueIsFunc || !isDate || StringUtils.isBlank(defaultValue) && StringUtils.isBlank(updateTypeValue)) {
            return;
        }

        switch (sqlTypes) {
            case TIME:
            case DATE: {
                columnEditor.changeType(PolarDbXTypes.TIMESTAMP.getCodeKey());

                if (StringUtils.isNotBlank(defaultValue)) {
                    if (StringUtils.equalsIgnoreCase(defaultValue, "curdate()") || StringUtils.equalsIgnoreCase(defaultValue, "curtime()")) {
                        columnEditor.setDefault("current_timestamp", true);
                    }
                }

                if (StringUtils.isNotBlank(updateTypeValue)) {
                    CURRENT_UPDATE_TYPE.setValue(domain.getAttribute(), PolarDbXOnCurrentUpdateType.CurrentTimestamp.getTypeName());
                }
            }
            default:
                break;
        }
    }

    // number default has func ignore
    private void processNumberDefaultHasFuncIgnore(PolarDbXTypes sqlTypes, ColumnEditor columnEditor) {
        EColumn domain = columnEditor.getSource();
        String defaultValue = domain.getDefaultValue();
        boolean defaultValueIsFunc = domain.isDefaultValueIsFunc();

        if (defaultValueIsFunc && StringUtils.isBlank(defaultValue)) {
            switch (sqlTypes) {
                case BIT:
                case TINYINT:
                case SMALLINT:
                case MEDIUMINT:
                case INT:
                case BIGINT:
                case FLOAT:
                case DOUBLE:
                case DECIMAL: {
                    columnEditor.setDefault(null, false);
                    break;
                }
            }
        }
    }

    private void processTimestampStrategy(TableEditor tableEditor, TriggerContext triggerContext) {
        // find all timestamp column
        List<String> timestampColumns = tableEditor.getSource().getColumnList().stream().filter(eColumn -> {
            PolarDbXTypes sqlTypes = PolarDbXTypes.valueOfCode(eColumn.getDbType());
            return sqlTypes == PolarDbXTypes.TIMESTAMP;
        }).map(EColumn::getName).collect(Collectors.toList());

        TimeDefaultStrategyEnum strategy = TimeDefaultStrategyEnum.valueOfCode(triggerContext.getAttribute(MYSQL_ZERO_TIME_STRATEGY));
        for (int i = 1; i < timestampColumns.size(); i++) {
            String columnName = timestampColumns.get(i);
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            String defaultValue = columnEditor.getSource().getDefaultValue();
            if (StringUtils.isBlank(defaultValue) || StringUtils.startsWith(defaultValue, "0000-00-00 00:00:00")) {
                if (strategy == TimeDefaultStrategyEnum.ZERO) {
                    columnEditor.setNullable(false);
                    columnEditor.setDateTimePrecision(0);
                    columnEditor.setDefault("0000-00-00 00:00:00", false);
                } else if (strategy == TimeDefaultStrategyEnum.CURRENT) {
                    columnEditor.setNullable(false);
                    columnEditor.setDateTimePrecision(0);
                    columnEditor.setDefault("current_timestamp", true);
                    //                } else if (StringUtils.equalsIgnoreCase(strategy, "UTC_ZERO")) {
                    //                    columnEditor.setNullable(false);
                    //                    columnEditor.setDateTimePrecision(0);
                    //                    columnEditor.setDefault("1970-01-01 00:00:00", false);  // TODO 暂时不支持，需要加上数据库的时区偏移后才能建表成功，例如 数据库系统时区+8，那么这里需要 1970-01-01 08:00:00
                } else if (strategy == TimeDefaultStrategyEnum.IS_NULL) {
                    columnEditor.setNullable(true);
                    columnEditor.setDateTimePrecision(0);
                    columnEditor.setDefault("", false);
                } else if (strategy == TimeDefaultStrategyEnum.NOTHING) {
                    //
                }
            }
        }
    }

    private void processMaxRowSize(TableEditor tableEditor) {
        long rowSize = evalRowSize(tableEditor, tableEditor.getColumnNames());
        if (rowSize >= 65535) {
            processColumns(tableEditor, rowSize - 65535);
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
                PolarDbXTypes types = PolarDbXTypes.valueOfCode(columnEditor.getSource().getDbType());
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

    private long evalRowSize(TableEditor tableEditor, List<String> columnNames) {
        long rowSize = 0;
        for (String columnName : columnNames) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            PolarDbXTypes types = PolarDbXTypes.valueOfCode(columnEditor.getSource().getDbType());
            rowSize += typeOfSize(types, columnEditor.getSource());
        }
        return rowSize;
    }

    private void processColumns(TableEditor tableEditor, long rowOverFlowSize) {
        Map<String, PolarDbXTypes> columnTypes = new HashMap<>();
        List<ColumnEditor> needProcess = new ArrayList<>();
        for (String columnName : tableEditor.getColumnNames()) {
            ColumnEditor columnEditor = tableEditor.getColumn(columnName);
            EColumn eColumn = columnEditor.getSource();

            PolarDbXTypes types = PolarDbXTypes.valueOfCode(eColumn.getDbType());
            if (isChars(types) || isBinary(types)) {
                needProcess.add(columnEditor);
                columnTypes.put(eColumn.getName(), types);
            }
        }
        needProcess.sort((o1, o2) -> {
            PolarDbXTypes t1 = columnTypes.get(o1.getSource().getName());
            PolarDbXTypes t2 = columnTypes.get(o2.getSource().getName());
            long typeSize1 = typeOfSize(t1, o1.getSource());
            long typeSize2 = typeOfSize(t2, o2.getSource());
            return -Long.compare(typeSize1, typeSize2);
        });

        for (ColumnEditor column : needProcess) {
            EColumn eColumn = column.getSource();

            PolarDbXTypes type = columnTypes.get(eColumn.getName());
            long typeSize = typeOfSize(type, eColumn);

            if (typeSize < 20) {
                continue;
            }

            if (isChars(type)) {
                column.changeType(PolarDbXTypes.TEXT.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }
            if (isBinary(type)) {
                column.changeType(PolarDbXTypes.BLOB.getCodeKey());
                rowOverFlowSize = rowOverFlowSize - typeSize + 20;
            }

            if (rowOverFlowSize <= 0) {
                return;
            }
        }
    }

    private boolean isLob(PolarDbXTypes type) {
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

    private boolean isChars(PolarDbXTypes type) {
        switch (type) {
            case CHAR:
            case VARCHAR:
                return true;
            default:
                return false;
        }
    }

    private boolean isBinary(PolarDbXTypes type) {
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
    private long typeOfSize(PolarDbXTypes type, EColumn eColumn) {
        Long length = eColumn.getLength();
        if (length == null || length <= 0) {
            length = 0L;
        }

        switch (type) {
            case BIT:
                return (length + 7) / 8; // BIT(M) approximately (M+7)/8 bytes
            case TINYINT:
                return 1;
            case SMALLINT:
                return 2;
            case MEDIUMINT:
                return 3;
            case INT:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
                return 8;
            case DECIMAL:
                return 8; // max is 8 bytes.
            case DATE:
                return 3;
            case DATETIME:
                return 8; // 5 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIMESTAMP:
                return 7; // 4 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case TIME:
                return 6; // 3 bytes + fractional seconds storage (fractional is 0 ~ 3 bytes)
            case YEAR:
                return 1;
            case CHAR:
            case VARCHAR:
                return length * 4;// use utf8mb4 length TODO Character set factors should be considered
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
            case GEOMETRY:
            case POINT:
            case LINESTRING:
            case POLYGON:
            case MULTIPOINT:
            case GEOMETRY_COLLECTION:
            case GEOM_COLLECTION:
            case MULTILINESTRING:
            case MULTIPOLYGON:
                return 4;
            default:
                break;
        }
        return 0;
    }

    private boolean isAliSQL(TriggerContext triggerContext) {
        return StringUtils.equalsIgnoreCase(triggerContext.getAttribute(MYSQL_IS_ALI_SQL), "true");
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return PolarDbXTypes.valueOfCode(columnType);
    }
}
