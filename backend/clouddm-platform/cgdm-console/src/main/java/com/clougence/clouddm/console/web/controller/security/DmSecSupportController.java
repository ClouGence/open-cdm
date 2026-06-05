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
package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_READ;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/2/25
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/security/support")
@Slf4j
public class DmSecSupportController {

    @Resource
    private CheckRulesService checkRulesService;

    @RequestAuth(value = DM_SECRULES_READ)
    @RequestMapping(value = "/ruleSettingDef", method = RequestMethod.POST)
    public ResWebData<?> ruleSettingDef(HttpServletRequest request) {
        return ResWebDataUtils.buildSuccess(checkRulesService.ruleSettingDef());
    }
}
