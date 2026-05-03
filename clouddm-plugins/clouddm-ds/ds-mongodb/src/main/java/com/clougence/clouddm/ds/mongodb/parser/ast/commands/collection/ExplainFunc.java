package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class ExplainFunc extends CollectionFunc {

    private CollectionFunc func;
    private String         verbosity;

    public ExplainFunc(CollectionFunc func){
        this.func = func;
    }

    public void setCollectionName(String collectionName) {
        this.func.setCollectionName(collectionName);
    }

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.EXPLAIN; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("explain", func.toBson());
        commandBuilder.addOption("verbosity", verbosity);
        return commandBuilder.toString();
    }
}
