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

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.dameng.DmSqlTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnAfterMappingHandler;
import com.clougence.schema.handlers.TableAfterMappingHandler;
import com.clougence.schema.metadata.MainVersion;

/**
 * @author mode 2021/1/8 19:56
 */
public class DamengTableImporter implements TableAfterMappingHandler, ColumnAfterMappingHandler {

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // drop fk
        processForeignKey(tableEditor);

        // BLOB/TEXT has index
        processIdxWithText(tableEditor);

        indexAntiDuplication(tableEditor);
    }

    private void processForeignKey(TableEditor tableEditor) {
        for (String fkName : new ArrayList<>(tableEditor.getForeignKeys())) {
            tableEditor.getForeignKeyEditor(fkName).delete();// delete all fk
        }
    }

    private void indexAntiDuplication(TableEditor tableEditor) {
        Set<String> mastColumnSet = new HashSet<>();

        // pk
        TableEditor.PrimaryEditor primaryKey = tableEditor.getPrimaryEditor();
        if (primaryKey != null) {
            primaryKey.rename(null);
            List<String> pkColumn = primaryKey.getSource().getColumnList();

            String joinPkColumn = "'" + pkColumn.stream().map(s -> s.replace("'", "\\'")).collect(Collectors.joining("','")) + "'";

            mastColumnSet.add(joinPkColumn);
        }

        // uk
        for (String idxName : tableEditor.getIndexes()) {
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(idxName);
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
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(idxName);
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

    private void processIdxWithText(TableEditor tableEditor) {
        List<String> indexNames = tableEditor.getIndexes();
        for (String indexName : indexNames) {
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            List<String> columnList = indexEditor.getSource().getColumnList();

            boolean hasLob = false;
            for (String column : columnList) {
                ColumnEditor editorColumn = tableEditor.getColumn(column);
                DmSqlTypes type = DmSqlTypes.valueOfCode(editorColumn.getSource().getDbType());
                hasLob = hasLob | isLob(type);
            }

            if (hasLob) {
                indexEditor.delete();
            }
        }
    }

    @Override
    public void afterMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, //
                             DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {

    }

    private boolean isLob(DmSqlTypes type) {
        switch (type) {
            case CLOB:
            case TEXT:
            case IMAGE:
                return true;
            default:
                return false;
        }
    }
}
