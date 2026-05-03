package com.clougence.rdp.component.ticket.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.approval.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.component.ticket.RdpTicketHelperService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.vo.RdpApproTemplateVO;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketProcessStatus;
import com.clougence.rdp.dal.enumeration.RdpTicketStage;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.dal.mapper.*;
import com.clougence.rdp.dal.model.*;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalActivityInfo;
import com.clougence.clouddm.sdk.LifeSpiRequest;
import com.clougence.clouddm.sdk.LifeSpiResponse;
import com.clougence.clouddm.sdk.LifeSpiStatus;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 14:16
*/
@Slf4j
@Service
public class RdpApproServiceImpl implements RdpApprovalService {

    @Resource
    private RdpUserMapper                  rdpUserMapper;
    @Resource
    private RdpCacheApproTemplateMapper    approTemplateMapper;
    @Resource
    private RdpUserKvBaseConfigMapper      rdpUserKvBaseConfigMapper;
    @Resource
    private RdpTicketProcessMapper         rdpTicketProcessMapper;
    @Resource
    private RdpTicketMapper                rdpTicketMapper;
    @Resource
    private RdpTicketProcessActivityMapper activityMapper;
    @Resource
    private RdpTicketHelperService         rdpTicketHelperService;
    @Resource
    private RdpCacheApproTemplateMapper    rdpCacheApproTemplateMapper;

    @Override
    public List<Map<String, Object>> getTicketTypes(String ownerUid) {
        List<Map<String, Object>> list = new ArrayList<>();

        // inner
        Map<String, Object> innerProvider = new HashMap<>();
        innerProvider.put("approvalType", RdpApprovalType.Internal.name());
        innerProvider.put("i18n", RdpI18nUtils.getMessage(RdpApprovalType.Internal.getI18nKey()));
        innerProvider.put("enable", true);
        innerProvider.put("desc", "");
        list.add(innerProvider);

        List<String> serviceNames = PluginManager.getSpiNamesByType(ApprovalProviderSpi.class);

        for (ApprovalProvider type : RdpApprovalService.SupportList) {
            Map<String, Object> provider = new HashMap<>();
            provider.put("approvalType", type.name());
            provider.put("i18n", RdpI18nUtils.getMessage(RdpApprovalType.valueOfProvider(type).getI18nKey()));

            // not found plugin
            if (!serviceNames.contains(type.name())) {
                provider.put("enable", false);
                provider.put("desc", RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_NOT_FOUND.name()));
                list.add(provider);
                continue;
            }

            // not enable
            if (!this.checkEnableApproval(ownerUid, type)) {
                provider.put("enable", false);
                provider.put("desc", RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_ENABLE.name()));
                list.add(provider);
                continue;
            }

