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
public class EPartition extends EChange<EPartition> {

    // main partition
    private String                     partitionMethod;
    private String                     partitionExpression;
    private Boolean                    manualPartition;
    private Long                       partitionCount;
    // subpartition
    private String                     subPartitionMethod;
    private String                     subPartitionExpression;
    private Boolean                    subManualPartition;
    private Long                       subPartitionCount;

    // partition definition  for oracle series，who has template
    private List<EPartitionDefinition> definitions = new ArrayList<>();

    // item
    private List<EPartitionItem>       items       = new ArrayList<>();

    private Map<String, String>        attribute   = new HashMap<>();

    @Override
    public EPartition clone() {
        EPartition ePartition = new EPartition();
        ePartition.partitionMethod = this.partitionMethod;
        ePartition.partitionExpression = this.partitionExpression;
        ePartition.subPartitionMethod = this.subPartitionMethod;
        ePartition.subPartitionExpression = this.subPartitionExpression;
        ePartition.attribute.putAll(this.attribute);
        this.definitions.forEach(o -> {
            ePartition.getDefinitions().add(o.clone());
        });
        this.items.forEach(o -> {
            ePartition.getItems().add(o.clone());
        });
        return ePartition;
    }

    @Override
    boolean testChanged(EPartition initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.partitionMethod, initData.partitionMethod)) {
            return true;
        }
        if (!Objects.equals(this.partitionExpression, initData.partitionExpression)) {
            return true;
        }
        if (!Objects.equals(this.subPartitionMethod, initData.subPartitionMethod)) {
            return true;
        }
        if (!Objects.equals(this.subPartitionExpression, initData.subPartitionExpression)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
