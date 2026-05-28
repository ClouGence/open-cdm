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
package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_READ;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.auth.ListElementsOfLevelFO;
import com.clougence.clouddm.console.web.model.fo.auth.ListUserLeafFo;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLeafFO;
import com.clougence.clouddm.console.web.model.vo.RdpAuthObjectVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/auth")
@Slf4j
public class DmResAuthController {

    @Resource
    private DmAuthServiceForManage dmAuthServiceForManage;
    @Resource
    private RdpDsEnvService        rdpDsEnvService;
    @Resource
    private BrowseService          browseService;
    @Resource
    private DmDsConfigService      dmDsConfigService;
    @Resource
    private ObjectCacheDao         objectCacheDao;
    @Resource
    private DmResAuthService       dmDsAuthService;
    @Resource
    private RdpDsService           rdpDsService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (CollectionUtils.isEmpty(fo.getResPaths())) {
            // env list
            List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
            List<BrowseLevelsVO> vos = dsEnvDOList.stream().map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
        } else if (fo.getResPaths().size() == 1) {
            // ds list
            List<RdpAuthObjectVO> vos = this.dmAuthServiceForManage.listElements(puid, fo.getResPaths().get(0), fo.getAuthKind());
            return ResWebDataUtils.buildSuccess(vos);
        } else {
            // ds object list
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getResPaths());
            this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
            List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, false);
            return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
        }
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listElementOfLeaf", method = RequestMethod.POST)
    public ResWebData<?> listLeaf(@Valid @RequestBody BrowseLeafFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> vos = this.browseService.listLeaf(puid, uid, levels, leafType, fo.getPattern(), false);

        return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
    }

    @RequestAuth(RDP_AUTH_READ)
    @RequestMapping(value = "/listUserElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listUserElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (CollectionUtils.isEmpty(fo.getResPaths())) {
            // env list
            List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(fo.getUid(), AuthKind.DataSource);
            return filterEnv(dsEnvDOList, dsIds);
        } else if (fo.getResPaths().size() == 1) {
            // ds list
            List<RdpAuthObjectVO> vos = this.dmAuthServiceForManage.listElements(puid, fo.getResPaths().get(0), fo.getAuthKind());

            // filter
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(fo.getUid(), AuthKind.DataSource);
            vos = vos.stream().filter(value -> {
                return dsIds.contains(value.getObjId());
            }).collect(Collectors.toList());

            return ResWebDataUtils.buildSuccess(vos);
        } else {
            // ds object list
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getResPaths());
            this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
            List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, false);
            // filter
            ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, fo.getUid());
            if (!resourceAccessInfo.isAllAllow()) {
                vos = vos.stream().filter(obj -> {
                    return resourceAccessInfo.getAllowQueryList().contains(obj.getObjName());
                }).collect(Collectors.toList());
            }
            return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
        }
    }

    @RequestAuth(RDP_AUTH_READ)
    @RequestMapping(value = "/listUserElementOfLeaf", method = RequestMethod.POST)
    public ResWebData<?> listUserLeaf(@Valid @RequestBody ListUserLeafFo fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> vos = this.browseService.listLeaf(puid, uid, levels, leafType, fo.getPattern(), false);

        ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, fo.getUid());
        if (!resourceAccessInfo.isAllAllow()) {
            vos = vos.stream().filter(obj -> {
                return resourceAccessInfo.getAllowQueryList().contains(obj.getObjName());
            }).collect(Collectors.toList());
        }

        return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listMyElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (CollectionUtils.isEmpty(fo.getResPaths())) {
            // env list
            List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
            return filterEnv(dsEnvDOList, dsIds);
        } else if (fo.getResPaths().size() == 1) {
            // ds list
            List<RdpAuthObjectVO> vos = this.dmAuthServiceForManage.listElements(puid, fo.getResPaths().get(0), fo.getAuthKind());
            // filter
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
            vos = vos.stream().filter(value -> {
                return dsIds.contains(value.getObjId());
            }).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(vos);
        } else {
            // ds object list
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getResPaths());
            this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
            List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, false);
            // filter
            ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, uid);
            if (!resourceAccessInfo.isAllAllow()) {
                vos = vos.stream().filter(obj -> {
                    return resourceAccessInfo.getAllowQueryList().contains(obj.getObjName());
                }).collect(Collectors.toList());
            }
            return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
        }
    }

    @NotNull
    private ResWebData<?> filterEnv(List<DmSysEnvDO> dsEnvDOList, List<Long> dsIds) {
        if (CollectionUtils.isEmpty(dsIds)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }
        List<DmDsDO> rdpDataSourceDOS = rdpDsService.listByIds(dsIds);
        List<Long> collect = rdpDataSourceDOS.stream().map(DmDsDO::getDsEnvId).distinct().collect(Collectors.toList());
        List<BrowseLevelsVO> vos = dsEnvDOList.stream().filter(env -> {
            return collect.contains(env.getId());
        }).map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listMyElementOfLeaf", method = RequestMethod.POST)
    public ResWebData<?> listMyLeaf(@Valid @RequestBody BrowseLeafFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> vos = this.browseService.listLeaf(puid, uid, levels, leafType, fo.getPattern(), false);

        ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, uid);
        if (!resourceAccessInfo.isAllAllow()) {
            vos = vos.stream().filter(obj -> {
                return resourceAccessInfo.getAllowQueryList().contains(obj.getObjName());
            }).collect(Collectors.toList());
        }

        return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
    }

    private RdpAuthObjectVO convertToAuthObjectVO(BrowseLevelsVO vo) {
        RdpAuthObjectVO authObjectVO = new RdpAuthObjectVO();
        authObjectVO.setObjId(Long.parseLong(vo.getObjId()));
        authObjectVO.setObjName(vo.getObjName());
        authObjectVO.setObjType(vo.getObjType());
        return authObjectVO;
    }
}
