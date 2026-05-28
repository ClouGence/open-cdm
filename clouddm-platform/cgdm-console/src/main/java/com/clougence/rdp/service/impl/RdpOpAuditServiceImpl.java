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
package com.clougence.rdp.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceFlagEnum;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.fo.ExportOpAuditFO;
import com.clougence.clouddm.console.web.model.vo.*;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonOpAuditDO;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.rdp.service.RdpOpAuditService;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020/4/13 13:39
 */
@Service
@Slf4j
public class RdpOpAuditServiceImpl implements RdpOpAuditService {

    //    @Value("${clougence.rdp.operation.audit.log.path}")
    //    private String                                opAuditLogPath;

    //    private static final Logger                   auditLogger    = LoggerFactory.getLogger("user_audit_detail");

    private final Map<String, List<ResourceType>> resTypeMap     = new HashMap<>();

    private final Map<String, List<AuditType>>    auditTypeMap   = new HashMap<>();

    private final Set<String>                     isExistsLogSet = new HashSet<>();

    @Resource
    private SystemDal                             systemDal;
    @Resource
    private MonitorDal                            monitorDal;
    @Resource
    private DataSourceDal                         datasourceDal;
    @Resource
    private AuthDal                               authDal;
    @Resource
    private DmConsoleConfig                       rdpConfig;

    @PostConstruct
    private void init() {
        resTypeMap.put(QUERY_CONDITION_CC, Arrays.asList(ResourceType.DATA_JOB, ResourceType.ACCOUNT));
        resTypeMap.put(QUERY_CONDITION_RDP, Arrays.asList(ResourceType.DATASOURCE, ResourceType.ACCOUNT, ResourceType.ROLE, ResourceType.DS_ENV));

        List<AuditType> auditTypesForCc = Arrays.asList(AuditType.ADD_CHECK, //
                AuditType.QUERY_JOB_INFO, //
                AuditType.UPDATE_PARAMS, //
                AuditType.UPDATE_SUBSCRIBE, //
                AuditType.UPDATE_SUBSCRIBE_FULL, //
                AuditType.CREATE_JOB, //
                AuditType.START_JOB, //
                AuditType.STOP_JOB, //
                AuditType.RESTART_JOB, //
                AuditType.DELETE_JOB, //
                AuditType.MANUAL_MERGE, //
                AuditType.REPLAY_JOB, //
                AuditType.UPDATE_POSITION, //
                AuditType.RESET_POSITION, //
                AuditType.ATTACH_INCRE_TASK, //
                AuditType.DETACH_INCRE_TASK, //
                AuditType.ADD_REVISE, //
                AuditType.PAUSE_SCHEDULE, //
                AuditType.RESUME_SCHEDULE, //
                AuditType.START_SCHEDULE, //
                AuditType.ACTIVE_FSM, //
                AuditType.MODIFY_SUB_ACCOUNT_AUTH);
        auditTypeMap.put(QUERY_CONDITION_CC, auditTypesForCc);

        List<AuditType> auditTypesForRdp = Arrays.asList(AuditType.ADD_DATA_SOURCE, //
                AuditType.DELETE_DATA_SOURCE, //
                AuditType.QUERY_DATA_SOURCE_CONFIG, //
                AuditType.UPDATE_DATA_SOURCE_CONFIG, //
                AuditType.UPDATE_DATA_SOURCE_DESC, //
                AuditType.UPDATE_DS_ACCOUNT_PASSWD, //
                AuditType.DELETE_DS_ACCOUNT_PASSWD, //
                AuditType.ADD_SUB_ACCOUNT, //
                AuditType.UPDATE_SUB_ACCOUNT, //
                AuditType.MODIFY_SUB_ACCOUNT_AUTH, //
                AuditType.ENABLE_SUB_ACCOUNT, //
                AuditType.DISABLE_SUB_ACCOUNT, //
                AuditType.UPDATE_SUB_ACCOUNT_PWD, //
                AuditType.UPDATE_SUB_ACCOUNT_ROLE, //
                AuditType.DELETE_SUB_ACCOUNT, //
                AuditType.CREATE_ROLE, //
                AuditType.UPDATE_ROLE, //
                AuditType.DELETE_ROLE, //
                AuditType.ADD_DS_ENV, //
                AuditType.UPDATE_DS_ENV, //
                AuditType.DELETE_DS_ENV, //
                AuditType.LOGIN_SUCCESS, //
                AuditType.LOGIN_FAIL, //
                AuditType.LOGOUT, //
                AuditType.QUERY_ACCOUNT_AK_SK, //
                AuditType.RESET_ACCOUNT_AK_SK, //
                AuditType.UPDATE_ACCOUNT_EMAIL, //
                AuditType.UPDATE_ACCOUNT_PHONE, //
                AuditType.UPDATE_ACCOUNT_PWD, //
                AuditType.UPDATE_ACCOUNT_OP_PWD, //
                AuditType.UPDATE_SYSTEM_CONFIG, //
                AuditType.AUTHORIZE_ACCESS_TO_ALIYUN, //
                AuditType.REVOKE_ACCESS_TO_ALIYUN);

        auditTypeMap.put(QUERY_CONDITION_RDP, auditTypesForRdp);

        isExistsLogSet.add(AuditType.QUERY_DATA_SOURCE_CONFIG.name());
        isExistsLogSet.add(AuditType.UPDATE_DATA_SOURCE_CONFIG.name());
        isExistsLogSet.add(AuditType.UPDATE_DATA_SOURCE_DESC.name());

        isExistsLogSet.add(AuditType.UPDATE_PARAMS.name());
        isExistsLogSet.add(AuditType.UPDATE_SUBSCRIBE.name());
        isExistsLogSet.add(AuditType.UPDATE_SUBSCRIBE_FULL.name());
        isExistsLogSet.add(AuditType.CREATE_JOB.name());
        isExistsLogSet.add(AuditType.UPDATE_POSITION.name());
        isExistsLogSet.add(AuditType.RESET_POSITION.name());
        isExistsLogSet.add(AuditType.ADD_CHECK.name());

        isExistsLogSet.add(AuditType.UPDATE_SYSTEM_CONFIG.name());
        isExistsLogSet.add(AuditType.UPDATE_ACCOUNT_PHONE.name());
        isExistsLogSet.add(AuditType.UPDATE_ACCOUNT_EMAIL.name());
        isExistsLogSet.add(AuditType.UPDATE_SUB_ACCOUNT_ROLE.name());
        isExistsLogSet.add(AuditType.UPDATE_SUB_ACCOUNT.name());
        isExistsLogSet.add(AuditType.MODIFY_SUB_ACCOUNT_AUTH.name());
        isExistsLogSet.add(AuditType.UPDATE_DS_ENV.name());
        isExistsLogSet.add(AuditType.LOGIN_FAIL.name());
    }

