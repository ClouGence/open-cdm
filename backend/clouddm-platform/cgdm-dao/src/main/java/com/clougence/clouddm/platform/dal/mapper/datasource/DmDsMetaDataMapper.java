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
package com.clougence.clouddm.platform.dal.mapper.datasource;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsMetaDataDO;

public interface DmDsMetaDataMapper extends BaseMapper<DmDsMetaDataDO> {

    DmDsMetaDataDO queryCache(@Param("puid") String puid, @Param("dsId") Long dsId, @Param("path") String path, @Param("type") MetaInformationType type);

    void insertOrUpdate(@Param("puid") String puid, @Param("dsId") Long dsId, @Param("path") String path, @Param("type") MetaInformationType type,
                        @Param("context") String context);

    void deleteByPath(Long dsId, String path, MetaInformationType type, Date time);

    void deleteByPathLike(Long dsId, String path, Date time);

    int deleteBeforeDate(Date date);
}
