package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpDsKvBaseConfigDO;

/**
 * @author bucketli 2022/1/10 19:51:40
 */
public interface RdpDsKvBaseConfigMapper extends BaseMapper<RdpDsKvBaseConfigDO> {

    RdpDsKvBaseConfigDO queryByDsIdAndConfigName(long dsId, String configName);

    List<RdpDsKvBaseConfigDO> listByDsId(@Param("dsId") long dsId);

    List<RdpDsKvBaseConfigDO> listByDsIdAndConfigNames(long dsId, List<String> configNames);

    void updateDsConfig(long dsId, String configName, String configValue);

    void deleteDsConfigs(long dsId);
}
