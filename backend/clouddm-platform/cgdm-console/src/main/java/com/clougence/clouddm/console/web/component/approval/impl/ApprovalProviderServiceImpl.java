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
package com.clougence.clouddm.console.web.component.approval.impl;

import static com.clougence.clouddm.console.web.component.approval.CustomApprovalConstants.TEMPLATE_ID_PREFIX;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalConfigService;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalConnectionTestFO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalProviderOptionVO;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.approval.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivityStatus;
import com.clougence.clouddm.sdk.service.approval.ApprovalIdentity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 14:16
*/
@Slf4j
@Service
public class ApprovalProviderServiceImpl implements ApprovalRefreshService {
    @Resource
    private PlatformTransactionManager              transactionManager;
    @Resource
    private CustomApprovalConfigService             customConfig;
    @Resource
    private SystemDal                               systemDal;
    @Resource
    private AuthDal                                 authDal;
    @Resource
    private ApprovalDal                             approvalDal;
    @Resource
    private ImSenderService                         imSenderService;
    @Resource
    private ApprovalStateService                    approvalStateService;

    private final Map<ApprovalBiz, ApprovalHandler> approvalHandlers;

    public ApprovalProviderServiceImpl(List<ApprovalHandler> approvalHandlers){
        this.approvalHandlers = new EnumMap<>(ApprovalBiz.class);
        for (ApprovalHandler approvalHandler : approvalHandlers) {
            ApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    @Override
    public void refreshTicket(ApprovalIdentity callback) {
        DmApprovalDO approvalDO = approvalDal.approvalMapper().queryByApproIdentity(callback.getApproIdentity(), callback.getProviderType(), callback.getOwnerUid());
        if (approvalDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", //
                    callback.getApproIdentity(), callback.getProviderType(), callback.getOwnerUid());
            return;
        }

        this.refreshApprovalStatus(approvalDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void updateActivity(ApprovalActivity activity) {
        DmApprovalDO approvalDO = approvalDal.approvalMapper().queryByApproIdentity(activity.getApprovalIdentity(), activity.getPlatform(), activity.getPuid());
        // avoid receive another callback
        if (approvalDO == null) {
            log.error("Callback event not find ticket for approval instance: {} and type: {} and puid: {}", //
                    activity.getApprovalIdentity(), activity.getPlatform(), activity.getPuid());
            return;
        }

        DmApprovalProcessDO processDO = this.approvalDal.processMapper().queryByStage(approvalDO.getId(), ApprovalStage.APPROVAL);
        DmApprovalProcessActivityDO activityDO = this.approvalDal.activityMapper().queryByProcessIdAndActivityIdForUpdate(processDO.getId(), activity.getActivityId());

        String context = activityDO.getContext();
        List<ApprovalActivity> list;
        if (StringUtils.isEmpty(context)) {
            list = new ArrayList<>();
        } else {
            list = JsonUtils.toList(context, new TypeReference<>() {});
        }

        ApprovalActivity originTask = null;
        for (ApprovalActivity approvalTask : list) {
            if (approvalTask.getTaskId().equals(activity.getTaskId())) {
                originTask = approvalTask;
                break;
            }
        }

        if (originTask == null) {
            if (activity.getStatus() == ApprovalActivityStatus.CLOSE) {
                return;
            }

            if (StringUtils.isEmpty(activity.getUserName())) {
                ApprovalProviderSpi service = PluginManager.findSpi(ApprovalProviderSpi.class, approvalDO.getApproType().name());
                ApprovalUserInfo userInfo = service.getUserDetailByUid(approvalDO.getPrimaryUid(), activity.getUserId());
                activity.setUserName(userInfo.getUsername());
            }

            list.add(activity);
        } else if (ApprovalActivityStatus.canUpdate(originTask.getStatus(), activity.getStatus())) {
            if (activity.getStatus() == ApprovalActivityStatus.CLOSE) {
                list.remove(originTask);
            } else {
                originTask.setStatus(activity.getStatus());
                originTask.setRemark(activity.getRemark());
                originTask.setFinishTime(activity.getFinishTime());
            }
        }

        String json = JsonUtils.toJson(list);
        approvalDal.activityMapper().updateContext(processDO.getId(), activityDO.getActivityId(), json);
    }

    public List<ApprovalProviderOptionVO> listApprovalProviders(String ownerUid) {
        List<ApprovalProviderOptionVO> list = new ArrayList<>();

        // inner
        ApprovalProviderOptionVO innerProvider = new ApprovalProviderOptionVO();
        innerProvider.setApprovalType(ApprovalType.Internal.name());
        innerProvider.setI18n(DmI18nUtils.getMessage(ApprovalType.Internal.getI18nKey()));
        innerProvider.setAvailable(true);
        innerProvider.setDesc("");
        list.add(innerProvider);

        List<String> serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);

        for (ApprovalProvider type : ApprovalFlowService.SUPPORT_LIST) {
            ApprovalProviderOptionVO provider = new ApprovalProviderOptionVO();
            provider.setApprovalType(type.name());
            provider.setI18n(DmI18nUtils.getMessage(ApprovalType.valueOfProvider(type).getI18nKey()));

            // not found plugin
            if (!serviceNames.contains(type.name())) {
                provider.setAvailable(false);
                provider.setDesc(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_NOT_FOUND.name()));
                list.add(provider);
                continue;
            }

            // configuration incomplete
            if (!this.isApprovalConfigured(ownerUid, type)) {
                provider.setAvailable(false);
                provider.setDesc(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_CONFIGURED.name()));
                list.add(provider);
                continue;
            }

            ApprovalProviderSpi sdkService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            LifeSpiResponse invoke = sdkService.status(ownerUid, new LifeSpiRequest());
            LifeSpiStatus info = JsonUtils.toObj(invoke.getBody(), LifeSpiStatus.class);
            if (!info.isRunning()) {
                provider.setAvailable(false);
                provider.setDesc(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_STATUS_FAILED.name()));
                list.add(provider);
                continue;
            }

            provider.setAvailable(true);
            provider.setDesc("");
            list.add(provider);
        }

