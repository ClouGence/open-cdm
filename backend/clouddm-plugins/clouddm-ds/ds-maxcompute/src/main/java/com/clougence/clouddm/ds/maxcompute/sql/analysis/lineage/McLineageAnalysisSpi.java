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
package com.clougence.clouddm.ds.maxcompute.sql.analysis.lineage;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.ds.maxcompute.sql.analysis.security.McSQLParserVisitor;
import com.clougence.clouddm.ds.maxcompute.sql.analysis.security.builder.McBuilderFactory;
import com.clougence.clouddm.ds.maxcompute.sql.parser.McSqlDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;

public class McLineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public McLineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return McSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(McBuilderFactory domainBuilder, Parser parser) {
        return new McSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<ColumnLineage> analyze(String sql, LineageContext lineageContext) {
        McConfig mcConfig = (McConfig) lineageContext.getDsConfig();
        McBuilderFactory builder = new McBuilderFactory(this.metaService, mcConfig.getSchemaStyle());
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<MutableColumnLineage> columns = analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), builder.buildKeepOrigin());
        return toResultColumns(columns, null, lineageContext.getLevelsParam().get(UmiTypes.Schema).toString());
    }
}
