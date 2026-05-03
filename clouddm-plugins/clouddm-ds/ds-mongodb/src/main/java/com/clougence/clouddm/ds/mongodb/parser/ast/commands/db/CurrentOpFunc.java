package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

public class CurrentOpFunc extends AbstractMongoFunc {

    private Map<String, String> options = new LinkedHashMap<>();

    public void addOption(String key, String value) {
        options.put(key, value);
    }

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.CURRENT_OP; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("currentOp", "true");

        options.forEach(commandBuilder::addOption);
        return commandBuilder.toString();
    }
}
