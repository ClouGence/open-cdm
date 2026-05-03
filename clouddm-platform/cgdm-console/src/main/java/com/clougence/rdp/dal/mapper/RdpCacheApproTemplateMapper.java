package com.clougence.rdp.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.model.RdpCacheApproTemplateDO;

/**
 * @author wanshao create time is 2021/1/26
 **/
public interface RdpCacheApproTemplateMapper extends BaseMapper<RdpCacheApproTemplateDO> {

    List<RdpCacheApproTemplateDO> listByPrimaryUid(String primaryUid);

    List<RdpCacheApproTemplateDO> listByPrimaryUidAndType(@Param("primaryUid") String primaryUid, @Param("type") RdpApprovalType type);

    RdpCacheApproTemplateDO queryByUidAndTemId(@Param("primaryUid") String primaryUid, @Param("templateId") String templateId);

    void deleteByPrimaryUid(String primaryUid, RdpApprovalType approvalType);

    int insertTemplateBatch(@Param("cacheList") List<RdpCacheApproTemplateDO> cacheList);

    void updateTemplateBatch(@Param("cacheList") List<RdpCacheApproTemplateDO> cacheList);

    void updateDmTicketInsTemplateBatch(@Param("cacheList") List<RdpCacheApproTemplateDO> cacheList);
}
