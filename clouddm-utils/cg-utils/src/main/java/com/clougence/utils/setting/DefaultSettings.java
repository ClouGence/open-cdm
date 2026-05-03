/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.setting;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;

import com.clougence.utils.setting.provider.StreamType;

/**
 * 继承自{@link MergedSettings}父类。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2013-9-9
 */
public class DefaultSettings extends MergedSettings {
    private String resource;

    /** 创建{@link DefaultSettings}类型对象。 */
    public DefaultSettings() throws IOException {
        this.refresh();
    }

    /** 创建{@link DefaultSettings}类型对象。 */
    public DefaultSettings(String mainSettings) throws IOException {
        this();
        if (StringUtils.isNotBlank(mainSettings)) {
            this.resource = mainSettings;
            this.loadSettings();
        }
    }

    /** 创建{@link DefaultSettings}类型对象。 */
    public DefaultSettings(File mainSettings) throws IOException {
        this();
        if (mainSettings != null) {
            this.resource = mainSettings.toURI().toURL().toString();
            this.loadSettings();
        }
    }

    /** 创建{@link DefaultSettings}类型对象。 */
    public DefaultSettings(URI mainSettings) throws IOException {
        this();
        if (mainSettings != null) {
            this.resource = mainSettings.toURL().toString();
            this.loadSettings();
        }
    }

    @Override
    protected void loadSettings() throws IOException {
        super.loadSettings();

        if (StringUtils.isNotBlank(this.resource)) {
            try (InputStream stream = ResourcesUtils.getResourceAsStream(this.resource)) {
                if (stream != null) {
                    String lowerCase = this.resource.toLowerCase();
                    if (lowerCase.endsWith(".xml")) {
                        this.loadStream(stream, StreamType.Xml);
                    } else if (lowerCase.endsWith(".yaml") || lowerCase.endsWith(".yml")) {
                        this.loadStream(stream, StreamType.Yaml);
                    } else {
                        this.loadStream(stream, StreamType.Properties);
                    }
                }
            }
        }

        super.loadSettings();
    }
}
