package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpAlertEventLogDO;
import com.clougence.rdp.service.enumeration.AlertEventStatus;

public interface RdpAlertEventLogMapper extends BaseMapper<RdpAlertEventLogDO> {

    List<RdpAlertEventLogDO> queryPageAlertEventLogs(long startId, int pageSize, Date startTime, Date endTime, AlertEventStatus status, String uid);
}
