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
package com.clougence.clouddm.console.web.controller.editor.property;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

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
import com.clougence.clouddm.console.web.model.fo.editor.property.PropertyEditorFO;
import com.clougence.clouddm.console.web.model.fo.editor.property.PropertyInitFO;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.editor.DsObjPropertyService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.ui.editor.property.PropertyEditorUiData;
import com.clougence.schema.umi.struts.UmiTypes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/editor/properties")
@Slf4j
public class PropertyController {

    @Resource
    private DmDsConfigService    dmDsConfigService;
    @Resource
    private ObjectCacheDao       objectCacheDao;
    @Resource
    private DsObjPropertyService propertyEditorService;
    @Resource
    private DmAuthServiceForBiz  dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/propertiesDef", method = RequestMethod.POST)
    public ResWebData<?> editorDef(@Valid @RequestBody PropertyEditorFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());

        UmiTypes umiTypes = UmiTypes.valueOfCode(fo.getTypes());

        TableEditorForm form = this.propertyEditorService.loadPropertyDef(puid, uid, levels, umiTypes);
        return ResWebDataUtils.buildSuccess(form);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/getProperties", method = RequestMethod.POST)
    public ResWebData<?> initEditor(@Valid @RequestBody PropertyInitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        DsResPath dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getLeafName());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, levels.dsDO().getId(), AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_QUERY);

        UmiTypes umiTypes = UmiTypes.valueOfCode(fo.getType());
        PropertyEditorUiData uiData = this.propertyEditorService.loadPropertyData(puid, uid, levels, umiTypes, fo.getLeafName());
        return ResWebDataUtils.buildSuccess(uiData);
    }
}
