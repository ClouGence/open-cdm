package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_SECRULES_READ;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.rdp.constant.auth.RequestAuth;

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
