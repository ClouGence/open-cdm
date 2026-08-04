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
package com.clougence.sql.common.analysis.lineage.model;

import java.util.ArrayList;
import java.util.List;

public record LineageQueryBlock(List<LineageCte> ctes, List<LineageSelectItem> selectItems,
                                List<LineageRelation> relations) {

    public LineageQueryBlock{
        ctes = ctes == null ? List.of() : List.copyOf(ctes);
        selectItems = selectItems == null ? List.of() : List.copyOf(selectItems);
        relations = relations == null ? List.of() : List.copyOf(relations);
    }

    public LineageQueryBlock(List<LineageSelectItem> selectItems, List<LineageRelation> relations){
        this(List.of(), selectItems, relations);
    }

    public LineageQueryBlock withCtes(List<LineageCte> localCtes) {
        if (localCtes == null || localCtes.isEmpty()) {
            return this;
        }
        List<LineageCte> allCtes = new ArrayList<>(localCtes);
        allCtes.addAll(ctes);
        return new LineageQueryBlock(allCtes, selectItems, relations);
    }
}
