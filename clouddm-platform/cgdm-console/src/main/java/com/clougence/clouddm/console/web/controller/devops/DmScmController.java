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

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_CICD_READ;

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
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmDeleteFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsScmUpdateFO;
import com.clougence.clouddm.console.web.model.vo.project.DevopsScmVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.project.DmProjectService;
import com.clougence.clouddm.console.web.service.project.DmScmService;
import com.clougence.clouddm.console.web.service.project.domain.DmScmDef;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.model.project.DmProjectDevopsDO;
import com.clougence.clouddm.platform.dal.model.project.DmProjectScmDO;
import com.clougence.clouddm.platform.dal.model.project.ScmType;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/devops/scm")
@Slf4j
public class DmScmController {

    @Resource
    private DmScmService     dmScmService;
    @Resource
    private DmProjectService dmProjectService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/defList", method = RequestMethod.POST)
    public ResWebData<?> defList(HttpServletRequest request) {
        List<Map<String, String>> services = new ArrayList<>();
        for (DmScmDef scmDef : this.dmScmService.getScmDefList()) {
            Map<String, String> item = new HashMap<>();
            item.put("scmType", scmDef.getScmType().name());
            item.put("scmTypeI18n", DmI18nUtils.getMessage(scmDef.getScmType().getI18nKey()));
            item.put("serviceUrl", scmDef.getServiceUrl());
            item.put("custom", String.valueOf(scmDef.isCustom()));
            item.put("helpUrl", scmDef.getHelpUrl());
            services.add(item);
        }
        return ResWebDataUtils.buildSuccess(services);
    }

    @RequestAuth(value = DM_CICD_READ)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> list(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmScmDef> defList = this.dmScmService.getScmDefList();
        Map<ScmType, DmScmDef> defMap = defList.stream().collect(Collectors.toMap(DmScmDef::getScmType, d -> d));

        List<DmProjectScmDO> scmList = this.dmScmService.queryScmList(puid);
        List<DevopsScmVO> vos = scmList.stream().map(scmDO -> {
            return DmConvertUtils.convertToDevopsScmVO(scmDO, defMap);
        }).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(value = DM_CICD_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<?> add(HttpServletRequest request, @Valid @RequestBody DevopsScmAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        this.dmScmService.addScm(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_CICD_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delete(HttpServletRequest request, @Valid @RequestBody DevopsScmDeleteFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectScmDO scmDO = this.dmScmService.queryScmById(puid, fo.getScmId());
        if (scmDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        if (!fo.isForce()) {
            List<DmProjectDevopsDO> useList = this.dmProjectService.queryEnableDevopsByScmId(puid, fo.getScmId());
            if (!useList.isEmpty()) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_INUSE_ERROR.name(), scmDO.getScmDisplay()));
            }
        }

        this.dmScmService.deleteScmById(puid, fo.getScmId());
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_CICD_MANAGE)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResWebData<?> update(HttpServletRequest request, @Valid @RequestBody DevopsScmUpdateFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        DmProjectScmDO scmDO = this.dmScmService.queryScmById(puid, fo.getScmId());
        if (scmDO == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_NOT_EXIST_ERROR.name()));
        }

        // key config change
        if (StringUtils.isNotBlank(fo.getNewAccessToken()) || StringUtils.isNotBlank(fo.getNewServiceUrl())) {
            if (!fo.isForce()) {
                List<DmProjectDevopsDO> useList = this.dmProjectService.queryEnableDevopsByScmId(puid, fo.getScmId());
                if (!useList.isEmpty()) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DEVOPS_SCM_INUSE_ERROR.name(), scmDO.getScmDisplay()));
                }
            }
        }

        this.dmScmService.updateScmById(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }

    @RequestAuth(value = DM_CICD_MANAGE)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResWebData<?> test(HttpServletRequest request, @Valid @RequestBody DevopsScmAddFO fo) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        if (fo.getScmId() != null) {
            DmProjectScmDO scmDO = this.dmScmService.queryScmById(puid, fo.getScmId());
            fo.setScmType(scmDO.getScmType());
            fo.setDisplay(scmDO.getScmDisplay());
            fo.setServiceUrl(scmDO.getScmServiceUrl());
            fo.setAccessToken(scmDO.getScmAccessToken());
        }

        this.dmScmService.testScmByConfig(puid, fo);
        return ResWebDataUtils.buildSuccess(true);
    }
}
