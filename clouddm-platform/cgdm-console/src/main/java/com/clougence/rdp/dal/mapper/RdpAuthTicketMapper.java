package com.clougence.rdp.dal.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpAuthTicketDO;

/**
 * @author wanshao create time is 2019/12/12 9:25 下午
 **/
public interface RdpAuthTicketMapper extends BaseMapper<RdpAuthTicketDO> {

    RdpAuthTicketDO getAuthTicketInfo(@Param("rdpTickedInsId") String rdpTickedInsId);
}
