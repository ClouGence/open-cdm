package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_READ;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.model.DmSecRefererDO;
import com.clougence.clouddm.console.web.dal.model.DmSecSpecDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.model.fo.checkrules.*;
import com.clougence.clouddm.console.web.model.vo.checkrules.*;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.console.web.service.security.mode.DmSecRuleMO;
import com.clougence.clouddm.console.web.util.DmCheckerUtils;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.service.secrules.SecParam;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.dal.model.RdpDsEnvDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/2/25
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/security/rules")
@Slf4j
public class DmSecRulesController {

    @Resource
    private CheckRulesService checkRulesService;
    @Resource
    private DmEnvParamService rdpDsEnvService;

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/specList", method = RequestMethod.POST)
    public ResWebData<?> specList(@RequestBody @Valid SpecListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmSecSpecDO> specPage = this.checkRulesService.querySpecList(puid, fo.getSearch());
        List<SpecVO> collect = specPage.stream().map(DmConvertUtils::convertToDmSecSpecVO).collect(Collectors.toList());

        return ResWebDataUtils.buildSuccess(collect);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/specDetail", method = RequestMethod.POST)
    public ResWebData<?> specDetail(@RequestBody @Valid SpecDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }
        List<DmSecRefererDO> refererList = this.checkRulesService.queryRuleRefererBySpec(puid, fo.getSpecId(), fo.getRuleKind());
        Map<Long, DmSecRefererDO> refererMap = new HashMap<>();
        refererList.forEach(ref -> refererMap.put(ref.getRefRule(), ref));

        List<DmSecRuleMO> rulePage = this.checkRulesService.queryRuleListByUser(puid, fo.getRuleKind(), fo.getSearch());
        List<SpecRulesVO> collect = rulePage.stream().map(defDO -> {
            return DmConvertUtils.convertToDmSecRulesVO(defDO, refererMap, specDO);
        }).collect(Collectors.toList());

        SpecDetailVO detailVO = new SpecDetailVO();
        detailVO.setSpecId(specDO.getId());
        detailVO.setSpecName(specDO.getName());
        detailVO.setSpecDesc(specDO.getDescription());
        detailVO.setEnable(specDO.isEnable());
        detailVO.setRuleList(collect);

        return ResWebDataUtils.buildSuccess(detailVO);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/specRuleDetail", method = RequestMethod.POST)
    public ResWebData<?> specRuleDetail(@RequestBody @Valid SpecRuleDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }
        DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, fo.getRuleId(), fo.getRuleKind());
        if (ruleMO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
        }

        DmSecRefererDO refererDO = this.checkRulesService.querySpecRefererById(puid, fo.getSpecId(), fo.getRuleId(), fo.getRuleKind());

        Map<Long, DmSecRefererDO> refererMap = new HashMap<>();
        refererMap.put(fo.getRuleId(), refererDO);

