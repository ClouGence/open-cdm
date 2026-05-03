package com.clougence.rdp.component.ticket.impl;

import static com.clougence.rdp.util.RdpTimeUtil.getDateTimeOfTimestamp;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.sdk.approval.ApprovalUrl;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.rdp.component.resulttask.AsyncTaskWithResultService;
import com.clougence.rdp.component.resulttask.TaskType;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.component.ticket.RdpTicketHelperService;
import com.clougence.rdp.component.ticket.RdpTicketProcessService;
import com.clougence.rdp.component.ticket.RdpTicketService;
import com.clougence.rdp.component.ticket.model.RdpExecStageContextMO;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.fo.security.ListMyAuthTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpApprovalTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpListTicketFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpQueryTicketDetailFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpTicketBasicVO;
import com.clougence.rdp.controller.model.vo.ticket.RdpTicketActivityVO;
import com.clougence.rdp.controller.model.vo.ticket.RdpTicketBaseInfoVO;
import com.clougence.rdp.controller.model.vo.ticket.RdpTicketProcessVO;
import com.clougence.rdp.dal.enumeration.*;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.*;
import com.clougence.rdp.dal.model.queryobj.RdpTicketQueryObject;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.RdpPageUtil;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2024-05-07 15:48
 */

@Service
@Slf4j
public class RdpTicketServiceImpl implements RdpTicketService {