    @Override
    public void addOperationAudit(DmMonOpAuditDO auditDO) {
        if (StringUtils.isNotBlank(auditDO.getUid()) && StringUtils.isNotBlank(auditDO.getResourceValue())) {
            auditDO.setOperateDate(new Date());
            try {
                monitorDal.opAuditMapper().insert(auditDO);
            } catch (Exception e) {
                throw new RuntimeException("operation audit data failed.");
            }
        } else {
            throw new RuntimeException("operation audit data invalid, uid or resource_value is empty.");
        }
    }

    @Override
    public List<RdpOpAuditVO> findAuditByUserName(String puid, String userName, SecurityLevel securityLevel, String type, String resType, Date start, Date end, long startId,
                                                  int pageSize) {
        if (StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException("find audit by userName,but userName is empty.");
        }

        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        List<DmMonOpAuditDO> auditDOs = monitorDal.opAuditMapper().queryByUidsJoinUrlAuth(puid, securityLevel, type, resType, start, end, startId, pageSize);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<RdpOpAuditVO> auditVOs = auditDOs.stream().map(auditDO -> {
            //            RdpOpAuditVO rdpOpAuditVO = fillAuditVO(new RdpOpAuditVO().convertFromDO(auditDO));
            RdpOpAuditVO rdpOpAuditVO = new RdpOpAuditVO().convertFromDO(auditDO);
            rdpOpAuditVO.setIsExistsLog(StringUtils.isNotBlank(rdpOpAuditVO.getAuditType()) && isExistsLogSet.contains(rdpOpAuditVO.getAuditType()));
            return rdpOpAuditVO;
        }).collect(Collectors.toList());

        //        Map<String, String> userNameMap = users.stream().collect(Collectors.toMap(DmAuthUserDO::getUid, DmAuthUserDO::getUsername));

        fillExtraVO(auditVOs);

        return auditVOs;
    }

