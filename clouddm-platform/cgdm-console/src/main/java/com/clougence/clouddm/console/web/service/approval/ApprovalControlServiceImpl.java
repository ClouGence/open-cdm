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
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckContext;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesEngine;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.security.ListMyAuthTicketFO;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.clouddm.platform.dal.access.*;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.approval.ApprovalUrl;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleLevel;
import com.clougence.rdp.component.resulttask.AsyncTaskWithResultService;
import com.clougence.rdp.component.resulttask.TaskType;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.model.EnvTicketMO;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.future.CgFuture;
import com.fasterxml.jackson.core.type.TypeReference;

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
    private SystemDal                   systemDal;
    @Resource
    private MonitorDal                  monitorDal;
    @Resource
    private ExecutionDal                executionDal;
    @Resource
    private DataSourceDal               datasourceDal;
    @Resource
    private AuthDal                     authDal;
    @Resource
    private ApprovalDal                 approvalDal;
    @Resource
    private ObjectCacheDao              objectCacheDao;
    @Resource
    private QueryAnalysisService        queryAnalysisService;
    @Resource
    private DmDsConfigService           dmDsConfigService;
    @Resource
    private NamingDao               namingDao;
    @Resource
    private ApprovalFlowService         approvalFlowService;
    @Resource
    private DmAuthServiceForManage      authServiceForManage;
    @Resource
    private SecRulesEngine              ruleCheckService;
    @Resource
    private RdpDsEnvService             rdpDsEnvService;
    @Resource
    private DmEnvParamService           dmEnvParamService;
    @Resource
    private ApprovalProviderServiceImpl approvalService;
    @Resource
    private AutoExecService             autoExecService;
    @Resource
    private AsyncTaskWithResultService  asyncTaskWithResultService;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        DmDsDO dsDO = dsLevels.dsDO();
        DataSourceType dsType = dsDO.getDataSourceType();
        DmSysEnvDO envDO = this.systemDal.envMapper().queryByEnvID(puid, dsDO.getDsEnvId());

        // check approval
        DmEnvParamTicketDesVO ticketConfig = this.dmEnvParamService.querySqlTicketInfoParam(puid, dsDO.getDsEnvId());
        if (ticketConfig == null || !ticketConfig.isOpenTicket() || StringUtils.isBlank(ticketConfig.getType())) {
            String title = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TYPE_SQL_TITLE.name());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), title));
        }
        if (ticketConfig.isDelete()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name()));
        }
        ApprovalType approvalType = ApprovalType.valueOf(ticketConfig.getType());
        if (approvalType != ApprovalType.Internal) {
            DmApprovalTemplateDO templateDO = this.approvalService.checkApprovalAndReturnTemplate(puid, approvalType, ticketConfig.getTemplateId(), null);
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
        String bizId = this.namingDao.genTicketBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(puid);
        ticket.setBindDsId(dsDO.getId());
        ticket.setTargetInfo(targetInfo);
        ticket.setDescription(fo.getDescription());
        ticket.setTicketTitle(fo.getTicketTitle());
        ticket.setTicketStatus(ApprovalStatus.PRE_INIT);
        ticket.setApproBiz(ApprovalBiz.DM_QUERY);
        ticket.setStatusMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name()));
        ticket.setApproType(ApprovalType.valueOf(ticketConfig.getType()));
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

        if (ticket.getApproType() == ApprovalType.Internal) {
            DmApprovalPersonDO primary = new DmApprovalPersonDO();
            primary.setPersonUid(puid);
            primary.setTicketBzId(bizId);
            this.approvalDal.personMapper().insert(primary);
        }

        this.approvalDal.approvalMapper().insert(ticket);

        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DM_QUERY, ticketInfo.getMessage() == null);

        result.setTicketId(ticket.getId());
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void createAuthTicket(String ownerUid, String uid, RdpAddAuthTicketFO fo) {
        List<Long> dsIds = fo.getApplyAuths().stream().map(ApplyAuth::getResId).sorted().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dsIds)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_AUTH_TICKET_IS_EMPTY_MESSAGE.name()));
        }

        List<DmDsDO> dss = this.datasourceDal.dsMapper().listByIds(dsIds);
        Map<Long, List<Long>> groupByEnv = CollectionUtils.groupBy(dss, DmDsDO::getDsEnvId, DmDsDO::getId);

        for (Long envId : groupByEnv.keySet()) {
            RdpAddAuthTicketFO tfo = new RdpAddAuthTicketFO();
            tfo.setAuthKind(fo.getAuthKind());
            tfo.setApplyAuths(fo.getApplyAuths().stream().filter(a -> groupByEnv.get(envId).contains(a.getResId())).collect(Collectors.toList()));
            this.createAuthTicketItem(ownerUid, uid, tfo, envId);
        }
    }

    private void createAuthTicketItem(String ownerUid, String uid, RdpAddAuthTicketFO fo, long envId) {
        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);
        String bizId = this.namingDao.genTicketBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(ownerUid);
        ticket.setTargetInfo(DmI18nUtils.getMessage(I18nRdpLabelKeys.AUTH_TICKET_TARGET.name()));
        ticket.setDescription(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketTitle(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketStatus(ApprovalStatus.WAIT_APPROVAL);
        ticket.setApproBiz(ApprovalBiz.DATA_SOURCE_AUTH);

        DmSysEnvParamDO paramDO = this.systemDal.envParamMapper().queryByParamKey(ownerUid, EnvParamKeys.AUTH_TICKET_INFO, envId);
        if (paramDO != null) {
            EnvTicketMO ticketMO = JsonUtils.toObj(paramDO.getConfigValue(), EnvTicketMO.class);
            ticket.setApproType(ApprovalType.getByName(ticketMO.getApprovalType()));
            ticket.setApproTemplateIdentity(ticketMO.getTemplateId());
            ticket.setApproTemplateName(ticketMO.getTemplateName());

            if (ticket.getApproType() != ApprovalType.Internal) {
                DmApprovalTemplateDO templateDO = this.approvalFlowService.checkApprovalAndReturnTemplate(ownerUid, ticket.getApproType(), ticketMO.getTemplateId(), null);
                ticket.setApproTemplateName(templateDO.getTemplateName());
            }
        } else {
            ticket.setApproType(ApprovalType.Internal);
            ticket.setApproTemplateIdentity(ApprovalFlowService.INNER_TEMPLATE_ID);
            ticket.setApproTemplateName(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }

        this.fillAuthInfo(fo.getApplyAuths());

        DmAuthApprovalDO authTicket = new DmAuthApprovalDO();
        authTicket.setRdpTicketInsId(bizId);
        authTicket.setApplyAuthInfo(JsonUtils.toJson(fo));
        authTicket.setKindType(fo.getAuthKind());

        DmApprovalPersonDO primary = new DmApprovalPersonDO();
        primary.setPersonUid(ownerUid);
        primary.setTicketBzId(bizId);

        this.approvalDal.personMapper().insert(primary);
        this.approvalDal.approvalMapper().insert(ticket);
        this.authDal.approvalMapper().insert(authTicket);
        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DATA_SOURCE_AUTH, true);
    }

    @Override
    public RdpAuthTicketDetailVO queryAuthTicketDetail(String ownerUid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        DmAuthApprovalDO authTicketInfo = this.authDal.approvalMapper().getAuthTicketInfo(ticketDO.getBizId());
        RdpAddAuthTicketFO fo = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<RdpAddAuthTicketFO>() {});

        RdpAuthTicketDetailVO vo = new RdpAuthTicketDetailVO();
        vo.setApplyAuths(fo.getApplyAuths().stream().map(this::labelI18).collect(Collectors.toList()));
        vo.setAuthKind(fo.getAuthKind());
        return vo;
    }

    private ApplyAuth labelI18(ApplyAuth applyAuth) {
        List<AuthInfo> allAuthLabel = authServiceForManage.getAllAuthLabel(AuthKind.DataSource);
        Map<String, String> collect = allAuthLabel.stream().collect(Collectors.toMap(AuthInfo::getKey, AuthInfo::getKeyI18n));
        List<String> labels = new ArrayList<>();
        for (String authLabel : applyAuth.getAuthLabels()) {
            labels.add(DmI18nUtils.getMessage(collect.get(authLabel)));
        }

        applyAuth.setAuthLabels(labels);
        return applyAuth;
    }

    private List<ApplyAuth> fillAuthInfo(List<ApplyAuth> applyAuths) {
        Set<Long> dsIds = applyAuths.stream().map(ApplyAuth::getResId).collect(Collectors.toSet());
        if (dsIds.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_AUTH_TICKET_IS_EMPTY_MESSAGE.name()));
        }

        Map<Long, String> resInstIdMap = new HashMap<>();
        Map<Long, String> resDescMap = new HashMap<>();
        List<DmDsDO> dss = datasourceDal.dsMapper().listByIds(new ArrayList<>(dsIds));
        for (DmDsDO ds : dss) {
            resInstIdMap.put(ds.getId(), ds.getInstanceId());

            if (StringUtils.isBlank(ds.getInstanceDesc())) {
                resDescMap.put(ds.getId(), ds.getInstanceId());
            } else {
                resDescMap.put(ds.getId(), ds.getInstanceDesc());
            }
        }

        for (ApplyAuth applyAuth : applyAuths) {
            applyAuth.setResInstId(resInstIdMap.get(applyAuth.getResId()));
            applyAuth.setResDesc(resDescMap.get(applyAuth.getResId()));
        }

        return applyAuths;
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
        ApprovalStatus actionStatus = statusFromConfirmAction(fo.getConfirmActionType(), fo.getAutoExecConfig().getAutoExecType());

        checkJobOperationEnable(rdpTicketDO, fo.getConfirmUid());

        if (rdpTicketDO.getTicketStatus() != ApprovalStatus.WAIT_CONFIRM) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        DmApprovalDO dmTicketDO = this.approvalDal.approvalMapper().queryByBizId(rdpTicketDO.getBizId());
        if (dmTicketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        DmAuthUserDO confirmUser = this.authDal.userMapper().queryByUid(fo.getConfirmUid());
        ApprovalStageMO cContext = new ApprovalStageMO();
        cContext.setExecUserName(Collections.singletonList(confirmUser.getUsername()));
        if (StringUtils.isNotBlank(fo.getComment())) {
            cContext.setExecMsg(fo.getComment());
        }

        // update processDO
        DmApprovalProcessDO processDO = null;
        processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.CONFIRM);
        this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.FINISH, JsonUtils.toJson(cContext));

        // update processDO
        processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        String execUser = execUserFromConfirmAction(fo.getConfirmActionType(), confirmUser);
        ApprovalStageMO nContext = new ApprovalStageMO();
        if (fo.getAutoExecConfig().getAutoExecType() != AutoExecType.MANUAL_EXEC) {
            nContext.setAutoExecute(true);
        }
        nContext.setExecUserName(Collections.singletonList(execUser));
        if (actionStatus == ApprovalStatus.REJECTED) {
            processDO.setProcessStatus(ApprovalProcessStatus.REJECT);
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.REJECT, JsonUtils.toJson(nContext));
        } else if (actionStatus == ApprovalStatus.FINISHED) {
            processDO.setProcessStatus(ApprovalProcessStatus.FINISH);
            nContext.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_COMPLETE_MESSAGE.name()));
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.FINISH, JsonUtils.toJson(nContext));
        } else if (actionStatus == ApprovalStatus.WAIT_EXEC) {
            String ticketInfo = dmTicketDO.getTicketInfo();
            ApprovalMO info;
            if (StringUtils.isEmpty(ticketInfo)) {
                info = new ApprovalMO();
            } else {
                info = JsonUtils.toObj(ticketInfo, ApprovalMO.class);
            }
            info.setAutoExec(true);
            this.approvalDal.approvalMapper().updateTicketInfo(dmTicketDO.getId(), JsonUtils.toJson(info));
            createAutoExecJob(fo, rdpTicketDO, dmTicketDO, confirmUser);
            this.approvalDal.processMapper().updateContextById(processDO.getId(), JsonUtils.toJson(info));
        }
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, actionStatus, fo.getComment());
    }

    private void createAutoExecJob(DmConfirmTicketFO fo, DmApprovalDO rdpTicket, DmApprovalDO dmTicket, DmAuthUserDO confirmUser) {
        DsCacheEntry dsCacheEntry = objectCacheDao.queryByDsId(rdpTicket.getBindDsId());
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
        DmDsDO rdpDataSourceDO = this.datasourceDal.dsMapper().queryDsIdentityById(rdpTicket.getBindDsId());
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

        DmApprovalDO dmTicketDO = this.approvalDal.approvalMapper().queryByBizId(ticketDO.getBizId());
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
        Page<?> page = PageUtils.startPage(fo.getPage());
        ArgApprovalQueryObj queryParams = ArgApprovalQueryObj.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .uids(Collections.singletonList(fo.getUid()))
            .build();
        IPage<DmApprovalDO> tickets = this.approvalDal.approvalMapper().listAuthTicketByConditionAndPage(page, queryParams);
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
            DmDsDO dsDO = this.datasourceDal.dsMapper().queryDsIdentityById(approvalDO.getBindDsId());
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
        ApprovalStatus ticketStatus = approvalDO.getTicketStatus();
        vo.setTicketStatus(ticketStatus);

        List<DmApprovalProcessDO> processDOS = this.approvalDal.processMapper().listByTicketId(approvalDO.getId());
        List<RdpTicketProcessVO> processVOS = processDOS.stream().map(RdpConvertUtils::convertToTicketProcessVO).collect(Collectors.toList());
        List<DmApprovalPersonDO> persons = this.approvalDal.personMapper().queryByTicketBzId(approvalDO.getBizId());

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

        if (ticketStatus == ApprovalStatus.WAIT_CONFIRM) {
            if (approvalPersonList.contains(uid) || isPrimary) {
                vo.setCanExecute(true);
            }
        }

        if (approvalDO.getApproType() == ApprovalType.Internal && ticketStatus == ApprovalStatus.WAIT_APPROVAL) {
            if (approvalPersonList.contains(uid) || isPrimary) {
                vo.setCanApproval(true);
            }
        }

        vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getFinishTime()));
        vo.setTicketProcessVOList(processVOS);
        DmAuthUserDO userByUid = this.authDal.userMapper().queryByUid(approvalDO.getOwnerUid());
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
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(fo.getTicketId());
        if (ticketDO == null) {
            return;
        }
        if (fo.isRefreshCache() && ticketDO.getApproType() != ApprovalType.Internal && ticketDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {
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
        if (ticketDO.getApproType() != ApprovalType.Internal) {
            List<DmApprovalProcessActivityDO> activities = this.approvalDal.activityMapper().queryByTicketId(ticketDO.getId());

            for (RdpTicketProcessVO vo : dmTicketDetailVO.getTicketProcessVOList()) {
                Long ticketProcessId = vo.getTicketProcessId();
                List<RdpTicketActivityVO> list = new ArrayList<>();
                if (vo.getTicketProcessStatus() == ApprovalProcessStatus.FAIL) {
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
    public List<RdpApproTemplateVO> listTemplates(String ownerUid, ApprovalType approvalType) {
        return this.approvalService.listTemplates(ownerUid, approvalType);
    }

    @Override
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, ApprovalType approvalType) {
        return this.approvalService.refreshTemplates(ownerUid, approvalType);
    }

    @Override
    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        return this.approvalService.getTicketTypes(ownerUid);
    }

    @Override
    public void addTemplateByUrl(String ownerUid, ApprovalType approvalType, String templateUrl) {
        this.approvalService.addTemplateByUrl(ownerUid, approvalType, templateUrl);
    }

    @Override
    public void removeTemplateById(String ownerUid, ApprovalType approvalType, String templateId) {
        this.approvalService.removeTemplateById(ownerUid, approvalType, templateId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void retryJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.retryJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);

        approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_EXEC, null);
        approvalDal.processMapper().updateProcessStatusByTicketIdAndStage(ticketId, ApprovalStage.EXECUTION, ApprovalProcessStatus.INIT);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId(), puid);
        checkJobOperationEnable(ticketDO, uid);
        boolean jobFinish = this.autoExecService.skipTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId(), uid);
        if (jobFinish) {
            approvalDal.approvalMapper().updateStatusByEnum(fo.getTicketId(), ApprovalStatus.FINISHED, null);
            approvalDal.processMapper().updateProcessStatusByTicketIdAndStage(fo.getTicketId(), ApprovalStage.EXECUTION, ApprovalProcessStatus.FINISH);
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
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketDO.getId(), ApprovalStatus.CLOSED, null);

        DmApprovalProcessDO rdpTicketProcessDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        ApprovalStageMO mo;
        if (!StringUtils.isEmpty(rdpTicketProcessDO.getStageContext())) {
            mo = JsonUtils.toObj(rdpTicketProcessDO.getStageContext(), ApprovalStageMO.class);
        } else {
            mo = new ApprovalStageMO();
        }
        DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(uid);
        mo.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_CLOSE_AT_CONSOLE_BY_END_JOB_MESSAGE.name(), rdpUserDO.getUsername()));

        this.approvalDal.processMapper().updateTicketStatusByEnum(rdpTicketProcessDO.getId(), ApprovalProcessStatus.CLOSED, JsonUtils.toJson(mo));
    }

    @Override
    public void stopJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId, puid);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.stopJob(ticketDO.getBizId(), SQLJobBizType.TICKET, uid);
    }

    @Override
    public List<DmBizLogVO> queryExecLog(String ownerUid, DmQueryExecLogFO fo) {
        DmExecAutoJobDO jobDO = checkJob(ownerUid, fo.getJobId());
        List<DmMonBizLogDO> dmBizLogDOS;
        if (fo.getDependBizType() == LogDependBizType.AUTO_EXEC_JOB) {
            dmBizLogDOS = this.monitorDal.bizLogMapper().queryListByBizId(jobDO.getBizId());
        } else {
            if (fo.getTaskId() == null) {
                throw new ErrorMessageException("taskId must not null");
            }
            DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(fo.getTaskId());
            dmBizLogDOS = this.monitorDal.bizLogMapper().queryListByBizId(execTaskDO.getBizId());
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

        Map<Long, DmAuthUserDO> ticketUserMap = genTicketUserMap(records);
        Map<Long, DmDsDO> ticketDsMap = genTicketDsMap(records);

        this.rdpDsEnvService.fillDsEnvInfo(new ArrayList<>(ticketDsMap.values()));
        List<RdpTicketBasicVO> vos = new ArrayList<>();

        for (DmApprovalDO ticketDO : records) {
            RdpTicketBasicVO t;
            if (ticketDO.getApproBiz() == ApprovalBiz.DM_QUERY || ticketDO.getApproBiz() == ApprovalBiz.DM_CHANGE) {
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
        Page<?> page = PageUtils.startPage(fo.getPage());
        ArgApprovalQueryObj queryParams = ArgApprovalQueryObj.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .approvalPersonUid(fo.getUid())
            .build();
        return this.approvalDal.approvalMapper().listConfirmTicketByConditionAndPage(page, queryParams);
    }

    private Map<Long, DmAuthUserDO> genTicketUserMap(List<DmApprovalDO> tickets) {
        List<String> uids = tickets.stream().map(DmApprovalDO::getOwnerUid).collect(Collectors.toCollection(ArrayList::new));
        List<DmAuthUserDO> users = this.authDal.userMapper().listByUids(uids);
        Map<String, DmAuthUserDO> userMap = users.stream().collect(Collectors.toMap(DmAuthUserDO::getUid, u -> u));

        Map<Long, DmAuthUserDO> ticketUserMap = new HashMap<>();
        for (DmApprovalDO ticketDO : tickets) {
            String uid = ticketDO.getOwnerUid();
            ticketUserMap.put(ticketDO.getId(), userMap.get(uid));
        }
        return ticketUserMap;
    }

    private Map<Long, DmDsDO> genTicketDsMap(List<DmApprovalDO> tickets) {
        Set<Long> dsIds = tickets.stream().map(DmApprovalDO::getBindDsId).collect(Collectors.toSet());
        List<DmDsDO> dsList = this.datasourceDal.dsMapper().listByIdsIncludeDeleted(dsIds);
        Map<Long, DmDsDO> result = new HashMap<>();
        for (DmDsDO ds : dsList) {
            result.put(ds.getId(), ds);
        }

        Collection<Long> envIds = dsList.stream().map(DmDsDO::getDsEnvId).collect(Collectors.toSet());
        if (!envIds.isEmpty()) {
            List<DmSysEnvDO> envs = this.systemDal.envMapper().selectBatchIds(envIds);
            Map<Long, DmSysEnvDO> envMap = new HashMap<>();
            for (DmSysEnvDO env : envs) {
                envMap.put(env.getId(), env);
            }
            result.forEach((key, dsDo) -> dsDo.setDsEnvDO(envMap.get(dsDo.getDsEnvId())));
        }
        for (DmDsDO ds : dsList) {
            result.put(ds.getId(), ds);
        }
        return result;
    }

    private IPage<DmApprovalDO> getUserCreatedTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = PageUtils.startPage(fo.getPage());
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(fo.getUid());
        ArgApprovalQueryObj queryParams = ArgApprovalQueryObj.builder()
            .ticketStatus(fo.getTicketStatus())
            .uids(Collections.singletonList(String.valueOf(userDO.getUid())))
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();
        return this.approvalDal.approvalMapper().listTicketByConditionAndPage(page, queryParams, puid);
    }

    private IPage<DmApprovalDO> getAllTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = PageUtils.startPage(fo.getPage());
        ArgApprovalQueryObj queryParams = ArgApprovalQueryObj.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();

        return this.approvalDal.approvalMapper().listTicketByConditionAndPage(page, queryParams, puid);
    }

    private void checkJobOperationEnable(DmApprovalDO ticketDO, String uid) {
        if (!checkOperationEnableWithResult(ticketDO, uid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NO_PERMISSION_OPERATION_ERROR_MESSAGE.name()));
        }
    }

    private boolean checkOperationEnableWithResult(DmApprovalDO ticketDO, String uid) {
        DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(uid);
        DmAuthRoleDO rdpRoleDO = authDal.roleMapper().selectById(rdpUserDO.getRoleId());
        if (rdpUserDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            return true;
        }
        if (rdpRoleDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE) && ticketDO.getOwnerUid().equals(uid)) {
            return true;
        }

        List<RsAuthPersonObj> rdpTicketApproPersonDOS = this.authDal.userMapper()
            .queryApproPerson(AccountType.SUB_ACCOUNT, rdpUserDO.getParentId(), ticketDO.getBindDsId(), ticketDO.getTargetInfo());
        for (RsAuthPersonObj rdpTicketApproPersonDO : rdpTicketApproPersonDOS) {
            if (rdpTicketApproPersonDO.getUid().equals(uid)) {
                return true;
            }
        }
        return false;
    }

    protected ApprovalStatus statusFromConfirmAction(DmConfirmActionType actionType, AutoExecType autoExecType) {
        switch (actionType) {
            case REFUSE: {
                return ApprovalStatus.REJECTED;
            }
            case CONFIRM: {
                if (autoExecType == AutoExecType.MANUAL_EXEC) {
                    return ApprovalStatus.FINISHED;
                } else {
                    return ApprovalStatus.WAIT_EXEC;
                }
            }
            default:
                throw new UnsupportedOperationException("Not supported confirm action type " + actionType.name());
        }
    }

    protected String execUserFromConfirmAction(DmConfirmActionType actionType, DmAuthUserDO confirmUser) {
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
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        if (ticketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
        }

        return ticketDO;
    }

    private DmExecAutoJobDO checkJob(String puid, Long jobId) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(jobId);
        if (jobDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_EXISTS_ERROR_MESSAGE.name()));
        }
        if (!jobDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_BELONG_CURRENT_TEAM.name()));
        }
        return jobDO;
    }
}
