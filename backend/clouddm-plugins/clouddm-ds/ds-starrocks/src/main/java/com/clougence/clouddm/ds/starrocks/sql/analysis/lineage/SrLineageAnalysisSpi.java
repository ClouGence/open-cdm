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
package com.clougence.clouddm.ds.starrocks.sql.analysis.lineage;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.starrocks.sql.analysis.security.SrSqlParserVisitor;
import com.clougence.clouddm.ds.starrocks.sql.analysis.security.builder.SrBuilderFactory;
import com.clougence.clouddm.ds.starrocks.sql.parser.SrDslProvider;
import com.clougence.clouddm.ds.starrocks.sql.parser.SrSplitAnalysisSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;

public class SrLineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public SrLineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return SrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor parserVisitor(SrBuilderFactory domainBuilder, Parser parser) {
        return new SrSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<LineageColumn> analyze(String sql, LineageContext lineageContext) {
        try (var scripts = new SrSplitAnalysisSpi().splitScriptStream(new StringReader(sql), List.of(), 1, 0)) {
            var iterator = scripts.iterator();
            if (!iterator.hasNext()) {
                return List.of();
            }
            iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalArgumentException("Lineage analysis supports at most one SQL statement");
            }
        }

        return analyzeStatement(new StringReader(sql), lineageContext);
    }

    private List<LineageColumn> analyzeStatement(Reader sql, LineageContext lineageContext) {
        SrBuilderFactory builder = new SrBuilderFactory(this.metaService);
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<MutableColumnLineage> columns = analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), builder.buildKeepOrigin());
        return toResultColumns(columns, lineageContext.getLevelsParam().get(UmiTypes.Catalog).toString(), lineageContext.getLevelsParam().get(UmiTypes.Schema).toString());
    }
}
