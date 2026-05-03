package com.clougence.clouddm.ds.maxcompute.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class McSqlAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((McParserParser) parser).block().children;
        for (ParseTree child : children) {
            if (child instanceof McParserParser.StmtContext) {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == McParserParser.L_WS) {
            } else if (start.getType() == McParserParser.T_SEMICOLON) {
                break;
            } else if (start.getType() == McParserParser.L_M_COMMENT || start.getType() == McParserParser.L_S_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == McParserParser.L_WS) {
            } else if (end.getType() == McParserParser.T_SEMICOLON) {
                endToken = end;
                break;
            } else if (end.getType() == McParserParser.L_M_COMMENT || end.getType() == McParserParser.L_S_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
