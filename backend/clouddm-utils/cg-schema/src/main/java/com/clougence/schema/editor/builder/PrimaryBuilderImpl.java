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
