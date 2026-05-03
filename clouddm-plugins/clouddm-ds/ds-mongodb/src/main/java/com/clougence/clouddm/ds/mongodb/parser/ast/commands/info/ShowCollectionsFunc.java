package com.clougence.clouddm.ds.mongodb.parser.ast.commands.info;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class ShowCollectionsFunc extends AbstractMongoFunc {

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.SHOW_COLLECTIONS; }

}
