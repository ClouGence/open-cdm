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

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmInitScriptStrategy;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.cicd.*;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeFlowVO;
import com.clougence.clouddm.console.web.model.vo.cicd.GuideCreateChangeFlowVO;
import com.clougence.clouddm.console.web.service.cicd.domain.DmBranchDef;
import com.clougence.clouddm.console.web.service.cicd.domain.DmScmDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HashUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmChangeFlowServiceImpl implements DmChangeFlowService {
    @Resource
    private SystemDal         systemDal;
    @Resource
    private ChangeFlowDal     changeFlowDal;
    @Resource
    private DmDsConfigService dmDsConfigService;
    @Resource
    private ObjectCacheDao    objectCacheDao;
    @Resource
    private DmImService       dmImService;
    @Resource
    private DmScmService      dmScmService;
    @Resource
    private ImSenderService   senderService;
    @Resource
    private DmDsService       dmDsService;

    @Override
    public IPage<ChangeFlowVO> queryChangeFlowListByPage(String ownerUid, ChangeFlowListFO fo) {
        Page<?> page = PageUtils.startPage(fo.getPage());

        ArgChangeFlowQueryObj queryParams = ArgChangeFlowQueryObj.builder()//
            .searchKeywords(StringUtils.isBlank(fo.getSearchKeywords()) ? null : fo.getSearchKeywords())
            .status(StringUtils.isBlank(fo.getStatus()) ? null : fo.getStatus())
            .build();

        IPage<DmChangeFlowDO> pageData = this.changeFlowDal.flowMapper().listFlowByConditionAndPage(page, queryParams, ownerUid);
        List<DmChangeFlowDO> records = pageData.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        List<ChangeFlowVO> vos = records.stream().map(obj -> {
            return DmConvertUtils.convertToChangeFlowVO(obj, this.objectCacheDao);
        }).collect(Collectors.toList());

        IPage<ChangeFlowVO> results = new Page<>();
        results.setRecords(vos);
        results.setCurrent(pageData.getCurrent());
        results.setSize(pageData.getSize());
        results.setPages(pageData.getPages());
        results.setTotal(pageData.getTotal());
        return results;
    }

    @Override
    public List<ChangeFlowVO> queryChangeFlowListByIds(String ownerUid, Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<DmChangeFlowDO> res = this.changeFlowDal.flowMapper().listFlowByIds(ownerUid, ids);
        return res.stream().map(obj -> {
            return DmConvertUtils.convertToChangeFlowVO(obj, objectCacheDao);
        }).collect(Collectors.toList());
    }

    @Override
    public List<DmChangeFlowDO> queryEnableDevopsByDsId(String ownerUid, long dsId) {
        return this.changeFlowDal.flowMapper().queryEnabledByOwnerAndDsId(ownerUid, dsId);
    }

    @Override
    public List<DmChangeFlowDO> queryEnableDevopsByScmId(String ownerUid, long scmId) {
        return this.changeFlowDal.flowMapper().queryEnabledByOwnerAndScmId(ownerUid, scmId);
    }

    @Override
    public List<DmChangeFlowDO> queryEnableDevopsByImId(String ownerUid, long imId) {
        return this.changeFlowDal.flowMapper().queryEnabledByOwnerAndImId(ownerUid, imId);
    }

    @Override
    public List<DmChangeFlowDO> queryEnableDevopsByScmHash(String ownerUid, long scmHash) {
        return this.changeFlowDal.flowMapper().queryEnabledByOwnerAndHash(ownerUid, scmHash);
    }

    @Override
    public List<DmChangeFlowDO> queryAllGitOpsByFlowId(String ownerUid, long flowId) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null || flow.isDeleted()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(flow);
    }

    @Override
    public DmChangeFlowDO queryMessageByFlowId(String ownerUid, long flowId) {
        return this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
    }

    @Override
    public long toHash(GuideCheckFlowFO fo) {
        String strBuilder = fo.getRepoScmUrl().trim() + "/" + fo.getRepoBranch().trim() + "/" + fo.getDsId() + "/" + "[" + StringUtils.join(fo.getDsLevels(), "/") + "]";
        return HashUtils.fnvHash(strBuilder);
    }

    private long toHash(DmChangeFlowDO fo) {
        String strBuilder = fo.getScmRepoUrl().trim() + "/" + fo.getScmRepoBranch().trim() + "/" + fo.getDsId() + "/" + "[" + fo.getDsPath() + "]";
        return HashUtils.fnvHash(strBuilder);
    }

    private String toString(DmChangeFlowDO fo) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(fo.getScmRepoUrl().trim() + ":");
        strBuilder.append(fo.getScmRepoBranch().trim());

        strBuilder.append("\n");
        strBuilder.append(fo.getScmRepoScript());

        strBuilder.append("\n");
        DsCacheEntry dsEntry = this.objectCacheDao.queryByDsId(fo.getDsId());
        strBuilder.append("(" + dsEntry.getDsType() + ") " + dsEntry.getDsInstId() + "[" + dsEntry.getDsInstDesc() + "] " + fo.getDsPath());
        return strBuilder.toString();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public GuideCreateChangeFlowVO createChangeFlow(String ownerUid, String currentUser, GuideCreateFO fo) {
        DmChangeFlowDO flowDO = checkAndCreateDevops(ownerUid, fo.getPipeline());
        checkDevopsConflict(ownerUid, flowDO);

        flowDO.setOwnerUid(ownerUid);
        flowDO.setFlowUid(RandomStrUtils.fixedLenRandomStr(12));
        flowDO.setFlowName(fo.getFlowName());
        flowDO.setFlowDesc(fo.getFlowDesc());
        flowDO.setFlowManagerUid(StringUtils.isBlank(fo.getFlowManagerUid()) ? currentUser : fo.getFlowManagerUid());
        flowDO.setFlowStatus(ChangeFlowStatus.NORMAL);
        flowDO.setFlowCheck((fo.getOption() != null && fo.getOption().getCheckStrategy() != null) ? fo.getOption().getCheckStrategy() : ChangeCheckStrategy.Always);
        flowDO.setFlowApprove((fo.getOption() != null && fo.getOption().getApproveStrategy() != null) ? fo.getOption().getApproveStrategy() : ChangeApproveStrategy.Enable);
        flowDO.setFlowExecute((fo.getOption() != null && fo.getOption().getExecuteStrategy() != null) ? fo.getOption().getExecuteStrategy() : ChangeExecStrategy.Manual);
        flowDO.setFlowOptions(createFlowOptions(fo.getOption()));
        mergeMsgConfig(flowDO, checkAndCreateMsg(ownerUid, fo));

        this.changeFlowDal.flowMapper().insert(flowDO);

        if (fo.getOption() != null && fo.getOption().getInitScript() != null) {
            this.initInitScript(flowDO, flowDO, fo.getOption().getInitScript());
        } else {
            this.initInitScript(flowDO, flowDO, DmInitScriptStrategy.None);
        }

        GuideCreateChangeFlowVO vo = new GuideCreateChangeFlowVO();
        vo.setFlowId(flowDO.getId());
        vo.setRepoUrl(flowDO.getScmRepoUrl());
        vo.setWebHookUrl(DmConvertUtils.generateCicdWebhookEventUrl(flowDO));
        vo.setWebHookPwd(flowDO.getScmBindWebhookPwd());

        DmScmDef defByType = this.dmScmService.getScmDefByType(flowDO.getRefScmType());
        if (defByType != null) {
            vo.setWebHookHelpUrl(defByType.getHelpUrl());
        }
        return vo;
    }

    private void mergeMsgConfig(DmChangeFlowDO flowDO, DmChangeFlowDO msgDO) {
        if (msgDO == null) {
            flowDO.setEnableMsg(false);
            flowDO.setEventFlowStatus(false);
            flowDO.setEventFlowConfig(false);
            flowDO.setEventChangeLife(false);
            flowDO.setEventChangeNotice(false);
            return;
        }

        flowDO.setRefMsgId(msgDO.getRefMsgId());
        flowDO.setRefMsgType(msgDO.getRefMsgType());
        flowDO.setMsgLanguage(msgDO.getMsgLanguage());
        flowDO.setEnableMsg(msgDO.isEnableMsg());
        flowDO.setEventFlowStatus(msgDO.isEventFlowStatus());
        flowDO.setEventFlowConfig(msgDO.isEventFlowConfig());
        flowDO.setEventChangeLife(msgDO.isEventChangeLife());
        flowDO.setEventChangeNotice(msgDO.isEventChangeNotice());
    }

    private DmChangeFlowDO checkAndCreateMsg(String ownerUid, GuideCreateFO fo) {
        if (fo.getMessenger() == null) {
            return null;
        }

        DmSysMessengerDO messengerDO = this.dmImService.queryImById(ownerUid, fo.getMessenger().getImId());
        if (messengerDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        DmChangeFlowDO msgDO = new DmChangeFlowDO();
        msgDO.setOwnerUid(ownerUid);
        msgDO.setRefMsgId(messengerDO.getId());
        msgDO.setRefMsgType(messengerDO.getImType());
        msgDO.setEnableMsg(true);
        msgDO.setLanguage(fo.getMessenger().getLanguage());
        msgDO.setEventChangeFlowStatus(fo.getMessenger().isEventChangeFlowStatus());
        msgDO.setEventFlowConfig(fo.getMessenger().isEventFlowConfig());
        msgDO.setEventChangeLife(fo.getMessenger().isEventChangeLife());
        msgDO.setEventChangeNotice(fo.getMessenger().isEventChangeNotice());
        return msgDO;
    }

    private DmChangeFlowDO checkAndCreateDevops(String ownerUid, GuidePipelineFO pipeline) {
        if (pipeline == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        DsLevels dsLevels = this.dmDsConfigService.parseLevels(pipeline.getDsLevels());
        DmDsDO dsDO = dsLevels.dsDO();
        if (dsDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }
        DmGitOpsScmDO scmDO = this.dmScmService.queryScmById(ownerUid, pipeline.getRepoScmId());
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        DmChangeFlowDO gitOpsFlowDO = new DmChangeFlowDO();
        gitOpsFlowDO.setOwnerUid(ownerUid);
        gitOpsFlowDO.setRefScmId(pipeline.getRepoScmId());
        gitOpsFlowDO.setRefScmType(scmDO.getScmType());
        gitOpsFlowDO.setScmRepoSpace(pipeline.getRepoSpace());
        gitOpsFlowDO.setScmRepoName(pipeline.getRepoName());
        gitOpsFlowDO.setScmRepoUrl(pipeline.getRepoScmUrl());
        gitOpsFlowDO.setScmRepoBranch(pipeline.getRepoBranch());
        gitOpsFlowDO.setScmRepoEvent(pipeline.getEventType());
        if (StringUtils.isNotBlank(pipeline.getRepoScriptPath())) {
            gitOpsFlowDO.setScmRepoScript(StringUtils.trimStart(pipeline.getRepoScriptPath(), '/'));
        } else {
            gitOpsFlowDO.setScmRepoScript("");
        }

        gitOpsFlowDO.setDsId(dsDO.getId());
        gitOpsFlowDO.setDsType(dsDO.getDataSourceType());
        gitOpsFlowDO.setDsInstance(dsDO.getInstanceId());
        gitOpsFlowDO.setDsDesc(dsDO.getInstanceDesc());
        gitOpsFlowDO.setDsPath("/" + StringUtils.join(pipeline.getDsLevels().toArray(), "/"));

        gitOpsFlowDO.setFlowScmOptions(this.createDevopsOptions(null));
        gitOpsFlowDO.setFlowHashcode(this.toHash(gitOpsFlowDO));
        gitOpsFlowDO.setScmBindWebhookPwd(RandomStrUtils.fixedLenRandomStr(32).toUpperCase());
        gitOpsFlowDO.setEnableWebhook(true);
        gitOpsFlowDO.setCallbackUrl("");
        gitOpsFlowDO.setCallbackMethod("POST");
        gitOpsFlowDO.setEnableCallback(false);
        gitOpsFlowDO.setEnableTrigger(false);
        gitOpsFlowDO.setTriggerToken(RandomStrUtils.fixedLenRandomStr(32).toUpperCase());
        gitOpsFlowDO.setEnable(true);
        return gitOpsFlowDO;
    }

    private void checkDevopsConflict(String ownerUid, DmChangeFlowDO gitOpsFlowDO) {
        if (gitOpsFlowDO == null) {
            return;
        }
        List<DmChangeFlowDO> devops = this.queryEnableDevopsByScmHash(ownerUid, gitOpsFlowDO.getFlowHashcode());
        if (!devops.isEmpty()) {
            Set<Long> flowIds = devops.stream().map(DmChangeFlowDO::getRefFlowId).collect(Collectors.toSet());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CONFLICT_ERROR.name(), flowIds.size()));
        }
    }

    private RsChangeFlowOptionObj createFlowOptions(ChangeFlowOptionFO fo) {
        RsChangeFlowOptionObj options = new RsChangeFlowOptionObj();
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

    private RsChangeFlowScmOptionObj createDevopsOptions(ChangeFlowGitOpsOptionFO fo) {
        return new RsChangeFlowScmOptionObj();
    }

    private void initInitScript(DmChangeFlowDO flowDO, DmChangeFlowDO gitOpsFlowDO, DmInitScriptStrategy initScript) {
        switch (initScript) {
            case Snapshot:
                this.initInitScriptForSnapshot(flowDO, gitOpsFlowDO);
                break;
            case CreateChange:
                this.initInitScriptForChange(flowDO, gitOpsFlowDO);
                break;
            case None:
            default:
                break;
        }
    }

    private void initInitScriptForSnapshot(DmChangeFlowDO flowDO, DmChangeFlowDO gitOpsFlowDO) {
        DmBranchDef branch = this.dmScmService
            .fetchBranchByScmAndRepo(flowDO.getOwnerUid(), gitOpsFlowDO.getRefScmId(), gitOpsFlowDO.getScmRepoName(), gitOpsFlowDO.getScmRepoBranch());
        if (branch == null) {
            return;
        }

        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(flowDO.getOwnerUid());
        changeDO.setRefFlowId(flowDO.getId());
        changeDO.setRefFlowId(gitOpsFlowDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_SNAPSHOT_NAME.name()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ChangeStep.INIT_SNAPSHOT);
        changeDO.setCurrentStatus(ChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(true);
        changeDO.setFlowWalked(new RsChangeFlowWalkedObj());
        this.changeFlowDal.changeMapper().insert(changeDO);
    }

    private void initInitScriptForChange(DmChangeFlowDO flowDO, DmChangeFlowDO gitOpsFlowDO) {
        DmBranchDef branch = this.dmScmService
            .fetchBranchByScmAndRepo(flowDO.getOwnerUid(), gitOpsFlowDO.getRefScmId(), gitOpsFlowDO.getScmRepoName(), gitOpsFlowDO.getScmRepoBranch());
        if (branch == null) {
            return;
        }

        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(flowDO.getOwnerUid());
        changeDO.setRefFlowId(flowDO.getId());
        changeDO.setRefFlowId(gitOpsFlowDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_CHANGE_NAME.name()));
        changeDO.setChangeBranch(branch.getBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ChangeStep.INIT);
        changeDO.setCurrentStatus(ChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(branch.getBranchCommitId());
        changeDO.setLockStatus(false);
        changeDO.setFlowWalked(new RsChangeFlowWalkedObj());
        this.changeFlowDal.changeMapper().insert(changeDO);
    }

    @Override
    public DmChangeFlowDO queryFlowById(String ownerUid, long flowId) {
        return this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
    }

    @Override
    public void updateInfoByFlowId(String ownerUid, long flowId, ChangeFlowUpdateFO fo) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        String flowName = flow.getFlowName();
        List<String> messageList = new ArrayList<>();

        // for PM
        if (StringUtils.isNotBlank(fo.getNewAdminUid()) && !fo.getNewAdminUid().equals(flow.getFlowManagerUid())) {
            UserCacheEntry user = this.objectCacheDao.queryByUid(fo.getNewAdminUid());
            if (user == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_USER_NOT_EXIST_ERROR.name()));
            }
            this.changeFlowDal.flowMapper().updateManagerByOwnerAndId(ownerUid, flowId, fo.getNewAdminUid());

            // message
            UserCacheEntry operatorUser = this.objectCacheDao.queryByUid(fo.getNewAdminUid());
            String operatorMsg = String.format("[%s] %s", DmI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_PM_MESSAGE.name(), operatorMsg);
            messageList.add(textMsg);
        }

        // for name
        if (StringUtils.isNotBlank(fo.getNewName()) && !fo.getNewName().equals(flow.getFlowName())) {
            this.changeFlowDal.flowMapper().updateNameByOwnerAndId(ownerUid, flowId, fo.getNewName());

            // message
            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_RENAME_MESSAGE.name(), fo.getNewName());
            messageList.add(textMsg);
        }

        // for desc
        if (StringUtils.isNotBlank(fo.getNewDesc()) && !fo.getNewDesc().equals(flow.getFlowDesc())) {
            this.changeFlowDal.flowMapper().updateDescByOwnerAndId(ownerUid, flowId, fo.getNewDesc());

            String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_DESC_MESSAGE.name(), fo.getNewDesc());
            messageList.add(textMsg);
        }

        // message
        if (!messageList.isEmpty()) {
            StringBuilder strBuilder = new StringBuilder(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_TITLE_MESSAGE.name(), flowName));
            for (int i = 0; i < messageList.size(); i++) {
                String strBody = messageList.get(i);
                strBuilder.append("\n");
                strBuilder.append((i + 1) + ". " + strBody);
            }
            this.senderService.sendMessage(ownerUid, flowId, ImMessageType.FlowConfig, strBuilder.toString());
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateMessageByFlowId(String ownerUid, long flowId, ChangeFlowImConfigFO fo) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        if (fo.isDelete()) {
            deleteOldMessenger(ownerUid, flowId);
        } else {
            DmSysMessengerDO messengerDO = this.dmImService.queryImById(ownerUid, fo.getImId());
            if (messengerDO == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
            }

            DmChangeFlowDO msgDO = new DmChangeFlowDO();
            msgDO.setOwnerUid(ownerUid);
            msgDO.setRefFlowId(flow.getId());
            msgDO.setRefMsgId(messengerDO.getId());
            msgDO.setRefMsgType(messengerDO.getImType());
            msgDO.setLanguage(fo.getLanguage());
            msgDO.setEnableMsg(true);
            msgDO.setEventChangeFlowStatus(fo.isEventChangeFlowStatus());
            msgDO.setEventFlowConfig(fo.isEventFlowConfig());
            msgDO.setEventChangeLife(fo.isEventChangeLife());
            msgDO.setEventChangeNotice(fo.isEventChangeNotice());

            this.changeFlowDal.flowMapper().updateMessageConfigByOwnerAndId(ownerUid, flowId, msgDO);
        }

        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_IM_MESSAGE.name(), flow.getFlowName());
        this.senderService.sendMessage(ownerUid, flowId, ImMessageType.FlowConfig, textMsg);
    }

    @Override
    public void updateFlowConfigByFlowId(String ownerUid, long flowId, ChangeFlowConfigFO fo) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (fo.getCheckStrategy() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getApproveStrategy() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getExecuteStrategy() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        if (fo.getExecuteStrategy() == ChangeExecStrategy.Auto && fo.getErrorStrategy() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }
        flow.setFlowCheck(fo.getCheckStrategy());
        flow.setFlowApprove(fo.getApproveStrategy());
        flow.setFlowExecute(fo.getExecuteStrategy());
        if (fo.getExecuteStrategy() == ChangeExecStrategy.Auto) {
            flow.getOptions().setTransactional(fo.isTransactional());
            flow.getOptions().setErrorStrategy(fo.getErrorStrategy());
        }
        this.changeFlowDal.flowMapper().updateFlowConfigByOwnerAndId(ownerUid, flowId, flow);
    }

    private void deleteOldMessenger(String ownerUid, long flowId) {
        DmChangeFlowDO msgDO = new DmChangeFlowDO();
        msgDO.setEnableMsg(false);
        msgDO.setEventFlowStatus(false);
        msgDO.setEventFlowConfig(false);
        msgDO.setEventChangeLife(false);
        msgDO.setEventChangeNotice(false);
        this.changeFlowDal.flowMapper().updateMessageConfigByOwnerAndId(ownerUid, flowId, msgDO);
    }

    @Override
    public long createGitOpsFlow(String ownerUid, long flowId, ChangeFlowGitOpsCreateFO fo) {
        DmChangeFlowDO baseFlow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (baseFlow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (baseFlow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        DmChangeFlowDO flowDO = checkAndCreateDevops(ownerUid, fo.getPipeline());
        flowDO.setFlowUid(RandomStrUtils.fixedLenRandomStr(12));
        flowDO.setFlowName(baseFlow.getFlowName());
        flowDO.setFlowDesc(baseFlow.getFlowDesc());
        flowDO.setFlowManagerUid(baseFlow.getFlowManagerUid());
        flowDO.setFlowStatus(ChangeFlowStatus.NORMAL);
        flowDO.setFlowCheck(baseFlow.getFlowCheck());
        flowDO.setFlowApprove(baseFlow.getFlowApprove());
        flowDO.setFlowExecute(baseFlow.getFlowExecute());
        flowDO.setFlowOptions(baseFlow.getFlowOptions());
        flowDO.setRefMsgId(baseFlow.getRefMsgId());
        flowDO.setRefMsgType(baseFlow.getRefMsgType());
        flowDO.setMsgLanguage(baseFlow.getMsgLanguage());
        flowDO.setEnableMsg(baseFlow.isEnableMsg());
        flowDO.setEventFlowStatus(baseFlow.isEventFlowStatus());
        flowDO.setEventFlowConfig(baseFlow.isEventFlowConfig());
        flowDO.setEventChangeLife(baseFlow.isEventChangeLife());
        flowDO.setEventChangeNotice(baseFlow.isEventChangeNotice());
        checkDevopsConflict(ownerUid, flowDO);

        this.changeFlowDal.flowMapper().insert(flowDO);
        DmInitScriptStrategy initScript = DmInitScriptStrategy.None;
        if (fo.getOption() != null && fo.getOption().getInitScript() != null) {
            initScript = fo.getOption().getInitScript();
        }
        this.initInitScript(flowDO, flowDO, initScript);

        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_NEW_DEVOPS_MESSAGE.name(), flowDO.getFlowName(), toString(flowDO));
        this.senderService.sendMessage(ownerUid, flowDO.getId(), ImMessageType.FlowConfig, textMsg);
        return flowDO.getId();
    }

    @Override
    public void deleteGitOpsFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        int useCount = this.changeFlowDal.changeMapper().countUnfinishedChangeByFlowId(ownerUid, flowId);
        if (useCount > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CHANGE_IN_INUSE_ERROR.name(), useCount));
        }

        int res = this.changeFlowDal.flowMapper().deleteByOwnerAndId(ownerUid, flowId);

        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CONFIG_DEL_DEVOPS_MESSAGE.name(), flow.getFlowName(), toString(flow));
        this.senderService.sendMessage(ownerUid, flowId, ImMessageType.FlowConfig, textMsg);
    }

    @Override
    public void enableGitOpsFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        checkDevopsConflict(ownerUid, flow);
        this.changeFlowDal.flowMapper().enableFlowByOwnerAndId(ownerUid, flowId);
    }

    @Override
    public void disableGitOpsFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        this.changeFlowDal.flowMapper().disableFlowByOwnerAndId(ownerUid, flowId);
    }

    @Override
    public void configGitOpsWebhook(String ownerUid, long flowId, boolean enable) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flow.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        if (enable) {
            this.changeFlowDal.flowMapper().enableWebHookByOwnerAndId(ownerUid, flowId);
        } else {
            this.changeFlowDal.flowMapper().disableWebHookByOwnerAndId(ownerUid, flowId);
        }
    }

    @Override
    public void configGitOpsTrigger(String ownerUid, long flowId, boolean enable) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flow.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        if (enable) {
            this.changeFlowDal.flowMapper().enableTriggerByOwnerAndId(ownerUid, flowId);
        } else {
            this.changeFlowDal.flowMapper().disableTriggerByOwnerAndId(ownerUid, flowId);
        }
    }

    @Override
    public void configGitOpsCallback(String ownerUid, long flowId, ChangeFlowCallbackFO fo) {
        boolean methodOk = StringUtils.equalsIgnoreCase(fo.getMethod(), "post") || StringUtils.equalsIgnoreCase(fo.getMethod(), "get");
        boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getUrl(), "https://");
        if (!methodOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_CONFIG_METHOD_NOT_SUPPORT.name(), fo.getMethod()));
        }
        if (!urlOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CALLBACK_CONFIG_URL_NOT_SUPPORT.name()));
        }

        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flow.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }

        this.changeFlowDal.flowMapper().configCallBackByOwnerAndId(ownerUid, flowId, fo.isEnable(), fo.getMethod(), fo.getUrl());
    }

    @Override
    public void archiveFlow(String ownerUid, long flowId, String operatorUid) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        switch (flow.getChangeFlowStatus()) {
            case DELETE:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_DELETE_UN_SUPPORT_ARCHIVE_ERROR.name()));
            case ARCHIVE:
                return;
            case NORMAL:
                break;
            default:
                throw new UnsupportedOperationException();
        }

        int usingCount = this.changeFlowDal.changeMapper().countUnfinishedChangeByFlowId(ownerUid, flowId);
        if (usingCount > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_ARCHIVE_CHANGE_ON_END_ERROR.name(), usingCount));
        }

        // send message
        UserCacheEntry operatorUser = this.objectCacheDao.queryByUid(operatorUid);
        String operatorMsg = String.format("[%s] %s", DmI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
        String textMsg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_STATUS_ARCHIVE_MESSAGE.name(), operatorMsg, flow.getFlowName());
        this.senderService.sendMessage(ownerUid, flowId, ImMessageType.ChangeFlowStatus, textMsg);

        //
        this.changeFlowDal.flowMapper().disableFlowByOwnerAndId(ownerUid, flowId);
        this.changeFlowDal.flowMapper().updateStatusByOwnerAndId(ownerUid, flowId, ChangeFlowStatus.ARCHIVE);
    }

    @Override
    public void recoverFlowTo(String ownerUid, long flowId, ChangeFlowStatus toStatus) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (toStatus == ChangeFlowStatus.DELETE) {
            throw new UnsupportedOperationException();
        }

        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            this.changeFlowDal.flowMapper().updateStatusByOwnerAndId(ownerUid, flowId, toStatus);
        }

        if (toStatus == ChangeFlowStatus.NORMAL) {
            this.changeFlowDal.flowMapper().enableFlowByOwnerAndId(ownerUid, flowId);
        }
    }

    @Override
    public void deleteFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flow == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        switch (flow.getChangeFlowStatus()) {
            case NORMAL:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NORMAL_UN_SUPPORT_DELETE_ERROR.name()));
            case ARCHIVE:
                break;
            case DELETE:
                return;
            default:
                throw new UnsupportedOperationException();
        }

        this.changeFlowDal.flowMapper().deleteByOwnerAndId(ownerUid, flowId);
    }

    @Override
    public File getCicdWorkspace(String ownerUid, long flowId) {
        DmSysUserConfDO currentConfig = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, RootUserConfig.Fields.defaultCicdWorkspace);
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
    public File getCicdTempSpace(String ownerUid, long flowId) {
        DmSysUserConfDO currentConfig = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, RootUserConfig.Fields.defaultCicdTempSpace);
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
