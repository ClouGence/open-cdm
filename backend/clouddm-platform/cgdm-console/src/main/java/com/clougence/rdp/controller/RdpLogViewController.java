///*
// * Copyright 2026 杭州开云集致科技有限公司
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.clougence.rdp.controller;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.clougence.clouddm.api.common.rpc.ResWebData;
//import com.clougence.clouddm.api.common.rpc.ResWebDataUtils;
//import com.clougence.rdp.constant.auth.RequestAuth;
//import com.clougence.rdp.controller.model.fo.GrepOperationLogFO;
//import com.clougence.rdp.controller.model.http.RdpControllerUrlPrefix;
//import com.clougence.rdp.service.RdpLogViewService;
//
//@RestController
//@RequestMapping(value = RdpControllerUrlPrefix.CONSOLE_PREFIX + "/logview")
//public class RdpLogViewController {
//
//    @Resource
//    private RdpLogViewService logViewService;
//
//    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
//    @RequestMapping(value = "/grep/operationlog", method = RequestMethod.POST)
//    public ResWebData<?> grepOperationLog(@RequestBody @Valid GrepOperationLogFO logFO, HttpServletRequest request) {
//        return ResWebDataUtils.buildSuccess(logViewService.grepOperationLogs(logFO.getOperationId()));
//    }
//}
