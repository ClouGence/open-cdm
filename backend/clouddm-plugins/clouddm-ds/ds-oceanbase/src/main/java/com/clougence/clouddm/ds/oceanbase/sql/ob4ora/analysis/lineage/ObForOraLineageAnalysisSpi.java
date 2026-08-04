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
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.lineage;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.security.ObForOracleSqlParserVisitor;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.parser.ObOraDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.sql.oracle.analysis.lineage.OraLineageAnalysisSpi;
import com.clougence.sql.oracle.analysis.security.builder.OraBuilderFactory;

public class ObForOraLineageAnalysisSpi extends OraLineageAnalysisSpi {

    public ObForOraLineageAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return ObOraDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(OraBuilderFactory domainBuilder, Parser parser) {
        return new ObForOracleSqlParserVisitor(domainBuilder, parser);
    }
}
