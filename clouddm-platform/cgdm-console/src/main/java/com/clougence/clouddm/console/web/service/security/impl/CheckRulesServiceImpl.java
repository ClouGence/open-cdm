package com.clougence.clouddm.console.web.service.security.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.detectrule.SecRangeVerify;
import com.clougence.clouddm.console.web.component.detectrule.domain.SecRange;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.*;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.model.fo.checkrules.RangeDeleteFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.RuleSaveFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecRulesFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecSaveRangeFO;
import com.clougence.clouddm.console.web.model.vo.checkrules.I18nKeyVal;
import com.clougence.clouddm.console.web.model.vo.checkrules.QueryRuleDef;
import com.clougence.clouddm.console.web.model.vo.checkrules.SecSettingDef;
import com.clougence.clouddm.console.web.model.vo.checkrules.SensitiveRuleDef;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleConfig;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleMO;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.secrules.SecRulesSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
import com.clougence.clouddm.sdk.service.secrules.SecRulesCheckerService;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.codec.MD5;

import lombok.SneakyThrows;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Service
public class CheckRulesServiceImpl implements CheckRulesService, UnifiedPostConstruct {

    @Resource
    private DmSecSpecMapper                      dmSecSpecMapper;
    @Resource
    private DmSecRefererMapper                   dmSecRefererMapper;
    @Resource
    private DmSecRulesMapper                     dmSecRulesMapper;
    @Resource
    private DmSecSensitiveMapper                 dmSecSensitiveMapper;
    @Resource
    private DmSecRangeMapper                     dmSecRangeMapper;
    @Resource
    private NamingService                        namingService;
    private Map<DataSourceType, DmSecRuleConfig> ruleSupportDsTypes;
    private SecSettingDef                        ruleSettingDef;

    @Override
    public void init() throws Exception {
        this.ruleSupportDsTypes = new HashMap<>();
        for (DataSourceType dsType : DataSourceType.values()) {
            SecRulesSupportSpi supportSpi = null;
            boolean support;
            try {
                supportSpi = PluginManager.findSecRulesSupportSpi(dsType);
                support = supportSpi.isSupport();
            } catch (Exception e) {
                support = false;
            }
            if (support) {
                DmSecRuleConfig ruleConf = new DmSecRuleConfig();
                ruleConf.setRuleTargets(supportSpi.supportModel());
                ruleConf.setQueryRangeExactTargets(supportSpi.exactRangeForQuery());
                ruleConf.setQueryRangePrefixTargets(supportSpi.prefixRangeForQuery());
                ruleConf.setQueryRangeSuffixTargets(supportSpi.suffixRangeForQuery());
                ruleConf.setQueryRangeIncludeTargets(supportSpi.includeRangeForQuery());
                ruleConf.setSenRangeExactTargets(supportSpi.exactRangeForSen());
                ruleConf.setSenRangePrefixTargets(supportSpi.prefixRangeForSen());
                ruleConf.setSenRangeSuffixTargets(supportSpi.suffixRangeForSen());
                ruleConf.setSenRangeIncludeTargets(supportSpi.includeRangeForSen());
                this.ruleSupportDsTypes.put(dsType, ruleConf);
            }
        }

        this.ruleSettingDef = new SecSettingDef();
        this.ruleSettingDef.setQueryConf(this.createQueryConf());
        this.ruleSettingDef.setSenConf(this.createSensitiveRuleDef());
    }

    @Override
    public void stop() {

    }

