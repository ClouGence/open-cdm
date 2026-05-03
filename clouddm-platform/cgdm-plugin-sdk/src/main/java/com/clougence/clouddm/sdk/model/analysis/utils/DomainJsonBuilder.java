package com.clougence.clouddm.sdk.model.analysis.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class DomainJsonBuilder {

    private final Map<String, String> body = new LinkedHashMap<>();

    public void add(String key, String value) {
        body.put(key, value);
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String> entry : body.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
