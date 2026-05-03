package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectScmDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectScmMapper extends BaseMapper<DmProjectScmDO> {

    List<DmProjectScmDO> queryListByOwner(String ownerUid);

    List<DmProjectScmDO> queryListByOwnerAndIds(String ownerUid, Collection<Long> ids);

    DmProjectScmDO queryById(long scmId);

    void deleteByOwnerAndId(String ownerUid, long scmId);

    void updateDisplayByOwnerAndId(String ownerUid, long scmId, String newData);

    void updateServiceUrlByOwnerAndId(String ownerUid, long scmId, String newData);

    void updateAccessTokenByOwnerAndId(String ownerUid, long scmId, String newData);
}
