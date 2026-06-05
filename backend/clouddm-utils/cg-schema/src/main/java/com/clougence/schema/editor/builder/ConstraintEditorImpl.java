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

import com.clougence.schema.editor.ConflictException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.TableEditor.ConstraintEditor;
import com.clougence.schema.editor.domain.EConstraint;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.utils.StringUtils;

import java.util.Map;

public class ConstraintEditorImpl extends AbstractEditor implements ConstraintEditor {

    private final ETable      eTable;
    private final EConstraint eConstraint;

    ConstraintEditorImpl(EditorContext context, ETable eTable, EConstraint eConstraint){
        super(context);
        this.eTable = eTable;
        this.eConstraint = eConstraint;
    }

    @Override
    public void rename(String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new NullPointerException("new name is null.");
        }
        String oldName = this.eConstraint.getName();
        if (StringUtils.equals(oldName, newName)) {
            return;
        }

        this.eConstraint.setName(newName);
    }

    @Override
    public EConstraint getSource() { return this.eConstraint; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }
}
