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
package com.clougence.clouddm.dsfamily.language.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.clougence.clouddm.sdk.language.validate.Diagnostic;
import com.clougence.clouddm.sdk.language.validate.ValidateRequest;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslProvider;

public abstract class ValidateStrategyCenter {

    private volatile List<ValidateStrategy> strategies;

    public List<Diagnostic> validate(ValidateRequest request, MetaService metaService) {
        if (request == null) {
            return List.of();
        }

        DslProvider dslProvider = Objects.requireNonNull(dslProvider(request), "dslProvider");
        ValidateContext context = ValidateContext.resolve(request, dslProvider);
        List<Diagnostic> diagnostics = new ArrayList<>();
        for (ValidateStrategy strategy : strategies()) {
            if (!strategy.match(context)) {
                continue;
            }

            List<Diagnostic> strategyDiagnostics = strategy.validate(context, metaService);
            if (strategyDiagnostics == null || strategyDiagnostics.isEmpty()) {
                continue;
            }

            diagnostics.addAll(strategyDiagnostics);
            if (strategy.stopOnDiagnostics()) {
                break;
            }
        }
        return diagnostics;
    }

    private List<ValidateStrategy> strategies() {
        List<ValidateStrategy> localStrategies = this.strategies;
        if (localStrategies != null) {
            return localStrategies;
        }

        synchronized (this) {
            if (this.strategies == null) {
                List<ValidateStrategy> registeredStrategies = new ArrayList<>();
                register(registeredStrategies);
                registeredStrategies.removeIf(Objects::isNull);
                this.strategies = List.copyOf(registeredStrategies);
            }
            return this.strategies;
        }
    }

    protected abstract void register(List<ValidateStrategy> strategies);

    protected abstract DslProvider dslProvider(ValidateRequest request);
}
