package com.clougence.reactor.handlers.exporter;

import java.util.List;

import com.clougence.schema.DsType;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.triggers.TriggerContext;
import com.clougence.schema.handlers.ColumnBeforeMappingHandler;
import com.clougence.schema.handlers.TableBeforeMappingHandler;
import com.clougence.schema.metadata.MainVersion;

public class DorisTableExporter implements TableBeforeMappingHandler, ColumnBeforeMappingHandler {

    @Override
    public void beforeMapping(TableEditor tableEditor, ColumnEditor columnEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType,
                              MainVersion dstMainVersion) {

        processUkToPk(tableEditor);

        // set nullable = true for non-primary columns  
        processChangePkColToNotNull(tableEditor);

        processDropAllUk(tableEditor);
    }

    protected void processUkToPk(TableEditor editor) {
        List<String> indexNames = editor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = editor.getIndexEditor(indexName);
            if (indexEditor == null) {
                break;
            }
            if (editor.getPrimaryEditor() != null) {
                break;
            }
            editor.createPrimaryEditor(indexEditor.getSource().getColumnList());
            // Doris can only have one index for every table
            break;
        }

    }

    protected void processChangePkColToNotNull(TableEditor editor) {
        PrimaryEditor primaryEditor = editor.getPrimaryEditor();
        if (primaryEditor == null) {
            return;
        }
        List<String> pkColList = primaryEditor.getSource().getColumnList();
        for (String col : pkColList) {
            ColumnEditor column = editor.getColumn(col);
            if (column != null) {
                column.setNullable(false);
            }
        }
    }

    protected void processDropAllUk(TableEditor editor) {
        List<String> indexNames = editor.getIndexes();
        for (String indexName : indexNames) {
            IndexEditor indexEditor = editor.getIndexEditor(indexName);
            if (indexEditor != null) {
                indexEditor.delete();
            }
        }
    }

    @Override
    public void beforeMapping(TableEditor tableEditor, TriggerContext triggerContext, DsType srcType, MainVersion srcMainVersion, DsType dstType, MainVersion dstMainVersion) {
    }
}
