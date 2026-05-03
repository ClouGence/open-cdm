package com.clougence.clouddm.ds.ads.parser.ads4my;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.ads.parser.ads4my.antlr.AdsMyParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class AdsMyStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((AdsMyParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof AdsMyParser.SqlStatementsContext) {
                for (ParseTree parseTree : ((AdsMyParser.SqlStatementsContext) child).children) {
                    if (parseTree instanceof AdsMyParser.SqlStatementContext) {
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
            if (start.getType() == AdsMyParser.SPACE) {
                // ignore
            } else if (start.getType() == AdsMyParser.SEMI) {
                break;
            } else if (start.getType() == AdsMyParser.COMMENT_INPUT || start.getType() == AdsMyParser.LINE_COMMENT) {
                startToken = start;
            } else {
                startToken = start;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == AdsMyParser.SPACE) {
                //ignore
            } else if (end.getType() == AdsMyParser.SEMI) {
                endToken = end;
                break;
            } else if (end.getType() == AdsMyParser.COMMENT_INPUT || end.getType() == AdsMyParser.LINE_COMMENT) {
                endToken = end;
            } else {
                endToken = end;;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
