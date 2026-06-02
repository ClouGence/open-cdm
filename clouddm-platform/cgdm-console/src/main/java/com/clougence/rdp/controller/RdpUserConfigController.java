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
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_PRI_USER_KV_CONF_W;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.model.fo.UpsertUserConfigFO;
import com.clougence.clouddm.console.web.model.fo.user.GetUserSpecifiedConfsFO;
import com.clougence.clouddm.console.web.model.lo.UpsertUserConfigLO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/3/11
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/user/config")
@Slf4j
public class RdpUserConfigController {

    @Resource
    private UserConfigService userConfigService;
    @Resource
    private RdpOpAuditService rdpOpAuditService;

    @RequestAuth(strategy = AuthStrategy.RefAnyOnes)
    @RequestMapping(value = "/getcurruserconfigs", method = RequestMethod.POST)
    public ResWebData<?> getCurrUserConfigs(HttpServletRequest request) {
        // prepare auth info
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        return ResWebDataUtils.buildSuccess(userConfigService.queryUserConfigVosWithNewEntries(puid));
    }

    @RequestAuth(strategy = AuthStrategy.RefAnyOnes)
    @RequestMapping(value = "/getUserSpecifiedConfs", method = RequestMethod.POST)
    public ResWebData<?> getUserSpecifiedConfs(@RequestBody @Valid GetUserSpecifiedConfsFO configFO, HttpServletRequest request) {
        if (configFO.getConfigNames() == null || configFO.getConfigNames().isEmpty()) {
            return ResWebDataUtils.buildSuccess(new HashMap<>());
        }

        // prepare auth info
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (StringUtils.isBlank(puid)) {
            throw new IllegalArgumentException("Empty puid.maybe not login.");
        }

        return ResWebDataUtils.buildSuccess(userConfigService.queryWithNewEntriesAndSpecifiedConfs(puid, configFO.getConfigNames()));
    }

    @RequestAuth(level = HIGH, value = RDP_PRI_USER_KV_CONF_W)
    @RequestMapping(value = "/upsertuserconfigs", method = RequestMethod.POST)
    public ResWebData<?> upsertUserConfigs(@RequestBody @Valid UpsertUserConfigFO configFO, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(configFO.getUpdateConfigs()) && CollectionUtils.isEmpty(configFO.getNeedCreateConfigs())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.UPDATE_USER_CONFIG_PARAMS_ARE_EMPTY.name()));
        }

        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<UpsertUserConfigLO> configLOs = userConfigService.upsertConfigValue(puid, configFO);

        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), uid, configLOs, SecurityLevel.HIGH, AuditType.UPDATE_SYSTEM_CONFIG, ResourceType.ACCOUNT);
        return ResWebDataUtils.buildSuccess();
    }
}
