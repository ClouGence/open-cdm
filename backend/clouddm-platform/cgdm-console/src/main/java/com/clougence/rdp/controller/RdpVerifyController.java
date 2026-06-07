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

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.SendCodeAfterLoginFO;
import com.clougence.clouddm.console.web.model.fo.SendCodeByAccountFO;
import com.clougence.clouddm.console.web.model.fo.SendCodeFO;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;

import jakarta.validation.Valid;

/**
 * @author bucketli 2020-01-14 21:36
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/verify")
public class RdpVerifyController {

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcode", method = RequestMethod.POST)
    public ResWebData<?> sendCode(@Valid @RequestBody SendCodeFO sendCodeFO) {
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcodeinloginstate", method = RequestMethod.POST)
    public ResWebData<?> sendCodeInLoginState(@Valid @RequestBody SendCodeAfterLoginFO verifyData) {
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/sendcodebyaccount", method = RequestMethod.POST)
    public ResWebData<?> sendCodeByAccount(@RequestBody @Valid SendCodeByAccountFO sendCodeFO) {
        return ResWebDataUtils.buildSuccess();
    }
}
