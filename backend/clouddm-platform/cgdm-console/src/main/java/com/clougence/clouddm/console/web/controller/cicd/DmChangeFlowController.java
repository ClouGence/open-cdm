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
package com.clougence.clouddm.console.web.controller.cicd;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_FLOW_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_FLOW_READ;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.fo.cicd.*;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.cicd.*;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.cicd.DmChangeFlowService;
import com.clougence.clouddm.console.web.service.cicd.DmChangeService;
import com.clougence.clouddm.console.web.service.cicd.DmImService;
import com.clougence.clouddm.console.web.service.cicd.DmScmService;
import com.clougence.clouddm.console.web.service.cicd.domain.*;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.gitops.ScmType;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.ImType;
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
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/cicd/flow")
@Slf4j
public class DmChangeFlowController {
    @Resource
    private ChangeFlowDal       changeFlowDal;
    @Resource
    private DataSourceDal       dsDal;
    @Resource
    private AuthDal             authDal;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmChangeFlowService changeFlowService;
    @Resource
    private DmImService         dmImService;
    @Resource
    private DmScmService        dmScmService;
    @Resource
    private DmChangeService     dmChangeService;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private BrowseService       browseService;

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResWebData<?> devopsUsers(HttpServletRequest request, @Valid @RequestBody GuideUsersFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmAuthUserDO mainUser = this.authDal.userMapper().queryByUid(puid);
        String search = StringUtils.isBlank(fo.getSearch()) ? null : fo.getSearch();
        List<RsAuthPersonObj> result = this.authDal.userMapper().searchUserByKeywords(mainUser.getId(), search);
        List<ChangeFlowUserVO> vos = result.stream().map(DmConvertUtils::convertToChangeFlowUserVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/gitOpsScmList", method = RequestMethod.POST)
    public ResWebData<?> devopsScmList(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmScmDef> defList = dmScmService.getScmDefList();
        Map<ScmType, DmScmDef> defMap = defList.stream().collect(Collectors.toMap(DmScmDef::getScmType, d -> d));

        List<DmGitOpsScmDO> scmList = this.dmScmService.queryScmList(puid);
        List<GitOpsScmVO> voList = scmList.stream().map(scmDO -> DmConvertUtils.convertToGitOpsScmVO(scmDO, defMap)).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(voList);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/gitOpsRepos", method = RequestMethod.POST)
    public ResWebData<?> devopsRepos(HttpServletRequest request, @Valid @RequestBody GuideReposFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmRepoDef> repoList = this.dmScmService.fetchReposByScmId(puid, fo.getScmId());
        List<DevopsScmRepoVO> vos = repoList.stream().map(DmConvertUtils::convertToDevopsScmRepoVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/dsInsLevels", method = RequestMethod.POST)
    public ResWebData<?> devopsDsInsLevels(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds list
        List<BrowseLevelsVO> levels = this.browseService.listDsIncludeAllEnv(puid, uid);
        return ResWebDataUtils.buildSuccess(levels);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/dsDbLevels", method = RequestMethod.POST)
    public ResWebData<?> devopsDsDbLevels(HttpServletRequest request, @Valid @RequestBody BrowseLevelsFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, fo.isRefreshCache());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResWebData<?> devopsCheck(HttpServletRequest request, @Valid @RequestBody GuideCheckFlowFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        long hash = this.changeFlowService.toHash(fo);
        List<DmChangeFlowDO> devops = this.changeFlowService.queryEnableDevopsByScmHash(puid, hash);

        GuideCheckFlowVO vo = new GuideCheckFlowVO();
        if (!devops.isEmpty()) {
            Set<Long> flowIds = devops.stream().map(DmChangeFlowDO::getRefFlowId).collect(Collectors.toSet());
            List<ChangeFlowVO> flowList = this.changeFlowService.queryChangeFlowListByIds(puid, flowIds);
            vo.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CONFLICT_ERROR.name(), flowList.size()));
            vo.setReferer(flowList.stream()
                .map(DmConvertUtils::convertToDevopsRefFlowVO)
                .sorted(Comparator.comparing(GuideCheckFlowRefFlowVO::getFlowName))
                .collect(Collectors.toList()));
            vo.setSuccess(false);
        } else {
            vo.setSuccess(true);
        }

        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/ims", method = RequestMethod.POST)
    public ResWebData<?> devopsIms(HttpServletRequest request, @Valid @RequestBody GuideImListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.getImType() == null) {
            return ResWebDataUtils.buildSuccess(Collections.emptyList());
        }

        List<DmImDef> defs = this.dmImService.getImDefList();
        Map<ImType, DmImDef> imDefMap = defs.stream().collect(Collectors.toMap(DmImDef::getImType, d -> d));

        List<DmSysMessengerDO> messengers = this.dmImService.queryMessengerByOwnerAndType(puid, fo.getImType());
        List<ChangeFlowImVO> vos = messengers.stream().map(m -> {
            return DmConvertUtils.convertToChangeFlowImVO(m, imDefMap);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> flowList(HttpServletRequest request, @Valid @RequestBody ChangeFlowListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmPageVO<ChangeFlowVO> result = this.changeFlowService.queryChangeFlowListByPage(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<?> flowCreate(HttpServletRequest request, @Valid @RequestBody GuideCreateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        GuideCreateChangeFlowVO vo = this.changeFlowService.createChangeFlow(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResWebData<?> flowDetail(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmChangeFlowDO data = this.changeFlowService.queryFlowById(puid, fo.getFlowId());
        if (data == null) {
            return ResWebDataUtils.buildSuccess(data);
        } else {
            return ResWebDataUtils.buildSuccess(DmConvertUtils.convertToChangeFlowVO(data, this.objectCacheDao));
        }
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> flowUpdate(HttpServletRequest request, @Valid @RequestBody ChangeFlowUpdateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.changeFlowService.updateInfoByFlowId(puid, fo.getFlowId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/gitOpsList", method = RequestMethod.POST)
    public ResWebData<?> flowGitOpsList(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmChangeFlowDO flow = this.changeFlowService.queryFlowById(puid, fo.getFlowId());
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }

        List<DmChangeFlowDO> data = this.changeFlowService.queryAllGitOpsByFlowId(puid, fo.getFlowId());

        // fetch ds
        Map<Long, DmDsDO> dsMap = new HashMap<>();
        Set<Long> dsIds = data.stream().map(DmChangeFlowDO::getDsId).collect(Collectors.toSet());
        if (!dsIds.isEmpty()) {
            List<DmDsDO> dsList = this.dsDal.dsMapper().listByIds(new ArrayList<>(dsIds));
            dsList.forEach(ds -> dsMap.put(ds.getId(), ds));
        }

        // fetch scm
        Map<Long, DmGitOpsScmDO> scmMap = new HashMap<>();
        Set<Long> scmIds = data.stream().map(DmChangeFlowDO::getRefScmId).collect(Collectors.toSet());
        if (!scmIds.isEmpty()) {
            List<DmGitOpsScmDO> scmList = this.changeFlowDal.scmMapper().queryListByOwnerAndIds(puid, new ArrayList<>(scmIds));
            scmList.forEach(ds -> scmMap.put(ds.getId(), ds));
        }

        // convert to vo
        List<ChangeFlowGitOpsVO> vos = data.stream().map(d -> {
            return DmConvertUtils.convertToChangeFlowGitOpsVO(d, scmMap, dsMap, this.dmScmService);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_CICD_FLOW_READ)
    @RequestMapping(value = "/fetchImConfig", method = RequestMethod.POST)
    public ResWebData<?> flowFetchImConfig(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmChangeFlowDO flow = this.changeFlowService.queryFlowById(puid, fo.getFlowId());
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }

        DmChangeFlowDO data = this.changeFlowService.queryMessageByFlowId(puid, fo.getFlowId());
        DmSysMessengerDO messengerDO = null;
        if (data != null && data.getRefMsgId() != null) {
            messengerDO = this.dmImService.queryImById(puid, data.getRefMsgId());
        }
        return ResWebDataUtils.buildSuccess(DmConvertUtils.convertToChangeFlowImConfigVO(data, messengerDO));
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/pushImConfig", method = RequestMethod.POST)
    public ResWebData<?> flowPushImConfig(HttpServletRequest request, @Valid @RequestBody ChangeFlowImConfigFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.changeFlowService.updateMessageByFlowId(puid, fo.getFlowId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/gitOpsCreate", method = RequestMethod.POST)
    public ResWebData<?> flowGitOpsCreate(HttpServletRequest request, @Valid @RequestBody ChangeFlowGitOpsCreateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        GuideCreateChangeFlowVO result = this.changeFlowService.createGitOpsFlow(puid, fo.getFlowId(), fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/gitOpsDelete", method = RequestMethod.POST)
    public ResWebData<?> flowGitOpsDelete(HttpServletRequest request, @Valid @RequestBody ChangeFlowGitOpsDeleteFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.changeFlowService.deleteGitOpsFlow(puid, fo.getFlowId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/switch", method = RequestMethod.POST)
    public ResWebData<?> flowGitOpsSwitch(HttpServletRequest request, @Valid @RequestBody ChangeFlowGitOpsSwitchFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.isEnable()) {
            this.changeFlowService.enableGitOpsFlow(puid, fo.getFlowId());
        } else {
            this.changeFlowService.disableGitOpsFlow(puid, fo.getFlowId());
        }

        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/triggerConfig", method = RequestMethod.POST)
    public ResWebData<?> flowTriggerConfig(HttpServletRequest request, @Valid @RequestBody ChangeFlowTriggerConfigFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.isUpdateHook()) {
            this.changeFlowService.configGitOpsWebhook(puid, fo.getFlowId(), fo.isHookEnable(), fo.getHookSigningToken(), fo.isClearHookSigningToken());
        }
        if (fo.isUpdateTrigger()) {
            this.changeFlowService.configGitOpsTrigger(puid, fo.getFlowId(), fo.isTriggerEnable());
        }
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/callbackConfig", method = RequestMethod.POST)
    public ResWebData<?> flowCallbackConfig(HttpServletRequest request, @Valid @RequestBody ChangeFlowCallbackFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.changeFlowService.configGitOpsCallback(puid, fo.getFlowId(), fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/archive", method = RequestMethod.POST)
    public ResWebData<?> flowArchive(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.changeFlowService.archiveFlow(puid, fo.getFlowId(), uid);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> flowDelete(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.changeFlowService.deleteFlow(puid, fo.getFlowId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    public ResWebData<?> flowRecover(HttpServletRequest request, @Valid @RequestBody ChangeFlowRequestFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DmChangeFlowDO flowDO = this.changeFlowService.queryFlowById(puid, fo.getFlowId());
        if (flowDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flowDO.getChangeFlowStatus() == ChangeFlowStatus.NORMAL) {
            return ResWebDataUtils.buildSuccess(true);
        }

        if (flowDO.getChangeFlowStatus() == ChangeFlowStatus.DELETE) {
            this.changeFlowService.recoverFlowTo(puid, fo.getFlowId(), ChangeFlowStatus.ARCHIVE);
        } else if (flowDO.getChangeFlowStatus() == ChangeFlowStatus.ARCHIVE) {
            this.changeFlowService.recoverFlowTo(puid, fo.getFlowId(), ChangeFlowStatus.NORMAL);
        }

        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/triggerChange", method = RequestMethod.POST)
    public ResWebData<?> flowTriggerChange(HttpServletRequest request, @Valid @RequestBody ChangeFlowTriggerFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.verifyFlow(puid, fo.getFlowId());
        DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(puid, fo.getFlowId());
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo( //
                gitOpsFlowDO.getOwnerUid(), //
                gitOpsFlowDO.getRefScmId(), //
                gitOpsFlowDO.getScmRepoIdentifier(), //
                gitOpsFlowDO.getScmRepoSpace(), //
                gitOpsFlowDO.getScmRepoName(), //
                gitOpsFlowDO.getScmRepoBranch());
        if (branch == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_BRANCH_NOT_EXIST_ERROR.name()));
        }

        // create
        return this.dmChangeService.triggerChangeSuggest(puid, gitOpsFlowDO.getId(), ChangeTriggerContext.manual(branch.getBranchCommitId()));
    }

    @RequestAuth(DM_CICD_FLOW_MANAGE)
    @RequestMapping(value = "/triggerSnapshot", method = RequestMethod.POST)
    public ResWebData<?> flowTriggerSnapshot(HttpServletRequest request, @Valid @RequestBody ChangeFlowTriggerFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmChangeService.verifyFlow(puid, fo.getFlowId());
        DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(puid, fo.getFlowId());
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(gitOpsFlowDO.getOwnerUid(),//
                gitOpsFlowDO.getRefScmId(),//
                gitOpsFlowDO.getScmRepoIdentifier(),//
                gitOpsFlowDO.getScmRepoSpace(),//
                gitOpsFlowDO.getScmRepoName(),//
                gitOpsFlowDO.getScmRepoBranch());
        if (branch == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_BRANCH_NOT_EXIST_ERROR.name()));
        }

        // check and create
        List<DmChangeDO> list = this.changeFlowDal.changeMapper().queryUnlockedChange(puid, gitOpsFlowDO.getId());
        if (!list.isEmpty()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_TRIGGER_SNAPSHOT_HAS_CHANGE_ERROR.name()));
        }

        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(gitOpsFlowDO.getOwnerUid());
        changeDO.setRefFlowId(gitOpsFlowDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_SNAPSHOT_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ChangeStep.INIT_SNAPSHOT);
        changeDO.setCurrentStatus(ChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(true);
        this.changeFlowDal.changeMapper().insert(changeDO);
        return ResWebDataUtils.buildSuccess(true);
    }
}
