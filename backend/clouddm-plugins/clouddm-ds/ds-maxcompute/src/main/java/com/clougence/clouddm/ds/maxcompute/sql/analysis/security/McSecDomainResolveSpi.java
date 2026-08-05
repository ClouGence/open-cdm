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
package com.clougence.clouddm.ds.maxcompute.sql.analysis.security;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.ds.maxcompute.sql.analysis.security.builder.McBuilderFactory;
import com.clougence.clouddm.ds.maxcompute.sql.parser.McSqlDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;

public class McSecDomainResolveSpi implements SecDomainResolveSpi, McSecDomainOptionKeys {

    private final MetaService metaService;

    public McSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return McSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(McBuilderFactory domainBuilder, Parser parser) {
        return new McSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        return resolveDomainMaterialized(dsType, queryReader, codeInfo, ctxInfo).stream();
    }

    private List<RuleDomain> resolveDomainMaterialized(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        CodeLocation dslBase = new CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();
        McConfig mcConfig = (McConfig) ctxInfo.getDataSourceConfig();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), queryReader, dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            McBuilderFactory builder = new McBuilderFactory(this.metaService, mcConfig.getSchemaStyle());
            try (StringReader reader = new StringReader(s.getScript())) {
                DslHelper.doVisitor(dslProvider(), reader, (lexer, parser) -> this.parserVisitor(builder, parser));
            }
            List<RuleDomain> build = builder.build();
            for (RuleDomain domain : build) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }

        return domainList;
    }
}
