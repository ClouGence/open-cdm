package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmConsoleHeartbeatDO;

@Deprecated
public interface DmConsoleHeartbeatMapper extends BaseMapper<DmConsoleHeartbeatDO> {

    List<DmConsoleHeartbeatDO> queryActive();

    void updateHeartbeatByIds(@Param("hbDOs") List<DmConsoleHeartbeatDO> hbDOs);

    DmConsoleHeartbeatDO queryByConsoleInfo(@Param("consoleIp") String consoleIp, @Param("macAddress") String macAddress, @Param("hardwareUuid") String hardwareUuid);

}
