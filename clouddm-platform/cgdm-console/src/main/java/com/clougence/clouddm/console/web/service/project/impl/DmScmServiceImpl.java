package com.clougence.clouddm.console.web.service.project.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ScmType;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectScmMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectScmDO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.project.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.ScmBranch;
import com.clougence.clouddm.sdk.scm.ScmProviderSpi;
import com.clougence.clouddm.sdk.scm.ScmRepo;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmScmServiceImpl implements DmScmService, UnifiedPostConstruct {

    @Resource
    private DmProjectScmMapper    dmProjectScmMapper;
    @Resource
    private DmProjectDevopsMapper dmProjectDevopsMapper;
    private final List<DmScmDef>  scmDefList = new ArrayList<>();

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
        return this.dmProjectScmMapper.queryListByOwnerAndIds(ownerUid, scmIds);
    }

    @Override
    public List<DmProjectScmDO> queryScmList(String ownerUid) {
        return dmProjectScmMapper.queryListByOwner(ownerUid);
    }

    @Override
    public DmProjectScmDO queryScmById(String ownerUid, long scmId) {
        return dmProjectScmMapper.queryById(scmId);
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
        this.dmProjectScmMapper.insert(scmDO);
    }

    @Override
    public void deleteScmById(String ownerUid, long scmId) {
        this.dmProjectScmMapper.deleteByOwnerAndId(ownerUid, scmId);
        this.dmProjectDevopsMapper.disableByOwnerAndScmId(ownerUid, scmId);
    }

    @Override
    public void updateScmById(String ownerUid, DevopsScmUpdateFO fo) {
        if (StringUtils.isNotBlank(fo.getNewDisplay())) {
            this.dmProjectScmMapper.updateDisplayByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewDisplay());
        }
        if (StringUtils.isNotBlank(fo.getNewServiceUrl())) {
            this.dmProjectScmMapper.updateServiceUrlByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewServiceUrl());
        }
        if (StringUtils.isNotBlank(fo.getNewAccessToken())) {
            this.dmProjectScmMapper.updateAccessTokenByOwnerAndId(ownerUid, fo.getScmId(), fo.getNewAccessToken());
        }
    }

    @Override
    public List<DmRepoDef> fetchReposByScmId(String ownerUid, long scmId) {
        DmProjectScmDO scmDO = dmProjectScmMapper.queryById(scmId);
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
        DmProjectScmDO scmDO = dmProjectScmMapper.queryById(scmId);
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
