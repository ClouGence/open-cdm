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
package com.clougence.clouddm.console.web.service.project;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.platform.dal.model.project.ProjectStatus;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDevopsDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectMsgDO;
import com.clougence.clouddm.console.web.model.fo.project.*;
import com.clougence.clouddm.console.web.model.vo.project.GuideCreateProjectVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectVO;

public interface DmProjectService {

    IPage<ProjectVO> queryProjectListByPage(String ownerUid, ProjectListFO fo);

    List<ProjectVO> queryProjectListByIDs(String ownerUid, Set<Long> ids);

    List<DmProjectDevopsDO> queryEnableDevopsByDsId(String ownerUid, long dsId);

    List<DmProjectDevopsDO> queryEnableDevopsByScmId(String ownerUid, long scmId);

    List<DmProjectMsgDO> queryEnableDevopsByImId(String ownerUid, long imId);

    List<DmProjectDevopsDO> queryEnableDevopsByScmHash(String ownerUid, long hash);

    List<DmProjectDevopsDO> queryAllDevopsByProjectId(String ownerUid, long projectId);

    DmProjectMsgDO queryMessageByProjectId(String ownerUid, long projectId);

    long toHash(GuideCheckFlowFO fo);

    GuideCreateProjectVO createProject(String ownerUid, String currentUser, GuideCreateFO fo);

    DmProjectDO queryProjectById(String ownerUid, long projectId);

    void updateMessageByProjectId(String ownerUid, long projectId, ProjectPushImConfigFO fo);

    void updateFlowByProjectId(String ownerUid, long projectId, ProjectPushFlowConfigFO fo);

    long createProjectDevops(String ownerUid, long projectId, ProjectDevopsCreateFO fo);

    void updateInfoByProjectId(String ownerUid, long projectId, ProjectUpdateFO fo);

    void deleteProjectDevops(String ownerUid, long projectId, long devopsId);

    void projectDevopsEnable(String ownerUid, long projectId, long devopsId);

    void projectDevopsDisable(String ownerUid, long projectId, long devopsId);

    void projectDevopsConfigWebHook(String ownerUid, long projectId, long devopsId, boolean enable);

    void projectDevopsConfigTrigger(String ownerUid, long projectId, long devopsId, boolean enable);

    void projectDevopsConfigCallBack(String ownerUid, long projectId, long devopsId, ProjectDevopsCallBackFO fo);

    void archiveProject(String ownerUid, long projectId, String operatorUid);

    void recoverProjectTo(String ownerUid, long projectId, ProjectStatus toStatus);

    void deleteProject(String ownerUid, long projectId);

    File getProjectSpace(String ownerUid, long projectId);

    File getTempSpace(String ownerUid, long projectId);
}
