package com.clougence.dslpaser.ast;

import java.util.List;

import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.Format;

public interface StatementSet extends Format, VisitorTree {

    List<Statement> getStatements();
}
