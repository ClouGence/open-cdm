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
package com.clougence.clouddm.inner.website;

import java.util.Objects;

import com.clougence.clouddm.sdk.resource.ResourceCategory;
import com.clougence.clouddm.sdk.resource.ResourceObject;
import com.clougence.clouddm.sdk.resource.ResourceRequest;
import com.clougence.clouddm.sdk.resource.ResourceSpi;
import com.clougence.clouddm.sdk.resource.impl.ClasspathResourceObject;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

public class WebsiteResourceSpi implements ResourceSpi {

    private final ResourceLoader resourceLoader;

    public WebsiteResourceSpi(ClassLoader pluginClassLoader){
        this.resourceLoader = new ClassPathResourceLoader(Objects.requireNonNull(pluginClassLoader, "pluginClassLoader"), "");
    }

    private ResourceObject classpathResource(String resourceLocation, String contentType) {
        if (!resourceLoader.exist(resourceLocation)) {
            return null;
        }
        return new ClasspathResourceObject(resourceLoader, resourceLocation, contentType);
    }

    @Override
    public ResourceObject findResource(String category, String resourcePath, ResourceRequest request) {
        if (!ResourceCategory.WEBSIDE.getCode().equals(category)) {
            return null;
        }

        return switch (resourcePath) {
            case "logo_login" -> classpathResource("webside/logo_login.svg", "image/svg+xml");
            case "logo_header" -> classpathResource("webside/logo_header.svg", "image/svg+xml");
            case "favicon" -> classpathResource("webside/favicon.ico", "image/x-icon");
            default -> null;
        };
    }
}
