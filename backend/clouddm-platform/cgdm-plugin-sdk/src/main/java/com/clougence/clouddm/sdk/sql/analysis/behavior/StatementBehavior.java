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
package com.clougence.clouddm.sdk.sql.analysis.behavior;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Behavior analysis result for one SQL statement.
 *
 * <p>{@code statementTypes} are statement classifications. Permission consumers must interpret
 * {@code relations}; they must not infer per-resource authorization requirements directly from
 * {@code statementTypes}.</p>
 */
@Getter
@Setter
public class StatementBehavior {

    private Set<StatementType>     statementTypes = new LinkedHashSet<>();
    private List<BehaviorRelation> relations      = new ArrayList<>();

    /** Primary classification compatibility for callers which only consume one type. */
    public StatementType getStatementType() {
        return this.statementTypes.stream().findFirst().orElse(StatementType.UNKNOWN);
    }

    /** Replaces the current classifications with one primary type. */
    public void setStatementType(StatementType statementType) {
        this.statementTypes.clear();
        this.statementTypes.add(Objects.requireNonNullElse(statementType, StatementType.UNKNOWN));
    }
}
