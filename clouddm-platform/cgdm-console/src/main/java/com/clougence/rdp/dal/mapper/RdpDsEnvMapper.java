package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpDsEnvDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface RdpDsEnvMapper extends BaseMapper<RdpDsEnvDO> {

    List<RdpDsEnvDO> queryListByParameterKey(@Param("puid") String puid, @Param("paramKey") String paramKey);

    List<RdpDsEnvDO> queryListByUid(@Param("puid") String puid);

    List<RdpDsEnvDO> queryListByUidAndId(@Param("puid") String puid, @Param("envIds") List<Long> envIds);

    List<RdpDsEnvDO> listByCondition(String ownerUid, String envName);

    int updateDsEnv(Long id, String ownerUid, String envName, String description);

    int deleteDsEnv(Long id, String ownerUid);

    List<RdpDsEnvDO> listByIds(List<Long> ids);

    RdpDsEnvDO queryByEnvName(String ownerUid, String envName);

    RdpDsEnvDO queryByEnvID(String ownerUid, Long envId);
}