    @Override
    public void logAndAddOperationAudit(String puid, String uid, String requestUri, String remoteAddr, Object resId, Object obj, SecurityLevel securityLevel, AuditType type,
                                        ResourceType resType, String oldName) {
        logOperation(puid, uid, requestUri, remoteAddr, resId, obj, securityLevel, type, resType, oldName);
    }

    @Override
    public void logAndAddOperationAudit(String puid, String uid, String requestUri, String remoteAddr, Object resId, Object obj, SecurityLevel securityLevel, AuditType type,
                                        ResourceType resType) {
        String resourceName = getResourceName(resId, resType);
        logOperation(puid, uid, requestUri, remoteAddr, resId, obj, securityLevel, type, resType, resourceName);
    }

    private void logOperation(String puid, String uid, String requestUri, String remoteAddr, Object resId, Object obj, SecurityLevel securityLevel, AuditType type,
                              ResourceType resType, String resourceName) {
        try {
            DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(uid);
            DmMonOpAuditDO opAuditDO = new DmMonOpAuditDO();
            Date currentTime = new Date();
            opAuditDO.setResourceName(resourceName);
            String UUIDKey = genUUIDKey(currentTime);
            opAuditDO.setUuidKey(AuditType.genUK(type, UUIDKey, resId));
            opAuditDO.setAuditType(type);
            opAuditDO.setResourceValue(String.valueOf(resId));
            opAuditDO.setOperationUri(requestUri);
            opAuditDO.setUid(uid);
            opAuditDO.setOwnerUid(puid);
            opAuditDO.setUserName(rdpUserDO.getUsername());
            opAuditDO.setSourceIp(remoteAddr);
            opAuditDO.setResourceType(resType);
            opAuditDO.setSecurityLevel(securityLevel);
            opAuditDO.setIp(rdpConfig.getConsolePackageMode().getLocalIpOrHostName());
            opAuditDO.setOperateDate(currentTime);
            // opAuditDO.setLogPath(this.opAuditLogPath);
            // record update detail info to log
            if (obj != null) {
                String json = null;
                try {
                    json = JsonUtils.toJson(obj);

                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
                    log.info("[DataTime: {} uid:\"{}\" key:\"{}\"] {}", date, uid, UUIDKey, json);

                    //check if json length exceed 65535
                    if (json.length() > 65535) {
                        json = json.substring(0, 65530);
                        json += "...";
                    }
                    opAuditDO.setLogInfo(json);
                } catch (Exception e) {
                    String msg = "operation audit content json serialize failed,msg : " + ExceptionUtils.getRootCauseMessage(e);
                    log.error(msg);
                }
            }
            addOperationAudit(opAuditDO);
        } catch (Exception e) {
            String msg = "log operation audit error, res id : " + resId + ", audit type: " + type + ", obj: " + obj;
            log.error(msg, e);
        }
    }

    private String getResourceName(Object resourceId, ResourceType type) {
        String resourceIdStr = StringUtils.toString(resourceId);
        if (StringUtils.isEmpty(resourceIdStr)) {
            return "(null)";
        }
        switch (type) {
            case DATASOURCE: {
                DmDsDO rdpDataSourceDO = datasourceDal.dsMapper().queryDsIdentityById(Long.valueOf(resourceIdStr));
                return rdpDataSourceDO.getInstanceId();
            }
            case ROLE: {
                DmAuthRoleDO rdpRoleDO = authDal.roleMapper().selectById(Long.valueOf(resourceIdStr));
                return rdpRoleDO.getRoleName();
            }
            case ACCOUNT: {
                DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(resourceIdStr);
                return rdpUserDO.getUsername();
            }
            case DS_ENV: {
                DmSysEnvDO rdpDsEnvDO = systemDal.envMapper().selectById(Long.valueOf(resourceIdStr));
                return rdpDsEnvDO.getEnvName();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported resource type: " + type);
            }
        }
    }

    @Override
    public OpAuditConditionVO queryListCondition(String conditionType) {
        OpAuditConditionVO opAuditVO = new OpAuditConditionVO();
        opAuditVO.setAuditTypeVOS(fillAuditTypes(conditionType));
        opAuditVO.setResourceTypeVOS(fillResourceType(conditionType));
        return opAuditVO;
    }

