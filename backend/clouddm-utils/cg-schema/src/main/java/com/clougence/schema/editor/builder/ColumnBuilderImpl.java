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

import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.Builder;
import com.clougence.schema.editor.TableEditor.ColumnBuilder;
import com.clougence.schema.editor.TableEditor.ColumnEditor;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.struts.UmiAttributeNames;

class ColumnBuilderImpl extends AbstractBuilder implements ColumnBuilder {

    private final TableEditor  tableEditor;
    private final ColumnEditor editorColumn;
    private final EColumn      data;
    private boolean            isDrop;

    public ColumnBuilderImpl(TableEditor tableEditor, ColumnEditor editorColumn){
        super();
        this.tableEditor = tableEditor;
        this.editorColumn = editorColumn;
        this.isDrop = false;
        if (editorColumn != null) {
            this.data = new EColumn(editorColumn.getSource().getName());
            this.data.setName(editorColumn.getSource().getName());
            this.data.setDbType(editorColumn.getSource().getDbType());
            this.data.setNullable(editorColumn.getSource().getNullable());
            this.data.setLength(editorColumn.getSource().getLength());
            this.data.setNumericPrecision(editorColumn.getSource().getNumericPrecision());
            this.data.setNumericScale(editorColumn.getSource().getNumericScale());
            this.data.setNumericUnsigned(editorColumn.getSource().isNumericUnsigned());
            this.data.setDatetimePrecision(editorColumn.getSource().getDatetimePrecision());
            this.data.setDefaultValue(editorColumn.getSource().getDefaultValue());
            this.data.setDefaultValueIsFunc(editorColumn.getSource().isDefaultValueIsFunc());
            this.data.setAutoGenerate(editorColumn.getSource().isAutoGenerate());
            this.data.setComment(editorColumn.getSource().getComment());
        } else {
            this.data = new EColumn("");
        }
    }

    @Override
    public ColumnBuilder rename(String newName) {
        this.data.setName(newName);
        return this;
    }

    @Override
    public ColumnBuilder changeType(String dbType) {
        this.data.setDbType(dbType);
        return this;
    }

    @Override
    public ColumnBuilder changeType(FieldType dbType) {
        this.data.setDbType(dbType.getCodeKey());
        return this;
    }

    @Override
    public ColumnBuilder setNullable(Boolean isNullable) {
        this.data.setNullable(isNullable);
        return this;
    }

    @Override
    public ColumnBuilder setCharLength(long length) {
        this.data.setLength(length);
        return this;
    }

    @Override
    public ColumnBuilder setPrecision(int numericPrecision) {
        this.data.setNumericPrecision(numericPrecision);
        return this;
    }

    @Override
    public ColumnBuilder setScale(int numericScale) {
        this.data.setNumericScale(numericScale);
        return this;
    }

    @Override
    public ColumnBuilder setDateTimePrecision(int datetimePrecision) {
        this.data.setDatetimePrecision(datetimePrecision);
        return this;
    }

    @Override
    public ColumnBuilder setDefault(String defaultValue, boolean defaultValueIsFunc) {
        this.data.setDefaultValue(defaultValue);
        this.data.setDefaultValueIsFunc(defaultValueIsFunc);
        return this;
    }

    @Override
    public ColumnBuilder setComment(String comment) {
        this.data.setComment(comment);
        return this;
    }

    @Override
    public ColumnBuilder setAutoGenerate(boolean autoGenerate) {
        this.data.setAutoGenerate(autoGenerate);
        return this;
    }

    @Override
    public Builder delete() {
        this.isDrop = true;
        return this;
    }

    @Override
    public ColumnBuilder setUnsigned(boolean isUnsigned) {
        this.data.setNumericUnsigned(isUnsigned);
        return this;
    }

    @Override
    public ColumnBuilder putAttribute(UmiAttributeNames name, String value) {
        this.data.getAttribute().put(name.getCodeKey(), value);
        return this;
    }

    @Override
    protected void doChanges() {
        if (this.isDrop) {
            if (this.editorColumn != null) {
                this.editorColumn.delete();
            }
            return;
        }

        if (this.editorColumn == null) {
            this.tableEditor.addColumn(this.data.getName(), this.data.getDbType(), this.data.getNullable(), this.data.getLength(), //
                    this.data.getNumericPrecision(), this.data.getNumericScale(), this.data.isNumericUnsigned(), this.data.getDatetimePrecision(), //
                    this.data.getDefaultValue(), this.data.isDefaultValueIsFunc(), //
                    this.data.isAutoGenerate(), this.data.getComment());
        } else {
            this.editorColumn.rename(this.data.getName());
            this.editorColumn.change(this.data.getDbType(), this.data.getNullable(), this.data.getLength(), //
                    this.data.getNumericPrecision(), this.data.getNumericScale(), this.data.isNumericUnsigned(), this.data.getDatetimePrecision(), //
                    this.data.getDefaultValue(), this.data.isDefaultValueIsFunc(), //
                    this.data.isAutoGenerate(), this.data.getComment());
        }
    }
}
