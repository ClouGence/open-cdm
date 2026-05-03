
package com.clougence.clouddm.sec.rules.execute.checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sdk.service.secrules.*;
import com.clougence.clouddm.sec.rules.domain.func.FuncLoggerUtils;
import com.clougence.clouddm.sec.rules.domain.func.FuncOutParamUtils;
import com.clougence.clouddm.sec.rules.execute.AbstractCheckerSpiProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.ValueObject;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.runtime.v1.DetectRuleEngineV1;

import lombok.SneakyThrows;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
public class SecRulesCheckerServiceProvider extends AbstractCheckerSpiProvider implements SecRulesCheckerService {

    public SecRulesCheckerServiceProvider(CacheService cacheService){
        super(cacheService);
    }

    @Override
    @SneakyThrows
    public List<SecParam> getParameters(String scriptType, String script) {
        return super.parserParameters(scriptType, script);
    }

    @SneakyThrows
    @Override
    public SecResult doChecker(CheckerRule checker, CheckerData domain, CheckerOptions options) {
        // test range
        if (this.testSkipByRange(checker, domain)) {
            SecResult result = new SecResult();
            result.setSuccessful(true);
            result.setLogger(Collections.emptyList());
            return result;
        }

        // run rule
        try {
            FuncOutParamUtils.initOutParams();
            DetectRuleEngineV1 visitor = this.createEngine(domain, options);

            StatementSet statementSet = cacheOrParserDsl(checker.getScript());
            statementSet.accept(visitor);
            LangObject returnData = visitor.returnData(new ValueObject(true, TypeType.Boolean));

            SecResult result = new SecResult();
            result.setSuccessful((Boolean) returnData.unwrap());
            result.setLogger(new ArrayList<>(FuncLoggerUtils.outputLog));
            result.setOutParams(FuncOutParamUtils.getOutParams());
            return result;
        } finally {
            FuncOutParamUtils.clearOutParams();
            FuncLoggerUtils.outputLog.clear();
        }
    }
}
