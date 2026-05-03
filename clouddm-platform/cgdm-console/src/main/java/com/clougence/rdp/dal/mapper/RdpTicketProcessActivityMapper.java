package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpTicketProcessActivityDO;

public interface RdpTicketProcessActivityMapper extends BaseMapper<RdpTicketProcessActivityDO> {

    List<RdpTicketProcessActivityDO> queryByTicketId(Long ticketId);

    List<RdpTicketProcessActivityDO> queryByProcessId(Long processId);

    RdpTicketProcessActivityDO queryByProcessIdAndActivityIdForUpdate(@Param("processId") Long processId, @Param("activityId") String activityId);

    void updateContext(@Param("processId") Long processId, @Param("activityId") String activityId, @Param("context") String context);
}
