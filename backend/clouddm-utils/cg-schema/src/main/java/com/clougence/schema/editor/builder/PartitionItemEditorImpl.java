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

import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.domain.EPartitionItem;

import java.util.Collections;
import java.util.Map;

public class PartitionItemEditorImpl extends AbstractEditor implements TableEditor.PartitionItemEditor {

    //    private final EPartition           ePartition;
    private final EPartitionItem eItem;

    PartitionItemEditorImpl(EditorContext context, EPartitionItem eItem){
        super(context);
        //        this.ePartition = ePartition;
        this.eItem = eItem;
    }

    @Override
    public TableEditor.PartitionItemEditor addSubItem(String name, String description) {
        EPartitionItem subItem = new EPartitionItem();
        subItem.setName(name);
        subItem.setDescription(description);
        this.eItem.getSubPartitionItems().add(subItem);
        return new PartitionItemEditorImpl(this.context, subItem);
    }

    @Override
    public EPartitionItem getSource() { return this.eItem; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }
}
