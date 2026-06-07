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
package com.clougence.clouddm.console.web.controller.devops;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_IM_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_IM_READ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImDeleteFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImProviderListFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImUpdateFO;
import com.clougence.clouddm.console.web.model.vo.project.DevopsImVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.project.DmImService;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.project.DmProjectMsgDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysMessengerDO;
import com.clougence.clouddm.platform.dal.model.system.ImType;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/devops/im")
@Slf4j
public class DmImController {

    @Resource
    private DmImService      dmImService;
    @Resource
    private DmProjectService projectService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/defList", method = RequestMethod.POST)
    public ResWebData<?> defList(HttpServletRequest request) {
        List<Map<String, String>> services = new ArrayList<>();
        for (DmImDef scmDef : this.dmImService.getImDefList()) {
            Map<String, String> item = new HashMap<>();
            item.put("imType", scmDef.getImType().name());
            item.put("imTypeI18n", DmI18nUtils.getMessage(scmDef.getImType().getI18nKey()));
            item.put("helpUrl", scmDef.getHelpUrl());
            item.put("iconResource", "webside/" + scmDef.getImType().name() + "@im-icon");
            services.add(item);
        }
        return ResWebDataUtils.buildSuccess(services);
    }

    @RequestAuth(value = DM_IM_READ)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> list(HttpServletRequest request, @Valid @RequestBody DevopsImProviderListFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmImDef> defList = this.dmImService.getImDefList();
        Map<ImType, DmImDef> defMap = defList.stream().collect(Collectors.toMap(DmImDef::getImType, d -> d));

        List<DmSysMessengerDO> imList = this.dmImService.queryMessengerByOwnerAndType(puid, fo.getImType());
        List<DevopsImVO> vos = imList.stream().map(scmDO -> {
            return DmConvertUtils.convertToDevopsImVO(scmDO, defMap);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<?> add(HttpServletRequest request, @Valid @RequestBody DevopsImAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.dmImService.addIm(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(HttpServletRequest request, @Valid @RequestBody DevopsImDeleteFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSysMessengerDO imDO = this.dmImService.queryImById(puid, fo.getImId());
        if (imDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        if (!fo.isForce()) {
            List<DmProjectMsgDO> useList = this.projectService.queryEnableDevopsByImId(puid, fo.getImId());
            if (!useList.isEmpty()) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_INUSE_ERROR.name(), imDO.getImDisplay()));
            }
        }

        this.dmImService.deleteImById(puid, fo.getImId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> update(HttpServletRequest request, @Valid @RequestBody DevopsImUpdateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmSysMessengerDO imDO = this.dmImService.queryImById(puid, fo.getImId());
        if (imDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_NOT_EXIST_ERROR.name()));
        }

        // key config change
        if (StringUtils.isNotBlank(fo.getNewWebhookUrl())) {
            if (!fo.isForce()) {
                List<DmProjectMsgDO> useList = this.projectService.queryEnableDevopsByImId(puid, fo.getImId());
                if (!useList.isEmpty()) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_IM_INUSE_ERROR.name(), imDO.getImDisplay()));
                }
            }
        }

        this.dmImService.updateImById(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_IM_MANAGE)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResWebData<?> test(HttpServletRequest request, @Valid @RequestBody DevopsImAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.getImId() != null) {
            DmSysMessengerDO messengerDO = this.dmImService.queryImById(puid, fo.getImId());
            fo.setImType(messengerDO.getImType());
            fo.setDisplay(messengerDO.getImDisplay());
            fo.setWebhookUrl(messengerDO.getWebhook());
            fo.setSecret(messengerDO.getSecret());
        }

        this.dmImService.testImByConfig(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }
}
