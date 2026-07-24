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
package com.clougence.clouddm.console.web.controller.cicd;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.cicd.ChangeFlowConstants;
import com.clougence.clouddm.console.web.component.cicd.ChangeFlowWebhookPolicy;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.service.cicd.DmChangeService;
import com.clougence.clouddm.console.web.service.cicd.DmScmService;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.DmBranchDef;
import com.clougence.clouddm.console.web.util.ScmWebhookRequestUtils;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping("/cicd/webhook")
@Slf4j
public class DmChangeFlowWebhookController {

    @Resource
    private ChangeFlowDal   changeFlowDal;
    @Resource
    private DmChangeService dmChangeService;
    @Resource
    private DmScmService    dmScmService;

    private long resolveFlowId(String flow, String config) {
        String flowId = StringUtils.isNotBlank(flow) ? flow : config;
        if (!StringUtils.isNumeric(flowId)) {
            throw new ErrorMessageException("invalid args.");
        }
        return Long.parseLong(flowId);
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    public ResponseEntity<ResWebData<?>> callback(@RequestParam String owner, @RequestParam(value = "flow", required = false) String flow,
                                                  @RequestParam(value = "config", required = false) String config, @RequestParam ScmProviderNames provider,
                                                  HttpServletRequest request) {
        try {
            long flowId = resolveWebhookFlowId(owner, flow, config);
            DmChangeFlowDO gitOpsFlowDO = requireWebhookFlow(owner, flowId, provider);
            ScmEvent eventInfo = readWebhookEvent(owner, provider, request, gitOpsFlowDO);
            return processWebhookEvent(owner, flowId, gitOpsFlowDO, eventInfo);
        } catch (ScmWebhookException e) {
            return webhookResponse(e.getStatusCode(), false, e.getMessage());
        } catch (Exception e) {
            log.error("webhook processing failed for owner={}, flow={}", owner, flow, e);
            return webhookResponse(500, false, "webhook processing failed");
        }
    }

    private long resolveWebhookFlowId(String owner, String flow, String config) {
        if (StringUtils.isBlank(owner)) {
            throw new ScmWebhookException(400, "invalid owner");
        }
        try {
            return resolveFlowId(flow, config);
        } catch (Exception e) {
            throw new ScmWebhookException(400, "invalid flow");
        }
    }

    private DmChangeFlowDO requireWebhookFlow(String owner, long flowId, ScmProviderNames provider) {
        DmChangeFlowDO flow = changeFlowDal.flowMapper().queryByOwnerAndId(owner, flowId);
        if (flow == null || flow.isDeleted()) {
            throw new ScmWebhookException(404, "flow not found");
        }
        if (flow.getRefScmType() == null || flow.getRefScmType().getProviderType() != provider) {
            throw new ScmWebhookException(400, "provider does not match flow");
        }
        return flow;
    }

    private ScmEvent readWebhookEvent(String owner, ScmProviderNames provider, HttpServletRequest request, DmChangeFlowDO flow) throws IOException {
        DmGitOpsScmDO scm = dmScmService.queryScmById(owner, flow.getRefScmId());
        ScmProviderSpi providerSpi = PluginManager.findSpi(ScmProviderSpi.class, provider.name());
        if (scm == null || providerSpi == null) {
            throw new ScmWebhookException(404, "SCM provider not found");
        }

        Map<String, List<String>> headers = ScmWebhookRequestUtils.readHeaders(request);
        String jsonBody = ScmWebhookRequestUtils.readUtf8Body(request, ChangeFlowConstants.MAX_WEBHOOK_BODY_BYTES);
        return providerSpi.readEvent(scm.getScmServiceUrl(), scm.getScmAccessToken(), flow.getScmRepoIdentifier(), flow.getScmRepoSpace(), flow.getScmRepoName(), flow
            .getScmBindWebhookPwd(), flow.getScmBindWebhookSigningToken(), headers, jsonBody);
    }

    private ResponseEntity<ResWebData<?>> processWebhookEvent(String owner, long flowId, DmChangeFlowDO flow, ScmEvent event) {
        if (event == null || filterEvent(event, flow)) {
            return webhookResponse(200, true, "event filtered");
        }
        if (!ChangeFlowWebhookPolicy.isCommitShaValid(event.getEventId())) {
            throw new ScmWebhookException(400, "event commit SHA is missing or invalid");
        }
        if (!ChangeFlowWebhookPolicy.isDeliveryIdValid(event.getDeliveryId())) {
            throw new ScmWebhookException(400, "webhook delivery identifier is too long");
        }
        // Provider authentication has completed; invalid requests must not consume the authenticated flow quota.
        if (!ChangeFlowWebhookPolicy.allowRequest(owner, flowId)) {
            throw new ScmWebhookException(429, "webhook rate limit exceeded");
        }
        try {
            dmChangeService.verifyFlow(owner, flowId);
        } catch (ErrorMessageException e) {
            throw new ScmWebhookException(400, e.getMessage());
        }
        refreshRepoMetadata(owner, flowId, event, flow);

        ChangeTriggerContext triggerContext = ChangeTriggerContext.webhook(event.getEventId(), event.getDeliveryId(), event.getEventType());
        ResWebData<String> result = dmChangeService.triggerChangeSuggest(owner, flowId, triggerContext);
        return ResponseEntity.ok(result.isSuccess() ? result : ResWebDataUtils.buildSuccess(result.getData()));
    }

    // keep create.
    private static boolean filterEvent(ScmEvent eventInfo, DmChangeFlowDO gitOpsFlowDO) {
        boolean hasStableId = StringUtils.isNotBlank(gitOpsFlowDO.getScmRepoIdentifier());
        boolean eqRepoId = !hasStableId || StringUtils.equals(eventInfo.getTarRepoId(), gitOpsFlowDO.getScmRepoIdentifier());
        boolean eqRepoPath = hasStableId || StringUtils.equals(eventInfo.getTarRepoPath(), gitOpsFlowDO.getScmRepoSpace());
        boolean eqRepoName = hasStableId || StringUtils.equals(eventInfo.getTarRepoName(), gitOpsFlowDO.getScmRepoName());
        boolean eqRepoBranch = StringUtils.equals(eventInfo.getTarRepoBranch(), gitOpsFlowDO.getScmRepoBranch());
        //boolean eqBind = StringUtils.equals(eventInfo.getHookId(), gitOpsFlowDO.getScmBindWebhook());
        boolean eqEvent = eventInfo.getEventType() == gitOpsFlowDO.getScmRepoEvent();
        if (!eqRepoId || !eqRepoPath || !eqRepoName || !eqRepoBranch || !eqEvent) {
            return true;
        }

        switch (eventInfo.getEventType()) {
            case Push:
            case Tag:
                return eventInfo.getStatus() == ScmEventStatus.Delete;
            case PullRequest:
                return eventInfo.getStatus() != ScmEventStatus.Merged;
            default:
                break;
        }

        return false;
    }

    private void refreshRepoMetadata(String owner, long flowId, ScmEvent event, DmChangeFlowDO flow) {
        if (!StringUtils.equals(event.getTarRepoId(), flow.getScmRepoIdentifier())) {
            return;
        }
        if (!StringUtils.equals(event.getTarRepoPath(), flow.getScmRepoSpace()) || !StringUtils.equals(event.getTarRepoName(), flow.getScmRepoName())
            || (StringUtils.isNotBlank(event.getTarRepoUrl()) && !StringUtils.equals(event.getTarRepoUrl(), flow.getScmRepoUrl()))) {
            changeFlowDal.flowMapper()
                .updateScmRepoMetadata(owner, flowId, event.getTarRepoPath(), event
                    .getTarRepoName(), StringUtils.isBlank(event.getTarRepoUrl()) ? flow.getScmRepoUrl() : event.getTarRepoUrl());
        }
    }

    private static ResponseEntity<ResWebData<?>> webhookResponse(int status, boolean success, String message) {
        ResWebData<?> body = success ? ResWebDataUtils.buildSuccess(message) : ResWebDataUtils.buildError(message);
        return ResponseEntity.status(status).body(body);
    }

    @RequestMapping(value = "/trigger", method = RequestMethod.GET)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    public ResponseEntity<String> trigger(@RequestParam String owner, @RequestParam(value = "flow", required = false) String flow,
                                          @RequestParam(value = "config", required = false) String config, @RequestParam String token, @RequestParam String format) {
        try {
            long flowId = resolveFlowId(flow, config);
            DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(owner, flowId);
            if (gitOpsFlowDO == null) {
                return this.responseData(format, false, "flow not found.", 404);
            }
            if (!gitOpsFlowDO.isEnableTrigger()) {
                return this.responseData(format, false, "trigger is disable.", 500);
            }
            if (StringUtils.isBlank(token) || !StringUtils.equals(token, gitOpsFlowDO.getTriggerToken())) {
                return this.responseData(format, false, "invalid token.", 500);
            }

            String ownerUid = gitOpsFlowDO.getOwnerUid();

            this.dmChangeService.verifyFlow(ownerUid, gitOpsFlowDO.getId());
            DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(ownerUid, gitOpsFlowDO.getRefScmId(), gitOpsFlowDO.getScmRepoIdentifier(), gitOpsFlowDO
                .getScmRepoSpace(), gitOpsFlowDO.getScmRepoName(), gitOpsFlowDO.getScmRepoBranch());
            if (branch == null) {
                return this.responseData(format, false, "branch not exist.", 500);
            }

            // create
            ChangeTriggerContext triggerContext = ChangeTriggerContext.remote(branch.getBranchCommitId());
            ResWebData<String> res = this.dmChangeService.triggerChangeSuggest(ownerUid, gitOpsFlowDO.getId(), triggerContext);
            if (res.isSuccess()) {
                return this.responseData(format, true, res.getData(), 200);
            } else {
                return this.responseData(format, false, res.getData(), 500);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return this.responseData(format, false, e.getMessage(), 500);
        }
    }

    private ResponseEntity<String> responseData(String format, boolean success, String message, int status) {
        if (StringUtils.equalsIgnoreCase(format, "json")) {
            Map<String, Object> map = CollectionUtils.asMap(//
                    "success", success,//
                    "code", status,    //
                    "message", message //
            );
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(JsonUtils.toJson(map));
        } else if (StringUtils.equalsIgnoreCase(format, "text")) {
            return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(status + ": " + message);
        } else {
            return ResponseEntity.status(status).contentType(MediaType.TEXT_PLAIN).body(status + ": " + message);
        }
    }
}
