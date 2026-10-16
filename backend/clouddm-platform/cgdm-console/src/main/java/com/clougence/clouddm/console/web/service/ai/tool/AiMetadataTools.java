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
package com.clougence.clouddm.console.web.service.ai.tool;

import java.util.*;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.model.ai.AiContextReference;
import com.clougence.clouddm.console.web.model.ai.AiObject;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.ai.AiTableSchema;
import com.clougence.clouddm.console.web.service.ai.AiPlatformAccess;
import com.clougence.clouddm.console.web.service.ai.AiRunHandle;

import jakarta.annotation.Resource;

@Component
public class AiMetadataTools {
    @Resource
    private AiPlatformAccess platform;

    public Map<String, Object> listTables(AiMetadataContext context, String keyword, String cursor, AiRunHandle handle) {
        handle.checkActive();
        AiObject after = null;
        if (cursor != null) {
            AiCandidateCursor position = context.getCursors().get(cursor);
            if (position == null || !position.getKeyword().equals(keyword)) {
                throw new IllegalArgumentException("Invalid current-turn table reference or cursor. Search tables again to obtain valid references.");
            }
            after = position.getAfter();
        }
        List<AiObject> matches = platform.listObjects(context.getPuid(), context.getUid(), context.getLevels(), keyword);
        Comparator<AiObject> order = Comparator.comparing(AiObject::getName).thenComparing(o -> o.getType().name());
        List<Map<String, String>> items = new ArrayList<>();
        List<AiObject> disclosed = new ArrayList<>();
        Map<String, AiObject> added = new LinkedHashMap<>();
        boolean more = false;
        for (AiObject object : matches) {
            if (after != null && order.compare(object, after) <= 0) {
                continue;
            }
            String ref = context.getCandidates().entrySet().stream().filter(e -> e.getValue().equals(object)).map(Map.Entry::getKey).findFirst().orElse(null);
            boolean isNew = ref == null;
            if (isNew) {
                ref = UUID.randomUUID().toString();
            }
            Map<String, String> item = Map
                .of(AiSqlConstants.FIELD_OBJECT_REF, ref, AiSqlConstants.FIELD_TYPE, object.getType().name(), AiSqlConstants.FIELD_NAME, object.getName());
            if (items.size() >= 200) {
                more = true;
                break;
            }
            items.add(item);
            disclosed.add(object);
            if (isNew) {
                added.put(ref, object);
            }
        }
        handle.checkActive();
        platform.authorizeObjects(context.getPuid(), context.getUid(), context.getLevels(), disclosed);
        context.getCandidates().putAll(added);
        String next = null;
        if (more) {
            next = UUID.randomUUID().toString();
            context.getCursors().put(next, new AiCandidateCursor(keyword, disclosed.get(disclosed.size() - 1)));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_ITEMS, items);
        result.put(AiSqlConstants.FIELD_NEXT_CURSOR, next);
        return result;
    }

    public Map<String, Object> describeTables(AiMetadataContext context, List<String> refs, AiRunHandle handle) {
        List<AiObject> objects = new ArrayList<>();
        for (String ref : refs) {
            AiObject object = context.getCandidates().get(ref);
            if (object == null) {
                throw new IllegalArgumentException("Invalid current-turn table reference or cursor. Search tables again to obtain valid references.");
            }
            objects.add(object);
        }
        platform.authorizeObjects(context.getPuid(), context.getUid(), context.getLevels(), objects);
        Set<AiObject> total = new LinkedHashSet<>(context.getSchemas().keySet());
        total.addAll(objects);
        if (total.size() > context.getSchemas().size()) {
            handle.checkActive();
            // Refresh the set together so newly read targets also complete earlier source relationships.
            List<AiTableSchema> refreshed = platform.describeTables(context.getPuid(), context.getUid(), context.getLevels(), List.copyOf(total), handle);
            handle.checkActive();
            for (AiTableSchema schema : refreshed) {
                context.getSchemas().put(schema.getObject(), schema);
            }
        }
        platform.authorizeReferences(context.getPuid(), context.getUid(), context.getLevels(), references(context, List.of()));
        List<Map<String, Object>> tables = new ArrayList<>();
        for (AiTableSchema schema : context.getSchemas().values()) {
            String ref = context.getCandidates().entrySet().stream().filter(e -> e.getValue().equals(schema.getObject())).map(Map.Entry::getKey).findFirst().orElseThrow();
            Map<String, Object> item = new LinkedHashMap<>();
            item.put(AiSqlConstants.FIELD_OBJECT_REF, ref);
            item.put(AiSqlConstants.FIELD_OBJECT, schema.getObject());
            item.put(AiSqlConstants.FIELD_COMMENT, schema.getComment());
            item.put(AiSqlConstants.FIELD_COLUMNS, schema.getColumns());
            item.put(AiSqlConstants.FIELD_COLUMNS_COMPLETE, schema.isColumnsComplete());
            item.put(AiSqlConstants.FIELD_PRIMARY_KEY, schema.getPrimaryKey());
            item.put(AiSqlConstants.FIELD_FOREIGN_KEYS, schema.getForeignKeys());
            item.put(AiSqlConstants.FIELD_KEYS_KNOWN, schema.isKeysKnown());
            item.put(AiSqlConstants.FIELD_SCHEMA_READ_AT, schema.getSchemaReadAt());
            tables.add(item);
        }
        handle.checkActive();
        Map<String, Object> result = Map.of(AiSqlConstants.FIELD_TABLES, tables);
        return result;
    }

    public List<AiContextReference> references(AiMetadataContext context, List<AiContextReference> previous) {
        List<AiContextReference> current = context.getCandidates().values().stream().distinct().map(object -> {
            AiTableSchema schema = context.getSchemas().get(object);
            List<String> columns = List.of();
            if (schema != null) {
                columns = schema.getColumns().stream().map(column -> column.getName()).toList();
            }
            return new AiContextReference(object.getType(), object.getName(), columns);
        }).toList();
        Map<AiObject, Set<String>> merged = new LinkedHashMap<>();
        List<AiContextReference> all = new ArrayList<>(previous);
        all.addAll(current);
        for (AiContextReference ref : all) {
            merged.computeIfAbsent(new AiObject(ref.getType(), ref.getName()), ignored -> new LinkedHashSet<>()).addAll(ref.getColumns());
        }
        return merged.entrySet().stream().map(e -> new AiContextReference(e.getKey().getType(), e.getKey().getName(), List.copyOf(e.getValue()))).toList();
    }
}
