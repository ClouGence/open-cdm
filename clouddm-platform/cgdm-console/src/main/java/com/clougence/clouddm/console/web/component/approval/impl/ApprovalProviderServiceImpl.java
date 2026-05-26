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

import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.approval.ApprovalHandler;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.dal.enumeration.*;
import com.clougence.clouddm.console.web.dal.mapper.*;
import com.clougence.clouddm.console.web.dal.model.*;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.clouddm.sdk.approval.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.approval.ApprovalActivity;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 14:16
*/
@Slf4j
@Service
public class ApprovalProviderServiceImpl {

    @Resource
    private RdpUserMapper                              userMapper;
    @Resource
    private DmApprovalCacheTemplateMapper              approTemplateMapper;
    @Resource
    private RdpUserKvBaseConfigMapper                  rdpUserKvBaseConfigMapper;
    @Resource
    private DmApprovalProcessMapper                    approvalProcessMapper;
    @Resource
    private DmApprovalMapper                           approvalMapper;
    @Resource
    private DmApprovalProcessActivityMapper            activityMapper;
    @Resource
    private DmApprovalCacheTemplateMapper              approvalCacheTemplateMapper;
    @Resource
    private ImSenderService                            imSenderService;

    private final Map<RdpApprovalBiz, ApprovalHandler> approvalHandlers;

    public ApprovalProviderServiceImpl(List<ApprovalHandler> approvalHandlers){
        this.approvalHandlers = new EnumMap<>(RdpApprovalBiz.class);
        for (ApprovalHandler approvalHandler : approvalHandlers) {
            RdpApprovalBiz type = approvalHandler.handleType();
            if (this.approvalHandlers.putIfAbsent(type, approvalHandler) != null) {
                throw new IllegalStateException("ApprovalHandler about " + type + " already exists");
            }
        }
    }

    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        List<Map<String, Object>> list = new ArrayList<>();

        // inner
        Map<String, Object> innerProvider = new HashMap<>();
        innerProvider.put("approvalType", RdpApprovalType.Internal.name());
        innerProvider.put("i18n", DmI18nUtils.getMessage(RdpApprovalType.Internal.getI18nKey()));
        innerProvider.put("enable", true);
        innerProvider.put("desc", "");
        list.add(innerProvider);

