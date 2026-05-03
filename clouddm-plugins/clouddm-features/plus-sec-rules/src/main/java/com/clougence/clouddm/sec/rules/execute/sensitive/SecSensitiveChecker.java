package com.clougence.clouddm.sec.rules.execute.sensitive;

import java.util.ArrayList;
import java.util.Collections;

import com.clougence.clouddm.sdk.service.secrules.CheckerData;
import com.clougence.clouddm.sdk.service.secrules.CheckerOptions;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;
import com.clougence.clouddm.sdk.service.secrules.SecResult;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.clouddm.sec.rules.domain.func.FuncLoggerUtils;
import com.clougence.clouddm.sec.rules.execute.AbstractCheckerSpiProvider;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.detectrule.lang.LangObject;
import com.clougence.detectrule.lang.ValueObject;
import com.clougence.detectrule.lang.reflect.TypeType;
import com.clougence.detectrule.runtime.v1.DetectRuleEngineV1;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Slf4j
public class SecSensitiveChecker extends AbstractCheckerSpiProvider {

    public SecSensitiveChecker(CacheService cacheService){
        super(cacheService);
    }

    @SneakyThrows
    public SecResult doChecker(CheckerRule checker, CheckerData domain, CheckerOptions options) {
        // test range
        if (this.testSkipByRange(checker, domain)) {
            SecResult result = new SecResult();
            result.setSuccessful(true);
            result.setResult(SenAlgorithm.SEN_ORIGINAL);
            result.setLogger(Collections.emptyList());
            return result;
        }

        // run rule
        try {
            DetectRuleEngineV1 visitor = this.createEngine(domain, options);

            StatementSet statementSet = cacheOrParserDsl(checker.getScript());
            statementSet.accept(visitor);
            LangObject returnData = visitor.returnData(new ValueObject(SenAlgorithm.SEN_ORIGINAL, TypeType.String));

            SecResult result = new SecResult();
            result.setSuccessful(true);
            result.setResult(returnData.getType() == TypeType.Null ? SenAlgorithm.SEN_ORIGINAL : returnData.unwrap());
            result.setLogger(new ArrayList<>(FuncLoggerUtils.outputLog));
            return result;
        } catch (RuntimeException e) {
            log.error("secSensitive rule '" + checker.getRuleName() + "' failed : " + e.getMessage(), e);
            throw e;
        } finally {
            FuncLoggerUtils.outputLog.clear();
        }
    }
}
