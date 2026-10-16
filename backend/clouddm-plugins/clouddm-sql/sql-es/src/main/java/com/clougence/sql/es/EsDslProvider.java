/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.es;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.es.antlr.EsLexer;
import com.clougence.sql.es.antlr.EsParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class EsDslProvider implements DslProvider {
    private final EsRequestValidator     validator;
    private final EsAntlrStatementParser statementParser = new EsAntlrStatementParser();

    public EsDslProvider(SqlParserParameters parameters){
        this.validator = new EsRequestValidator(parameters);
    }

    @Override
    public String[] getDslName() { return new String[] { EsSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        return new EsLexer(charStream);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        EsParser parser = new EsParser(new CommonTokenStream(lexer));
        EsSyntaxErrorListener.install(parser);
        parser.addParseListener(validator);
        return parser;
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        EsSyntaxErrorListener.install(parser);
        List<EsRequest> requests = new ArrayList<>();
        for (EsParser.RequestContext context : ((EsParser) parser).script().request()) {
            EsRequest request = EsAstBuilder.from(context, parser.getTokenStream().getText(context));
            validator.validate(request);
            requests.add(request);
        }
        return new EsRequestSet(requests, parser.getTokenStream().getText());
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> result = new ArrayList<>();
        ParseTree lastTree = null;
        for (ParseTree tree : statementParser.statementList(lexer, parser)) {
            EsParser.RequestContext context = (EsParser.RequestContext) tree;
            EsRequest request = EsAstBuilder.from(context, parser.getTokenStream().getText(context));
            validator.validate(request);
            EsSourceRange range = request.range();
            Token start = EsAntlrStatementParser.scriptStart(parser.getTokenStream(), lastTree, context.getStart());
            result.add(AstSplitScript.builder()
                .script(statementParser.getTextKeepComment(parser.getTokenStream(), lastTree, context.getStart(), context.getStop()))
                .astTree(tree)
                .parser(parser)
                .lexer(lexer)
                .bodyStartCodeLine(range.startLine())
                .bodyStartCodeColumn(range.startColumn())
                .startCodeLine(start.getLine())
                .startCodeColumn(start.getCharPositionInLine())
                .endCodeLine(range.endLine())
                .endCodeColumn(range.endColumn())
                .build());
            lastTree = tree;
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        for (ParseTree tree : statementParser.statementList(lexer, parser)) {
            EsParser.RequestContext context = (EsParser.RequestContext) tree;
            validator.validate(EsAstBuilder.from(context, parser.getTokenStream().getText(context)));
            visitor.visit(tree);
        }
    }
}
