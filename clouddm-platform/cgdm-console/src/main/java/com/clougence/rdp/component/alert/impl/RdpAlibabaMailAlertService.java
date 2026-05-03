package com.clougence.rdp.component.alert.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.rdp.component.alert.RdpMailAlertService;
import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskService;
import com.clougence.rdp.component.asyntask.RdpAsyncTaskType;
import com.clougence.rdp.component.asyntask.handler.RdpSendEmailTask;
import com.clougence.rdp.component.asyntask.model.AsyncEmailTaskConfig;
import com.clougence.rdp.component.asyntask.model.RdpAsyncTaskConfig;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.vo.RdpUserConfigVO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;
import com.clougence.rdp.service.model.MailDTO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-02-01 11:52
 * @since 1.1.3
 */
@Service(value = "RdpAlibabaMailAlertService")
@Slf4j
public class RdpAlibabaMailAlertService implements RdpMailAlertService {

    @Resource
    @Setter
    private RdpAsyncTaskService     asyncTaskService;

    @Resource
    @Setter
    private RdpUserConfigService    rdpUserConfigService;

    @Resource
    private RdpAlertEventLogService rdpAlertEventLogService;

    @Override
    public SendMsgResult sendMail(MailDTO mailDTO, RdpUserDO sendUser, List<String> receiverUids) {
        SendMsgResult result;

        try {
            if (sendUser != null) {
                List<RdpUserConfigVO> emailConfigs = rdpUserConfigService.queryOneConfigTypeByUid(sendUser.getUid(), UserConfigTagType.EMAIL_CONFIG);
                if (!verifyNecessaryEmailConfig(emailConfigs)) {
                    String errMsg = "Unable to find necessary mailbox configuration, Please configure mailbox configuration in user settings";
                    result = new SendMsgResult(false, errMsg, mailDTO.getContent(), AlertMediaType.MAIL, receiverUids);
                    saveSendLog(result);
                    return result;
                }
            }

            RdpAsyncTaskConfig taskConfig = new RdpAsyncTaskConfig("SEND_EMAIL_TITLE", //
                "SEND_EMAIL_DESC",
                RdpAsyncTaskType.SEND_EMAIL_TASK,
                RdpSendEmailTask.class);

            AsyncEmailTaskConfig configData = new AsyncEmailTaskConfig();
            configData.setMailDTO(mailDTO);
            configData.setUserDO(sendUser);
            configData.setReceiverUids(receiverUids);
            taskConfig.setConfigData(JsonUtils.toJson(configData));

            asyncTaskService.submitTask("system", taskConfig);
        } catch (Exception e) {
            String msg = "Begin to send email failed,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
        } finally {
            if (CollectionUtils.isEmpty(receiverUids)) {
                result = new SendMsgResult(true,
                    null,
                    mailDTO.getContent(),
                    AlertMediaType.MAIL,
                    sendUser == null ? new ArrayList<>() : Collections.singletonList(sendUser.getUid()));
            } else {
                result = new SendMsgResult(true, null, mailDTO.getContent(), AlertMediaType.MAIL, receiverUids);
            }
        }

        return result;
    }

    protected boolean verifyNecessaryEmailConfig(List<RdpUserConfigVO> emailConfigs) {
        if (emailConfigs == null || emailConfigs.size() == 0) {
            return false;
        }

        // emailHost, emailPort, emailUserName and emailPwd must not null
        int signal = 0;
        for (RdpUserConfigVO vo : emailConfigs) {
            if (StringUtils.isNotBlank(vo.getConfigValue())) {
                String confName = vo.getConfigName();
                if (confName.equals(UserDefinedConfig.Fields.emailHost) || confName.equals(UserDefinedConfig.Fields.emailPort)
                    || confName.equals(UserDefinedConfig.Fields.emailUserName) || confName.equals(UserDefinedConfig.Fields.emailPwd)
                    || confName.equals(UserDefinedConfig.Fields.emailFrom)) {
                    signal++;
                }
            }
        }

        return signal == 5;
    }

    protected void saveSendLog(SendMsgResult result) {
        if (result == null) {
            return;
        }

        AlertEventStatus status = result.isSuccess() ? AlertEventStatus.SUCCESS : AlertEventStatus.ERROR;
        rdpAlertEventLogService.save(status, result.getContent(), result.getErrMsg(), result.getMediaType(), result.getSendUids());
    }
}
