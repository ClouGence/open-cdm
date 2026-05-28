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
package com.clougence.rdp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsUsageDO;
import com.clougence.rdp.service.RdpDsUsageService;

import jakarta.annotation.Resource;

/**
 * @author bucketli 2024/2/27 11:20:29
 */
@Service
public class RdpDsUsageServiceImpl implements RdpDsUsageService {
    @Resource
    private DataSourceDal datasourceDal;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void addDsUsages(List<DmDsUsageDO> usageDOs) {
        for (DmDsUsageDO usageDO : usageDOs) {
            datasourceDal.usageMapper().insert(usageDO);
        }
    }

    @Override
    public List<DmDsUsageDO> listDsUsage(Long dsId) {
        return datasourceDal.usageMapper().listByDsId(dsId);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteDsUsage(List<DmDsUsageDO> dsUsages) {
        for (DmDsUsageDO usageDO : dsUsages) {
            //check for different product cluster with same res_id, res_instance_id and endpoint.
            List<DmDsUsageDO> usageDOS = datasourceDal.usageMapper()
                .listByRes(usageDO.getDsId(), usageDO.getResType(), usageDO.getResId(), usageDO.getResInstanceId(), usageDO.getEndpoint());
            if (usageDOS != null && usageDOS.size() > 1) {
                throw new IllegalArgumentException("DataSource usage info is duplicated, dsId:" + usageDO.getDsId() + ",resId:" + usageDO.getResId());
            }

            //like child data_job have no usage info
            if (usageDOS == null || usageDOS.isEmpty()) {
                continue;
            }

            datasourceDal.usageMapper().deleteByRes(usageDO.getDsId(), usageDO.getResType(), usageDO.getResId(), usageDO.getResInstanceId(), usageDO.getEndpoint());
        }
    }
}
