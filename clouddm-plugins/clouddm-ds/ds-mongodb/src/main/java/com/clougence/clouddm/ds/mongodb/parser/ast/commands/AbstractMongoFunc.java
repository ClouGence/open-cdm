package com.clougence.clouddm.ds.mongodb.parser.ast.commands;

import com.clougence.clouddm.ds.mongodb.parser.ast.AbstractMongoAst;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.dslpaser.ast.Statement;

public abstract class AbstractMongoFunc extends AbstractMongoAst implements Statement {

    protected MongoFuncType funcType;

    public AbstractMongoFunc(MongoFuncType funcType) {
        this.funcType = funcType;
    }

    public AbstractMongoFunc() {
    }

    public abstract MongoFuncType getFuncType();

    public String toBson() {
        throw new UnsupportedOperationException();
    }

}