    @Override
    public Boolean isExistsOpAuditLog(String auditType) {
        return StringUtils.isNotBlank(auditType) && this.isExistsLogSet.contains(auditType);
    }

    private List<ResourceTypeVO> fillResourceType(String conditionType) {
        List<ResourceType> resTypes = resTypeMap.get(conditionType);

        if (resTypes == null || resTypes.isEmpty()) {
            return new ArrayList<>();
        }

        return resTypes.stream().map(this::convertResourceType).collect(Collectors.toList());
    }

    private ResourceTypeVO convertResourceType(ResourceType resType) {
        ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
        resourceTypeVO.setResourceType(resType.name());
        resourceTypeVO.setAlias(DmI18nUtils.getMessage(resType.name()));
        return resourceTypeVO;
    }

    private List<AuditTypeVO> fillAuditTypes(String conditionType) {
        List<AuditType> auditTypes = auditTypeMap.get(conditionType);

        if (auditTypes == null || auditTypes.isEmpty()) {
            return new ArrayList<>();
        }

        return auditTypes.stream().map(this::convertAuditType).collect(Collectors.toList());
    }

    private AuditTypeVO convertAuditType(AuditType auditType) {
        AuditTypeVO auditTypeVO = new AuditTypeVO();
        auditTypeVO.setAuditType(auditType.name());
        auditTypeVO.setAlias(DmI18nUtils.getMessage(auditType.name()));
        return auditTypeVO;
    }

    @Override
    public List<RdpOpAuditVO> findAuditByUid(String uid, SecurityLevel securityLevel, String auditType, String resourceType, Date start, Date end, long startId, int pageSize) {
        if (StringUtils.isBlank(uid)) {
            throw new IllegalArgumentException("find audit by uid,but uid is empty.");
        }

        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        List<DmMonOpAuditDO> auditDOs = monitorDal.opAuditMapper().queryByUidJoinUrlAuth(uid, securityLevel, auditType, resourceType, start, end, startId, pageSize);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<RdpOpAuditVO> auditVOs = auditDOs.stream().map(auditDO -> {
            RdpOpAuditVO rdpOpAuditVO = new RdpOpAuditVO().convertFromDO(auditDO);
            rdpOpAuditVO.setIsExistsLog(StringUtils.isNotBlank(rdpOpAuditVO.getAuditType()) && isExistsLogSet.contains(rdpOpAuditVO.getAuditType()));
            return rdpOpAuditVO;
        }).collect(Collectors.toList());

        fillExtraVO(auditVOs);

        return auditVOs;
    }

