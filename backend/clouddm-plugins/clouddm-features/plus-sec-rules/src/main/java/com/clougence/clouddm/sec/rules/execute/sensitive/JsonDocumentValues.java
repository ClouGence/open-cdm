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
package com.clougence.clouddm.sec.rules.execute.sensitive;

import java.io.IOException;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import lombok.Getter;

/** Expands JSON leaves for the same scalar/row rules, then restores the original document shape. */
final class JsonDocumentValues {
    private static final JsonMapper        MAPPER    = JsonMapper.builder()
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
        .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
        .build();
    @Getter
    private final Map<String, ColMetaData> metadata  = new LinkedHashMap<>();
    @Getter
    private final List<String>             values    = new ArrayList<>();
    private final Map<Integer, JsonNode>   documents = new LinkedHashMap<>();
    private final List<Integer>            columns   = new ArrayList<>();
    private final List<String>             pointers  = new ArrayList<>();
    private final List<String>             original;

    JsonDocumentValues(Map<String, ColMetaData> meta, List<String> row){
        original = row;
        int column = 0;
        for (var entry : meta.entrySet()) {
            ColMetaData source = entry.getValue();
            String value = row.get(column);
            if (ColMetaData.JSON_DOCUMENT.equals(source.getColumnType()) && value != null) {
                try {
                    JsonNode document = MAPPER.readTree(value);
                    if (document == null || !document.isObject()) {
                        throw ThirdPartyApiException.as().with(new IOException("JSON document value must be an object"));
                    }
                    documents.put(column, document);
                    expand(source, column, document, "", "");
                } catch (IOException e) {
                    // Never return a partially parsed or unprocessed document.
                    throw ThirdPartyApiException.as().with(new IOException("Invalid JSON document value"));
                }
            } else {
                metadata.put(entry.getKey(), source);
                values.add(value);
                columns.add(column);
                pointers.add(null);
            }
            column++;
        }
    }

    private void expand(ColMetaData source, int column, JsonNode node, String path, String pointer) {
        if (node.isObject()) {
            for (var field : node.properties()) {
                String name = field.getKey();
                String childPath = name;
                if (!path.isEmpty()) {
                    childPath = path + "." + name;
                }
                expand(source, column, field.getValue(), childPath, pointer + "/" + name.replace("~", "~0").replace("/", "~1"));
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                expand(source, column, node.get(i), path, pointer + "/" + i);
            }
        } else {
            ColMetaData leaf = new ColMetaData();
            leaf.setCatalog(source.getCatalog());
            leaf.setSchema(source.getSchema());
            leaf.setTable(source.getTable());
            leaf.setColumn(path);
            leaf.setIndex(values.size());
            leaf.setJdbcType(JDBCType.VARCHAR);
            leaf.setColumnType("string");
            if (node.isNumber()) {
                leaf.setJdbcType(JDBCType.DECIMAL);
                leaf.setColumnType("number");
            } else if (node.isBoolean()) {
                leaf.setJdbcType(JDBCType.BOOLEAN);
                leaf.setColumnType("boolean");
            }
            metadata.put("$" + values.size(), leaf);
            String value = null;
            if (!node.isNull()) {
                value = node.asText();
            }
            values.add(value);
            columns.add(column);
            pointers.add(pointer);
        }
    }

    List<String> restore(List<String> processed) {
        List<String> result = new ArrayList<>(original);
        for (int i = 0; i < processed.size(); i++) {
            if (Objects.equals(values.get(i), processed.get(i))) {
                continue;
            }
            int column = columns.get(i);
            String pointer = pointers.get(i);
            if (pointer == null) {
                result.set(column, processed.get(i));
                continue;
            }
            int slash = pointer.lastIndexOf('/');
            JsonNode parent = documents.get(column).at(pointer.substring(0, slash));
            String key = pointer.substring(slash + 1).replace("~1", "/").replace("~0", "~");
            JsonNode value = TextNode.valueOf(processed.get(i));
            if (parent instanceof ObjectNode object) {
                object.set(key, value);
            } else {
                ((ArrayNode) parent).set(Integer.parseInt(key), value);
            }
        }
        documents.forEach((column, document) -> result.set(column, document.toString()));
        return result;
    }
}
