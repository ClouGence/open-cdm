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
package com.clougence.clouddm.console.web.component.approval.schedule;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.console.web.component.approval.*;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalAnalysisStateMO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 *    // PRE_INIT_WAIT  -> PRE_INIT_RUN -> WAIT_APPROVAL
 *    // WAIT_APPROVAL  -> [WAIT_APPROVAL \ WAIT_CONFIRM \ REJECTED]
 *    // WAIT_CONFIRM   -> [WAIT_EXEC \ REJECTED \ FINISHED]
 *    //  -- TicketService.confirmTicket
 *
 *    // WAIT_EXEC      -> RUNNING
 *    // RUNNING        -> [EXEC_FAIL \ FINISHED]
 *    // EXEC_FAIL      -> [WAIT_EXEC \ CLOSED \ CANCELED]
 *    //  -- TicketService.retryTicket    WAIT_EXEC
 *    //  -- TicketService.deleteTicket   CLOSED and delete
 *    //  -- TicketService.closeTicket    CLOSED
 *    //  -- TicketService.cancelTicket   CANCELED
 *
 * @author Ekko
 * @date 2024/5/7 14:25
*/
@Slf4j
@Service
@UnifiedPostConstructOrder(-1)
public class ApprovalTaskProcessor implements UnifiedPostConstruct {

    @Resource
    private ConsoleClusterService                   cluster;
    @Resource
    private SystemDal                               systemDal;
    @Resource
    private AuthDal                                 authDal;
    @Resource
    private ApprovalDal                             approvalDal;
    @Resource
    private ApprovalProviderServiceImpl             approvalProviderService;
    @Resource
    private ImSenderService                         imSenderService;
    @Resource
    private ApprovalPreInitService                  preInitService;
    @Resource
    private ApprovalFlowService                     approvalFlowService;
    @Resource
    private ApprovalStateService                    approvalStateService;
    @Resource
    private ApprovalPersonService                   approvalPersonService;
    @Resource
    private CustomApprovalService                   customApprovalService;
    @Resource
    private PlatformTransactionManager              txManager;
    @Resource
    private List<ApprovalHandler>                   handlers;
    private final Map<ApprovalBiz, ApprovalHandler> approvalHandlers = new EnumMap<>(ApprovalBiz.class);

    @Override
    public void init() {
        for (ApprovalHandler approvalHandler : this.handlers) {
            ApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    @Override
    public void stop() {

    }

    // PRE_INIT_WAIT -> PRE_INIT_RUN
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public boolean preparePreInit(long ticketId) {
        DmApprovalDO approvalDO = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
        if (approvalDO == null || approvalDO.getTicketStatus() != ApprovalStatus.PRE_INIT_WAIT) {
            return false;
        }

        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXPLAIN);
        if (!approvalDO.hasFeature(ApprovalFeature.PRE_INIT)) {
            if (processDO != null) {
                this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.EXPLAIN, ApprovalProcessStatus.FINISH, null);
            }

            this.approvalStateService.updateApprovalStatus(ticketId, ApprovalStatus.WAIT_APPROVAL, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
            this.customApprovalService.startApproval(ticketId);
            return false;
        }

        if (processDO == null) {
            throw new IllegalStateException("EXPLAIN process not found, ticketId=" + ticketId);
        }

        this.approvalStateService.initializeAnalysisActivities(ticketId, this.preInitService.initialStates(approvalDO));

        this.approvalStateService.updateApprovalStatus(ticketId, ApprovalStatus.PRE_INIT_RUN, DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_STATUS_WAIT_EXPLAIN.name()));
        return true;
    }

    public void submitPreInitChildren(DmApprovalDO approvalDO, ApprovalTaskSubmitter taskSubmitter) {
        this.preInitService.process(approvalDO, taskSubmitter, this::processPreInitRun);
    }

    //

