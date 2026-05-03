package com.clougence.rdp.service;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.fo.VerifyMO;
import com.clougence.rdp.service.model.CheckVerifyMO;

/**
 * @author bucketli 2020/2/27 20:31
 */
public interface RdpVerifyService {

    int MAX_FAIL_TIME                            = 5;
    int PUNISH_MINUTES_WHEN_EXCEED_MAX_FAIL_TIME = 30;
    int CODE_EXPIRE_MINUTES                      = 20;
    int CODE_SEND_FREQUENCY_SECONDS              = 50;

    // /** when user registered ,init several verify records 1. verify DOs is exist 2. insert DO */
    // void initUserVerify(DmUserDO userDO);

    void dropUserVerify(String uid);

    /**
     * 1. verify phone or email whether is registered 2. verify login fail times ,if exceed the max value and fail datetime less than punish time (or re-count fail times), wait a period time 3.
     * generate verify code and update login verify code and time 4. send SMS or email verify code
     */
    void sendLoginVerifyCode(VerifyMO verifyData);

    /**
     * 1. verify phone or email whether is registered 2. if not, check whether try to register other time , if not ,insert a verify do 3. if yes, check fail times, if exceed the max value and fail
     * datetime less than punish time (or re-count fail times), wait a period time 4. send SMS or email verify code
     */
    void sendRegisterVerifyCode(VerifyMO verifyData);

    void sendResetPasswordVerifyCode(VerifyMO verifyData);

    void sendSmsVerifyCode(String uid, VerifyCodeType verifyCodeType, String smsTemplateCode);

    void sendEmailVerifyCode(String uid, VerifyCodeType verifyCodeType);

    /**
     * 1. query {@link CheckVerifyMO} by phone/email and code. 2. if exist, check fail times,if exceed the max value and fail datetime less than punish time (or re-count fail times), wait a period
     * time 3. if exist and valid ,return true 4. if is empty ,return false
     */
    void checkVerifyCode(CheckVerifyMO checkVerifyPO);

    void updateEmailOrPhoneByUid(String uid, String phone, String email);

    void sendVerifyCodeByChangeAccount(VerifyMO verifyData, String uid, VerifyCodeType verifyCodeType, String smsTemplateCode);

    ResWebData<Boolean> verifyIm(String uid, String puid);

    ResWebData<Boolean> verifyMail(String uid);

    void sendSsoBindVerifyCode(VerifyMO verifyData);
}
