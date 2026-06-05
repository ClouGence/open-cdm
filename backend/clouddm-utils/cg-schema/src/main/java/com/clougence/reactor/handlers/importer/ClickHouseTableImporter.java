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

import static com.clougence.adapter.clickhouse.ClickHouseAttributeNames.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clougence.adapter.clickhouse.ClickHouseTableEngine;
import com.clougence.adapter.clickhouse.ClickHouseTypes;
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

/**
 * @author mode 2021/1/8 19:56
 */
public class ClickHouseTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

        // select table engine
        processNumReplicas(tableEditor, triggerContext);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // index to sorting_key.
        processIndexes(tableEditor, triggerContext);

        // drop pk if engine is ReplacingMergeTree.
        processPrimaryKey(tableEditor, triggerContext);
    }

    private void processPrimaryKey(TableEditor tableEditor, TriggerContext triggerContext) {
        ClickHouseTableEngine tableEngine = ClickHouseTableEngine.valueOfCode(triggerContext.getAttribute(ENGINE));
        if ((tableEngine == ClickHouseTableEngine.ReplacingMergeTree || tableEngine == ClickHouseTableEngine.ReplicatedReplacingMergeTree)
            && tableEditor.getPrimaryEditor() != null) {
            tableEditor.getPrimaryEditor().delete();
        }
    }

    private void processNumReplicas(TableEditor tableEditor, TriggerContext triggerContext) {
        ClickHouseTableEngine tableEngine = ClickHouseTableEngine.valueOfCode(triggerContext.getAttribute(ENGINE));
        if (tableEngine == null) {
            ENGINE.setValue(tableEditor.getSource().getAttribute(), ClickHouseTableEngine.ReplacingMergeTree.getExpr());
        } else {
            ENGINE.setValue(tableEditor.getSource().getAttribute(), tableEngine.getExpr());
        }
    }

    private void processIndexes(TableEditor tableEditor, TriggerContext triggerContext) {
        PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        List<String> indexes = tableEditor.getIndexes();
        Set<String> needSorting = new HashSet<>();

        if (primaryEditor != null) {
            needSorting.addAll(primaryEditor.getSource().getColumnList());
        } else {
            for (String indexName : indexes) {
                IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
                if (indexEditor.getSource().getType() == EIndexType.Unique) {
                    needSorting.addAll(indexEditor.getSource().getColumnList());
                    break;
                }
            }
        }

        for (String sortingColumn : needSorting) {
            ColumnEditor column = tableEditor.getColumn(sortingColumn);
            if (column != null) {
                SORTING_KEY.setValue(column.getSource().getAttribute(), "true");
            }
        }

        String engineFull = tableEditor.getSource().getAttribute().get(ENGINE_FULL.getCodeKey());

        List<String> pks = primaryEditor == null ? new ArrayList<>() : new ArrayList<>(primaryEditor.getSource().getColumnList());
        if (StringUtils.isBlank(engineFull)) {
            for (String columnName : tableEditor.getColumnNames()) {
                ColumnEditor columnEditor = tableEditor.getColumn(columnName);
                if (columnEditor == null || (columnEditor.getSource().getNullable() != null && columnEditor.getSource().getNullable()) || pks.contains(columnName)) {
                    continue;
                }

                columnEditor.setNullable(true);
            }
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType,
                             MainVersion dstMainVersion) {

        // has unsigned upgrade to Uxxx
        processUnsignedUpgrade(columnEditor, srcType, srcMainVersion, dstType, dstMainVersion);
    }

    private void processUnsignedUpgrade(ColumnEditor column, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        EColumn eColumn = column.getSource();
        boolean unsigned = eColumn.isNumericUnsigned();
        if (!unsigned) {
            return;
        }

        ClickHouseTypes sqlType = ClickHouseTypes.valueOfCode(eColumn.getDbType());
        switch (sqlType) {
            case Int8:
                column.changeType(ClickHouseTypes.UInt8.getCodeKey());
                break;
            case Int16:
                column.changeType(ClickHouseTypes.UInt16.getCodeKey());
                break;
            case Int32:
                column.changeType(ClickHouseTypes.UInt32.getCodeKey());
                break;
            case Int64:
                column.changeType(ClickHouseTypes.UInt64.getCodeKey());
                break;
            case Int128:
                column.changeType(ClickHouseTypes.UInt128.getCodeKey());
                break;
            case Int256:
                column.changeType(ClickHouseTypes.UInt256.getCodeKey());
                break;
            default:
                break;
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return ClickHouseTypes.valueOfCode(columnType);
    }
}
