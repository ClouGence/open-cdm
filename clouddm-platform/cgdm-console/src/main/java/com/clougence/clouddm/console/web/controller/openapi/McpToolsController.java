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
package com.clougence.clouddm.console.web.controller.openapi;

import static com.clougence.clouddm.console.web.global.mcp.McpConstants.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.mcp.McpApiProvider;
import com.clougence.clouddm.console.web.global.mcp.McpI18nProxy;
import com.clougence.clouddm.console.web.global.mcp.model.McpClientMethod;
import com.clougence.clouddm.console.web.global.mcp.model.McpProtocolBase;
import com.clougence.clouddm.console.web.global.mcp.model.McpTool;
import com.clougence.clouddm.console.web.global.mcp.model.request.McpRequest;
import com.clougence.clouddm.console.web.global.mcp.model.request.ToolCallParams;
import com.clougence.clouddm.console.web.global.mcp.model.response.*;
import com.clougence.clouddm.console.web.global.mcp.utils.JacksonHelper;
import com.clougence.clouddm.console.web.global.mcp.utils.Json;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.rdp.component.openapi.OpenApiHttpClient;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/12/9 17:00:13
 */
@RestController
@RequestMapping(DmControllerUrlPrefix.DM_MCP_API_PREFIX)
@Slf4j
public class McpToolsController extends BasicApi implements UnifiedPostConstruct {

    private Map<String, String> toolApi         = Collections.emptyMap();
    private ToolsListResult     toolsListResult = new ToolsListResult(Collections.emptyList(), null);
    private InitializeResult    initResult;
    private Map<String, Object> pongResult;

    @Resource
    private ApplicationContext  context;
    @Resource
    private DmConsoleConfig     consoleConfig;

    @Override
    public void init() {
        log.info("[MCP] Tools loading...");

        Map<String, Object> providers = context.getBeansWithAnnotation(McpApiProvider.class);

        List<ToolsListResult.ToolDesc> tools = new ArrayList<>();
        Map<String, String> api = new HashMap<>();
        for (Object bean : providers.values()) {
            try {
                initTools(bean, tools, api);
            } catch (Exception e) {
                String error = "[MPC] Tools init failed,but ignore it.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(error, e);
                throw new IllegalStateException(error, e);
            }
        }

        this.toolApi = Collections.unmodifiableMap(api);
        this.toolsListResult = new ToolsListResult(Collections.unmodifiableList(tools), null);

        InitializeResult.ServerInfo serverInfo = new InitializeResult.ServerInfo("CloudDM-MCP", "1.0.0");
        InitializeResult.Tools capabilitiesTools = new InitializeResult.Tools(true);
        InitializeResult.Capabilities capabilities = new InitializeResult.Capabilities(capabilitiesTools);
        this.initResult = new InitializeResult("2025-06-18", capabilities, serverInfo);

        Map<String, Object> pong = new HashMap<>();
        pong.put("ok", true);
        this.pongResult = Collections.unmodifiableMap(pong);

        log.info("[MCP] Tools loaded: {}", this.toolApi.keySet());
    }

