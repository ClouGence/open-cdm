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
public class EPartitionDefinition extends EChange<EPartitionDefinition> {

    private String               type;
    private String               expression;

    private List<EPartitionItem> partitionTemplate = new ArrayList<>();

    private Map<String, String>  attribute         = new HashMap<>();

    @Override
    public EPartitionDefinition clone() {
        EPartitionDefinition clone = new EPartitionDefinition();
        clone.type = type;
        clone.expression = expression;
        clone.attribute.putAll(this.attribute);
        this.partitionTemplate.forEach(o -> clone.partitionTemplate.add(o.clone()));
        return clone;
    }

    @Override
    boolean testChanged(EPartitionDefinition initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.type, initData.type)) {
            return true;
        }
        if (!Objects.equals(this.expression, initData.expression)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        if (EditorUtils.testList(this.partitionTemplate, initData.partitionTemplate)) {
            return true;
        }
        return false;
    }
}
