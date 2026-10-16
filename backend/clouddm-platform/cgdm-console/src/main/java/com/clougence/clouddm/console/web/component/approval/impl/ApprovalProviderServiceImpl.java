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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.cicd.ImSenderService;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
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
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 14:16
*/
@Slf4j
@Service
public class ApprovalProviderServiceImpl implements ApprovalRefreshService {
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
    @SneakyThrows
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
    @SneakyThrows
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
        if (originTask == null && activity.getStatus() != ApprovalActivityStatus.CLOSE) {
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

    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        List<Map<String, Object>> list = new ArrayList<>();

        // inner
        Map<String, Object> innerProvider = new HashMap<>();
        innerProvider.put("approvalType", ApprovalType.Internal.name());
        innerProvider.put("i18n", DmI18nUtils.getMessage(ApprovalType.Internal.getI18nKey()));
        innerProvider.put("enable", true);
        innerProvider.put("desc", "");
        list.add(innerProvider);

        List<String> serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);

        for (ApprovalProvider type : ApprovalFlowService.SUPPORT_LIST) {
            Map<String, Object> provider = new HashMap<>();
            provider.put("approvalType", type.name());
            provider.put("i18n", DmI18nUtils.getMessage(ApprovalType.valueOfProvider(type).getI18nKey()));

            // not found plugin
            if (!serviceNames.contains(type.name())) {
                provider.put("enable", false);
                provider.put("desc", DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_NOT_FOUND.name()));
                list.add(provider);
                continue;
            }

            // not enable
            if (!this.checkEnableApproval(ownerUid, type)) {
                provider.put("enable", false);
                provider.put("desc", DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_ENABLE.name()));
                list.add(provider);
                continue;
            }

            ApprovalProviderSpi sdkService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            LifeSpiResponse invoke = sdkService.status(ownerUid, new LifeSpiRequest());
            LifeSpiStatus info = JsonUtils.toObj(invoke.getBody(), LifeSpiStatus.class);
            if (!info.isRunning()) {
                provider.put("enable", false);
                provider.put("desc", DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_STATUS_FAILED.name()));
                list.add(provider);
                continue;
            }

            provider.put("enable", true);
            provider.put("desc", "");
            list.add(provider);
        }

        return list;
    }

    public boolean checkEnableApproval(String ownerUid, ApprovalProvider type) {
        String cfgKey = RdpConvertUtils.convertToApprovalEnableConfigKey(type);
        if (StringUtils.isBlank(cfgKey)) {
            return true;
        }

        DmSysUserConfDO configDO = systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, cfgKey);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(configDO.getConfigValue().trim(), "true");
    }

    public List<RdpApproTemplateVO> listTemplates(String ownerUid, ApprovalType type) {
        if (type != ApprovalType.Internal) {
            List<DmApprovalTemplateDO> templateDOS = approvalDal.templateMapper().listByPrimaryUidAndType(ownerUid, type);
            if (templateDOS.isEmpty()) {
                return refreshTemplates(ownerUid, type);
            } else {
                return templateDOS.stream().map(cacheObj -> {
                    RdpApproTemplateVO vo = new RdpApproTemplateVO();
                    vo.setApproTemplateName(cacheObj.getTemplateName());
                    vo.setTemplateIdentity(cacheObj.getTemplateIdentity());
                    vo.setApproUrl(cacheObj.getApproUrl());
                    return vo;
                }).collect(Collectors.toList());
            }

        } else {
            return Collections.singletonList(ApprovalFlowService.innerTemplate());
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, ApprovalType type) {
        List<RdpApproTemplateVO> voList;
        List<DmApprovalTemplateDO> cacheList;
        if (type != ApprovalType.Internal) {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            if (approvalService == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
            }

            List<ApprovalTemplate> templates = approvalService.getTemplates(ownerUid);
            cacheList = templates.stream().map(template -> {
                DmApprovalTemplateDO templateDO = new DmApprovalTemplateDO();
                templateDO.setPrimaryUid(ownerUid);
                templateDO.setApprovalType(type);
                templateDO.setTemplateName(template.getApproTemplateName());
                templateDO.setTemplateIdentity(template.getTemplateIdentity());
                templateDO.setApproUrl(template.getApproUrl());
                return templateDO;
            }).collect(Collectors.toList());

            voList = cacheList.stream().map(cacheObj -> {
                RdpApproTemplateVO vo = new RdpApproTemplateVO();
                vo.setApproTemplateName(cacheObj.getTemplateName());
                vo.setTemplateIdentity(cacheObj.getTemplateIdentity());
                vo.setApproUrl(cacheObj.getApproUrl());
                return vo;
            }).collect(Collectors.toList());

        } else {
            RdpApproTemplateVO vo = ApprovalFlowService.innerTemplate();
            voList = Collections.singletonList(vo);

            DmApprovalTemplateDO templateDO = new DmApprovalTemplateDO();
            templateDO.setApproUrl(vo.getApproUrl());
            templateDO.setTemplateName(vo.getApproTemplateName());
            templateDO.setApprovalType(ApprovalType.Internal);
            templateDO.setTemplateIdentity(vo.getTemplateIdentity());
            templateDO.setPrimaryUid(ownerUid);

            cacheList = Collections.singletonList(templateDO);
        }
        approvalDal.templateMapper().deleteByPrimaryUid(ownerUid, type);
        if (CollectionUtils.isNotEmpty(cacheList)) {
            approvalDal.templateMapper().insertTemplateBatch(cacheList);
        }
        return voList;
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
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            if (locale == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), locale, type));
            }
        }

        DmApprovalTemplateDO templateDO = this.approvalDal.templateMapper().queryByUidAndTemId(ownerUid, templateId);
        if (templateDO == null) {
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
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
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
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
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
