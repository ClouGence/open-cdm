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
package com.clougence.clouddm.platform.dal.mapper.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface DmSysEnvMapper extends BaseMapper<DmSysEnvDO> {

    List<DmSysEnvDO> queryListByParameterKey(@Param("puid") String puid, @Param("paramKey") String paramKey);

    List<DmSysEnvDO> queryListByUid(@Param("puid") String puid);

    List<DmSysEnvDO> queryListByUidAndId(@Param("puid") String puid, @Param("envIds") List<Long> envIds);

    List<DmSysEnvDO> listByCondition(String ownerUid, String envName);

    int updateDsEnv(Long id, String ownerUid, String envName, String description);

    int deleteDsEnv(Long id, String ownerUid);

    List<DmSysEnvDO> listByIds(List<Long> ids);

    DmSysEnvDO queryByEnvName(String ownerUid, String envName);

    DmSysEnvDO queryByEnvID(String ownerUid, Long envId);
}
