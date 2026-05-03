package com.clougence.rdp.controller;

import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;
import static com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.rdp.constant.auth.SecurityLevel.HIGH;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.rdp.component.ticket.RdpTicketService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.controller.model.fo.ticket.*;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.ticket.RdpAuthTicketDetailVO;
import com.clougence.rdp.controller.model.vo.ticket.RdpTicketBaseInfoVO;
import com.clougence.rdp.service.RdpAuthTicketService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.RdpI18nUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/ticket")
@Slf4j
public class RdpTicketController {

    @Resource
    private RdpAuthTicketService rdpAuthTicketService;
    @Resource
    private RdpTicketService     rdpTicketService;

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/listbasic", method = RequestMethod.POST)
    public ResWebData<?> listTicketsBasic(@Valid @RequestBody RdpListTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);
        fo.setUid(uid);

        IPage<RdpTicketBasicVO> result = this.rdpTicketService.queryTicketListByPage(puid, fo);
        return ResWebDataUtils.buildSuccess(result);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryTicketBaseInfo", method = RequestMethod.POST)
    public ResWebData<RdpTicketBaseInfoVO> queryTicketBaseInfo(@RequestBody RdpQueryTicketDetailFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpTicketBaseInfoVO vo = rdpTicketService.queryTicketBaseInfo(puid, uid, fo);
        return ResWebDataUtils.buildSuccess(vo);
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_APPROVE)
    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    public ResWebData<?> approvalTicket(@Valid @RequestBody RdpApprovalTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        this.rdpTicketService.approvalTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_REQUEST, RDP_WORKER_ORDER_APPROVE })
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResWebData<?> cancelTicket(@Valid @RequestBody RdpCancelTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        String msg = RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CANCEL_AT_CONSOLE_MESSAGE.name());
        this.rdpTicketService.cancelTicket(puid, fo.getTicketId(), msg);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_REQUEST, RDP_WORKER_ORDER_APPROVE })
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResWebData<?> closeTicket(@Valid @RequestBody RdpCloseTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        String msg = RdpI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CLOSE_AT_CONSOLE_MESSAGE.name());
        this.rdpTicketService.closeTicket(fo.getTicketId(), msg, puid, uid);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(strategy = Ignore)
    @RequestMapping(value = "/createDataSourceAuthTicket", method = RequestMethod.POST)
    public ResWebData<String> createDataSourceAuthTicket(@RequestBody RdpAddAuthTicketFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        rdpAuthTicketService.createAuthTicket(puid, uid, fo);
        return ResWebDataUtils.buildSuccess("ok.");
    }

    @RequestAuth(level = HIGH, value = RDP_WORKER_ORDER_READ)
    @RequestMapping(value = "/queryDataSourceAuthTicketDetail", method = RequestMethod.POST)
    public ResWebData<RdpAuthTicketDetailVO> queryAuthTicketDetail(@RequestBody RdpQueryTicketDetailFO fo, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        String puid = (String) request.getAttribute(RdpUserService.PUID);

        RdpAuthTicketDetailVO vo = rdpAuthTicketService.queryAuthTicketDetail(puid, uid, fo.getTicketId());
        return ResWebDataUtils.buildSuccess(vo);
    }

}
