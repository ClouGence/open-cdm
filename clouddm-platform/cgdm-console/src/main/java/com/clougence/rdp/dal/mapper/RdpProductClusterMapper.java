package com.clougence.rdp.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpProductClusterDO;

/**
 * @author bucketli 2020/2/29 12:01
 */
public interface RdpProductClusterMapper extends BaseMapper<RdpProductClusterDO> {

    List<String> supportProductType();

    List<RdpProductClusterDO> listProductCluster();

    RdpProductClusterDO queryByClusterCode(String clusterCode);

    RdpProductClusterDO queryByClusterName(String clusterName);

    int updateApiAddrById(Long id, String apiAddr);

    int updateProductVersionById(Long id, String productVersion);
}
