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
package com.clougence.clouddm.console.web.controller.resource;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.console.web.component.file.mode.PluginResourceData;
import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.jwtsession.JwtService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.console.web.service.resource.PluginResourceService;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.resource.ResourceRequest;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = DmControllerUrlPrefix.CONSOLE_PREFIX + "/resource")
@Slf4j
public class PluginResourceController {

    @Resource
    private PluginResourceService pluginResourceService;
    @Resource
    private JwtService            jwtService;
    @Resource
    private RdpUserService        rdpUserService;

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/fetch", method = { RequestMethod.GET })
    public ResponseEntity<InputStreamResource> fetch(@RequestParam("resource") String resourceName, @RequestParam(value = "format", required = false) String expectedFormat,
                                                     @RequestParam Map<String, String> params, HttpServletRequest httpRequest) {
        try {
            Map<String, String> requestParams = new LinkedHashMap<>(params);
            requestParams.remove("resource");
            requestParams.remove("format");

            ResourceRequest resourceRequest = buildResourceRequest(expectedFormat, requestParams, httpRequest);
            PluginResourceData resourceData = this.pluginResourceService.getResource(resourceName, resourceRequest);
            if (resourceData == null || resourceData.inputStream() == null) {
                return ResponseEntity.notFound().build();
            }
            if (resourceData.loginRequired() && !resourceRequest.isLoggedIn()) {
                return ResponseEntity.status(401).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(resourceData.contentType()));
            headers.setCacheControl(CacheControl.noCache());
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(resourceData.inputStream()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            log.warn("Failed to load plugin resource '{}'.", resourceName, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResourceRequest buildResourceRequest(String expectedFormat, Map<String, String> params, HttpServletRequest httpRequest) {
        ResourceRequest request = new ResourceRequest();
        LoginContext loginContext = resolveLoginContext(httpRequest);
        request.setUserId(loginContext.userId);
        request.setLoggedIn(loginContext.loggedIn);
        request.setLanguage(resolveLanguage(httpRequest));
        request.setExpectedFormat(expectedFormat);

        Map<String, Object> objectParams = new LinkedHashMap<>();
        if (params != null) {
            params.forEach(objectParams::put);
        }
        request.setParams(objectParams);
        return request;
    }

    private String resolveLanguage(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }
        Locale locale = httpRequest.getLocale();
        return locale == null ? null : locale.toLanguageTag();
    }

    private LoginContext resolveLoginContext(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return new LoginContext(null, false);
        }

        String userId = (String) httpRequest.getAttribute(RdpUserService.UID);
        if (StringUtils.isNotBlank(userId)) {
            return new LoginContext(userId, true);
        }

        DecodedJWT jwt = this.jwtService.verify(httpRequest);
        if (jwt == null || StringUtils.isBlank(jwt.getId())) {
            return new LoginContext(null, false);
        }

        DmAuthUserDO user = this.rdpUserService.getUserByUid(jwt.getId());
        if (user == null || user.isDisable()) {
            return new LoginContext(null, false);
        }
        return new LoginContext(jwt.getId(), true);
    }

    private record LoginContext(String userId, boolean loggedIn) {
    }
}
