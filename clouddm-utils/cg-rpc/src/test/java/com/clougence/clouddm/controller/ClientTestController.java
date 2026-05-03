package com.clougence.clouddm.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import com.clougence.test.constants.TestRouteNames;

/**
 * @author wanshao create time is 2021/1/8
 **/
@Controller
public class ClientTestController {

    @MessageMapping(TestRouteNames.CLIENT_EXAMPLE_DST)
    public String test() {
        return "received(client)";
    }
}
