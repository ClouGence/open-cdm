package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_MANAGER;
import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_READ;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_DS_MANAGE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_DS_READ;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMAction;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsDeletePrepareService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDriverService;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.component.dskvconfig.model.LLMExtraConfig;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.SecurityLevel;
import com.clougence.rdp.constant.operation.AuditType;
import com.clougence.rdp.controller.model.fo.*;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.lo.UpdateDsConfigLO;
import com.clougence.rdp.controller.model.lo.UpdateDsDescLO;
import com.clougence.rdp.controller.model.lo.UpdatePriHostLO;
import com.clougence.rdp.controller.model.lo.UpdatePubHostLO;
import com.clougence.rdp.controller.model.vo.*;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpResAuthDO;
import com.clougence.rdp.dal.model.queryobj.DsQueryParam;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpAuthUtils;
import com.clougence.rdp.util.RdpConvertUtils;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-15 11:10
 * @since 1.1.3
 */
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/datasource")
@Slf4j
public class RdpDsController {

    @Resource
    private RdpDsService         rdpDsService;
    @Resource
    private RdpAuthServiceForBiz rdpAuthService;
    @Resource
    private RdpOpAuditService    rdpOpAuditService;
    @Resource
    private DmDriverService      dmDriverService;
    @Resource
    private DmDsDeletePrepareService dmDsDeletePrepareService;

    @RequestAuth(RDP_DS_READ)
    @RequestMapping(value = "/listbycondition", method = RequestMethod.POST)
    public ResWebData<?> listByCondition(@RequestBody @Valid ListDsFO listDsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<RdpResAuthDO> authList = this.rdpAuthService.listAuthByUser(uid, AuthKind.DataSource);
        if (authList == null || authList.isEmpty()) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        }

        List<Long> authedDsIds = authList.stream().map(RdpResAuthDO::getResId).distinct().collect(Collectors.toList());

