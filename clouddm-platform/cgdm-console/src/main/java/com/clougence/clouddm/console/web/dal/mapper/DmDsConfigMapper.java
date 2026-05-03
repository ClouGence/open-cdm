package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.rdp.dal.enumeration.HostType;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmDsConfigMapper extends BaseMapper<DmDsConfigDO> {

    Long queryEnableDsCount();

    DmDsConfigDO queryById(String ownerUid, long dataSourceId);

    List<DmDsConfigDO> queryByIds(String ownerUid, List<Long> ids);

    List<DmDsConfigDO> queryByUid(String ownerUid);

    void deleteByDisable(String ownerUid, long dataSourceId);

    List<DmDsConfigDO> queryByClusterId(long clusterId);

    void updateStatusByDataSourceId(long dataSourceId, DataSourceStatus status);

    void updateHostTypeById(long id, HostType hostType);

    DmDsConfigDO queryByDataSourceId(long dataSourceId);

    List<DmDsConfigDO> queryByDataSourceIds(List<Long> dataSourceIds);

    void updateDevOps(String ownerUid, long dataSourceId, boolean enableDevops);
}
