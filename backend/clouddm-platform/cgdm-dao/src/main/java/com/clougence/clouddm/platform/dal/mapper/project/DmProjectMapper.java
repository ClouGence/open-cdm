/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.mapper.project;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.project.ProjectStatus;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDO;
import com.clougence.clouddm.platform.dal.model.project.ArgDmProjectQueryObj;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectMapper extends BaseMapper<DmProjectDO> {

    IPage<DmProjectDO> listProjectByConditionAndPage(Page<?> page, ArgDmProjectQueryObj param, String ownerUid);

    List<DmProjectDO> listProjectByIds(String ownerUid, Set<Long> ids);

    DmProjectDO queryByOwnerAndId(String ownerUid, long projectId);

    void updateProjectManagerByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectNameByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectDescByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateProjectStatusByOwnerAndId(String ownerUid, long projectId, ProjectStatus newData);

    void updateProjectMarkByOwnerAndId(String ownerUid, long projectId, String newData);

    void updateFlowByOwnerAndId(String ownerUid, long projectId, DmProjectDO project);
}
