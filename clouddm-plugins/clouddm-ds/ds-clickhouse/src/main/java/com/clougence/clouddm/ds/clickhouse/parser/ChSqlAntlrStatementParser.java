package com.clougence.clouddm.ds.clickhouse.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class ChSqlAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((ClickHouseParser) parser).root().children;
        for (ParseTree child : children) {
            if (child instanceof ClickHouseParser.QueryStmtContext) {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == ClickHouseParser.WHITESPACE) {
                // ignore
            } else if (start.getType() == ClickHouseParser.SEMICOLON) {
                break;
            } else if (start.getType() == ClickHouseParser.MULTI_LINE_COMMENT || start.getType() == ClickHouseParser.SINGLE_LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == ClickHouseParser.WHITESPACE) {
                //ignore
            } else if (end.getType() == ClickHouseParser.SEMICOLON) {
                break;
            } else if (end.getType() == ClickHouseParser.MULTI_LINE_COMMENT || end.getType() == ClickHouseParser.SINGLE_LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
