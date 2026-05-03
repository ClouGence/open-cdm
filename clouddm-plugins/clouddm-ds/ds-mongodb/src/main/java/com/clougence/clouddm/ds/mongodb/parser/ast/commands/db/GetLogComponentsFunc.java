package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class GetLogComponentsFunc extends AbstractMongoFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.GET_LOG_COMPONENTS; }

    @Override
    public String toBson() {
        return "{\"getParameter\":1,\"logComponentVerbosity\":1}";
    }
}
