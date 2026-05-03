package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.Builder;
import com.clougence.schema.editor.TableEditor.PrimaryBuilder;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.domain.EPrimaryKey;

class PrimaryBuilderImpl extends AbstractBuilder implements PrimaryBuilder {

    private final TableEditor   tableEditor;
    private final PrimaryEditor primaryEditor;
    private final EPrimaryKey   data;
    private boolean             isDrop;

    public PrimaryBuilderImpl(TableEditor tableEditor, TableEditor.PrimaryEditor primaryEditor){
        super();
        this.tableEditor = tableEditor;
        this.primaryEditor = primaryEditor;
        this.data = new EPrimaryKey();
        this.isDrop = false;

        if (primaryEditor != null) {
            this.data.setPrimaryKeyName(primaryEditor.getSource().getPrimaryKeyName());
            this.data.setColumnList(new ArrayList<>(primaryEditor.getSource().getColumnList()));
        }
    }

    @Override
    public PrimaryBuilder addColumn(List<String> columnName) {
        for (String col : columnName) {
            if (!this.data.getColumnList().contains(col)) {
                this.data.getColumnList().add(col);
            }
        }
        return this;
    }

    @Override
    public PrimaryBuilder removeColumn(List<String> columnName) {
        for (String col : columnName) {
            this.data.getColumnList().remove(col);
        }
        return this;
    }

    @Override
    public PrimaryBuilder rename(String newName) {
        this.data.setPrimaryKeyName(newName);
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
            if (this.primaryEditor != null) {
                this.primaryEditor.delete();
            }
            return;
        }

        if (this.primaryEditor == null) {
            this.tableEditor.createPrimaryEditor(this.data.getPrimaryKeyName(), this.data.getColumnList());
            return;
        }

        List<String> needRemove = new ArrayList<>();
        for (String column : this.primaryEditor.getSource().getColumnList()) {
            if (!this.data.getColumnList().contains(column)) {
                needRemove.add(column);
            }
        }
        if (!needRemove.isEmpty()) {
            this.primaryEditor.removeColumn(needRemove.toArray(new String[0]));
        }

        List<String> needAdd = new ArrayList<>();
        for (String column : this.data.getColumnList()) {
            if (!this.primaryEditor.getSource().getColumnList().contains(column)) {
                needAdd.add(column);
            }
        }
        if (!needAdd.isEmpty()) {
            this.primaryEditor.addColumn(needRemove.toArray(new String[0]));
        }
    }
}
