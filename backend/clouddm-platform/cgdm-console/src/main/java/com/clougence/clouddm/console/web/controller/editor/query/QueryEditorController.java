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
package com.clougence.clouddm.console.web.controller.editor.query;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.editor.query.*;
import com.clougence.clouddm.console.web.model.vo.editor.query.DsStatusConfVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.DsStatusSupportConfVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.OperationSessionVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.SessionVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.model.rdb.BrowseColumnMO;
import com.clougence.clouddm.console.web.service.editor.DsQueryEditorService;
import com.clougence.clouddm.console.web.service.editor.model.DataResultDataVO;
import com.clougence.clouddm.console.web.service.editor.model.DataResultPageVO;
import com.clougence.clouddm.console.web.service.editor.model.DsAvailableDTO;
import com.clougence.clouddm.console.web.service.editor.model.FileSaveAsDTO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.datasource.DataSourceStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;
import com.clougence.utils.io.FileUtils;
import com.clougence.utils.io.FilenameUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/query")
@Slf4j
public class QueryEditorController {

    @Resource
    private ExecutionDal         executionDal;
    @Resource
    private DsQueryEditorService queryService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private ObjectCacheDao       objectCacheDao;
    @Resource
    private DmAuthServiceForBiz  dmAuthServiceForBiz;
    @Resource
    private DmSupportSpiWrapper  dmSupportSpiWrapper;

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/createsession", method = RequestMethod.POST)
    public ResWebData<?> createSession(@Valid @RequestBody CreateSessionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        RdbIsolation initIsolation = RdbIsolation.valueOfCode(fo.getInitIsolation());
        String sessionId = this.queryService.createSession(uid, fo.getLevels(), fo.getInitAutoCommit(), initIsolation);
        return ResWebDataUtils.buildSuccess(new OperationSessionVO(sessionId, false));
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/closesession", method = RequestMethod.POST)
    public ResWebData<?> closeSession(@Valid @RequestBody CloseSessionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.queryService.closeSession(puid, uid, fo.getSessionId());
        return ResWebDataUtils.buildSuccess(fo.getSessionId());
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/listsession", method = RequestMethod.POST)
    public ResWebData<List<SessionVO>> sessionList(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<SessionVO> vos = this.queryService.getSessionList(puid, uid);
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(checkOpPassword = true, value = DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchDsStatusConf", method = RequestMethod.POST)
    public ResWebData<DsStatusConfVO> fetchDsStatusConf(@Valid @RequestBody AvailableDsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsStatusConfVO vo = new DsStatusConfVO();
        DsCacheEntry entry = this.objectCacheDao.queryByDsId(fo.getDsId());
        if (entry == null) {
            vo.setDsStatus(DataSourceStatus.Deleted);
            vo.setDsStatusMessage(DmConvertUtils.convertToDataSourceStatusI18n(DataSourceStatus.Deleted, null));
            return ResWebDataUtils.buildSuccess(vo);
        }

        DsAvailableDTO dto = this.queryService.availableDataSource(puid, uid, fo.getDsId());
        vo.setDsStatus(dto.getDsStatus());
        vo.setDsStatusMessage(dto.getDsStatusMessage());

        // supports
        RdbSupportSpi supportSpi;
        try {
            supportSpi = PluginManager.findRdbSupportSpi(entry.getDsType());
        } catch (UnsupportedOperationException e) {
            supportSpi = null;
        }

        I18nUtils dsI18n = PluginManager.findDsI18nUtil(entry.getDsType());
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(entry.getDsNumId(), entry.getDsType());
        if (supportSpi != null) {
            vo.setCatalog(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_CATALOG, this.dmSupportSpiWrapper.supportChangeCatalog(supportSpi, dsConfig), dsI18n));
            vo.setSchema(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_SCHEMA, this.dmSupportSpiWrapper.supportChangeSchema(supportSpi, dsConfig), dsI18n));
            vo.setIsolation(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_ISOLATION, this.dmSupportSpiWrapper.supportChangeIsolation(supportSpi, dsConfig), dsI18n));
            vo.setAutoCommit(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_AUTO_COMMIT, this.dmSupportSpiWrapper.supportChangeAutoCommit(supportSpi, dsConfig), dsI18n));
            vo.setReadOnly(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_READONLY, this.dmSupportSpiWrapper.supportChangeReadOnly(supportSpi, dsConfig), dsI18n));
            vo.setCancel(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CANCEL_QUERY, this.dmSupportSpiWrapper.supportCancelQuery(supportSpi, dsConfig), dsI18n));
            vo.setExplain(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_EXPLAIN_QUERY, this.dmSupportSpiWrapper.supportExplain(supportSpi, dsConfig), dsI18n));
            vo.setFormat(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_FORMAT_QUERY, this.dmSupportSpiWrapper.supportFormat(supportSpi, dsConfig), dsI18n));
            //vo.setArgSupport(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_ARGS_QUERY, this.dmSupportSpiWrapper.supportArgs(supportSpi, dsConfig), dsI18n));
        } else {
            vo.setCatalog(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_CATALOG, RdbSupportLevel.No, dsI18n));
            vo.setSchema(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_SCHEMA, RdbSupportLevel.No, dsI18n));
            vo.setIsolation(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_ISOLATION, RdbSupportLevel.No, dsI18n));
            vo.setAutoCommit(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_AUTO_COMMIT, RdbSupportLevel.No, dsI18n));
            vo.setReadOnly(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CHANGE_READONLY, RdbSupportLevel.No, dsI18n));
            vo.setCancel(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_CANCEL_QUERY, RdbSupportLevel.No, dsI18n));
            vo.setExplain(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_EXPLAIN_QUERY, RdbSupportLevel.No, dsI18n));
            vo.setFormat(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_FORMAT_QUERY, RdbSupportLevel.No, dsI18n));
            //vo.setArgSupport(convertToSupportedInfoMap(RdbSupportSpi.HINT_FOR_ARGS_QUERY, RdbSupportLevel.No, dsI18n));
        }

        vo.getCatalog().setDefaultValue(StringUtils.isBlank(dsConfig.getDefaultDataBase()) ? "" : dsConfig.getDefaultDataBase());
        vo.getSchema().setDefaultValue(StringUtils.isBlank(dsConfig.getDefaultSchema()) ? "" : dsConfig.getDefaultSchema());
        vo.getIsolation().setDefaultValue(RdbIsolation.valueOfCode(dsConfig.getIsolation()).getName());
        vo.getAutoCommit().setDefaultValue(String.valueOf(dsConfig.getAutoCommit() == null || dsConfig.getAutoCommit()));
        vo.getReadOnly().setDefaultValue(String.valueOf(Boolean.TRUE.equals(dsConfig.getReadOnly())));
        return ResWebDataUtils.buildSuccess(vo);
    }

    private static DsStatusSupportConfVO convertToSupportedInfoMap(String i18nKey, RdbSupportLevel supportLevel, I18nUtils dsI18n) {
        DsStatusSupportConfVO vo = new DsStatusSupportConfVO();
        if (supportLevel == RdbSupportLevel.Hint) {
            vo.setConf(supportLevel);
            vo.setHint(dsI18n == null ? i18nKey : dsI18n.getMessage(i18nKey));
        } else {
            vo.setConf(supportLevel);
            vo.setHint(null);
        }
        return vo;
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/rdbColumns", method = RequestMethod.POST)
    public ResWebData<?> rdbColumns(@Valid @RequestBody BatchColumnsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes leafType = UmiTypes.valueOfCode(fo.getTargetType());
        List<String> leafName = fo.getTargetNames();

        leafName = leafName.stream().filter(tabName -> {
            DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), tabName);
            return this.dmAuthServiceForBiz.checkResPathWithoutError(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);
        }).collect(Collectors.toList());

        if (leafName.isEmpty()) {
            return ResWebDataUtils.buildSuccess(Collections.emptyMap());
        }

        Map<String, List<BrowseColumnMO>> result = this.queryService.rdbBatchColumns(puid, uid, levels, leafType, leafName);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/closeResultWindow", method = RequestMethod.POST)
    public ResWebData<?> closeResultWindow(@Valid @RequestBody CloseResultWindowFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        for (String resultId : fo.getResultIds()) {
            try {
                this.dmAuthServiceForBiz.checkResultFile(puid, uid, resultId);
                this.queryService.removeResultSetCacheFile(puid, uid, resultId);
            } catch (Exception ignored) {
                /* do not do any thing, Keep silent*/
            }
        }

        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/exportResult", method = RequestMethod.POST)
    public ResWebData<?> exportResult(@Valid @RequestBody ExportResultFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (!this.dmAuthServiceForBiz.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_EXPORT)) {
            throw new ErrorMessageException(RdpAuthUtils.missRoleAuthMsg(SecRoleAuthLabel.DM_QUERY_EXPORT));
        }

        this.dmAuthServiceForBiz.checkResultFile(puid, uid, fo.getResultId());
        if (this.queryService.fetchFileSizeByUniqueId(puid, uid, fo.getResultId()) <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        this.executionDal.fileMapper().updateAccessTimeByUniqueId(fo.getResultId(), "prepare for export " + fo.getDstFormatName());

        String optionJson = fo.getOption() == null ? null : JsonUtils.toJson(fo.getOption());
        FileSaveAsDTO taskId = this.queryService.resultSetFileSaveAs(puid, uid, fo.getResultId(), null, fo.getDstFormatName(), true, optionJson);
        return ResWebDataUtils.buildSuccess(taskId);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/downloadResult", method = RequestMethod.POST)
    public void downloadResult(@Valid @RequestBody DownloadResultFO fo, HttpServletResponse response, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (!this.dmAuthServiceForBiz.checkRoleAuthWithoutError(puid, uid, SecRoleAuthLabel.DM_QUERY_EXPORT)) {
            throw new ErrorMessageException(RdpAuthUtils.missRoleAuthMsg(SecRoleAuthLabel.DM_QUERY_EXPORT));
        }

        this.dmAuthServiceForBiz.checkResultFile(puid, uid, fo.getResultId());
        if (this.queryService.fetchFileSizeByUniqueId(puid, uid, fo.getResultId()) <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        DmExecFileDO fileDO = this.queryService.queryUserFileByUniqueId(puid, uid, fo.getResultId());
        URI fileUri = DmConvertUtils.createFileUri(fileDO.getFileUri());
        String fileName = FilenameUtils.getName(fileUri.getPath());
        long fileSize = this.queryService.fetchFileSizeByUri(puid, uid, fileDO.getFileUri());
        String fileSizeStr = FileUtils.readableFileSize(fileSize);
        if (fileSize <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_EXPORT_DOWNLOAD_FILE_NOT_EXIST_ERROR.name(), fileDO.getFileUri()));
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentLengthLong(fileSize);

        this.executionDal.fileMapper().updateAccessTimeByUniqueId(fileDO.getUniqueId(), "download 0% of " + fileSizeStr);
        long t = System.currentTimeMillis();
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            long readOffset = 0;
            while (readOffset < fileSize) {
                byte[] binaryData = this.queryService.fetchFileData(puid, uid, fileDO.getFileUri(), readOffset, 262144); /* 256K */
                if (binaryData == null) {
                    break;
                }

                if (System.currentTimeMillis() < (t + 3000)) {
                    t = System.currentTimeMillis();
                    int percent = (int) ((((double) readOffset) + binaryData.length) / ((double) fileSize)) * 100;
                    this.executionDal.fileMapper().updateAccessTimeByUniqueId(fileDO.getUniqueId(), "download " + percent + "% of " + fileSizeStr);
                }

                readOffset += binaryData.length;
                outputStream.write(binaryData);
                outputStream.flush();
            }

            this.executionDal.fileMapper().updateAccessTimeByUniqueId(fileDO.getUniqueId(), "download 100% of " + fileSizeStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchResultPage", method = RequestMethod.POST)
    public ResWebData<?> fetchResultPage(@Valid @RequestBody FetchResultPageFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmAuthServiceForBiz.checkResultFile(puid, uid, fo.getResultId());
        if (this.queryService.fetchFileSizeByUniqueId(puid, uid, fo.getResultId()) <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        int pageSize = Math.min(fo.getPageSize(), 20000);
        this.executionDal.fileMapper().updateAccessTimeByUniqueId(fo.getResultId(),//
                "fetch rows " + fo.getOffsetRow() + " to " + (fo.getOffsetRow() + fo.getPageSize()));
        DataResultPageVO dto = this.queryService.fetchResultPage(puid, uid, fo.getResultId(), fo.getOffsetRow(), pageSize);
        return ResWebDataUtils.buildSuccess(dto);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchResultData", method = RequestMethod.POST)
    public ResWebData<?> fetchResultData(@Valid @RequestBody FetchResultDataFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.dmAuthServiceForBiz.checkResultFile(puid, uid, fo.getResultId());
        if (this.queryService.fetchFileSizeByUniqueId(puid, uid, fo.getResultId()) <= 0) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        int safeFetchSize = Math.min(fo.getFetchSize(), 512 * 1024);
        this.executionDal.fileMapper().updateAccessTimeByUniqueId(fo.getResultId(), //
                "fetch data at " + fo.getRowNumber() + ":" + fo.getColNumber() + ", " + fo.getOffset() + " to " + fo.getOffset() + safeFetchSize);
        DataResultDataVO dto = this.queryService.fetchResultData(puid, uid, fo.getResultId(), fo.getRowNumber(), fo.getColNumber(), fo.getOffset(), safeFetchSize);
        return ResWebDataUtils.buildSuccess(dto);
    }
}
