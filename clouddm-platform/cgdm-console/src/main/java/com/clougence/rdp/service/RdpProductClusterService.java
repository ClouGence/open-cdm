package com.clougence.rdp.service;

import java.util.List;

import com.clougence.rdp.controller.model.fo.AddProductClusterFO;
import com.clougence.rdp.dal.model.RdpProductClusterDO;

/**
 * @author wanshao create time is 2019/12/12 9:36 下午
 **/
public interface RdpProductClusterService {

    List<RdpProductClusterDO> listProductCluster();

    String queryApiAddrByProductCode(String productCode);

    void addProductCluster(AddProductClusterFO clusterFO);

    void updateApiAddrById(Long id, String apiAddr);

    void updateProductVersionById(Long id, String productVersion);

    void deleteProductClusterById(Long id);
}
