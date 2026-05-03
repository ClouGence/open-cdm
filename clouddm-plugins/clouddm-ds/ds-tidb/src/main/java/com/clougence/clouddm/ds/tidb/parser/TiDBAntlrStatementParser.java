package com.clougence.clouddm.ds.tidb.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.parser.antlr.TiDBParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class TiDBAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((TiDBParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof TiDBParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((TiDBParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof TiDBParser.SqlStatementContext) {
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
            if (start.getType() == TiDBParser.SPACE) {
                // ignore
            } else if (start.getType() == TiDBParser.SEMI) {
                break;
            } else if (start.getType() == TiDBParser.COMMENT_INPUT || start.getType() == TiDBParser.LINE_COMMENT) {
                startToken = start;
            } else {
                startToken = start;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == TiDBParser.SPACE) {
                //ignore
            } else if (end.getType() == TiDBParser.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == TiDBParser.COMMENT_INPUT || end.getType() == TiDBParser.LINE_COMMENT) {
                endToken = end;
            } else {
                endToken = end;;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
