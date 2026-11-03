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
package com.clougence.clouddm.base.service.plugin;

import java.io.IOException;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Slf4j
public class TestRSocketClientAuthManager implements RSocketClientAuthManager {

    @Override
    public ConnAuthDTO acquireClientAuthInfo() throws IOException {
        ConnAuthDTO authDto = GlobalConfUtils.loadGlobalConf();
        if (StringUtils.isBlank(authDto.getWsn()) || StringUtils.isBlank(authDto.getAk()) || StringUtils.isBlank(authDto.getSk())) {
            throw new IllegalArgumentException("properties in global config (" + authDto + ") are empty.");
        }

        return authDto;
    }

    @Override
    public String acquireServerDomain() throws IOException {
        ConnAuthDTO dto = GlobalConfUtils.loadGlobalConf();

        //        if (StringUtils.isBlank(dto.getConsoleDomain())) {
        //            throw new IllegalArgumentException("properties console domain in global config (" + dto.getGlobalConfResource() + ") are empty.");
        //        }
        //
        return dto.getConsoleHost();
    }

}
