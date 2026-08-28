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

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.JDBCType;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.component.resultfile.ResultFileRequests;
import com.clougence.clouddm.component.resultfile.ResultFileWriter;
import com.clougence.clouddm.console.web.global.events.DmGlobalEventBus;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.fo.ExportOpAuditFO;
import com.clougence.clouddm.console.web.model.vo.*;
import com.clougence.clouddm.console.web.model.vo.export.OpAuditExportProgressVO;
import com.clougence.clouddm.console.web.model.vo.export.OpAuditExportStage;
import com.clougence.clouddm.console.web.util.RdpHostUtil;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.ResourceType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.monitor.AuditType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonOpAuditDO;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.resultset.file.FileFormatConvert;
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
    private final List<ResourceType> resourceTypes  = new ArrayList<>();
    private final List<AuditType>    auditTypes     = new ArrayList<>();
    private final Set<String>        isExistsLogSet = new HashSet<>();

    @Resource
    private SystemDal                systemDal;
    @Resource
    private MonitorDal               monitorDal;
    @Resource
    private DataSourceDal            datasourceDal;
    @Resource
    private AuthDal                  authDal;

    @PostConstruct
    private void init() {
        resourceTypes.addAll(Arrays.asList(ResourceType.DATASOURCE, //
                ResourceType.DATA_EXPORT, //
                ResourceType.ACCOUNT, //
                ResourceType.ROLE, //
                ResourceType.DS_ENV));

        auditTypes.addAll(Arrays.asList(AuditType.ADD_DATA_SOURCE, //
                AuditType.DELETE_DATA_SOURCE, //
                AuditType.QUERY_DATA_SOURCE_CONFIG, //
                AuditType.UPDATE_DATA_SOURCE_CONFIG, //
                AuditType.UPDATE_DATA_SOURCE_DESC, //
                AuditType.UPDATE_DS_ACCOUNT_PASSWD, //
                AuditType.DELETE_DS_ACCOUNT_PASSWD, //
                AuditType.EXPORT_QUERY_RESULT, //
                AuditType.DOWNLOAD_QUERY_RESULT, //
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
                AuditType.REVOKE_ACCESS_TO_ALIYUN));

        isExistsLogSet.add(AuditType.QUERY_DATA_SOURCE_CONFIG.name());
        isExistsLogSet.add(AuditType.UPDATE_DATA_SOURCE_CONFIG.name());
        isExistsLogSet.add(AuditType.UPDATE_DATA_SOURCE_DESC.name());

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
            opAuditDO.setIp(RdpHostUtil.getHostIp());
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
        return switch (type) {
            case DATASOURCE -> {
                DmDsDO rdpDataSourceDO = datasourceDal.dsMapper().queryDsIdentityById(Long.valueOf(resourceIdStr));
                yield rdpDataSourceDO.getInstanceId();
            }
            case ROLE -> {
                DmAuthRoleDO rdpRoleDO = authDal.roleMapper().selectById(Long.valueOf(resourceIdStr));
                yield rdpRoleDO.getRoleName();
            }
            case ACCOUNT -> {
                DmAuthUserDO rdpUserDO = authDal.userMapper().queryByUid(resourceIdStr);
                yield rdpUserDO.getUsername();
            }
            case DS_ENV -> {
                DmSysEnvDO rdpDsEnvDO = systemDal.envMapper().selectById(Long.valueOf(resourceIdStr));
                yield rdpDsEnvDO.getEnvName();
            }
            default -> throw new UnsupportedOperationException("Unsupported resource type: " + type);
        };
    }

    @Override
    public OpAuditConditionVO queryListCondition() {
        OpAuditConditionVO opAuditVO = new OpAuditConditionVO();
        opAuditVO.setAuditTypeVOS(fillAuditTypes());
        opAuditVO.setResourceTypeVOS(fillResourceType());
        return opAuditVO;
    }

    private List<ResourceTypeVO> fillResourceType() {
        if (resourceTypes.isEmpty()) {
            return new ArrayList<>();
        }

        return resourceTypes.stream().map(this::convertResourceType).collect(Collectors.toList());
    }

    private ResourceTypeVO convertResourceType(ResourceType resType) {
        ResourceTypeVO resourceTypeVO = new ResourceTypeVO();
        resourceTypeVO.setResourceType(resType.name());
        resourceTypeVO.setAlias(DmI18nUtils.getMessage(resType.name()));
        return resourceTypeVO;
    }

    private List<AuditTypeVO> fillAuditTypes() {
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

    @Override
    public DmPageVO<RdpOpAuditVO> pageUserAllAudit(String puid, String uid, SecurityLevel securityLevel, String userNameLike, String auditType, String resourceType, Date start,
                                                   Date end, int pageNumber, int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        int offset = (pageNumber - 1) * pageSize;
        List<DmMonOpAuditDO> auditDOs = monitorDal.opAuditMapper().pageByCondition(puid, uid, securityLevel, auditType, resourceType, userNameLike, start, end, offset, pageSize);
        List<RdpOpAuditVO> auditVOs = new ArrayList<>();
        if (auditDOs != null && !auditDOs.isEmpty()) {
            auditVOs = auditDOs.stream().map(auditDO -> {
                RdpOpAuditVO auditVO = new RdpOpAuditVO().convertFromDO(auditDO);
                auditVO.setIsExistsLog(StringUtils.isNotBlank(auditVO.getAuditType()) && isExistsLogSet.contains(auditVO.getAuditType()));
                return auditVO;
            }).collect(Collectors.toList());
            fillExtraVO(auditVOs);
        }

        long total = monitorDal.opAuditMapper().countByCondition(puid, uid, securityLevel, auditType, resourceType, userNameLike, start, end);
        return new DmPageVO<>(pageNumber, pageSize, total, auditVOs);
    }

    private String genUUIDKey(Date currentTime) {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(currentTime);
        return date + UUID.randomUUID().toString().substring(0, 8);
    }

    private void fillExtraVO(List<RdpOpAuditVO> auditVOs) {
        fillResourceInfo(auditVOs);

        //        fillUserInfo(auditVOs, userNameMap);
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
        auditVOs.forEach(auditVO -> {
            if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DATASOURCE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                auditVO.setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()), auditVO.getResourceName(), resourceFlagDesc(ResourceType.DATASOURCE)));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ACCOUNT.name())) {
                auditVO.setResourceVO(new ResourceVO(null, auditVO.getResourceName(), resourceFlagDesc(ResourceType.ACCOUNT)));

            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.ROLE.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                auditVO.setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()), auditVO.getResourceName(), resourceFlagDesc(ResourceType.ROLE)));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DS_ENV.name()) && NumberUtils.isNumber(auditVO.getResourceValue())) {
                auditVO.setResourceVO(new ResourceVO(Long.parseLong(auditVO.getResourceValue()), auditVO.getResourceName(), resourceFlagDesc(ResourceType.DS_ENV)));
            } else if (StringUtils.equals(auditVO.getResourceType(), ResourceType.DATA_EXPORT.name())) {
                auditVO.setResourceVO(new ResourceVO(null, auditVO.getResourceName(), resourceFlagDesc(ResourceType.DATA_EXPORT)));
            }
        });
    }

    private static String resourceFlagDesc(ResourceType resourceType) {
        return switch (resourceType) {
            case DATASOURCE -> "InstantId";
            case ACCOUNT -> "Username";
            case ROLE -> "RoleName";
            case DS_ENV -> "DsEnvName";
            case DATA_EXPORT -> "FileName";
            default -> null;
        };
    }

    @Override
    public void exportAuditLog(ExportOpAuditFO fo, HttpServletResponse response) {
        String formatName = fo.getFormatName();
        if (StringUtils.isBlank(formatName)) {
            throw new IllegalArgumentException("Export formatName must not be empty.");
        }

        FileFormatConvert convert = PluginManager.findSpi(FileFormatConvert.class, formatName);
        if (convert == null) {
            throw new IllegalArgumentException("Unsupported export formatName : " + formatName);
        }

        String exportId = StringUtils.isBlank(fo.getExportId()) ? UUID.randomUUID().toString() : fo.getExportId();
        String requesterUid = StringUtils.isBlank(fo.getRequesterUid()) ? fo.getPuid() : fo.getRequesterUid();
        String baseName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_operation_audit";
        File exportDir = new File(GlobalConfUtils.getAppDataHome(), "export");
        File resultFile = new File(exportDir, exportId + ".resultset");
        File exportFile = new File(exportDir, exportId + "." + convert.extension());

        try {
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                throw new IOException("Create export directory failed: " + exportDir.getAbsolutePath());
            }

            long rows = this.prepareAuditResultFile(fo, requesterUid, exportId, resultFile);
            this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.CONVERTING, rows, 0, rows, 0, "Start converting operation audit export file.", null);

            String option = this.defaultConvertOption(formatName);
            convert.convert(exportId, DmFileType.ResultSet, resultFile, exportFile, log, (message, from, to, current) -> {
                int percent = to <= 0 ? 0 : (int) Math.min(100, Math.max(0, current * 100 / to));
                this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.CONVERTING, rows, current, to, percent, message, null);
            }, option);

            this.writeExportResponse(response, exportFile, baseName + "." + convert.extension());
            this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.DONE, rows, rows, rows, 100, "Operation audit export finished.", null);
        } catch (Exception e) {
            this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.FAILED, 0, 0, 0, 0, "Operation audit export failed.", ExceptionUtils.getRootCauseMessage(e));
            throw new RuntimeException("Export operation audit log failed: " + ExceptionUtils.getRootCauseMessage(e), e);
        } finally {
            this.deleteQuietly(resultFile);
            this.deleteQuietly(exportFile);
        }
    }

    private long prepareAuditResultFile(ExportOpAuditFO fo, String requesterUid, String exportId, File resultFile) throws IOException {
        ResultFileRequests.ResultFileRequest resultRequest = ResultFileRequests.fromColumns(exportId, "operation audit export", this.exportColumns());
        long preparedRows = 0;
        int offset = 0;
        int batchSize = 1000;
        long maxRows = fo.getMaxRows() == null || fo.getMaxRows() <= 0 ? Long.MAX_VALUE : fo.getMaxRows();
        long lastReportTime = 0;

        try (ResultFileWriter writer = ResultFileWriter.open(resultFile, resultRequest.getQuery(), resultRequest.getColumns())) {
            while (preparedRows < maxRows) {
                int currentBatchSize = (int) Math.min(batchSize, maxRows - preparedRows);
                List<DmMonOpAuditDO> auditDOs = monitorDal.opAuditMapper()
                    .pageByCondition(fo.getPuid(), fo.getUid(), fo.getSecurityLevel(), fo.getAuditType(), fo.getResourceType(), fo.getUserNameLike(), fo.getOpStart(), fo
                        .getOpEnd(), offset, currentBatchSize);

                if (auditDOs == null || auditDOs.isEmpty()) {
                    break;
                }

                List<RdpOpAuditVO> auditVOs = auditDOs.stream().map(auditDO -> new RdpOpAuditVO().convertFromDO(auditDO)).collect(Collectors.toList());
                fillExtraVO(auditVOs);
                for (RdpOpAuditVO auditVO : auditVOs) {
                    writer.writeRow(this.exportRow(auditVO));
                    preparedRows++;
                }

                offset += auditDOs.size();
                long now = System.currentTimeMillis();
                if (now - lastReportTime > 1000) {
                    lastReportTime = now;
                    this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.PREPARING, preparedRows, 0, 0, 0, "Prepared " + preparedRows
                                                                                                                                + " operation audit rows.", null);
                }

                if (auditDOs.size() < currentBatchSize) {
                    break;
                }
            }
        }

        this.sendOpAuditExportProgress(requesterUid, exportId, OpAuditExportStage.PREPARING, preparedRows, 0, 0, 0, "Prepared " + preparedRows + " operation audit rows.", null);
        return preparedRows;
    }

    private LinkedHashMap<String, JDBCType> exportColumns() {
        LinkedHashMap<String, JDBCType> columns = new LinkedHashMap<>();
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_USER_NAME"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_UID"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_OPERATE_DATE"), JDBCType.TIMESTAMP);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_RESOURCE_TYPE"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_AUDIT_TYPE"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_RESOURCE_VALUE"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_SOURCE_IP"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_LOG_PATH_WORKER_IP"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_SECURITY_LEVEL"), JDBCType.VARCHAR);
        columns.put(DmI18nUtils.getMessage("EXPORT_OPAUDIT_UUID_KEY"), JDBCType.VARCHAR);
        return columns;
    }

    private List<String> exportRow(RdpOpAuditVO auditVO) {
        List<String> row = new ArrayList<>();
        row.add(auditVO.getUserName());
        row.add(auditVO.getUid());
        row.add(auditVO.getOperateDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auditVO.getOperateDate()));
        row.add(StringUtils.isBlank(auditVO.getResourceTypeDesc()) ? auditVO.getResourceType() : auditVO.getResourceTypeDesc());
        row.add(StringUtils.isBlank(auditVO.getAuditTypeDesc()) ? auditVO.getAuditType() : auditVO.getAuditTypeDesc());
        row.add(this.exportResource(auditVO));
        row.add(auditVO.getSourceIp());
        row.add(auditVO.getLogPathWorkerIp());
        row.add(auditVO.getSecurityLevel() == null ? null : auditVO.getSecurityLevel().name());
        row.add(auditVO.getUuidKey());
        return row;
    }

    private String exportResource(RdpOpAuditVO auditVO) {
        if (auditVO.getResourceVO() != null && StringUtils.isNotBlank(auditVO.getResourceVO().getResourceFlag())) {
            return auditVO.getResourceVO().getResourceFlag();
        } else if (StringUtils.isNotBlank(auditVO.getResourceName())) {
            return auditVO.getResourceName();
        } else if (StringUtils.isNotBlank(auditVO.getOperationUri())) {
            return auditVO.getOperationUri();
        } else {
            return auditVO.getResourceValue();
        }
    }

    private String defaultConvertOption(String formatName) {
        if (StringUtils.equals(formatName, "application/sql")) {
            return "{\"limit\":-1,\"tableName\":\"operation_audit\",\"dataSourceType\":\"MySQL\"}";
        } else {
            return "{\"limit\":-1}";
        }
    }

    private void writeExportResponse(HttpServletResponse response, File exportFile, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setContentLengthLong(exportFile.length());
        Files.copy(exportFile.toPath(), response.getOutputStream());
        response.flushBuffer();
    }

    private void sendOpAuditExportProgress(String uid, String exportId, OpAuditExportStage stage, long preparedRows, long current, long total, int percent, String message,
                                           String errorMessage) {
        OpAuditExportProgressVO vo = new OpAuditExportProgressVO();
        vo.setUid(uid);
        vo.setExportId(exportId);
        vo.setStage(stage);
        vo.setPreparedRows(preparedRows);
        vo.setCurrent(current);
        vo.setTotal(total);
        vo.setPercent(percent);
        vo.setMessage(message);
        vo.setErrorMessage(errorMessage);
        vo.setSuccess(stage == OpAuditExportStage.DONE);
        DmGlobalEventBus.triggerOpAuditExportEvent(vo);
    }

    private void deleteQuietly(File file) {
        if (file != null) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (Exception e) {
                log.warn("Delete temporary operation audit export file failed: " + file.getAbsolutePath(), e);
            }
        }
    }
}
