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
package com.clougence.clouddm.console.web.controller.openapi;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResApiData;
import com.clougence.clouddm.api.common.rpc.ResApiDataUtils;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.mcp.McpApiProvider;
import com.clougence.clouddm.console.web.global.mcp.model.McpTool;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsDetailFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsLeafFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsLevelsFO;
import com.clougence.clouddm.console.web.model.fo.openapi.DmApiDsListFO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.openapi.DmApiDataSourceVO;
import com.clougence.clouddm.console.web.model.vo.openapi.DmConsoleSettingsVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.component.openapi.OpenApiSessionManager;
import com.clougence.rdp.service.RdpDsEnvService;
import com.clougence.rdp.service.openapi.RdpDsOpenApiService;
import com.clougence.rdp.service.openapi.model.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/12/9 16:38:05
 */
@McpApiProvider
@RestController
@RequestMapping(value = DmControllerUrlPrefix.OPEN_API_PREFIX + "/datasource")
@Slf4j
public class DataSourceApi extends BasicApi {

    @Resource
    private RdpDsOpenApiService rdpDsOpenApiService;
    @Resource
    private DmResAuthService    dmDsAuthService;
    @Resource
    private DmDsService         dmDsService;
    @Resource
    private RdpDsEnvService     rdpDsEnvService;
    @Resource
    private BrowseService       browseService;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmAuthServiceForBiz dmAuthServiceForBiz;

    @McpTool(DmMcpI18nKey.M_DS_SETTINGS)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/dsSettings", method = { RequestMethod.POST })
    public ResWebData<List<DmConsoleSettingsVO>> dsSettings(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        List<DmConsoleSettingsVO> settings = new ArrayList<>();
        Map<DataSourceType, DsConfig> dsConfigMap = this.dmDsService.dsConstantSettings();
        dsConfigMap.forEach((dsType, dsConfig) -> {
            DmConsoleSettingsVO vo = new DmConsoleSettingsVO();
            vo.setDsType(dsType);
            vo.setClassify(dsConfig.getClassify());
            vo.setCaseType(dsConfig.getConstant().getCaseType());
            vo.setLeftQualifier(dsConfig.getConstant().getLeftQualifier());
            vo.setRightQualifier(dsConfig.getConstant().getRightQualifier());
            vo.setLevels(dsConfig.getCategories().getLevels());
            vo.setLeafExpand(dsConfig.getCategories().getLeafExpand());
            settings.add(vo);
        });

        return ResWebDataUtils.buildSuccess(settings);
    }

    @McpTool(DmMcpI18nKey.M_LIST_DS)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listDs", method = RequestMethod.POST)
    public ResApiData<List<DmApiDataSourceVO>> listDs(@RequestBody @Valid DmApiDsListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("listDs for open api request id :" + requestId);

        // has auth dsIds
        List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
        if (dsIds.isEmpty()) {
            return ResApiDataUtils.buildSuccess(Collections.emptyList());
        }

        // filter by auth
        List<ApiDataSourceVO> apiVos = rdpDsOpenApiService.listDs(requestId, puid, DmConvertUtils.convertToApiListDsFO(fo));
        apiVos.removeIf(vo -> !dsIds.contains(vo.getId()));

        // filter by enable
        List<DmDsConfigDO> dmDsConf = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        Set<Long> dsCollect = dmDsConf.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toSet());
        apiVos.removeIf(vo -> !dsCollect.contains(vo.getId()));

        // return
        List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
        Map<Long, DmSysEnvDO> envMap = new HashMap<>();
        dsEnvDOList.forEach(e -> envMap.put(e.getId(), e));

        Map<Long, DmSysEnvDO> dsEnvMapping = new LinkedHashMap<>();
        dmDsConf.forEach(d -> {
            if (envMap.containsKey(d.getBindEnvId())) {
                dsEnvMapping.put(d.getDataSourceId(), envMap.get(d.getBindEnvId()));
            }
        });

