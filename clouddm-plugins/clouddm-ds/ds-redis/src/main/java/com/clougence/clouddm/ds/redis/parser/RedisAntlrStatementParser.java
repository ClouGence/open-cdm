package com.clougence.clouddm.ds.redis.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser;
import com.clougence.dslpaser.parse.AntlrStatementParser;

public class RedisAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        recursionAddText(result, ((RedisParser) parser).rootInstSet());
        return result;
    }

    private void recursionAddText(List<ParseTree> result, ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof RedisParser.CmdInstContext) {
                result.add(child);
            } else {
                recursionAddText(result, child);
            }
        }
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        int deleteChars = 0;
        if (lastTree != null) {
            startToken = ((ParserRuleContext) lastTree).getStop();
            deleteChars = startToken.getText().length();
        }

        String text = tokens.getText(startToken, endToken).trim();
        if (deleteChars > 0) {
            text = text.substring(deleteChars).trim();
        }
        return text;
    }
}
