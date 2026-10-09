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

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.kafka.antlr.KafkaLexer;
import com.clougence.sql.kafka.antlr.KafkaParser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class KafkaSplitAnalysisSpi extends AbstractSplitAnalysisSpi {
    @Override
    protected DslProvider dslProvider() {
        return KafkaDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return new KafkaSplitVisitor();
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((KafkaParser) parser).script();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof KafkaParser.CommandContext;
    }

    @Override
    protected boolean isStatementTerminated(Parser parser) {
        int type = parser.getCurrentToken().getType();
        return type == KafkaLexer.SEMICOLON || type == KafkaLexer.EOL || type == Token.EOF;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new KafkaAntlrStatementParser();
    }

    @Override
    public Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        if (args != null && !args.isEmpty()) {
            throw ThirdPartyApiException.as().with(KafkaSqlI18nKeys.KAFKA_ARGS_UNSUPPORTED);
        }
        return super.splitScriptStream(reader, args, baseCodeLine, baseCodeColumn);
    }
}
