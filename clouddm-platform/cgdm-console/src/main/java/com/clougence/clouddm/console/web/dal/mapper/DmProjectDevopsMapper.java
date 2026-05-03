package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectDevopsMapper extends BaseMapper<DmProjectDevopsDO> {

    List<DmProjectDevopsDO> queryByIds(String ownerUid, Collection<Long> devopsIds);

    List<DmProjectDevopsDO> queryEnableByOwnerAndDsId(String ownerUid, long dsId);

    List<DmProjectDevopsDO> queryEnableByOwnerAndScmId(String ownerUid, long scmId);

    List<DmProjectDevopsDO> queryEnableByOwnerAndScmHash(String ownerUid, long scmHash);

    List<DmProjectDevopsDO> queryAllDevopsByProjectId(String ownerUid, long projectId);

    DmProjectDevopsDO queryByOwnerAndId(String ownerUid, long devopsId);

    void disableByOwnerAndScmId(String ownerUid, long scmId);

    int deleteByOwnerAndProjectAndId(String ownerUid, long projectId, long devopsId);

    void enableDevopsByProjectAndId(String ownerUid, long projectId, long devopsId);

    void disableDevopsByProjectAndId(String ownerUid, long projectId, long devopsId);

    void enableWebHookByProjectAndId(String ownerUid, long projectId, long devopsId);

    void disableWebHookByProjectAndId(String ownerUid, long projectId, long devopsId);

    void enableTriggerByProjectAndId(String ownerUid, long projectId, long devopsId);

    void disableTriggerByProjectAndId(String ownerUid, long projectId, long devopsId);

    void configCallBackByProjectAndId(String ownerUid, long projectId, long devopsId, boolean enable, String httpMethod, String httpUrl);

    void configTriggerByProjectAndId(String ownerUid, long projectId, long devopsId, boolean enable, String token);

    void disableAllDevopsByProjectId(String ownerUid, long projectId);
}
