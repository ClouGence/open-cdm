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
package com.clougence.clouddm.ai.dao.mapper;

import java.util.Date;
import java.util.List;

import com.clougence.clouddm.ai.dao.model.DmAiProviderConfigDO;
import com.clougence.clouddm.ai.model.AiPreset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface DmAiProviderConfigMapper extends BaseMapper<DmAiProviderConfigDO> {
    List<String> findCodes();

    List<DmAiProviderConfigDO> findByCodes(@Param("codes") List<String> codes);

    DmAiProviderConfigDO findByCode(@Param("code") String code);

    int updateByCode(@Param("providerCode") String providerCode, @Param("displayName") String displayName, @Param("adapterType") AiPreset adapterType,
                     @Param("baseUrl") String baseUrl, @Param("accessToken") String accessToken, @Param("replaceApiKey") boolean replaceApiKey,
                     @Param("gmtModified") Date gmtModified);

    int deleteByCode(@Param("code") String code);

}
