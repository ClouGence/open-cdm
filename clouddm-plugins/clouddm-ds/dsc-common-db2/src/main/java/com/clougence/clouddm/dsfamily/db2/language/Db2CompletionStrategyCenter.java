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
package com.clougence.clouddm.dsfamily.db2.language;

import java.util.List;

import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionDialect;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategyCenter;
import com.clougence.clouddm.dsfamily.language.completion.rdb.*;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;

public class Db2CompletionStrategyCenter extends CompletionStrategyCenter {

    @Override
    protected CompletionDialect dialect(CompletionRequest request) {
        return Db2Dialect.INSTANCE;
    }

    @Override
    protected CompletionContext context(CompletionRequest request, CompletionDialect dialect) {
        return new CompletionContext(request, dialect);
    }

    @Override
    protected void register(List<CompletionStrategy> strategies) {
        strategies.add(new QualifiedColumnCompletionStrategy());
        strategies.add(new SelectAllCompletionStrategy());
        strategies.add(new SelectFromKeywordCompletionStrategy());
        strategies.add(new SelectColumnCompletionStrategy());
        strategies.add(new WhereColumnCompletionStrategy());
        strategies.add(new PredicateColumnCompletionStrategy());
        strategies.add(new OrderGroupByColumnCompletionStrategy());
        strategies.add(new ObjectCompletionStrategy());
    }
}
