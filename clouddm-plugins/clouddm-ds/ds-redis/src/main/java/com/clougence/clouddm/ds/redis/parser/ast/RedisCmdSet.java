package com.clougence.clouddm.ds.redis.parser.ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class RedisCmdSet extends AbstractRedisAst implements StatementSet {

    private final List<Statement> statementList = new ArrayList<>();

    @Override
    public List<Statement> getStatements() { return this.statementList; }

    public void add(Statement cmdInst) {
        if (cmdInst != null) {
            this.statementList.add(cmdInst);
        }
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < this.statementList.size(); i++) {
            Statement cmd = this.statementList.get(i);
            if (i > 0) {
                strBuilder.append("\n");
            }
            strBuilder.append(cmd.toString());
        }
        return strBuilder.toString();
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {

    }
}
