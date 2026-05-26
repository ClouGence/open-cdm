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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.UserCacheEntry;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.project.ImMessageType;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.component.project.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.project.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.*;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecTaskDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import com.clougence.clouddm.console.web.dal.model.queryobj.DmChangeQueryObj;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecLogFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeExecTaskListFO;
import com.clougence.clouddm.console.web.model.fo.project.ProjectChangeListFO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeBodyItemVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.project.ProjectChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmPageVO;
import com.clougence.clouddm.console.web.service.project.domain.CreateSuggest;
import com.clougence.clouddm.console.web.service.project.domain.CreateSuggestType;
import com.clougence.clouddm.console.web.service.project.domain.Item;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RdpPageUtil;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.format.WellKnowFormat;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmChangeServiceImpl implements DmChangeService {

    @Resource
    private RdpDataSourceMapper       dataSourceMapper;
    @Resource
    private DmProjectDevopsMapper     dmProjectDevopsMapper;
    @Resource
    private DmProjectChangeMapper     dmProjectChangeMapper;
    @Resource
    private DmProjectChangeItemMapper dmProjectChangeItemMapper;
    @Resource
    private DmProjectDevopsItemMapper dmProjectDevopsItemMapper;
    @Resource
    private DmProjectMapper           dmProjectMapper;
    @Resource
    private DmApprovalMapper          approvalMapper;
    @Resource
    private DmScmService              dmScmService;
    @Resource
    private ImSenderService           imSenderService;
    @Resource
    private BizResOwnerCacheService   ownerCacheService;
    @Resource
    private AutoExecService           autoExecService;
    @Resource
    private DmBizLogMapper            dmBizLogMapper;
    @Resource
    private DmAutoExecTaskMapper      dmSqlTaskMapper;
    @Resource
    private DmAutoExecJobMapper       dmAutoExecJobMapper;
    @Resource
    private ApprovalFlowService       approvalFlowService;

    @Override
    public IPage<ProjectChangeVO> queryChangeByProjectAndQuery(String ownerUid, long projectId, ProjectChangeListFO fo) {
        Page<?> page = RdpPageUtil.startPage(fo.getPage());

        // page
        DmChangeQueryObj queryParams = DmChangeQueryObj.builder()//
            .ownerUid(ownerUid)
            .projectId(projectId)
            .searchKeywords(StringUtils.isBlank(fo.getSearchKeywords()) ? null : fo.getSearchKeywords())
            .build();

        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        IPage<DmProjectChangeDO> pageData = this.dmProjectChangeMapper.listChangeByConditionAndPage(page, queryParams);
        List<DmProjectChangeDO> records = pageData.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }
        Map<Long, DmProjectDevopsDO> devopsMap;
        Map<Long, RdpDataSourceDO> dsMap;
        Map<Long, DmProjectScmDO> scmMap;

        // devopsMap
        Set<Long> devopsIds = records.stream().map(DmProjectChangeDO::getRefDevopsId).collect(Collectors.toSet());
        if (!devopsIds.isEmpty()) {
            List<DmProjectDevopsDO> devops = dmProjectDevopsMapper.queryByIds(ownerUid, devopsIds);
            devopsMap = new HashMap<>();
            devops.forEach(d -> devopsMap.put(d.getId(), d));

            dsMap = new HashMap<>();
            Set<Long> dsIds = devops.stream().map(DmProjectDevopsDO::getDsId).collect(Collectors.toSet());
            List<RdpDataSourceDO> dsList = dataSourceMapper.listByIdsIncludeDeleted(new ArrayList<>(dsIds));
            dsList.forEach(d -> dsMap.put(d.getId(), d));

            scmMap = new HashMap<>();
            Set<Long> scmIds = devops.stream().map(DmProjectDevopsDO::getRefScmId).collect(Collectors.toSet());
            List<DmProjectScmDO> scmList = dmScmService.queryScmByIds(ownerUid, scmIds);
            scmList.forEach(d -> scmMap.put(d.getId(), d));
        } else {
            devopsMap = Collections.emptyMap();
            dsMap = Collections.emptyMap();
            scmMap = Collections.emptyMap();
        }

        // convert
        List<ProjectChangeVO> vos = records.stream().map(obj -> {
            return DmConvertUtils.convertToProjectChangeVO(projectDO, obj, devopsMap, dsMap, scmMap);
        }).collect(Collectors.toList());

        IPage<ProjectChangeVO> results = new Page<>();
        results.setRecords(vos);
        results.setCurrent(pageData.getCurrent());
        results.setSize(pageData.getSize());
        results.setPages(pageData.getPages());
        results.setTotal(pageData.getTotal());
        return results;
    }

    @Override
    public DmProjectChangeDO queryChangeById(String ownerUid, long changeId) {
        return this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
    }

    @Override
    public ProjectChangeBodyVO fetchChangeBodyByChangeId(String ownerUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);

        // current content, map by name
        List<DmProjectDevopsItemDO> versionedList = this.dmProjectDevopsItemMapper.queryItemByDevopsId(change.getOwnerUid(), change.getRefDevopsId());
        List<DmProjectChangeItemDO> changeList = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.SQL);

        Map<String, DmProjectDevopsItemDO> versionedByName = new HashMap<>();
        Map<String, DmProjectChangeItemDO> changeByName = new HashMap<>();
        for (DmProjectDevopsItemDO item : versionedList) {
            versionedByName.put(item.getContentName(), item);
        }
        for (DmProjectChangeItemDO item : changeList) {
            changeByName.put(item.getContentName(), item);
        }

        // all item names, keep order.
        List<Item> allItem = new ArrayList<>();
        versionedList.forEach(i -> allItem.add(new Item(i)));
        changeList.forEach(i -> allItem.add(new Item(i)));
        Set<String> itemNames = new LinkedHashSet<>();
        itemNames.addAll(allItem.stream().sorted(Comparator.comparingInt(Item::getIndex)).map(Item::getName).collect(Collectors.toList()));

        //
        List<ProjectChangeBodyItemVO> bodyItemList = itemNames.stream().map(name -> {
            ProjectChangeBodyItemVO vo = new ProjectChangeBodyItemVO();
            vo.setContentName(name);

            if (versionedByName.containsKey(name)) {
                vo.setOldBody(versionedByName.get(name).getContent());
            }
            if (changeByName.containsKey(name)) {
                vo.setNewBody(changeByName.get(name).getContent());
            }

            return StringUtils.equals(vo.getNewBody(), vo.getOldBody()) ? null : vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // find diff result
        List<DmProjectChangeItemDO> diffChange = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.REVIEW);
        String sqlChange = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();
        ProjectChangeBodyVO vo = new ProjectChangeBodyVO();
        vo.setChangeBody(sqlChange);
        vo.setItemList(bodyItemList);
        return vo;
    }

    @Override
    public List<DmProjectChangeItemDO> fetchChangeCheckByChangeId(String ownerUid, long changeId) {
        return this.dmProjectChangeItemMapper.queryChangeItemByChangeId(ownerUid, changeId, DmChangeItemType.CHECKS);
    }

    @Override
    public ChangeTicketInfoResult fetchChangeApprovalByChangeId(String ownerUid, long changeId) {
        List<DmProjectChangeItemDO> list = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(ownerUid, changeId, DmChangeItemType.TICKET);
        DmProjectChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_STEP_NO_BODY_ERROR.name()));
        }
        ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
        if (ticketInfo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_STEP_NO_BODY_ERROR.name()));
        }

        DmApprovalDO ticketDO = this.approvalMapper.queryById(ticketInfo.getTicketId());
        if (ticketDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_NOT_EXIST_ERROR.name()));
        }

        ChangeTicketInfoResult result = new ChangeTicketInfoResult();
        result.setTicketId(ticketInfo.getTicketId());
        result.setTicketBizId(ticketInfo.getTicketBizId());
        result.setTicketBizType(ticketInfo.getTicketBizType());
        result.setApprovalType(ticketInfo.getApprovalType());
        result.setTicketStatus(ticketDO.getTicketStatus());
        return result;
    }

    @Override
    public ChangeExecuteInfo fetchChangeExecuteByChangeId(String ownerUid, long changeId) {
        List<DmProjectChangeItemDO> list = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(ownerUid, changeId, DmChangeItemType.EXECUTE);
        DmProjectChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item == null) {
            return null;
        }
        return JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
    }

    @Override
    public void skipCheck(String ownerUid, String userUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ProjectChangeStep.CHECK) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NEED_CHECK_STEP_ERROR.name()));
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        UserCacheEntry operatorUser = this.ownerCacheService.queryByUid(userUid);
        String operatorMsg = String.format("[%s] %s", DmI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_SKIP_CHECK_STEP_ERROR.name(), locale, change.getChangeName(), operatorMsg);
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeLife, message);
        this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.APPROVAL, message);
        this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion() + 1, ProjectChangeStatus.READY, message);

    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void confirmExec(String ownerUid, String userUid, long changeId, DmAutoExecConfigFO fo) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ProjectChangeStep.EXECUTE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NEED_EXECUTE_STEP_ERROR.name()));
        }
        if (change.getCurrentStatus() != ProjectChangeStatus.OPEN) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NEED_EXECUTE_OPEN_ERROR.name()));
        }
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(ownerUid, change.getRefProjectId());
        if (projectDO.getFlowExecute() != DmChangeExecStrategy.Manual) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_IS_NOT_MANUAL_ERROR.name()));
        }

        ChangeExecuteInfo config = new ChangeExecuteInfo();
        config.setExecType(fo.getAutoExecType());
        config.setTransactional(fo.isEnableTransactional());
        config.setErrorStrategy(fo.getErrorStrategy());
        config.setRetryWaitTime(fo.getRetryWaitTime());
        config.setRetryCount(fo.getRetryCount());
        config.setExecTime(fo.getExecTime());
        config.setSnapshot(fo.isSnapshot());

        DmProjectChangeItemDO itemDO = new DmProjectChangeItemDO();
        itemDO.setOwnerUid(change.getOwnerUid());
        itemDO.setRefProjectId(change.getRefProjectId());
        itemDO.setRefChangeId(change.getId());
        itemDO.setChangeItemType(DmChangeItemType.EXECUTE);
        itemDO.setContent(JsonUtils.toJson(config));
        itemDO.setContentIndex(1);
        itemDO.setContentName("exec");
        this.dmProjectChangeItemMapper.deleteByChangeItemType(change.getOwnerUid(), change.getId(), DmChangeItemType.EXECUTE);
        this.dmProjectChangeItemMapper.insert(itemDO);
        this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.READY, "");
    }

    private static void checkRunStatus(DmProjectChangeDO change) {
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ProjectChangeStep.EXECUTE && change.getCurrentStep() != ProjectChangeStep.FINISH) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NEED_EXECUTE_STEP_ERROR.name()));
        }
    }

    @Override
    public DmAutoExecJobVO queryExecJobInfo(String ownerUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        return this.autoExecService.queryAutoExecJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true);
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String ownerUid, ProjectChangeExecTaskListFO fo) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, fo.getChangeId());
        checkRunStatus(change);

        return this.autoExecService.queryAutoExecTaskList(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true, fo.getTaskStatus(), fo.getPage());
    }

    @Override
    public List<DmBizLogVO> queryExecLog(String ownerUid, ProjectChangeExecLogFO fo) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, fo.getChangeId());
        checkRunStatus(change);

        DmAutoExecJobDO jobDO = checkJob(ownerUid, fo.getJobId());
        List<DmBizLogDO> dmBizLogDOS;
        if (fo.getBizType() == DmLogDependBizType.AUTO_EXEC_JOB) {
            if (jobDO.getBizId() == null) {
                return Collections.emptyList();
            } else {
                dmBizLogDOS = this.dmBizLogMapper.queryListByBizId(jobDO.getBizId());
            }
        } else {
            if (fo.getTaskId() == null) {
                return Collections.emptyList();
            } else {
                DmAutoExecTaskDO execTaskDO = dmSqlTaskMapper.selectById(fo.getTaskId());
                dmBizLogDOS = this.dmBizLogMapper.queryListByBizId(execTaskDO.getBizId());
            }
        }

        return dmBizLogDOS.stream().map((dmBizLogDO -> {
            DmBizLogVO vo = new DmBizLogVO();
            vo.setContent(dmBizLogDO.getContent());
            vo.setId(dmBizLogDO.getId());
            vo.setLogLevel(dmBizLogDO.getLogLevel());
            vo.setDependOnBizType(dmBizLogDO.getDependOnBizType());
            vo.setTime(DateFormatType.s_yyyyMMdd_HHmmss.format(dmBizLogDO.getGmtCreate()));
            return vo;
        })).collect(Collectors.toList());
    }

    @Override
    public void pauseExecJob(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.stopJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void startExecJob(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.retryJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void retryExecJob(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.retryJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void abortExecJob(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.endJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipExecTask(String ownerUid, String curUid, long changeId, long taskId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.skipTask(String.valueOf(changeId), SQLJobBizType.CHANGE, taskId, curUid);
    }

    @Override
    public void retryChange(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        if (change == null || change.isLockStatus()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStatus() == ProjectChangeStatus.READY) {
            return;
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        switch (change.getCurrentStep()) {
            case INIT:
            case CHECK:
                this.retryChangeAtInitOrCheck(locale, change, false);
                return;
            case APPROVAL:
                this.retryChangeAtApproval(locale, change, ownerUid, curUid, false);
                return;
            case EXECUTE:
                this.retryChangeAtExecute(locale, change, ownerUid, curUid);
                return;
            case FINISH:
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    @Override
    public void restartChange(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        if (change == null || change.isLockStatus()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() == ProjectChangeStep.INIT && change.getCurrentStatus() == ProjectChangeStatus.READY) {
            return;
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        switch (change.getCurrentStep()) {
            case INIT:
            case CHECK:
                this.retryChangeAtInitOrCheck(locale, change, true);
                this.dmProjectChangeItemMapper.deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case APPROVAL:
                this.retryChangeAtApproval(locale, change, ownerUid, curUid, true);
                this.dmProjectChangeItemMapper.deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case EXECUTE:
                this.restartChangeAtExecute(locale, change, ownerUid, curUid);
                this.dmProjectChangeItemMapper.deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case FINISH:
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void retryChangeAtInitOrCheck(Locale locale, DmProjectChangeDO change, boolean isRestart) {
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());

        if (isRestart) {
            int res1 = this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.INIT, msg1);
            int res2 = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion() + 1, ProjectChangeStatus.READY, msg1);
        } else {
            int res1 = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.READY, msg1);
        }

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);
    }

    private void retryChangeAtApproval(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid, boolean isRestart) {
        // close ticket
        if (change.getCurrentStatus() == ProjectChangeStatus.WAIT) {
            List<DmProjectChangeItemDO> list = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(ownerUid, change.getId(), DmChangeItemType.TICKET);
            DmProjectChangeItemDO item = list.isEmpty() ? null : list.get(0);
            if (item != null) {
                ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
                if (ticketInfo != null) {
                    String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REAPPROVAL_AT_CONSOLE_NOTICE.name());
                    this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, ownerUid, curUid);
                    change = this.dmProjectChangeMapper.queryChangeById(ownerUid, change.getId());
                }
            }
        }

        if (isRestart) {
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());
            int res1 = this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.INIT, msg1);
            int res2 = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion() + 1, ProjectChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);
        } else {
            if (change.getCurrentStatus() == ProjectChangeStatus.READY) {
                return;
            }

            // message
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REAPPROVAL_AT_CONSOLE_MESSAGE.name());
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REAPPROVAL_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);
        }
    }

    private void retryChangeAtExecute(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid) {
        switch (change.getCurrentStatus()) {
            case OPEN:
            case READY:
                return;
            case WAIT:
            case FINISH:
            case CLOSED:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UNSUPPORT_STATUS_MESSAGE.name()));
            case FAILED:
            default:
                break;
        }

        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REEXE_AT_CONSOLE_MESSAGE.name());
        List<DmProjectChangeItemDO> items = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), DmChangeItemType.EXECUTE);
        DmProjectChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);

        if (item == null || StringUtils.isEmpty(item.getContent())) {
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.READY, msg1);
        } else {
            this.autoExecService.retryJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, curUid);
            int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.WAIT, msg1);
        }

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REEXE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);
    }

    private void restartChangeAtExecute(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid) {
        if (change.getCurrentStatus() == ProjectChangeStatus.OPEN) {
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());
            int res1 = this.dmProjectChangeMapper.updateStepTo(change.getId(), change.getVersion(), ProjectChangeStep.INIT, msg1);
            int res2 = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion() + 1, ProjectChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UNSUPPORT_STATUS_MESSAGE.name()));
        }
    }

    @Override
    public void closeChange(String ownerUid, String curUid, long changeId) {
        DmProjectChangeDO change = this.dmProjectChangeMapper.queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_NOT_EXIST_ERROR.name()));
        }

        String language = this.imSenderService.getProjectLanguage(change.getOwnerUid(), change.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);

        switch (change.getCurrentStep()) {
            case INIT:
            case CHECK:
                this.closeChangeAtInitOrCheck(locale, change);
                return;
            case APPROVAL:
                this.closeChangeAtApproval(locale, change, ownerUid, curUid);
                return;
            case EXECUTE:
                this.closeChangeAtExecute(locale, change, ownerUid, curUid);
                return;
            case INIT_SNAPSHOT:
                this.closeChangeAtSnapshot(locale, change, ownerUid, curUid);
                return;
            case FINISH:
                return;
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void closeChangeAtInitOrCheck(Locale locale, DmProjectChangeDO change) {
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);

        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtApproval(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid) {
        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // close ticket
        List<DmProjectChangeItemDO> list = this.dmProjectChangeItemMapper.queryChangeItemByChangeId(ownerUid, change.getId(), DmChangeItemType.TICKET);
        DmProjectChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item != null) {
            ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
            if (ticketInfo != null && !this.approvalFlowService.isFinish(ticketInfo.getTicketId())) {
                this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, ownerUid, curUid);
            }
        }

        // send message and update status
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);

        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtExecute(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid) {
        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // close auto exec
        DmAutoExecJobVO jobVO = this.autoExecService.queryAutoExecJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true);
        if (jobVO != null) {
            if (jobVO.getStatus() == AutoExecJobStatus.FINISH || jobVO.getStatus() == AutoExecJobStatus.TERMINATION) {
                // is end status
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_EXECUTE_NOT_FINISH.name()));
            }
        }

        // send message and update status
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);

        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtSnapshot(Locale locale, DmProjectChangeDO change, String ownerUid, String curUid) {
        if (change.getCurrentStatus() == ProjectChangeStatus.FINISH || change.getCurrentStatus() == ProjectChangeStatus.CLOSED) {
            return;
        }

        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // send message and update status
        int res = this.dmProjectChangeMapper.updateStatusTo(change.getId(), change.getVersion(), ProjectChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.imSenderService.sendMessage(change.getOwnerUid(), change.getRefProjectId(), ImMessageType.ChangeNotice, msg2);

        this.dmProjectChangeMapper.lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private DmAutoExecJobDO checkJob(String ownerUid, long jobId) {
        DmAutoExecJobDO jobDO = this.dmAutoExecJobMapper.selectById(jobId);
        if (jobDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_EXISTS_ERROR_MESSAGE.name()));
        }
        if (!jobDO.getPrimaryUid().equals(ownerUid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_BELONG_CURRENT_TEAM.name()));
        }
        return jobDO;
    }

    @Override
    public void verifyDevops(String ownerUid, long projectId, long devopsId) {
        DmProjectDO projectDO = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (projectDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        if (projectDO.getProjectStatus() != ProjectStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_PROJECT_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }

        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        if (devopsDO == null || devopsDO.isDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NOT_EXIST_ERROR.name()));
        }
        if (!devopsDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }
        if (!devopsDO.isEnableWebhook()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_WEBHOOK_NOT_ENABLE_MESSAGE.name()));
        }

        DmProjectScmDO scmDO = this.dmScmService.queryScmById(ownerUid, devopsDO.getRefScmId());
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }
    }

    @Override
    public CreateSuggest createChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId) {
        DmProjectDevopsDO devopsDO = dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);
        List<DmProjectChangeDO> changeList = this.dmProjectChangeMapper.queryUnLockChange(devopsDO.getRefProjectId(), devopsDO.getId());
        if (CollectionUtils.isNotEmpty(changeList)) {
            for (DmProjectChangeDO changeDO : changeList) {
                switch (changeDO.getCurrentStep()) {
                    case INIT_SNAPSHOT: {
                        CreateSuggest suggest = new CreateSuggest();
                        suggest.setChange(changeDO);
                        suggest.setSuggestType(CreateSuggestType.Later);
                        return suggest;
                    }
                    case INIT:
                    case CHECK:
                    case APPROVAL: {
                        CreateSuggest suggest = new CreateSuggest();
                        suggest.setChange(changeDO);
                        suggest.setSuggestType(CreateSuggestType.Restart);
                        return suggest;
                    }
                    case EXECUTE: {
                        if (changeDO.getCurrentStatus() == ProjectChangeStatus.OPEN) {
                            CreateSuggest suggest = new CreateSuggest();
                            suggest.setChange(changeDO);
                            suggest.setSuggestType(CreateSuggestType.Restart);
                            return suggest;
                        } else {
                            CreateSuggest suggest = new CreateSuggest();
                            suggest.setChange(changeDO);
                            suggest.setSuggestType(CreateSuggestType.Later);
                            return suggest;
                        }
                    }
                    case FINISH: {
                        CreateSuggest suggest = new CreateSuggest();
                        suggest.setChange(changeDO);
                        suggest.setSuggestType(CreateSuggestType.Later);
                        return suggest;
                    }
                }
            }
        }

        CreateSuggest suggest = new CreateSuggest();
        suggest.setSuggestType(CreateSuggestType.Create);
        return suggest;
    }

    @Override
    public ResWebData<String> triggerChangeSuggest(String ownerUid, long projectId, long devopsId, String commitId) {
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(ownerUid, devopsId);

        // create
        try {
            CreateSuggest suggest = this.createChangeSuggest(ownerUid, projectId, devopsId, commitId);
            switch (suggest.getSuggestType()) {
                case Create:
                    doCreateChange(ownerUid, devopsDO, commitId);
                    return ResWebDataUtils.buildSuccess("change created.");
                case Restart:
                    doRestartChange(suggest);
                    return ResWebDataUtils.buildSuccess("change restarted.");
                case Later:
                    doLaterChange(ownerUid, devopsDO, commitId, suggest);
                    return ResWebDataUtils.buildError("change later.");
                default: {
                    return ResWebDataUtils.buildError("InnerError: Unknown SuggestType.");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(e.getMessage());
        }
    }

    private void doCreateChange(String owner, DmProjectDevopsDO devopsDO, String commitId) {
        DmProjectChangeDO changeDO = new DmProjectChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setRefProjectId(devopsDO.getRefProjectId());
        changeDO.setRefDevopsId(devopsDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_INIT_REPO_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(devopsDO.getScmRepoBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ProjectChangeStep.INIT);
        changeDO.setCurrentStatus(ProjectChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(commitId);
        changeDO.setLockStatus(false);
        changeDO.setFlowWalked(new DmProjectChangeFlowWalked());
        this.dmProjectChangeMapper.insert(changeDO);
    }

    private void doRestartChange(CreateSuggest suggest) {
        DmProjectChangeDO changeDO = suggest.getChange();

        // language
        String language = this.imSenderService.getProjectLanguage(changeDO.getOwnerUid(), changeDO.getRefProjectId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_RESTART_BY_REPO.name(), locale, changeDO.getChangeName());
        try {
            this.imSenderService.sendMessage(changeDO.getOwnerUid(), changeDO.getRefProjectId(), ImMessageType.ChangeLife, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        this.restartChange(changeDO.getOwnerUid(), changeDO.getOwnerUid(), changeDO.getId());
    }

    private void doLaterChange(String owner, DmProjectDevopsDO devopsDO, String commitId, CreateSuggest suggest) {
        DmProjectChangeDO changeDO = new DmProjectChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setRefProjectId(devopsDO.getRefProjectId());
        changeDO.setRefDevopsId(devopsDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_INIT_REPO_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(devopsDO.getScmRepoBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ProjectChangeStep.INIT);
        changeDO.setCurrentStatus(ProjectChangeStatus.FAILED);
        changeDO.setRemark(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_CHANGE_WAIT_OTHER_RUNNING_MESSAGE.name(), suggest.getChange().getChangeName()));
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(commitId);
        changeDO.setLockStatus(true);
        changeDO.setFlowWalked(new DmProjectChangeFlowWalked());
        this.dmProjectChangeMapper.insert(changeDO);
    }
}
