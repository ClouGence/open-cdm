package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertFunc extends CollectionFunc {

    private String documents;

    //option
    private String writeConcern;

    private String ordered;

    public InsertFunc(MongoFuncType type){
        super(type);
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("insert", getQuoteName());
        commandBuilder.addOption("documents", documents);
        commandBuilder.addOption("ordered", ordered);
        commandBuilder.addOption("writeConcern", writeConcern);
        return commandBuilder.toString();
    }

}
