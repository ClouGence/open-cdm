package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIndexFunc extends CollectionFunc {

    private List<LinkedHashMap<String, String>> keys    = new ArrayList<>();
    private LinkedHashMap<String, String>       options = new LinkedHashMap<>();
    private String                              commitQuorum;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.CREATE_INDEX; }

    public CreateIndexFunc(MongoFuncType type){
        super(type);
    }

    private void check() {
        if (options.get("name") != null && keys.size() > 1) {
            throw new UnsupportedOperationException("CreateIndexes not support set name");
        }
    }

    private String parserName(LinkedHashMap<String, String> options) {
        List<String> sb = new ArrayList<>();
        options.forEach((key, value) -> {
            if (key.startsWith("\"") || key.startsWith("'")) {
                sb.add(key.substring(1, key.length() - 1));
            } else {
                sb.add(key);
            }

            if (value.startsWith("\"") || value.endsWith("'")) {
                sb.add(value.substring(1, value.length() - 1));
            } else {
                sb.add(value);
            }
        });
        return String.join("_", sb);
    }

    @Override
    public String toBson() {
        check();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"createIndexes\":");
        sb.append("\"").append(this.getCollectionName()).append("\"");
        sb.append(",indexes:[");
        for (int i = 0; i < keys.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            LinkedHashMap<String, String> stringStringLinkedHashMap = keys.get(i);
            sb.append("{\"key\":{");
            stringStringLinkedHashMap.forEach((key, value) -> {
                sb.append(key);
                sb.append(":").append(value);
                sb.append(",");
            });
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            this.options.forEach((k, v) -> sb.append(",\"").append(k).append("\":").append(v));

            if (!this.options.containsKey("name")) {
                sb.append(",\"name\":\"").append(parserName(stringStringLinkedHashMap)).append("\"");
            }
            sb.append("}");
        }

        sb.append("]");

        if (commitQuorum != null) {
            sb.append(", \"commitQuorum\":").append(commitQuorum);
        }
        sb.append("}");
        return sb.toString();
    }
}
