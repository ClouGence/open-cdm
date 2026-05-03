package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class FsyncUnlockFunc extends AbstractMongoFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.FSYNC_UNLOCK; }

    @Override
    public String toBson() {
        return "{\"fsyncUnlock\":1}";
    }
}
