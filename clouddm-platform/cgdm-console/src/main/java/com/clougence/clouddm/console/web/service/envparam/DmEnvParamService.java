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
package com.clougence.clouddm.console.web.service.envparam;

import java.util.List;

import com.clougence.clouddm.console.web.model.fo.envparam.DmBindEnvParamFO;
import com.clougence.clouddm.console.web.model.fo.envparam.DmUnbindEnvParamFO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamOpenVO;
import com.clougence.clouddm.console.web.model.vo.envparam.DmEnvParamTicketDesVO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;

/**
 * @Author: Ekko
 * @Date: 2024-05-31 10:04
 */
public interface DmEnvParamService {

    void bindEnvParam(String puid, String uid, DmBindEnvParamFO fo);

    void unbindEnvParam(String puid, String uid, DmUnbindEnvParamFO fo);

    List<DmEnvParamOpenVO> listEnvParamOpen(String puid, String uid);

    List<DmSysEnvDO> queryListByParamKeyValue(String puid, String paramKey, String paramValue);

    String queryParam(String puid, long envID, String paramKey);

    List<DmSysEnvDO> queryListByParamKey(String puid, String paramKey);

    //
    DmEnvParamTicketDesVO queryAuthTicketInfoParam(String ownerUid, long envId);

    DmEnvParamTicketDesVO querySqlTicketInfoParam(String ownerUid, long envId);

    DmEnvParamTicketDesVO queryChangeTicketInfoParam(String ownerUid, long envId);
}
