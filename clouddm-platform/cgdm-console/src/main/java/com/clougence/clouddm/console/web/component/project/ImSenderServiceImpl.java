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
package com.clougence.clouddm.console.web.component.project;

import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ProjectDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectMsgDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.messenger.*;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImSenderServiceImpl implements ImSenderService {
    @Resource
    private SystemDal  systemDal;
    @Resource
    private ProjectDal projectDal;

    @Override
    public void sendMessage(String ownerUid, long projectId, ImMessageType imMessageType, Function<Locale, String> msgFunction) {
        String language = this.getProjectLanguage(ownerUid, projectId);
        Locale locale = I18nUtils.getLocale(language);
        String textMsg = msgFunction.apply(locale);
        this.sendMessage(ownerUid, projectId, imMessageType, textMsg);
    }

    @Override
    public void sendMessage(String ownerUid, long projectId, ImMessageType imMessageType, String textMsg) {
        if (StringUtils.isBlank(textMsg)) {
            return;
        }

        MsgContent message = new MsgContent();
        message.setMessageId(UUID.randomUUID().toString());
        message.setBody(textMsg);
        message.setType(MsgSendType.Text);
        this.sendMessage(ownerUid, projectId, imMessageType, message);
    }

    @Override
    public String getProjectLanguage(String ownerUid, long projectId) {
        DmProjectMsgDO msgDO = this.projectDal.msgMapper().queryMessageByProjectId(ownerUid, projectId);
        if (msgDO != null && StringUtils.isNotBlank(msgDO.getLanguage())) {
            return msgDO.getLanguage();
        }

        DmSysUserConfDO defaultLanguage = this.systemDal.userConfMapper().queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.defaultLanguage);
        if (defaultLanguage == null || StringUtils.isBlank(defaultLanguage.getConfigValue())) {
            return "zh_CN";
        } else {
            return defaultLanguage.getConfigValue();
        }
    }

    @Override
    public void sendMessage(String ownerUid, long projectId, ImMessageType messageType, MsgContent message) {
        DmProjectDO project = this.projectDal.projectMapper().queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        DmProjectMsgDO msgDO = this.projectDal.msgMapper().queryMessageByProjectId(ownerUid, projectId);
        if (msgDO == null || !msgDO.isEnable()) {
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IM_NOT_AVAILABLE_MESSAGE.name(), project.getProjectName());
            this.sendDone(ownerUid, message, MsgSendResult.failed(message.getMessageId(), msg));
            return;
        }

        if (!messageType.testEnable(msgDO)) {
            return;
        }

        DmSysMessengerDO messengerDO = this.systemDal.messengerMapper().queryImById(ownerUid, msgDO.getRefMsgId());
        ImSenderConfig imConfig = ImSenderConfig.builder()//
            .imType(messengerDO.getImType())
            .webhookUrl(messengerDO.getWebhook())
            .secret(messengerDO.getSecret())
            .build();

        this.sendMessage(ownerUid, imConfig, message);
    }

    @Override
    public MsgSendResult sendMessage(String ownerUid, ImSenderConfig imConfig, MsgContent message) {
        MsgSendSpi service = PluginManager.findSpi(MsgSendSpi.class, imConfig.getImType().getProviderType().name());
        if (service == null) {
            String imTypeI18n = DmI18nUtils.getMessage(imConfig.getImType().getI18nKey());
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_MISSING_PROVIDER.name(), imTypeI18n));
        }

        try {
            this.sendRecord(ownerUid, message);

            MsgSendConfig config = new MsgSendConfig();
            config.setWebhookUrl(imConfig.getWebhookUrl());
            config.setSecret(imConfig.getSecret());
            MsgSendResult result = service.sendMessage(config, message);
            this.sendDone(ownerUid, message, result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MsgSendResult result = MsgSendResult.failed(message.getMessageId(), e.getMessage());
            this.sendDone(ownerUid, message, result);
            return result;
        }
    }

    private void sendRecord(String ownerUid, MsgContent message) {
        // TODO 将消息记录到数据库
    }

    private void sendDone(String ownerUid, MsgContent message, MsgSendResult result) {
        // TODO 更新消息发送到状态
    }
}
