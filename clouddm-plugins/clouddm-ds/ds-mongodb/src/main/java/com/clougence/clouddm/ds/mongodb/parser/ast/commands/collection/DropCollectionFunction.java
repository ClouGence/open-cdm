package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import java.util.LinkedHashMap;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropCollectionFunction extends CollectionFunc {

    private LinkedHashMap<String, String> options = new LinkedHashMap<>();

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DROP; }

    @Override
    public String toBson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{drop:");
        sb.append("\"").append(this.getCollectionName()).append("\"");
        this.options.forEach((k, v) -> sb.append(",").append("\"").append(k).append("\"").append(":").append(v));
        sb.append("}");
        return sb.toString();
    }
}
