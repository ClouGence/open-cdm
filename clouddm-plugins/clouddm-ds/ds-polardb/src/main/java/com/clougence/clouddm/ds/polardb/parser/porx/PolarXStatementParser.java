package com.clougence.clouddm.ds.polardb.parser.porx;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.polardb.parser.porx.antlr.PolardbXLexer;
import com.clougence.clouddm.ds.polardb.parser.porx.antlr.PolardbXParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class PolarXStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((PolardbXParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof PolardbXParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((PolardbXParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof PolardbXParser.SqlStatementContext) {
                        result.add(parseTree);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == PolardbXLexer.SPACE) {
                // ignore
            } else if (start.getType() == PolardbXLexer.SEMI) {
                break;
            } else if (start.getType() == PolardbXLexer.COMMENT_INPUT || start.getType() == PolardbXLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == PolardbXLexer.SPACE) {
                //ignore
            } else if (end.getType() == PolardbXLexer.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == PolardbXLexer.COMMENT_INPUT || end.getType() == PolardbXLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
