package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmTicketDO;

/**
 * @author Ekko
 * @date 2024/5/9 11:38
*/
public interface DmTicketMapper extends BaseMapper<DmTicketDO> {

    long getDmTickedId(@Param("rdpTickedInsId") String rdpTickedInsId);

    int logicDeleteByIds(@Param("ids") List<Long> ids);

    int logicDeleteById(@Param("id") Long id);

    DmTicketDO getDmTicketInfo(@Param("rdpTickedInsId") String rdpTickedInsId);

    void updateTicketInfo(@Param("ticketId") Long ticketId, @Param("ticketInfo") String ticketInfo);
}
