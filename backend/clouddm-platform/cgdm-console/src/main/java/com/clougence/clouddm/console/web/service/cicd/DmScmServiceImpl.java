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
package com.clougence.clouddm.console.web.service.cicd;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.cicd.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.cicd.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.service.cicd.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.cicd.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.cicd.domain.DmScmDef;
import com.clougence.clouddm.console.web.service.cicd.domain.ScmConnectionTestResult;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmScmServiceImpl implements DmScmService, UnifiedPostConstruct {
    @Resource
    private ChangeFlowDal        changeFlowDal;

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
    public List<DmGitOpsScmDO> queryScmByIds(String ownerUid, Collection<Long> scmIds) {
        return this.changeFlowDal.scmMapper().queryListByOwnerAndIds(ownerUid, scmIds);
    }

    @Override
    public List<DmGitOpsScmDO> queryScmList(String ownerUid) {
        return changeFlowDal.scmMapper().queryListByOwner(ownerUid);
    }

    @Override
    public DmGitOpsScmDO queryScmById(String ownerUid, long scmId) {
        return changeFlowDal.scmMapper().queryByOwnerAndId(ownerUid, scmId);
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

        fo.setServiceUrl(normalizeAndValidateUrl(fo.getScmType(), fo.getServiceUrl(), fo.isPlainHttpAcknowledged()));
        testScmByConfig(ownerUid, fo);

        DmGitOpsScmDO scmDO = new DmGitOpsScmDO();
        scmDO.setOwnerUid(ownerUid);
        scmDO.setScmType(fo.getScmType());
        scmDO.setScmDisplay(fo.getDisplay());
        scmDO.setScmServiceUrl(fo.getServiceUrl());
        scmDO.setScmAccessToken(fo.getAccessToken());
        this.changeFlowDal.scmMapper().insert(scmDO);
    }

    @Override
    public void deleteScmById(String ownerUid, long scmId) {
        this.changeFlowDal.scmMapper().deleteByOwnerAndId(ownerUid, scmId);
        this.changeFlowDal.flowMapper().disableByOwnerAndScmId(ownerUid, scmId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<Long> updateScmById(String ownerUid, DevopsScmUpdateFO fo) {
        DmGitOpsScmDO current = queryScmById(ownerUid, fo.getScmId());
        if (current == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }
        String serviceUrl = current.getScmServiceUrl();
        if (StringUtils.isNotBlank(fo.getNewServiceUrl())) {
            boolean currentHttpUrl = StringUtils.equals(fo.getNewServiceUrl().trim(), current.getScmServiceUrl());
            serviceUrl = normalizeAndValidateUrl(current.getScmType(), fo.getNewServiceUrl(), currentHttpUrl || fo.isPlainHttpAcknowledged());
        }
        String accessToken = StringUtils.isBlank(fo.getNewAccessToken()) ? current.getScmAccessToken() : fo.getNewAccessToken();
        boolean urlChanged = !StringUtils.equals(serviceUrl, current.getScmServiceUrl());
        boolean tokenChanged = StringUtils.isNotBlank(fo.getNewAccessToken());
        List<DmChangeFlowDO> activeFlows = changeFlowDal.flowMapper().queryEnabledByOwnerAndScmId(ownerUid, fo.getScmId());
        List<Long> affectedFlowIds = activeFlows.stream().map(DmChangeFlowDO::getId).collect(Collectors.toList());

        ScmProviderSpi provider = provider(current.getScmType());
        if (urlChanged && !activeFlows.isEmpty() && !fo.isForce()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_URL_CHANGE_INUSE.name(), affectedFlowIds));
        }
        if (urlChanged || tokenChanged) {
            provider.fetchRepoList(serviceUrl, accessToken, null);
        }
        if (tokenChanged && !urlChanged) {
            for (DmChangeFlowDO flow : activeFlows) {
                ScmRepo repo = toScmRepo(flow);
                List<ScmBranch> branches = provider.fetchBranchList(serviceUrl, accessToken, repo, flow.getScmRepoBranch(), true);
                if (findExactBranch(branches, flow.getScmRepoBranch()) == null) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_BRANCH_NOT_EXIST_ERROR.name()));
                }
            }
        }

        if (StringUtils.isNotBlank(fo.getNewDisplay())) {
            this.changeFlowDal.scmMapper().updateDisplayByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewDisplay());
        }
        if (urlChanged) {
            this.changeFlowDal.scmMapper().updateUrlByOwnerAndId(ownerUid, fo.getScmId(), serviceUrl);
            if (!activeFlows.isEmpty()) {
                this.changeFlowDal.flowMapper().disableByOwnerAndScmId(ownerUid, fo.getScmId());
            }
        }
        if (StringUtils.isNotBlank(fo.getNewAccessToken())) {
            this.changeFlowDal.scmMapper().updateTokenByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewAccessToken());
        }
        return urlChanged ? affectedFlowIds : Collections.emptyList();
    }

    @Override
    public List<DmRepoDef> fetchReposByScmId(String ownerUid, long scmId) {
        DmGitOpsScmDO scmDO = changeFlowDal.scmMapper().queryByOwnerAndId(ownerUid, scmId);
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
            def.setRepoId(repo.getRepoId());
            def.setRepoPath(repo.getRepoPath());
            def.setRepoSpace(repo.getRepoSpace());
            def.setRepoName(repo.getRepoName());
            def.setRepoUrl(repo.getRepoUrl());
            def.setRepoHome(repo.getRepoHome());
            def.setBranch(repo.getBranchName());
            def.setArchived(repo.isArchived());
            def.setEmpty(repo.isEmpty());
            return def;
        }).collect(Collectors.toList());
    }

    @Override
    public DmBranchDef fetchBranchByScmAndRepo(String ownerUid, long scmId, String repoId, String repoSpace, String repoName, String branch) {
        DmGitOpsScmDO scmDO = changeFlowDal.scmMapper().queryByOwnerAndId(ownerUid, scmId);
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmDO.getScmType().getProviderType().name());
        if (service == null) {
            String scmTypeI18n = DmI18nUtils.getMessage(scmDO.getScmType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }

        ScmRepo repo = new ScmRepo();
        String repoPath = ScmUtils.buildRepoPath(repoSpace, repoName);
        repo.setRepoId(StringUtils.isBlank(repoId) ? repoPath : repoId);
        repo.setRepoPath(repoPath);
        repo.setRepoSpace(repoSpace);
        repo.setRepoName(repoName);
        List<ScmBranch> repos = service.fetchBranchList(scmDO.getScmServiceUrl(), scmDO.getScmAccessToken(), repo, branch, true);
        ScmBranch exactBranch = findExactBranch(repos, branch);
        if (exactBranch == null) {
            return null;
        } else {
            DmBranchDef def = new DmBranchDef();
            def.setScmId(scmDO.getId());
            def.setRepoId(repo.getRepoId());
            def.setRepoName(repoName);
            def.setBranch(exactBranch.getBranchName());
            def.setBranchCommitId(exactBranch.getCommitId());
            return def;
        }
    }

    private static ScmBranch findExactBranch(List<ScmBranch> branches, String branchName) {
        if (branches == null) {
            return null;
        }
        return branches.stream().filter(branch -> branch != null && StringUtils.equals(branchName, branch.getBranchName())).findFirst().orElse(null);
    }

    @Override
    public ScmConnectionTestResult testScmByConfig(String ownerUid, DevopsScmAddFO fo) {
        if (StringUtils.isBlank(fo.getAccessToken())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NEED_ACCESS_TOKEN.name()));
        }
        if (fo.getScmType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NEED_PROVIDER_TYPE.name()));
        }

        fo.setServiceUrl(normalizeAndValidateUrl(fo.getScmType(), fo.getServiceUrl(), fo.isPlainHttpAcknowledged()));
        ScmProviderSpi provider = provider(fo.getScmType());
        List<ScmRepo> projects = provider.fetchRepoList(fo.getServiceUrl(), fo.getAccessToken(), null);
        ScmConnectionTestResult result = new ScmConnectionTestResult();
        result.setProjectCount(projects.size());
        result.setServerVersion(provider.fetchServerVersion(fo.getServiceUrl(), fo.getAccessToken()));
        if (projects.isEmpty()) {
            result.setWarning(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NO_PROJECT_WARNING.name()));
        }
        return result;
    }

    private ScmProviderSpi provider(ScmType scmType) {
        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, scmType.getProviderType().name());
        if (service == null) {
            String scmTypeI18n = DmI18nUtils.getMessage(scmType.getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), scmTypeI18n));
        }
        return service;
    }

    private String normalizeAndValidateUrl(ScmType type, String serviceUrl, boolean httpAcknowledged) {
        String result = serviceUrl;
        if (type == ScmType.Gitlab) {
            try {
                result = ScmUtils.normalizeGitlabWebUrl(serviceUrl);
            } catch (IllegalArgumentException e) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_URL_INVALID.name()));
            }
            if (StringUtils.startsWithIgnoreCase(result, "http://") && !httpAcknowledged) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_HTTP_ACK_REQUIRED.name()));
            }
        }
        return result;
    }

    private static ScmRepo toScmRepo(DmChangeFlowDO flow) {
        ScmRepo repo = new ScmRepo();
        repo.setRepoId(flow.getScmRepoIdentifier());
        repo.setRepoSpace(flow.getScmRepoSpace());
        repo.setRepoName(flow.getScmRepoName());
        repo.setRepoPath(ScmUtils.buildRepoPath(flow.getScmRepoSpace(), flow.getScmRepoName()));
        return repo;
    }
}
