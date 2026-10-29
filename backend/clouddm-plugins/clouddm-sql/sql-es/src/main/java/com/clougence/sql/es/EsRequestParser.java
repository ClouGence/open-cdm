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
import com.clougence.sql.es.antlr.EsParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

public class EsRequestParser {
    private final EsDslProvider      provider;
    private final EsRequestValidator validator;

    public EsRequestParser(SqlParserParameters parameters){
        this.provider = new EsDslProvider(parameters);
        this.validator = new EsRequestValidator(parameters);
    }

    public EsRequest parse(String text) {
        Lexer lexer = provider.createLexer(CharStreams.fromString(text));
        EsParser parser = (EsParser) provider.createParser(lexer);
        EsParser.RequestContext context = parser.singleRequest().request();
        EsRequest request = EsAstBuilder.from(context, parser.getTokenStream().getText(context));
        validator.validate(request);
        return request;
    }

    public EsRequestSet parseScript(String text) {
        Lexer lexer = provider.createLexer(CharStreams.fromString(text));
        Parser parser = provider.createParser(lexer);
        return (EsRequestSet) provider.doParser(lexer, parser);
    }
}
