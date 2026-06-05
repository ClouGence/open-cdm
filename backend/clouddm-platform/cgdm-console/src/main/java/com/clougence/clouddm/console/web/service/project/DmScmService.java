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

import java.util.Collection;
import java.util.List;

import com.clougence.clouddm.platform.dal.model.project.ScmType;
import com.clougence.clouddm.platform.dal.model.project.DmProjectScmDO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.project.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;

public interface DmScmService {

    List<DmScmDef> getScmDefList();

    DmScmDef getScmDefByType(ScmType scmType);

    List<DmProjectScmDO> queryScmByIds(String ownerUid, Collection<Long> scmIds);

    List<DmProjectScmDO> queryScmList(String ownerUid);

    DmProjectScmDO queryScmById(String ownerUid, long scmId);

    void addScm(String ownerUid, DevopsScmAddFO fo);

    void deleteScmById(String ownerUid, long scmId);

    void updateScmById(String ownerUid, DevopsScmUpdateFO fo);

    List<DmRepoDef> fetchReposByScmId(String ownerUid, long scmId);

    DmBranchDef fetchBranchByScmAndRepo(String ownerUid, long scmId, String repoName, String branch);

    void testScmByConfig(String ownerUid, DevopsScmAddFO fo);

}
