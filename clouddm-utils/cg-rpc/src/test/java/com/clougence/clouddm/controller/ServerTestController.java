package com.clougence.clouddm.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import com.clougence.test.constants.TestRouteNames;

/**
 * @author wanshao create time is 2021/1/8
 **/
@Controller
public class ServerTestController {

    @MessageMapping(TestRouteNames.SERVER_EXAMPLE_DST)
    public String test(List<Object> paramOList) {

        return "received(server)";
    }
}
