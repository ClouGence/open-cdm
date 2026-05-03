package com.clougence.clouddm.ds.sqlserver.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerLexer;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class MsSqlStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        List<ParseTree> children = ((SqlServerParser) parser).tsql_file().children;
        for (ParseTree child : children) {
            if (child instanceof SqlServerParser.Sql_clausesContext) {
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
            if (type == SqlServerLexer.SPACE) {
                //ignore
            } else if (type == SqlServerLexer.SEMI) {
                break;
            } else if (type == SqlServerLexer.COMMENT || type == SqlServerLexer.LINE_COMMENT) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            int type = end.getType();
            if (type == SqlServerLexer.SPACE) {
                //ignore
            } else if (type == SqlServerLexer.SEMI) {
                endToken = end;
                break;
            } else if (type == SqlServerLexer.COMMENT || type == SqlServerLexer.LINE_COMMENT) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
