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

import com.clougence.clouddm.dsfamily.language.completion.analyzer.CompletionAnalyzer;
import com.clougence.clouddm.dsfamily.language.completion.rdb.AfterFromTableCompletionStrategy;
import com.clougence.clouddm.dsfamily.language.completion.rdb.ExpressionCompletionStrategy;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public abstract class CompletionStrategyCenter {

    private volatile List<CompletionStrategy> strategies;

    public List<CompletionItem> complete(CompletionRequest request, MetaService metaService) {
        if (request == null || request.getDataSourceId() == null || metaService == null) {
            return Collections.emptyList();
        }

        try {
            CompletionDialect dialect = Objects.requireNonNull(dialect(request), "dialect");
            CompletionContext context = context(request, dialect);
            return strategies().stream()
                .filter(strategy -> strategy.match(context))
                .max(Comparator.comparingInt(CompletionStrategy::weight))
                .map(strategy -> strategy.complete(context, metaService))
                .map(CompletionStrategyCenter::normalizeItems)
                .orElseGet(Collections::emptyList);
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    private static List<CompletionItem> normalizeItems(List<CompletionItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, CompletionItem> uniqueItems = new LinkedHashMap<>();
        for (CompletionItem item : items) {
            if (item == null) {
                continue;
            }
            uniqueItems.putIfAbsent(itemKey(item), item);
        }
        return uniqueItems.values()
            .stream()
            .sorted(Comparator.comparing(CompletionStrategyCenter::itemWeight, Comparator.reverseOrder())
                .thenComparing(CompletionStrategyCenter::itemLabel, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(CompletionStrategyCenter::itemInsertText, String.CASE_INSENSITIVE_ORDER))
            .toList();
    }

    private static String itemKey(CompletionItem item) {
        return String.join("\u0001", String.valueOf(item.getKind()), itemLabel(item).toLowerCase(Locale.ROOT), itemInsertText(item).toLowerCase(Locale.ROOT));
    }

    private static int itemWeight(CompletionItem item) {
        return item.getWeight() == null ? 0 : item.getWeight();
    }

    private static String itemLabel(CompletionItem item) {
        return item.getLabel() == null ? "" : item.getLabel();
    }

    private static String itemInsertText(CompletionItem item) {
        return item.getInsertText() == null ? "" : item.getInsertText();
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
                registeredStrategies.add(new AfterFromTableCompletionStrategy());
                registeredStrategies.add(new ExpressionCompletionStrategy());
                registeredStrategies.removeIf(Objects::isNull);
                this.strategies = List.copyOf(registeredStrategies);
            }
            return this.strategies;
        }
    }

    protected abstract void register(List<CompletionStrategy> strategies);

    protected abstract CompletionDialect dialect(CompletionRequest request);

    protected CompletionContext context(CompletionRequest request, CompletionDialect dialect) {
        return CompletionAnalyzer.INSTANCE.analyze(request, dialect);
    }
}
