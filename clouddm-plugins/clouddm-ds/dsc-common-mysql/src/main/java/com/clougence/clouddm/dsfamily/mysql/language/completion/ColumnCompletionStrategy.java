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

import java.util.*;

import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class ColumnCompletionStrategy implements MyCompletionStrategy {

    private static final int COLUMN_WEIGHT     = 900;
    private static final int SELECT_ALL_WEIGHT = 1000;

    @Override
    public boolean support(MyCompletionContext context) {
        if (context.hasQualifier()) {
            return true;
        }

        int offset = StringUtils.isBlank(context.getPrefix()) ? 0 : 1;
        String previous = context.tokenFromEnd(offset).toLowerCase(Locale.ROOT);
        String beforePrevious = context.tokenFromEnd(offset + 1).toLowerCase(Locale.ROOT);
        return switch (previous) {
            case "select", "where", "having", "on", "by", "set", "and", "or", "not" -> true;
            default -> (("order".equals(beforePrevious) || "group".equals(beforePrevious)) && "by".equals(previous)) || isPredicateBoundary(context);
        };
    }

    @Override
    public List<CompletionItem> complete(MyCompletionContext context, MetaService metaService) {
        List<CompletionItem> items = new ArrayList<>();
        if (isSelectAllCandidate(context)) {
            CompletionItem item = new CompletionItem();
            item.setLabel("*");
            item.setKind(CompletionItemKind.KEYWORD);
            item.setInsertText("*");
            item.setWeight(SELECT_ALL_WEIGHT);
            items.add(item);
        }

        Map<String, String> aliasToTable = parseTableRefs(context.getSqlText());
        if (aliasToTable.isEmpty()) {
            return items;
        }

        List<String> tables = targetTables(context, aliasToTable);
        boolean showTableName = tables.size() > 1;
        for (String table : tables) {
            for (MetaCol column : metaService
                .fetchTableColumns(context.getRequest().getCurrentUserId(), context.getRequest().getDataSourceId(), context.getRequest().getLevelsParam(), table)) {
                if (column == null || StringUtils.isBlank(column.getColumn()) || !context.matchPrefix(column.getColumn())) {
                    continue;
                }

                CompletionItem item = new CompletionItem();
                item.setLabel(showTableName ? column.getColumn() + " (" + table + ")" : column.getColumn());
                item.setKind(CompletionItemKind.COLUMN);
                item.setUmiType(UmiTypes.Column);
                item.setIcon("COLUMN");
                item.setInsertText(column.getColumn());
                item.setWeight(COLUMN_WEIGHT);
                items.add(item);
            }
        }
        return items;
    }

    private static List<String> targetTables(MyCompletionContext context, Map<String, String> aliasToTable) {
        if (context.hasQualifier()) {
            String table = aliasToTable.get(context.getQualifier().toLowerCase(Locale.ROOT));
            return StringUtils.isBlank(table) ? List.of() : List.of(table);
        }

        return aliasToTable.values().stream().distinct().toList();
    }

    private static Map<String, String> parseTableRefs(String sqlText) {
        List<String> tokens = MyCompletionContext.tokenize(sqlText);
        Map<String, String> refs = new LinkedHashMap<>();
        for (int i = 0; i < tokens.size() - 1; i++) {
            String token = tokens.get(i).toLowerCase(Locale.ROOT);
            if (!"from".equals(token) && !"join".equals(token)) {
                continue;
            }

            String table = tokens.get(++i);
            if (isStopToken(table)) {
                continue;
            }

            refs.put(table.toLowerCase(Locale.ROOT), table);
            if (i + 1 < tokens.size() && "as".equalsIgnoreCase(tokens.get(i + 1))) {
                i++;
            }
            if (i + 1 < tokens.size() && !isStopToken(tokens.get(i + 1))) {
                String alias = tokens.get(i + 1);
                refs.put(alias.toLowerCase(Locale.ROOT), table);
                i++;
            }
        }
        return refs;
    }

    private static boolean isSelectAllCandidate(MyCompletionContext context) {
        if (StringUtils.isNotBlank(context.getPrefix()) || context.hasQualifier()) {
            return false;
        }
        return "select".equalsIgnoreCase(context.previousToken());
    }

    private static boolean isStopToken(String token) {
        return switch (StringUtils.toString(token).toLowerCase(Locale.ROOT)) {
            case "", "where", "join", "left", "right", "inner", "outer", "cross", "full", "on", "order", "group", "having", "limit", "union", "select" -> true;
            default -> false;
        };
    }

    private static boolean isPredicateBoundary(MyCompletionContext context) {
        return switch (context.getPreviousSignificantChar()) {
            case ',', '(', ')' -> inColumnClause(context);
            default -> false;
        };
    }

    private static boolean inColumnClause(MyCompletionContext context) {
        for (int i = 0; i < context.getTokensBeforeCursor().size(); i++) {
            String token = context.tokenFromEnd(i).toLowerCase(Locale.ROOT);
            switch (token) {
                case "where", "having", "on", "select", "set" -> {
                    return true;
                }
                case "by" -> {
                    String before = context.tokenFromEnd(i + 1).toLowerCase(Locale.ROOT);
                    return "order".equals(before) || "group".equals(before);
                }
                case "from", "join", "into", "update", "table", "values" -> {
                    return false;
                }
                default -> {
                    // continue scanning backwards
                }
            }
        }
        return false;
    }
}
