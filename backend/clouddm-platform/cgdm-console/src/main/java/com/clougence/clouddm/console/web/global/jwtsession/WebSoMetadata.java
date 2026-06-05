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
package com.clougence.clouddm.console.web.global.jwtsession;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSoMetadata {

    private final List<String>                  webSocketList      = new ArrayList<>();
    private final Map<String, Class<?>>         webSocketClassMap  = new HashMap<>();
    private final Map<String, WebSocketHandler> webSocketObjectMap = new HashMap<>();

    public void initMetadata(ApplicationContext applicationContext) throws ClassNotFoundException {
        this.webSocketList.clear();
        this.webSocketClassMap.clear();
        this.webSocketObjectMap.clear();

        Collection<WebSocketHandler> handlers = applicationContext.getBeansOfType(WebSocketHandler.class).values();
        log.info("web socket handler bean size:" + handlers.size());

        List<Class<?>> filtedClasses = handlers.stream()
            .map(Object::getClass)
            .filter(aClass -> aClass.getAnnotation(Service.class) != null)
            .filter(aClass -> aClass.getAnnotation(ServerEndpoint.class) != null)
            .collect(Collectors.toList());

        log.info("filted web socket handler size:" + filtedClasses.size());

        for (Class<?> clazz : filtedClasses) {
            ServerEndpoint webSocket = clazz.getAnnotation(ServerEndpoint.class);
            String path = webSocket.value();

            Object contextBean = applicationContext.getBean(clazz);
            log.info("add web socket class,path:" + path + ",class name:" + clazz.getName());
            webSocketList.add(path);
            webSocketClassMap.put(path, clazz);
            webSocketObjectMap.put(path, (WebSocketHandler) contextBean);
        }
    }

    public void registryWebSocket(WebSocketHandlerRegistry registry, WebSoInterceptor dbWebSocketInterceptor) {
        for (String item : this.webSocketList) {
            registry.addHandler(webSocketObjectMap.get(item), item).addInterceptors(dbWebSocketInterceptor).setAllowedOrigins("*");
        }
    }

    public Class<?> getMetaDataByPath(String requestURI) {
        return this.webSocketClassMap.get(requestURI);
    }
}
