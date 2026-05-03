package com.clougence.dslpaser.parse;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public interface AntlrParseTreeVisitorCreator {

    AbstractParseTreeVisitor<?> createVisitor(Lexer lexer, Parser parser);
}
