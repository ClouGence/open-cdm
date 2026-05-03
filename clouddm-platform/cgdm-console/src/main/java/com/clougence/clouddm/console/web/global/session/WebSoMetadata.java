package com.clougence.clouddm.console.web.global.session;

import java.util.*;
import java.util.stream.Collectors;

import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

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
