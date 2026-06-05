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
package com.clougence.schema.editor.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.*;
import com.clougence.schema.editor.domain.ETable;

class TableBuilderImpl extends AbstractBuilder implements TableBuilder {

    protected final TableEditor     tableEditor;
    protected final ETable          data;
    protected boolean               isDrop;
    protected List<AbstractBuilder> changes;

    public TableBuilderImpl(TableEditor tableEditor){
        super();
        this.tableEditor = tableEditor;
        this.data = new ETable();
        this.isDrop = false;
        this.changes = new ArrayList<>();

        this.data.setSchema(tableEditor.getSource().getSchema());
        this.data.setName(tableEditor.getSource().getName());
        this.data.setComment(tableEditor.getSource().getComment());
    }

    @Override
    public void finish() {
        super.finish();

        doChanges();
    }

    @Override
    protected void doChanges() {
        if (this.isDrop) {
            this.tableEditor.buildDrop();
            return;
        }

        this.tableEditor.changeSchema(this.data.getSchema());
        this.tableEditor.rename(this.data.getName());
        this.tableEditor.setComment(this.data.getComment());

        for (AbstractBuilder change : this.changes) {
            if (change.isFinish()) {
                change.doChanges();
            }
        }
    }

    @Override
    public TableBuilder changeSchema(String newSchema) {
        this.data.setSchema(newSchema);
        return this;
    }

    @Override
    public TableBuilder rename(String newName) {
        this.data.setName(newName);
        return this;
    }

    @Override
    public TableBuilder setComment(String newComment) {
        this.data.setComment(newComment);
        return this;
    }

    @Override
    public ColumnBuilder buildColumn(String name) {

        ColumnEditor columnEditor = this.tableEditor.getColumn(name);
        ColumnBuilderImpl builder = new ColumnBuilderImpl(this.tableEditor, columnEditor);
        this.changes.add(builder);

        return builder;
    }

    @Override
    public PrimaryBuilder buildPrimaryKey() {

        PrimaryEditor primaryEditor = this.tableEditor.getPrimaryEditor();
        PrimaryBuilderImpl builder = new PrimaryBuilderImpl(this.tableEditor, primaryEditor);
        this.changes.add(builder);

        return builder;
    }

    @Override
    public IndexBuilder buildIndex(String name) {
        IndexEditor indexEditor = this.tableEditor.getIndexEditor(name);
        IndexBuilderImpl builder = new IndexBuilderImpl(this.tableEditor, indexEditor);
        this.changes.add(builder);

        return builder;
    }

    @Override
    public ForeignKeyBuilder buildForeignKey(String name) {

        ForeignKeyEditor foreignKeyEditor = this.tableEditor.getForeignKeyEditor(name);
        ForeignKeyBuilderImpl builder = new ForeignKeyBuilderImpl(this.tableEditor, foreignKeyEditor);
        this.changes.add(builder);

        return builder;
    }

    @Override
    public Builder drop() {
        this.isDrop = true;
        return this;
    }
}
