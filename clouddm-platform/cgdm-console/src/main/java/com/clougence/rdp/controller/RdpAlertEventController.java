package com.clougence.rdp.controller;

import java.util.List;
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
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.rdp.controller.model.fo.ListAlertEventsFO;
import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
import com.clougence.rdp.controller.model.vo.AlertEventListVO;
import com.clougence.rdp.controller.model.vo.AlertEventLogVO;
import com.clougence.rdp.dal.model.RdpAlertEventLogDO;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/4/15
 **/
@RestController
@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/alert/event")
@Slf4j
public class RdpAlertEventController {

    @Resource
    private RdpAlertEventLogService rdpAlertEventLogService;

    @Resource
    private RdpUserService          rdpUserService;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResWebData<?> listAlertEvents(@RequestBody @Valid ListAlertEventsFO listFO, HttpServletRequest request) {
        String uid = (String) request.getAttribute(RdpUserService.UID);
        if (StringUtils.isBlank(uid)) {
            throw new RuntimeException("Uid is empty.login first.");
        }

        boolean isMaintainer = rdpUserService.isMaintainer(uid);
        List<RdpAlertEventLogDO> eventLogs;
        if (isMaintainer) {
            eventLogs = rdpAlertEventLogService
                .listAlertEventLogs(listFO.getLeftTimeMillis(), listFO.getRightTimeMillis(), listFO.getStatus(), null, listFO.getStartId(), listFO.getPageSize());
        } else {
            eventLogs = rdpAlertEventLogService
                .listAlertEventLogs(listFO.getLeftTimeMillis(), listFO.getRightTimeMillis(), listFO.getStatus(), uid, listFO.getStartId(), listFO.getPageSize());
        }

        List<AlertEventLogVO> logVOs = eventLogs.stream().map(logDO -> {
            AlertEventLogVO logVO = new AlertEventLogVO();
            logVO.convertFromDO(logDO);
            return logVO;
        }).collect(Collectors.toList());

        AlertEventListVO events = new AlertEventListVO();
        events.setAlertEventLogVOList(logVOs);
        return ResWebDataUtils.buildSuccess(events);
    }
}
