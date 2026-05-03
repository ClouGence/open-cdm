package com.clougence.clouddm.ds.mongodb.parser.ast;

import java.io.IOException;

import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

public abstract class AbstractMongoAst extends BlockLocation implements VisitorTree {

    @Override
    public void accept(Visitor visitor) {

    }

    public void doFormat(FmtWriter writer) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
