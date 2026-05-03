package com.clougence.clouddm.ds.oceanbase.parser.ob4my;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlLexer;
import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlParser;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.dslpaser.parse.AstSplitScript;

public class ObMyDslProvider implements DslProvider {

    public static final DslProvider    INSTANCE    = new ObMyDslProvider();
    private final AntlrStatementParser TREE_PARSER = new ObMyStatementParser();

    @Override
    public String[] getDslName() { return new String[] { "OceanBase for MySQL", "OceanBase for MySQL Language" }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        return new ObForMySqlLexer(charStream);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        return new ObForMySqlParser(new CommonTokenStream(lexer));
    }

    protected AntlrStatementParser treeParser() {
        return TREE_PARSER;
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        TokenStream tokenStream = parser.getTokenStream();
        List<ParseTree> astList = this.treeParser().statementList(lexer, parser);

        List<AstSplitScript> result = new ArrayList<>();
        ParseTree lastTree = null;
        for (ParseTree parseTree : astList) {
            ParserRuleContext context = (ParserRuleContext) parseTree;
            Token startToken = context.getStart();
            Token stopToken = context.getStop();

            result.add(AstSplitScript.builder()
                .script(this.treeParser().getTextKeepComment(tokenStream, lastTree, startToken, stopToken))
                .astTree(parseTree)
                .parser(parser)
                .lexer(lexer)
                .bodyStartCodeLine(startToken.getLine())
                .bodyStartCodeColumn(startToken.getCharPositionInLine())
                .build());
            lastTree = parseTree;
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        List<ParseTree> astList = this.treeParser().statementList(lexer, parser);
        for (ParseTree astTree : astList) {
            visitor.visit(astTree);
        }
    }
}
