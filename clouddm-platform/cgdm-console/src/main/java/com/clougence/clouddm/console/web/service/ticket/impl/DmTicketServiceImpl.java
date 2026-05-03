package com.clougence.clouddm.console.web.service.ticket.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesEngine;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.clougence.clouddm.console.web.constants.DmErrorCode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DmAutoExecType;
import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;
import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecTaskMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmBizLogMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmTicketMapper;
import com.clougence.clouddm.console.web.dal.model.DmTicketDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.service.ticket.DmTicketService;
import com.clougence.clouddm.console.web.service.ticket.model.TicketInfo;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.component.ticket.RdpTicketProcessService;
import com.clougence.rdp.component.ticket.model.RdpExecStageContextMO;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.enumeration.*;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.*;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 15:37
 */
@Service
@Slf4j
public class DmTicketServiceImpl implements DmTicketService {

    @Resource
    private QueryAnalysisService    queryAnalysisService;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private NamingService           namingService;
    @Resource
    private RdpTicketProcessService rdpTicketProcessService;
    @Resource
    private SecRulesEngine          ruleCheckService;
    @Resource
    private DmTicketMapper          dmTicketMapper;
    @Resource
    private RdpTicketMapper         rdpTicketMapper;
    @Resource
    private RdpDataSourceMapper     rdpDataSourceMapper;
    @Resource
    private RdpUserMapper           rdpUserMapper;
    @Resource
    private RdpTicketProcessMapper  rdpTicketProcessMapper;
    @Resource
    private RdpDsEnvMapper          rdpEnvMapper;
    @Resource
    private DmEnvParamService       dmEnvParamService;
    @Resource
    private RdpApprovalPersonMapper rdpApprovalPersonMapper;
    @Resource
    private RdpApprovalService      approvalService;
    @Resource
    private DmAutoExecTaskMapper    dmSqlTaskMapper;
    @Resource
    private DmAutoExecJobMapper     dmAutoExecJobMapper;
    @Resource
    private DmBizLogMapper          dmBizLogMapper;
    @Resource
    private AutoExecService         autoExecService;
    @Resource
    private RdpRoleMapper           rdpRoleMapper;
    @Resource
    private BizResOwnerCacheService bizResOwnerCacheService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        RdpDataSourceDO dsDO = dsLevels.getDsDO();
        DataSourceType dsType = dsDO.getDataSourceType();
        RdpDsEnvDO envDO = this.rdpEnvMapper.queryByEnvID(puid, dsDO.getDsEnvId());

