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
package com.clougence.clouddm.console.web.controller.faker;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.DM_QUERY_CONSOLE;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.faker.*;
import com.clougence.clouddm.console.web.model.vo.faker.FakerDefVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerLogVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerPreviewVO;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.faker.FakerService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPathObj;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.ui.faker.FakerUiData;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/faker")
@Slf4j
public class FakerController {

    @Resource
    private FakerService        fakerService;
    @Resource
    private AsyncTaskService    asyncTaskService;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private ObjectCacheDao      objectCacheDao;
    @Resource
    private DmAuthServiceForBiz dmAuthServiceForBiz;

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fakerDef", method = RequestMethod.POST)
    public ResWebData<?> fakerDef(@Valid @RequestBody FakerDefFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        FakerDefVO vo = this.fakerService.loadFakerDef(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/initFaker", method = RequestMethod.POST)
    public ResWebData<?> initFaker(@Valid @RequestBody FakerInitFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        DsLevels levels = this.checkOwnDataSourceAndReturnDsLevels(puid, fo.getLevels());
        Long dsID = levels.dsDO().getId();
        DsResPathObj dsResource = RdpAuthUtils.genResPathByList(levels.dbLevels(), fo.getTable());
        this.dmAuthServiceForBiz.checkResPath(puid, uid, dsID, AuthKind.DataSource, dsResource, SecDataAuthLabel.DM_DAUTH_DML);

        FakerUiData vo = this.fakerService.loadColumnSeed(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    public ResWebData<?> preview(@Valid @RequestBody FakerConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        if (fo.getTableConfigs().isEmpty()) {
            return ResWebDataUtils.buildError(DmI18nUtils.getMessage("faker.config.table.notnull"));
        }
        for (FakerTableFO tableFO : fo.getTableConfigs()) {
            if (CollectionUtils.isEmpty(tableFO.getColumnConfigs())) {
                return ResWebDataUtils.buildError(DmI18nUtils.getMessage("faker.config.column.notnull", tableFO.getName()));
            }
        }

        FakerPreviewVO vo = this.fakerService.dataPreview(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResWebData<?> execute(@Valid @RequestBody FakerConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String toolSessionId = this.fakerService.execute(uid, fo);
        return ResWebDataUtils.buildSuccess(toolSessionId);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/tailLog", method = RequestMethod.POST)
    public ResWebData<?> tailLog(@Valid @RequestBody TailLogFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        FakerLogVO vo = this.fakerService.tailLog(uid, fo.getToolSessionId(), fo.getStartLine());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    public ResWebData<?> pause(@Valid @RequestBody FakerPauseFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.fakerService.pause(uid, fo.getToolSessionId());
        this.asyncTaskService.pauseTask(FakerPluginConfig.TOOL_NAME, fo.getToolSessionId(), "pause By console");
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public ResWebData<?> resume(@Valid @RequestBody FakerPauseFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.fakerService.resume(uid, fo.getToolSessionId());
        this.asyncTaskService.resumeTask(FakerPluginConfig.TOOL_NAME, fo.getToolSessionId(), "resume By console");
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResWebData<?> close(@Valid @RequestBody FakerCloseFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.fakerService.close(uid, fo.getToolSessionId());
        this.asyncTaskService.cancelTask(fo.getToolSessionId(), FakerPluginConfig.TOOL_NAME, "close By console");
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(DM_QUERY_CONSOLE)
    @RequestMapping(value = "/fetchUiConfig", method = RequestMethod.POST)
    public ResWebData<?> fetchUiConfig(@Valid @RequestBody FetchUiConfigFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        FakerConfigFO configFO = this.fakerService.fetchFoConfigByToolsSession(uid, fo.getToolSessionId());
        return ResWebDataUtils.buildSuccess(configFO);
    }

    private DsLevels checkOwnDataSourceAndReturnDsLevels(String puid, List<String> levels) {
        if (CollectionUtils.isEmpty(levels) || levels.size() < 2) {
            throw new IllegalStateException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_ACTION_BAD_ARG_ERROR.name()));
        }
        // the object
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(levels);
        this.objectCacheDao.ownDataSource(puid, dsLevels.dsDO().getId());
        return dsLevels;
    }
}
