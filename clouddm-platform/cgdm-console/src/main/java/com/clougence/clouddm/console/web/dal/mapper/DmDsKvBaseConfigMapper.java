package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsKvBaseConfigDO;

/**
 * @author bucketli 2022/1/10 19:51:40
 */
public interface DmDsKvBaseConfigMapper extends BaseMapper<DmDsKvBaseConfigDO> {

    DmDsKvBaseConfigDO queryByDsIdAndConfigName(long dsId, String configName);

    List<DmDsKvBaseConfigDO> listByDsId(@Param("dsId") long dsId);

    List<DmDsKvBaseConfigDO> listByDsIdAndConfigNames(long dsId, List<String> configNames);

    List<DmDsKvBaseConfigDO> listByConfigName(String configName);

    void updateDsConfig(long dsId, String configName, String configValue);

    void deleteDsConfigs(long dsId);
}
