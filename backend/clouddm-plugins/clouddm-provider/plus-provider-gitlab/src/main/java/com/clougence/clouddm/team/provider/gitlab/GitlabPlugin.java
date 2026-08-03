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
package com.clougence.clouddm.team.provider.gitlab;

import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.sdk.DsPlugin;
import com.clougence.clouddm.sdk.DsPluginBinder;
import com.clougence.clouddm.sdk.Plugin;
import com.clougence.clouddm.team.provider.gitlab.constants.GitlabI18nKeys;
import com.clougence.clouddm.team.provider.gitlab.devops.GitlabDevopsScmProviderSpi;
import com.clougence.clouddm.team.provider.gitlab.resource.GitlabScmIconResourceSpi;

import okhttp3.OkHttpClient;

@Plugin(name = "i18n::" + GitlabI18nKeys.PLUGIN_NAME_GITLAB)
public class GitlabPlugin implements DsPlugin {
    @Override
    public void loadPlugin(DsPluginBinder dsPlugin) {
        dsPlugin.bindGlobalI18n(GitlabI18nKeys.class);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
        dsPlugin.addGlobalSpi(new GitlabScmIconResourceSpi(dsPlugin.getPluginClassLoader()));
        dsPlugin.addGlobalSpi(new GitlabDevopsScmProviderSpi(client));
    }
}
