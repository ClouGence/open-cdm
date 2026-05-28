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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
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
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 *    // PRE_INIT       -> WAIT_APPROVAL
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
public class ApprovalTaskScheduleProcess {
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
    private final Map<ApprovalBiz, ApprovalHandler> approvalHandlers;

    public ApprovalTaskScheduleProcess(List<ApprovalHandler> handlers){
        this.approvalHandlers = new EnumMap<>(ApprovalBiz.class);
        for (ApprovalHandler approvalHandler : handlers) {
            ApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    // PRE_INIT -> WAIT_APPROVAL
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processPreInit(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXPLAIN);
        //         sometimes it will be null , not find question
        if (processDO == null) {
            this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_APPROVAL, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
            return;
        }
        this.approvalDal.processMapper().updateTicketStatusByEnum(processDO.getId(), ApprovalProcessStatus.FINISH, null);
        this.approvalDal.approvalMapper().updateStatusByEnum(ticketId, ApprovalStatus.WAIT_APPROVAL, DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
    }

    // WAIT_APPROVAL -> [WAIT_APPROVAL \ WAIT_CONFIRM \ REJECTED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitApproval(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
        if (approvalDO.getApproType() != ApprovalType.Internal) {
            if (StringUtils.isEmpty(approvalDO.getApproIdentity())) {
                // avoid create multiple approval processes in multiple consoles
                DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalDO.getId());
                if (StringUtils.isEmpty(ticketDO.getApproIdentity())) {
                    approvalHandler(approvalDO.getApproBiz()).createApproval(ticketDO.getId(), imSenderService);
                }
            }
            getLastInfoIfNecessary(processDO, approvalDO);
        }

    }

    private void getLastInfoIfNecessary(DmApprovalProcessDO processDO, DmApprovalDO ticketDO) {
        long last = processDO.getGmtModified().getTime();
        long now = new Date().getTime();
        long timeInterval = now - last;
        long diff = TimeUnit.SECONDS.convert(timeInterval, TimeUnit.MILLISECONDS);
        long intervalTime;
        DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(ticketDO.getPrimaryUid(), UserDefinedConfig.Fields.updateApprovalStatusIntervalTime);
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
        DmApprovalDO ticketDO = approvalDal.approvalMapper().queryById(approvalDO.getId());

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

        return userVOS.stream().distinct().collect(Collectors.toList());
    }

    // WAIT_EXEC -> [RUNNING \ FINISHED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitExec(DmApprovalDO approvalDO) {
        approvalHandler(approvalDO.getApproBiz()).executeTicket(approvalDO.getId(), approvalDO.getApproBiz(), imSenderService);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processReject(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        DmApprovalProcessDO processDOC = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.CONFIRM);
        DmApprovalProcessDO processDOE = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        if (approvalDO.getTicketStatus() == ApprovalStatus.REJECTED) {
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOC.getId(), ApprovalProcessStatus.REJECT, null);
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOE.getId(), ApprovalProcessStatus.REJECT, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processFailed(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        DmApprovalProcessDO processDOC = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.CONFIRM);
        DmApprovalProcessDO processDOE = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        DmApprovalProcessDO processDOA = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.APPROVAL);
        if (approvalDO.getTicketStatus() == ApprovalStatus.WAIT_APPROVAL) {
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOC.getId(), ApprovalProcessStatus.FAIL, null);
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOE.getId(), ApprovalProcessStatus.FAIL, null);
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOA.getId(), ApprovalProcessStatus.FAIL, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processCanceled(DmApprovalDO approvalDO) {
        long ticketId = approvalDO.getId();
        DmApprovalProcessDO processDOC = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.CONFIRM);
        DmApprovalProcessDO processDOE = this.approvalDal.processMapper().queryByStage(ticketId, ApprovalStage.EXECUTION);
        if (approvalDO.getTicketStatus() == ApprovalStatus.CANCELED) {
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOC.getId(), ApprovalProcessStatus.CLOSED, null);
            this.approvalDal.processMapper().updateTicketStatusByEnum(processDOE.getId(), ApprovalProcessStatus.CLOSED, null);
        }
    }

    // RUNNING -> [RUNNING \ EXEC_FAIL \ FINISHED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processRunningCheck(DmApprovalDO approvalDO) {
        //List<PrimaryUserVO> primaryUserVOS = queryOrderExecPerson(approvalDO);
        //updatePerson(primaryUserVOS, approvalDO, ApprovalStage.CONFIRM);
        approvalHandler(approvalDO.getApproBiz()).runningCheck(approvalDO.getId(), approvalDO.getApproBiz(), imSenderService);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processApprovalPerson(String puid, String uid, DmApprovalDO approvalDO) {
        if (approvalDO.getApproType() == ApprovalType.Internal) {
            // avoid dead lock
            DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalDO.getId());
            List<PrimaryUserVO> primaryUserVOS = approvalHandler(approvalDO.getApproBiz()).queryPerson(approvalDO.getId());
            updatePerson(primaryUserVOS, ticketDO, ApprovalStage.APPROVAL);
        }
    }

    private ApprovalHandler approvalHandler(ApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    private void updatePerson(List<PrimaryUserVO> primaryUserVOS, DmApprovalDO ticketDO, ApprovalStage rdpTicketStage) {
        // query all resource user.
        List<DmAuthUserDO> allResUsers = this.authDal.userMapper().listSubResourceManagersByPrimaryUId(ticketDO.getPrimaryUid());

        List<String> newUids1 = primaryUserVOS.stream().map(PrimaryUserVO::getUid).collect(Collectors.toList());
        List<String> newUids2 = allResUsers.stream().map(DmAuthUserDO::getUid).collect(Collectors.toList());
        List<String> newUids = new ArrayList<>(newUids1);
        for (String uid : newUids2) {
            if (!newUids.contains(uid)) {
                newUids.add(uid);
            }
        }

        List<DmApprovalPersonDO> personDOS = this.approvalDal.personMapper().queryByTicketBzId(ticketDO.getBizId());
        List<String> oldUids = personDOS.stream().map(DmApprovalPersonDO::getPersonUid).collect(Collectors.toList());

        if (newUids.size() == oldUids.size()) {
            Collections.sort(newUids);
            Collections.sort(oldUids);
            if (newUids.equals(oldUids)) {
                return;
            }
        }

        this.approvalDal.personMapper().deleteByTicketBzId(ticketDO.getBizId());
        List<DmApprovalPersonDO> personDO = new ArrayList<>();
        newUids.forEach(personUid -> {
            DmApprovalPersonDO approvalPersonDO = new DmApprovalPersonDO();
            approvalPersonDO.setTicketBzId(ticketDO.getBizId());
            approvalPersonDO.setPersonUid(personUid);
            personDO.add(approvalPersonDO);
        });
        approvalDal.personMapper().insertPersonBatch(personDO);

        // update process person
        List<String> approvalPersonList = new ArrayList<>();
        personDO.forEach(person -> {
            approvalPersonList.add(person.getPersonUid());
        });

        List<String> personName = new ArrayList<>();
        approvalPersonList.forEach(personUid -> {
            personName.add(this.authDal.userMapper().queryByUid(personUid).getUsername());
        });

        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(ticketDO.getId(), rdpTicketStage);
        ApprovalStageMO mo = new ApprovalStageMO();
        mo.setExecUserName(personName);
        this.approvalDal.processMapper().updateContextById(processDO.getId(), JsonUtils.toJson(mo));
    }
}