        List<DmApiDataSourceVO> list = apiVos.stream().map(v -> {
            return DmConvertUtils.convertToDmApiDataSourceVO(v, dsEnvMapping);
        }).collect(Collectors.toList());
        return ResApiDataUtils.buildSuccess(requestId, list);
    }

    @McpTool(DmMcpI18nKey.M_DETAIL_DS)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/detailDs", method = RequestMethod.POST)
    public ResApiData<ApiDataSourceVO> detailDs(@RequestBody @Valid DmApiDsDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("queryDs for open api request id :" + requestId);

        // has auth dsIds
        List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
        if (dsIds.isEmpty()) {
            return ResApiDataUtils.buildSuccess(null);
        }

        // filter by auth
        ApiQueryDsFO copyFO = new ApiQueryDsFO();
        copyFO.setDataSourceId(fo.getDataSourceId());
        ApiDataSourceVO apiVo = this.rdpDsOpenApiService.queryDs(puid, copyFO);
        if (!dsIds.contains(apiVo.getId())) {
            return ResApiDataUtils.buildSuccess(null);
        }

        // filter by enable
        List<DmDsConfigDO> dmDsConfigDOS = this.dmDsService.fetchDsConfigByIds(puid, dsIds);
        Set<Long> collect = dmDsConfigDOS.stream().map(DmDsConfigDO::getDataSourceId).collect(Collectors.toSet());
        if (!collect.contains(apiVo.getId())) {
            return ResApiDataUtils.buildSuccess(null);
        }

        // return
        return ResApiDataUtils.buildSuccess(requestId, apiVo);
    }

    @McpTool(DmMcpI18nKey.M_LIST_LEVELS)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listLevels", method = RequestMethod.POST)
    public ResWebData<List<BrowseLevelsVO>> listLevels(@Valid @RequestBody DmApiDsLevelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        if (CollectionUtils.isEmpty(fo.getLevels())) {
            // env list
            List<Long> dsIds = this.dmDsAuthService.listResByUserContainAnyAuth(uid, AuthKind.DataSource);
            if (dsIds.isEmpty()) {
                return ResWebDataUtils.buildSuccess(Collections.emptyList());
            }

            List<DmSysEnvDO> dsEnvDOList = this.rdpDsEnvService.listDsEnv(puid, uid, null);
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
            this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
            DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels());
            this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);
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

    @McpTool(DmMcpI18nKey.M_LIST_LEAF)
    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/listLeaf", method = RequestMethod.POST)
    public ResWebData<List<BrowseLevelsVO>> listLeaf(@Valid @RequestBody DmApiDsLeafFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        if (fo.getLevels().size() < 2) {
            return ResWebDataUtils.buildSuccess(Collections.emptyList());
        }

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels());
        this.dmAuthServiceForBiz.checkBrowseAuth(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getLeafType());
        List<BrowseLevelsVO> vos = this.browseService.listLeaf(puid, uid, levels, leafType, null, fo.isRefreshCache());

        ResourceAccessInfo resourceAccessInfo = this.dmDsAuthService.getAllowBrowseInfo(levels, uid);
        if (!resourceAccessInfo.isAllAllow()) {
            vos = vos.stream().filter(vo -> {
                return resourceAccessInfo.getAllowQueryList().contains(vo.getObjName());
            }).collect(Collectors.toList());
        }
        return ResWebDataUtils.buildSuccess(vos);
    }

    //
    //
    //

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/addds", method = RequestMethod.POST)
    public ResApiData<?> addDataSource(@RequestParam("dataSourceAddData") String data, @RequestParam(value = "securityFile", required = false) MultipartFile securityFile,
                                       @RequestParam(value = "secretFile", required = false) MultipartFile secretFile, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        ResWebData<Long> res = rdpDsOpenApiService.addDs(data, securityFile, secretFile, uid, puid);
        if (!res.isPermission() || !res.isSuccess()) {
            return ResApiDataUtils.buildError(res.getCode(), res.getMsg());
        } else {
            return ResApiDataUtils.buildSuccess(res.getData());
        }
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/deleteds", method = RequestMethod.POST)
    public ResApiData<?> deleteDs(@RequestBody @Valid ApiDeleteDsFO data, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("deleteDataSource for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.deleteDs(puid, data);
        return ResApiDataUtils.buildSuccess(requestId, null);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updatedatasourcedesc", method = RequestMethod.POST)
    public ResApiData<?> updateDataSourceDesc(@RequestBody @Valid ApiUpdateDsDescFO updateFO, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("updateDataSourceDesc for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.updateDsDesc(puid, updateFO);
        return ResApiDataUtils.buildSuccess(requestId);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updateaccountandpassword", method = RequestMethod.POST)
    public ResApiData<?> updateAccountAndPassword(@RequestParam("DataSourceUpdateData") String data,
                                                  @RequestParam(value = "securityFile", required = false) MultipartFile securityFile,
                                                  @RequestParam(value = "secretFile", required = false) MultipartFile secretFile, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("updateAccountAndPassword for open api request id :" + requestId);

        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.updateAccountAndPasswd(data, securityFile, secretFile, puid);
        return ResApiDataUtils.buildSuccess(requestId);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updatepublichost", method = RequestMethod.POST)
    public ResApiData<?> updatePublicHost(@RequestBody @Valid ApiUpdatePubHostFO updateFO, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("updatepublichost for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.updatePublicHost(puid, updateFO);
        return ResApiDataUtils.buildSuccess(requestId);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/updateprivatehost", method = RequestMethod.POST)
    public ResApiData<?> updatePrivateHost(@RequestBody @Valid ApiUpdatePriHostFO updateFO, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("updateprivatehost for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.updatePrivateHost(puid, updateFO);
        return ResApiDataUtils.buildSuccess(requestId);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/querydsconfig", method = RequestMethod.POST)
    public ResApiData<?> queryDsCOnfig(@RequestBody @Valid ApiListDsKvConfigsByDsIdFO fo, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("queryJobById for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<ApiDsKvConfigVo> apiConfVos = rdpDsOpenApiService.listDsKvConfs(puid, fo);
        return ResApiDataUtils.buildSuccess(requestId, apiConfVos);
    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/upsertdsconfig", method = RequestMethod.POST)
    public ResApiData<?> upsertDsConfig(@RequestBody @Valid ApiUpsertDsKvConfigFO configFO, HttpServletRequest request) {
        String requestId = (String) request.getAttribute(OpenApiSessionManager.OPEN_API_REQUEST_ID);
        log.info("queryJobById for open api request id :" + requestId);

        //api user must be an primary user,just check owner
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        rdpDsOpenApiService.upsertDsKvConfs(puid, configFO);
        return ResApiDataUtils.buildSuccess(requestId, null);
    }
}
