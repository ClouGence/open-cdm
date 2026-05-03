package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpApprovalPersonDO;

/**
 * @author Ekko
 * @date 2024/6/21 14:49
*/
public interface RdpApprovalPersonMapper extends BaseMapper<RdpApprovalPersonDO> {

    int insertTemplateBatch(@Param("cacheList") List<RdpApprovalPersonDO> cacheList);

    List<RdpApprovalPersonDO> queryByTicketBzId(@Param("ticketBzId") String ticketBzId);

    int deleteByTicketBzId(@Param("ticketBzId") String ticketBzId);
}
