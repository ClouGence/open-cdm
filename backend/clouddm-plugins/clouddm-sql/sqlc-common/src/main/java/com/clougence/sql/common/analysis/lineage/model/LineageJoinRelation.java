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

import java.util.List;

/**
 * A FROM relation tree. ON predicates intentionally do not belong to this
 * model; only NATURAL/USING output-column semantics affect result lineage.
 */
public record LineageJoinRelation(LineageRelation left, LineageRelation right, boolean natural, List<String> usingColumns) implements LineageRelation {

    public LineageJoinRelation{
        usingColumns = usingColumns == null ? List.of() : List.copyOf(usingColumns);
    }

    @Override
    public String alias() {
        return "";
    }

    @Override
    public List<String> columnAliases() {
        return List.of();
    }
}
