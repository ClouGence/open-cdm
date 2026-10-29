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
package com.clougence.clouddm.console.web.controller.approval;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.approval.CustomApprovalConfigService;
import com.clougence.clouddm.console.web.component.approval.impl.ApprovalProviderServiceImpl;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalConnectionTestFO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalEngineDeleteFO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalEngineSaveFO;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalTeamConfig;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(DmControllerUrlPrefix.CONSOLE_PREFIX + "/approval/configuration")
public class ApprovalConfigurationController {
    @Resource
    private CustomApprovalConfigService configurationService;

    @Resource
    private ApprovalProviderServiceImpl providerService;

    @PostMapping("/testConnection")
    @RequestAuth(level = SecurityLevel.HIGH, value = SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W)
    public ResWebData<?> testConnection(@Valid @RequestBody ApprovalConnectionTestFO body, HttpServletRequest request) {
        providerService.testConnection((String) request.getAttribute(RdpUserService.PUID), body);
        return ResWebDataUtils.buildSuccess();
    }

    @PostMapping("/query")
    @RequestAuth(level = SecurityLevel.HIGH, value = SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_R)
    public ResWebData<?> queryConfiguration(HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(configurationService.getConfiguration((String) request.getAttribute(RdpUserService.PUID)));
    }

    @PostMapping("/saveEngine")
    @RequestAuth(level = SecurityLevel.HIGH, value = SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W)
    public ResWebData<?> saveEngine(@RequestBody ApprovalEngineSaveFO body, HttpServletRequest request) {
        configurationService.saveEngine((String) request.getAttribute(RdpUserService.PUID), body);
        return ResWebDataUtils.buildSuccess();
    }

    @PostMapping("/saveTeams")
    @RequestAuth(level = SecurityLevel.HIGH, value = SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W)
    public ResWebData<?> saveTeams(@RequestBody ApprovalTeamConfig body, HttpServletRequest request) {
        configurationService.saveTeams((String) request.getAttribute(RdpUserService.PUID), body);
        return ResWebDataUtils.buildSuccess();
    }

    @PostMapping("/deleteEngine")
    @RequestAuth(level = SecurityLevel.HIGH, value = SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W)
    public ResWebData<?> deleteEngine(@RequestBody ApprovalEngineDeleteFO body, HttpServletRequest request) {
        configurationService.deleteEngine((String) request.getAttribute(RdpUserService.PUID), body);
        return ResWebDataUtils.buildSuccess();
    }
}
