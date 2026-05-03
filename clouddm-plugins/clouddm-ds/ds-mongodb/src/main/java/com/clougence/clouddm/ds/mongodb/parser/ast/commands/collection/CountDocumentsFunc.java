package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountDocumentsFunc extends CollectionFunc {

    private String filter = "{}";

    private String limit;
    private String skip   = "0";
    private String hint;
    private String maxTimeMS;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.COUNT_DOCUMENTS; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("aggregate", getQuoteName());

        String pipeline = "[{\"$match\":" + filter + "},{$skip:" + skip + "},{\"$group\":{\"_id\":null,\"n\":{\"$sum\":1}}}]";

        commandBuilder.addOption("pipeline", pipeline);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("cursor", "{}");
        return commandBuilder.toString();
    }
}
