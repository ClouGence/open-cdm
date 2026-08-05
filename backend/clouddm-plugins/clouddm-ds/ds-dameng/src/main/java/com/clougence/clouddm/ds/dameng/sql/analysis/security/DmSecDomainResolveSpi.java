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
package com.clougence.clouddm.ds.dameng.sql.analysis.security;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.CodeInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.security.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;

public class DmSecDomainResolveSpi implements SecDomainResolveSpi {
    @SuppressWarnings("unused")
    private final MetaService metaService;

    public DmSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public Stream<RuleDomain> resolveDomainStream(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        return resolveDomainMaterialized(dsType, queryReader, codeInfo, ctxInfo).stream();
    }

    private List<RuleDomain> resolveDomainMaterialized(DataSourceType dsType, Reader queryReader, CodeInfo codeInfo, ContextInfo ctxInfo) {
        CodeLocation dslBase = new CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<AstSplitScript> scripts = DslHelper.splitDsl(DmDslProvider.INSTANCE, queryReader, dslBase);
        List<RuleDomain> domainList = new ArrayList<>();
        for (AstSplitScript script : scripts) {
            SplitScript splitScript = new SplitScript();
            splitScript.setScript(script.getScript());
            splitScript.setBodyStartCodeLine(script.getBodyStartCodeLine());
            splitScript.setBodyEndCodeLine(script.getEndCodeLine());
            splitScript.setBodyStartCodeColumn(script.getBodyStartCodeColumn());
            splitScript.setBodyEndCodeColumn(script.getEndCodeColumn());

            DmDomainCollectVisitor visitor = new DmDomainCollectVisitor();
            visitor.visit(script.getAstTree());
            for (RuleDomain domain : visitor.getDomains()) {
                domain.setDsType(dsType);
                domain.setSplitScript(splitScript);
                domainList.add(domain);
            }
        }
        return domainList;
    }
}
