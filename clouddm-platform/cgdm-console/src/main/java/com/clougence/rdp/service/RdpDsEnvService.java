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

import com.clougence.clouddm.console.web.model.fo.env.UpdateDsEnvFO;
import com.clougence.clouddm.console.web.model.lo.UpdateDsEnvLO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;

/**
 * @author wanshao create time is 2021/1/18
 **/
public interface RdpDsEnvService {

    List<DmSysEnvDO> listDsEnv(String puid, String uid, String match);

    DmSysEnvDO queryByUserAndId(String puid, String uid, long envID);

    int initPrimaryUserDefaultEnv(String puid, String uid);

    int addEnvDs(String puid, String uid, DmSysEnvDO dsEnvDO);

    int deleteDsEnv(String puid, String uid, Long dsEnvId);

    UpdateDsEnvLO updateDsEnv(String puid, String uid, UpdateDsEnvFO updateDsEnvFO);

    void fillDsEnvInfo(List<DmDsDO> dss);

}
