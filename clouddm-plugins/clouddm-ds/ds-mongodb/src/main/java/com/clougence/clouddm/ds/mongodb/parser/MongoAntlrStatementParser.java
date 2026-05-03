package com.clougence.clouddm.ds.mongodb.parser;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.mongodb.parser.antlr.MongoParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.dslpaser.parse.AntlrStatementParser;

public class MongoAntlrStatementParser implements AntlrStatementParser {

    @Override
    public List<ParseTree> statementList(Lexer lexer, Parser parser) {
        List<ParseTree> result = new ArrayList<>();
        recursionAddText(result, ((MongoParser) parser).root());
        return result;
    }

    private void recursionAddText(List<ParseTree> result, ParseTree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof MongoParser.CommandContext) {
                result.add(child);
            }
        }
    }

    @Override
    public String getTextKeepComment(TokenStream tokens, ParseTree lastTree, Token startToken, Token endToken) {
        return tokens.getText(startToken, endToken).trim();
    }
}
