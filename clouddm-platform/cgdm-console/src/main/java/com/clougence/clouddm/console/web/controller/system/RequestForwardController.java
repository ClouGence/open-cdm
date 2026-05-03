package com.clougence.clouddm.console.web.controller.system;

import javax.annotation.Resource;

import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.comm.model.RSocketRespDTO;
import com.clougence.clouddm.comm.model.RequestForwardDTO;
import com.clougence.clouddm.comm.util.RSocketRespUtil;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.constant.auth.RequestAuth.AuthStrategy;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/15
 **/
@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/rsocket")
@Slf4j
public class RequestForwardController {

    private final String        FORWARD_LOG_NAME = "forward";

    @Resource
    private RSocketServerSender rSocketServerSender;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/forward", method = RequestMethod.POST)
    public RSocketRespDTO<?> forwardRequest(@RequestBody RequestForwardDTO forwardDTO) {
        try {
            MDC.put("module", FORWARD_LOG_NAME);
            if (forwardDTO == null || StringUtils.isBlank(forwardDTO.getApiMethodName()) || StringUtils.isBlank(forwardDTO.getTargetWsn())) {
                log.error("Receive illegal forward DTO. Its values is " + new ObjectMapper().writeValueAsString(forwardDTO));
                return RSocketRespUtil.buildError("forward dto's info can not empty.");
            }

            log.info("Forwarded info is " + new ObjectMapper().writeValueAsString(forwardDTO));

            return rSocketServerSender.requestNonBlockWithJsonParams(forwardDTO.getApiMethodName(), forwardDTO.getTargetWsn(), forwardDTO.getJsonParams());
        } catch (Exception e) {
            String msg = "forward request to sidecar " + forwardDTO.getTargetWsn() + " failed.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg);
        } finally {
            MDC.remove(FORWARD_LOG_NAME);
        }
    }
}
