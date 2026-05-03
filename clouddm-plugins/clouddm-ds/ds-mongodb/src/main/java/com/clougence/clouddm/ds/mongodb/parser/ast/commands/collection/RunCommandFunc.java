package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandType;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class RunCommandFunc extends CollectionFunc {

    private String      command;

    private CommandType commandType;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.RUN_COMMAND; }

    public String toBson() {
        return command;
    }
}
