package com.clougence.clouddm.console.web.controller.security;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_AUTH_READ;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.fo.auth.ListElementsOfLevelFO;
import com.clougence.clouddm.console.web.model.fo.auth.ListUserLeafFo;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLeafFO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.vo.RdpAuthObjectVO;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpDsEnvDO;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/auth")
@Slf4j
public class DmResAuthController {

    @Resource
    private DmAuthServiceForManage  dmAuthServiceForManage;
    @Resource
    private RdpDsEnvService         rdpDsEnvService;
    @Resource
    private BrowseService           browseService;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DmResAuthService        dmDsAuthService;
    @Resource
    private RdpDsService            rdpDsService;

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/listElementsOfLevel", method = RequestMethod.POST)
    public ResWebData<?> listElementsOfLevel(@Valid @RequestBody ListElementsOfLevelFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (CollectionUtils.isEmpty(fo.getResPaths())) {
            // env list
            List<RdpDsEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
            List<BrowseLevelsVO> vos = dsEnvDOList.stream().map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(vos.stream().map(this::convertToAuthObjectVO).collect(Collectors.toList()));
        } else if (fo.getResPaths().size() == 1) {
            // ds list
            List<RdpAuthObjectVO> vos = this.dmAuthServiceForManage.listElements(puid, fo.getResPaths().get(0), fo.getAuthKind());
            return ResWebDataUtils.buildSuccess(vos);
        } else {
            // ds object list
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getResPaths());
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
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
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());

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
            List<RdpDsEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
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
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
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
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());

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
            List<RdpDsEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
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
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
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
    private ResWebData<?> filterEnv(List<RdpDsEnvDO> dsEnvDOList, List<Long> dsIds) {
        if (CollectionUtils.isEmpty(dsIds)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }
        List<RdpDataSourceDO> rdpDataSourceDOS = rdpDsService.listByIds(dsIds);
        List<Long> collect = rdpDataSourceDOS.stream().map(RdpDataSourceDO::getDsEnvId).distinct().collect(Collectors.toList());
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
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());

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
