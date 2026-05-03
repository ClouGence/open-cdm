package com.clougence.clouddm.console.web.service.project.impl;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectChangeMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMsgMapper;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.dal.model.queryobj.DmProjectQueryObj;
import org.springframework.transaction.annotation.Transactional;
import com.clougence.clouddm.console.web.model.fo.project.*;
import com.clougence.clouddm.console.web.model.vo.project.GuideCreateProjectVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectVO;
import com.clougence.clouddm.console.web.service.project.DmImService;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.mapper.RdpUserKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.util.RandomStrUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.rdp.util.RdpPageUtil;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HashUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmProjectServiceImpl implements DmProjectService {

    @Resource
    private RdpUserKvBaseConfigMapper userConfigMapper;
    @Resource
    private DmDsConfigService         dmDsConfigService;
    @Resource
    private DmProjectMapper           dmProjectMapper;
    @Resource
    private DmProjectChangeMapper     dmProjectChangeMapper;
    @Resource
    private DmProjectDevopsMapper     dmProjectDevopsMapper;
    @Resource
    private DmProjectMsgMapper        dmProjectMsgMapper;
    @Resource
    private BizResOwnerCacheService   ownerCacheService;
    @Resource
    private DmImService               dmImService;
    @Resource
    private DmScmService              dmScmService;
    @Resource
    private ImSenderService           imSenderService;
    @Resource
    private DmDsService               dmDsService;

    @Override
    public IPage<ProjectVO> queryProjectListByPage(String ownerUid, ProjectListFO fo) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());

        DmProjectQueryObj queryParams = DmProjectQueryObj.builder()//
            .searchKeywords(StringUtils.isBlank(fo.getSearchKeywords()) ? null : fo.getSearchKeywords())
            .mark(StringUtils.isBlank(fo.getMark()) ? null : fo.getMark())
            .status(StringUtils.isBlank(fo.getStatus()) ? null : fo.getStatus())
            .build();

        IPage<DmProjectDO> pageData = this.dmProjectMapper.listProjectByConditionAndPage(page, queryParams, ownerUid);
        List<DmProjectDO> records = pageData.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        List<ProjectVO> vos = records.stream().map(obj -> {
            return DmConvertUtils.convertToProjectVO(obj, this.ownerCacheService);
        }).collect(Collectors.toList());

        IPage<ProjectVO> results = new Page<>();
        results.setRecords(vos);
        results.setCurrent(pageData.getCurrent());
        results.setSize(pageData.getSize());
        results.setPages(pageData.getPages());
        results.setTotal(pageData.getTotal());
        return results;
    }

    @Override
    public List<ProjectVO> queryProjectListByIDs(String ownerUid, Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<DmProjectDO> res = this.dmProjectMapper.listProjectByIds(ownerUid, ids);
        return res.stream().map(obj -> {
            return DmConvertUtils.convertToProjectVO(obj, ownerCacheService);
        }).collect(Collectors.toList());
    }

    @Override
    public List<DmProjectDevopsDO> queryEnableDevopsByDsId(String ownerUid, long dsId) {
        return this.dmProjectDevopsMapper.queryEnableByOwnerAndDsId(ownerUid, dsId);
    }

    @Override
    public List<DmProjectDevopsDO> queryEnableDevopsByScmId(String ownerUid, long scmId) {
        return this.dmProjectDevopsMapper.queryEnableByOwnerAndScmId(ownerUid, scmId);
    }

    @Override
    public List<DmProjectMsgDO> queryEnableDevopsByImId(String ownerUid, long imId) {
        return this.dmProjectMsgMapper.queryEnableByOwnerAndImId(ownerUid, imId);
    }

    @Override
    public List<DmProjectDevopsDO> queryEnableDevopsByScmHash(String ownerUid, long scmHash) {
        return this.dmProjectDevopsMapper.queryEnableByOwnerAndScmHash(ownerUid, scmHash);
    }

    @Override
    public List<DmProjectDevopsDO> queryAllDevopsByProjectId(String ownerUid, long projectId) {
        return this.dmProjectDevopsMapper.queryAllDevopsByProjectId(ownerUid, projectId);
    }

    @Override
    public DmProjectMsgDO queryMessageByProjectId(String ownerUid, long projectId) {
        return this.dmProjectMsgMapper.queryMessageByProjectId(ownerUid, projectId);
    }

    @Override
    public long toHash(GuideCheckFlowFO fo) {
        String strBuilder = fo.getRepoScmUrl().trim() + "/" + fo.getRepoBranch().trim() + "/" + fo.getDsId() + "/" + "[" + StringUtils.join(fo.getDsLevels(), "/") + "]";
        return HashUtils.fnvHash(strBuilder);
    }

    private long toHash(DmProjectDevopsDO fo) {
        String strBuilder = fo.getScmRepoUrl().trim() + "/" + fo.getScmRepoBranch().trim() + "/" + fo.getDsId() + "/" + "[" + fo.getDsPath() + "]";
        return HashUtils.fnvHash(strBuilder);
    }

    private String toString(DmProjectDevopsDO fo) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(fo.getScmRepoUrl().trim() + ":");
        strBuilder.append(fo.getScmRepoBranch().trim());

        strBuilder.append("\n");
        strBuilder.append(fo.getScmRepoScript());

        strBuilder.append("\n");
        DsCacheEntry dsEntry = this.ownerCacheService.queryByDsId(fo.getDsId());
        strBuilder.append("(" + dsEntry.getDsType() + ") " + dsEntry.getDsInstId() + "[" + dsEntry.getDsInstDesc() + "] " + fo.getDsPath());
        return strBuilder.toString();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public GuideCreateProjectVO createProject(String ownerUid, String currentUser, GuideCreateFO fo) {
        DmProjectMsgDO msgDO = checkAndCreateMsg(ownerUid, fo);
        DmProjectDevopsDO devopsDO = checkAndCreateDevops(ownerUid, fo.getPipeline());
        checkDevopsConflict(ownerUid, devopsDO);

        //
        DmProjectDO projectDO = new DmProjectDO();
        projectDO.setOwnerUid(ownerUid);
        projectDO.setProjectCode(RandomStrUtils.fixedLenRandomStr(12));
        projectDO.setProjectName(fo.getProjectName());
        projectDO.setProjectDesc(fo.getProjectDesc());
        projectDO.setProjectUid(StringUtils.isBlank(fo.getProjectOwnerUid()) ? currentUser : fo.getProjectOwnerUid());
        projectDO.setProjectStatus(ProjectStatus.NORMAL);
        projectDO.setProjectMark("CircleGray");
        projectDO.setFlowCheck((fo.getOption() != null && fo.getOption().getCheckStrategy() != null) ? fo.getOption().getCheckStrategy() : DmChangeCheckStrategy.Always);
        projectDO.setFlowApprove((fo.getOption() != null && fo.getOption().getApproveStrategy() != null) ? fo.getOption().getApproveStrategy() : DmChangeApproveStrategy.Enable);
        projectDO.setOptions(createProjectOptions(fo.getOption()));

        int res1 = this.dmProjectMapper.insert(projectDO);

        if (devopsDO != null) {
            devopsDO.setRefProjectId(projectDO.getId());
            int res2 = this.dmProjectDevopsMapper.insert(devopsDO);
        }
        if (msgDO != null) {
            msgDO.setRefProjectId(projectDO.getId());
            int res3 = this.dmProjectMsgMapper.insert(msgDO);
        }

        if (fo.getOption() != null && fo.getOption().getInitScript() != null) {
            this.initInitScript(projectDO, devopsDO, fo.getOption().getInitScript());
        } else {
            this.initInitScript(projectDO, devopsDO, DmInitScriptStrategy.None);
        }

        GuideCreateProjectVO vo = new GuideCreateProjectVO();
        vo.setProjectId(projectDO.getId());
        if (devopsDO != null) {
            vo.setDevopsId(devopsDO.getId());
            vo.setRepoUrl(devopsDO.getScmRepoUrl());
            vo.setWebHookUrl(DmConvertUtils.generateDevOpsWebHookCallBackUrl(devopsDO));
            vo.setWebHookPwd(devopsDO.getScmBindWebhookPwd());

            DmScmDef defByType = this.dmScmService.getScmDefByType(devopsDO.getRefScmType());
            if (defByType != null) {
                vo.setWebHookHelpUrl(defByType.getHelpUrl());
            }
        }
        return vo;
    }

    private DmProjectMsgDO checkAndCreateMsg(String ownerUid, GuideCreateFO fo) {
        if (fo.getMessenger() == null) {
            return null;
        }

        DmMessengerDO messengerDO = this.dmImService.queryImById(ownerUid, fo.getMessenger().getImId());
        if (messengerDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        DmProjectMsgDO msgDO = new DmProjectMsgDO();
        msgDO.setOwnerUid(ownerUid);
        msgDO.setRefProjectId(-1);
        msgDO.setRefMsgId(messengerDO.getId());
        msgDO.setRefMsgType(messengerDO.getImType());
        msgDO.setEnable(true);
        msgDO.setLanguage(fo.getMessenger().getLanguage());
        msgDO.setEventProjectStatus(fo.getMessenger().isEventProjectStatus());
        msgDO.setEventProjectConfig(fo.getMessenger().isEventProjectConfig());
        msgDO.setEventChangeLife(fo.getMessenger().isEventChangeLife());
        msgDO.setEventChangeNotice(fo.getMessenger().isEventChangeNotice());
        return msgDO;
    }

    private DmProjectDevopsDO checkAndCreateDevops(String ownerUid, GuidePipelineFO pipeline) {
        if (pipeline == null) {
            return null;
        }

        DsLevels dsLevels = this.dmDsConfigService.parseLevels(pipeline.getDsLevels());
        RdpDataSourceDO dsDO = dsLevels.getDsDO();
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }
        DmDsConfigDO dmDsConfigDO = dmDsService.fetchDsConfigById(ownerUid, dsDO.getId());
        if (dmDsConfigDO == null || !dmDsConfigDO.isEnableDevops()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DEVOPS_NEED_ENABLE.name()));
        }
        DmProjectScmDO scmDO = this.dmScmService.queryScmById(ownerUid, pipeline.getRepoScmId());
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        DmProjectDevopsDO devopsDO = new DmProjectDevopsDO();
        devopsDO.setOwnerUid(ownerUid);
        devopsDO.setRefProjectId(-1);
        devopsDO.setRefScmId(pipeline.getRepoScmId());
        devopsDO.setRefScmType(scmDO.getScmType());
        devopsDO.setScmRepoSpace(pipeline.getRepoSpace());
        devopsDO.setScmRepoName(pipeline.getRepoName());
        devopsDO.setScmRepoUrl(pipeline.getRepoScmUrl());
        devopsDO.setScmRepoBranch(pipeline.getRepoBranch());
        devopsDO.setScmRepoEvent(pipeline.getEventType());
        if (StringUtils.isNotBlank(pipeline.getRepoScriptPath())) {
            devopsDO.setScmRepoScript(StringUtils.trimStart(pipeline.getRepoScriptPath(), '/'));
        } else {
            devopsDO.setScmRepoScript("");
        }

        devopsDO.setDsId(dsDO.getId());
        devopsDO.setDsType(dsDO.getDataSourceType());
        devopsDO.setDsInstance(dsDO.getInstanceId());
        devopsDO.setDsDesc(dsDO.getInstanceDesc());
        devopsDO.setDsPath("/" + StringUtils.join(pipeline.getDsLevels().toArray(), "/"));

        devopsDO.setOptions(this.createDevopsOptions(null));
        devopsDO.setDevopsHashcode(this.toHash(devopsDO));
        devopsDO.setScmBindWebhookPwd(RandomStrUtils.fixedLenRandomStr(32).toUpperCase());
        devopsDO.setEnableWebhook(true);
        devopsDO.setCallbackUrl("");
        devopsDO.setCallbackMethod("POST");
        devopsDO.setEnableCallback(false);
        devopsDO.setEnableTrigger(false);
        devopsDO.setTriggerToken(RandomStrUtils.fixedLenRandomStr(32).toUpperCase());
        devopsDO.setEnable(true);
        return devopsDO;
    }

    private void checkDevopsConflict(String ownerUid, DmProjectDevopsDO devopsDO) {
        if (devopsDO == null) {
            return;
        }
        List<DmProjectDevopsDO> devops = this.queryEnableDevopsByScmHash(ownerUid, devopsDO.getDevopsHashcode());
        if (!devops.isEmpty()) {
            Set<Long> projectId = devops.stream().map(DmProjectDevopsDO::getRefProjectId).collect(Collectors.toSet());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CONFLICT_ERROR.name(), projectId.size()));
        }
    }

    private DmProjectOption createProjectOptions(ProjectOptionFO fo) {
        DmProjectOption options = new DmProjectOption();
        if (fo == null) {
            options.setTransactional(false);
            options.setErrorStrategy(ErrorStrategy.NONE);
            return options;
        }

        // exec default
        options.setTransactional(fo.isTransactional());
        options.setErrorStrategy(fo.getErrorStrategy());
        options.setRetryCount(fo.getRetryCount());
        options.setRetryWaitTime(fo.getRetryWaitTime());
        return options;
    }

    private DmProjectDevopsOption createDevopsOptions(ProjectDevopsOptionFO fo) {
        return new DmProjectDevopsOption();
    }

    private void initInitScript(DmProjectDO projectDO, DmProjectDevopsDO devopsDO, DmInitScriptStrategy initScript) {
        switch (initScript) {
            case Snapshot:
                this.initInitScriptForSnapshot(projectDO, devopsDO);
                break;
            case CreateChange:
                this.initInitScriptForChange(projectDO, devopsDO);
                break;
            case None:
            default:
                break;
        }
    }

    private void initInitScriptForSnapshot(DmProjectDO projectDO, DmProjectDevopsDO devopsDO) {
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(projectDO.getOwnerUid(), devopsDO.getRefScmId(), devopsDO.getScmRepoName(), devopsDO.getScmRepoBranch());
        if (branch == null) {
            return;
        }

        DmProjectChangeDO changeDO = new DmProjectChangeDO();
        changeDO.setOwnerUid(projectDO.getOwnerUid());
        changeDO.setRefProjectId(projectDO.getId());
        changeDO.setRefDevopsId(devopsDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_INIT_SNAPSHOT_NAME.name()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ProjectChangeStep.INIT_SNAPSHOT);
        changeDO.setCurrentStatus(ProjectChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(true);
        changeDO.setFlowWalked(new DmProjectChangeFlowWalked());
        this.dmProjectChangeMapper.insert(changeDO);
    }

    private void initInitScriptForChange(DmProjectDO projectDO, DmProjectDevopsDO devopsDO) {
        DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(projectDO.getOwnerUid(), devopsDO.getRefScmId(), devopsDO.getScmRepoName(), devopsDO.getScmRepoBranch());
        if (branch == null) {
            return;
        }

        DmProjectChangeDO changeDO = new DmProjectChangeDO();
        changeDO.setOwnerUid(projectDO.getOwnerUid());
        changeDO.setRefProjectId(projectDO.getId());
        changeDO.setRefDevopsId(devopsDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_INIT_CHANGE_NAME.name()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ProjectChangeStep.INIT);
        changeDO.setCurrentStatus(ProjectChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(false);
        changeDO.setFlowWalked(new DmProjectChangeFlowWalked());
        this.dmProjectChangeMapper.insert(changeDO);
    }

    @Override
    public DmProjectDO queryProjectById(String ownerUid, long projectId) {
        return this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
    }

    @Override
    public void updateInfoByProjectId(String ownerUid, long projectId, ProjectUpdateFO fo) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, fo.getProjectId());
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        String projectName = project.getProjectName();
        List<String> messageList = new ArrayList<>();

        // for PM
        if (StringUtils.isNotBlank(fo.getNewAdminUid()) && !fo.getNewAdminUid().equals(project.getProjectUid())) {
            UserCacheEntry user = this.ownerCacheService.queryByUid(fo.getNewAdminUid());
            if (user == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_USER_NOT_EXIST_ERROR.name()));
            }
            this.dmProjectMapper.updateProjectManagerByOwnerAndId(ownerUid, projectId, fo.getNewAdminUid());

            // message
            UserCacheEntry operatorUser = this.ownerCacheService.queryByUid(fo.getNewAdminUid());
            String operatorMsg = String.format("[%s] %s", RdpI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_PM_MESSAGE.name(), operatorMsg);
            messageList.add(textMsg);
        }

        // for name
        if (StringUtils.isNotBlank(fo.getNewName()) && !fo.getNewName().equals(project.getProjectName())) {
            this.dmProjectMapper.updateProjectNameByOwnerAndId(ownerUid, projectId, fo.getNewName());

            // message
            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_RENAME_MESSAGE.name(), fo.getNewName());
            messageList.add(textMsg);
        }

        // for desc
        if (StringUtils.isNotBlank(fo.getNewDesc()) && !fo.getNewDesc().equals(project.getProjectDesc())) {
            this.dmProjectMapper.updateProjectDescByOwnerAndId(ownerUid, projectId, fo.getNewDesc());

            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_DESC_MESSAGE.name(), fo.getNewDesc());
            messageList.add(textMsg);
        }

        // for mark
        if (StringUtils.isNotBlank(fo.getNewMark()) && !fo.getNewMark().equals(project.getProjectMark())) {
            this.dmProjectMapper.updateProjectMarkByOwnerAndId(ownerUid, projectId, fo.getNewMark());

            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_MARK_MESSAGE.name(), project.getProjectMark(), fo.getNewMark());
            messageList.add(textMsg);
        }

        // message
        if (!messageList.isEmpty()) {
            StringBuilder strBuilder = new StringBuilder(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_TITLE_MESSAGE.name(), projectName));
            for (int i = 0; i < messageList.size(); i++) {
                String strBody = messageList.get(i);
                strBuilder.append("\n");
                strBuilder.append((i + 1) + ". " + strBody);
            }
            this.imSenderService.sendMessage(ownerUid, projectId, ImMessageType.ProjectConfig, strBuilder.toString());
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateMessageByProjectId(String ownerUid, long projectId, ProjectPushImConfigFO fo) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, fo.getProjectId());
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        if (fo.isDelete()) {
            deleteOldMessenger(ownerUid, projectId);
        } else {
            DmMessengerDO messengerDO = this.dmImService.queryImById(ownerUid, fo.getImId());
            if (messengerDO == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
            }

            DmProjectMsgDO msgDO = new DmProjectMsgDO();
            msgDO.setOwnerUid(ownerUid);
            msgDO.setRefProjectId(project.getId());
            msgDO.setRefMsgId(messengerDO.getId());
            msgDO.setRefMsgType(messengerDO.getImType());
            msgDO.setLanguage(fo.getLanguage());
            msgDO.setEnable(true);
            msgDO.setEventProjectStatus(fo.isEventProjectStatus());
            msgDO.setEventProjectConfig(fo.isEventProjectConfig());
            msgDO.setEventChangeLife(fo.isEventChangeLife());
            msgDO.setEventChangeNotice(fo.isEventChangeNotice());

            deleteOldMessenger(ownerUid, projectId);
            int res = this.dmProjectMsgMapper.insert(msgDO);
        }

        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_IM_MESSAGE.name(), project.getProjectName());
        this.imSenderService.sendMessage(ownerUid, projectId, ImMessageType.ProjectConfig, textMsg);
    }

    @Override
    public void updateFlowByProjectId(String ownerUid, long projectId, ProjectPushFlowConfigFO fo) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, fo.getProjectId());
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (fo.getCheckStrategy() == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getApproveStrategy() == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getExecuteStrategy() == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getExecuteStrategy() == DmChangeExecStrategy.Auto && fo.getErrorStrategy() == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        project.setFlowCheck(fo.getCheckStrategy());
        project.setFlowApprove(fo.getApproveStrategy());
        project.setFlowExecute(fo.getExecuteStrategy());
        if (fo.getExecuteStrategy() == DmChangeExecStrategy.Auto) {
            project.getOptions().setTransactional(fo.isTransactional());
            project.getOptions().setErrorStrategy(fo.getErrorStrategy());
        }
        this.dmProjectMapper.updateFlowByOwnerAndId(ownerUid, fo.getProjectId(), project);
    }

    private void deleteOldMessenger(String ownerUid, long projectId) {
        DmProjectMsgDO oldMsgConfig = this.dmProjectMsgMapper.queryMessageByProjectId(ownerUid, projectId);
        if (oldMsgConfig != null) {
            this.dmProjectMsgMapper.deleteByOwnerAndId(ownerUid, oldMsgConfig.getId());
        }
    }

    @Override
    public long createProjectDevops(String ownerUid, long projectId, ProjectDevopsCreateFO fo) {
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(ownerUid, fo.getProjectId());
        if (projectDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (projectDO.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        DmProjectDevopsDO devopsDO = checkAndCreateDevops(ownerUid, fo.getPipeline());
        checkDevopsConflict(ownerUid, devopsDO);

        try {
            devopsDO.setRefProjectId(projectDO.getId());
            int res = this.dmProjectDevopsMapper.insert(devopsDO);
        } finally {
            this.initInitScript(projectDO, devopsDO, fo.getOption().getInitScript());
        }

        //
        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_NEW_DEVOPS_MESSAGE.name(), projectDO.getProjectName(), toString(devopsDO));
        this.imSenderService.sendMessage(ownerUid, projectId, ImMessageType.ProjectConfig, textMsg);
        return devopsDO.getId();
    }

    @Override
    public void deleteProjectDevops(String ownerUid, long projectId, long devopsId) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        int useCount = this.dmProjectChangeMapper.countUnEndChangeByDevopsId(ownerUid, devopsId);
        if (useCount > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CHANGE_IN_INUSE_ERROR.name(), useCount));
        }

        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (devopsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NOT_EXIST_ERROR.name()));
        }

        int res = this.dmProjectDevopsMapper.deleteByOwnerAndProjectAndId(ownerUid, projectId, devopsId);

        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CONFIG_DEL_DEVOPS_MESSAGE.name(), project.getProjectName(), toString(devopsDO));
        this.imSenderService.sendMessage(ownerUid, projectId, ImMessageType.ProjectConfig, textMsg);
    }

    @Override
    public void projectDevopsEnable(String ownerUid, long projectId, long devopsId) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (devopsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NOT_EXIST_ERROR.name()));
        }
        DmProjectDevopsDO dmProjectDevopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);

        DmDsConfigDO dmDsConfigDO = dmDsService.fetchDsConfigById(ownerUid, dmProjectDevopsDO.getDsId());
        if (dmDsConfigDO == null || !dmDsConfigDO.isEnableDevops()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_DEVOPS_NEED_ENABLE.name()));
        }

        checkDevopsConflict(ownerUid, devopsDO);
        this.dmProjectDevopsMapper.enableDevopsByProjectAndId(ownerUid, projectId, devopsId);
    }

    @Override
    public void projectDevopsDisable(String ownerUid, long projectId, long devopsId) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        this.dmProjectDevopsMapper.disableDevopsByProjectAndId(ownerUid, projectId, devopsId);
    }

    @Override
    public void projectDevopsConfigWebHook(String ownerUid, long projectId, long devopsId, boolean enable) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (!devopsDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        if (enable) {
            this.dmProjectDevopsMapper.enableWebHookByProjectAndId(ownerUid, projectId, devopsId);
        } else {
            this.dmProjectDevopsMapper.disableWebHookByProjectAndId(ownerUid, projectId, devopsId);
        }
    }

    @Override
    public void projectDevopsConfigTrigger(String ownerUid, long projectId, long devopsId, boolean enable) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (!devopsDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        if (enable) {
            this.dmProjectDevopsMapper.enableTriggerByProjectAndId(ownerUid, projectId, devopsId);
        } else {
            this.dmProjectDevopsMapper.disableTriggerByProjectAndId(ownerUid, projectId, devopsId);
        }
    }

    @Override
    public void projectDevopsConfigCallBack(String ownerUid, long projectId, long devopsId, ProjectDevopsCallBackFO fo) {
        boolean methodOk = StringUtils.equalsIgnoreCase(fo.getMethod(), "post") || StringUtils.equalsIgnoreCase(fo.getMethod(), "get");
        boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getUrl(), "https://");
        if (!methodOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_CONFIG_METHOD_NOT_SUPPORT.name(), fo.getMethod()));
        }
        if (!urlOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CALLBACK_CONFIG_URL_NOT_SUPPORT.name()));
        }

        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (!devopsDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        this.dmProjectDevopsMapper.configCallBackByProjectAndId(ownerUid, projectId, devopsId, fo.isEnable(), fo.getMethod(), fo.getUrl());
    }

    @Override
    public void archiveProject(String ownerUid, long projectId, String operatorUid) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        switch (project.getProjectStatus()) {
            case DELETE:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_DELETE_UN_SUPPORT_ARCHIVE_ERROR.name()));
            case ARCHIVE:
                return;
            case NORMAL:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        int usingCount = this.dmProjectChangeMapper.countUnEndChangeByProjectId(ownerUid, projectId);
        if (usingCount > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_ARCHIVE_CHANGE_ON_END_ERROR.name(), usingCount));
        }

        // send message
        UserCacheEntry operatorUser = this.ownerCacheService.queryByUid(operatorUid);
        String operatorMsg = String.format("[%s] %s", RdpI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_STATUS_ARCHIVE_MESSAGE.name(), operatorMsg, project.getProjectName());
        this.imSenderService.sendMessage(ownerUid, projectId, ImMessageType.ProjectStatus, textMsg);

        //
        this.dmProjectDevopsMapper.disableAllDevopsByProjectId(ownerUid, projectId);
        this.dmProjectMsgMapper.disableAllImByProjectId(ownerUid, projectId);
        this.dmProjectMapper.updateProjectStatusByOwnerAndId(ownerUid, projectId, ProjectStatus.ARCHIVE);
    }

    @Override
    public void recoverProjectTo(String ownerUid, long projectId, ProjectStatus toStatus) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (toStatus == ProjectStatus.DELETE) {
            throw new UnsupportedOperationException();
        }

        if (project.getProjectStatus() != ProjectStatus.NORMAL) {
            this.dmProjectMapper.updateProjectStatusByOwnerAndId(ownerUid, projectId, toStatus);
        }

        if (toStatus == ProjectStatus.NORMAL) {
            this.dmProjectMsgMapper.enableAllImByProjectId(ownerUid, projectId);
        }
    }

    @Override
    public void deleteProject(String ownerUid, long projectId) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        switch (project.getProjectStatus()) {
            case NORMAL:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NORMAL_UN_SUPPORT_DELETE_ERROR.name()));
            case ARCHIVE:
                break;
            case DELETE:
                return;
            default:
                throw new UnsupportedOperationException();
        }

        this.dmProjectMapper.updateProjectStatusByOwnerAndId(ownerUid, projectId, ProjectStatus.DELETE);
    }

    @Override
    public File getProjectSpace(String ownerUid, long projectId) {
        RdpUserKvBaseConfigDO currentConfig = this.userConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.defaultProjectSpace);
        if (currentConfig == null) {
            return new File(GlobalConfUtils.getAppDataHome(), "default");
        }

        String configValue = currentConfig.getConfigValue();
        if (StringUtils.isNotBlank(configValue)) {
            File test = new File(configValue);
            if (StringUtils.equals(test.getAbsolutePath(), configValue)) {
                return test;
            } else {
                return new File(GlobalConfUtils.getAppDataHome(), configValue);
            }
        } else {
            return new File(GlobalConfUtils.getAppDataHome(), "default");
        }
    }

    @Override
    public File getTempSpace(String ownerUid, long projectId) {
        RdpUserKvBaseConfigDO currentConfig = this.userConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.defaultTempSpace);
        if (currentConfig == null) {
            return new File(GlobalConfUtils.getTempDataHome());
        }

        String configValue = currentConfig.getConfigValue();
        if (StringUtils.isNotBlank(configValue)) {
            File test = new File(configValue);
            if (StringUtils.equals(test.getAbsolutePath(), configValue)) {
                return test;
            } else {
                return new File(GlobalConfUtils.getAppDataHome(), configValue);
            }
        } else {
            return new File(GlobalConfUtils.getTempDataHome());
        }
    }
}