    @Override
    public List<RdpOpAuditVO> queryUserAllAudit(String puid, String uid, SecurityLevel securityLevel, String userNameLike, String auditType, String resourceType, Date start,
                                                Date end, long startId, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        List<DmMonOpAuditDO> auditDOs = monitorDal.opAuditMapper().queryByCondition(puid, uid, securityLevel, auditType, resourceType, userNameLike, start, end, startId, pageSize);

        if (auditDOs == null || auditDOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<RdpOpAuditVO> auditVOs = auditDOs.stream().map(auditDO -> {
            RdpOpAuditVO rdpOpAuditVO = new RdpOpAuditVO().convertFromDO(auditDO);
            rdpOpAuditVO.setIsExistsLog(StringUtils.isNotBlank(rdpOpAuditVO.getAuditType()) && isExistsLogSet.contains(rdpOpAuditVO.getAuditType()));
            return rdpOpAuditVO;
        }).collect(Collectors.toList());

        fillExtraVO(auditVOs);

        return auditVOs;
    }

    //    public RdpOpAuditVO fillAuditVO(RdpOpAuditVO vo) {
    //        try {
    //            DmAuthUserDO userDO = this.tempCache.get(vo.getUid());
    //            if (userDO != null) {
    //                vo.setUserName(userDO.getUsername());
    //            }
    //        } catch (ExecutionException e) {
    //            throw new RuntimeException(e.getCause());
    //        }
    //
    //        return vo;
    //    }

    private String genUUIDKey(Date currentTime) {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(currentTime);
        return date + UUID.randomUUID().toString().substring(0, 8);
    }

    private void fillExtraVO(List<RdpOpAuditVO> auditVOs) {
        fillResourceInfo(auditVOs);

        //        fillUserInfo(auditVOs, userNameMap);
    }

    private void fillUserInfo(List<RdpOpAuditVO> auditVOs, Map<String, String> userNameMap) {
        if (userNameMap.isEmpty()) {
            return;
        }

        auditVOs.forEach(auditVO -> {
            if (userNameMap.containsKey(auditVO.getUid())) {
                auditVO.setUserName(userNameMap.get(auditVO.getUid()));
            }
        });
    }

    private void fillResourceInfo(List<RdpOpAuditVO> auditVOs) {
        Set<Long> dataSourceIds = new HashSet<>();
        Set<String> accountUids = new HashSet<>();
        Set<Long> roleIds = new HashSet<>();
        Set<Long> dsEnvIds = new HashSet<>();

        for (RdpOpAuditVO auditVO : auditVOs) {
            if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DATASOURCE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                dataSourceIds.add(Long.parseLong(auditVO.getResourceValue()));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ACCOUNT.name()) && StringUtils.isNotBlank(auditVO.getResourceValue())) {
                accountUids.add(auditVO.getResourceValue());
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ROLE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                roleIds.add(Long.valueOf(auditVO.getResourceValue()));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DS_ENV.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                dsEnvIds.add(Long.valueOf(auditVO.getResourceValue()));
            }
        }
        //
        //        List<DmDsDO> dsList = new ArrayList<>();
        //        List<DmAuthRoleDO> roleList = new ArrayList<>();
        //        List<DmSysEnvDO> dsEnvList = new ArrayList<>();
        //        List<DmAuthUserDO> accountList = new ArrayList<>();
        //        if (!dataSourceIds.isEmpty()) {
        //            dsList = datasourceDal.dsMapper().listByIdsIncludeDeleted(dataSourceIds);
        //        }
        //        if (!roleIds.isEmpty()) {
        //            roleList = authDal.roleMapper().listByIds(new ArrayList<>(roleIds));
        //        }
        //        if (!dsEnvIds.isEmpty()) {
        //            dsEnvList = systemDal.envMapper().listByIds(new ArrayList<>(dsEnvIds));
        //        }
        //        if (!accountUids.isEmpty()) {
        //            accountList = authDal.userMapper().listByUids(new ArrayList<>(accountUids));
        //        }
        //
        //        Map<Long, DmDsDO> dsMap;
        //        Map<Long, DmAuthRoleDO> roleMap;
        //        Map<Long, DmSysEnvDO> dsEnvMap;
        //        Map<String, DmAuthUserDO> accountMap;
        //        if (CollectionUtils.isNotEmpty(dsList)) {
        //            dsMap = dsList.stream().collect(Collectors.toMap(DmDsDO::getId, dsDO -> dsDO));
        //        } else {
        //            dsMap = new HashMap<>();
        //        }
        //        if (CollectionUtils.isNotEmpty(roleList)) {
        //            roleMap = roleList.stream().collect(Collectors.toMap(DmAuthRoleDO::getId, roleDO -> roleDO));
        //        } else {
        //            roleMap = new HashMap<>();
        //        }
        //        if (CollectionUtils.isNotEmpty(dsEnvList)) {
        //            dsEnvMap = dsEnvList.stream().collect(Collectors.toMap(DmSysEnvDO::getId, dsEnvDO -> dsEnvDO));
        //        } else {
        //            dsEnvMap = new HashMap<>();
        //        }
        //        if (CollectionUtils.isNotEmpty(accountList)) {
        //            accountMap = accountList.stream().collect(Collectors.toMap(DmAuthUserDO::getUid, accountDO -> accountDO));
        //        } else {
        //            accountMap = new HashMap<>();
        //        }

