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
package com.clougence.clouddm.console.web.service.approval;

import static com.clougence.clouddm.console.web.util.RdpTimeUtil.getDateTimeOfTimestamp;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
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
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.*;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import com.clougence.clouddm.console.web.dal.model.queryobj.RdpTicketQueryObject;
import com.clougence.clouddm.console.web.model.fo.security.ListMyAuthTicketFO;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.system.NamingService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.RdpPageUtil;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.approval.ApprovalUrl;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.rdp.component.resulttask.AsyncTaskWithResultService;
import com.clougence.rdp.component.resulttask.TaskType;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.future.CgFuture;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 15:37
 */
@Service
@Slf4j
public class ApprovalControlServiceImpl implements ApprovalControlService {

    @Resource
    private QueryAnalysisService            queryAnalysisService;
    @Resource
    private DmDsConfigService               dmDsConfigService;
    @Resource
    private NamingService                   namingService;
    @Resource
    private ApprovalFlowService             approvalFlowService;
    @Resource
    private SecRulesEngine                  ruleCheckService;
    @Resource
    private DmApprovalMapper                approvalMapper;
    @Resource
    private RdpDataSourceMapper             rdpDataSourceMapper;
    @Resource
    private RdpUserMapper                   userMapper;
    @Resource
    private DmApprovalProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpDsEnvMapper                  dsEnvMapper;
    @Resource
    private RdpDsEnvService                 rdpDsEnvService;
    @Resource
    private DmEnvParamService               dmEnvParamService;
    @Resource
    private DmApprovalPersonMapper          personMapper;
    @Resource
    private ApprovalProviderServiceImpl     approvalService;
    @Resource
    private DmAutoExecTaskMapper            dmSqlTaskMapper;
    @Resource
    private DmAutoExecJobMapper             dmAutoExecJobMapper;
    @Resource
    private DmBizLogMapper                  dmBizLogMapper;
    @Resource
    private AutoExecService                 autoExecService;
    @Resource
    private RdpRoleMapper                   roleMapper;
    @Resource
    private BizResOwnerCacheService         bizResOwnerCacheService;
    @Resource
    private DmApprovalProcessActivityMapper approvalProcessActivityMapper;
    @Resource
    private AsyncTaskWithResultService      asyncTaskWithResultService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        RdpDataSourceDO dsDO = dsLevels.dsDO();
        DataSourceType dsType = dsDO.getDataSourceType();
        RdpDsEnvDO envDO = this.dsEnvMapper.queryByEnvID(puid, dsDO.getDsEnvId());

