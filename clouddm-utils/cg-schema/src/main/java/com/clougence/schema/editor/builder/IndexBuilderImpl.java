package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.Builder;
import com.clougence.schema.editor.TableEditor.IndexBuilder;
import com.clougence.schema.editor.TableEditor.IndexEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EIndex;
import com.clougence.schema.editor.domain.EIndexType;

class IndexBuilderImpl extends AbstractBuilder implements IndexBuilder {

    private final TableEditor tableEditor;
    private final IndexEditor indexEditor;
    private final EIndex      data;
    private boolean           isDrop;

    public IndexBuilderImpl(TableEditor tableEditor, IndexEditor indexEditor){
        super();
        this.tableEditor = tableEditor;
        this.indexEditor = indexEditor;
        this.isDrop = false;

        if (indexEditor != null) {
            this.data = new EIndex(indexEditor.getSource().getName());
            this.data.setName(indexEditor.getSource().getName());
            this.data.setType(indexEditor.getSource().getType());
            this.data.setColumnList(new ArrayList<>(indexEditor.getSource().getColumnList()));
        } else {
            this.data = new EIndex("");
        }
    }

    @Override
    public IndexBuilder rename(String newName) {
        this.data.setName(newName);
        return this;
    }

    @Override
    public IndexBuilder addColumn(List<String> columnName) {
        for (String col : columnName) {
            if (!this.data.getColumnList().contains(col)) {
                this.data.getColumnList().add(col);
            }
        }
        return this;
    }

    @Override
    public IndexBuilder removeColumn(List<String> columnName) {
        for (String col : columnName) {
            this.data.getColumnList().remove(col);
        }
        return this;
    }

    @Override
    public IndexBuilder configType(EIndexType indexType) {
        this.data.setType(indexType);
        return this;
    }

    @Override
    public Builder delete() {
        this.isDrop = true;
        return this;
    }

    @Override
    protected void doChanges() {
        if (this.isDrop || this.data.getColumnList().isEmpty()) {
            if (this.indexEditor != null) {
                this.indexEditor.delete();
            }
            return;
        }

        if (this.indexEditor == null) {
            this.tableEditor.addIndexEditor(this.data.getName(), this.data.getType(), this.data.getColumnList());
            return;
        }

        List<String> needRemove = new ArrayList<>();
        for (String column : this.indexEditor.getSource().getColumnList()) {
            if (!this.data.getColumnList().contains(column)) {
                needRemove.add(column);
            }
        }
        if (!needRemove.isEmpty()) {
            this.indexEditor.removeColumn(needRemove.toArray(new String[0]));
        }

        List<String> needAdd = new ArrayList<>();
        for (String column : this.data.getColumnList()) {
            if (!this.indexEditor.getSource().getColumnList().contains(column)) {
                needAdd.add(column);
            }
        }
        if (!needAdd.isEmpty()) {
            this.indexEditor.addColumn(needRemove.toArray(new String[0]));
        }
    }
}
