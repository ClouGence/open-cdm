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
package com.clougence.clouddm.console.web.controller.browse;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.RDP_DS_MANAGE;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.browse.*;
import com.clougence.clouddm.console.web.model.fo.object.ObjectEditorDefFO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseGenSqlVO;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorFieldForm;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.ActionService;
import com.clougence.clouddm.console.web.service.browse.model.ActionTargetMO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPathObj;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode create time is 2021/1/5
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/browse/actions")
@Slf4j
public class ActionController {

    @Resource
    private ActionService       actionService;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmAuthServiceForBiz dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/genAction", method = RequestMethod.POST)
    public ResWebData<?> genAction(@Valid @RequestBody BrowseActionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        ActionTargetMO mo = DmConvertUtils.convertToActionTargetMO(fo);
        if (mo.getActionType() == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_ACTION_UNSUPPORTED_ERROR.name(), fo.getActionType()));
        }

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        String dataAuthLabel = mo.getActionType().getDataAuth();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), mo.getTargetName());
        boolean checkAuth = this.dmAuthServiceForBiz.checkResPathWithoutError(puid, uid, dsID, AuthKind.DataSource, dsResource, dataAuthLabel);

        List<String> result = this.actionService.genAction(levels, mo);
        BrowseGenSqlVO vo = new BrowseGenSqlVO();
        vo.setDanger(mo.getActionType().isDanger());
        vo.setSql(StringUtils.join(result.toArray(), System.lineSeparator() + System.lineSeparator()));
        if (checkAuth) {
            return ResWebDataUtils.buildSuccess(vo);
        } else {
            ResWebData<Object> data = RdpAuthUtils.missDataPermission(dsID, dsResource.getResPath(), dataAuthLabel);
            data.setData(vo);
            return data;
        }
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/doAction", method = RequestMethod.POST)
    public ResWebData<?> doAction(@Valid @RequestBody BrowseActionFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        ActionTargetMO mo = DmConvertUtils.convertToActionTargetMO(fo);
        if (mo.getActionType() == null) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_ACTION_UNSUPPORTED_ERROR.name(), fo.getActionType()));
        }

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        String dataAuthLabel = mo.getActionType().getDataAuth();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), mo.getTargetName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, dsID, AuthKind.DataSource, dsResource, dataAuthLabel);
        List<String> result = this.actionService.doAction(puid, uid, levels, mo, request.getRemoteAddr());
        BrowseGenSqlVO vo = new BrowseGenSqlVO();
        vo.setDanger(mo.getActionType().isDanger());
        vo.setSql(StringUtils.join(result.toArray(), "\n\n"));
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/editorDef", method = RequestMethod.POST)
    public ResWebData<?> editorDef(@Valid @RequestBody ObjectEditorDefFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        List<TableEditorFieldForm> form = this.actionService.loadObjectEditorDef(puid, uid, levels, fo);
        return ResWebDataUtils.buildSuccess(form);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/requestScript", method = RequestMethod.POST)
    public ResWebData<?> requestScript(@Valid @RequestBody BrowseRequestFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTargetName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, dsID, AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        ActionTargetMO mo = DmConvertUtils.convertToActionTargetMO(fo);
        String script = this.actionService.requestObjectScript(uid, levels, mo);

        BrowseGenSqlVO vo = new BrowseGenSqlVO();
        vo.setSql(script);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/generateScript", method = RequestMethod.POST)
    public ResWebData<?> generateScript(@Valid @RequestBody BrowseGenerateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTargetName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, dsID, AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        ActionTargetMO mo = DmConvertUtils.convertToActionTargetMO(fo);
        String script = this.actionService.generateObjectScript(uid, levels, mo);

        BrowseGenSqlVO vo = new BrowseGenSqlVO();
        vo.setSql(script);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/convertDDL", method = RequestMethod.POST)
    public ResWebData<?> convertDDL(@Valid @RequestBody BrowseConvertDDLFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getSourceTableName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, dsID, AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        ActionTargetMO mo = DmConvertUtils.convertToActionTargetMO(fo);
        DataSourceType targetsType = DataSourceType.getTypeByName(fo.getTargetDsType());
        List<String> script = this.actionService.convertDDL(puid, uid, levels, mo, targetsType);

        BrowseGenSqlVO vo = new BrowseGenSqlVO();
        vo.setSql(StringUtils.join(script.toArray(), "\n\n"));
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/instanceRemark", method = RequestMethod.POST)
    public ResWebData<?> instanceRemark(@Valid @RequestBody BrowseRemarkDsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());

        this.actionService.instanceRemarks(puid, uid, levels, fo.getRemark());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/instanceDelete", method = RequestMethod.POST)
    public ResWebData<?> instanceDelete(@Valid @RequestBody BrowseDeleteDsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());

        this.actionService.instanceDelete(puid, uid, levels);
        return ResWebDataUtils.buildSuccess(null);
    }

    @RequestAuth(RDP_DS_MANAGE)
    @RequestMapping(value = "/loadObject", method = RequestMethod.POST)
    public ResWebData<?> loadObjectTemplate(@Valid @RequestBody BrowseDetailFO detailFO, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        UmiTypes leafType = UmiTypes.valueOfCode(detailFO.getTargetType());
        String leafName = detailFO.getTargetName();
        Map<String, Object> objectVO = this.actionService.loadObject(puid, uid, detailFO.getLevels(), leafType, leafName);
        return ResWebDataUtils.buildSuccess(objectVO);
    }

    private DsLevels checkOwnDataSourceAndReturnDsLevels(String puid, List<String> levels) {
        if (CollectionUtils.isEmpty(levels) || levels.size() < 2) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_ACTION_BAD_ARG_ERROR.name()));
        }
        // the object
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(levels);
        this.objectCacheDao.ownDataSource(puid, dsLevels.dsDO().getId());
        return dsLevels;
    }
}
