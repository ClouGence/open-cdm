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
import com.clougence.schema.editor.TableEditor.PartitionEditor;
import com.clougence.schema.editor.domain.EPartition;
import com.clougence.schema.editor.domain.EPartitionDefinition;
import com.clougence.schema.editor.domain.EPartitionItem;
import com.clougence.schema.editor.domain.ETable;

import java.util.Map;

public class PartitionEditorImpl extends AbstractEditor implements PartitionEditor {

    private final ETable     eTable;
    private final EPartition ePartition;

    PartitionEditorImpl(EditorContext context, ETable eTable, EPartition ePartition){
        super(context);
        this.eTable = eTable;
        this.ePartition = ePartition;
    }

    @Override
    public TableEditor.PartitionDefinitionEditor addDefinition(String type, String expression) {
        EPartitionDefinition definition = new EPartitionDefinition();
        definition.setType(type);
        definition.setExpression(expression);
        this.ePartition.getDefinitions().add(definition);
        return new PartitionDefinitionEditorImpl(context, ePartition, definition);
    }

    @Override
    public TableEditor.PartitionItemEditor addItem(String name, String description, String partitionMethod) {
        EPartitionItem ePartitionItem = new EPartitionItem();
        ePartitionItem.setDescription(description);
        ePartitionItem.setName(name);
        ePartitionItem.setPartitionMethod(partitionMethod);
        this.ePartition.getItems().add(ePartitionItem);
        return new PartitionItemEditorImpl(context, ePartitionItem);
    }

    @Override
    public EPartition getSource() { return this.ePartition; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }
}
