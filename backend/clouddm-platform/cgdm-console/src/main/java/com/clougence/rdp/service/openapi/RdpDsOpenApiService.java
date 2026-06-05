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
package com.clougence.rdp.service.openapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.service.openapi.model.*;

public interface RdpDsOpenApiService {

    List<ApiDataSourceVO> listDs(String requestId, String puid, ApiListDsFO fo);

    ApiDataSourceVO queryDs(String puid, ApiQueryDsFO fo);

    ResWebData<Long> addDs(String data, MultipartFile securityFile, MultipartFile secretFile, String uid, String puid);

    void deleteDs(String puid, ApiDeleteDsFO fo);

    void updateDsDesc(String puid, ApiUpdateDsDescFO fo);

    void updateAccountAndPasswd(String data, MultipartFile securityFile, MultipartFile secretFile, String puid);

    void updatePrivateHost(String puid, ApiUpdatePriHostFO fo);

    void updatePublicHost(String puid, ApiUpdatePubHostFO fo);

    void cleanDsAccount(String puid, ApiDeleteAccountFO fo);

    List<ApiDsKvConfigVo> listDsKvConfs(String puid, ApiListDsKvConfigsByDsIdFO fo);

    void upsertDsKvConfs(String puid, ApiUpsertDsKvConfigFO fo);
}
