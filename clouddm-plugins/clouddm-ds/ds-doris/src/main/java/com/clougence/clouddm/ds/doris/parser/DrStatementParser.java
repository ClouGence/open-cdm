package com.clougence.clouddm.ds.doris.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.doris.parser.antlr.DorisLexer;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public class DrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((DorisParser) parser).multiStatements().children;

        for (ParseTree child : children) {
            if (child instanceof DorisParser.StatementContext) {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            int type = start.getType();
            if (type == DorisLexer.SEMICOLON) {
                break;
            } else if (type == DorisLexer.SIMPLE_COMMENT || type == DorisLexer.BRACKETED_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            int type = end.getType();
            if (type == DorisLexer.SEMICOLON) {
                endToken = end;
                break;
            } else if (type == DorisLexer.SIMPLE_COMMENT || type == DorisLexer.BRACKETED_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
