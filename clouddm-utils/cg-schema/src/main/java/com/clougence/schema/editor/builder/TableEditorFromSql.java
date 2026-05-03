package com.clougence.schema.editor.builder;

import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.domain.ETable;

/**
 * @author mode 2021/5/21 19:56
 */
public class TableEditorFromSql extends TableEditorImpl {

    public TableEditorFromSql(ETable eTable, EditorContext context){
        super(eTable, context);
    }

    @Override
    public TableBuilder createBuilder() {
        return new TableBuilderFromSql(this);
    }
}
