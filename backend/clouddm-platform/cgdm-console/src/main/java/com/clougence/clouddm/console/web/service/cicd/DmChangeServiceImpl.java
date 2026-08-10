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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.component.cicd.ImMessageType;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfo;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeListFO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeBodyItemVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeSqlPreviewVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeTransferVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeVO;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggestType;
import com.clougence.clouddm.console.web.service.upload.impl.SqlFilePreviewReader;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.cicd.*;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.dal.util.PageUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmChangeServiceImpl implements DmChangeService {
    private static final String  CHANGE_SOURCE_PREFIX = "/* sourceCode: ";
    private static final String  CHANGE_SOURCE_SUFFIX = " */";

    @Resource
    private ChangeFlowDal        changeFlowDal;
    @Resource
    private DataSourceDal        dsDal;
    @Resource
    private ApprovalDal          approvalDal;
    @Resource
    private ObjectCacheDao       objectCacheDao;
    @Resource
    private DmScmService         dmScmService;
    @Resource
    private ImSenderService      senderService;
    @Resource
    private ApprovalFlowService  approvalFlowService;
    @Resource
    private ChangeSqlService     changeSqlService;
    @Resource
    private ChangeCascadeService changeCascadeService;

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
            Set<Long> scmIds = devops.stream().map(DmChangeFlowDO::getRefScmId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!scmIds.isEmpty()) {
                List<DmGitOpsScmDO> scmList = dmScmService.queryScmByIds(ownerUid, scmIds);
                scmList.forEach(d -> scmMap.put(d.getId(), d));
            }
        } else {
            devopsMap = Collections.emptyMap();
            dsMap = Collections.emptyMap();
            scmMap = Collections.emptyMap();
        }

        // convert
        List<ChangeVO> vos = records.stream().map(obj -> {
            return DmConvertUtils.convertToChangeVO(flowDO, obj, devopsMap, dsMap, scmMap, objectCacheDao);
        }).collect(Collectors.toList());

        Set<Long> batchIds = records.stream().map(DmChangeDO::getRefBatchId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DmChangeBatchDO> batches = new HashMap<>();
        if (!batchIds.isEmpty()) {
            this.changeFlowDal.batchMapper().queryByIds(ownerUid, batchIds).forEach(batch -> batches.put(batch.getId(), batch));
        }

        Set<Long> parentChangeIds = records.stream().map(DmChangeDO::getRefParentChangeId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DmChangeDO> parentChanges = new HashMap<>();
        if (!parentChangeIds.isEmpty()) {
            this.changeFlowDal.changeMapper().queryByIds(ownerUid, parentChangeIds).forEach(parentChange -> parentChanges.put(parentChange.getId(), parentChange));
        }
        Set<Long> parentFlowIds = parentChanges.values().stream().map(DmChangeDO::getRefFlowId).collect(Collectors.toSet());
        Map<Long, DmChangeFlowDO> parentFlows = new HashMap<>();
        if (!parentFlowIds.isEmpty()) {
            this.changeFlowDal.flowMapper().queryByIds(ownerUid, parentFlowIds).forEach(parentFlow -> parentFlows.put(parentFlow.getId(), parentFlow));
        }

        Set<Long> cascadeChangeIds = records.stream().filter(change -> change.getRefBatchId() != null).map(DmChangeDO::getId).collect(Collectors.toSet());
        Map<Long, List<ChangeTransferVO>> downstream = Collections.emptyMap();
        if (!cascadeChangeIds.isEmpty()) {
            downstream = this.changeCascadeService.queryDownstreamTransfers(ownerUid, cascadeChangeIds);
        }
        for (int i = 0; i < records.size(); i++) {
            DmChangeDO change = records.get(i);
            ChangeVO vo = vos.get(i);
            if (change.getRefBatchId() != null) {
                DmChangeBatchDO batch = batches.get(change.getRefBatchId());
                if (batch != null) {
                    vo.setRootChangeId(batch.getRefRootChangeId());
                    vo.setBatchStatus(batch.getBatchStatus());
                }
            }
            if (change.getRefParentChangeId() != null) {
                DmChangeDO parentChange = parentChanges.get(change.getRefParentChangeId());
                if (parentChange != null) {
                    DmChangeFlowDO parentFlow = parentFlows.get(parentChange.getRefFlowId());
                    vo.setParentFlowId(parentChange.getRefFlowId());
                    if (parentFlow != null) {
                        vo.setParentFlowName(parentFlow.getFlowName());
                    }
                }
            }
            vo.setDownstream(downstream.getOrDefault(change.getId(), Collections.emptyList()));
        }

        results.setRecords(vos);
        return results;
    }

    @Override
    public DmChangeDO queryChangeById(long changeId) {
        return this.changeFlowDal.changeMapper().queryChangeById(changeId);
    }

    @Override
    public ChangeSqlPreviewVO previewChangeSql(long changeId, int startLine, int lineCount, String contentName) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }

        if (StringUtils.isNotBlank(contentName)) {
            DmChangeItemDO baselineSnapshot = this.changeFlowDal.changeItemMapper().queryChangeItemByName(change.getOwnerUid(), changeId, ChangeItemType.SQL_BASELINE, contentName);
            DmChangeItemDO current = this.changeFlowDal.changeItemMapper().queryChangeItemByName(change.getOwnerUid(), changeId, ChangeItemType.SQL, contentName);
            ChangeBodyItemVO item = new ChangeBodyItemVO();
            item.setContentName(contentName);
            item.setNewBody(current == null ? null : current.getContent());
            if (baselineSnapshot != null) {
                item.setOldBody(baselineSnapshot.getContent());
            } else {
                DmChangeFlowItemDO baseline = this.changeFlowDal.flowItemMapper().queryItemByFlowIdAndName(change.getOwnerUid(), change.getRefFlowId(), contentName);
                item.setOldBody(baseline == null ? null : baseline.getContent());
                if (Objects.equals(item.getOldBody(), item.getNewBody())) {
                    String legacyDiff = this.changeSqlService.consumeSqlFile(changeId, file -> readChangeSource(file, contentName));
                    if (StringUtils.isNotBlank(legacyDiff)) {
                        item.setOldBody(null);
                        item.setNewBody(legacyDiff);
                    }
                }
            }
            ChangeSqlPreviewVO vo = new ChangeSqlPreviewVO();
            vo.setItemList(Collections.singletonList(item));
            return vo;
        }

        var preview = this.changeSqlService.consumeSqlFile(changeId, f -> SqlFilePreviewReader.read(f, startLine, lineCount));
        ChangeSqlPreviewVO vo = new ChangeSqlPreviewVO();
        vo.setStartLine(preview.startLine());
        vo.setTotalLines(preview.totalLines());
        vo.setContent(preview.content());
        vo.setEof(preview.eof());
        if (startLine == 1) {
            List<DmChangeItemDO> diffItems = this.changeFlowDal.changeItemMapper().queryChangeItemMetaByChangeId(change.getOwnerUid(), changeId, ChangeItemType.SQL_BASELINE);
            if (diffItems.isEmpty()) {
                diffItems = this.changeFlowDal.changeItemMapper().queryChangedItemMeta(change.getOwnerUid(), change.getRefFlowId(), changeId);
            }
            List<String> contentNames = diffItems.stream().map(DmChangeItemDO::getContentName).toList();
            List<ChangeBodyItemVO> items;
            if (contentNames.isEmpty()) {
                items = this.changeSqlService.consumeSqlFile(changeId, file -> {
                    List<ChangeBodyItemVO> legacyItems = new ArrayList<>();
                    for (String name : readChangeSourceNames(file)) {
                        ChangeBodyItemVO itemVO = new ChangeBodyItemVO();
                        itemVO.setContentName(name);
                        itemVO.setNewBody(readChangeSource(file, name));
                        legacyItems.add(itemVO);
                    }
                    return legacyItems;
                });
            } else {
                items = contentNames.stream().map(name -> {
                    ChangeBodyItemVO itemVO = new ChangeBodyItemVO();
                    itemVO.setContentName(name);
                    return itemVO;
                }).toList();
            }
            vo.setItemList(items);
        }
        return vo;
    }

    private List<String> readChangeSourceNames(Path file) throws IOException {
        Set<String> names = new LinkedHashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = parseChangeSourceName(line);
                if (name != null) {
                    names.add(name);
                }
            }
        }
        return new ArrayList<>(names);
    }

    private String readChangeSource(Path file, String contentName) throws IOException {
        StringBuilder content = new StringBuilder();
        boolean selected = false;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = parseChangeSourceName(line);
                if (name != null) {
                    if (selected) {
                        break;
                    }
                    selected = contentName.equals(name);
                    continue;
                }
                if (selected) {
                    if (!content.isEmpty()) {
                        content.append('\n');
                    }
                    content.append(line);
                }
            }
        }
        return content.toString().trim();
    }

    private String parseChangeSourceName(String line) {
        if (!line.startsWith(CHANGE_SOURCE_PREFIX) || !line.endsWith(CHANGE_SOURCE_SUFFIX)) {
            return null;
        }
        return line.substring(CHANGE_SOURCE_PREFIX.length(), line.length() - CHANGE_SOURCE_SUFFIX.length()).trim();
    }

    @Override
    public ChangeTicketInfoResult fetchChangeApprovalByChangeId(long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }

        List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), changeId, ChangeItemType.TICKET);
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
    public Map<Long, Long> queryTicketIds(String ownerUid, Collection<Long> changeIds) {
        if (CollectionUtils.isEmpty(changeIds)) {
            return Collections.emptyMap();
        }
        List<DmChangeItemDO> items = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeIds(ownerUid, changeIds, ChangeItemType.TICKET);
        Map<Long, Long> ticketIds = new HashMap<>();
        for (DmChangeItemDO item : items) {
            ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
            if (ticketInfo != null && ticketInfo.getTicketId() != null) {
                ticketIds.put(item.getRefChangeId(), ticketInfo.getTicketId());
            }
        }
        return ticketIds;
    }

    @Override
    public void retryChange(String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
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
                this.retryChangeAtInit(locale, change, false);
                return;
            case APPROVAL:
                this.retryChangeAtApproval(locale, change, curUid, false);
                return;
            case FINISH:
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    @Override
    public void restartChange(String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
        if (change == null || change.isLockStatus()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }
        if (change.getCurrentStep() == ChangeStep.INIT && change.getCurrentStatus() == ChangeStatus.READY) {
            return;
        }
        DmChangeFlowDO restartFlow = this.changeFlowDal.flowMapper().queryByOwnerAndId(change.getOwnerUid(), change.getRefFlowId());
        if (restartFlow != null && restartFlow.getFlowType() == ChangeFlowType.BUILT_IN) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        switch (change.getCurrentStep()) {
            case INIT:
                this.retryChangeAtInit(locale, change, true);
                this.changeFlowDal.changeItemMapper().deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case APPROVAL:
                this.retryChangeAtApproval(locale, change, curUid, true);
                this.changeFlowDal.changeItemMapper().deleteByChangeItemAll(change.getOwnerUid(), change.getId());
                return;
            case FINISH:
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void retryChangeAtInit(Locale locale, DmChangeDO change, boolean isRestart) {
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

    private void retryChangeAtApproval(Locale locale, DmChangeDO change, String curUid, boolean isRestart) {
        // close ticket
        if (change.getCurrentStatus() == ChangeStatus.WAIT) {
            List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.TICKET);
            DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
            if (item != null) {
                ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
                if (ticketInfo != null) {
                    String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_REAPPROVAL_AT_CONSOLE_NOTICE.name());
                    this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, change.getOwnerUid(), curUid);
                    change = this.changeFlowDal.changeMapper().queryChangeById(change.getId());
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

    @Override
    public void closeChange(String curUid, long changeId) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }

        String language = this.senderService.getFlowLanguage(change.getOwnerUid(), change.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);

        switch (change.getCurrentStep()) {
            case INIT:
                this.closeChangeAtInit(locale, change);
                return;
            case APPROVAL:
                this.closeChangeAtApproval(locale, change, curUid);
                return;
            case INIT_SNAPSHOT:
                this.closeChangeAtSnapshot(locale, change);
                return;
            case FINISH:
                return;
            default:
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_UNSUPPORT_RETRY_MESSAGE.name()));
        }
    }

    private void closeChangeAtInit(Locale locale, DmChangeDO change) {
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
        this.changeCascadeService.onChangeTerminal(change);
    }

    private void closeChangeAtApproval(Locale locale, DmChangeDO change, String curUid) {
        // message
        String msg1 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_MESSAGE.name());

        // close ticket
        List<DmChangeItemDO> list = this.changeFlowDal.changeItemMapper().queryChangeItemByChangeId(change.getOwnerUid(), change.getId(), ChangeItemType.TICKET);
        DmChangeItemDO item = list.isEmpty() ? null : list.get(0);
        if (item != null) {
            ChangeTicketInfo ticketInfo = JsonUtils.toObj(item.getContent(), ChangeTicketInfo.class);
            if (ticketInfo != null && !this.approvalFlowService.isFinish(ticketInfo.getTicketId())) {
                this.approvalFlowService.closeTicket(ticketInfo.getTicketId(), msg1, change.getOwnerUid(), curUid);
            }
        }

        // send message and update status
        int res = this.changeFlowDal.changeMapper().updateStatusTo(change.getId(), change.getVersion(), ChangeStatus.CLOSED, msg1);

        String msg2 = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_CLOSE_AT_CONSOLE_NOTICE.name(), locale, change.getChangeName());
        this.senderService.sendMessage(change.getOwnerUid(), change.getRefFlowId(), ImMessageType.ChangeNotice, msg2);

        this.changeFlowDal.changeMapper().lockChangeById(change.getId(), change.getVersion() + 1);
        this.changeCascadeService.onChangeTerminal(change);
    }

    private void closeChangeAtSnapshot(Locale locale, DmChangeDO change) {
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
        this.changeCascadeService.onChangeTerminal(change);
    }

    @Override
    public void verifyFlow(String ownerUid, long flowId) {
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(ownerUid, flowId);
        if (flowDO == null || flowDO.isDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        ChangeFlowType flowType = flowDO.getFlowType() == null ? ChangeFlowType.SCM : flowDO.getFlowType();
        if (flowType == ChangeFlowType.BUILT_IN) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_BUILT_IN_TRIGGER_ERROR.name()));
        }
        if (flowDO.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
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
                if (!StringUtils.equals(commitId, changeDO.getLastCommitId())) {
                    CreateSuggest suggest = new CreateSuggest();
                    suggest.setChange(changeDO);
                    suggest.setSuggestType(CreateSuggestType.Later);
                    return suggest;
                }
                switch (changeDO.getCurrentStep()) {
                    case INIT_SNAPSHOT: {
                        CreateSuggest suggest = new CreateSuggest();
                        suggest.setChange(changeDO);
                        suggest.setSuggestType(CreateSuggestType.Later);
                        return suggest;
                    }
                    case INIT:
                    case APPROVAL: {
                        CreateSuggest suggest = new CreateSuggest();
                        suggest.setChange(changeDO);
                        suggest.setSuggestType(CreateSuggestType.Restart);
                        return suggest;
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
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void triggerBuiltInChange(String ownerUid, String triggerUid, long flowId, String sql) {
        if (StringUtils.isBlank(sql)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_MANUAL_SQL_REQUIRED_ERROR.name()));
        }
        DmChangeFlowDO flow = this.changeFlowDal.flowMapper().queryByOwnerAndIdForUpdate(ownerUid, flowId);
        if (flow == null || flow.isDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        if (flow.getFlowType() != ChangeFlowType.BUILT_IN || flow.getRefParentFlowId() != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_BUILT_IN_TRIGGER_ERROR.name()));
        }
        if (flow.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flow.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }
        if (this.changeFlowDal.batchMapper().queryRunningByRootFlow(ownerUid, flowId) != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CASCADE_RUNNING_ERROR.name()));
        }
        int unfinished = this.changeFlowDal.changeMapper().countUnfinishedChangeByFlowId(ownerUid, flowId);
        if (unfinished > 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_CHANGE_IN_INUSE_ERROR.name(), unfinished));
        }

        DmChangeDO change = new DmChangeDO();
        change.setOwnerUid(ownerUid);
        change.setTriggerUid(triggerUid);
        change.setRefFlowId(flowId);
        change.setChangeName(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_INIT_MANUAL_NAME.name(), WellKnowFormat.WKF_DATE_TIME24.now()));
        change.setChangeBranch("");
        change.setChangeTime(new Date());
        change.setCurrentStep(ChangeStep.APPROVAL);
        change.setCurrentStatus(ChangeStatus.READY);
        change.setVersion(0);
        change.setTryTimes(0);
        change.setLastCommitId("manual-" + UUID.randomUUID());
        change.setLockStatus(false);
        this.changeFlowDal.changeMapper().insert(change);

        DmChangeItemDO sqlItem = new DmChangeItemDO();
        sqlItem.setOwnerUid(ownerUid);
        sqlItem.setRefFlowId(flowId);
        sqlItem.setRefChangeId(change.getId());
        sqlItem.setChangeItemType(ChangeItemType.SQL);
        sqlItem.setContentName("manual.sql");
        sqlItem.setContentIndex(0);
        sqlItem.setContent(sql);
        this.changeFlowDal.changeItemMapper().insert(sqlItem);

        DmChangeItemDO reviewItem = new DmChangeItemDO();
        reviewItem.setOwnerUid(ownerUid);
        reviewItem.setRefFlowId(flowId);
        reviewItem.setRefChangeId(change.getId());
        reviewItem.setChangeItemType(ChangeItemType.REVIEW);
        reviewItem.setContentName("manual.sql");
        reviewItem.setContentIndex(0);
        reviewItem.setContent(sql);
        this.changeFlowDal.changeItemMapper().insert(reviewItem);

        this.changeCascadeService.createRootBatch(change);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public ResWebData<String> triggerChangeSuggest(String ownerUid, long flowId, ChangeTriggerContext triggerContext) {
        if (triggerContext == null || StringUtils.isBlank(triggerContext.getCommitId())) {
            throw new ErrorMessageException("change trigger commit is missing.");
        }
        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndIdForUpdate(ownerUid, flowId);
        if (flowDO == null || flowDO.isDeleted()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_NOT_EXIST_ERROR.name()));
        }
        ChangeFlowType flowType = flowDO.getFlowType() == null ? ChangeFlowType.SCM : flowDO.getFlowType();
        if (flowType == ChangeFlowType.BUILT_IN) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_BUILT_IN_TRIGGER_ERROR.name()));
        }
        if (flowDO.getChangeFlowStatus() != ChangeFlowStatus.NORMAL) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_IS_ARCHIVE_OR_DELETE_ERROR.name()));
        }
        if (!flowDO.isEnable()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IS_DISABLED_ERROR.name()));
        }
        boolean hasChildren = this.changeFlowDal.flowMapper().countChildren(ownerUid, flowId) > 0;
        if (hasChildren && !triggerContext.isManual()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CASCADE_MANUAL_ONLY_ERROR.name()));
        }
        if (hasChildren && this.changeFlowDal.batchMapper().queryRunningByRootFlow(ownerUid, flowId) != null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_FLOW_CASCADE_RUNNING_ERROR.name()));
        }

        DmChangeTriggerReceiptDO receipt = new DmChangeTriggerReceiptDO();
        receipt.setOwnerUid(ownerUid);
        receipt.setRefFlowId(flowId);
        receipt.setProvider(flowDO.getRefScmType());
        receipt.setDeliveryId(triggerContext.getDeliveryId());
        receipt.setCommitId(triggerContext.getCommitId());
        receipt.setTriggerType(triggerContext.getTriggerType());
        if (this.changeFlowDal.triggerReceiptMapper().reserve(receipt) == 0) {
            return ResWebDataUtils.buildSuccess("duplicate change trigger ignored.");
        }
        // create
        try {
            CreateSuggest suggest = this.createChangeSuggest(ownerUid, flowId, triggerContext.getCommitId());
            switch (suggest.getSuggestType()) {
                case Create:
                    DmChangeDO change = doCreateChange(ownerUid, flowDO, triggerContext.getCommitId(), triggerContext.getTriggerUid());
                    this.changeCascadeService.createRootBatch(change);
                    return ResWebDataUtils.buildSuccess("change created.");
                case Restart:
                    doRestartChange(suggest, triggerContext.getTriggerUid());
                    return ResWebDataUtils.buildSuccess("change restarted.");
                case Later:
                    doLaterChange(ownerUid, flowDO, triggerContext.getCommitId(), triggerContext.getTriggerUid(), suggest);
                    return ResWebDataUtils.buildError("change later.");
                default: {
                    return ResWebDataUtils.buildError("InnerError: Unknown SuggestType.");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(e);
        }
    }

    private DmChangeDO doCreateChange(String owner, DmChangeFlowDO gitOpsFlowDO, String commitId, String triggerUid) {
        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setTriggerUid(triggerUid);
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
        this.changeFlowDal.changeMapper().insert(changeDO);
        return changeDO;
    }

    private void doRestartChange(CreateSuggest suggest, String triggerUid) {
        DmChangeDO changeDO = suggest.getChange();
        if (triggerUid != null) {
            changeDO.setTriggerUid(triggerUid);
            this.changeFlowDal.changeMapper().updateTriggerUid(changeDO.getId(), triggerUid);
        }

        // language
        String language = this.senderService.getFlowLanguage(changeDO.getOwnerUid(), changeDO.getRefFlowId());
        Locale locale = I18nUtils.getLocale(language);
        String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_RESTART_BY_REPO.name(), locale, changeDO.getChangeName());
        try {
            this.senderService.sendMessage(changeDO.getOwnerUid(), changeDO.getRefFlowId(), ImMessageType.ChangeLife, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        this.restartChange(changeDO.getOwnerUid(), changeDO.getId());
    }

    private void doLaterChange(String owner, DmChangeFlowDO gitOpsFlowDO, String commitId, String triggerUid, CreateSuggest suggest) {
        DmChangeDO changeDO = new DmChangeDO();
        changeDO.setOwnerUid(owner);
        changeDO.setTriggerUid(triggerUid);
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
        this.changeFlowDal.changeMapper().insert(changeDO);
    }
}
