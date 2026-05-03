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
