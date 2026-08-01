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
package com.clougence.clouddm.ds.starrocks.sql.analysis.security;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.starrocks.sql.analysis.security.builder.SrBuilderFactory;
import com.clougence.clouddm.ds.starrocks.sql.parser.SrDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AstSplitScript;

public class SrSecDomainResolveSpi implements SecDomainResolveSpi {

    private final MetaService metaService;

    public SrSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return SrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor parserVisitor(SrBuilderFactory domainBuilder, Parser parser) {
        return new SrSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase = //
                new com.clougence.dslpaser.ast.location.CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), queryReader, dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            SrBuilderFactory builder = new SrBuilderFactory(this.metaService);
            try (StringReader reader = new StringReader(s.getScript())) {
                DslHelper.doVisitor(dslProvider(), reader, (lexer, parser) -> this.parserVisitor(builder, parser));
            }
            List<RuleDomain> build;
            if (ctxInfo == null) {
                build = builder.build();
            } else {
                build = builder.build(ctxInfo.getCuid(), ctxInfo.getDsId(), ctxInfo.getLevelsParam());
            }
            for (RuleDomain domain : build) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }

        return domainList;
    }
}
