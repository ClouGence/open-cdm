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

import static com.clougence.adapter.doris.DorisAttributeNames.DORIS_ADD_OP_COLUMN;
import static com.clougence.adapter.doris.DorisAttributeNames.DORIS_ADD_OP_COL_NAME;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.reactor.handlers.attributes.HandlersAttributeNames;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
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
public class DorisTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // drop all index
        if (!StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), triggerContext.getAttribute(HandlersAttributeNames.DR_KEEP_INDEX))) {
            processDropAllIndex(tableEditor);
        }

        // non-primary columns set nullable = true
        processChangeColToNullable(tableEditor);

        // pk to uk
        processChangePkToUniqueKey(tableEditor);

        // drop comment
        //        processDropTableComment(tableEditor);

        // add _op column
        processAddOpColumn(tableEditor, triggerContext);
    }

    private void processAddOpColumn(TableEditor tableEditor, TriggerContext triggerContext) {
        String needAddOP = triggerContext.getAttribute(DORIS_ADD_OP_COLUMN);
        String needAddOpName = triggerContext.getAttribute(DORIS_ADD_OP_COL_NAME);
        if (!StringUtils.equalsIgnoreCase("true", needAddOP) || StringUtils.isBlank(needAddOpName)) {
            return;
        }

        //        if (tableEditor.getColumnNames().contains("__op")) {
        //            return;
        //        }
        //
        //        tableEditor.addColumn("__op", DorisTypes.BOOLEAN.name());

        if (tableEditor.getColumnNames().contains(needAddOpName)) {
            return;
        }

        tableEditor.addColumn(needAddOpName, DorisTypes.BOOLEAN.name());
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    protected void processChangeColToNullable(TableEditor edit) {
        PrimaryEditor primaryEditor = edit.getPrimaryEditor();
        List<String> pks = new ArrayList<>();
        if (primaryEditor != null) {
            pks.addAll(primaryEditor.getSource().getColumnList());
        }

        for (String colName : edit.getColumnNames()) {
            ColumnEditor colEditor = edit.getColumn(colName);
            if ((colEditor.getSource().getNullable() != null && colEditor.getSource().getNullable()) || pks.contains(colName)) {
                continue;
            }

            colEditor.setNullable(true);
        }
    }

    protected void processChangePkToUniqueKey(TableEditor edit) {
        PrimaryEditor pkEditor = edit.getPrimaryEditor();
        if (pkEditor != null) {
            List<String> pks = new ArrayList<>(pkEditor.getSource().getColumnList());
            pkEditor.delete();
            edit.addIndexEditor("_cc_uk_from_pk", EIndexType.Unique, pks);
        }
    }

    protected void processDropTableComment(TableEditor edit) {
        edit.setComment("");
    }

    protected void processDropAllIndex(TableEditor edit) {
        for (String index : edit.getIndexes()) {
            TableEditor.IndexEditor indexEditor = edit.getIndexEditor(index);
            indexEditor.delete();
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        DorisTypes sqlType = DorisTypes.valueOfCode(columnEditor.getSource().getDbType());

        // CHAR / VARCHAR length extend size
        processExtendVarcharCharLength(sqlType, columnEditor);

        // has unsigned upgrade to Uxxx
        processUnsignedUpgrade(columnEditor, sqlType);
    }

    private void processUnsignedUpgrade(ColumnEditor columnEditor, DorisTypes sqlType) {
        EColumn eColumn = columnEditor.getSource();
        if (!eColumn.isNumericUnsigned()) {
            return;
        }
        switch (sqlType) {
            case TINYINT:
                columnEditor.changeType(DorisTypes.SMALLINT.getCodeKey());
                break;
            case SMALLINT:
                columnEditor.changeType(DorisTypes.INT.getCodeKey());
                break;
            case INT:
                columnEditor.changeType(DorisTypes.BIGINT.getCodeKey());
                break;
            case BIGINT:
                columnEditor.changeType(DorisTypes.LARGEINT.getCodeKey());
                break;
            case DECIMAL:
                columnEditor.changeType(DorisTypes.DECIMAL.getCodeKey());
                break;
            case FLOAT:
            case DOUBLE:
                columnEditor.changeType(DorisTypes.DOUBLE.getCodeKey());
                break;
            default:
                break;
        }
    }

    private void processExtendVarcharCharLength(DorisTypes sqlTypes, ColumnEditor columnEditor) {
        switch (sqlTypes) {
            case CHAR: {
                Long sourceLength = columnEditor.getSource().getLength();
                if (sourceLength == null || (sourceLength * 3) > 255) {
                    columnEditor.setCharLength(255L);
                } else {
                    columnEditor.setCharLength(sourceLength * 3);
                }
            }
            case VARCHAR: {
                Long sourceLength = columnEditor.getSource().getLength();
                if (sourceLength == null || sourceLength <= 0) {
                    sourceLength = 65535L;
                    columnEditor.setCharLength(sourceLength);
                } else {
                    columnEditor.setCharLength(sourceLength * 3);
                }
            }
        }
    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return DorisTypes.valueOfCode(columnType);
    }
}
