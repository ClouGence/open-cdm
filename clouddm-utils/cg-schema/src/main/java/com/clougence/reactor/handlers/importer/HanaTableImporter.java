package com.clougence.reactor.handlers.importer;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaTableType;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public class HanaTableImporter extends AbstractRdbTableImporter {

    @Override
    public void afterMapping(TableEditor tableEditor, TableEditor.ColumnEditor columnEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion,
                             DsType dstType, MainVersion dstMainVersion) {
        processColumnDefault(columnEditor);

        indexAntiDuplication(tableEditor);
    }

    @Override
    public void afterMapping(TableEditor tableEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
        // fill table type and check data type
        processTableType(tableEditor);

        // drop idx with expression
        processExpressionIndex(tableEditor);

        // pk/uk/index duplicate column name.
        super.processIdxDuplicateColumnName(tableEditor);

        // hana will auto build full index for text type
        processRemoveFullIdx(tableEditor);
    }

    private boolean isText(HanaTypes type) {
        switch (type) {
            case SHORTTEXT:
            case TEXT:
            case BINTEXT: {
                return true;
            }
        }
        return false;
    }

    private void processRemoveFullIdx(TableEditor tableEditor) {
        List<String> colNames = tableEditor.getColumnNames();
        List<String> indexNames = tableEditor.getIndexes();

        for (String colName : colNames) {
            String columnType = tableEditor.getColumn(colName).getSource().getDbType();
            HanaTypes type = HanaTypes.valueOfCode(columnType);

            if (!isText(type)) {
                continue;
            }

            for (String indexName : indexNames) {
                TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
                if (indexEditor == null) {
                    continue;
                }

                List<String> columnList = indexEditor.getSource().getColumnList();
                if (!columnList.contains(colName)) {
                    continue;
                }

                indexEditor.delete();
            }
        }
    }

    private void processColumnDefault(TableEditor.ColumnEditor columnEditor) {
        EColumn eColumn = columnEditor.getSource();
        HanaTypes hanaTypes = HanaTypes.valueOfCode(eColumn.getDbType());
        if (hanaTypes == HanaTypes.TEXT || hanaTypes == HanaTypes.BINARY || hanaTypes == HanaTypes.VARBINARY) {
            columnEditor.setDefault(null, false);
            columnEditor.setNullable(true);
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

    private void processTableType(TableEditor tableEditor) {
        ETable eTable = tableEditor.getSource();
        String tableType = HanaAttributeNames.TABLE_TYPE.getValue(eTable.getAttribute());
        if (StringUtils.isBlank(tableType)) {
            tableEditor.addAttr(HanaAttributeNames.TABLE_TYPE.getCodeKey(), HanaTableType.COLUMN.getTypeName());
            return;
        }
        // hana some type not support row table, e.g. st_point
        if (HanaTableType.valueOfCode(tableType) == HanaTableType.ROW) {
            List<EColumn> copyList = new LinkedList<>(eTable.getColumnList());
            for (EColumn col : copyList) {
                HanaTypes type = HanaTypes.valueOfCode(col.getDbType());
                if (HanaTypes.ONLY_SUPPORT_COLUMN_TABLE_LIST.contains(type)) {
                    tableEditor.getColumn(col.getName()).delete();
                }
            }
        }
    }

    // todo SECONDDATE/TIMESTAMP must between 0001-01-01 00:00:01 and 9999-12-31 24:00:00.
    private void processTimestampStrategy(TableEditor tableEditor, TriggerContext triggerContext) {

    }

    // todo length overflow
    private void processMaxRowSize() {

    }

    @Override
    protected FieldType parseFieldTypeByColumnType(String columnType) {
        return HanaTypes.valueOfCode(columnType);
    }
}
