package com.clougence.rdp.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.dal.model.RdpTicketDO;
import com.clougence.rdp.dal.model.queryobj.RdpTicketQueryObject;

/**
 * @author Ekko
 * @date 2024/5/13 14:43
*/
public interface RdpTicketMapper extends BaseMapper<RdpTicketDO> {

    String getTicketBzId(@Param("ticketId") long ticketId);

    long getBindDsId(@Param("tickedBzId") String tickedBzId);

    int updateApproIdentity(long ticketId, String approIdentity);

    void updateTicketStatus(long ticketId, String ticketStatus, String statusMessage);

    default void updateTicketStatusByEnum(long ticketId, RdpTicketStatus ticketStatus, String statusMessage) {
        this.updateTicketStatus(ticketId, ticketStatus.name(), statusMessage);
    }

    void updateErrorCount(@Param("ticketId") long ticketId, @Param("count") Integer count);

    //    void updateTicketStatistics(long ticketId, Long expectedAffectedRows, Integer riskSqlCount);

    @Deprecated // this mapper query not safe.
    List<RdpTicketDO> listTicketByCondition(RdpTicketQueryObject ticketQueryObject);

    IPage<RdpTicketDO> listTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject, @Param("puid") String puid);

    IPage<RdpTicketDO> listConfirmTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject);

    IPage<RdpTicketDO> listAuthTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject);

    List<RdpTicketDO> listTicketByStatus(@Param("status") List<RdpTicketStatus> status, @Param("testDate") Date testDate);

    List<RdpTicketDO> listTicketByApproval(@Param("status") RdpTicketStatus status, @Param("ticketId") Long ticketId);

    Page<RdpTicketDO> listTicketByPage(@Param("status") RdpTicketStatus status, Page page);

    int logicDeleteByIds(@Param("ids") List<Long> ids);

    RdpTicketDO queryByBizId(String bizId);

    RdpTicketDO queryById(Long id);

    RdpTicketDO queryByApproIdentity(@Param("approIdentity") String approIdentity, @Param("type") String type, @Param("puid") String puid);

    int updateModified(Long id);

    List<Long> listUnFinishTicketIdList(@Param("time") Date time);

    RdpTicketDO selectByIdForUpdate(@Param("ticketId") Long ticketId);

    void updateThirdApprovalInfo(@Param("ticketId") Long ticketId, @Param("approvalIdentity") String approvalIdentity, @Param("url") String url);

    void updateComment(@Param("ticketId") Long ticketId, @Param("comment") String comment);
}
