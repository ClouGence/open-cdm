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
package com.clougence.clouddm.init.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.vo.GlobalSettingsVO;
import com.clougence.clouddm.console.web.model.vo.SystemStatusVO;
import com.clougence.clouddm.init.model.SystemStatusResult;
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SysInitDefService;

import jakarta.annotation.Resource;

@Profile("init")
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/")
public class InitHomeController {

    private static final long RESTART_FLAG_TTL_MILLIS = 120_000L;

    @Resource
    private SysInitDefService defService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/dm_global_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmGlobalSettings() {
        GlobalSettingsVO vo = new GlobalSettingsVO();
        vo.setVersion(GlobalConfUtils.getAppVersion());
        vo.setAloneMode(GlobalConfUtils.isAloneMode());

        SystemStatusVO statusVO = new SystemStatusVO();
        SystemStatusResult dbStatus = InitDBStatusDetector.detectDBStatus(this.defService.loadSystemProperties());
        if (isRestartPending()) {
            statusVO.setStatus(SystemStatus.Initial);
            statusVO.setInitReason("restarting");
            statusVO.setDbError(null);
        } else {
            statusVO.setStatus(resolveInitApplicationStatus(dbStatus.getStatus()));
            statusVO.setInitReason(dbStatus.getInitReason());
            statusVO.setDbError(dbStatus.getDbError());
        }
        statusVO.setUpgradeScripts(dbStatus.getUpgradeScripts());
        vo.setSystemStatus(statusVO);

        return ResWebDataUtils.buildSuccess(vo);
    }

    private SystemStatus resolveInitApplicationStatus(SystemStatus status) {
        return status == SystemStatus.Ready ? SystemStatus.Initial : status;
    }

    private boolean isRestartPending() {
        Path restartFlag = resolveRestartFlagPath();
        if (!Files.isRegularFile(restartFlag)) {
            return false;
        }
        try {
            String flagValue = Files.readString(restartFlag, StandardCharsets.UTF_8).trim();
            long restartAt = Long.parseLong(flagValue);
            return System.currentTimeMillis() - restartAt <= RESTART_FLAG_TTL_MILLIS;
        } catch (IOException | NumberFormatException e) {
            return false;
        }
    }

    private Path resolveRestartFlagPath() {
        return Paths.get(GlobalConfUtils.getAppHome(), ".restarting");
    }
}
