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

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_DS_MANAGE;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.envparam.DmBindEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmFetchEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmUnbindEnvParamFO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamOpenVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:02
 */
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/envparam")
@Slf4j
public class DmEnvParamController {

    @Resource
    private DmEnvParamService dmEnvParamService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/fetchTicketParamForSec", method = RequestMethod.POST)
    public ResWebData<?> fetchTicketParamForSec(HttpServletRequest request, @Valid @RequestBody DmFetchEnvParamFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmEnvParamTicketDesVO vo;
        if (StringUtils.equals(fo.getParamKey(), EnvParamKeys.AUTH_TICKET_INFO)) {
            vo = this.dmEnvParamService.queryAuthTicketInfoParam(puid, fo.getEnvId());
        } else if (StringUtils.equals(fo.getParamKey(), EnvParamKeys.CHANGE_TICKET_INFO)) {
            vo = this.dmEnvParamService.queryChangeTicketInfoParam(puid, fo.getEnvId());
        } else if (StringUtils.equals(fo.getParamKey(), EnvParamKeys.SQL_TICKET_INFO)) {
            vo = this.dmEnvParamService.querySqlTicketInfoParam(puid, fo.getEnvId());
        } else {
            vo = null;
        }
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listEnvParamForSec", method = RequestMethod.POST)
    public ResWebData<?> listEnvParamForSec(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmEnvParamOpenVO> vos = this.dmEnvParamService.listEnvParamOpen(puid, uid);
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/bindEnvParam", method = RequestMethod.POST)
    public ResWebData<?> bindEnvParam(@Valid @RequestBody DmBindEnvParamFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmEnvParamService.bindEnvParam(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/unbindEnvParam", method = RequestMethod.POST)
    public ResWebData<?> unbindEnvParam(@Valid @RequestBody DmUnbindEnvParamFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmEnvParamService.unbindEnvParam(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }
}