        List<String> serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);

        for (ApprovalProvider type : ApprovalFlowService.SUPPORT_LIST) {
            Map<String, Object> provider = new HashMap<>();
            provider.put("approvalType", type.name());
            provider.put("i18n", DmI18nUtils.getMessage(RdpApprovalType.valueOfProvider(type).getI18nKey()));

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

        RdpUserKvBaseConfigDO configDO = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, cfgKey);
        if (configDO == null || StringUtils.isBlank(configDO.getConfigValue())) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(configDO.getConfigValue().trim(), "true");
    }

    public List<RdpApproTemplateVO> listTemplates(String ownerUid, RdpApprovalType type) {
        if (type != RdpApprovalType.Internal) {
            List<DmApprovalCacheTemplateDO> templateDOS = approTemplateMapper.listByPrimaryUidAndType(ownerUid, type);
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
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, RdpApprovalType type) {
        List<RdpApproTemplateVO> voList;
        List<DmApprovalCacheTemplateDO> cacheList;
        if (type != RdpApprovalType.Internal) {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            if (approvalService == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
            }

            List<ApprovalTemplate> templates = approvalService.getTemplates(ownerUid);
            cacheList = templates.stream().map(template -> {
                DmApprovalCacheTemplateDO templateDO = new DmApprovalCacheTemplateDO();
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

            DmApprovalCacheTemplateDO templateDO = new DmApprovalCacheTemplateDO();
            templateDO.setApproUrl(vo.getApproUrl());
            templateDO.setTemplateName(vo.getApproTemplateName());
            templateDO.setApprovalType(RdpApprovalType.Internal);
            templateDO.setTemplateIdentity(vo.getTemplateIdentity());
            templateDO.setPrimaryUid(ownerUid);

            cacheList = Collections.singletonList(templateDO);
        }
        approTemplateMapper.deleteByPrimaryUid(ownerUid, type);
        if (CollectionUtils.isNotEmpty(cacheList)) {
            approTemplateMapper.insertTemplateBatch(cacheList);
        }
        return voList;
    }

    public void cancelApprovalInst(Long ticketId) {
        DmApprovalDO ticketDO = approvalMapper.queryById(ticketId);
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        if (approvalService == null) {
            return;
        }

        RdpUserDO ticketUser = userMapper.queryByUid(ticketDO.getOwnerUid());

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
        DmApprovalDO ticket = approvalMapper.queryById(ticketId);
        if (RdpTicketStatus.isEndStatus(ticket.getTicketStatus())) {
            return;
        }

        try {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticket.getApproType().name());
            if (approvalService == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticket.getApproType()));
            }
            ApprovalInstanceInfo lastInfo = approvalService.getLastInfo(ticket.getPrimaryUid(), ticket.getApproIdentity());
            ApprovalInstanceStatus status = lastInfo.getStatus();

            DmApprovalProcessDO processDO = approvalProcessMapper.queryByStage(ticket.getId(), RdpTicketStage.APPROVAL);
            for (Map.Entry<String, List<ApprovalActivity>> stringListEntry : lastInfo.getMap().entrySet()) {
                this.activityMapper.updateContext(processDO.getId(), stringListEntry.getKey(), JsonUtils.toJson(stringListEntry.getValue()));
            }

            switch (status) {
                case CANCELED:
                case TERMINATED: {
                    // step1
                    this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.CLOSED, null);
                    // step2
                    this.approvalMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.CANCELED, null);
                    this.approvalProcessMapper.updateNotEndProcessByTicketId(ticketId, RdpTicketProcessStatus.CLOSED);
                    // step3
                    this.approvalHandler(ticket.getApproBiz()).approvalCanceled(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }
                case COMPLETED: {
                    // step1
                    this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, null);
                    // step2
                    this.approvalHandler(ticket.getApproBiz()).approvalCompleted(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }
                case REFUSE: {
                    // step1
                    this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.REJECT, null);
                    // step2
                    this.approvalMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.REJECTED, null);
                    this.approvalProcessMapper.updateNotEndProcessByTicketId(ticketId, RdpTicketProcessStatus.REJECT);
                    // step3
                    this.approvalHandler(ticket.getApproBiz()).approvalRefuse(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }
                case FAILED: {
                    // step1
                    this.approvalMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.FAILED, null);
                    this.failedTicket(ticket);
                    // step2
                    this.approvalHandler(ticket.getApproBiz()).approvalFailed(ticket.getId(), ticket.getApproBiz(), imSenderService);
                    break;
                }
            }

            this.approvalProcessMapper.updateModified(processDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error("ticketId：{},refreshTicketStatus net error,message：{}", ticketId, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private void failedTicket(DmApprovalDO ticket) {
        List<DmApprovalProcessDO> processList = approvalProcessMapper.listByTicketId(ticket.getId());
        for (DmApprovalProcessDO processDO : processList) {
            if (processDO.getProcessStatus() != RdpTicketProcessStatus.FINISH) {
                // update status
                this.approvalProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FAIL, null);
            }
        }
    }

    private ApprovalHandler approvalHandler(RdpApprovalBiz approvalBiz) {
        ApprovalHandler approvalHandler = this.approvalHandlers.get(approvalBiz);
        if (approvalHandler == null) {
            throw new IllegalStateException("ApprovalHandler about " + approvalBiz + " does not exist");
        }
        return approvalHandler;
    }

    public DmApprovalCacheTemplateDO checkApprovalAndReturnTemplate(String ownerUid, RdpApprovalType type, String templateId, Locale locale) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            if (locale == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
            } else {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), locale, type));
            }
        }

        DmApprovalCacheTemplateDO templateDO = this.approvalCacheTemplateMapper.queryByUidAndTemId(ownerUid, templateId);
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

    public void addTemplateByUrl(String ownerUid, RdpApprovalType type, String templateUrl) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
        }
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }

        if (type == RdpApprovalType.Feishu) {
            processFeishu(ownerUid, type, templateUrl); // process feishu
        } else if (type == RdpApprovalType.Wechat) {
            processWeChat(ownerUid, type, templateUrl); // process Wechat
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }
    }

    private void processFeishu(String ownerUid, RdpApprovalType type, String templateUrl) {
        //  - like https://www.feishu.cn/approval/admin/createApproval?id=7512448164570939393&definitionCode=CA7EB488-A2CA-4F56-A1E5-B226C9E2479E
        if (templateUrl.indexOf("?") > 0) {
            String params = templateUrl.substring(templateUrl.indexOf("?") + 1);
            Map<String, String> map = StringUtils.toMap(params, "&", "=");
            if (map.containsKey("definitionCode")) {
                RdpUserKvBaseConfigDO configDO = this.rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.feishuApprovalTemplateList);
                if (configDO == null) {
                    throw new ErrorMessageException("cannot find config feishuApprovalTemplateList.");
                }

                Set<String> newList = new LinkedHashSet<>();
                for (String str : configDO.getConfigValue().split(",")) {
                    newList.add(str.trim());
                }
                newList.add(map.get("definitionCode").trim());
                configDO.setConfigValue(StringUtils.join(newList, ","));

                this.rdpUserKvBaseConfigMapper.updateUserConfig(ownerUid, UserDefinedConfig.Fields.feishuApprovalTemplateList, configDO.getConfigValue());
                this.refreshTemplates(ownerUid, type);
            }
        }
    }

    private void processWeChat(String ownerUid, RdpApprovalType type, String templateUrl) {
        //  - like https://work.weixin.qq.com/wework_admin/frame#approval_v2/app/120/C4c5FLb3D23jBPxumAfnUv1mk6YESzMfyaHDiuH6d
        String[] split = StringUtils.split(templateUrl, "/");
        if (split.length > 0) {
            String definitionCode = split[split.length - 1].trim();

            RdpUserKvBaseConfigDO configDO = this.rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.wechatApprovalTemplateList);
            if (configDO == null) {
                throw new ErrorMessageException("cannot find config wechatApprovalTemplateList.");
            }

            Set<String> newList = new LinkedHashSet<>();
            for (String str : configDO.getConfigValue().split(",")) {
                newList.add(str.trim());
            }
            newList.add(definitionCode);
            configDO.setConfigValue(StringUtils.join(newList, ","));

            this.rdpUserKvBaseConfigMapper.updateUserConfig(ownerUid, UserDefinedConfig.Fields.wechatApprovalTemplateList, configDO.getConfigValue());
            this.refreshTemplates(ownerUid, type);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void removeTemplateById(String ownerUid, RdpApprovalType type, String templateId) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
        }
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }

        switch (type) {
            case Feishu:
                removeAndRefresh(approvalService, ownerUid, type, UserDefinedConfig.Fields.feishuApprovalTemplateList, templateId);
                return;
            case Wechat:
                removeAndRefresh(approvalService, ownerUid, type, UserDefinedConfig.Fields.wechatApprovalTemplateList, templateId);
                return;
            default:
                approvalService.useTemplate(ownerUid, templateId, null);
        }
    }

    private void removeAndRefresh(ApprovalProviderSpi approvalService, String ownerUid, RdpApprovalType type, String templateListKey, String templateId) {
        RdpUserKvBaseConfigDO configDO = this.rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, templateListKey);
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

        this.rdpUserKvBaseConfigMapper.updateUserConfig(ownerUid, templateListKey, configDO.getConfigValue());
        this.refreshTemplates(ownerUid, type);
        approvalService.useTemplate(ownerUid, templateId, null);
    }
}
