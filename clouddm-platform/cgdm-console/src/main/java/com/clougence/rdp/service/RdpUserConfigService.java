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
package com.clougence.rdp.service;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.model.fo.UpsertUserConfigFO;
import com.clougence.clouddm.console.web.model.lo.UpsertUserConfigLO;
import com.clougence.clouddm.console.web.model.vo.RdpUserConfigVO;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;

/**
 * @author bucketli 2022/1/10 20:18:18
 */
public interface RdpUserConfigService {

    UserDefinedConfig fetchPriUserConfig(String uid);

    List<RdpUserConfigVO> getAllConfig(String uid);

    List<RdpUserConfigVO> queryUserConfigVosWithNewEntries(String uid);

    /**
     * @return key is configName, value is config detail.
     */
    Map<String, RdpUserConfigVO> queryWithNewEntriesAndSpecifiedConfs(String uid, List<String> configs);

    List<DmSysUserConfDO> getSpecifiedConfigs(String uid, List<String> configNames);

    DmSysUserConfDO getDefaultClusterName(String uid);

    DmSysUserConfDO getSpecifiedConfig(String uid, String configName);

    List<RdpUserConfigVO> queryOneConfigTypeByUid(String uid, UserConfigTagType type);

    List<UpsertUserConfigLO> upsertConfigValue(String ownerUid, UpsertUserConfigFO config);

    void initUserConfigs(String uid);

    void initSubAccountConfigs(String uid);
}
