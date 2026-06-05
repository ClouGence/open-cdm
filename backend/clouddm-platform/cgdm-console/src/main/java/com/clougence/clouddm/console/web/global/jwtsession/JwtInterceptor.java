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

import java.io.PrintWriter;
import java.lang.reflect.Method;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.util.RdpWebUtils;
import com.clougence.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtManager      jwtManager;
    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RdpWebUtils.initLocal(rdpConfig, request);

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        JwtCheckResult checkResult = jwtManager.preHandle(request, response, () -> {
            if (handler instanceof HandlerMethod) {
                Method targetMethod = ((HandlerMethod) handler).getMethod();
                RequestAuth requestAuth = targetMethod.getAnnotation(RequestAuth.class);
                if (requestAuth == null) {
                    requestAuth = targetMethod.getDeclaringClass().getAnnotation(RequestAuth.class);
                }
                return requestAuth;
            }
            return null;
        }, handler);

        if (checkResult.isSuccess()) {
            return true;
        }

        response.setStatus(checkResult.getErrorCode());
        PrintWriter writer = null;
        try {
            ObjectMapper om = new ObjectMapper();
            String content = om.writeValueAsString(checkResult);
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            writer.write(content);
            writer.flush();
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        RdpWebUtils.cleanLocal();
    }
}
