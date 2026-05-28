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
package com.clougence.rdp.controller;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.RefAnyOnes;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.mfa.CloseMfaSettingsFO;
import com.clougence.clouddm.console.web.model.fo.mfa.ConfirmInitMfaSettingsFO;
import com.clougence.clouddm.console.web.model.fo.mfa.ConfirmResetMfaSettingsFO;
import com.clougence.clouddm.console.web.model.fo.mfa.ResetMfaSettingsFO;
import com.clougence.clouddm.console.web.service.auth.RdpUserMfaService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.utils.ExceptionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
