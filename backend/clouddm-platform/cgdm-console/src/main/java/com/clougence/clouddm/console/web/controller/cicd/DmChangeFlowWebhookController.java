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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.service.cicd.DmChangeService;
import com.clougence.clouddm.console.web.service.cicd.DmScmService;
import com.clougence.clouddm.console.web.service.cicd.domain.DmBranchDef;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;
import com.clougence.clouddm.platform.dal.model.gitops.DmGitOpsScmDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.scm.ScmEvent;
import com.clougence.clouddm.sdk.scm.ScmEventStatus;
import com.clougence.clouddm.sdk.scm.ScmProviderNames;
import com.clougence.clouddm.sdk.scm.ScmProviderSpi;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletInputStream;
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

    private void verify(String owner, String flow, String config) {
        if (StringUtils.isBlank(owner)) {
            throw new ErrorMessageException("invalid args.");
        }
        long flowId = resolveFlowId(flow, config);

        DmChangeFlowDO flowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(owner, flowId);
        if (flowDO == null) {
            throw new ErrorMessageException("not found config.");
        } else {
            this.dmChangeService.verifyFlow(owner, flowDO.getId());
        }
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    public ResWebData<?> callback(@RequestParam String owner,               //
                                  @RequestParam(value = "flow", required = false) String flow,//
                                  @RequestParam(value = "config", required = false) String config,//
                                  @RequestParam ScmProviderNames provider,//
                                  HttpServletRequest request) throws IOException {
        this.verify(owner, flow, config);
        long flowId = resolveFlowId(flow, config);

        // parser event
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerData = request.getHeaders(headerName);
            List<String> data = new ArrayList<>();
            while (headerData.hasMoreElements()) {
                data.add(headerData.nextElement());
            }
            headers.put(headerName, data);
        }
        String jsonBody;
        try (ServletInputStream in = request.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtils.copy(in, out);
            jsonBody = out.toString();
        }

        DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(owner, flowId);
        String repoPath = gitOpsFlowDO.getScmRepoSpace();
        String repoName = gitOpsFlowDO.getScmRepoName();
        String bindWebhookPwd = gitOpsFlowDO.getScmBindWebhookPwd();
        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, provider.name());
        DmGitOpsScmDO scmDO = this.dmScmService.queryScmById(owner, gitOpsFlowDO.getRefScmId());
        ScmEvent eventInfo = service.readEvent(scmDO.getScmServiceUrl(), scmDO.getScmAccessToken(), repoPath, repoName, bindWebhookPwd, headers, jsonBody);
        if (eventInfo == null) {
            return ResWebDataUtils.buildError("invalid event.");
        }

        // filter event
        if (filterEvent(eventInfo, gitOpsFlowDO)) {
            return ResWebDataUtils.buildSuccess("change filtered.");
        }

        // create
        try {
            return this.dmChangeService.triggerChangeSuggest(owner, gitOpsFlowDO.getId(), eventInfo.getEventId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResWebDataUtils.buildError("change failed, " + e.getMessage());
        }
    }

    // keep create.
    private static boolean filterEvent(ScmEvent eventInfo, DmChangeFlowDO gitOpsFlowDO) {
        boolean eqRepoPath = StringUtils.equals(eventInfo.getTarRepoPath(), gitOpsFlowDO.getScmRepoSpace());
        boolean eqRepoName = StringUtils.equals(eventInfo.getTarRepoName(), gitOpsFlowDO.getScmRepoName());
        boolean eqRepoBranch = StringUtils.equals(eventInfo.getTarRepoBranch(), gitOpsFlowDO.getScmRepoBranch());
        //boolean eqBind = StringUtils.equals(eventInfo.getHookId(), gitOpsFlowDO.getScmBindWebhook());
        boolean eqEvent = eventInfo.getEventType() == gitOpsFlowDO.getScmRepoEvent();
        if (!eqRepoPath || !eqRepoName || !eqRepoBranch || !eqEvent) {
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

    @RequestMapping(value = "/trigger", method = RequestMethod.GET)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    public ResponseEntity<String> trigger(@RequestParam String owner, @RequestParam(value = "flow", required = false) String flow,
                                          @RequestParam(value = "config", required = false) String config, @RequestParam String token, @RequestParam String format) {
        try {
            this.verify(owner, flow, config);
            long flowId = resolveFlowId(flow, config);
            DmChangeFlowDO gitOpsFlowDO = this.changeFlowDal.flowMapper().queryByOwnerAndId(owner, flowId);
            if (!gitOpsFlowDO.isEnableTrigger()) {
                return this.responseData(format, false, "trigger is disable.", 500);
            }
            if (StringUtils.isBlank(token) || !StringUtils.equals(token, gitOpsFlowDO.getTriggerToken())) {
                return this.responseData(format, false, "invalid token.", 500);
            }

            String ownerUid = gitOpsFlowDO.getOwnerUid();

            this.dmChangeService.verifyFlow(ownerUid, gitOpsFlowDO.getId());
            DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(ownerUid, gitOpsFlowDO.getRefScmId(), gitOpsFlowDO.getScmRepoName(), gitOpsFlowDO.getScmRepoBranch());
            if (branch == null) {
                return this.responseData(format, false, "branch not exist.", 500);
            }

            // create
            ResWebData<String> res = this.dmChangeService.triggerChangeSuggest(ownerUid, gitOpsFlowDO.getId(), branch.getBranchCommitId());
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
