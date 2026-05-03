package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.dal.model.RdpDsUsageDO;

/**
 * @author bucketli 2024/2/27 11:17:36
 */
public interface RdpDsUsageService {

    void addDsUsages(List<RdpDsUsageDO> usageDOs);

    List<RdpDsUsageDO> listDsUsage(Long dsId);

    void deleteDsUsage(List<RdpDsUsageDO> dsUsages);
}
