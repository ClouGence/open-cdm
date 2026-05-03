package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;

public class DataSizeFunc extends CollectionFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DATA_SIZE; }

    public String toBson(String database) {
        return "{\"dataSize\":\"" + database + "." + collectionName + "\"}";
    }
}
