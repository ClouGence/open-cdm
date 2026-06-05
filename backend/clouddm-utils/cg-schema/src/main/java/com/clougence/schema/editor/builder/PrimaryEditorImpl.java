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
import java.util.Map;
import java.util.stream.Collectors;

import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.NoColumnException;
import com.clougence.schema.editor.TableEditor.PrimaryEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.editor.domain.EPrimaryKey;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/5/21 19:56
 */
class PrimaryEditorImpl extends AbstractEditor implements PrimaryEditor {

    private final ETable      eTable;
    private final EPrimaryKey primaryKey;

    public PrimaryEditorImpl(EditorContext context, ETable eTable, EPrimaryKey primaryKey){
        super(context);
        this.eTable = eTable;
        this.primaryKey = primaryKey;
    }

    @Override
    public EPrimaryKey getSource() { return this.primaryKey; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }

    @Override
    public void addColumn(String[] columnName) {
        List<String> needAdd = new ArrayList<>();
        for (String column : columnName) {
            if (!this.primaryKey.getColumnList().contains(column)) {
                needAdd.add(column);
            }
        }

        if (needAdd.isEmpty()) {
            return;
        }

        List<String> tableAllColumns = this.eTable.getColumnList().stream().map(EColumn::getName).collect(Collectors.toList());
        for (String column : needAdd) {
            if (!tableAllColumns.contains(column)) {
                throw new NoColumnException("column '" + column + "' not found.");
            }
        }

        this.primaryKey.getColumnList().addAll(needAdd);
    }

    @Override
    public void removeColumn(String[] columnName) {
        List<String> needRemove = new ArrayList<>();
        for (String column : columnName) {
            if (this.primaryKey.getColumnList().contains(column)) {
                needRemove.add(column);
            }
        }

        if (needRemove.isEmpty()) {
            return;
        }

        this.primaryKey.getColumnList().removeAll(needRemove);
        if (this.primaryKey.getColumnList().isEmpty()) {
            this.delete();
        }
    }

    @Override
    public void delete() {
        this.eTable.setPrimaryKey(null);

    }

    @Override
    public void rename(String newName) {
        if (StringUtils.equals(this.primaryKey.getPrimaryKeyName(), newName)) {
            return;
        }

        this.primaryKey.setPrimaryKeyName(newName);
    }
}