        DsQueryParam queryMO = DsQueryParam.builder()
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
            if (!new HashSet<>(authedDsIds).containsAll(queryMO.getDataSourceIds())) {
                throw new IllegalArgumentException("DataSource have no auth.");
            }
        }

        List<RdpDataSourceDO> result = this.rdpDsService.fetchByCondition(puid, queryMO, true);
        if (CollectionUtils.isEmpty(result)) {
            return ResWebDataUtils.buildSuccess(new ArrayList<>());
        } else {
            List<RdpSimpleDsVO> vos = genAndFilterToSimpleVO(result, listDsFO);
            return ResWebDataUtils.buildSuccess(vos);
        }
    }

    private List<RdpSimpleDsVO> genAndFilterToSimpleVO(List<RdpDataSourceDO> dsDOs, ListDsFO listDsFO) {
        List<RdpSimpleDsVO> vos = new ArrayList<>();
        if (CollectionUtils.isEmpty(dsDOs)) {
            return vos;
        }

        vos = dsDOs.stream().map(this::genRdpSimpleDsVO).filter(vo -> {
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

    protected RdpSimpleDsVO genRdpSimpleDsVO(RdpDataSourceDO dsDO) {
        RdpSimpleDsVO vo = new RdpSimpleDsVO();
        vo.convertFromDO(dsDO);
        return vo;
    }

    @RequestAuth(RDP_DS_READ)
    @RequestMapping(value = "/queryds", method = RequestMethod.POST)
    public ResWebData<?> queryDs(@RequestBody @Valid QueryDsFO queryDsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.rdpAuthService.checkResAuth(puid, uid, queryDsFO.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);

        RdpDataSourceDO result = this.rdpDsService.queryDsByIdWithoutPasswd(queryDsFO.getDataSourceId());
        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), queryDsFO
            .getDataSourceId(), result, SecurityLevel.NORMAL, AuditType.QUERY_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        RdpDataSourceVO vo = RdpConvertUtils.convertToRdpDataSourceVO(result);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/dsKvConfigDef", method = RequestMethod.POST)
    public ResWebData<?> dsKvConfigDef(@RequestBody QueryDsDefaultKvConfigFO queryDsFO) {
        List<DefaultDsKvConfigVO> vos = rdpDsService.queryDsDefaultConfig(queryDsFO.getDataSourceType(), queryDsFO.getDeployEnvType());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/checkDriverStatus", method = RequestMethod.POST)
    public ResWebData<DriverVersionStatusVO> checkDriverStatus(@RequestBody @Valid CheckDriverVersionFO fo) {
        DriverVersionStatusVO statusVO = this.dmDriverService.checkDriverStatus(fo.getClusterId(),//
                fo.getDriverFamily(), fo.getDriverVersion());
        return ResWebDataUtils.buildSuccess(statusVO);
    }

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/downloadDriver", method = RequestMethod.POST)
    public ResWebData<?> downloadDriver(@RequestBody @Valid CheckDriverVersionFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmDriverService.downloadDriver(uid, fo.getClusterId(),//
                fo.getDriverFamily(), fo.getDriverVersion());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResWebData<Long> addDs(@RequestParam("DataSourceAddData") String data, //
                                  @RequestParam(value = "securityFile", required = false) MultipartFile securityFile, //
                                  @RequestParam(value = "secretFile", required = false) MultipartFile secretFile, //
                                  @RequestParam(value = "clientSecurityFile", required = false) MultipartFile clientSecurityFile, //
                                  HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        AddDsFO addDsFO = RdpConvertUtils.convertToAddDsFO(data);
        if (addDsFO == null) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.DS_ADD_DATA_ILLEGAL_ERROR.name()));
        }

        addDsFO.setSecurityFile(securityFile);
        addDsFO.setClientSecurityFile(clientSecurityFile);
        addDsFO.setSecretFile(secretFile);

        // do not change the order for check security file and secret file;
        addDsFO.manualValidAndTrim();
        ResWebData<Long> longResWebData = this.rdpDsService.addDataSource(puid, uid, addDsFO);
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), longResWebData
            .getData(), "", SecurityLevel.HIGH, AuditType.ADD_DATA_SOURCE, ResourceType.DATASOURCE);
        return longResWebData;
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResWebData<?> delDs(@RequestBody @Valid DeleteDsFO deleteDsFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = deleteDsFO.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        RdpDataSourceDO rdpDataSourceDO = rdpDsService.fetchAndCheckById(resId);
        this.dmDsDeletePrepareService.prepareDelete(puid, resId);
        ResWebData<Long> longResWebData = this.rdpDsService.delDataSource(puid, resId);

        if (longResWebData.isSuccess()) {
            this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request
                .getRemoteAddr(), resId, "", SecurityLevel.HIGH, AuditType.DELETE_DATA_SOURCE, ResourceType.DATASOURCE, rdpDataSourceDO.getInstanceId());
        }

        return longResWebData;
    }

    @RequestAuth(RDP_DS_READ)
    @RequestMapping(value = "/querydsconfig", method = RequestMethod.POST)
    public ResWebData<?> queryDsConfig(@RequestBody QueryDsConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);

        List<DsKvConfigVO> vos = this.rdpDsService.queryDsConfigs(fo.getDataSourceId());
        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), vos, SecurityLevel.NORMAL, AuditType.QUERY_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(RDP_DS_READ)
    @RequestMapping(value = "/queryllmconfig", method = RequestMethod.POST)
    public ResWebData<?> queryLLMConfig(@RequestBody DsLLMConfigModelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_READ, AuthKind.DataSource);

        LLMExtraConfig llmExtraConfig = new LLMExtraConfig();
        DsKvConfigVO vos;

        if (fo.getLlmAction() == LLMAction.EMBEDDING) {
            vos = this.rdpDsService.queryDsConfig(fo.getDataSourceId(), LLMExtraConfig.Fields.llmEmbedding);
            if (vos != null) {
                llmExtraConfig.setLlmEmbedding(vos.getConfigValue());
            }
        } else if (fo.getLlmAction() == LLMAction.CHAT) {
            vos = this.rdpDsService.queryDsConfig(fo.getDataSourceId(), LLMExtraConfig.Fields.llmChat);
            if (vos != null) {
                llmExtraConfig.setLlmChat(vos.getConfigValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported llm action: " + fo.getLlmAction());
        }

        llmExtraConfig.deserialize();

        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), vos, SecurityLevel.NORMAL, AuditType.QUERY_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);

        if (fo.getLlmAction() == LLMAction.EMBEDDING) {
            return ResWebDataUtils.buildSuccess(llmExtraConfig.getLlmEmbeddingConfigs());
        } else {
            return ResWebDataUtils.buildSuccess(llmExtraConfig.getLlmChatConfigs());
        }
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/upsertdsconfig", method = RequestMethod.POST)
    public ResWebData<?> upsertDsConfig(@RequestBody UpsertDsKvConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        List<UpdateDsConfigLO> configLOS = this.rdpDsService.upsertDsConfigs(puid, fo);
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), configLOS, SecurityLevel.HIGH, AuditType.UPDATE_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updatedatasourcedesc", method = RequestMethod.POST)
    public ResWebData<?> updateDataSourceDesc(@RequestBody @Valid UpdateDsDescFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        UpdateDsDescLO dsDescLO = this.rdpDsService.updateDataSourceDesc(puid, fo.getDataSourceId(), fo.getInstanceDesc());
        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), dsDescLO, SecurityLevel.HIGH, AuditType.UPDATE_DATA_SOURCE_DESC, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updateaccountandpassword", method = RequestMethod.POST)
    public ResWebData<?> updateAccountAndPassword(@RequestParam("DataSourceUpdateData") String data,
                                                  @RequestParam(value = "securityFile", required = false) MultipartFile securityFile,
                                                  @RequestParam(value = "clientSecurityFile", required = false) MultipartFile clientSecurityFile,
                                                  @RequestParam(value = "secretFile", required = false) MultipartFile secretFile, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        UpdateSecurityInfoFO fo = RdpConvertUtils.convertToUpdateSecurityInfoFO(data);
        if (fo == null) {
            return ResWebDataUtils.buildError(RdpI18nUtils.getMessage(I18nRdpMsgKeys.DS_ADD_DATA_ILLEGAL_ERROR.name()));
        }

        this.rdpAuthService.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        fo.setSecurityFile(securityFile);
        fo.setClientSecurityFile(clientSecurityFile);
        fo.setSecretFile(secretFile);

        this.rdpDsService.updateDataSourceAccount(puid, fo);
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), "", SecurityLevel.HIGH, AuditType.UPDATE_DS_ACCOUNT_PASSWD, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updatealiyunrdsaksk", method = RequestMethod.POST)
    public ResWebData<?> updateAliyunRdsAkSk(@RequestBody @Valid UpdateAliyunRdsAkSkFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        this.rdpDsService.updateAliyunRdsAkSk(puid, fo.getDataSourceId(), fo.getAccessKey(), fo.getSecretKey());
        rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), "", SecurityLevel.HIGH, AuditType.UPDATE_DS_ACCOUNT_PASSWD, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/deleteaccount", method = RequestMethod.POST)
    public ResWebData<?> deleteAccount(@RequestBody @Valid DeleteAccountFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.rdpAuthService.checkResAuth(puid, uid, fo.getDataSourceId(), RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        RdpDataSourceDO rdpDataSourceDO = this.rdpDsService.queryById(fo.getDataSourceId());
        this.rdpDsService.cleanDataSourceAccount(puid, fo.getDataSourceId());
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), "", SecurityLevel.HIGH, AuditType.DELETE_DS_ACCOUNT_PASSWD, ResourceType.DATASOURCE, rdpDataSourceDO.getInstanceId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updatepublichost", method = RequestMethod.POST)
    public ResWebData<?> updatePublicHost(@RequestBody @Valid UpdatePubHostFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        UpdatePubHostLO lo = this.rdpDsService.updateDataSourcePublicHost(puid, fo.getDataSourceId(), fo.getPublicHost());
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), lo, SecurityLevel.HIGH, AuditType.UPDATE_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = SecurityLevel.HIGH, value = RDP_DS_MANAGE)
    @RequestMapping(value = "/updateprivatehost", method = RequestMethod.POST)
    public ResWebData<?> updatePrivateHost(@RequestBody @Valid UpdatePriHostFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        long resId = fo.getDataSourceId();

        this.rdpAuthService.checkResAuth(puid, uid, resId, RdpAuthUtils.genEmptyResPath(), RDP_DAUTH_DS_MANAGER, AuthKind.DataSource);

        UpdatePriHostLO lo = this.rdpDsService.updateDataSourcePrivateHost(puid, fo.getDataSourceId(), fo.getPrivateHost());
        this.rdpOpAuditService.logAndAddOperationAudit(puid, uid, request.getRequestURI(), request.getRemoteAddr(), fo
            .getDataSourceId(), lo, SecurityLevel.HIGH, AuditType.UPDATE_DATA_SOURCE_CONFIG, ResourceType.DATASOURCE);
        return ResWebDataUtils.buildSuccess();
    }
}
