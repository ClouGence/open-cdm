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
package com.clougence.clouddm.ds.gauss.analysis.gsog;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsSqlParserVisitor;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.gauss.parser.GaussDslProvider;
import com.clougence.dslpaser.antlr.DslProvider;

public class GsogSelectColumnAnalysisSpi extends PgSelectColumnAnalysisSpi {

    public GsogSelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    @Override
    protected DslProvider dslProvider() {
        return GaussDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<Void> parserVisitor(PgBuilderFactory domainBuilder, Parser parser) {
        return new GsSqlParserVisitor(domainBuilder, parser);
    }
}
