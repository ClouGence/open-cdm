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
