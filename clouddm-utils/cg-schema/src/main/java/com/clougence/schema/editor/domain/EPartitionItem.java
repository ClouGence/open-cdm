package com.clougence.schema.editor.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class EPartitionItem extends EChange<EPartitionItem> {

    private String               name;
    private String               description;
    private String               tablespace;
    private String               partitionMethod;
    private String               comment;

    private List<EPartitionItem> subPartitionItems = new ArrayList<>();
    private Map<String, String>  attribute         = new HashMap<>();

    @Override
    public EPartitionItem clone() {
        EPartitionItem clone = new EPartitionItem();
        clone.setName(name);
        clone.setDescription(description);
        clone.setTablespace(tablespace);
        clone.setPartitionMethod(partitionMethod);
        clone.getAttribute().putAll(attribute);
        this.subPartitionItems.forEach(o -> clone.getSubPartitionItems().add(o.clone()));
        return clone;
    }

    @Override
    boolean testChanged(EPartitionItem initData) {
        if (initData == null) {
            return true;
        }

        if (!Objects.equals(this.name, initData.getName())) {
            return true;
        }
        if (!Objects.equals(this.description, initData.getDescription())) {
            return true;
        }
        if (!Objects.equals(this.tablespace, initData.getTablespace())) {
            return true;
        }
        if (!Objects.equals(this.partitionMethod, initData.getPartitionMethod())) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.getAttribute())) {
            return true;
        }

        if (EditorUtils.testList(this.subPartitionItems, initData.getSubPartitionItems())) {
            return true;
        }
        return false;
    }
}
