package com.clougence.clouddm.init.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;

import jakarta.annotation.Resource;

@Configuration
@EnableWebSocket
@Profile("init")
public class InitInstallLogWebSocketConfig implements WebSocketConfigurer {

    @Resource
    private InitInstallLogWebSocketHandler initInstallLogWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(initInstallLogWebSocketHandler, DmControllerUrlPrefix.CONSOLE_PREFIX + "/init/ws/install-log").setAllowedOrigins("*");
    }
}
