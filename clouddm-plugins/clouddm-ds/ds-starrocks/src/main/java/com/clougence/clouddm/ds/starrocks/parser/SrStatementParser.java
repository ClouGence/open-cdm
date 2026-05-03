package com.clougence.clouddm.ds.starrocks.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.SyntaxErrorListener;

public class SrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        parser.addErrorListener(SyntaxErrorListener.INSTANCE);
        List<ParseTree> result = new ArrayList<>();
        List<StarRocksParser.SingleStatementContext> singleStatementContexts = ((StarRocksParser) parser).sqlStatements().singleStatement();
        for (StarRocksParser.SingleStatementContext singleStatementContext : singleStatementContexts) {
            if (singleStatementContext.statement()!=null) {
                result.add(singleStatementContext.statement());
            }
        }

        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            int type = start.getType();
            if (type == StarRocksParser.SEMICOLON) {
                break;
            } else if (type == StarRocksParser.SIMPLE_COMMENT || type == StarRocksParser.BRACKETED_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            int type = end.getType();
            if (type == StarRocksParser.SEMICOLON) {
                endToken = end;
                break;
            } else if (type == StarRocksParser.SIMPLE_COMMENT || type == StarRocksParser.BRACKETED_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
