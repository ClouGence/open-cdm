package com.clougence.clouddm.console.web.controller.system;

import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.vo.version.VersionDetailVO;
import com.clougence.clouddm.console.web.model.vo.version.VersionPromptVO;
import com.clougence.clouddm.console.web.service.system.VersionPromptService;
import com.clougence.rdp.constant.auth.RequestAuth;

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
