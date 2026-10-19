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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.config.AiProperties;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.ai.AiError;
import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.model.fo.ai.*;
import com.clougence.clouddm.console.web.model.vo.ai.AiEngineConfigVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiModelVO;
import com.clougence.clouddm.console.web.model.vo.ai.AiTurnRecordVO;
import com.clougence.clouddm.console.web.service.ai.*;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.util.SseResponseUtils;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(DmControllerUrlPrefix.CONSOLE_PREFIX + "/ai/turn")
public class AiTurnController {
    @Resource
    private DmAuthServiceForBiz authorization;
    @Resource
    private AiEngineService     engines;
    @Resource
    private AiProperties        limits;
    @Resource
    private AiSessionService    sessions;
    @Resource
    private AiTurnStore         store;
    @Resource
    private AiTurnService       turns;

    @PostMapping("/getCapabilities")
    @RequestAuth(strategy = RequestAuth.AuthStrategy.RefAnyOnes)
    public ResWebData<?> getCapabilities(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        boolean generate = authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_CONSOLE);
        List<AiModelVO> models = List.of();
        String reason = null;
        if (!generate) {
            reason = AiError.FORBIDDEN.name();

        } else {
            models = engines.models(puid, uid);
            if (models.isEmpty()) {
                reason = AiError.ENGINE_UNAVAILABLE.name();
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(AiSqlConstants.FIELD_CAN_MANAGE_ENGINES, authorization.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_AI_ENGINE_MANAGE));
        result.put(AiSqlConstants.FIELD_CAN_GENERATE, generate);
        result.put(AiSqlConstants.FIELD_MODELS, models);
        result.put(AiSqlConstants.FIELD_CONTEXT_TOKENS, limits.getContextTokens());
        result.put(AiSqlConstants.FIELD_GENERATION_AVAILABLE, reason == null);
        result.put(AiSqlConstants.FIELD_UNAVAILABLE_CODE, reason);
        result.put(AiSqlConstants.FIELD_LIMITS, Map.of(AiSqlConstants.FIELD_PROMPT_CHARACTERS, 16384, AiSqlConstants.FIELD_TURN_SECONDS, limits.getTurnSeconds()));
        return ResWebDataUtils.buildSuccess(result);
    }

    @PostMapping("/createSession")
    @RequestAuth(SecRoleAuthLabel.DM_QUERY_CONSOLE)
    public ResWebData<?> createSession(@Valid @RequestBody AiCreateSessionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        AiEngineConfigVO engine = engines.requireAvailable(puid, uid, fo.getEngineId(), fo.getEngineVersion());
        return ResWebDataUtils.buildSuccess(sessions.create(puid, uid, engine));
    }

    @PostMapping("/switchSessionModel")
    @RequestAuth(SecRoleAuthLabel.DM_QUERY_CONSOLE)
    public ResWebData<?> switchSessionModel(@Valid @RequestBody AiSwitchModelFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(sessions.switchModel(puid, uid, fo));
    }

    @PostMapping("/listSessions")
    @RequestAuth(strategy = RequestAuth.AuthStrategy.RefAnyOnes)
    public ResWebData<?> listSessions(@Valid @RequestBody AiSessionListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(sessions.list(puid, uid, fo));
    }

    @PostMapping("/getSession")
    @RequestAuth(strategy = RequestAuth.AuthStrategy.RefAnyOnes)
    public ResWebData<?> getSession(@Valid @RequestBody AiGetSessionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        return ResWebDataUtils.buildSuccess(sessions.get(puid, uid, fo));
    }

    @PostMapping("/cancelTurn")
    @RequestAuth(strategy = RequestAuth.AuthStrategy.RefAnyOnes)
    public ResWebData<?> cancelTurn(@Valid @RequestBody AiCancelTurnFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        AiTurnRecordVO turn = turns.cancel(puid, uid, fo.getSessionId(), fo.getRequestId());
        return ResWebDataUtils.buildSuccess(Map.of(AiSqlConstants.FIELD_TURN_ID, turn.getId(), AiSqlConstants.FIELD_STATUS, turn.getStatus()));
    }

    @PostMapping("/deleteSession")
    @RequestAuth(strategy = RequestAuth.AuthStrategy.RefAnyOnes)
    public ResWebData<?> deleteSession(@Valid @RequestBody AiSessionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        store.delete(puid, uid, fo.getSessionId());
        return ResWebDataUtils.buildSuccess(Map.of(AiSqlConstants.FIELD_DELETED, true));
    }

    @PostMapping(value = "/streamTurn", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequestAuth(SecRoleAuthLabel.DM_QUERY_CONSOLE)
    public SseEmitter streamTurn(@Valid @RequestBody AiTurnFO fo, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        SseResponseUtils.applyStreamingHeaders(response);
        AiEventSink sink = turns.start(puid, uid, fo);
        return prepareStreamResponse(sink, request);
    }

    @PostMapping(value = "/resumeTurn", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequestAuth(SecRoleAuthLabel.DM_QUERY_CONSOLE)
    public SseEmitter resumeTurn(@Valid @RequestBody AiResumeTurnFO fo, HttpServletRequest request, HttpServletResponse response) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        SseResponseUtils.applyStreamingHeaders(response);
        AiEventSink sink = turns.resume(puid, uid, fo);
        return prepareStreamResponse(sink, request);
    }

    private SseEmitter prepareStreamResponse(AiEventSink sink, HttpServletRequest request) {
        request.setAttribute(AiEventSink.REQUEST_ATTRIBUTE, sink);
        return sink.emitter();
    }
}
