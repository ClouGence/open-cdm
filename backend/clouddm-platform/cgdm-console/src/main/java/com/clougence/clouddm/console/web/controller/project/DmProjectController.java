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
package com.clougence.clouddm.console.web.controller.project;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_PROJECT_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_PROJECT_READ;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.fo.project.*;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.project.*;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.project.DmChangeService;
import com.clougence.clouddm.console.web.service.project.DmImService;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.console.web.service.project.domain.DmRepoDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.ImType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/project")
@Slf4j
public class DmProjectController {
    @Resource
    private ProjectDal        projectDal;
    @Resource
    private DataSourceDal     dsDal;
    @Resource
    private AuthDal           authDal;
    @Resource
    private ObjectCacheDao    objectCacheDao;
    @Resource
    private DmProjectService  dmProjectService;
    @Resource
    private DmImService       dmImService;
    @Resource
    private DmScmService      dmScmService;
    @Resource
    private DmChangeService   dmChangeService;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private BrowseService     browseService;
    @Resource
    private DmDsService       dmDsService;

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsUsers", method = RequestMethod.POST)
    public ResWebData<?> devopsUsers(HttpServletRequest request, @Valid @RequestBody GuideUsersFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmAuthUserDO mainUser = this.authDal.userMapper().queryByUid(puid);
        String search = StringUtils.isBlank(fo.getSearch()) ? null : fo.getSearch();
        List<RsAuthPersonObj> result = this.authDal.userMapper().searchUserByKeywords(mainUser.getUserDomain(), search);
        List<ProjectUserVO> vos = result.stream().map(DmConvertUtils::convertToProjectUserVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsScmList", method = RequestMethod.POST)
    public ResWebData<?> devopsScmList(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmScmDef> defList = dmScmService.getScmDefList();
        Map<ScmType, DmScmDef> defMap = defList.stream().collect(Collectors.toMap(DmScmDef::getScmType, d -> d));

        List<DmProjectScmDO> scmList = this.dmScmService.queryScmList(puid);
        List<ProjectScmVO> voList = scmList.stream().map(scmDO -> DmConvertUtils.convertToProjectScmVO(scmDO, defMap)).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(voList);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsRepos", method = RequestMethod.POST)
    public ResWebData<?> devopsRepos(HttpServletRequest request, @Valid @RequestBody GuideReposFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmRepoDef> repoList = this.dmScmService.fetchReposByScmId(puid, fo.getScmId());
        List<DevopsScmRepoVO> vos = repoList.stream().map(DmConvertUtils::convertToDevopsScmRepoVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsDsInsLevels", method = RequestMethod.POST)
    public ResWebData<?> devopsDsInsLevels(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds list
        List<BrowseLevelsVO> levels = this.browseService.listDsIncludeAllEnv(puid, uid);
        if (!CollectionUtils.isEmpty(levels)) {
            List<Long> dsIds = levels.stream().map(BrowseLevelsVO::getObjId).map(Long::parseLong).collect(Collectors.toList());
            List<DmDsConfigDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
            Map<Long, DmDsConfigDO> confMap = confList.stream().collect(Collectors.toMap(DmDsConfigDO::getDataSourceId, d -> d));
            levels = levels.stream().filter(vo -> {
                Long dsId = Long.parseLong(vo.getObjId());
                return confMap.containsKey(dsId) && confMap.get(dsId).isEnableDevops();
            }).collect(Collectors.toList());
        }

        return ResWebDataUtils.buildSuccess(levels);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsDsDbLevels", method = RequestMethod.POST)
    public ResWebData<?> devopsDsDbLevels(HttpServletRequest request, @Valid @RequestBody BrowseLevelsFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, fo.isRefreshCache());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsCheck", method = RequestMethod.POST)
    public ResWebData<?> devopsCheck(HttpServletRequest request, @Valid @RequestBody GuideCheckFlowFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        long hash = this.dmProjectService.toHash(fo);
        List<DmProjectDevopsDO> devops = this.dmProjectService.queryEnableDevopsByScmHash(puid, hash);

        GuideCheckFlowVO vo = new GuideCheckFlowVO();
        if (!devops.isEmpty()) {
            Set<Long> projectId = devops.stream().map(DmProjectDevopsDO::getRefProjectId).collect(Collectors.toSet());
            List<ProjectVO> projectList = this.dmProjectService.queryProjectListByIDs(puid, projectId);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CONFLICT_ERROR.name(), projectList.size()));
            vo.setReferer(projectList.stream()
                .map(DmConvertUtils::convertToDevopsRefProjectVO)
                .sorted(Comparator.comparing(GuideCheckFlowRefProjectVO::getProjectName))
                .collect(Collectors.toList()));
            vo.setSuccess(false);
        } else {
            vo.setSuccess(true);
        }

        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/devopsIms", method = RequestMethod.POST)
    public ResWebData<?> devopsIms(HttpServletRequest request, @Valid @RequestBody GuideImListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.getImType() == null) {
            return ResWebDataUtils.buildSuccess(Collections.emptyList());
        }

        List<DmImDef> defs = this.dmImService.getImDefList();
        Map<ImType, DmImDef> imDefMap = defs.stream().collect(Collectors.toMap(DmImDef::getImType, d -> d));

        List<DmSysMessengerDO> messengers = this.dmImService.queryMessengerByOwnerAndType(puid, fo.getImType());
        List<ProjectImVO> vos = messengers.stream().map(m -> {
            return DmConvertUtils.convertToProjectImVO(m, imDefMap);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/projectList", method = RequestMethod.POST)
    public ResWebData<?> projectList(HttpServletRequest request, @Valid @RequestBody ProjectListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        IPage<ProjectVO> result = this.dmProjectService.queryProjectListByPage(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectCreate", method = RequestMethod.POST)
    public ResWebData<?> projectCreate(HttpServletRequest request, @Valid @RequestBody GuideCreateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        GuideCreateProjectVO vo = this.dmProjectService.createProject(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/projectDetail", method = RequestMethod.POST)
    public ResWebData<?> projectDetail(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmProjectDO data = this.dmProjectService.queryProjectById(puid, fo.getProjectId());
        if (data == null) {
            return ResWebDataUtils.buildSuccess(data);
        } else {
            return ResWebDataUtils.buildSuccess(DmConvertUtils.convertToProjectVO(data, this.objectCacheDao));
        }
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectUpdate", method = RequestMethod.POST)
    public ResWebData<?> projectUpdate(HttpServletRequest request, @Valid @RequestBody ProjectUpdateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmProjectService.updateInfoByProjectId(puid, fo.getProjectId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/projectDevopsList", method = RequestMethod.POST)
    public ResWebData<?> projectDevopsList(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmProjectDO project = this.dmProjectService.queryProjectById(puid, fo.getProjectId());
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }

        List<DmProjectDevopsDO> data = this.dmProjectService.queryAllDevopsByProjectId(puid, fo.getProjectId());

        // fetch ds
        Map<Long, DmDsDO> dsMap = new HashMap<>();
        Set<Long> dsIds = data.stream().map(DmProjectDevopsDO::getDsId).collect(Collectors.toSet());
        if (!dsIds.isEmpty()) {
            List<DmDsDO> dsList = this.dsDal.dsMapper().listByIds(new ArrayList<>(dsIds));
            dsList.forEach(ds -> dsMap.put(ds.getId(), ds));
        }

        // fetch scm
        Map<Long, DmProjectScmDO> scmMap = new HashMap<>();
        Set<Long> scmIds = data.stream().map(DmProjectDevopsDO::getRefScmId).collect(Collectors.toSet());
        if (!scmIds.isEmpty()) {
            List<DmProjectScmDO> scmList = this.projectDal.scmMapper().queryListByOwnerAndIds(puid, new ArrayList<>(scmIds));
            scmList.forEach(ds -> scmMap.put(ds.getId(), ds));
        }

        // convert to vo
        List<ProjectDevopsVO> vos = data.stream().map(d -> {
            return DmConvertUtils.convertToProjectDevopsVO(d, scmMap, dsMap, this.dmScmService);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_PROJECT_READ)
    @RequestMapping(value = "/projectFetchImConfig", method = RequestMethod.POST)
    public ResWebData<?> projectFetchImConfig(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmProjectDO project = this.dmProjectService.queryProjectById(puid, fo.getProjectId());
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }

        DmProjectMsgDO data = this.dmProjectService.queryMessageByProjectId(puid, fo.getProjectId());
        DmSysMessengerDO messengerDO = null;
        if (data != null) {
            messengerDO = this.dmImService.queryImById(puid, data.getRefMsgId());
        }
        return ResWebDataUtils.buildSuccess(DmConvertUtils.convertToProjectImConfigVO(data, messengerDO));
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectPushImConfig", method = RequestMethod.POST)
    public ResWebData<?> projectPushImConfig(HttpServletRequest request, @Valid @RequestBody ProjectPushImConfigFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmProjectService.updateMessageByProjectId(puid, fo.getProjectId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectPushFlowConfig", method = RequestMethod.POST)
    public ResWebData<?> projectPushFlowConfig(HttpServletRequest request, @Valid @RequestBody ProjectPushFlowConfigFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmProjectService.updateFlowByProjectId(puid, fo.getProjectId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectDevopsCreate", method = RequestMethod.POST)
    public ResWebData<?> projectDevopsCreate(HttpServletRequest request, @Valid @RequestBody ProjectDevopsCreateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        long newConfigId = this.dmProjectService.createProjectDevops(puid, fo.getProjectId(), fo);
        return ResWebDataUtils.buildSuccess(newConfigId);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectDevopsDelete", method = RequestMethod.POST)
    public ResWebData<?> projectDevopsDelete(HttpServletRequest request, @Valid @RequestBody ProjectDevopsDeleteFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.dmProjectService.deleteProjectDevops(puid, fo.getProjectId(), fo.getDevopsId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectDevopsSwitch", method = RequestMethod.POST)
    public ResWebData<?> projectDevopsSwitch(HttpServletRequest request, @Valid @RequestBody ProjectDevopsSwitchFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.isEnable()) {
            this.dmProjectService.projectDevopsEnable(puid, fo.getProjectId(), fo.getDevopsId());
        } else {
            this.dmProjectService.projectDevopsDisable(puid, fo.getProjectId(), fo.getDevopsId());
        }

        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectTriggerConfig", method = RequestMethod.POST)
    public ResWebData<?> projectTriggerConfig(HttpServletRequest request, @Valid @RequestBody ProjectDevopsTriggerConfigFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.isUpdateHook()) {
            this.dmProjectService.projectDevopsConfigWebHook(puid, fo.getProjectId(), fo.getDevopsId(), fo.isHookEnable());
        }
        if (fo.isUpdateTrigger()) {
            this.dmProjectService.projectDevopsConfigTrigger(puid, fo.getProjectId(), fo.getDevopsId(), fo.isTriggerEnable());
        }
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectCallBackConfig", method = RequestMethod.POST)
    public ResWebData<?> projectCallBackConfig(HttpServletRequest request, @Valid @RequestBody ProjectDevopsCallBackFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.dmProjectService.projectDevopsConfigCallBack(puid, fo.getProjectId(), fo.getDevopsId(), fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectArchive", method = RequestMethod.POST)
    public ResWebData<?> projectArchive(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmProjectService.archiveProject(puid, fo.getProjectId(), uid);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectDelete", method = RequestMethod.POST)
    public ResWebData<?> projectDelete(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmProjectService.deleteProject(puid, fo.getProjectId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectRecover", method = RequestMethod.POST)
    public ResWebData<?> projectRecover(HttpServletRequest request, @Valid @RequestBody ProjectRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmProjectDO projectDO = this.dmProjectService.queryProjectById(puid, fo.getProjectId());
        if (projectDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (projectDO.getProjectStatus() == ProjectStatus.NORMAL) {
            return ResWebDataUtils.buildSuccess(true);
        }

        if (projectDO.getProjectStatus() == ProjectStatus.DELETE) {
            this.dmProjectService.recoverProjectTo(puid, fo.getProjectId(), ProjectStatus.ARCHIVE);
        } else if (projectDO.getProjectStatus() == ProjectStatus.ARCHIVE) {
            this.dmProjectService.recoverProjectTo(puid, fo.getProjectId(), ProjectStatus.NORMAL);
        }

        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectTriggerChange", method = RequestMethod.POST)
    public ResWebData<?> projectTriggerChange(HttpServletRequest request, @Valid @RequestBody ProjectDevopsChangeTriggerFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.verifyDevops(puid, fo.getProjectId(), fo.getDevopsId());
        DmProjectDevopsDO devopsDO = this.projectDal.devopsMapper().queryByOwnerAndId(puid, fo.getDevopsId());
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(devopsDO.getOwnerUid(), devopsDO.getRefScmId(), devopsDO.getScmRepoName(), devopsDO.getScmRepoBranch());
        if (branch == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_BRANCH_NOT_EXIST_ERROR.name()));
        }

        // create
        return this.dmChangeService.triggerChangeSuggest(puid, devopsDO.getRefProjectId(), devopsDO.getId(), branch.getBranchCommitId());
    }

    @RequestAuth(DM_PROJECT_MANAGE)
    @RequestMapping(value = "/projectTriggerSnapshot", method = RequestMethod.POST)
    public ResWebData<?> projectTriggerSnapshot(HttpServletRequest request, @Valid @RequestBody ProjectDevopsChangeTriggerFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.verifyDevops(puid, fo.getProjectId(), fo.getDevopsId());
        DmProjectDO projectDO = this.dmProjectService.queryProjectById(puid, fo.getProjectId());
        DmProjectDevopsDO devopsDO = this.projectDal.devopsMapper().queryByOwnerAndId(puid, fo.getDevopsId());
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(projectDO.getOwnerUid(), devopsDO.getRefScmId(), devopsDO.getScmRepoName(), devopsDO.getScmRepoBranch());
        if (branch == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_BRANCH_NOT_EXIST_ERROR.name()));
        }

        // check and create
        List<DmProjectChangeDO> list = this.projectDal.changeMapper().queryUnLockChange(projectDO.getId(), devopsDO.getId());
        if (!list.isEmpty()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_TRIGGER_SNAPSHOT_HAS_CHANGE_ERROR.name()));
        }

        DmProjectChangeDO changeDO = new DmProjectChangeDO();
        changeDO.setOwnerUid(projectDO.getOwnerUid());
        changeDO.setRefProjectId(projectDO.getId());
        changeDO.setRefDevopsId(devopsDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_INIT_SNAPSHOT_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ProjectChangeStep.INIT_SNAPSHOT);
        changeDO.setCurrentStatus(ProjectChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(true);
        changeDO.setFlowWalked(new RsProjectChangeFlowWalkedObj());
        this.projectDal.changeMapper().insert(changeDO);
        return ResWebDataUtils.buildSuccess(true);
    }
}
