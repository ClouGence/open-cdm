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
package com.clougence.sql.hana.analysis;

import java.util.Map;
import java.util.function.BiFunction;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.execute.MetaIndexedObject;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.hana.i18n.HanaSqlI18nKeys;
import com.clougence.sql.hana.parser.HanaDslProvider;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;

@Slf4j
public final class HanaSqlAnalyzer {
    private HanaSqlAnalyzer(){
    }

    public static HanaAnalysisResult analyze(SplitScript script, Map<UmiTypes, Object> levels) {
        return analyze(script, levels, null);
    }

    public static HanaAnalysisResult analyze(SplitScript script, Map<UmiTypes, Object> levels, BiFunction<Map<UmiTypes, Object>, String, MetaIndexedObject> indexedObjectResolver) {
        try {
            var lexer = HanaDslProvider.INSTANCE.createLexer(CharStreams.fromString(script.getScript()));
            HanaParser parser = (HanaParser) HanaDslProvider.INSTANCE.createParser(lexer);
            var tree = parser.statementRoot();
            HanaBehaviorParserVisitor visitor = new HanaBehaviorParserVisitor(script, levels, indexedObjectResolver);
            visitor.visit(tree);
            return visitor.result();
        } catch (AntlerSyntaxException e) {
            String msg = "Cannot analyze HANA statement at line " + script.getBodyStartCodeLine();
            log.error(msg, e);
            throw ThirdPartyApiException.as()
                .with(e, HanaSqlI18nKeys.HANA_SQL_ANALYSIS_UNSUPPORTED, script.getBodyStartCodeLine() + e.getLine() -
                                                                        1, e.getColumn() + (e.getLine() == 1 ? script.getBodyStartCodeColumn() : 0), e.getMessage());
        }
    }
}
