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

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.model.vo.GlobalSettingsVO;
import com.clougence.clouddm.console.web.model.vo.SystemStatusVO;
import com.clougence.clouddm.init.model.SystemStatusResult;
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SysInitDefService;
import com.clougence.rdp.constant.auth.RequestAuth;

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
        SystemStatusVO statusVO = new SystemStatusVO();
        SystemStatusResult dbStatus = InitDBStatusDetector.detectDBStatus(this.defService.loadSystemProperties());

        if (isRestartPending()) {
            statusVO.setStatus(SystemStatus.Initial);
            statusVO.setInitReason("restarting");
            statusVO.setDbError(null);
        } else {
            statusVO.setStatus(dbStatus.getStatus());
            statusVO.setInitReason(dbStatus.getInitReason());
            statusVO.setDbError(dbStatus.getDbError());
        }
        statusVO.setUpgradeScripts(dbStatus.getUpgradeScripts());

        GlobalSettingsVO vo = new GlobalSettingsVO();
        vo.setSystemStatus(statusVO);
        return ResWebDataUtils.buildSuccess(vo);
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
