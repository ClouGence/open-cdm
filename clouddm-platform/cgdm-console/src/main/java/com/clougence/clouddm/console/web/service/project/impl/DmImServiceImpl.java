package com.clougence.clouddm.console.web.service.project.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.project.ImSenderConfig;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.ImType;
import com.clougence.clouddm.console.web.dal.mapper.DmMessengerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMsgMapper;
import com.clougence.clouddm.console.web.dal.model.DmMessengerDO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImUpdateFO;
import com.clougence.clouddm.console.web.service.project.DmImService;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.messenger.MsgContent;
import com.clougence.clouddm.sdk.messenger.MsgSendResult;
import com.clougence.clouddm.sdk.messenger.MsgSendSpi;
import com.clougence.clouddm.sdk.messenger.MsgSendType;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmImServiceImpl implements DmImService, UnifiedPostConstruct {

    @Resource
    private DmMessengerMapper   dmMessengerMapper;
    @Resource
    private DmProjectMsgMapper  dmProjectMsgMapper;
    @Resource
    private ImSenderService     imSenderService;
    private final List<DmImDef> imDefList = new ArrayList<>();

    @Override
    public void init() throws Exception {
        for (ImType imType : Arrays.stream(ImType.values()).sorted().toArray(ImType[]::new)) {
            MsgSendSpi service = PluginManager.findSpi(MsgSendSpi.class, imType.getProviderType().name());
            if (service == null) {
                continue;
            }

            DmImDef item = new DmImDef();
            item.setImType(imType);
            item.setImTypeI18n(imType.getI18nKey());
            item.setHelpUrl(service.getHelpUrl());
            this.imDefList.add(item);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public List<DmImDef> getImDefList() { return this.imDefList; }

    @Override
    public List<DmMessengerDO> queryImList(String ownerUid) {
        return this.dmMessengerMapper.queryMessengerByOwner(ownerUid);
    }

    @Override
    public List<DmMessengerDO> queryMessengerByOwnerAndType(String ownerUid, ImType imType) {
        return this.dmMessengerMapper.queryMessengerByOwnerAndType(ownerUid, imType);
    }

    @Override
    public DmMessengerDO queryImById(String ownerUid, long imId) {
        return this.dmMessengerMapper.queryImById(ownerUid, imId);
    }

    @Override
    public void addIm(String ownerUid, DevopsImAddFO fo) {
        if (StringUtils.isBlank(fo.getWebhookUrl())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NEED_WEBHOOK.name()));
        }
        if (fo.getImType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NEED_PROVIDER_TYPE.name()));
        }
        List<ImType> defMap = this.imDefList.stream().map(DmImDef::getImType).collect(Collectors.toList());
        if (!defMap.contains(fo.getImType())) {
            String imTypeI18n = DmI18nUtils.getMessage(fo.getImType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), imTypeI18n));
        }
        if (StringUtils.isBlank(fo.getDisplay())) {
            String imTypeI18n = DmI18nUtils.getMessage(fo.getImType().getI18nKey());
            String nowStr = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
            fo.setDisplay(imTypeI18n + "-" + nowStr);
        }
        boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getWebhookUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getWebhookUrl(), "https://");
        if (!urlOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_WEBHOOK_URL_NOT_SUPPORT.name()));
        }

        DmMessengerDO scmDO = new DmMessengerDO();
        scmDO.setOwnerUid(ownerUid);
        scmDO.setImType(fo.getImType());
        scmDO.setImDisplay(fo.getDisplay());
        scmDO.setWebhook(fo.getWebhookUrl());
        scmDO.setSecret(fo.getSecret());
        scmDO.setEnable(true);
        this.dmMessengerMapper.insert(scmDO);
    }

    @Override
    public void deleteImById(String ownerUid, long imId) {
        this.dmMessengerMapper.deleteByOwnerAndId(ownerUid, imId);
        this.dmProjectMsgMapper.disableByOwnerAndImId(ownerUid, imId);
    }

    @Override
    public void updateImById(String ownerUid, DevopsImUpdateFO fo) {
        if (StringUtils.isNotBlank(fo.getNewDisplay())) {
            this.dmMessengerMapper.updateDisplayByOwnerAndId(ownerUid, fo.getImId(), fo.getNewDisplay());
        }
        if (StringUtils.isNotBlank(fo.getNewWebhookUrl())) {
            boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getNewWebhookUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getNewWebhookUrl(), "https://");
            if (!urlOk) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_WEBHOOK_URL_NOT_SUPPORT.name()));
            }
            this.dmMessengerMapper.updateWebhookUrlByOwnerAndId(ownerUid, fo.getImId(), fo.getNewWebhookUrl(), fo.getNewSecret());
        }
    }

    @Override
    public void testImByConfig(String ownerUid, DevopsImAddFO fo) {
        if (StringUtils.isBlank(fo.getWebhookUrl())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NEED_WEBHOOK.name()));
        }
        if (fo.getImType() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_NEED_PROVIDER_TYPE.name()));
        }
        boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getWebhookUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getWebhookUrl(), "https://");
        if (!urlOk) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_WEBHOOK_URL_NOT_SUPPORT.name()));
        }

        String imI18n = DmI18nUtils.getMessage(fo.getImType().getI18nKey());
        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_TEST_MESSAGE.name(), imI18n);
        MsgContent testMessage = new MsgContent();
        testMessage.setMessageId(UUID.randomUUID().toString());
        testMessage.setType(MsgSendType.Text);
        testMessage.setBody(message);
        ImSenderConfig imConfig = ImSenderConfig.builder()//
            .imType(fo.getImType())
            .webhookUrl(fo.getWebhookUrl())
            .secret(fo.getSecret())
            .build();
        MsgSendResult res = this.imSenderService.sendMessage(ownerUid, imConfig, testMessage);
        if (!res.isSuccess()) {
            if (res.getMessage() != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(res.getMessage()));
            } else {
                throw new ErrorMessageException(res.getMessage());
            }
        }
    }
}
