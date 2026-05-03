package com.clougence.rdp.service.impl;

import static com.clougence.rdp.controller.model.enumeration.VerifyType.SMS_VERIFY_CODE;

import java.time.Duration;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.AlarmLevel;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeployMode;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.controller.model.fo.VerifyMO;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.clougence.rdp.dal.enumeration.AreaCode;
import com.clougence.rdp.dal.mapper.RdpUserMapper;
import com.clougence.rdp.dal.mapper.RdpVerifyMapper;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpVerifyDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.exception.ConsoleRuntimeException;
import com.clougence.rdp.service.RdpUserAlertService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.rdp.service.model.MailDTO;
import com.clougence.rdp.util.NamedThreadFactory;
import com.clougence.rdp.util.RandomStrUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/2/27 21:08
 */
@Service
@Slf4j
public class RdpVerifyServiceImpl implements RdpVerifyService, UnifiedPostConstruct {

    @Autowired
    private RdpConsoleConfig    rdpConfig;
    @Resource
    private RdpVerifyMapper     rdpVerifyMapper;
    @Resource
    private RdpUserAlertService rdpUserAlertService;
    @Resource
    private RdpUserMapper       rdpUserMapper;

    @Override
    public void init() {
        ScheduledExecutorService verifyCleanExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("clouddm-auth-integrity-check", true));
        verifyCleanExecutor.scheduleAtFixedRate(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -10);
                rdpVerifyMapper.deleteOldData(calendar.getTime());
                log.info("[Rdp Verify Service] Verify info cleaned.");
            } catch (Throwable e) {
                log.error("Clean verify info failed, but ignore. msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }, 2, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {

    }

    @Override
    public void dropUserVerify(String uid) {
        rdpVerifyMapper.deleteByUid(uid);
    }

    @Override
    public void sendLoginVerifyCode(VerifyMO verifyData) {
        RdpVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyRecord = initGetVerifyByPhone(verifyData.isSub(), verifyData.getAccount(), SMS_VERIFY_CODE, VerifyCodeType.LOGIN, verifyData.getPhoneNumber(), verifyData
                    .getPhoneAreaCode());
                break;
            case EMAIL_VERIFY_CODE:
                verifyRecord = initGetVerifyByMail(verifyData.isSub(), verifyData.getAccount(), VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.LOGIN, verifyData.getEmail());
                break;
            default:
                throw new RuntimeException("unsupported verify type:" + verifyData.getVerifyType());
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyData.getVerifyCodeType(), null, false), fetchEmailMsg(verifyData.getVerifyCodeType(), code, true), verifyRecord);
    }

    @Override
    public void sendSsoBindVerifyCode(VerifyMO verifyData) {
        verifyPhoneEmpty(verifyData.getPhoneNumber());
        RdpVerifyDO verifyRecord = initGetVerifyByPhone(false, null, SMS_VERIFY_CODE, VerifyCodeType.SSO_REGISTER_BIND, verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyData.getVerifyCodeType(), null, false), fetchEmailMsg(verifyData.getVerifyCodeType(), code, true), verifyRecord);
    }

    @Override
    public void sendRegisterVerifyCode(VerifyMO verifyData) {
        // 1. verify phone or email whether is registered
        RdpVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyPhoneEmpty(verifyData.getPhoneNumber());
                RdpUserDO userDOByPhone = rdpUserMapper.queryPrimaryByPhone(verifyData.getPhoneNumber());
                if (userDOByPhone != null) {
                    throw new ConsoleRuntimeException(ConsoleErrorCode.ALREADY_REGISTER, verifyData.getPhoneNumber());
                }

                // 2. check whether try to register other time , if not ,insert a verify DO
                verifyRecord = initGetVerifyByPhone(false, null, SMS_VERIFY_CODE, VerifyCodeType.REGISTER, verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                break;
            case EMAIL_VERIFY_CODE:
                verifyEmailEmpty(verifyData.getEmail());
                RdpUserDO userDOByEmail = rdpUserMapper.queryPrimaryByEmail(verifyData.getEmail());
                if (userDOByEmail != null) {
                    throw new ConsoleRuntimeException(ConsoleErrorCode.ALREADY_REGISTER, verifyData.getEmail());
                }

                // 2. check whether try to register other time , if not ,insert a verify DO
                verifyRecord = initGetVerifyByMail(false, null, VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.REGISTER, verifyData.getEmail());
                break;
            default:
                throw new RuntimeException("unsupported verify type:" + verifyData.getVerifyType());
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyData.getVerifyCodeType(), null, false), fetchEmailMsg(verifyData.getVerifyCodeType(), code, true), verifyRecord);
    }

    @Override
    public void sendResetPasswordVerifyCode(VerifyMO verifyData) {
        if (verifyData.isSub()) {
            RdpUserDO subUser = this.rdpUserMapper.queryBySubAccount(verifyData.getAccount());
            if (subUser != null && !StringUtils.equals(subUser.getPhone(), verifyData.getPhoneNumber())) {
                throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_PHONE_DISAGREE_FIRST);
            }
        }

        RdpVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyRecord = initGetVerifyByPhone(verifyData.isSub(), verifyData.getAccount(), SMS_VERIFY_CODE, VerifyCodeType.RESET_PASSWORD, verifyData
                    .getPhoneNumber(), verifyData.getPhoneAreaCode());
                break;
            case EMAIL_VERIFY_CODE:
                verifyRecord = initGetVerifyByMail(verifyData.isSub(), verifyData.getAccount(), VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.RESET_PASSWORD, verifyData.getEmail());
                break;
            default:
                throw new RuntimeException("unsupported verify type:" + verifyData.getVerifyType());
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyData.getVerifyCodeType(), null, false), fetchEmailMsg(verifyData.getVerifyCodeType(), code, true), verifyRecord);
    }

    private RdpVerifyDO initGetVerifyByPhone(boolean isSubAccount, String subAccount, VerifyType verifyType, VerifyCodeType codeType, String phoneNumber, AreaCode phoneAreaCode) {
        if (VerifyCodeType.REGISTER != codeType && VerifyCodeType.SSO_REGISTER_BIND != codeType) {
            verifyPhoneRegistered(isSubAccount, subAccount, phoneNumber);
        }

        RdpVerifyDO verifyRecord;
        RdpUserDO user;
        if (isSubAccount) {
            user = this.rdpUserMapper.querySubAccountByPhoneAndAccount(phoneNumber, subAccount);
            verifyRecord = this.rdpVerifyMapper.queryByUidAndType(verifyType, codeType, user.getUid());
        } else {
            user = this.rdpUserMapper.queryPrimaryByPhone(phoneNumber);
            verifyRecord = this.rdpVerifyMapper.queryByPrimaryPhone(verifyType, codeType, phoneNumber);
        }

        if (phoneAreaCode == null && user != null) {
            phoneAreaCode = user.getPhoneAreaCode();
        }

        //if user not set phone area code, just as in china
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        if (verifyRecord == null) {
            verifyRecord = new RdpVerifyDO();
            verifyRecord.setAccountType(isSubAccount ? AccountType.SUB_ACCOUNT : AccountType.PRIMARY_ACCOUNT);
            verifyRecord.setPhone(phoneNumber);
            verifyRecord.setPhoneAreaCode(phoneAreaCode);
            verifyRecord.setVerifyCodeType(codeType);
            verifyRecord.setVerifyType(verifyType);
            verifyRecord.setUid(user == null ? "" : user.getUid());

            rdpVerifyMapper.insert(verifyRecord);

            return initGetVerifyByPhone(isSubAccount, subAccount, verifyType, codeType, phoneNumber, phoneAreaCode);
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        return verifyRecord;
    }

    private RdpVerifyDO initGetVerifyByMail(boolean isSubAccount, String subAccount, VerifyType verifyType, VerifyCodeType codeType, String email) {
        if (VerifyCodeType.REGISTER != codeType && VerifyCodeType.SSO_REGISTER_BIND != codeType) {
            verifyEmailRegistered(isSubAccount, subAccount, email);
        }

        RdpVerifyDO verifyRecord;

        RdpUserDO user;
        if (isSubAccount) {
            user = this.rdpUserMapper.querySubAccountByEmailAndAccount(email, subAccount);
            verifyRecord = this.rdpVerifyMapper.queryByUidAndType(verifyType, codeType, user.getUid());
        } else {
            user = this.rdpUserMapper.queryPrimaryByEmail(email);
            verifyRecord = this.rdpVerifyMapper.queryByPrimaryEmail(verifyType, codeType, email);
        }

        if (verifyRecord == null) {
            verifyRecord = new RdpVerifyDO();
            verifyRecord.setAccountType(isSubAccount ? AccountType.SUB_ACCOUNT : AccountType.PRIMARY_ACCOUNT);
            verifyRecord.setEmail(email);
            verifyRecord.setVerifyCodeType(codeType);
            verifyRecord.setVerifyType(verifyType);
            verifyRecord.setUid(user == null ? "" : user.getUid());

            rdpVerifyMapper.insert(verifyRecord);

            return initGetVerifyByMail(isSubAccount, subAccount, verifyType, codeType, email);
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        return verifyRecord;
    }

    @Override
    public void sendSmsVerifyCode(String uid, VerifyCodeType verifyCodeType, String smsTemplateCode) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        AreaCode phoneAreaCode = userDO.getPhoneAreaCode();
        if (phoneAreaCode == null) {
            phoneAreaCode = AreaCode.CHINA;
        }

        verifyPhoneAndAreaCode(userDO.getPhone(), phoneAreaCode);

        RdpVerifyDO verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.SMS_VERIFY_CODE, verifyCodeType, uid);

        if (verifyRecord == null) {
            RdpVerifyDO verifyDO = new RdpVerifyDO();
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setUid(uid);
            verifyDO.setPhone(userDO.getPhone());
            verifyDO.setPhoneAreaCode(phoneAreaCode);
            verifyDO.setVerifyCodeType(verifyCodeType);
            verifyDO.setVerifyType(SMS_VERIFY_CODE);

            rdpVerifyMapper.insert(verifyDO);

            verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.SMS_VERIFY_CODE, verifyCodeType, uid);
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, smsTemplateCode, null, null, verifyRecord);
    }

    protected void verifyPhoneAndAreaCode(String phoneNumber, AreaCode phoneAreaCode) {
        if (StringUtils.isBlank(phoneNumber) || phoneAreaCode == null) {
            throw new IllegalArgumentException("phoneNumber and phoneAreaCode can not be null.");
        }

        if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china && phoneAreaCode != AreaCode.CHINA) {
            throw new RuntimeException("China site not support register without a chinese phone.");
        }
    }

    @Override
    public void sendEmailVerifyCode(String uid, VerifyCodeType verifyCodeType) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("user not exist.");
        }

        boolean isSubAccount = (userDO.getAccountType() != null && userDO.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, userDO.getSubAccount(), userDO.getEmail());

        RdpVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getUid());
        } else {
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getEmail());
        }

        if (verifyRecord == null) {
            RdpVerifyDO verifyDO = new RdpVerifyDO();
            verifyDO.setEmail(userDO.getEmail());
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setVerifyCodeType(verifyCodeType);
            verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
            verifyDO.setUid(uid);
            rdpVerifyMapper.insert(verifyDO);
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getEmail());
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyCodeType, null, false), fetchEmailMsg(verifyCodeType, code, true), verifyRecord);
    }

    protected void sendCode(String code, String smsTemplateCode, String emailSubject, String emailContent, RdpVerifyDO verifyDO) {
        switch (verifyDO.getVerifyType()) {
            case SMS_VERIFY_CODE: {
                AreaCode phoneAreaCode = verifyDO.getPhoneAreaCode();
                if (phoneAreaCode == null) {
                    phoneAreaCode = AreaCode.CHINA;
                }

                log.info("{} verify code persisted for phone {}{} without sending sms.", verifyDO.getVerifyCodeType(), phoneAreaCode.getCode(), verifyDO.getPhone());
                break;
            }
            case EMAIL_VERIFY_CODE: {
                if (GlobalDeploySite.currDeploySite == GlobalDeploySite.china) {
                    throw new RuntimeException("China site not support sending email verify code.");
                }
                // email config not null will send email
                if (StringUtils.isNotBlank(rdpConfig.getEmailHostConfigKey()) && StringUtils.isNotBlank(rdpConfig.getEmailFromConfigKey())
                    && StringUtils.isNotBlank(rdpConfig.getEmailUserNameConfigKey()) && StringUtils.isNotBlank(rdpConfig.getEmailPasswordConfigKey())) {
                    SendMsgResult r = rdpUserAlertService.chooseMailAlertService()
                        .sendMail(MailDTO.builder()
                            .subject(emailSubject)
                            .mailTo(Collections.singletonList(verifyDO.getEmail()))
                            .content(emailContent)
                            .isHtml(true)
                            .build(), null, null);
                    handleSendResult(r);
                    log.info(verifyDO.getVerifyCodeType() + " send code to email " + verifyDO.getEmail() + " successful.");
                }
                break;
            }
            default:
                throw new RuntimeException("unsupported verify type:" + verifyDO.getVerifyType());
        }
    }

    protected void handleSendResult(SendMsgResult r) {
        if (r.isSuccess() || rdpConfig.isProductTrial()) {
            return;
        }

        throw new RuntimeException("Send message error.msg:" + r.getErrMsg());
    }

    private String fetchEmailMsg(VerifyCodeType verifyCodeType, String code, boolean isContent) {
        String htmlTemplate = I18nRdpMsgKeys.EMAIL_CODE_HTML_CONTENT_TEMPLATE.name();
        if (GlobalDeployMode.inCloud()) {
            htmlTemplate = I18nRdpMsgKeys.CLOUD_EMAIL_CODE_HTML_CONTENT_TEMPLATE.name();
        }

        return fetchCloudEmailMsg(verifyCodeType, code, isContent, GlobalDeploySite.rdpProductName(), htmlTemplate);
    }

    private String fetchCloudEmailMsg(VerifyCodeType verifyCodeType, String code, boolean isContent, String productName, String htmlTemplate) {
        switch (verifyCodeType) {
            case LOGIN:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.LOGIN_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case SSO_REGISTER_BIND:
            case REGISTER:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case RESET_PASSWORD:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.RESET_PASSWORD_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.RESET_PASSWORD_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_AUTH_CODE:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.FETCH_AUTH_CODE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.FETCH_AUTH_CODE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_EMAIL:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_EMAIL_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_EMAIL_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case VERIFY_OLD_ACCOUNT:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OLD_ACCOUNT_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.VERIFY_OLD_ACCOUNT_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_USER_AK_SK:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.FETCH_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.FETCH_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case RESET_USER_AK_SK:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.RESET_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.RESET_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_INFO:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_INFO_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_INFO_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_PHONE:
                return isContent ? RdpI18nUtils
                    .getMessage(htmlTemplate, RdpI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_PHONE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_PHONE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_WORKER_DEPLOY_CORE_CONFIG:
                return isContent ? RdpI18nUtils.getMessage(htmlTemplate, RdpI18nUtils
                    .getMessage(I18nRdpMsgKeys.FETCH_WORKER_DEPLOY_CORE_CONFIG_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : RdpI18nUtils
                        .getMessage(I18nRdpMsgKeys.FETCH_WORKER_DEPLOY_CORE_CONFIG_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            default:
                throw new IllegalArgumentException("Unsupported verifyCodeType: " + verifyCodeType);
        }
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void checkVerifyCode(CheckVerifyMO verifyData) {
        if (StringUtils.isBlank(verifyData.getVerifyCode())) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.EMPTY_VERIFY_CODE);
        }

        RdpVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                if (StringUtils.isNotBlank(verifyData.getUid())) {
                    verifyRecord = rdpVerifyMapper.queryByUidAndType(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getUid());
                } else {
                    verifyPhoneAndAreaCode(verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                    if (verifyData.isSubAccount()) {
                        RdpUserDO subUser = this.rdpUserMapper.querySubAccountByPhoneAndAccount(verifyData.getPhoneNumber(), verifyData.getSubAccountName());
                        verifyRecord = rdpVerifyMapper.queryByUidAndType(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), subUser.getUid());
                    } else {
                        verifyRecord = rdpVerifyMapper
                            .queryByPrimaryPhoneAndAreaCode(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                        if (verifyRecord == null) {
                            // history register phone no have area code
                            verifyRecord = rdpVerifyMapper.queryByPrimaryPhone(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getPhoneNumber());
                        }
                    }
                }
                break;
            case EMAIL_VERIFY_CODE:
                if (StringUtils.isNotBlank(verifyData.getUid())) {
                    verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getUid());
                } else {
                    verifyEmailEmpty(verifyData.getEmail());
                    if (verifyData.isSubAccount()) {
                        RdpUserDO subUser = this.rdpUserMapper.querySubAccountByEmailAndAccount(verifyData.getEmail(), verifyData.getSubAccountName());
                        verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), subUser.getUid());
                    } else {
                        verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getEmail());
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported verify type:" + verifyData.getVerifyType());
        }

        if (verifyRecord == null || StringUtils.isBlank(verifyRecord.getVerifyCode())) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.GET_A_VERIFY_CODE_FIRST);
        }

        checkFailTimesAndDate(verifyRecord);

        if (!verifyData.getVerifyCode().equals(verifyRecord.getVerifyCode())) {
            rdpVerifyMapper.updateFailTimesAndDateById(verifyRecord.getFailTimes() + 1, new Date(), verifyRecord.getId());
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_IS_ERROR);
        }

        if (judgeCodeExpired(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_IS_EXPIRED);
        }

        rdpVerifyMapper.updateFailTimesAndDateById(0, null, verifyRecord.getId());
        rdpVerifyMapper.updateVerifyCodeAndSendTime("", null, verifyRecord.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateEmailOrPhoneByUid(String uid, String phone, String email) {
        if (StringUtils.isNotBlank(phone)) {
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.REGISTER, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.LOGIN, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_PASSWORD, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_OP_PASSWORD, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_WORKER_DEPLOY_CORE_CONFIG, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_USER_AK_SK, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_USER_AK_SK, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.PRODUCT_TRIAL, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.DELETE_JOB, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.DELETE_POSITION, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_INFO, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_PHONE, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_EMAIL, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_AUTH_CODE, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.VERIFY_OLD_ACCOUNT, SMS_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.SSO_REGISTER_BIND, SMS_VERIFY_CODE);
        }

        if (StringUtils.isNotBlank(email)) {
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.REGISTER, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.LOGIN, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_PASSWORD, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_OP_PASSWORD, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_WORKER_DEPLOY_CORE_CONFIG, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_USER_AK_SK, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_USER_AK_SK, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.PRODUCT_TRIAL, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.DELETE_JOB, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.DELETE_POSITION, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_INFO, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_PHONE, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_EMAIL, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_AUTH_CODE, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.VERIFY_OLD_ACCOUNT, VerifyType.EMAIL_VERIFY_CODE);
            rdpVerifyMapper.updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.SSO_REGISTER_BIND, VerifyType.EMAIL_VERIFY_CODE);
        }
    }

    @Override
    public void sendVerifyCodeByChangeAccount(VerifyMO verifyData, String uid, VerifyCodeType verifyCodeType, String smsTemplateCode) {
        RdpVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyPhoneAndAreaCode(verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                verifyRecord = rdpVerifyMapper.queryByPrimaryPhoneAndAreaCode(SMS_VERIFY_CODE, verifyCodeType, verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                if (verifyRecord == null) {
                    // history register phone no have area code
                    verifyRecord = rdpVerifyMapper.queryByPrimaryPhone(SMS_VERIFY_CODE, verifyCodeType, verifyData.getPhoneNumber());
                }

                if (verifyRecord == null) {
                    RdpVerifyDO verifyDO = new RdpVerifyDO();
                    verifyDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
                    verifyDO.setUid(uid);
                    verifyDO.setPhone(verifyData.getPhoneNumber());
                    verifyDO.setPhoneAreaCode(verifyData.getPhoneAreaCode());
                    verifyDO.setVerifyCodeType(verifyCodeType);
                    verifyDO.setVerifyType(SMS_VERIFY_CODE);

                    rdpVerifyMapper.insert(verifyDO);
                    verifyRecord = rdpVerifyMapper.queryByPrimaryPhoneAndAreaCode(SMS_VERIFY_CODE, verifyCodeType, verifyData.getPhoneNumber(), verifyData.getPhoneAreaCode());
                }
                break;
            case EMAIL_VERIFY_CODE:
                verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, verifyData.getEmail());
                if (verifyRecord == null) {
                    RdpVerifyDO verifyDO = new RdpVerifyDO();
                    verifyDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
                    verifyDO.setEmail(verifyData.getEmail());
                    verifyDO.setVerifyCodeType(verifyCodeType);
                    verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                    verifyDO.setUid(uid);
                    rdpVerifyMapper.insert(verifyDO);
                    verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, verifyData.getEmail());
                }
                break;
            default:
                throw new RuntimeException("unsupported verify type:" + verifyData.getVerifyType());
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, smsTemplateCode, fetchEmailMsg(verifyCodeType, null, false), fetchEmailMsg(verifyCodeType, code, true), verifyRecord);
    }

    /** judge verify code send frequency to fast */
    protected boolean judgeCodeFrequencyTooFast(RdpVerifyDO verifyRecord) {
        if (verifyRecord.getVerifyCodeSendTime() != null) {
            Calendar now = Calendar.getInstance();
            Calendar sendTime = Calendar.getInstance();
            sendTime.setTime(verifyRecord.getVerifyCodeSendTime());
            Duration duration = Duration.between(sendTime.toInstant(), now.toInstant());
            return duration.getSeconds() < CODE_SEND_FREQUENCY_SECONDS;
        } else {
            return false;
        }
    }

    /** judge verify code is expired */
    protected boolean judgeCodeExpired(RdpVerifyDO verifyRecord) {
        if (verifyRecord.getVerifyCodeSendTime() != null) {
            Calendar now = Calendar.getInstance();
            Calendar sendTime = Calendar.getInstance();
            sendTime.setTime(verifyRecord.getVerifyCodeSendTime());
            Duration duration = Duration.between(sendTime.toInstant(), now.toInstant());
            return duration.getSeconds() > CODE_EXPIRE_MINUTES * 60;
        } else {
            return false;
        }
    }

    /** generate verify code and update login verify code and time */
    protected String generateCodeAndUpdateDbRecord(Long id) {
        String code = RandomStrUtils.fixedLenRandomNumberStr(6);
        if (this.rdpConfig.isProductTrial()) {
            code = this.rdpConfig.getProductTrialVerifyCode();
        }

        rdpVerifyMapper.updateVerifyCodeAndSendTime(code, new Date(), id);
        return code;
    }

    /** check fail times, if exceed the max value and fail datetime less than punish time (or re-count fail times), wait a period time */
    protected void checkFailTimesAndDate(RdpVerifyDO verifyRecord) {
        if (verifyRecord.getFailTimes() > MAX_FAIL_TIME) {
            if (verifyRecord.getLastFailDate() != null) {
                Calendar calendar = Calendar.getInstance();
                Calendar lastFailDate = Calendar.getInstance();
                lastFailDate.setTime(verifyRecord.getLastFailDate());
                Duration d = Duration.between(lastFailDate.toInstant(), calendar.toInstant());
                if (d.getSeconds() < PUNISH_MINUTES_WHEN_EXCEED_MAX_FAIL_TIME * 60) {
                    throw new ConsoleRuntimeException(ConsoleErrorCode.PUNISH_NOT_FINISH_YET,
                        String.valueOf(MAX_FAIL_TIME),
                        String.valueOf(PUNISH_MINUTES_WHEN_EXCEED_MAX_FAIL_TIME));
                } else {
                    // reset it
                    rdpVerifyMapper.updateFailTimesAndDateById(0, null, verifyRecord.getId());
                }
            } else {
                throw new RuntimeException("fail time exceed max value but last fail date is empty.phone:" + verifyRecord.getPhone() + ",email:" + verifyRecord.getEmail());
            }
        }
    }

    protected void verifyEmailEmpty(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("verify email can not be null.");
        }
    }

    protected void verifyPhoneEmpty(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw new IllegalArgumentException("phoneNumber can not be null.");
        }
    }

    protected void verifyEmailRegistered(boolean isSub, String subAccount, String email) {
        verifyEmailEmpty(email);

        RdpUserDO userDO;
        if (isSub) {
            userDO = rdpUserMapper.querySubAccountByEmailAndAccount(email, subAccount);
        } else {
            userDO = rdpUserMapper.queryPrimaryByEmail(email);
        }

        if (userDO == null) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.NEED_REGISTER_FIRST, email);
        }
    }

    protected void verifyPhoneRegistered(boolean isSub, String subAccount, String phoneNumber) {
        verifyPhoneEmpty(phoneNumber);

        RdpUserDO userDO;
        if (isSub) {
            userDO = rdpUserMapper.querySubAccountByPhoneAndAccount(phoneNumber, subAccount);
        } else {
            userDO = rdpUserMapper.queryPrimaryByPhone(phoneNumber);
        }

        if (userDO == null) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.NEED_REGISTER_FIRST, phoneNumber);
        }
    }

    @Override
    public ResWebData<Boolean> verifyMail(String uid) {
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User not exist.");
        }

        boolean isSubAccount = (userDO.getAccountType() != null && userDO.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, userDO.getSubAccount(), userDO.getEmail());

        RdpVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getUid());
        } else {
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getEmail());
        }

        if (verifyRecord == null) {
            RdpVerifyDO verifyDO = new RdpVerifyDO();
            verifyDO.setEmail(userDO.getEmail());
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setVerifyCodeType(VerifyCodeType.VERIFY_EMAIL_TEST);
            verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
            verifyDO.setUid(uid);
            rdpVerifyMapper.insert(verifyDO);
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getEmail());
        }

        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_EMAIL_FREQUENCY_TOO_FAST);
        }

        rdpVerifyMapper.updateVerifyCodeAndSendTime(null, new Date(), verifyRecord.getId());

        MailDTO mailDTO = MailDTO.builder()
            .content(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_CONTENT_MSG.name(), GlobalDeploySite.rdpProductName()))
            .subject(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_SUBJECT_MSG.name(), GlobalDeploySite.rdpProductName()))
            .mailTo(Collections.singletonList(userDO.getEmail()))
            .build();
        SendMsgResult r1 = this.rdpUserAlertService.chooseMailAlertService().sendMail(mailDTO, userDO, Collections.singletonList(userDO.getUid()));
        if (!r1.isSuccess()) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_SEND_ERROR.name(), r1.getErrMsg()));
        } else {
            return ResWebDataUtils.buildSuccess();
        }
    }

    @Override
    public ResWebData<Boolean> verifyIm(String uid, String puid) {
        RdpUserDO receiver = this.rdpUserMapper.queryByUid(uid);
        RdpUserDO owner = receiver;

        if (receiver == null) {
            throw new IllegalArgumentException("User not exist.");
        }

        boolean isSubAccount = (receiver.getAccountType() != null && receiver.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, receiver.getSubAccount(), receiver.getEmail());

        RdpVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = rdpVerifyMapper.queryByUidAndType(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getUid());
            owner = this.rdpUserMapper.queryByUid(puid);
        } else {
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getEmail());
        }

        if (verifyRecord == null) {
            RdpVerifyDO verifyDO = new RdpVerifyDO();
            verifyDO.setEmail(receiver.getEmail());
            verifyDO.setAccountType(receiver.getAccountType());
            verifyDO.setVerifyCodeType(VerifyCodeType.VERIFY_IM_TEST);
            verifyDO.setVerifyType(VerifyType.SMS_VERIFY_CODE);
            verifyDO.setUid(uid);
            rdpVerifyMapper.insert(verifyDO);
            verifyRecord = rdpVerifyMapper.queryByPrimaryEmail(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getEmail());
        }

        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_IM_FREQUENCY_TOO_FAST);
        }

        rdpVerifyMapper.updateVerifyCodeAndSendTime(null, new Date(), verifyRecord.getId());

        String msg = RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_CONTENT_MSG.name(), GlobalDeploySite.rdpProductName());

        boolean imAlertAtAll = this.rdpUserAlertService.fetchUserImAlertAtAll(puid);

        SendMsgResult r1 = this.rdpUserAlertService.chooseImAlertService(puid)
            .sendMsg(buildMsgOwner(), "[MAJOR] " + msg, null, owner, Collections.singletonList(receiver), AlarmLevel.Major, imAlertAtAll);
        if (!r1.isSuccess()) {
            return ResWebDataUtils
                .buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_SEND_ERROR.name(), GlobalDeploySite.rdpProductName(), "[Major Level] " + r1.getErrMsg()));
        }

        SendMsgResult r2 = this.rdpUserAlertService.chooseImAlertService(puid)
            .sendMsg(buildMsgOwner(), "[CRITICAL] " + msg, null, owner, Collections.singletonList(receiver), AlarmLevel.Critical, imAlertAtAll);
        if (!r2.isSuccess()) {
            return ResWebDataUtils
                .buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_SEND_ERROR.name(), GlobalDeploySite.rdpProductName(), "[Critical Level] " + r2.getErrMsg()));
        }

        return ResWebDataUtils.buildSuccess();
    }

    private String buildMsgOwner() {
        return "【" + GlobalDeploySite.rdpProductName() + "】";
    }
}
