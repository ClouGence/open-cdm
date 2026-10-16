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

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.es.antlr.EsLexer;
import com.clougence.sql.es.antlr.EsParser;

public class EsSplitAnalysisSpi extends AbstractSplitAnalysisSpi {
    private final EsDslProvider   provider;
    private final EsRequestParser requestParser;

    public EsSplitAnalysisSpi(SqlParserParameters parameters){
        this.provider = new EsDslProvider(parameters);
        this.requestParser = new EsRequestParser(parameters);
    }

    @Override
    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return new EsSplitVisitor();
    }

    @Override
    protected void parseRoot(Parser parser) {
        EsSyntaxErrorListener.install(parser);
        ((EsParser) parser).script();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof EsParser.RequestContext;
    }

    @Override
    protected boolean isStatementTerminated(Parser parser) {
        int type = parser.getCurrentToken().getType();
        return type == EsLexer.EOL || type == Token.EOF;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new EsAntlrStatementParser();
    }

    @Override
    public Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        if (args != null && !args.isEmpty()) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_ARGS_UNSUPPORTED);
        }
        return super.splitScriptStream(reader, args, baseCodeLine, baseCodeColumn).map(script -> {
            // Validate after emission: throwing inside an ANTLR exit listener can re-enter it while unwinding.
            requestParser.parse(script.getScript());
            return script;
        });
    }
}
