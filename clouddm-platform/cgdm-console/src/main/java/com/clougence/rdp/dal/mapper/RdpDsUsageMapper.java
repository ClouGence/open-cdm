package com.clougence.rdp.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsUsageEndpoint;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.rdp.dal.model.RdpDsUsageDO;

/**
 * @author wanshao create time is 2019/12/12 9:25 下午
 **/
public interface RdpDsUsageMapper extends BaseMapper<RdpDsUsageDO> {

    List<RdpDsUsageDO> listByDsId(Long dsId);

    List<RdpDsUsageDO> listByRes(Long dsId, ResourceType resType, Long resId, String resInstanceId, DsUsageEndpoint endpoint);

    void deleteByRes(Long dsId, ResourceType resType, Long resId, String resInstanceId, DsUsageEndpoint endpoint);
}
