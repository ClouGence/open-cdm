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
package com.clougence.sql.iotdb;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.iotdb.antlr.IotdbLexer;
import com.clougence.sql.iotdb.antlr.IotdbParser;

final class IotdbSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final IotdbDslProvider provider;

    IotdbSplitAnalysisSpi(IotdbDslProvider provider){
        this.provider = provider;
    }

    @Override
    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return IotdbSplitVisitor.INSTANCE;
    }

    @Override
    protected void parseRoot(Parser parser) {
        provider.statements((IotdbParser) parser);
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof IotdbParser.TreeStatementContext || context instanceof IotdbParser.TableStatementContext;
    }

    @Override
    protected boolean isStatementTerminated(Parser parser) {
        int next = parser.getTokenStream().LA(1);
        return next == IotdbLexer.SEMI || next == Token.EOF;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return provider;
    }

    @Override
    public Stream<SplitScript> splitScriptStream(Reader reader, List<QueryArg> args, int baseLine, int baseColumn) {
        if (args != null && !args.isEmpty()) {
            throw ThirdPartyApiException.as().with("IoTDB SQL parameters are not supported");
        }
        return splitSourceStream(reader, baseLine, baseColumn).map(script -> {
            script.setScript(jdbcSql(script.getScript()));
            return script;
        });
    }

    Stream<SplitScript> splitSourceStream(Reader reader, int baseLine, int baseColumn) {
        return super.splitScriptStream(reader, List.of(), baseLine, baseColumn);
    }

    private String jdbcSql(String source) {
        // The JDBC Table parser expects EOF after the statement. The Tree parser also rejects comments.
        if (source.endsWith(";")) {
            source = source.substring(0, source.length() - 1);
        }
        if (provider == IotdbDslProvider.TABLE || (!source.contains("--") && !source.contains("/*"))) {
            return source;
        }
        StringBuilder sql = new StringBuilder(source.length());
        for (Token token : provider.createLexer(CharStreams.fromString(source)).getAllTokens()) {
            String text = token.getText();
            if (token.getType() == IotdbLexer.LINE_COMMENT || token.getType() == IotdbLexer.BLOCK_COMMENT) {
                text = text.replaceAll("[^\\r\\n]", " ");
            }
            sql.append(text);
        }
        return sql.toString();
    }
}
