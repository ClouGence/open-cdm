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
package com.clougence.rdp.component.dskvconfig.operate;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.rdp.component.dskvconfig.model.FileExtraConfig;
import com.clougence.rdp.component.dskvconfig.model.SshFileExtraConfig;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2025/2/27 12:26:49
 */
@Service
@Slf4j
public class SshFileExtraConfGen extends FileExtraConfGen {

    @Override
    public DsExtraConfig newDsExtraConfig() {
        return new SshFileExtraConfig();
    }

    @Override
    protected void validate(DmDsDO dsDo, FileExtraConfig extraConfig) {
        super.validate(dsDo, extraConfig);

        String dbsJson = extraConfig.getDbsJson();
        if (StringUtils.isBlank(dbsJson)) {
            throw new IllegalArgumentException(dsDo.getDataSourceType() + " dbsJson can not blank");
        }
    }
}
