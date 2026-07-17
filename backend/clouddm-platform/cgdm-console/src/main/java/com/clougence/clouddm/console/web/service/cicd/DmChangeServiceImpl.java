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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeExecuteInfo;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeExecLogFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeExecTaskListFO;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeListFO;
import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeBodyItemVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeBodyVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecJobVO;
import com.clougence.clouddm.console.web.model.vo.ticket.DmAutoExecTaskVO;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggestType;
import com.clougence.clouddm.console.web.service.cicd.domain.Item;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.*;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.util.PageUtils;
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
    private ChangeFlowDal       changeFlowDal;
    @Resource
    private MonitorDal          monitorDal;
    @Resource
    private ExecutionDal        executionDal;
    @Resource
    private DataSourceDal       dsDal;
    @Resource
    private ApprovalDal         approvalDal;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmScmService        dmScmService;
    @Resource
    private ImSenderService     senderService;
    @Resource
    private AutoExecService     autoExecService;
    @Resource
    private ApprovalFlowService approvalFlowService;

    @Override
    public DmPageVO<ChangeVO> queryChangeByFlowAndQuery(String ownerUid, long flowId, ChangeListFO fo) {
        Page<?> page = PageUtils.startPage(fo.getPage());

        // page
        ArgChangeQueryObj queryParams = ArgChangeQueryObj.builder()//
            .ownerUid(ownerUid)
            .flowId(flowId)
            .searchKeywords(StringUtils.isBlank(fo.getSearchKeywords()) ? null : fo.getSearchKeywords())
            .build();

        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        IPage<DmChangeDO> pageData = this.changeFlowDal.changeMapper().listChangeByConditionAndPage(page, queryParams);
        DmPageVO<ChangeVO> results = new DmPageVO<>(pageData);
        List<DmChangeDO> records = pageData.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return results;
        }
        Map<Long, DmChangeFlowDO> devopsMap;
        Map<Long, DmDsDO> dsMap;
        Map<Long, DmGitOpsScmDO> scmMap;

        // devopsMap
        Set<Long> devopsIds = records.stream().map(DmChangeDO::getRefFlowId).collect(Collectors.toSet());
        if (!devopsIds.isEmpty()) {
            List<DmChangeFlowDO> devops = changeFlowDal.flowMapper().queryByIds(ownerUid, devopsIds);
            devopsMap = new HashMap<>();
            devops.forEach(d -> devopsMap.put(d.getId(), d));

            dsMap = new HashMap<>();
            Set<Long> dsIds = devops.stream().map(DmChangeFlowDO::getDsId).collect(Collectors.toSet());
            List<DmDsDO> dsList = dsDal.dsMapper().listByIdsIncludeDeleted(new ArrayList<>(dsIds));
            dsList.forEach(d -> dsMap.put(d.getId(), d));

            scmMap = new HashMap<>();
            Set<Long> scmIds = devops.stream().map(DmChangeFlowDO::getRefScmId).collect(Collectors.toSet());
            List<DmGitOpsScmDO> scmList = dmScmService.queryScmByIds(ownerUid, scmIds);
            scmList.forEach(d -> scmMap.put(d.getId(), d));
        } else {
            devopsMap = Collections.emptyMap();
            dsMap = Collections.emptyMap();
            scmMap = Collections.emptyMap();
        }

        // convert
        List<ChangeVO> vos = records.stream().map(obj -> {
            return DmConvertUtils.convertToChangeVO(flowDO, obj, devopsMap, dsMap, scmMap);
        }).collect(Collectors.toList());

        results.setRecords(vos);
        return results;
    }

    @Override
    public DmChangeDO queryChangeById(String ownerUid, long changeId) {
        return this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
    }

    @Override
    public ChangeBodyVO fetchChangeBodyByChangeId(String ownerUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);

        // current content, map by name
        List<DmChangeFlowItemDO> versionedList = this.changeFlowDal.flowItemMapper().queryItemByFlowId(change.getOwnerUid(), change.getRefFlowId());
        List<DmChangeItemDO> changeList = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.SQL);

        Map<String, DmChangeFlowItemDO> versionedByName = new HashMap<>();
        Map<String, DmChangeItemDO> changeByName = new HashMap<>();
        for (DmChangeFlowItemDO item : versionedList) {
            versionedByName.put(item.getContentName(), item);
        }
        for (DmChangeItemDO item : changeList) {
            changeByName.put(item.getContentName(), item);
        }

        // all item names, keep order.
        List<Item> allItem = new ArrayList<>();
        versionedList.forEach(i -> allItem.add(new Item(i)));
        changeList.forEach(i -> allItem.add(new Item(i)));
        Set<String> itemNames = new LinkedHashSet<>();
        itemNames.addAll(allItem.stream().sorted(Comparator.comparingInt(Item::getIndex)).map(Item::getName).collect(Collectors.toList()));

        //
        List<ChangeBodyItemVO> bodyItemList = itemNames.stream().map(name -> {
            ChangeBodyItemVO vo = new ChangeBodyItemVO();
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
        List<DmChangeItemDO> diffChange = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW);
        String sqlChange = diffChange.isEmpty() ? "" : diffChange.get(0).getContent();
        ChangeBodyVO vo = new ChangeBodyVO();
        vo.setChangeBody(sqlChange);
        vo.setItemList(bodyItemList);
        return vo;
    }

    @Override
    public List<DmChangeItemDO> fetchChangeCheckByChangeId(String ownerUid, long changeId) {
        return this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(ownerUid, changeId, ChangeItemType.CHECKS);
    }

    @Override
    public ChangeTicketInfoResult fetchChangeApprovalByChangeId(String ownerUid, long changeId) {
        List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(ownerUid, changeId, ChangeItemType.TICKET);
        DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_STEP_NO_BODY_ERROR.name()));
        }
        ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
        if (ticketInfo == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_STEP_NO_BODY_ERROR.name()));
        }

        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(ticketInfo.getTicketId());
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
        List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(ownerUid, changeId, ChangeItemType.EXECUTE);
        DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item == null) {
            return null;
        }
        return JsonUtils.toObj(item.getContent(), ChangeExecuteInfo.class);
    }

    @Override
    public void skipCheck(String ownerUid, String userUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ChangeStep.CHECK) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NEED_CHECK_STEP_ERROR.name()));
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        UserCacheEntry operatorUser = this.objectCacheDao.queryByUid(userUid);
        String operatorMsg = String.format("[%s] %s", DmI18nUtils.getMessage(operatorUser.getRoleName()), operatorUser.getUserName());
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_SKIP_CHECK_STEP_ERROR.name(), locale, change.getChangeName(), operatorMsg);
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeLife, message);
        this.changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.APPROVAL, message);
        this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion() + 1, ChangeStatus.READY, message);

    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void confirmExec(String ownerUid, String userUid, long changeId, DmAutoExecConfigFO fo) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ChangeStep.EXECUTE) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NEED_EXECUTE_STEP_ERROR.name()));
        }
        if (change.getCurrentStatus() != ChangeStatus.OPEN) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NEED_EXECUTE_OPEN_ERROR.name()));
        }
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, change.getRefFlowId());
        if (flowDO.getFlowExecute() != ChangeExecStrategy.Manual) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_IS_NOT_MANUAL_ERROR.name()));
        }

        ChangeExecuteInfo config = new ChangeExecuteInfo();
        config.setExecType(fo.getAutoExecType());
        config.setTransactional(fo.isEnableTransactional());
        config.setErrorStrategy(fo.getErrorStrategy());
        config.setRetryWaitTime(fo.getRetryWaitTime());
        config.setRetryCount(fo.getRetryCount());
        config.setExecTime(fo.getExecTime());
        config.setSnapshot(fo.isSnapshot());

        DmChangeItemDO itemDO = new DmChangeItemDO();
        itemDO.setOwnerUid(change.getOwnerUid());
        itemDO.setRefFlowId(change.getRefFlowId());
        itemDO.setRefChangeId(change.getId());
        itemDO.setChangeItemType(ChangeItemType.EXECUTE);
        itemDO.setContent(JsonUtils.toJson(config));
        itemDO.setContentIndex(1);
        itemDO.setContentName("exec");
        this.changeFlowDal.changeItemMapper().deleteByChangeItemType(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
        this.changeFlowDal.changeItemMapper().insert(itemDO);
        this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.READY, "");
    }

    private static void checkRunStatus(DmChangeDO change) {
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() != ChangeStep.EXECUTE && change.getCurrentStep() != ChangeStep.FINISH) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NEED_EXECUTE_STEP_ERROR.name()));
        }
    }

    @Override
    public DmAutoExecJobVO queryExecJobInfo(String ownerUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        return this.autoExecService.queryAutoExecJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true);
    }

    @Override
    public DmPageVO<DmAutoExecTaskVO> queryExecTaskList(String ownerUid, ChangeExecTaskListFO fo) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, fo.getChangeId());
        checkRunStatus(change);

        return this.autoExecService.queryAutoExecTaskList(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true, fo.getTaskStatus(), fo.getPage());
    }

    @Override
    public List<DmBizLogVO> queryExecLog(String ownerUid, ChangeExecLogFO fo) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, fo.getChangeId());
        checkRunStatus(change);

        DmExecAutoJobDO jobDO = checkJob(ownerUid, fo.getJobId());
        List<DmMonBizLogDO> dmBizLogDOS;
        if (fo.getBizType() == LogDependBizType.AUTO_EXEC_JOB) {
            if (jobDO.getBizId() == null) {
                return Collections.emptyList();
            } else {
                dmBizLogDOS = this.monitorDal.bizLogMapper().queryListByBizId(jobDO.getBizId());
            }
        } else {
            if (fo.getTaskId() == null) {
                return Collections.emptyList();
            } else {
                DmExecAutoTaskDO execTaskDO = executionDal.autoTaskMapper().selectById(fo.getTaskId());
                dmBizLogDOS = this.monitorDal.bizLogMapper().queryListByBizId(execTaskDO.getBizId());
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
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.stopJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void startExecJob(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.retryJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void retryExecJob(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.retryJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Override
    public void abortExecJob(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.endJob(String.valueOf(changeId), SQLJobBizType.CHANGE, curUid);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void skipExecTask(String ownerUid, String curUid, long changeId, long taskId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        checkRunStatus(change);

        this.autoExecService.skipTask(String.valueOf(changeId), SQLJobBizType.CHANGE, taskId, curUid);
    }

    @Override
    public void retryChange(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        if (change == null || change.isLockStatus()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStatus() == ChangeStatus.READY) {
            return;
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
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
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    @Override
    public void restartChange(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        if (change == null || change.isLockStatus()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() == ChangeStep.INIT && change.getCurrentStatus() == ChangeStatus.READY) {
            return;
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        switch (change.getCurrentStep()) {
            case INIT:
            case CHECK:
                this.retryChangeAtInitOrCheck(locale, change, true);
                this.changeFlowDal.changeItemMapper().deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case APPROVAL:
                this.retryChangeAtApproval(locale, change, ownerUid, curUid, true);
                this.changeFlowDal.changeItemMapper().deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case EXECUTE:
                this.restartChangeAtExecute(locale, change, ownerUid, curUid);
                this.changeFlowDal.changeItemMapper().deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case FINISH:
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void retryChangeAtInitOrCheck(Locale locale, DmChangeDO change, boolean isRestart) {
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());

        if (isRestart) {
            int res1 = this.changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.INIT, msg1);
            int res2 = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion() + 1, ChangeStatus.READY, msg1);
        } else {
            int res1 = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.READY, msg1);
        }

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);
    }

    private void retryChangeAtApproval(Locale locale, DmChangeDO change, String ownerUid, String curUid, boolean isRestart) {
        // close ticket
        if (change.getCurrentStatus() == ChangeStatus.WAIT) {
            List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(ownerUid, change.getId(), ChangeItemType.TICKET);
            DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
            if (item != null) {
                ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
                if (ticketInfo != null) {
                    String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REAPPROVAL_AT_CONSOLE_NOTICE.name());
                    this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, ownerUid, curUid);
                    change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, change.getId());
                }
            }
        }

        if (isRestart) {
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());
            int res1 = this.changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.INIT, msg1);
            int res2 = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion() + 1, ChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);
        } else {
            if (change.getCurrentStatus() == ChangeStatus.READY) {
                return;
            }

            // message
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REAPPROVAL_AT_CONSOLE_MESSAGE.name());
            int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REAPPROVAL_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);
        }
    }

    private void retryChangeAtExecute(Locale locale, DmChangeDO change, String ownerUid, String curUid) {
        switch (change.getCurrentStatus()) {
            case OPEN:
            case READY:
                return;
            case WAIT:
            case FINISH:
            case CLOSED:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_STATUS_MESSAGE.name()));
            case FAILED:
            default:
                break;
        }

        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REEXE_AT_CONSOLE_MESSAGE.name());
        List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.EXECUTE);
        DmChangeItemDO item = CollectionUtils.isEmpty(items) ? null : items.get(0);

        if (item == null || StringUtils.isEmpty(item.getContent())) {
            int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.READY, msg1);
        } else {
            this.autoExecService.retryJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, curUid);
            int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.WAIT, msg1);
        }

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REEXE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);
    }

    private void restartChangeAtExecute(Locale locale, DmChangeDO change, String ownerUid, String curUid) {
        if (change.getCurrentStatus() == ChangeStatus.OPEN) {
            String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_MESSAGE.name());
            int res1 = this.changeFlowDal.changeMapper().updateStepTo(change.getId(), change.getVersion(), ChangeStep.INIT, msg1);
            int res2 = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion() + 1, ChangeStatus.READY, msg1);

            String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REINIT_OR_RECHECK_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
            this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_STATUS_MESSAGE.name()));
        }
    }

    @Override
    public void closeChange(String ownerUid, String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(ownerUid, changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
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
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void closeChangeAtInitOrCheck(Locale locale, DmChangeDO change) {
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtApproval(Locale locale, DmChangeDO change, String ownerUid, String curUid) {
        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // close ticket
        List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(ownerUid, change.getId(), ChangeItemType.TICKET);
        DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item != null) {
            ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
            if (ticketInfo != null && !this.approvalFlowService.isFinish(ticketInfo.getTicketId())) {
                this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, ownerUid, curUid);
            }
        }

        // send message and update status
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtExecute(Locale locale, DmChangeDO change, String ownerUid, String curUid) {
        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // close auto exec
        DmAutoExecJobVO jobVO = this.autoExecService.queryAutoExecJob(String.valueOf(change.getId()), SQLJobBizType.CHANGE, true);
        if (jobVO != null) {
            if (jobVO.getStatus() == AutoExecJobStatus.FINISH || jobVO.getStatus() == AutoExecJobStatus.TERMINATION) {
                // is end status
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_EXECUTE_NOT_FINISH.name()));
            }
        }

        // send message and update status
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private void closeChangeAtSnapshot(Locale locale, DmChangeDO change, String ownerUid, String curUid) {
        if (change.getCurrentStatus() == ChangeStatus.FINISH || change.getCurrentStatus() == ChangeStatus.CLOSED) {
            return;
        }

        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // send message and update status
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
    }

    private DmExecAutoJobDO checkJob(String ownerUid, long jobId) {
        DmExecAutoJobDO jobDO = this.executionDal.autoJobMapper().selectById(jobId);
        if (jobDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_EXISTS_ERROR_MESSAGE.name()));
        }
        if (!jobDO.getPrimaryUid().equals(ownerUid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_NOT_BELONG_CURRENT_TEAM.name()));
        }
        return jobDO;
    }

    @Override
    public void verifyFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flowDO == null || flowDO.isDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flowDO.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flowDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }
        if (!flowDO.isEnableWebhook()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_WEBHOOK_NOT_ENABLE_MESSAGE.name()));
        }

        DmGitOpsScmDO scmDO = this.dmScmService.queryScmById(ownerUid, flowDO.getRefScmId());
        if (scmDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }
    }

    @Override
    public CreateSuggest createChangeSuggest(String ownerUid, long flowId, String commitId) {
        DmChangeFlowDO flowDO = changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        List<DmChangeDO> changeList = this.changeFlowDal.changeMapper().queryUnlockedChange(ownerUid, flowDO.getId());
        if (CollectionUtils.isNotEmpty(changeList)) {
            for (DmChangeDO changeDO : changeList) {
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
                        if (changeDO.getCurrentStatus() == ChangeStatus.OPEN) {
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
    public ResWebData<String> triggerChangeSuggest(String ownerUid, long flowId, String commitId) {
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);

        // create
        try {
            CreateSuggest suggest = this.createChangeSuggest(ownerUid, flowId, commitId);
            switch (suggest.getSuggestType()) {
                case Create:
                    doCreateChange(ownerUid, flowDO, commitId);
                    return ResWebDataUtils.buildSuccess("change created.");
                case Restart:
                    doRestartChange(suggest);
                    return ResWebDataUtils.buildSuccess("change restarted.");
                case Later:
                    doLaterChange(ownerUid, flowDO, commitId, suggest);
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

    private void doCreateChange(String owner, DmChangeFlowDO gitOpsFlowDO, String commitId) {
        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setRefFlowId(gitOpsFlowDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_REPO_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(gitOpsFlowDO.getScmRepoBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ChangeStep.INIT);
        changeDO.setCurrentStatus(ChangeStatus.READY);
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(commitId);
        changeDO.setLockStatus(false);
        changeDO.setFlowWalked(new RsChangeFlowWalkedObj());
        this.changeFlowDal.changeMapper().insert(changeDO);
    }

    private void doRestartChange(CreateSuggest suggest) {
        DmChangeDO changeDO = suggest.getChange();

        // language
        String language = this.senderService.getFlowLanguage(changeDO.getOwnerUid(), changeDO.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_RESTART_BY_REPO.name(), locale, changeDO.getChangeName());
        try {
            this.senderService.sendMessage(changeDO.getOwnerUid(), changeDO.getRefFlowId(), ImMessageType.ChangeLife, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        this.restartChange(changeDO.getOwnerUid(), changeDO.getOwnerUid(), changeDO.getId());
    }

    private void doLaterChange(String owner, DmChangeFlowDO gitOpsFlowDO, String commitId, CreateSuggest suggest) {
        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setRefFlowId(gitOpsFlowDO.getRefFlowId());
        changeDO.setRefFlowId(gitOpsFlowDO.getId());
        changeDO.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_REPO_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        changeDO.setChangeBranch(gitOpsFlowDO.getScmRepoBranch());
        changeDO.setChangeTime(new Date());
        changeDO.setCurrentStep(ChangeStep.INIT);
        changeDO.setCurrentStatus(ChangeStatus.FAILED);
        changeDO.setRemark(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_WAIT_OTHER_RUNNING_MESSAGE.name(), suggest.getChange().getChangeName()));
        changeDO.setVersion(0);
        changeDO.setTryTimes(0);
        changeDO.setLastCommitId(commitId);
        changeDO.setLockStatus(true);
        changeDO.setFlowWalked(new RsChangeFlowWalkedObj());
        this.changeFlowDal.changeMapper().insert(changeDO);
    }
}
