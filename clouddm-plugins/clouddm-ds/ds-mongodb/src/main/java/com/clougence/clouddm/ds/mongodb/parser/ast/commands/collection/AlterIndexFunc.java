package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class AlterIndexFunc extends CollectionFunc {

    private String  keyPattern;
    private String  name;

    private Boolean hidden;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.ALTER_INDEX; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("collMod", getQuoteName());
        CommandBuilder index = new CommandBuilder();
        index.addOption("keyPattern", keyPattern);
        index.addOption("name", name);
        if (hidden != null) {
            index.addOption("hidden", hidden.toString());
        }
        commandBuilder.addOption("index", index.toString());
        return commandBuilder.toString();

    }
}
