package com.clougence.clouddm.ds.mongodb.parser.ast.commands.db;

import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

import lombok.Setter;

public class ProfileFunc extends AbstractMongoFunc {

    @Setter
    private String              level;
    private Map<String, String> options = new LinkedHashMap<>();

    public void addOption(String key, String value) {
        options.put(key, value);
    }

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.PROFILE; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("profile", level);
        options.forEach(commandBuilder::addOption);
        return commandBuilder.toString();
    }
}
