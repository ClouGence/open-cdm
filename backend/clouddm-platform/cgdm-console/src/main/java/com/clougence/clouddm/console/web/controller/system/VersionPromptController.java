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
package com.clougence.clouddm.console.web.controller.system;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.vo.version.VersionDetailVO;
import com.clougence.clouddm.console.web.model.vo.version.VersionPromptVO;
import com.clougence.clouddm.console.web.service.system.VersionPromptService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/version")
@Slf4j
public class VersionPromptController {

    @Resource
    private VersionPromptService versionPromptService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResWebData<?> check() {
        VersionPromptVO versionPromptVO = this.versionPromptService.check();
        return ResWebDataUtils.buildSuccess(versionPromptVO);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResWebData<?> detail() {
        VersionDetailVO detail = versionPromptService.detail();
        return ResWebDataUtils.buildSuccess(detail);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public ResWebData<?> ignore() {
        versionPromptService.ignore();
        return ResWebDataUtils.buildSuccess();
    }
}
