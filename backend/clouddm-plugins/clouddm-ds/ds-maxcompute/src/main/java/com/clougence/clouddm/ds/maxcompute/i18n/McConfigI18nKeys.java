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
package com.clougence.clouddm.ds.maxcompute.i18n;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/mc-config")
public interface McConfigI18nKeys extends ConfigI18nKey {

    String CONFIG_MC_PROJECT_LABEL                = "CONFIG_MC_PROJECT_LABEL";
    String CONFIG_MC_PROJECT_DESCRIPTION          = "CONFIG_MC_PROJECT_DESCRIPTION";
    String CONFIG_ADD_DS_MC_ENDPOINT_LABEL        = "CONFIG_ADD_DS_MC_ENDPOINT_LABEL";
    String CONFIG_ADD_DS_MC_ENDPOINT_DESC         = "CONFIG_ADD_DS_MC_ENDPOINT_DESC";
    String CONFIG_MC_INTERACTIVE_MODE_LABEL       = "CONFIG_MC_INTERACTIVE_MODE_LABEL";
    String CONFIG_MC_INTERACTIVE_MODE_DESCRIPTION = "CONFIG_MC_INTERACTIVE_MODE_DESCRIPTION";
    String CONFIG_MC_SDK_ENDPOINT_DESCRIPTION     = "CONFIG_MC_SDK_ENDPOINT_DESCRIPTION";
    String CONFIG_MC_SCHEMA_STYLE_LABEL           = "CONFIG_MC_SCHEMA_STYLE_LABEL";
    String CONFIG_MC_SCHEMA_STYLE_DESCRIPTION     = "CONFIG_MC_SCHEMA_STYLE_DESCRIPTION";
    String CONFIG_MC_DEFAULT_SCHEMA_DESCRIPTION   = "CONFIG_MC_DEFAULT_SCHEMA_DESCRIPTION";
}
