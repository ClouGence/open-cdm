package com.clougence.clouddm.console.web.service.project;

import java.util.Collection;
import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;
import com.clougence.clouddm.console.web.dal.model.DmProjectScmDO;
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
