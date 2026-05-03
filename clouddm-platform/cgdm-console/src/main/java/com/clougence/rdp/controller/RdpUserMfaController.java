package com.clougence.rdp.controller;

import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.RefAnyOnes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.fo.mfa.CloseMfaSettingsFO;
import com.clougence.rdp.controller.model.fo.mfa.ConfirmInitMfaSettingsFO;
import com.clougence.rdp.controller.model.fo.mfa.ConfirmResetMfaSettingsFO;
import com.clougence.rdp.controller.model.fo.mfa.ResetMfaSettingsFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpUserMfaService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/mfa")
@Slf4j
public class RdpUserMfaController {

    @Resource
    private RdpUserMfaService rdpUserMfaService;

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/ctrl_initMfaSetting", method = RequestMethod.POST)
    public ResWebData<?> ctrlInitMfaSetting() {
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/initMfaSetting", method = RequestMethod.POST)
    public void initMfaSetting(HttpServletRequest request, HttpServletResponse response) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        byte[] qrCode = rdpUserMfaService.initUserMfaSetting(uid);
        sendPictureToWeb(qrCode, response);
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/confirmInitMfaSetting", method = RequestMethod.POST)
    public ResWebData<?> confirmInitMfaSetting(@Valid @RequestBody ConfirmInitMfaSettingsFO settingsFO, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        rdpUserMfaService.confirmUserMfaSetting(uid, false, Integer.parseInt(settingsFO.getMfaCode()));
        return ResWebDataUtils.buildSuccess("ok");
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/resetMfaSetting", method = RequestMethod.POST)
    public void resetMfaSetting(@Valid @RequestBody ResetMfaSettingsFO fo, HttpServletRequest request, HttpServletResponse response) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        byte[] qrCode = rdpUserMfaService.resetMfaSetting(uid, Integer.parseInt(fo.getMfaCode()));
        sendPictureToWeb(qrCode, response);
    }

    private void sendPictureToWeb(byte[] qrCode, HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=\"qrcode\"");
        response.setContentLength(qrCode.length);

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(qrCode);
            outputStream.flush();
        } catch (Exception e) {
            String errorMsg = "Transport qrcode image error.msg：" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errorMsg, e);
        }
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/confirmResetMfaSetting", method = RequestMethod.POST)
    public ResWebData<?> confirmResetMfaSetting(@Valid @RequestBody ConfirmResetMfaSettingsFO settingsFO, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        rdpUserMfaService.confirmUserMfaSetting(uid, true, Integer.parseInt(settingsFO.getMfaCode()));
        return ResWebDataUtils.buildSuccess("ok");
    }

    @RequestAuth(strategy = RefAnyOnes)
    @RequestMapping(value = "/closeMfaSettings", method = RequestMethod.POST)
    public ResWebData<?> closeMfaSettings(@Valid @RequestBody CloseMfaSettingsFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        rdpUserMfaService.closeUserMfa(uid, Integer.parseInt(fo.getMfaCode()));
        return ResWebDataUtils.buildSuccess("ok");
    }
}