    // Direct callback from a completed child, also used as a low-frequency scheduler fallback.
    public void processPreInitRun(long ticketId) {
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        transaction.executeWithoutResult(status -> {
            DmApprovalDO approvalDO = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
            if (approvalDO == null || approvalDO.getTicketStatus() != ApprovalStatus.PRE_INIT_RUN) {
                return;
            }

            DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXPLAIN);
            if (processDO == null) {
                throw new IllegalStateException("EXPLAIN process not found, ticketId=" + ticketId);
            }

            List<DmApprovalProcessActivityDO> activities = this.approvalDal.activityMapper().queryByTicketId(ticketId);
            boolean hasAnalysis = false;
            boolean failed = false;
            String failureMessage = null;
            for (DmApprovalProcessActivityDO activity : activities) {
                if (!processDO.getId().equals(activity.getProcessId()) || activity.getTaskStatus() == null
                    || ApprovalAnalysisStateMO.TYPE_SQL_RECOGNITION.equals(activity.getActivityId())) {
                    continue;
                }

                hasAnalysis = true;
                if (!ApprovalAnalysisStateMO.STATUS_FINISHED.equals(activity.getTaskStatus()) && !ApprovalAnalysisStateMO.STATUS_FAILED.equals(activity.getTaskStatus())) {
                    return;
                }

                if (ApprovalAnalysisStateMO.STATUS_FAILED.equals(activity.getTaskStatus())) {
                    failed = true;
                    if (StringUtils.isBlank(failureMessage) && StringUtils.isNotBlank(activity.getContext())) {
                        ApprovalAnalysisStateMO analysis = JsonUtils.toObj(activity.getContext(), ApprovalAnalysisStateMO.class);
                        failureMessage = analysis.getErrorMessage();
                    }
                }
            }

            if (!hasAnalysis) {
                return;
            }

            if (failed) {
                if (StringUtils.isBlank(failureMessage)) {
                    failureMessage = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_EXPLAIN_FAILED_MESSAGE.name());
                }

                this.approvalFlowService.failTicket(ticketId, failureMessage, approvalDO.getPrimaryUid());
                return;
            }

            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.EXPLAIN, ApprovalProcessStatus.FINISH, null);
            this.approvalStateService.updateApprovalStatus(ticketId, ApprovalStatus.WAIT_APPROVAL, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
            this.customApprovalService.startApproval(ticketId);
        });
    }

    // WAIT_APPROVAL -> [WAIT_APPROVAL \ WAIT_CONFIRM \ REJECTED]
    public void processWaitApproval(DmApprovalDO approvalDO) {
        if (approvalDO.getApproType() == ApprovalType.Internal) {
            return;
        }

        long ticketId = approvalDO.getId();
        TransactionTemplate transaction = new TransactionTemplate(this.txManager);
        transaction.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        if (StringUtils.isBlank(approvalDO.getApproIdentity())) {
            this.createExternalApprovalIfNecessary(ticketId, transaction);
        }

        DmApprovalDO ticket = this.approvalDal.approvalMapper().queryById(ticketId);
        if (ticket != null && StringUtils.isNotBlank(ticket.getApproIdentity())) {
            transaction.executeWithoutResult(status -> {
                this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
                DmApprovalProcessDO process = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
                ApprovalStageMO context = CustomApprovalService.parseStageContext(process);
                if (context.getExecMsg() != null && Boolean.TRUE.equals(context.getExternalCreateStarted())) {
                    context.setExecMsg(null);
                    this.approvalDal.processMapper().updateContextById(process.getId(), JsonUtils.toJson(context));
                }
            });

            this.getLastInfoIfNecessary(this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL), ticket);
        }
    }

    private void createExternalApprovalIfNecessary(long ticketId, TransactionTemplate transaction) {
        Boolean claimed = transaction.execute(status -> {
            this.cluster.requireSchedulerLeader();
            DmApprovalDO ticket = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
            if (this.isApprovalCreationIntentBlocked(ticket)) {
                return false;
            }

            DmApprovalProcessDO process = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
            ApprovalStageMO context = CustomApprovalService.parseStageContext(process);
            if (Boolean.TRUE.equals(context.getExternalCreateStarted())) {
                return false;
            }

            // Commit the intent before the external call; an unknown outcome must not create another instance.
            context.setExternalCreateStarted(true);
            context.setExecMsg(DmI18nUtils.getMessage(I18nDmMsgKeys.CLUSTER_EXECUTION_UNKNOWN.name()));
            this.approvalDal.processMapper().updateContextById(process.getId(), JsonUtils.toJson(context));
            return true;
        });

        if (Boolean.TRUE.equals(claimed)) {
            transaction.executeWithoutResult(status -> {
                DmApprovalDO ticket = this.approvalDal.approvalMapper().selectByIdForUpdate(ticketId);
                if (this.isExternalApprovalCreationBlocked(ticket)) {
                    return;
                }

                this.cluster.requireSchedulerLeader();
                this.approvalHandler(ticket.getApproBiz()).createApproval(ticketId, this.imSenderService);
            });
        }
    }

    private boolean isApprovalCreationIntentBlocked(DmApprovalDO ticket) {
        if (ticket == null || ticket.getDeleted()) {
            return true;
        }

        if (ticket.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL) {
            return true;
        }
        return StringUtils.isNotBlank(ticket.getApproIdentity());
    }

    private boolean isExternalApprovalCreationBlocked(DmApprovalDO ticket) {
        if (ticket == null || ticket.getDeleted()) {
            return true;
        }

        if (ticket.getTicketStatus() != ApprovalStatus.WAIT_APPROVAL || ticket.getApproType() == ApprovalType.Internal) {
            return true;
        }
        return StringUtils.isNotBlank(ticket.getApproIdentity());
    }

    private void getLastInfoIfNecessary(DmApprovalProcessDO processDO, DmApprovalDO ticketDO) {
        long last = processDO.getGmtModified().getTime();
        long now = new Date().getTime();
        long timeInterval = now - last;
        long diff = TimeUnit.SECONDS.convert(timeInterval, TimeUnit.MILLISECONDS);
        long intervalTime;
        DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(ticketDO.getPrimaryUid(), RootUserConfig.Fields.updateApprovalStatusIntervalTime);
        if (configDO == null || !NumberUtils.isNumber(configDO.getConfigValue())) {
            // one day
            intervalTime = 60 * 60 * 24;
        } else {
            intervalTime = Long.parseLong(configDO.getConfigValue());
            // if intervalTime <=0 not actively get last info
            if (intervalTime <= 0) {
                return;
            }
        }

        if (diff > intervalTime) {
            approvalProviderService.refreshApprovalStatus(ticketDO.getId());
        }
    }

    // WAIT_CONFIRM -> [WAIT_EXEC \ REJECTED \ FINISHED]
    //  -- TicketService.confirmTicket
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitConfirm(DmApprovalDO approvalDO) {
        DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalDO.getId());
        if (ticketDO == null || ticketDO.getTicketStatus() != ApprovalStatus.WAIT_CONFIRM) {
            return;
        }

        if (customApprovalService.isCustomApproval(ticketDO)) {
            return;
        }

        List<PrimaryUserVO> primaryUserVOS = queryOrderExecPerson(ticketDO);
        updatePerson(primaryUserVOS, ticketDO, ApprovalStage.CONFIRM);
    }

    private List<PrimaryUserVO> queryOrderExecPerson(DmApprovalDO ticketDO) {
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        // add primary account
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // user self
        DmAuthUserDO rdpUserDO = this.authDal.userMapper().queryByUid(ticketDO.getOwnerUid());
        if (rdpUserDO != null) {
            DmAuthRoleDO rdpRoleDO = authDal.roleMapper().selectById(rdpUserDO.getRoleId());
            if (rdpRoleDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE)) {
                PrimaryUserVO vo = new PrimaryUserVO();
                vo.setUid(rdpUserDO.getUid());
                vo.setUsername(rdpUserDO.getUsername());
                userVOS.add(vo);
            }
        }

        // add sub account who have auth to approval ticket and manger datasource
        List<RsAuthPersonObj> personDOS = this.authDal.userMapper().queryAuthApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId());

        for (RsAuthPersonObj personDO : personDOS) {
            List<String> roleAuthLabels = personDO.getRoleAuthLabels();
            List<String> resAuthLabel = personDO.getResAuthLabel();
            if (CollectionUtils.isNotEmpty(roleAuthLabels) //
                && CollectionUtils.isNotEmpty(resAuthLabel) //
                && roleAuthLabels.contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE) //
                && resAuthLabel.contains(SecDataAuthLabel.DM_DAUTH_TICKET) && personDO.getResId().equals(ticketDO.getBindDsId())) //
            {
                PrimaryUserVO primaryUserVO2 = new PrimaryUserVO();
                primaryUserVO2.setUid(personDO.getUid());
                primaryUserVO2.setUsername(personDO.getUsername());
                userVOS.add(primaryUserVO2);
            }
        }

        return new ArrayList<>(new LinkedHashSet<>(userVOS));
    }

    // WAIT_EXEC -> [RUNNING \ FINISHED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitExec(DmApprovalDO approvalDO) {
        approvalHandler(approvalDO.getApproBiz()).executeTicket(approvalDO.getId(), approvalDO.getApproBiz(), imSenderService);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processReject(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        if (approvalDO.getTicketStatus() == ApprovalStatus.REJECTED) {
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.CONFIRM, ApprovalProcessStatus.REJECT, null);
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.EXECUTION, ApprovalProcessStatus.REJECT, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processFailed(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        if (approvalDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.CONFIRM, ApprovalProcessStatus.FAIL, null);
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.EXECUTION, ApprovalProcessStatus.FAIL, null);
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.APPROVAL, ApprovalProcessStatus.FAIL, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processCanceled(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        if (approvalDO.getTicketStatus() == ApprovalStatus.CANCELED) {
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.CONFIRM, ApprovalProcessStatus.CLOSED, null);
            this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.EXECUTION, ApprovalProcessStatus.CLOSED, null);
        }
    }

    // RUNNING -> [RUNNING \ EXEC_FAIL \ FINISHED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processRunningCheck(DmApprovalDO approvalDO) {
        //List<PrimaryUserVO> primaryUserVOS = queryOrderExecPerson(approvalDO);
        //updatePerson(primaryUserVOS, approvalDO, ApprovalStage.CONFIRM);
        approvalHandler(approvalDO.getApproBiz()).runningCheck(approvalDO.getId(), approvalDO.getApproBiz(), imSenderService);
    }

    private ApprovalHandler approvalHandler(ApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    private void updatePerson(List<PrimaryUserVO> primaryUserVOS, DmApprovalDO ticketDO, ApprovalStage rdpTicketStage) {
        List<String> personName = this.approvalPersonService.replacePersons(ticketDO, primaryUserVOS);

        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketDO.getId(), rdpTicketStage);
        ApprovalStageMO mo = new ApprovalStageMO();
        if (StringUtils.isNotBlank(processDO.getStageContext())) {
            mo = JsonUtils.toObj(processDO.getStageContext(), ApprovalStageMO.class);
        }

        if (mo.getExecUserName() != null && new HashSet<>(mo.getExecUserName()).equals(new HashSet<>(personName))) {
            return;
        }

        mo.setExecUserName(personName);
        this.approvalDal.processMapper().updateContextById(processDO.getId(), JsonUtils.toJson(mo));
    }
}
