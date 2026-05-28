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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.util.RdpClassUtil;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pudding
 * @date 2020-01-17 15:29
 */

@Slf4j
@Service
@UnifiedPostConstructOrder(1)
public class RequestAuthServiceImpl implements RequestAuthService, UnifiedPostConstruct {

    private static final String                  SCAN_PATH    = "com/clougence/";

    @Resource
    private ApplicationContext                   applicationContext;
    private final Map<String, RequestAuth>       dataMap      = new HashMap<>();
    private final Map<String, List<RequestAuth>> roleTypeMap  = new HashMap<>();
    private final Map<String, String>            i18nMappings = new HashMap<>();

    @Override
    public void init() throws ClassNotFoundException {
        ClassLoader classLoader = Objects.requireNonNull(this.applicationContext.getClassLoader());
        CgResourceScanner scanner = new CgResourceScanner(new ClassPathResourceLoader(classLoader, SCAN_PATH));
        Set<Class<?>> scanningClassSet1 = RdpClassUtil.getClassSet(classLoader, scanner, new String[] { "com.clougence.rdp.controller" }, RestController.class);
        Set<Class<?>> scanningClassSet2 = RdpClassUtil.getClassSet(classLoader, scanner, new String[] { "com.clougence.rdp.controller" }, Controller.class);
        Set<Class<?>> scanSet = new HashSet<>();
        scanSet.addAll(scanningClassSet1);
        scanSet.addAll(scanningClassSet2);

        for (Class<?> controller : scanSet) {
            if (controller == RestController.class) {
                continue;
            }

            RestController restController = controller.getAnnotation(RestController.class);
            RequestMapping restControllerMapping = controller.getAnnotation(RequestMapping.class);
            if (restController == null) {
                continue;
            }

            String[] basePath = (restControllerMapping == null) ? new String[] { "" } : restControllerMapping.value();
            RequestAuth requestAuth = controller.getAnnotation(RequestAuth.class);
            for (Method method : controller.getMethods()) {
                if (method.isAnnotationPresent(RequestAuth.class)) {
                    requestAuth = method.getAnnotation(RequestAuth.class);
                }
                if (requestAuth == null) {
                    continue;
                }
                String[] methodPath = fetchController(method);
                if (methodPath == null || methodPath.length == 0) {
                    continue;
                }
                String methodName = (method.getDeclaringClass().getName() + "." + method.getName()).toLowerCase();
                setupInfo(basePath, methodPath, requestAuth, methodName);
            }
        }
        //
        log.info("[RDP] Start to check controller i18n.");
        Map<String, String> i18nError = new HashMap<>();
        i18nMappings.forEach((pathKey, mappingTo) -> {
            // String i18N = getI18N(mappingTo, "zh_CN");
            String i18N = DmI18nUtils.getMessage(mappingTo);
            if (StringUtils.isBlank(i18N) || mappingTo.equals(i18N)) {
                log.error("[RDP] Miss i18n message,key:" + mappingTo + ",path:" + pathKey);
            }
        });

        log.info("[RDP] Check controller i18n done.");
    }

    @Override
    public void stop() {

    }

    private String[] fetchController(Method testMethod) {
        Annotation[] annotations = testMethod.getAnnotations();
        for (Annotation anno : annotations) {
            if (anno instanceof RequestMapping) {
                return ((RequestMapping) anno).value();
            } else if (anno instanceof PutMapping) {
                return ((PutMapping) anno).value();
            } else if (anno instanceof PostMapping) {
                return ((PostMapping) anno).value();
            } else if (anno instanceof GetMapping) {
                return ((GetMapping) anno).value();
            } else if (anno instanceof PatchMapping) {
                return ((PatchMapping) anno).value();
            } else if (anno instanceof DeleteMapping) {
                return ((DeleteMapping) anno).value();
            }
        }
        return null;
    }

    private void setupInfo(String[] basePathSet, String[] methodPath, RequestAuth requestAuth, String i18nKey) {
        if (requestAuth.value().length == 0 && requestAuth.strategy() == RequestAuth.AuthStrategy.RefRoleSet) {
            log.error("bad auth basePathSet=" + JsonUtils.toJson(basePathSet) + ", methodPath=" + JsonUtils.toJson(methodPath));
        }
        //
        for (String basePath : basePathSet) {
            String tmpBasePath = basePath;
            if (basePath.length() > 1) {
                tmpBasePath = tmpBasePath.charAt(tmpBasePath.length() - 1) == '/' ? //
                    tmpBasePath.substring(0, tmpBasePath.length() - 1) : //
                    tmpBasePath;
            }
            for (String method : methodPath) {
                String tmpMethod = method;
                tmpMethod = tmpMethod.charAt(0) == '/' ? //
                    tmpMethod.substring(1) : //
                    tmpMethod;
                String actionPath = tmpBasePath + "/" + tmpMethod;
                if (this.dataMap.containsKey(actionPath)) {
                    throw new RuntimeException("conflict RequestMapping ->" + actionPath);
                }
                //
                actionPath = actionPath.replace("//", "/");
                this.dataMap.put(actionPath, requestAuth);
                this.i18nMappings.put(actionPath, i18nKey);
                for (String labelKey : requestAuth.value()) {
                    List<RequestAuth> authList = this.roleTypeMap.computeIfAbsent(labelKey, s -> new ArrayList<>());
                    authList.add(requestAuth);
                }
                //log.info("@@@@@ " + actionPath + "\t" + StringUtils.join(requestAuth.value(), ","));
            }
        }
    }

    @Override
    public void foreach(BiConsumer<String, RequestAuth> biConsumer) {
        this.dataMap.forEach(biConsumer);
    }

    @Override
    public String getI18N(String resourceValue) {
        resourceValue = this.i18nMappings.getOrDefault(resourceValue, resourceValue);
        return DmI18nUtils.getMessage(resourceValue);//need to change
    }

    @Override
    public RequestAuth loadAuthByApi(String resourceValue) {
        return this.dataMap.get(resourceValue);// ControllerUrlPrefix.CONSOLE_PREFIX
    }
}
