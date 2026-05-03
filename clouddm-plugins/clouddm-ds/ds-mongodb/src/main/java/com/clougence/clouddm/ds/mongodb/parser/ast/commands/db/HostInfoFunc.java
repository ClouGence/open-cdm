package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class HostInfoFunc extends AbstractMongoFunc {
    @Override
    public MongoFuncType getFuncType() {
        return MongoFuncType.HOST_INFO;
    }

    @Override
    public String toBson() {
        return "{\"hostInfo\":1}";
    }
}
