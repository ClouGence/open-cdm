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
package com.clougence.clouddm.platform.dal.mapper.auth;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthMFADO;
import com.clougence.clouddm.platform.dal.model.auth.MfaStatus;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface DmAuthMFAMapper extends BaseMapper<DmAuthMFADO> {

    DmAuthMFADO queryByUid(String uid);

    void updateById(@Param("id") Long id, @Param("mfaKey") String mfaKey, @Param("mfaStatus") MfaStatus mfaStatus, @Param("mfaAccountType") String mfaAccountType);

    void updateStatusById(@Param("id") Long id, @Param("mfaStatus") MfaStatus mfaStatus);

    void emptyResetMfaKeyById(@Param("id") Long id);

    void updateResetMfaKeyById(@Param("id") Long id, @Param("resetMfaKey") String resetMfaKey, @Param("resetMfaAccountType") String resetMfaAccountType);

    /**
     * For sub-account.
     */
    void deleteByUid(String uid);
}