            ApprovalProviderSpi sdkService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            LifeSpiResponse invoke = sdkService.status(ownerUid, new LifeSpiRequest());
            LifeSpiStatus info = JsonUtils.toObj(invoke.getBody(), LifeSpiStatus.class);
            if (!info.isRunning()) {
                provider.put("enable", false);
                provider.put("desc", RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_PLUGIN_STATUS_FAILED.name()));
                list.add(provider);
                continue;
            }

            provider.put("enable", true);
            provider.put("desc", "");
            list.add(provider);
        }

        return list;
    }

    @Override
    public boolean checkEnableApproval(String ownerUid, ApprovalProvider type) {
        String cfgKey = RdpConvertUtils.convertToApprovalEnableConfigKey(type);
        if (StringUtils.isBlank(cfgKey)) {
            return true;
        }

        RdpUserKvBaseConfigDO configDO = rdpUserKvBaseConfigMapper.queryByUidAndConfigName(ownerUid, cfgKey);
        return configDO == null || StringUtils.equalsIgnoreCase(configDO.getConfigValue().trim(), "true");
    }

    @Override
    public List<RdpApproTemplateVO> listTemplates(String ownerUid, RdpApprovalType type) {
        if (type != RdpApprovalType.Internal) {
            List<RdpCacheApproTemplateDO> templateDOS = approTemplateMapper.listByPrimaryUidAndType(ownerUid, type);
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
            return Collections.singletonList(RdpApprovalService.innerTemplate());
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public List<RdpApproTemplateVO> refreshTemplates(String ownerUid, RdpApprovalType type) {
        List<RdpApproTemplateVO> voList;
        List<RdpCacheApproTemplateDO> cacheList;
        if (type != RdpApprovalType.Internal) {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
            if (approvalService == null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
            }

            List<ApprovalTemplate> templates = approvalService.getTemplates(ownerUid);
            cacheList = templates.stream().map(template -> {
                RdpCacheApproTemplateDO templateDO = new RdpCacheApproTemplateDO();
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
            RdpApproTemplateVO vo = RdpApprovalService.innerTemplate();
            voList = Collections.singletonList(vo);

            RdpCacheApproTemplateDO templateDO = new RdpCacheApproTemplateDO();
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

    @Override
    public void cancelApprovalInst(Long ticketId) {
        RdpTicketDO ticketDO = rdpTicketMapper.queryById(ticketId);
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticketDO.getApproType().name());
        if (approvalService == null) {
            return;
        }

        RdpUserDO ticketUser = rdpUserMapper.queryByUid(ticketDO.getOwnerUid());

        CancelInstanceInfo cancelInstanceInfo = new CancelInstanceInfo();
        cancelInstanceInfo.setTicketUserPhone(ticketUser.getPhone());
        cancelInstanceInfo.setApprovalInstanceIdentity(ticketDO.getApproIdentity());
        cancelInstanceInfo.setApprovalTemplateCode(ticketDO.getApproTemplateIdentity());
        if (StringUtils.isNotEmpty(ticketDO.getApproIdentity())) {
            approvalService.cancelApprovalInst(ticketDO.getPrimaryUid(), cancelInstanceInfo);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void refreshApprovalStatus(long ticketId) {
        RdpTicketDO ticket = rdpTicketMapper.queryById(ticketId);
        if (RdpTicketStatus.isEndStatus(ticket.getTicketStatus())) {
            return;
        }

        try {
            ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, ticket.getApproType().name());
            if (approvalService == null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), ticket.getApproType()));
            }
            ApprovalInstanceInfo lastInfo = approvalService.getLastInfo(ticket.getPrimaryUid(), ticket.getApproIdentity());
            ApprovalInstanceStatus status = lastInfo.getStatus();

            RdpTicketProcessDO processDO = rdpTicketProcessMapper.queryByStage(ticket.getId(), RdpTicketStage.APPROVAL);
            for (Map.Entry<String, List<RdpApprovalActivityInfo>> stringListEntry : lastInfo.getMap().entrySet()) {
                this.activityMapper.updateContext(processDO.getId(), stringListEntry.getKey(), JsonUtils.toJson(stringListEntry.getValue()));
            }

            switch (status) {
                case CANCELED:
                case TERMINATED: {
                    // step1
                    this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.CLOSED, null);
                    // step2
                    this.rdpTicketMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.CANCELED, null);
                    this.rdpTicketProcessMapper.updateNotEndProcessByTicketId(ticketId, RdpTicketProcessStatus.CLOSED);
                    // step3
                    this.rdpTicketHelperService.getTicketHelper(ticket.getApproBiz()).approvalCanceled(ticket.getId());
                    break;
                }
                case COMPLETED: {
                    // step1
                    this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FINISH, null);
                    // step2
                    this.rdpTicketHelperService.getTicketHelper(ticket.getApproBiz()).approvalCompleted(ticket.getId());
                    break;
                }
                case REFUSE: {
                    // step1
                    this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.REJECT, null);
                    // step2
                    this.rdpTicketMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.REJECTED, null);
                    this.rdpTicketProcessMapper.updateNotEndProcessByTicketId(ticketId, RdpTicketProcessStatus.REJECT);
                    // step3
                    this.rdpTicketHelperService.getTicketHelper(ticket.getApproBiz()).approvalRefuse(ticket.getId());
                    break;
                }
                case FAILED: {
                    // step1
                    this.rdpTicketMapper.updateTicketStatusByEnum(ticket.getId(), RdpTicketStatus.FAILED, null);
                    this.failedTicket(ticket);
                    // step2
                    this.rdpTicketHelperService.getTicketHelper(ticket.getApproBiz()).approvalFailed(ticket.getId());
                    break;
                }
            }

            this.rdpTicketProcessMapper.updateModified(processDO.getId());
        } catch (ThirdPartyApiException e) {
            if (e.getErrorType() == ThirdPartyApiErrorType.CONNECTION_ERROR) {
                log.error("ticketId：{},refreshTicketStatus net error,message：{}", ticketId, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private void failedTicket(RdpTicketDO ticket) {
        List<RdpTicketProcessDO> processList = rdpTicketProcessMapper.listByTicketId(ticket.getId());
        for (RdpTicketProcessDO processDO : processList) {
            if (processDO.getProcessStatus() != RdpTicketProcessStatus.FINISH) {
                // update status
                this.rdpTicketProcessMapper.updateTicketStatusByEnum(processDO.getId(), RdpTicketProcessStatus.FAIL, null);
            }
        }
    }

    @Override
    public RdpCacheApproTemplateDO checkApprovalAndReturnTemplate(String ownerUid, RdpApprovalType type, String templateId, Locale locale) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            if (locale == null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
            } else {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), locale, type));
            }
        }

        RdpCacheApproTemplateDO templateDO = this.rdpCacheApproTemplateMapper.queryByUidAndTemId(ownerUid, templateId);
        if (templateDO == null) {
            if (locale == null) {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name()));
            } else {
                throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_TEMPLATE_NOT_EXISTS.name(), locale));
            }
        } else {
            return templateDO;
        }
    }

    @Override
    public void addTemplateByUrl(String ownerUid, RdpApprovalType type, String templateUrl) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
        }
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
        }

        if (type == RdpApprovalType.Feishu) {
            processFeishu(ownerUid, type, templateUrl); // process feishu
        } else if (type == RdpApprovalType.Wechat) {
            processWeChat(ownerUid, type, templateUrl); // process Wechat
        } else {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
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
    @Override
    public void removeTemplateById(String ownerUid, RdpApprovalType type, String templateId) {
        if (!this.checkEnableApproval(ownerUid, type.getProviderType())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_TYPE_NOT_ENABLE.name(), type));
        }
        ApprovalProviderSpi approvalService = PluginManager.findSpi(ApprovalProviderSpi.class, type.name());
        if (approvalService == null) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_APPROVAL_NOT_SUPPORT.name(), type));
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
