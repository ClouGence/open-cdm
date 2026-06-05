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
