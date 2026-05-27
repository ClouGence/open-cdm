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
package com.clougence.clouddm.console.web.component.detectrule.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecCheckerRules;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SecRuleDal;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.secrule.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.service.secrules.CheckerRange;
import com.clougence.clouddm.sdk.service.secrules.CheckerRule;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
import com.clougence.clouddm.sdk.service.secrules.SecRulesCheckerService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Service
public class SecRulesServiceImpl implements SecRulesService, UnifiedPostConstruct {

    @Resource
    private DataSourceDal                dsDal;
    @Resource
    private SecRuleDal                   secRuleDal;
    @Resource
    private ObjectCacheDao               objectCacheDao;
    @Resource
    private DmEnvParamService            dmEnvParamService;

    private Map<String, SecCheckerRules> checkerRuleCache;
    private SecRulesCheckerService       checkerSpiCache;

    @Override
    public void init() throws Exception {
        this.checkerRuleCache = new ConcurrentHashMap<>();
        ThreadUtils.runDaemonThread(() -> {
            Thread.currentThread().setName("SecRulesEngine-cache-cleanup");
            while (true) {
                checkerRuleCache.clear();
                ThreadUtils.sleep(60 * 1000);
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public SecRulesCheckerService checkerSpi() {
        if (this.checkerSpiCache == null) {
            try {
                this.checkerSpiCache = PluginManager.findService(SecRulesCheckerService.class);
            } catch (UnsupportedOperationException e) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_UNSUPPORTED_MESSAGE.name()));
            }
        }
        return this.checkerSpiCache;
    }

    @Override
    public void cleanCache(String checkerName) {

    }

    @Override
    public SecCheckerRules fetchCheckerRulesByDsId(long dsId) {
        DsCacheEntry dsCache = this.objectCacheDao.queryByDsId(dsId);

        DataSourceType dsType = dsCache.getDsType();
        String cacheKey = dsCache.getOwnerUid() + "-" + dsId + "-" + dsType;
        //return this.checkerRuleCache.computeIfAbsent(cacheKey, s -> {
        return this.resolveCheckerRules(dsCache.getOwnerUid(), dsId, dsType);
        //});
    }

    @Override
    public SecCheckerRules fetchCheckerRules(String ownerUid, long dsId) {
        DsCacheEntry dsCache = this.objectCacheDao.queryByDsId(dsId);

        DataSourceType dsType = dsCache.getDsType();
        String cacheKey = ownerUid + "-" + dsId + "-" + dsType;
        //return this.checkerRuleCache.computeIfAbsent(cacheKey, s -> {
        return this.resolveCheckerRules(ownerUid, dsId, dsType);
        //});
    }

    private SecCheckerRules resolveCheckerRules(String ownerUid, long dsId, DataSourceType dsType) {
        DmDsConfigDO dmDsConfigDO = this.dsDal.configMapper().queryById(ownerUid, dsId);
        long envId = dmDsConfigDO.getBindEnvId();

        String usingSpec = this.dmEnvParamService.queryParam(ownerUid, envId, EnvParamKeys.DM_BIND_CHECK_SPEC);
        if (StringUtils.isBlank(usingSpec) || !NumberUtils.isNumber(usingSpec)) {
            return new SecCheckerRules();
        }

        long specId = Long.parseLong(usingSpec);
        DmSecSpecDO specDO = this.secRuleDal.specMapper().queryByIdAndUid(ownerUid, specId);
        if (specDO == null || !specDO.isEnable()) {
            return new SecCheckerRules();
        }

        // fetch all rule of ds.
        Collection<SecRefererWrap> refererWraps = resolveSecRefererList(ownerUid, specId);
        if (refererWraps.isEmpty()) {
            return new SecCheckerRules();
        }

        Map<Long, SecRefererWrap> ruleOfQuery = new HashMap<>();
        Map<Long, SecRefererWrap> ruleOfSen = new HashMap<>();
        refererWraps.forEach(r -> {
            if (r.getReferer().getRefRuleKind() == RuleKind.QUERY) {
                ruleOfQuery.put(r.getReferer().getRefRule(), r);
            } else if (r.getReferer().getRefRuleKind() == RuleKind.SENSITIVE) {
                ruleOfSen.put(r.getReferer().getRefRule(), r);
            }
        });

        // result
        List<CheckerRule> resultOfQuery = this.convertQueryRulesToCheckerRule(ownerUid, dsType, ruleOfQuery);
        List<CheckerRule> resultOfSen = this.convertSenRulesToCheckerRule(ownerUid, ruleOfSen);
        DsCacheEntry dsCache = this.objectCacheDao.queryByDsId(dsId);
        return new SecCheckerRules(envId, dsId, dsCache.getDsInstId(), dsType, specDO.getName(), resultOfQuery, resultOfSen);
    }

    private Collection<SecRefererWrap> resolveSecRefererList(String ownerUid, long specId) {
        Map<Long, SecRefererWrap> groupBy = new HashMap<>();
        List<DmSecRefererDO> refererList = this.secRuleDal.refererMapper().listBySpecId(ownerUid, specId);
        for (DmSecRefererDO referer : refererList) {
            if (referer.isEnable()) {
                groupBy.put(referer.getId(), new SecRefererWrap(referer));
            }
        }

        if (groupBy.isEmpty()) {
            return Collections.emptyList();
        }

        List<DmSecRangeDO> range = this.secRuleDal.rangeMapper().queryListBySpecId(ownerUid, specId);
        for (DmSecRangeDO rangeDO : range) {
            SecRefererWrap refererWrap = groupBy.get(rangeDO.getRefId());
            if (refererWrap != null) {
                refererWrap.getRangeList().add(rangeDO);
            }
        }

        return groupBy.values();
    }

    private List<CheckerRule> convertQueryRulesToCheckerRule(String ownerUid, DataSourceType dsType, Map<Long, SecRefererWrap> refMap) {
        if (refMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<DmSecRuleDO> queryRules = this.secRuleDal.rulesMapper().queryByIds(ownerUid, refMap.keySet());
        return queryRules.stream().filter(r -> {
            return r.getRuleDsRange().contains(dsType);
        }).map(r -> {
            SecRefererWrap refererWrap = refMap.get(r.getId());
            DmSecRefererDO referer = refererWrap.getReferer();

            CheckerRule rule = new CheckerRule();
            rule.setRuleName(r.getName());
            rule.setRuleDesc(r.getDescription());
            rule.setParameters(extractParameter(r.getScriptType(), r.getScriptContent(), referer.getRuleParam()));
            rule.setScriptType(r.getScriptType().name());
            rule.setScript(r.getScriptContent());
            rule.setRangeList(convertSecRangeDOToCheckerRule(refererWrap.getRangeList()));

            rule.setLevel(referer.getWarnLevel().getRuleLevel());
            if (r.getRuleTarget() != null) {
                rule.setTarget(r.getRuleTarget().getType());
            } else {
                rule.setTarget(null);
            }
            return rule;
        }).collect(Collectors.toList());
    }

    private List<CheckerRule> convertSenRulesToCheckerRule(String ownerUid, Map<Long, SecRefererWrap> refMap) {
        if (refMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<DmSecSensitiveDO> queryRules = this.secRuleDal.sensitiveMapper().queryByIds(ownerUid, refMap.keySet());
        return queryRules.stream().map(r -> {
            SecRefererWrap refererWrap = refMap.get(r.getId());
            DmSecRefererDO referer = refererWrap.getReferer();

            CheckerRule rule = new CheckerRule();
            rule.setRuleName(r.getName());
            rule.setRuleDesc(r.getDescription());
            rule.setParameters(extractParameter(r.getScriptType(), r.getScriptContent(), referer.getRuleParam()));
            rule.setScriptType(r.getScriptType().name());
            rule.setScript(r.getScriptContent());
            rule.setRangeList(convertSecRangeDOToCheckerRule(refererWrap.getRangeList()));

            RuleSensitiveMode senMode = refererWrap.getReferer().getSenMode();
            if (senMode != null) {
                rule.setSenMode(senMode.getSenMode());
            } else {
                rule.setSenMode(r.getSenMode().getSenMode());
            }
            return rule;
        }).collect(Collectors.toList());
    }

    private Map<String, String> extractParameter(RuleScriptType scriptType, String scriptContent, Map<String, String> userParam) {
        List<SecParam> parameters = this.checkerSpi().getParameters(scriptType.name(), scriptContent);
        Map<String, String> paramMap = new HashMap<>();
        if (parameters != null) {
            for (SecParam secParam : parameters) {
                paramMap.put(secParam.getName(), secParam.getDefaultValue());
            }
        }

        if (userParam != null) {
            paramMap.putAll(userParam);
        }
        return paramMap;
    }

    private List<CheckerRange> convertSecRangeDOToCheckerRule(List<DmSecRangeDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        } else {
            return list.stream().map(DmConvertUtils::convertToCheckerRange).collect(Collectors.toList());
        }
    }
}
