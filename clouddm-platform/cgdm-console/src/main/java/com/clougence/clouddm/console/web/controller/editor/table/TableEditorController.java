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
package com.clougence.clouddm.console.web.controller.editor.table;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.editor.table.*;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.editor.DsTableEditorService;
import com.clougence.clouddm.console.web.service.editor.model.ResultSetDTO;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/editor/table")
@Slf4j
public class TableEditorController {

    @Resource
    private DsTableEditorService tableEditorService;
    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private ObjectCacheDao       objectCacheDao;
    @Resource
    private DmAuthServiceForBiz  dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/editorDef", method = RequestMethod.POST)
    public ResWebData<?> editorDef(@Valid @RequestBody EditorDefFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        TableEditorForm form = this.tableEditorService.loadTableEditorDef(puid, uid, levels, fo);
        return ResWebDataUtils.buildSuccess(form);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/initEditor", method = RequestMethod.POST)
    public ResWebData<?> initEditor(@Valid @RequestBody EditorInitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTable());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        TableEditorUiData uiData = this.tableEditorService.loadTableEditorData(puid, uid, levels, fo);
        return ResWebDataUtils.buildSuccess(uiData);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/generateScript", method = RequestMethod.POST)
    public ResWebData<?> generateScript(@Valid @RequestBody EditorGenFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        Long dsID = levels.dsDO().getId();
        String dataAuthLabel = SecDataAuthLabel.DM_DAUTH_DDL;
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTable());
        boolean checkAuth = this.dmAuthServiceForBiz.checkResPathWithoutError(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, dataAuthLabel);

        List<ResultSetDTO> dtoList = this.tableEditorService.tableEditorGenerate(puid, uid, levels, fo);
        if (checkAuth) {
            return ResWebDataUtils.buildSuccess(dtoList);
        } else {
            ResWebData<Object> data = RdpAuthUtils.missDataPermission(dsID, dsResource.getResPath(), dataAuthLabel);
            data.setData(dtoList);
            return data;
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/scriptExecute", method = RequestMethod.POST)
    public ResWebData<?> scriptExecute(@Valid @RequestBody EditorExecFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTable());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_DDL);

        List<ResultSetDTO> dtoList = this.tableEditorService.tableEditorSave(puid, uid, levels, fo, request.getRemoteAddr());
        return ResWebDataUtils.buildSuccess(dtoList);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchReferencedColumns", method = RequestMethod.POST)
    public ResWebData<?> fetchReferencedColumns(@Valid @RequestBody EditorReferencedFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTable());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_DDL);

        List<String> dtoList = this.tableEditorService.fetchReferencedColumns(puid, uid, levels, fo);
        return ResWebDataUtils.buildSuccess(dtoList);
    }
}
