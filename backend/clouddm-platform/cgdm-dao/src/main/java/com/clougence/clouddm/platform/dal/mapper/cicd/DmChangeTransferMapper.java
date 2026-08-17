/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.platform.dal.mapper.cicd;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeTransferDO;

public interface DmChangeTransferMapper extends BaseMapper<DmChangeTransferDO> {
    int insertIgnore(DmChangeTransferDO transfer);

    List<DmChangeTransferDO> queryReadyList(Date date, int limit);

    DmChangeTransferDO queryById(String ownerUid, long transferId);

    List<DmChangeTransferDO> queryBySourceChange(String ownerUid, long sourceChangeId);

    List<DmChangeTransferDO> queryBySourceChanges(String ownerUid, Collection<Long> sourceChangeIds);

    List<DmChangeTransferDO> queryByBatchIds(String ownerUid, Collection<Long> batchIds);

    List<DmChangeTransferDO> queryByBatchIdForUpdate(String ownerUid, long batchId);

    int assignPending(long transferId);

    int releaseProcessing(long transferId);

    int recoverStaleProcessing(Date staleBefore);

    int markSuccess(long transferId, long targetChangeId);

    int markRetry(long transferId, Date scheduleTime, String errorMessage);

    int markFailed(long transferId, String errorMessage);

    int retryFailed(String ownerUid, long transferId);
}
