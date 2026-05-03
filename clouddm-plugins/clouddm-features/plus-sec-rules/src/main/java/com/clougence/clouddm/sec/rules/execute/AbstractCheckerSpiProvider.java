package com.clougence.clouddm.sec.rules.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.secrules.*;
import com.clougence.clouddm.sec.rules.domain.CheckerDomain;
import com.clougence.detectrule.domain.ParamInfo;
import com.clougence.detectrule.lang.reflect.ReflectHelper;
import com.clougence.detectrule.lang.reflect.Type;
import com.clougence.detectrule.parser.DetectRuleHelper;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.detectrule.runtime.DefaultDataTimeValueParser;
import com.clougence.detectrule.runtime.EngineOption;
import com.clougence.detectrule.runtime.v1.DetectRuleEngineV1;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.codec.MD5;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public class AbstractCheckerSpiProvider {

    private final EngineOption engineOption;
    private final CacheService cacheService;

    public AbstractCheckerSpiProvider(CacheService cacheService){
        this.cacheService = cacheService;
        this.engineOption = new EngineOption();
        this.engineOption.setDataTimeValueParser(new DefaultDataTimeValueParser());
    }

    protected StatementSet cacheOrParserDsl(String script) throws Exception {
        return (StatementSet) this.cacheService.getObjectIfAbsent("detectrule-script-" + MD5.getMD5(script), s -> {
            return DslHelper.parserDsl("DetectRule", script);
        });
    }

    protected Type cacheOrParserType(Class<?> domainType) throws Exception {
        return (Type) this.cacheService.getObjectIfAbsent("detectrule-domain-" + domainType.getName(), s -> {
            return ReflectHelper.resolveDomain(domainType);
        });
    }

    protected List<SecParam> parserParameters(String scriptType, String script) throws Exception {
        if (StringUtils.equalsIgnoreCase(scriptType, "DetectRules")) {
            List<SecParam> params = new ArrayList<>();
            StatementSet statementSet = cacheOrParserDsl(script);
            List<ParamInfo> infos = DetectRuleHelper.extractParameters((StatementList) statementSet);
            for (int i = 0; i < infos.size(); i++) {
                ParamInfo p = infos.get(i);
                if (StringUtils.isBlank(p.getType())) {
                    p.setType("string");
                }
                if (StringUtils.isBlank(p.getHint())) {
                    p.setHint("the arg" + i);
                }

                SecParam secp = new SecParam();
                secp.setName(p.getName());
                secp.setType(p.getType());
                secp.setDefaultValue(p.getDefaultValue());
                secp.setRange(p.getEnums());
                secp.setHint(p.getHint());
                params.add(secp);
            }

            return params;
        } else {
            // TODO need plugin load jar, the content should be a json data to describe more information.
            throw new UnsupportedOperationException("need plugin load jar, Unsupported.");
        }
    }

    protected boolean testSkipByRange(CheckerRule checker, CheckerData domain) {
        // test range
        boolean skipByRange = CollectionUtils.isNotEmpty(checker.getRangeList());
        if (skipByRange) {
            for (CheckerRange range : checker.getRangeList()) {
                if (Utils.checkRangeIncludeDomain(range, domain)) {
                    skipByRange = false;
                    break;
                }
            }
        }
        return skipByRange;
    }

    protected DetectRuleEngineV1 createEngine(CheckerData domain, CheckerOptions options) throws Exception {
        RuleDomain domainData = Objects.requireNonNull(domain.getDomain(), "domain is null.");

        CheckerDomain checkerDomain = DomainHelper.create(domainData);
        Type checkerType = cacheOrParserType(checkerDomain.getClass());

        DetectRuleEngineV1 engine = new DetectRuleEngineV1(checkerDomain, checkerType, this.engineOption);
        engine.putVariables(options.getParameters());

        return engine;
    }
}
