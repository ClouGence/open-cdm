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
package com.clougence.clouddm.dsfamily.language.completion.rdb;

import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.utils.StringUtils;

public class SelectAllCompletionStrategy implements CompletionStrategy {

    private static final int SELECT_ALL_WEIGHT = 1000;

    @Override
    public int weight() {
        return 900;
    }

    @Override
    public boolean match(CompletionContext context) {
        if (!context.isInSelectList()) {
            return false;
        }
        return StringUtils.isBlank(context.getPrefix()) &&          //
               !context.hasQualifier() &&                           //
               "select".equalsIgnoreCase(context.previousToken()) &&//
               context.getPreviousSignificantChar() != '*';
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        CompletionItem item = new CompletionItem();
        item.setLabel("*");
        item.setKind(CompletionItemKind.KEYWORD);
        item.setInsertText("*");
        item.setWeight(SELECT_ALL_WEIGHT);
        return List.of(item);
    }
}
