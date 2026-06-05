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
package com.clougence.clouddm.faker;

import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.faker.config.i18n.FakerI18nKeys;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerDefService;
import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.sdk.service.config.ConfigService;
import com.clougence.clouddm.sdk.service.execute.SessionService;

/**
 * @author olddream
 */
@Plugin()
public class FakerPlugin implements DsPlugin {

    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        SessionService sessionService = dsPlugin.findGlobalService(SessionService.class);
        ConfigService configService = dsPlugin.findGlobalService(ConfigService.class);

        dsPlugin.bindGlobalToolService(FakerPluginConfig.TOOL_NAME, (toolsConfig) -> {
            return new FakerTools(sessionService, configService, (FakerPluginConfig) toolsConfig);
        });
        dsPlugin.addGlobalService(new FakerDefService());

        dsPlugin.bindGlobalI18n(FakerI18nKeys.class);
        dsPlugin.bindGlobalI18n(FakerSeedI18nKeys.class);
    }
}