    @Override
    public void stop() {

    }

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/clouddm", method = { RequestMethod.POST, RequestMethod.GET })
    public McpProtocolBase handle(@RequestBody @Valid McpRequest req, HttpServletRequest request) {
        String puid = (String) request.getAttribute(RdpUserService.PUID);
        if (!this.isEnableMcp(puid)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.AI_MCP_DISABLE_ERROR.name()));
        }

        String requestId = (String) request.getAttribute(MCP_API_REQUEST_ID);
        log.info("[MCP] request id: {}", requestId);

        if (req == null) {
            McpError err = new McpError(McpError.INTERNAL_ERROR, "Empty request");
            return new McpErrorResponse(null, err);
        }

        Object id = req.getId();
        McpClientMethod method = req.getMethod();

        try {
            switch (method) {
                case INITIALIZE: {
                    return new McpResponse(String.valueOf(id), initResult);
                }
                case PING: {
                    return new McpResponse(String.valueOf(id), pongResult);
                }
                case TOOLS_LIST: {
                    return new McpResponse(String.valueOf(id), toolsListResult);
                }
                case TOOLS_CALL: {
                    String host = (String) request.getAttribute(MCP_API_REQUEST_API_HOST);
                    String ak = (String) request.getAttribute(MCP_API_REQUEST_AK);
                    String sk = (String) request.getAttribute(MCP_API_REQUEST_SK);
                    Object params = req.getParams();
                    ToolCallParams callParams = JacksonHelper.convert(params, ToolCallParams.class);
                    return handleToolCall(id, host, ak, sk, callParams);
                }
                default: {
                    McpError err = new McpError(McpError.METHOD_NOT_FOUND, "Unknown method: " + method);
                    return new McpErrorResponse(String.valueOf(id), err);
                }
            }
        } catch (Exception e) {
            log.warn("[MCP] Handle request error,method:{}.msg:{}", method, ExceptionUtils.getRootCauseMessage(e), e);
            McpError err = new McpError(McpError.INTERNAL_ERROR, "Internal error: " + ExceptionUtils.getRootCauseMessage(e));
            return new McpErrorResponse(String.valueOf(id), err);
        }
    }

    private McpResponse handleToolCall(Object id, String host, String ak, String sk, ToolCallParams callParams) throws Exception {
        String api = toolApi.get(callParams.getName());
        if (api == null) {
            throw new ErrorMessageException("Tool " + callParams.getName() + " not found.");
        }

        if (StringUtils.isBlank(host)) {
            host = "127.0.0.1:8222";
        }

        if (StringUtils.isBlank(ak) || StringUtils.isBlank(sk)) {
            throw new ErrorMessageException("AccessKey/SecretKey is missing.");
        }

        OpenApiHttpClient client = new OpenApiHttpClient(host, consoleConfig.getOpenApiTimeout(), ak, sk);
        String paramStr = Json.toJson(callParams.getArguments());
        String result = client.doJsonPost(api, paramStr);

        ToolCallResult toolCallResult = ToolCallResult.fromText(result);
        return new McpResponse(id, toolCallResult);
    }

    private void initTools(Object bean, List<ToolsListResult.ToolDesc> tools, Map<String, String> api) {
        Class<?> clazz = bean.getClass();
        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);

        String classBase = (classMapping != null) ? firstNotEmpty(classMapping.value(), classMapping.path()) : "";

        McpI18nProxy i18nProxy = DmI18nUtils::getMessage;

        for (Method method : clazz.getDeclaredMethods()) {
            McpTool mcpTool = method.getAnnotation(McpTool.class);
            if (mcpTool == null) {
                continue;
            }

            Parameter input = findRequestBodyParam(method);
            if (input != null && isListLike(input)) {
                log.warn("[MCP] Skip method with list/array @RequestBody (not supported by MCP): {}.{} param={}", clazz.getSimpleName(), method.getName(), input.getType()
                    .getName());
                continue;
            }

            String toolName = mcpTool.name();
            if (StringUtils.isBlank(toolName)) {
                toolName = method.getName();
            }

            if (api.containsKey(toolName)) {
                String error = String.format("[MCP] Duplicate tool name detected: '%s'. " //
                                             + "Class: %s. MCP tool names must be unique. " //
                                             + "Please provide a unique 'name' attribute in the @McpTool annotation " //
                                             + "or rename the conflicting method.", toolName, clazz.getName());
                throw new IllegalStateException(error);
            }

            String mcpLabel = mcpTool.value();
            String desc = DmI18nUtils.getMessage(mcpLabel);
            String methodPath = resolveMethodPath(method);
            String fullPath = normalizePath(classBase, methodPath);

            tools.add(new ToolsListResult.ToolDesc(toolName, desc, input, i18nProxy));
            api.put(toolName, fullPath);
        }
    }

    private Parameter findRequestBodyParam(Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                return parameter;
            }
        }
        return null;
    }

    private boolean isListLike(Parameter p) {
        Class<?> rawType = p.getType();
        return rawType.isArray() || Collection.class.isAssignableFrom(rawType);
    }

    private static String resolveMethodPath(Method method) {
        RequestMapping m = method.getAnnotation(RequestMapping.class);
        if (m != null) {
            return firstNotEmpty(m.value(), m.path());
        }

        PostMapping pm = method.getAnnotation(PostMapping.class);
        if (pm != null) {
            return firstNotEmpty(pm.value(), pm.path());
        }

        GetMapping gm = method.getAnnotation(GetMapping.class);
        if (gm != null) {
            return firstNotEmpty(gm.value(), gm.path());
        }

        return "";
    }

    private static String normalizePath(String classBase, String methodPath) {
        String base = StringUtils.defaultString(classBase, "");
        String m = StringUtils.defaultString(methodPath, "");
        if (!base.startsWith("/")) {
            base = "/" + base;
        }

        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }

        if (!m.startsWith("/")) {
            m = "/" + m;
        }

        return base + m;
    }

    public static String firstNotEmpty(String[] value, String[] path) {
        String p = firstOrEmpty(value);
        if (StringUtils.isNotBlank(p)) {
            return p;
        }
        return firstOrEmpty(path);
    }

    public static String firstOrEmpty(String[] arr) {
        return (arr != null && arr.length > 0) ? StringUtils.defaultString(arr[0]) : "";
    }
}
