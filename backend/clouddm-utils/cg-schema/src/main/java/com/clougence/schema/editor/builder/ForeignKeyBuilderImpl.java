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
import java.util.HashMap;
import java.util.List;

import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.Builder;
import com.clougence.schema.editor.TableEditor.ForeignKeyBuilder;
import com.clougence.schema.editor.TableEditor.ForeignKeyEditor;
import com.clougence.schema.editor.domain.EForeignKey;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;

class ForeignKeyBuilderImpl extends AbstractBuilder implements ForeignKeyBuilder {

    private final TableEditor      tableEditor;
    private final ForeignKeyEditor foreignKeyEditor;
    private final EForeignKey      data;
    private boolean                isDrop;

    public ForeignKeyBuilderImpl(TableEditor tableEditor, ForeignKeyEditor foreignKeyEditor){
        super();
        this.tableEditor = tableEditor;
        this.foreignKeyEditor = foreignKeyEditor;
        this.data = new EForeignKey();
        this.isDrop = false;

        if (foreignKeyEditor != null) {
            this.data.setName(foreignKeyEditor.getSource().getName());
            this.data.setColumnList(new ArrayList<>(foreignKeyEditor.getSource().getColumnList()));
            this.data.setReferenceSchema(foreignKeyEditor.getSource().getReferenceSchema());
            this.data.setReferenceTable(foreignKeyEditor.getSource().getReferenceTable());
            this.data.setReferenceMapping(new HashMap<>(foreignKeyEditor.getSource().getReferenceMapping()));
            this.data.setUpdateRule(foreignKeyEditor.getSource().getUpdateRule());
            this.data.setDeleteRule(foreignKeyEditor.getSource().getDeleteRule());
        }
    }

    @Override
    public ForeignKeyBuilder rename(String newName) {
        this.data.setName(newName);
        return this;
    }

    @Override
    public ForeignKeyBuilder addColumn(String columnName, String referenceColumn) {

        boolean contains = this.data.getColumnList().contains(columnName);
        if (contains) {
            if (referenceColumn.equals(this.data.getReferenceMapping().get(columnName))) {
                return this;
            } else {
                throw new IllegalStateException("");
            }
        }

        this.data.getColumnList().add(columnName);
        this.data.getReferenceMapping().put(columnName, referenceColumn);

        return this;
    }

    @Override
    public ForeignKeyBuilder removeColumn(List<String> columnName) {
        this.data.getColumnList().removeAll(columnName);
        return this;
    }

    @Override
    public ForeignKeyBuilder configUpdateRule(RdbForeignKeyRule foreignRule) {
        this.data.setUpdateRule(foreignRule);
        return this;
    }

    @Override
    public ForeignKeyBuilder configDeleteRule(RdbForeignKeyRule foreignRule) {
        this.data.setDeleteRule(foreignRule);
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
            if (this.foreignKeyEditor != null) {
                this.foreignKeyEditor.delete();
            }
            return;
        }

        if (this.foreignKeyEditor != null) {
            this.foreignKeyEditor.delete();
        }

        this.tableEditor.addForeignKeyEditor(this.data.getName(), this.data.getReferenceSchema(), this.data.getReferenceTable(), this.data.getUpdateRule(), this.data
            .getDeleteRule(), this.data.getReferenceMapping());
    }
}
