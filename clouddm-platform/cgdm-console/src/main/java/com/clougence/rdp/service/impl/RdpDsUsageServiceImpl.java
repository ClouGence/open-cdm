package com.clougence.rdp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.rdp.dal.mapper.RdpDsUsageMapper;
import com.clougence.rdp.dal.model.RdpDsUsageDO;
import com.clougence.rdp.service.RdpDsUsageService;

/**
 * @author bucketli 2024/2/27 11:20:29
 */
@Service
public class RdpDsUsageServiceImpl implements RdpDsUsageService {

    @Resource
    private RdpDsUsageMapper rdpDsUsageMapper;

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void addDsUsages(List<RdpDsUsageDO> usageDOs) {
        for (RdpDsUsageDO usageDO : usageDOs) {
            rdpDsUsageMapper.insert(usageDO);
        }
    }

    @Override
    public List<RdpDsUsageDO> listDsUsage(Long dsId) {
        return rdpDsUsageMapper.listByDsId(dsId);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteDsUsage(List<RdpDsUsageDO> dsUsages) {
        for (RdpDsUsageDO usageDO : dsUsages) {
            //check for different product cluster with same res_id, res_instance_id and endpoint.
            List<RdpDsUsageDO> usageDOS = rdpDsUsageMapper
                .listByRes(usageDO.getDsId(), usageDO.getResType(), usageDO.getResId(), usageDO.getResInstanceId(), usageDO.getEndpoint());
            if (usageDOS != null && usageDOS.size() > 1) {
                throw new IllegalArgumentException("DataSource usage info is duplicated, dsId:" + usageDO.getDsId() + ",resId:" + usageDO.getResId());
            }

            //like child data_job have no usage info
            if (usageDOS == null || usageDOS.isEmpty()) {
                continue;
            }

            rdpDsUsageMapper.deleteByRes(usageDO.getDsId(), usageDO.getResType(), usageDO.getResId(), usageDO.getResInstanceId(), usageDO.getEndpoint());
        }
    }
}
