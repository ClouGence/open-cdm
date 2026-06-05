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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.project.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.model.project.DmProjectScmDO;
import com.clougence.clouddm.platform.dal.model.project.ScmType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.ScmBranch;
import com.clougence.clouddm.sdk.scm.ScmProviderSpi;
import com.clougence.clouddm.sdk.scm.ScmRepo;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmScmServiceImpl implements DmScmService, UnifiedPostConstruct {
    @Resource
    private ProjectDal           projectDal;

    private final List<DmScmDef> scmDefList = new ArrayList<>();

    @Override
    public void init() throws Exception {
        for (ScmType scmType : Arrays.stream(ScmType.values()).sorted().toArray(ScmType[]::new)) {
            ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmType.getProviderType().name());
            if (service == null) {
                continue;
            }

            DmScmDef item = new DmScmDef();
            item.setScmType(scmType);
            item.setServiceUrl(service.getServiceUrl());
            item.setCustom(scmType.isSupportCustom());
            item.setHelpUrl(service.getHelpUrl());
            item.setEvents(service.devopsSupportEvents());
            this.scmDefList.add(item);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public List<DmScmDef> getScmDefList() { return this.scmDefList; }

    @Override
    public DmScmDef getScmDefByType(ScmType scmType) {
        return this.scmDefList.stream().filter(d -> d.getScmType().equals(scmType)).findAny().orElse(null);
    }

    @Override
    public List<DmProjectScmDO> queryScmByIds(String ownerUid, Collection<Long> scmIds) {
        return this.projectDal.scmMapper().queryListByOwnerAndIds(ownerUid, scmIds);
    }

    @Override
    public List<DmProjectScmDO> queryScmList(String ownerUid) {
        return projectDal.scmMapper().queryListByOwner(ownerUid);
    }

    @Override
    public DmProjectScmDO queryScmById(String ownerUid, long scmId) {
        return projectDal.scmMapper().queryById(scmId);
    }

    @Override
    public void addScm(String ownerUid, DevopsScmAddFO fo) {
        if (StringUtils.isBlank(fo.getAccessToken())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NEED_ACCESS_TOKEN.name()));
        }
        if (fo.getScmType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NEED_PROVIDER_TYPE.name()));
        }
        List<ScmType> defMap = this.scmDefList.stream().map(DmScmDef::getScmType).collect(Collectors.toList());
        if (!defMap.contains(fo.getScmType())) {
            String scmTypeI18n = DmI18nUtils.getMessage(fo.getScmType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }
        if (StringUtils.isBlank(fo.getDisplay())) {
            String scmTypeI18n = DmI18nUtils.getMessage(fo.getScmType().getI18nKey());
            String nowStr = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
            fo.setDisplay(scmTypeI18n + "-" + nowStr);
        }

        DmProjectScmDO scmDO = new DmProjectScmDO();
        scmDO.setOwnerUid(ownerUid);
        scmDO.setScmType(fo.getScmType());
        scmDO.setScmDisplay(fo.getDisplay());
        scmDO.setScmServiceUrl(fo.getServiceUrl());
        scmDO.setScmAccessToken(fo.getAccessToken());
        this.projectDal.scmMapper().insert(scmDO);
    }

    @Override
    public void deleteScmById(String ownerUid, long scmId) {
        this.projectDal.scmMapper().deleteByOwnerAndId(ownerUid, scmId);
        this.projectDal.devopsMapper().disableByOwnerAndScmId(ownerUid, scmId);
    }

    @Override
    public void updateScmById(String ownerUid, DevopsScmUpdateFO fo) {
        if (StringUtils.isNotBlank(fo.getNewDisplay())) {
            this.projectDal.scmMapper().updateDisplayByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewDisplay());
        }
        if (StringUtils.isNotBlank(fo.getNewServiceUrl())) {
            this.projectDal.scmMapper().updateServiceUrlByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewServiceUrl());
        }
        if (StringUtils.isNotBlank(fo.getNewAccessToken())) {
            this.projectDal.scmMapper().updateAccessTokenByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewAccessToken());
        }
    }

    @Override
    public List<DmRepoDef> fetchReposByScmId(String ownerUid, long scmId) {
        DmProjectScmDO scmDO = projectDal.scmMapper().queryById(scmId);
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmDO.getScmType().getProviderType().name());
        if (service == null) {
            String scmTypeI18n = DmI18nUtils.getMessage(scmDO.getScmType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }

        List<ScmRepo> repos = service.fetchRepoList(scmDO.getScmServiceUrl(), scmDO.getScmAccessToken(), null);
        return repos.stream().map(repo -> {
            DmRepoDef def = new DmRepoDef();
            def.setScmId(scmDO.getId());
            def.setRepoSpace(repo.getRepoSpace());
            def.setRepoName(repo.getRepoName());
            def.setRepoUrl(repo.getRepoUrl());
            def.setRepoHome(repo.getRepoHome());
            def.setBranch(repo.getBranchName());
            return def;
        }).collect(Collectors.toList());
    }

    @Override
    public DmBranchDef fetchBranchByScmAndRepo(String ownerUid, long scmId, String repoName, String branch) {
        DmProjectScmDO scmDO = projectDal.scmMapper().queryById(scmId);
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmDO.getScmType().getProviderType().name());
        if (service == null) {
            String scmTypeI18n = DmI18nUtils.getMessage(scmDO.getScmType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }

        List<ScmBranch> repos = service.fetchBranchList(scmDO.getScmServiceUrl(), scmDO.getScmAccessToken(), repoName, branch, true);
        if (repos.isEmpty()) {
            return null;
        } else {
            ScmBranch b = repos.get(0);
            DmBranchDef def = new DmBranchDef();
            def.setScmId(scmDO.getId());
            def.setRepoName(repoName);
            def.setBranch(b.getBranchName());
            def.setBranchCommitId(b.getCommitId());
            return def;
        }
    }

    @Override
    public void testScmByConfig(String ownerUid, DevopsScmAddFO fo) {
        if (StringUtils.isBlank(fo.getAccessToken())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NEED_ACCESS_TOKEN.name()));
        }
        if (fo.getScmType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NEED_PROVIDER_TYPE.name()));
        }

        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, fo.getScmType().getProviderType().name());
        if (service == null) {
            String scmTypeI18n = DmI18nUtils.getMessage(fo.getScmType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }

        service.fetchRepoList(fo.getServiceUrl(), fo.getAccessToken(), null);
    }
}
