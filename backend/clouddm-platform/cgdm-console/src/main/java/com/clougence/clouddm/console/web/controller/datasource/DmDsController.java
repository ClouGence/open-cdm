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
import com.clougence.clouddm.console.web.component.dsconfig.impl.DmDsConfigHelper;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfigKvDef;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.CheckDriverVersionFO;
import com.clougence.clouddm.console.web.model.fo.datasource.*;
import com.clougence.clouddm.console.web.model.lo.UpdateDsDescLO;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.console.web.model.vo.cluster.ClusterVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DmSimpleDsVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DsBindEnvNodeVO;
import com.clougence.clouddm.console.web.model.vo.datasource.FetchDsAddConfigVO;
import com.clougence.clouddm.console.web.model.vo.datasource.FetchDsBindInfoVO;
import com.clougence.clouddm.console.web.model.vo.env.DsEnvVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.cluster.ClusterService;
import com.clougence.clouddm.console.web.service.datasource.DmDsWebService;
import com.clougence.clouddm.console.web.service.upload.ConsoleUploadService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RandomStrUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.datasource.ArgDsQueryParamObj;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
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
    private DmDsWebService       dsService;
    @Resource
    private DmDsService          dmDsService;
    @Resource
    private DataSourceDal        dsDal;
    @Resource
    private DmResAuthService     authService;
    @Resource
    private ObjectCacheDao       cacheDao;
    @Resource
    private DmAuthServiceForBiz  authServiceForBiz;
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
    public ResWebData<?> fetchDsConfig(@RequestBody @Valid FetchDsAddConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DataSourceType dsType = fo.getDsType();
        Map<String, String> defaultConfig = new LinkedHashMap<>();
        DmDsDO dsDO = null;

        if (fo.getDsId() != null && fo.getDsId() > 0) {
            this.cacheDao.ownDataSource(puid, fo.getDsId());
            this.authServiceForBiz.checkResAuth(puid, uid, fo.getDsId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);
            dsDO = this.dmDsService.fetchAndCheckById(fo.getDsId());
            dsType = dsDO.getDataSourceType();
            DataSourceConfig dsConfig = this.dsConfigService.fetchFullDsConfigFromExists(fo.getDsId());
            defaultConfig = DmDsConfigHelper.collectConfigs(dsConfig)
                .stream()
                .collect(Collectors.toMap(DsConfigKvDef::getConfigName, DsConfigKvDef::getConfigValue, (oldVal, newVal) -> newVal, LinkedHashMap::new));
            defaultConfig.put("dsId", String.valueOf(fo.getDsId()));
        }

        FetchDsAddConfigVO vo = new FetchDsAddConfigVO();
        String instanceId = dsDO == null ? dsType.getShortName() + "-" + RandomStrUtils.fixedLenRandomStr(15) : dsDO.getInstanceId();
        vo.setDsId(dsDO == null ? null : dsDO.getId());
        vo.setDsType(dsType);
        vo.setEnvId(dsDO == null ? null : dsDO.getDsEnvId());
        vo.setClusterId(dsDO == null ? null : dsDO.getBindClusterId());
        vo.setInstanceId(instanceId);
        vo.setInstanceName(dsDO == null ? instanceId : dsDO.getInstanceDesc());
        vo.setPanels(UiWebUtil.addDsUiPanels2VO(this.dsConfigService.fetchDsConfigPanels(dsType, defaultConfig)));

        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_DS_MANAGE)
    @RequestMapping(value = "/uploadCertificate", method = RequestMethod.POST)
    public ResWebData<?> uploadCertificate(@RequestParam("file") MultipartFile file) {
        return ResWebDataUtils.buildSuccess(this.uploadService.uploadCertificate(file));
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/addDs", method = RequestMethod.POST)
    public ResWebData<Long> addDs(@RequestBody @Valid DsConfigSubmitFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getClusterId() != null) {
            this.cacheDao.ownCluster(AuthDal.ROOT_USER_UID, fo.getClusterId());
        }

        ResWebData<Long> result = this.dsService.addDs(uid, fo);
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
            List<Long> dsIds = result.stream().map(DmDsDO::getId).collect(Collectors.toList());
            List<DmDsDO> confList = this.dsService.fetchDsConfigByIds(puid, dsIds);
            Map<Long, DmDsDO> confMap = confList.stream().collect(Collectors.toMap(DmDsDO::getId, d -> d));
            List<DmSimpleDsVO> vos = result.stream().map(ds -> DmConvertUtils.convertToDmSimpleDsVO(ds, confMap)).collect(Collectors.toList());
            return ResWebDataUtils.buildSuccess(vos);
        }
    }

    @RequestAuth(value = DM_DS_MANAGE, level = HIGH)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> deleteDs(@RequestBody @Valid DeleteDsFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        long dataSourceId = fo.getDataSourceId();

        this.cacheDao.ownDataSource(puid, dataSourceId);
        this.authServiceForBiz.checkResAuth(puid, uid, dataSourceId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        DmDsDO dsDO = this.dsService.queryById(dataSourceId);
        String instanceId = dsDO == null ? String.valueOf(dataSourceId) : dsDO.getInstanceId();

        ResWebData<Long> result = this.dsService.delDataSource(puid, dataSourceId);
        this.auditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), dataSourceId, fo, HIGH, AuditType.DELETE_DATA_SOURCE, ResourceType.DATASOURCE, instanceId);
        return result;
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updateDs", method = RequestMethod.POST)
    public ResWebData<Long> updateDs(@RequestBody @Valid DsConfigSubmitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        Long dsId = fo.getDsId();
        if (dsId == null || dsId <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_ID_REQUIRED_ERROR.name()));
        }

        this.cacheDao.ownDataSource(puid, dsId);
        this.authServiceForBiz.checkResAuth(puid, uid, dsId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);
        if (fo.getClusterId() != null) {
            this.cacheDao.ownCluster(puid, fo.getClusterId());
        }

        ResWebData<Long> result = this.dsService.updateDs(uid, fo);
        this.auditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), dsId, fo, SecurityLevel.HIGH, AuditType.UPDATE_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        return result;
    }

    @RequestAuth(value = DM_DS_MANAGE, level = HIGH)
    @RequestMapping(value = "/updateDsDesc", method = RequestMethod.POST)
    public ResWebData<?> updateDsDesc(@RequestBody @Valid UpdateDsDescFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        long dataSourceId = fo.getDataSourceId();

        this.cacheDao.ownDataSource(puid, dataSourceId);
        this.authServiceForBiz.checkResAuth(puid, uid, dataSourceId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        UpdateDsDescLO updateLO = this.dsService.updateDataSourceDesc(puid, dataSourceId, fo.getInstanceDesc());
        this.auditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
            .getRemoteAddr(), dataSourceId, updateLO, HIGH, AuditType.UPDATE_DATA_SOURCE_DESC, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/testConnect", method = RequestMethod.POST)
    public ResWebData<?> testConnect(@Valid @RequestBody TestDsConnectionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getDataSourceId() == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        this.cacheDao.ownDataSource(puid, fo.getDataSourceId());
        this.authServiceForBiz.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        DataSourceType dsType = null;
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(fo.getDataSourceId());
        if (dsDO != null) {
            dsType = dsDO.getDataSourceType();
        }

        try {
            String version = this.dmDsService.testConnect(fo.getDataSourceId());
            return ResWebDataUtils.buildSuccess(version);
        } catch (Exception e) {
            log.error("testDsConnect failed, " + e.getMessage());
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TEST_CONNECT_ERROR.name(), connectErrorMessage(e, dsType)));
        }
    }

    private String connectErrorMessage(Exception e, DataSourceType dsType) {
        String message = ExceptionUtils.getRootCauseMessage(e);
        if (StringUtils.isBlank(message)) {
            message = e.getMessage();
        }
        return stripExceptionPrefix(message);
    }

    private String stripExceptionPrefix(String message) {
        if (StringUtils.isBlank(message)) {
            return message;
        }
        String result = message;
        while (true) {
            String stripped = result.replaceFirst("^(?:[\\w.$]+Exception|[\\w.$]+Error):\\s*", "");
            if (stripped.equals(result)) {
                return result;
            }
            result = stripped;
        }
    }

    // form Utils

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
}
