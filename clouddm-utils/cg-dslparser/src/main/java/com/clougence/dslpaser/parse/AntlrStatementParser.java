package com.clougence.dslpaser.parse;

import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public interface AntlrStatementParser {

    List<ParseTree> statementList(Lexer lexer, Parser parser);

    String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken);
}
