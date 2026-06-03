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
package com.clougence.clouddm.ds.polardb.resource;

import java.util.Objects;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import com.clougence.clouddm.sdk.resource.ResourceCategory;
import com.clougence.clouddm.sdk.resource.ResourceObject;
import com.clougence.clouddm.sdk.resource.ResourceRequest;
import com.clougence.clouddm.sdk.resource.ResourceSpi;
import com.clougence.clouddm.sdk.resource.impl.ClasspathResourceObject;
import com.clougence.utils.loader.ResourceLoader;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

public class PorMyEditorResourceSpi implements ResourceSpi {

    private final ResourceLoader resourceLoader;

    public PorMyEditorResourceSpi(ClassLoader loader){
        this.resourceLoader = new ClassPathResourceLoader(Objects.requireNonNull(loader, "loader"), "");
    }

    @Override
    public String name() {
        return DataSourceType.PolarDbMySQL.name();
    }

    @Override
    public ResourceObject findResource(String category, String resourcePath, ResourceRequest request) {
        if (!ResourceCategory.EDITOR.getCode().equals(category)) {
            return null;
        }

        return switch (resourcePath) {
            case "keywords" -> keywordsResource();
            default -> null;
        };
    }

    private ResourceObject keywordsResource() {
        return new ClasspathResourceObject(resourceLoader, "META-INF/clougence/db-keywords/polar-mysql.keywords", "text/plain;charset=UTF-8", true, true);
    }
}
