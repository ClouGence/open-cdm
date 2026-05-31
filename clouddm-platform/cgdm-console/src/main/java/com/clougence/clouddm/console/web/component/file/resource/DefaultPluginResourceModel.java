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
package com.clougence.clouddm.console.web.component.file.resource;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import com.clougence.clouddm.console.web.component.file.mode.PluginResourceData;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.sdk.resource.ResourceObject;
import com.clougence.clouddm.sdk.resource.ResourceRequest;
import com.clougence.clouddm.sdk.resource.ResourceSpi;
import com.clougence.utils.StringUtils;
import com.clougence.utils.loader.ResourceLoader;

public class DefaultPluginResourceModel implements PluginResourceModel {

    private static final Map<String, String> CONTENT_TYPE_MAP = Map.ofEntries(//
            Map.entry(".svg", "image/svg+xml"),         //
            Map.entry(".json", "application/json"),     //
            Map.entry(".js", "application/javascript"), //
            Map.entry(".css", "text/css"),              //
            Map.entry(".png", "image/png"),             //
            Map.entry(".jpg", "image/jpeg"),            //
            Map.entry(".jpeg", "image/jpeg"),           //
            Map.entry(".gif", "image/gif"),             //
            Map.entry(".webp", "image/webp"),           //
            Map.entry(".ico", "image/x-icon"),          //
            Map.entry(".txt", "text/plain")             //
    );

    private final String                     qualifiedResourceName;
    private final String                     category;
    private final String                     module;
    private final String                     resourcePath;
    private final ResourceSpi                resourceSpi;
    private final ResourceLoader             resourceLoader;
    private final Path                       cachePath;

    public DefaultPluginResourceModel(String qualifiedResourceName, String category, String module, String resourcePath, ResourceSpi resourceSpi, ResourceLoader resourceLoader,
                                      Path cachePath){
        this.qualifiedResourceName = qualifiedResourceName;
        this.category = category;
        this.module = module;
        this.resourcePath = resourcePath;
        this.resourceSpi = resourceSpi;
        this.resourceLoader = resourceLoader;
        this.cachePath = cachePath;
    }

    @Override
    public String getQualifiedResourceName() { return qualifiedResourceName; }

    @Override
    public String getCategory() { return category; }

    @Override
    public String getModule() { return module; }

    @Override
    public String getResourcePath() { return resourcePath; }

    @Override
    public InputStream getInputStream(ResourceRequest request) throws IOException {
        PluginResourceData data = load(request);
        return data == null ? null : data.inputStream();
    }

    @Override
    public PluginResourceData load(ResourceRequest request) throws IOException {
        ResourceObject resource = resourceSpi.findResource(category, resourcePath, request);
        if (resource == null) {
            return null;
        }

        String contentType = resolveContentType(resource.contentType());
        if (resource.isCache() && Files.exists(cachePath)) {
            return new PluginResourceData(contentType, Files.newInputStream(cachePath), resource.isLoginRequired());
        }

        if (resource.isCache()) {
            Files.createDirectories(cachePath.getParent());
            try (InputStream inputStream = openResourceStream(resource)) {
                Files.copy(inputStream, cachePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return new PluginResourceData(contentType, Files.newInputStream(cachePath), resource.isLoginRequired());
        }

        return new PluginResourceData(contentType, openResourceStream(resource), resource.isLoginRequired());
    }

    @Override
    public void deleteCache() throws IOException {
        Files.deleteIfExists(cachePath);
    }

    private InputStream openResourceStream(ResourceObject resource) throws IOException {
        if (resource.isText()) {
            String text = resource.getText() == null ? "" : resource.getText();
            return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        }
        InputStream inputStream = resource.getStream();
        if (inputStream == null) {
            inputStream = resourceLoader.getResourceAsStream(resourcePath);
        }
        if (inputStream == null) {
            throw new FileNotFoundException(DmI18nUtils.getMessage(I18nDmMsgKeys.PLUGIN_RESOURCE_FILE_NOT_EXIST_ERROR.name(), qualifiedResourceName));
        }
        return inputStream;
    }

    private String resolveContentType(String contentType) {
        if (!StringUtils.isBlank(contentType)) {
            return contentType;
        }
        for (Map.Entry<String, String> entry : CONTENT_TYPE_MAP.entrySet()) {
            if (resourcePath.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "application/octet-stream";
    }
}
