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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.project.ImSenderConfig;
import com.clougence.clouddm.console.web.component.project.ImSenderService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImUpdateFO;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.ImType;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.messenger.MsgContent;
import com.clougence.clouddm.sdk.messenger.MsgSendResult;
import com.clougence.clouddm.sdk.messenger.MsgSendSpi;
import com.clougence.clouddm.sdk.messenger.MsgSendType;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DmImServiceImpl implements DmImService, UnifiedPostConstruct {
    @Resource
    private SystemDal           systemDal;
    @Resource
    private ProjectDal          projectDal;
    @Resource
    private ImSenderService     senderService;
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
    public List<DmSysMessengerDO> queryImList(String ownerUid) {
        return this.systemDal.messengerMapper().queryMessengerByOwner(ownerUid);
    }

    @Override
    public List<DmSysMessengerDO> queryMessengerByOwnerAndType(String ownerUid, ImType imType) {
        return this.systemDal.messengerMapper().queryMessengerByOwnerAndType(ownerUid, imType);
    }

    @Override
    public DmSysMessengerDO queryImById(String ownerUid, long imId) {
        return this.systemDal.messengerMapper().queryImById(ownerUid, imId);
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

        DmSysMessengerDO scmDO = new DmSysMessengerDO();
        scmDO.setOwnerUid(ownerUid);
        scmDO.setImType(fo.getImType());
        scmDO.setImDisplay(fo.getDisplay());
        scmDO.setWebhook(fo.getWebhookUrl());
        scmDO.setSecret(fo.getSecret());
        scmDO.setEnable(true);
        this.systemDal.messengerMapper().insert(scmDO);
    }

    @Override
    public void deleteImById(String ownerUid, long imId) {
        this.systemDal.messengerMapper().deleteByOwnerAndId(ownerUid, imId);
        this.projectDal.msgMapper().disableByOwnerAndImId(ownerUid, imId);
    }

    @Override
    public void updateImById(String ownerUid, DevopsImUpdateFO fo) {
        if (StringUtils.isNotBlank(fo.getNewDisplay())) {
            this.systemDal.messengerMapper().updateDisplayByOwnerAndId(ownerUid, fo.getImId(), fo.getNewDisplay());
        }
        if (StringUtils.isNotBlank(fo.getNewWebhookUrl())) {
            boolean urlOk = StringUtils.startsWithIgnoreCase(fo.getNewWebhookUrl(), "http://") || StringUtils.startsWithIgnoreCase(fo.getNewWebhookUrl(), "https://");
            if (!urlOk) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_WEBHOOK_URL_NOT_SUPPORT.name()));
            }
            this.systemDal.messengerMapper().updateWebhookUrlByOwnerAndId(ownerUid, fo.getImId(), fo.getNewWebhookUrl(), fo.getNewSecret());
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
        MsgSendResult res = this.senderService.sendMessage(ownerUid, imConfig, testMessage);
        if (!res.isSuccess()) {
            if (res.getMessage() != null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(res.getMessage()));
            } else {
                throw new ErrorMessageException(res.getMessage());
            }
        }
    }
}
