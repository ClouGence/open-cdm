package com.clougence.rdp.component.ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.component.ticket.model.RdpExecStageContextMO;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.vo.PrimaryUserVO;
import com.clougence.rdp.dal.enumeration.*;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.*;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

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
public class RdpApprovalTaskScheduleProcess {

    @Resource
    private RdpTicketMapper           rdpTicketMapper;

    @Resource
    private RdpTicketProcessMapper    rdpTicketProcessMapper;

    @Resource
    private RdpApprovalService        rdpApproService;

    @Resource
    private RdpUserMapper             rdpUserMapper;

    @Resource
    private RdpApprovalPersonMapper   rdpApprovalPersonMapper;

    @Resource
    private RdpUserKvBaseConfigMapper rdpUserKvBaseConfigMapper;

    @Resource
    private RdpTicketHelperService    rdpTicketHelperService;

    @Resource
    private RdpRoleMapper             rdpRoleMapper;

    // one day
    private final long                DEFAULT_INTERVAL_TIME = 60 * 60 * 24;

    // PRE_INIT -> WAIT_APPROVAL
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processPreInit(RdpTicketDO rdpTicketDO) {
        long ticketId = rdpTicketDO.getId();
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXPLAIN);
        //         sometimes it will be null , not find question
        if (processDO == null) {
            this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_APPROVAL, RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
            return;
        }
        this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, null);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_APPROVAL, RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_APPROVAL.name()));
    }

    // WAIT_APPROVAL -> [WAIT_APPROVAL \ WAIT_CONFIRM \ REJECTED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitApproval(RdpTicketDO rdpTicketDO) {
        long ticketId = rdpTicketDO.getId();
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.APPROVAL);
        if (rdpTicketDO.getApproType() != RdpApprovalType.Internal) {
            if (StringUtils.isEmpty(rdpTicketDO.getApproIdentity())) {
                // avoid create multiple approval processes in multiple consoles
                RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(rdpTicketDO.getId());
                if (StringUtils.isEmpty(ticketDO.getApproIdentity())) {
                    rdpTicketHelperService.getTicketHelper(rdpTicketDO.getApproBiz()).createApproval(ticketDO.getId());
                }
            }
            getLastInfoIfNecessary(processDO, rdpTicketDO);
        }

    }

    private void getLastInfoIfNecessary(RdpTicketProcessDO processDO, RdpTicketDO ticketDO) {
        long last = processDO.getGmtModified().getTime();
        long now = new Date().getTime();
        long timeInterval = now - last;
        long diff = TimeUnit.SECONDS.convert(timeInterval, TimeUnit.MILLISECONDS);
        long intervalTime;
        RdpUserKvBaseConfigDO configDO = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ticketDO.getPrimaryUid(), UserDefinedConfig.Fields.updateApprovalStatusIntervalTime);
        if (configDO == null || !NumberUtils.isNumber(configDO.getConfigValue())) {
            intervalTime = DEFAULT_INTERVAL_TIME;
        } else {
            intervalTime = Long.parseLong(configDO.getConfigValue());
            // if intervalTime <=0 not actively get last info
            if (intervalTime <= 0) {
                return;
            }
        }

        if (diff > intervalTime) {
            rdpApproService.refreshApprovalStatus(ticketDO.getId());
        }
    }

    // WAIT_CONFIRM -> [WAIT_EXEC \ REJECTED \ FINISHED]
    //  -- TicketService.confirmTicket
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processWaitConfirm(RdpTicketDO rdpTicketDO) {
        RdpTicketDO ticketDO = rdpTicketMapper.queryById(rdpTicketDO.getId());

        List<PrimaryUserVO> primaryUserVOS = queryOrderExecPerson(ticketDO);
        updatePerson(primaryUserVOS, ticketDO, RdpTicketStage.CONFIRM);
    }

    private List<PrimaryUserVO> queryOrderExecPerson(RdpTicketDO ticketDO) {
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        // add primary account
        RdpUserDO parentUserDO = this.rdpUserMapper.queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // user self
        RdpUserDO rdpUserDO = this.rdpUserMapper.queryByUid(ticketDO.getOwnerUid());
        if (rdpUserDO != null) {
            RdpRoleDO rdpRoleDO = rdpRoleMapper.selectById(rdpUserDO.getRoleId());
            if (rdpRoleDO.getRoleAuthLabels().contains(SecRoleAuthLabel.RDP_WORKER_ORDER_EXECUTE)) {
                PrimaryUserVO vo = new PrimaryUserVO();
                vo.setUid(rdpUserDO.getUid());
                vo.setUsername(rdpUserDO.getUsername());
                userVOS.add(vo);
            }
        }

        // add sub account who have auth to approval ticket and manger datasource
        List<RdpTicketApproPersonDO> personDOS = this.rdpUserMapper.queryAuthApproPerson(AccountType.SUB_ACCOUNT, parentUserDO.getId());

        for (RdpTicketApproPersonDO personDO : personDOS) {
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
    public void processWaitExec(RdpTicketDO rdpTicketDO) {
        rdpTicketHelperService.getTicketHelper(rdpTicketDO.getApproBiz()).executeTicket(rdpTicketDO.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processReject(RdpTicketDO rdpTicketDO) {
        long ticketId = rdpTicketDO.getId();
        RdpTicketProcessDO processDOC = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.CONFIRM);
        RdpTicketProcessDO processDOE = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        if (rdpTicketDO.getTicketStatus() == RdpTicketStatus.REJECTED) {
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOC.getId(), RdpTicketProcessStatus.REJECT, null);
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOE.getId(), RdpTicketProcessStatus.REJECT, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processFailed(RdpTicketDO rdpTicketDO) {
        long ticketId = rdpTicketDO.getId();
        RdpTicketProcessDO processDOC = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.CONFIRM);
        RdpTicketProcessDO processDOE = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        RdpTicketProcessDO processDOA = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.APPROVAL);
        if (rdpTicketDO.getTicketStatus() == RdpTicketStatus.WAIT_APPROVAL) {
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOC.getId(), RdpTicketProcessStatus.FAIL, null);
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOE.getId(), RdpTicketProcessStatus.FAIL, null);
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOA.getId(), RdpTicketProcessStatus.FAIL, null);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processCanceled(RdpTicketDO rdpTicketDO) {
        long ticketId = rdpTicketDO.getId();
        RdpTicketProcessDO processDOC = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.CONFIRM);
        RdpTicketProcessDO processDOE = this.rdpTicketProcessMapper.queryByStage(ticketId, RdpTicketStage.EXECUTION);
        if (rdpTicketDO.getTicketStatus() == RdpTicketStatus.CANCELED) {
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOC.getId(), RdpTicketProcessStatus.CLOSED, null);
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDOE.getId(), RdpTicketProcessStatus.CLOSED, null);
        }
    }

    // RUNNING -> [RUNNING \ EXEC_FAIL \ FINISHED]
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processRunningCheck(RdpTicketDO rdpTicketDO) {
        //List<PrimaryUserVO> primaryUserVOS = queryOrderExecPerson(rdpTicketDO);
        //updatePerson(primaryUserVOS, rdpTicketDO, RdpTicketStage.CONFIRM);
        rdpTicketHelperService.getTicketHelper(rdpTicketDO.getApproBiz()).runningCheck(rdpTicketDO.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void processApprovalPerson(String puid, String uid, RdpTicketDO rdpTicketDO) {
        if (rdpTicketDO.getApproType() == RdpApprovalType.Internal) {
            // avoid dead lock
            RdpTicketDO ticketDO = rdpTicketMapper.selectByIdForUpdate(rdpTicketDO.getId());
            List<PrimaryUserVO> primaryUserVOS = rdpTicketHelperService.getTicketHelper(rdpTicketDO.getApproBiz()).queryPerson(rdpTicketDO.getId());
            updatePerson(primaryUserVOS, ticketDO, RdpTicketStage.APPROVAL);
        }
    }

    private void updatePerson(List<PrimaryUserVO> primaryUserVOS, RdpTicketDO ticketDO, RdpTicketStage rdpTicketStage) {
        // query all resource user.
        List<RdpUserDO> allResUsers = this.rdpUserMapper.listSubResourceManagersByPrimaryUId(ticketDO.getPrimaryUid());

        List<String> newUids1 = primaryUserVOS.stream().map(PrimaryUserVO::getUid).collect(Collectors.toList());
        List<String> newUids2 = allResUsers.stream().map(RdpUserDO::getUid).collect(Collectors.toList());
        List<String> newUids = new ArrayList<>(newUids1);
        for (String uid : newUids2) {
            if (!newUids.contains(uid)) {
                newUids.add(uid);
            }
        }

        List<RdpApprovalPersonDO> personDOS = this.rdpApprovalPersonMapper.queryByTicketBzId(ticketDO.getBizId());
        List<String> oldUids = personDOS.stream().map(RdpApprovalPersonDO::getPersonUid).collect(Collectors.toList());

        if (newUids.size() == oldUids.size()) {
            Collections.sort(newUids);
            Collections.sort(oldUids);
            if (newUids.equals(oldUids)) {
                return;
            }
        }

        this.rdpApprovalPersonMapper.deleteByTicketBzId(ticketDO.getBizId());
        List<RdpApprovalPersonDO> personDO = new ArrayList<>();
        newUids.forEach(personUid -> {
            RdpApprovalPersonDO rdpApprovalPersonDO = new RdpApprovalPersonDO();
            rdpApprovalPersonDO.setTicketBzId(ticketDO.getBizId());
            rdpApprovalPersonDO.setPersonUid(personUid);
            personDO.add(rdpApprovalPersonDO);
        });
        rdpApprovalPersonMapper.insertTemplateBatch(personDO);

        // update process person
        List<String> approvalPersonList = new ArrayList<>();
        personDO.forEach(person -> {
            approvalPersonList.add(person.getPersonUid());
        });

        List<String> personName = new ArrayList<>();
        approvalPersonList.forEach(personUid -> {
            personName.add(this.rdpUserMapper.queryByUid(personUid).getUsername());
        });

        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(ticketDO.getId(), rdpTicketStage);
        RdpExecStageContextMO mo = new RdpExecStageContextMO();
        mo.setExecUserName(personName);
        this.rdpTicketProcessMapper.updateContextById(processDO.getId(), JsonUtils.toJson(mo));
    }
}
