package com.clougence.rdp.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.rdp.dal.model.RdpBlobResourceDO;

/**
 * @author wanshao create time is 2020/3/2
 **/
public interface RdpBlobResourceMapper extends BaseMapper<RdpBlobResourceDO> {

    RdpBlobResourceDO queryByIdentify(String instanceId, ResourceType resourceType, SecurityFileType fileType);

    void updateByIdentify(byte[] content, String instanceId, ResourceType resourceType, SecurityFileType fileType);

    int deleteByIdentify(String instanceId, ResourceType resourceType, SecurityFileType fileType);
}
