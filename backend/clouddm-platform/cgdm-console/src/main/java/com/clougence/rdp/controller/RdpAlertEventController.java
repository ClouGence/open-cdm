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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;
import com.clougence.clouddm.console.web.model.fo.ListAlertEventsFO;
import com.clougence.clouddm.console.web.model.vo.AlertEventListVO;
import com.clougence.clouddm.console.web.model.vo.AlertEventLogVO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertEventLogDO;
import com.clougence.rdp.constant.RdpControllerUrlPrefix;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

        List<DmMonAlertEventLogDO> eventLogs = rdpAlertEventLogService
            .listAlertEventLogs(listFO.getLeftTimeMillis(), listFO.getRightTimeMillis(), listFO.getStatus(), null, listFO.getStartId(), listFO.getPageSize());

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