        return list;
    }

    public boolean isApprovalConfigured(String ownerUid, ApprovalProvider type) {
        if (type == ApprovalProvider.Internal) {
            return true;
        }

        List<String> requiredKeys = requiredApprovalConfigKeys(type);
        if (requiredKeys.isEmpty()) {
            return false;
        }

        List<DmSysUserConfDO> configs = systemDal.userConfMapper().listByUidAndConfigNames(ownerUid, requiredKeys);
        Set<String> configuredKeys = new HashSet<>();
        for (DmSysUserConfDO config : configs) {
            if (StringUtils.isNotBlank(config.getConfigValue())) {
                configuredKeys.add(config.getConfigName());
            }
        }
        return configuredKeys.containsAll(requiredKeys);
    }

    private List<String> requiredApprovalConfigKeys(ApprovalProvider type) {
        switch (type) {
            case DingTalk:
                return List.of(RootUserConfig.Fields.dingApprovalConfigAk, RootUserConfig.Fields.dingApprovalConfigSk);
            case Feishu:
                return List.of(RootUserConfig.Fields.feishuApprovalAppID, RootUserConfig.Fields.feishuApprovalAppSecret);
            case Wechat:
                return List
                    .of(RootUserConfig.Fields.wechatApprovalCorpId, RootUserConfig.Fields.wechatApprovalAgentId, RootUserConfig.Fields.wechatApprovalSecret, RootUserConfig.Fields.wechatApprovalToken, RootUserConfig.Fields.wechatApprovalEncodingAesKey);
            case Internal:
                return List.of();
            default:
                return List.of();
        }

    }

    public void testConnection(String ownerUid, ApprovalConnectionTestFO request) {
        ApprovalProviderSpi provider = PluginManager.findSpi(ApprovalProviderSpi.class, request.getProvider().name());
        if (provider == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), request.getProvider()));
        }

        List<String> keys = requiredApprovalConfigKeys(request.getProvider());
        Map<String, String> config = new HashMap<>(request.getConfigs());
        List<DmSysUserConfDO> savedConfigs = systemDal.userConfMapper().listByUidAndConfigNames(ownerUid, keys);
        for (DmSysUserConfDO saved : savedConfigs) {
            config.putIfAbsent(saved.getConfigName(), saved.getConfigValue());
        }

        for (String key : keys) {
            if (StringUtils.isBlank(config.get(key))) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_CONFIGURED.name()));
            }
        }

        provider.testConnection(config);
    }

    public List<RdpApproTemplateVO> listTemplates(String ownerUid, ApprovalType type) {
        if (type != ApprovalType.Internal) {
            List<DmApprovalTemplateDO> templateDOS = approvalDal.templateMapper().listByPrimaryUidAndType(ownerUid, type);
            if (templateDOS.isEmpty()) {
                return refreshTemplates(ownerUid, type);
            }

            List<RdpApproTemplateVO> result = new ArrayList<>();
            for (DmApprovalTemplateDO cacheObj : templateDOS) {
                RdpApproTemplateVO vo = new RdpApproTemplateVO();
                vo.setApproTemplateName(cacheObj.getTemplateName());
                vo.setTemplateIdentity(cacheObj.getTemplateIdentity());
                vo.setApproUrl(cacheObj.getApproUrl());
                result.add(vo);
            }
            return result;
        }

        List<RdpApproTemplateVO> templates = new ArrayList<>();
        templates.add(ApprovalFlowService.innerTemplate());
        List<DmApprovalTemplateDO> customTemplates = customConfig.listCustomTemplates(ownerUid);
        for (DmApprovalTemplateDO template : customTemplates) {
            RdpApproTemplateVO vo = new RdpApproTemplateVO();
            vo.setApproTemplateName(template.getTemplateName());
            vo.setTemplateIdentity(template.getTemplateIdentity());
            templates.add(vo);
        }
        return templates;
    }

    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, ApprovalType type) {
        // Provider I/O happens before acquiring the tenant database mutex.
        List<DmApprovalTemplateDO> cacheList = new ArrayList<>();
        if (type != ApprovalType.Internal) {
            ApprovalProviderSpi provider = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            if (provider == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
            }

            Set<String> identities = new HashSet<>();
            List<ApprovalTemplate> providerTemplates = provider.getTemplates(ownerUid);
            for (ApprovalTemplate template : providerTemplates) {
                if (!identities.add(template.getTemplateIdentity())) {
                    continue;
                }

                DmApprovalTemplateDO cached = new DmApprovalTemplateDO();
                cached.setPrimaryUid(ownerUid);
                cached.setApprovalType(type);
                cached.setTemplateSource(ApprovalTemplateSource.PROVIDER);
                cached.setTemplateName(template.getApproTemplateName());
                cached.setTemplateIdentity(template.getTemplateIdentity());
                cached.setApproUrl(template.getApproUrl());
                cached.setVersion(1L);
                cacheList.add(cached);
            }
        }

        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        transaction.executeWithoutResult(status -> {
            customConfig.lockConfiguration(ownerUid);
            if (type == ApprovalType.Internal) {
                RdpApproTemplateVO builtin = ApprovalFlowService.innerTemplate();
                DmApprovalTemplateDO existing = approvalDal.templateMapper().queryByPrimaryUidAndTypeAndIdentity(ownerUid, type, builtin.getTemplateIdentity());
                if (existing == null) {
                    existing = new DmApprovalTemplateDO();
                    existing.setPrimaryUid(ownerUid);
                    existing.setApprovalType(type);
                    existing.setTemplateIdentity(builtin.getTemplateIdentity());
                    existing.setTemplateName(builtin.getApproTemplateName());
                    existing.setTemplateSource(ApprovalTemplateSource.BUILTIN);
                    existing.setVersion(1L);
                    approvalDal.templateMapper().insert(existing);
                }
            } else {
                approvalDal.templateMapper().deleteByPrimaryUid(ownerUid, type);
                if (!cacheList.isEmpty()) {
                    approvalDal.templateMapper().insertTemplateBatch(cacheList);
                }
            }
        });
        if (type == ApprovalType.Internal) {
            return listTemplates(ownerUid, type);
        }

        List<RdpApproTemplateVO> result = new ArrayList<>();
        for (DmApprovalTemplateDO template : cacheList) {
            RdpApproTemplateVO vo = new RdpApproTemplateVO();
            vo.setApproTemplateName(template.getTemplateName());
            vo.setTemplateIdentity(template.getTemplateIdentity());
            vo.setApproUrl(template.getApproUrl());
            result.add(vo);
        }
        return result;
    }

    public void cancelApprovalInst(Long ticketId) {
        DmApprovalDO ticketDO = approvalDal.approvalMapper().queryById(ticketId);
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        if (approvalService == null) {
            return;
        }

        DmAuthUserDO ticketUser = authDal.userMapper().queryByUid(ticketDO.getOwnerUid());

        ApprovalInstanceCancelInfo cancelInstanceInfo = new ApprovalInstanceCancelInfo();
        cancelInstanceInfo.setTicketUserPhone(ticketUser.getPhone());
        cancelInstanceInfo.setApprovalInstanceIdentity(ticketDO.getApproIdentity());
        cancelInstanceInfo.setApprovalTemplateCode(ticketDO.getApproTemplateIdentity());
        if (StringUtils.isNotEmpty(ticketDO.getApproIdentity())) {
            approvalService.cancelApprovalInst(ticketDO.getPrimaryUid(), cancelInstanceInfo);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void refreshApprovalStatus(long ticketId) {
        DmApprovalDO ticket = approvalDal.approvalMapper().queryById(ticketId);
        if (ApprovalStatus.isEndStatus(ticket.getTicketStatus())) {
            return;
        }

        try {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticket.getApproType().name());
            if (approvalService == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticket.getApproType()));
            }

            ApprovalInstanceInfo lastInfo = approvalService.getLastInfo(ticket.getPrimaryUid(), ticket.getApproIdentity());
            ApprovalInstanceStatus status = lastInfo.getStatus();

            DmApprovalProcessDO processDO = approvalDal.processMapper().queryByStage(ticket.getId(), ApprovalStage.APPROVAL);
            for (Map.Entry<String, List<ApprovalActivity>> stringListEntry : lastInfo.getMap().entrySet()) {
                this.approvalDal.activityMapper().updateContext(processDO.getId(), stringListEntry.getKey(), JsonUtils.toJson(stringListEntry.getValue()));
            }

            switch (status) {
                case CANCELED:
                case TERMINATED: {
                    // step1
                    this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.APPROVAL, ApprovalProcessStatus.CLOSED, null);
                    // step2
                    this.approvalStateService.updateApprovalStatus(ticket.getId(), ApprovalStatus.CANCELED, null);
                    this.approvalDal.processMapper().updateNotEndProcessByTicketId(ticketId, ApprovalProcessStatus.CLOSED);
                    // step3
                    this.approvalHandler(ticket.getApproBiz()).approvalCanceled(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }

                case COMPLETED: {
                    // step1
                    this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.APPROVAL, ApprovalProcessStatus.FINISH, null);
                    // step2
                    this.approvalHandler(ticket.getApproBiz()).approvalApproved(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }

                case REFUSE: {
                    // step1
                    this.approvalStateService.updateProcessStatus(ticketId, ApprovalStage.APPROVAL, ApprovalProcessStatus.REJECT, null);
                    // step2
                    this.approvalStateService.updateApprovalStatus(ticket.getId(), ApprovalStatus.REJECTED, null);
                    this.approvalDal.processMapper().updateNotEndProcessByTicketId(ticketId, ApprovalProcessStatus.REJECT);
                    // step3
                    this.approvalHandler(ticket.getApproBiz()).approvalRejected(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }

                case FAILED: {
                    // step1
                    this.approvalStateService.updateApprovalStatus(ticket.getId(), ApprovalStatus.FAILED, null);
                    this.failedTicket(ticket);
                    // step2
                    this.approvalHandler(ticket.getApproBiz()).approvalFailed(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }
            }

            this.approvalDal.processMapper().updateModified(processDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error("ticketId：{},refreshTicketStatus net error,message：{}", ticketId, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private void failedTicket(DmApprovalDO ticket) {
        List<DmApprovalProcessDO> processList = approvalDal.processMapper().listByTicketId(ticket.getId());
        for (DmApprovalProcessDO processDO : processList) {
            if (processDO.getTicketStage() == ApprovalStage.APPROVAL &&//
                processDO.getProcessStatus() != ApprovalProcessStatus.FINISH) {
                this.approvalStateService.updateProcessStatus(ticket.getId(), processDO.getTicketStage(), ApprovalProcessStatus.FAIL, null);
            } else if (processDO.getTicketStage().ordinal() > ApprovalStage.APPROVAL.ordinal() && //
                       processDO.getProcessStatus() == ApprovalProcessStatus.INIT) {
                this.approvalStateService.updateProcessStatus(ticket.getId(), processDO.getTicketStage(), ApprovalProcessStatus.CLOSED, null);
            }
        }
    }

    private ApprovalHandler approvalHandler(ApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    public DmApprovalTemplateDO checkApprovalAndReturnTemplate(String ownerUid, ApprovalType type, String templateId, Locale locale) {
        if (templateId != null && templateId.startsWith(TEMPLATE_ID_PREFIX)) {
            if (type != ApprovalType.Internal) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CUSTOM_APPROVAL_CONFIG_INVALID.name()));
            }
            return customConfig.lockAndValidateTemplate(ownerUid, templateId);
        }

        if (!this.isApprovalConfigured(ownerUid, type.getProviderType())) {
            if (locale == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_CONFIGURED.name(), type));
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_CONFIGURED.name(), locale, type));
            }
        }

        DmApprovalTemplateDO templateDO = this.approvalDal.templateMapper().queryByPrimaryUidAndTypeAndIdentity(ownerUid, type, templateId);
        if (templateDO == null || templateDO.getApprovalType() != type) {
            if (locale == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name()));
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name(), locale));
            }
        } else {
            return templateDO;
        }
    }

    public void addTemplateByUrl(String ownerUid, ApprovalType type, String templateUrl) {
        if (!this.isApprovalConfigured(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_CONFIGURED.name(), type));
        }

        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }

        if (type == ApprovalType.Feishu) {
            processFeishu(ownerUid, type, templateUrl); // process feishu
        } else if (type == ApprovalType.Wechat) {
            processWeChat(ownerUid, type, templateUrl); // process Wechat
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }
    }

    private void processFeishu(String ownerUid, ApprovalType type, String templateUrl) {
        //  - like https://www.feishu.cn/approval/admin/createApproval?id=7512448164570939393&definitionCode=CA7EB488-A2CA-4F56-A1E5-B226C9E2479E
        if (templateUrl.indexOf("?") > 0) {
            String params = templateUrl.substring(templateUrl.indexOf("?") + 1);
            Map<String, String> map = StringUtils.toMap(params, "&", "=");
            if (map.containsKey("definitionCode")) {
                addAndRefresh(ownerUid, type, RootUserConfig.Fields.feishuApprovalTemplateList, map.get("definitionCode"));
            }
        }
    }

    private void processWeChat(String ownerUid, ApprovalType type, String templateUrl) {
        //  - like https://work.weixin.qq.com/wework_admin/frame#approval_v2/app/120/C4c5FLb3D23jBPxumAfnUv1mk6YESzMfyaHDiuH6d
        String[] split = StringUtils.split(templateUrl, "/");
        if (split.length > 0) {
            String definitionCode = split[split.length - 1].trim();
            addAndRefresh(ownerUid, type, RootUserConfig.Fields.wechatApprovalTemplateList, definitionCode);
        }
    }

    private void addAndRefresh(String ownerUid, ApprovalType type, String templateListKey, String templateId) {
        DmSysUserConfDO configDO = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, templateListKey);
        Set<String> newList = new LinkedHashSet<>();
        if (configDO != null && StringUtils.isNotBlank(configDO.getConfigValue())) {
            for (String str : configDO.getConfigValue().split(",")) {
                newList.add(str.trim());
            }
        }

        newList.add(templateId.trim());
        String configValue = StringUtils.join(newList, ",");

        if (configDO == null) {
            this.systemDal.userConfMapper().insertUserConfig(ownerUid, templateListKey, configValue);
        } else {
            this.systemDal.userConfMapper().updateUserConfig(ownerUid, templateListKey, configValue);
        }

        this.refreshTemplates(ownerUid, type);
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void removeTemplateById(String ownerUid, ApprovalType type, String templateId) {
        if (!this.isApprovalConfigured(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_CONFIGURED.name(), type));
        }

        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }

        switch (type) {
            case Feishu:
                removeAndRefresh(approvalService, ownerUid, type, RootUserConfig.Fields.feishuApprovalTemplateList, templateId);
                return;
            case Wechat:
                removeAndRefresh(approvalService, ownerUid, type, RootUserConfig.Fields.wechatApprovalTemplateList, templateId);
                return;
            default:
                approvalService.useTemplate(ownerUid, templateId, null);
        }
    }

    private void removeAndRefresh(ApprovalProviderSpi approvalService, String ownerUid, ApprovalType type, String templateListKey, String templateId) {
        DmSysUserConfDO configDO = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, templateListKey);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return;
        }

        List<String> newList = new ArrayList<>();
        for (String str : configDO.getConfigValue().split(",")) {
            if (!StringUtils.equals(str.trim(), templateId.trim())) {
                newList.add(str.trim());
            }
        }

        configDO.setConfigValue(StringUtils.join(newList, ","));

        this.systemDal.userConfMapper().updateUserConfig(ownerUid, templateListKey, configDO.getConfigValue());
        this.refreshTemplates(ownerUid, type);
        approvalService.useTemplate(ownerUid, templateId, null);
    }
}
