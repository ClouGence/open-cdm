package com.clougence.clouddm.init.controller;

import jakarta.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.SystemStatus;
import com.clougence.clouddm.console.web.model.vo.GlobalSettingsVO;
import com.clougence.clouddm.console.web.model.vo.SystemStatusVO;
import com.clougence.clouddm.init.model.SystemStatusResult;
import com.clougence.clouddm.init.service.InitDBStatusDetector;
import com.clougence.clouddm.init.service.SysInitDefService;
import com.clougence.rdp.constant.auth.RequestAuth;

@Profile("init")
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/")
public class InitHomeController {

    @Resource
    private SysInitDefService defService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/dm_global_settings", method = { RequestMethod.POST })
    public ResWebData<?> dmGlobalSettings() {
        SystemStatusVO statusVO = new SystemStatusVO();
        SystemStatusResult dbStatus = InitDBStatusDetector.detectDBStatus(this.defService.loadSystemProperties());

        // While the init application is running, the frontend should never treat this
        // endpoint as the full application becoming ready. Ready is only reported by
        // the normal console application after the process restarts into full mode.
        statusVO.setStatus(SystemStatus.Initial);
        statusVO.setInitReason(dbStatus.getInitReason());
        statusVO.setDbError(dbStatus.getDbError());

        GlobalSettingsVO vo = new GlobalSettingsVO();
        vo.setSystemStatus(statusVO);
        return ResWebDataUtils.buildSuccess(vo);
    }
}
