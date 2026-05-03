package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

import lombok.Setter;

@Setter
public class AdminCommandFunc extends AbstractMongoFunc {

    private String command;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.ADMIN_COMMAND; }

    public String toBson() {
        return command;
    }
}
