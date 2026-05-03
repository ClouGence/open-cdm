package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class FindAndModifyFunc extends CollectionFunc {

    private String query;
    private String sort;
    private String remove;
    private String update;
    // new
    private String new_;
    private String fields;
    private String upsert;
    private String bypassDocumentValidation;
    private String writeConcern;
    private String maxTimeMS;
    private String collation;
    private String arrayFilters;
    private String hint;
    private String comment;
    private String let;

    public FindAndModifyFunc(MongoFuncType type){
        super(type);
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("findAndModify", getQuoteName());
        commandBuilder.addOption("query", query);
        commandBuilder.addOption("sort", sort);
        commandBuilder.addOption("remove", remove);
        commandBuilder.addOption("update", update);
        commandBuilder.addOption("new", new_);
        commandBuilder.addOption("fields", fields);
        commandBuilder.addOption("upsert", upsert);
        commandBuilder.addOption("bypassDocumentValidation", bypassDocumentValidation);
        commandBuilder.addOption("writeConcern", writeConcern);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("collation", collation);
        commandBuilder.addOption("arrayFilters", arrayFilters);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("comment", comment);
        commandBuilder.addOption("let", let);
        return commandBuilder.toString();
    }
}
