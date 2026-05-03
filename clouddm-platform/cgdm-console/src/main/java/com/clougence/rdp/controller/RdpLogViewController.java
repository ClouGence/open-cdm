//package com.clougence.rdp.controller;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
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
