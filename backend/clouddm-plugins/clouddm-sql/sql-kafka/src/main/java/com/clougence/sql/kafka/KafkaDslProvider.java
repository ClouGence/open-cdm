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
package com.clougence.sql.kafka;

import java.util.ArrayList;
import java.util.List;

import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.kafka.antlr.KafkaLexer;
import com.clougence.sql.kafka.antlr.KafkaParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public class KafkaDslProvider implements DslProvider {
    public static final KafkaDslProvider    INSTANCE        = new KafkaDslProvider();
    private final KafkaAntlrStatementParser statementParser = new KafkaAntlrStatementParser();

    @Override
    public String[] getDslName() { return new String[] { KafkaSqlEngineSpi.NAME }; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        return new KafkaLexer(charStream);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        return new KafkaParser(new CommonTokenStream(lexer));
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        KafkaCommandParser converter = new KafkaCommandParser();
        return new KafkaCommandSet(((KafkaParser) parser).script().command().stream().map(converter::from).toList());
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        List<AstSplitScript> result = new ArrayList<>();
        ParseTree lastTree = null;
        for (ParseTree tree : statementParser.statementList(lexer, parser)) {
            ParserRuleContext context = (ParserRuleContext) tree;
            result.add(AstSplitScript.builder()
                .script(statementParser.getTextKeepComment(parser.getTokenStream(), lastTree, context.getStart(), context.getStop()))
                .astTree(tree)
                .parser(parser)
                .lexer(lexer)
                .bodyStartCodeLine(context.getStart().getLine())
                .bodyStartCodeColumn(context.getStart().getCharPositionInLine())
                .build());
            lastTree = tree;
        }
        return result;
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        for (ParseTree tree : statementParser.statementList(lexer, parser)) {
            visitor.visit(tree);
        }
    }
}
