package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class CreateFunc extends AbstractMongoFunc {

    private String              name;
    private Map<String, String> options = new LinkedHashMap<>();

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    public CreateFunc(String name, MongoFuncType type){
        super(type);
        this.name = name;
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("create", name);
        options.forEach(commandBuilder::addOption);
        return commandBuilder.toString();
    }
}
