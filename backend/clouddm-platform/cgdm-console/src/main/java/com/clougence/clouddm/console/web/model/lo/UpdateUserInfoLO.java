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
package com.clougence.clouddm.console.web.model.lo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chunlin create time is 2024/11/5
 */
@Getter
@Setter
public class UpdateUserInfoLO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String targetUid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newSubAccount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldSubAccount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newUserName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldUserName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newPhone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldPhone;
}
