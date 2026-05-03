package com.clougence.rdp.component.asyntask.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.component.asyntask.RdpAsyncTask;
import com.clougence.rdp.component.asyntask.model.AsyncEmailTaskConfig;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.vo.RdpUserConfigVO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;
import com.clougence.rdp.service.model.MailDTO;
import com.clougence.rdp.util.RdpJacksonUtil;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * email Task
 * @version : 2023-09-24
 */
@Slf4j
@Service
@Scope("prototype")
public class RdpSendEmailTask extends RdpAsyncTask {

    private static final Integer    FAILED_RETRY_COUNT = 3;

    @Resource
    @Setter
    private RdpUserConfigService    rdpUserConfigService;

    @Resource
    @Setter
    private RdpConsoleConfig        rdpConfig;

    @Resource
    private RdpAlertEventLogService rdpAlertEventLogService;

    @Override
    protected void executeTask(int doCnt, String configData) {
        AsyncEmailTaskConfig config = RdpJacksonUtil.readJsonWithUnknown(configData, AsyncEmailTaskConfig.class);
        sendMailInner(config.getMailDTO(), config.getUserDO(), config.getReceiverUids());
    }

    private void sendMailInner(MailDTO mailDTO, RdpUserDO userDO, List<String> receiverUids) {
        int retry = 0;

        while (retry < FAILED_RETRY_COUNT) {
            try {
                SendMsgResult result = sendMail(mailDTO, userDO, receiverUids);
                if (result.isSuccess()) {
                    break;
                }
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                String msg = "Send email failed,retry count: " + retry + ",msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
            }

            retry++;
        }
    }

    private SendMsgResult sendMail(MailDTO mailDTO, RdpUserDO sendUser, List<String> receiverUids) {
        if (!validate(mailDTO)) {
            String msg = "Send email error. mails or subject or content empty.mail to:" + StringUtils.join(mailDTO.getMailTo(), ", ") + ",subject:" + mailDTO.getSubject()
                         + ",mail content:" + mailDTO.getContent();
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        SendMsgResult result;
        try {
            JavaMailSenderImpl mailSender;
            if (sendUser == null) {
                mailSender = genSystemMailSender();
            } else {
                mailSender = initMailSenderByConfig(sendUser);
            }

            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
            messageHelper.setFrom(getMailSendFrom(mailSender), getMailDisPlayName(mailSender));
            messageHelper.setTo(mailDTO.getMailTo().toArray(new String[0]));
            messageHelper.setSubject(mailDTO.getSubject());
            messageHelper.setText(mailDTO.getContent(), mailDTO.isHtml());
            if (CollectionUtils.isNotEmpty(mailDTO.getCc())) {
                messageHelper.setCc(mailDTO.getCc().toArray(new String[0]));
            }

            if (CollectionUtils.isNotEmpty(mailDTO.getBcc())) {
                messageHelper.setBcc(mailDTO.getBcc().toArray(new String[0]));
            }

            if (mailDTO.getMultipartFiles() != null) {
                for (MultipartFile file : mailDTO.getMultipartFiles()) {
                    messageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
                }
            }

            mailSender.send(messageHelper.getMimeMessage());

            if (CollectionUtils.isEmpty(receiverUids)) {
                result = new SendMsgResult(true,
                    null,
                    mailDTO.getContent(),
                    AlertMediaType.MAIL,
                    sendUser == null ? new ArrayList<>() : Collections.singletonList(sendUser.getUid()));
            } else {
                result = new SendMsgResult(true, null, mailDTO.getContent(), AlertMediaType.MAIL, receiverUids);
            }
        } catch (Exception e) {
            String errMsg = "Mail send error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            if (CollectionUtils.isEmpty(receiverUids)) {
                result = new SendMsgResult(false,
                    errMsg,
                    mailDTO.getContent(),
                    AlertMediaType.MAIL,
                    sendUser == null ? new ArrayList<>() : Collections.singletonList(sendUser.getUid()));
            } else {
                result = new SendMsgResult(false, errMsg, mailDTO.getContent(), AlertMediaType.MAIL, receiverUids);
            }
        }

        saveSendLog(result);
        return result;
    }

    protected void saveSendLog(SendMsgResult result) {
        if (result == null) {
            return;
        }

        AlertEventStatus status = result.isSuccess() ? AlertEventStatus.SUCCESS : AlertEventStatus.ERROR;
        rdpAlertEventLogService.save(status, result.getContent(), result.getErrMsg(), result.getMediaType(), result.getSendUids());
    }

