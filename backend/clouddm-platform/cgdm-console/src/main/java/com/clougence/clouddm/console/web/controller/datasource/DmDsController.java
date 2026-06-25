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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.CheckDriverVersionFO;
import com.clougence.clouddm.console.web.model.fo.QueryDsFO;
import com.clougence.clouddm.console.web.model.fo.checkrules.SpecListFO;
import com.clougence.clouddm.console.web.model.fo.datasource.*;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.console.web.model.vo.DsKvConfigVO;
import com.clougence.clouddm.console.web.model.vo.RdpDataSourceVO;
import com.clougence.clouddm.console.web.model.vo.checkrules.SpecVO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DmSimpleDsVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DsBindEnvNodeVO;
import com.clougence.clouddm.console.web.model.vo.datasource.FetchDsAddConfigVO;
import com.clougence.clouddm.console.web.model.vo.datasource.FetchDsBindInfoVO;
import com.clougence.clouddm.console.web.model.vo.env.DsEnvVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.console.web.service.security.CheckRulesService;
import com.clougence.clouddm.console.web.service.upload.ConsoleUploadService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.RdpConvertUtils;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.secrule.DmSecSpecDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.utils.CollectionUtils;

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
    private DmDsWebService       dsService;
    @Resource
    private DmDsService          dmDsService;
    @Resource
    private DmResAuthService     authService;
    @Resource
    private ObjectCacheDao       cacheDao;
    @Resource
    private DmAuthServiceForBiz  authServiceForBiz;
    @Resource
    private CheckRulesService    rulesService;
    @Resource
    private DmDsConfigService    dsConfigService;
    @Resource
    private DmDriverService      driverService;
    @Resource
    private RdpOpAuditService    auditService;
    @Resource
    private ClusterService       clusterService;
    @Resource
    private RdpDsEnvService      envService;
    @Resource
    private ConsoleUploadService uploadService;

    // drivers

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/checkDriverStatus", method = RequestMethod.POST)
    public ResWebData<DriverVersionStatusVO> checkDriverStatus(@RequestBody @Valid CheckDriverVersionFO fo) {
        DriverVersionStatusVO statusVO = this.driverService.checkDriverStatus(fo.getClusterId(), fo.getDriverFamily(), fo.getDriverVersion());
        return ResWebDataUtils.buildSuccess(statusVO);
    }

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/downloadDriver", method = RequestMethod.POST)
    public ResWebData<?> downloadDriver(@RequestBody @Valid CheckDriverVersionFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.driverService.downloadDriver(uid, fo.getClusterId(), fo.getDriverFamily(), fo.getDriverVersion());
        return ResWebDataUtils.buildSuccess();
    }

    // ds add

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/fetchDsConfig", method = RequestMethod.POST)
    public ResWebData<?> fetchDsConfig(@RequestBody @Valid FetchDsAddConfigFO fo) {
        DataSourceType dsType = fo.getDsType();

        FetchDsAddConfigVO vo = new FetchDsAddConfigVO();
        vo.setPanels(UiWebUtil.addDsUiPanels2VO(this.dsConfigService.fetchDsConfigPanels(dsType)));

        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/uploadCertificate", method = RequestMethod.POST)
    public ResWebData<?> uploadCertificate(@RequestParam("file") MultipartFile file) {
        return ResWebDataUtils.buildSuccess(this.uploadService.uploadCertificate(file));
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<Long> addDs(@RequestBody @Valid DsConfigSubmitFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getClusterId() != null) {
            this.cacheDao.ownCluster(AuthDal.ROOT_USER_UID, fo.getClusterId());
        }

        ResWebData<Long> result = this.dsService.addDataSource(uid, fo);
        this.auditService.logAndAddOperationAudit(AuthDal.ROOT_USER_UID, uid, request.getRequestURI(), request.getRemoteAddr(), result
            .getData(), "", SecurityLevel.HIGH, AuditType.ADD_DATA_SOURCE, ResourceType.DATASOURCE);
        return result;
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/connectDs", method = RequestMethod.POST)
    public ResWebData<?> connectDs(@Valid @RequestBody DsConfigSubmitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getClusterId() != null) {
            this.cacheDao.ownCluster(puid, fo.getClusterId());
        }
        return ResWebDataUtils.buildSuccess(this.dsService.testConnect(uid, fo));
    }

    // ds manager

    @RequestAuth(DM_DS_READ)
    @RequestMapping(value = "/listByCondition", method = RequestMethod.POST)
    public ResWebData<?> listByCondition(@RequestBody @Valid ListDsFO listDsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DmAuthResDO> authList = this.authService.listAuthByUser(uid, AuthKind.DataSource);
        if (authList == null || authList.isEmpty()) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }

        List<Long> authedDsIds = authList.stream().map(DmAuthResDO::getResId).distinct().toList();
        ArgDsQueryParamObj queryMO = ArgDsQueryParamObj.builder()
            .dataSourceType(listDsFO.getType())
            .dataSourceDescLike(listDsFO.getDataSourceDescLike())
            .dataSourceIds(Stream.of(listDsFO.getDataSourceId()).filter(Objects::nonNull).collect(Collectors.toList()))
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

        List<DmDsDO> result = this.dsService.fetchByCondition(puid, queryMO, true);
        if (CollectionUtils.isEmpty(result)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        } else {
            List<DmSimpleDsVO> vos = genAndFilterToSimpleVO(puid, result, listDsFO);
            return ResWebDataUtils.buildSuccess(vos);
        }
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/fetchBindInfo", method = RequestMethod.POST)
    public ResWebData<?> fetchBindInfo(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<DsEnvVO> envs = DsEnvVO.generateVO(this.envService.listDsEnv(puid, uid, null));
        List<ClusterVO> clusters = this.clusterService.listByOwnerUid(puid);
        if (clusters == null) {
            clusters = Collections.emptyList();
        }

        FetchDsBindInfoVO vo = new FetchDsBindInfoVO();
        final List<ClusterVO> bindClusters = clusters;
        vo.setEnvs(envs);
        vo.setClusters(bindClusters);
        vo.setEnvClusterTree(envs.stream().map(env -> {
            DsBindEnvNodeVO node = new DsBindEnvNodeVO();
            node.setId(env.getId());
            node.setOwnerUid(env.getOwnerUid());
            node.setEnvName(env.getEnvName());
            node.setDescription(env.getDescription());
            node.setQueryLimit(env.getQueryLimit());
            node.setChildren(new ArrayList<>(bindClusters));
            return node;
        }).collect(Collectors.toList()));
        return ResWebDataUtils.buildSuccess(vo);
    }

    private List<DmSimpleDsVO> genAndFilterToSimpleVO(String puid, List<DmDsDO> dos, ListDsFO listDsFO) {
        List<DmSimpleDsVO> vos = new ArrayList<>();
        if (CollectionUtils.isEmpty(dos)) {
            return vos;
        }

        List<Long> dsIds = dos.stream().map(DmDsDO::getId).collect(Collectors.toList());

        List<DmDsDO> confList = this.dsService.fetchDsConfigByIds(puid, dsIds);
        Map<Long, DmDsDO> confMap = confList.stream().collect(Collectors.toMap(DmDsDO::getId, d -> d));

        vos = dos.stream().map(ds -> DmConvertUtils.convertToDmSimpleDsVO(ds, confMap)).collect(Collectors.toList());
        return vos;
    }

    @RequestAuth(DM_DS_READ)
    @RequestMapping(value = "/queryDsConfig", method = RequestMethod.POST)
    public ResWebData<?> queryDsConfig(@RequestBody QueryDsConfigFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.cacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.authServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);

        List<String> blackList = Arrays.asList(       //
                DataSourceConfig.Fields.host,         //
                DataSourceConfig.Fields.securityType, //
                DataSourceConfig.Fields.userName,     //
                DataSourceConfig.Fields.password,     //
                DataSourceConfig.Fields.configVersion);

        List<DsKvConfigVO> vos = this.dsService.queryDsConfigIncludeNewEntries(fo.getDataSourceId());
        vos = vos.stream().filter(c -> !blackList.contains(c.getConfigName())).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/testConnect", method = RequestMethod.POST)
    public ResWebData<?> testConnect(@Valid @RequestBody TestDsConnectionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getDataSourceId() != null) {
            this.cacheDao.ownCluster(puid, fo.getClusterId());
            this.cacheDao.ownDataSource(puid, fo.getDataSourceId());
            this.authServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

            try {
                String version = this.dmDsService.testConnect(puid, fo.getDataSourceId(), fo.getClusterId());
                return ResWebDataUtils.buildSuccess(version);
            } catch (Exception e) {
                log.error("testDsConnect failed, " + e.getMessage());
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TEST_CONNECT_ERROR.name(), e.getMessage()));
            }
        }

        if (CollectionUtils.isEmpty(fo.getLevels()) || fo.getLevels().size() < 2) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        DsLevels dsLevels = this.dsConfigService.parseLevels(fo.getLevels());
        List<Long> dsIds = this.authService.listResByUser(uid, AuthKind.DataSource);
        if (!dsIds.contains(dsLevels.dsDO().getId())) {
            return ResWebDataUtils.buildError(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.NoAuthority, dsLevels.dsDO().getDataSourceType()));
        } else {
            this.dmDsService.testConnect(puid, uid, dsLevels);
            return ResWebDataUtils.buildSuccess();
        }
    }

    @RequestAuth(value = DM_DS_MANAGE, level = HIGH)
    @RequestMapping(value = "/upsertDsConfig", method = RequestMethod.POST)
    public ResWebData<?> upsertDsConfig(@RequestBody UpsertDsConfigFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        this.cacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.authServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        this.dsService.upsertConfigs(puid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/listSpec", method = RequestMethod.POST)
    public ResWebData<?> listSpec(@RequestBody @Valid SpecListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<DmSecSpecDO> specPage = this.rulesService.querySpecList(puid, fo.getSearch());
        List<SpecVO> collect = specPage.stream().map(DmConvertUtils::convertToDmSecSpecVO).collect(Collectors.toList());

        return ResWebDataUtils.buildSuccess(collect);
    }
}
