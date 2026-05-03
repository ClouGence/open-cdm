package com.clougence.clouddm.ds.redis.analysis;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.ds.redis.analysis.builder.RedisBuilderFactory;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.redis.parser.RedisDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class RedisSecDomainResolveSpi implements SecDomainResolveSpi {

    private final MetaService metaService;

    public RedisSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return RedisDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(RedisBuilderFactory domainBuilder, Parser parser) {
        return new RedisParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase = //
                new com.clougence.dslpaser.ast.location.CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
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
            RedisBuilderFactory builder = new RedisBuilderFactory(this.metaService);
            DslHelper.doVisitor(dslProvider(), s.getScript(), (lexer, parser) -> this.parserVisitor(builder, parser));
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
