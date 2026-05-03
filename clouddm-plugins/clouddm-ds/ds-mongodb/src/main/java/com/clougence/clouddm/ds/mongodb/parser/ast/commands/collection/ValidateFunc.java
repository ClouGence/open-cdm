package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class ValidateFunc extends CollectionFunc {

    private String full;
    private String repair;
    private String checkBSONConformance;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.VALIDATE; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("validate", getQuoteName());
        commandBuilder.addOption("full", full);
        commandBuilder.addOption("repair", repair);
        commandBuilder.addOption("checkBSONConformance", checkBSONConformance);
        return commandBuilder.toString();
    }
}
