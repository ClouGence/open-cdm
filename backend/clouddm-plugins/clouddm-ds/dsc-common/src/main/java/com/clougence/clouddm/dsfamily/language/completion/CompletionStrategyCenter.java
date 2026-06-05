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
package com.clougence.clouddm.dsfamily.language.completion;

import java.util.*;

import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public abstract class CompletionStrategyCenter {

    private volatile List<CompletionStrategy> strategies;

    public List<CompletionItem> complete(CompletionRequest request, MetaService metaService) {
        if (request == null || request.getDataSourceId() == null || metaService == null) {
            return Collections.emptyList();
        }

        CompletionDialect dialect = Objects.requireNonNull(dialect(request), "dialect");
        CompletionContext context = context(request, dialect);
        return strategies().stream()
            .filter(strategy -> strategy.match(context))
            .max(Comparator.comparingInt(CompletionStrategy::weight))
            .map(strategy -> strategy.complete(context, metaService))
            .orElseGet(Collections::emptyList);
    }

    private List<CompletionStrategy> strategies() {
        List<CompletionStrategy> localStrategies = this.strategies;
        if (localStrategies != null) {
            return localStrategies;
        }

        synchronized (this) {
            if (this.strategies == null) {
                List<CompletionStrategy> registeredStrategies = new ArrayList<>();
                register(registeredStrategies);
                registeredStrategies.removeIf(Objects::isNull);
                this.strategies = List.copyOf(registeredStrategies);
            }
            return this.strategies;
        }
    }

    protected abstract void register(List<CompletionStrategy> strategies);

    protected abstract CompletionDialect dialect(CompletionRequest request);

    protected abstract CompletionContext context(CompletionRequest request, CompletionDialect dialect);
}
