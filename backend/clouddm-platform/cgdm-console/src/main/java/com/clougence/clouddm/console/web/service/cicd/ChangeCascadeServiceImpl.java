/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.console.web.service.cicd;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeTransferVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeCascadeServiceImpl implements ChangeCascadeService {
    private static final long RETRY_DELAY_MILLIS               = 10_000L;
    private static final int  TERMINAL_APPROVAL_RECOVERY_LIMIT = 100;

    @Resource
    private ApprovalDal       approvalDal;
    @Resource
    private ChangeFlowDal     changeFlowDal;
    @Resource
    private ObjectCacheDao    objectCacheDao;
    @Resource
    private SystemDal         systemDal;
    @Resource
    private ImSenderService   senderService;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void createRootBatch(DmChangeDO rootChange) {
        if (this.changeFlowDal.flowMapper().countChildren(rootChange.getOwnerUid(), rootChange.getRefFlowId()) == 0) {
            return;
        }
        DmChangeBatchDO batch = new DmChangeBatchDO();
        batch.setOwnerUid(rootChange.getOwnerUid());
        batch.setRefRootFlowId(rootChange.getRefFlowId());
        batch.setRefRootChangeId(rootChange.getId());
        batch.setBatchStatus(ChangeBatchStatus.RUNNING);
        this.changeFlowDal.batchMapper().insert(batch);
        if (this.changeFlowDal.changeMapper().updateBatch(rootChange.getId(), batch.getId()) != 1) {
            throw new IllegalStateException("failed to bind root change to cascade batch");
        }
        rootChange.setRefBatchId(batch.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void onChangeFinished(DmChangeDO change) {
        if (change.getRefBatchId() == null) {
            return;
        }
        List<DmChangeFlowDO> children = this.changeFlowDal.flowMapper().queryChildren(change.getOwnerUid(), change.getRefFlowId());
        for (DmChangeFlowDO child : children) {
            DmChangeTransferDO transfer = new DmChangeTransferDO();
            transfer.setOwnerUid(change.getOwnerUid());
            transfer.setRefBatchId(change.getRefBatchId());
            transfer.setRefSourceFlowId(change.getRefFlowId());
            transfer.setRefSourceChangeId(change.getId());
            transfer.setRefTargetFlowId(child.getId());
            transfer.setTransferStatus(ChangeTransferStatus.PENDING);
            transfer.setScheduleTime(new Date());
            transfer.setTryTimes(0);
            this.changeFlowDal.transferMapper().insertIgnore(transfer);
        }
        completeBatchIfPossible(change.getOwnerUid(), change.getRefBatchId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void onChangeTerminal(DmChangeDO change) {
        if (change.getRefBatchId() != null) {
            completeBatchIfPossible(change.getOwnerUid(), change.getRefBatchId());
        }
    }

    @Override
    public List<DmChangeTransferDO> queryReadyTransfers(Date date, int limit) {
        return this.changeFlowDal.transferMapper().queryReadyList(date, limit);
    }

    @Override
    public boolean assignTransfer(long transferId) {
        return this.changeFlowDal.transferMapper().assignPending(transferId) == 1;
    }

    @Override
    public void releaseTransfer(long transferId) {
        this.changeFlowDal.transferMapper().releaseProcessing(transferId);
    }

    @Override
    public int recoverStaleTransfers(Date staleBefore) {
        return this.changeFlowDal.transferMapper().recoverStaleProcessing(staleBefore);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public int finishCompletedBatches() {
        List<DmChangeBatchDO> batches = this.changeFlowDal.batchMapper().queryRunningList();
        recoverCanceledApprovalChanges(batches);
        int finishedCount = 0;
        for (DmChangeBatchDO batch : batches) {
            if (completeBatchIfPossible(batch.getOwnerUid(), batch.getId())) {
                finishedCount++;
            }
        }
        return finishedCount;
    }

    private void recoverCanceledApprovalChanges(List<DmChangeBatchDO> batches) {
        Map<String, List<Long>> batchIdsByOwner = new LinkedHashMap<>();
        for (DmChangeBatchDO batch : batches) {
            batchIdsByOwner.computeIfAbsent(batch.getOwnerUid(), key -> new ArrayList<>()).add(batch.getId());
        }
        List<DmChangeDO> candidates = new ArrayList<>();
        for (Map.Entry<String, List<Long>> entry : batchIdsByOwner.entrySet()) {
            List<DmChangeDO> changes = this.changeFlowDal.changeMapper().queryByBatchIds(entry.getKey(), entry.getValue());
            for (DmChangeDO change : changes) {
                if (change.isLockStatus() || change.getCurrentStep() != ChangeStep.APPROVAL || change.getCurrentStatus() != ChangeStatus.FAILED) {
                    continue;
                }
                candidates.add(change);
                if (candidates.size() == TERMINAL_APPROVAL_RECOVERY_LIMIT) {
                    break;
                }
            }
            if (candidates.size() == TERMINAL_APPROVAL_RECOVERY_LIMIT) {
                break;
            }
        }
        for (DmChangeDO change : candidates) {
            List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.TICKET);
            if (items.isEmpty() || StringUtils.isBlank(items.get(0).getContent())) {
                continue;
            }
            ChangeTicketInfo ticketInfo = JsonUtils.toObj(items.get(0).getContent(), ChangeTicketInfo.class);
            if (ticketInfo == null || ticketInfo.getTicketId() == null) {
                continue;
            }
            DmApprovalDO approval = this.approvalDal.approvalMapper().queryById(ticketInfo.getTicketId());
            if (approval == null || (approval.getTicketStatus() != ApprovalStatus.CLOSED && approval.getTicketStatus() != ApprovalStatus.CANCELED)) {
                continue;
            }
            if (this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion()) == 1) {
                log.info("recovered canceled approval change " + change.getId() + " as cascade terminal");
            }
        }
    }

    @Override
    public boolean hasRunningBatchForFlows(String ownerUid, Collection<Long> flowIds) {
        if (flowIds == null || flowIds.isEmpty()) {
            return false;
        }
        List<DmChangeBatchDO> batches = this.changeFlowDal.batchMapper().queryRunningByOwner(ownerUid);
        if (batches.isEmpty()) {
            return false;
        }
        Set<Long> relatedFlowIds = new HashSet<>(flowIds);
        List<Long> batchIds = new ArrayList<>(batches.size());
        for (DmChangeBatchDO batch : batches) {
            batchIds.add(batch.getId());
        }
        List<DmChangeDO> changes = this.changeFlowDal.changeMapper().queryByBatchIds(ownerUid, batchIds);
        for (DmChangeDO change : changes) {
            if (relatedFlowIds.contains(change.getRefFlowId())) {
                return true;
            }
        }
        List<DmChangeTransferDO> transfers = this.changeFlowDal.transferMapper().queryByBatchIds(ownerUid, batchIds);
        for (DmChangeTransferDO transfer : transfers) {
            if (relatedFlowIds.contains(transfer.getRefSourceFlowId()) || relatedFlowIds.contains(transfer.getRefTargetFlowId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ChangeTransferVO> queryDownstreamTransfers(String ownerUid, long sourceChangeId) {
        return queryDownstreamTransfers(ownerUid, Collections.singleton(sourceChangeId)).getOrDefault(sourceChangeId, Collections.emptyList());
    }

    @Override
    public Map<Long, List<ChangeTransferVO>> queryDownstreamTransfers(String ownerUid, Collection<Long> sourceChangeIds) {
        Map<Long, List<ChangeTransferVO>> result = new LinkedHashMap<>();
        Map<Long, Long> rootByChange = new HashMap<>();
        Set<Long> pendingChanges = new HashSet<>();
        Set<Long> visitedChanges = new HashSet<>();
        Set<Long> visitedTransfers = new HashSet<>();
        for (Long sourceChangeId : sourceChangeIds) {
            if (sourceChangeId == null) {
                continue;
            }
            result.put(sourceChangeId, new ArrayList<>());
            rootByChange.put(sourceChangeId, sourceChangeId);
            pendingChanges.add(sourceChangeId);
            visitedChanges.add(sourceChangeId);
        }
        while (!pendingChanges.isEmpty()) {
            List<DmChangeTransferDO> transfers = this.changeFlowDal.transferMapper().queryBySourceChanges(ownerUid, pendingChanges);
            Set<Long> targetFlowIds = new HashSet<>();
            Set<Long> targetChangeIds = new HashSet<>();
            for (DmChangeTransferDO transfer : transfers) {
                targetFlowIds.add(transfer.getRefTargetFlowId());
                if (transfer.getRefTargetChangeId() != null) {
                    targetChangeIds.add(transfer.getRefTargetChangeId());
                }
            }

            Map<Long, DmChangeFlowDO> targetFlows = new HashMap<>();
            Map<Long, String> targetFlowManagerNames = new HashMap<>();
            if (!targetFlowIds.isEmpty()) {
                for (DmChangeFlowDO flow : this.changeFlowDal.flowMapper().queryByIds(ownerUid, targetFlowIds)) {
                    targetFlows.put(flow.getId(), flow);
                    UserCacheEntry manager = this.objectCacheDao.queryByUid(flow.getFlowManagerUid());
                    String managerName;
                    if (manager == null) {
                        managerName = "UID:" + flow.getFlowManagerUid();
                    } else {
                        managerName = manager.getUserName();
                    }
                    targetFlowManagerNames.put(flow.getId(), managerName);
                }
            }
            Map<Long, DmChangeDO> targetChanges = new HashMap<>();
            if (!targetChangeIds.isEmpty()) {
                for (DmChangeDO change : this.changeFlowDal.changeMapper().queryByIds(ownerUid, targetChangeIds)) {
                    targetChanges.put(change.getId(), change);
                }
            }

            Set<Long> nextChanges = new HashSet<>();
            for (DmChangeTransferDO transfer : transfers) {
                if (!visitedTransfers.add(transfer.getId())) {
                    continue;
                }
                Long rootChangeId = rootByChange.get(transfer.getRefSourceChangeId());
                if (rootChangeId == null) {
                    continue;
                }
                DmChangeFlowDO target = targetFlows.get(transfer.getRefTargetFlowId());
                ChangeTransferVO item = new ChangeTransferVO();
                item.setTransferId(transfer.getId());
                item.setSourceChangeId(transfer.getRefSourceChangeId());
                item.setTargetFlowId(transfer.getRefTargetFlowId());
                item.setTargetFlowName(target == null ? null : target.getFlowName());
                item.setTargetFlowManagerName(targetFlowManagerNames.get(transfer.getRefTargetFlowId()));
                item.setTargetChangeId(transfer.getRefTargetChangeId());
                item.setStatus(transfer.getTransferStatus());
                item.setErrorMessage(transfer.getLastError());
                if (transfer.getRefTargetChangeId() != null) {
                    DmChangeDO targetChange = targetChanges.get(transfer.getRefTargetChangeId());
                    if (targetChange != null) {
                        item.setTargetChangeName(targetChange.getChangeName());
                        item.setTargetChangeStep(targetChange.getCurrentStep());
                        item.setTargetChangeStatus(targetChange.getCurrentStatus());
                    }
                }
                result.get(rootChangeId).add(item);
                Long targetChangeId = transfer.getRefTargetChangeId();
                if (targetChangeId != null && visitedChanges.add(targetChangeId)) {
                    rootByChange.put(targetChangeId, rootChangeId);
                    nextChanges.add(targetChangeId);
                }
            }
            pendingChanges = nextChanges;
        }
        return result;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void processTransfer(DmChangeTransferDO transfer) {
        DmChangeTransferDO current = this.changeFlowDal.transferMapper().queryById(transfer.getOwnerUid(), transfer.getId());
        if (current == null || current.getTransferStatus() != ChangeTransferStatus.PROCESSING) {
            return;
        }
        if (current.getRefTargetChangeId() != null) {
            this.changeFlowDal.transferMapper().markSuccess(current.getId(), current.getRefTargetChangeId());
            return;
        }

        DmChangeDO source = this.changeFlowDal.changeMapper().queryChangeById(current.getRefSourceChangeId());
        DmChangeFlowDO target = this.changeFlowDal.flowMapper().queryByOwnerAndId(current.getOwnerUid(), current.getRefTargetFlowId());
        if (source == null || target == null || target.isDeleted() || !target.isEnable() || target.getChangeFlowStatus() != ChangeFlowStatus.NORMAL
            || target.getFlowType() != ChangeFlowType.BUILT_IN || target.getRefParentFlowId() == null
            || !Objects.equals(target.getRefParentFlowId(), current.getRefSourceFlowId())) {
            throw new IllegalStateException("parent-child change flow relation is unavailable");
        }

        DmChangeDO child = new DmChangeDO();
        child.setOwnerUid(source.getOwnerUid());
        child.setTriggerUid(source.getTriggerUid());
        child.setRefFlowId(target.getId());
        child.setRefBatchId(source.getRefBatchId());
        child.setRefParentChangeId(source.getId());
        child.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_PARENT_NAME.name(), source.getChangeName()));
        child.setChangeTime(new Date());
        child.setChangeBranch(source.getChangeBranch());
        child.setCurrentStep(ChangeStep.APPROVAL);
        child.setCurrentStatus(ChangeStatus.READY);
        child.setVersion(0);
        child.setTryTimes(0);
        child.setLastCommitId(source.getLastCommitId());
        child.setLockStatus(false);
        this.changeFlowDal.changeMapper().insert(child);

        copyItems(source, child, ChangeItemType.SQL);
        copyItems(source, child, ChangeItemType.REVIEW);
        if (this.changeFlowDal.transferMapper().markSuccess(current.getId(), child.getId()) != 1) {
            throw new IllegalStateException("change transfer state changed concurrently");
        }
    }

    private void copyItems(DmChangeDO source, DmChangeDO target, ChangeItemType type) {
        List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(source.getOwnerUid(), source.getId(), type);
        for (DmChangeItemDO item : items) {
            DmChangeItemDO copied = new DmChangeItemDO();
            copied.setOwnerUid(target.getOwnerUid());
            copied.setRefFlowId(target.getRefFlowId());
            copied.setRefChangeId(target.getId());
            copied.setChangeItemType(item.getChangeItemType());
            copied.setContentName(item.getContentName());
            copied.setContentIndex(item.getContentIndex());
            copied.setContent(item.getContent());
            this.changeFlowDal.changeItemMapper().insert(copied);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void markTransferFailure(DmChangeTransferDO transfer, Throwable error) {
        String errorMessage = ExceptionUtils.getRootCauseMessage(error);
        int maxFailedTimes = maxFailedTimes(transfer.getOwnerUid());
        if (transfer.getTryTimes() + 1 >= maxFailedTimes) {
            this.changeFlowDal.transferMapper().markFailed(transfer.getId(), errorMessage);
            notifyTransferFailure(transfer, errorMessage);
        } else {
            Date retryAt = new Date(System.currentTimeMillis() + RETRY_DELAY_MILLIS);
            this.changeFlowDal.transferMapper().markRetry(transfer.getId(), retryAt, errorMessage);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void retryTransfer(String ownerUid, long transferId) {
        if (this.changeFlowDal.transferMapper().retryFailed(ownerUid, transferId) != 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_TRANSFER_NOT_RETRYABLE_ERROR.name()));
        }
    }

    private boolean completeBatchIfPossible(String ownerUid, long batchId) {
        DmChangeBatchDO batch = this.changeFlowDal.batchMapper().queryByIdForUpdate(ownerUid, batchId);
        if (batch == null || batch.getBatchStatus() != ChangeBatchStatus.RUNNING) {
            return false;
        }
        List<DmChangeDO> changes = this.changeFlowDal.changeMapper().queryByBatchIdForUpdate(ownerUid, batchId);
        for (DmChangeDO change : changes) {
            if (!change.isLockStatus()) {
                return false;
            }
        }
        List<DmChangeTransferDO> transfers = this.changeFlowDal.transferMapper().queryByBatchIdForUpdate(ownerUid, batchId);
        for (DmChangeTransferDO transfer : transfers) {
            if (transfer.getTransferStatus() != ChangeTransferStatus.SUCCESS) {
                return false;
            }
        }
        return this.changeFlowDal.batchMapper().finishRunningBatch(ownerUid, batchId) == 1;
    }

    private void notifyTransferFailure(DmChangeTransferDO transfer, String errorMessage) {
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_TRANSFER_FAILED_NOTICE.name(), errorMessage);
        try {
            this.senderService.sendMessage(transfer.getOwnerUid(), transfer.getRefSourceFlowId(), ImMessageType.ChangeNotice, message);
        } catch (Exception e) {
            log.warn("notify source change flow transfer failure failed", e);
        }
        try {
            this.senderService.sendMessage(transfer.getOwnerUid(), transfer.getRefTargetFlowId(), ImMessageType.ChangeNotice, message);
        } catch (Exception e) {
            log.warn("notify target change flow transfer failure failed", e);
        }
    }

    private int maxFailedTimes(String ownerUid) {
        DmSysUserConfDO config = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, RootUserConfig.Fields.cicdMaxFailedTimes);
        if (config == null || StringUtils.isBlank(config.getConfigValue())) {
            return 3;
        }
        return Integer.parseInt(config.getConfigValue());
    }
}
