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

import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.utils.StringUtils;
import lombok.Getter;

@Getter
public class ResourceRoute {

    public static final String DEFAULT_MODULE = "default";
    private final String       category;
    private final String       module;
    private final String       resourcePath;

    private ResourceRoute(String category, String module, String resourcePath){
        this.category = category;
        this.module = module;
        this.resourcePath = resourcePath;
    }

    public String getQualifiedResourceName() { return category + "/" + (DEFAULT_MODULE.equals(module) ? "" : module + "@") + resourcePath; }

    public static ResourceRoute parse(String resourceName) {
        if (StringUtils.isBlank(resourceName)) {
            throw new IllegalArgumentException(i18n(I18nDmMsgKeys.PLUGIN_RESOURCE_NAME_EMPTY_ERROR));
        }

        int slashIndex = resourceName.indexOf('/');
        if (slashIndex <= 0 || slashIndex == resourceName.length() - 1) {
            throw new IllegalArgumentException(i18n(I18nDmMsgKeys.PLUGIN_RESOURCE_NAME_FORMAT_ERROR));
        }

        String category = resourceName.substring(0, slashIndex);
        String rest = resourceName.substring(slashIndex + 1);
        String module = DEFAULT_MODULE;
        String resourcePath = rest;

        int moduleIndex = rest.indexOf('@');
        if (moduleIndex >= 0) {
            if (moduleIndex == 0 || moduleIndex == rest.length() - 1) {
                throw new IllegalArgumentException(i18n(I18nDmMsgKeys.PLUGIN_RESOURCE_NAME_FORMAT_ERROR));
            }
            module = rest.substring(0, moduleIndex);
            resourcePath = rest.substring(moduleIndex + 1);
        }

        if (StringUtils.isBlank(category) || StringUtils.isBlank(module) || StringUtils.isBlank(resourcePath)) {
            throw new IllegalArgumentException(i18n(I18nDmMsgKeys.PLUGIN_RESOURCE_ROUTE_BLANK_ERROR));
        }
        if (resourcePath.startsWith("/") || resourcePath.contains("..")) {
            throw new IllegalArgumentException(i18n(I18nDmMsgKeys.PLUGIN_RESOURCE_PATH_INVALID_ERROR, resourcePath));
        }

        return new ResourceRoute(category, module, resourcePath);
    }

    private static String i18n(I18nDmMsgKeys key, Object... args) {
        return DmI18nUtils.getMessage(key.name(), args);
    }
}