    protected boolean validate(MailDTO mailDTO) {
        return CollectionUtils.isNotEmpty(mailDTO.getMailTo()) && StringUtils.isNotBlank(mailDTO.getContent()) && StringUtils.isNotBlank(mailDTO.getSubject());
    }

    protected JavaMailSenderImpl genSystemMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.getJavaMailProperties().setProperty("mail.smtp.auth", rdpConfig.getEmailSmtpAuthKey());
        mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", rdpConfig.getEmailSmtpStarttlsEnableKey());
        mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.required", rdpConfig.getEmailSmtpStarttlsRequiredKey());
        mailSender.getJavaMailProperties().setProperty("mail.smtp.ssl.enable", rdpConfig.getEmailSmtpSslEnableKey());
        mailSender.getJavaMailProperties().setProperty("mail.transport.protocol", rdpConfig.getEmailTransportProtocolKey());
        mailSender.setHost(rdpConfig.getEmailHostConfigKey());
        mailSender.setPort(Integer.parseInt(rdpConfig.getEmailPortConfigKey()));
        mailSender.setUsername(rdpConfig.getEmailUserNameConfigKey());
        mailSender.setPassword(rdpConfig.getEmailPasswordConfigKey());
        mailSender.getJavaMailProperties().setProperty("from", rdpConfig.getEmailFromConfigKey());
        mailSender.getJavaMailProperties().setProperty("display", rdpConfig.getEmailDisplayConfigKey());
        return mailSender;
    }

    protected JavaMailSenderImpl initMailSenderByConfig(RdpUserDO sendUser) {
        List<RdpUserConfigVO> emailConfigs = rdpUserConfigService.queryOneConfigTypeByUid(sendUser.getUid(), UserConfigTagType.EMAIL_CONFIG);

        if (!verifyNecessaryEmailConfig(emailConfigs)) {
            return null;
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        for (RdpUserConfigVO eConfig : emailConfigs) {
            switch (eConfig.getConfigName()) {
                case UserDefinedConfig.Fields.emailHost: {
                    mailSender.setHost(eConfig.getConfigValue());
                    break;
                }
                case UserDefinedConfig.Fields.emailPort: {
                    mailSender.setPort(Integer.parseInt(eConfig.getConfigValue()));
                    break;
                }
                case UserDefinedConfig.Fields.emailUserName: {
                    mailSender.setUsername(eConfig.getConfigValue());
                    break;
                }
                case UserDefinedConfig.Fields.emailPwd: {
                    mailSender.setPassword(eConfig.getConfigValue());
                    break;
                }
                case UserDefinedConfig.Fields.emailFrom: {
                    mailSender.getJavaMailProperties().setProperty("from", eConfig.getConfigValue());
                    break;
                }
                case UserDefinedConfig.Fields.emailDisplay: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue())) {
                        mailSender.getJavaMailProperties().setProperty("display", eConfig.getConfigValue());
                    }
                    break;
                }
                case UserDefinedConfig.Fields.emailSmtpAuth: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue()) && StringUtils.equals(eConfig.getConfigValue(), "true")) {
                        mailSender.getJavaMailProperties().setProperty("mail.smtp.auth", "true");
                    }
                    break;
                }
                case UserDefinedConfig.Fields.emailEnableTls: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue()) && StringUtils.equals(eConfig.getConfigValue(), "true")) {
                        mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
                    }
                    break;
                }
                case UserDefinedConfig.Fields.emailRequiredTls: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue()) && StringUtils.equals(eConfig.getConfigValue(), "true")) {
                        mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.required", "true");
                    }
                    break;
                }
                case UserDefinedConfig.Fields.emailEnableSsl: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue()) && StringUtils.equals(eConfig.getConfigValue(), "true")) {
                        mailSender.getJavaMailProperties().setProperty("mail.smtp.ssl.enable", "true");
                    }
                    break;
                }
                case UserDefinedConfig.Fields.emailProtocol: {
                    if (StringUtils.isNotBlank(eConfig.getConfigValue())) {
                        mailSender.getJavaMailProperties().setProperty("mail.transport.protocol", eConfig.getConfigValue());
                    }
                    break;
                }
            }
        }
        return mailSender;
    }

    public boolean verifyNecessaryEmailConfig(List<RdpUserConfigVO> emailConfigs) {
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

    protected String getMailSendFrom(JavaMailSenderImpl mailSender) {
        return mailSender.getJavaMailProperties().getProperty("from");
    }

    protected String getMailDisPlayName(JavaMailSenderImpl mailSender) {
        return mailSender.getJavaMailProperties().getProperty("display");
    }
}