        // check approval
        DmEnvParamTicketDesVO ticketConfig = this.dmEnvParamService.querySqlTicketInfoParam(puid, dsDO.getDsEnvId());
        if (ticketConfig == null || !ticketConfig.isOpenTicket() || StringUtils.isBlank(ticketConfig.getType())) {
            String title = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TYPE_SQL_TITLE.name());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), title));
        }
        if (ticketConfig.isDelete()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name()));
        }
        RdpApprovalType approvalType = RdpApprovalType.valueOf(ticketConfig.getType());
        if (approvalType != RdpApprovalType.Internal) {
            DmApprovalCacheTemplateDO templateDO = this.approvalService.checkApprovalAndReturnTemplate(puid, approvalType, ticketConfig.getTemplateId(), null);
            ticketConfig.setTemplateName(templateDO.getTemplateName());// update form cache.
        }

        // rule check
        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();
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
        ApprovalMO ticketInfo = new ApprovalMO();
        int totalCount = this.analysisSqlAndCheckResource(fo, dsType, dsLevels, ticketInfo);
        if (!fo.isForce()) {
            if (result.isFailure() || result.isConfirm()) {
                return result;
            }
        } else {
            result = new DmTicketResultVO();
        }

        // query env bind param
        String targetInfo = "/" + dsLevels.dsDO().getInstanceId();
        if (dsLevels.levelsDef().contains(UmiTypes.Catalog)) {
            targetInfo += String.format("/%s/%s", levelsParam.get(UmiTypes.Catalog), levelsParam.get(UmiTypes.Schema));
        } else {
            targetInfo += String.format("/%s", levelsParam.get(UmiTypes.Schema));
        }

        // RDP ticket ins
        String bizId = this.namingService.genTicketBizId();
        DmApprovalDO ticket = new DmApprovalDO();
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

        ticket.setRawSql(fo.getRawSql());
        ticket.setTotalCount(totalCount);
        ticket.setExpectedAffectedRows(fo.getExpectedAffectedRows());
        ticket.setTicketInfo(JsonUtils.toJson(ticketInfo));
        ticket.setLevels(dsLevels.dbLevels());
        if (StringUtils.isNotBlank(fo.getRollBackSql())) {
            ticket.setRollBackSql(fo.getRollBackSql());
        }
        ticket.setCheckedInfo(JsonUtils.toJson(result.getCheckedVOS()));

        if (ticket.getApproType() == RdpApprovalType.Internal) {
            DmApprovalPersonDO primary = new DmApprovalPersonDO();
            primary.setPersonUid(puid);
            primary.setTicketBzId(bizId);
            this.personMapper.insert(primary);
        }

        this.approvalMapper.insert(ticket);

        this.approvalFlowService.createProcess(ticket.getId(), RdpApprovalBiz.DM_QUERY, ticketInfo.getMessage() == null);

        result.setTicketId(ticket.getId());
        return result;
    }

    private int analysisSqlAndCheckResource(DmAddTicketFO fo, DataSourceType dsType, DsLevels dsLevels, ApprovalMO ticketInfo) {
        int totalCount = 1;
        try {
            List<SplitScript> sqlList = this.queryAnalysisService.analysisSplit(dsType, fo.getRawSql(), null, 1, 0);
            totalCount = sqlList.size();
            // check resource match fo.levels
            Map<String, Object> params = new HashMap<>();
            dsLevels.levelsParam().forEach((umiType, value) -> {
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
            DataSourceConfig dataSourceConfig = dmDsConfigService.fetchDsConfigFromDM(dsLevels.dsDO().getId(), dsLevels.dsDO().getDataSourceType());
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
        DmApprovalDO rdpTicketDO = this.checkTicket(ticketId, puid);
        RdpTicketStatus actionStatus = statusFromConfirmAction(fo.getConfirmActionType(), fo.getAutoExecConfig().getAutoExecType());

        checkJobOperationEnable(rdpTicketDO, fo.getConfirmUid());

        if (rdpTicketDO.getTicketStatus() != RdpTicketStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        DmApprovalDO dmTicketDO = this.approvalMapper.queryByBizId(rdpTicketDO.getBizId());
        if (dmTicketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        RdpUserDO confirmUser = this.userMapper.queryByUid(fo.getConfirmUid());
        ApprovalStageMO cContext = new ApprovalStageMO();
        cContext.setExecUserName(Collections.singletonList(confirmUser.getUsername()));
        if (StringUtils.isNotBlank(fo.getComment())) {
            cContext.setExecMsg(fo.getComment());
        }

        // update processDO
        DmApprovalProcessDO processDO = null;
        processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.CONFIRM);
        this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(cContext));

        // update processDO
        processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        String execUser = execUserFromConfirmAction(fo.getConfirmActionType(), confirmUser);
        ApprovalStageMO nContext = new ApprovalStageMO();
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
            ApprovalMO info;
            if (StringUtils.isEmpty(ticketInfo)) {
                info = new ApprovalMO();
            } else {
                info = JsonUtils.toObj(ticketInfo, ApprovalMO.class);
            }
            info.setAutoExec(true);
            this.approvalMapper.updateTicketInfo(dmTicketDO.getId(), JsonUtils.toJson(info));
            createAutoExecJob(fo, rdpTicketDO, dmTicketDO, confirmUser);
            this.rdpTicketProcessMapper.updateContextById(processDO.getId(), JsonUtils.toJson(info));
        }
        this.approvalMapper.updateTicketStatusByEnum(ticketId, actionStatus, fo.getComment());
    }

    private void createAutoExecJob(DmConfirmTicketFO fo, DmApprovalDO rdpTicket, DmApprovalDO dmTicket, RdpUserDO confirmUser) {
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

        RdbSupportSpi rdbSupportSpi = PluginManager.findRdbSupportSpi(dsLevels.dsDO().getDataSourceType());
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
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
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

        DmApprovalDO dmTicketDO = this.approvalMapper.queryByBizId(ticketDO.getBizId());
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
            ApprovalMO ticketInfo = JsonUtils.toObj(dmTicketDO.getTicketInfo(), ApprovalMO.class);
            String message = ticketInfo.getMessage();
            vo.setTicketMessage(message);
            vo.setAutoExec(ticketInfo.isAutoExec());
        }
        vo.setCheckedList(JsonUtils.toListUseType(dmTicketDO.getCheckedInfo(), CheckedVO.class));
        return vo;
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String puid, String uid, DmQueryTaskListFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        return this.autoExecService
            .queryAutoExecTaskList(ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid), fo.getTaskStatus(), fo.getPage());
    }

    @Override
    public DmAutoExecJobVO queryExecJobInfo(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
        return this.autoExecService.queryAutoExecJob(ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid));
    }

    @Override
    public IPage<RdpTicketBasicVO> queryAuthTicketListByPage(String puid, ListMyAuthTicketFO fo) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .uids(Collections.singletonList(fo.getUid()))
            .build();
        IPage<DmApprovalDO> tickets = this.approvalMapper.listAuthTicketByConditionAndPage(page, queryParams);
        return convertAndFillExtraInfo(tickets);
    }

    @Override
    public IPage<RdpTicketBasicVO> queryTicketListByPage(String puid, RdpListTicketFO fo) {
        IPage<DmApprovalDO> tickets;
        switch (fo.getTicketListType()) {
            case SELF_CREATE: {
                tickets = getUserCreatedTicketsByPage(fo, puid);
                break;
            }
            case WAIT_SELF_PROCESS: {
                tickets = getCanConfirmTicketsByPage(fo);
                break;
            }
            case ALL: {
                tickets = getAllTicketsByPage(fo, puid);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported list type " + fo.getTicketListType());
        }
        return convertAndFillExtraInfo(tickets);
    }

    @Override
    public RdpTicketBaseInfoVO queryTicketBaseInfo(String puid, String uid, RdpQueryTicketDetailFO fo) {
        updateStatusFromThirdPartyIfNecessary(fo);
        DmApprovalDO approvalDO = checkTicket(fo.getTicketId(), puid);

        boolean isPrimary = uid.equals(puid);
        boolean isOwn = uid.equals(approvalDO.getOwnerUid());

        RdpTicketBaseInfoVO vo = new RdpTicketBaseInfoVO();
        vo.setId(approvalDO.getId());
        vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getGmtCreate()));
        vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getGmtModified()));
        vo.setDataSourceId(approvalDO.getBindDsId());
        if (approvalDO.getBindDsId() != null) {
            RdpDataSourceDO dsDO = this.rdpDataSourceMapper.queryDsIdentityById(approvalDO.getBindDsId());
            if (dsDO != null) {
                vo.setDataSourceType(dsDO.getDataSourceType());
                vo.setDsDeployType(dsDO.getDeployType());
            }
        }
        vo.setTargetInfo(approvalDO.getTargetInfo());
        vo.setApproType(approvalDO.getApproType());
        vo.setApproBiz(approvalDO.getApproBiz());
        vo.setApproIdentity(approvalDO.getApproIdentity());
        vo.setApproTemplateName(approvalDO.getApproTemplateName());
        vo.setDescription(approvalDO.getDescription());
        vo.setStatusMessage(approvalDO.getStatusMessage());
        vo.setTicketTitle(approvalDO.getTicketTitle());
        vo.setDsEnvName(approvalDO.getEnvName());
        RdpTicketStatus ticketStatus = approvalDO.getTicketStatus();
        vo.setTicketStatus(ticketStatus);

        List<DmApprovalProcessDO> processDOS = this.rdpTicketProcessMapper.listByTicketId(approvalDO.getId());
        List<RdpTicketProcessVO> processVOS = processDOS.stream().map(RdpConvertUtils::convertToTicketProcessVO).collect(Collectors.toList());
        List<DmApprovalPersonDO> persons = this.personMapper.queryByTicketBzId(approvalDO.getBizId());

        List<String> approvalPersonList = new ArrayList<>();
        persons.forEach(person -> approvalPersonList.add(person.getPersonUid()));

        switch (ticketStatus) {
            case PRE_INIT:
            case WAIT_CONFIRM:
            case WAIT_APPROVAL: {
                if (isPrimary || isOwn) {
                    vo.setCanClose(true);
                }
                break;
            }
            default:
                break;
        }

        if (ticketStatus == RdpTicketStatus.WAIT_CONFIRM) {
            if (approvalPersonList.contains(uid) || isPrimary) {
                vo.setCanExecute(true);
            }
        }

        if (approvalDO.getApproType() == RdpApprovalType.Internal && ticketStatus == RdpTicketStatus.WAIT_APPROVAL) {
            if (approvalPersonList.contains(uid) || isPrimary) {
                vo.setCanApproval(true);
            }
        }

        vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getFinishTime()));
        vo.setTicketProcessVOList(processVOS);
        RdpUserDO userByUid = this.userMapper.queryByUid(approvalDO.getOwnerUid());
        if (userByUid == null) {
            vo.setUserName(approvalDO.getOwnerUid() + "(" + DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()) + ")");
        } else {
            vo.setUserName(userByUid.getUsername());
        }

        vo.setApproComment(approvalDO.getApproComment());
        thirdPartyApprovalHandle(vo, approvalDO);

        return vo;
    }

    private void updateStatusFromThirdPartyIfNecessary(RdpQueryTicketDetailFO fo) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(fo.getTicketId());
        if (ticketDO == null) {
            return;
        }
        if (fo.isRefreshCache() && ticketDO.getApproType() != RdpApprovalType.Internal && ticketDO.getTicketStatus() == RdpTicketStatus.WAIT_APPROVAL) {
            CgFuture<Boolean> cgFuture = asyncTaskWithResultService.submitTask(TaskType.getKey(TaskType.APPROVAL_LAST_STATUS, ticketDO.getId()), () -> refreshCache(ticketDO));
            try {
                cgFuture.get(2, java.util.concurrent.TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("call " + ticketDO.getApproType() + " api is running");
            }
        }
    }

    @Transactional
    public boolean refreshCache(DmApprovalDO ticketDO) {
        if (StringUtils.isEmpty(ticketDO.getApproIdentity())) {
            return false;
        }
        try {
            approvalService.refreshApprovalStatus(ticketDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() != ThirdPartyApiErrorType.CONNECTION_ERROR) {
                this.approvalFlowService.failTicket(ticketDO.getId(), DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), ticketDO.getPrimaryUid());
            }
            return false;
        }

        return true;
    }

    private void thirdPartyApprovalHandle(RdpTicketBaseInfoVO dmTicketDetailVO, DmApprovalDO ticketDO) {
        if (ticketDO.getApproType() != RdpApprovalType.Internal) {
            List<DmApprovalProcessActivityDO> activities = this.approvalProcessActivityMapper.queryByTicketId(ticketDO.getId());

            for (RdpTicketProcessVO vo : dmTicketDetailVO.getTicketProcessVOList()) {
                Long ticketProcessId = vo.getTicketProcessId();
                List<RdpTicketActivityVO> list = new ArrayList<>();
                if (vo.getTicketProcessStatus() == RdpTicketProcessStatus.FAIL) {
                    continue;
                }
                for (DmApprovalProcessActivityDO activity : activities) {
                    if (activity.getProcessId().equals(ticketProcessId)) {
                        list.addAll(RdpConvertUtils.convertToTicketActivityVO(vo.getTicketProcessStatus(), activity));
                    }
                }
                if (!list.isEmpty()) {
                    list.sort((a, b) -> {
                        if (a.getFinishTime() == null && b.getFinishTime() != null) {
                            return 1;
                        } else if (a.getFinishTime() != null) {
                            if (b.getFinishTime() == null) {
                                return -1;
                            }
                            return a.getFinishTime().compareTo(b.getFinishTime());
                        } else if (a.getStartTime() != null && b.getStartTime() != null) {
                            return a.getStartTime().compareTo(b.getStartTime());
                        } else {
                            return 0;
                        }
                    });
                    vo.setActivityList(list);
                    vo.setHasActivity(true);
                }
            }
            String approvalUrl = ticketDO.getApprovalUrl();
            if (StringUtils.isNotEmpty(approvalUrl)) {
                ApprovalUrl urlDTO = JsonUtils.toObj(approvalUrl, ApprovalUrl.class);
                dmTicketDetailVO.setPcUrl(urlDTO.getPcUrl());
                dmTicketDetailVO.setMobileUrl(urlDTO.getMobileUrl());
            }
        } else {
            dmTicketDetailVO.setApproTypeName(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }
    }

    @Override
    public List<RdpApproTemplateVO> listTemplates(String ownerUid, RdpApprovalType approvalType) {
        return this.approvalService.listTemplates(ownerUid, approvalType);
    }

    @Override
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, RdpApprovalType approvalType) {
        return this.approvalService.refreshTemplates(ownerUid, approvalType);
    }

    @Override
    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        return this.approvalService.getTicketTypes(ownerUid);
    }

    @Override
    public void addTemplateByUrl(String ownerUid, RdpApprovalType approvalType, String templateUrl) {
        this.approvalService.addTemplateByUrl(ownerUid, approvalType, templateUrl);
    }

    @Override
    public void removeTemplateById(String ownerUid, RdpApprovalType approvalType, String templateId) {
        this.approvalService.removeTemplateById(ownerUid, approvalType, templateId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void retryJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.retryJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);

        approvalMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_EXEC, null);
        rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(ticketId, RdpTicketStage.EXECUTION, RdpTicketProcessStatus.INIT);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        checkJobOperationEnable(ticketDO, uid);
        boolean jobFinish = this.autoExecService.skipTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId(), uid);
        if (jobFinish) {
            approvalMapper.updateTicketStatusByEnum(fo.getTicketId(), RdpTicketStatus.FINISHED, null);
            rdpTicketProcessMapper.updateProcessStatusByTicketIdAndStage(fo.getTicketId(), RdpTicketStage.EXECUTION, RdpTicketProcessStatus.FINISH);
        }
    }

    @Override
    public void canceledSkipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        checkJobOperationEnable(ticketDO, uid);
        this.autoExecService.continueTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void endAutoExecJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.endJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);
        this.approvalMapper.updateTicketStatusByEnum(ticketDO.getId(), RdpTicketStatus.CLOSED, null);

        DmApprovalProcessDO rdpTicketProcessDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        ApprovalStageMO mo;
        if (!StringUtils.isEmpty(rdpTicketProcessDO.getStageContext())) {
            mo = JsonUtils.toObj(rdpTicketProcessDO.getStageContext(), ApprovalStageMO.class);
        } else {
            mo = new ApprovalStageMO();
        }
        RdpUserDO rdpUserDO = userMapper.queryByUid(uid);
        mo.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_CLOSE_AT_CONSOLE_BY_END_JOB_MESSAGE.name(), rdpUserDO.getUsername()));

        this.rdpTicketProcessMapper.updateTicketStatusByEnum(rdpTicketProcessDO.getId(), RdpTicketProcessStatus.CLOSED, JsonUtils.toJson(mo));
    }

    @Override
    public void stopJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
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

    private IPage<RdpTicketBasicVO> convertAndFillExtraInfo(IPage<DmApprovalDO> tickets) {
        List<DmApprovalDO> records = tickets.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        Map<Long, RdpUserDO> ticketUserMap = genTicketUserMap(records);
        Map<Long, RdpDataSourceDO> ticketDsMap = genTicketDsMap(records);

        this.rdpDsEnvService.fillDsEnvInfo(new ArrayList<>(ticketDsMap.values()));
        List<RdpTicketBasicVO> vos = new ArrayList<>();

        for (DmApprovalDO ticketDO : records) {
            RdpTicketBasicVO t;
            if (ticketDO.getApproBiz() == RdpApprovalBiz.DM_QUERY || ticketDO.getApproBiz() == RdpApprovalBiz.DM_CHANGE) {
                t = RdpTicketBasicVO.generateVO(ticketDO, ticketDsMap.get(ticketDO.getBindDsId()).getDataSourceType().getTypeName(), ticketUserMap.get(ticketDO.getId()));
            } else {
                t = RdpTicketBasicVO.generateVO(ticketDO, ticketDO.getApproBiz().name(), ticketUserMap.get(ticketDO.getId()));
            }
            vos.add(t);
        }
        vos.sort((o1, o2) -> -o1.getGmtCreate().compareTo(o2.getGmtCreate()));

        IPage<RdpTicketBasicVO> results = new Page<>();
        results.setRecords(vos);
        results.setCurrent(tickets.getCurrent());
        results.setSize(tickets.getSize());
        results.setPages(tickets.getPages());
        results.setTotal(tickets.getTotal());
        return results;
    }

    private IPage<DmApprovalDO> getCanConfirmTicketsByPage(RdpListTicketFO fo) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .approvalPersonUid(fo.getUid())
            .build();
        return this.approvalMapper.listConfirmTicketByConditionAndPage(page, queryParams);
    }

    private Map<Long, RdpUserDO> genTicketUserMap(List<DmApprovalDO> tickets) {
        List<String> uids = tickets.stream().map(DmApprovalDO::getOwnerUid).collect(Collectors.toCollection(ArrayList::new));
        List<RdpUserDO> users = this.userMapper.listByUids(uids);
        Map<String, RdpUserDO> userMap = users.stream().collect(Collectors.toMap(RdpUserDO::getUid, u -> u));

        Map<Long, RdpUserDO> ticketUserMap = new HashMap<>();
        for (DmApprovalDO ticketDO : tickets) {
            String uid = ticketDO.getOwnerUid();
            ticketUserMap.put(ticketDO.getId(), userMap.get(uid));
        }
        return ticketUserMap;
    }

    private Map<Long, RdpDataSourceDO> genTicketDsMap(List<DmApprovalDO> tickets) {
        Set<Long> dsIds = tickets.stream().map(DmApprovalDO::getBindDsId).collect(Collectors.toSet());
        List<RdpDataSourceDO> dsList = this.rdpDataSourceMapper.listByIdsIncludeDeleted(dsIds);
        Map<Long, RdpDataSourceDO> result = new HashMap<>();
        for (RdpDataSourceDO ds : dsList) {
            result.put(ds.getId(), ds);
        }

        Collection<Long> envIds = dsList.stream().map(RdpDataSourceDO::getDsEnvId).collect(Collectors.toSet());
        if (!envIds.isEmpty()) {
            List<RdpDsEnvDO> envs = this.dsEnvMapper.selectBatchIds(envIds);
            Map<Long, RdpDsEnvDO> envMap = new HashMap<>();
            for (RdpDsEnvDO env : envs) {
                envMap.put(env.getId(), env);
            }
            result.forEach((key, dsDo) -> dsDo.setDsEnvDO(envMap.get(dsDo.getDsEnvId())));
        }
        for (RdpDataSourceDO ds : dsList) {
            result.put(ds.getId(), ds);
        }
        return result;
    }

    private IPage<DmApprovalDO> getUserCreatedTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpUserDO userDO = this.userMapper.queryByUid(fo.getUid());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .uids(Collections.singletonList(String.valueOf(userDO.getUid())))
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();
        return this.approvalMapper.listTicketByConditionAndPage(page, queryParams, puid);
    }

    private IPage<DmApprovalDO> getAllTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();

        return this.approvalMapper.listTicketByConditionAndPage(page, queryParams, puid);
    }

    private void checkJobOperationEnable(DmApprovalDO ticketDO, String uid) {
        if (!checkOperationEnableWithResult(ticketDO, uid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NO_PERMISSION_OPERATION_ERROR_MESSAGE.name()));
        }
    }

    private boolean checkOperationEnableWithResult(DmApprovalDO ticketDO, String uid) {
        RdpUserDO rdpUserDO = userMapper.queryByUid(uid);
        RdpRoleDO rdpRoleDO = roleMapper.selectById(rdpUserDO.getRoleId());
        if (rdpUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return true;
        }
        if (rdpRoleDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE) && ticketDO.getOwnerUid().equals(uid)) {
            return true;
        }

        List<RdpTicketApproPersonDO> rdpTicketApproPersonDOS = this.userMapper
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

    private DmApprovalDO checkTicket(long ticketId, String puid) {
        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketId);
        if (ticketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
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
