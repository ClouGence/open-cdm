package com.clougence.rdp.component.jwtsession;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.clougence.rdp.constant.auth.RequestAuth;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.service.RdpProductClusterService;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RdpJwtInterceptor implements HandlerInterceptor {

    @Resource
    private RdpJwtManager            rdpJwtManager;
    @Resource
    private RdpProductClusterService rdpProductClusterService;

    @Resource
    private RdpConsoleConfig         rdpConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RdpWebUtils.initLocal(rdpConfig, request);
        String s = request.getHeader("X-Product-Code");
        if (StringUtils.isNotBlank(s) && !"null".equals(s)) {
            String addr = this.rdpProductClusterService.queryApiAddrByProductCode(s);
            if (StringUtils.isNotBlank(addr)) {
                if (addr.endsWith("/")) {
                    addr = addr.substring(0, addr.length() - 1);
                }
                responseRedirect(response, addr);
                return false;
            }
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        RdpJwtCheckResult checkResult = rdpJwtManager.preHandle(request, response, () -> {
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

    protected void responseRedirect(HttpServletResponse response, String addr) throws Exception {
        response.setStatus(307);
        PrintWriter writer = null;
        try {
            response.setHeader("Access-Control-Expose-Headers", "X-Redirect-Addr");
            response.setHeader("X-Redirect-Addr", addr);
            writer = response.getWriter();
            writer.write("{}");
            writer.flush();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        RdpWebUtils.cleanLocal();
    }
}
