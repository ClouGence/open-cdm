package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpEnvParamDO;

/**
 * @Author: Ekko
 * @Date: 2024-05-30 15:28
 */
public interface RdpEnvParamMapper extends BaseMapper<RdpEnvParamDO> {

    RdpEnvParamDO queryByParamKey(@Param("puid") String puid, @Param("paramKey") String paramKey, @Param("envId") long envId);

    List<RdpEnvParamDO> queryByParamKeySet(String puid, List<String> paramKeys);

    List<RdpEnvParamDO> queryByEnvIdAndPUid(@Param("envId") long envId, @Param("puid") String puid);

    List<RdpEnvParamDO> queryByUid(@Param("puid") String puid);

    int deleteEnvParam(@Param("paramKey") String paramKey, @Param("puid") String puid, @Param("envId") long envId);

    List<RdpEnvParamDO> queryByConfigKey(@Param("configKeys") List<String> configKeys);
}
