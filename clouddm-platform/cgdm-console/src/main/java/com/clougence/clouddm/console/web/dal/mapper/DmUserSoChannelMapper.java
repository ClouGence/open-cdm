package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmUserSoChannelDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmUserSoChannelMapper extends BaseMapper<DmUserSoChannelDO> {

    DmUserSoChannelDO queryByUser(String ownerUid, long dataSourceId);

    void deleteByUser(String ownerUid, long dataSourceId);

    List<DmUserSoChannelDO> queryByClusterId(long clusterId);
}
