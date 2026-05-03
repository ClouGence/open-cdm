package com.clougence.reactor.handlers.importer;

import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.INDEX_CONTAINS_EXPRESSION;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.clougence.reactor.handlers.constant.ImporterConstant;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EIndexType;
import com.clougence.schema.handlers.ColumnAfterMappingHandler;
import com.clougence.schema.handlers.TableAfterMappingHandler;
import com.clougence.schema.metadata.FieldType;

public abstract class AbstractRdbTableImporter implements TableAfterMappingHandler, ColumnAfterMappingHandler {

    // default process operation to delete index with expression
    protected void processExpressionIndex(TableEditor tableEditor) {
        for (String index : tableEditor.getIndexes()) {
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(index);
            if (Boolean.parseBoolean(indexEditor.getAttr(INDEX_CONTAINS_EXPRESSION.getCodeKey()))) {
                indexEditor.delete();
            }
        }
    }

    protected void processDecimalType(TableEditor.ColumnEditor columnEditor, FieldType fieldType) {
        if (fieldType.isAccurateDecimal()) {
            if (columnEditor.getSource().getNumericPrecision() == null) {
                columnEditor.getSource().setNumericPrecision(ImporterConstant.DEFAULT_DECIMAL_NUMERIC_PRECISION);
            }
            if (columnEditor.getSource().getNumericScale() == null) {
                columnEditor.getSource().setNumericScale(0);
            }
        }
    }

    protected void processIdxDuplicateColumnName(TableEditor tableEditor) {
        TableEditor.PrimaryEditor primaryEditor = tableEditor.getPrimaryEditor();
        List<String> indexNames = tableEditor.getIndexes();

        if (primaryEditor != null) {
            String pkName = primaryEditor.getSource().getPrimaryKeyName();
            List<String> columnList = primaryEditor.getSource().getColumnList();
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(columnList);

            if (hashSet.size() != columnList.size()) {
                primaryEditor.delete();
                tableEditor.createPrimaryEditor(pkName, new ArrayList<>(hashSet));
            }
        }

        for (String indexName : indexNames) {
            TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
            String idxName = indexEditor.getSource().getName();
            EIndexType idxType = indexEditor.getSource().getType();
            List<String> columnList = indexEditor.getSource().getColumnList();
            LinkedHashSet<String> hashSet = new LinkedHashSet<>(columnList);

            if (hashSet.size() != columnList.size()) {
                indexEditor.delete();
                tableEditor.addIndexEditor(idxName, idxType, new ArrayList<>(hashSet));
            }
        }
    }

    protected abstract FieldType parseFieldTypeByColumnType(String columnType);

    protected void processIdxWithGemo(TableEditor tableEditor) {
        List<String> colNames = tableEditor.getColumnNames();
        List<String> indexNames = tableEditor.getIndexes();

        for (String colName : colNames) {
            String columnType = tableEditor.getColumn(colName).getSource().getDbType();
            FieldType type = parseFieldTypeByColumnType(columnType);
            if (!type.isGeometry()) {
                continue;
            }

            for (String indexName : indexNames) {
                TableEditor.IndexEditor indexEditor = tableEditor.getIndexEditor(indexName);
                List<String> columnList = indexEditor.getSource().getColumnList();
                if (!columnList.contains(colName)) {
                    continue;
                }

                String idxName = indexEditor.getSource().getName();
                EIndexType indexType = indexEditor.getSource().getType();
                List<String> idxCols = new ArrayList<>(columnList);
                idxCols.remove(colName);

                indexEditor.delete();
                if (!idxCols.isEmpty()) {
                    tableEditor.addIndexEditor(idxName, indexType, idxCols);
                }
            }
        }
    }
}
