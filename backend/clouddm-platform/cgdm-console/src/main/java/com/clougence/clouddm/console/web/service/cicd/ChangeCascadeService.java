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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.model.vo.cicd.ChangeTransferVO;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeTransferDO;

public interface ChangeCascadeService {
    void createRootBatch(DmChangeDO rootChange);

    void onChangeFinished(DmChangeDO change);

    void onChangeTerminal(DmChangeDO change);

    List<DmChangeTransferDO> queryReadyTransfers(Date date, int limit);

    boolean assignTransfer(long transferId);

    void releaseTransfer(long transferId);

    int recoverStaleTransfers(Date staleBefore);

    int finishCompletedBatches();

    boolean hasRunningBatchForFlows(String ownerUid, Collection<Long> flowIds);

    List<ChangeTransferVO> queryDownstreamTransfers(String ownerUid, long sourceChangeId);

    Map<Long, List<ChangeTransferVO>> queryDownstreamTransfers(String ownerUid, Collection<Long> sourceChangeIds);

    void processTransfer(DmChangeTransferDO transfer);

    void markTransferFailure(DmChangeTransferDO transfer, Throwable error);

    void retryTransfer(String ownerUid, long transferId);
}
