package com.clougence.clouddm.ds.mongodb.parser.ast.commands.client;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFunc extends AbstractMongoFunc {

    private String database;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.USE; }

}
