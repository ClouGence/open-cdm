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

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDevopsDO;

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
