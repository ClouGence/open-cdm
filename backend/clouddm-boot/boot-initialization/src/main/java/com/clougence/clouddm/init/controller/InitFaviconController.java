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
package com.clougence.clouddm.init.controller;

import java.io.IOException;
import java.util.Locale;

import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.console.web.component.file.mode.PluginResourceData;
import com.clougence.clouddm.console.web.component.file.resource.PluginResourceManager;
import com.clougence.clouddm.console.web.component.file.resource.PluginResourceModel;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.sdk.resource.ResourceRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("init")
@RestController
public class InitFaviconController {

    private static final String FAVICON_RESOURCE = "webside/favicon";
    private static final String FAVICON_FORMAT   = "ico";

    @RequestAuth(strategy = RequestAuth.AuthStrategy.Ignore)
    @RequestMapping(value = "/favicon.ico", method = { RequestMethod.GET })
    public ResponseEntity<byte[]> favicon(HttpServletRequest httpRequest) {
        try {
            PluginResourceModel resourceModel = PluginResourceManager.findResource(FAVICON_RESOURCE);
            if (resourceModel == null) {
                return ResponseEntity.notFound().build();
            }

            PluginResourceData resourceData = resourceModel.load(buildResourceRequest(httpRequest));
            if (resourceData == null || resourceData.inputStream() == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(resourceData.contentType()));
            headers.setCacheControl(CacheControl.noCache());
            try (var inputStream = resourceData.inputStream()) {
                return ResponseEntity.ok().headers(headers).body(inputStream.readAllBytes());
            }
        } catch (IOException e) {
            log.warn("Failed to load favicon resource '{}'.", FAVICON_RESOURCE, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResourceRequest buildResourceRequest(HttpServletRequest httpRequest) {
        ResourceRequest request = new ResourceRequest();
        request.setLoggedIn(false);
        request.setExpectedFormat(FAVICON_FORMAT);

        if (httpRequest != null) {
            Locale locale = httpRequest.getLocale();
            request.setLanguage(locale == null ? null : locale.toLanguageTag());
        }
        return request;
    }
}