        // check approval
        DmEnvParamTicketDesVO ticketConfig = this.dmEnvParamService.querySqlTicketInfoParam(puid, dsDO.getDsEnvId());
        if (ticketConfig == null || !ticketConfig.isOpenTicket() || StringUtils.isBlank(ticketConfig.getType())) {
            String title = RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TYPE_SQL_TITLE.name());
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), title));
        }
        if (ticketConfig.isDelete()) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name()));
        }
        RdpApprovalType approvalType = RdpApprovalType.valueOf(ticketConfig.getType());
        if (approvalType != RdpApprovalType.Internal) {
            RdpCacheApproTemplateDO templateDO = this.approvalService.checkApprovalAndReturnTemplate(puid, approvalType, ticketConfig.getTemplateId(), null);
            ticketConfig.setTemplateName(templateDO.getTemplateName());// update form cache.
        }

        // rule check
        Map<UmiTypes, Object> levelsParam = dsLevels.getLevelsParam();
        SecRulesCheckContext checkContext = SecRulesCheckContext.builder()
            .basicCodeLine(1)
            .basicCodeColumn(0)
            .dsId(dsDO.getId())
            .currentUID(uid)
            .currentCatalog((String) levelsParam.get(UmiTypes.Catalog))
            .currentSchema((String) levelsParam.get(UmiTypes.Schema))
            .requester(Requester.TICKET)
            .unsupportedLevel(WarnLevel.FAILURE)
            .build();
        SecRulesCheckResult checkResult = this.ruleCheckService.doQueryCheck(puid, uid, fo.getRawSql(), checkContext);
        DmTicketResultVO result = this.convertToRuleCheckResult(checkResult);

        // check force
        TicketInfo ticketInfo = new TicketInfo();
        int totalCount = this.analysisSqlAndCheckResource(fo, dsType, dsLevels, ticketInfo);
        if (!fo.isForce()) {
            if (result.isFailure() || result.isConfirm()) {
                return result;
            }
        } else {
            result = new DmTicketResultVO();
        }

        // query env bind param
        String targetInfo = "/" + dsLevels.getDsDO().getInstanceId();
        if (dsLevels.getLevelsDef().contains(UmiTypes.Catalog)) {
            targetInfo += String.format("/%s/%s", levelsParam.get(UmiTypes.Catalog), levelsParam.get(UmiTypes.Schema));
        } else {
            targetInfo += String.format("/%s", levelsParam.get(UmiTypes.Schema));
        }

        // RDP ticket ins
        String bizId = this.namingService.genTicketBizId();
        RdpTicketDO ticket = new RdpTicketDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(puid);
        ticket.setBindDsId(dsDO.getId());
        ticket.setTargetInfo(targetInfo);
        ticket.setDescription(fo.getDescription());
        ticket.setTicketTitle(fo.getTicketTitle());
        ticket.setTicketStatus(RdpTicketStatus.PRE_INIT);
        ticket.setApproBiz(RdpApprovalBiz.DM_QUERY);
        ticket.setStatusMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name()));
        ticket.setApproType(RdpApprovalType.valueOf(ticketConfig.getType()));
        ticket.setApproTemplateIdentity(ticketConfig.getTemplateId());
        ticket.setApproTemplateName(ticketConfig.getTemplateName());
        ticket.setEnvName(envDO.getEnvName());

        // DM ticket ins
        DmTicketDO dmTicketDO = new DmTicketDO();
        dmTicketDO.setRdpTicketInsId(bizId);
        dmTicketDO.setRawSql(fo.getRawSql());
        dmTicketDO.setTotalCount(totalCount);
        dmTicketDO.setExpectedAffectedRows(fo.getExpectedAffectedRows());
        dmTicketDO.setTicketInfo(JsonUtils.toJson(ticketInfo));
        dmTicketDO.setLevels(dsLevels.getDbLevels());
        if (StringUtils.isNotBlank(fo.getRollBackSql())) {
            dmTicketDO.setRollBackSql(fo.getRollBackSql());
        }
        dmTicketDO.setCheckedInfo(JsonUtils.toJson(result.getCheckedVOS()));

        if (ticket.getApproType() == RdpApprovalType.Internal) {
            RdpApprovalPersonDO primary = new RdpApprovalPersonDO();
            primary.setPersonUid(puid);
            primary.setTicketBzId(bizId);
            this.rdpApprovalPersonMapper.insert(primary);
        }

        this.rdpTicketMapper.insert(ticket);
        this.dmTicketMapper.insert(dmTicketDO);

        this.rdpTicketProcessService.createProcess(ticket.getId(), RdpApprovalBiz.DM_QUERY, ticketInfo.getMessage() == null);

        result.setTicketId(ticket.getId());
        return result;
    }

    private int analysisSqlAndCheckResource(DmAddTicketFO fo, DataSourceType dsType, DsLevels dsLevels, TicketInfo ticketInfo) {
        int totalCount = 1;
        try {
            List<SplitScript> sqlList = this.queryAnalysisService.analysisSplit(dsType, fo.getRawSql(), null, 1, 0);
            totalCount = sqlList.size();
            // check resource match fo.levels
            Map<String, Object> params = new HashMap<>();
            dsLevels.getLevelsParam().forEach((umiType, value) -> {
                switch (umiType) {
                    case Catalog:
                        params.put(SessionSpi.PARAMS_DEFAULT_DB, value);
                        break;
                    case Schema:
                        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, value);
                        break;
                    default:
                        break;
                }
            });
            DataSourceConfig dataSourceConfig = dmDsConfigService.fetchDsConfigFromDM(dsLevels.getDsDO().getId(), dsLevels.getDsDO().getDataSourceType());
            Map<RuleDomain, List<ResObject>> ruleDomainListMap = this.queryAnalysisService.analysisResourceV2(dataSourceConfig, fo.getRawSql(), params);
            List<ResObject> resObjects = ruleDomainListMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
            String path = dsLevels.asResPath().getResPath();
            for (ResObject resObject : resObjects) {
                if (resObject.getType() == TargetType.ConfigKey) {
                    continue;
                }

                if (!resObject.toDsResPath().getResPath().startsWith(path)) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PASRSE_SQL_RESOURCE_ERROR.name(), path));
                }
            }
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            if (fo.isForce()) {
                ticketInfo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.PASRSE_SQL_FAILED_FORCE.name()));
            } else {
                throw new ErrorMessageException(DmErrorCode.TICKET_SQL_PARSE_FAILED.code(), DmI18nUtils.getMessage(I18nDmMsgKeys.PASRSE_SQL_FAILED_MESSAGE.name()));
            }
        }

        if (totalCount > 1000) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_ROW_NUMBER_OVER_ERROR.name()));
        }
        return totalCount;
    }

    private static final RuleLevel[] CHECK_LEVELS_FAILURE = new RuleLevel[] { RuleLevel.FAILURE };

    private DmTicketResultVO convertToRuleCheckResult(SecRulesCheckResult result) {
        DmTicketResultVO vo = new DmTicketResultVO();
        vo.setConfirm(!result.isAllSuccess());
        vo.setFailure(result.hasAnyTarget(CHECK_LEVELS_FAILURE));

        List<CheckedVO> checkedVOS = new ArrayList<>();
        Map<String, RuleLevel> checked = result.getChecked();
        Map<String, String> descMap = result.getMessageMap();
        Map<String, Set<Integer>> scriptMap = result.getScriptMap();

        for (String key : checked.keySet()) {
            CheckedVO checkedVO = new CheckedVO();
            RuleLevel ruleLevel = checked.get(key);
            checkedVO.setName(key);
            checkedVO.setRuleLevel(ruleLevel);
            checkedVO.setDesc(descMap.get(key));
            if (CollectionUtils.isNotEmpty(scriptMap.get(key))) {
                checkedVO.setLines(scriptMap.get(key).stream().sorted().collect(Collectors.toList()));
            }
            checkedVOS.add(checkedVO);
        }
        vo.setCheckedVOS(checkedVOS);

        return vo;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void confirmTicket(String puid, long ticketId, DmConfirmTicketFO fo) {
        RdpTicketDO rdpTicketDO = this.checkTicket(ticketId, puid);
        RdpTicketStatus actionStatus = statusFromConfirmAction(fo.getConfirmActionType(), fo.getAutoExecConfig().getAutoExecType());

        checkJobOperationEnable(rdpTicketDO, fo.getConfirmUid());

        if (rdpTicketDO.getTicketStatus() != RdpTicketStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        DmTicketDO dmTicketDO = this.dmTicketMapper.getDmTicketInfo(rdpTicketDO.getBizId());
        if (dmTicketDO == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        RdpUserDO confirmUser = this.rdpUserMapper.queryByUid(fo.getConfirmUid());
        RdpExecStageContextMO cContext = new RdpExecStageContextMO();
        cContext.setExecUserName(Collections.singletonList(confirmUser.getUsername()));
        if (StringUtils.isNotBlank(fo.getComment())) {
            cContext.setExecMsg(fo.getComment());
        }

        // update processDO
        RdpTicketProcessDO processDO = null;
        processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.CONFIRM);
        this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(cContext));

        // update processDO
        processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        String execUser = execUserFromConfirmAction(fo.getConfirmActionType(), confirmUser);
        RdpExecStageContextMO nContext = new RdpExecStageContextMO();
        if (fo.getAutoExecConfig().getAutoExecType() != DmAutoExecType.MANUAL_EXEC) {
            nContext.setAutoExecute(true);
        }
        nContext.setExecUserName(Collections.singletonList(execUser));
        if (actionStatus == RdpTicketStatus.REJECTED) {
            processDO.setProcessStatus(RdpTicketProcessStatus.REJECT);
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.REJECT, JsonUtils.toJson(nContext));
        } else if (actionStatus == RdpTicketStatus.FINISHED) {
            processDO.setProcessStatus(RdpTicketProcessStatus.FINISH);
            nContext.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_COMPLETE_MESSAGE.name()));
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(nContext));
        } else if (actionStatus == RdpTicketStatus.WAIT_EXEC) {
            String ticketInfo = dmTicketDO.getTicketInfo();
            TicketInfo info;
            if (StringUtils.isEmpty(ticketInfo)) {
                info = new TicketInfo();
            } else {
                info = JsonUtils.toObj(ticketInfo, TicketInfo.class);
            }
            info.setAutoExec(true);
            this.dmTicketMapper.updateTicketInfo(dmTicketDO.getId(), JsonUtils.toJson(info));
            createAutoExecJob(fo, rdpTicketDO, dmTicketDO, confirmUser);
            this.rdpTicketProcessMapper.updateContextById(processDO.getId(), JsonUtils.toJson(info));
        }
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, actionStatus, fo.getComment());
    }

    private void createAutoExecJob(DmConfirmTicketFO fo, RdpTicketDO rdpTicket, DmTicketDO dmTicket, RdpUserDO confirmUser) {
        DsCacheEntry dsCacheEntry = bizResOwnerCacheService.queryByDsId(rdpTicket.getBindDsId());
        Long dsEnvId = dsCacheEntry.getEnvId();

        List<String> levels = new ArrayList<>();
        levels.add(dsEnvId.toString());
        levels.add(rdpTicket.getBindDsId().toString());

        if (dmTicket.getLevels() != null) {
            levels.addAll(dmTicket.getLevels());
        } else {
            String[] split = rdpTicket.getTargetInfo().split("/");
            levels.addAll(Arrays.asList(split).subList(1, split.length));
        }
        DsLevels dsLevels = dmDsConfigService.parseLevels(levels);

        List<SplitScript> splitScripts;
        RdpDataSourceDO rdpDataSourceDO = this.rdpDataSourceMapper.queryDsIdentityById(rdpTicket.getBindDsId());
        try {
            splitScripts = this.queryAnalysisService.analysisSplit(rdpDataSourceDO.getDataSourceType(), dmTicket.getRawSql(), null, 1, 0);
        } catch (Exception e) {
            log.warn("can not parse sql");
            SplitScript splitScript = new SplitScript();
            splitScript.setScript(dmTicket.getRawSql());
            splitScript.setType(SecQueryType.UNKNOWN);
            splitScripts = Collections.singletonList(splitScript);
        }

        RdbSupportSpi rdbSupportSpi = PluginManager.findRdbSupportSpi(dsLevels.getDsDO().getDataSourceType());
        if (!rdbSupportSpi.supportMultiStatement(false)) {
            SplitScript splitScript = new SplitScript();
            splitScript.setScript(dmTicket.getRawSql());
            if (splitScripts.size() > 1) {
                splitScript.setType(SecQueryType.UNKNOWN);
            } else {
                splitScript.setType(splitScripts.get(0).getType());
            }
            splitScripts = Collections.singletonList(splitScript);
        }

        this.autoExecService.createJob(rdpTicket.getPrimaryUid(), confirmUser.getUid(), fo.getAutoExecConfig(), dsLevels, SQLJobBizType.TICKET, rdpTicket.getBizId(), splitScripts);
    }

    @Override
    public DmQueryTicketVO queryQueryTicketDetail(String puid, DmQueryTicketDetailFO fo) {
        RdpTicketDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        if (ticketDO.getApproBiz() == null) {
            return null;
        }
        switch (ticketDO.getApproBiz()) {
            case DM_QUERY:
            case DM_CHANGE:
                break;
            default:
                return null;
        }

        DmTicketDO dmTicketDO = this.dmTicketMapper.getDmTicketInfo(ticketDO.getBizId());
        if (dmTicketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_BAD_DATA_NOT_SYNC_ERROR.name()));
        }

        // key is ticket id
        DmQueryTicketVO vo = new DmQueryTicketVO();
        vo.setRawSql(dmTicketDO.getRawSql());
        vo.setRollBackSql(dmTicketDO.getRollBackSql());
        vo.setTotalCount(dmTicketDO.getTotalCount());
        vo.setExpectedAffectedRows(dmTicketDO.getExpectedAffectedRows());
        if (StringUtils.isNotEmpty(dmTicketDO.getTicketInfo())) {
            TicketInfo ticketInfo = JsonUtils.toObj(dmTicketDO.getTicketInfo(), TicketInfo.class);
            String message = ticketInfo.getMessage();
            vo.setTicketMessage(message);
            vo.setAutoExec(ticketInfo.isAutoExec());
        }
        vo.setCheckedList(JsonUtils.toListUseType(dmTicketDO.getCheckedInfo(), CheckedVO.class));
        return vo;
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String puid, String uid, DmQueryTaskListFO fo) {
        RdpTicketDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        return this.autoExecService
            .queryAutoExecTaskList(ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid), fo.getTaskStatus(), fo.getPage());
    }

    @Override
    public DmAutoExecJobVO queryExecJobInfo(String puid, String uid, long ticketId) {
        RdpTicketDO ticketDO = this.checkTicket(ticketId, puid);
        return this.autoExecService.queryAutoExecJob(ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void retryJob(String puid, String uid, long ticketId) {
        RdpTicketDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.retryJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);

        rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_EXEC, null);
        rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.INIT);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        RdpTicketDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        checkJobOperationEnable(ticketDO, uid);
        boolean jobFinish = this.autoExecService.skipTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId(), uid);
        if (jobFinish) {
            rdpTicketMapper.updateTicketStatusByEnum(fo.getTicketId(), RdpTicketStatus.FINISHED, null);
            rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(fo.getTicketId(), RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FINISH);
        }
    }

    @Override
    public void canceledSkipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        RdpTicketDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        checkJobOperationEnable(ticketDO, uid);
        this.autoExecService.continueTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void endAutoExecJob(String puid, String uid, long ticketId) {
        RdpTicketDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.endJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketDO.getId(), RdpTicketStatus.CLOSED, null);

        RdpTicketProcessDO rdpTicketProcessDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        RdpExecStageContextMO mo;
        if (!StringUtils.isEmpty(rdpTicketProcessDO.getStageContext())) {
            mo = JsonUtils.toObj(rdpTicketProcessDO.getStageContext(), RdpExecStageContextMO.class);
        } else {
            mo = new RdpExecStageContextMO();
        }
        RdpUserDO rdpUserDO = rdpUserMapper.queryByUid(uid);
        mo.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_CLOSE_AT_CONSOLE_BY_END_JOB_MESSAGE.name(), rdpUserDO.getUsername()));

        this.rdpTicketProcessMapper.updateTicketStatusByEnum(rdpTicketProcessDO.getId(), RdpTicketProcessStatus.CLOSED, JsonUtils.toJson(mo));
    }

    @Override
    public void stopJob(String puid, String uid, long ticketId) {
        RdpTicketDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.stopJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);
    }

    @Override
    public List<DmBizLogVO> queryExecLog(String ownerUid, DmQueryExecLogFO fo) {
        DmAutoExecJobDO jobDO = checkJob(ownerUid, fo.getJobId());
        List<DmBizLogDO> dmBizLogDOS;
        if (fo.getDependBizType() == DmLogDependBizType.AUTO_EXEC_JOB) {
            dmBizLogDOS = this.dmBizLogMapper.queryListByBizId(jobDO.getBizId());
        } else {
            if (fo.getTaskId() == null) {
                throw new ErrorMessageException("taskId must not null");
            }
            DmAutoExecTaskDO execTaskDO = dmSqlTaskMapper.selectById(fo.getTaskId());
            dmBizLogDOS = this.dmBizLogMapper.queryListByBizId(execTaskDO.getBizId());
        }
        return dmBizLogDOS.stream().map((dmBizLogDO -> {
            DmBizLogVO vo = new DmBizLogVO();
            vo.setContent(dmBizLogDO.getContent());
            vo.setId(dmBizLogDO.getId());
            vo.setLogLevel(dmBizLogDO.getLogLevel());
            vo.setDependOnBizType(dmBizLogDO.getDependOnBizType());
            vo.setTime(DateFormatType.s_yyyyMMdd_HHmmss.format(dmBizLogDO.getGmtCreate()));
            return vo;
        })).collect(Collectors.toList());
    }

    private void checkJobOperationEnable(RdpTicketDO ticketDO, String uid) {
        if (!checkOperationEnableWithResult(ticketDO, uid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NO_PERMISSION_OPERATION_ERROR_MESSAGE.name()));
        }
    }

    private boolean checkOperationEnableWithResult(RdpTicketDO ticketDO, String uid) {
        RdpUserDO rdpUserDO = rdpUserMapper.queryByUid(uid);
        RdpRoleDO rdpRoleDO = rdpRoleMapper.selectById(rdpUserDO.getRoleId());
        if (rdpUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return true;
        }
        if (rdpRoleDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE) && ticketDO.getOwnerUid().equals(uid)) {
            return true;
        }

        List<RdpTicketApproPersonDO> rdpTicketApproPersonDOS = this.rdpUserMapper
            .queryApproPerson(AccountType.SUB_ACCOUNT, rdpUserDO.getParentId(), ticketDO.getBindDsId(), ticketDO.getTargetInfo());
        for (RdpTicketApproPersonDO rdpTicketApproPersonDO : rdpTicketApproPersonDOS) {
            if (rdpTicketApproPersonDO.getUid().equals(uid)) {
                return true;
            }
        }
        return false;
    }

    protected RdpTicketStatus statusFromConfirmAction(DmConfirmActionType actionType, DmAutoExecType autoExecType) {
        switch (actionType) {
            case REFUSE: {
                return RdpTicketStatus.REJECTED;
            }
            case CONFIRM: {
                if (autoExecType == DmAutoExecType.MANUAL_EXEC) {
                    return RdpTicketStatus.FINISHED;
                } else {
                    return RdpTicketStatus.WAIT_EXEC;
                }
            }
            default:
                throw new UnsupportedOperationException("Not supported confirm action type " + actionType.name());
        }
    }

    protected String execUserFromConfirmAction(DmConfirmActionType actionType, RdpUserDO confirmUser) {
        switch (actionType) {
            case REFUSE: {
                return null;
            }
            case CONFIRM: {
                return confirmUser.getUsername();
            }
            default:
                throw new UnsupportedOperationException("Not supported confirm action type " + actionType.name());
        }
    }

    private RdpTicketDO checkTicket(long ticketId, String puid) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        if (ticketDO == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
        }

        return ticketDO;
    }

    private DmAutoExecJobDO checkJob(String puid, Long jobId) {
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.selectById(jobId);
        if (jobDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_EXISTS_ERROR_MESSAGE.name()));
        }
        if (!jobDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_BELONG_CURRENT_TEAM.name()));
        }
        return jobDO;
    }
}
