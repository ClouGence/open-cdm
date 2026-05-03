package com.clougence.clouddm.console.web.controller.system;

import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_WORKER_MANAGE;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.fo.cluster.OnOffWorkerAlertFO;
import com.clougence.clouddm.console.web.service.system.AlertConfigService;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.service.RdpUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-20 12:05
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/alertconfig")
@Slf4j
public class AlertConfigController {

    @Resource
    private AlertConfigService alertConfigService;

    @RequestAuth(level = HIGH, value = DM_WORKER_MANAGE)
    @RequestMapping(value = "/onoffworkeralert", method = RequestMethod.POST)
    public ResWebData<?> onOffWorkerAlert(@Valid @RequestBody OnOffWorkerAlertFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.alertConfigService.onOffWorkerAlert(fo, uid);
        return ResWebDataUtils.buildSuccess();
    }
}
