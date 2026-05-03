package com.clougence.dslpaser.antlr;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;

public interface DslProvider {

    String[] getDslName();

    Lexer createLexer(CharStream charStream);

    Parser createParser(Lexer lexer);

    StatementSet doParser(Lexer lexer, Parser parser);

    List<AstSplitScript> doSplit(Lexer lexer, Parser parser);

    void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor);
}
