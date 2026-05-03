package com.clougence.clouddm.ds.doris.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.doris.analysis.builder.DrBuilderFactory;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.doris.parser.DrDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrSecDomainResolveSpi implements SecDomainResolveSpi {

    private final MetaService metaService;

    public DrSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return DrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(DrBuilderFactory domainBuilder, Parser parser) {
        return new DrSqlParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        CodeLocation dslBase = //
                new CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), codeInfo.getQuery(), dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            DrBuilderFactory builder = new DrBuilderFactory(this.metaService);
            DslHelper.doVisitor(dslProvider(), s.getScript(), (lexer, parser) -> this.parserVisitor(builder, parser));
            List<RuleDomain> build;
            if (ctxInfo == null || !ctxInfo.isDeepParser()) {
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
