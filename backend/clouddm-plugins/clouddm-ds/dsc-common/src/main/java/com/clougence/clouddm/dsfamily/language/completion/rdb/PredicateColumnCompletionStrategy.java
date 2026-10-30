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
import java.util.Locale;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.utils.StringUtils;

public class PredicateColumnCompletionStrategy extends AbstractColumnCompletionStrategy {

    @Override
    public int weight() {
        return 750;
    }

    @Override
    public boolean match(CompletionContext context) {
        if (context.isInPredicate()) {
            if (StringUtils.isNotBlank(context.getPrefix())) {
                return false;
            }
            String previous = context.previousToken();
            return "and".equalsIgnoreCase(previous) || "or".equalsIgnoreCase(previous) || "not".equalsIgnoreCase(previous) || context.getPreviousSignificantChar() == ','
                   || context.getPreviousSignificantChar() == '(' || context.getPreviousSignificantChar() == ')';
        }

        int offset = StringUtils.isBlank(context.getPrefix()) ? 0 : 1;
        String previous = context.tokenFromEnd(offset);
        if ("and".equalsIgnoreCase(previous) || "or".equalsIgnoreCase(previous) || "not".equalsIgnoreCase(previous)) {
            return true;
        }

        if (StringUtils.isNotBlank(context.getPrefix())) {
            return false;
        }
        return switch (context.getPreviousSignificantChar()) {
            case ',', '(', ')' -> {
                boolean inColumnClause = false;
                for (int i = 0; i < context.getTokensBeforeCursor().size(); i++) {
                    String token = context.tokenFromEnd(i).toLowerCase(Locale.ROOT);
                    switch (token) {
                        case "where", "having", "on", "select", "set" -> {
                            inColumnClause = true;
                            break;
                        }
                        case "by" -> {
                            String before = context.tokenFromEnd(i + 1);
                            inColumnClause = "order".equalsIgnoreCase(before) || "group".equalsIgnoreCase(before);
                            break;
                        }
                        case "from", "join", "into", "update", "table", "values" -> {
                            inColumnClause = false;
                            break;
                        }
                        default -> {
                            // continue scanning backwards
                        }
                    }
                    if (inColumnClause ||//
                        "from".equalsIgnoreCase(token) ||//
                        "join".equalsIgnoreCase(token) ||//
                        "into".equalsIgnoreCase(token) ||//
                        "update".equalsIgnoreCase(token) ||//
                        "table".equalsIgnoreCase(token) ||//
                        "values".equalsIgnoreCase(token)) {
                        break;
                    }
                }
                yield inColumnClause;
            }
            default -> false;
        };
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        return columnItems(context, metaService);
    }
}
