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
package com.clougence.clouddm.console.web.service.editor;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.component.file.FileService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DataSourceStatus;
import com.clougence.clouddm.console.web.dal.enumeration.FileStatus;
import com.clougence.clouddm.console.web.dal.mapper.DmDsConfigMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmDsSessionMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmFileMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsConfigDO;
import com.clougence.clouddm.console.web.dal.model.DmDsSessionDO;
import com.clougence.clouddm.console.web.dal.model.DmFileDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SessionVO;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.service.editor.model.DsAvailableDTO;
import com.clougence.clouddm.console.web.service.editor.model.FileSaveAsDTO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DsQueryEditorServiceImpl implements DsQueryEditorService {

    @Value("${clouddm.console.max-tx-session-user:5}")
    private int                     maxTxSessionQuota;
    @Resource
    private QueryService            queryService;
    @Resource
    private FileService             fileService;
    @Resource
    private DmFileMapper            dmFileMapper;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private DmDsConfigMapper        dmDsMapper;
    @Resource
    private DmWorkerStatusMapper    dmWorkerStatusMapper;
    @Resource
    private DmResAuthService        dmDsAuthService;
    @Resource
    private DmAuthServiceForBiz     dmAuthServiceForBiz;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DsSchemaService         dsSchemaService;
    @Resource
    private DmDsSessionMapper       sessionMapper;

    @Override
    public boolean hasMoreSessionQuota(String userId) {
        Integer count = this.sessionMapper.getUserSessionCount(userId);
        return count == null || count < this.maxTxSessionQuota;
    }

    @Override
    public int getMaxTxSessionUserQuota() { return this.maxTxSessionQuota; }

    /**
     * for service API '/query/listsession'
     */
    @Override
    public List<SessionVO> getSessionList(String puid, String uid) {
        List<DmDsSessionDO> sessionList = this.sessionMapper.queryByUser(uid);

        if (sessionList == null) {
            return Collections.emptyList();
        }

        List<SessionContextDTO> sessions = sessionList.stream().map(DmDsSessionDO::toRdbCtx).collect(Collectors.toList());
        return sessions.stream().map(contextDTO -> {
            SessionVO sessionVO = new SessionVO();
            sessionVO.setSessionId(contextDTO.getSessionId());
            sessionVO.setRdbCatalog(contextDTO.getRdbCatalog());
            sessionVO.setRdbSchema(contextDTO.getRdbSchema());
            sessionVO.setRdbAutoCommit(contextDTO.isRdbAutoCommit());
            sessionVO.setRdbTxIsolation(contextDTO.getRdbTxIsolation());
            sessionVO.setRdbReadOnly(contextDTO.isRdbReadOnly());
            return sessionVO;
        }).collect(Collectors.toList());
    }

    /**
     * for service API '/query/createsession'
     */
    @Override
    public String createSession(String curUid, List<String> levels, boolean autoCommit, RdbIsolation initIsolation) {
        String usingSessionId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        if (autoCommit) {
            return usingSessionId;
        }

        // root level is env list
        if (CollectionUtils.isEmpty(levels) || levels.size() == 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        // third level begin is dbObjects.
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(levels);
        if (dsLevels == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        RdpDataSourceDO dsDO = dsLevels.dsDO();
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());

        SessionContextDTO sessionCtx = this.createSessionCtx(levels);
        sessionCtx.setSessionId(usingSessionId);
        sessionCtx.setRdbAutoCommit(autoCommit);
        sessionCtx.setRdbTxIsolation(initIsolation);
        sessionCtx.setRdbReadOnly(Boolean.TRUE.equals(dsConfig.getReadOnly()));
        return this.queryService.createSession(curUid, dsLevels, sessionCtx);
    }

    /**
     * for service API '/query/closesession'
     */
    @Override
    public void closeSession(String puid, String uid, List<String> sessionIds) {
        if (CollectionUtils.isNotEmpty(sessionIds)) {
            for (String sessionId : sessionIds) {
                this.queryService.closeSession(uid, sessionId);
            }
        }
    }

    /**
     * for service API '/query/rdbColumns'
     */
    @Override
    public Map<String, List<BrowseColumnMO>> rdbBatchColumns(String puid, String uid, DsLevels levels, UmiTypes leafType, List<String> leafNames) {
        Map<String, List<RdbColumn>> value = this.dsSchemaService.loadColumns(uid, levels.dsDO(), levels.levelsParam(), leafType, leafNames);

        // convert
        Map<String, List<BrowseColumnMO>> result = new HashMap<>();
        value.forEach((table, columns) -> {
            result.put(table, columns.stream().map(DmConvertUtils::convertToBrowseColumnMOTipsType).collect(Collectors.toList()));
        });

        return result;
    }

    @Override
    public DsAvailableDTO availableDataSource(String puid, String uid, long dsId) {
        DmDsConfigDO dsConf = this.dmDsMapper.queryById(puid, dsId);
        if (dsConf == null) {
            DsAvailableDTO dto = new DsAvailableDTO();
            dto.setDsId(dsId);
            dto.setDsStatus(DataSourceStatus.Deleted);
            dto.setDsStatusMessage(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.Deleted, null));
            return dto;
        }

        List<Long> dsIds = this.dmDsAuthService.listResByUser(uid, AuthKind.DataSource);
        if (!dsIds.contains(dsId)) {
            DsAvailableDTO dto = new DsAvailableDTO();
            dto.setDsId(dsId);
            dto.setDsStatus(DataSourceStatus.NoAuthority);
            dto.setDsStatusMessage(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.NoAuthority, dsConf.getDataSourceType()));
            return dto;
        }

        if (!this.dmAuthServiceForBiz.checkResPathChildrenWithoutError(puid, uid, dsId, AuthKind.DataSource, RdpAuthUtils.genEmptyResPath(), SecDataAuthLabel.DM_DAUTH_QUERY)) {
            DsCacheEntry entry = this.ownerCacheService.queryByDsId(dsId);
            String authRes = entry.getDsInstId() + "/";
            String dataAuthMsg = DmI18nUtils.getMessage(SecDataAuthLabel.DM_DAUTH_QUERY);
            String authMessage = DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), authRes, dataAuthMsg);

            DsAvailableDTO dto = new DsAvailableDTO();
            dto.setDsId(dsId);
            dto.setDsStatus(DataSourceStatus.NoAuthority);
            dto.setDsStatusMessage(authMessage);
            return dto;
        }

        DmDsConfigDO dmDsConf = this.dmDsMapper.queryById(puid, dsId);
        if (dmDsConf == null) {
            DsAvailableDTO dto = new DsAvailableDTO();
            dto.setDsId(dsId);
            dto.setDsStatus(DataSourceStatus.QueryNotEnabled);
            dto.setDsStatusMessage(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.QueryNotEnabled, dsConf.getDataSourceType()));
            return dto;
        }

        long bindClusterId = dmDsConf.getBindClusterId();
        List<DmWorkerStatusDO> workers = this.dmWorkerStatusMapper.queryByClusterIdAndStatus(bindClusterId, WorkerConnStatus.CONNECTED);
        if (workers.isEmpty()) {
            DsAvailableDTO dto = new DsAvailableDTO();
            dto.setDsId(dsId);
            dto.setDsStatus(DataSourceStatus.NotWorker);
            dto.setDsStatusMessage(MessageUtils.getClusterHaveNoWorksErrorMessage(bindClusterId));
            return dto;
        }

        DsAvailableDTO dto = new DsAvailableDTO();
        dto.setDsId(dsId);
        dto.setDsStatus(dmDsConf.getStatus());
        dto.setDsStatusMessage(DmConvertUtils.convertToDataSourceStatusI18n(dmDsConf.getStatus(), dsConf.getDataSourceType()) + " "
                               + StringUtils.defaultIfEmpty(dmDsConf.getStatusMessage(), ""));
        return dto;
    }

    @Override
    public DmFileDO queryUserFileByUniqueId(String puid, String uid, String resultId) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(resultId);
        if (fileDO == null) {
            return null;
        }
        if (StringUtils.equals(fileDO.getOwnerUid(), puid) && StringUtils.equals(fileDO.getUserId(), uid)) {
            return fileDO;
        } else {
            return null;
        }
    }

    @Override
    public FileSaveAsDTO resultSetFileSaveAs(String puid, String uid, String resultId, String dstFileName, String dstFormatName, boolean autoName, String option) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(resultId);

        URI fileUri = DmConvertUtils.createFileUri(fileDO.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            String suffix = this.fileService.fetchFileExtensionByFormatName(dstFormatName);
            String srcFileId = fileDO.getUniqueId();
            String exportId = ("e_" + resultId.substring(2) + "_" + StringUtils.leftPad(Long.toString(System.currentTimeMillis(), 16), 11, '0')).toLowerCase();
            String exportFile = "export" + File.separator + exportId + "." + suffix;
            String exportFileUri = this.fileService
                .submitFileConvert(puid, uid, wsn, srcFileId, exportId, DmFileType.ResultSet, fileUri.getPath(), exportFile, dstFormatName, option);

            DmFileDO exportFileDO = new DmFileDO();
            exportFileDO.setFileUri(exportFileUri);
            exportFileDO.setFileFormat(dstFormatName);
            exportFileDO.setInnerFormat(false);
            exportFileDO.setOwnerUid(puid);
            exportFileDO.setUserId(uid);
            exportFileDO.setStatus(FileStatus.Pending);
            exportFileDO.setQueryId(fileDO.getQueryId());
            exportFileDO.setUniqueId(exportId);
            exportFileDO.setHeartbeat(new Date());
            this.dmFileMapper.insert(exportFileDO);

            FileSaveAsDTO saveAs = new FileSaveAsDTO();
            saveAs.setTrackId(exportId);
            saveAs.setNewFile(exportFileUri);
            saveAs.setNewFormat(dstFormatName);
            return saveAs;
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    @Override
    public void removeResultSetCacheFile(String puid, String uid, String fileUniqueId) {
        this.dmFileMapper.updateStatusByUniqueId(fileUniqueId, FileStatus.Delete, "from closeResultWindow.");
    }

    @Override
    public long fetchFileSizeByUri(String puid, String uid, String fileUriStr) {
        URI fileUri = DmConvertUtils.createFileUri(fileUriStr);
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            return this.fileService.fetchFileSize(wsn, fileUri.getPath());
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    @Override
    public long fetchFileSizeByUniqueId(String puid, String uid, String fileUniqueId) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(fileUniqueId);
        if (fileDO == null) {
            return -1;
        }

        URI fileUri = DmConvertUtils.createFileUri(fileDO.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            return this.fileService.fetchFileSize(wsn, fileUri.getPath());
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    @Override
    public byte[] fetchFileData(String puid, String uid, String fileUriStr, long offset, int length) {
        URI fileUri = DmConvertUtils.createFileUri(fileUriStr);
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            return this.fileService.fetchFileData(wsn, fileUri.getPath(), offset, length);
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    @Override
    public DataResultPageVO fetchResultPage(String puid, String uid, String resultId, long offsetRow, int pageSize) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(resultId);

        URI fileUri = DmConvertUtils.createFileUri(fileDO.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            return this.fileService.fetchResultPage(wsn, fileUri.getPath(), offsetRow, pageSize);
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    @Override
    public DataResultDataVO fetchResultData(String puid, String uid, String resultId, long rowNumber, long colNumber, long offset, int length) {
        DmFileDO fileDO = this.dmFileMapper.queryFileByUniqueId(resultId);

        URI fileUri = DmConvertUtils.createFileUri(fileDO.getFileUri());
        String fsName = fileUri.getScheme().toLowerCase();
        if (StringUtils.equalsIgnoreCase(fsName, "wsn")) {
            String wsn = fileUri.getHost();
            DmWorkerStatusDO statusDO = this.dmWorkerStatusMapper.queryByWsn(wsn);
            if (statusDO == null || statusDO.getWorkerConnStatus() != WorkerConnStatus.CONNECTED) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
            }

            return this.fileService.fetchResultCol(wsn, fileUri.getPath(), rowNumber, colNumber, offset, length);
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.FILE_FS_UNSUPPORT_ERROR.name(), fsName));
    }

    //
    // Util Method
    // 

    private SessionContextDTO createSessionCtx(List<String> levels) {
        DsLevels parsed = this.dmDsConfigService.parseLevels(levels);
        RdpDataSourceDO dsDO = parsed.dsDO();
        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());

        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());

        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(parsed.levelsParam().get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(parsed.levelsParam().get(UmiTypes.Schema)));

        return sessionSpi.createSessionContext(dsConfig, params);
    }
}
