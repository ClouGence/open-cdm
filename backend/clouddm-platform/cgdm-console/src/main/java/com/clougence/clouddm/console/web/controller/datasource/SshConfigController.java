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
package com.clougence.clouddm.console.web.controller.datasource;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SSH_CHANNEL_READ;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SSH_CHANNEL_WRITE;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.ssh.SshConfigIdFO;
import com.clougence.clouddm.console.web.model.fo.ssh.SshConfigListFO;
import com.clougence.clouddm.console.web.model.fo.ssh.SshConfigSaveFO;
import com.clougence.clouddm.console.web.model.fo.ssh.TestSshConnectionFO;
import com.clougence.clouddm.console.web.service.ssh.SshConfigService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/sshConfig")
@Slf4j
public class SshConfigController {

    @Resource
    private SshConfigService sshConfigService;

    @RequestAuth(DM_SSH_CHANNEL_READ)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> list(@Valid @RequestBody SshConfigListFO fo) {
        return ResWebDataUtils.buildSuccess(this.sshConfigService.list(fo.getSearch()));
    }

    @RequestAuth(DM_SSH_CHANNEL_READ)
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResWebData<?> detail(@Valid @RequestBody SshConfigIdFO fo) {
        return ResWebDataUtils.buildSuccess(this.sshConfigService.detail(fo.getId()));
    }

    @RequestAuth(DM_SSH_CHANNEL_WRITE)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<?> create(@Valid @RequestBody SshConfigSaveFO fo) {
        return ResWebDataUtils.buildSuccess(this.sshConfigService.create(fo));
    }

    @RequestAuth(DM_SSH_CHANNEL_WRITE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> update(@Valid @RequestBody SshConfigSaveFO fo) {
        this.sshConfigService.update(fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_SSH_CHANNEL_WRITE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(@Valid @RequestBody SshConfigIdFO fo) {
        this.sshConfigService.delete(fo.getId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_SSH_CHANNEL_READ)
    @RequestMapping(value = "/testConnection", method = RequestMethod.POST)
    public ResWebData<?> testConnection(@Valid @RequestBody TestSshConnectionFO fo) {
        return ResWebDataUtils.buildSuccess(this.sshConfigService.testConnection(fo));
    }
}
