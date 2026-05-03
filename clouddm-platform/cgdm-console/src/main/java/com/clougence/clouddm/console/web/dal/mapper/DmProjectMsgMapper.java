package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectMsgDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectMsgMapper extends BaseMapper<DmProjectMsgDO> {

    List<DmProjectMsgDO> queryEnableByOwnerAndImId(String ownerUid, long imId);

    DmProjectMsgDO queryMessageByProjectId(String ownerUid, long projectId);

    void disableByOwnerAndImId(String ownerUid, long imId);

    void deleteByOwnerAndId(String ownerUid, long id);

    void disableAllImByProjectId(String ownerUid, long projectId);

    void enableAllImByProjectId(String ownerUid, long projectId);
}
