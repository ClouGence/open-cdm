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
package com.clougence.clouddm.console.web.controller.ai;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletionException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineListFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineSaveFO;
import com.clougence.clouddm.console.web.model.fo.ai.AiEngineVersionFO;
import com.clougence.clouddm.console.web.service.ai.AiEngineProbeService;
import com.clougence.clouddm.console.web.service.ai.AiEngineService;
import com.clougence.clouddm.console.web.service.ai.AiRunHandle;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.ds.ai.ChatProtocolConstants;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(DmControllerUrlPrefix.CONSOLE_PREFIX + "/ai/engine")
@RequestAuth(SecRoleAuthLabel.DM_AI_ENGINE_MANAGE)
public class AiEngineController {
    @Resource
    private AiEngineService      engines;
    @Resource
    private AiEngineProbeService probes;

    @PostMapping("/listEngines")
    public ResWebData<?> listEngines(@Valid @RequestBody AiEngineListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(engines.list(puid, uid, fo));
    }

    @PostMapping("/saveEngine")
    public ResWebData<?> saveEngine(@Valid @RequestBody AiEngineSaveFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(engines.save(puid, uid, fo));
    }

    @PostMapping("/deleteEngine")
    public ResWebData<?> deleteEngine(@Valid @RequestBody AiEngineVersionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        engines.delete(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(Map.of(AiSqlConstants.FIELD_DELETED, true));
    }

    @PostMapping("/testEngine")
    public DeferredResult<ResWebData<?>> testEngine(@Valid @RequestBody AiEngineVersionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        AiRunHandle handle = new AiRunHandle(Duration.ofSeconds(30));
        DeferredResult<ResWebData<?>> result = new DeferredResult<>(35000L);
        result.onTimeout(() -> {
            handle.cancel();
            result.setErrorResult(new ErrorMessageException(ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT, DmI18nUtils.getMessage(ChatProtocolConstants.ERROR_PROVIDER_TIMEOUT)));
        });
        result.onError(error -> handle.cancel());
        result.onCompletion(handle::cancel);
        probes.test(puid, uid, fo, handle).whenComplete((value, error) -> {
            if (error != null) {
                Throwable cause = error;
                if (error instanceof CompletionException && error.getCause() != null) {
                    cause = error.getCause();
                }
                result.setErrorResult(cause);
            } else {
                result.setResult(ResWebDataUtils.buildSuccess(value));
            }
        });
        return result;
    }
}
