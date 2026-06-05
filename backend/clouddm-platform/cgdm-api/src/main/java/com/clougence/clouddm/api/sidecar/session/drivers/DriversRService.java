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
package com.clougence.clouddm.api.sidecar.session.drivers;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;

/**
 * @author mode 2021/1/14 14:10
 */
@RSocketApiClass
public interface DriversRService {

    DsDriverVer refreshDriverVersion(RSocketSendDTO sendDTO, String familyName, String version);

    void deleteDriverResource(RSocketSendDTO sendDTO, String familyName, String version);

    void transferDriverResource(RSocketSendDTO sendDTO, String familyName, String version, String fileName, long offset, byte[] content);
}
