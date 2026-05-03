package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.console.web.dal.enumeration.ProjectStatus;
import com.clougence.clouddm.console.web.dal.model.DmProjectDO;
import com.clougence.clouddm.console.web.dal.model.queryobj.DmProjectQueryObj;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectMapper extends BaseMapper<DmProjectDO> {

    IPage<DmProjectDO> listProjectByConditionAndPage(Page<?> page, DmProjectQueryObj param, String ownerUid);

    List<DmProjectDO> listProjectByIds(String ownerUid, Set<Long> ids);

    DmProjectDO queryByOwnerAndId(String ownerUid, long projectId);

    void updateProjectManagerByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectNameByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectDescByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectStatusByOwnerAndId(String ownerUid, long projectId, ProjectStatus newData);

    void updateProjectMarkByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateFlowByOwnerAndId(String ownerUid, long projectId, DmProjectDO project);
}
