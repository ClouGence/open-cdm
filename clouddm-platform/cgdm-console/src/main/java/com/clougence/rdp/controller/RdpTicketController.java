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
package com.clougence.rdp.controller;

import static com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy.Ignore;
import static com.clougence.clouddm.console.web.global.jwtsession.SecurityLevel.HIGH;
import static com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel.*;

import com.clougence.clouddm.console.web.model.fo.ticket.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.rdp.component.ticket.RdpTicketService;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpAuthTicketDetailVO;
import com.clougence.clouddm.console.web.model.vo.ticket.RdpTicketBaseInfoVO;
import com.clougence.rdp.service.RdpAuthTicketService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

        String msg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CANCEL_AT_CONSOLE_MESSAGE.name());
        this.rdpTicketService.cancelTicket(puid, fo.getTicketId(), msg);
        return ResWebDataUtils.buildSuccess();
    }

    @RequestAuth(level = HIGH, value = { RDP_WORKER_ORDER_REQUEST, RDP_WORKER_ORDER_APPROVE })
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResWebData<?> closeTicket(@Valid @RequestBody RdpCloseTicketFO fo, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        String uid = (String) request.getAttribute(RdpUserService.UID);

        String msg = DmI18nUtils.getMessage(I18nRdpMsgKeys.TICKET_CLOSE_AT_CONSOLE_MESSAGE.name());
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
