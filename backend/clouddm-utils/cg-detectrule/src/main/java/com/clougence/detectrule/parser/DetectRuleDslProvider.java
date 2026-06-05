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
package com.clougence.detectrule.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.detectrule.parser.antlr.DefaultDetectRulesVisitor;
import com.clougence.detectrule.parser.antlr.DetectRulesLexer;
import com.clougence.detectrule.parser.antlr.DetectRulesParser;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class DetectRuleDslProvider implements DslProvider {

    private final String[]                 dslName;
    private final List<DetectRulesFeature> features;

    public DetectRuleDslProvider(){
        this("full-featured", DetectRulesFeature.values());
    }

    public DetectRuleDslProvider(DetectRulesFeature[] features){
        this(autoName(features), features);
    }

    private DetectRuleDslProvider(String dslName, DetectRulesFeature[] features){
        this.dslName = new String[] { "DetectRule", "DetectRule Language (" + dslName + ")" };
        this.features = (features == null) ? Collections.emptyList() : Arrays.asList(features);
    }

    private static String autoName(DetectRulesFeature[] features) {
        if (features == null || features.length == 0) {
            return "primary";
        } else {
            List<String> str = Arrays.stream(features).map(DetectRulesFeature::getSortName).collect(Collectors.toList());
            return StringUtils.join(str.toArray(), ",") + "-featured";
        }
    }

    @Override
    public String[] getDslName() { return this.dslName; }

    @Override
    public Lexer createLexer(CharStream charStream) {
        return new DetectRulesLexer(charStream, this.features);
    }

    @Override
    public Parser createParser(Lexer lexer) {
        return new DetectRulesParser(new CommonTokenStream(lexer), this.features);
    }

    @Override
    public StatementSet doParser(Lexer lexer, Parser parser) {
        DefaultDetectRulesVisitor visitor = new DefaultDetectRulesVisitor(this.features);
        return (StatementList) visitor.visit(((DetectRulesParser) parser).root());
    }

    @Override
    public List<AstSplitScript> doSplit(Lexer lexer, Parser parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doVisitor(Lexer lexer, Parser parser, AbstractParseTreeVisitor<?> visitor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "DetectRuleDslProvider[" + this.getDslName()[1] + "]";
    }
}
