package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropIndexesFunc extends CollectionFunc {

    private String index;

    public DropIndexesFunc(MongoFuncType type){
        super(type);
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    @Override
    public String toBson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"dropIndexes\":\"");
        sb.append(getCollectionName()).append("\"");
        sb.append(",\"index\":");
        if (this.index != null) {
            sb.append(index);
        } else {
            sb.append("\"*\"");
        }
        sb.append("}");
        return sb.toString();

    }
}
