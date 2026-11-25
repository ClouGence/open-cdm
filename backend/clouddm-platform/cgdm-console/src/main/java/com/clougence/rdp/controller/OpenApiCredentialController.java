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

import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_USER_MANAGE;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.user.OpenApiCredentialFO;
import com.clougence.clouddm.console.web.model.vo.openapi.OpenApiCredentialVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.openapi.OpenApiCredentialService;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.rdp.service.RdpOpAuditService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(DmControllerUrlPrefix.CONSOLE_PREFIX + "/user/manager")
public class OpenApiCredentialController {

    @Resource
    private OpenApiCredentialService credentialService;
    @Resource
    private RdpOpAuditService        auditService;

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @PostMapping("/issueOpenApiCredential")
    public ResWebData<?> issueOpenApiCredential(@Valid @RequestBody OpenApiCredentialFO fo, HttpServletRequest request, HttpServletResponse response) {
        return writeCredential(fo.getTargetUid(), false, request, response);
    }

    @RequestAuth(level = HIGH, value = RDP_USER_MANAGE)
    @PostMapping("/resetOpenApiCredential")
    public ResWebData<?> resetOpenApiCredential(@Valid @RequestBody OpenApiCredentialFO fo, HttpServletRequest request, HttpServletResponse response) {
        return writeCredential(fo.getTargetUid(), true, request, response);
    }

    private ResWebData<?> writeCredential(String targetUid, boolean reset, HttpServletRequest request, HttpServletResponse response) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        OpenApiCredentialVO credential = credentialService.writeCredential(puid, targetUid, reset);

        auditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), targetUid, Map
            .of("openApiCredential", reset ? "reset" : "issue"), HIGH, AuditType.UPDATE_SUB_ACCOUNT, ResourceType.ACCOUNT);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        return ResWebDataUtils.buildSuccess(credential);
    }
}
