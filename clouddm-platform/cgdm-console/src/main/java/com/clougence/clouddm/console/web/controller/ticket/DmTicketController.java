package com.clougence.clouddm.console.web.controller.ticket;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;
import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.browse.BrowseLevelsFO;
import com.clougence.clouddm.console.web.model.fo.ticket.*;
import com.clougence.clouddm.console.web.model.vo.DmBizLogVO;
import com.clougence.clouddm.console.web.model.vo.browse.BrowseLevelsVO;
import com.clougence.clouddm.console.web.model.vo.ticket.*;
import com.clougence.clouddm.console.web.service.browse.BrowseService;
import com.clougence.clouddm.console.web.service.ticket.DmTicketService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.rdp.component.ticket.RdpApprovalService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.fo.ticket.RdpAddTemplateFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpListApproTemplateFO;
import com.clougence.rdp.controller.model.fo.ticket.RdpRemoveTemplateFO;
import com.clougence.rdp.controller.model.vo.RdpApproTemplateVO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2024/5/7 16:43
*/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/ticket")
@Slf4j
public class DmTicketController {

    @Resource
    private DmTicketService         dmTicketService;
    @Resource
    private RdpApprovalService      approvalService;
    @Resource
    private BrowseService           browseService;
    @Resource
    private DmDsConfigService       dmDsConfigService;
    @Resource
    private BizResOwnerCacheService ownerCacheService;

    // ------------------------------------------- move to rdp --------------------------------------------------------
    // -- for Templates
    @RequestAuth(SecRoleAuthLabel.DM_DS_READ)
    @RequestMapping(value = "listTemplates", method = RequestMethod.POST)
    public ResWebData<?> listTemplates(@Valid @RequestBody RdpListApproTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        List<RdpApproTemplateVO> vos = this.approvalService.listTemplates(puid, fo.getApprovalType());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "refreshTemplates", method = RequestMethod.POST)
    public ResWebData<?> refreshTemplates(@Valid @RequestBody RdpListApproTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        List<RdpApproTemplateVO> vos = this.approvalService.refreshTemplates(puid, fo.getApprovalType());
        return ResWebDataUtils.buildSuccess(vos);
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "addTemplate", method = RequestMethod.POST)
    public ResWebData<?> addTemplate(@Valid @RequestBody RdpAddTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (fo.getApprovalType() == null || StringUtils.isBlank(fo.getTemplateUrl())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        this.approvalService.addTemplateByUrl(puid, fo.getApprovalType(), fo.getTemplateUrl());
        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_MANAGE)
    @RequestMapping(value = "removeTemplate", method = RequestMethod.POST)
    public ResWebData<?> removeTemplate(@Valid @RequestBody RdpRemoveTemplateFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (fo.getApprovalType() == null || StringUtils.isBlank(fo.getTemplateId())) {
            throw new ErrorMessageException(RdpI18nUtils.getMessage(I18nRdpMsgKeys.COMM_BAD_ARG_ERROR.name()));
        }

        this.approvalService.removeTemplateById(puid, fo.getApprovalType(), fo.getTemplateId());
        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(SecRoleAuthLabel.DM_DS_READ)
    @RequestMapping(value = "ticketType", method = RequestMethod.POST)
    public ResWebData<?> ticketType(HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<Map<String, Object>> result = this.approvalService.getTicketTypes(puid);
        return ResWebDataUtils.buildSuccess(result);
    }
    // ------------------------------------------- move to rdp --------------------------------------------------------

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
        this.ownerCacheService.ownDataSource(puid, levels.getDsDO().getId());
        List<BrowseLevelsVO> vos = this.browseService.listLevels(puid, uid, levels, fo.isRefreshCache());
        vos = vos.stream().filter((vo -> {
            return !vo.getObjType().equals(UmiTypes.ExternalCatalog.getTypeName());
        })).collect(Collectors.toList());
        return ResWebDataUtils.buildSuccess(vos);

    }

    // -- for used
    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_REQUEST)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResWebData<?> createTicket(@Valid @RequestBody DmAddTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        checkLevels(fo);
        DmTicketResultVO vo = this.dmTicketService.createSqlTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryQueryTicketDetail", method = RequestMethod.POST)
    public ResWebData<?> queryQueryTicketDetail(@Valid @RequestBody DmQueryTicketDetailFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);

        DmQueryTicketVO vo = this.dmTicketService.queryQueryTicketDetail(puid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryAutoExecJobInfo", method = RequestMethod.POST)
    public ResWebData<?> queryAutoExecJobInfo(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmAutoExecJobVO vo = this.dmTicketService.queryExecJobInfo(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/skipAutoExecTask", method = RequestMethod.POST)
    public ResWebData<?> skipAutoExecTask(@Valid @RequestBody DmQueryAutoExecFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmTicketService.skipTask(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/continueAutoExecTask", method = RequestMethod.POST)
    public ResWebData<?> continueAutoExecTask(@Valid @RequestBody DmQueryAutoExecFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmTicketService.canceledSkipTask(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryAutoExecTaskList", method = RequestMethod.POST)
    public ResWebData<?> queryAutoExecTaskList(@Valid @RequestBody DmQueryTaskListFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        DmPageVO<DmAutoExecTaskVO> result = this.dmTicketService.queryExecTaskList(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/endAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> endAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmTicketService.endAutoExecJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/autoExecLog", method = RequestMethod.POST)
    public ResWebData<?> queryExecLog(@Valid @RequestBody DmQueryExecLogFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        List<DmBizLogVO> result = this.dmTicketService.queryExecLog(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_EXECUTE })
    @RequestMapping(value = "/retryAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> retryAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmTicketService.retryJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_EXECUTE })
    @RequestMapping(value = "/stopAutoExecJob", method = RequestMethod.POST)
    public ResWebData<?> stopAutoExecJob(@Valid @RequestBody DmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        this.dmTicketService.stopJob(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_EXECUTE)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResWebData<?> confirmTicket(@Valid @RequestBody DmConfirmTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setConfirmUid(uid);

        this.dmTicketService.confirmTicket(puid, fo.getTicketId(), fo);
        return ResWebDataUtils.buildSuccess();
    }

    private void checkLevels(DmAddTicketFO fo) {
        DsLevels dsLevels = this.dmDsConfigService.parseLevels(fo.getDbLevels());
        DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsLevels.getDsDO().getDataSourceType());
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
