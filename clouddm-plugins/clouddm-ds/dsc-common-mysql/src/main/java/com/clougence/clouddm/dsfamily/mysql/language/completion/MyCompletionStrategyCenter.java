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
package com.clougence.clouddm.dsfamily.mysql.language.completion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class MyCompletionStrategyCenter {

    private final List<MyCompletionStrategy> strategies = Arrays.asList(//
            new ColumnCompletionStrategy(), //
            new ObjectCompletionStrategy());

    public List<CompletionItem> complete(CompletionRequest request, MetaService metaService) {
        if (request == null || request.getDataSourceId() == null || metaService == null) {
            return Collections.emptyList();
        }

        MyCompletionContext context = new MyCompletionContext(request);
        for (MyCompletionStrategy strategy : strategies) {
            if (strategy.support(context)) {
                return strategy.complete(context, metaService);
            }
        }
        return Collections.emptyList();
    }
}
