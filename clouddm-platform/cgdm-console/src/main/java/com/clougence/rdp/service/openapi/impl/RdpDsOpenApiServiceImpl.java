package com.clougence.rdp.service.openapi.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsDeletePrepareService;
import com.clougence.rdp.component.cache.RdpResOwnerCacheService;
import com.clougence.rdp.controller.model.fo.AddDsFO;
import com.clougence.rdp.controller.model.fo.UpdateSecurityInfoFO;
import com.clougence.rdp.controller.model.fo.UpsertDsKvConfigFO;
import com.clougence.rdp.controller.model.vo.DsKvConfigVO;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.rdp.dal.model.queryobj.DsQueryParam;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.openapi.RdpDsOpenApiService;
import com.clougence.rdp.service.openapi.model.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RdpDsOpenApiServiceImpl implements RdpDsOpenApiService {

    @Resource
    private RdpAuthServiceForBiz    rdpAuthServiceForBiz;

    @Resource
    private RdpDsService            rdpDsService;

    @Resource
    private RdpResOwnerCacheService rdpResOwnerCacheService;
    @Resource
    private DmDsDeletePrepareService dmDsDeletePrepareService;

    @Override
    public List<ApiDataSourceVO> listDs(String requestId, String puid, ApiListDsFO fo) {
        DsQueryParam dsQueryParam = DsQueryParam.builder()
            .dataSourceType(fo.getType())
            .dataSourceDescLike(fo.getDataSourceDescLike())
            .dataSourceIds(Stream.of(fo.getDataSourceId()).filter(Objects::nonNull).collect(Collectors.toList()))
            .deployType(fo.getDeployType())
            .lifeCycleState(fo.getLifeCycleState())
            .dsHostLike(fo.getDsHostLike())
            .dataSourceType(fo.getType())
            .instanceIdLike(fo.getInstanceIdLike())
            .build();

        List<RdpResAuthDO> authList = this.rdpAuthServiceForBiz.listAuthByUser(puid, AuthKind.DataSource);
        if (authList == null || authList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> ids = authList.stream().map(RdpResAuthDO::getResId).distinct().collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(dsQueryParam.getDataSourceIds())) {
            dsQueryParam.setDataSourceIds(new ArrayList<>(ids));
        } else {
            if (!ids.containsAll(dsQueryParam.getDataSourceIds())) {
                throw new IllegalArgumentException("Have no auth to query specified DataSource.");
            }
        }

        List<RdpDataSourceDO> result = rdpDsService.fetchByCondition(dsQueryParam);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        }

        result = result.stream().filter(vo -> {
            if (HostType.PRIVATE == fo.getHostType()) {
                return StringUtils.isNotBlank(vo.getPrivateHost());
            } else if (HostType.PUBLIC == fo.getHostType()) {
                return StringUtils.isNotBlank(vo.getPublicHost());
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        return genFromDsVOs(result);
    }

    protected List<ApiDataSourceVO> genFromDsVOs(List<RdpDataSourceDO> dos) {
        List<ApiDataSourceVO> apiVos = new ArrayList<>();
        for (RdpDataSourceDO d : dos) {
            ApiDataSourceVO apiVo = new ApiDataSourceVO();
            apiVo.convertFromDsVO(d);
            apiVos.add(apiVo);
        }

        return apiVos;
    }

    @Override
    public ApiDataSourceVO queryDs(String puid, ApiQueryDsFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        RdpDataSourceDO result = rdpDsService.queryDsByIdWithoutPasswd(fo.getDataSourceId());
        ApiDataSourceVO apiVo = new ApiDataSourceVO();
        apiVo.convertFromDsVO(result);

        return apiVo;
    }

    @Override
    public ResWebData<Long> addDs(String data, MultipartFile securityFile, MultipartFile secretFile, String uid, String puid) {
        if (StringUtils.isBlank(data)) {
            throw new IllegalArgumentException("data can not be empty.");
        }

        AddDsFO addDsFO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            addDsFO = objectMapper.readValue(data, new TypeReference<AddDsFO>() {
            });
        } catch (Exception e) {
            String msg = "deserialize add ds info error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        if (addDsFO == null) {
            throw new IllegalArgumentException("datasource data is illegal.");
        }

        addDsFO.setSecurityFile(securityFile);
        addDsFO.setSecretFile(secretFile);

        // do not change the order for check security file and secret file;
        addDsFO.manualValidAndTrim();
        return rdpDsService.addDataSource(puid, uid, addDsFO);
    }

    @Override
    public void deleteDs(String puid, ApiDeleteDsFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        this.dmDsDeletePrepareService.prepareDelete(puid, fo.getDataSourceId());
        rdpDsService.delDataSource(puid, fo.getDataSourceId());
    }

    @Override
    public void updateDsDesc(String puid, ApiUpdateDsDescFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        rdpDsService.updateDataSourceDesc(puid, fo.getDataSourceId(), fo.getInstanceDesc());
    }

    @Override
    public void updateAccountAndPasswd(String data, MultipartFile securityFile, MultipartFile secretFile, String puid) {
        if (StringUtils.isBlank(data)) {
            throw new IllegalArgumentException("data can not be empty.");
        }

        UpdateSecurityInfoFO updateFO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            updateFO = objectMapper.readValue(data, new TypeReference<UpdateSecurityInfoFO>() {
            });
        } catch (Exception e) {
            String msg = "deserialize updateFO ds info error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        if (updateFO == null) {
            throw new IllegalArgumentException("datasource data is illegal.");
        }

        rdpResOwnerCacheService.ownDataSource(puid, updateFO.getDataSourceId());

        updateFO.setSecurityFile(securityFile);
        updateFO.setSecretFile(secretFile);

        rdpDsService.updateDataSourceAccount(puid, updateFO);
    }

    @Override
    public void updateAliyunRdsAkSk(String puid, ApiUpdateAliyunRdsAkSkFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        rdpDsService.updateAliyunRdsAkSk(puid, fo.getDataSourceId(), fo.getAccessKey(), fo.getSecretKey());
    }

    @Override
    public void updatePrivateHost(String puid, ApiUpdatePriHostFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        rdpDsService.updateDataSourcePrivateHost(puid, fo.getDataSourceId(), fo.getPrivateHost());
    }

    @Override
    public void updatePublicHost(String puid, ApiUpdatePubHostFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        rdpDsService.updateDataSourcePublicHost(puid, fo.getDataSourceId(), fo.getPublicHost());
    }

    @Override
    public void cleanDsAccount(String puid, ApiDeleteAccountFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());
        rdpDsService.cleanDataSourceAccount(puid, fo.getDataSourceId());
    }

    @Override
    public List<ApiDsKvConfigVo> listDsKvConfs(String puid, ApiListDsKvConfigsByDsIdFO fo) {
        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());

        List<DsKvConfigVO> confVos;
        if (StringUtils.isNotBlank(fo.getConfigName())) {
            DsKvConfigVO vo = rdpDsService.queryDsConfig(fo.getDataSourceId(), fo.getConfigName());

            if (vo == null) {
                return new ArrayList<>();
            }

            confVos = Collections.singletonList(vo);
        } else {
            confVos = rdpDsService.queryDsConfigs(fo.getDataSourceId());
        }

        List<ApiDsKvConfigVo> apiConfVos = new ArrayList<>();
        for (DsKvConfigVO v : confVos) {
            ApiDsKvConfigVo c = new ApiDsKvConfigVo();
            c.convertFromDsKvConfigVO(v);
            apiConfVos.add(c);
        }

        return apiConfVos;
    }

    @Override
    public void upsertDsKvConfs(String puid, ApiUpsertDsKvConfigFO fo) {
        if (fo.getUpdateConfigs() == null && fo.getNeedCreateConfigs() == null) {
            throw new IllegalArgumentException("update config map and need create config map are both empty.");
        }

        rdpResOwnerCacheService.ownDataSource(puid, fo.getDataSourceId());

        UpsertDsKvConfigFO c = fo.genUpsertDsKvConfigFo();
        rdpDsService.upsertDsConfigs(puid, c);
    }
}
