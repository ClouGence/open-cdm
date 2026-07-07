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
package com.clougence.clouddm.worker.component.resource.file;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.component.cache.LocalCacheComponent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileResourceManager {

    private final File fileDir = new File(GlobalConfUtils.getPluginDir("files"));

    public String cacheFile(DataSourceConfig config, String fileName, byte[] fileBytes) throws IOException {
        String path = LocalCacheComponent.getInstance().cacheFile(this.fileDir, config.getInstanceId(), fileName, fileBytes);
        log.info("cache file location: {}", path);
        return path;
    }
}
