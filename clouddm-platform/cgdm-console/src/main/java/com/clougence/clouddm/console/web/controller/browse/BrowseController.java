package com.clougence.clouddm.console.web.controller.browse;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseDetailFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLeafFO;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseObjectMO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.dal.model.RdpDsEnvDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/browse")
@Slf4j
public class BrowseController {

    @Resource
    private BrowseService           browseService;
    @Resource
    private RdpDsEnvService         rdpDsEnvService;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DmAuthServiceForBiz     dmAuthServiceForBiz;
    @Resource
    private DmResAuthService        dmDsAuthService;
    @Resource
    private DmDsService             dmDsService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listLevels", method = RequestMethod.POST)
    public ResWebData<?> listLevels(@Valid @RequestBody BrowseLevelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (CollectionUtils.isEmpty(fo.getLevels())) {
            // env list
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
            if (dsIds.isEmpty()) {
                return ResWebDataUtils.buildSuccess(new ArrayList<RdpDsEnvDO>());
            }

            List<RdpDsEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
            List<DmDsConfigDO> dmDsConfigDOS = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
            Set<Long> collect = dmDsConfigDOS.stream().map(DmDsConfigDO::getBindEnvId).collect(Collectors.toSet());
            List<BrowseLevelsVO> vos = dsEnvDOList.stream().filter(env -> {
                return collect.contains(env.getId());
            }).map(DmConvertUtils::convertToBrowseLevelsVO).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(vos);
        } else if (fo.getLevels().size() == 1) {
            // ds list
            List<BrowseLevelsVO> levels = this.browseService.listDs(puid, uid, fo.getLevels().get(0));
            // filter
            List<Long> dsIds = this.dmDsAuthService.listResByUser(uid, AuthKind.DataSource);
            levels = levels.stream().filter(value -> {
                return dsIds.contains(Long.parseLong(value.getObjId()));
            }).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(levels);
        } else {
            // ds object list
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
            DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels());
            this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);
            List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, fo.isRefreshCache());

            // filter
            ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, uid);
            if (!resourceAccessInfo.isAllAllow()) {
                vos = vos.stream().filter(obj -> {
                    return resourceAccessInfo.getAllowQueryList().contains(obj.getObjName());
                }).collect(Collectors.toList());
            }
            return ResWebDataUtils.buildSuccess(vos);
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/detailLevels", method = RequestMethod.POST)
    public ResWebData<?> detailLevels(@Valid @RequestBody BrowseLevelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (CollectionUtils.isEmpty(fo.getLevels())) {
            // empty
            return ResWebDataUtils.buildSuccess(null);
        } else if (fo.getLevels().size() == 1) {
            // the env
            RdpDsEnvDO dsEnvDO = this.rdpDsEnvService.queryByUserAndId(puid, uid, Long.parseLong(fo.getLevels().get(0)));
            BrowseLevelsVO vo = (dsEnvDO == null) ? null : DmConvertUtils.convertToBrowseLevelsVO(dsEnvDO);
            return ResWebDataUtils.buildSuccess(vo);
        } else if (fo.getLevels().size() == 2) {
            // the ds
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
            this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, levels.asResPath(), SecDataAuthLabel.DM_DAUTH_QUERY);

            BrowseLevelsVO vo = this.browseService.detailDs(uid, levels);
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            // the ds object
            DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
            this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
            this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, levels.asResPath(), SecDataAuthLabel.DM_DAUTH_QUERY);

            BrowseLevelsVO vo = this.browseService.detailLevels(puid, uid, levels);
            return ResWebDataUtils.buildSuccess(vo);
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/listLeaf", method = RequestMethod.POST)
    public ResWebData<?> listLeaf(@Valid @RequestBody BrowseLeafFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels());
        this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> vos = this.browseService.listLeaf(puid, uid, levels, leafType, fo.getPattern(), fo.isRefreshCache());

        ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, uid);
        if (!resourceAccessInfo.isAllAllow()) {
            vos = vos.stream().filter(vo -> {
                return resourceAccessInfo.getAllowQueryList().contains(vo.getObjName());
            }).collect(Collectors.toList());
        }
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/rdbObjectDetail", method = RequestMethod.POST)
    public ResWebData<?> rdbObjectDetail(@Valid @RequestBody BrowseDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(null);
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.getDbLevels(), fo.getTargetName());
        this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getTargetType());
        String leafName = fo.getTargetName();
        BrowseObjectMO result = this.browseService.rdbObjectDetail(puid, uid, levels, leafType, leafName, fo.isRefreshCache());
        return ResWebDataUtils.buildSuccess(DmConvertUtils.convertToBrowseObjectVO(result));
    }
}