        SpecRulesVO vo = DmConvertUtils.convertToDmSecRulesVO(ruleMO, refererMap, specDO);
        vo.setRuleContent(ruleMO.getScriptContent());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specUpdateInfo", method = RequestMethod.POST)
    public ResWebData<?> specUpdateInfo(@RequestBody @Valid SpecUpdateInfoFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        this.checkRulesService.updateSpec(puid, fo.getSpecId(), fo.getNewName(), fo.getNewDesc());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specConfig", method = RequestMethod.POST)
    public ResWebData<?> specConfig(@RequestBody @Valid SpecConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        SpecUpdateVO vo = new SpecUpdateVO();
        List<RdpDsEnvDO> envs = this.rdpDsEnvService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
        if (!envs.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
            vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
        }

        if (!envs.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);

            String i18nKey = fo.isEnable() ? I18nDmMsgKeys.CHECKRULES_SPEC_ENABLE_FINISH_MESSAGE.name() : I18nDmMsgKeys.CHECKRULES_SPEC_DISABLE_FINISH_MESSAGE.name();
            vo.setMessage(DmI18nUtils.getMessage(i18nKey, specDO.getName()));
            this.checkRulesService.configStatus(puid, fo.getSpecId(), fo.isEnable());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specDelete", method = RequestMethod.POST)
    public ResWebData<?> specDelete(@RequestBody @Valid SpecDeleteFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        SpecDeleteVO vo = new SpecDeleteVO();
        List<RdpDsEnvDO> envs = this.rdpDsEnvService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
        if (!envs.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
            vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
        }

        if (!envs.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_DELETE_FINISH_MESSAGE.name(), specDO.getName()));
            this.checkRulesService.deleteSpec(puid, fo.getSpecId());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specCreate", method = RequestMethod.POST)
    public ResWebData<?> specCreate(@RequestBody @Valid SpecCreateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.createSpec(puid, fo.getSpecName(), fo.getSpecDesc(), true);
        return ResWebDataUtils.buildSuccess(specDO.getId());
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specSaveRule", method = RequestMethod.POST)
    public ResWebData<?> specSaveRule(@RequestBody @Valid SpecSaveRuleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        DmSecRefererDO refDO = checkRulesService.querySpecRefererById(puid, fo.getSpecId(), fo.getRule().getRuleId(), fo.getRule().getRuleKind());
        SpecUpdateVO vo = new SpecUpdateVO();
        if (refDO != null && refDO.isEnable()) {
            List<RdpDsEnvDO> envs = this.rdpDsEnvService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
            if (!envs.isEmpty()) {
                vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
                vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
            }

            if (!envs.isEmpty() && !fo.isForce()) {
                vo.setSuccess(false);
                return ResWebDataUtils.buildSuccess(vo);
            }
        }

        vo.setSuccess(true);
        vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_UPDATE_FINISH_MESSAGE.name(), specDO.getName()));
        this.checkRulesService.saveSpecRules(puid, specDO.getId(), Collections.singletonList(fo.getRule()));
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/specDeleteRule", method = RequestMethod.POST)
    public ResWebData<?> specDeleteRule(@RequestBody @Valid SpecDeleteRuleFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecSpecDO specDO = this.checkRulesService.querySpecById(puid, fo.getSpecId());
        if (specDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_NOT_EXIST_ERROR.name()));
        }

        SpecUpdateVO vo = new SpecUpdateVO();
        List<RdpDsEnvDO> envs = this.rdpDsEnvService.queryListByParamKeyValue(puid, EnvParamKeys.DM_BIND_CHECK_SPEC, String.valueOf(fo.getSpecId()));
        if (!envs.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_INUSE_MESSAGE.name(), specDO.getName()));
            vo.setReferer(envs.stream().map(DmConvertUtils::convertToRefEnvVO).collect(Collectors.toList()));
        }

        if (!envs.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_SPEC_UPDATE_FINISH_MESSAGE.name(), specDO.getName()));
            this.checkRulesService.deleteSpecRules(puid, specDO.getId(), Collections.singletonList(fo.getRule()));
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleList", method = RequestMethod.POST)
    public ResWebData<?> ruleList(@RequestBody @Valid RuleListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmSecRuleMO> rulePage = this.checkRulesService.queryRuleListByUser(puid, fo.getRuleKind(), fo.getSearch());

        List<RuleVO> collect = rulePage.stream().map(ruleDO -> {
            return DmConvertUtils.convertToDmSecRulesVO(ruleDO, true);
        }).collect(Collectors.toList());

        return ResWebDataUtils.buildSuccess(collect);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleDetail", method = RequestMethod.POST)
    public ResWebData<?> ruleDetail(@RequestBody @Valid RuleDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, fo.getRuleId(), fo.getRuleKind());
        if (ruleMO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
        }

        RuleVO vo = DmConvertUtils.convertToDmSecRulesVO(ruleMO, false);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/ruleSave", method = RequestMethod.POST)
    public ResWebData<?> ruleSave(@RequestBody @Valid RuleSaveFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RuleUpdateVO vo = new RuleUpdateVO();
        if (fo.getRuleId() != null) {
            DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, fo.getRuleId(), fo.getRuleKind());
            if (ruleMO == null) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
            }
            if (ruleMO.isInnerShare()) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_IS_INNER_MESSAGE.name()));
            }

            String name = DmConvertUtils.tryRuleI18nMessage(ruleMO.getName());

            List<DmSecSpecDO> specList = this.checkRulesService.querySpecListByRuleId(puid, fo.getRuleId(), fo.getRuleKind());
            if (!specList.isEmpty()) {
                vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_INUSE_MESSAGE.name(), name));
                vo.setReferer(specList.stream().map(DmConvertUtils::convertToRefSpecVO).collect(Collectors.toList()));
            } else {
                vo.setReferer(Collections.emptyList());
            }

            if (!specList.isEmpty() && !fo.isForce()) {
                vo.setSuccess(false);
                vo.setRuleId(fo.getRuleId());
                vo.setRuleKind(fo.getRuleKind());
                return ResWebDataUtils.buildSuccess(vo);
            } else {
                vo.setSuccess(true);
                vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_UPDATE_FINISH_MESSAGE.name(), name));
                vo.setRuleId(fo.getRuleId());
                vo.setRuleKind(fo.getRuleKind());
                this.checkRulesService.updateRule(puid, fo.getRuleId(), fo);
                return ResWebDataUtils.buildSuccess(vo);
            }
        } else {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_INSERT_FINISH_MESSAGE.name(), fo.getRuleName()));
            vo.setReferer(Collections.emptyList());
            DmSecRuleMO ruleDO = this.checkRulesService.createRule(puid, fo);
            vo.setSuccess(true);
            vo.setRuleId(ruleDO.getId());
            vo.setRuleKind(ruleDO.getRuleKind());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_MANAGE)
    @RequestMapping(value = "/ruleDelete", method = RequestMethod.POST)
    public ResWebData<?> ruleDelete(@RequestBody @Valid RuleDeleteFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RuleUpdateVO vo = new RuleUpdateVO();
        DmSecRuleMO ruleMO = this.checkRulesService.queryRuleById(puid, fo.getRuleId(), fo.getRuleKind());
        if (ruleMO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_NOT_EXIST_ERROR.name()));
        }
        if (ruleMO.isInnerShare()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_IS_INNER_MESSAGE.name()));
        }

        String name = DmConvertUtils.tryRuleI18nMessage(ruleMO.getName());

        List<DmSecSpecDO> specList = this.checkRulesService.querySpecListByRuleId(puid, fo.getRuleId(), fo.getRuleKind());
        if (!specList.isEmpty()) {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_INUSE_MESSAGE.name(), name));
            vo.setReferer(specList.stream().map(DmConvertUtils::convertToRefSpecVO).collect(Collectors.toList()));
        } else {
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CHECKRULES_RULE_DELETE_FINISH_MESSAGE.name(), name));
            vo.setReferer(Collections.emptyList());
        }

        if (!specList.isEmpty() && !fo.isForce()) {
            vo.setSuccess(false);
            vo.setRuleId(fo.getRuleId());
            vo.setRuleKind(fo.getRuleKind());
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            vo.setSuccess(true);
            vo.setRuleId(fo.getRuleId());
            vo.setRuleKind(fo.getRuleKind());
            this.checkRulesService.deleteRule(puid, fo.getRuleId(), fo.getRuleKind());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleExtract", method = RequestMethod.POST)
    public ResWebData<?> ruleExtract(@RequestBody @Valid RuleExtractParametersFO fo, HttpServletRequest request) {
        List<SecParam> params = this.checkRulesService.extractParameters(fo.getType(), fo.getContent());
        return ResWebDataUtils.buildSuccess(params);
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleFormat", method = RequestMethod.POST)
    public ResWebData<?> ruleFormat(@RequestBody @Valid RuleFormatFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        try {
            DmCheckerUtils.checkDetectRuleScript(fo.getContent());

            StatementSet statementSet = DslHelper.parserDsl("DetectRule", fo.getContent());

            Map<String, String> fmtOptions = new HashMap<>(this.checkRulesService.getRuleScriptFormatByUid(puid));

            StringWriter writer = new StringWriter();
            statementSet.doFormat(new FmtWriter(writer, fmtOptions));

            RuleFormatVO vo = new RuleFormatVO();
            vo.setSuccess(true);
            vo.setContent(writer.toString());
            return ResWebDataUtils.buildSuccess();
        } catch (ErrorMessageException e) {
            RuleFormatVO vo = new RuleFormatVO();
            vo.setSuccess(false);
            vo.setMessage(e.getErrorMessage());
            vo.setContent(fo.getContent());
            return ResWebDataUtils.buildSuccess(vo);
        } catch (Exception e) {
            RuleFormatVO vo = new RuleFormatVO();
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleVerify", method = RequestMethod.POST)
    public ResWebData<?> ruleVerify(@RequestBody @Valid RuleVerifyFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        try {
            DmCheckerUtils.checkDetectRuleScript(fo.getContent());
            //StatementSet statementSet = DslHelper.parserDsl("DetectRule", );

            //            Map<String, String> fmtOptions = new HashMap<>(this.checkRulesService.getRuleScriptFormatByUid(puid));
            //            StringWriter writer = new StringWriter();
            //            statementSet.doFormat(new FmtWriter(writer, fmtOptions));

            RuleVerifyVO vo = new RuleVerifyVO();
            vo.setSuccess(true);
            return ResWebDataUtils.buildSuccess();
        } catch (ErrorMessageException e) {
            RuleVerifyVO vo = new RuleVerifyVO();
            vo.setSuccess(false);
            vo.setMessage(e.getErrorMessage());
            return ResWebDataUtils.buildSuccess(vo);
        } catch (Exception e) {
            RuleVerifyVO vo = new RuleVerifyVO();
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
            return ResWebDataUtils.buildSuccess(vo);
        }
    }
}
