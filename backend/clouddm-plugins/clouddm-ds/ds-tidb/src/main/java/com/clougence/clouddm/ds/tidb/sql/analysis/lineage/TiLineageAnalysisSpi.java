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
package com.clougence.clouddm.ds.tidb.sql.analysis.lineage;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.tidb.sql.analysis.security.TiParserVisitor;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;
import com.clougence.sql.mysql.analysis.security.builder.MyBuilderFactory;

public class TiLineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    private final TiDBDslProvider provider;

    public TiLineageAnalysisSpi(MetaService metaService, TiDBDslProvider provider){
        super(metaService);
        this.provider = provider;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new TiParserVisitor(domainBuilder, parser);
    }

    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<LineageColumn> analyze(String sql, LineageContext context) {
        MyBuilderFactory builder = new MyBuilderFactory(metaService);
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> parserVisitor(builder, parser));
        List<MutableColumnLineage> columns = analyzeColumns(context.getUserUID(), context.getDsId(), context.getLevelsParam(), builder.buildKeepOrigin());
        return toResultColumns(columns, null, context.getLevelsParam().get(UmiTypes.Schema).toString());
    }
}