    private static void initPatternMatchForQuery(SecMatchMode matchMode, QueryRuleDef def, DataSourceType dsType, List<TargetType> supported) {
        boolean preSetChoose = matchMode == SecMatchMode.EXACT;

        //
        boolean has = false;
        for (I18nKeyVal i18n : def.getMatchMode()) {
            has = has | i18n.getName().equals(matchMode.name());
        }
        if (!has) {
            I18nKeyVal val = new I18nKeyVal(matchMode.name(), DmI18nUtils.getMessage(matchMode.getI18nKey()), preSetChoose);
            val.setChildren(new ArrayList<>());
            if (matchMode == SecMatchMode.EXACT) {
                val.getChildren().add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false, false));
                val.getChildren().add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false, false));
            } else {
                val.getChildren().add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false, true));
                val.getChildren().add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false, true));
            }
            val.getChildren().add(new I18nKeyVal(SecRangeType.Catalog.name(), DmI18nUtils.getMessage(SecRangeType.Catalog.getI18nKey()), false, false));
            val.getChildren().add(new I18nKeyVal(SecRangeType.Schema.name(), DmI18nUtils.getMessage(SecRangeType.Schema.getI18nKey()), false, false));
            val.getChildren().add(new I18nKeyVal(SecRangeType.TableOrView.name(), DmI18nUtils.getMessage(SecRangeType.TableOrView.getI18nKey()), false, false));
            //val.getChildren().add(new I18nKeyVal(SecRangeType.Column.name(), DmI18nUtils.getMessage(SecRangeType.Column.getI18nKey()), false, false));
            def.getMatchMode().add(val);
        }

        //
        List<I18nKeyVal> exactMatch = new ArrayList<>();
        exactMatch.add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false));
        exactMatch.add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false));
        exactMatch.addAll(supported.stream().map(SecRangeType::ofTarget).filter(Objects::nonNull).distinct().map(rt -> {
            I18nKeyVal i18n = new I18nKeyVal(rt.name(), DmI18nUtils.getMessage(rt.getI18nKey()), false);
            if (rt == SecRangeType.TableOrView) {
                i18n.setChildren(new ArrayList<>());
                if (supported.contains(TargetType.Table)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.Table.name(), DmI18nUtils.getMessage(RuleTarget.Table.getI18nKey()), false));
                }
                if (supported.contains(TargetType.View)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.View.name(), DmI18nUtils.getMessage(RuleTarget.View.getI18nKey()), false));
                }
                if (supported.contains(TargetType.Materialized)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.Materialized.name(), DmI18nUtils.getMessage(RuleTarget.Materialized.getI18nKey()), false));
                }
            }
            return i18n;
        }).collect(Collectors.toList()));

        def.getScopeByMatchMode().computeIfAbsent(matchMode, m -> new HashMap<>()).put(dsType, exactMatch);
    }

    private static void initPatternMatchForSensitive(SecMatchMode matchMode, SensitiveRuleDef def, DataSourceType dsType, List<TargetType> supported) {
        boolean preSetChoose = matchMode == SecMatchMode.EXACT;

        //
        boolean has = false;
        for (I18nKeyVal i18n : def.getMatchMode()) {
            has = has | i18n.getName().equals(matchMode.name());
        }
        if (!has) {
            I18nKeyVal val = new I18nKeyVal(matchMode.name(), DmI18nUtils.getMessage(matchMode.getI18nKey()), preSetChoose);
            val.setChildren(new ArrayList<>());
            if (matchMode == SecMatchMode.EXACT) {
                val.getChildren().add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false, false));
                val.getChildren().add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false, false));
            } else {
                val.getChildren().add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false, true));
                val.getChildren().add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false, true));
            }
            val.getChildren().add(new I18nKeyVal(SecRangeType.Catalog.name(), DmI18nUtils.getMessage(SecRangeType.Catalog.getI18nKey()), false, false));
            val.getChildren().add(new I18nKeyVal(SecRangeType.Schema.name(), DmI18nUtils.getMessage(SecRangeType.Schema.getI18nKey()), false, false));
            val.getChildren().add(new I18nKeyVal(SecRangeType.TableOrView.name(), DmI18nUtils.getMessage(SecRangeType.TableOrView.getI18nKey()), false, false));
            val.getChildren().add(new I18nKeyVal(SecRangeType.Column.name(), DmI18nUtils.getMessage(SecRangeType.Column.getI18nKey()), false, false));
            def.getMatchMode().add(val);
        }

        //
        List<I18nKeyVal> exactMatch = new ArrayList<>();
        exactMatch.add(new I18nKeyVal(SecRangeType.Environment.name(), DmI18nUtils.getMessage(SecRangeType.Environment.getI18nKey()), false));
        exactMatch.add(new I18nKeyVal(SecRangeType.Instance.name(), DmI18nUtils.getMessage(SecRangeType.Instance.getI18nKey()), false));
        exactMatch.addAll(supported.stream().map(SecRangeType::ofTarget).filter(Objects::nonNull).distinct().map(rt -> {
            I18nKeyVal i18n = new I18nKeyVal(rt.name(), DmI18nUtils.getMessage(rt.getI18nKey()), false);
            if (rt == SecRangeType.TableOrView) {
                i18n.setChildren(new ArrayList<>());
                if (supported.contains(TargetType.Table)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.Table.name(), DmI18nUtils.getMessage(RuleTarget.Table.getI18nKey()), false));
                }
                if (supported.contains(TargetType.View)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.View.name(), DmI18nUtils.getMessage(RuleTarget.View.getI18nKey()), false));
                }
                if (supported.contains(TargetType.Materialized)) {
                    i18n.getChildren().add(new I18nKeyVal(RuleTarget.Materialized.name(), DmI18nUtils.getMessage(RuleTarget.Materialized.getI18nKey()), false));
                }
            }
            return i18n;
        }).collect(Collectors.toList()));

        def.getScopeByMatchMode().computeIfAbsent(matchMode, m -> new HashMap<>()).put(dsType, exactMatch);
    }

    private QueryRuleDef createQueryConf() {
        QueryRuleDef def = new QueryRuleDef();
        def.setSupportDs(new ArrayList<>());
        def.setTargets(new HashMap<>());
        def.setMatchMode(new ArrayList<>());
        def.setScopeByMatchMode(new HashMap<>());

        // by ds
        this.ruleSupportDsTypes.forEach((dsType, ruleConf) -> {
            DmSecRuleConfig ruleConfig = this.ruleSupportDsTypes.get(dsType);

            // supportRule
            def.getSupportDs().add(new I18nKeyVal(dsType.name(), dsType.name()));

            // targets
            List<I18nKeyVal> targets = ruleConfig.getRuleTargets().stream().sorted().map(targetType -> {
                RuleTarget ruleTarget = RuleTarget.ofTarget(targetType);
                if (ruleTarget == RuleTarget.Query) {
                    return new I18nKeyVal(ruleTarget.getType().name(), DmI18nUtils.getMessage(ruleTarget.getI18nKey()), true);
                } else {
                    return new I18nKeyVal(ruleTarget.getType().name(), DmI18nUtils.getMessage(ruleTarget.getI18nKey()));
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            def.getTargets().put(dsType, targets);

            // match
            initPatternMatchForQuery(SecMatchMode.EXACT, def, dsType, ruleConfig.getQueryRangeExactTargets());
            initPatternMatchForQuery(SecMatchMode.PREFIX, def, dsType, ruleConfig.getQueryRangePrefixTargets());
            initPatternMatchForQuery(SecMatchMode.SUFFIX, def, dsType, ruleConfig.getQueryRangeSuffixTargets());
            initPatternMatchForQuery(SecMatchMode.INCLUDE, def, dsType, ruleConfig.getQueryRangeIncludeTargets());
        });
        return def;
    }

    private SensitiveRuleDef createSensitiveRuleDef() {
        SensitiveRuleDef def = new SensitiveRuleDef();
        def.setMatchMode(new ArrayList<>());
        def.setScopeByMatchMode(new HashMap<>());

        // senMode
        List<I18nKeyVal> senMode = new ArrayList<>();
        senMode.add(new I18nKeyVal(RuleSensitiveMode.VALUE.name(), DmI18nUtils.getMessage(RuleSensitiveMode.VALUE.getI18nKey()), true));
        senMode.add(new I18nKeyVal(RuleSensitiveMode.ROW.name(), DmI18nUtils.getMessage(RuleSensitiveMode.ROW.getI18nKey())));
        def.setSenMode(senMode);

        // by ds
        this.ruleSupportDsTypes.forEach((dsType, ruleConf) -> {
            DmSecRuleConfig ruleConfig = this.ruleSupportDsTypes.get(dsType);
            initPatternMatchForSensitive(SecMatchMode.EXACT, def, dsType, ruleConfig.getSenRangeExactTargets());
            initPatternMatchForSensitive(SecMatchMode.PREFIX, def, dsType, ruleConfig.getSenRangePrefixTargets());
            initPatternMatchForSensitive(SecMatchMode.SUFFIX, def, dsType, ruleConfig.getSenRangeSuffixTargets());
            initPatternMatchForSensitive(SecMatchMode.INCLUDE, def, dsType, ruleConfig.getSenRangeIncludeTargets());
        });
        return def;
    }

    @Override
    public SecSettingDef ruleSettingDef() {
        return this.ruleSettingDef;
    }

    @Override
    public List<DmSecSpecDO> querySpecList(String ownerUid, String search) {
        //Page<?> page = PageUtil.startPage(pageInfo);
        search = StringUtils.isBlank(search) ? null : search.trim();
        return this.dmSecSpecMapper.searchSpec(ownerUid, search);
    }

    @Override
    public DmSecSpecDO querySpecById(String ownerUid, long specId) {
        return this.dmSecSpecMapper.queryByIdAndUid(ownerUid, specId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DmSecSpecDO createSpec(String ownerUid, String specName, String specDesc, boolean initSpec) {
        DmSecSpecDO specDO = new DmSecSpecDO();
        specDO.setOwnerUid(ownerUid);
        specDO.setName(specName);
        specDO.setDescription(specDesc);
        specDO.setEnable(true);

        this.dmSecSpecMapper.insert(specDO);

        if (initSpec) {
            List<DmSecRuleDO> ruleList = this.listQueryRuleByUid(ownerUid);
            for (DmSecRuleDO r : ruleList) {
                if (r.isDeprecated()) {
                    continue;
                }

                DmSecRefererDO refDO = new DmSecRefererDO();
                refDO.setOwnerUid(ownerUid);
                refDO.setRefSpec(specDO.getId());
                refDO.setRefRule(r.getId());
                refDO.setRefRuleKind(RuleKind.QUERY);
                refDO.setEnable(false);
                refDO.setWarnLevel(WarnLevel.FAILURE);
                refDO.setRuleParam(new HashMap<>());
                refDO.setRefMD5(r.getScriptMD5());

                if (StringUtils.isNotBlank(r.getScriptDef())) {
                    for (SecParam paramVO : DmConvertUtils.jsonConvertToSecRuleParamVO(r.getScriptDef())) {
                        refDO.getRuleParam().put(paramVO.getName(), paramVO.getDefaultValue());
                    }
                }
                this.dmSecRefererMapper.insert(refDO);
            }
        }

        return specDO;
    }

    @Override
    public void updateSpec(String ownerUid, long specId, String newName, String newDesc) {
        this.dmSecSpecMapper.updateInfo(ownerUid, specId, newName, newDesc);
    }

    @Override
    public void configStatus(String ownerUid, long specId, boolean enable) {
        if (enable) {
            this.dmSecSpecMapper.enableSpec(ownerUid, specId);
        } else {
            this.dmSecSpecMapper.disableSpec(ownerUid, specId);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSpec(String ownerUid, long specId) {
        this.dmSecRangeMapper.deleteBySpecId(ownerUid, specId);
        this.dmSecRefererMapper.deleteBySpecId(ownerUid, specId);
        this.dmSecSpecMapper.deleteByUidAndId(ownerUid, specId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveSpecRules(String ownerUid, long specId, List<SpecRulesFO> rules) {
        List<Long> queryRuleIds = new ArrayList<>();
        List<Long> senRuleIds = new ArrayList<>();
        rules.forEach(fo -> {
            if (fo.getRuleKind() == RuleKind.QUERY) {
                queryRuleIds.add(fo.getRuleId());
            } else if (fo.getRuleKind() == RuleKind.SENSITIVE) {
                senRuleIds.add(fo.getRuleId());
            }
        });

        // group by info
        List<DmSecRuleDO> queryRules = queryRuleIds.isEmpty() ? Collections.emptyList() : this.dmSecRulesMapper.queryByIds(ownerUid, queryRuleIds);
        List<DmSecSensitiveDO> senRules = senRuleIds.isEmpty() ? Collections.emptyList() : this.dmSecSensitiveMapper.queryByIds(ownerUid, senRuleIds);
        Map<Long, DmSecRuleDO> queryRuleGroupBy = new HashMap<>();
        Map<Long, DmSecSensitiveDO> senRuleGroupBy = new HashMap<>();
        queryRules.forEach(r -> queryRuleGroupBy.put(r.getId(), r));
        senRules.forEach(r -> senRuleGroupBy.put(r.getId(), r));

        // pre save
        List<DmSecRefererDO> collect = rules.stream().map(fo -> {
            DmSecRefererDO refDO = new DmSecRefererDO();
            refDO.setOwnerUid(ownerUid);
            refDO.setRefSpec(specId);
            refDO.setRefRule(fo.getRuleId());
            refDO.setRefRuleKind(fo.getRuleKind());
            refDO.setEnable(fo.isEnable());
            refDO.setRuleParam(fo.getRuleParam());

            if (fo.getRuleKind() == RuleKind.QUERY) {
                refDO.setWarnLevel(fo.getWarnLevel());
                refDO.setRefMD5(queryRuleGroupBy.get(fo.getRuleId()).getScriptMD5());
            } else if (fo.getRuleKind() == RuleKind.SENSITIVE) {
                refDO.setWarnLevel(WarnLevel.FAILURE);
                refDO.setSenMode(Objects.requireNonNull(fo.getSenMode(), "arg senMode is null."));
                refDO.setRefMD5(senRuleGroupBy.get(fo.getRuleId()).getScriptMD5());
            }

            return refDO;
        }).collect(Collectors.toList());

        for (DmSecRefererDO refererDO : collect) {
            DmSecRefererDO refDO = this.dmSecRefererMapper.queryBySpecAndRuleOrSen(ownerUid, refererDO.getRefSpec(), refererDO.getRefRule(), refererDO.getRefRuleKind());
            if (refDO == null) {
                this.dmSecRefererMapper.insert(refererDO);
            } else {
                this.dmSecRefererMapper.updateRuleReferer(ownerUid, refDO.getId(), refererDO);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSpecRules(String ownerUid, long specId, List<SpecRulesFO> rules) {
        for (SpecRulesFO r : rules) {
            this.dmSecRangeMapper.deleteByRefId(ownerUid, r.getRefId());
            this.dmSecRefererMapper.deleteById(ownerUid, r.getRuleId());
        }
    }

    @Override
    public boolean isSupportRangeType(SecRangeType rangeType, SecMatchMode matchType, RuleKind ruleKind, DataSourceType dsType) {
        if (rangeType == SecRangeType.Environment || rangeType == SecRangeType.Instance) {
            return true;
        }

        List<TargetType> testData;
        switch (ruleKind) {
            case QUERY:
                switch (matchType) {
                    case EXACT:
                        testData = this.ruleSupportDsTypes.get(dsType).getQueryRangeExactTargets();
                        break;
                    case PREFIX:
                        testData = this.ruleSupportDsTypes.get(dsType).getQueryRangePrefixTargets();
                        break;
                    case SUFFIX:
                        testData = this.ruleSupportDsTypes.get(dsType).getQueryRangeSuffixTargets();
                        break;
                    case INCLUDE:
                        testData = this.ruleSupportDsTypes.get(dsType).getQueryRangeIncludeTargets();
                        break;
                    default:
                        return true;
                }
                break;
            case SENSITIVE:
                switch (matchType) {
                    case EXACT:
                        testData = this.ruleSupportDsTypes.get(dsType).getSenRangeExactTargets();
                        break;
                    case PREFIX:
                        testData = this.ruleSupportDsTypes.get(dsType).getSenRangePrefixTargets();
                        break;
                    case SUFFIX:
                        testData = this.ruleSupportDsTypes.get(dsType).getSenRangeSuffixTargets();
                        break;
                    case INCLUDE:
                        testData = this.ruleSupportDsTypes.get(dsType).getSenRangeIncludeTargets();
                        break;
                    default:
                        return true;
                }
                break;
            default:
                return true;
        }

        switch (rangeType) {
            case Environment:
                return testData.contains(TargetType.Environment);
            case Instance:
                return testData.contains(TargetType.Instance);
            case Catalog:
                return testData.contains(TargetType.Catalog);
            case Schema:
                return testData.contains(TargetType.Schema);
            case TableOrView:
                return testData.contains(TargetType.Table) || testData.contains(TargetType.View) || testData.contains(TargetType.Materialized);
            case Column:
                return testData.contains(TargetType.Column);
        }
        return false;
    }

    @Override
    public List<DmSecRuleDO> listQueryRuleByUid(String ownerUid) {
        return this.dmSecRulesMapper.listByUid(ownerUid);
    }

    @Override
    public DmSecRuleMO queryRuleById(String ownerUid, long refRuleOrSenId, RuleKind ruleKind) {
        if (ruleKind == RuleKind.QUERY) {
            DmSecRuleDO ruleDO = this.dmSecRulesMapper.queryById(ownerUid, refRuleOrSenId);
            return ruleDO == null ? null : new DmSecRuleMO(ruleDO);
        } else if (ruleKind == RuleKind.SENSITIVE) {
            DmSecSensitiveDO senDO = this.dmSecSensitiveMapper.queryById(ownerUid, refRuleOrSenId);
            return senDO == null ? null : new DmSecRuleMO(senDO);
        } else {
            return null;
        }
    }

    @Override
    public List<DmSecRuleMO> queryRuleListByUser(String ownerUid, RuleKind ruleKind, String search) {
        //Page<?> page = PageUtil.startPage(pageInfo);
        search = StringUtils.isBlank(search) ? null : search.trim();
        if (ruleKind == RuleKind.QUERY) {
            List<DmSecRuleDO> list = this.dmSecRulesMapper.searchRules(ownerUid, search);
            return list.stream().map(DmSecRuleMO::new).sorted((o1, o2) -> {
                if (o1.getRuleDO().getRuleDsRange().size() == o2.getRuleDO().getRuleDsRange().size()) {
                    String dsStr1 = o1.getRuleDO().getRuleDsRange().stream().map(Enum::name).sorted().reduce((s1, s2) -> s1 + "," + s2).orElse("");
                    String dsStr2 = o2.getRuleDO().getRuleDsRange().stream().map(Enum::name).sorted().reduce((s1, s2) -> s1 + "," + s2).orElse("");
                    return -dsStr1.compareTo(dsStr2);
                } else {
                    return -Integer.compare(o1.getRuleDO().getRuleDsRange().size(), o2.getRuleDO().getRuleDsRange().size());
                }
            }).collect(Collectors.toList());
        } else if (ruleKind == RuleKind.SENSITIVE) {
            List<DmSecSensitiveDO> list = this.dmSecSensitiveMapper.searchSens(ownerUid, search);
            return list.stream().map(DmSecRuleMO::new).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<DmSecRefererDO> queryRuleRefererBySpec(String ownerUid, long specId, RuleKind ruleKind) {
        return dmSecRefererMapper.listBySpecAndKind(ownerUid, specId, ruleKind);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteRule(String ownerUid, long refRuleOrSenId, RuleKind ruleKind) {
        // delete referer and range
        List<DmSecRefererDO> refs = this.dmSecRefererMapper.listByRuleId(ownerUid, refRuleOrSenId, ruleKind);
        for (DmSecRefererDO ref : refs) {
            this.dmSecRefererMapper.deleteById(ownerUid, ref.getId());
            this.dmSecRangeMapper.deleteByRefId(ownerUid, ref.getId());
        }

        // delete rule
        if (ruleKind == RuleKind.QUERY) {
            this.dmSecRulesMapper.deleteByUidAndId(ownerUid, refRuleOrSenId);
        } else if (ruleKind == RuleKind.SENSITIVE) {
            this.dmSecSensitiveMapper.deleteByUidAndId(ownerUid, refRuleOrSenId);
        } else {
            throw new UnsupportedOperationException("RuleKind: " + ruleKind + " not supported");
        }
    }

    @Override
    public Map<String, String> getRuleScriptFormatByUid(String ownerUid) {
        return Collections.emptyMap(); // TODO keys see com.clougence.dslparser.detectrule.format.DetectRuleFmtOptions
    }

    @SneakyThrows
    @Override
    public void updateRule(String ownerUid, long ruleId, RuleSaveFO fo) {
        if (fo.getRuleKind() == RuleKind.QUERY) {
            if (CollectionUtils.isEmpty(fo.getDsRange())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_QUERY_DS_UNDEFINED_ERROR.name()));
            }

            checkQueryRuleSupport(fo);

            DmSecRuleDO ruleDO = new DmSecRuleDO();
            ruleDO.setName(fo.getRuleName());
            ruleDO.setDescription(fo.getRuleDesc());
            ruleDO.setScriptType(fo.getRuleType());
            ruleDO.setScriptDef(JsonUtils.toJson(this.extractParameters(fo.getRuleType(), fo.getContent())));
            ruleDO.setScriptContent(scriptVerify(fo.getContent()));
            ruleDO.setScriptMD5(MD5.getMD5(ruleDO.getScriptContent()));

            ruleDO.setRuleDsRange(fo.getDsRange());
            ruleDO.setRuleTarget(fo.getTargetType());

            this.dmSecRulesMapper.updateRule(ownerUid, ruleId, ruleDO);
        } else if (fo.getRuleKind() == RuleKind.SENSITIVE) {
            DmSecSensitiveDO senDO = new DmSecSensitiveDO();
            senDO.setName(fo.getRuleName());
            senDO.setDescription(fo.getRuleDesc());
            senDO.setScriptType(fo.getRuleType());
            senDO.setScriptDef(JsonUtils.toJson(this.extractParameters(fo.getRuleType(), fo.getContent())));
            senDO.setScriptContent(scriptVerify(fo.getContent()));
            senDO.setScriptMD5(MD5.getMD5(senDO.getScriptContent()));

            senDO.setSenMode(fo.getSenMode());

            this.dmSecSensitiveMapper.updateSen(ownerUid, ruleId, senDO);
        } else {
            throw new UnsupportedOperationException("RuleKind: " + fo.getRuleKind() + " not supported.");
        }
    }

    @SneakyThrows
    @Override
    public DmSecRuleMO createRule(String ownerUid, RuleSaveFO fo) {
        if (fo.getRuleKind() == RuleKind.QUERY) {
            if (CollectionUtils.isEmpty(fo.getDsRange())) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_QUERY_DS_UNDEFINED_ERROR.name()));
            }

            checkQueryRuleSupport(fo);

            DmSecRuleDO ruleDO = new DmSecRuleDO();
            ruleDO.setOwnerUid(ownerUid);
            ruleDO.setRuleId(this.namingService.genSecRuleName(RuleKind.QUERY));
            ruleDO.setName(fo.getRuleName());
            ruleDO.setDescription(fo.getRuleDesc());
            ruleDO.setScriptType(fo.getRuleType());
            ruleDO.setScriptDef(JsonUtils.toJson(this.extractParameters(fo.getRuleType(), fo.getContent())));
            ruleDO.setScriptContent(scriptVerify(fo.getContent()));
            ruleDO.setScriptMD5(MD5.getMD5(ruleDO.getScriptContent()));
            ruleDO.setInnerShare(false);

            ruleDO.setRuleDsRange(fo.getDsRange());
            ruleDO.setRuleTarget(fo.getTargetType());
            this.dmSecRulesMapper.insert(ruleDO);
            return new DmSecRuleMO(ruleDO);
        } else if (fo.getRuleKind() == RuleKind.SENSITIVE) {
            if (fo.getSenMode() == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_SEN_MODE_UNDEFINED_ERROR.name()));
            }

            DmSecSensitiveDO senDO = new DmSecSensitiveDO();
            senDO.setOwnerUid(ownerUid);
            senDO.setSenId(this.namingService.genSecRuleName(RuleKind.SENSITIVE));
            senDO.setName(fo.getRuleName());
            senDO.setDescription(fo.getRuleDesc());
            senDO.setScriptType(fo.getRuleType());
            senDO.setScriptDef(JsonUtils.toJson(this.extractParameters(fo.getRuleType(), fo.getContent())));
            senDO.setScriptContent(scriptVerify(fo.getContent()));
            senDO.setScriptMD5(MD5.getMD5(senDO.getScriptContent()));
            senDO.setInnerShare(false);

            senDO.setSenMode(fo.getSenMode());
            this.dmSecSensitiveMapper.insert(senDO);
            return new DmSecRuleMO(senDO);
        } else {
            throw new UnsupportedOperationException("RuleKind: " + fo.getRuleKind() + " not supported.");
        }
    }

    private static String scriptVerify(String scriptContent) {
        if (StringUtils.isBlank(scriptContent)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_SCRIPT_EMPTY_ERROR.name()));
        }

        try {
            StatementSet statementSet = DslHelper.parserDsl("DetectRule", scriptContent);
            long codeLines = statementSet.getStatements().stream().filter(s -> {
                return !s.getClass().getSimpleName().equals("DefineStatement");
            }).count();
            if (codeLines == 0) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_SCRIPT_EMPTY_ERROR.name()));
            }

            return scriptContent;
        } catch (ErrorMessageException e) {
            throw e;
        } catch (AntlerSyntaxException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_DSL_SYNTAX_ERROR.name(), e.getLine(), e.getColumn(), e.getMessage()));
        } catch (Exception e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_DSL_UNKNOWN_ERROR.name(), e.getMessage()));
        }
    }

    private void checkQueryRuleSupport(RuleSaveFO fo) {
        for (DataSourceType dsType : fo.getDsRange()) {
            if (!this.ruleSettingDef.getQueryConf().getTargets().containsKey(dsType)) {
                throw new UnsupportedOperationException();
            }

            SecRulesSupportSpi supportSpi = PluginManager.findSecRulesSupportSpi(dsType);
            if (!supportSpi.supportModel().contains(fo.getTargetType().getType())) {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public List<SecParam> extractParameters(RuleScriptType contentType, String content) {
        if (StringUtils.isBlank(content)) {
            return Collections.emptyList();
        }

        SecRulesCheckerService checkerSpi;
        try {
            checkerSpi = PluginManager.findService(SecRulesCheckerService.class);
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }

        try {
            return checkerSpi.getParameters(contentType.name(), content);
        } catch (AntlerSyntaxException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_DSL_SYNTAX_ERROR.name(), e.getLine(), e.getColumn(), e.getMessage()));
        }
    }

    @Override
    public DmSecRefererDO querySpecRefererById(String ownerUid, long specId, long refRuleOrSenId, RuleKind ruleKind) {
        return this.dmSecRefererMapper.queryBySpecAndRuleOrSen(ownerUid, specId, refRuleOrSenId, ruleKind);
    }

    @Override
    public DmSecRefererDO querySpecRefererById(String ownerUid, long refId) {
        return this.dmSecRefererMapper.queryById(ownerUid, refId);
    }

    @Override
    public List<DmSecSpecDO> querySpecListByRuleId(String ownerUid, long refRuleOrSenId, RuleKind ruleKind) {
        List<DmSecRefererDO> refererList = this.dmSecRefererMapper.listByRuleId(ownerUid, refRuleOrSenId, ruleKind);
        Set<Long> secIds = refererList.stream().map(DmSecRefererDO::getRefSpec).collect(Collectors.toSet());
        if (secIds.isEmpty()) {
            return Collections.emptyList();
        } else {
            return this.dmSecSpecMapper.queryByIds(ownerUid, secIds);
        }
    }

    @Override
    public List<SecRange> fetchRangeBySpec(String ownerUid, long specId) {
        List<DmSecRangeDO> rangeList = this.dmSecRangeMapper.queryListBySpecId(ownerUid, specId);
        return parseSecRanges(rangeList);
    }

    @Override
    public List<SecRange> fetchRangeByRef(String ownerUid, long refId) {
        List<DmSecRangeDO> rangeList = this.dmSecRangeMapper.queryListByRefId(ownerUid, refId);
        return parseSecRanges(rangeList);
    }

    private static List<SecRange> parseSecRanges(List<DmSecRangeDO> rangeList) {
        List<SecRange> result = new ArrayList<>();
        for (DmSecRangeDO rangeDO : rangeList) {
            SecRangeType rangeType = rangeDO.getRangeType();
            String[] levelPrefix = rangeDO.getLevelPrefix().substring(1).split("/");
            List<String> levelNodes = rangeDO.getLevelNodes();

            SecRange range = new SecRange();
            range.setRangeId(rangeDO.getId());
            range.setRefId(rangeDO.getRefId());
            range.setMatchMode(rangeDO.getMatchMode());
            range.setRangeType(rangeType);
            range.setChooseAll(rangeDO.isChooseAll());
            range.setNodes(new ArrayList<>());
            range.setVerify(SecRangeVerify.Succeed); // default
            range.setVerifyMessage(new ArrayList<>());
            range.setDsType(rangeDO.getReferDsType());
            range.setTableLevelType(rangeDO.getTableLevelType());

            switch (rangeType) {
                case Environment:
                    FetchRangeUtils.fetchRangeTypeByEnv(range, levelNodes);
                    break;
                case Instance:
                    FetchRangeUtils.fetchRangeTypeByInstance(range, levelPrefix, levelNodes);
                    break;
                case Catalog:
                    FetchRangeUtils.fetchRangeTypeByCatalog(range, levelPrefix, levelNodes);
                    break;
                case Schema:
                    FetchRangeUtils.fetchRangeTypeBySchema(range, levelPrefix, levelNodes);
                    break;
                case TableOrView:
                    FetchRangeUtils.fetchRangeTypeByTable(range, levelPrefix, levelNodes);
                    break;
                case Column:
                    FetchRangeUtils.fetchRangeTypeByColumn(range, levelPrefix, levelNodes);
                    break;
                default:
                    String rangeTypeName = DmI18nUtils.getMessage(rangeType.getI18nKey());
                    String verifyMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_TYPE_UNSUPPORTED_MESSAGE.name(), rangeTypeName);

                    range.setVerify(SecRangeVerify.Broken);
                    range.getVerifyMessage().add(verifyMessage);
                    break;
            }

            result.add(range);
        }

        return result;
    }

    @Override
    public DmSecRangeDO saveRange(String ownerUid, SpecSaveRangeFO fo) {
        DmSecRefererDO refDO = this.dmSecRefererMapper.queryBySpecAndRuleOrSen(ownerUid, fo.getSpecId(), fo.getRuleId(), fo.getRuleKind());
        if (refDO == null || !refDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_IS_DISABLED_MESSAGE.name()));
        }

        DmSecRangeDO rangeDO;
        if (fo.getRangeId() == null) {
            rangeDO = new DmSecRangeDO();
            rangeDO.setOwnerUid(ownerUid);
            rangeDO.setRefSpec(fo.getSpecId());
            rangeDO.setRefId(refDO.getId());
        } else {
            rangeDO = this.dmSecRangeMapper.selectById(fo.getRangeId());
        }

        rangeDO.setMatchMode(fo.getMatchMode());
        rangeDO.setRangeType(fo.getRangeType());
        rangeDO.setChooseAll(fo.isChooseAll());

        SecRangeType rangeType = rangeDO.getRangeType();
        switch (rangeType) {
            case Environment:
                rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixByEnv(fo));
                break;
            case Instance:
                rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixByInstance(fo));
                break;
            case Catalog:
                rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixByCatalog(fo));
                rangeDO.setReferDsType(FetchRangeUtils.toReferDsType(fo));
                break;
            case Schema:
                rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixBySchema(fo));
                rangeDO.setReferDsType(FetchRangeUtils.toReferDsType(fo));
                break;
            case TableOrView:
            case Column: {
                switch (rangeType) {
                    case TableOrView: {
                        rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixByTable(fo));
                        rangeDO.setReferDsType(FetchRangeUtils.toReferDsType(fo));
                        break;
                    }
                    case Column: {
                        rangeDO.setLevelPrefix(FetchRangeUtils.toLevelPrefixByColumn(fo));
                        rangeDO.setReferDsType(FetchRangeUtils.toReferDsType(fo));
                        break;
                    }
                }

                switch (fo.getTableLevelType()) {
                    case Table:
                        rangeDO.setTableLevelType(TargetType.Table);
                        break;
                    case View:
                        rangeDO.setTableLevelType(TargetType.View);
                        break;
                    case Materialized:
                        rangeDO.setTableLevelType(TargetType.Materialized);
                        break;
                    default:
                        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_MISSING_TABLE_LEVEL_TYPE_ERROR.name()));
                }
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }

        rangeDO.setLevelNodes(fo.getNodes());

        if (fo.getRangeId() == null) {
            this.dmSecRangeMapper.insert(rangeDO);
        } else {
            this.dmSecRangeMapper.updateRange(ownerUid, fo.getRangeId(), rangeDO);
        }

        return rangeDO;
    }

    @Override
    public void deleteRange(String ownerUid, RangeDeleteFO fo) {
        DmSecRangeDO rangeDO = this.dmSecRangeMapper.selectById(fo.getRangeId());
        if (rangeDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RANGE_NOT_EXIST_ERROR.name()));
        }

        this.dmSecRangeMapper.deleteRange(ownerUid, fo.getRangeId());
    }
}
