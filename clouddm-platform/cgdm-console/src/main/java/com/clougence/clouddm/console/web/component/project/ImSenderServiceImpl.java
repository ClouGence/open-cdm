package com.clougence.clouddm.console.web.component.project;

import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.messenger.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.DmMessengerMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectMsgMapper;
import com.clougence.clouddm.console.web.dal.model.DmMessengerDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectMsgDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.rdp.dal.mapper.RdpUserKvBaseConfigMapper;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImSenderServiceImpl implements ImSenderService {

    @Resource
    private DmProjectMapper           dmProjectMapper;
    @Resource
    private DmProjectMsgMapper        dmProjectMsgMapper;
    @Resource
    private DmMessengerMapper         dmMessengerMapper;
    @Resource
    private RdpUserKvBaseConfigMapper userConfigMapper;

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
        DmProjectMsgDO msgDO = this.dmProjectMsgMapper.queryMessageByProjectId(ownerUid, projectId);
        if (msgDO != null && StringUtils.isNotBlank(msgDO.getLanguage())) {
            return msgDO.getLanguage();
        }

        RdpUserKvBaseConfigDO defaultLanguage = this.userConfigMapper.queryByUidAndConfigName(ownerUid, UserDefinedConfig.Fields.defaultLanguage);
        if (defaultLanguage == null || StringUtils.isBlank(defaultLanguage.getConfigValue())) {
            return "zh_CN";
        } else {
            return defaultLanguage.getConfigValue();
        }
    }

    @Override
    public void sendMessage(String ownerUid, long projectId, ImMessageType messageType, MsgContent message) {
        DmProjectDO project = this.dmProjectMapper.queryByOwnerAndId(ownerUid, projectId);
        if (project == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_NOT_EXIST_ERROR.name()));
        }
        DmProjectMsgDO msgDO = this.dmProjectMsgMapper.queryMessageByProjectId(ownerUid, projectId);
        if (msgDO == null || !msgDO.isEnable()) {
            String msg = DmI18nUtils.getMessage(I18nDmMsgKeys.PROJECT_IM_NOT_AVAILABLE_MESSAGE.name(), project.getProjectName());
            this.sendDone(ownerUid, message, MsgSendResult.failed(message.getMessageId(), msg));
            return;
        }

        if (!messageType.testEnable(msgDO)) {
            return;
        }

        DmMessengerDO messengerDO = this.dmMessengerMapper.queryImById(ownerUid, msgDO.getRefMsgId());
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
