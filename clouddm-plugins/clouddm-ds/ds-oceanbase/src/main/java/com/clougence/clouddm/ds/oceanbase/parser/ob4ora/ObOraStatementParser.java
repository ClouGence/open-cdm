package com.clougence.clouddm.ds.oceanbase.parser.ob4ora;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.oceanbase.parser.ob4ora.antlr.ObForOracleParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class ObOraStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((ObForOracleParser) parser).sql_script().children;
        for (ParseTree child : children) {
            if (child instanceof TerminalNodeImpl) {
                continue;
            } else {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        for (int i = startToken.getTokenIndex() - 1; i >= 0; i--) {
            Token start = tokens.get(i);
            if (start.getType() == ObForOracleParser.SPACES) {
                // ignore
            } else if (start.getType() == ObForOracleParser.SEMICOLON) {
                break;
            } else if (start.getType() == ObForOracleParser.SINGLE_LINE_COMMENT || start.getType() == ObForOracleParser.MULTI_LINE_COMMENT
                       || start.getType() == ObForOracleParser.REMARK_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            if (end.getType() == ObForOracleParser.SPACES) {
                //ignore
            } else if (end.getType() == ObForOracleParser.SEMICOLON) {
                endToken = end;
                break;
            } else if (end.getType() == ObForOracleParser.SINGLE_LINE_COMMENT || end.getType() == ObForOracleParser.MULTI_LINE_COMMENT
                       || end.getType() == ObForOracleParser.REMARK_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
