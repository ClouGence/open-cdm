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
package com.clougence.rdp.service.impl;

import static com.clougence.clouddm.platform.dal.model.auth.VerifyType.SMS_VERIFY_CODE;

import java.time.Duration;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ConsoleErrorCode;
import com.clougence.clouddm.api.common.exception.ConsoleRuntimeException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.AlarmLevel;
import com.clougence.clouddm.base.metadata.rdp.enumeration.GlobalDeploySite;
import com.clougence.clouddm.console.web.component.alert.model.SendMsgResult;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.model.fo.VerifyMO;
import com.clougence.clouddm.console.web.util.NamedThreadFactory;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.rdp.service.RdpUserAlertService;
import com.clougence.rdp.service.RdpVerifyService;
import com.clougence.rdp.service.model.CheckVerifyMO;
import com.clougence.rdp.service.model.MailDTO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/2/27 21:08
 */
@Service
@Slf4j
public class RdpVerifyServiceImpl implements RdpVerifyService, UnifiedPostConstruct {

    @Autowired
    private DmConsoleConfig     rdpConfig;
    @Resource
    private AuthDal             authDal;

    @Resource
    private RdpUserAlertService rdpUserAlertService;

    @Override
    public void init() {
        ScheduledExecutorService verifyCleanExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("clouddm-auth-integrity-check", true));
        verifyCleanExecutor.scheduleAtFixedRate(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -10);
                authDal.verifyMapper().deleteOldData(calendar.getTime());
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
        authDal.verifyMapper().deleteByUid(uid);
    }

    @Override
    public void sendLoginVerifyCode(VerifyMO verifyData) {
        DmAuthVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyRecord = initGetVerifyByPhone(verifyData.isSub(), verifyData.getAccount(), SMS_VERIFY_CODE, VerifyCodeType.LOGIN, verifyData.getPhoneNumber());
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
        DmAuthVerifyDO verifyRecord = initGetVerifyByPhone(false, null, SMS_VERIFY_CODE, VerifyCodeType.SSO_REGISTER_BIND, verifyData.getPhoneNumber());
        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyData.getVerifyCodeType(), null, false), fetchEmailMsg(verifyData.getVerifyCodeType(), code, true), verifyRecord);
    }

    @Override
    public void sendRegisterVerifyCode(VerifyMO verifyData) {
        // 1. verify phone or email whether is registered
        DmAuthVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyPhoneEmpty(verifyData.getPhoneNumber());
                DmAuthUserDO userDOByPhone = authDal.userMapper().queryPrimaryByPhone(verifyData.getPhoneNumber());
                if (userDOByPhone != null) {
                    throw new ConsoleRuntimeException(ConsoleErrorCode.ALREADY_REGISTER, verifyData.getPhoneNumber());
                }

                // 2. check whether try to register other time , if not ,insert a verify DO
                verifyRecord = initGetVerifyByPhone(false, null, SMS_VERIFY_CODE, VerifyCodeType.REGISTER, verifyData.getPhoneNumber());
                break;
            case EMAIL_VERIFY_CODE:
                verifyEmailEmpty(verifyData.getEmail());
                DmAuthUserDO userDOByEmail = authDal.userMapper().queryPrimaryByEmail(verifyData.getEmail());
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
            DmAuthUserDO subUser = this.authDal.userMapper().queryBySubAccount(verifyData.getAccount());
            if (subUser != null && !StringUtils.equals(subUser.getPhone(), verifyData.getPhoneNumber())) {
                throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_PHONE_DISAGREE_FIRST);
            }
        }

        DmAuthVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyRecord = initGetVerifyByPhone(verifyData.isSub(), verifyData.getAccount(), SMS_VERIFY_CODE, VerifyCodeType.RESET_PASSWORD, verifyData.getPhoneNumber());
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

    private DmAuthVerifyDO initGetVerifyByPhone(boolean isSubAccount, String subAccount, VerifyType verifyType, VerifyCodeType codeType, String phoneNumber) {
        if (VerifyCodeType.REGISTER != codeType && VerifyCodeType.SSO_REGISTER_BIND != codeType) {
            verifyPhoneRegistered(isSubAccount, subAccount, phoneNumber);
        }

        DmAuthVerifyDO verifyRecord;
        DmAuthUserDO user;
        if (isSubAccount) {
            user = this.authDal.userMapper().querySubAccountByPhoneAndAccount(phoneNumber, subAccount);
            verifyRecord = this.authDal.verifyMapper().queryByUidAndType(verifyType, codeType, user.getUid());
        } else {
            user = this.authDal.userMapper().queryPrimaryByPhone(phoneNumber);
            verifyRecord = this.authDal.verifyMapper().queryByPrimaryPhone(verifyType, codeType, phoneNumber);
        }

        if (verifyRecord == null) {
            verifyRecord = new DmAuthVerifyDO();
            verifyRecord.setAccountType(isSubAccount ? AccountType.SUB_ACCOUNT : AccountType.PRIMARY_ACCOUNT);
            verifyRecord.setPhone(phoneNumber);
            verifyRecord.setVerifyCodeType(codeType);
            verifyRecord.setVerifyType(verifyType);
            verifyRecord.setUid(user == null ? "" : user.getUid());

            authDal.verifyMapper().insert(verifyRecord);

            return initGetVerifyByPhone(isSubAccount, subAccount, verifyType, codeType, phoneNumber);
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        return verifyRecord;
    }

    private DmAuthVerifyDO initGetVerifyByMail(boolean isSubAccount, String subAccount, VerifyType verifyType, VerifyCodeType codeType, String email) {
        if (VerifyCodeType.REGISTER != codeType && VerifyCodeType.SSO_REGISTER_BIND != codeType) {
            verifyEmailRegistered(isSubAccount, subAccount, email);
        }

        DmAuthVerifyDO verifyRecord;

        DmAuthUserDO user;
        if (isSubAccount) {
            user = this.authDal.userMapper().querySubAccountByEmailAndAccount(email, subAccount);
            verifyRecord = this.authDal.verifyMapper().queryByUidAndType(verifyType, codeType, user.getUid());
        } else {
            user = this.authDal.userMapper().queryPrimaryByEmail(email);
            verifyRecord = this.authDal.verifyMapper().queryByPrimaryEmail(verifyType, codeType, email);
        }

        if (verifyRecord == null) {
            verifyRecord = new DmAuthVerifyDO();
            verifyRecord.setAccountType(isSubAccount ? AccountType.SUB_ACCOUNT : AccountType.PRIMARY_ACCOUNT);
            verifyRecord.setEmail(email);
            verifyRecord.setVerifyCodeType(codeType);
            verifyRecord.setVerifyType(verifyType);
            verifyRecord.setUid(user == null ? "" : user.getUid());

            authDal.verifyMapper().insert(verifyRecord);

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
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        verifyPhoneEmpty(userDO.getPhone());

        DmAuthVerifyDO verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.SMS_VERIFY_CODE, verifyCodeType, uid);

        if (verifyRecord == null) {
            DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setUid(uid);
            verifyDO.setPhone(userDO.getPhone());
            verifyDO.setVerifyCodeType(verifyCodeType);
            verifyDO.setVerifyType(SMS_VERIFY_CODE);

            authDal.verifyMapper().insert(verifyDO);

            verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.SMS_VERIFY_CODE, verifyCodeType, uid);
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, smsTemplateCode, null, null, verifyRecord);
    }

    @Override
    public void sendEmailVerifyCode(String uid, VerifyCodeType verifyCodeType) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("user not exist.");
        }

        boolean isSubAccount = (userDO.getAccountType() != null && userDO.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, userDO.getSubAccount(), userDO.getEmail());

        DmAuthVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getUid());
        } else {
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getEmail());
        }

        if (verifyRecord == null) {
            DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
            verifyDO.setEmail(userDO.getEmail());
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setVerifyCodeType(verifyCodeType);
            verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
            verifyDO.setUid(uid);
            authDal.verifyMapper().insert(verifyDO);
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, userDO.getEmail());
        }

        checkFailTimesAndDate(verifyRecord);
        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_FREQUENCY_TOO_FAST);
        }

        String code = generateCodeAndUpdateDbRecord(verifyRecord.getId());
        sendCode(code, null, fetchEmailMsg(verifyCodeType, null, false), fetchEmailMsg(verifyCodeType, code, true), verifyRecord);
    }

    protected void sendCode(String code, String smsTemplateCode, String emailSubject, String emailContent, DmAuthVerifyDO verifyDO) {
        switch (verifyDO.getVerifyType()) {
            case SMS_VERIFY_CODE: {
                log.info("{} verify code persisted for phone {} without sending sms.", verifyDO.getVerifyCodeType(), verifyDO.getPhone());
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
        if (r.success() || rdpConfig.isProductTrial()) {
            return;
        }

        throw new RuntimeException("Send message error.msg:" + r.errMsg());
    }

    private String fetchEmailMsg(VerifyCodeType verifyCodeType, String code, boolean isContent) {
        String htmlTemplate = I18nRdpMsgKeys.EMAIL_CODE_HTML_CONTENT_TEMPLATE.name();
        return fetchCloudEmailMsg(verifyCodeType, code, isContent, GlobalDeploySite.rdpProductName(), htmlTemplate);
    }

    private String fetchCloudEmailMsg(VerifyCodeType verifyCodeType, String code, boolean isContent, String productName, String htmlTemplate) {
        switch (verifyCodeType) {
            case LOGIN:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.LOGIN_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.LOGIN_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case SSO_REGISTER_BIND:
            case REGISTER:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.REGISTER_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case RESET_PASSWORD:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.RESET_PASSWORD_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.RESET_PASSWORD_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_AUTH_CODE:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.FETCH_AUTH_CODE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.FETCH_AUTH_CODE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_EMAIL:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_EMAIL_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_EMAIL_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case VERIFY_OLD_ACCOUNT:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_OLD_ACCOUNT_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.VERIFY_OLD_ACCOUNT_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_USER_AK_SK:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.FETCH_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.FETCH_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case RESET_USER_AK_SK:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.RESET_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.RESET_USER_AK_SK_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_INFO:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_INFO_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_INFO_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case UPDATE_USER_PHONE:
                return isContent ? DmI18nUtils
                    .getMessage(htmlTemplate, DmI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_PHONE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
                        .getMessage(I18nRdpMsgKeys.UPDATE_USER_PHONE_EMAIL_VERIFY_CODE_SUBJECT.name(), productName);
            case FETCH_WORKER_DEPLOY_CORE_CONFIG:
                return isContent ? DmI18nUtils.getMessage(htmlTemplate, DmI18nUtils
                    .getMessage(I18nRdpMsgKeys.FETCH_WORKER_DEPLOY_CORE_CONFIG_EMAIL_VERIFY_CODE_SUBJECT.name(), productName), code) : DmI18nUtils
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

        DmAuthVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                if (StringUtils.isNotBlank(verifyData.getUid())) {
                    verifyRecord = authDal.verifyMapper().queryByUidAndType(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getUid());
                } else {
                    verifyPhoneEmpty(verifyData.getPhoneNumber());
                    if (verifyData.isSubAccount()) {
                        DmAuthUserDO subUser = this.authDal.userMapper().querySubAccountByPhoneAndAccount(verifyData.getPhoneNumber(), verifyData.getSubAccountName());
                        verifyRecord = authDal.verifyMapper().queryByUidAndType(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), subUser.getUid());
                    } else {
                        verifyRecord = authDal.verifyMapper().queryByPrimaryPhone(SMS_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getPhoneNumber());
                    }
                }
                break;
            case EMAIL_VERIFY_CODE:
                if (StringUtils.isNotBlank(verifyData.getUid())) {
                    verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getUid());
                } else {
                    verifyEmailEmpty(verifyData.getEmail());
                    if (verifyData.isSubAccount()) {
                        DmAuthUserDO subUser = this.authDal.userMapper().querySubAccountByEmailAndAccount(verifyData.getEmail(), verifyData.getSubAccountName());
                        verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), subUser.getUid());
                    } else {
                        verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyData.getVerifyCodeType(), verifyData.getEmail());
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
            authDal.verifyMapper().updateFailTimesAndDateById(verifyRecord.getFailTimes() + 1, new Date(), verifyRecord.getId());
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_IS_ERROR);
        }

        if (judgeCodeExpired(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_CODE_IS_EXPIRED);
        }

        authDal.verifyMapper().updateFailTimesAndDateById(0, null, verifyRecord.getId());
        authDal.verifyMapper().updateVerifyCodeAndSendTime("", null, verifyRecord.getId());
    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateEmailOrPhoneByUid(String uid, String phone, String email) {
        if (StringUtils.isNotBlank(phone)) {
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.REGISTER, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.LOGIN, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_PASSWORD, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_OP_PASSWORD, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_WORKER_DEPLOY_CORE_CONFIG, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_USER_AK_SK, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.RESET_USER_AK_SK, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.PRODUCT_TRIAL, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.DELETE_JOB, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.DELETE_POSITION, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_INFO, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_PHONE, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.UPDATE_USER_EMAIL, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.FETCH_AUTH_CODE, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.VERIFY_OLD_ACCOUNT, SMS_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, phone, null, VerifyCodeType.SSO_REGISTER_BIND, SMS_VERIFY_CODE);
        }

        if (StringUtils.isNotBlank(email)) {
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.REGISTER, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.LOGIN, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_PASSWORD, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_OP_PASSWORD, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_WORKER_DEPLOY_CORE_CONFIG, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_USER_AK_SK, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.RESET_USER_AK_SK, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.PRODUCT_TRIAL, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.DELETE_JOB, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.DELETE_POSITION, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_INFO, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_PHONE, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.UPDATE_USER_EMAIL, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.FETCH_AUTH_CODE, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.VERIFY_OLD_ACCOUNT, VerifyType.EMAIL_VERIFY_CODE);
            authDal.verifyMapper().updatePhoneOrEmailByUid(uid, null, email, VerifyCodeType.SSO_REGISTER_BIND, VerifyType.EMAIL_VERIFY_CODE);
        }
    }

    @Override
    public void sendVerifyCodeByChangeAccount(VerifyMO verifyData, String uid, VerifyCodeType verifyCodeType, String smsTemplateCode) {
        DmAuthVerifyDO verifyRecord;
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                verifyPhoneEmpty(verifyData.getPhoneNumber());
                verifyRecord = authDal.verifyMapper().queryByPrimaryPhone(SMS_VERIFY_CODE, verifyCodeType, verifyData.getPhoneNumber());

                if (verifyRecord == null) {
                    DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
                    verifyDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
                    verifyDO.setUid(uid);
                    verifyDO.setPhone(verifyData.getPhoneNumber());
                    verifyDO.setVerifyCodeType(verifyCodeType);
                    verifyDO.setVerifyType(SMS_VERIFY_CODE);

                    authDal.verifyMapper().insert(verifyDO);
                    verifyRecord = authDal.verifyMapper().queryByPrimaryPhone(SMS_VERIFY_CODE, verifyCodeType, verifyData.getPhoneNumber());
                }
                break;
            case EMAIL_VERIFY_CODE:
                verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, verifyData.getEmail());
                if (verifyRecord == null) {
                    DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
                    verifyDO.setAccountType(AccountType.PRIMARY_ACCOUNT);
                    verifyDO.setEmail(verifyData.getEmail());
                    verifyDO.setVerifyCodeType(verifyCodeType);
                    verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
                    verifyDO.setUid(uid);
                    authDal.verifyMapper().insert(verifyDO);
                    verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, verifyCodeType, verifyData.getEmail());
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
    protected boolean judgeCodeFrequencyTooFast(DmAuthVerifyDO verifyRecord) {
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
    protected boolean judgeCodeExpired(DmAuthVerifyDO verifyRecord) {
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

        authDal.verifyMapper().updateVerifyCodeAndSendTime(code, new Date(), id);
        return code;
    }

    /** check fail times, if exceed the max value and fail datetime less than punish time (or re-count fail times), wait a period time */
    protected void checkFailTimesAndDate(DmAuthVerifyDO verifyRecord) {
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
                    authDal.verifyMapper().updateFailTimesAndDateById(0, null, verifyRecord.getId());
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

        DmAuthUserDO userDO;
        if (isSub) {
            userDO = authDal.userMapper().querySubAccountByEmailAndAccount(email, subAccount);
        } else {
            userDO = authDal.userMapper().queryPrimaryByEmail(email);
        }

        if (userDO == null) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.NEED_REGISTER_FIRST, email);
        }
    }

    protected void verifyPhoneRegistered(boolean isSub, String subAccount, String phoneNumber) {
        verifyPhoneEmpty(phoneNumber);

        DmAuthUserDO userDO;
        if (isSub) {
            userDO = authDal.userMapper().querySubAccountByPhoneAndAccount(phoneNumber, subAccount);
        } else {
            userDO = authDal.userMapper().queryPrimaryByPhone(phoneNumber);
        }

        if (userDO == null) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.NEED_REGISTER_FIRST, phoneNumber);
        }
    }

    @Override
    public ResWebData<Boolean> verifyMail(String uid) {
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            throw new IllegalArgumentException("User not exist.");
        }

        boolean isSubAccount = (userDO.getAccountType() != null && userDO.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, userDO.getSubAccount(), userDO.getEmail());

        DmAuthVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getUid());
        } else {
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getEmail());
        }

        if (verifyRecord == null) {
            DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
            verifyDO.setEmail(userDO.getEmail());
            verifyDO.setAccountType(userDO.getAccountType());
            verifyDO.setVerifyCodeType(VerifyCodeType.VERIFY_EMAIL_TEST);
            verifyDO.setVerifyType(VerifyType.EMAIL_VERIFY_CODE);
            verifyDO.setUid(uid);
            authDal.verifyMapper().insert(verifyDO);
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.EMAIL_VERIFY_CODE, VerifyCodeType.VERIFY_EMAIL_TEST, userDO.getEmail());
        }

        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_EMAIL_FREQUENCY_TOO_FAST);
        }

        authDal.verifyMapper().updateVerifyCodeAndSendTime(null, new Date(), verifyRecord.getId());

        MailDTO mailDTO = MailDTO.builder()
            .content(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_CONTENT_MSG.name(), GlobalDeploySite.rdpProductName()))
            .subject(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_SUBJECT_MSG.name(), GlobalDeploySite.rdpProductName()))
            .mailTo(Collections.singletonList(userDO.getEmail()))
            .build();
        SendMsgResult r1 = this.rdpUserAlertService.chooseMailAlertService().sendMail(mailDTO, userDO, Collections.singletonList(userDO.getUid()));
        if (!r1.success()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_EMAIL_SEND_ERROR.name(), r1.errMsg()));
        } else {
            return ResWebDataUtils.buildSuccess();
        }
    }

    @Override
    public ResWebData<Boolean> verifyIm(String uid, String puid) {
        DmAuthUserDO receiver = this.authDal.userMapper().queryByUid(uid);
        DmAuthUserDO owner = receiver;

        if (receiver == null) {
            throw new IllegalArgumentException("User not exist.");
        }

        boolean isSubAccount = (receiver.getAccountType() != null && receiver.getAccountType() == AccountType.SUB_ACCOUNT);
        verifyEmailRegistered(isSubAccount, receiver.getSubAccount(), receiver.getEmail());

        DmAuthVerifyDO verifyRecord;
        if (isSubAccount) {
            verifyRecord = authDal.verifyMapper().queryByUidAndType(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getUid());
            owner = this.authDal.userMapper().queryByUid(puid);
        } else {
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getEmail());
        }

        if (verifyRecord == null) {
            DmAuthVerifyDO verifyDO = new DmAuthVerifyDO();
            verifyDO.setEmail(receiver.getEmail());
            verifyDO.setAccountType(receiver.getAccountType());
            verifyDO.setVerifyCodeType(VerifyCodeType.VERIFY_IM_TEST);
            verifyDO.setVerifyType(VerifyType.SMS_VERIFY_CODE);
            verifyDO.setUid(uid);
            authDal.verifyMapper().insert(verifyDO);
            verifyRecord = authDal.verifyMapper().queryByPrimaryEmail(VerifyType.SMS_VERIFY_CODE, VerifyCodeType.VERIFY_IM_TEST, receiver.getEmail());
        }

        if (judgeCodeFrequencyTooFast(verifyRecord)) {
            throw new ConsoleRuntimeException(ConsoleErrorCode.VERIFY_IM_FREQUENCY_TOO_FAST);
        }

        authDal.verifyMapper().updateVerifyCodeAndSendTime(null, new Date(), verifyRecord.getId());

        String msg = DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_CONTENT_MSG.name(), GlobalDeploySite.rdpProductName());

        boolean imAlertAtAll = this.rdpUserAlertService.fetchUserImAlertAtAll(puid);

        SendMsgResult r1 = this.rdpUserAlertService.chooseImAlertService(puid)
            .sendMsg(buildMsgOwner(), "[MAJOR] " + msg, null, owner, Collections.singletonList(receiver), AlarmLevel.Major, imAlertAtAll);
        if (!r1.success()) {
            return ResWebDataUtils
                .buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_SEND_ERROR.name(), GlobalDeploySite.rdpProductName(), "[Major Level] " + r1.errMsg()));
        }

        SendMsgResult r2 = this.rdpUserAlertService.chooseImAlertService(puid)
            .sendMsg(buildMsgOwner(), "[CRITICAL] " + msg, null, owner, Collections.singletonList(receiver), AlarmLevel.Critical, imAlertAtAll);
        if (!r2.success()) {
            return ResWebDataUtils
                .buildError(DmI18nUtils.getMessage(I18nRdpMsgKeys.VERIFY_IM_SEND_ERROR.name(), GlobalDeploySite.rdpProductName(), "[Critical Level] " + r2.errMsg()));
        }

        return ResWebDataUtils.buildSuccess();
    }

    private String buildMsgOwner() {
        return "【" + GlobalDeploySite.rdpProductName() + "】";
    }
}
