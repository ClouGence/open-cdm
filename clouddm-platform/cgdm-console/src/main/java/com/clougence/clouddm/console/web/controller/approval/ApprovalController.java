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
package com.clougence.clouddm.console.web.controller.approval;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.approval.ApprovalFlowService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.RdpApproTemplateVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.approval.ApprovalControlService;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 16:43
*/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/approval")
@Slf4j
public class ApprovalController {

    @Resource
    private ApprovalControlService approvalControlService;
    @Resource
    private ApprovalFlowService    approvalFlowService;
    @Resource
    private BrowseService          browseService;
    @Resource
    private DmDsConfigService      dmDsConfigService;
    @Resource
    private ObjectCacheDao         objectCacheDao;

    //
    // control
    //

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_REQUEST)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<?> createTicket(@Valid @RequestBody DmAddTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        checkLevels(fo);
        DmTicketResultVO vo = this.approvalControlService.createSqlTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/createDataSourceAuthApproval", method = RequestMethod.POST)
    public ResWebData<String> createDataSourceAuthTicket(@RequestBody RdpAddAuthTicketFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        approvalControlService.createAuthTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_EXECUTE)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResWebData<?> confirmTicket(@Valid @RequestBody DmConfirmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setConfirmUid(uid);

        this.approvalControlService.confirmTicket(puid, fo.getTicketId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_APPROVE)
    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    public ResWebData<?> approvalTicket(@Valid @RequestBody RdpApprovalFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.approvalFlowService.approvalTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_REQUEST, RDP_WORKER_ORDER_APPROVE })
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResWebData<?> cancelTicket(@Valid @RequestBody RdpCancelTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        String msg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CANCEL_AT_CONSOLE_MESSAGE.name());
        this.approvalFlowService.cancelTicket(puid, fo.getTicketId(), msg);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_REQUEST, RDP_WORKER_ORDER_APPROVE })
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResWebData<?> closeTicket(@Valid @RequestBody RdpCloseTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        String msg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CLOSE_AT_CONSOLE_MESSAGE.name());
        this.approvalFlowService.closeTicket(fo.getTicketId(), msg, puid, uid);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_EXECUTE })
    @RequestMapping(value = "/retryAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> retryAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.approvalControlService.retryJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/skipAutoExecTask", method = RequestMethod.POST)
    public ResWebData<?> skipAutoExecTask(@Valid @RequestBody DmQueryAutoExecFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.approvalControlService.skipTask(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/continueAutoExecTask", method = RequestMethod.POST)
    public ResWebData<?> continueAutoExecTask(@Valid @RequestBody DmQueryAutoExecFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.approvalControlService.canceledSkipTask(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_EXECUTE })
    @RequestMapping(value = "/stopAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> stopAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.approvalControlService.stopJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/endAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> endAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.approvalControlService.endAutoExecJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    //
    // query
    //

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/listbasic", method = RequestMethod.POST)
    public ResWebData<?> listTicketsBasic(@Valid @RequestBody RdpListTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);

        IPage<RdpTicketBasicVO> result = this.approvalControlService.queryTicketListByPage(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(RDP_WORKER_ORDER_REQUEST)
    @RequestMapping(value = "/listDsInsLevels", method = RequestMethod.POST)
    public ResWebData<?> listLevels(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds list
        List<BrowseLevelsVO> levels = this.browseService.listDsIncludeAllEnv(puid, uid);
        return ResWebDataUtils.buildSuccess(levels);
    }

    @RequestAuth(RDP_WORKER_ORDER_REQUEST)
    @RequestMapping(value = "/listDbLevels", method = RequestMethod.POST)
    public ResWebData<?> listDbLevels(@Valid @RequestBody BrowseLevelsFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        // ds object list
        DsLevels levels = this.dmDsConfigService.parseLevels(fo.getLevels());
        this.objectCacheDao.ownDataSource(puid, levels.dsDO().getId());
        List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, fo.isRefreshCache());
        vos = vos.stream().filter((vo -> {
            return !vo.getObjType().equals(UmiTypes.ExternalCatalog.getTypeName());
        })).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);

    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryApprovalBaseInfo", method = RequestMethod.POST)
    public ResWebData<RdpTicketBaseInfoVO> queryTicketBaseInfo(@RequestBody RdpQueryTicketDetailFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpTicketBaseInfoVO vo = approvalControlService.queryTicketBaseInfo(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryQueryApprovalDetail", method = RequestMethod.POST)
    public ResWebData<?> queryQueryTicketDetail(@Valid @RequestBody DmQueryTicketDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);

        DmQueryTicketVO vo = this.approvalControlService.queryQueryTicketDetail(puid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryDataSourceAuthApprovalDetail", method = RequestMethod.POST)
    public ResWebData<RdpAuthTicketDetailVO> queryAuthTicketDetail(@RequestBody RdpQueryTicketDetailFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpAuthTicketDetailVO vo = approvalControlService.queryAuthTicketDetail(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryAutoExecJobInfo", method = RequestMethod.POST)
    public ResWebData<?> queryAutoExecJobInfo(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmAutoExecJobVO vo = this.approvalControlService.queryExecJobInfo(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryAutoExecTaskList", method = RequestMethod.POST)
    public ResWebData<?> queryAutoExecTaskList(@Valid @RequestBody DmQueryTaskListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmPageVO<DmAutoExecTaskVO> result = this.approvalControlService.queryExecTaskList(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/autoExecLog", method = RequestMethod.POST)
    public ResWebData<?> queryExecLog(@Valid @RequestBody DmQueryExecLogFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<DmBizLogVO> result = this.approvalControlService.queryExecLog(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_READ)
    @RequestMapping(value = "listTemplates", method = RequestMethod.POST)
    public ResWebData<?> listTemplates(@Valid @RequestBody RdpListApproTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<RdpApproTemplateVO> vos = this.approvalControlService.listTemplates(puid, fo.getApprovalType());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_READ)
    @RequestMapping(value = "approvalType", method = RequestMethod.POST)
    public ResWebData<?> ticketType(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<Map<String, Object>> result = this.approvalControlService.getTicketTypes(puid);
        return ResWebDataUtils.buildSuccess(result);
    }

    //
    // assistant
    //

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "refreshTemplates", method = RequestMethod.POST)
    public ResWebData<?> refreshTemplates(@Valid @RequestBody RdpListApproTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<RdpApproTemplateVO> vos = this.approvalControlService.refreshTemplates(puid, fo.getApprovalType());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "addTemplate", method = RequestMethod.POST)
    public ResWebData<?> addTemplate(@Valid @RequestBody RdpAddTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (fo.getApprovalType() == null || StringUtils.isBlank(fo.getTemplateUrl())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        this.approvalControlService.addTemplateByUrl(puid, fo.getApprovalType(), fo.getTemplateUrl());
        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "removeTemplate", method = RequestMethod.POST)
    public ResWebData<?> removeTemplate(@Valid @RequestBody RdpRemoveTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (fo.getApprovalType() == null || StringUtils.isBlank(fo.getTemplateId())) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        this.approvalControlService.removeTemplateById(puid, fo.getApprovalType(), fo.getTemplateId());
        return ResWebDataUtils.buildSuccess("ok.");
    }

    private void checkLevels(DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsLevels.dsDO().getDataSourceType());
        List<String> group = dsConfig.getCategories().getLevels();
        if (group.contains(UmiTypes.Catalog.getTypeName())) {
            if (fo.getDbLevels().size() < 4) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_CATALOG_OR_SCHEMA_NULL_ERROR.name()));
            }
        } else if (fo.getDbLevels().size() < 3) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SCHEMA_NULL_ERROR.name()));
        }
    }
}
