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
package com.clougence.clouddm.console.web.component.alert.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.alert.RdpMailAlertService;
import com.clougence.clouddm.console.web.component.alert.model.SendMsgResult;
import com.clougence.clouddm.console.web.component.alert.task.AsyncEmailTaskConfig;
import com.clougence.clouddm.console.web.component.alert.task.RdpSendEmailTask;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.console.web.model.vo.RdpUserConfigVO;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskService;
import com.clougence.clouddm.console.web.service.auth.RdpUserConfigService;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.monitor.AlertEventStatus;
import com.clougence.clouddm.platform.dal.model.monitor.AlertMediaType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.model.MailDTO;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
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
    private AsyncTaskService        asyncTaskService;
    @Resource
    @Setter
    private RdpUserConfigService    rdpUserConfigService;
    @Resource
    private RdpAlertEventLogService rdpAlertEventLogService;

    @Override
    public SendMsgResult sendMail(MailDTO mailDTO, DmAuthUserDO sendUser, List<String> receiverUids) {
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

            AsyncTaskConfig taskConfig = new AsyncTaskConfig();
            taskConfig.setTitle("SEND_EMAIL_TITLE");
            taskConfig.setDescription("SEND_EMAIL_DESC");
            taskConfig.setBizId(UUID.randomUUID().toString().replace("-", ""));
            taskConfig.setBizType("SEND_EMAIL_TASK");
            taskConfig.setHandlerType(RdpSendEmailTask.class);
            taskConfig.setShowInDock(false);
            taskConfig.setProcessType(AsyncTaskProcessType.PROGRESS);

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

        AlertEventStatus status = result.success() ? AlertEventStatus.SUCCESS : AlertEventStatus.ERROR;
        rdpAlertEventLogService.save(status, result.content(), result.errMsg(), result.mediaType(), result.sendUids());
    }
}
