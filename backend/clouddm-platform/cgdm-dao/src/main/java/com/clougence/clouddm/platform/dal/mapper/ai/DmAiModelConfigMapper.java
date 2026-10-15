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
package com.clougence.clouddm.platform.dal.mapper.ai;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;
import com.clougence.clouddm.platform.dal.model.ai.DmAiModelConfigDO;

public interface DmAiModelConfigMapper extends BaseMapper<DmAiModelConfigDO> {
    IPage<DmAiModelConfigDO> findPageByProviderCodes(IPage<DmAiModelConfigDO> page, @Param("keyword") String keyword, @Param("providerCodes") List<String> providerCodes);

    List<DmAiModelConfigDO> findByProviderCodes(@Param("providerCodes") List<String> providerCodes);

    DmAiModelConfigDO findByCode(@Param("code") String code);

    int updateByCodeAndVersion(@Param("modelCode") String modelCode, @Param("label") String label, @Param("upstreamModelName") String upstreamModelName,
                               @Param("updatedBy") String updatedBy, @Param("version") int version, @Param("verificationStatus") AiVerificationStatus verificationStatus,
                               @Param("verifiedVersion") Integer verifiedVersion, @Param("verifiedAt") Date verifiedAt, @Param("expectedVersion") int expectedVersion,
                               @Param("gmtModified") Date gmtModified);

    int updateVerificationByCodeAndVersion(@Param("code") String code, @Param("version") int version, @Param("status") AiVerificationStatus status,
                                           @Param("verifiedAt") Date verifiedAt);

    int deleteByCodeAndVersion(@Param("code") String code, @Param("version") int version);

}
