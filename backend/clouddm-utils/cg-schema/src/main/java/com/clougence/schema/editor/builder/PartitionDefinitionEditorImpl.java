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
import com.clougence.schema.editor.domain.EPartition;
import com.clougence.schema.editor.domain.EPartitionDefinition;
import com.clougence.schema.editor.domain.EPartitionItem;

import java.util.Map;

public class PartitionDefinitionEditorImpl extends AbstractEditor implements TableEditor.PartitionDefinitionEditor {

    private final EPartition           ePartition;
    private final EPartitionDefinition eDefinition;

    PartitionDefinitionEditorImpl(EditorContext context, EPartition ePartition, EPartitionDefinition eDefinition){
        super(context);
        this.eDefinition = eDefinition;
        this.ePartition = ePartition;
    }

    @Override
    public void addTemplate(String name, String description) {
        EPartitionItem ePartitionItem = new EPartitionItem();
        ePartitionItem.setName(name);
        ePartitionItem.setDescription(description);
        eDefinition.getPartitionTemplate().add(ePartitionItem);
    }

    @Override
    public EPartitionDefinition getSource() { return this.eDefinition; }

    @Override
    protected Map<String, String> attrMap() {
        return getSource().getAttribute();
    }
}
