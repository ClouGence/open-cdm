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
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.approval.ApprovalActivityInfo;
import com.clougence.clouddm.sdk.approval.ApprovalCreateInstanceResult;
import com.clougence.clouddm.sdk.approval.ApprovalProviderSpi;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QueryApprovalHandler implements ApprovalHandler {
    private static final int     EXTERNAL_APPROVAL_SQL_MAX_CHARS = 4000;
    @Resource
    private ExecutionDal         execDal;
    @Resource
    private AuthDal              authDal;
    @Resource
    private ApprovalDal          approvalDal;
    @Resource
    private ApprovalService      approvalService;
    @Resource
    private ApprovalStateService approvalStateService;
    @Resource
    private UserConfigService    userConfigService;

    @Override
    public ApprovalBiz handleType() {
        return ApprovalBiz.DM_QUERY;
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void executeTicket(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().queryByDependOnBizId(ticketDO.getBizId());
        if (jobDO == null) {
            return;
        }

        this.updateExecutionStatus(approvalId, jobDO.getStatus());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void runningCheck(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        DmExecAutoJobDO jobDO = this.execDal.autoJobMapper().queryByDependOnBizId(ticketDO.getBizId());
        this.updateExecutionStatus(approvalId, jobDO.getStatus());
    }

    private void updateExecutionStatus(long approvalId, AutoExecJobStatus status) {
        switch (status) {
            case FINISH -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.FINISH, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.FINISHED, null);
            }
            case FAILED -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.FAIL, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.EXEC_FAIL, null);
            }
            case PAUSE -> {
                this.approvalStateService.updateProcessStatus(approvalId, ApprovalStage.EXECUTION, ApprovalProcessStatus.PAUSE, null);
                this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.EXEC_PAUSE, null);
            }
            default -> {
                // Execution progress states are synchronized by execution callbacks.
            }
        }
    }

    @Override
    public List<PrimaryUserVO> queryPerson(long approvalId) {
        DmApprovalDO ticketDO = this.approvalDal.approvalMapper().queryById(approvalId);
        List<PrimaryUserVO> userVOS = new ArrayList<>();

        // add primary account
        DmAuthUserDO parentUserDO = this.authDal.userMapper().queryByUid(ticketDO.getPrimaryUid());
        PrimaryUserVO primaryUserVO = new PrimaryUserVO();
        primaryUserVO.setUid(ticketDO.getPrimaryUid());
        primaryUserVO.setUsername(parentUserDO.getUsername());
        userVOS.add(primaryUserVO);

        // add sub account who have auth to approval ticket and manger datasource
        List<RsAuthPersonObj> personDOS = this.authDal.userMapper().queryApproPerson(//
                AccountType.SUB_ACCOUNT, parentUserDO.getId(), ticketDO.getBindDsId(), ticketDO.getLevelPath());
        for (RsAuthPersonObj personDO : personDOS) {
            List<String> roleAuthLabels = personDO.getRoleAuthLabels();
            List<String> resAuthLabel = personDO.getResAuthLabel();
            if (CollectionUtils.isNotEmpty(roleAuthLabels) //
                && CollectionUtils.isNotEmpty(resAuthLabel) //
                && roleAuthLabels.contains(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE) //
                && resAuthLabel.contains(SecDataAuthLabel.DM_DAUTH_TICKET)) //
            {
                PrimaryUserVO primaryUserVO2 = new PrimaryUserVO();
                primaryUserVO2.setUid(personDO.getUid());
                primaryUserVO2.setUsername(personDO.getUsername());
                userVOS.add(primaryUserVO2);
            }
        }
        return userVOS;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void createApproval(long approvalId, ImSenderService sender) {
        DmApprovalDO ticketDO = approvalDal.approvalMapper().selectByIdForUpdate(approvalId);
        if (ticketDO.getApproType() == ApprovalType.Internal) {
            return; // only external approval need to create approval instance.
        }

        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        QueryForm form = buildExternalApprovalForm(ticketDO, ticketDO.getApproTemplateIdentity());

        ApprovalCreateInstanceResult createInstance;
        try {
            createInstance = approvalService.createApprovalInstance(ticketDO.getPrimaryUid(), form);
        } catch (ThirdPartyApiException e) {
            // wait retry
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error(DmI18nUtils.getMessage(e.getMessageKey()));
                return;
            }
            throw e;
        }

        for (ApprovalActivityInfo activity : createInstance.getActivityList()) {
            this.approvalStateService
                .initializeActivity(ticketDO.getId(), ApprovalStage.APPROVAL, activity.getActivityId(), activity.getActivityName(), activity.getOrder(), null, null);
        }
        String url = null;
        if (createInstance.getApprovalUrl() != null) {
            url = JsonUtils.toJson(createInstance.getApprovalUrl());
        }
        approvalDal.approvalMapper().updateThirdApprovalInfo(ticketDO.getId(), createInstance.getApprovalIdentity(), url);
    }

    @Override
    public void approvalCompleted(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalApproved(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        this.approvalStateService.updateApprovalStatus(approvalId, ApprovalStatus.WAIT_CONFIRM, null);
    }

    @Override
    public void approvalRejected(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalFailed(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    @Override
    public void approvalCanceled(long approvalId, ApprovalBiz bizType, ImSenderService sender) {
        // do nothing
    }

    private QueryForm buildExternalApprovalForm(DmApprovalDO approvalDO, String templateId) {
        QueryForm form = new QueryForm();

        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(approvalDO.getOwnerUid());
        form.setTicketUserPhone(userDO.getPhone());
        form.setExecuteSql(this.approvalService.consumeSqlFile(approvalDO.getId(), sqlFile -> {
            String language = this.userConfigService.defaultLanguage();
            Locale locale = I18nUtils.getLocale(language);
            StringBuilder prefix = new StringBuilder(EXTERNAL_APPROVAL_SQL_MAX_CHARS);
            long totalChars = 0;
            int totalLines = 0;
            boolean hasContent = false;
            boolean previousWasCr = false;
            char lastChar = 0;
            char[] buffer = new char[8192];
            try (Reader reader = Files.newBufferedReader(sqlFile, StandardCharsets.UTF_8)) {
                int readLength;
                while ((readLength = reader.read(buffer)) >= 0) {
                    if (readLength == 0) {
                        continue;
                    }
                    hasContent = true;
                    totalChars += readLength;
                    int appendLength = Math.min(readLength, EXTERNAL_APPROVAL_SQL_MAX_CHARS - prefix.length());
                    if (appendLength > 0) {
                        prefix.append(buffer, 0, appendLength);
                    }
                    for (int i = 0; i < readLength; i++) {
                        char current = buffer[i];
                        if (current == '\r') {
                            totalLines++;
                            previousWasCr = true;
                        } else {
                            if (current == '\n' && !previousWasCr) {
                                totalLines++;
                            }
                            previousWasCr = false;
                        }
                    }
                    lastChar = buffer[readLength - 1];
                }
            }
            if (hasContent && lastChar != '\r' && lastChar != '\n') {
                totalLines++;
            }
            if (totalChars <= EXTERNAL_APPROVAL_SQL_MAX_CHARS) {
                return prefix.toString();
            }

            String separator = "\n\n";
            int contentLength = EXTERNAL_APPROVAL_SQL_MAX_CHARS;
            String truncatedMessage;
            while (true) {
                int displayedLines = 0;
                boolean displayedPreviousWasCr = false;
                for (int i = 0; i < contentLength; i++) {
                    char current = prefix.charAt(i);
                    if (current == '\r') {
                        displayedLines++;
                        displayedPreviousWasCr = true;
                    } else {
                        if (current == '\n' && !displayedPreviousWasCr) {
                            displayedLines++;
                        }
                        displayedPreviousWasCr = false;
                    }
                }
                int hiddenLines = Math.max(1, totalLines - displayedLines);
                truncatedMessage = DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_EXTERNAL_APPROVAL_SQL_TRUNCATED_MESSAGE.name(), locale, hiddenLines);
                int nextContentLength = Math.max(0, EXTERNAL_APPROVAL_SQL_MAX_CHARS - separator.length() - truncatedMessage.length());
                if (nextContentLength == contentLength) {
                    break;
                }
                contentLength = nextContentLength;
            }
            if (contentLength > 0 && Character.isHighSurrogate(prefix.charAt(contentLength - 1))) {
                contentLength--;
            }
            return prefix.substring(0, contentLength) + separator + truncatedMessage;
        }));
        form.setRollBackSql(approvalDO.getRollBackSql());
        form.setAffectCount(approvalDO.getExpectedAffectedRows());
        form.setTargetDs(approvalDO.getTargetInfo());
        form.setTicketDesc(approvalDO.getDescription());
        form.setTicketTitle(approvalDO.getTicketTitle());
        form.setTemplateIdentity(templateId);

        return form;
    }
}
