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
