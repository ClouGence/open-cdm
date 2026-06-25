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
package com.clougence.clouddm.platform.dal.mapper.gitops;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;

public interface DmGitOpsScmMapper extends BaseMapper<DmGitOpsScmDO> {
    List<DmGitOpsScmDO> queryListByOwner(String ownerUid);

    List<DmGitOpsScmDO> queryListByOwnerAndIds(String ownerUid, Collection<Long> scmIds);

    DmGitOpsScmDO queryByOwnerAndId(String ownerUid, long scmId);

    void deleteByOwnerAndId(String ownerUid, long scmId);

    void updateDisplayByOwnerAndId(String ownerUid, long scmId, String newData);

    void updateTokenByOwnerAndId(String ownerUid, long scmId, String newData);

    void updateUrlByOwnerAndId(String ownerUid, long scmId, String newData);
}
