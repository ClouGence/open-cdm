package com.clougence.rdp.dal.mapper.authcode;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.authcode.RdpApplyCodeInfoDO;

@Deprecated
public interface RdpApplyCodeInfoMapper extends BaseMapper<RdpApplyCodeInfoDO> {

    List<RdpApplyCodeInfoDO> queryInfosByCc();

    RdpApplyCodeInfoDO queryUniqueInfosByCc(@Param("clusterIp") String clusterIp, @Param("clusterMacAddress") String clusterMacAddress,
                                            @Param("clusterHardwareUuid") String clusterHardwareUuid);

    List<RdpApplyCodeInfoDO> queryInfosByDm();

    RdpApplyCodeInfoDO queryUniqueInfosByDm(@Param("clusterIp") String clusterIp, @Param("clusterHardwareUuid") String clusterHardwareUuid);

    void refreshInfos(Date expiredTime);
}
