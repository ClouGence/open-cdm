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
package com.clougence.clouddm.console.web.model.vo.ai;

import java.util.Date;

import com.clougence.clouddm.platform.dal.model.ai.AiPreset;
import com.clougence.clouddm.platform.dal.model.ai.AiVerificationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiEngineConfigVO {

    private String               providerCode;
    private String               id;
    private String               name;
    private AiPreset             preset;
    private String               model;
    private String               baseUrl;
    private String               accessToken;
    private int                  version;
    private AiVerificationStatus verificationStatus;
    private Integer              verifiedVersion;
    private Date                 verifiedAt;
    private String               createdBy;
    private String               updatedBy;
    private Date                 gmtCreate;
    private Date                 gmtModified;
}
