package com.clougence.clouddm.console.web.controller.project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.clougence.clouddm.sdk.scm.ScmEvent;
import com.clougence.clouddm.sdk.scm.ScmEventStatus;
import com.clougence.clouddm.sdk.scm.ScmProviderNames;
import com.clougence.clouddm.sdk.scm.ScmProviderSpi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.dal.mapper.DmProjectDevopsMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsDO;
import com.clougence.clouddm.console.web.dal.model.DmProjectScmDO;
import com.clougence.clouddm.console.web.service.project.DmChangeService;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmBranchDef;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping("/project/webhook")
@Slf4j
public class DmProjectCallbackController {

    @Resource
    private DmChangeService       dmChangeService;
    @Resource
    private DmScmService          dmScmService;
    @Resource
    private DmProjectDevopsMapper dmProjectDevopsMapper;

    private void verify(String owner, String config) {
        if (!StringUtils.isNumeric(config)) {
            throw new ErrorMessageException("invalid args.");
        }
        if (StringUtils.isBlank(owner) || StringUtils.isBlank(config)) {
            throw new ErrorMessageException("invalid args.");
        }

        //  verifyDevops
        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(owner, Long.parseLong(config));
        if (devopsDO == null) {
            throw new ErrorMessageException("not found config.");
        } else {
            this.dmChangeService.verifyDevops(owner, devopsDO.getRefProjectId(), devopsDO.getId());
        }
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    public ResWebData<?> callback(@RequestParam String owner,               //
                                  @RequestParam String config,              //
                                  @RequestParam ScmProviderNames provider,//
                                  HttpServletRequest request) throws IOException {
        this.verify(owner, config);

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

        DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(owner, Long.parseLong(config));
        String repoPath = devopsDO.getScmRepoSpace();
        String repoName = devopsDO.getScmRepoName();
        String bindWebhookPwd = devopsDO.getScmBindWebhookPwd();
        ScmProviderSpi service = PluginManager.findSpi(ScmProviderSpi.class, provider.name());
        DmProjectScmDO scmDO = this.dmScmService.queryScmById(owner, devopsDO.getRefScmId());
        ScmEvent eventInfo = service.readEvent(scmDO.getScmServiceUrl(), scmDO.getScmAccessToken(), repoPath, repoName, bindWebhookPwd, headers, jsonBody);
        if (eventInfo == null) {
            return ResWebDataUtils.buildError("invalid event.");
        }

        // filter event
        if (filterEvent(eventInfo, devopsDO)) {
            return ResWebDataUtils.buildSuccess("change filtered.");
        }

        // create
        try {
            return this.dmChangeService.triggerChangeSuggest(owner, devopsDO.getRefProjectId(), devopsDO.getId(), eventInfo.getEventId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResWebDataUtils.buildError("change failed, " + e.getMessage());
        }
    }

    // keep create.
    private static boolean filterEvent(ScmEvent eventInfo, DmProjectDevopsDO devopsDO) {
        boolean eqRepoPath = StringUtils.equals(eventInfo.getTarRepoPath(), devopsDO.getScmRepoSpace());
        boolean eqRepoName = StringUtils.equals(eventInfo.getTarRepoName(), devopsDO.getScmRepoName());
        boolean eqRepoBranch = StringUtils.equals(eventInfo.getTarRepoBranch(), devopsDO.getScmRepoBranch());
        //boolean eqBind = StringUtils.equals(eventInfo.getHookId(), devopsDO.getScmBindWebhook());
        boolean eqEvent = eventInfo.getEventType() == devopsDO.getScmRepoEvent();
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
    public ResponseEntity<String> trigger(@RequestParam String owner, @RequestParam String config, @RequestParam String token, @RequestParam String format) {
        try {
            this.verify(owner, config);
            DmProjectDevopsDO devopsDO = this.dmProjectDevopsMapper.queryByOwnerAndId(owner, Long.parseLong(config));
            if (!devopsDO.isEnableTrigger()) {
                return this.responseData(format, false, "trigger is disable.", 500);
            }
            if (StringUtils.isBlank(token) || !StringUtils.equals(token, devopsDO.getTriggerToken())) {
                return this.responseData(format, false, "invalid token.", 500);
            }

            String ownerUid = devopsDO.getOwnerUid();
            long projectId = devopsDO.getRefProjectId();

            this.dmChangeService.verifyDevops(ownerUid, projectId, devopsDO.getId());
            DmBranchDef branch = this.dmScmService.fetchBranchByScmAndRepo(ownerUid, devopsDO.getRefScmId(), devopsDO.getScmRepoName(), devopsDO.getScmRepoBranch());
            if (branch == null) {
                return this.responseData(format, false, "branch not exist.", 500);
            }

            // create
            ResWebData<String> res = this.dmChangeService.triggerChangeSuggest(ownerUid, devopsDO.getRefProjectId(), devopsDO.getId(), branch.getBranchCommitId());
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
