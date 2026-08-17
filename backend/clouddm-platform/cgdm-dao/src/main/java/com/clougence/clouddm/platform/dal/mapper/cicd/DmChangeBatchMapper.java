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
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeBatchDO;

public interface DmChangeBatchMapper extends BaseMapper<DmChangeBatchDO> {
    DmChangeBatchDO queryRunningByRootFlow(String ownerUid, long rootFlowId);

    DmChangeBatchDO queryById(String ownerUid, long batchId);

    DmChangeBatchDO queryByIdForUpdate(String ownerUid, long batchId);

    List<DmChangeBatchDO> queryByIds(String ownerUid, Collection<Long> batchIds);

    List<DmChangeBatchDO> queryRunningList();

    List<DmChangeBatchDO> queryRunningByOwner(String ownerUid);

    int finishRunningBatch(String ownerUid, long batchId);

    int countRunningByRootFlow(String ownerUid, long rootFlowId);

}
