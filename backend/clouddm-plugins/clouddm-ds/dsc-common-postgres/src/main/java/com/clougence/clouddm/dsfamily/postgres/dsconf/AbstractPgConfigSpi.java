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
package com.clougence.clouddm.dsfamily.postgres.dsconf;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.common.dsconf.AbstractDsConfigSpi;

public abstract class AbstractPgConfigSpi extends AbstractDsConfigSpi {

    private static final List<String> CERTIFICATE_TYPES = List.of("pem", "crt", "cer");
    private static final List<String> CLIENT_KEY_TYPES  = List.of("pk8");

    @Override
    public List<String> certificateTextFileTypes(SslMode sslMode, String configName) {
        if (DataSourceConfig.Fields.sslClientKeyData.equals(configName)) {
            return List.of();
        }
        return CERTIFICATE_TYPES;
    }

    @Override
    public List<String> certificateBinaryFileTypes(SslMode sslMode, String configName) {
        if (DataSourceConfig.Fields.sslClientKeyData.equals(configName)) {
            return CLIENT_KEY_TYPES;
        }
        return CERTIFICATE_TYPES;
    }
}
