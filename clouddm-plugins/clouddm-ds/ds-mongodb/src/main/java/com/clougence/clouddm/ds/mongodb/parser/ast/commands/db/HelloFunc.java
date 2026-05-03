package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class HelloFunc extends AbstractMongoFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.HELLO; }

    @Override
    public String toBson() {
        return "{\"hello\":null}";
    }
}
