package com.clougence.clouddm.dsfamily.postgres.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlLexer;
import com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.utils.CollectionUtils;

public class PgStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> children = ((PgSqlParser) parser).root().children;
        PgSqlParser.StmtmultiContext stmtmultiContext = (PgSqlParser.StmtmultiContext) children.get(0).getChild(0);
        if (CollectionUtils.isEmpty(stmtmultiContext.children)) {
            return Collections.emptyList();
        }

        List<ParseTree> result = new ArrayList<>();
        for (ParseTree child : stmtmultiContext.children) {
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
            int type = start.getType();
            if (type == PgSqlLexer.Whitespace || type == PgSqlLexer.Newline) {
                // ignore
            } else if (type == PgSqlLexer.SEMI) {
                break;
            } else if (type == PgSqlLexer.BlockComment || type == PgSqlLexer.LineComment || type == PgSqlLexer.UnterminatedBlockComment) {
                startToken = start;
            } else {
                break;
            }
        }

        for (int i = endToken.getTokenIndex() + 1; i < tokens.size(); i++) {
            Token end = tokens.get(i);
            int type = end.getType();
            if (type == PgSqlLexer.Whitespace || type == PgSqlLexer.Newline) {
                //ignore
            } else if (type == PgSqlLexer.SEMI) {
                endToken = end;
                break;
            } else if (type == PgSqlLexer.BlockComment || type == PgSqlLexer.LineComment || type == PgSqlLexer.UnterminatedBlockComment) {
                endToken = end;
            } else {
                break;
            }
        }

        return tokens.getText(startToken, endToken);
    }
}
