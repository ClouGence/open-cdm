package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.constants.CloudOrIdcName;
import com.clougence.clouddm.console.web.dal.model.DmClusterDO;

/**
 * @author wanshao create time is 2019/12/12 9:26 下午
 **/
public interface DmClusterMapper extends BaseMapper<DmClusterDO> {

    DmClusterDO queryById(long resId);

    void updateClusterDesc(DmClusterDO clusterDO);

    List<DmClusterDO> listByIds(@Param("ids") List<Long> ids);

    List<DmClusterDO> listByCondition(String ownerUid, String clusterName, CloudOrIdcName cloudOrIdcName, String region, String clusterDesc);

    DmClusterDO getClusterByName(String clusterName);
}
