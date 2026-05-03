package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.enumeration.RdpTicketProcessStatus;
import com.clougence.rdp.dal.enumeration.RdpTicketStage;
import com.clougence.rdp.dal.model.RdpTicketProcessDO;

/**
 * @author wanshao create time is 2021/1/26
 **/
public interface RdpTicketProcessMapper extends BaseMapper<RdpTicketProcessDO> {

    /** @return Ticket process items are returned with order */
    List<RdpTicketProcessDO> listByTicketId(Long ticketId);

    int logicDeleteByIds(@Param("ids") List<Long> ids);

    int updateContextById(Long id, String context);

    int updateModified(Long id);

    RdpTicketProcessDO queryByStage(long ticketId, RdpTicketStage ticketStage);

    default RdpTicketProcessDO queryTicketProcessById(long ticketId, long processId) {
        RdpTicketProcessDO processDO = selectById(processId);
        if (processDO.getTicketId() == ticketId) {
            return processDO;
        } else {
            return null;
        }
    }

    void updateNotEndProcessByTicketId(long ticketId, RdpTicketProcessStatus status);

    void updateProcessStatusByTicketIdAndStage(@Param("ticketId") long ticketId, @Param("stage") RdpTicketStage stage, @Param("status") RdpTicketProcessStatus status);

    void updateProcessStatus(@Param("id") Long id, @Param("status") String status, @Param("context") String context);

    default void updateTicketStatusByEnum(Long id, RdpTicketProcessStatus status, String context) {
        this.updateProcessStatus(id, status.name(), context);
    }
}