    @Resource
    private RdpTicketProcessService        rdpTicketProcessService;
    @Resource
    private RdpTicketMapper                rdpTicketMapper;
    @Resource
    private RdpDsEnvService                rdpDsEnvService;
    @Resource
    private RdpUserMapper                  rdpUserMapper;
    @Resource
    private RdpDsEnvMapper                 rdpEnvMapper;
    @Resource
    private RdpDataSourceMapper            rdpDsMapper;
    @Resource
    private RdpTicketProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpTicketProcessActivityMapper rdpTicketProcessActivityMapper;
    @Resource
    private RdpApprovalPersonMapper        rdpApprovalPersonMapper;
    @Resource
    private AsyncTaskWithResultService     asyncTaskWithResultService;
    @Resource
    private RdpApprovalService             approvalService;
    @Resource
    private RdpUserService                 rdpUserService;
    @Resource
    private RdpTicketHelperService         rdpTicketHelperService;

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid, String uid) {
        RdpTicketDO ticketDO = checkTicket(ticketId, puid);
        RdpTicketStatus ticketStatus = ticketDO.getTicketStatus();
        if (ticketStatus == RdpTicketStatus.WAIT_EXEC || ticketStatus == RdpTicketStatus.EXEC_FAIL || ticketStatus == RdpTicketStatus.EXEC_PAUSE) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }
        checkInProgress(ticketDO);
        List<RdpTicketProcessDO> rdpTicketProcessDOS = rdpTicketProcessMapper.listByTicketId(ticketId);
        RdpUserDO rdpUserDO = rdpUserMapper.queryByUid(uid);
        for (RdpTicketProcessDO rdpTicketProcessDO : rdpTicketProcessDOS) {
            if (rdpTicketProcessDO.getProcessStatus() == RdpTicketProcessStatus.INIT) {
                RdpExecStageContextMO execMO = new RdpExecStageContextMO();
                execMO.setExecUserName(Collections.singletonList(rdpUserDO.getUsername()));
                this.rdpTicketProcessMapper.updateContextById(rdpTicketProcessDO.getId(), JsonUtils.toJson(execMO));
                break;
            }
        }
        this.rdpTicketProcessService.cancelAllProcess(ticketId);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CLOSED, statusMessage);
        this.rdpTicketHelperService.getTicketHelper(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void closeTicket(long ticketId, String statusMessage, String puid) {
        RdpTicketDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);
        this.rdpTicketProcessService.cancelAllProcess(ticketId);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CLOSED, statusMessage);
        this.rdpTicketHelperService.getTicketHelper(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void failTicket(long ticketId, String statusMessage, String puid) {
        RdpTicketDO ticketDO = checkTicket(ticketId, puid);
        checkInProgress(ticketDO);

        this.rdpTicketProcessService.failedAllProcess(ticketId);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.FAILED, statusMessage);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void cancelTicket(String puid, long ticketId, String statusMessage) {
        RdpTicketDO ticketDO = checkTicket(ticketId, puid);

        checkInProgress(ticketDO);
        this.rdpTicketProcessService.cancelAllProcess(ticketId);
        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.CANCELED, statusMessage);
        this.rdpTicketHelperService.getTicketHelper(ticketDO.getApproBiz()).approvalCanceled(ticketDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void approvalTicket(String puid, String uid, RdpApprovalTicketFO fo) {
        RdpTicketDO ticketDO = checkTicket(fo.getTicketId(), puid);
        if (ticketDO.getTicketStatus() != RdpTicketStatus.WAIT_APPROVAL) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_OPERATOR_TYPE_NOT_MATCH_STATUS.name()));
        }

        List<RdpApprovalPersonDO> persons = this.rdpApprovalPersonMapper.queryByTicketBzId(ticketDO.getBizId());
        List<String> allowUsers = persons.stream().map(RdpApprovalPersonDO::getPersonUid).collect(Collectors.toList());

        if (!allowUsers.contains(uid)) {
            throw new RuntimeException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NO_PERMISSION_ERROR.name()));
        }

        RdpExecStageContextMO execMO = new RdpExecStageContextMO();
        execMO.setExecUserName(Collections.singletonList(this.rdpUserMapper.queryByUid(uid).getUsername()));
        RdpTicketProcessDO processDO = this.rdpTicketProcessMapper.queryByStage(fo.getTicketId(), RdpTicketStage.APPROVAL);
        this.rdpTicketMapper.updateComment(ticketDO.getId(), fo.getComment());
        if (fo.isRejected()) {
            // WAIT_APPROVAL -> REJECTED
            rdpTicketHelperService.getTicketHelper(ticketDO.getApproBiz()).approvalRefuse(ticketDO.getId());
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
            } else {
                execMO.setExecMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_REJECTED_BY_APPROVAL.name()));
            }
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.REJECT, JsonUtils.toJson(execMO));
            this.rdpTicketMapper.updateTicketStatusByEnum(ticketDO.getId(), RdpTicketStatus.REJECTED, null);
        } else {
            // WAIT_APPROVAL -> WAIT_CONFIRM
            rdpTicketHelperService.getTicketHelper(ticketDO.getApproBiz()).approvalCompleted(ticketDO.getId());
            if (StringUtils.isNotBlank(fo.getComment())) {
                execMO.setExecMsg(fo.getComment());
                ticketDO.setApproComment(fo.getComment());
            } else {
                execMO.setExecMsg(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_ADOPT_BY_APPROVAL.name()));
            }
            this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, JsonUtils.toJson(execMO));
        }

        //  update real approval person
        this.rdpApprovalPersonMapper.deleteByTicketBzId(ticketDO.getBizId());
    }

    @Override
    public boolean isFinish(long ticketId) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        return ticketDO == null || RdpTicketStatus.isEndStatus(ticketDO.getTicketStatus());
    }

    @Override
    public RdpTicketBaseInfoVO queryTicketBaseInfo(String puid, String uid, RdpQueryTicketDetailFO fo) {
        updateStatusFromThirdPartyIfNecessary(fo);
        RdpTicketDO rdpTicketDO = checkTicket(fo.getTicketId(), puid);

        boolean isPrimary = uid.equals(puid);

        boolean isOwn = uid.equals(rdpTicketDO.getOwnerUid());

        RdpTicketBaseInfoVO vo = new RdpTicketBaseInfoVO();
        vo.setId(rdpTicketDO.getId());
        vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(rdpTicketDO.getGmtCreate()));
        vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(rdpTicketDO.getGmtModified()));
        vo.setDataSourceId(rdpTicketDO.getBindDsId());
        if (rdpTicketDO.getBindDsId() != null) {
            RdpDataSourceDO dsDO = this.rdpDsMapper.queryDsIdentityById(rdpTicketDO.getBindDsId());
            if (dsDO != null) {
                vo.setDataSourceType(dsDO.getDataSourceType());
                vo.setDsDeployType(dsDO.getDeployType());
            }
        }
        vo.setTargetInfo(rdpTicketDO.getTargetInfo());
        vo.setApproType(rdpTicketDO.getApproType());
        vo.setApproBiz(rdpTicketDO.getApproBiz());
        vo.setApproIdentity(rdpTicketDO.getApproIdentity());
        vo.setApproTemplateName(rdpTicketDO.getApproTemplateName());
        vo.setDescription(rdpTicketDO.getDescription());
        vo.setStatusMessage(rdpTicketDO.getStatusMessage());
        vo.setTicketTitle(rdpTicketDO.getTicketTitle());
        vo.setDsEnvName(rdpTicketDO.getEnvName());
        RdpTicketStatus ticketStatus = rdpTicketDO.getTicketStatus();
        vo.setTicketStatus(ticketStatus);

        List<RdpTicketProcessDO> dos = this.rdpTicketProcessMapper.listByTicketId(rdpTicketDO.getId());
        List<RdpTicketProcessVO> vos = dos.stream().map(RdpConvertUtils::convertToTicketProcessVO).collect(Collectors.toList());
        List<RdpApprovalPersonDO> persons = this.rdpApprovalPersonMapper.queryByTicketBzId(rdpTicketDO.getBizId());

        List<String> approvalPersonList = new ArrayList<>();

        persons.forEach(person -> {
            approvalPersonList.add(person.getPersonUid());
        });

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

        if (rdpTicketDO.getApproType() == RdpApprovalType.Internal && ticketStatus == RdpTicketStatus.WAIT_APPROVAL) {
            if (approvalPersonList.contains(uid) || isPrimary) {
                vo.setCanApproval(true);
            }
        }

        vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(rdpTicketDO.getFinishTime()));
        vo.setTicketProcessVOList(vos);
        RdpUserDO userByUid = this.rdpUserService.getUserByUid(rdpTicketDO.getOwnerUid());
        if (userByUid == null) {
            vo.setUserName(rdpTicketDO.getOwnerUid() + "(" + RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()) + ")");
        } else {
            vo.setUserName(userByUid.getUsername());
        }

        vo.setApproComment(rdpTicketDO.getApproComment());

        thirdPartyApprovalHandle(vo, rdpTicketDO);

        return vo;
    }

    private void updateStatusFromThirdPartyIfNecessary(RdpQueryTicketDetailFO fo) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(fo.getTicketId());
        if (ticketDO == null) {
            return;
        }
        if (fo.isRefreshCache() && ticketDO.getApproType() != RdpApprovalType.Internal && ticketDO.getTicketStatus() == RdpTicketStatus.WAIT_APPROVAL) {
            CgFuture<Boolean> cgFuture = asyncTaskWithResultService.submitTask(TaskType.getKey(TaskType.APPROVAL_LAST_STATUS, ticketDO.getId()), () -> refreshCache(ticketDO));
            try {
                // Wait for a maximum of 2 seconds
                cgFuture.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("call " + ticketDO.getApproType() + " api is running");
            }
        }

    }

    @Transactional
    public boolean refreshCache(RdpTicketDO ticketDO) {
        // avoid not create approval to refresh
        if (StringUtils.isEmpty(ticketDO.getApproIdentity())) {
            return false;
        }
        try {
            approvalService.refreshApprovalStatus(ticketDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() != ThirdPartyApiErrorType.CONNECTION_ERROR) {
                this.failTicket(ticketDO.getId(), RdpI18nUtils.getMessage(e.getMessageKey(), e.getMessageArgs()), ticketDO.getPrimaryUid());
            }
            return false;
        }

        return true;
    }

    private void thirdPartyApprovalHandle(RdpTicketBaseInfoVO dmTicketDetailVO, RdpTicketDO ticketDO) {
        if (ticketDO.getApproType() != RdpApprovalType.Internal) {
            List<RdpTicketProcessActivityDO> activities = this.rdpTicketProcessActivityMapper.queryByTicketId(ticketDO.getId());

            for (RdpTicketProcessVO vo : dmTicketDetailVO.getTicketProcessVOList()) {
                Long ticketProcessId = vo.getTicketProcessId();
                List<RdpTicketActivityVO> list = new ArrayList<>();
                if (vo.getTicketProcessStatus() == RdpTicketProcessStatus.FAIL) {
                    continue;
                }
                // just approval  now
                for (RdpTicketProcessActivityDO activity : activities) {
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
            dmTicketDetailVO.setApproTypeName(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_INTERNAL_TEMPLATE.name()));
        }
    }

    @Override
    public void retryTicket(String puid, long ticketId) {
        RdpTicketDO ticketDO = checkTicket(ticketId, puid);

        if (ticketDO.getTicketStatus() != RdpTicketStatus.EXEC_FAIL) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_RETRY_STATUS_DISCONTENT_ERROR.name()));
        }

        this.rdpTicketMapper.updateTicketStatusByEnum(ticketId, RdpTicketStatus.WAIT_EXEC, RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_WAIT_EXEC_MESSAGE.name()));
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
        IPage<RdpTicketDO> tickets = this.rdpTicketMapper.listAuthTicketByConditionAndPage(page, queryParams);
        return convertAndFillExtraInfo(tickets);
    }

    @Override
    public IPage<RdpTicketBasicVO> queryTicketListByPage(String puid, RdpListTicketFO fo) {
        IPage<RdpTicketDO> tickets;
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

    private IPage<RdpTicketBasicVO> convertAndFillExtraInfo(IPage<RdpTicketDO> tickets) {
        List<RdpTicketDO> records = tickets.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        // key is ticket id
        Map<Long, RdpUserDO> ticketUserMap = genTicketUserMap(records);
        // key is ticket id
        Map<Long, RdpDataSourceDO> ticketDsMap = genTicketDsMap(records);

        this.rdpDsEnvService.fillDsEnvInfo(new ArrayList<>(ticketDsMap.values()));
        List<RdpTicketBasicVO> vos = new ArrayList<>();

        for (RdpTicketDO ticketDO : records) {
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

    private void checkInProgress(RdpTicketDO ticketDO) {
        switch (ticketDO.getTicketStatus()) {
            case REJECTED:
            case FINISHED:
            case CLOSED:
            case CANCELED:
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_STATUS_FINAL_ERROR.name()));
            default:
                break;
        }
    }

    private IPage<RdpTicketDO> getCanConfirmTicketsByPage(RdpListTicketFO fo) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .approvalPersonUid(fo.getUid())
            .build();
        return this.rdpTicketMapper.listConfirmTicketByConditionAndPage(page, queryParams);
    }

    private Map<Long, RdpUserDO> genTicketUserMap(List<RdpTicketDO> tickets) {
        // keep order
        List<String> uids = tickets.stream().map(RdpTicketDO::getOwnerUid).collect(Collectors.toCollection(ArrayList::new));
        List<RdpUserDO> users = this.rdpUserMapper.listByUids(uids);

        // key is uid
        Map<String, RdpUserDO> userMap = users.stream().collect(Collectors.toMap(RdpUserDO::getUid, u -> u));

        Map<Long, RdpUserDO> ticketUserMap = new HashMap<>();
        for (RdpTicketDO ticketDO : tickets) {
            String uid = ticketDO.getOwnerUid();
            ticketUserMap.put(ticketDO.getId(), userMap.get(uid));
        }

        return ticketUserMap;
    }

    private Map<Long, RdpDataSourceDO> genTicketDsMap(List<RdpTicketDO> tickets) {
        // keep order
        Set<Long> dsIds = tickets.stream().map(RdpTicketDO::getBindDsId).collect(Collectors.toSet());

        // key is datasource id
        List<RdpDataSourceDO> dsList = this.rdpDsMapper.listByIdsIncludeDeleted(dsIds);
        Map<Long, RdpDataSourceDO> result = new HashMap<>();
        for (RdpDataSourceDO ds : dsList) {
            result.put(ds.getId(), ds);
        }

        // key is env id
        Collection<Long> envIds = dsList.stream().map(RdpDataSourceDO::getDsEnvId).collect(Collectors.toSet());
        if (!envIds.isEmpty()) {
            List<RdpDsEnvDO> envs = this.rdpEnvMapper.selectBatchIds(envIds);
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

    private IPage<RdpTicketDO> getUserCreatedTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(fo.getUid());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .uids(Collections.singletonList(String.valueOf(userDO.getUid())))
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();
        return this.rdpTicketMapper.listTicketByConditionAndPage(page, queryParams, puid);
    }

    private IPage<RdpTicketDO> getAllTicketsByPage(RdpListTicketFO fo, String puid) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());
        RdpTicketQueryObject queryParams = RdpTicketQueryObject.builder()
            .ticketStatus(fo.getTicketStatus())
            .ticketTitleName(fo.getTicketTitleName())
            .ticketId(fo.getTicketId())
            .startTime(getDateTimeOfTimestamp(fo.getStartTimeMs()))
            .endTime(getDateTimeOfTimestamp(fo.getEndTimeMs()))
            .build();

        return this.rdpTicketMapper.listTicketByConditionAndPage(page, queryParams, puid);
    }

    private RdpTicketDO checkTicket(long ticketId, String puid) {
        RdpTicketDO ticketDO = this.rdpTicketMapper.queryById(ticketId);
        if (ticketDO == null || ticketDO.getDeleted()) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }
        if (!ticketDO.getPrimaryUid().equals(puid)) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_BELONG_CURRENT_TEAM.name()));
        }

        return ticketDO;
    }
}
