package com.clougence.schema.editor.builder;

import com.clougence.schema.editor.TableEditor;

class TableBuilderFromSql extends TableBuilderImpl {

    public TableBuilderFromSql(TableEditor tableEditor){
        super(tableEditor);
    }

    @Override
    protected void doChanges() {
        if (this.isDrop) {
            this.tableEditor.buildDrop();
            return;
        }

        for (AbstractBuilder change : this.changes) {
            if (change.isFinish()) {
                change.doChanges();
            }
        }

        this.tableEditor.changeSchema(this.data.getSchema());
        this.tableEditor.rename(this.data.getName());
        this.tableEditor.setComment(this.data.getComment());
    }
}
