package com.clougence.rdp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.rdp.controller.model.fo.AddProductClusterFO;
import com.clougence.rdp.dal.mapper.RdpProductClusterMapper;
import com.clougence.rdp.dal.model.RdpProductClusterDO;
import com.clougence.rdp.global.exception.ConsoleRuntimeException;
import com.clougence.rdp.service.RdpNamingService;
import com.clougence.rdp.service.RdpProductClusterService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/11/23 16:10:41
 */
@Slf4j
@Service
public class RdpProductClusterServiceImpl implements RdpProductClusterService {

    @Resource
    private RdpProductClusterMapper rdpProductClusterMapper;

    @Resource
    private RdpNamingService        rdpNamingService;

    @Override
    public List<RdpProductClusterDO> listProductCluster() {
        //add auth check later
        return rdpProductClusterMapper.listProductCluster();
    }

    @Override
    public String queryApiAddrByProductCode(String productCode) {
        // if productCode is empty and only have one product cluster, just return the only one product api.
        if (StringUtils.isBlank(productCode)) {
            List<RdpProductClusterDO> allCluster = rdpProductClusterMapper.listProductCluster();
            // compatible no product cluster
            if (allCluster == null || allCluster.isEmpty()) {
                // no redirect
                return null;
            }

            // compatible one product cluster
            if (allCluster.size() == 1) {
                return allCluster.get(0).getApiAddr();
            }

            throw new IllegalArgumentException("productCode can not be empty.");
        }

        RdpProductClusterDO clusterDO = rdpProductClusterMapper.queryByClusterCode(productCode);

        return clusterDO == null ? null : clusterDO.getApiAddr();
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void addProductCluster(AddProductClusterFO clusterFO) {
        String clusterCode = clusterFO.getClusterCode();
        if (StringUtils.isBlank(clusterCode)) {
            clusterCode = rdpNamingService.genProductClusterCode();
            RdpProductClusterDO clusterDO = rdpProductClusterMapper.queryByClusterCode(clusterCode);
            if (clusterDO != null) {
                //only retry once
                clusterCode = rdpNamingService.genProductClusterCode();
            }
        }

        RdpProductClusterDO clusterDO = rdpProductClusterMapper.queryByClusterCode(clusterCode);
        if (clusterDO != null) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.ALREADY_HAVE_SAME_CODE_OF_PRODUCT_CLUSTER, clusterDO.getClusterCode());
        }

        String clusterName = rdpNamingService.genProductClusterName();
        RdpProductClusterDO t = rdpProductClusterMapper.queryByClusterName(clusterName);
        if (t != null) {
            //only retry once
            clusterName = rdpNamingService.genProductClusterName();
            t = rdpProductClusterMapper.queryByClusterName(clusterName);
            if (t != null) {
                throw new ConsoleRuntimeException(ConsoleErrorCode.ALREADY_HAVE_SAME_NAME_OF_PRODUCT_CLUSTER);
            }
        }

        RdpProductClusterDO i = new RdpProductClusterDO();
        i.setClusterCode(clusterCode);
        i.setClusterName(clusterName);
        i.setProduct(clusterFO.getProduct());
        i.setProductVersion(clusterFO.getProductVersion());
        i.setApiAddr(clusterFO.getApiAddr());

        if (StringUtils.isNotBlank(clusterFO.getClusterDesc())) {
            i.setClusterDesc(clusterFO.getClusterDesc());
        } else {
            i.setClusterDesc(clusterName);
        }

        rdpProductClusterMapper.insert(i);
    }

    @Override
    public void updateApiAddrById(Long id, String apiAddr) {
        if (StringUtils.isBlank(apiAddr)) {
            throw new IllegalArgumentException("apiAddr can not be blank.");
        }

        rdpProductClusterMapper.updateApiAddrById(id, apiAddr);
    }

    @Override
    public void updateProductVersionById(Long id, String productVersion) {
        if (StringUtils.isBlank(productVersion)) {
            throw new IllegalArgumentException("productVersion can not be blank.");
        }

        rdpProductClusterMapper.updateProductVersionById(id, productVersion);
    }

    @Override
    public void deleteProductClusterById(Long id) {
        rdpProductClusterMapper.deleteById(id);
    }
}
