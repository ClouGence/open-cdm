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

import java.util.*;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.analyzer.CompletionTableRef;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

abstract class AbstractColumnCompletionStrategy implements CompletionStrategy {

    protected static final int COLUMN_WEIGHT         = 900;
    protected static final int PRIMARY_COLUMN_WEIGHT = 950;

    protected List<CompletionItem> columnItems(CompletionContext context, MetaService metaService) {
        Map<String, String> aliasToTable = parseTableRefs(context);
        if (aliasToTable.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompletionItem> items = new ArrayList<>();
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
                item.setIcon(StringUtils.defaultIfBlank(column.getIcon(), "COLUMN-DEFAULT"));
                item.setInsertText(column.getColumn());
                item.setWeight(columnWeight(column));
                items.add(item);
            }
        }
        return items;
    }

    protected int columnWeight(MetaCol column) {
        return isPrimaryColumn(column) ? PRIMARY_COLUMN_WEIGHT : COLUMN_WEIGHT;
    }

    protected boolean isPrimaryColumn(MetaCol column) {
        return column != null && "COLUMN-PK".equalsIgnoreCase(StringUtils.toString(column.getIcon()));
    }

    private static List<String> targetTables(CompletionContext context, Map<String, String> aliasToTable) {
        if (context.hasQualifier()) {
            String table = aliasToTable.get(context.getQualifier().toLowerCase(Locale.ROOT));
            return StringUtils.isBlank(table) ? List.of() : List.of(table);
        }

        return aliasToTable.values().stream().distinct().toList();
    }

    private static Map<String, String> parseTableRefs(CompletionContext context) {
        if (!context.getTableRefs().isEmpty()) {
            Map<String, String> refs = new LinkedHashMap<>();
            for (CompletionTableRef tableRef : context.getTableRefs()) {
                if (StringUtils.isBlank(tableRef.tableName())) {
                    continue;
                }
                refs.put(tableRef.tableName().toLowerCase(Locale.ROOT), tableRef.tableName());
                if (StringUtils.isNotBlank(tableRef.getAlias())) {
                    refs.put(tableRef.getAlias().toLowerCase(Locale.ROOT), tableRef.tableName());
                }
            }
            return refs;
        }

        List<String> tokens = context.tokenize(context.getSqlText());
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

    private static boolean isStopToken(String token) {
        return switch (StringUtils.toString(token).toLowerCase(Locale.ROOT)) {
            case "", "where", "join", "left", "right", "inner", "outer", "cross",//
                    "full", "on", "order", "group", "having", "limit", "union", "select" ->
                true;
            default -> false;
        };
    }
}
