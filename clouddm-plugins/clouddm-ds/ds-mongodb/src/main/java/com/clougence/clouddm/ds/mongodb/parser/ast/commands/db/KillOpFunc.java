package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class KillOpFunc extends AbstractMongoFunc {

    private String opid;

    public KillOpFunc(String opid){
        this.opid = opid;
    }

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.KILL_OP; }

    @Override
    public String toBson() {
        return "{\"killOp\":" + opid + "}";
    }
}
