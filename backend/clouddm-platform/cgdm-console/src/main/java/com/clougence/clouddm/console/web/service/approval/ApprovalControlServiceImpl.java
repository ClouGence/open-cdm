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

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.AnalysisRuleOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.approval.schedule.ApprovalTaskScheduler;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.autoexec.model.AutoExecJobCreateRequest;
import com.clougence.clouddm.console.web.component.detectrule.SecRulesCheckResult;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.service.upload.impl.SqlFilePreviewReader;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmTeamUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
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
import com.clougence.clouddm.platform.dal.model.system.DmSysAttachmentDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvParamDO;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.clouddm.sdk.approval.ApprovalUrl;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
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

    private static final int            AUTO_EXEC_TASK_SQL_SUMMARY_LENGTH = 200;

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
    private NamingDao                   namingDao;
    @Resource
    private DmAuthServiceForManage      authServiceForManage;
    @Resource
    private DmEnvParamService           dmEnvParamService;
    @Resource
    private AutoExecService             autoExecService;
    @Resource
    private AsyncTaskWithResultService  asyncTaskWithResultService;
    @Resource
    private ApprovalFlowService         approvalFlowService;
    @Resource
    private ApprovalService             approvalService;
    @Resource
    private ApprovalProviderServiceImpl approvalProviderService;
    @Resource
    private ApprovalTaskScheduler       approvalTaskScheduler;
    @Resource
    private PlatformTransactionManager  txManager;

    //
    // ticket list
    //

    @Override
    public DmPageVO<RdpTicketBasicVO> queryTicketListByPage(String puid, RdpListTicketFO fo) {
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

    private DmPageVO<RdpTicketBasicVO> convertAndFillExtraInfo(IPage<DmApprovalDO> tickets) {
        DmPageVO<RdpTicketBasicVO> results = new DmPageVO<>(tickets);
        List<DmApprovalDO> records = tickets.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return results;
        }

        List<String> uids = records.stream().map(DmApprovalDO::getOwnerUid).collect(Collectors.toCollection(ArrayList::new));
        List<DmAuthUserDO> users = this.authDal.userMapper().listByUids(uids);
        Map<String, DmAuthUserDO> userMap = users.stream().collect(Collectors.toMap(DmAuthUserDO::getUid, u -> u));
        Map<Long, DmAuthUserDO> ticketUserMap = new HashMap<>();
        for (DmApprovalDO ticketDO : records) {
            ticketUserMap.put(ticketDO.getId(), userMap.get(ticketDO.getOwnerUid()));
        }

        Set<Long> dsIds = records.stream().map(DmApprovalDO::getBindDsId).collect(Collectors.toSet());
        List<DmDsDO> dsList = this.datasourceDal.dsMapper().listByIdsIncludeDeleted(dsIds);
        Map<Long, DmDsDO> ticketDsMap = new HashMap<>();
        for (DmDsDO ds : dsList) {
            ticketDsMap.put(ds.getId(), ds);
        }

        Collection<Long> envIds = dsList.stream().map(DmDsDO::getDsEnvId).collect(Collectors.toSet());
        if (!envIds.isEmpty()) {
            List<DmSysEnvDO> envs = this.systemDal.envMapper().selectBatchIds(envIds);
            Map<Long, DmSysEnvDO> envMap = new HashMap<>();
            for (DmSysEnvDO env : envs) {
                envMap.put(env.getId(), env);
            }
            ticketDsMap.forEach((key, dsDo) -> dsDo.setDsEnvDO(envMap.get(dsDo.getDsEnvId())));
        }

        List<RdpTicketBasicVO> vos = new ArrayList<>();
        for (DmApprovalDO tdo : records) {
            RdpTicketBasicVO t;
            if (tdo.getApproBiz() == ApprovalBiz.DM_QUERY || tdo.getApproBiz() == ApprovalBiz.DM_CHANGE) {
                t = RdpConvertUtils.convertToTicketBasicVO(tdo, ticketDsMap.get(tdo.getBindDsId()).getDataSourceType().getTypeName(), ticketUserMap.get(tdo.getId()));
            } else {
                t = RdpConvertUtils.convertToTicketBasicVO(tdo, tdo.getApproBiz().name(), ticketUserMap.get(tdo.getId()));
            }
            vos.add(t);
        }

        vos.sort((o1, o2) -> -o1.getGmtCreate().compareTo(o2.getGmtCreate()));

        results.setRecords(vos);
        return results;
    }

    @Override
    public RdpTicketBaseInfoVO queryTicketBaseInfo(String puid, String uid, RdpQueryTicketDetailFO fo) {
        DmApprovalDO cachedTicketDO = this.approvalDal.approvalMapper().queryById(fo.getTicketId());
        if (cachedTicketDO != null &&   //
            fo.isRefreshCache() &&      //
            cachedTicketDO.getApproType() != ApprovalType.Internal &&//
            cachedTicketDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {

            CgFuture<Boolean> cgFuture = this.asyncTaskWithResultService.submitTask(        //
                    TaskType.getKey(TaskType.APPROVAL_LAST_STATUS, cachedTicketDO.getId()), //
                    () -> refreshCache(cachedTicketDO));

            try {
                cgFuture.get(2, java.util.concurrent.TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("call " + cachedTicketDO.getApproType() + " api is running");
            }
        }

        //
        DmApprovalDO approvalDO = checkTicket(fo.getTicketId());
        RdpTicketBaseInfoVO vo = new RdpTicketBaseInfoVO();
        vo.setId(approvalDO.getId());
        vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getGmtCreate()));
        vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(approvalDO.getGmtModified()));
        vo.setDataSourceId(approvalDO.getBindDsId());
        if (approvalDO.getBindDsId() != null) {
            DmDsDO dsDO = this.datasourceDal.dsMapper().queryDsIdentityById(approvalDO.getBindDsId());
            if (dsDO != null) {
                vo.setDataSourceType(dsDO.getDataSourceType());
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

        //
        boolean isPrimary = uid.equals(puid);
        boolean isOwn = uid.equals(approvalDO.getOwnerUid());
        switch (ticketStatus) {
            case PRE_INIT_WAIT:
            case PRE_INIT_RUN:
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
        List<DmApprovalProcessActivityDO> activities = this.approvalDal.activityMapper().queryByTicketId(approvalDO.getId());
        for (RdpTicketProcessVO processVO : vo.getTicketProcessVOList()) {
            List<RdpTicketActivityVO> vos;
            if (processVO.getTicketStage() == ApprovalStage.EXPLAIN) {
                vos = this.convertAnalysisActivities(processVO, activities);
            } else if (approvalDO.getApproType() != ApprovalType.Internal && processVO.getTicketProcessStatus() != ApprovalProcessStatus.FAIL) {
                vos = this.convertApprovalActivities(processVO, activities);
            } else {
                continue;
            }
            if (!vos.isEmpty()) {
                processVO.setActivityList(vos);
                processVO.setHasActivity(true);
            }
        }

        if (approvalDO.getApproType() != ApprovalType.Internal) {
            String approvalUrl = approvalDO.getApprovalUrl();
            if (StringUtils.isNotEmpty(approvalUrl)) {
                ApprovalUrl urlDTO = JsonUtils.toObj(approvalUrl, ApprovalUrl.class);
                vo.setPcUrl(urlDTO.getPcUrl());
                vo.setMobileUrl(urlDTO.getMobileUrl());
            }
        } else {
            vo.setApproTypeName(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }

        return vo;
    }

    private List<RdpTicketActivityVO> convertAnalysisActivities(RdpTicketProcessVO processVO, List<DmApprovalProcessActivityDO> activities) {
        List<RdpTicketActivityVO> vos = new ArrayList<>();
        for (DmApprovalProcessActivityDO activity : activities) {
            if (ApprovalAnalysisStateMO.TYPE_SQL_RECOGNITION.equals(activity.getActivityId())) {
                continue;
            }
            if (activity.getProcessId().equals(processVO.getTicketProcessId()) && StringUtils.isNotBlank(activity.getContext())) {
                ApprovalAnalysisStateMO state = JsonUtils.toObj(activity.getContext(), ApprovalAnalysisStateMO.class);
                RdpTicketActivityVO vo = RdpConvertUtils.convertToAnalysisActivityVO(state);
                if (vo.getDisplayOrder() == null) {
                    vo.setDisplayOrder(activity.getOrderNumber());
                }
                vos.add(vo);
            }
        }
        return vos;
    }

    private List<RdpTicketActivityVO> convertApprovalActivities(RdpTicketProcessVO processVO, List<DmApprovalProcessActivityDO> activities) {
        List<RdpTicketActivityVO> vos = new ArrayList<>();
        for (DmApprovalProcessActivityDO activity : activities) {
            if (activity.getProcessId().equals(processVO.getTicketProcessId())) {
                vos.addAll(RdpConvertUtils.convertToTicketActivityVO(processVO.getTicketProcessStatus(), activity));
            }
        }
        vos.sort((a, b) -> {
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
        return vos;
    }

    @Override
    public DmQueryTicketVO queryTicketDetail(String puid, DmQueryTicketDetailFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId());
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

        DmApprovalDO approvalDO = this.approvalDal.approvalMapper().queryByBizIdWithoutRawSql(ticketDO.getBizId());
        if (approvalDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NOT_FOUND_ERROR.name()));
        }

        // key is ticket id
        DmQueryTicketVO vo = new DmQueryTicketVO();
        vo.setContentType(approvalDO.getContentType());
        if (approvalDO.getContentType() == SqlContentType.ATTACHMENT) {
            DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectConfirmedByApprovalId(approvalDO.getId());
            if (attachment != null) {
                vo.setAttachmentId(attachment.getId());
                vo.setAttachmentFileName(attachment.getFileName());
                vo.setAttachmentFileSize(attachment.getFileSize());
            }
        }

        vo.setRollBackSql(approvalDO.getRollBackSql());
        vo.setExpectedAffectedRows(approvalDO.getExpectedAffectedRows());
        this.fillAnalysisDetail(vo, approvalDO.getId());

        if (StringUtils.isNotEmpty(approvalDO.getTicketInfo())) {
            ApprovalMO ticketInfo = JsonUtils.toObj(approvalDO.getTicketInfo(), ApprovalMO.class);
            String message = ticketInfo.getMessage();
            vo.setTicketMessage(message);
            vo.setAutoExec(ticketInfo.isAutoExec());
        }
        return vo;
    }

    private void fillAnalysisDetail(DmQueryTicketVO vo, Long ticketId) {
        List<DmApprovalProcessActivityDO> activities = this.approvalDal.activityMapper().queryByTicketId(ticketId);
        for (DmApprovalProcessActivityDO activity : activities) {
            if (StringUtils.isBlank(activity.getContext())) {
                continue;
            }
            String activityId = activity.getActivityId();
            if (!ApprovalAnalysisStateMO.TYPE_SQL_RECOGNITION.equals(activityId) && !ApprovalAnalysisStateMO.TYPE_BEHAVIOR_ANALYSIS.equals(activityId)
                && !ApprovalAnalysisStateMO.TYPE_SECURITY_RULE.equals(activityId)) {
                continue;
            }
            ApprovalAnalysisStateMO state = JsonUtils.toObj(activity.getContext(), ApprovalAnalysisStateMO.class);
            if (ApprovalAnalysisStateMO.TYPE_SQL_RECOGNITION.equals(state.getAnalysisType())) {
                vo.setTotalCount(state.getTotalCount());
            } else if (ApprovalAnalysisStateMO.TYPE_BEHAVIOR_ANALYSIS.equals(state.getAnalysisType())) {
                if (state.getTotalCount() != null) {
                    vo.setTotalCount(state.getTotalCount());
                }
                vo.setBehaviors(state.getBehaviors());
            } else if (ApprovalAnalysisStateMO.TYPE_SECURITY_RULE.equals(state.getAnalysisType())) {
                vo.setCheckedList(state.getCheckedInfo());
            }
        }
    }

    //
    // auth Ticket
    //

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void createAuthTicket(String ownerUid, String uid, RdpAddAuthTicketFO fo) {
        DmAuthUserDO user = this.authDal.userMapper().queryByUid(uid);
        if (user != null && user.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_AUTH_TICKET_ROOT_ACCOUNT_UNSUPPORTED.name()));
        }

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
        String bizId = this.namingDao.genApprovalBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(ownerUid);
        ticket.setTargetInfo(DmI18nUtils.getMessage(I18nRdpLabelKeys.AUTH_TICKET_TARGET.name()));
        ticket.setDescription(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketTitle(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TITLE_AUTH.name(), user.getUsername()));
        ticket.setTicketStatus(ApprovalStatus.WAIT_APPROVAL);
        ticket.setApproBiz(ApprovalBiz.DATA_SOURCE_AUTH);
        ticket.setFeatures(Collections.emptyList());

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
        RdpAddAuthTicketFO fo = JsonUtils.toList(authTicketInfo.getApplyAuthInfo(), new TypeReference<>() {});

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

    //
    // Sql Ticket
    //

    @Override
    public DmTicketResultVO createSqlTicket(String puid, String uid, DmAddTicketFO fo) {
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        DmTicketResultVO result = transaction.execute(status -> this.createSqlTicketInTransaction(puid, uid, fo));
        if (result != null && result.getTicketId() != null) {
            this.approvalTaskScheduler.trySchedule(result.getTicketId());
        }
        return result;
    }

    private DmTicketResultVO createSqlTicketInTransaction(String puid, String uid, DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        DmDsDO dsDO = dsLevels.dsDO();
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
            DmApprovalTemplateDO templateDO = this.approvalProviderService.checkApprovalAndReturnTemplate(puid, approvalType, ticketConfig.getTemplateId(), null);
            ticketConfig.setTemplateName(templateDO.getTemplateName());// update form cache.
        }

        Map<UmiTypes, Object> levelsParam = dsLevels.levelsParam();
        SqlContentType contentType = fo.getContentType();
        DmTicketResultVO result = switch (contentType) {
            case INLINE -> {
                if (StringUtils.isBlank(fo.getRawSql())) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_REQUIRED_ERROR.name()));
                }

                AnalysisRuleOptions options = AnalysisRuleOptions.builder()
                    .currentUid(uid)
                    .dsId(dsDO.getId())
                    .levels(levelsParam)
                    .requester(Requester.TICKET)
                    .unsupportedLevel(WarnLevel.FAILURE)
                    .build();
                DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromExists(dsDO.getId());
                SecRulesCheckResult checkResult = new SecRulesCheckResult();
                try (StringReader reader = new StringReader(fo.getRawSql()); Stream<SecRulesCheckResult> results = this.queryAnalysisService.analysisRulesStream(//
                        dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                    results.forEachOrdered(checkResult::merge);
                }
                yield DmConvertUtils.convertToRuleCheckResult(checkResult);
            }
            case ATTACHMENT -> {
                if (fo.getAttachmentId() == null) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_NOT_FOUND_ERROR.name()));
                }
                yield new DmTicketResultVO();
            }
        };

        // check force
        ApprovalMO mo = new ApprovalMO();
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
        String bizId = this.namingDao.genApprovalBizId();
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setBizId(bizId);
        ticket.setOwnerUid(uid);
        ticket.setPrimaryUid(puid);
        ticket.setBindDsId(dsDO.getId());
        ticket.setTargetInfo(targetInfo);
        ticket.setDescription(fo.getDescription());
        ticket.setTicketTitle(fo.getTicketTitle());
        ticket.setTicketStatus(ApprovalStatus.PRE_INIT_WAIT);
        ticket.setApproBiz(ApprovalBiz.DM_QUERY);
        ticket.setStatusMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name()));
        ticket.setApproType(ApprovalType.valueOf(ticketConfig.getType()));
        ticket.setApproTemplateIdentity(ticketConfig.getTemplateId());
        ticket.setApproTemplateName(ticketConfig.getTemplateName());
        ticket.setEnvName(envDO.getEnvName());

        ticket.setContentType(contentType);
        ticket.setFeatures(List.of(ApprovalFeature.values()));
        switch (contentType) {
            case INLINE -> ticket.setRawSql(fo.getRawSql());
            case ATTACHMENT -> ticket.setRawSql(null);
        }
        ticket.setExpectedAffectedRows(fo.getAffectedRows());
        ticket.setTicketInfo(JsonUtils.toJson(mo));
        ticket.setLevels(dsLevels.dbLevels());
        if (StringUtils.isNotBlank(fo.getRollBackSql())) {
            ticket.setRollBackSql(fo.getRollBackSql());
        }

        if (ticket.getApproType() == ApprovalType.Internal) {
            DmApprovalPersonDO primary = new DmApprovalPersonDO();
            primary.setPersonUid(puid);
            primary.setTicketBzId(bizId);
            this.approvalDal.personMapper().insert(primary);
        }

        this.approvalDal.approvalMapper().insert(ticket);
        if (contentType == SqlContentType.ATTACHMENT) {
            this.approvalService.checkSqlFile(fo.getAttachmentId(), uid);
            this.approvalService.confirmSqlFile(ticket.getId(), fo.getAttachmentId(), uid);
        }

        this.approvalFlowService.createProcess(ticket.getId(), ApprovalBiz.DM_QUERY, mo.getMessage() == null);

        result.setTicketId(ticket.getId());
        return result;
    }

    @Override
    public DmApprovalSqlPreviewVO previewSqlFile(long approvalId, int startLine, int lineCount) {
        var preview = this.approvalService.consumeSqlFile(approvalId, file -> {
            return SqlFilePreviewReader.read(file, startLine, lineCount);
        });
        DmApprovalSqlPreviewVO vo = new DmApprovalSqlPreviewVO();
        vo.setStartLine(preview.startLine());
        vo.setTotalLines(preview.totalLines());
        vo.setContent(preview.content());
        vo.setEof(preview.eof());
        return vo;
    }

    @Override
    public String confirmTicket(String puid, long ticketId, DmConfirmTicketFO fo) {
        ApprovalStatus actionStatus = statusFromConfirmAction(fo.getConfirmActionType(), fo.getAutoExecConfig().getAutoExecType());
        if (actionStatus == ApprovalStatus.WAIT_EXEC) {
            String jobBizId = DmTeamUtils.nextExecJobBizId(SQLJobBizType.TICKET);
            this.confirmTicketInNewTransaction(ticketId, fo, actionStatus);
            if (!this.approvalTaskScheduler.submitControlTask(ticketId, () -> this.prepareExecJobAsync(ticketId, fo, jobBizId))) {
                String message = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_EXEC_TASK_SUBMIT_BUSY.name());
                this.restoreExecutionConfirmation(ticketId, message);
                throw new ErrorMessageException(message);
            }
            return jobBizId;
        }
        this.confirmTicketInNewTransaction(ticketId, fo, actionStatus);
        return null;
    }

    private void prepareExecJobAsync(long ticketId, DmConfirmTicketFO fo, String jobBizId) {
        try {
            DmApprovalDO rdpTicketDO = this.checkTicket(ticketId);
            checkJobOperationEnable(rdpTicketDO, fo.getConfirmUid());
            if (rdpTicketDO.getTicketStatus() != ApprovalStatus.WAIT_EXEC) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
            }

            DmApprovalDO dmTicketDO = this.approvalDal.approvalMapper().queryByBizId(rdpTicketDO.getBizId());
            if (dmTicketDO == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
            }
            this.createExecJob(fo, rdpTicketDO, dmTicketDO, jobBizId);
            this.updateAutoExecFlag(ticketId, true);
            this.autoExecService.startJob(jobBizId, fo.getConfirmUid());
        } catch (RuntimeException e) {
            log.error("Prepare ticket execution job failed, ticketId={}", ticketId, e);
            try {
                this.autoExecService.deleteJob(jobBizId);
            } catch (RuntimeException cleanupError) {
                e.addSuppressed(cleanupError);
                log.error("Cleanup prepared auto execution job failed, jobBizId={}", jobBizId, cleanupError);
            }
            String failure = StringUtils.isBlank(e.getMessage()) ? e.getClass().getSimpleName() : e.getMessage();
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_PREPARE_ERROR_MESSAGE.name(), failure);
            this.restoreExecutionConfirmation(ticketId, message);
        }
    }

    private void confirmTicketInNewTransaction(long ticketId, DmConfirmTicketFO fo, ApprovalStatus actionStatus) {
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        transaction.executeWithoutResult(status -> this.confirmTicketInTransaction(ticketId, fo, actionStatus));
    }

    private void confirmTicketInTransaction(long ticketId, DmConfirmTicketFO fo, ApprovalStatus actionStatus) {
        DmApprovalDO rdpTicketDO = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
        if (rdpTicketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
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
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.INIT, JsonUtils.toJson(nContext));
        }
        String statusMessage = actionStatus == ApprovalStatus.WAIT_EXEC ? DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_EXEC_MESSAGE.name()) : fo.getComment();
        if (ApprovalStatus.isEndStatus(actionStatus)) {
            this.approvalFlowService.transitionTicketToTerminal(ticketId, actionStatus, statusMessage);
        } else {
            this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, actionStatus, statusMessage);
        }
    }

    private void updateAutoExecFlag(long ticketId, boolean autoExec) {
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        transaction.executeWithoutResult(status -> {
            DmApprovalDO rdpTicketDO = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
            if (rdpTicketDO == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
            }
            DmApprovalDO dmTicketDO = this.approvalDal.approvalMapper().queryByBizId(rdpTicketDO.getBizId());
            if (dmTicketDO == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
            }
            ApprovalMO info = StringUtils.isEmpty(dmTicketDO.getTicketInfo()) ? new ApprovalMO() : JsonUtils.toObj(dmTicketDO.getTicketInfo(), ApprovalMO.class);
            info.setAutoExec(autoExec);
            this.approvalDal.approvalMapper().updateTicketInfo(dmTicketDO.getId(), JsonUtils.toJson(info));
        });
    }

    private void restoreExecutionConfirmation(long ticketId, String message) {
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        transaction.executeWithoutResult(status -> {
            DmApprovalDO rdpTicketDO = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
            if (rdpTicketDO == null || rdpTicketDO.getTicketStatus() != ApprovalStatus.WAIT_EXEC) {
                return;
            }
            DmApprovalDO dmTicketDO = this.approvalDal.approvalMapper().queryByBizId(rdpTicketDO.getBizId());
            if (dmTicketDO != null) {
                ApprovalMO info = StringUtils.isEmpty(dmTicketDO.getTicketInfo()) ? new ApprovalMO() : JsonUtils.toObj(dmTicketDO.getTicketInfo(), ApprovalMO.class);
                info.setAutoExec(false);
                this.approvalDal.approvalMapper().updateTicketInfo(dmTicketDO.getId(), JsonUtils.toJson(info));
            }
            DmApprovalProcessDO confirmProcess = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.CONFIRM);
            DmApprovalProcessDO executionProcess = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
            this.approvalDal.processMapper().updateTicketStatusByEnum(confirmProcess.getId(), ApprovalProcessStatus.INIT, null);
            this.approvalDal.processMapper().updateTicketStatusByEnum(executionProcess.getId(), ApprovalProcessStatus.INIT, null);
            this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_CONFIRM, message);
        });
    }

    private void createExecJob(DmConfirmTicketFO fo, DmApprovalDO rdpTicket, DmApprovalDO dmTicket, String jobBizId) {
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
        DataSourceConfig dsConfig = dmDsConfigService.fetchDsConfigFromExists(rdpTicket.getBindDsId());
        DmAutoExecConfigFO config = fo.getAutoExecConfig();
        AutoExecJobCreateRequest request = AutoExecJobCreateRequest.builder()//
            .dsLevels(dsLevels)
            .jobBizId(jobBizId)
            .bizType(SQLJobBizType.TICKET)
            .bizId(rdpTicket.getBizId())
            .execType(config.getAutoExecType())
            .transactional(config.isEnableTransactional())
            .errorStrategy(config.getErrorStrategy())
            .retryWaitTime(config.getRetryWaitTime())
            .retryCount(config.getRetryCount())
            .execTime(config.getExecTime())
            .build();
        this.approvalService.consumeSqlFile(dmTicket.getId(), sqlFile -> {
            try (Reader reader = Files.newBufferedReader(sqlFile, StandardCharsets.UTF_8);
                    Stream<SplitScript> scripts = this.queryAnalysisService.analysisSplitStream(dsConfig, reader, null, 1, 0)) {
                this.autoExecService.createJob(request, scripts);
                return null;
            }
        });
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String puid, String uid, DmQueryTaskListFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId());
        return this.autoExecService.queryAutoExecTaskSummaryList(//
                ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid), fo.getTaskStatus(), fo.getPage(), AUTO_EXEC_TASK_SQL_SUMMARY_LENGTH);
    }

    @Override
    public String queryExecTaskSql(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId());
        return this.autoExecService.queryAutoExecTaskSql(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId());
    }

    @Override
    public DmAutoExecJobVO queryExecJobInfo(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId);
        return this.autoExecService.queryAutoExecJob(ticketDO.getBizId(), SQLJobBizType.TICKET, checkOperationEnableWithResult(ticketDO, uid));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void retryJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.retryJob(ticketDO.getBizId(), SQLJobBizType.TICKET);

        approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_EXEC, null);
        approvalDal.processMapper().updateProcessStatusByTicketIdAndStage(ticketId, ApprovalStage.EXECUTION, ApprovalProcessStatus.INIT);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId());
        checkJobOperationEnable(ticketDO, uid);
        boolean jobFinish = this.autoExecService.skipTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId());

        if (jobFinish) {
            approvalDal.processMapper().updateProcessStatusByTicketIdAndStage(fo.getTicketId(), ApprovalStage.EXECUTION, ApprovalProcessStatus.FINISH);
            this.approvalFlowService.transitionTicketToTerminal(fo.getTicketId(), ApprovalStatus.FINISHED, null);
        }
    }

    @Override
    public void canceledSkipTask(String puid, String uid, DmQueryAutoExecFO fo) {
        DmApprovalDO ticketDO = this.checkTicket(fo.getTicketId());
        checkJobOperationEnable(ticketDO, uid);
        this.autoExecService.continueTask(ticketDO.getBizId(), SQLJobBizType.TICKET, fo.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void endAutoExecJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.endJob(ticketDO.getBizId(), SQLJobBizType.TICKET);

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
        this.approvalFlowService.transitionTicketToTerminal(ticketDO.getId(), ApprovalStatus.CLOSED, null);
    }

    @Override
    public void stopJob(String puid, String uid, long ticketId) {
        DmApprovalDO ticketDO = this.checkTicket(ticketId);
        checkJobOperationEnable(ticketDO, uid);

        this.autoExecService.stopJob(ticketDO.getBizId(), SQLJobBizType.TICKET);
    }

    @Override
    public List<DmBizLogVO> queryExecLog(DmQueryExecLogFO fo) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(fo.getJobId());
        if (jobDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_EXISTS_ERROR_MESSAGE.name()));
        }

        List<DmMonBizLogDO> logDOS;
        if (fo.getDependBizType() == LogDependBizType.AUTO_EXEC_JOB) {
            logDOS = this.monitorDal.bizLogMapper().queryListByBizId(jobDO.getBizId());
        } else {
            if (fo.getTaskId() == null) {
                throw new ErrorMessageException("taskId must not null");
            }
            DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(fo.getTaskId());
            logDOS = this.monitorDal.bizLogMapper().queryListByBizId(execTaskDO.getBizId());
        }

        return logDOS.stream().map((b -> {
            DmBizLogVO vo = new DmBizLogVO();
            vo.setContent(b.getContent());
            vo.setId(b.getId());
            vo.setLogLevel(b.getLogLevel());
            vo.setDependOnBizType(b.getDependOnBizType());
            vo.setTime(DateFormatType.s_yyyyMMdd_HHmmss.format(b.getGmtCreate()));
            return vo;
        })).collect(Collectors.toList());
    }

    //
    // ThirdParty support
    //

    @Transactional
    public boolean refreshCache(DmApprovalDO ticketDO) {
        if (StringUtils.isEmpty(ticketDO.getApproIdentity())) {
            return false;
        }
        try {
            approvalProviderService.refreshApprovalStatus(ticketDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() != ThirdPartyApiErrorType.CONNECTION_ERROR) {
                this.approvalFlowService.failTicket(ticketDO.getId(), DmI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), ticketDO.getPrimaryUid());
            }
            return false;
        }

        return true;
    }

    @Override
    public List<RdpApproTemplateVO> listTemplates(String ownerUid, ApprovalType approvalType) {
        return this.approvalProviderService.listTemplates(ownerUid, approvalType);
    }

    @Override
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, ApprovalType approvalType) {
        return this.approvalProviderService.refreshTemplates(ownerUid, approvalType);
    }

    @Override
    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        return this.approvalProviderService.getTicketTypes(ownerUid);
    }

    @Override
    public void addTemplateByUrl(String ownerUid, ApprovalType approvalType, String templateUrl) {
        this.approvalProviderService.addTemplateByUrl(ownerUid, approvalType, templateUrl);
    }

    @Override
    public void removeTemplateById(String ownerUid, ApprovalType approvalType, String templateId) {
        this.approvalProviderService.removeTemplateById(ownerUid, approvalType, templateId);
    }

    //
    // utils
    //

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
        return switch (actionType) {
            case REFUSE -> null;
            case CONFIRM -> confirmUser.getUsername();
            default -> throw new UnsupportedOperationException("Not supported confirm action type " + actionType.name());
        };
    }

    private DmApprovalDO checkTicket(long ticketId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketId);
        if (ticketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        return ticketDO;
    }

}
