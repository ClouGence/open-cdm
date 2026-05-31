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
package com.clougence.clouddm.sdk.resource.impl;

import java.io.IOException;
import java.io.InputStream;

import com.clougence.clouddm.sdk.resource.ResourceObject;
import com.clougence.utils.loader.ResourceLoader;

public class ClasspathResourceObject implements ResourceObject {

    private final ResourceLoader resourceLoader;
    private final String         resourceLocation;
    private final String         contentType;
    private final boolean        cache;
    private final boolean        loginRequired;

    public ClasspathResourceObject(ResourceLoader resourceLoader, String resourceLocation, String contentType){
        this(resourceLoader, resourceLocation, contentType, false, false);
    }

    public ClasspathResourceObject(ResourceLoader resourceLoader, String resourceLocation, String contentType, boolean cache, boolean loginRequired){
        this.resourceLoader = resourceLoader;
        this.resourceLocation = resourceLocation;
        this.contentType = contentType;
        this.cache = cache;
        this.loginRequired = loginRequired;
    }

    @Override
    public boolean isText() { return false; }

    @Override
    public String getText() { return null; }

    @Override
    public InputStream getStream() {
        try {
            return resourceLoader.getResourceAsStream(resourceLocation);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String contentType() {
        return contentType;
    }

    @Override
    public boolean isCache() { return cache; }

    @Override
    public boolean isLoginRequired() { return loginRequired; }
}
