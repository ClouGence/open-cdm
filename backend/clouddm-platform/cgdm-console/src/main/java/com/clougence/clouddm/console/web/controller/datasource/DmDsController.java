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

import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_MANAGER;
import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_READ;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecListFO;
import com.clougence.clouddm.console.web.model.fo.datasource.*;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.checkrules.SpecVO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.model.vo.datasource.ConnectDsResultVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DmSimpleDsVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSpecDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/datasource")
@Slf4j
public class DmDsController {

    @Resource
    private DmDsService         dmDsService;
    @Resource
    private RdpDsService        rdpDsService;
    @Resource
    private DmResAuthService    dmDsAuthService;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmAuthServiceForBiz rdpAuthServiceForBiz;
    @Resource
    private ClusterService      clusterService;
    @Resource
    private CheckRulesService   checkRulesService;
    @Resource
    private DmDsConfigService   dmDsConfigService;

    @RequestAuth(DM_DS_READ)
    @RequestMapping(value = "/listByCondition", method = RequestMethod.POST)
    public ResWebData<?> listByCondition(@RequestBody @Valid ListDsFO listDsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmAuthResDO> authList = this.dmDsAuthService.listAuthByUser(uid, AuthKind.DataSource);
        if (authList == null || authList.isEmpty()) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }

        List<Long> authedDsIds = authList.stream().map(DmAuthResDO::getResId).distinct().collect(Collectors.toList());

        ArgDsQueryParamObj queryMO = ArgDsQueryParamObj.builder()
            .dataSourceType(listDsFO.getType())
            .dataSourceDescLike(listDsFO.getDataSourceDescLike())
            .dataSourceIds(Stream.of(listDsFO.getDataSourceId()).filter(Objects::nonNull).collect(Collectors.toList()))
            .deployType(listDsFO.getDeployType())
            .lifeCycleState(listDsFO.getLifeCycleState())
            .dsHostLike(listDsFO.getDsHostLike())
            .dataSourceType(listDsFO.getType())
            .instanceIdLike(listDsFO.getInstanceIdLike())
            .build();

        if (CollectionUtils.isEmpty(queryMO.getDataSourceIds())) {
            queryMO.setDataSourceIds(new ArrayList<>(authedDsIds));
        } else {
            if (!authedDsIds.containsAll(queryMO.getDataSourceIds())) {
                throw new IllegalArgumentException("DataSource have no auth.");
            }
        }

        List<DmDsDO> result = this.rdpDsService.fetchByCondition(puid, queryMO, true);
        if (CollectionUtils.isEmpty(result)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        } else {
            List<DmSimpleDsVO> vos = genAndFilterToSimpleVO(puid, result, listDsFO);
            return ResWebDataUtils.buildSuccess(vos);
        }
    }

    private List<DmSimpleDsVO> genAndFilterToSimpleVO(String puid, List<DmDsDO> dos, ListDsFO listDsFO) {
        List<DmSimpleDsVO> vos = new ArrayList<>();
        if (CollectionUtils.isEmpty(dos)) {
            return vos;
        }

        List<Long> dsIds = dos.stream().map(DmDsDO::getId).collect(Collectors.toList());

        List<DmDsDO> confList = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        Map<Long, DmDsDO> confMap = confList.stream().collect(Collectors.toMap(DmDsDO::getId, d -> d));

        vos = dos.stream().map(ds -> DmConvertUtils.convertToDmSimpleDsVO(ds, confMap)).filter(vo -> {
            if (HostType.PRIVATE == listDsFO.getHostType()) {
                return StringUtils.isNotBlank(vo.getPrivateHost());
            } else if (HostType.PUBLIC == listDsFO.getHostType()) {
                return StringUtils.isNotBlank(vo.getPublicHost());
            } else {
                return true;
            }
        }).collect(Collectors.toList());
        return vos;
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/testEnableQuery", method = RequestMethod.POST)
    public ResWebData<?> testEnableQuery(@Valid @RequestBody EnableDsQueryFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownCluster(puid, fo.getClusterId());
        this.objectCacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.rdpAuthServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        try {
            String version = this.dmDsService.testAndFetchDsVersion(puid, fo);
            return ResWebDataUtils.buildSuccess(version);
        } catch (Exception e) {
            log.error("testDsQuery failed, " + e.getMessage());
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TEST_CONNECT_ERROR.name(), e.getMessage()));
        }
    }

    @RequestAuth(DM_DS_READ)
    @RequestMapping(value = "/queryDsConfig", method = RequestMethod.POST)
    public ResWebData<?> queryDsConfig(@RequestBody QueryDsConfigFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.rdpAuthServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);

        List<String> blackList = Arrays.asList(       //
                DataSourceConfig.Fields.host,         //
                DataSourceConfig.Fields.securityType, //
                DataSourceConfig.Fields.userName,     //
                DataSourceConfig.Fields.password,     //
                DataSourceConfig.Fields.configVersion,//
                DataSourceConfig.Fields.storePassword);

        List<DsKvConfigVO> vos = this.dmDsService.queryDsConfigIncludeNewEntries(fo.getDataSourceId());
        vos = vos.stream().filter(c -> !blackList.contains(c.getConfigName())).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/connectDs", method = RequestMethod.POST)
    public ResWebData<?> connectDs(@Valid @RequestBody ConnectDsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.objectCacheDao.ownCluster(puid, fo.getBindClusterId());

        try {
            String version = this.dmDsService.testConnect(uid, fo);
            ConnectDsResultVO result = new ConnectDsResultVO();
            result.setSuccess(true);
            result.setVersion(version);
            return ResWebDataUtils.buildSuccess(result);
        } catch (Exception e) {
            log.error("connectDs failed, uid={}, clusterId={}, dsType={}, {}", uid, fo.getBindClusterId(), fo.getDataSourceType(), e.getMessage(), e);
            ConnectDsResultVO result = new ConnectDsResultVO();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return ResWebDataUtils.buildSuccess(result);
        }
    }

    @RequestAuth(value = DM_DS_MANAGE, level = HIGH)
    @RequestMapping(value = "/upsertDsConfig", method = RequestMethod.POST)
    public ResWebData<?> upsertDsConfig(@RequestBody UpsertDsConfigFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.objectCacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.rdpAuthServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        this.dmDsService.upsertDsConfigs(puid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/testConnect", method = RequestMethod.POST)
    public ResWebData<?> testConnect(@Valid @RequestBody TestDsConnectionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getLevels().size() < 2) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getLevels());
        List<Long> dsIds = this.dmDsAuthService.listResByUser(uid, AuthKind.DataSource);
        if (!dsIds.contains(dsLevels.dsDO().getId())) {
            return ResWebDataUtils.buildError(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.NoAuthority, dsLevels.dsDO().getDataSourceType()));
        } else {
            this.dmDsService.testConnect(puid, uid, dsLevels);
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/listDsBindCluster", method = RequestMethod.POST)
    public ResWebData<?> listBindCluster(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<ClusterVO> clusterVOs = this.clusterService.listByOwnerUid(puid);
        return ResWebDataUtils.buildSuccess(clusterVOs);
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/listSpec", method = RequestMethod.POST)
    public ResWebData<?> listSpec(@RequestBody @Valid SpecListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmSecSpecDO> specPage = this.checkRulesService.querySpecList(puid, fo.getSearch());
        List<SpecVO> collect = specPage.stream().map(DmConvertUtils::convertToDmSecSpecVO).collect(Collectors.toList());

        return ResWebDataUtils.buildSuccess(collect);
    }
}
