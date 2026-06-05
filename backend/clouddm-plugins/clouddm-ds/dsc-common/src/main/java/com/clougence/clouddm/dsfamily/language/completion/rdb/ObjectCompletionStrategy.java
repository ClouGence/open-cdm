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

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class ObjectCompletionStrategy implements CompletionStrategy {

    @Override
    public int weight() {
        return 100;
    }

    @Override
    public boolean match(CompletionContext context) {
        String previous = context.previousToken();
        return StringUtils.isBlank(previous) || switch (previous.toLowerCase()) {
            case "from", "join", "into", "update", "table" -> true;
            default -> false;
        };
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        List<CompletionItem> items = new ArrayList<>();
        List<MetaObj> metaObjs = metaService.cachedObjectNames(//
                context.getRequest().getPrimaryUserId(),//
                context.getRequest().getCurrentUserId(),//
                context.getRequest().getDataSourceId(), //
                context.getRequest().getLevels(),       //
                context.getRequest().getLevelsParam());
        for (MetaObj metaObj : metaObjs) {
            if (metaObj == null || StringUtils.isBlank(metaObj.getName()) || !context.matchPrefix(metaObj.getName())) {
                continue;
            }

            CompletionItem item = new CompletionItem();
            item.setLabel(metaObj.getName());
            item.setKind(toCompletionKind(metaObj.getType()));
            item.setUmiType(metaObj.getType());
            item.setIcon(icon(metaObj.getType()));
            item.setInsertText(insertText(metaObj));
            item.setWeight(weight(metaObj.getType()));
            items.add(item);
        }
        return items;
    }

    static CompletionItemKind toCompletionKind(UmiTypes type) {
        if (type == null) {
            return CompletionItemKind.TEXT;
        }
        return switch (type) {
            case Catalog, ExternalCatalog -> CompletionItemKind.CATALOG;
            case Schema, ExternalSchema -> CompletionItemKind.SCHEMA;
            case Table, ExternalTable, Materialized -> CompletionItemKind.TABLE;
            case View -> CompletionItemKind.VIEW;
            case Column -> CompletionItemKind.COLUMN;
            case Function -> CompletionItemKind.FUNCTION;
            case Procedure -> CompletionItemKind.PROCEDURE;
            default -> CompletionItemKind.TEXT;
        };
    }

    static String icon(UmiTypes type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case Catalog, ExternalCatalog -> "CATALOG";
            case Schema, ExternalSchema -> "SCHEMA";
            case Table -> "TABLE";
            case ExternalTable -> "EXTERNAL_TABLE";
            case Materialized -> "MATERIALIZED";
            case View -> "VIEW";
            case Column -> "COLUMN";
            case Function -> "FUNC";
            case Procedure -> "PROC";
            default -> null;
        };
    }

    private static String insertText(MetaObj metaObj) {
        if (metaObj.getType() == UmiTypes.Function || metaObj.getType() == UmiTypes.Procedure) {
            return metaObj.getName() + "()";
        }
        return metaObj.getName();
    }

    private static int weight(UmiTypes type) {
        if (type == UmiTypes.Table || type == UmiTypes.View) {
            return 800;
        } else if (type == UmiTypes.Function || type == UmiTypes.Procedure) {
            return 700;
        } else if (type == UmiTypes.Schema || type == UmiTypes.Catalog) {
            return 600;
        } else {
            return 500;
        }
    }
}
