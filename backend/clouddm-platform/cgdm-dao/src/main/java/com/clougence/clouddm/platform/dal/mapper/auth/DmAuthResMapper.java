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

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface DmAuthResMapper extends BaseMapper<DmAuthResDO> {

    List<DmAuthResDO> queryByPathLike(long resId, String ownerUid, AuthKind authKind, List<String> prefixList);

    List<DmAuthResDO> queryByLikePath(long resId, String ownerUid, AuthKind authKind, String path);

    List<DmAuthResDO> queryByPath(long resId, String ownerUid, AuthKind authKind, String path);

    void deleteByPath(long resId, String ownerUid, AuthKind authKind, String path);

    List<DmAuthResDO> listByKind(String ownerUid, AuthKind authKind);

    List<DmAuthResDO> listWithoutLabels(String ownerUid, AuthKind authKind);

    DmAuthResDO queryByUniqueKey(long resId, String ownerUid, String resPath, AuthKind authKind);

    long queryAuthCountByUser(String ownerUid);

    void updateAuthById(long id, String resAuthLabel, Date startTime, Date endTime);

    void deleteByRes(long resId, AuthKind authKind);

    void deleteByUser(String ownerUid);

    void deleteByPrefix(String prefixPath, String kindType);

    void deleteByEndTimeExceed(Date endTime);
}
