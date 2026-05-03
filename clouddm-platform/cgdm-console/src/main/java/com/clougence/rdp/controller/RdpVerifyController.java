package com.clougence.rdp.controller;

import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.fo.SendCodeAfterLoginFO;
import com.clougence.rdp.controller.model.fo.SendCodeByAccountFO;
import com.clougence.rdp.controller.model.fo.SendCodeFO;
import com.clougence.rdp.controller.model.fo.VerifyMO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.service.RdpVerifyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-14 21:36
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/verify")
@Slf4j
public class RdpVerifyController {

    @Resource
    private RdpVerifyService rdpVerifyService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcode", method = RequestMethod.POST)
    public ResWebData<?> sendCode(@Valid @RequestBody SendCodeFO sendCodeFO) {
        VerifyMO m = new VerifyMO();
        m.setAccount(sendCodeFO.getAccount());
        m.setSub(sendCodeFO.isSub());
        m.setEmail(sendCodeFO.getEmail());
        m.setPhoneNumber(sendCodeFO.getPhoneNumber());
        m.setPhoneAreaCode(sendCodeFO.getPhoneAreaCode());
        m.setVerifyType(sendCodeFO.getVerifyType());
        m.setVerifyCodeType(sendCodeFO.getVerifyCodeType());

        switch (sendCodeFO.getVerifyCodeType()) {
            case LOGIN:
                rdpVerifyService.sendLoginVerifyCode(m);
                break;
            case REGISTER:
                rdpVerifyService.sendRegisterVerifyCode(m);
                break;
            case RESET_PASSWORD:
                rdpVerifyService.sendResetPasswordVerifyCode(m);
                break;
            case SSO_REGISTER_BIND:
                rdpVerifyService.sendSsoBindVerifyCode(m);
                break;
            default:
                throw new RuntimeException("unsupported verify code type:" + m.getVerifyCodeType());
        }

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcodeinloginstate", method = RequestMethod.POST)
    public ResWebData<?> sendCodeInLoginState(@Valid @RequestBody SendCodeAfterLoginFO verifyData, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        switch (verifyData.getVerifyType()) {
            case SMS_VERIFY_CODE:
                rdpVerifyService.sendSmsVerifyCode(uid, verifyData.getVerifyCodeType(), null);
                break;
            case EMAIL_VERIFY_CODE:
                rdpVerifyService.sendEmailVerifyCode(uid, verifyData.getVerifyCodeType());
                break;
            default:
                throw new RuntimeException("unsupported verify type:" + verifyData.getVerifyType());
        }

        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcodebyaccount", method = RequestMethod.POST)
    public ResWebData<?> sendCodeByAccount(@RequestBody @Valid SendCodeByAccountFO sendCodeFO, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        VerifyMO m = new VerifyMO();
        m.setEmail(sendCodeFO.getEmail());
        m.setPhoneNumber(sendCodeFO.getPhoneNumber());
        m.setPhoneAreaCode(sendCodeFO.getPhoneAreaCode());
        m.setVerifyType(sendCodeFO.getVerifyType());
        m.setVerifyCodeType(sendCodeFO.getVerifyCodeType());

        rdpVerifyService.sendVerifyCodeByChangeAccount(m, uid, sendCodeFO.getVerifyCodeType(), null);
        return ResWebDataUtils.buildSuccess();
    }
}
