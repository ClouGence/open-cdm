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
package com.clougence.clouddm.ds.ads.sql.ads4my.analysis.lineage;

import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.ads.sql.ads4my.analysis.security.AdsMyParserVisitor;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.AdsMyDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageColumn;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.lineage.AbstractLineageAnalysisSpi;
import com.clougence.sql.mysql.analysis.security.builder.MyBuilderFactory;

public class AdsMyLineageAnalysisSpi extends AbstractLineageAnalysisSpi {

    public AdsMyLineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return AdsMyDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new AdsMyParserVisitor(domainBuilder, parser);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public Stream<LineageColumn> analyzeStream(Reader sql, LineageContext context) {
        return analyzeMaterialized(sql, context).stream();
    }

    private List<LineageColumn> analyzeMaterialized(Reader sql, LineageContext context) {
        MyBuilderFactory builder = new MyBuilderFactory(metaService);
        DslHelper.doVisitor(dslProvider(), sql, (lexer, parser) -> parserVisitor(builder, parser));
        List<MutableColumnLineage> columns = analyzeColumns(context.getUserUID(), context.getDsId(), context.getLevelsParam(), builder.buildKeepOrigin());
        return toResultColumns(columns, null, context.getLevelsParam().get(UmiTypes.Schema).toString());
    }
}
