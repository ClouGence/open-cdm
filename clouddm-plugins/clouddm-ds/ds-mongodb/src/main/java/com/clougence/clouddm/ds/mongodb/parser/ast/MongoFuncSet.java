package com.clougence.clouddm.ds.mongodb.parser.ast;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.mongodb.parser.ast.AbstractMongoAst;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;

public class MongoFuncSet extends AbstractMongoAst implements StatementSet {

    private final List<Statement> statementList = new ArrayList<>();

    @Override
    public List<Statement> getStatements() { return this.statementList; }

    public void add(Statement cmdInst) {
        if (cmdInst != null) {
            this.statementList.add(cmdInst);
        }
    }

}