        auditVOs.forEach(auditVO -> {
            if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DATASOURCE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                //                DmDsDO dataSourceDO = dsMap.get(Long.parseLong(auditVO.getResourceValue()));
                //                if (dataSourceDO != null) {
                //                    auditVO.setResourceVO(new ResourceVO(dataSourceDO.getId(), dataSourceDO.getInstanceId(), ResourceFlagEnum.getFlagDesc(ResourceType.DATASOURCE.name())));
                //                }
                auditVO.setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()),
                    auditVO.getResourceName(),
                    ResourceFlagEnum.getFlagDesc(ResourceType.DATASOURCE.name())));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ACCOUNT.name())) {
                //                DmAuthUserDO accountDO = accountMap.get(auditVO.getResourceValue());
                //                if (accountDO != null) {
                //                    auditVO.setResourceVO(new ResourceVO(accountDO.getId(), accountDO.getUsername(), ResourceFlagEnum.getFlagDesc(ResourceType.ACCOUNT.name())));
                //                }
                auditVO.setResourceVO(new ResourceVO(null, auditVO.getResourceName(), ResourceFlagEnum.getFlagDesc(ResourceType.ACCOUNT.name())));

            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ROLE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                //                DmAuthRoleDO roleDO = roleMap.get(Long.parseLong(auditVO.getResourceValue()));
                //                if (roleDO != null) {
                //                    auditVO.setResourceVO(new ResourceVO(roleDO.getId(), roleDO.getRoleName(), ResourceFlagEnum.getFlagDesc(ResourceType.ROLE.name())));
                //                }
                auditVO
                    .setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()), auditVO.getResourceName(), ResourceFlagEnum.getFlagDesc(ResourceType.ROLE.name())));
                //                }
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DS_ENV.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                //                DmSysEnvDO dsEnvDO = dsEnvMap.get(Long.parseLong(auditVO.getResourceValue()));
                //                if (dsEnvDO != null) {
                //                    auditVO.setResourceVO(new ResourceVO(dsEnvDO.getId(), dsEnvDO.getEnvName(), ResourceFlagEnum.getFlagDesc(ResourceType.DS_ENV.name())));
                //                }
                auditVO
                    .setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()), auditVO.getResourceName(), ResourceFlagEnum.getFlagDesc(ResourceType.DS_ENV.name())));

            }
        });
    }

    @Override
    public void exportAuditLog(ExportOpAuditFO exportOpAuditFO, HttpServletResponse response) {
        //        ExportFileType exportType = exportOpAuditFO.getExportType();
        //        if (exportType == ExportFileType.EXCEL) {
        //            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_operation_audit" + ".xlsx";
        //            ExcelExportUtils.exportExcel(exportOpAuditFO, new RsDmMonOpAuditExportObj(), fileName, "operationAudit", rdpConfig.getMaxExportSize(), response, this::getOpAuditExportDTO);
        //        } else {
        throw new IllegalArgumentException("Unsupported export type : xxxx");
        //        }
    }

    //    private List<RsDmMonOpAuditExportObj> getOpAuditExportDTO(ExportOpAuditFO exportOpAuditFO, int maxBatch, int currentBatch) {
    //        String puid = exportOpAuditFO.getPuid();
    //        String uid = exportOpAuditFO.getUid();
    //        SecurityLevel securityLevel = exportOpAuditFO.getSecurityLevel();
    //        String userNameLike = exportOpAuditFO.getUserNameLike();
    //        String auditType = exportOpAuditFO.getAuditType();
    //        String resourceType = exportOpAuditFO.getResourceType();
    //        Date start = exportOpAuditFO.getOpStart();
    //        Date end = exportOpAuditFO.getOpEnd();
    //
    //        List<DmMonOpAuditDO> auditDOs = rdpOpAuditMapper
    //            .pageByCondition(puid, uid, securityLevel, auditType, resourceType, userNameLike, start, end, maxBatch * currentBatch, maxBatch);
    //
    //        if (auditDOs == null || auditDOs.isEmpty()) {
    //            return new ArrayList<>();
    //        }
    //
    //        Set<String> uids = auditDOs.stream().map(DmMonOpAuditDO::getUid).collect(Collectors.toSet());
    //        List<DmAuthUserDO> users = authDal.userMapper().listByUids(new ArrayList<>(uids));
    //        Map<String, String> userNameMap = users.stream().collect(Collectors.toMap(DmAuthUserDO::getUid, DmAuthUserDO::getUsername));
    //        return auditDOs.stream().map(auditDO -> {
    //            RsDmMonOpAuditExportObj auditDTO = new RsDmMonOpAuditExportObj().convertFromDO(auditDO);
    //            auditDTO.setUserName(userNameMap.get(auditDTO.getUid()));
    //            return auditDTO;
    //        }).collect(Collectors.toList());
    //    }
}
