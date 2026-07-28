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
package com.clougence.sql.doris.analysis.lineage;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;
import com.clougence.sql.doris.analysis.security.DrSqlParserVisitor;
import com.clougence.sql.doris.analysis.security.builder.DrBuilderFactory;
import com.clougence.sql.doris.parser.DrDslProvider;

public class DrLineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public DrLineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return DrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(DrBuilderFactory domainBuilder, Parser parser) {
        return new DrSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<ColumnLineage> analyze(String sql, LineageContext lineageContext) {
        DrBuilderFactory builder = new DrBuilderFactory(this.metaService);
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<MutableColumnLineage> columns = analyzeColumns(lineageContext.getUserUID(), lineageContext.getDsId(), lineageContext.getLevelsParam(), builder.buildKeepOrigin());
        return toResultColumns(columns, lineageContext.getLevelsParam().get(UmiTypes.Catalog).toString(), lineageContext.getLevelsParam().get(UmiTypes.Schema).toString());
    }
}
