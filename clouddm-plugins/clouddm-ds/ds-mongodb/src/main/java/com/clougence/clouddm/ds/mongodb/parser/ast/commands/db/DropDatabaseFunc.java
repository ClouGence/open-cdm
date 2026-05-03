package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class DropDatabaseFunc extends AbstractMongoFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.DROP_DATABASE; }

    @Override
    public String toBson() {
        return "{\"dropDatabase\":1}";
    }
}
