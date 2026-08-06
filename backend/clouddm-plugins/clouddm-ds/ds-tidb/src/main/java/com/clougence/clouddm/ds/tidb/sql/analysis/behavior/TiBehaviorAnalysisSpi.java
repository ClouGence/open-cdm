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
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBResourceRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBParserConfig;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;

public class TiBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final TiDBDslProvider      provider;
    private final TiDBResourceRegistry resources;

    public TiBehaviorAnalysisSpi(TiDBParserConfig config){
        this.provider = new TiDBDslProvider(config);
        this.resources = TiDBResourceRegistry.instance();
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new TiDBSplitAnalysisSpi(this.provider).splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return analyzeStatement(reader, levels, codeLine, codeColumn).stream();
        }).onClose(scripts::close);
    }

    List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        try (Stream<StatementBehavior> behaviors = analysisBehaviorStream(new StringReader(query), levels, baseLine, baseColumn)) {
            return behaviors.toList();
        }
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        TiBehaviorParserVisitor[] holder = new TiBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, queryReader, (lexer, parser) -> {
            holder[0] = new TiBehaviorParserVisitor(parser, provider, levels, baseLine, baseColumn, resources);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
